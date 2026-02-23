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
@Document(collection = "login")
public class Login implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer employeeId;
	private String email;
	private String username;
	private String password;
	private String loginStatus;
	private long loginDateTime;
	private long logoutDateTime;
	private long mobileNo;
	private long dateTime;
	private String mobileDeviceId;
	private String isCoDriver;
	private String tokenNo;
	
}
