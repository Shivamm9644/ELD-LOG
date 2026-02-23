package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LiveDataLogViewDto {
	
	@JsonProperty("MAC")
	private String MAC;
	
	@JsonProperty("AmbTemp")
	private double AmbTemp;
	
	@JsonProperty("DateTime")
	private String DateTime;
	
	@JsonProperty("utcDateTime")
	private long utcDateTime;
	
	@JsonProperty("FuelEconomy")
	private double FuelEconomy;
	
	@JsonProperty("FuelRate")
	private double FuelRate;
	
	@JsonProperty("IdleHours")
	private long IdleHours;
	
	@JsonProperty("Lattitude")
	private double Lattitude;
	
	@JsonProperty("Longitude")
	private double Longitude;
	
	@JsonProperty("PlaceAddress")
	private String PlaceAddress;
	
	@JsonProperty("Model")
	private String Model;
	
	@JsonProperty("Odometer")
	private String Odometer;
	
	@JsonProperty("SatStatus")
	private String SatStatus;
	
	@JsonProperty("SerialNo")
	private String SerialNo;
	
	@JsonProperty("Speed")
	private long Speed;
	
	@JsonProperty("TotalFuelIdle")
	private double TotalFuelIdle;
	
	@JsonProperty("TotalFuelUsed")
	private double TotalFuelUsed;
	
	@JsonProperty("VIN")
	private String VIN;
	
	@JsonProperty("VehicleId")
	private String VehicleId;
	
	@JsonProperty("VehicleName")
	private String VehicleName;
	
	@JsonProperty("DriverId")
	private String DriverId;
	
	@JsonProperty("DriverName")
	private String DriverName;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("Version")
	private String Version;
	
	@JsonProperty("stateId")
	private long stateId;
	
	@JsonProperty("stateName")
	private String stateName;
	
	@JsonProperty("stateCode")
	private String stateCode;
	
	@JsonProperty("receive_time")
	private long receive_time;
	
}
