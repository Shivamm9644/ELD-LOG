package com.mes.eld_log.serviceImpl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.security.Timestamp;

import static com.mes.eld_log.security.Constants.WEB_URL_BASE_PATH;
import static com.mes.eld_log.security.Constants.WEB_URL_FILE_UPLOAD;

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

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
//import org.locationtech.jts.algorithm.PointInPolygon;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermissions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;

import com.mes.eld_log.dtos.DriveringStatusViewDto;
import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.LiveDataLogViewDto;
import com.mes.eld_log.dtos.MaxIdViewDto;
import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.dtos.UserMasterViewDto;
import com.mes.eld_log.models.ClientMaster;
import com.mes.eld_log.models.CompanyMaster;
import com.mes.eld_log.models.DriveringStatus;
import com.mes.eld_log.models.ELDLogData;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.GeofanceMaster;
import com.mes.eld_log.models.IdlingReport;
import com.mes.eld_log.models.IgnitionReport;
import com.mes.eld_log.models.Login;
import com.mes.eld_log.models.LoginLog;
import com.mes.eld_log.models.MACAddressMaster;
import com.mes.eld_log.models.UserMaster;
import com.mes.eld_log.models.VehicleMaster;
import com.mes.eld_log.repo.ClientMasterRepo;
import com.mes.eld_log.repo.DriveringStatusRepo;
import com.mes.eld_log.repo.EmployeeMasterRepo;
import com.mes.eld_log.repo.GeofanceMasterRepo;
import com.mes.eld_log.repo.IdlingReportRepo;
import com.mes.eld_log.repo.IgnitionReportRepo;
import com.mes.eld_log.repo.LoginLogRepo;
import com.mes.eld_log.repo.LoginRepo;
import com.mes.eld_log.repo.MACAddressMasterRepo;
import com.mes.eld_log.repo.UserMasterRepo;
import com.mes.eld_log.repo.VehicleMasterRepo;
import com.mes.eld_log.results.Result;
import com.mes.eld_log.results.ResultWrapper;
import com.mes.eld_log.service.SystemService;
import com.mes.eld_log.service.UserInfoService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DuplicateKeyException;

@EnableScheduling
@PropertySource("classpath:application.properties")
@Transactional

@Service(value = "systemService")
public class SystemServiceImpl implements SystemService{
	
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private EmployeeMasterRepo employeeMasterRepo;
	
	@Autowired
	private IgnitionReportRepo ignitionReportRepo;
	
	@Autowired
	private IdlingReportRepo idlingReportRepo;
	
	@Autowired
	VehicleMasterRepo vehicleMasterRepo;
	
	@Autowired
	GeofanceMasterRepo geofanceMasterRepo;
	
	@Autowired
	private UserMasterRepo userMasterRepo;
	
	@Autowired
	MACAddressMasterRepo macAddressMasterRepo;
	
	@Autowired
	private ClientMasterRepo clientMasterRepo;
	
	@Autowired
	DriveringStatusRepo driveringStatusRepo;
	
	@Autowired
	LoginRepo loginRepo;
	
	@Autowired
	LoginLogRepo loginLogRepo;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	private Path fileStorageLocation;
	
	@Autowired
    public SystemServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
	
//	@Scheduled(cron = "${cron.expression5}")
	public ResultWrapper<String> DriverLogService() {
		String sDebug="";
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			
			LocalDate currentDate = LocalDate.now();
			currentDate = currentDate.minusDays(1);
			LocalDateTime startOfDay = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT);
			LocalDateTime endOfDay = LocalDateTime.of(currentDate, LocalTime.MAX);
			long from = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long to = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
//			System.out.println(" >> Date : "+currentDate); 
			
			List<EmployeeMaster> employeeInfo = employeeMasterRepo.findAll();
			for(int i=0;i<employeeInfo.size();i++) {
				
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
				Query query = new Query(
				  Criteria.where("driverId").is(employeeInfo.get(i).getEmployeeId())
				  .and("utcDateTime").gte(from).lte(to)
				);
				query.limit(1);
				query.with(pageableRequest);
				List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				sDebug+=" D : "+employeeInfo.get(i).getEmployeeId()+" == "+driveringStatusViewDtoData.size()+",";
				if(driveringStatusViewDtoData.size()>0) {
					try {
						sDebug+=" Status : "+employeeInfo.get(i).getEmployeeId()+" <> "+driveringStatusViewDtoData.get(0).getStatus()+",";
						if(driveringStatusViewDtoData.get(0).getStatus().equals("OffDuty")) {
							DriveringStatus driveringStatus = new DriveringStatus();
							
							long maxId = lookupMaxIdOfDriverOperation(employeeInfo.get(i).getEmployeeId());
//							System.out.println(maxId);
							if(maxId<=0) {
								maxId=1;
								driveringStatus.setStatusId(maxId);
							}else {
								maxId++;
								driveringStatus.setStatusId(maxId);
							}
							
							driveringStatus.setDriverId(employeeInfo.get(i).getEmployeeId());
							driveringStatus.setVehicleId(0);
							driveringStatus.setStatus("OffDuty");
							driveringStatus.setLattitude(0);
							driveringStatus.setLongitude(0);
							driveringStatus.setDateTime(LocalDate.now()+" 00:00:00");
							
							LocalDateTime ldtDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
							long lDateTime = ldtDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
							
							driveringStatus.setLDateTime(lDateTime);
							driveringStatus.setUtcDateTime(lDateTime);

							driveringStatus.setLogType("System Generated");
							driveringStatus.setOrigin("System Generated");
							driveringStatus.setAppVersion("0.0");
							driveringStatus.setOsVersion("0.0");
							driveringStatus.setIsVoilation(0);
							driveringStatus.setSimCardNo("0");
							driveringStatus.setNote("");
							driveringStatus.setCustomLocation("");
							driveringStatus.setCurrentLocation("");
							driveringStatus.setEngineHour("0");
							driveringStatus.setOdometer(0);
							driveringStatus.setTimezone("");
//							driveringStatus.setShift();
//							driveringStatus.setDays();
							driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
							
							driveringStatus = driveringStatusRepo.save(driveringStatus);
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}else {
					try {
						
						DriveringStatus driveringStatus = new DriveringStatus();
						
						long maxId = lookupMaxIdOfDriverOperation(employeeInfo.get(i).getEmployeeId());
//						System.out.println(maxId);
						if(maxId<=0) {
							maxId=1;
							driveringStatus.setStatusId(maxId);
						}else {
							maxId++;
							driveringStatus.setStatusId(maxId);
						}
						
						driveringStatus.setDriverId(employeeInfo.get(i).getEmployeeId());
						driveringStatus.setVehicleId(0);
						driveringStatus.setStatus("OffDuty");
						driveringStatus.setLattitude(0);
						driveringStatus.setLongitude(0);
						driveringStatus.setDateTime(LocalDate.now()+" 00:00:00");
						
						LocalDateTime ldtDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
						long lDateTime = ldtDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						driveringStatus.setLDateTime(lDateTime);
						driveringStatus.setUtcDateTime(lDateTime);

						driveringStatus.setLogType("System Generated");
						driveringStatus.setOrigin("System Generated");
						driveringStatus.setAppVersion("0.0");
						driveringStatus.setOsVersion("0.0");
						driveringStatus.setIsVoilation(0);
						driveringStatus.setSimCardNo("0");
						driveringStatus.setNote("");
						driveringStatus.setCustomLocation("");
						driveringStatus.setCurrentLocation("");
						driveringStatus.setEngineHour("0");
						driveringStatus.setOdometer(0);
						driveringStatus.setTimezone("");
//						driveringStatus.setShift();
//						driveringStatus.setDays();
						driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
						
						driveringStatus = driveringStatusRepo.save(driveringStatus);
						
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
						
			result.setResult("Saved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Log Created Successfully."+sDebug);
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public long lookupMaxIdOfDriverOperation(long driverId){
		
		// Create an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("driverId").is(driverId)),  // Filter by driver
                Aggregation.group().max("statusId").as("statusId")        // Get the max driver
        );

        AggregationResults<MaxIdViewDto> result = mongoTemplate.aggregate(aggregation, "drivering_status", MaxIdViewDto.class);
        List<MaxIdViewDto> results = result.getMappedResults();
        
        return results.isEmpty() ? -1 : results.get(0).getStatusId();
    }
	
//	@Scheduled(cron = "${cron.expression5}")
	public ResultWrapper<String> IftaReportService() {
		String sDebug="";
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			
			LocalDate currentDate = LocalDate.now();
			currentDate = currentDate.minusDays(1);
			LocalDateTime startOfDay = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT);
			LocalDateTime endOfDay = LocalDateTime.of(currentDate, LocalTime.MAX);
			long from = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long to = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
//			System.out.println(" >> Date : "+currentDate); 
			long lastVehicleId=0, lastDriverId=0, lastTotalKm=0, lastUtcDateTime=0;
			double  lastLat=0, lastLng=0, lastOdometer=0;
			int iCount=0;
			String lastPlaceAddress="";
			Map<Long, Long> utcDateTimeMap = new HashMap<>();
			Map<Long, String> placeAddressMap = new HashMap<>();
			Map<Long, Double> odometerMap = new HashMap<>();
			List<LiveDataLogViewDto> eldLog = lookupELDLogHistoryOperation(from,to);
//			for(int i=0;i<eldLog.size();i++) {
//				long vehicleId = Long.parseLong(eldLog.get(i).getVehicleId());
				iCount++;
				sDebug+="1,";
//				lastTotalKm+=CalculateDistance(eldLog.get(i).getLattitude(),eldLog.get(i).getLongitude(),lastLat,lastLng);
				List<GeofanceMaster> geofances = geofanceMasterRepo.findAll();
				for(int g=0;g<geofances.size();g++) {
//					String geoCoords = geofances.get(g).getLatLng();
					
					HashMap latlng = geofances.get(g).getLatLng();
			        Object cords = latlng.get("coordinates");
			        String sLatLng = cords.toString();
			        
//			        sDebug+=" >> "+sLatLng;
			        
			        sLatLng = sLatLng.replace("[[[[", "");
			        sLatLng = sLatLng.replace("]]]]", "");
			        sLatLng = sLatLng.replace("[[[", "");
			        sLatLng = sLatLng.replace("]]]", "");
			        sLatLng = sLatLng.replace(",", "");
			        sLatLng = sLatLng.replace("] [", ",");
			        sLatLng = sLatLng.replace("][", ",");
			        
			        sDebug+=" >> "+sLatLng;
					
//					String polygonWKT = "POLYGON((-88.124658 30.28364, -88.224615 30.245559, -88.17335 30.252418, " +
//		                    "-88.158303 30.252393, -88.141143 30.255024, -88.130631 30.262125, " +
//		                    "-88.124722 30.273541, -88.124658 30.28364))";
//		            // Convert the polygon WKT to a JTS Polygon object
//		            WKTReader reader = new WKTReader();
//		            Polygon polygon = (Polygon) reader.read(polygonWKT);
//
//		            // Latitude/Longitude to check (Point)
//		            double lat = eldLog.get(i).getLattitude();
//		            double lng = eldLog.get(i).getLongitude();
//
//		            // Create a Point from the lat/lng coordinates
//		            Point point = new GeometryFactory().createPoint(new Coordinate(lng, lat));
//
//		            // Check if the point is inside the polygon
//		            if (polygon.contains(point)) {
////		                System.out.println("Point is inside the polygon.");
//		            	lastVehicleId=vehicleId;
//						lastDriverId = Long.parseLong(eldLog.get(i).getDriverId());
//						lastLat = eldLog.get(i).getLattitude();
//						lastLng = eldLog.get(i).getLongitude();
//						lastUtcDateTime = eldLog.get(i).getUtcDateTime();
//						lastPlaceAddress = eldLog.get(i).getPlaceAddress();
//						lastOdometer = Double.parseDouble(eldLog.get(i).getOdometer());
//						if(iCount==1) {
//							utcDateTimeMap.put(lastVehicleId, lastUtcDateTime);
//							placeAddressMap.put(lastVehicleId, lastPlaceAddress);
//							odometerMap.put(lastVehicleId, lastOdometer);
//						}
//		            } else {
////		                System.out.println("Point is outside the polygon.");
//		            	iCount=0;
//		            	
//		            }

				}
					            
				
//			}
						
			result.setResult("Saved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("IFTA Report Generated Successfully."+sDebug);
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	@Scheduled(cron = "${cron.expression5}")
	public ResultWrapper<String> ReportGeneratedService() {
		String sDebug="";
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			LocalDate currentDate = LocalDate.now();
			currentDate = currentDate.minusDays(1);
			LocalDateTime startOfDay = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT);
			LocalDateTime endOfDay = LocalDateTime.of(currentDate, LocalTime.MAX);
			long from = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long to = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			List<ELDLogData> eldLog = lookupELDLogDataHistoryOperation(from,to);
			GenerateIdlingReport(eldLog);
			GenerateIgnitionReport(eldLog);
						
			result.setResult("Saved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Report Generated Successfully."+sDebug);
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public void GenerateIdlingReport(List<ELDLogData> logs) {
		String sDebug="";
		
	    if (logs == null || logs.size() < 2) return;

	    final long MIN_IDLE_DURATION = 60 * 1000; // 1 minute in milliseconds
	    final double LOCATION_THRESHOLD = 0.0001;
	    final double SPEED_THRESHOLD = 1.0;
	    final int RPM_THRESHOLD = 500;

	    ELDLogData idleStart = null;
	    
	    try {
	    	
	    	for (int i = 0; i < logs.size(); i++) {
		        ELDLogData log = logs.get(i);
//		        boolean isIdle = log.getRPM() > RPM_THRESHOLD && log.getSpeed() <= SPEED_THRESHOLD;
		        boolean isIdle = log.getSpeed() <= SPEED_THRESHOLD;
		        sDebug+="RPM :=> "+isIdle+" : "+log.getRPM()+" : "+log.getSpeed()+"\n";
		        if (isIdle) {
		            if (idleStart == null) {
		            	sDebug+="Idle Log1, ";
		                idleStart = log;
		            } else {
		                // If location has changed, reset idleStart
		                double latDiff = Math.abs(idleStart.getLattitude() - log.getLattitude());
		                double lonDiff = Math.abs(idleStart.getLongitude() - log.getLongitude());

		                if (latDiff > LOCATION_THRESHOLD || lonDiff > LOCATION_THRESHOLD) {
		                	sDebug+="Idle Log2, ";
		                    idleStart = log;
		                }
		            }
		        } else {
		            if (idleStart != null) {
		                long duration = log.getUtcDateTime() - idleStart.getUtcDateTime();
		                if (duration >= MIN_IDLE_DURATION) {
		                    IdlingReport idlingReport = new IdlingReport();
		                    sDebug+=" Save Idle, ";
		                    idlingReport.setDriverId(idleStart.getDriverId());
		                    idlingReport.setVehicleId(idleStart.getVehicleId());
		                    idlingReport.setClientId(idleStart.getClientId());
		                    idlingReport.setVIN(idleStart.getVIN());
		                    idlingReport.setVersion(idleStart.getVersion());
		                    idlingReport.setMAC(idleStart.getMAC());
		                    idlingReport.setModel(idleStart.getModel());
		                    idlingReport.setSerialNo(idleStart.getSerialNo());

		                    idlingReport.setStartDateTime(idleStart.getDateTime());
		                    idlingReport.setEndDateTime(log.getDateTime());
		                    idlingReport.setStartUtcDateTime(idleStart.getUtcDateTime());
		                    idlingReport.setEndUtcDateTime(log.getUtcDateTime());
		                    idlingReport.setDurationMillis(duration);

		                    idlingReport.setStartAddress(idleStart.getPlaceAddress());
		                    idlingReport.setEndAddress(log.getPlaceAddress());

		                    idlingReport.setStartLattitude(idleStart.getLattitude());
		                    idlingReport.setEndLattitude(log.getLattitude());
		                    idlingReport.setStartLongitude(idleStart.getLongitude());
		                    idlingReport.setEndLongitude(log.getLongitude());

		                    idlingReport.setStartRPM(idleStart.getRPM());
		                    idlingReport.setEndRPM(log.getRPM());
		                    idlingReport.setStartCoolantTemp(idleStart.getCoolantTemp());
		                    idlingReport.setEndCoolantTemp(log.getCoolantTemp());
		                    idlingReport.setStartFuelTankTemp(idleStart.getFuelTankTemp());
		                    idlingReport.setEndFuelTankTemp(log.getFuelTankTemp());
		                    idlingReport.setStartOilTemp(idleStart.getOilTemp());
		                    idlingReport.setEndOilTemp(log.getOilTemp());
		                    idlingReport.setStartEngineHours(idleStart.getEngineHours());
		                    idlingReport.setEndEngineHours(log.getEngineHours());
		                    idlingReport.setStartOdometer(idleStart.getOdometer());
		                    idlingReport.setEndOdometer(log.getOdometer());
		                    idlingReport.setStartStateId(idleStart.getStateId());
		                    idlingReport.setEndStateId(log.getStateId());

		                    idlingReportRepo.save(idlingReport);
		                }
		                idleStart = null;
		            }
		        }
		    }

		    // Handle final idle period at end of log list
		    if (idleStart != null) {
		        ELDLogData last = logs.get(logs.size() - 1);
		        long duration = last.getUtcDateTime() - idleStart.getUtcDateTime();
		        if (duration >= MIN_IDLE_DURATION) {
		            IdlingReport idlingReport = new IdlingReport();
		            sDebug+=" Save Idle, ";
		            idlingReport.setDriverId(idleStart.getDriverId());
		            idlingReport.setVehicleId(idleStart.getVehicleId());
		            idlingReport.setClientId(idleStart.getClientId());
		            idlingReport.setVIN(idleStart.getVIN());
		            idlingReport.setVersion(idleStart.getVersion());
		            idlingReport.setMAC(idleStart.getMAC());
		            idlingReport.setModel(idleStart.getModel());
		            idlingReport.setSerialNo(idleStart.getSerialNo());

		            idlingReport.setStartDateTime(idleStart.getDateTime());
		            idlingReport.setEndDateTime(last.getDateTime());
		            idlingReport.setStartUtcDateTime(idleStart.getUtcDateTime());
		            idlingReport.setEndUtcDateTime(last.getUtcDateTime());
		            idlingReport.setDurationMillis(duration);

		            idlingReport.setStartAddress(idleStart.getPlaceAddress());
		            idlingReport.setEndAddress(last.getPlaceAddress());

		            idlingReport.setStartLattitude(idleStart.getLattitude());
		            idlingReport.setEndLattitude(last.getLattitude());
		            idlingReport.setStartLongitude(idleStart.getLongitude());
		            idlingReport.setEndLongitude(last.getLongitude());

		            idlingReport.setStartRPM(idleStart.getRPM());
		            idlingReport.setEndRPM(last.getRPM());
		            idlingReport.setStartCoolantTemp(idleStart.getCoolantTemp());
		            idlingReport.setEndCoolantTemp(last.getCoolantTemp());
		            idlingReport.setStartFuelTankTemp(idleStart.getFuelTankTemp());
		            idlingReport.setEndFuelTankTemp(last.getFuelTankTemp());
		            idlingReport.setStartOilTemp(idleStart.getOilTemp());
		            idlingReport.setEndOilTemp(last.getOilTemp());
		            idlingReport.setStartEngineHours(idleStart.getEngineHours());
		            idlingReport.setEndEngineHours(last.getEngineHours());
		            idlingReport.setStartOdometer(idleStart.getOdometer());
		            idlingReport.setEndOdometer(last.getOdometer());
		            idlingReport.setStartStateId(idleStart.getStateId());
		            idlingReport.setEndStateId(last.getStateId());

		            idlingReportRepo.save(idlingReport);
		        }
		    }
		    SaveLog(sDebug);
	    }catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	    
	}
	
	public void SaveLog(String sLine) throws Exception{
		this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/log").toAbsolutePath().normalize(); //production
		try {
			if (Files.notExists(this.fileStorageLocation)) {
				 Files.createDirectories(this.fileStorageLocation);
			}
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
        }
		Path filePath = this.fileStorageLocation.resolve("eld_log.txt");
        Files.write(filePath, sLine.getBytes(),StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}
	
	public void GenerateIgnitionReport(List<ELDLogData> logs){
		String sDebug="";
		
		if (logs == null || logs.size() < 2) return;

	    ELDLogData prev = logs.get(0);
	    boolean prevIsOn = prev.getRPM() >= 500;

	    try {
	    	for (int i = 1; i < logs.size(); i++) {
		        ELDLogData curr = logs.get(i);
		        boolean currIsOn = curr.getRPM() >= 500;
		        if (currIsOn != prevIsOn) {
		            IgnitionReport ignitionReport = new IgnitionReport();
		            sDebug+="Save Ignition, ";
		            ignitionReport.setDriverId(prev.getDriverId());
		            ignitionReport.setVehicleId(prev.getVehicleId());
		            ignitionReport.setClientId(prev.getClientId());
		            ignitionReport.setVIN(prev.getVIN());
		            ignitionReport.setVersion(prev.getVersion());
		            ignitionReport.setMAC(prev.getMAC());
		            ignitionReport.setModel(prev.getModel());
		            ignitionReport.setSerialNo(prev.getSerialNo());
		            ignitionReport.setEngineState(currIsOn ? "ON" : "OFF");
		            ignitionReport.setStartDateTime(prev.getDateTime());
		            ignitionReport.setEndDateTime(curr.getDateTime());
		            ignitionReport.setStartUtcDateTime(prev.getUtcDateTime());
		            ignitionReport.setEndUtcDateTime(curr.getUtcDateTime());
		            ignitionReport.setDurationMillis(curr.getUtcDateTime() - prev.getUtcDateTime());
		            ignitionReport.setStartAddress(prev.getPlaceAddress());
		            ignitionReport.setEndAddress(curr.getPlaceAddress());
		            ignitionReport.setStartRPM(prev.getRPM());
		            ignitionReport.setEndRPM(curr.getRPM());
		            
		            ignitionReport.setStartLattitude(prev.getLattitude());
		            ignitionReport.setEndLattitude(curr.getLattitude());
		            ignitionReport.setStartLongitude(prev.getLongitude());
		            ignitionReport.setEndLongitude(curr.getLongitude());
		            
		            ignitionReport.setStartSpeed(prev.getSpeed());
		            ignitionReport.setEndSpeed(curr.getSpeed());
		            ignitionReport.setStartCoolantTemp(prev.getCoolantTemp());
		            ignitionReport.setEndCoolantTemp(curr.getCoolantTemp());
		            ignitionReport.setStartFuelTankTemp(prev.getFuelTankTemp());
		            ignitionReport.setEndFuelTankTemp(curr.getFuelTankTemp());
		            ignitionReport.setStartOilTemp(prev.getOilTemp());
		            ignitionReport.setEndOilTemp(curr.getOilTemp());
		            ignitionReport.setStartEngineHours(prev.getEngineHours());
		            ignitionReport.setEndEngineHours(curr.getEngineHours());
		            ignitionReport.setStartOdometer(prev.getOdometer());
		            ignitionReport.setEndOdometer(curr.getOdometer());
		            ignitionReport.setStartStateId(prev.getStateId());
		            ignitionReport.setEndStateId(curr.getStateId());
		            ignitionReport.setStartGeoStateId(prev.getGeoStateId());
		            ignitionReport.setEndGeoStateId(curr.getGeoStateId());

		            ignitionReportRepo.save(ignitionReport);

		            prevIsOn = currIsOn;
			        prev = curr;
		        }

		    }
	    	
	    	SaveLog(sDebug);
	    } catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	    
	}
	
	public List<ELDLogData> lookupELDLogDataHistoryOperation(long from, long to){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"CoolantTemp","MAC","AmbTemp","DateTime","lDateTime","FuelEconomy","FuelRate","FuelTankTemp","IdleHours","Lattitude","Longitude",
				"Model","Odometer","SatStatus","SerialNo","Speed","TotalFuelIdle","TotalFuelUsed","VIN","Version","receive_time","PlaceAddress",
				"EngineHours","DriverId","VehicleId","clientId","utcDateTime","OilTemp","RPM","stateId","geoStateId").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,
	    		sort(Direction.ASC, "VehicleId").and(Direction.ASC, "utcDateTime"));
        List<ELDLogData> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , ELDLogData.class).getMappedResults();
        return results;
		
    }
	
	private static double CalculateDistance(double lat1, double lon1, double lat2, double lon2) {
	    double dist;
	    boolean calculate = true;
	    if (lat1 == lat2 && lon1 == lon2)
	      calculate = false; 
	    if (lat1 <= 0.0D || lat2 <= 0.0D || lon1 <= 0.0D || lon2 <= 0.0D)
	      calculate = false; 
	    if (lat1 > 0.0D && calculate) {
	      double theta = lon1 - lon2;
	      dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist *= 111.13384D;
	      dist = Math.round(dist * 10000.0D) / 10000.0D;
	    } else {
	      dist = 0.0D;
	    } 
	    if(dist>1){
	        dist = 0.0D;
	    }
	    return dist;
	  }
	  
	  private static double deg2rad(double deg) {
	    return deg * Math.PI / 180.0D;
	  }
	  
	  private static double rad2deg(double rad) {
	    return rad * 180.0D / Math.PI;
	  }
	
	public List<LiveDataLogViewDto> lookupELDLogHistoryOperation(long from, long to){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"MAC","AmbTemp","DateTime","FuelEconomy","FuelRate","IdleHours","Lattitude","Longitude","Model","Odometer",
				"SatStatus","SerialNo","Speed","TotalFuelIdle","TotalFuelUsed","VIN","Version","receive_time","PlaceAddress",
				"DriverId","VehicleId","utcDateTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,sort(Direction.DESC, "VehicleId"));
        List<LiveDataLogViewDto> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , LiveDataLogViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<String> ImportAndReadJsonFile(List<MultipartFile> file) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();			
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/json_file").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
			int iCount=0;
			String originalFileName="";
			for(MultipartFile mf: file) {
//				System.out.println("File Name : "+mf.getOriginalFilename());
				originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
				iCount++;
				try {
	                // Check if the file's name contains invalid characters
	                if(originalFileName.contains("..")) {
	                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
	                }
	                if(iCount==1) {
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		
	                	}else {
	       	                Path targetLocation = this.fileStorageLocation.resolve(originalFileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	       	                Files.setPosixFilePermissions(targetLocation, PosixFilePermissions.fromString("rwxrwxrwx"));

	                	}
	                }
	                
	            } catch (IOException ex) {
	                throw new Exception("Could not store file " + originalFileName + ". Please try again!", ex);
	            }
				
			}
			
//			String url = WEB_URL_BASE_PATH+"/uploads/json_file/"+originalFileName; //production
			String url = WEB_URL_FILE_UPLOAD+"/uploads/json_file/"+originalFileName; //production
//			String url = "E:\\opt\\tomcat\\webapps\\uploads\\json_file\\"+originalFileName; //localhost
			
			System.out.println(url);
			String geofanceId="",stateName="",type="";
			long stateId=0;
			HashMap<String,Object> lalLngMap= new HashMap<>();	
			GeofanceMaster geofances = new GeofanceMaster();
			try {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(url));
				JSONObject jo = (JSONObject) obj;
	            JSONArray jsonArray = (JSONArray) jo.get("features");
	            for (Object o : jsonArray) {
	            	geofances = new GeofanceMaster();
	                jo = (JSONObject) o;
	                JSONObject jo1 = (JSONObject) jo.get("properties");
//	                System.out.println(" >> " + jo.get("geometry").toString());
	                geofanceId = jo1.get("GEO_ID").toString();
	                stateId = Long.parseLong(jo1.get("STATE").toString());
	                stateName = jo1.get("NAME").toString();
//	                System.out.println(" >> " + geofanceId+" :: "+stateId+" :: "+stateName);
	                JSONObject jo2 = (JSONObject) jo.get("geometry");
	                type = jo2.get("type").toString();
	                lalLngMap= new HashMap<>();	
	    			lalLngMap.put("type", type);
	    			lalLngMap.put("coordinates", jo2.get("coordinates"));
//	                System.out.println(" >> " + lalLngMap);
	    			
	    			Object maxID = geofanceMasterRepo.findMaxIdInGeofances();
	    			Integer ID=0;
	    			if(maxID==null) {
	    				ID=1;
	    				geofances.setGeoId(ID);
	    			}else {
	    				ID = (Integer) maxID;
	    				ID++;
	    				geofances.setGeoId(ID);
	    			}
	    			geofances.setAddedTimestamp(instant.toEpochMilli());
	    			geofances.setUpdatedTimestamp(instant.toEpochMilli());
	    			
	    			geofances.setGeofanceId(geofanceId);
	    			geofances.setStateId(stateId);
	    			geofances.setStateName(stateName);
	    			geofances.setType(type);
	    			geofances.setLatLng(lalLngMap);
	    			
	    			geofanceMasterRepo.save(geofances);
	    			
	            }
	            
			} catch(Exception ex) {
				ex.printStackTrace();
			}

			result.setResult("Import");
			result.setStatus(Result.SUCCESS);
			result.setMessage("File Imported Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
}
