package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DispatchDataViewDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("load")
	private String load;
	
	@JsonProperty("customerId")
	private long customerId;
	
	@JsonProperty("customerName")
	private String customerName;
	
	@JsonProperty("customerReferenceNo")
	private String customerReferenceNo;
	
	@JsonProperty("dispatcher")
	private String dispatcher;
	
	@JsonProperty("loadEnterBy")
	private String loadEnterBy;
	
	@JsonProperty("currency")
	private String currency;
	
	@JsonProperty("flatRate")
	private double flatRate;
	
	@JsonProperty("extraPD")
	private double extraPD;
	
	@JsonProperty("fuelSurCharge")
	private double fuelSurCharge;
	
	@JsonProperty("extraCharge")
	private double extraCharge;
	
	@JsonProperty("otherCharge")
	private double otherCharge;
	
	@JsonProperty("dudectionTotal")
	private double dudectionTotal;
	
	@JsonProperty("reimbursementTotal")
	private double reimbursementTotal;
	
	@JsonProperty("total")
	private double total;
	
	@JsonProperty("dispatchBy")
	private String dispatchBy;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("driverTruck")
	private String driverTruck;
	
	@JsonProperty("driverTrailer")
	private String driverTrailer;
	
	@JsonProperty("driverRate")
	private double driverRate;
	
	@JsonProperty("totalMiles")
	private double totalMiles;
	
	@JsonProperty("carrierId")
	private long carrierId;
	
	@JsonProperty("carrierName")
	private String carrierName;
	
	@JsonProperty("carrierTruck")
	private String carrierTruck;
	
	@JsonProperty("carrierTrailer")
	private String carrierTrailer;
	
	@JsonProperty("equipmentType")
	private long equipmentType;
	
	@JsonProperty("carrierRate")
	private double carrierRate;
	
	@JsonProperty("carrierOtherCharges")
	private double carrierOtherCharges;
	
	@JsonProperty("isDataSaveAsDraft")
	private String isDataSaveAsDraft;
	
	@JsonProperty("divR")
	private String divR;
	
}
