package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.UserMasterViewDto;
import com.mes.eld_log.models.UserMaster;

@Repository
public interface UserMasterRepo extends MongoRepository<UserMaster, String> {
	
	@Query(value="{ 'userId' : ?0 }")
	public UserMaster findByUserId(Integer userId);
	
	@Query(value="{ 'userId' : ?0}")
	public List<UserMaster> findAndViewByUserId(Integer userId);
	
	@Query(value="{ 'email' : ?0,'password' : ?1 }")
	public List<UserMasterViewDto> findAndViewByEmailAndPassword(String email,String password);
	
	@Query(value="{ 'clientId' : ?0 }")
	public List<UserMaster> findAllUsers(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $userId }}}" })
	public Object findMaxIdInUserMaster();
	
	@Query(value="{'userId' : ?0}", delete = true)
	public UserMaster DeleteUserById(Integer userId);

}
