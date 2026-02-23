package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TimezoneMasterCRUDDto {
	
	@JsonProperty("timezoneId")
	private Integer timezoneId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
