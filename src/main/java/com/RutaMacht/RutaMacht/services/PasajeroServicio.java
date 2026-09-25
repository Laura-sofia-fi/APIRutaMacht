package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.model.IActualizable;
import com.RutaMacht.RutaMacht.model.Pasajero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PasajeroServicio {

    private static final List<Pasajero> pasajeros = new ArrayList<>();
    private static final List<IActualizable> guiActualiza = new ArrayList<>();

    public static void registrarGUI(IActualizable gui) {
        if (gui != null && !guiActualiza.contains(gui)) {
            guiActualiza.add(gui);
        }
    }
    public static void crearPasajero(Pasajero pasajero) {

        if (pasajero == null || !pasajero.validarPasajero()) {
            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        if (buscarPasajero(pasajero.getId()) != null) {
            throw new RuntimeException(
                    "ERROR: ya existe un pasajero con el ID "
                            + pasajero.getId()
            );
        }

        pasajeros.add(pasajero);
        actualizar();
    }

    public static List<Pasajero> listarPasajeros() {
        return Collections.unmodifiableList(pasajeros);
    }

    public static Pasajero buscarPasajero(String idUsuario) {

        for (Pasajero pasajero : pasajeros) {

            if (Objects.equals(
                    pasajero.getId(),
                    idUsuario)) {

                return pasajero;
            }
        }

        return null;
    }

    public static void modificarPasajero(Pasajero pasajeroActualizado) {

        if (pasajeroActualizado == null
                || !pasajeroActualizado.validarPasajero()) {

            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

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

    public static void eliminarPasajero(String idUsuario) {

        boolean eliminado = pasajeros.removeIf(
                p -> Objects.equals(
                        p.getId(),
                        idUsuario
                )
        );

        if (eliminado) {
            actualizar();
        } else {
            throw new RuntimeException(
                    "No se encontró ningún pasajero con ese ID."
            );
        }
    }

    public static void actualizar() {

        for (IActualizable act : guiActualiza) {
            act.actualizar();
        }
    }
}
