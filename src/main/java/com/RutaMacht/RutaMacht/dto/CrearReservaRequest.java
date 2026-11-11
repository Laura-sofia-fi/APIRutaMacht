package com.RutaMacht.RutaMacht.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearReservaRequest(
        @NotBlank(message = "Indica el punto donde te recogerán.")
        @Size(max = 160, message = "El punto de recogida no puede superar 160 caracteres.")
        String puntoRecogida) {
}
