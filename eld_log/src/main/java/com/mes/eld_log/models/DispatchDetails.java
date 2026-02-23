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
@Document(collection = "dispatch_details")
public class DispatchDetails implements Serializable{
	private static final long serialVersionUID = 1L;

	private long clientId;
	private String clientName;
	
	private String load;
	private long customerId;
	private String customerReferenceNo;
	private String dispatcher;
	private String loadEnterBy;
	private String currency;
	private double flatRate;
	private double extraPD;
	private double fuelSurCharge;
	private double extraCharge;
	private double otherCharge;
//	private ArrayList otherChargeData;
	private double dudectionTotal;
	private double reimbursementTotal;
	private double total;
	
	private String dispatchBy;
	
	private long driverId;
	private long coDriverId;
	private long driverTruckId;
	private String driverTruck;
	private long driverTrailerId;
	private String driverTrailer;
	private double driverRate;
	private double totalMiles;
	
	private long carrierId;
	private String carrierTruck;
	private String carrierTrailer;
	private long equipmentType;
	private double carrierRate;
	private double carrierOtherCharges;
	
//	private ArrayList shipperData;
//	private ArrayList receiverData;
	
	private String isDataSaveAsDraft;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
