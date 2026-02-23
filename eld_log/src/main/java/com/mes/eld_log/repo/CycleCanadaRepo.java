package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CycleCanada;

@Repository
public interface CycleCanadaRepo extends MongoRepository<CycleCanada, String> {
	
	@Query(value="{ 'cycleCanadaId' : ?0 }")
	public CycleCanada findByCycleCanadaId(Integer cycleCanadaId);
	
	@Query(value="{ 'cycleCanadaId' : ?0}")
	public List<CycleCanada> findAndViewByCycleCanadaId(Integer cycleUsaId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CycleCanada> findAllCycleCanada(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $cycleCanadaId }}}" })
	public Object findMaxIdInCycleCanada();
	
	@Query(value="{'cycleCanadaId' : ?0}", delete = true)
	public CycleCanada DeleteCycleCanadaById(Integer cycleCanadaId);

}
