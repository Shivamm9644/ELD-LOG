package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaymentStatusMasterCRUDDto {
	
	@JsonProperty("paymentStatusId")
	private Integer paymentStatusId;
	
	@JsonProperty("clientId")
	private long clientId;
}
