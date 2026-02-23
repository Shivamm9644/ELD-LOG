package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.LiveDataLog;

@Repository
public interface LiveDataLogRepo extends MongoRepository<LiveDataLog, String> {
	
	@Query(value="{ 'DriverId' : ?0,'MAC' : ?1 }")
	public List<LiveDataLog> findAndViewLiveDataLog(String DriverId, String MAC);
	
}
