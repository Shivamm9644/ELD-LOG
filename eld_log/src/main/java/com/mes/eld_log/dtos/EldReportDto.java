package com.mes.eld_log.dtos;

import lombok.Data;
import java.util.List;

@Data
public class EldReportDto {
    private EldHeader eldHeader;
    private Carrier carrier;
    private Driver driver;
    private Vehicle vehicle;
    private List<EventRecord> eventRecords;
    private List<Annotation> annotations;
    private List<Object> malfunctions;
    private List<Object> diagnostics;
    private String signature;

    @Data
    public static class EldHeader {
        private String eldIdentifier;
        private String eldProvider;
        private String eldSoftwareVersion;
        private String outputFileFormat;
        private String fileGeneratedTime;
        private String fileSignature;
    }

    @Data
    public static class Carrier {
        private String carrierName;
        private String usdotNumber;
        private String homeTerminal;
    }

    @Data
    public static class Driver {
        private String driverId;
        private String driverName;
        private String licenseNumber;
        private String licenseState;
    }

    @Data
    public static class Vehicle {
        private String unitNumber;
        private String vin;
        private String odometerStart;
        private String odometerEnd;
        private String trailerNo;
    }

    @Data
    public static class EventRecord {
        private String eventDate;
		private String eventCode;
        private String eventDescription;
        private String eventTime;
        private String location;
        private String odometer;
        private String engineHours;
		private String eventType;
		private String origin;
		
		
	    private String totalVehicleMiles;
	    private String totalEngineHours;
	   
		private String eventSeqId;
		
    }

    @Data
    public static class Annotation {
        private String eventRef;
        private String note;
	private String ShippingId;
        
    @Data
    public static class TrailerMaster{
    	private int trailerId; 
}
    }


}
