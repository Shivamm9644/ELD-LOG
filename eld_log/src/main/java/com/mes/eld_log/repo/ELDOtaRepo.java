package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ELDOta;

@Repository
public interface ELDOtaRepo extends MongoRepository<ELDOta, String> {
	
	@Query(value="{'otaId' : ?0}", delete = true)
	public ELDOta deleteELDOtaById(Integer otaId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $otaId }}}" })
	public Object findMaxIdInELDOta();
	
}
