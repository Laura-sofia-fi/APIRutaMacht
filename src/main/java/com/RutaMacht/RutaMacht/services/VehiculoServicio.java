package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.model.IActualizable;
import com.RutaMacht.RutaMacht.model.Vehiculo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VehiculoServicio {

    private static final List<Vehiculo> vehiculos = new ArrayList<>();
    public static void crearVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null || !vehiculo.validarVehiculo()) {
            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        if (buscarVehiculo(vehiculo.getPlaca()) != null) {
            throw new RuntimeException(
                    "ERROR: ya existe un vehículo con la placa "
                            + vehiculo.getPlaca()
            );
        }

        vehiculos.add(vehiculo);
        actualizar();
    }

    public static List<Vehiculo> listarVehiculos() {
        return Collections.unmodifiableList(vehiculos);
    }

    public static Vehiculo buscarVehiculo(String placa) {

        for (Vehiculo vehiculo : vehiculos) {

            if (Objects.equals(vehiculo.getPlaca(), placa)) {
                return vehiculo;
            }
        }

        return null;
    }

    public static void modificarVehiculo(Vehiculo vehiculoActualizado) {

        if (vehiculoActualizado == null
                || !vehiculoActualizado.validarVehiculo()) {

            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        for (int i = 0; i < vehiculos.size(); i++) {

            if (Objects.equals(
                    vehiculos.get(i).getPlaca(),
                    vehiculoActualizado.getPlaca())) {

                vehiculos.set(i, vehiculoActualizado);
                actualizar();
                return;
            }
        }

        throw new RuntimeException(
                "No se encontró ningún vehículo con esa placa."
        );
    }

    public static void eliminarVehiculo(String placa) {

        boolean eliminado = vehiculos.removeIf(
                v -> Objects.equals(v.getPlaca(), placa)
        );

        if (eliminado) {
            actualizar();
        } else {
            throw new RuntimeException(
                    "No se encontró ningún vehículo con esa placa."
            );
        }
    }
    public static void actualizar() {
    }
}

