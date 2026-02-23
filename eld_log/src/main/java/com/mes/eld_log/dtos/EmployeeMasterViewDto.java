package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmployeeMasterViewDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("employeeId")
	private Integer employeeId;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("deviceStatus")
	private String deviceStatus;
	
	@JsonProperty("driverId")
	private String driverId;
	
	@JsonProperty("password")
	private String password;
	
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
	
	@JsonProperty("unlimitedTrailers")
	private String unlimitedTrailers;
	
	@JsonProperty("unlimitedShippingDocs")
	private String unlimitedShippingDocs;
		
	@JsonProperty("companyId")
	private long companyId;
	
	@JsonProperty("languageId")
	private long languageId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("clientName")
	private ArrayList clientName;
	
	@JsonProperty("startTime")
	private String startTime;
	
	@JsonProperty("appVersion")
	private String appVersion;
	
	@JsonProperty("osVersion")
	private String osVersion;
	
	@JsonProperty("simCardNo")
	private String simCardNo;
	
	@JsonProperty("cycleUsaId")
	private long cycleUsaId;
	
	@JsonProperty("cycleUsaName")
	private ArrayList cycleUsaName;
	
	@JsonProperty("cycleCanadaId")
	private long cycleCanadaId;
	
	@JsonProperty("cycleCanadaName")
	private ArrayList cycleCanadaName;
	
	@JsonProperty("mainTerminalId")
	private long mainTerminalId;
	
	@JsonProperty("mainTerminalName")
	private ArrayList mainTerminalName;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("cdlNo")
	private String cdlNo;
	
	@JsonProperty("cdlCountryId")
	private long cdlCountryId;
	
	@JsonProperty("countryName")
	private ArrayList countryName;
	
	@JsonProperty("cdlStateId")
	private long cdlStateId;
	
	@JsonProperty("stateName")
	private ArrayList stateName;
	
	@JsonProperty("cdlExpiryDate")
	private String cdlExpiryDate;
	
	@JsonProperty("lCdlExpiryDate")
	private long lCdlExpiryDate;
	
	@JsonProperty("pdfEmail")
	private String pdfEmail;
	
	@JsonProperty("flatRate")
	private double flatRate;
	
	@JsonProperty("exempt")
	private String exempt;
	
	@JsonProperty("personalUse")
	private String personalUse;
	
	@JsonProperty("yardMoves")
	private String yardMoves;
	
	@JsonProperty("divR")
	private String divR;
	
	@JsonProperty("cargoTypeId")
	private long cargoTypeId;
	
	@JsonProperty("cargoTypeName")
	private ArrayList cargoTypeName;
	
	@JsonProperty("truckNo")
	private long truckNo;
	
	@JsonProperty("vehicleNo")
	private ArrayList vehicleNo;
	
	@JsonProperty("manageEquipement")
	private String manageEquipement;
	
	@JsonProperty("transferLog")
	private String transferLog;
	
	@JsonProperty("remarks")
	private String remarks;
	
	@JsonProperty("workingStatus")
	private String workingStatus;
	
	@JsonProperty("onDutyTime")
	private String onDutyTime;
	
	@JsonProperty("onDriveTime")
	private String onDriveTime;
	
	@JsonProperty("onSleepTime")
	private String onSleepTime;
	
	@JsonProperty("weeklyTime")
	private String weeklyTime;
	
	@JsonProperty("onBreak")
	private String onBreak;
	
	@JsonProperty("addedTimestamp")
	private long addedTimestamp;
	
	@JsonProperty("updatedTimestamp")
	private long updatedTimestamp;
	
	@JsonProperty("currentLocation")
	private String currentLocation;
	
	@JsonProperty("timezoneName")
	private String timezoneName;
	
	@JsonProperty("timezoneOffSet")
	private String timezoneOffSet;
	
}
