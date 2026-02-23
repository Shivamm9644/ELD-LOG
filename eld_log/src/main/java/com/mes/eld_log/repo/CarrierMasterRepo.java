package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CarrierMaster;

@Repository
public interface CarrierMasterRepo extends MongoRepository<CarrierMaster, String> {
	
	@Query(value="{ 'carrierId' : ?0 }")
	public CarrierMaster findByCarrierId(Integer carrierId);
	
	@Query(value="{ 'carrierId' : ?0, 'clientId' : ?1 }")
	public List<CarrierMaster> findAndViewByCarrierId(Integer carrierId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CarrierMaster> findAllCarriers(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $carrierId }}}" })
	public Object findMaxIdInCarrierMaster();
	
	@Query(value="{'carrierId' : ?0}", delete = true)
	public CarrierMaster DeleteCarrierById(Integer carrierId);

}
