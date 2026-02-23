package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.DefectMaster;

@Repository
public interface DefectMasterRepo extends MongoRepository<DefectMaster, String> {
	
	@Query(value="{ 'defectId' : ?0 }")
	public DefectMaster findByDefectMasterId(Integer defectId);
	
	@Query(value="{ 'defectId' : ?0}")
	public List<DefectMaster> findAndViewByDefectMasterId(Integer defectId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<DefectMaster> findAllDefectMaster(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $defectId }}}" })
	public Object findMaxIdInDefectMaster();
	
	@Query(value="{'defectId' : ?0}", delete = true)
	public DefectMaster DeleteDefectMasterById(Integer defectId);

}
