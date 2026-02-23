package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeviceModalMasterCRUDDto {
	
	@JsonProperty("deviceModalId")
	private Integer deviceModalId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
