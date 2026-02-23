package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginUpdateDto {
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("loginDateTime")
	private long loginDateTime;
	
	@JsonProperty("timestamp")
	private long timestamp;	
	
}
