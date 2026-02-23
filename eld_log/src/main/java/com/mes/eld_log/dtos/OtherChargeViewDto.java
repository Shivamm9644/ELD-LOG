package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OtherChargeViewDto {
	
	@JsonProperty("dispatchId")
	private String dispatchId;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("amount")
	private double amount;
	
	@JsonProperty("reason")
	private String reason;
	
}
