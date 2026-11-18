package com.RutaMacht.RutaMacht.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "Id de un conductor existente y activo") @NotBlank String conductorId,
        @Schema(description = "Id de un vehículo existente") @NotBlank String vehiculoId,
        @Schema(example = "Ibagué") @NotBlank @Size(max = 120) String origen,
        @Schema(example = "Bogotá") @NotBlank @Size(max = 120) String destino,
        @Size(max = 20) List<@NotBlank @Size(max = 120) String> paradas,
        @Schema(description = "Fecha futura en ISO-8601 (UTC)", example = "2027-01-15T14:30:00Z") @NotNull @Future Instant fechaHoraSalida,
        @Schema(description = "Cupos para pasajeros (máximo capacidad del vehículo - 1)", example = "3") @Min(1) int cupos,
        @Schema(example = "25000") @NotNull @DecimalMin("0.00") BigDecimal aportePorCupo
) {
}
