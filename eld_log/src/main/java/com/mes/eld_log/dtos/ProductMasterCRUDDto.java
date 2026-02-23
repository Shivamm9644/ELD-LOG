package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductMasterCRUDDto {
	
	@JsonProperty("productId")
	private Integer productId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
