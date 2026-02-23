package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ExceptionLog;

@Repository
public interface ExceptionLogRepo extends MongoRepository<ExceptionLog, String> {
	
	@Query(value="{ 'driverId' : ?0 }")
	public List<ExceptionLog> findAndViewByDriverId(Integer driverId);
	
}
