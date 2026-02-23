package com.mes.eld_log.repo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.SimulatorCRUDDto;
import com.mes.eld_log.models.Simulator;

@Repository
public interface SimulatorRepo extends MongoRepository<Simulator, String> {
	
	@Query(value="{'_id' : ?0}", delete = true)
	public Simulator DeleteSimulatorById(ObjectId _id);

}
