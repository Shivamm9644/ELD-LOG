package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReceiverDataViewDto {
	
	@JsonProperty("dispatchId")
	private String dispatchId;
	
	@JsonProperty("receiverId")
	private long receiverId;
	
	@JsonProperty("receiverName")
	private String receiverName;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("countryId")
	private long countryId;
	
	@JsonProperty("countryName")
	private String countryName;
	
	@JsonProperty("stateId")
	private long stateId;
	
	@JsonProperty("stateName")
	private String stateName;
	
	@JsonProperty("cityId")
	private long cityId;
	
	@JsonProperty("cityName")
	private String cityName;
	
	@JsonProperty("date")
	private String date;
	
	@JsonProperty("time")
	private String time;
	
	@JsonProperty("receivingNo")
	private String receivingNo;
	
	@JsonProperty("comodity")
	private String comodity;
	
	@JsonProperty("reeferTemp")
	private double reeferTemp;
	
	@JsonProperty("referModeId")
	private long referModeId;
	
	@JsonProperty("referModeName")
	private String referModeName;
	
	@JsonProperty("caseCount")
	private long caseCount;
	
	@JsonProperty("pallets")
	private long pallets;
	
	@JsonProperty("weight")
	private double weight;
	
	@JsonProperty("shippingNotes")
	private String shippingNotes;
	
	@JsonProperty("contactPerson")
	private String contactPerson;
	
	@JsonProperty("mobileNo")
	private String mobileNo;
	
	@JsonProperty("order")
	private String order;
	
}
