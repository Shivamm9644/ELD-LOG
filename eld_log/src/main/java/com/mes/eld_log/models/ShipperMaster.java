
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
@Document(collection = "shipper_master")
public class ShipperMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer shipperId;
	private String shipperName;
	private String shipperStartTime;
	private String shipperEndTime;
	private String appointment;
	private String physicalAddress;
	private long clientId;
	private long countryId;
	private long stateId;
	private long cityId;
	private long zipcode;
	private long contactNo;
	private String contactPerson;

	private String status;
	private String remarks;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
