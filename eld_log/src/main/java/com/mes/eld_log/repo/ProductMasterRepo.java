package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.ProductMaster;

@Repository
public interface ProductMasterRepo extends MongoRepository<ProductMaster, String> {
	
	@Query(value="{ 'productId' : ?0 }")
	public ProductMaster findByProductId(Integer productId);
	
	@Query(value="{ 'productId' : ?0,'clientId' : ?1 }")
	public List<ProductMaster> findAndViewByProductId(Integer productId,long clientId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<ProductMaster> findAllProducts(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $productId }}}" })
	public Object findMaxIdInProductMaster();
	
	@Query(value="{'productId' : ?0}", delete = true)
	public ProductMaster DeleteProductById(Integer productId);

}
