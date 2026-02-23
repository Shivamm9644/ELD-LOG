package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.ReceiverDataViewDto;
import com.mes.eld_log.dtos.ShipperDataViewDto;
import com.mes.eld_log.models.ShipperReceiverDetails;

@Repository
public interface ShipperReceiverDetailsRepo extends MongoRepository<ShipperReceiverDetails, String> {
	
	@Query(value="{ 'dispatchId' : ?0 }")
	public ShipperReceiverDetails findByDispatchId(String dispatchId);
	
	@Query(value="{ 'dispatchId' : ?0 }")
	public List<ShipperReceiverDetails> findByDispatchSRId(String dispatchId);
	
	@Query(value="{ 'dispatchId' : ?0, 'shipperId' : ?1 }")
	public List<ShipperDataViewDto> findByDispatchIdOfShipperData(String dispatchId, long shipperId);
	
	@Query(value="{ 'dispatchId' : ?0,'receiverId' : ?1 }")
	public List<ReceiverDataViewDto> findByDispatchIdOfReceiverData(String dispatchId, long receiverId);
}
