package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AlertsLogViewDto {

	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("MAC")
	private String MAC;
	
	@JsonProperty("model")
	private String model;
	
	@JsonProperty("serialNo")
	private String serialNo;
	
	@JsonProperty("vin")
	private String vin;
	
	@JsonProperty("version")
	private String version;
	
	@JsonProperty("odometer")
	private String odometer;
	
	@JsonProperty("startUtcDateTime")
	private long startUtcDateTime;
	
	@JsonProperty("endUtcDateTime")
	private long endUtcDateTime;
	
	@JsonProperty("durationInMillis")
	private long durationInMillis;
	
	@JsonProperty("placeAddress")
	private String placeAddress;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("isRead")
	private int isRead;
	
	@JsonProperty("readByEmail")
	private String readByEmail;
	
	@JsonProperty("readingTimestamp")
	private long readingTimestamp;
	
	@JsonProperty("lattitude")
	private double lattitude;
	
	@JsonProperty("longitude")
	private double longitude;
	
}
