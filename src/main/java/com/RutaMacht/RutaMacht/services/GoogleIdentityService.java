package com.RutaMacht.RutaMacht.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleIdentityService {
    private final String clientId;

    public GoogleIdentityService(@Value("${app.google.client-id:}") String clientId) {
        this.clientId = clientId;
    }

    public IdentidadGoogle autenticar(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inicia sesión con Google para continuar.");
        }
        if (clientId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Falta configurar GOOGLE_CLIENT_ID en APIRutaMacht.");
        }

        String credential = authorization.substring(7).trim();
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(List.of(clientId))
                    .build();
            GoogleIdToken token = verifier.verify(credential);
            if (token == null || token.getPayload().getSubject() == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La sesión de Google venció o no es válida. Vuelve a iniciar sesión.");
            }
            GoogleIdToken.Payload claims = token.getPayload();
            if (!Boolean.TRUE.equals(claims.get("email_verified"))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La cuenta de Google no tiene el correo verificado.");
            }
            return new IdentidadGoogle(claims.getSubject(), claims.getEmail(), (String) claims.get("name"));
        } catch (GeneralSecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo verificar la sesión con Google.", e);
        }
    }

    public record IdentidadGoogle(String id, String correo, String nombre) {
    }
}
