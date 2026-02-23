package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DefectMasterCRUDDto {
	
	@JsonProperty("defectId")
	private Integer defectId;
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
