package com.RutaMacht.RutaMacht.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "viajes")
@CompoundIndex(name = "viajes_estado_salida_idx", def = "{ 'estado': 1, 'fechaHoraSalida': 1 }")
public class Viaje {

    @Id
    private String id;
    private String conductorId;
    private String vehiculoId;
    private String origen;
    private String destino;
    private List<String> paradas = new ArrayList<>();
    private Instant fechaHoraSalida;
    private Integer cupos;
    private Integer cuposDisponibles;
    private BigDecimal aportePorCupo;
    private EstadoViaje estado;
    private Instant creadoEn;
}
