package com.mes.eld_log.dtos;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CertifiedLogViewDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("vehicleName")
	private String vehicleName;
	
	@JsonProperty("trailers")
	private ArrayList trailers;
	
	@JsonProperty("shippingDocs")
	private ArrayList shippingDocs;
	
	@JsonProperty("coDriverId")
	private long coDriverId;
	
	@JsonProperty("coDriverName")
	private String coDriverName;
	
	@JsonProperty("certifiedSignature")
	private String certifiedSignature;
	
	@JsonProperty("certifiedDate")
	private String certifiedDate;
	
	@JsonProperty("lCertifiedDate")
	private long lCertifiedDate;
	
	@JsonProperty("certifiedDateTime")
	private long certifiedDateTime;
	
	@JsonProperty("certifiedAt")
	private long certifiedAt;
	
	@JsonProperty("addedTimestamp")
	private long addedTimestamp;
	
}
