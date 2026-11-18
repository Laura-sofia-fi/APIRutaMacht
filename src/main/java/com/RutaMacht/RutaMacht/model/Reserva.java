package com.RutaMacht.RutaMacht.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "reservas")
@CompoundIndex(name = "reserva_viaje_pasajero_uq", def = "{'viajeId': 1, 'pasajeroId': 1}", unique = true)
public class Reserva {
    @Id
    private String id;
    private String viajeId;
    private String pasajeroId;
    private String correoPasajero;
    private String nombrePasajero;
    private String puntoRecogida;
    private EstadoReserva estado;
    private Instant creadaEn;
    private Instant canceladaEn;
}
