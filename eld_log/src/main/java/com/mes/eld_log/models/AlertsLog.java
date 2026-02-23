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
@Document(collection = "alerts_log")
//@Document(collection = "certified_log_test")
public class AlertsLog implements Serializable{
	private static final long serialVersionUID = 1L;

	private long driverId;
	private long vehicleId;
	private long clientId;
	private String MAC;
	private String model;
	private String serialNo;
	private String vin;
	private String version;
	private String odometer;
	private long startUtcDateTime;
	private long endUtcDateTime;
	private long durationInMillis;
	private String placeAddress;
	private String message;
	private int isRead;
	private String readByEmail;
	private long readingTimestamp;
	private double lattitude;
	private double longitude;
	
	private long addedTimestamp;
	
}
