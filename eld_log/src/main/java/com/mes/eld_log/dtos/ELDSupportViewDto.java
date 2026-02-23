package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ELDSupportViewDto {
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
		
	@JsonProperty("companyId")
	private long companyId;
	
	@JsonProperty("companyName")
	private String companyName;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("utcDateTime")
	private long utcDateTime;
	
}
