package com.mes.eld_log.models;

import java.io.Serializable;

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
@Document(collection = "cycle_usa_master")
public class CycleUsa implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer cycleUsaId;
	private String cycleUsaName;
	private long countryId;
	private long stateId;
	private long clientId;
	private long cycleHour;
	private long cycleDays;
	private long onDutyTime;
	private long onDriveTime;
	private long onSleepTime;
	private long continueDriveTime;
	private long breakTime;
	private long cycleRestartTime;
	private long warningTime1;
	private long warningTime2;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
