package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DispatchDetailViewDto {
	
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
	
	@JsonProperty("shipperId")
	private long shipperId;
	
	@JsonProperty("shipperName")
	private String shipperName;
	
	@JsonProperty("receiverId")
	private long receiverId;
	
	@JsonProperty("receiverName")
	private String receiverName;
	
	@JsonProperty("shippingDate")
	private String shippingDate;
	
	@JsonProperty("receivingDate")
	private String receivingDate;
	
	
	
}
