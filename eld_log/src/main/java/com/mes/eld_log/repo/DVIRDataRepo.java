package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.DVIRDataCRUDDto;
import com.mes.eld_log.models.DVIRData;
import com.mes.eld_log.models.DriveringStatus;

@Repository
public interface DVIRDataRepo extends MongoRepository<DVIRData, String> {
	
	@Query(value="{'driverId' : ?0}", count = true)
	public Integer CountDVIRByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0}", delete = true)
	public DVIRData deleteAllDVIRDataByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0, 'lDateTime' : { $gte: ?1, $lte: ?2 }}", delete = true)
	public long deleteAllDVIRDataByDriverIdAndDate(long driverId,long startTimestamp, long endTimestamp);
	
	@Query(value="{'driverId' : ?0, 'lDateTime' : ?1}")
	public DVIRDataCRUDDto findAndViewDvirDataByDriverId(long driverId, long lDateTime);
	
	@Query(value="{'driverId' : ?0, 'timestamp' : ?1}", delete = true)
	public DVIRDataCRUDDto deleteDVIRDataByDriverIdAndTimestamp(long driverId,String timestamp);
	
}
