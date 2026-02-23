package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.FuelTypeMaster;

@Repository
public interface FuelTypeMasterRepo extends MongoRepository<FuelTypeMaster, String> {
	
	@Query(value="{ 'fuelTypeId' : ?0 }")
	public FuelTypeMaster findByFuelTypeId(Integer fuelTypeId);
	
	@Query(value="{ 'fuelTypeId' : ?0}")
	public List<FuelTypeMaster> findAndViewByFuelTypeId(Integer fuelTypeId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<FuelTypeMaster> findAllFuelTypes(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $fuelTypeId }}}" })
	public Object findMaxIdInFuelTypeMaster();
	
	@Query(value="{'fuelTypeId' : ?0}", delete = true)
	public FuelTypeMaster DeleteFuelTypeById(Integer fuelTypeId);


}
