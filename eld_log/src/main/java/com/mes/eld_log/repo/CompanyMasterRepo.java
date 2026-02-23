package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CompanyMaster;

@Repository
public interface CompanyMasterRepo extends MongoRepository<CompanyMaster, String> {
	
	@Query(value="{ 'companyId' : ?0 }")
	public CompanyMaster findByCompanyId(Integer companyId);
	
	@Query(value="{ 'companyId' : ?0}")
	public List<CompanyMaster> findAndViewByCompanyId(Integer companyId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CompanyMaster> findAllCompanies(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $companyId }}}" })
	public Object findMaxIdInCompanyMaster();
	
	@Query(value="{'companyId' : ?0}", delete = true)
	public CompanyMaster DeleteCompanyById(Integer companyId);

}
