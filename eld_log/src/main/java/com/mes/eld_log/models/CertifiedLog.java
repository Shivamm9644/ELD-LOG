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
@Document(collection = "certified_log")
//@Document(collection = "certified_log_test")
public class CertifiedLog implements Serializable{
	private static final long serialVersionUID = 1L;

	private long driverId;
	private String localId;
	private String tokenNo;
	private long vehicleId;
	private ArrayList trailers;
	private ArrayList shippingDocs;
	private long coDriverId;
	private String certifiedDate;
	private long lCertifiedDate;
	private long certifiedDateTime;
	private long certifiedAt;
	
	private String certifiedSignature;
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
