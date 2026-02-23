package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ReceiverMaster;

@Repository
public interface ReceiverMasterRepo extends MongoRepository<ReceiverMaster, String> {
	
	@Query(value="{ 'receiverId' : ?0 }")
	public ReceiverMaster findByReceiverId(Integer receiverId);
	
	@Query(value="{ 'receiverId' : ?0, 'clientId' : ?1 }")
	public List<ReceiverMaster> findAndViewByReceiverId(Integer receiverId, long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<ReceiverMaster> findAllReceivers(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $receiverId }}}" })
	public Object findMaxIdInReceiverMaster();
	
	@Query(value="{'receiverId' : ?0}", delete = true)
	public ReceiverMaster DeleteReceiverById(Integer receiverId);

}
