package com.mes.eld_log.models;

import java.io.Serializable;
import java.util.HashMap;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Document(collection = "ifta_reports")
public class IFTAReports implements Serializable{
	private static final long serialVersionUID = 1L;

	private String carrierName;
	private String fromDate;
	private String toDate;
	private String vehicleNo;
	private String vehicleId;
	private String driverId;
	private long clientId;
	private long stateId;
	private long utcDateTime;
	private String make;
	private String vin;
	private String model;
	private long manufacturingYear;
	private String stateName;
	private String stateCode;
	private double firstOdometer;
	private double lastOdometer;
	private double totalOdometer;
	private double Lattitude;
	private double Longitude;
	private double gpsKm;
	private long currentTimestamp;
	private String fileName;
	
}
