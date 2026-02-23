package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeofanceMasterCRUDDto {
	
	@JsonProperty("geoId")
	private Integer geoId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
