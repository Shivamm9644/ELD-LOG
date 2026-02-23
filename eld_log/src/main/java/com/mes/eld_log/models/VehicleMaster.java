package com.mes.eld_log.models;

import java.io.Serializable;

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
@Document(collection = "vehicle_master")
public class VehicleMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer vehicleId;
	private long clientId;
	private String vehicleNo;
	private String make;
	private String model;
	private long manufacturingYear;
	private String licensePlate;
	private long countryId;
	private long stateId;
	private String vin;
	private long fuelTypeId;
	private String status;
	private long deviceId;
	private long eldConnectionInterfaceId;
	
	
//	private String vehicleNo;
//	private long vehicleTypeId;
//	private long clientId;
//	private String licensePlate;
//	private long countryId;
//	private long stateId;
//	private String make;
//	private long manufacturingYear;
//	private long deviceId;
//	private long userId;
//	private String vin;
//	private String registartionDate;
//	private String pollutionExpiryDate;
//	private String fitnessExpiryDate;
//	private String insuranceExpiryDate;
//	private long lRegistartionDate;
//	private long lPollutionExpiryDate;
//	private long lFitnessExpiryDate;
//	private long lInsuranceExpiryDate;
//	private long fuelTypeId;
//	private String billingDate;
//	private long lBillingDate;
//	private long paymentStatusId;
//	private String serviceDate;
//	private long lServiceDate;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
	
}
