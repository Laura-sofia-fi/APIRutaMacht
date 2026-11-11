package com.RutaMacht.RutaMacht.dto;

import com.RutaMacht.RutaMacht.model.EstadoReserva;
import com.RutaMacht.RutaMacht.model.Reserva;
import com.RutaMacht.RutaMacht.model.Viaje;

import java.math.BigDecimal;
import java.time.Instant;

public record ReservaResponse(
        String id,
        String viajeId,
        String origen,
        String destino,
        Instant fechaHoraSalida,
        BigDecimal aportePorCupo,
        String puntoRecogida,
        EstadoReserva estado,
        Instant creadaEn,
        Instant canceladaEn) {
    public static ReservaResponse desde(Reserva reserva, Viaje viaje) {
        return new ReservaResponse(
                reserva.getId(), reserva.getViajeId(),
                viaje == null ? "Viaje no disponible" : viaje.getOrigen(),
                viaje == null ? "" : viaje.getDestino(),
                viaje == null ? null : viaje.getFechaHoraSalida(),
                viaje == null ? null : viaje.getAportePorCupo(),
                reserva.getPuntoRecogida(), reserva.getEstado(),
                reserva.getCreadaEn(), reserva.getCanceladaEn());
    }
}
