package com.RutaMacht.RutaMacht.repository;

import com.RutaMacht.RutaMacht.model.EstadoViaje;
import com.RutaMacht.RutaMacht.model.Viaje;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ViajeRepository extends MongoRepository<Viaje, String> {

    boolean existsByConductorIdAndEstado(String conductorId, EstadoViaje estado);

    boolean existsByVehiculoIdAndEstado(String vehiculoId, EstadoViaje estado);
}
