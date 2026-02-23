package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.models.UserTypeMaster;

@Repository
public interface UserTypeMasterRepo extends MongoRepository<UserTypeMaster, String> {
	
	@Query(value="{ 'userTypeId' : ?0 }")
	public UserTypeMaster findByUserTypeId(Integer userTypeId);
	
	@Query(value="{ 'userTypeId' : ?0}")
	public List<UserTypeMaster> findAndViewByUserTypeId(Integer userTypeId);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<UserTypeMaster> findAllUserTypes(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $userTypeId }}}" })
	public Object findMaxIdInUserTypeMaster();
	
	@Query(value="{'userTypeId' : ?0}", delete = true)
	public UserTypeMaster DeleteUserTypeById(Integer userTypeId);

}
