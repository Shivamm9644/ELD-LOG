package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.DVIRData;
import com.mes.eld_log.models.DefectDetails;

import lombok.Data;

@Data
public class AddDvirDataDto {

	@JsonProperty("dvirStatusData")
	private ArrayList<DVIRData> dvirStatusData;
	
	@JsonProperty("defectData")
	private ArrayList<DefectDetails> defectData;
	
}
