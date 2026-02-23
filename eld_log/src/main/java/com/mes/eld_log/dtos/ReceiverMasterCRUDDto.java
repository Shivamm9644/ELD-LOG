package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReceiverMasterCRUDDto {
	
	@JsonProperty("receiverId")
	private Integer receiverId;
	
	@JsonProperty("clientId")
	private long clientId;
	
}
