package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.PaymentStatusMaster;

@Repository
public interface PaymentStatusMasterRepo extends MongoRepository<PaymentStatusMaster, String> {
	
	@Query(value="{ 'paymentStatusId' : ?0 }")
	public PaymentStatusMaster findByPaymentStatusId(Integer paymentStatusId);
	
	@Query(value="{ 'paymentStatusId' : ?0}")
	public List<PaymentStatusMaster> findAndViewByPaymentStatusId(Integer paymentStatusId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<PaymentStatusMaster> findAllPaymentStatus(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $paymentStatusId }}}" })
	public Object findMaxIdInPaymentStatusMaster();
	
	@Query(value="{'paymentStatusId' : ?0}", delete = true)
	public PaymentStatusMaster DeletePaymentStatusById(Integer paymentStatusId);

}
