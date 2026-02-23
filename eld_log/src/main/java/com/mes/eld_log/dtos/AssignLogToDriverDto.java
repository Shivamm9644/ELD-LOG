package com.mes.eld_log.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AssignLogToDriverDto {
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("hour")
	private Integer hour;
	
	@JsonProperty("logShift")
	private String logShift;
	
	@JsonProperty("checkDvir")
	private String checkDvir;
	
	@JsonProperty("checkCertified")
	private String checkCertified;
	
	@JsonProperty("logStatusData")
	private List<DriveringStatusLogViewDto> logStatusData;
	
}
