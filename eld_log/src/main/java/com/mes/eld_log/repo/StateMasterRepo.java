package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.StateMaster;

@Repository
public interface StateMasterRepo extends MongoRepository<StateMaster, String> {
	
	@Query(value="{ 'stateId' : ?0 }")
	public StateMaster findByStateId(Integer stateId);
	
	@Query(value="{ 'stateId' : ?0}")
	public List<StateMaster> findAndViewByStateId(Integer stateId);
	
	@Query(value="{ 'countryId' : ?0}")
	public List<StateMaster> findAndViewByCountryId(Integer countryId);
	
	@Query(value="{ 'geofanceId' : ?0}")
	public List<StateMaster> findAndViewByGeofanceId(long geofanceId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<StateMaster> findAllStates(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $stateId }}}" })
	public Object findMaxIdInStateMaster();
	
	@Query(value="{'stateId' : ?0}", delete = true)
	public StateMaster DeleteStateById(Integer stateId);

}
