package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.DefectDetailCRUDDto;
import com.mes.eld_log.models.DefectDetails;

@Repository
public interface DefectDetailsRepo extends MongoRepository<DefectDetails, String> {
	
	@Query(value="{'driverId' : ?0, 'lDateTime' : ?1}")
	public DefectDetailCRUDDto findAndViewDefectDataByDriverId(long driverId, long lDateTime);
	
	@Query(value="{'dvirId' : ?0}")
	public List<DefectDetailCRUDDto> findAndViewDefectDataByDvirId(String dvirId);
	
	@Query(value="{'dvirId' : ?0}", delete = true)
	public long DeleteAllDVIRDataByDriverIdAndDate(String dvirId);
	
}
