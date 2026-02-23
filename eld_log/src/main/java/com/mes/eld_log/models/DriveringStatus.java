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
@Document(collection = "drivering_status")
//@Document(collection = "drivering_status_offline")
public class DriveringStatus implements Serializable{
	private static final long serialVersionUID = 1L;

	private long statusId;
	private String tokenNo;
	private long driverId;
	private long vehicleId;
	private long clientId;
	private String status;
	private double lattitude;
	private double longitude;
	private String dateTime;
	private long lDateTime;
	private long utcDateTime;
	private String logType;
	private String appVersion;
	private String osVersion;
	private Integer isVoilation;
	private Integer voilationHour;
	private String simCardNo;
	private String note;
	private String customLocation;
	private String currentLocation;
	private String engineHour;
	private String engineStatus;
	private String origin;
	private double odometer;
	private Integer identifier;
	
	private String timezone;
	private long remainingWeeklyTime;
	private long remainingDutyTime;
	private long remainingDriveTime;
	private long remainingSleepTime;
	
	private String localId;
	
	private Integer shift;
	private Integer days;
	private Integer isReportGenerated;
	private Integer isSplit;
	private Integer isVisible;
	private String email;
	
	private long receivedTimestamp;
	private long updatedTimestamp;
}
