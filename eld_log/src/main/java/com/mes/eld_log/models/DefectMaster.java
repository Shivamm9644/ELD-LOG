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
@Document(collection = "defect_master")
public class DefectMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer defectId;
	private String defectName;
	private String defectType;
	private long clientId;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
