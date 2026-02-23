package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.TimezoneMaster;

@Repository
public interface TimezoneMasterRepo extends MongoRepository<TimezoneMaster, String> {
	
	@Query(value="{ 'timezoneId' : ?0 }")
	public TimezoneMaster findByTimezoneId(Integer timezoneId);
	
	@Query(value="{ 'timezoneId' : ?0}")
	public List<TimezoneMaster> findAndViewByTimezoneId(Integer timezoneId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<TimezoneMaster> findAllTimezones(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $timezoneId }}}" })
	public Object findMaxIdInTimezoneMaster();
	
	@Query(value="{'timezoneId' : ?0}", delete = true)
	public TimezoneMaster DeleteTimezoneById(Integer timezoneId);

}
