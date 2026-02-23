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
@Document(collection = "user_master")
public class UserMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer userId;
	private long disclaimerRead;
	private String username;
	private String password;
	private long userTypeId;
	private long clientId;
	private String firstName;
	private String lastName;
	private long mobileNo;
	private String email;
	private long countryId;
	private long stateId;
	private long cityId;
	private long zipcode;
	private String timezone;
	private String status;
	private String webAccess;
	private String mobileAccess;
	
	private String eldFeature;
	private String dispatchFeature;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
