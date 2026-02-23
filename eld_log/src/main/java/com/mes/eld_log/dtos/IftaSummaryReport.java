package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IftaSummaryReport {
	
	@JsonProperty("carrierName")
	private String carrierName;
	
	@JsonProperty("fromDate")
	private long fromDate;
	
	@JsonProperty("toDate")
	private long toDate;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("vehicleId")
	private String vehicleId;
	
	@JsonProperty("make")
	private String make;
	
	@JsonProperty("model")
	private String model;
	
	@JsonProperty("manufacturingYear")
	private long manufacturingYear;
	
	@JsonProperty("driverId")
	private String driverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("fromPlaceAddress")
	private String fromPlaceAddress;
	
	@JsonProperty("toPlaceAddress")
	private String toPlaceAddress;	
	
	@JsonProperty("fromStateName")
	private String fromStateName;
	
	@JsonProperty("toStateName")
	private String toStateName;
	
	@JsonProperty("fromStateCode")
	private String fromStateCode;
	
	@JsonProperty("toStateCode")
	private String toStateCode;
	
	@JsonProperty("firstOdometer")
	private double firstOdometer;
	
	@JsonProperty("lastOdometer")
	private double lastOdometer;
	
	@JsonProperty("odometerMileage")
	private double odometerMileage;
	
	@JsonProperty("gpsMileage")
	private double gpsMileage;
	
}
