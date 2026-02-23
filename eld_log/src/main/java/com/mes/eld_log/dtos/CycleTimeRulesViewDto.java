package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CycleTimeRulesViewDto {
	
	@JsonProperty("cycleTime")
	private long cycleTime;
	
	@JsonProperty("cycleDays")
	private long cycleDays;
	
	@JsonProperty("onDutyTime")
	private long onDutyTime;
	
	@JsonProperty("onDriveTime")
	private long onDriveTime;
	
	@JsonProperty("onSleepTime")
	private long onSleepTime;
	
	@JsonProperty("continueDriveTime")
	private long continueDriveTime;
	
	@JsonProperty("breakTime")
	private long breakTime;
	
	@JsonProperty("cycleRestartTime")
	private long cycleRestartTime;
	
	@JsonProperty("warningOnDutyTime1")
	private long warningOnDutyTime1;
	
	@JsonProperty("warningOnDutyTime2")
	private long warningOnDutyTime2;
	
	@JsonProperty("warningOnDriveTime1")
	private long warningOnDriveTime1;
	
	@JsonProperty("warningOnDriveTime2")
	private long warningOnDriveTime2;
	
	@JsonProperty("warningBreakTime1")
	private long warningBreakTime1;
	
	@JsonProperty("warningBreakTime2")
	private long warningBreakTime2;
	
}
