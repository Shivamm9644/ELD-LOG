package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriverWorkingStatusViewDto {
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("driverName")
	private String driverName;

	@JsonProperty("fromOnDuty")
	private long fromOnDuty;
	
	@JsonProperty("toOnDuty")
	private long toOnDuty;
	
	@JsonProperty("onDuty")
	private long onDuty;
	
	@JsonProperty("fromOnDrive")
	private long fromOnDrive;
	
	@JsonProperty("toOnDrive")
	private long toOnDrive;
	
	@JsonProperty("onDrive")
	private long onDrive;
	
	@JsonProperty("fromOnSleep")
	private long fromOnSleep;
	
	@JsonProperty("toOnSleep")
	private long toOnSleep;
	
	@JsonProperty("onSleep")
	private long onSleep;
	
	@JsonProperty("fromOffDuty")
	private long fromOffDuty;
	
	@JsonProperty("toOffDuty")
	private long toOffDuty;
	
	@JsonProperty("offDuty")
	private long offDuty;
}
