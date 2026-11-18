package com.RutaMacht.RutaMacht.repository;

import com.RutaMacht.RutaMacht.model.Reserva;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends MongoRepository<Reserva, String> {
    Optional<Reserva> findByViajeIdAndPasajeroId(String viajeId, String pasajeroId);
    List<Reserva> findByPasajeroIdOrderByCreadaEnDesc(String pasajeroId);
}
