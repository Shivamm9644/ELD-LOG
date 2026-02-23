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
@Document(collection = "eld_log_data")
public class ELDLogData implements Serializable{
	private static final long serialVersionUID = 1L;

	private long CoolantTemp;
	private String DateTime;
	private long lDateTime;
	private String DriverId;
	private long clientId;
	private double EngineHours;
	private long FuelTankTemp;
	private String LatLong;
	private double Lattitude;
	private double Longitude;
	private String PlaceAddress;
	private String MAC;
	private String Model;
	private String Odometer;
	private long OilTemp;
	private long RPM;
	private String SerialNo;
	private long Speed;
	private String VIN;
	private String VehicleId;
	private String Version;
	private long utcDateTime;
	private long stateId;
	private long geoStateId;
	
	private long receive_time;
	private String receive_data_time;
	
}
