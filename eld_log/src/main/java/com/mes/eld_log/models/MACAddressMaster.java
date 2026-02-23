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
@Document(collection = "mac_address_master")
public class MACAddressMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private long driverId;
	private String tokenNo;
	private long vehicleId;
	private String macAddress;
	private String serialNo;
	private String version;
	private String modelNo;
	private String deviceStatus;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
