package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.MACAddressMaster;

@Repository
public interface MACAddressMasterRepo extends MongoRepository<MACAddressMaster, String> {
	
	@Query(value="{ 'driverId' : ?0 }")
	public MACAddressMaster findByMACAddressMasterId(long driverId);
	
	@Query(value="{ 'vehicleId' : ?0 }")
	public List<MACAddressMaster> findByMACAddressMasterByVehicleId(long vehicleId);
	
	@Query(value="{ 'driverId' : ?0}")
	public List<MACAddressMaster> findAndViewByMACAddressMasterId(long driverId);
	

}
