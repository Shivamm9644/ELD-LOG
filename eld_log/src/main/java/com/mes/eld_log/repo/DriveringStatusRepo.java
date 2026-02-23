package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.DriveringStatusViewDto;
import com.mes.eld_log.models.DriveringStatus;

@Repository
public interface DriveringStatusRepo extends MongoRepository<DriveringStatus, String> {
	
	@Query(value="{ 'driverId' : ?0, 'utcDateTime' : ?1 }")
	public DriveringStatusViewDto findAndViewDriverStatusById(long driverId, long utcDateTime);
	
	@Query(value="{ 'driverId' : ?0, 'dateTime' : ?1 }")
	public List<DriveringStatusViewDto> findAndViewDriverStatusByDate(long driverId, String dateTime);
	
	@Query(value = "{ 'driverId' : ?0, 'utcDateTime' : { $gte: ?1, $lte: ?2 } }")
	List<DriveringStatus> findAndViewDriverStatusData(long driverId, long startTimestamp, long endTimestamp);
	
	@Query(value="{'driverId' : ?0}", count = true)
	public Integer CountDriverStatusByDriverId(long driverId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $id }}}" })
	public Object findMaxIdInDriveringStatus(long driverId);
	
	@Query(value="{'driverId' : ?0}", delete = true)
	public DriveringStatus deleteAllDriveringStatusByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0, 'utcDateTime' : { $gte: ?1, $lte: ?2 }}", delete = true)
	public long deleteAllDriveringStatusByDriverIdAndDate(long driverId,long startTimestamp, long endTimestamp);
	
	@Query(value="{'driverId' : ?0, 'utcDateTime' : { $gte: ?1, $lte: ?2 }, 'isVoilation' : ?3}", delete = true)
	public long deleteAllDriveringStatusVoilation(long driverId,long startTimestamp, long endTimestamp,Integer isVoilation);
	
	@Query(value="{'driverId' : ?0, 'utcDateTime' : { $gte: ?1, $lte: ?2 }, 'isVoilation' : ?3,'voilationHour' : ?4}", delete = true)
	public long deleteAllDriveringStatusVoilationByHour(long driverId,long startTimestamp, long endTimestamp,Integer isVoilation, Integer voilationHour);
	
	@Query(value = "{'driverId' : ?0, 'utcDateTime' : { $gte: ?1, $lte: ?2 }, 'isVoilation' : ?3, 'voilationHour' : ?4}",count = true)
	public Integer countVoilationRecords(long driverId, long startTimestamp, long endTimestamp, Integer isVoilation, Integer voilationHour);
	
}
