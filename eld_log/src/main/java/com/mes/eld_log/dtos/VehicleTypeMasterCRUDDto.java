package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleTypeMasterCRUDDto {
	
	@JsonProperty("vehicleTypeId")
	private Integer vehicleTypeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
