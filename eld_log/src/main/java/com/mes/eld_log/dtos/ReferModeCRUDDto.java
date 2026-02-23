package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReferModeCRUDDto {
	
	@JsonProperty("referModeId")
	private Integer referModeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
