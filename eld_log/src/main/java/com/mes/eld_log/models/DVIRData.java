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
@Document(collection = "dvir_data")
//@Document(collection = "dvir_data_test")
public class DVIRData implements Serializable{
	private static final long serialVersionUID = 1L;

	private String tokenNo;
	private String localId;
	private long driverId;
	private long vehicleId;
	private long clientId;
	private String dateTime;
	private long lDateTime;
	private String location;
	private ArrayList truckDefect;
	private ArrayList trailerDefect;
	private String notes;
	private String vehicleCondition;
	private String driverSignFile;
	private String companyName;
	private double odometer;
	private String engineHour;
	private ArrayList trailer;
	
	private String timestamp;
	
	private long receivedTimestamp;
	
}
