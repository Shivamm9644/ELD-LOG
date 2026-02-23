package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginLogViewDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("employeeId")
	private Integer employeeId;
	
	@JsonProperty("userId")
	private Integer userId;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("loginDateTime")
	private long loginDateTime;
	
	@JsonProperty("logoutDateTime")
	private long logoutDateTime;
	
	@JsonProperty("receivedTimestamp")
	private long receivedTimestamp;
	
	@JsonProperty("isCoDriver")
	private String isCoDriver;
	
}
