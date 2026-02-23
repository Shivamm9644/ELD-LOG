package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RestartMasterCRUDDto {
	
	@JsonProperty("restartId")
	private Integer restartId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
