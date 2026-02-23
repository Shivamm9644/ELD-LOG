package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ELDOtaStatus;

@Repository
public interface ELDOtaStatusRepo extends MongoRepository<ELDOtaStatus, String> {
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $otaStatusId }}}" })
	public Object findMaxIdInELDOtaStatus();
	
}
