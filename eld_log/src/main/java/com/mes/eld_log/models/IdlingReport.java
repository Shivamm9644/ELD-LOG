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
@Document(collection = "idling_report")
public class IdlingReport implements Serializable{
	private static final long serialVersionUID = 1L;

	private String driverId;
    private String vehicleId;
    private long clientId;
    private String VIN;
    private String Version;
    private String MAC;
	private String Model;
	private String SerialNo;
	private String engineState;
    private String startDateTime;
    private String endDateTime;
	private long startUtcDateTime;
	private long endUtcDateTime;
    private long durationMillis;
    private String startAddress;
    private String endAddress;
    private long startRPM;
    private long endRPM;
    private double startLattitude;
    private double startLongitude;
    private double endLattitude;
    private double endLongitude;
    private long startSpeed;
    private long endSpeed;
    private long startCoolantTemp;
    private long endCoolantTemp;
    private long startFuelTankTemp;
    private long endFuelTankTemp;
    private long startOilTemp;
    private long endOilTemp;
    private double startEngineHours;
    private double endEngineHours;
    private String startOdometer;
    private String endOdometer;
    private long startStateId;
    private long endStateId;
    private long startGeoStateId;
    private long endGeoStateId;

}
