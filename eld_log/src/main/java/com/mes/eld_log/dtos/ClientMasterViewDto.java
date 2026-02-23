package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClientMasterViewDto {
	
	@JsonProperty("companyId")
	private String companyId;
	
	@JsonProperty("clientId")
	private Integer clientId;
	
	@JsonProperty("clientName")
	private String clientName; // company name
	
	@JsonProperty("dotNo")
	private String dotNo; 
	
	@JsonProperty("timezoneId")
	private long timezoneId;
	
	@JsonProperty("timezone")
	private String timezone;
	
	@JsonProperty("street")
	private String street;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("countryId")
	private long countryId;
	
	@JsonProperty("countryName")
	private ArrayList countryName;
	
	@JsonProperty("stateId")
	private long stateId;
	
	@JsonProperty("stateName")
	private ArrayList stateName;
	
	@JsonProperty("zipcode")
	private long zipcode;
	
	@JsonProperty("terminalData")
	private ArrayList terminalData;
	
	@JsonProperty("complianceMode")
	private String complianceMode;
	
	@JsonProperty("vehicleMotionThresold")
	private String vehicleMotionThresold;

	@JsonProperty("exemptDriver")
	private String exemptDriver;
	
	@JsonProperty("cycleUsaId")
	private long cycleUsaId;
	
	@JsonProperty("cycleUsaName")
	private ArrayList cycleUsaName;
	
	@JsonProperty("cargoTypeId")
	private long cargoTypeId;
	
	@JsonProperty("cargoTypeName")
	private ArrayList cargoTypeName;
	
	@JsonProperty("restartId")
	private long restartId;
	
	@JsonProperty("restartName")
	private ArrayList restartName;
	
	@JsonProperty("restBreakId")
	private long restBreakId;
	
	@JsonProperty("restBreakName")
	private ArrayList restBreakName;
	
	@JsonProperty("shortHaulException")
	private String shortHaulException;
	
	@JsonProperty("personalUse")
	private String personalUse;
	
	@JsonProperty("yardMoves")
	private String yardMoves;
	
	@JsonProperty("allowTracking")
	private String allowTracking;
	
	@JsonProperty("allowGpsTracking")
	private String allowGpsTracking;
	
	@JsonProperty("allowIfta")
	private String allowIfta;
	
	@JsonProperty("project44")
	private String project44;
	
	@JsonProperty("microPoint")
	private String microPoint;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("addedTimestamp")
	private long addedTimestamp;
	
	@JsonProperty("updatedTimestamp")
	private long updatedTimestamp;
	
	@JsonProperty("lattitude")
	private double lattitude;
	
	@JsonProperty("longitude")
	private double longitude;
	
	@JsonProperty("subscriptionEndTime")
	private long subscriptionEndTime;
	
	@JsonProperty("graceTime")
	private long graceTime;
}
