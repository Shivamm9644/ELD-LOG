package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FuelTypeMasterCRUDDto {
	
	@JsonProperty("fuelTypeId")
	private Integer fuelTypeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
