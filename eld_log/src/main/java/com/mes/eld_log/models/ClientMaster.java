package com.mes.eld_log.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
@Document(collection = "client_master")
public class ClientMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer clientId;
	private String companyId;
	private String clientName; // company name
	private String dotNo; 
	private long timezoneId; 
	private String street;
	private String city;
	private long countryId;
	private long stateId;
	private long zipcode;
	private ArrayList terminalData;
	private String complianceMode;
	private String vehicleMotionThresold;

	private String exemptDriver;
	private long cycleUsaId;
	private long cargoTypeId;
	private long restartId;
	private long restBreakId;
	private String shortHaulException;
	private String personalUse;
	private String yardMoves;
	private String allowTracking;
	private String allowGpsTracking;
	private String allowIfta;
	private String project44;
	private String microPoint;
	private String status;
	
//	private String currency;
//	private long contactNo;
//	private String contactEmail;
//	private String address;
//	private long countryId;
//	private long stateId;
//	private long cityId;
//	private long zipcode;
//	private long paymentStatusId;
//	private String status;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	private long subscriptionEndTime;
	private long graceTime;
	
}
