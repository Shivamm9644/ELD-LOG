package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CargoTypeMaster;

@Repository
public interface CargoTypeMasterRepo extends MongoRepository<CargoTypeMaster, String> {
	
	@Query(value="{ 'cargoTypeId' : ?0 }")
	public CargoTypeMaster findByCargoTypeId(Integer cargoTypeId);
	
	@Query(value="{ 'cargoTypeId' : ?0 }")
	public List<CargoTypeMaster> findAndViewByCargoTypeId(Integer cargoTypeId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CargoTypeMaster> findAndllCargoTypes(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $cargoTypeId }}}" })
	public Object findMaxIdInCargoTypeMaster();
	
	@Query(value="{'cargoTypeId' : ?0}", delete = true)
	public CargoTypeMaster DeleteCargoTypeById(Integer cargoTypeId);

}
