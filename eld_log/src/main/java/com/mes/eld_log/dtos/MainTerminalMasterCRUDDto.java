package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MainTerminalMasterCRUDDto {
	
	@JsonProperty("mainTerminalId")
	private Integer mainTerminalId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
