package com.RutaMacht.RutaMacht.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)

public class Vehiculo {
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
