package com.mes.eld_log.serviceImpl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Timestamp;

import static com.mes.eld_log.security.Constants.WEB_URL_BASE_PATH;
import static com.mes.eld_log.security.Constants.WEB_URL_FILE_UPLOAD;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mes.eld_log.dtos.CertifiedLogViewDto;
import com.mes.eld_log.dtos.CompanyTerminalDataViewDto;
import com.mes.eld_log.dtos.CycleTimeRulesViewDto;
import com.mes.eld_log.dtos.DVIRDataCRUDDto;
import com.mes.eld_log.dtos.DriveringStatusLogViewDto;
import com.mes.eld_log.dtos.DriveringStatusViewDto;
import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.LoginLogViewDto;
import com.mes.eld_log.dtos.LoginUpdateDto;
import com.mes.eld_log.dtos.MaxIdViewDto;
import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.dtos.UserMasterViewDto;
import com.mes.eld_log.models.ClientMaster;
import com.mes.eld_log.models.CompanyMaster;
import com.mes.eld_log.models.CountryMaster;
import com.mes.eld_log.models.CycleUsa;
import com.mes.eld_log.models.DriverStatusLog;
import com.mes.eld_log.models.ELDSettings;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.Login;
import com.mes.eld_log.models.LoginLog;
import com.mes.eld_log.models.MACAddressMaster;
import com.mes.eld_log.models.MainTerminalMaster;
import com.mes.eld_log.models.SplitLog;
import com.mes.eld_log.models.StateMaster;
import com.mes.eld_log.models.UserMaster;
import com.mes.eld_log.models.VehicleMaster;
import com.mes.eld_log.repo.CityMasterRepo;
import com.mes.eld_log.repo.ClientMasterRepo;
import com.mes.eld_log.repo.CountryMasterRepo;
import com.mes.eld_log.repo.CycleUsaRepo;
import com.mes.eld_log.repo.DriverStatusLogRepo;
import com.mes.eld_log.repo.ELDSettingsRepo;
import com.mes.eld_log.repo.EmployeeMasterRepo;
import com.mes.eld_log.repo.LoginLogRepo;
import com.mes.eld_log.repo.LoginRepo;
import com.mes.eld_log.repo.MACAddressMasterRepo;
import com.mes.eld_log.repo.MainTerminalMasterRepo;
import com.mes.eld_log.repo.SplitLogRepo;
import com.mes.eld_log.repo.StateMasterRepo;
import com.mes.eld_log.repo.UserMasterRepo;
import com.mes.eld_log.repo.VehicleMasterRepo;
import com.mes.eld_log.results.Result;
import com.mes.eld_log.results.ResultWrapper;
import com.mes.eld_log.service.UserInfoService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DuplicateKeyException;

@Service(value = "userInfoService")
public class UserInfoServiceImpl implements UserDetailsService,UserInfoService{
	
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private EmployeeMasterRepo employeeMasterRepo;
	
	@Autowired
	SplitLogRepo splitLogRepo;
	
	@Autowired
	ELDSettingsRepo eldSettingsRepo;
	
	@Autowired
	private DriverStatusLogRepo driverStatusLogRepo;
	
	@Autowired
	CycleUsaRepo cycleUsaRepo;
	
	@Autowired
	private CountryMasterRepo countryMasterRepo;
	
	@Autowired
	private StateMasterRepo stateMasterRepo;
	
	@Autowired
	private CityMasterRepo cityMasterRepo;
	
	@Autowired
	MainTerminalMasterRepo mainTerminalMasterRepo;
	
	@Autowired
	VehicleMasterRepo vehicleMasterRepo;
	
	@Autowired
	private UserMasterRepo userMasterRepo;
	
	@Autowired
	MACAddressMasterRepo macAddressMasterRepo;
	
	@Autowired
	private ClientMasterRepo clientMasterRepo;
	
	@Autowired
	LoginRepo loginRepo;
	
	@Autowired
	LoginLogRepo loginLogRepo;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
    public UserInfoServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
	
	@Override
	public ResultWrapper<EmployeeMasterCRUDDto> Login(UserLoginDto userLoginDto, String token) {
		String sDebug="";
		ResultWrapper<EmployeeMasterCRUDDto> result = new ResultWrapper<>();
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = new ArrayList<>();
			String timezoneName="",timezoneOffSet="", subscriptionExpire="";
			
			EmployeeMasterCRUDDto empInfo = null;
			MainTerminalMaster mainTerminal = null;
			String username = userLoginDto.getUsername();
			String password = userLoginDto.getPassword();
//			String mobileDeviceId = userLoginDto.getMobileDeviceId();
			sDebug+=" CRED : "+username+" :: "+password+",";
			List<Login> login = loginRepo.ValidateAndLogin(username,password);
			Instant instant = Instant.now();
			String status="", email="";
			if(login.size()>0) {
				email = login.get(0).getEmail();
				UserLoginDto loginData = loginRepo.GetLoginDataByEmail(email);
				empInfo = employeeMasterRepo.findEmployeeByEmail1(email);
				// check disclaimer
//				if(empInfo.getDisclaimerRead()==0) {
//					try {
//				        String disclaimerFilePath = WEB_URL_FILE_UPLOAD+"/disclaimer/disclaimer.txt"; 				        
//				        File disclaimerFile = new File(disclaimerFilePath);
//				        if (disclaimerFile.exists()) {
//				            String disclaimerText = new String(Files.readAllBytes(disclaimerFile.toPath()), StandardCharsets.UTF_8);
//				            empInfo.setDisclaimer(disclaimerText);
//				        } else {
//				        	empInfo.setDisclaimer("Disclaimer file not found.");
//				        }
//				    } catch (IOException e) {
//				    	empInfo.setDisclaimer("Error reading disclaimer file: " + e.getMessage());
//				    }
//					
//				}
				
				if(empInfo.getClientId()>0) {
					ClientMaster clientMaster = clientMasterRepo.findByClientId((int)empInfo.getClientId());
					status = clientMaster.getStatus();
					long graceTime = clientMaster.getGraceTime();
					
					StateMaster stateInfo = stateMasterRepo.findByStateId((int)clientMaster.getTimezoneId());
					timezoneOffSet=stateInfo.getTimezoneOffSet();
					timezoneOffSet = timezoneOffSet.trim()
                            .replace("\u2212", "-")
                            .replace("\uFF0B", "+");
					
					String splitStr[] = timezoneOffSet.split(":");
					long hour = Math.abs(Long.parseLong(splitStr[0]));
					long minutes = Math.abs(Long.parseLong(splitStr[1]));
					int totalMinutes = (int) ((hour * 60) + minutes);
					
					Clock cl = Clock.systemUTC(); 
					Clock adjustedClock;
					if (timezoneOffSet.startsWith("-")) {
					    adjustedClock = Clock.offset(cl, Duration.ofMinutes(-totalMinutes));
					} else {
					    adjustedClock = Clock.offset(cl, Duration.ofMinutes(totalMinutes));
					}
			        LocalDate lt = LocalDate.now(adjustedClock); 
			        LocalDateTime endOfDay = lt.atTime(LocalTime.MAX);
			        long endOfDayEpoch = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			        
			        long timestampValue=0;
					if(timezoneOffSet.substring(0,1).equals("-")) {
						timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
					}else {
						timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
					}
					
					if(timestampValue>graceTime) {
						// not login
						subscriptionExpire="true";
					}else {
						// login
						subscriptionExpire="false";
					}
				}
				if(subscriptionExpire.equals("false")) {
					if(empInfo.getStatus().equals("active") && status.equals("true")) {
						//update login status
						if(userLoginDto.getIsCoDriver()==null || userLoginDto.getIsCoDriver().equals("")) {
							Query query = new Query();
					        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
					        Update update = new Update();
//						        update.set("loginStatus", "true");
					        update.set("isCoDriver", "false");
					        update.set("tokenNo", token);
					        update.set("loginDateTime", instant.toEpochMilli());
					        mongoTemplate.findAndModify(query, update, Login.class);
						}else {
							Query query = new Query();
					        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
					        Update update = new Update();
//						        update.set("loginStatus", "true");
					        update.set("isCoDriver", userLoginDto.getIsCoDriver());
					        update.set("tokenNo", token);
					        update.set("loginDateTime", instant.toEpochMilli());
					        mongoTemplate.findAndModify(query, update, Login.class);
						}
						
						List<ELDSettings> settings =  eldSettingsRepo.findAndViewBySettingId(1);
						if(settings.size()>0) {
							empInfo.setAndroidVersion(settings.get(0).getAndroidVersion());
							empInfo.setAndroidCode(settings.get(0).getAndroidCode());
							empInfo.setIosVersion(settings.get(0).getIosVersion());
							empInfo.setIosCode(settings.get(0).getIosCode());
							empInfo.setTermsAndCondition(settings.get(0).getTermsAndCondition());
						}else {
							empInfo.setAndroidVersion("");
							empInfo.setAndroidCode("");
							empInfo.setIosVersion("");
							empInfo.setIosCode("");
							empInfo.setTermsAndCondition("");
						}
						
						EmployeeMaster empDetailData = employeeMasterRepo.findByEmployeeId((int)login.get(0).getEmployeeId());
						empInfo.setExempt(empDetailData.getExempt());
						empInfo.setPersonalUse(empDetailData.getPersonalUse());
						empInfo.setYardMoves(empDetailData.getYardMoves());
						empInfo.setShortHaulException(empDetailData.getShortHaulException());
						empInfo.setUnlimitedTrailers(empDetailData.getUnlimitedTrailers());
						empInfo.setUnlimitedShippingDocs(empDetailData.getUnlimitedShippingDocs());
						
						CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetailData.getCycleUsaId());
						empInfo.setOnDutyTime(cycleUsaData.getOnDutyTime());
						empInfo.setOnDriveTime(cycleUsaData.getOnDriveTime());
						empInfo.setOnSleepTime(cycleUsaData.getOnSleepTime());
						empInfo.setContinueDriveTime(cycleUsaData.getContinueDriveTime());
						empInfo.setBreakTime(cycleUsaData.getBreakTime());
						empInfo.setCycleRestartTime(cycleUsaData.getCycleRestartTime());
						
				        loginData = loginRepo.GetLoginDataByEmail(email);
				        sDebug+=" >> ";
				        try {
							MACAddressMaster macAddressData = macAddressMasterRepo.findByMACAddressMasterId(login.get(0).getEmployeeId());
							sDebug+="1,";
							empInfo.setMacAddress(macAddressData.getMacAddress());
							empInfo.setVehicleId(macAddressData.getVehicleId());
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) macAddressData.getVehicleId());
							sDebug+="2,";
							empInfo.setVehicleNo(vehcileInfo.getVehicleNo());
				        } catch(Exception ex) {
				        	ex.printStackTrace();
				        	empInfo.setMacAddress("");
							empInfo.setVehicleId(0);
							empInfo.setVehicleNo("None");
				        }
				        try {	
				        	if(empInfo.getClientId()>0) {
				        		sDebug+="3,";
								ClientMaster client = clientMasterRepo.findByClientId((int)empInfo.getClientId());
								empInfo.setClientName(client.getClientName());
								
//									ObjectMapper objectMapper = new ObjectMapper();
//									String jsonString = objectMapper.writeValueAsString(clientMaster.getTerminalData().get(0));
//						            JSONObject obj = new JSONObject(jsonString);
								
//									long stateId = client.getTerminalData().get(0).getTerminalTimezoneId();
								sDebug+="4,";
								mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
								if(mainTerminal.getStateId()!=0) {
									sDebug+="5,";
									StateMaster states = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
									sDebug+="6,";
									empInfo.setTimezone(states.getTimeZone());
									empInfo.setTimezoneOffSet(states.getTimezoneOffSet());
								}else {
									empInfo.setTimezone("");
									empInfo.setTimezoneOffSet("");
								}
							}else {
								empInfo.setClientName("");
								empInfo.setTimezone("");
								empInfo.setTimezoneOffSet("");
							}
				        }catch(Exception ex) {
				        	ex.printStackTrace();
				        }
				        sDebug+="7,";
				        LoginLog loginLog = new LoginLog();
				        loginLog.setEmployeeId(login.get(0).getEmployeeId());
				        loginLog.setLoginDateTime(loginData.getLoginDateTime());
				        loginLog.setLogoutDateTime(0L);
				        loginLog.setIsCoDriver(loginData.getIsCoDriver());
				        loginLog.setReceivedTimestamp(instant.toEpochMilli());
				        loginLog.setLoginType("app");
				        loginLogRepo.save(loginLog);
				        
				        DriverStatusLog driverStatusLog = new DriverStatusLog();
				        long maxId = lookupMaxIdOfDriverOperation(login.get(0).getEmployeeId());
//						System.out.println(maxId);
						if(maxId<=0) {
							maxId=1;
							driverStatusLog.setStatusId(maxId);
						}else {
							maxId++;
							driverStatusLog.setStatusId(maxId);
						}
						
						Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query query = new Query(
						  Criteria.where("employeeId").is(login.get(0).getEmployeeId())
						);
						query.limit(1);
						query.with(pageableRequest);
						List<LoginLogViewDto> loginLogViewDto = mongoTemplate.find(query, LoginLogViewDto.class,"login_log");
						
						driverStatusLog.setLogDataId(loginLogViewDto.get(0).get_id());
						driverStatusLog.setDriverId(login.get(0).getEmployeeId());
						driverStatusLog.setVehicleId(0);
						driverStatusLog.setClientId(empInfo.getClientId());
						driverStatusLog.setStatus("Login");
						driverStatusLog.setLattitude(0);
						driverStatusLog.setLongitude(0);
						driverStatusLog.setDateTime(loginLogViewDto.get(0).getLoginDateTime());
						driverStatusLog.setLogType("Login");
						driverStatusLog.setEngineHour("0");
						driverStatusLog.setOrigin("");
						driverStatusLog.setOdometer(0);
						driverStatusLog.setIsVoilation(0);
						driverStatusLog.setNote("");
						driverStatusLog.setCustomLocation("");
						driverStatusLog.setIsReportGenerated(1);
						driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
						driverStatusLog.setIsVisible(1);
						
//						List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(loginLogViewDto.get(0).get_id());
//						if(logDataExist.size()<=0) {
//							driverStatusLogRepo.save(driverStatusLog);
//							UpdateDriverSequenceId(login.get(0).getEmployeeId(),loginLogViewDto.get(0).getLoginDateTime());
//						}
						
						empInfo.setLoginDateTime(loginData.getLoginDateTime());
						
						LocalDate periviousDate = LocalDate.now();
						periviousDate = periviousDate.minusDays(14);
						String fromDate = periviousDate.toString()+" 00:00:00";
						String toDate = LocalDate.now().toString()+" 23:59:59";

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
						LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
						long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
						long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						String driverName="";
						String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
						String setImagePath1;
						List<DriveringStatusViewDto> driveringStatusViewDtoData = lookupDriverStatusDataOperation(from,to,login.get(0).getEmployeeId());
						for(int i=0;i<driveringStatusViewDtoData.size();i++) {
							driveringStatusViewDto.add(driveringStatusViewDtoData.get(i));
						}
						for(int i=0;i<driveringStatusViewDto.size();i++) {
							driveringStatusViewDto.get(i).setFromDate(from);
							driveringStatusViewDto.get(i).setToDate(to);
//								System.out.println(driverId);
							driveringStatusViewDto.get(i).setLastOnSleepTime(String.valueOf((cycleUsaData.getOnSleepTime()*60*60)));
							try {
								if(driveringStatusViewDto.get(i).getLogType().equals("System Generated")) {
									driveringStatusViewDto.get(i).setIsSystemGenerated(1);
								}
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//									System.out.println(empDetails);
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								driveringStatusViewDto.get(i).setDriverName(driverName);
								driveringStatusViewDto.get(i).setMobileNo(empDetails.getMobileNo());
								driveringStatusViewDto.get(i).setEmail(empDetails.getEmail());
//									driveringStatusViewDto.get(i).setCompanyDriverId(empDetails.getDriverId());
								driveringStatusViewDto.get(i).setCompanyDriverId(empDetails.getUsername());

								driveringStatusViewDto.get(i).setCdlNo(empDetails.getCdlNo());
								
								CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empDetails.getCdlCountryId());
								driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
								StateMaster stateInfo = stateMasterRepo.findByStateId((int)empDetails.getCdlStateId());
								driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
								
								driveringStatusViewDto.get(i).setExempt(empDetails.getExempt());
								
								VehicleMaster vehcileInfo = null;
								if(driveringStatusViewDto.get(i).getVehicleId()>0) {
									vehcileInfo = vehicleMasterRepo.findByVehicleId((int)driveringStatusViewDto.get(i).getVehicleId());
									driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
									driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
								}else {
									driveringStatusViewDto.get(i).setTruckNo("");
									driveringStatusViewDto.get(i).setVin("");
								}
								
								
								mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empDetails.getMainTerminalId());
								driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
								
								if(mainTerminal.getStateId()>0) {
									stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
									timezoneName=stateInfo.getTimeZone();
									timezoneOffSet=stateInfo.getTimezoneOffSet();;
								}
								
								CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
								driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
								
								if(empDetails.getClientId()>0) {
									ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empDetails.getClientId());
									driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
									driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
								}
								
								List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driveringStatusViewDto.get(i).getDriverId());
								if(certifiedLogViewDto.size()>0) {
									for(int c=0;c<certifiedLogViewDto.size();c++) {
										if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
											empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//												driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getDriverId());
											driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getUsername());
											driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
										}
										
										driveringStatusViewDto.get(i).setCoDriverName(driverName);
										driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
										driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
										
										setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
										driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
										
										vehcileInfo = vehicleMasterRepo.findByVehicleId((int) certifiedLogViewDto.get(c).getVehicleId());
										driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
										driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
										
									}
								}else {
									driveringStatusViewDto.get(i).setTrailers(new ArrayList());
									driveringStatusViewDto.get(i).setShippingDocs(new ArrayList());
								}
								
								

							}catch(Exception ex) {
								ex.printStackTrace();
							}
			        	
						}
						
						//check last status and set shift and days
						
//						Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
//						Query query = new Query(
//						  Criteria.where("driverId").is(login.get(0).getEmployeeId())
//						  .and("utcDateTime").lte(to)
//						);
//						
//						query.limit(1);
//						query.with(pageableRequest);
//						driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
//						if(driveringStatusViewDtoData.size()>0) {
//							String splitStr[] = timezoneOffSet.split(":");
//							long hour = Math.abs(Long.parseLong(splitStr[0]));
//							long minutes = Math.abs(Long.parseLong(splitStr[1]));
////							System.out.println(Long.parseLong(splitStr[0]));
//							String minusPlus="";
//							long timestampValue=0;
//							if(timezoneOffSet.substring(0,1).equals("-")) {
//								minusPlus = "minus";
//								timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
//							}else {
//								minusPlus = "plus";
//								timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
//							}
//							
//							long milliseconds = timestampValue - driveringStatusViewDtoData.get(0).getUtcDateTime();
//							long seconds = milliseconds / 1000;
//							long hours = seconds / 3600;
//							if(hours>=10 && hour<34) {
//								int days = driveringStatusViewDtoData.get(0).getDays();
//								days+=1;
//								driveringStatusViewDtoData.get(0).setDays(days);
//							}else if(hours>=34) {
//								driveringStatusViewDtoData.get(0).setDays(0);
//								driveringStatusViewDtoData.get(0).setShift(0);
//							}
//							
//							driveringStatusViewDtoData.get(0).setUtcDateTime(timestampValue);
//							driveringStatusViewDto.add(driveringStatusViewDtoData.get(0));
//						}
						
						List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(from,to,login.get(0).getEmployeeId());
						try {
							for(int i=0;i<dvirDataViewDto.size();i++) {
								setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
								dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
								
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)login.get(0).getEmployeeId());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								dvirDataViewDto.get(i).setDriverName(driverName);
								
								try {
									if(dvirDataViewDto.get(i).getVehicleId()>0) {
										VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
										dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
									}
								}catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						
						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,login.get(0).getEmployeeId());
						try {
							for(int i=0;i<certifiedLogViewDto.size();i++) {
								setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
								certifiedLogViewDto.get(i).setCertifiedSignature(setImagePath1);
								
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)login.get(0).getEmployeeId());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								certifiedLogViewDto.get(i).setDriverName(driverName);
								
								if(certifiedLogViewDto.get(i).getCoDriverId()>0) {
									empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(i).getCoDriverId());
									driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
									certifiedLogViewDto.get(i).setCoDriverName(driverName);
								}
								if(certifiedLogViewDto.get(i).getVehicleId()>0) {
									VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)certifiedLogViewDto.get(i).getVehicleId());
									certifiedLogViewDto.get(i).setVehicleName(vehcileInfo.getVehicleNo());
								}
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						
						List<DriveringStatusLogViewDto> loginLogoutLogViewDto = new ArrayList<>();
						DriveringStatusLogViewDto driveringStatusLogData = new DriveringStatusLogViewDto();
						List<LoginLog> loginLogData = lookupLoginLogDataOperation(from,to,login.get(0).getEmployeeId());
						String employeeStatus="";
						for(int i=0;i<loginLogData.size();i++) {
							try {
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)loginLogData.get(i).getEmployeeId());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								employeeStatus = empInfo.getStatus();
								
								if(loginLogData.get(i).getLoginDateTime()>0) {
									driveringStatusLogData = new DriveringStatusLogViewDto();
									driveringStatusLogData.setDriverId(loginLogData.get(i).getEmployeeId());
									driveringStatusLogData.setDriverName(driverName);
									driveringStatusLogData.setLattitude(0);
									driveringStatusLogData.setLongitude(0);
									driveringStatusLogData.setCustomLocation("");
									driveringStatusLogData.setOrigin("");
									driveringStatusLogData.setOdometer(0);
									driveringStatusLogData.setEngineHour("0");
									driveringStatusLogData.setNote("");
									driveringStatusLogData.setIsVoilation(0);
									driveringStatusLogData.setLogType("Login");
									driveringStatusLogData.setStatusId(0);
									
									driveringStatusLogData.setDateTime(String.valueOf(loginLogData.get(i).getLoginDateTime()));
									driveringStatusLogData.setStatus("Login");
									driveringStatusLogData.setEmployeeStatus(employeeStatus);
									
									loginLogoutLogViewDto.add(driveringStatusLogData);
								}
								if(loginLogData.get(i).getLogoutDateTime()>0) {
									driveringStatusLogData = new DriveringStatusLogViewDto();
									driveringStatusLogData.setDriverId(loginLogData.get(i).getEmployeeId());
									driveringStatusLogData.setDriverName(driverName);
									driveringStatusLogData.setLattitude(0);
									driveringStatusLogData.setLongitude(0);
									driveringStatusLogData.setCustomLocation("");
									driveringStatusLogData.setOrigin("");
									driveringStatusLogData.setOdometer(0);
									driveringStatusLogData.setEngineHour("0");
									driveringStatusLogData.setNote("");
									driveringStatusLogData.setIsVoilation(0);
									driveringStatusLogData.setLogType("Logout");
									driveringStatusLogData.setStatusId(0);
									
									driveringStatusLogData.setDateTime(String.valueOf(loginLogData.get(i).getLogoutDateTime()));
									driveringStatusLogData.setStatus("Logout");
									driveringStatusLogData.setEmployeeStatus(employeeStatus);
									
									loginLogoutLogViewDto.add(driveringStatusLogData);
								}
								
							}catch(Exception ex) {
								ex.printStackTrace();
							}
						}
						
						 List<SplitLog> splitLogData = splitLogRepo.findSplitLogByDriverId(login.get(0).getEmployeeId());
						 
						empDetailData = employeeMasterRepo.findByEmployeeId((int)login.get(0).getEmployeeId());
						cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetailData.getCycleUsaId());
						List<CycleTimeRulesViewDto> cycleTimeRulesViewDto = new ArrayList<>();
						CycleTimeRulesViewDto cycleTimeRules = new CycleTimeRulesViewDto();
						long hourToSec = 60*60; long minToSec = 60;
						
						cycleTimeRules.setCycleTime(cycleUsaData.getCycleHour()*hourToSec);
						cycleTimeRules.setCycleDays(cycleUsaData.getCycleDays());
						cycleTimeRules.setOnDutyTime(cycleUsaData.getOnDutyTime()*hourToSec);
						cycleTimeRules.setOnDriveTime(cycleUsaData.getOnDriveTime()*hourToSec);
						cycleTimeRules.setOnSleepTime(cycleUsaData.getOnSleepTime()*hourToSec);
						cycleTimeRules.setContinueDriveTime(cycleUsaData.getContinueDriveTime()*hourToSec);
						cycleTimeRules.setBreakTime(cycleUsaData.getBreakTime()*minToSec);
						cycleTimeRules.setCycleRestartTime(cycleUsaData.getCycleRestartTime()*hourToSec);
						cycleTimeRules.setWarningOnDutyTime1((cycleUsaData.getOnDutyTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
						cycleTimeRules.setWarningOnDutyTime2((cycleUsaData.getOnDutyTime()*hourToSec)-(cycleUsaData.getWarningTime2()*minToSec));
						cycleTimeRules.setWarningOnDriveTime1((cycleUsaData.getOnDriveTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
						cycleTimeRules.setWarningOnDriveTime2((cycleUsaData.getOnDriveTime()*hourToSec)-(cycleUsaData.getWarningTime2()*minToSec));
						cycleTimeRules.setWarningBreakTime1((cycleUsaData.getContinueDriveTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
						cycleTimeRules.setWarningBreakTime2((cycleUsaData.getContinueDriveTime()*hourToSec)-(cycleUsaData.getWarningTime2()*minToSec));
						cycleTimeRulesViewDto.add(cycleTimeRules);
						
//						pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
//						query = new Query(
//						  Criteria.where("driverId").is(login.get(0).getEmployeeId())
//						  .and("utcDateTime").lte(to)
//						  .and("isVoilation").is(0)
//						);
//						query.limit(1);
//						query.with(pageableRequest);
//						driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
//						long lastLogUtcDataTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
//						
//						String splitStr[] = timezoneOffSet.split(":");
//						long hour = Math.abs(Long.parseLong(splitStr[0]));
//						long minutes = Math.abs(Long.parseLong(splitStr[1]));
//						String minusPlus="";
//						long timestampValue=0;
//						if(timezoneOffSet.substring(0,1).equals("-")) {
//							minusPlus = "minus";
//							timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
//						}else {
//							minusPlus = "plus";
//							timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
//						}
//						long diffInMillis = timestampValue - lastLogUtcDataTime;
//						long hours = diffInMillis / (1000 * 60 * 60);
//
//						if(hours>=34) {
//							empInfo.setRules(cycleTimeRulesViewDto);
//							
//							query = new Query();
//					        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
//					        Update update = new Update();
//					        update.set("tokenNo", "0");
//					        mongoTemplate.findAndModify(query, update, Login.class);
//						}else {
//							empInfo.setRules(new ArrayList<>());
//						}
						
						empInfo.setRules(cycleTimeRulesViewDto);
						empInfo.setDriverLog(driveringStatusViewDto);
						empInfo.setDriverDvirLog(dvirDataViewDto);
						empInfo.setDriverCertifiedLog(certifiedLogViewDto);
						empInfo.setLoginLogoutLog(loginLogoutLogViewDto);
						empInfo.setSplitLog(splitLogData);
				        
						result.setResult(empInfo);
						result.setToken(token);
						result.setStatus(Result.SUCCESS);
						result.setMessage("Logged In Successfully"+sDebug);
				        
					}else {
						result.setResult(null);
						result.setToken(null);
						result.setStatus(Result.FAIL);
						result.setMessage("Your account has been deactivated, please contact to admin.");
					}
				}else {
					result.setResult(null);
					result.setToken(null);
					result.setStatus(Result.FAIL);
					result.setMessage("You are not authorized for login.");
					
				}	
			}else {
				result.setResult(null);
				result.setToken(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid username or password");
			}
						
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public void UpdateDriverSequenceId(long driverId, long utcDateTime) {
	    String sDebug = "SEQ ID : ";
	    try {
	        long baseStatusId = 0;
	        Query lastLogQuery = new Query(
	            Criteria.where("driverId").is(driverId)
	                    .and("dateTime").lt(utcDateTime)
	                    .and("isVoilation").is(0)
	                    .and("isVisible").is(1)
	        ).with(Sort.by(Sort.Direction.DESC, "dateTime")).limit(1);
	        List<DriverStatusLog> lastLogList = mongoTemplate.find(lastLogQuery, DriverStatusLog.class, "driver_status_log");
//	        sDebug+=" >> "+lastLogList+"\n";
	        if (!lastLogList.isEmpty()) {
	            baseStatusId = lastLogList.get(0).getStatusId();
	        }
	        sDebug+=" >> "+baseStatusId+" :: "+utcDateTime+"\n";
	        Query logsToUpdateQuery = new Query(
	            Criteria.where("driverId").is(driverId)
	                    .and("dateTime").gte(utcDateTime)
	                    .and("isVoilation").is(0)
	                    .and("isVisible").is(1)
	        ).with(Sort.by(Sort.Direction.ASC, "dateTime"));

	        List<DriverStatusLog> logsToUpdate = mongoTemplate.find(logsToUpdateQuery, DriverStatusLog.class, "driver_status_log");

	        for (int i = 0; i < logsToUpdate.size(); i++) {
	            long newStatusId = baseStatusId + i + 1; 
	            DriverStatusLog log = logsToUpdate.get(i);

	            sDebug += "status ID: " + newStatusId + " :: " + log.getStatus() + " :: " + log.getLogDataId() + "\n";

	            Query updateQuery = new Query(
	            		Criteria.where("logDataId").is(log.getLogDataId())
	            		.and("driverId").is(driverId)
        		);
	            Update update = new Update().set("statusId", newStatusId);
	            mongoTemplate.updateFirst(updateQuery, update, DriverStatusLog.class);
	        }

	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public ResultWrapper<EmployeeMasterCRUDDto> LoginDataByEmployeeId(UserLoginDto userLoginDto) {
		String sDebug="";
		ResultWrapper<EmployeeMasterCRUDDto> result = new ResultWrapper<>();
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = new ArrayList<>();
			String timezoneName="",timezoneOffSet="";
			
			EmployeeMasterCRUDDto empInfo = null;
			MainTerminalMaster mainTerminal = null;
			Integer employeeId = userLoginDto.getEmployeeId();
			Instant instant = Instant.now();
			
			empInfo = employeeMasterRepo.findEmployeeByEmployeeId1(employeeId);
					
			EmployeeMaster empDetailData = employeeMasterRepo.findByEmployeeId(employeeId);
			CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetailData.getCycleUsaId());
			empInfo.setOnDutyTime(cycleUsaData.getOnDutyTime());
			empInfo.setOnDriveTime(cycleUsaData.getOnDriveTime());
			empInfo.setOnSleepTime(cycleUsaData.getOnSleepTime());
			empInfo.setContinueDriveTime(cycleUsaData.getContinueDriveTime());
			empInfo.setBreakTime(cycleUsaData.getBreakTime());
			empInfo.setCycleRestartTime(cycleUsaData.getCycleRestartTime());
			
			empInfo.setTokenNo(userLoginDto.getTokenNo());
				
			UserLoginDto loginData = loginRepo.GetLoginDataByEmail(empDetailData.getEmail());
	        sDebug+=" >> ";
	        try {
				MACAddressMaster macAddressData = macAddressMasterRepo.findByMACAddressMasterId(employeeId);
				sDebug+="1,";
				empInfo.setMacAddress(macAddressData.getMacAddress());
				empInfo.setVehicleId(macAddressData.getVehicleId());
				VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) macAddressData.getVehicleId());
				sDebug+="2,";
				empInfo.setVehicleNo(vehcileInfo.getVehicleNo());
	        } catch(Exception ex) {
	        	ex.printStackTrace();
	        	empInfo.setMacAddress("");
				empInfo.setVehicleId(0);
				empInfo.setVehicleNo("None");
	        }
	        try {	
	        	if(empInfo.getClientId()>0) {
	        		sDebug+="3,";
					ClientMaster client = clientMasterRepo.findByClientId((int)empInfo.getClientId());
					empInfo.setClientName(client.getClientName());
					
					sDebug+="4,";
					mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
					if(mainTerminal.getStateId()!=0) {
						sDebug+="5,";
						StateMaster states = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
						sDebug+="6,";
						empInfo.setTimezone(states.getTimeZone());
						empInfo.setTimezoneOffSet(states.getTimezoneOffSet());
					}else {
						empInfo.setTimezone("");
						empInfo.setTimezoneOffSet("");
					}
				}else {
					empInfo.setClientName("");
					empInfo.setTimezone("");
					empInfo.setTimezoneOffSet("");
				}
	        }catch(Exception ex) {
	        	ex.printStackTrace();
	        }
				
			empInfo.setLoginDateTime(loginData.getLoginDateTime());
				
			LocalDate periviousDate = LocalDate.now();
			periviousDate = periviousDate.minusDays(14);
			String fromDate = periviousDate.toString()+" 00:00:00";
			String toDate = LocalDate.now().toString()+" 23:59:59";

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
			List<DriveringStatusViewDto> driveringStatusViewDtoData = lookupDriverStatusDataOperation(from,to,employeeId);
			for(int i=0;i<driveringStatusViewDtoData.size();i++) {
				driveringStatusViewDto.add(driveringStatusViewDtoData.get(i));
			}
			
			for(int i=0;i<driveringStatusViewDto.size();i++) {
				driveringStatusViewDto.get(i).setFromDate(from);
				driveringStatusViewDto.get(i).setToDate(to);
//								System.out.println(driverId);
				driveringStatusViewDto.get(i).setLastOnSleepTime(String.valueOf((cycleUsaData.getOnSleepTime()*60*60)));
				try {
					if(driveringStatusViewDto.get(i).getLogType().equals("System Generated")) {
						driveringStatusViewDto.get(i).setIsSystemGenerated(1);
					}
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//									System.out.println(empDetails);
					driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
					driveringStatusViewDto.get(i).setDriverName(driverName);
					driveringStatusViewDto.get(i).setMobileNo(empDetails.getMobileNo());
					driveringStatusViewDto.get(i).setEmail(empDetails.getEmail());
//									driveringStatusViewDto.get(i).setCompanyDriverId(empDetails.getDriverId());
					driveringStatusViewDto.get(i).setCompanyDriverId(empDetails.getUsername());

					driveringStatusViewDto.get(i).setCdlNo(empDetails.getCdlNo());
					
					CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empDetails.getCdlCountryId());
					driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
					StateMaster stateInfo = stateMasterRepo.findByStateId((int)empDetails.getCdlStateId());
					driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
					
					driveringStatusViewDto.get(i).setExempt(empDetails.getExempt());
					
					VehicleMaster vehcileInfo = null;
					if(driveringStatusViewDto.get(i).getVehicleId()>0) {
						vehcileInfo = vehicleMasterRepo.findByVehicleId((int)driveringStatusViewDto.get(i).getVehicleId());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
					}else {
						driveringStatusViewDto.get(i).setTruckNo("");
						driveringStatusViewDto.get(i).setVin("");
					}
					
					
					mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empDetails.getMainTerminalId());
					driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
					
					if(mainTerminal.getStateId()>0) {
						stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
						timezoneName=stateInfo.getTimeZone();
						timezoneOffSet=stateInfo.getTimezoneOffSet();;
					}
					
					CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
					driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
					
					if(empDetails.getClientId()>0) {
						ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empDetails.getClientId());
						driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
						driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
					}
					
					List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driveringStatusViewDto.get(i).getDriverId());
					if(certifiedLogViewDto.size()>0) {
						for(int c=0;c<certifiedLogViewDto.size();c++) {
							if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
								empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//												driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getDriverId());
								driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getUsername());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
							}
							
							driveringStatusViewDto.get(i).setCoDriverName(driverName);
							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
							
							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
							
							vehcileInfo = vehicleMasterRepo.findByVehicleId((int) certifiedLogViewDto.get(c).getVehicleId());
							driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
							driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
							
						}
					}else {
						driveringStatusViewDto.get(i).setTrailers(new ArrayList());
						driveringStatusViewDto.get(i).setShippingDocs(new ArrayList());
					}
					
					

				}catch(Exception ex) {
					ex.printStackTrace();
				}
        	
			}
			
			List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(from,to,employeeId);
			try {
				for(int i=0;i<dvirDataViewDto.size();i++) {
					setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
					dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
					
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId(employeeId);
					driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
					dvirDataViewDto.get(i).setDriverName(driverName);
					
					try {
						if(dvirDataViewDto.get(i).getVehicleId()>0) {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
							dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
				
			List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,employeeId);
			try {
				for(int i=0;i<certifiedLogViewDto.size();i++) {
					setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
					certifiedLogViewDto.get(i).setCertifiedSignature(setImagePath1);
					
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId(employeeId);
					driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
					certifiedLogViewDto.get(i).setDriverName(driverName);
					
					if(certifiedLogViewDto.get(i).getCoDriverId()>0) {
						empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(i).getCoDriverId());
						driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
						certifiedLogViewDto.get(i).setCoDriverName(driverName);
					}
					if(certifiedLogViewDto.get(i).getVehicleId()>0) {
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)certifiedLogViewDto.get(i).getVehicleId());
						certifiedLogViewDto.get(i).setVehicleName(vehcileInfo.getVehicleNo());
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
				
			List<DriveringStatusLogViewDto> loginLogoutLogViewDto = new ArrayList<>();
			DriveringStatusLogViewDto driveringStatusLogData = new DriveringStatusLogViewDto();
			List<LoginLog> loginLogData = lookupLoginLogDataOperation(from,to,employeeId);
			String employeeStatus="";
			for(int i=0;i<loginLogData.size();i++) {
				try {
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)loginLogData.get(i).getEmployeeId());
					driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
					employeeStatus = empInfo.getStatus();
					
					if(loginLogData.get(i).getLoginDateTime()>0) {
						driveringStatusLogData = new DriveringStatusLogViewDto();
						driveringStatusLogData.setDriverId(loginLogData.get(i).getEmployeeId());
						driveringStatusLogData.setDriverName(driverName);
						driveringStatusLogData.setLattitude(0);
						driveringStatusLogData.setLongitude(0);
						driveringStatusLogData.setCustomLocation("");
						driveringStatusLogData.setOrigin("");
						driveringStatusLogData.setOdometer(0);
						driveringStatusLogData.setEngineHour("0");
						driveringStatusLogData.setNote("");
						driveringStatusLogData.setIsVoilation(0);
						driveringStatusLogData.setLogType("Login");
						driveringStatusLogData.setStatusId(0);
						
						driveringStatusLogData.setDateTime(String.valueOf(loginLogData.get(i).getLoginDateTime()));
						driveringStatusLogData.setStatus("Login");
						driveringStatusLogData.setEmployeeStatus(employeeStatus);
						
						loginLogoutLogViewDto.add(driveringStatusLogData);
					}
					if(loginLogData.get(i).getLogoutDateTime()>0) {
						driveringStatusLogData = new DriveringStatusLogViewDto();
						driveringStatusLogData.setDriverId(loginLogData.get(i).getEmployeeId());
						driveringStatusLogData.setDriverName(driverName);
						driveringStatusLogData.setLattitude(0);
						driveringStatusLogData.setLongitude(0);
						driveringStatusLogData.setCustomLocation("");
						driveringStatusLogData.setOrigin("");
						driveringStatusLogData.setOdometer(0);
						driveringStatusLogData.setEngineHour("0");
						driveringStatusLogData.setNote("");
						driveringStatusLogData.setIsVoilation(0);
						driveringStatusLogData.setLogType("Logout");
						driveringStatusLogData.setStatusId(0);
						
						driveringStatusLogData.setDateTime(String.valueOf(loginLogData.get(i).getLogoutDateTime()));
						driveringStatusLogData.setStatus("Logout");
						driveringStatusLogData.setEmployeeStatus(employeeStatus);
						
						loginLogoutLogViewDto.add(driveringStatusLogData);
					}
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			 List<SplitLog> splitLogData = splitLogRepo.findSplitLogByDriverId(employeeId);
			 
			 empDetailData = employeeMasterRepo.findByEmployeeId((int)employeeId);
			cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetailData.getCycleUsaId());
			List<CycleTimeRulesViewDto> cycleTimeRulesViewDto = new ArrayList<>();
			CycleTimeRulesViewDto cycleTimeRules = new CycleTimeRulesViewDto();
			long hourToSec = 60*60; long minToSec = 60;
			
			cycleTimeRules.setCycleTime(cycleUsaData.getCycleHour()*hourToSec);
			cycleTimeRules.setCycleDays(cycleUsaData.getCycleDays());
			cycleTimeRules.setOnDutyTime(cycleUsaData.getOnDutyTime()*hourToSec);
			cycleTimeRules.setOnDriveTime(cycleUsaData.getOnDriveTime()*hourToSec);
			cycleTimeRules.setOnSleepTime(cycleUsaData.getOnSleepTime()*hourToSec);
			cycleTimeRules.setContinueDriveTime(cycleUsaData.getContinueDriveTime()*hourToSec);
			cycleTimeRules.setBreakTime(cycleUsaData.getBreakTime()*minToSec);
			cycleTimeRules.setCycleRestartTime(cycleUsaData.getCycleRestartTime()*hourToSec);
			cycleTimeRules.setWarningOnDutyTime1((cycleUsaData.getOnDutyTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
			cycleTimeRules.setWarningOnDutyTime2((cycleUsaData.getOnDutyTime()*hourToSec)-(cycleUsaData.getWarningTime2()*minToSec));
			cycleTimeRules.setWarningOnDriveTime1((cycleUsaData.getOnDriveTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
			cycleTimeRules.setWarningOnDriveTime2((cycleUsaData.getOnDriveTime()*hourToSec)-(cycleUsaData.getWarningTime2()*minToSec));
			cycleTimeRules.setWarningBreakTime1((cycleUsaData.getContinueDriveTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
			cycleTimeRules.setWarningBreakTime2((cycleUsaData.getContinueDriveTime()*hourToSec)-(cycleUsaData.getWarningTime2()*minToSec));
			cycleTimeRulesViewDto.add(cycleTimeRules);
			
//			Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
//			Query query = new Query(
//			  Criteria.where("driverId").is(employeeId)
//			  .and("utcDateTime").lte(to)
//			  .and("isVoilation").is(0)
//			);
//			query.limit(1);
//			query.with(pageableRequest);
//			driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
//			long lastLogUtcDataTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
//			
//			String splitStr[] = timezoneOffSet.split(":");
//			long hour = Math.abs(Long.parseLong(splitStr[0]));
//			long minutes = Math.abs(Long.parseLong(splitStr[1]));
//			String minusPlus="";
//			long timestampValue=0;
//			if(timezoneOffSet.substring(0,1).equals("-")) {
//				minusPlus = "minus";
//				timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
//			}else {
//				minusPlus = "plus";
//				timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
//			}
//			long diffInMillis = timestampValue - lastLogUtcDataTime;
//			long hours = diffInMillis / (1000 * 60 * 60);
//
//			if(hours>=34) {
//				empInfo.setRules(cycleTimeRulesViewDto);
//				
//				query = new Query();
//		        query.addCriteria(Criteria.where("employeeId").is(employeeId));
//		        Update update = new Update();
//		        update.set("tokenNo", "0");
//		        mongoTemplate.findAndModify(query, update, Login.class);
//			}else {
//				empInfo.setRules(new ArrayList<>());
//			}
			
			empInfo.setRules(cycleTimeRulesViewDto);
			empInfo.setDriverLog(driveringStatusViewDto);
			empInfo.setDriverDvirLog(dvirDataViewDto);
			empInfo.setDriverCertifiedLog(certifiedLogViewDto);
			empInfo.setLoginLogoutLog(loginLogoutLogViewDto);
			empInfo.setSplitLog(splitLogData);

			result.setResult(empInfo);
			result.setStatus(Result.SUCCESS);
			result.setToken(userLoginDto.getTokenNo());
			result.setMessage("Logged In Data Send Successfully"+sDebug);
				
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public long lookupMaxIdOfDriverOperation(long driverId){
		
		// Create an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("driverId").is(driverId)),  // Filter by driver
                Aggregation.group().max("statusId").as("statusId")        // Get the max driver
        );

        AggregationResults<MaxIdViewDto> result = mongoTemplate.aggregate(aggregation, "driver_status_log", MaxIdViewDto.class);
        List<MaxIdViewDto> results = result.getMappedResults();
        
        return results.isEmpty() ? -1 : results.get(0).getStatusId();
    }
	
	public List<LoginLog> lookupLoginLogDataOperation(long from, long to, long driverId){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("receivedTimestamp").gte(from).lte(to)
					.and("employeeId").is(driverId)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"employeeId","loginDateTime","logoutDateTime","receivedTimestamp","isCoDriver").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage);
        List<LoginLog> results = mongoTemplate.aggregate(aggregation, "login_log" , LoginLog.class).getMappedResults();
        return results;
		
    }

	@Override
	public ResultWrapper<String> UpdateLoginWithLoginLog(LoginUpdateDto loginUpdateDto) {
		String sDebug="";
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			
			Instant instant = Instant.now();
			
			Query query = new Query();
	        query.addCriteria(Criteria.where("employeeId").is(loginUpdateDto.getDriverId()));
	        Update update = new Update();
	        update.set("loginDateTime", loginUpdateDto.getTimestamp());
	        mongoTemplate.findAndModify(query, update, Login.class);
	        
	        query = new Query();
			query.addCriteria(
        			Criteria.where("employeeId").is(loginUpdateDto.getDriverId())
        			.and("loginDateTime").is(loginUpdateDto.getLoginDateTime())
        		);
	        update = new Update();
	        update.set("loginDateTime", loginUpdateDto.getTimestamp());
	        mongoTemplate.findAndModify(query, update, LoginLog.class);
	        
//	       ======================Update Driver status login time============================
	        
//	        query = new Query();
//	        query.addCriteria(
//	            Criteria.where("employeeId").is(loginUpdateDto.getDriverId())
//	                .and("loginDateTime").is(loginUpdateDto.getLoginDateTime())
//	        );
//	        query.with(Sort.by(Sort.Direction.DESC, "_id"));
//	        query.limit(1);
//	        LoginLogViewDto loginLogViewDto = mongoTemplate.findOne(query, LoginLogViewDto.class, "login_log");
//	        if (loginLogViewDto != null) {
//	        	query = new Query();
//				query.addCriteria(
//	        			Criteria.where("driverId").is(loginUpdateDto.getDriverId())
//	        			.and("logDataId").is(loginLogViewDto.get_id())
//	        		);
//		        update = new Update();
//		        update.set("dateTime", loginUpdateDto.getTimestamp());
//		        mongoTemplate.findAndModify(query, update, DriverStatusLog.class);
//	        }
	        
	        DriverStatusLog driverStatusLog = new DriverStatusLog();
	        long maxId = lookupMaxIdOfDriverOperation(loginUpdateDto.getDriverId());
			if(maxId<=0) {
				maxId=1;
				driverStatusLog.setStatusId(maxId);
			}else {
				maxId++;
				driverStatusLog.setStatusId(maxId);
			}
			Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
			query = new Query(
			  Criteria.where("employeeId").is(loginUpdateDto.getDriverId())
			);
			query.limit(1);
			query.with(pageableRequest);
			List<LoginLogViewDto> loginLogViewDto = mongoTemplate.find(query, LoginLogViewDto.class,"login_log");
			
			EmployeeMaster empDetailData = employeeMasterRepo.findByEmployeeId((int)loginUpdateDto.getDriverId());
			
			driverStatusLog.setLogDataId(loginLogViewDto.get(0).get_id());
			driverStatusLog.setDriverId(loginUpdateDto.getDriverId());
			driverStatusLog.setVehicleId(0);
			driverStatusLog.setClientId(empDetailData.getClientId());
			driverStatusLog.setStatus("Login");
			driverStatusLog.setLattitude(0);
			driverStatusLog.setLongitude(0);
			driverStatusLog.setDateTime(loginUpdateDto.getTimestamp());
			driverStatusLog.setLogType("Login");
			driverStatusLog.setEngineHour("0");
			driverStatusLog.setOrigin("");
			driverStatusLog.setOdometer(0);
			driverStatusLog.setIsVoilation(0);
			driverStatusLog.setNote("");
			driverStatusLog.setCustomLocation("");
			driverStatusLog.setIsReportGenerated(1);
			driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
			driverStatusLog.setIsVisible(1);
			
			List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(loginLogViewDto.get(0).get_id());
			if(logDataExist.size()<=0) {
				driverStatusLogRepo.save(driverStatusLog);
				UpdateDriverSequenceId(loginUpdateDto.getDriverId(),loginUpdateDto.getTimestamp());
			}
	        
//		  ======================End============================
	        
	        query = new Query();
	        query.addCriteria(Criteria.where("employeeId").is(loginUpdateDto.getDriverId()));
	        update = new Update();
	        update.set("isFirstLogin", "false");
	        mongoTemplate.findAndModify(query, update, EmployeeMaster.class);
	       
			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Login Log Updated successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	@Override
	public ResultWrapper<EmployeeMasterCRUDDto> LoginByDate(UserLoginDto userLoginDto, String token) {
		String sDebug="";
		ResultWrapper<EmployeeMasterCRUDDto> result = new ResultWrapper<>();
		try {
			EmployeeMasterCRUDDto empInfo = null;
			String username = userLoginDto.getUsername();
			String password = userLoginDto.getPassword();
//			String mobileDeviceId = userLoginDto.getMobileDeviceId();
			List<Login> login = loginRepo.ValidateAndLogin(username,password);
			Instant instant = Instant.now();
			String email="";
			if(login.size()>0) {
//				if(login.get(0).getMobileDeviceId()==null || login.get(0).getMobileDeviceId().equals("")) {
//					Query query = new Query();
//			        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
//			        Update update = new Update();
//			        update.set("mobileDeviceId", mobileDeviceId);
//			        mongoTemplate.findAndModify(query, update, Login.class);
//				}
				email = login.get(0).getEmail();
				UserLoginDto loginData = loginRepo.GetLoginDataByEmail(email);

				if(login.get(0).getLoginStatus().equals("false")) {
//					if(loginData.getMobileDeviceId().equals(mobileDeviceId)) {
						//update login status
						if(userLoginDto.getIsCoDriver()==null || userLoginDto.getIsCoDriver().equals("")) {
							Query query = new Query();
					        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
					        Update update = new Update();
//					        update.set("loginStatus", "true");
					        update.set("isCoDriver", "false");
					        update.set("tokenNo", token);
					        update.set("loginDateTime", instant.toEpochMilli());
					        mongoTemplate.findAndModify(query, update, Login.class);
						}else {
							Query query = new Query();
					        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
					        Update update = new Update();
//					        update.set("loginStatus", "true");
					        update.set("isCoDriver", userLoginDto.getIsCoDriver());
					        update.set("tokenNo", token);
					        update.set("loginDateTime", instant.toEpochMilli());
					        mongoTemplate.findAndModify(query, update, Login.class);
						}
						
				        
				        empInfo = employeeMasterRepo.findEmployeeByEmail1(email);
				        loginData = loginRepo.GetLoginDataByEmail(email);
				        
				        try {
							MACAddressMaster macAddressData = macAddressMasterRepo.findByMACAddressMasterId(login.get(0).getEmployeeId());
							empInfo.setMacAddress(macAddressData.getMacAddress());
							empInfo.setVehicleId(macAddressData.getVehicleId());
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) macAddressData.getVehicleId());
							empInfo.setVehicleNo(vehcileInfo.getVehicleNo());
							
				        	if(empInfo.getClientId()>0) {
								ClientMaster client = clientMasterRepo.findByClientId((int)empInfo.getClientId());
								empInfo.setClientName(client.getClientName());
							}else {
								empInfo.setClientName("");
							}
				        }catch(Exception ex) {
				        	ex.printStackTrace();
				        }
				        
				        LoginLog loginLog = new LoginLog();
				        loginLog.setEmployeeId(login.get(0).getEmployeeId());
				        loginLog.setLoginDateTime(loginData.getLoginDateTime());
				        loginLog.setLogoutDateTime(0L);
				        loginLog.setIsCoDriver(loginData.getIsCoDriver());
				        loginLog.setReceivedTimestamp(instant.toEpochMilli());
				        loginLog.setLoginType("app");
				        loginLogRepo.save(loginLog);
						
						empInfo.setLoginDateTime(loginData.getLoginDateTime());
						
						String dateTime = userLoginDto.getDateTime();
						LocalDate periviousDate = LocalDate.parse(dateTime);
						periviousDate = periviousDate.minusDays(14);
						String fromDate = periviousDate.toString()+" 00:00:00";
						String toDate = dateTime+" 23:59:59";

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
						LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
						long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
						long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						String driverName="";
						String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
						String setImagePath1;
						List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,login.get(0).getEmployeeId());
						for(int i=0;i<driveringStatusViewDto.size();i++) {
							driveringStatusViewDto.get(i).setFromDate(from);
							driveringStatusViewDto.get(i).setToDate(to);
//							System.out.println(driverId);
							try {
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//								System.out.println(empDetails);
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								driveringStatusViewDto.get(i).setDriverName(driverName);
								driveringStatusViewDto.get(i).setMobileNo(empDetails.getMobileNo());
								driveringStatusViewDto.get(i).setEmail(empDetails.getEmail());
//								driveringStatusViewDto.get(i).setCompanyDriverId(empDetails.getDriverId());
								driveringStatusViewDto.get(i).setCompanyDriverId(empDetails.getUsername());

								driveringStatusViewDto.get(i).setCdlNo(empDetails.getCdlNo());
								
								CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empDetails.getCdlCountryId());
								driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
								StateMaster stateInfo = stateMasterRepo.findByStateId((int)empDetails.getCdlStateId());
								driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
								
								driveringStatusViewDto.get(i).setExempt(empDetails.getExempt());
								
								VehicleMaster vehcileInfo = null;
								if(driveringStatusViewDto.get(i).getVehicleId()>0) {
									vehcileInfo = vehicleMasterRepo.findByVehicleId((int)driveringStatusViewDto.get(i).getVehicleId());
									driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
									driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
								}else {
									driveringStatusViewDto.get(i).setTruckNo("");
									driveringStatusViewDto.get(i).setVin("");
								}
								
								
								MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empDetails.getMainTerminalId());
								driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
								
								CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
								driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
								
								if(empDetails.getClientId()>0) {
									ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empDetails.getClientId());
									driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
									driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
								}
								
								List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driveringStatusViewDto.get(i).getDriverId());
								if(certifiedLogViewDto.size()>0) {
									for(int c=0;c<certifiedLogViewDto.size();c++) {
										if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
											empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//											driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getDriverId());
											driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getUsername());
											driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
										}
										
										driveringStatusViewDto.get(i).setCoDriverName(driverName);
										driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
										driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
										
										setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
										driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
										
										vehcileInfo = vehicleMasterRepo.findByVehicleId((int) certifiedLogViewDto.get(c).getVehicleId());
										driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
										driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
										
									}
								}else {
									driveringStatusViewDto.get(i).setTrailers(new ArrayList());
									driveringStatusViewDto.get(i).setShippingDocs(new ArrayList());
								}
								
								

							}catch(Exception ex) {
								ex.printStackTrace();
							}
			        	
						}
						
						List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(from,to,login.get(0).getEmployeeId());
						try {
							for(int i=0;i<dvirDataViewDto.size();i++) {
								setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
								dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
								
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)login.get(0).getEmployeeId());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								dvirDataViewDto.get(i).setDriverName(driverName);
								
								try {
									if(dvirDataViewDto.get(i).getVehicleId()>0) {
										VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
										dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
									}
								}catch(Exception ex) {
									ex.printStackTrace();
								}
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						
						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,login.get(0).getEmployeeId());
						try {
							for(int i=0;i<certifiedLogViewDto.size();i++) {
								setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
								certifiedLogViewDto.get(i).setCertifiedSignature(setImagePath1);
								
								EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)login.get(0).getEmployeeId());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								certifiedLogViewDto.get(i).setDriverName(driverName);
								
								if(certifiedLogViewDto.get(i).getCoDriverId()>0) {
									empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(i).getCoDriverId());
									driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
									certifiedLogViewDto.get(i).setCoDriverName(driverName);
								}
								if(certifiedLogViewDto.get(i).getVehicleId()>0) {
									VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)certifiedLogViewDto.get(i).getVehicleId());
									certifiedLogViewDto.get(i).setVehicleName(vehcileInfo.getVehicleNo());
								}
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						
						empInfo.setDriverLog(driveringStatusViewDto);
						empInfo.setDriverDvirLog(dvirDataViewDto);
						empInfo.setDriverCertifiedLog(certifiedLogViewDto);
				        
						result.setResult(empInfo);
						result.setToken(token);
						result.setStatus(Result.SUCCESS);
						result.setMessage("Logged In Successfully");
//					}else {
//						result.setResult(null);
//						result.setToken(null);
//						result.setStatus(Result.FAIL);
//						result.setMessage("Your mobile device is change.");
//					}
				}else {
//					if(loginData.getMobileDeviceId().equals(mobileDeviceId)) {
//						//update login status
//						Query query = new Query();
//				        query.addCriteria(Criteria.where("employeeId").is(login.get(0).getEmployeeId()));
//				        Update update = new Update();
//				        update.set("loginStatus", "true");
//				        update.set("loginDateTime", instant.toEpochMilli());
//				        mongoTemplate.findAndModify(query, update, Login.class);
//				        
//				        empInfo = employeeMasterRepo.findEmployeeByEmail1(email);
//				        loginData = loginRepo.GetLoginDataByEmail(email);
//				        
//				        LoginLog loginLog = new LoginLog();
//				        loginLog.setEmployeeId(login.get(0).getEmployeeId());
//				        loginLog.setLoginDateTime(loginData.getLoginDateTime());
//				        loginLog.setLogoutDateTime(0L);
//				        loginLog.setReceivedTimestamp(instant.toEpochMilli());
//				        loginLogRepo.save(loginLog);
//						
//						empInfo.setLoginDateTime(loginData.getLoginDateTime());
//						result.setResult(empInfo);
//						result.setToken(null);
//						result.setStatus(Result.SUCCESS);
//						result.setMessage("Logged In Successfully");
//					}else {
//						result.setResult(null);
//						result.setToken(null);
//						result.setStatus(Result.FAIL);
//						result.setMessage("Your mobile device is change.");
//					}
					
					result.setResult(null);
					result.setToken(null);
					result.setStatus(Result.FAIL);
					result.setMessage("You are not authorized for login.");
					
				}	
			}else {
				result.setResult(null);
				result.setToken(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid username or password");
			}
						
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DVIRDataCRUDDto> lookupDVIRDataOperation(long from, long to, long driverId){
		
		MatchOperation filter = null;
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("lDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("lDateTime").gte(from).lte(to)
				);
		}
		ProjectionOperation projectStage = Aggregation.project(
				"_id","driverId","location","truckDefect","trailerDefect","dateTime","lDateTime","notes","vehicleCondition",
				"driverSignFile","receivedTimestamp","engineHour","odometer","companyName","vehicleId","timestamp","trailer");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "driverId").and(Direction.DESC, "receivedTimestamp"));
        List<DVIRDataCRUDDto> results = mongoTemplate.aggregate(aggregation, "dvir_data" , DVIRDataCRUDDto.class).getMappedResults();
        return results;
		
    }
	
	public List<CertifiedLogViewDto> lookupCertifiedLogDataOperation(long from, long to, long driverId){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("lCertifiedDate").gte(from).lte(to)
					.and("driverId").is(driverId)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","vehicleId","trailers","shippingDocs","coDriverId","certifiedSignature","certifiedDate","lCertifiedDate",
				"addedTimestamp","certifiedAt").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage);
        List<CertifiedLogViewDto> results = mongoTemplate.aggregate(aggregation, "certified_log" , CertifiedLogViewDto.class).getMappedResults();
        return results;
		
    }

	public List<DriveringStatusViewDto> lookupDriverStatusDataOperation(long from, long to, long driverId){
		
		MatchOperation filter = null;
		
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
					.and("isVisible").is(1)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("isVisible").is(1)
				);
		}
			
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType","utcDateTime",
				"appVersion","isVoilation","note","customLocation","engineHour","engineStatus","odometer","vehicleId","shift","days","origin",
				"timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime","remainingSleepTime","remainingLastSleepTime",
				"identifier","isSplit").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.DESC, "lDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	@Override
	public ResultWrapper<List<UserMasterViewDto>> LoginWeb(UserLoginDto userLoginDto, String token) {
		String sDebug="==>";
		ResultWrapper<List<UserMasterViewDto>> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			String email = userLoginDto.getEmail();
			String password = userLoginDto.getPassword();
			String tokenNo = userLoginDto.getTokenNo();
			List<UserMasterViewDto> userMaster = userMasterRepo.findAndViewByEmailAndPassword(email,password);
			boolean isTokenValid=false;
//			sDebug+=" Token : "+userMaster.get(0).getTokenNo()+",";
//			if(email.equals("superadmin@gmail.com")) {
			if(userMaster.get(0).getUserTypeId()==1) {
				isTokenValid=true;
				if(userMaster.get(0).getDisclaimerRead()==0) {
					try {
				        String disclaimerFilePath = WEB_URL_FILE_UPLOAD+"/disclaimer/disclaimer.txt"; 				        
				        File disclaimerFile = new File(disclaimerFilePath);
				        if (disclaimerFile.exists()) {
				            String disclaimerText = new String(Files.readAllBytes(disclaimerFile.toPath()), StandardCharsets.UTF_8);
				            userMaster.get(0).setDisclaimer(disclaimerText);
				        } else {
				            userMaster.get(0).setDisclaimer("Disclaimer file not found.");
				        }
				    } catch (IOException e) {
				        userMaster.get(0).setDisclaimer("Error reading disclaimer file: " + e.getMessage());
				    }
					
//					Query query1 = new Query();
//			        query1.addCriteria(Criteria.where("userId").is(userMaster.get(0).getUserId()));
//			        Update update1 = new Update();
//			        update1.set("disclaimerRead", 1);
//			        mongoTemplate.findAndModify(query1, update1, UserMaster.class);
				}
			}else {
				String dbToken = userMaster.get(0).getTokenNo();
				if(dbToken!=null && !dbToken.isEmpty()) {
					if(dbToken.equals(tokenNo)) {
						isTokenValid=true;
					}
				}else {
//					sDebug+="Else : ";
					isTokenValid=true;
					Query query = new Query();
			        query.addCriteria(Criteria.where("userId").is(userMaster.get(0).getUserId()));
			        Update update = new Update();
			        update.set("tokenNo", tokenNo);
			        mongoTemplate.findAndModify(query, update, UserMaster.class);
				}
			}
			
			ClientMaster clientMaster=null;
			String status="", timezoneOffSet="", subscriptionExpire="";
			if(userMaster.size()>0) {
				if(userMaster.get(0).getClientId()>0) {
					clientMaster = clientMasterRepo.findByClientId((int)userMaster.get(0).getClientId());
					status = clientMaster.getStatus();
					long graceTime = clientMaster.getGraceTime();
					
					StateMaster stateInfo = stateMasterRepo.findByStateId((int)clientMaster.getTimezoneId());
					timezoneOffSet=stateInfo.getTimezoneOffSet();
					timezoneOffSet = timezoneOffSet.trim()
                            .replace("\u2212", "-")
                            .replace("\uFF0B", "+");
					
					String splitStr[] = timezoneOffSet.split(":");
					long hour = Math.abs(Long.parseLong(splitStr[0]));
					long minutes = Math.abs(Long.parseLong(splitStr[1]));
					int totalMinutes = (int) ((hour * 60) + minutes);
					
					Clock cl = Clock.systemUTC(); 
					Clock adjustedClock;
					if (timezoneOffSet.startsWith("-")) {
					    adjustedClock = Clock.offset(cl, Duration.ofMinutes(-totalMinutes));
					} else {
					    adjustedClock = Clock.offset(cl, Duration.ofMinutes(totalMinutes));
					}
			        LocalDate lt = LocalDate.now(adjustedClock); 
			        LocalDateTime endOfDay = lt.atTime(LocalTime.MAX);
			        long endOfDayEpoch = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			        
			        long timestampValue=0;
					if(timezoneOffSet.substring(0,1).equals("-")) {
						timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
					}else {
						timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
					}
					
					if(timestampValue>graceTime) {
						// not login
						subscriptionExpire="true";
					}else {
						// login
						subscriptionExpire="false";
					}
			        
				}else {
					status = "true";
					subscriptionExpire = "false";
				}
				
				if(subscriptionExpire.equals("true")) {
					result.setResult(null);
					result.setToken(null);
					result.setStatus(Result.FAIL);
					result.setMessage("You are not autorized for login.");
				}else {
					if((status.equals("true") && isTokenValid) && (userMaster.get(0).getWebAccess().equals("true") && userMaster.get(0).getStatus().equals("true"))) {
						LoginLog loginLog = new LoginLog();
				        loginLog.setUserId(userMaster.get(0).getUserId());
				        loginLog.setLoginDateTime(instant.toEpochMilli());
				        loginLog.setLogoutDateTime(0L);
				        loginLog.setIsCoDriver("false");
				        loginLog.setReceivedTimestamp(instant.toEpochMilli());
				        loginLog.setLoginType("web");
				        loginLogRepo.save(loginLog);
				        
				        userMaster.get(0).setLoginDateTime(loginLog.getLoginDateTime());
				        
						if(userMaster.get(0).getClientId()>0) {
							ClientMaster client = clientMasterRepo.findByClientId((int)userMaster.get(0).getClientId());
							userMaster.get(0).setClientName(client.getClientName());
							userMaster.get(0).setAllowTracking(client.getAllowTracking());
							userMaster.get(0).setAllowGpsTracking(client.getAllowGpsTracking());
							userMaster.get(0).setAllowIfta(client.getAllowIfta());
							userMaster.get(0).setExemptDriver(client.getExemptDriver());
							userMaster.get(0).setPersonalUse(client.getPersonalUse());
							userMaster.get(0).setYardMoves(client.getYardMoves());
							userMaster.get(0).setShortHaulException(client.getShortHaulException());
							
//							sDebug+=" >> "+client.getCountryId()+" < ";
							CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)client.getCountryId());
//							sDebug+=" :: "+countryInfo+",";
							userMaster.get(0).setLattitude(countryInfo.getLattitude());
							userMaster.get(0).setLongitude(countryInfo.getLongitude());
						}else {
							userMaster.get(0).setClientName("");
						}
						result.setResult(userMaster);
						result.setToken(token);
						result.setStatus(Result.SUCCESS);
						result.setMessage("Logged In Successfully"+sDebug); 
							
					}else {
						result.setResult(null);
						result.setToken(null);
						result.setStatus(Result.FAIL);
						result.setMessage("You are not autorized for login.");
					}
				}
			}else {
				result.setResult(null);
				result.setToken(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid username or password.");
			}
						
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	@Override
	public ResultWrapper<UserLoginDto> ForgotPassword(UserLoginDto userLoginDto) {
	    ResultWrapper<UserLoginDto> result = new ResultWrapper<>();
	    try {
	        String email = userLoginDto.getUsername();
	        List<Login> login = loginRepo.ForgotPassword(email);
	        
	        if (login.size() > 0) {
	            UserLoginDto userData = loginRepo.GetLoginDataByEmail(email);
	            EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId(userData.getEmployeeId());
				String driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
	            // send on forgot password email
	            try {
	                MimeMessage msg = javaMailSender.createMimeMessage();

	                MimeMessageHelper helper = new MimeMessageHelper(msg, true);

	                helper.setTo(userData.getEmail());
	                helper.setSubject("Forgot Password Request");

	                String msg_body = "<div style='font-family: Arial, sans-serif; font-size:14px; color:#333;'>"
	                        + "Dear <b>" + driverName + "</b>,<br/><br/>"
	                        + "We received a request to recover your password. "
	                        + "Here are your login details:<br/><br/>"
	                        + "Your password is: <b style='color:#6610f2;'>" + userData.getPassword() + "</b><br/><br/>"
	                        + "<b style='color:#000;'>Security Note:</b> Please do not share your password with anyone. "
	                        + "Our team will never ask you for your password via email, phone, or chat.<br/><br/>"
	                        + "Best Regards,<br/>"
	                        + "<b>ELD Support Team</b>"
	                        + "</div>";

	                helper.setText(msg_body, true);

	                javaMailSender.send(msg);

	            } catch (Exception ex) {
	                result.setStatus(Result.FAIL);
	                result.setMessage("Failed to send password email.");
	                return result;
	            }

	            result.setResult(userData);
	            result.setToken(null);
	            result.setStatus(Result.SUCCESS);
	            result.setMessage("Forgot password email sent successfully.");
	        } else {
	            result.setResult(null);
	            result.setToken(null);
	            result.setStatus(Result.FAIL);
	            result.setMessage("Invalid Username.");
	        }

	    } catch (Exception e) {
	        result.setStatus(Result.FAIL);
	        result.setMessage(e.getLocalizedMessage());
	    }
	    return result;
	}
	
//	@Override
//	public ResultWrapper<UserLoginDto> ForgotPassword(UserLoginDto userLoginDto) {
//		String sDebug="";
//		ResultWrapper<UserLoginDto> result = new ResultWrapper<>();
//		try {
//			String email = userLoginDto.getUsername();
//			List<Login> login = loginRepo.ForgotPassword(email);
//			if(login.size()>0) {
//				UserLoginDto userData = loginRepo.GetLoginDataByEmail(email);
//				//userData.setPassword(String.valueOf(userData.getMobileNo()));
//				//send on forgot password on gmail
//				try {
//					MimeMessage msg = javaMailSender.createMimeMessage();
//	
//			        // true = multipart message
//			        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
//					
////			        helper.setTo("mrityunjayk1998@gmail.com");
////			        helper.setBcc("pankaj.rayak@gmail.com");
//			        
//			        helper.setTo(userData.getEmail());
//	
//			        helper.setSubject("Forgot Password");
//	
//			        String msg_body = "Your forgot password is:<br/><br/>"+userData.getPassword()
//			        		+"<br/><br/>"
//	                        + "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//	                        ;
//			        // true = text/html
//			        helper.setText(msg_body, true);
//	
//					// hard coded a file path
//			        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));
//	
//			        //helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
//					
//			        javaMailSender.send(msg);
//			       
//				} catch(Exception ex) {
//					
//				}
//				
//				result.setResult(userData);
//				result.setToken(null);
//				result.setStatus(Result.SUCCESS);
//				result.setMessage("Forgot Password Send Successfully."); 
//			}else {
//				result.setResult(null);
//				result.setToken(null);
//				result.setStatus(Result.FAIL);
//				result.setMessage("Invalid Username.");
//			}
//						
//			
//		}catch(Exception e) {
//			result.setStatus(Result.FAIL);
//			result.setMessage(e.getLocalizedMessage());
//		}
//		return result;	
//	}
	
	@Override
	public ResultWrapper<UserLoginDto> ForgotUsername(UserLoginDto userLoginDto) {
		String sDebug="";
		ResultWrapper<UserLoginDto> result = new ResultWrapper<>();
		try {
			long mobileNo = userLoginDto.getMobileNo();
			List<Login> login = loginRepo.ForgotUsername(mobileNo);
			if(login.size()>0) {
				UserLoginDto userData = loginRepo.GetLoginDataByEmail(login.get(0).getEmail());
				userData.setPassword(String.valueOf(userData.getMobileNo()));
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId(userData.getEmployeeId());
				String driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
				//send on forgot password on gmail
				// send on forgot password email
	            try {
	                MimeMessage msg = javaMailSender.createMimeMessage();

	                MimeMessageHelper helper = new MimeMessageHelper(msg, true);

	                helper.setTo(userData.getEmail());
	                helper.setSubject("Forgot Username Request");

	                String msg_body = "<div style='font-family: Arial, sans-serif; font-size:14px; color:#333;'>"
	                        + "Dear <b>" + driverName + "</b>,<br/><br/>"
	                        + "We received a request to recover your username. "
	                        + "Here are your login details:<br/><br/>"
	                        + "Your username is: <b style='color:#6610f2;'>" + userData.getUsername() + "</b><br/><br/>"
	                        + "<b style='color:#000;'>Security Note:</b> Please do not share your username with anyone. "
	                        + "Our team will never ask you for your password via email, phone, or chat.<br/><br/>"
	                        + "Best Regards,<br/>"
	                        + "<b>ELD Support Team</b>"
	                        + "</div>";

	                helper.setText(msg_body, true);

	                javaMailSender.send(msg);

	            } catch (Exception ex) {
	                result.setStatus(Result.FAIL);
	                result.setMessage("Failed to send username.");
	                return result;
	            }
				
				result.setResult(userData);
				result.setToken(null);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Forgot Username Send Successfully."); 
			}else {
				result.setResult(null);
				result.setToken(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Mobile No.");
			}
						
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	@Override
	public ResultWrapper<UserLoginDto> LogoutApi(UserLoginDto userLoginDto, String tokenValid) {
		String sDebug="";
		ResultWrapper<UserLoginDto> result = new ResultWrapper<>();
		try {
			Integer employeeId = userLoginDto.getEmployeeId();
			long loginDateTime = userLoginDto.getLoginDateTime();
			Instant instant = Instant.now();
			
			if(employeeId>0) {
				if(tokenValid.equals("true")) {
					Query query = new Query();
			        query.addCriteria(Criteria.where("employeeId").is(employeeId));
			        Update update = new Update();
			        update.set("loginStatus", "false");
			        update.set("logoutDateTime", userLoginDto.getLogoutDateTime());
			        mongoTemplate.findAndModify(query, update, Login.class);
			        
			        Query query1 = new Query();
			        query1.addCriteria(
			        		Criteria.where("employeeId").is(employeeId)
			        		.and("loginDateTime").is(loginDateTime)
		        		);
			        Update update1 = new Update();
			        update1.set("logoutDateTime", userLoginDto.getLogoutDateTime());
			        mongoTemplate.findAndModify(query1, update1, LoginLog.class);
			        
			        EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId(employeeId);
			        
			        DriverStatusLog driverStatusLog = new DriverStatusLog();
			        long maxId = lookupMaxIdOfDriverOperation(employeeId);
//					System.out.println(maxId);
					if(maxId<=0) {
						maxId=1;
						driverStatusLog.setStatusId(maxId);
					}else {
						maxId++;
						driverStatusLog.setStatusId(maxId);
					}
					
			        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query queryLog = new Query(
					  Criteria.where("employeeId").is(employeeId)
					);
					queryLog.limit(1);
					queryLog.with(pageableRequest);
					List<LoginLogViewDto> loginLogViewDto = mongoTemplate.find(queryLog, LoginLogViewDto.class,"login_log");
					
					driverStatusLog.setLogDataId(loginLogViewDto.get(0).get_id());
					driverStatusLog.setDriverId(employeeId);
					driverStatusLog.setVehicleId(0);
					driverStatusLog.setClientId(empInfo.getClientId());
					driverStatusLog.setStatus("Logout");
					driverStatusLog.setLattitude(0);
					driverStatusLog.setLongitude(0);
					driverStatusLog.setDateTime(loginLogViewDto.get(0).getLogoutDateTime());
					driverStatusLog.setLogType("Logout");
					driverStatusLog.setEngineHour("0");
					driverStatusLog.setOrigin("");
					driverStatusLog.setOdometer(0);
					driverStatusLog.setIsVoilation(0);
					driverStatusLog.setNote("");
					driverStatusLog.setCustomLocation("");
					driverStatusLog.setIsReportGenerated(1);
					driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
					driverStatusLog.setIsVisible(1);
					List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(loginLogViewDto.get(0).get_id());
					if(logDataExist.size()<=0) {
						driverStatusLogRepo.save(driverStatusLog);
						UpdateDriverSequenceId(employeeId,loginLogViewDto.get(0).getLogoutDateTime());
					}
			        
					result.setResult(userLoginDto);
					result.setToken(tokenValid);
					result.setStatus(Result.SUCCESS);
					result.setMessage("Logout successfully.");
				}else {
					Query query = new Query();
			        query.addCriteria(Criteria.where("employeeId").is(employeeId));
			        Update update = new Update();
			        update.set("loginStatus", "false");
			        update.set("logoutDateTime", userLoginDto.getLogoutDateTime());
			        mongoTemplate.findAndModify(query, update, Login.class);
			        
			        Query query1 = new Query();
			        query1.addCriteria(
			        		Criteria.where("employeeId").is(employeeId)
			        		.and("loginDateTime").is(loginDateTime)
		        		);
			        Update update1 = new Update();
			        update1.set("logoutDateTime", userLoginDto.getLogoutDateTime());
			        mongoTemplate.findAndModify(query1, update1, LoginLog.class);
			        
			        EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId(employeeId);
			        
			        DriverStatusLog driverStatusLog = new DriverStatusLog();
			        long maxId = lookupMaxIdOfDriverOperation(employeeId);
//					System.out.println(maxId);
					if(maxId<=0) {
						maxId=1;
						driverStatusLog.setStatusId(maxId);
					}else {
						maxId++;
						driverStatusLog.setStatusId(maxId);
					}
					
			        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query queryLog = new Query(
					  Criteria.where("employeeId").is(employeeId)
					);
					queryLog.limit(1);
					queryLog.with(pageableRequest);
					List<LoginLogViewDto> loginLogViewDto = mongoTemplate.find(queryLog, LoginLogViewDto.class,"login_log");
					
					driverStatusLog.setLogDataId(loginLogViewDto.get(0).get_id());
					driverStatusLog.setDriverId(employeeId);
					driverStatusLog.setVehicleId(0);
					driverStatusLog.setClientId(empInfo.getClientId());
					driverStatusLog.setStatus("Logout");
					driverStatusLog.setLattitude(0);
					driverStatusLog.setLongitude(0);
					driverStatusLog.setDateTime(loginLogViewDto.get(0).getLogoutDateTime());
					driverStatusLog.setLogType("Logout");
					driverStatusLog.setEngineHour("0");
					driverStatusLog.setOrigin("");
					driverStatusLog.setOdometer(0);
					driverStatusLog.setIsVoilation(0);
					driverStatusLog.setNote("");
					driverStatusLog.setCustomLocation("");
					driverStatusLog.setIsReportGenerated(1);
					driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
					driverStatusLog.setIsVisible(1);
					
					List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(loginLogViewDto.get(0).get_id());
					if(logDataExist.size()<=0) {
						driverStatusLogRepo.save(driverStatusLog);
						UpdateDriverSequenceId(employeeId,loginLogViewDto.get(0).getLogoutDateTime());
					}
			        
					result.setResult(userLoginDto);
					result.setToken(tokenValid);
					result.setStatus(Result.SUCCESS);
					result.setMessage("Logout successfully.");
				}
				
			}else {
				result.setResult(null);
				result.setToken(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	@Override
	public ResultWrapper<String> LogoutWebApi(UserLoginDto userLoginDto) {
		String sDebug="";
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Integer employeeId = userLoginDto.getEmployeeId();
			long loginDateTime = userLoginDto.getLoginDateTime();
			Instant instant = Instant.now();
			if(employeeId>0) {
		        Query query1 = new Query();
		        query1.addCriteria(
		        		Criteria.where("userId").is(employeeId)
		        		.and("loginDateTime").is(loginDateTime)
	        		);
		        Update update1 = new Update();
		        update1.set("logoutDateTime", instant.toEpochMilli());
		        mongoTemplate.findAndModify(query1, update1, LoginLog.class);

				result.setResult("Logout");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Logout successfully.");
			
				
			}else {
				result.setResult(null);
				result.setToken(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	@Override
	public ResultWrapper<String> ValidateTokenNo(String tokenValid) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			
			result.setResult("Validate Token");
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Validate Token No. successfully.");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
