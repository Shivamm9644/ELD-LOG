package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClientMasterCRUDDto {
	
	@JsonProperty("clientId")
	private Integer clientId;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
}
