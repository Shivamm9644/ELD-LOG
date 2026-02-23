package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleMasterViewDto {
	
	@JsonProperty("vehicleId")
	private Integer vehicleId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("make")
	private String make;
	
	@JsonProperty("model")
	private String model;
	
	@JsonProperty("manufacturingYear")
	private long manufacturingYear;
	
	@JsonProperty("licensePlate")
	private String licensePlate;
	
	@JsonProperty("countryId")
	private long countryId;
	
	@JsonProperty("stateId")
	private long stateId;
	
	@JsonProperty("vin")
	private String vin;
	
	@JsonProperty("fuelTypeId")
	private long fuelTypeId;
	
	@JsonProperty("deviceId")
	private long deviceId;
	
	@JsonProperty("deviceName")
	private String deviceName;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("eldConnectionInterfaceId")
	private long eldConnectionInterfaceId;
	
	@JsonProperty("macAddress")
	private String macAddress;
	
	@JsonProperty("serialNo")
	private String serialNo;
	
	@JsonProperty("version")
	private String version;
	
	@JsonProperty("modelNo")
	private String modelNo;
	
	@JsonProperty("deviceStatus")
	private String deviceStatus;
	
	@JsonProperty("addedTimestamp")
	private long addedTimestamp;
	
	@JsonProperty("updatedTimestamp")
	private long updatedTimestamp;
}
