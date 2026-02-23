package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.AlertsLogViewDto;
import com.mes.eld_log.models.AlertsLog;

@Repository
public interface AlertsLogRepo extends MongoRepository<AlertsLog, String> {
	
	@Query(value="{ 'driverId' : ?0, 'startUtcDateTime' : ?1 }")
	public List<AlertsLog> findAndViewByDriverIdAndUtcDateTime(long driverId,long startUtcDateTime);
	
	@Query(value="{ 'clientId' : ?0, 'isRead' : ?1 }")
	public List<AlertsLogViewDto> findAndViewAllUnreadAlerts(long clientId,int isRead);
	
}
