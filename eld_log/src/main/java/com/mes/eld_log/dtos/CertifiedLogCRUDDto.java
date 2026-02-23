package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CertifiedLogCRUDDto {
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("certifiedLogId")
	private String certifiedLogId;
	
	@JsonProperty("trailers")
	private ArrayList trailers;
	
	@JsonProperty("shippingDocs")
	private ArrayList shippingDocs;
	
	@JsonProperty("coDriverId")
	private long coDriverId;
	
}
