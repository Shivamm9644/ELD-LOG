package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.DriverWorkingStatus;

@Repository
public interface DriverWorkingStatusRepo extends MongoRepository<DriverWorkingStatus, String> {
	
	@Query(value="{ 'driverId' : ?0}")
	public DriverWorkingStatus findAndViewDriverWorkingstatusByDriverId(long driverId);
	
	@Query(value="{ 'driverId' : ?0}")
	public List<DriverWorkingStatus> findAndViewDriverWorkingstatusById(long driverId);
	
}
