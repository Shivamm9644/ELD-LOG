package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.models.Login;

@Repository
public interface LoginRepo extends MongoRepository<Login, String> {

	@Query(value="{'username' : ?0,'password' : ?1}") //and condition
	public List<Login> ValidateAndLogin(String username, String password);
	
	@Query(value="{ 'email' : ?0 }")
	public List<Login> ForgotPassword(String email);
	
	@Query(value="{ 'mobileNo' : ?0 }")
	public List<Login> ForgotUsername(long mobileNo);
	
	@Query(value="{ 'email' : ?0 }")
	public UserLoginDto GetLoginDataByEmail(String email);
	
	@Query(value="{ 'employeeId' : ?0 }")
	public UserLoginDto GetLoginDataByEmployeeId(Integer employeeId);
	
	@Query(value="{ 'employeeId' : ?0 },{ 'tokenNo' : ?1 }") //or condition
	public Login ValidateLoginToken(Integer employeeId, String tokenNo);
	
	@Query(value="{'employeeId' : ?0}", delete = true)
	public Login DeleteEmployeeById(Integer employeeId);

}
