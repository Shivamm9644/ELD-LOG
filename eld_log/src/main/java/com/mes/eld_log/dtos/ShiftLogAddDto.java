package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShiftLogAddDto {
	
	@JsonProperty("logStatusId")
	private String logStatusId;

	@JsonProperty("logStatus")
	private String logStatus;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("days")
	private int days;
	
	@JsonProperty("shift")
	private int shift;
	
	@JsonProperty("totalWeeklyTime")
	private long totalWeeklyTime;
	
	@JsonProperty("totalTimeOnDuty")
	private long totalTimeOnDuty;
	
	@JsonProperty("totalTimeDrive")
	private long totalTimeDrive;
	
	@JsonProperty("totalTimeOnSleep")
	private long totalTimeOnSleep;
	
	@JsonProperty("utcDateTime")
	private long utcDateTime;
	
	@JsonProperty("debugTime")
	private long debugTime;
	
	@JsonProperty("debugData")
	private String debugData;
		
}
