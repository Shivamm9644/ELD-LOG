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
@Document(collection = "shipper_receiver_details")
public class ShipperReceiverDetails implements Serializable{
	private static final long serialVersionUID = 1L;

	private String dispatchId;
	private long shipperId;
	private long receiverId;
	private String address;
	private long countryId;
	private long stateId;
	private long cityId;
	private String date;
	private String time;
	private String pickupNo;
	private String receivingNo;
	private String comodity;
	private double reeferTemp;
	
	private long referModeId;
	private long caseCount;
	private long pallets;
	private double weight;
	private String shippingNotes;
	private String contactPerson;
	private String mobileNo;
	private String order;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
