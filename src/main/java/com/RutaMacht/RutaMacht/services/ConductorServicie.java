package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.model.Conductor;
import com.RutaMacht.RutaMacht.model.IActualizable;
import com.RutaMacht.RutaMacht.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductorServicie {

    private final ConductorRepository repository;
    private final List<IActualizable> guiActualiza = new ArrayList<>();

    public void registrarGUI(IActualizable gui) {
        if (gui != null && !guiActualiza.contains(gui)) {
            guiActualiza.add(gui);
        }
    }

    public Conductor crearConductor(Conductor conductor) {

        if (conductor == null || !conductor.validarConductor()) {
            throw new RuntimeException("ERROR: ingrese los datos correctamente!");
        }

        validarFormato(conductor);

        // Verificar que no exista otro conductor con el mismo ID.
        if (repository.existsById(conductor.getId())) {
            throw new RuntimeException(
                    "ERROR: ya existe un conductor con el ID " + conductor.getId());
        }

        Conductor guardado = repository.save(conductor);
        actualizar();

        return guardado;
    }

    public List<Conductor> listarConductores() {
        return repository.findAll();
    }

    public void eliminarConductor(String id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("No se encontró ningún conductor con ese ID.");
        }

        repository.deleteById(id);
        actualizar();
    }

    public Conductor buscarConductor(String id) {
        return repository.findById(id).orElse(null);
    }

    public void modificarConductor(Conductor conductorActualizado) {

        if (conductorActualizado == null || !conductorActualizado.validarConductor()) {
            throw new RuntimeException("ERROR: ingrese los datos correctamente!");
        }

        validarFormato(conductorActualizado);
        if (!repository.existsById(conductorActualizado.getId())) {
            throw new RuntimeException("No se encontró ningún conductor con ese ID.");
        }

        repository.save(conductorActualizado);
        actualizar();
    }

    private void validarFormato(Conductor conductor) {
    }

    public void actualizar() {
        for (IActualizable act : guiActualiza) {
            act.actualizar();
        }
    }
}