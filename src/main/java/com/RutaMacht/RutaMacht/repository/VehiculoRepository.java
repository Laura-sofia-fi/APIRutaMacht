package com.RutaMacht.RutaMacht.repository;

import com.RutaMacht.RutaMacht.model.Vehiculo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {

    Optional<Vehiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);
}