package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeviceMasterCRUDDto {
	
	@JsonProperty("deviceId")
	private Integer deviceId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
