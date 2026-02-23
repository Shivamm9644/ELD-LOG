package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.DriveringStatus;

import lombok.Data;

@Data
public class AddDriveringStatusResponseDto {

	@JsonProperty("localId")
	private String localId;
	
	@JsonProperty("serverId")
	private String serverId;
}
