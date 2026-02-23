package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DriveringStatusViewDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("certifiedLogId")
	private String certifiedLogId;
	
	@JsonProperty("statusId")
	private long statusId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("companyDriverId")
	private String companyDriverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("cdlNo")
	private String cdlNo;
	
	@JsonProperty("countryName")
	private String countryName;
	
	@JsonProperty("stateName")
	private String stateName;
	
	@JsonProperty("exempt")
	private String exempt;
	
	@JsonProperty("shortHaulException")
	private String shortHaulException;
	
	@JsonProperty("companyCoDriverId")
	private String companyCoDriverId;
	
	@JsonProperty("coDriverId")
	private long coDriverId;
	
	@JsonProperty("coDriverName")
	private String coDriverName;	
	
	@JsonProperty("trailers")
	private ArrayList trailers;
	
	@JsonProperty("shippingDocs")
	private ArrayList shippingDocs;
	
	@JsonProperty("certifiedSignature")
	private String certifiedSignature;
	
	@JsonProperty("certifiedSignatureName")
	private String certifiedSignatureName;
	
	@JsonProperty("exception")
	private String exception;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("isSystemGenerated")
	private int isSystemGenerated;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("truckNo")
	private String truckNo;
	
	@JsonProperty("vin")
	private String vin;
	
	@JsonProperty("macAddress")
	private String macAddress;
	
	@JsonProperty("serialNo")
	private String serialNo;
	
	@JsonProperty("version")
	private String version;
	
	@JsonProperty("modelNo")
	private String modelNo;
		
	@JsonProperty("lattitude")
	private double lattitude;

	@JsonProperty("longitude")
	private double longitude;

	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("lDateTime")
	private long lDateTime;
	
	@JsonProperty("utcDateTime")
	private long utcDateTime;
	
	@JsonProperty("fromDate")
	private long fromDate;
	
	@JsonProperty("toDate")
	private long toDate;
	
	@JsonProperty("receivedTimestamp")
	private long receivedTimestamp;
	
	@JsonProperty("logType")
	private String logType;
	
	@JsonProperty("appVersion")
	private String appVersion;
	
	@JsonProperty("osVersion")
	private String osVersion;
	
	@JsonProperty("simCardNo")
	private String simCardNo;
	
	@JsonProperty("isVoilation")
	private Integer isVoilation;
	
	@JsonProperty("note")
	private String note;
	
	@JsonProperty("customLocation")
	private String customLocation;
	
	@JsonProperty("engineHour")
	private String engineHour;
	
	@JsonProperty("startEngineHour")
	private String startEngineHour;
	
	@JsonProperty("endEngineHour")
	private String endEngineHour;
	
	@JsonProperty("engineStatus")
	private String engineStatus;
	
	@JsonProperty("origin")
	private String origin;
	
	@JsonProperty("odometer")
	private double odometer;
	
	@JsonProperty("startOdometer")
	private double startOdometer;
	
	@JsonProperty("endOdometer")
	private double endOdometer;
	
	@JsonProperty("carrier")
	private String carrier;
	
	@JsonProperty("mainOffice")
	private String mainOffice;
	
	@JsonProperty("mainTerminalName")
	private String mainTerminalName;
	
	@JsonProperty("dotNo")
	private String dotNo;
	
	@JsonProperty("companyName")
	private String companyName;
	
	@JsonProperty("cycleUsaName")
	private String cycleUsaName;
	
	@JsonProperty("eldProvider")
	private String eldProvider;
	
	@JsonProperty("diagnosticIndicator")
	private String diagnosticIndicator;
	
	@JsonProperty("malfunctionIndicator")
	private String malfunctionIndicator;
	
	@JsonProperty("eldRegistrationId")
	private String eldRegistrationId;
	
	@JsonProperty("eldIdentifier")
	private String eldIdentifier;
	
	@JsonProperty("periodStartingTime")
	private String periodStartingTime;
	
	@JsonProperty("timezone")
	private String timezone;
	
	@JsonProperty("timezoneName")
	private String timezoneName;
	
	@JsonProperty("timezoneOffSet")
	private String timezoneOffSet;
	
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
	
	@JsonProperty("isSplit")
	private int isSplit;
	
	@JsonProperty("identifier")
	private int identifier;
	
	@JsonProperty("onDutyTime")
	private String onDutyTime;
	
	@JsonProperty("onDriveTime")
	private String onDriveTime;
	
	@JsonProperty("onSleepTime")
	private String onSleepTime;
	
	@JsonProperty("lastOnSleepTime")
	private String lastOnSleepTime;
	
	@JsonProperty("weeklyTime")
	private String weeklyTime;
	
	@JsonProperty("onBreak")
	private String onBreak;
	
	@JsonProperty("unidentifiedLog")
	private String unidentifiedLog;
	
	@JsonProperty("distance")
	private double distance;
	
	@JsonProperty("isReportGenerated")
	private int isReportGenerated;
	
	@JsonProperty("isPreviousLog")
	private int isPreviousLog;

	private  int getTrailerId;

	
}
