package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriveringStatusLogViewDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("driverStatusId")
	private String driverStatusId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("utcDateTime")
	private long utcDateTime;
	
	@JsonProperty("lastUtcDateTime")
	private long lastUtcDateTime;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("engineStatus")
	private String engineStatus;
	
	@JsonProperty("employeeStatus")
	private String employeeStatus;
	
	@JsonProperty("lattitude")
	private double lattitude;
	
	@JsonProperty("longitude")
	private double longitude;
	
	@JsonProperty("customLocation")
	private String customLocation;
	
	@JsonProperty("origin")
	private String origin;
	
	@JsonProperty("odometer")
	private double odometer;
	
	@JsonProperty("engineHour")
	private String engineHour;
	
	@JsonProperty("note")
	private String note;
	
	@JsonProperty("isVoilation")
	private long isVoilation;
	
	@JsonProperty("logType")
	private String logType;
	
	@JsonProperty("statusId")
	private long statusId;
	
	@JsonProperty("remainingWeeklyTime")
	private String remainingWeeklyTime;
	
	@JsonProperty("remainingDutyTime")
	private String remainingDutyTime;
	
	@JsonProperty("remainingDriveTime")
	private String remainingDriveTime;
	
	@JsonProperty("remainingSleepTime")
	private String remainingSleepTime;
	
	@JsonProperty("shift")
	private int shift;
	
	@JsonProperty("days")
	private int days;
	
	@JsonProperty("isReportGenerated")
	private long isReportGenerated;
	
	@JsonProperty("isPreviousLog")
	private int isPreviousLog;
	
	@JsonProperty("isLogDelete")
	private int isLogDelete;
	
	@JsonProperty("totalSeconds")
	private long totalSeconds;
	
	@JsonProperty("isDvirShift")
	private String isDvirShift;
	
}
