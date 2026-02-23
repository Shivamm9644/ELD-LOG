package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RestBreakMasterCRUDDto {
	
	@JsonProperty("restBreakId")
	private Integer restBreakId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
