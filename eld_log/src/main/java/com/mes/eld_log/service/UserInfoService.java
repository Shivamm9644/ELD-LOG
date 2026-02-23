package com.mes.eld_log.service;
import java.util.List;
import java.util.Map;

import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.LoginUpdateDto;
import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.dtos.UserMasterViewDto;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.UserMaster;
import com.mes.eld_log.results.ResultWrapper;
import com.fasterxml.jackson.core.JsonGenerator;

public interface UserInfoService {
	public ResultWrapper<EmployeeMasterCRUDDto> Login(UserLoginDto userLoginDto, String token);	
	public ResultWrapper<EmployeeMasterCRUDDto> LoginDataByEmployeeId(UserLoginDto userLoginDto);	

	public ResultWrapper<String> UpdateLoginWithLoginLog(LoginUpdateDto loginUpdateDto);	

	public ResultWrapper<EmployeeMasterCRUDDto> LoginByDate(UserLoginDto userLoginDto, String token);	

	public ResultWrapper<List<UserMasterViewDto>> LoginWeb(UserLoginDto userLoginDto, String token);

	public ResultWrapper<UserLoginDto> ForgotPassword(UserLoginDto userLoginDto);
	public ResultWrapper<UserLoginDto> ForgotUsername(UserLoginDto userLoginDto);
	public ResultWrapper<UserLoginDto> LogoutApi(UserLoginDto userLoginDto, String tokenValid);
	public ResultWrapper<String> LogoutWebApi(UserLoginDto userLoginDto);

	public ResultWrapper<String> ValidateTokenNo(String tokenValid);


}
