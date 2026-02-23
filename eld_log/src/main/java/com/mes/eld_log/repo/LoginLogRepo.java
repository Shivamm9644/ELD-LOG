package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.LoginLog;

@Repository
public interface LoginLogRepo extends MongoRepository<LoginLog, String> {
	
	@Query(value="{ 'employeeId' : ?0 }")
	public List<LoginLog> findAndViewLoginLog(Integer employeeId);
	
	@Query(value="{'employeeId' : ?0}", delete = true)
	public LoginLog deleteAllLoginLogByDriverId(Integer employeeId);
	
	@Query(value="{'employeeId' : ?0,'receivedTimestamp' : { $gte: ?1, $lte: ?2 }}", delete = true)
	public long deleteAllLoginLogByDriverIdAndDate(Integer employeeId,long startTimestamp, long endTimestamp);
	
	@Query(value="{'employeeId' : ?0}", count = true)
	public Integer CountLoginByDriverId(Integer employeeId);
	
}
