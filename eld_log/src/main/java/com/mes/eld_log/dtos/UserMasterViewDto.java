package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserMasterViewDto {
	
	@JsonProperty("userId")
	private Integer userId;
	
	@JsonProperty("disclaimerRead")
	private long disclaimerRead;
	
	@JsonProperty("disclaimer")
	private String disclaimer;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("clientName")
	private String clientName;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("tokenNo")
	private String tokenNo;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("userTypeId")
	private long userTypeId;
	
	@JsonProperty("userTypeName")
	private String userTypeName;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("mobileNo")
	private long mobileNo;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("countryId")
	private long countryId;
	
	@JsonProperty("stateId")
	private long stateId;
	
	@JsonProperty("cityId")
	private long cityId;
	
	@JsonProperty("zipcode")
	private long zipcode;
	
	@JsonProperty("timezone")
	private String timezone;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("webAccess")
	private String webAccess;
	
	@JsonProperty("mobileAccess")
	private String mobileAccess;
	
	@JsonProperty("eldFeature")
	private String eldFeature;
	
	@JsonProperty("dispatchFeature")
	private String dispatchFeature;
	
	@JsonProperty("addedTimestamp")
	private long addedTimestamp;
	
	@JsonProperty("updatedTimestamp")
	private long updatedTimestamp;
	
	@JsonProperty("lattitude")
	private double lattitude;
	
	@JsonProperty("longitude")
	private double longitude;
	
	@JsonProperty("loginDateTime")
	private long loginDateTime;
	
	@JsonProperty("allowTracking")
	private String allowTracking;
	
	@JsonProperty("allowGpsTracking")
	private String allowGpsTracking;
	
	@JsonProperty("allowIfta")
	private String allowIfta;
	
	@JsonProperty("exemptDriver")
	private String exemptDriver;
	
	@JsonProperty("personalUse")
	private String personalUse;
	
	@JsonProperty("yardMoves")
	private String yardMoves;
	
	@JsonProperty("shortHaulException")
	private String shortHaulException;
	
	
}
