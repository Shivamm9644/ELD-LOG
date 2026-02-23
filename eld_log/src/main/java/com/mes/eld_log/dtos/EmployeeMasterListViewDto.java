package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmployeeMasterListViewDto {
	
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
	
}
