package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.DriverStatusLog;
import com.mes.eld_log.models.DriverWorkingStatus;
import com.mes.eld_log.models.DriveringStatus;

@Repository
public interface DriverStatusLogRepo extends MongoRepository<DriverStatusLog, String> {
	
	@Query(value="{ 'logDataId' : ?0}")
	public DriverStatusLog findAndViewDriverStatusLogById(String logDataId);
	
	@Query(value="{ 'logDataId' : ?0}")
	public List<DriverStatusLog> CheckDriverStatusLogById(String logDataId);
	
	@Query(value="{ 'driverId' : ?0, 'status' : ?1}")
	public List<DriverStatusLog> findAndViewByDriverId(long driverId, String status);
	
	@Query(value="{ 'logDataId' : ?0, 'logType' : ?1}")
	public DriverStatusLog findAndViewDriverStatusLogById1(String logDataId, String logType);
	
	@Query(value="{'driverId' : ?0}", delete = true)
	public DriverStatusLog deleteAllDriveringStatusLogByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0}", count = true)
	public Integer CountDriverStatusLogByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0, 'dateTime' : { $gte: ?1, $lte: ?2 }}", delete = true)
	public long deleteAllDriveringStatusLogByDriverIdAndDate(long driverId,long startTimestamp, long endTimestamp);
	
}
