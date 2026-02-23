package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleConditionCRUDDto {
	
	@JsonProperty("vehicleConditionId")
	private Integer vehicleConditionId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
