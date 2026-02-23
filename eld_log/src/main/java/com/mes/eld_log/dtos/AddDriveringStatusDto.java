package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.DriveringStatus;
import com.mes.eld_log.models.ELDLogData;
import com.mes.eld_log.models.SplitLog;

import lombok.Data;

@Data
public class AddDriveringStatusDto {
	
	@JsonProperty("driveringStatusData")
	private ArrayList<DriveringStatus> driveringStatusData;
	
	@JsonProperty("eldLogData")
	private ArrayList<ELDLogData> eldLogData;
	
	@JsonProperty("splitLog")
	private SplitLog splitLog;
	
}
