package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TrailerMasterCRUDDto {
	
	@JsonProperty("trailerId")
	private Integer trailerId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
