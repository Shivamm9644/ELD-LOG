package com.mes.eld_log.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.eld_log.dtos.EldReportDto;
import com.mes.eld_log.dtos.EldReportDto.Annotation;
import com.mes.eld_log.dtos.EldReportDto.Carrier;
import com.mes.eld_log.dtos.EldReportDto.Driver;
import com.mes.eld_log.dtos.EldReportDto.EldHeader;
import com.mes.eld_log.dtos.EldReportDto.EventRecord;
import com.mes.eld_log.dtos.EldReportDto.Vehicle;
import com.mes.eld_log.models.ClientMaster;
import com.mes.eld_log.models.ELDSettings;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.MainTerminalMaster;
import com.mes.eld_log.models.StateMaster;
import com.mes.eld_log.models.VehicleMaster;
import com.mes.eld_log.repo.ClientMasterRepo;
import com.mes.eld_log.repo.ELDSettingsRepo;
import com.mes.eld_log.repo.EmployeeMasterRepo;
import com.mes.eld_log.repo.MainTerminalMasterRepo;
import com.mes.eld_log.repo.StateMasterRepo;
import com.mes.eld_log.repo.VehicleMasterRepo;
import com.mes.eld_log.dtos.DriveringStatusViewDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/jsoncrypto")
public class JsonCryptoController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	ClientMasterRepo clientMasterRepo;
	
	@Autowired
	ELDSettingsRepo eldSettingsRepo;
	
	@Autowired
	MainTerminalMasterRepo mainTerminalMasterRepo;
	
	@Autowired
	VehicleMasterRepo vehicleMasterRepo;
	
	@Autowired
	EmployeeMasterRepo employeeMasterRepo;
	
	@Autowired
	StateMasterRepo stateMasterRepo;
	
	@Autowired
    private JavaMailSender javaMailSender;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JsonCryptoController() throws Exception {
        this.privateKey = loadPrivateKey("private_key.pem");
        this.publicKey = loadPublicKey("public.pem");
    }
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    private static final Map<String, String> STATUS_MAP = new HashMap<>();
    static {
        STATUS_MAP.put("OnDuty", "OD");
        STATUS_MAP.put("OnDrive", "ODR");
        STATUS_MAP.put("OffDuty", "OFF");
        STATUS_MAP.put("OnSleep", "OS");
        STATUS_MAP.put("PersonalUse", "PU");
        STATUS_MAP.put("YardMove", "YM");
        STATUS_MAP.put("Engine On", "EngOn");
        STATUS_MAP.put("Engine Off", "EngOff");
        STATUS_MAP.put("Violation", "VT");
    }
    
    public static String getStatusCode(String status) {
        return STATUS_MAP.getOrDefault(status, "Unknown");
    }

    /*** Encrypt JSON using AES, encrypt AES key with RSA ***/
    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptJson(@RequestBody Map<String, Object> requestData) {
        try {
        	long driverId = Long.parseLong(requestData.get("driverId").toString());
        	String fromDate = requestData.get("fromDate").toString();
        	String toDate = requestData.get("toDate").toString();
        	
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
        	
        	LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();	
			
			EldHeader header = new EldHeader();
            Carrier carrier = new Carrier();
            Driver driver = new Driver();
            Vehicle vehicle = new Vehicle();
            Annotation ann = new Annotation();
			EventRecord ev = new EventRecord();
			List<EventRecord> evList = new ArrayList<>();
			double startOdometer=0, endOdometer=0;
			String startEngineHour="", endEngineHour="";
			String driverName="";
			
			List<DriveringStatusViewDto> driveringStatusViewDtoData = lookupDriverStatusDataForGraphOperation(from,to,driverId);
			String formatted = Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
			for(int i=0;i<driveringStatusViewDtoData.size();i++) {
				if(i==0) {
					try {
						Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").ascending());
						Query query = new Query();
				        query.addCriteria(
		        			Criteria.where("utcDateTime").gte(from).lte(to)
		        			.and("driverId").is(driverId)
		        			.and("odometer").gt(0)
		        		);
						query.with(pageableRequest);
						List<DriveringStatusViewDto> dsLogData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
						startOdometer = dsLogData.get(0).getOdometer();
						startEngineHour = dsLogData.get(0).getEngineHour();
								
						pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						query = new Query();
						query.addCriteria(
		        			Criteria.where("utcDateTime").gte(from).lte(to)
		        			.and("driverId").is(driverId)
		        			.and("odometer").gt(0)
		        		);
						query.with(pageableRequest);
						dsLogData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
						endOdometer = dsLogData.get(0).getOdometer();
						endEngineHour = dsLogData.get(0).getEngineHour();						
						
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					
		            EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDtoData.get(i).getDriverId());
					
					MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());

		            ClientMaster clientInfo = clientMasterRepo.findByClientId((int)driveringStatusViewDtoData.get(i).getClientId());
		            
		            StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
		            
		            VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
		            
		            // carrier
		            carrier.setCarrierName(clientInfo.getClientName());
		            carrier.setUsdotNumber(clientInfo.getDotNo());
		            carrier.setHomeTerminal(mainTerminal.getMainTerminalName());

		            // driver
		            driver.setDriverId(empInfo.getDriverId());
		            driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
		            driver.setDriverName(driverName);
		            driver.setLicenseNumber(empInfo.getCdlNo());
		            driver.setLicenseState(stateInfo.getStateName());

		            // vehicles
		            vehicle.setUnitNumber(vehcileInfo.getVehicleNo());
		            vehicle.setVin(vehcileInfo.getVin());
		            vehicle.setOdometerStart(String.format("%.2f", (startOdometer)));
		            vehicle.setOdometerEnd(String.format("%.2f", (endOdometer)));
		            
		            //annotation
		            ann.setEventRef(formatted);
		            ann.setNote("");
		            
				}
				
				String eventTime = Instant.ofEpochMilli(driveringStatusViewDtoData.get(i).getUtcDateTime())
						.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
				
				ev = new EventRecord();
	            ev.setEventCode(getStatusCode(driveringStatusViewDtoData.get(i).getStatus()));
	            ev.setEventDescription(driveringStatusViewDtoData.get(i).getStatus());
	            ev.setEventTime(eventTime);
	            ev.setLocation(driveringStatusViewDtoData.get(i).getCustomLocation());
	            ev.setOdometer(String.valueOf(driveringStatusViewDtoData.get(i).getOdometer()));
	            ev.setEngineHours(driveringStatusViewDtoData.get(i).getEngineHour());
	            evList.add(ev);
				
			}
			
			Map<String, Object> requestJsonData = new HashMap<>();
			requestJsonData.put("eventRecords", evList);
            
			byte[] jsonBytes = mapper.writeValueAsBytes(requestJsonData);
			String signatureEncrypted = signData(jsonBytes, privateKey);
//            String signatureEncrypted = signData(requestJsonData.toString(),privateKey);

            List<ELDSettings> settings =  eldSettingsRepo.findAndViewBySettingId(1);
			// header
			if(settings.size()>0) {
				header.setEldIdentifier(settings.get(0).getEldIdentifier());
	            header.setEldProvider(settings.get(0).getEldProvider());
	            header.setEldSoftwareVersion(settings.get(0).getEldSoftwareVersion());
			}
            header.setOutputFileFormat("JSON");
            header.setFileGeneratedTime(formatted);
            header.setFileSignature(signatureEncrypted);
            
            EldReportDto report = new EldReportDto();
            report.setEldHeader(header);
            report.setCarrier(carrier);
            report.setDriver(driver);
            report.setVehicle(vehicle);
            report.setEventRecords(evList);
            report.setAnnotations(Collections.singletonList(ann));
            report.setMalfunctions(Collections.emptyList());
            report.setDiagnostics(Collections.emptyList());
            report.setSignature(signatureEncrypted);

            String json = mapper.writeValueAsString(report);
            
            Map<String, Object> response = new HashMap<>();
//            response.put("data", requestJsonData.toString());
            response.put("eventRecords", evList);
            response.put("signature", signatureEncrypted);
            
            String email=requestData.get("email").toString();
            
            if (email != null && !email.isEmpty()) {
//                String subject = "Encrypted Log Report";
//
//                String htmlBody =
//                	    "<div style='font-family:Arial,sans-serif; margin:20px; text-align:center;'>"
//                	  + "  <h2 style='color:#333;'>" + driverName + " sent driver logs</h2>"
//                	  + "  <p style='font-size:14px; color:#555;'>" +formatDateRange(fromDate, toDate) + "</p>"
//                	  + "  <div id='jsonLogs' style='margin-top:20px; padding:15px; border:1px solid #ccc; background:#f9f9f9; "
//                	  + "       font-family:Courier New, monospace; white-space:pre; text-align:left;'>"
//                	  +        json
//                	  + "  </div>"
//                	  + "  <p style='color:#999; font-size:12px; margin-top:20px;'>"
//                	  + "     This is an automated message. Please do not reply."
//                	  + "  </p>"
//                	  + "</div>";
//                
//                sendEmail(email, subject, htmlBody);
            	
            	String subject = "Encrypted Log Report";

            	String htmlBody =
            	    "<div style='font-family:Arial,sans-serif; margin:20px; text-align:center;'>"
            	  + "  <h2 style='color:#333;'>" + driverName + " - Driver Logs</h2>"
            	  + "  <p style='font-size:14px; color:#555;'>Report Period: " + formatDateRange(fromDate, toDate) + "</p>"
            	  + "  <p style='color:#999; font-size:12px; margin-top:20px;'>"
            	  + "     The encrypted driver log report is attached as a JSON file.</p>"
            	  + "  <p style='color:#aaa; font-size:11px;'>This is an automated message. Please do not reply.</p>"
            	  + "</div>";

            	String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);

            	String fileName = "driver_logs_" + driverId + "_" + System.currentTimeMillis() + ".json";
            	java.io.File tempFile = new java.io.File(System.getProperty("java.io.tmpdir"), fileName);

            	try (java.io.FileWriter writer = new java.io.FileWriter(tempFile)) {
            	    writer.write(prettyJson);
            	}

            	sendEmailWithAttachment(email, subject, htmlBody, tempFile);
            	
            }
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    public void sendEmailWithAttachment(String to, String subject, String htmlBody, java.io.File attachment)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        if (attachment != null && attachment.exists()) {
            helper.addAttachment(attachment.getName(), attachment);
        }

        javaMailSender.send(message);

        // Optionally delete temp file after sending
        if (attachment.exists()) {
            attachment.delete();
        }
    }
    
    public static String signData_old(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes("UTF-8"));
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }
    
    public static String signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }
    
    public static String formatDateRange(String fromDateStr, String toDateStr) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, inputFormat);
        LocalDateTime toDate = LocalDateTime.parse(toDateStr, inputFormat);

        // Output format like: Sep 1, 2025
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        String formattedFrom = fromDate.format(monthDayYear);
        String formattedTo = toDate.format(monthDayYear);

        if (formattedFrom.equals(formattedTo)) {
            return formattedFrom; // same date
        } else {
            return formattedFrom + " - " + formattedTo;
        }
    }
    
    public List<DriveringStatusViewDto> lookupDriverStatusDataForGraphOperation(long from, long to, long driverId){
		
		MatchOperation filter = null;
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
					.and("isVisible").is(1)
					.and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty","PersonalUse","YardMove","Voilation")
//					.and("origin").is("Driver")
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("isVisible").is(1)
					.and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty","PersonalUse","YardMove","Voilation")
//					.and("origin").is("Driver")
				);
		}
			
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType","utcDateTime",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId","osVersion","simCardNo","origin",
				"timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime","clientId").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
	    
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
    
    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
	    MimeMessage message = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(htmlBody, true); // true = HTML
	    javaMailSender.send(message);
	}
    
    private boolean verifySignature(byte[] data, String signatureBase64, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(signatureBase64));
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyJson(@RequestBody Map<String, Object> signedPayload) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Extract & remove signature
            String signature = (String) signedPayload.remove("signature");

            if (signature == null) {
                response.put("valid", false);
                response.put("error", "Missing signature field");
                return response;
            }

            // Recreate original JSON
            byte[] jsonBytes = mapper.writeValueAsBytes(signedPayload);

            // Load public key
//            PublicKey publicKey = loadPublicKey("public.pem");

            // Verify
            boolean valid = verifySignature(jsonBytes, signature, publicKey);
            response.put("valid", valid);
            response.put("data", signedPayload);
            
        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", e.getMessage());
            e.printStackTrace(); // logs full details in console
        }
        return response;
    }

    /*** Load RSA private key ***/
    private static PrivateKey loadPrivateKey(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource("pem_file/" + filename);
        String keyPem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(keyPem);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    /*** Load RSA public key from X.509 cert ***/
    private static PublicKey loadPublicKey(String filename) throws Exception {
    	ClassPathResource resource = new ClassPathResource("pem_file/" + filename);
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        key = key.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
