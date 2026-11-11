package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.dto.CrearReservaRequest;
import com.RutaMacht.RutaMacht.dto.ReservaResponse;
import com.RutaMacht.RutaMacht.model.EstadoReserva;
import com.RutaMacht.RutaMacht.model.EstadoViaje;
import com.RutaMacht.RutaMacht.model.Reserva;
import com.RutaMacht.RutaMacht.model.Viaje;
import com.RutaMacht.RutaMacht.repository.ReservaRepository;
import com.RutaMacht.RutaMacht.repository.ViajeRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServicio {
    private final ReservaRepository reservaRepository;
    private final ViajeRepository viajeRepository;
    private final MongoTemplate mongoTemplate;

    public ReservaResponse reservar(String viajeId, GoogleIdentityService.IdentidadGoogle pasajero,
                                   CrearReservaRequest request) {
        String punto = request.puntoRecogida().trim();
        if (punto.isBlank()) {
            throw new IllegalArgumentException("Indica el punto donde te recogerán.");
        }

        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el viaje."));
        Instant ahora = Instant.now();
        if (viaje.getEstado() != EstadoViaje.PROGRAMADO || viaje.getFechaHoraSalida() == null || !viaje.getFechaHoraSalida().isAfter(ahora)) {
            throw new IllegalArgumentException("Este viaje ya no está disponible para reservar.");
        }

        Reserva existente = reservaRepository.findByViajeIdAndPasajeroId(viajeId, pasajero.id()).orElse(null);
        if (existente != null && existente.getEstado() == EstadoReserva.CONFIRMADA) {
            throw new IllegalArgumentException("Ya tienes un cupo reservado en este viaje.");
        }

        UpdateResult descuento = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(viajeId)
                        .and("estado").is(EstadoViaje.PROGRAMADO)
                        .and("fechaHoraSalida").gt(ahora)
                        .and("cuposDisponibles").gt(0)),
                new Update().inc("cuposDisponibles", -1), Viaje.class);
        if (descuento.getModifiedCount() == 0) {
            throw new IllegalArgumentException("Ya no quedan cupos disponibles para este viaje.");
        }

        Reserva guardada;
        try {
            if (existente == null) {
                Reserva nueva = new Reserva();
                nueva.setViajeId(viajeId);
                nueva.setPasajeroId(pasajero.id());
                nueva.setCorreoPasajero(pasajero.correo());
                nueva.setNombrePasajero(pasajero.nombre());
                nueva.setPuntoRecogida(punto);
                nueva.setEstado(EstadoReserva.CONFIRMADA);
                nueva.setCreadaEn(ahora);
                guardada = reservaRepository.save(nueva);
            } else {
                guardada = mongoTemplate.findAndModify(
                        Query.query(Criteria.where("_id").is(existente.getId())
                                .and("estado").is(EstadoReserva.CANCELADA)),
                        new Update().set("estado", EstadoReserva.CONFIRMADA)
                                .set("puntoRecogida", punto)
                                .set("correoPasajero", pasajero.correo())
                                .set("nombrePasajero", pasajero.nombre())
                                .set("creadaEn", ahora)
                                .unset("canceladaEn"),
                        FindAndModifyOptions.options().returnNew(true), Reserva.class);
                if (guardada == null) {
                    throw new IllegalArgumentException("Ya tienes un cupo reservado en este viaje.");
                }
            }
        } catch (DuplicateKeyException e) {
            devolverCupo(viajeId);
            throw new IllegalArgumentException("Ya tienes un cupo reservado en este viaje.");
        } catch (RuntimeException e) {
            devolverCupo(viajeId);
            throw e;
        }
        return ReservaResponse.desde(guardada, viaje);
    }

    public List<ReservaResponse> listarPropias(GoogleIdentityService.IdentidadGoogle pasajero) {
        return reservaRepository.findByPasajeroIdOrderByCreadaEnDesc(pasajero.id()).stream()
                .map(reserva -> ReservaResponse.desde(reserva, viajeRepository.findById(reserva.getViajeId()).orElse(null)))
                .toList();
    }

    public ReservaResponse cancelar(String reservaId, GoogleIdentityService.IdentidadGoogle pasajero) {
        Reserva existente = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la reserva."));
        if (!existente.getPasajeroId().equals(pasajero.id())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la reserva.");
        }
        if (existente.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalArgumentException("La reserva ya está cancelada.");
        }

        Viaje viaje = viajeRepository.findById(existente.getViajeId()).orElse(null);
        if (viaje != null && viaje.getFechaHoraSalida() != null && !viaje.getFechaHoraSalida().isAfter(Instant.now())) {
            throw new IllegalArgumentException("No puedes cancelar una reserva después de la salida del viaje.");
        }

        Instant ahora = Instant.now();
        Reserva cancelada = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(reservaId)
                        .and("pasajeroId").is(pasajero.id())
                        .and("estado").is(EstadoReserva.CONFIRMADA)),
                new Update().set("estado", EstadoReserva.CANCELADA).set("canceladaEn", ahora),
                FindAndModifyOptions.options().returnNew(true), Reserva.class);
        if (cancelada == null) {
            throw new IllegalArgumentException("La reserva ya está cancelada.");
        }

        if (viaje != null && viaje.getEstado() == EstadoViaje.PROGRAMADO) {
            try {
                devolverCupo(viaje.getId());
            } catch (RuntimeException e) {
                mongoTemplate.updateFirst(
                        Query.query(Criteria.where("_id").is(reservaId).and("estado").is(EstadoReserva.CANCELADA)),
                        new Update().set("estado", EstadoReserva.CONFIRMADA).unset("canceladaEn"), Reserva.class);
                throw e;
            }
        }
        return ReservaResponse.desde(cancelada, viaje);
    }

    private void devolverCupo(String viajeId) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(viajeId)),
                new Update().inc("cuposDisponibles", 1), Viaje.class);
    }
}
