package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CycleCanadaCRUDDto {
	
	@JsonProperty("cycleCanadaId")
	private Integer cycleCanadaId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
