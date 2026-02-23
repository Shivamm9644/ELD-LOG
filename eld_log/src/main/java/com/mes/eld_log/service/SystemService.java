package com.mes.eld_log.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mes.eld_log.results.ResultWrapper;

public interface SystemService {
	public ResultWrapper<String> DriverLogService();	
	public ResultWrapper<String> IftaReportService();	
	public ResultWrapper<String> ReportGeneratedService();	

	public ResultWrapper<String> ImportAndReadJsonFile(List<MultipartFile> file);

}
