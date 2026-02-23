package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CustomerMasterCRUDDto {
	
	@JsonProperty("customerId")
	private Integer customerId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
