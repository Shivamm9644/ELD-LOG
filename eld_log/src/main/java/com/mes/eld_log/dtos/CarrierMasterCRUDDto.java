package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CarrierMasterCRUDDto {
	
	@JsonProperty("carrierId")
	private Integer carrierId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
