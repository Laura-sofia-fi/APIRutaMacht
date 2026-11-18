package com.RutaMacht.RutaMacht.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "conductor")

public class Conductor extends Persona{
    @Schema(example = "LIC-998877")
    private String licencia;
    @Schema(example = "B1")
    private String categoriaLicencia;
    @Schema(description = "Formato AAAA-MM-DD", example = "2028-12-31")
    private String fechaVenciLicencia;
    @Schema(description = "true = activo, false = inactivo", example = "true")
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
