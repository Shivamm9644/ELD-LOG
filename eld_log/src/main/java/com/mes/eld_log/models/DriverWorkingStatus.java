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
@Document(collection = "driver_working_status")
public class DriverWorkingStatus implements Serializable{
	private static final long serialVersionUID = 1L;

	private long driverId;
	private Integer shift;
	private Integer days;
	private String status;
	private String onDutyTime;
	private String onDriveTime;
	private String onSleepTime;
	private String weeklyTime;
	private String onBreak;
	private String tokenNo;
	
	private long receivedTimestamp;
	
}
