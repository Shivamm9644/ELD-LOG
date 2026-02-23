package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AddEmployeeInfoDto {

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
	
	@JsonProperty("startTime")
	private String startTime;
	
	@JsonProperty("cycleUsa")
	private String cycleUsa;
	
	@JsonProperty("cycleCanada")
	private String cycleCanada;
	
	@JsonProperty("mainTerminal")
	private String mainTerminal;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("cdlNo")
	private String cdlNo;
	
	@JsonProperty("cdlCountry")
	private String cdlCountry;
	
	@JsonProperty("cdlState")
	private String cdlState;
	
	@JsonProperty("cdlExpiryDate")
	private String cdlExpiryDate;
	
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
	
	@JsonProperty("cargoType")
	private String cargoType;
	
	@JsonProperty("truckNo")
	private String truckNo;
	
	@JsonProperty("manageEquipement")
	private String manageEquipement;
	
	@JsonProperty("transferLog")
	private String transferLog;
	
	@JsonProperty("remarks")
	private String remarks;

}
