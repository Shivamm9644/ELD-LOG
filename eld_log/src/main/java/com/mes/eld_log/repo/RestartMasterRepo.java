package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.RestartMaster;

@Repository
public interface RestartMasterRepo extends MongoRepository<RestartMaster, String> {
	
	@Query(value="{ 'restartId' : ?0 }")
	public RestartMaster findBRestartMasterId(Integer restartId);
	
	@Query(value="{ 'restartId' : ?0}")
	public List<RestartMaster> findAndViewByRestartMasterId(Integer restartId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<RestartMaster> findAllRestartMaster(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $restartId }}}" })
	public Object findMaxIdInRestartMaster();
	
	@Query(value="{'restartId' : ?0}", delete = true)
	public RestartMaster DeleteRestartMasterById(Integer restartId);

}
