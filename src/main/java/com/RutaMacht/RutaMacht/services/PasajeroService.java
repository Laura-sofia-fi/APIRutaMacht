package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.model.IActualizable;
import com.RutaMacht.RutaMacht.model.Pasajero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PasajeroService {

    private final List<Pasajero> pasajeros = new ArrayList<>();
    private final List<IActualizable> guiActualiza = new ArrayList<>();

    public void registrarGUI(IActualizable gui) {
        if (gui != null && !guiActualiza.contains(gui)) {
            guiActualiza.add(gui);
        }
    }

    public Pasajero crearPasajero(Pasajero pasajero) {

        if (pasajero == null || !pasajero.validarPasajero()) {
            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        validarFormato(pasajero);

        if (buscarPasajero(pasajero.getId()) != null) {
            throw new RuntimeException(
                    "ERROR: ya existe un pasajero con el ID "
                            + pasajero.getId()
            );
        }

        pasajeros.add(pasajero);
        actualizar();

        return pasajero;
    }

    public List<Pasajero> listarPasajeros() {
        return Collections.unmodifiableList(pasajeros);
    }

    // Búsqueda exacta por ID (la usa crearPasajero para evitar duplicados)
    public Pasajero buscarPasajero(String id) {

        for (Pasajero pasajero : pasajeros) {

            if (Objects.equals(pasajero.getId(), id)) {
                return pasajero;
            }
        }

        return null;
    }

    // Búsqueda con coincidencias parciales (para la GUI)
    public List<Pasajero> buscarCoincidencias(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return listarPasajeros();
        }

        String filtro = texto.trim().toLowerCase();
        List<Pasajero> resultado = new ArrayList<>();

        for (Pasajero p : pasajeros) {
            if (contiene(p.getId(), filtro)
                    || contiene(p.getNombre(), filtro)
                    || contiene(p.getApellido(), filtro)
                    || contiene(p.getDocumento(), filtro)) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    private boolean contiene(String campo, String filtro) {
        return campo != null && campo.toLowerCase().contains(filtro);
    }

    public void modificarPasajero(Pasajero pasajeroActualizado) {

        if (pasajeroActualizado == null ||
                !pasajeroActualizado.validarPasajero()) {

            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        validarFormato(pasajeroActualizado);

        for (int i = 0; i < pasajeros.size(); i++) {

            if (Objects.equals(
                    pasajeros.get(i).getId(),
                    pasajeroActualizado.getId())) {

                pasajeros.set(i, pasajeroActualizado);
                actualizar();
                return;
            }
        }

        throw new RuntimeException(
                "No se encontró ningún pasajero con ese ID."
        );
    }

    public void eliminarPasajero(String id) {

        boolean eliminado =
                pasajeros.removeIf(
                        p -> Objects.equals(p.getId(), id)
                );

        if (eliminado) {
            actualizar();
        } else {
            throw new RuntimeException(
                    "No se encontró ningún pasajero con ese ID."
            );
        }
    }

    private void validarFormato(Pasajero pasajero) {

        if (!pasajero.getNombre().matches("[\\p{L} ]+")) {
            throw new RuntimeException(
                    "ERROR: el nombre solo puede contener letras."
            );
        }

        if (!pasajero.getApellido().matches("[\\p{L} ]+")) {
            throw new RuntimeException(
                    "ERROR: el apellido solo puede contener letras."
            );
        }

        // Solo dígitos
        if (!pasajero.getId().matches("\\d+")) {
            throw new RuntimeException(
                    "ERROR: el ID solo puede contener números."
            );
        }

        if (!pasajero.getDocumento().matches("\\d+")) {
            throw new RuntimeException(
                    "ERROR: el documento solo puede contener números."
            );
        }

        if (!pasajero.getTelefono().matches("\\d+")) {
            throw new RuntimeException(
                    "ERROR: el teléfono solo puede contener números."
            );
        }
    }
    public void actualizar() {

        for (IActualizable act : guiActualiza) {
            act.actualizar();
        }
    }
}