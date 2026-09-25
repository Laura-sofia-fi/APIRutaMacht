package com.RutaMacht.RutaMacht.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)

public class Conductor extends Persona{
    private String licencia;
    private String categoriaLicencia;
    private String fechaVenciLicencia;
    private Boolean estado;
    // private Vehiculo vehiculo;
}
