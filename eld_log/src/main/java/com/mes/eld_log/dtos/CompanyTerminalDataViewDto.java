package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyTerminalDataViewDto {
	
	@JsonProperty("terminalTimezoneId")
	private long terminalTimezoneId;
	
	@JsonProperty("terminalStartTime")
	private String terminalStartTime;
	
	@JsonProperty("terminalStreet")
	private String terminalStreet;
	
	@JsonProperty("terminalCity")
	private String terminalCity;
	
	@JsonProperty("terminalCountryId")
	private long terminalCountryId;
	
	@JsonProperty("terminalStateId")
	private long terminalStateId;
	
	@JsonProperty("terminalZipcode")
	private long terminalZipcode;
	
}
