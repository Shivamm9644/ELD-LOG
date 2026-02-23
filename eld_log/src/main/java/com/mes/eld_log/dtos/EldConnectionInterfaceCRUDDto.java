package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EldConnectionInterfaceCRUDDto {
	
	@JsonProperty("eldConnectionInterfaceId")
	private Integer eldConnectionInterfaceId;

}
