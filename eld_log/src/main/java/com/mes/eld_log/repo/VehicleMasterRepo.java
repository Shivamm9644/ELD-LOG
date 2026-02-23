package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.VehicleMasterViewDto;
import com.mes.eld_log.models.VehicleMaster;

@Repository
public interface VehicleMasterRepo extends MongoRepository<VehicleMaster, String> {
	
	@Query(value="{ 'vehicleId' : ?0 }")
	public VehicleMaster findByVehicleId(Integer vehicleId);
	
	@Query(value="{ 'vehicleId' : ?0,'clientId' : ?1 }")
	public List<VehicleMasterViewDto> findAndViewByVehicleId(Integer vehicleId, long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<VehicleMasterViewDto> findAllVehicles(long clientId);
	
	@Query(value="{ 'clientId' : ?0, 'status' : ?1 }")
	public List<VehicleMasterViewDto> findAllActiveVehicles(long clientId, String status);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $vehicleId }}}" })
	public Object findMaxIdInVehicleMaster();
	
	@Query(value="{'vehicleId' : ?0}", delete = true)
	public VehicleMaster DeleteVehicleById(Integer vehicleId);

}
