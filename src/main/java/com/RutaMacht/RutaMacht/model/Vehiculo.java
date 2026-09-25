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
}
