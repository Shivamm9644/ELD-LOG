package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.DispatchDetailViewDto;
import com.mes.eld_log.models.DispatchDetails;

@Repository
public interface DispatchDetailsRepo extends MongoRepository<DispatchDetails, String> {
	
	@Query(value="{ 'addedTimestamp' : ?0 }")
	public DispatchDetailViewDto findDispatchDetailsByTimestamp(long addedTimestamp);
	
	@Query(value="{ 'addedTimestamp' : ?0 }")
	public List<DispatchDetailViewDto> findAndViewDispatchDetailsByTimestamp(long addedTimestamp);
	
	@Query(value="{'clientId' : ?0}")
	public List<DispatchDetailViewDto> findAllDispatchDetails(long clientId);
	
	@Query(value="{ '_id' : ?0, 'clientId' : ?1 }")
	public List<DispatchDetailViewDto> findAndViewDispatchDetailsById(String _id, long clientId);
	
}
