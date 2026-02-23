package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.DVIRData;
import com.mes.eld_log.models.DefectDetails;

import lombok.Data;

@Data
public class DefectDetailCRUDDto {

	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("dvirId")
	private String dvirId;
	
	@JsonProperty("defectName")
	private String defectName;
	
	@JsonProperty("defectType")
	private String defectType;
	
	@JsonProperty("driverId")
	private long driverId;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("fileName")
	private String fileName;
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("lDateTime")
	private long lDateTime;
	
	private long receivedTimestamp;
	
}
