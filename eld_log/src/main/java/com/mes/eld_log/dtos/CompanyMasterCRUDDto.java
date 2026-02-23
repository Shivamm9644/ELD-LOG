package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyMasterCRUDDto {
	
	@JsonProperty("companyId")
	private Integer companyId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
