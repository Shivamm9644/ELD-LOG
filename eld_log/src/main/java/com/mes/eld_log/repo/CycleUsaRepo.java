package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CycleUsa;

@Repository
public interface CycleUsaRepo extends MongoRepository<CycleUsa, String> {
	
	@Query(value="{ 'cycleUsaId' : ?0 }")
	public CycleUsa findByCycleUsaId(Integer cycleUsaId);
	
	@Query(value="{ 'cycleUsaId' : ?0}")
	public List<CycleUsa> findAndViewByCycleUsaId(Integer cycleUsaId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CycleUsa> findAllCycleUsa(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $cycleUsaId }}}" })
	public Object findMaxIdInCycleUsa();
	
	@Query(value="{'cycleUsaId' : ?0}", delete = true)
	public CycleUsa DeleteCycleUsaById(Integer cycleUsaId);

}
