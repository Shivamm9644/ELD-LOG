package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserLoginDto {
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("employeeId")
	private Integer employeeId;
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("loginDateTime")
	private long loginDateTime;
	
	@JsonProperty("logoutDateTime")
	private long logoutDateTime;
	
	@JsonProperty("mobileDeviceId")
	private String mobileDeviceId;
	
	@JsonProperty("isCoDriver")
	private String isCoDriver;
	
//	@JsonProperty("appVersion")
//	private String appVersion;
//	
//	@JsonProperty("osVersion")
//	private String osVersion;
	
	
}
