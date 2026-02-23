package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.RestBreakMaster;

@Repository
public interface RestBreakMasterRepo extends MongoRepository<RestBreakMaster, String> {
	
	@Query(value="{ 'restBreakId' : ?0 }")
	public RestBreakMaster findByRestBreakMasterId(Integer restBreakId);
	
	@Query(value="{ 'restBreakId' : ?0}")
	public List<RestBreakMaster> findAndViewByRestBreakMasterId(Integer restBreakId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<RestBreakMaster> findAllRestBreakMaster(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $restBreakId }}}" })
	public Object findMaxIdInRestBreakMaster();
	
	@Query(value="{'restBreakId' : ?0}", delete = true)
	public RestBreakMaster DeleteRestBreakMasterById(Integer restBreakId);

}
