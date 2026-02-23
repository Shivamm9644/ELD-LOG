package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SimulatorCRUDDto {
	
	@JsonProperty("_id")
	private String _id;
	
	@JsonProperty("driverId")
	private Integer driverId;
	
	@JsonProperty("title")
	private ArrayList title;
	
	@JsonProperty("firstName")
	private ArrayList firstName;
	
	@JsonProperty("lastName")
	private ArrayList lastName;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("dateTime")
	private String dateTime;
	
	@JsonProperty("lDateTime")
	private long lDateTime;
	
	@JsonProperty("isSend")
	private String isSend;
	
	@JsonProperty("sendDateTime")
	private String sendDateTime;
	
	@JsonProperty("receivedTimestamp")
	private String receivedTimestamp;
	
	@JsonProperty("clientId")
	private long clientId;
	
	@JsonProperty("fromDate")
	private String fromDate;
	
	@JsonProperty("toDate")
	private String toDate;
	
}
