package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExceptionMasterCRUDDto {
	
	@JsonProperty("exceptionId")
	private Integer exceptionId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("tokenNo")
	private String tokenNo;
}
