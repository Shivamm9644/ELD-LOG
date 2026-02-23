package com.mes.eld_log.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@EntityScan
//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "eld_support")
public class ELDSupport implements Serializable{
	private static final long serialVersionUID = 1L;

	private String tokenNo;
	private long driverId;
	private long vehicleId;
	private long companyId;
	private String message;
	private String status;
	private long utcDateTime;
	private String remark;
	private long receivedTimestamp;
	
}
