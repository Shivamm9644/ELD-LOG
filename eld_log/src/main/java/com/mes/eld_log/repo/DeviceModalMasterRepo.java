package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.DeviceModalMaster;

@Repository
public interface DeviceModalMasterRepo extends MongoRepository<DeviceModalMaster, String> {
	
	@Query(value="{ 'deviceModalId' : ?0 }")
	public DeviceModalMaster findByDeviceModalId(Integer deviceModalId);
	
	@Query(value="{ 'deviceModalId' : ?0}")
	public List<DeviceModalMaster> findAndViewByDeviceModalId(Integer deviceModalId);
//	public List<DeviceModalMaster> findAndViewByDeviceModalId(Integer deviceModalId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<DeviceModalMaster> findAllDeviceModals(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $deviceModalId }}}" })
	public Object findMaxIdInDeviceModal();
	
	@Query(value="{'deviceModalId' : ?0}", delete = true)
	public DeviceModalMaster DeleteDeviceModalById(Integer deviceModalId);


}
