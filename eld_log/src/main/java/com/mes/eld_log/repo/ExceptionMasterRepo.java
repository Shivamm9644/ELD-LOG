package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ExceptionMaster;

@Repository
public interface ExceptionMasterRepo extends MongoRepository<ExceptionMaster, String> {
	
	@Query(value="{ 'exceptionId' : ?0 }")
	public ExceptionMaster findByExceptionId(Integer exceptionId);
	
	@Query(value="{ 'exceptionId' : ?0}")
	public List<ExceptionMaster> findAndViewByExceptionId(Integer exceptionId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<ExceptionMaster> findAllExceptions(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $exceptionId }}}" })
	public Object findMaxIdInExceptionMaster();
	
	@Query(value="{'exceptionId' : ?0}", delete = true)
	public ExceptionMaster DeleteExceptionById(Integer exceptionId);

}
