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

    public boolean validarConductor() {
        return this.getNombre() != null &&
                this.getApellido() != null &&
                this.getTipoDocumento() != null &&
                this.getDocumento() != null &&
                this.getTelefono() != null &&
                this.getCorreo() != null &&
                this.getLicencia() != null &&
                this.getCategoriaLicencia() != null &&
                this.getFechaVenciLicencia() != null;
    }

}
