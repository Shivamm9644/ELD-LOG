package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriverInfoViewDto {
	
	@JsonProperty("employeeId")
	private Integer employeeId;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("cdlNo")
	private String cdlNo;
	
	@JsonProperty("companyId")
	private long companyId;
	
	@JsonProperty("companyName")
	private String companyName;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("mainTerminalId")
	private long mainTerminalId;
	
	@JsonProperty("mainOfficeAddress")
	private String mainOfficeAddress;
	
	@JsonProperty("homeTerminalAddress")
	private String homeTerminalAddress;
	
	@JsonProperty("timeZone")
	private String timeZone;
	
	@JsonProperty("languageId")
	private long languageId;
	
	@JsonProperty("languageName")
	private String languageName;
	
	@JsonProperty("odometer")
	private long odometer;
	
	@JsonProperty("cdlStateId")
	private long cdlStateId;
	
	@JsonProperty("cdlStateName")
	private String cdlStateName;
	
}
