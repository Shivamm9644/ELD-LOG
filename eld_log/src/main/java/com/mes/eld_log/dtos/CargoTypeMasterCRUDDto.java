package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CargoTypeMasterCRUDDto {
	
	@JsonProperty("cargoTypeId")
	private Integer cargoTypeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
