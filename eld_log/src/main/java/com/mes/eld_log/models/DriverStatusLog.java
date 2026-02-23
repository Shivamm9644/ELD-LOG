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
@Document(collection = "driver_status_log")
public class DriverStatusLog implements Serializable{
	private static final long serialVersionUID = 1L;

	private long statusId;
	private String logDataId;
	private long driverId;
	private long vehicleId;
	private long clientId;
	private String status;
	private double lattitude;
	private double longitude;
	private long dateTime;
	private String logType;
	private String engineHour;
	private String origin;
	private double odometer;
	private Integer isVoilation;
	private String note;
	private String customLocation;
	private Integer isReportGenerated;
	private long receivedTimestamp;
	private Integer isVisible;

}
