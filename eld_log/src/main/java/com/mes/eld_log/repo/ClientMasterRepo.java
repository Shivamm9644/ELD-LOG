package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ClientMaster;

@Repository
public interface ClientMasterRepo extends MongoRepository<ClientMaster, String> {
	
	@Query(value="{ 'clientId' : ?0 }")
	public ClientMaster findByClientId(Integer clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<ClientMaster> findAndViewByClientId(Integer clientId);
	
	@Query(value="{ 'companyId' : ?0 }")
	public List<ClientMaster> findAndViewByCompanyId(String companyId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $clientId }}}" })
	public Object findMaxIdInClientMaster();
	
	@Query(value="{'clientId' : ?0}", delete = true)
	public ClientMaster DeleteClientById(Integer clientId);

}
