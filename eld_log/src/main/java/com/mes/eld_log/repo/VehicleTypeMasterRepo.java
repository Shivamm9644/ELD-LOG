package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.VehicleTypeMaster;

@Repository
public interface VehicleTypeMasterRepo extends MongoRepository<VehicleTypeMaster, String> {
	
	@Query(value="{ 'vehicleTypeId' : ?0 }")
	public VehicleTypeMaster findByVehicleTypeId(Integer vehicleTypeId);
	
	@Query(value="{ 'vehicleTypeId' : ?0, 'clientId' : ?1 }")
	public List<VehicleTypeMaster> findAndViewByVehicleTypeId(Integer vehicleTypeId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<VehicleTypeMaster> findAllVehicleTypes(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $vehicleTypeId }}}" })
	public Object findMaxIdInVehicleTypeMaster();
	
	@Query(value="{'vehicleTypeId' : ?0}", delete = true)
	public VehicleTypeMaster DeleteVehicleTypeById(Integer vehicleTypeId);

}
