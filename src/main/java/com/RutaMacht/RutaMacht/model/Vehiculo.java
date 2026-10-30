package com.RutaMacht.RutaMacht.model;

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
@Document(collation = "Vehiculo")

public class Vehiculo {
    @Id
    private String id;
    private String placa;
    private int  anio;
    private String marca;
    private String modelo;
    private String color;
    private String tipoCombustible;
    private String numeroChasis;

    public boolean validarVehiculo() {
        return getPlaca() != null && !getPlaca().isBlank() &&
                getMarca() != null && !getMarca().isBlank() &&
                getModelo() != null && !getModelo().isBlank() &&
                getColor() != null && !getColor().isBlank() &&
                getTipoCombustible() != null && !getTipoCombustible().isBlank() &&
                getNumeroChasis() != null && !getNumeroChasis().isBlank() &&
                getAnio() > 0;
    }

}
