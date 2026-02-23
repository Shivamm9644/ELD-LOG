package com.mes.eld_log.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EldLogDataViewDto {
	
	@JsonProperty("Model")
	private String Model;
	
	@JsonProperty("DriverId")
	private long DriverId;
	
	@JsonProperty("VehicleId")
	private long VehicleId;
	
	@JsonProperty("DeviceType")
	private String DeviceType;
	
	@JsonProperty("Lattitude")
	private double Lattitude;
	
	@JsonProperty("Longitude")
	private double Longitude;
	
	@JsonProperty("PlaceAddress")
	private String PlaceAddress;
	
	@JsonProperty("SerialNo")
	private String SerialNo;
	
	@JsonProperty("MAC")
	private String MAC;
	
	@JsonProperty("Version")
	private String Version;
	
	@JsonProperty("VIN")
	private String VIN;
	
	@JsonProperty("UpTime")
	private String UpTime;
	
	@JsonProperty("Event")
	private String Event;
	
	@JsonProperty("DateTime")
	private String DateTime;
	
	@JsonProperty("LatLong")
	private String LatLong;
	
	@JsonProperty("Heading")
	private String Heading;
	
	@JsonProperty("SatStatus")
	private String SatStatus;
	
	@JsonProperty("Odometer")
	private String Odometer;
	
	@JsonProperty("Velocity")
	private String Velocity;
	
	@JsonProperty("EngineHours")
	private String EngineHours;
	
	@JsonProperty("RPM")
	private String RPM;
	
	@JsonProperty("Gear")
	private String Gear;
	
	@JsonProperty("Bus")
	private String Bus;
	
	@JsonProperty("SeatBelt")
	private String SeatBelt;
	
	@JsonProperty("BreakBelt")
	private String BreakBelt;
	
	@JsonProperty("Speed")
	private String Speed;
	
	@JsonProperty("Retarder")
	private String Retarder;
	
	@JsonProperty("DTC")
	private String DTC;
	
	@JsonProperty("OilPressure")
	private String OilPressure;
	
	@JsonProperty("OilLevel")
	private String OilLevel;
	
	@JsonProperty("OilTemp")
	private String OilTemp;
	
	@JsonProperty("CoolantTemp")
	private String CoolantTemp;
	
	@JsonProperty("CoolantLevel")
	private String CoolantLevel;
	
	@JsonProperty("FuelLevel")
	private String FuelLevel;
	
	@JsonProperty("FuelLevelTank2")
	private String FuelLevelTank2;
	
	@JsonProperty("DEFLevel")
	private String DEFLevel;
	
	@JsonProperty("Load")
	private String Load;
	
	@JsonProperty("AmbPressure")
	private String AmbPressure;
	
	@JsonProperty("IntakeTemp")
	private String IntakeTemp;
	
	@JsonProperty("IntakePressure")
	private String IntakePressure;
	
	@JsonProperty("FuelTankTemp")
	private String FuelTankTemp;
	
	@JsonProperty("IntercoolerTemp")
	private String IntercoolerTemp;
	
	@JsonProperty("TurboOilTemp")
	private String TurboOilTemp;
	
	@JsonProperty("TransmOilTemp")
	private String TransmOilTemp;
	
	@JsonProperty("FuelRate")
	private String FuelRate;
	
	@JsonProperty("FuelEconomy")
	private String FuelEconomy;
	
	@JsonProperty("AmbTemp")
	private String AmbTemp;
	
	@JsonProperty("IdleHours")
	private String IdleHours;
	
	@JsonProperty("PTO")
	private String PTO;
	
	@JsonProperty("TotalFuelIdle")
	private String TotalFuelIdle;
	
	@JsonProperty("TotalFuelUsed")
	private String TotalFuelUsed;
	
	@JsonProperty("receive_time")
	private String receive_time;
	
	@JsonProperty("receive_data_time")
	private String receive_data_time;

	
	
}
