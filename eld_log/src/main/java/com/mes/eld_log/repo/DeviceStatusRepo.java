package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.DeviceStatus;

@Repository
public interface DeviceStatusRepo extends MongoRepository<DeviceStatus, String> {
	
	@Query(value="{ 'driverId' : ?0 }")
	public DeviceStatus findByDeviceStatusId(long driverId);
	
	@Query(value="{ 'driverId' : ?0}")
	public List<DeviceStatus> findAndViewByDeviceStatusId(long driverId);
	

}
