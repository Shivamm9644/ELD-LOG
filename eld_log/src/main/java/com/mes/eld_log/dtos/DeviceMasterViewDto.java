package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeviceMasterViewDto {
	
	@JsonProperty("deviceId")
	private Integer deviceId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("clientName")
	private String clientName;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("deviceNo")
	private String deviceNo;
	
	@JsonProperty("macId")
	private String macId;
	
	@JsonProperty("serialNo")
	private String serialNo;
	
	@JsonProperty("deviceModelId")
	private long deviceModelId;
	
	@JsonProperty("deviceModelName")
	private String deviceModelName;
	
	@JsonProperty("billingDate")
	private String billingDate;
	
	@JsonProperty("lBillingDate")
	private long lBillingDate;
	
	@JsonProperty("warranty")
	private long warranty;
	
	@JsonProperty("warrantyFromDate")
	private String warrantyFromDate;
	
	@JsonProperty("lWarrantyFromDate")
	private long lWarrantyFromDate;
	
	@JsonProperty("warrantyToDate")
	private String warrantyToDate;
	
	@JsonProperty("lWarrantyToDate")
	private long lWarrantyToDate;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("malFunction")
	private String malFunction;
	
	@JsonProperty("fwVersion")
	private String fwVersion;
	
	@JsonProperty("addedTimestamp")
	private long addedTimestamp;
	
	@JsonProperty("updatedTimestamp")
	private long updatedTimestamp;
	
}
