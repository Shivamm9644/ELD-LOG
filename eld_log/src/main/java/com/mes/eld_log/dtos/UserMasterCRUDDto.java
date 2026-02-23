package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserMasterCRUDDto {
	
	@JsonProperty("userId")
	private Integer userId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("graceTime")
	private String graceTime;
	
	@JsonProperty("webAccess")
	private String webAccess;
	
	@JsonProperty("mobileAccess")
	private String mobileAccess;
	
	@JsonProperty("status")
	private String status;
	
}
