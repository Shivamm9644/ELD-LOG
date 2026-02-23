package com.mes.eld_log.controller;

import java.io.UnsupportedEncodingException;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.LoginUpdateDto;
import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.dtos.UserMasterViewDto;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.UserMaster;
import com.mes.eld_log.results.ResultWrapper;
import com.mes.eld_log.security.TokenUtil;
import com.mes.eld_log.service.UserInfoService;
import com.mes.eld_log.util.eldLogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//import jdk.jfr.ContentType;

@RestController
//@RequestMapping("/auth")
@RequestMapping(value ="/auth", method = RequestMethod.POST)
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private eldLogUtils imobilityUtils;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@PostMapping(value="/login")
	public ResponseEntity<ResultWrapper<EmployeeMasterCRUDDto>> Login(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		
//		org.springframework.security.core.Authentication authentication = null;
//		authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
//		
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		final String token = tokenUtil.generateToken(authentication);
		
		final String token = UUID.randomUUID().toString();
		
		ResultWrapper<EmployeeMasterCRUDDto> result = null;	
		result = userInfoService.Login(userLoginDto,token);
		return new ResponseEntity<ResultWrapper<EmployeeMasterCRUDDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/login_data_by_employee_id")
	public ResponseEntity<ResultWrapper<EmployeeMasterCRUDDto>> LoginDataByEmployeeId(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		
		ResultWrapper<EmployeeMasterCRUDDto> result = null;	
		result = userInfoService.LoginDataByEmployeeId(userLoginDto);
		return new ResponseEntity<ResultWrapper<EmployeeMasterCRUDDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_login_with_login_log")
	public ResponseEntity<ResultWrapper<String>> UpdateLoginWithLoginLog(@Valid @RequestBody LoginUpdateDto loginUpdateDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = userInfoService.UpdateLoginWithLoginLog(loginUpdateDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/login_by_date")
	public ResponseEntity<ResultWrapper<EmployeeMasterCRUDDto>> LoginByDate(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		final String token = UUID.randomUUID().toString();
		ResultWrapper<EmployeeMasterCRUDDto> result = null;	
		result = userInfoService.LoginByDate(userLoginDto,token);
		return new ResponseEntity<ResultWrapper<EmployeeMasterCRUDDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/login_web")
	public ResponseEntity<ResultWrapper<List<UserMasterViewDto>>> LoginWeb(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<UserMasterViewDto>> result = null;	
		final String token = UUID.randomUUID().toString();
		result = userInfoService.LoginWeb(userLoginDto,token);
		return new ResponseEntity<ResultWrapper<List<UserMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/forgot_password")
	public ResponseEntity<ResultWrapper<UserLoginDto>> ForgotPassword(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserLoginDto> result = null;	
		result = userInfoService.ForgotPassword(userLoginDto);
		return new ResponseEntity<ResultWrapper<UserLoginDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/forgot_username")
	public ResponseEntity<ResultWrapper<UserLoginDto>> ForgotUsername(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserLoginDto> result = null;	
		result = userInfoService.ForgotUsername(userLoginDto);
		return new ResponseEntity<ResultWrapper<UserLoginDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/logout_api")
	public ResponseEntity<ResultWrapper<UserLoginDto>> LogoutApi(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserLoginDto> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)userLoginDto.getEmployeeId(),userLoginDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = userInfoService.LogoutApi(userLoginDto,tokenValid);
		return new ResponseEntity<ResultWrapper<UserLoginDto>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/logout_web_api")
	public ResponseEntity<ResultWrapper<String>> LogoutWebApi(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = userInfoService.LogoutWebApi(userLoginDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/validate_token_no")
	public ResponseEntity<ResultWrapper<String>> ValidateTokenNo(@Valid @RequestBody UserLoginDto userLoginDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)userLoginDto.getEmployeeId(),userLoginDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = userInfoService.ValidateTokenNo(tokenValid);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
		
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
	   Map<String, String> errors = new HashMap<>();
	 
	   ex.getBindingResult().getFieldErrors().forEach(error ->
	           errors.put(error.getField(), error.getDefaultMessage()));
	 
	   return errors;
	}
	

}
