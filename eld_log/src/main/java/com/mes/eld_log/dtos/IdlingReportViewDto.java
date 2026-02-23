package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IdlingReportViewDto {
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("vehicleId")
	private String vehicleId;
	
	@JsonProperty("driverId")
	private String driverId;
	
	@JsonProperty("clientId")
    private long clientId;
	
	@JsonProperty("clientName")
    private String clientName;
	
	@JsonProperty("VIN")
    private String VIN;
	
	@JsonProperty("Version")
    private String Version;
	
	@JsonProperty("MAC")
    private String MAC;
	
	@JsonProperty("Model")
	private String Model;
	
	@JsonProperty("SerialNo")
	private String SerialNo;
	
	@JsonProperty("startDateTime")
    private String startDateTime;
	
	@JsonProperty("endDateTime")
    private String endDateTime;
	
	@JsonProperty("startUtcDateTime")
	private long startUtcDateTime;
	
	@JsonProperty("endUtcDateTime")
	private long endUtcDateTime;
	
	@JsonProperty("durationMillis")
    private long durationMillis;
	
	@JsonProperty("startAddress")
    private String startAddress;
	
	@JsonProperty("endAddress")
    private String endAddress;

	@JsonProperty("startEngineHours")
    private double startEngineHours;
	
	@JsonProperty("endEngineHours")
    private double endEngineHours;
	
	@JsonProperty("startOdometer")
    private String startOdometer;
	
	@JsonProperty("endOdometer")
    private String endOdometer;
   
	@JsonProperty("fileName")
	private String fileName;
	
}
