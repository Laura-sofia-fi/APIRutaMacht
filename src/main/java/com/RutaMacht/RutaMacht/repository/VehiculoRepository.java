package com.RutaMacht.RutaMacht.repository;

import com.RutaMacht.RutaMacht.model.Conductor;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ConductorRepository extends MongoRepository<Conductor,String>{

}
