package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.model.Conductor;
import com.RutaMacht.RutaMacht.model.IActualizable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConductorServicie {

    private final List<Conductor> conductores = new ArrayList<>();
    private final List<IActualizable> guiActualiza = new ArrayList<>();

    public void registrarGUI(IActualizable gui) {
        if (gui != null && !guiActualiza.contains(gui)) {
            guiActualiza.add(gui);
        }
    }

    public Conductor crearConductor(Conductor conductor) {

        if (conductor == null || !conductor.validarConductor()) {
            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        validarFormato(conductor);

        if (buscarConductor(conductor.getId()) != null) {
            throw new RuntimeException(
                    "ERROR: ya existe un conductor con el ID "
                            + conductor.getId()
            );
        }

        conductores.add(conductor);
        actualizar();

        return conductor;
    }

    public List<Conductor> listarConductores() {
        return Collections.unmodifiableList(conductores);
    }

    public void eliminarConductor(String id) {

        boolean eliminado =
                conductores.removeIf(
                        c -> Objects.equals(c.getId(), id)
                );

        if (eliminado) {
            actualizar();
        } else {
            throw new RuntimeException(
                    "No se encontró ningún conductor con ese ID."
            );
        }
    }

    public Conductor buscarConductor(String id) {

        for (Conductor conductor : conductores) {

            if (Objects.equals(conductor.getId(), id)) {
                return conductor;
            }
        }

        return null;
    }

    public void modificarConductor(Conductor conductorActualizado) {

        if (conductorActualizado == null ||
                !conductorActualizado.validarConductor()) {

            throw new RuntimeException(
                    "ERROR: ingrese los datos correctamente!"
            );
        }

        validarFormato(conductorActualizado);

        for (int i = 0; i < conductores.size(); i++) {

            if (Objects.equals(
                    conductores.get(i).getId(),
                    conductorActualizado.getId())) {

                conductores.set(i, conductorActualizado);
                actualizar();
                return;
            }
        }

        throw new RuntimeException(
                "No se encontró ningún conductor con ese ID."
        );
    }

    private void validarFormato(Conductor conductor) {

        if (!conductor.getNombre().matches("[\\p{L} ]+")) {
            throw new RuntimeException(
                    "ERROR: el nombre solo puede contener letras."
            );
        }

        if (!conductor.getApellido().matches("[\\p{L} ]+")) {
            throw new RuntimeException(
                    "ERROR: el apellido solo puede contener letras."
            );
        }

        if (!conductor.getId().matches("\\d+")) {
            throw new RuntimeException(
                    "ERROR: el ID solo puede contener números."
            );
        }

        if (!conductor.getDocumento().matches("\\d+")) {
            throw new RuntimeException(
                    "ERROR: el documento solo puede contener números."
            );
        }
        try {
            java.time.LocalDate vencimiento = java.time.LocalDate.parse(
                    conductor.getFechaVenciLicencia(),
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );

            if (vencimiento.isBefore(java.time.LocalDate.now())) {
                throw new RuntimeException(
                        "ERROR: la licencia está vencida."
                );
            }
        } catch (java.time.format.DateTimeParseException e) {
            throw new RuntimeException(
                    "ERROR: la fecha de vencimiento debe tener el formato yyyy-MM-dd."
            );
        }
    }

    public void actualizar() {

        for (IActualizable act : guiActualiza) {
            act.actualizar();
        }
    }
}