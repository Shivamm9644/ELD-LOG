package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShipperMasterCRUDDto {
	
	@JsonProperty("shipperId")
	private Integer shipperId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
