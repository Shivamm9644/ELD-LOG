package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProjectDetailAnalyticsViewDto {
	
	@JsonProperty("totalUsers")
	private long totalUsers;
	
	@JsonProperty("totalDrivers")
	private long totalDrivers;
	
	@JsonProperty("totalCompanies")
	private long totalCompanies;
	
	@JsonProperty("totalVehicles")
	private long totalVehicles;
	
}
