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
@Document(collection = "live_data_log")
public class LiveDataLog implements Serializable{
	private static final long serialVersionUID = 1L;

	private String MAC;
	private long DateTime;
	private String DriverId;
	private double Lattitude;
	private double Longitude;
	private String PlaceAddress;
	private String Model;
	private String Odometer;
	private long clientId;
	private String SerialNo;
	private long Speed;
	private String VIN;
	private String VehicleId;
	private String Version;

	private long receive_time;
	
}
