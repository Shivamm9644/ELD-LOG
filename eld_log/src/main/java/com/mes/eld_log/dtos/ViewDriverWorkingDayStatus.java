package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ViewDriverWorkingDayStatus {
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("lDateTime")
	private long lDateTime;
	
}
