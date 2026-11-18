package com.RutaMacht.RutaMacht.repository;

import com.RutaMacht.RutaMacht.model.Viaje;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ViajeRepository extends MongoRepository<Viaje, String> {
}
