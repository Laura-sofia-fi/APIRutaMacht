package com.RutaMacht.RutaMacht.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String documento;
    private String tipoDocumento;
    private String telefono;
    private String correo;
    private String fechaRegistro;
}