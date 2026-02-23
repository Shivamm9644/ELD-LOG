package com.mes.eld_log.controller;

import java.io.UnsupportedEncodingException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.results.ResultWrapper;
import com.mes.eld_log.security.TokenUtil;
import com.mes.eld_log.service.SystemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//import jdk.jfr.ContentType;

@RestController
//@RequestMapping("/service")
@RequestMapping(value ="/service", method = RequestMethod.POST)
public class SystemServiceController {
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private SystemService systemService;
	
	@PostMapping(value="/driver_log_service")
	public ResponseEntity<ResultWrapper<String>> DriverLogService()
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = systemService.DriverLogService();
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/ifta_report_service")
	public ResponseEntity<ResultWrapper<String>> IftaReportService()
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = systemService.IftaReportService();
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/report_generated_service")
	public ResponseEntity<ResultWrapper<String>> ReportGeneratedService()
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = systemService.ReportGeneratedService();
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/import_and_read_json_file")
	public ResponseEntity<ResultWrapper<String>> ImportAndReadJsonFile(@RequestParam("file") List<MultipartFile> file)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = systemService.ImportAndReadJsonFile(file);
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
