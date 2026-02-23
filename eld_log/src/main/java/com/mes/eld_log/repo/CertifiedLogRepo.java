package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.CertifiedLogViewDto;
import com.mes.eld_log.models.CertifiedLog;

@Repository
public interface CertifiedLogRepo extends MongoRepository<CertifiedLog, String> {
	
	@Query(value="{'driverId' : ?0}", count = true)
	public Integer CountCertifiedLogByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0}", delete = true)
	public CertifiedLog deleteAllCertifiedLogByDriverId(long driverId);
	
	@Query(value="{'driverId' : ?0,'lCertifiedDate' : { $gte: ?1, $lte: ?2 }}", delete = true)
	public long deleteAllCertifiedLogByDriverIdAndDate(long driverId,long startTimestamp, long endTimestamp);
	
	@Query(value="{'driverId' : ?0, 'lCertifiedDate' : ?1}")
	public CertifiedLogViewDto findAndViewCertifiedLogByDriverId(long driverId, long lCertifiedDate);
	
}
