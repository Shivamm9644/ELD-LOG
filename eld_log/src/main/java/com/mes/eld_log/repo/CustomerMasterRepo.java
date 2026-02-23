package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.CustomerMaster;

@Repository
public interface CustomerMasterRepo extends MongoRepository<CustomerMaster, String> {
	
	@Query(value="{ 'customerId' : ?0 }")
	public CustomerMaster findByCustomerId(Integer customerId);
	
	@Query(value="{ 'customerId' : ?0, 'clientId' : ?1 }")
	public List<CustomerMaster> findAndViewByCustomerId(Integer customerId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<CustomerMaster> findAllCustomers(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $customerId }}}" })
	public Object findMaxIdInCustomerMaster();
	
	@Query(value="{'customerId' : ?0}", delete = true)
	public CustomerMaster DeleteCustomerById(Integer customerId);

}
