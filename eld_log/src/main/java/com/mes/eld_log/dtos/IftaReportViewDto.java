package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Data
public class IftaReportViewDto {
	
	@JsonProperty("carrierName")
	private String carrierName;
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("vehicleId")
	private String vehicleId;
	
	@JsonProperty("DriverId")
	private String DriverId;
	
	@JsonProperty("stateId")
	private long stateId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("utcDateTime")
	private long utcDateTime;
	
	@JsonProperty("make")
	private String make;
	
	@JsonProperty("vin")
	private String vin;
	
	@JsonProperty("model")
	private String model;
	
	@JsonProperty("manufacturingYear")
	private long manufacturingYear;
	
	@JsonProperty("stateName")
	private String stateName;
	
	@JsonProperty("stateCode")
	private String stateCode;
	
	@JsonProperty("totalOdometer")
	private double totalOdometer;
	
	@JsonProperty("Odometer")
	private String Odometer;
	
	@JsonProperty("firstOdometer")
	private double firstOdometer;
	
	@JsonProperty("lastOdometer")
	private double lastOdometer;
	
	@JsonProperty("Lattitude")
	private double Lattitude;
	
	@JsonProperty("Longitude")
	private double Longitude;
	
	@JsonProperty("gpsKm")
	private double gpsKm;
	
	@JsonProperty("currentTimestamp")
	private long currentTimestamp;
	
	@JsonProperty("fileName")
	private String fileName;
	
}
