package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RouteMasterCRUDDto {
	
	@JsonProperty("routeId")
	private Integer routeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
