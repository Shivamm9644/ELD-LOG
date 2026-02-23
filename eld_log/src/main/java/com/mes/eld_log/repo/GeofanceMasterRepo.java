package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.GeofanceMaster;


@Repository
public interface GeofanceMasterRepo extends MongoRepository<GeofanceMaster, String> {
	
	@Query(value = "{'latLng':{$geoIntersects:{$geometry:{type:'Point',coordinates:?0}}}}", fields = "{'stateName':1, 'type':1, 'stateId':1, 'geoId':1 }")
	GeofanceMaster findByAreaIntersects(double[] point); 

	@Query(value="{ 'geoId' : ?0 }")
	public List<GeofanceMaster> findGeofanceById(Integer geoId);
	
	@Query(value="{ 'geoId' : ?0 }")
	public GeofanceMaster findAndViewGeofanceById(Integer geoId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $geoId }}}" })
	public Object findMaxIdInGeofances();
	
	@Query(value="{'geoId' : ?0}", delete = true)
	public GeofanceMaster DeleteGeofanceById(Integer geoId);
}

