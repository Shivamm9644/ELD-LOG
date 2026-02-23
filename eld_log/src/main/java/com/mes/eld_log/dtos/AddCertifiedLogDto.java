package com.mes.eld_log.dtos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mes.eld_log.models.CertifiedLog;

import lombok.Data;

@Data
public class AddCertifiedLogDto {

	@JsonProperty("certifiedLogData")
	private ArrayList<CertifiedLog> certifiedLogData;
	
}
