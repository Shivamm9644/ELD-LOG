package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.OtherChargeViewDto;
import com.mes.eld_log.models.OtherCharges;

@Repository
public interface OtherChargesRepo extends MongoRepository<OtherCharges, String> {
	
	@Query(value="{ 'dispatchId' : ?0 }")
	public List<OtherChargeViewDto> findAndViewByDispatchId(String dispatchId);

}
