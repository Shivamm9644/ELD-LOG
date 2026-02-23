package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriveringStatusCRUDDto {
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("driverStatusId")
	private String driverStatusId;
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("lastDriverId")
	private long lastDriverId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("isVisible")
	private int isVisible;
	
	@JsonProperty("days")
	private int days;
	
	@JsonProperty("shift")
	private int shift;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("userId")
	private Integer userId;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("isGenerated")
	private Integer isGenerated;
	
}
