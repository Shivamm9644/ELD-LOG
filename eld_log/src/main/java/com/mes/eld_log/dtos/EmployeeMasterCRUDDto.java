package com.mes.eld_log.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.SplitLog;

import lombok.Data;

@Data
public class EmployeeMasterCRUDDto {
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("disclaimerRead")
	private long disclaimerRead;
	
	@JsonProperty("disclaimer")
	private String disclaimer;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("employeeId")
	private Integer employeeId;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("mainTerminalId")
	private long mainTerminalId;
	
	@JsonProperty("clientName")
	private String clientName;
	
	@JsonProperty("timezone")
	private String timezone;
	
	@JsonProperty("timezoneOffSet")
	private String timezoneOffSet;
	
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
	
	@JsonProperty("loginDateTime")
	private long loginDateTime;
	
	@JsonProperty("macAddress")
	private String macAddress;
	
	@JsonProperty("vehicleId")
	private long vehicleId;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("isFirstLogin")
	private String isFirstLogin;
	
	@JsonProperty("onDutyTime")
	private long onDutyTime;
	
	@JsonProperty("onDriveTime")
	private long onDriveTime;
	
	@JsonProperty("onSleepTime")
	private long onSleepTime;
	
	@JsonProperty("continueDriveTime")
	private long continueDriveTime;
	
	@JsonProperty("breakTime")
	private long breakTime;
	
	@JsonProperty("cycleRestartTime")
	private long cycleRestartTime;
	
	@JsonProperty("exempt")
	private String exempt;
	
	@JsonProperty("personalUse")
	private String personalUse;
	
	@JsonProperty("yardMoves")
	private String yardMoves;
	
	@JsonProperty("shortHaulException")
	private String shortHaulException;
	
	@JsonProperty("unlimitedTrailers")
	private String unlimitedTrailers;
	
	@JsonProperty("unlimitedShippingDocs")
	private String unlimitedShippingDocs;
	
	@JsonProperty("androidVersion")
	private String androidVersion;
	
	@JsonProperty("androidCode")
	private String androidCode;
	
	@JsonProperty("iosVersion")
	private String iosVersion;
	
	@JsonProperty("iosCode")
	private String iosCode;
	
	@JsonProperty("termsAndCondition")
	private String termsAndCondition;
	
	@JsonProperty("driverLog")
	private List<DriveringStatusViewDto> driverLog;
	
	@JsonProperty("driverDvirLog")
	private List<DVIRDataCRUDDto> driverDvirLog;
	
	@JsonProperty("driverCertifiedLog")
	private List<CertifiedLogViewDto> driverCertifiedLog;
	
	@JsonProperty("loginLogoutLog")
	private List<DriveringStatusLogViewDto> loginLogoutLog;
	
	@JsonProperty("splitLog")
	private List<SplitLog> splitLog;
	
	@JsonProperty("rules")
	private List<CycleTimeRulesViewDto> rules;
	
	
	
}
