package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.RouteMaster;

@Repository
public interface RouteMasterRepo extends MongoRepository<RouteMaster, String> {
	
	@Query(value="{ 'routeId' : ?0 }")
	public RouteMaster findByRouteId(Integer routeId);
	
	@Query(value="{ 'routeId' : ?0,'clientId' : ?1 }")
	public List<RouteMaster> findAndViewByRouteId(Integer routeId, long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<RouteMaster> findAllRoutes(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $routeId }}}" })
	public Object findMaxIdInRouteMaster();
	
	@Query(value="{'routeId' : ?0}", delete = true)
	public RouteMaster DeleteRouteById(Integer routeId);

}
