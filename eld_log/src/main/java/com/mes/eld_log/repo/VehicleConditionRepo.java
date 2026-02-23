package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.VehicleCondition;

@Repository
public interface VehicleConditionRepo extends MongoRepository<VehicleCondition, String> {
	
	@Query(value="{ 'vehicleConditionId' : ?0 }")
	public VehicleCondition findByVehicleConditionId(Integer vehicleConditionId);
	
	@Query(value="{ 'vehicleConditionId' : ?0,'clientId' : ?1}")
	public List<VehicleCondition> findAndViewByVehicleConditionId(Integer vehicleConditionId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<VehicleCondition> findAllVehicleConditions(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $vehicleConditionId }}}" })
	public Object findMaxIdInVehicleConditionMaster();
	
	@Query(value="{'vehicleConditionId' : ?0}", delete = true)
	public VehicleCondition DeleteVehicleConditionById(Integer vehicleConditionId);

}
