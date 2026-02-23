package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.IFTAReports;

@Repository
public interface IFTAReportsRepo extends MongoRepository<IFTAReports, String> {
	
	@Query(value="{ 'vehicleId' : ?0, 'fromDate' : ?1, 'toDate' : ?2 }")
	public List<IFTAReports> findAndViewIftaReport(String vehicleId, String fromDate, String toDate);
	
	@Query(value="{ 'clientId' : ?0}")
	public List<IFTAReports> findAndViewIftaReportByClient(long clientId);
}

