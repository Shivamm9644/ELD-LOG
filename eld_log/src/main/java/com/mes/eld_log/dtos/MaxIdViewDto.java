package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MaxIdViewDto {
	
	@JsonProperty("statusId")
	private long statusId;
	
}
