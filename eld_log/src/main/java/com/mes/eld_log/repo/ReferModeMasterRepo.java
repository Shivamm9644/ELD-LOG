package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ReferModeMaster;

@Repository
public interface ReferModeMasterRepo extends MongoRepository<ReferModeMaster, String> {
	
	@Query(value="{ 'referModeId' : ?0 }")
	public ReferModeMaster findByReferModeId(Integer referModeId);
	
	@Query(value="{ 'referModeId' : ?0}")
	public List<ReferModeMaster> findAndViewByReferModeId(Integer referModeId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<ReferModeMaster> findAllReferMode(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $referModeId }}}" })
	public Object findMaxIdInReferModeMaster();
	
	@Query(value="{'referModeId' : ?0}", delete = true)
	public ReferModeMaster DeleteReferModeById(Integer referModeId);

}
