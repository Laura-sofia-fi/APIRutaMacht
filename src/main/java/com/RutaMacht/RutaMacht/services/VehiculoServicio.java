package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.model.Vehiculo;
import com.RutaMacht.RutaMacht.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoServicio {

    // Los datos se almacenaban temporalmente en memoria.
    //private final List<Vehiculo> vehiculos = new ArrayList<>();

    private final VehiculoRepository repository;

    public Vehiculo crear(Vehiculo vehiculo) {

        if (vehiculo == null || !vehiculo.validarVehiculo()) {
            throw new RuntimeException("ERROR: ingrese los datos correctamente!");
        }

        validarFormato(vehiculo);

        if (repository.existsByPlaca(vehiculo.getPlaca())) {
            throw new RuntimeException(
                    "ERROR: ya existe un vehículo con la placa " + vehiculo.getPlaca());
        }

        // MongoDB genera el identificador automáticamente.
        vehiculo.setId(null);
        return repository.save(vehiculo);
    }

    public List<Vehiculo> listar() {
        return repository.findAll();
    }

    public Vehiculo buscarPorId(String id) {
        return repository.findById(id).orElse(null);
    }

    public Vehiculo buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa).orElse(null);
    }

    public Vehiculo actualizar(String id, Vehiculo datosActualizados) {

        if (datosActualizados == null || !datosActualizados.validarVehiculo()) {
            throw new RuntimeException("ERROR: ingrese los datos correctamente!");
        }

        validarFormato(datosActualizados);

        // Buscar el vehículo existente en MongoDB.
        Vehiculo vehiculo = repository.findById(id).orElse(null);

        if (vehiculo == null) {
            return null;
        }

        // Evitar que la nueva placa coincida con la de otro vehículo.
        Vehiculo conMismaPlaca =
                repository.findByPlaca(datosActualizados.getPlaca()).orElse(null);

        if (conMismaPlaca != null && !conMismaPlaca.getId().equals(id)) {
            throw new RuntimeException(
                    "ERROR: ya existe un vehículo con la placa "
                            + datosActualizados.getPlaca());
        }

        // Actualizar atributos (conservando el id existente).
        vehiculo.setPlaca(datosActualizados.getPlaca());
        vehiculo.setMarca(datosActualizados.getMarca());
        vehiculo.setColor(datosActualizados.getColor());
        vehiculo.setTipoCombustible(datosActualizados.getTipoCombustible());
        vehiculo.setNumeroChasis(datosActualizados.getNumeroChasis());
        // ... agrega aquí cualquier otro atributo de Vehiculo ...

        // Guardar los cambios en MongoDB.
        return repository.save(vehiculo);
    }

    public boolean eliminar(String id) {

        // Verificar que el vehículo exista.
        if (!repository.existsById(id)) {
            return false;
        }

        // Eliminar el documento.
        repository.deleteById(id);

        return true;
    }

    private void validarFormato(Vehiculo vehiculo) {

        // Solo letras (con tildes y ñ) y espacios
        if (!vehiculo.getMarca().matches("[\\p{L} ]+")) {
            throw new RuntimeException("ERROR: la marca solo puede contener letras.");
        }

        if (!vehiculo.getColor().matches("[\\p{L} ]+")) {
            throw new RuntimeException("ERROR: el color solo puede contener letras.");
        }

        if (!vehiculo.getTipoCombustible().matches("[\\p{L} ]+")) {
            throw new RuntimeException("ERROR: el combustible solo puede contener letras.");
        }

        if (!vehiculo.getNumeroChasis().matches("[A-Za-z0-9]{17}")) {
            throw new RuntimeException(
                    "ERROR: el número de chasis debe tener exactamente 17 caracteres alfanuméricos.");
        }

        if (!vehiculo.getPlaca().matches("[A-Za-z0-9]+")) {
            throw new RuntimeException(
                    "ERROR: la placa solo puede contener letras y números.");
        }
    }
}