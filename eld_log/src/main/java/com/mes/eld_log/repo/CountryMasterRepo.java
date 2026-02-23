package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CountryMaster;

@Repository
public interface CountryMasterRepo extends MongoRepository<CountryMaster, String> {
	
	@Query(value="{ 'countryId' : ?0 }")
	public CountryMaster findByCountryId(Integer countryId);
	
	@Query(value="{ 'countryId' : ?0}")
	public List<CountryMaster> findAndViewByCountryId(Integer countryId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CountryMaster> findAllCountries(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $countryId }}}" })
	public Object findMaxIdInCountryMaster();
	
	@Query(value="{'countryId' : ?0}", delete = true)
	public CountryMaster DeleteCountryById(Integer countryId);

}
