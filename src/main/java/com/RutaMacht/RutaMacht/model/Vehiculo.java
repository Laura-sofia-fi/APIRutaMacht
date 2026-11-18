package com.RutaMacht.RutaMacht.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "vehiculo")

public class Vehiculo {
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Lo genera MongoDB")
    private String id;
    @Schema(example = "ABC123")
    private String placa;
    @Schema(example = "2020")
    private int  anio;
    @Schema(example = "Toyota")
    private String marca;
    @Schema(example = "Corolla")
    private String modelo;
    @Schema(example = "Rojo")
    private String color;
    @Schema(example = "Gasolina")
    private String tipoCombustible;
    @Schema(description = "Exactamente 17 caracteres alfanuméricos", example = "1HGCM82633A004352")
    private String numeroChasis;
    @Schema(description = "Asientos totales, incluido el conductor (mínimo 2)", example = "5")
    private Integer capacidadTotal;

    public boolean validarVehiculo() {
        return getPlaca() != null && !getPlaca().isBlank() &&
                getMarca() != null && !getMarca().isBlank() &&
                getModelo() != null && !getModelo().isBlank() &&
                getColor() != null && !getColor().isBlank() &&
                getTipoCombustible() != null && !getTipoCombustible().isBlank() &&
                getNumeroChasis() != null && !getNumeroChasis().isBlank() &&
                getCapacidadTotal() != null && getCapacidadTotal() >= 2 &&
                getAnio() > 0;
    }

}
