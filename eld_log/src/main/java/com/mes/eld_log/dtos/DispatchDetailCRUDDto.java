package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.DispatchDetails;
import com.mes.eld_log.models.OtherCharges;
import com.mes.eld_log.models.ShipperReceiverDetails;

import lombok.Data;

@Data
public class DispatchDetailCRUDDto {
	
	@JsonProperty("dispatchId")
	private String dispatchId;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("dispatchData")
	private ArrayList<DispatchDetails> dispatchData;
	
	@JsonProperty("otherChargeData")
	private ArrayList<OtherCharges> otherChargeData;
	
	@JsonProperty("shipperData")
	private ArrayList<ShipperReceiverDetails> shipperData;
	
	@JsonProperty("receiverData")
	private ArrayList<ShipperReceiverDetails> receiverData;
	
}
