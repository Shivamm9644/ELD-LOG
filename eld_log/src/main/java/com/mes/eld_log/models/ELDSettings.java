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
@Document(collection = "eld_settings")
public class ELDSettings implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer settingId;
	private String eldIdentifier;
	private String eldProvider;
	private String eldSoftwareVersion;
	private String eldRegistrationId;
	private String googleApiUrl;
	private String smsApiUrl;
	private String androidVersion;
	private String androidCode;
	private String iosVersion;
	private String iosCode;
	private String termsAndCondition;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
