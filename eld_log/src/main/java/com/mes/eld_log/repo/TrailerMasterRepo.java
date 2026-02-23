package com.mes.eld_log.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.TrailerMaster;

@Repository
public interface TrailerMasterRepo extends MongoRepository<TrailerMaster, String> {
	
	@Query(value="{ 'trailerId' : ?0 }")
	public TrailerMaster findByTrailerId(Integer trailerId);
	
	@Query(value="{ 'trailerId' : ?0,'clientId' : ?1 }")
	public List<TrailerMaster> findAndViewByTrailerId(Integer trailerId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<TrailerMaster> findAllTrailers(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $trailerId }}}" })
	public Object findMaxIdInTrailerMaster();
	
	@Query(value="{'trailerId' : ?0}", delete = true)
	public TrailerMaster DeleteTrailerById(Integer trailerId);


}
