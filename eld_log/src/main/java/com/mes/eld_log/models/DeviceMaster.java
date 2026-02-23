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
@Document(collection = "device_master")
public class DeviceMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer deviceId;
	private long clientId;
	private long vehicleId;
	private String deviceNo;
	private String macId;
	private String serialNo;
	private long deviceModelId;
	private String billingDate;
	private long lBillingDate;
	private long warranty;
	private String warrantyFromDate;
	private long lWarrantyFromDate;
	private String warrantyToDate;
	private long lWarrantyToDate;
	private String status;
	private String malFunction;
	private String fwVersion;
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
