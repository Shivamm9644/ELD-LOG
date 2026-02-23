package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ShipperMaster;

@Repository
public interface ShipperMasterRepo extends MongoRepository<ShipperMaster, String> {
	
	@Query(value="{ 'shipperId' : ?0 }")
	public ShipperMaster findByShipperId(Integer shipperId);
	
	@Query(value="{ 'shipperId' : ?0,'clientId' : ?1 }")
	public List<ShipperMaster> findAndViewByShipperId(Integer shipperId, long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<ShipperMaster> findAllShippers(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $shipperId }}}" })
	public Object findMaxIdInShipperMaster();
	
	@Query(value="{'shipperId' : ?0}", delete = true)
	public ShipperMaster DeleteShipperById(Integer shipperId);

}
