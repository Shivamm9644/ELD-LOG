package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.SplitLog;

@Repository
public interface SplitLogRepo extends MongoRepository<SplitLog, String> {
	
	@Query(value="{ 'driverId' : ?0 }")
	public List<SplitLog> findSplitLogByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0}", delete = true)
	public long deleteAllSplitLogByDriverId(long driverId);
	
}
