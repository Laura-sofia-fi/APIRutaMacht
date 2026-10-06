package com.RutaMacht.RutaMacht.services;

import com.RutaMacht.RutaMacht.client.PasajeroClient;
import com.RutaMacht.RutaMacht.model.IActualizable;
import com.RutaMacht.RutaMacht.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasajeroService {

    private final PasajeroClient pasajeroClient;
    private final List<IActualizable> guiActualiza = new ArrayList<>();

    public void registrarGUI(IActualizable gui) {
        if (gui != null && !guiActualiza.contains(gui)) {
            guiActualiza.add(gui);
        }
    }

    public String verificarConexion() {
        return pasajeroClient.verificarConexion();
    }

    public List<Pasajero> listarPasajeros() {
        return pasajeroClient.listar();
    }

    public Pasajero buscarPasajero(String id) {
        return pasajeroClient.buscarPorId(id);
    }

    public Pasajero crearPasajero(Pasajero pasajero) {
        validar(pasajero);

        if (pasajeroClient.buscarPorId(pasajero.getId()) != null) {
            throw new IllegalStateException(
                    "ERROR: ya existe un pasajero con el ID " + pasajero.getId());
        }

        Pasajero creado = pasajeroClient.crear(pasajero);
        actualizar();
        return creado;
    }

    public void modificarPasajero(Pasajero pasajero) {
        validar(pasajero);

        if (pasajeroClient.buscarPorId(pasajero.getId()) == null) {
            throw new IllegalStateException("No se encontró ningún pasajero con ese ID.");
        }

        pasajeroClient.modificar(pasajero.getId(), pasajero);
        actualizar();
    }

    public void eliminarPasajero(String id) {
        if (pasajeroClient.buscarPorId(id) == null) {
            throw new IllegalStateException("No se encontró ningún pasajero con ese ID.");
        }

        pasajeroClient.eliminar(id);
        actualizar();
    }

    private void validar(Pasajero pasajero) {
        if (pasajero == null || !pasajero.validarPasajero()) {
            throw new IllegalArgumentException("ERROR: ingrese los datos correctamente!");
        }
    }

    private void actualizar() {
        guiActualiza.forEach(IActualizable::actualizar);
    }
}