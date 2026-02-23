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
@Document(collection = "customer_master")
public class CustomerMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer customerId;
	private String customerName;
	private long productId;
	private long clientId;
	private String billingAddress;
	private long billingCountryId;
	private long billingStateId;
	private long billingCityId;
	private long billingZipcode;
	private String contactEmail;
	private long contactPhone;
	
	private String physicalAddress;
	private long physicalCountryId;
	private long physicalStateId;
	private long physicalCityId;
	private long physicalZipcode;
	private String dispatchEmail;
	private long dispatchPhone;
	
	private long afterHoursPhone;
	private String accountingEmail;
	private long accountingPhone;
	private String status;
	private String remarks;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
