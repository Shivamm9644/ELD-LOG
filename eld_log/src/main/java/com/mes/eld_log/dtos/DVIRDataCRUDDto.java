package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DVIRDataCRUDDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("dvirLogId")
	private String dvirLogId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("lDateTime")
	private long lDateTime;
	
	@JsonProperty("location")
	private String location;
	
	@JsonProperty("truckDefect")
	private ArrayList truckDefect;
	
	@JsonProperty("trailerDefect")
	private ArrayList trailerDefect;
	
	@JsonProperty("trailer")
	private ArrayList trailer;
	
	@JsonProperty("truckDefectImage")
	private ArrayList truckDefectImage;
	
	@JsonProperty("trailerDefectImage")
	private ArrayList trailerDefectImage;
	
	@JsonProperty("notes")
	private String notes;
	
	@JsonProperty("vehicleCondition")
	private String vehicleCondition;
	
	@JsonProperty("driverSignFile")
	private String driverSignFile;
	
	@JsonProperty("companyName")
	private String companyName;
	
	@JsonProperty("odometer")
	private double odometer;
	
	@JsonProperty("engineHour")
	private String engineHour;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("vin")
	private String vin;
	
	@JsonProperty("timezoneName")
	private String timezoneName;
	
	@JsonProperty("timezoneOffSet")
	private String timezoneOffSet;
	
	@JsonProperty("timestamp")
	private String timestamp;
	
	@JsonProperty("receivedTimestamp")
	private long receivedTimestamp;
	
}
