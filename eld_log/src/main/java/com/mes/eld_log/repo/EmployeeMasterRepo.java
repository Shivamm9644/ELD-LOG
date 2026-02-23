package com.mes.eld_log.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mes.eld_log.dtos.DriverInfoViewDto;
import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.EmployeeMasterListViewDto;
import com.mes.eld_log.dtos.EmployeeMasterViewDto;
import com.mes.eld_log.models.EmployeeMaster;

@Repository
public interface EmployeeMasterRepo extends MongoRepository<EmployeeMaster, String> {
	
	@Query(value="{ 'employeeId' : ?0 }")
	public EmployeeMaster findByEmployeeId(Integer employeeId);
	
	@Query(value="{ 'email' : ?0 }")
	public EmployeeMaster findEmployeeByEmail(String email);
	
	@Query(value="{ 'email' : ?0 }")
	public EmployeeMasterCRUDDto findEmployeeByEmail1(String email);
	
	@Query(value="{ 'employeeId' : ?0 }")
	public EmployeeMasterCRUDDto findEmployeeByEmployeeId1(Integer employeeId);
	
	@Query(value="{ 'employeeId' : ?0, 'clientId' : ?1 }")
	public List<EmployeeMasterViewDto> findAndViewByEmployeeId(Integer employeeId, long clientId);
	
	@Query(value="{ 'isFirstLogin' : ?0, 'clientId' : ?1 }")
	public List<EmployeeMasterViewDto> findAndViewByEmployeeIdAndFirstLogin(String isFirstLogin, long clientId);
	
	@Query(value="{ 'employeeId' : ?0 }")
	public List<DriverInfoViewDto> findAndViewByDriverId(Integer employeeId);
	
	@Query(value="{}")
	public List<DriverInfoViewDto> findAndViewByDrivers();
	
	@Query(value="{ 'clientId' : ?0}",sort = "{'addedTimestamp': -1}")
	public List<EmployeeMasterViewDto> findAllEmployees(long clientId);
	
	@Query(value="{ 'clientId' : ?0}",sort = "{'addedTimestamp': -1}")
	public List<EmployeeMasterListViewDto> findAllEmployeeByClient(long clientId);
	
	@Aggregation(pipeline = { "{$group: { _id: '', maxID: {$max: $employeeId }}}" })
	public Object findMaxIdInEmployeeMaster();
	
	@Query(value="{'employeeId' : ?0}", delete = true)
	public EmployeeMaster DeleteEmployeeById(Integer employeeId);
	
	boolean existsByEmail(String email);
    boolean existsByMobileNo(long mobileNo);

}
