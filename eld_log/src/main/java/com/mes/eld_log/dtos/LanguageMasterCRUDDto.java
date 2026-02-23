package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LanguageMasterCRUDDto {
	
	@JsonProperty("languageId")
	private Integer languageId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
