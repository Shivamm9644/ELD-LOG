package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.DeviceMasterViewDto;
import com.mes.eld_log.models.DeviceMaster;

@Repository
public interface DeviceMasterRepo extends MongoRepository<DeviceMaster, String> {
	
	@Query(value="{ 'deviceId' : ?0 }")
	public DeviceMaster findByDeviceId(Integer deviceId);
	
	@Query(value="{ 'deviceId' : ?0,'clientId' : ?1 }")
	public List<DeviceMaster> findAndViewByDeviceId(Integer deviceId,long clientId);
	
	@Query(value="{ 'deviceId' : ?0,'clientId' : ?1 }")
	public List<DeviceMasterViewDto> findAndViewByDeviceId1(Integer deviceId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<DeviceMaster> findAllDevices(long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<DeviceMasterViewDto> findAllDeviceData(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $deviceId }}}" })
	public Object findMaxIdInDeviceMaster();
	
	@Query(value="{'deviceId' : ?0}", delete = true)
	public DeviceMaster DeleteDeviceById(Integer deviceId);

}
