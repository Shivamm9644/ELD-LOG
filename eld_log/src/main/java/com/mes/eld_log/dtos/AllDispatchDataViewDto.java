package com.mes.eld_log.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllDispatchDataViewDto {
	
	@JsonProperty("dispatchData")
	private List<DispatchDataViewDto> dispatchData;
	
	@JsonProperty("otherChargeData")
	private List<OtherChargeViewDto> otherChargeData;
	
	@JsonProperty("shippingData")
	private List<ShipperDataViewDto> shippingData;
	
	@JsonProperty("receivingData")
	private List<ReceiverDataViewDto> receivingData;
	
}
