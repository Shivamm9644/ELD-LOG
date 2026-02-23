package com.mes.eld_log.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.LoginLog;

import lombok.Data;

@Data
public class ViewDriverLogWithDetailDto {
	
	@JsonProperty("driverLog")
	private List<DriveringStatusViewDto> driverLog;
	
}
