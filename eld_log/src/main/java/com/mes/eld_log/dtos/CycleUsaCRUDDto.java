package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CycleUsaCRUDDto {
	
	@JsonProperty("cycleUsaId")
	private Integer cycleUsaId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
