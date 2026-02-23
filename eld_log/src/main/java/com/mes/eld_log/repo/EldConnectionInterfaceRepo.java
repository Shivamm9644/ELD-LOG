package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.EldConnectionInterface;

@Repository
public interface EldConnectionInterfaceRepo extends MongoRepository<EldConnectionInterface, String> {
	
	@Query(value="{ 'eldConnectionInterfaceId' : ?0 }")
	public EldConnectionInterface findByEldConnectionInterfaceId(Integer eldConnectionInterfaceId);
	
	@Query(value="{ 'eldConnectionInterfaceId' : ?0}")
	public List<EldConnectionInterface> findAndViewByEldConnectionInterfaceId(Integer eldConnectionInterfaceId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<EldConnectionInterface> findAllEldConnectionInterface(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $eldConnectionInterfaceId }}}" })
	public Object findMaxIdInEldConnectionInterface();
	
	@Query(value="{'eldConnectionInterfaceId' : ?0}", delete = true)
	public EldConnectionInterface DeleteEldConnectionInterfaceById(Integer eldConnectionInterfaceId);

}
