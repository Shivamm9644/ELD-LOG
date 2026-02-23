package com.mes.eld_log.models;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.index.Indexed;
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
@Document(collection = "employee_master")
public class EmployeeMaster implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer employeeId;
	private String title;
	private String firstName;
	private String lastName;
//	@NotNull(message = "Email is required")
//    @Email(message = "Invalid email format")
//    @Indexed(unique = true)
	private String email;
	private String username;
	private String status;
	
	private String driverId;
	private String password;
	private long restartId;
	private long restBreakId;
	private String shortHaulException;
	private String unlimitedTrailers;
	private String unlimitedShippingDocs;
		
	private long companyId;
	private long languageId;
	
	private long clientId;
	
	private String startTime;
	private long cycleUsaId;
	private long cycleCanadaId;
	private long mainTerminalId;
	private long mobileNo;
	private String cdlNo;
	private long cdlCountryId;
	private long cdlStateId;
	private String cdlExpiryDate;
	private long lCdlExpiryDate;
	private String pdfEmail;
	private double flatRate;
	private String exempt;
	private String personalUse;
	private String yardMoves;
	private String divR;
	private long cargoTypeId;
	private long truckNo;
	private String manageEquipement;
	private String transferLog;
	private String remarks;
	private String workingStatus;
	
	private String isFirstLogin;
	private Integer disclaimerRead;
	
	private long addedTimestamp;
	private long updatedTimestamp;
	
}
