package com.RutaMacht.RutaMacht.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CrearViajeRequest(
        @NotBlank String conductorId,
        @NotBlank String vehiculoId,
        @NotBlank @Size(max = 120) String origen,
        @NotBlank @Size(max = 120) String destino,
        @Size(max = 20) List<@NotBlank @Size(max = 120) String> paradas,
        @NotNull @Future Instant fechaHoraSalida,
        @Min(1) int cupos,
        @NotNull @DecimalMin("0.00") BigDecimal aportePorCupo
) {
}
