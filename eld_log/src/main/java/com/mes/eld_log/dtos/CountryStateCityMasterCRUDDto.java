package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CountryStateCityMasterCRUDDto {
	@JsonProperty("countryId")
	private Integer countryId;
	
	@JsonProperty("stateId")
	private Integer stateId;
	
	@JsonProperty("cityId")
	private Integer cityId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
