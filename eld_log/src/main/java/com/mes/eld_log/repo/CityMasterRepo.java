package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CityMaster;

@Repository
public interface CityMasterRepo extends MongoRepository<CityMaster, String> {
	
	@Query(value="{ 'cityId' : ?0 }")
	public CityMaster findByCityId(Integer cityId);
	
	@Query(value="{ 'cityId' : ?0}")
	public List<CityMaster> findAndViewByCityId(Integer cityId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CityMaster> findAllCities(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $cityId }}}" })
	public Object findMaxIdInCityMaster();
	
	@Query(value="{'cityId' : ?0}", delete = true)
	public CityMaster DeleteCityById(Integer cityId);

}
