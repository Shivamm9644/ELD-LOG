package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserTypeMasterCRUDDto {
	
	@JsonProperty("userTypeId")
	private Integer userTypeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
