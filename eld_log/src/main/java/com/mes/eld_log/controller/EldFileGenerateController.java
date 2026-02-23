
package com.mes.eld_log.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.eld_log.dtos.CertifiedLogViewDto;
import com.mes.eld_log.dtos.DriveringStatusViewDto;
import com.mes.eld_log.dtos.EldReportDto;
import com.mes.eld_log.dtos.EldReportDto.Annotation;
import com.mes.eld_log.dtos.EldReportDto.Carrier;
import com.mes.eld_log.dtos.EldReportDto.Driver;
import com.mes.eld_log.dtos.EldReportDto.EldHeader;
import com.mes.eld_log.dtos.EldReportDto.EventRecord;
import com.mes.eld_log.dtos.EldReportDto.Vehicle;
import com.mes.eld_log.models.ClientMaster;
import com.mes.eld_log.models.DriverStatusLog;
import com.mes.eld_log.models.ELDSettings;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.LoginLog;
import com.mes.eld_log.models.StateMaster;
import com.mes.eld_log.models.VehicleMaster;
import com.mes.eld_log.repo.ClientMasterRepo;
import com.mes.eld_log.repo.DriverStatusLogRepo;
import com.mes.eld_log.repo.ELDSettingsRepo;
import com.mes.eld_log.repo.EmployeeMasterRepo;
import com.mes.eld_log.repo.StateMasterRepo;
import com.mes.eld_log.repo.VehicleMasterRepo;
import com.mes.eld_log.serviceImpl.FmcsaSmimeEmailService;
import com.mes.eld_log.util.ELdUtils;

@RestController
@RequestMapping("/api/eld") 
public class EldFileGenerateController {

	private static final String FALLBACK_VIN = "4V4NC9EJ3GN959666";
	private static final String FALLBACK_SW = "1.0.2";
	private static final String FALLBACK_ELDID = "ELD001";
	private static final String FALLBACK_ELD_REG_ID = "TEST";

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ClientMasterRepo clientMasterRepo;
	@Autowired
	private ELDSettingsRepo eldSettingsRepo;
	@Autowired
	private VehicleMasterRepo vehicleMasterRepo;
	@Autowired
	private EmployeeMasterRepo employeeMasterRepo;
	@Autowired
	private StateMasterRepo stateMasterRepo;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private ELdUtils eldUtils;
	@Autowired
	private DriverStatusLogRepo driverStatusLogRepo;
	@Autowired
	private ObjectProvider<FmcsaSmimeEmailService> fmcsaSmimeEmailServiceProvider;

//   
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private final ObjectMapper mapper = new ObjectMapper();

	public EldFileGenerateController() throws Exception {
		this.privateKey = loadPrivateKey("private_key.pem");
		this.publicKey = loadPublicKey("public.pem");
	}

	// ================== STATUS MAP (CSV Event Codes) ==================
	private static final Map<String, String> STATE_CODE_MAP = Map.ofEntries(
	        Map.entry("ALABAMA", "AL"),
	        Map.entry("ALASKA", "AK"),
	        Map.entry("ARIZONA", "AZ"),
	        Map.entry("ARKANSAS", "AR"),
	        Map.entry("CALIFORNIA", "CA"),
	        Map.entry("COLORADO", "CO"),
	        Map.entry("CONNECTICUT", "CT"),
	        Map.entry("DELAWARE", "DE"),
	        Map.entry("DISTRICT OF COLUMBIA", "DC"),
	        Map.entry("FLORIDA", "FL"),
	        Map.entry("GEORGIA", "GA"),
	        Map.entry("HAWAII", "HI"),
	        Map.entry("IDAHO", "ID"),
	        Map.entry("ILLINOIS", "IL"),
	        Map.entry("INDIANA", "IN"),
	        Map.entry("IOWA", "IA"),
	        Map.entry("KANSAS", "KS"),
	        Map.entry("KENTUCKY", "KY"),
	        Map.entry("LOUISIANA", "LA"),
	        Map.entry("MAINE", "ME"),
	        Map.entry("MARYLAND", "MD"),
	        Map.entry("MASSACHUSETTS", "MA"),
	        Map.entry("MICHIGAN", "MI"),
	        Map.entry("MINNESOTA", "MN"),
	        Map.entry("MISSISSIPPI", "MS"),
	        Map.entry("MISSOURI", "MO"),
	        Map.entry("MONTANA", "MT"),
	        Map.entry("NEBRASKA", "NE"),
	        Map.entry("NEVADA", "NV"),
	        Map.entry("NEW HAMPSHIRE", "NH"),
	        Map.entry("NEW JERSEY", "NJ"),
	        Map.entry("NEW MEXICO", "NM"),
	        Map.entry("NEW YORK", "NY"),
	        Map.entry("NORTH CAROLINA", "NC"),
	        Map.entry("NORTH DAKOTA", "ND"),
	        Map.entry("OHIO", "OH"),
	        Map.entry("OKLAHOMA", "OK"),
	        Map.entry("OREGON", "OR"),
	        Map.entry("PENNSYLVANIA", "PA"),
	        Map.entry("RHODE ISLAND", "RI"),
	        Map.entry("SOUTH CAROLINA", "SC"),
	        Map.entry("SOUTH DAKOTA", "SD"),
	        Map.entry("TENNESSEE", "TN"),
	        Map.entry("TEXAS", "TX"),
	        Map.entry("UTAH", "UT"),
	        Map.entry("VERMONT", "VT"),
	        Map.entry("VIRGINIA", "VA"),
	        Map.entry("WASHINGTON", "WA"),
	        Map.entry("WEST VIRGINIA", "WV"),
	        Map.entry("WISCONSIN", "WI"),
	        Map.entry("WYOMING", "WY")
	);

	private static final Map<String, String> STATUS_MAP = new HashMap<>();
	static {
		STATUS_MAP.put("OnDrive", "ODR");
		STATUS_MAP.put("OnDuty", "OD");
		STATUS_MAP.put("OffDuty", "OFF");
		STATUS_MAP.put("OnSleep", "OS");
		STATUS_MAP.put("PersonalUse", "PC");
		STATUS_MAP.put("YardMove", "YM");
		STATUS_MAP.put("Violation", "VT");
	}

	private static final Map<String, Integer> HOS_ROW_MAP = new HashMap<>();
	static {
		// Row indices: 0=OFF, 1=SB, 2=D, 3=ON
		HOS_ROW_MAP.put("OFF", 0);
		HOS_ROW_MAP.put("SB", 1);
		HOS_ROW_MAP.put("D", 2);
		HOS_ROW_MAP.put("ON", 3);
		HOS_ROW_MAP.put("PC", 0); // PC -> off-duty row
		HOS_ROW_MAP.put("YM", 3); // YM -> on-duty row
	}

	// Map DB status string to HOS status (OFF / SB / D / ON / PC / YM)
	private String hosStatus(String status) {
		if (status == null)
			return "OFF";
		String s = status.trim().toUpperCase();

		if (s.contains("LOGIN") || s.contains("LOGOUT"))
			return "OTHER"; // separate row in UI
		if (s.contains("PERSONAL") || s.contains("PC"))
			return "PC";
		if (s.contains("YARD") || s.equals("YM"))
			return "YM";
		if (s.contains("SLEEP") || s.contains("SB"))
			return "SB";
		if (s.contains("DRIVE") || s.equals("ONDRIVE"))
			return "D";
		if (s.contains("ONDUTY") || s.equals("ON") || s.equals("OD"))
			return "ON";
		if (s.contains("OFF"))
			return "OFF";
		return "OFF";
	}

	// ===================== MAIN CSV API =====================
	@PostMapping("/generateCsv")
	public ResponseEntity<?> generateCsvAndSubmitFmcsa(@RequestBody Map<String, Object> req) {
		File tempFile = null;

		try {
			if (!req.containsKey("driverId"))
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "driverId required"));

			if (!req.containsKey("fromDate") || !req.containsKey("toDate"))
				return ResponseEntity.badRequest()
						.body(Map.of("success", false, "message", "fromDate & toDate required"));

			String fromAddress = req.get("fromAddress") == null ? "" : req.get("fromAddress").toString().trim();
			String regId = req.get("registrationId") == null ? "" : req.get("registrationId").toString().trim();
			String identifier = req.get("identifier") == null ? "" : req.get("identifier").toString().trim();
			boolean isTest = req.get("test") != null && Boolean.parseBoolean(req.get("test").toString());
			String comment = req.get("outputFileComment") == null ? "" : req.get("outputFileComment").toString();
			String email = req.get("email").toString().trim();

//            if (fromAddress.isEmpty())
//                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "fromAddress required"));

//            if (regId.isEmpty() || identifier.isEmpty())
//                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "registrationId & identifier required"));

			// ✅ One time get
			FmcsaSmimeEmailService smime = fmcsaSmimeEmailServiceProvider.getIfAvailable();
			if (smime == null) {
				return ResponseEntity.status(500).body(Map.of("success", false, "message",
						"S/MIME service not available. BouncyCastle CMS not visible to Tomcat. "
								+ "Fix: put bcprov/bcpkix/bcmail/bcutil jars in REAL CATALINA_BASE/lib and restart."));
			}

			// 1) Build report + signature
			  Map<String, Object> built = buildAndSign(req);
	            EldReportDto report = (EldReportDto) built.get("report");
	            String sigHex = (String) built.get("signature");
	            List<EventRecord> events = (List<EventRecord>) built.get("events");
			if (sigHex == null)
				sigHex = "";

			String csvContent = buildCsv(report, sigHex, regId, comment);

			// normalize: CR only
			csvContent = csvContent.replace("\r\n", "\r").replace("\n", "\r");

			// ASCII bytes
			byte[] fileBytes = csvContent.getBytes(java.nio.charset.StandardCharsets.US_ASCII);

			// hard guarantee: remove any LF bytes if still present
			fileBytes = normalizeCrOnly(fileBytes);
			long lf = 0;
			for (byte b : fileBytes) {
			    if (b == 0x0A) lf++;
			}
			System.out.println("LF bytes BEFORE write = " + lf);


			// 3) Temp file
			String attachfile = "Eld_log_output_" + (isTest ? "TEST_" : "") + regId + "_" + identifier + ".csv";

			tempFile = File.createTempFile("eld_output_Test_Textxx", ".csv");
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(fileBytes);
			}
			byte[] diskBytes = Files.readAllBytes(tempFile.toPath());

			long lf2 = 0;
			for (byte b : diskBytes) {
			    if (b == 0x0A) lf2++;
			}
			System.out.println("LF bytes AFTER write = " + lf2);

			// 4) ICD subject format
//			String subject = (isTest ? "TEST: " : "") + "ELD records from " + regId + ":" + identifier;

			// 5) Send FMCSA mail
			sendMail(email, tempFile, comment, attachfile);

			return ResponseEntity.ok(Map.of("success", true, "message", "ELD file generated and emailed successfully",
					"email", email, "attachfile", attachfile,"event",events

			));

		} catch (NoClassDefFoundError ncd) {
			return ResponseEntity.status(500)
					.body(Map.of("success", false, "message", "BouncyCastle runtime missing in Tomcat. Missing class: "
							+ ncd.getMessage()
							+ ". Fix: Put bcprov/bcpkix/bcmail/bcutil jars in REAL Tomcat CATALINA_BASE/lib and restart."));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(
					Map.of("success", false, "message", "Error: " + e.getClass().getName() + " " + e.getMessage()));
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
	}

	@GetMapping("/ping")
	public String ping() {
		return "eld controller OK";
	}
	// ===================== BUILD REPORT =====================

	public Map<String, Object> buildAndSign(Map<String, Object> req) throws Exception {
    	
    	ELdUtils.TruckSeqCounter seqCounter = null;

        long driverId = Long.parseLong(req.get("driverId").toString());
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sDebug=" >> ";
        long from = LocalDateTime.parse(req.get("fromDate").toString(), f)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long to = LocalDateTime.parse(req.get("toDate").toString(), f)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        List<DriveringStatusViewDto> rows = getRows(from, to, driverId);

        EldReportDto rep = new EldReportDto();
        Carrier car = new Carrier();
        Driver driver = new Driver();
        Vehicle veh = new Vehicle();
        CertifiedLogViewDto certified=new CertifiedLogViewDto();
        Annotation ann = new Annotation();
        List<EventRecord> evList = new ArrayList<>();

//        List<DriveringStatusViewDto> rows = getRows(from, to, driverId);
        String origin="";
        if (!rows.isEmpty()) {
            DriveringStatusViewDto firstRow = rows.get(0);

            // ---------- DRIVER ----------
            EmployeeMaster emp = employeeMasterRepo.findByEmployeeId((int) firstRow.getDriverId());
            if (emp != null) {
                driver.setDriverId(emp.getDriverId());
                driver.setDriverName((emp.getFirstName() + " " + emp.getLastName()).trim());
                driver.setLicenseNumber(emp.getCdlNo());
                StateMaster sm = stateMasterRepo.findByStateId((int) emp.getCdlStateId());
                driver.setLicenseState(sm != null ? safeState(sm.getStateName()) : "");
            }

            // ---------- VEHICLE ----------
            VehicleMaster vm = vehicleMasterRepo.findByVehicleId((int) firstRow.getVehicleId());

            String vin;
            String unitNumber;

            if (vm != null) {

                vin = (vm.getVin() != null && !vm.getVin().trim().isEmpty())
                        ? fixVin(vm.getVin().trim())
                        : FALLBACK_VIN;

                unitNumber = (vm.getVehicleNo() != null && !vm.getVehicleNo().trim().isEmpty())
                        ? vm.getVehicleNo().trim()
                        : vin; // ✅ fallback to VIN (not hardcode)

            } else {
                vin = FALLBACK_VIN;
                unitNumber = vin;
            }

            veh.setVin(vin);
            veh.setUnitNumber(unitNumber);

            // ✅ truck-wise key (best is VIN; if VIN can be fallback, you can use unitNumber)
            String truckKey = vin;

            // ✅ Windows-safe persistent file path
            Path seqPath = Paths.get(System.getProperty("user.dir"), "eld_state", "seq_" + truckKey + ".txt");
            seqCounter = new ELdUtils.TruckSeqCounter(seqPath);
           

            // ---------- CARRIER ----------
            ClientMaster cl = clientMasterRepo.findByClientId((int) firstRow.getClientId());
            if (cl != null) {
                car.setCarrierName(cl.getClientName());
                car.setUsdotNumber(cl.getDotNo());
                
     
         // IGNITION CYCLE START TRACKERS (WHOLE + TENTHS)
                long cycleStartMilesWhole = -1;
                int  cycleStartEngTenths  = -1; 
             
// 1406.8 -> 14068

                for (DriveringStatusViewDto row : rows) {
                	

                    EventRecord ev = new EventRecord();
                    DriverStatusLog dsLog = driverStatusLogRepo.findAndViewDriverStatusLogById1(row.get_id(),"showing");
					long seqNo = (dsLog != null) ? dsLog.getStatusId() : 0L;

                    String status      = n(row.getStatus());
                    String statusUpper = status.trim().toUpperCase();
                    origin = n(row.getOrigin()).trim().toUpperCase();

                    String t = Instant.ofEpochMilli(row.getUtcDateTime())
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
                    ev.setEventTime(t);

                    // TOTAL ECM values
                    Double odoObj = row.getOdometer();
                    double totalMilesRaw = (odoObj != null) ? odoObj : 0.0;
                    long totalMilesWhole = (long) Math.floor(totalMilesRaw + 0.00001);

                    double totalEngRaw = parseDouble(row.getEngineHour());
                    int totalEngTenths = (int) Math.round(totalEngRaw * 10.0);

                    boolean isEngineOnStatus =
                            statusUpper.contains("ENGINE ON") || statusUpper.contains("ENGINEON");

                    // ✅ initialize cycle start if missing
                    if (cycleStartMilesWhole < 0) {
                        cycleStartMilesWhole = totalMilesWhole;
                        cycleStartEngTenths  = totalEngTenths;
                    }

                    // ✅ Engine ON resets cycle start
                    if (isEngineOnStatus) {
                        cycleStartMilesWhole = totalMilesWhole;
                        cycleStartEngTenths  = totalEngTenths;
                    }

                    long accumMilesWhole = Math.max(0, totalMilesWhole - cycleStartMilesWhole);
                    int  elapsedEngTenths = Math.max(0, totalEngTenths - cycleStartEngTenths);

                    ev.setTotalVehicleMiles(String.valueOf(totalMilesWhole));
                    ev.setTotalEngineHours(String.format(Locale.US, "%.1f", totalEngTenths / 10.0));

                    ev.setOdometer(String.valueOf(accumMilesWhole));
                    ev.setEngineHours(String.format(Locale.US, "%.1f", elapsedEngTenths / 10.0));

                    // Location
                    Double latVal = row.getLattitude();
                    Double lonVal = row.getLongitude();
                    ev.setLocation((latVal == null || lonVal == null) ? "0,0" : (latVal + "," + lonVal));
                 



                    // Origin
                    String originCode;
                    if (statusUpper.contains("ENGINE ON") || statusUpper.contains("ENGINEON") ||
                        statusUpper.contains("ENGINE OFF") || statusUpper.contains("ENGINEOFF") ||statusUpper.contains("First Certified")||
                        statusUpper.contains("Recertified")||origin.contains("INTERMEDIATE")){
                        originCode = "1";
                    } else if (origin.contains("DRIVER") || origin.contains("MANUAL")) {
                        originCode = "2";
                    } else {
                        originCode = "1";
                    }
                    ev.setOrigin(originCode);

                    // Event type/code
                    
                    
                    if (origin.contains("INTERMEDIATE")) {
                        ev.setEventType("2");
                        ev.setEventCode("1");
                        ev.setEventDescription("Intermediate Log");
                       
                        
                    }
//                    
                        else if (statusUpper.contains("ENGINE ON") || statusUpper.contains("ENGINEON")) {
                        ev.setEventType("6");
                        ev.setEventCode("1");
                        ev.setEventDescription("EngineON");
                    } else if (statusUpper.contains("ENGINE OFF") || statusUpper.contains("ENGINEOFF")) {
                        ev.setEventType("6");
                        ev.setEventCode("3"); // ✅ FIXED shut-down
                        ev.setEventDescription("ENGINEOFF");
                    }
                    else if (statusUpper.contains("FIRST CERTIFIED") || statusUpper.equals("CERTIFIED") || statusUpper.contains(" CERTIFIED")) {
                        ev.setEventType("4");
                        ev.setEventCode("1");
                        ev.setEventDescription("First Certified");
                    }
                    else if (statusUpper.contains("RE CERTIFIED") || statusUpper.contains("RECERTIFIED") || statusUpper.contains("RE-CERTIFIED")) {
                        ev.setEventType("4");
                        ev.setEventCode("2");
                        ev.setEventDescription("Re-certified");
                    }
                   
//                    else if (statusUpper.contains("MALFUNCTION")) {
//
//                        ev.setEventType("7");           // internal FMCSA type
//
//                        ev.setEventCode("1");           // CSV EventCode = Malfunction
//                        ev.setEventDescription("M1");   // Power compliance (safe default)
//
//                    }
//                    else if (statusUpper.contains("DIAGNOSTIC") || statusUpper.contains("DIAGONSTIC")) {
//
//                        ev.setEventType("7");           // internal FMCSA type
//
//                        ev.setEventCode("2");           // CSV EventCode = Diagnostic
//                        ev.setEventDescription("D1");   // Power diagnostic
//
//                    }

                        else {
                        ev.setEventType("1");
                        ev.setEventCode(dutyCode(status));
                        ev.setEventDescription(status);
                    }

                    evList.add(ev);
                    
                    
                    

                    // PC/YM same as you wrote...
                

                // ---------- PC / YM SPECIAL DRIVING (TYPE 3) ----------
//                if ("1".equals(ev.getEventType())) {
//                    int specialCat = specialDrivingCategory(status);  // 0,1,2
//                    if (specialCat > 0) {
//                        EventRecord sc = new EventRecord();
//                        sc.setEventTime(ev.getEventTime());
//                        sc.setOdometer(ev.getOdometer());
//                        sc.setEngineHours(ev.getEngineHours());
//                        sc.setTotalVehicleMiles(ev.getTotalVehicleMiles());
//                        sc.setTotalEngineHours(ev.getTotalEngineHours());
//                        sc.setLocation(ev.getLocation());
//                        sc.setEventType("3");                         // special category change
//                        sc.setEventCode(String.valueOf(specialCat));  // 1 = PC, 2 = YM
//                        sc.setEventDescription(specialCat == 1 ? "PC" : "YM");
//                        sc.setOrigin(ev.getOrigin());
//                       
//
//
//                        evList.add(sc);
//                    }
//                }
            }
            
            

//         // ---------- INSERT REQUIRED POWER-UP EVENT IF MISSING ----------
//            if (!evList.isEmpty()) {
//                boolean hasPowerUp = evList.stream().anyMatch(e ->
//                        "6".equals(n(e.getEventType()).trim()) &&
//                        "1".equals(n(e.getEventCode()).trim())
//                );
//
//               if (!hasPowerUp) {
//                    EventRecord first = evList.get(0);
//                    EventRecord powerUp = new EventRecord();
//                    powerUp.setEventType("6");
//                   powerUp.setEventCode("1");
//                    powerUp.setEventDescription("Engine power-up");
//                    powerUp.setEventTime(first.getEventTime());
//                    powerUp.setOdometer(first.getOdometer());
//                    powerUp.setEngineHours(first.getEngineHours());
//                    powerUp.setTotalVehicleMiles(first.getTotalVehicleMiles());
//                    powerUp.setTotalEngineHours(first.getTotalEngineHours());
//                    powerUp.setLocation(first.getLocation() == null || first.getLocation().isBlank()
//                            ? "0,0" : first.getLocation());
//                    evList.add(0, powerUp);
//                }
//            }
                
                

             
//            // Make modifiable list (safe)
                List<EventRecord> events = new ArrayList<>(evList);

                List<CertifiedLogViewDto> certsRaw = lookupCertifiedLogDataOperation(from, to, driverId);
                List<CertifiedLogViewDto> certs = (certsRaw == null) ? new ArrayList<>() : new ArrayList<>(certsRaw);

                System.out.println("CERTS SIZE = " + certs.size());

                certs.sort(Comparator.comparing(
                        CertifiedLogViewDto::getLCertifiedDate,
                        Comparator.nullsLast(Long::compareTo)
                ));

         

                              boolean firstDone = false;

                for (CertifiedLogViewDto c : certs) {
                	

                    Long tsObj = c.getLCertifiedDate();
                    if (tsObj == null || tsObj <= 0L)
                    	
                    	continue;
                    DriverStatusLog dsLog = driverStatusLogRepo.findAndViewDriverStatusLogById1(c.get_id(),"Certified");
                	long seqNo = (dsLog != null) ? dsLog.getStatusId() : 0L;

                    // 👉 DB time is PT wall-clock stored as UTC
//                    LocalDateTime ptLocal = LocalDateTime.ofInstant(
//                            Instant.ofEpochMilli(tsObj),
//                            ZoneOffset.UTC
//                    );
//
//                    // ✅ IMPORTANT: skip if not same PT day
//                    if (!ptLocal.toLocalDate().equals(reqDay)) {
//                        continue;   // 🔥 THIS LINE PREVENTS "First certification"
//                    }
                	EventRecord ce = new EventRecord();
                	ce.setEventType("4");
                	ce.setEventCode(firstDone ? "2" : "1");
                	ce.setEventDescription(firstDone ? "Re-certification" : "First certification");
                	firstDone = true;

                	Instant instant = Instant.ofEpochMilli(tsObj);
                	OffsetDateTime utc = instant.atOffset(ZoneOffset.UTC);

                	ce.setEventTime(utc.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                	ce.setEventDate(utc.format(DateTimeFormatter.ofPattern("MMddyy")));

                	ce.setLocation("0,0");
                	ce.setOrigin("1");
//                	ce.setEventSeqId(seqCounter.nextHex());


                    events.add(ce);
                }


                System.out.println("EVENTS AFTER CERT = " + events.size());
                System.out.println("CERT EVENTS = " + events.stream()
                        .filter(x -> "4".equals(String.valueOf(x.getEventType()).trim()))
                        .count());

                // ✅ CRITICAL: now use this list everywhere
                evList = events;



            List<CertifiedLogViewDto> certifiedLogs = lookupCertifiedLogDataOperation(from, to, driverId);

            String trailerNo = "NA";
            String shippingId = "NA";

            if (certifiedLogs != null && !certifiedLogs.isEmpty()) {

                // agar latest chahiye to list sort hona chahiye, warna first le rahe hain
                CertifiedLogViewDto cl1 = certifiedLogs.get(0);

                // Trailer (CertifiedLogViewDto.getTrailers())
                if (cl1.getTrailers() != null && !cl1.getTrailers().isEmpty()) {
                    trailerNo = String.valueOf(cl1.getTrailers().get(0)).trim();
                    if (trailerNo.isEmpty()) trailerNo = "NA";
                }

                // Shipping (CertifiedLogViewDto.getShippingDocs())
                if (cl1.getShippingDocs() != null && !cl1.getShippingDocs().isEmpty()) {
                    shippingId = String.valueOf(cl1.getShippingDocs().get(0)).trim();
                    if (shippingId.isEmpty()) shippingId = "NA";
                }
            }
            
            // ✅ yahin store kar do, taaki CSV me direct mil jaye
            veh.setTrailerNo(trailerNo);
            ann.setShippingId(shippingId);

			
            List<LoginLog> loginLogs = lookupLoginLogDataOperation(from, to, driverId, "loginDateTime");
            for (LoginLog log : loginLogs) {
                if (log.getLoginDateTime() > 0L) {
					
					EventRecord loginEv = new EventRecord();
                    loginEv.setEventType("5");   // Login/Logout
                    loginEv.setEventCode("1"); 
                
                    String t = Instant.ofEpochMilli(log.getLoginDateTime())
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
                    loginEv.setEventTime(t);

                    loginEv.setEventDescription("Login");
                    loginEv.setOdometer("0000");
                    loginEv.setEngineHours("000");
                    loginEv.setTotalVehicleMiles("0.00");
                    loginEv.setTotalEngineHours("0.00");
                    loginEv.setLocation("0,0");
                    loginEv.setOrigin("1");
                   
                    
                  

                    evList.add(loginEv);
                }
            }

            // Logout logs
            List<LoginLog> logoutLogs = lookupLoginLogDataOperation(from, to, driverId, "logoutDateTime");
            for (LoginLog log : logoutLogs) {
            	
                if (log.getLogoutDateTime() > 0L) {
                    EventRecord logoutEv = new EventRecord();
                    logoutEv.setEventType("5");   // Login/Logout
                    logoutEv.setEventCode("2");

                    String t = Instant.ofEpochMilli(log.getLogoutDateTime())
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
                    logoutEv.setEventTime(t);
                    logoutEv.setEventDescription("Logout");
                    logoutEv.setOdometer("0000");
                    logoutEv.setEngineHours("000");
                    logoutEv.setTotalVehicleMiles("0.00");
                    logoutEv.setTotalEngineHours("0.00");
                    logoutEv.setLocation("0,0");
                    logoutEv.setOrigin("1");
                   
                    evList.add(logoutEv);
                }
            }
            }
           
        }
        
        // ---------- SIGNATURE ----------
        Map<String, Object> payload = new HashMap<>();
        payload.put("events", evList);
        byte[] data = mapper.writeValueAsBytes(payload);
        byte[] sigBytes = sign(data);

        String sigHex = toHex(sigBytes).replaceAll("[^0-9A-Fa-f]", "").toUpperCase();
        sigHex = enforceLength(sigHex, 16, 20);

        // ---------- HEADER ----------
        EldHeader h = new EldHeader();
        ELDSettings s = eldSettingsRepo.findAndViewBySettingId(1).stream().findFirst().orElse(null);

        String eldId = enforceLength(
                (s != null && s.getEldIdentifier() != null) ? s.getEldIdentifier().trim() : FALLBACK_ELDID,
                6, 6
        );

        h.setEldIdentifier(eldId);
        h.setEldProvider(s != null ? n(s.getEldProvider()) : "");
        h.setEldSoftwareVersion(s != null ? n(s.getEldSoftwareVersion()) : FALLBACK_SW);
        h.setOutputFileFormat("CSV");
        h.setFileSignature(sigHex);
        
        h.setFileGeneratedTime(nowUtc());
        if (seqCounter == null) {
            throw new IllegalStateException("seqCounter not initialized. VIN/vehicle missing.");
        }

        evList.sort(Comparator.comparing(e -> {
            try { return Instant.parse(n(e.getEventTime()).trim()); }
            catch (Exception ex) { return Instant.EPOCH; }
        }));

        for (EventRecord e : evList) {
            e.setEventSeqId(seqCounter.nextHex());   // now real seq ids will be set
        }

        rep.setEldHeader(h);
        rep.setDriver(driver);
        rep.setVehicle(veh);
        rep.setCarrier(car);
        rep.setEventRecords(evList);
        rep.setAnnotations(Collections.singletonList(ann));
        
        sDebug+=" Data :"+evList.toString();

        Map<String, Object> map = new HashMap<>();
        map.put("report", rep);
        map.put("signature", sigHex);
        map.put("events", evList); 
   
        return map;
        }
	
	public List<LoginLog> lookupLoginLogDataOperation(long from, long to, long driverId, String loginType) {

		MatchOperation filter = Aggregation.match(
				Criteria.where(loginType).gte(from).lte(to).and("employeeId").is(driverId).and("loginType").is("app"));

//		MatchOperation filter = Aggregation.match(
//	        new Criteria().and("employeeId").is(driverId)
//	                      .and("loginType").is("app")
//	                      .orOperator(
//	                          Criteria.where("loginDateTime").gte(from).lte(to),
//	                          Criteria.where("logoutDateTime").gte(from).lte(to)
//	                      )
//	    );

		ProjectionOperation projectStage = Aggregation.project("employeeId", "loginDateTime", "logoutDateTime",
				"receivedTimestamp", "isCoDriver", "_id");

		Aggregation aggregation = Aggregation.newAggregation(filter, projectStage);
		List<LoginLog> results = mongoTemplate.aggregate(aggregation, "login_log", LoginLog.class).getMappedResults();
		return results;

	}

	public List<CertifiedLogViewDto> lookupCertifiedLogDataOperation(long from, long to, long driverId) {

		MatchOperation filter = Aggregation
				.match(Criteria.where("lCertifiedDate").gte(from).lte(to).and("driverId").is(driverId));

		ProjectionOperation projectStage = Aggregation.project("driverId", "vehicleId", "trailers", "shippingDocs",
				"coDriverId", "certifiedSignature", "certifiedDate", "lCertifiedDate", "addedTimestamp", "_id",
				"certifiedAt");

		Aggregation aggregation = Aggregation.newAggregation(filter, projectStage);
		List<CertifiedLogViewDto> results = mongoTemplate
				.aggregate(aggregation, "certified_log", CertifiedLogViewDto.class).getMappedResults();
		return results;

	}

	// ===================== CSV BUILDER =====================
// yha se

	private String buildCsv(EldReportDto r, String sigHex, String eldRegistrationId, String comment) {

		

		String last = "", first = "";
		if (r.getDriver().getDriverName() != null && !r.getDriver().getDriverName().isEmpty()) {
			String[] parts = r.getDriver().getDriverName().trim().split("\\s+");
			first = parts[0].toUpperCase();
			if (parts.length > 1)
				last = parts[1].toUpperCase();
		}

		String driverIdStr = (r.getDriver().getDriverId() != null) ? r.getDriver().getDriverId().toString() : "0";
		String vin = n(r.getVehicle().getVin());

		StringBuilder sb = new StringBuilder();
		List<String> ldcvHexList = new ArrayList<>();

		BiConsumer<String, Boolean> appendLine = (line, addCheck) -> {
			String safe = trimCommentLikeFields(line);
			if (addCheck) {
				String chk = eldUtils.computeLineDataCheck(safe);
				ldcvHexList.add(chk);
				sb.append(safe).append(",").append(chk).append("\r");
			} else {
				sb.append(safe).append("\r");
			}
		};
		Function<EventRecord, String> seq = (ev) -> {
			String s = n(ev.getEventSeqId()).trim();
			return s.isBlank() ? "0" : s;
		};

		// ----- HEADER SEGMENT -----
		appendLine.accept("ELD File Header Segment:", false);
		String driverLine = String.join(",", last, first, "DUS" + driverIdStr, n(r.getDriver().getLicenseState()),
				n(r.getDriver().getLicenseNumber()));
		appendLine.accept(driverLine, true);

		appendLine.accept(",,,96", false);
		ldcvHexList.add("96");

		String trailer = n(r.getVehicle().getTrailerNo());
		if (trailer == null || trailer.isBlank())
			trailer = "NA";
		String unitNumber = n(r.getVehicle().getUnitNumber()).trim();
		String cmvLine = String.join(",", unitNumber, vin, trailer);
		appendLine.accept(cmvLine, true);

		String carrierLine = String.join(",", dotNumber(r.getCarrier().getUsdotNumber()),
				text(r.getCarrier().getCarrierName()), "7", "000000", "04");
		appendLine.accept(carrierLine, true);

		String shippingId = "NA";

		// Vehicle / report ke andar jo tumne buildAndSign() me set kiya hai
		if (r.getAnnotations() != null && !r.getAnnotations().isEmpty()
				&& r.getAnnotations().get(0).getShippingId() != null
				&& !r.getAnnotations().get(0).getShippingId().isBlank()) {

			shippingId = r.getAnnotations().get(0).getShippingId();
		}

		appendLine.accept(shippingId + ",0", true);

		List<EventRecord> events = r.getEventRecords();
		events = new ArrayList<>(events);
		events.sort(Comparator.comparing(ev -> {
			try {
				return Instant.parse(n(ev.getEventTime()).trim());
			} catch (Exception ex) {
				return Instant.EPOCH;
			}
		}));

		EventRecord firstEv = (events != null && !events.isEmpty()) ? events.get(0) : null;

		String timePlaceLine = String.join(",", date(firstEv), time(firstEv), lat(firstEv), lon(firstEv), "0", "0.0");
		appendLine.accept(timePlaceLine, true);

		// ----- ELD IDENTIFICATION LINE -----
		String eldIdLine = String.join(",", FALLBACK_ELD_REG_ID, n(r.getEldHeader().getEldIdentifier()), sigHex,
				"Test flag Y");
		String safeEldIdLine = trimCommentLikeFields(eldIdLine);
		String eldIdCheck = eldUtils.computeLineDataCheck(safeEldIdLine);
		ldcvHexList.add(eldIdCheck);
		sb.append(safeEldIdLine).append(",").append(eldIdCheck).append("\r");

		// ----- USER LIST -----
		appendLine.accept("User List:", false);
		String userLine = String.join(",", "1", "D", last, first);
		appendLine.accept(userLine, true);

		// ----- CMV LIST -----
		appendLine.accept("CMV List:", false);
		String cmvListLine = String.join(",", "1", n(r.getVehicle().getUnitNumber()), vin);
		appendLine.accept(cmvListLine, true);

		// ----- ELD EVENT LIST -----
		appendLine.accept("ELD Event List:", false);

		// or device serial / unit number

//        String eldKey = vin; // ya unitNumber/deviceSerial
//        Path seqPath = Paths.get(
//                System.getProperty("user.dir"),
//                "eld_state",
//                "eld_" + eldKey + "_seq.txt"
//        );
//
//        
//        Map<Long, String> seqMap = new HashMap<>();
//
//        if (events != null && !events.isEmpty()) {
//            List<EventRecord> sorted = new ArrayList<>(events);
//
//            sorted.sort(Comparator.comparing(e -> {
//                try { return Instant.parse(n(e.getEventTime()).trim()); }
//                catch (Exception ex) { return Instant.EPOCH; }
//            }));
//
//            for (EventRecord e : sorted) {
//                Long key = e.getStatusId();  // agar unique id hai to use karo
//                if (key == null || seqMap.containsKey(key)) continue;
//                seqMap.put(key, seqHexGen.nextHex());
//            }
//        }

		String lastMilesAcc = null; // already formatted whole miles
		String lastEngAcc = null; // already formatted 0.1 hours

		if (events != null && !events.isEmpty()) {
			for (EventRecord ev : events) {

				String eventType = n(ev.getEventType()).trim();

				// Event List me sirf 1,2,3 (duty, intermediate, special)
				if (!"1".equals(eventType) && !"2".equals(eventType) && !"3".equals(eventType)) {
					continue;
				}
//                String seq = seqMap.getOrDefault(ev.getStatusId(), "0");

				String eventCode = n(ev.getEventCode()).trim();
				String descUpper = n(ev.getEventDescription()).trim().toUpperCase();

				if ("1".equals(eventType)) {
					if (eventCode.isEmpty() || !eventCode.matches("[1-4]")) {
						eventCode = dutyCode(ev.getEventDescription());
					}
				} else if ("2".equals(eventType)) {
					if (eventCode.isEmpty())
						eventCode = "1";
				} 
				else if ("3".equals(eventType)) {
					if (eventCode.isEmpty())
						eventCode = "1";
				}

				// ✅ IMPORTANT: ELD Event List = ACCUMULATED values
				String milesAcc = milesWhole(ev.getOdometer()); // 0..9999 (whole miles)
				String engAcc = hours01(ev.getEngineHours()); // 0.0..999.9 (keep decimal)

				// Safety defaults
				if (milesAcc == null || milesAcc.isBlank())
					milesAcc = "0";
				if (engAcc == null || engAcc.isBlank())
					engAcc = "0.0";

				String location = (n(ev.getLocation()).isBlank()) ? "0,0" : ev.getLocation();
				String latStr = safeLat(location);
				String lonStr = safeLon(location);

				String originCodeCsv = n(ev.getOrigin()).trim();
				if (originCodeCsv.isEmpty() || !originCodeCsv.matches("[1-4]"))
					originCodeCsv = "1";
				String seqVal = seq.apply(ev);
				String eventLine = String.join(",", seqVal, "1", // Record Status
						originCodeCsv, // Origin
						eventType, eventCode, date(ev), time(ev), milesAcc, engAcc, latStr, lonStr, "0", "1", "1", "0",
						"0", "3F");

				appendLine.accept(eventLine, true);
			}
		}

		// ----- FOOTER SECTIONS -----
		appendLine.accept("ELD Event Annotations or Comments:", false);

		appendLine.accept("Driver's Certification/Recertification Actions:", false);

		// must be integer (NOT VIN)

		DateTimeFormatter d = DateTimeFormatter.ofPattern("MMddyy").withZone(ZoneOffset.UTC);
		DateTimeFormatter t1 = DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneOffset.UTC);

		for (EventRecord e : events) {

			if (!"4".equals(n(e.getEventType()).trim()))
				continue;

			String code = n(e.getEventCode()).trim();
			if (!"1".equals(code) && !"2".equals(code))
				continue; // only 1 or 2 allowed here

			String et = n(e.getEventTime()).trim();
			if (et.isBlank())
				continue;

			Instant ins;
			try {
				ins = Instant.parse(et);
			} catch (Exception ex) {
				continue;
			}
//            String seq1 = n(e.getEventSeqId());
//            if (seq.isBlank()) seq = "0"; // safety

			String eventDate = d.format(ins); // MMddyy
			String eventTime = t1.format(ins); // HHmmss

			// Date of Certified Records (most cases same as eventDate)
			String dateOfCertifiedRecords = eventDate;
			String seqVal = seq.apply(e);
			// Build first 6 fields (without LDCV)
			String base = String.join(",", seqVal, code, // Event Code (1/2)
					eventDate, // Event Date (MMddyy)
					eventTime, // Event Time (HHmmss)
					dateOfCertifiedRecords, "1"// Date of Certified Records (MMddyy)
												// CMV Order Number (integer)
			);

			// IMPORTANT: your LDCV must be calculated on "base"

			appendLine.accept(base, true);
		}

		appendLine.accept("Malfunctions and Data Diagnostic Events:", false);
//
//     // Most recent first
//        if (events != null && !events.isEmpty()) {
//
//     for (EventRecord ev :events) {
//    	 if (!"7".equals(ev.getEventType())) continue; 
//
//       
//
//         String seq1 = String.format("%04d", ev.getStatusId());
//
//         Instant ins = Instant.parse(ev.getEventTime());
//         String date = ins.atOffset(ZoneOffset.UTC)
//                 .format(DateTimeFormatter.ofPattern("MMddyy"));
//         String time = ins.atOffset(ZoneOffset.UTC)
//                 .format(DateTimeFormatter.ofPattern("HHmmss"));
//
//         String miles = n(ev.getTotalVehicleMiles()).isBlank()
//                 ? "0" : n(ev.getTotalVehicleMiles());
//         String hours = n(ev.getTotalEngineHours()).isBlank()
//                 ? "0.0" : n(ev.getTotalEngineHours());
//         long seqNo = seqGen.pick(ev.getStatusId());
//         String seq = seqGen.fmt(seqNo);
//
//         String base = String.join(",",
//        		 seq,
//                 ev.getEventCode(),          // 1 = Malfunction, 2 = Diagnostic
//                 ev.getEventDescription(),   // M1..M6 or D1..D5
//                 date,
//                 time,
//                 miles,
//                 hours,
//                 "1"
//         );
//     
//
//         appendLine.accept(base, true);
//     }
//        }
//        

		appendLine.accept("ELD Login/Logout Report:", false);

		if (events != null && !events.isEmpty()) {

//         String seq = String.format("%04d", e.getStatusId());

			String eldUser = "DUS" + driverIdStr;

			for (EventRecord e : events) {
				if (!"5".equals(e.getEventType()))
					continue;
				//// Sirf Type 5 (Login/Logout)

//                String seq = n(e.getEventSeqId());
//                if (seq.isBlank()) seq = "0"; // safety

				// TVM aur TEH values ko correctly zero-pad karein
				String tvm = digitsOnlyOrZero(e.getTotalVehicleMiles());
				String teh = digitsOnlyOrZero(e.getTotalEngineHours());
				String seqVal = seq.apply(e);
				String line = String.join(",", seqVal, // Corrected sequence
						e.getEventCode(), eldUser, date(e), time(e), tvm, teh);
				appendLine.accept(line, true);
			}

		}

		// ---- ENGINE POWER-UP / SHUT-DOWN ----
		appendLine.accept("CMV Engine Power-Up and Shut Down Activity:", false);

		if (events != null && !events.isEmpty()) {

			for (EventRecord e : events) {

				String t = n(e.getEventType()).trim();
				String c = n(e.getEventCode()).trim();

				if (!"6".equals(t))
					continue; // only engine power events

				String lineType;
				if ("1".equals(c))
					lineType = "1"; // power-up
				else if ("3".equals(c))
					lineType = "3"; // ✅ shut-down
				else
					continue;

//                String seq = n(e.getEventSeqId());
//                if (seq.isBlank()) seq = "0"; // safety

				String loc = (n(e.getLocation()).isBlank()) ? "0,0" : e.getLocation();

				// TOTAL values here
				long milesInt = (long) Math.floor(parseDouble(e.getTotalVehicleMiles()) + 0.00001);
				double engVal = parseDouble(e.getTotalEngineHours());
				engVal = Math.round(engVal * 10.0) / 10.0;

				String miles = String.valueOf(milesInt);
				String eng = String.format(Locale.US, "%.1f", engVal);
				String powerUnit = n(r.getVehicle().getUnitNumber()).trim();

				if (powerUnit.isBlank()) {
					throw new IllegalStateException(
							"CMV Power Unit Number missing for vehicleId=" + r.getVehicle().getVin());
				}
				String seqVal = seq.apply(e);

				String line = String.join(",", seqVal, lineType, date(e), time(e), miles, eng, safeLat(loc),
						safeLon(loc), powerUnit, // ✅ NEVER blank
						vin, trailer, shippingId);

				appendLine.accept(line, true);

			}
		}

		appendLine.accept("Unidentified Driver Profile Records:", false);
		appendLine.accept("End of File:", false);

		String fdcv = eldUtils.computeFileDataCheck(ldcvHexList);
		sb.append(fdcv).append("\r");

		return sb.toString();
	}

	// ===================== DB QUERY =====================

	private List<DriveringStatusViewDto> getRows(long from, long to, long driverId) {
		MatchOperation f = Aggregation
				.match(Criteria.where("utcDateTime").gte(from).lte(to).and("driverId").is(driverId)
				// .and("isVisible").is(1) // <-- is line ko hata do
				);

		ProjectionOperation p = Aggregation.project("driverId", "status", "customLocation", "engineHour", "odometer",
				"vehicleId", "clientId", "utcDateTime", "lattitude", "longitude", "origin", "_id");
//                .andExclude("_id");

		Aggregation agg = Aggregation.newAggregation(f, p, sort(Direction.ASC, "utcDateTime"));
		return mongoTemplate.aggregate(agg, "drivering_status", DriveringStatusViewDto.class).getMappedResults();
	}

	// ===================== HELPERS =====================
	private byte[] normalizeCrOnly(byte[] in) {
	    if (in == null || in.length == 0) return in;

	    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(in.length);
	    byte prev = 0;

	    for (byte b : in) {
	        // drop LF always
	        if (b == (byte) 0x0A) continue;

	        // keep CR
	        if (b == (byte) 0x0D) {
	            out.write(b);
	            prev = b;
	            continue;
	        }

	        // (optional) if any stray newline-like scenario occurs, keep as-is
	        out.write(b);
	        prev = b;
	    }
	    return out.toByteArray();
	}

	private String milesWhole(String s) {
		double v = parseDouble(s);
		long m = (long) Math.floor(v + 0.00001);
		return String.valueOf(m);
	}

	private String hours01(String s) {
		double v = parseDouble(s);
		v = Math.round(v * 10.0) / 10.0;
		return String.format(Locale.US, "%.1f", v);
	}

	private String formatDecimal(double v) {
		// two decimals, using US dot as separator
		return String.format(Locale.US, "%.2f", v);
	}

	private double parseDouble(String s) {
		if (s == null)
			return 0.0;
		String t = s.trim();
		if (t.isEmpty())
			return 0.0;
		try {
			return Double.parseDouble(t);
		} catch (Exception e) {
			return 0.0;
		}
	}

	// 0 = none, 1 = PC, 2 = YM (Table 2 – special driving categories)
	private int specialDrivingCategory(String status) {
		if (status == null)
			return 0;
		String s = status.trim().toUpperCase();

		if (s.contains("PERSONAL") || s.equals("PC") || s.equals("PU") || s.contains("PC/")
				|| s.contains("PERSONAL USE")) {
			return 1; // Authorized Personal Use of CMV (PC)
		}

		if (s.contains("YARD") || s.equals("YM") || s.contains("YARD MOVE")) {
			return 2; // Yard Moves (YM)
		}

		return 0; // none
	}

	private String digitsOnlyOrZero(String s) {
		if (s == null)
			return "0";
		String x = s.trim();
		if (x.isEmpty())
			return "0";
		int dot = x.indexOf('.');
		if (dot >= 0)
			x = x.substring(0, dot);
		x = x.replaceAll("[^0-9]", "");
		return x.isEmpty() ? "0" : x;
	}

	private String to9999(String v) {
		long x = 0;
		try {
			x = Long.parseLong(digitsOnlyOrZero(v));
		} catch (Exception ignored) {
		}
		long m = x % 10000;
		if (m < 0)
			m += 10000;
		return String.valueOf(m);
	}

	private String to999(String v) {
		long x = 0;
		try {
			x = Long.parseLong(digitsOnlyOrZero(v));
		} catch (Exception ignored) {
		}
		long m = x % 1000;
		if (m < 0)
			m += 1000;
		return String.valueOf(m);
	}

	private String safeLat(String loc) {
		try {
			String[] parts = loc.split(",", -1);
			String lat = parts[0].trim();
			if (lat.isEmpty())
				return "0";
			return lat;
		} catch (Exception e) {
			return "0";
		}
	}

	private String safeLon(String loc) {
		try {
			String[] parts = loc.split(",", -1);
			if (parts.length < 2)
				return "0";
			String lon = parts[1].trim();
			if (lon.isEmpty())
				return "0";
			return lon;
		} catch (Exception e) {
			return "0";
		}
	}

	private String n(String s) {
		return s == null ? "" : s;
	}

	private String nowUtc() {
		return Instant.now().atOffset(ZoneOffset.UTC).toString();
	}

	private String safeState(String s) {
	    if (s == null) return "";

	    String val = s.trim().toUpperCase();

	    // Remove brackets if present like "Texas (TX)"
	    if (val.contains("(") && val.contains(")")) {
	        int start = val.indexOf("(");
	        int end = val.indexOf(")");
	        if (end > start) {
	            String inside = val.substring(start + 1, end).trim();
	            if (inside.length() == 2) {
	                return inside.toUpperCase();
	            }
	        }
	    }

	    // Already 2-letter
	    if (val.length() == 2 && val.matches("[A-Z]{2}")) {
	        return val;
	    }

	    // Normalize multiple spaces
	    val = val.replaceAll("\\s+", " ");

	    if (STATE_CODE_MAP.containsKey(val)) {
	        return STATE_CODE_MAP.get(val);
	    }

	    return "";
	}


	private String date(EventRecord e) {
		if (e == null || e.getEventTime() == null)
			return "";
		try {
			Instant instant = Instant.parse(e.getEventTime());
			LocalDate date = instant.atOffset(ZoneOffset.UTC).toLocalDate();
			return date.format(DateTimeFormatter.ofPattern("MMddyy"));
		} catch (Exception ex) {
			return "";
		}
	}

	private String time(EventRecord e) {
		if (e == null || e.getEventTime() == null)
			return "";
		try {
			Instant instant = Instant.parse(e.getEventTime());
			return DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneOffset.UTC).format(instant);
		} catch (Exception ex) {
			if (e != null && e.getEventTime().length() >= 19)
				return e.getEventTime().substring(11, 19).replace(":", "");
			return "";
		}
	}

	private String dutyCode(String status) {
		if (status == null)
			return "1";

		String s = status.trim().toUpperCase().replace("-", "").replace(" ", "");

		if (s.equals("OFFDUTY") || s.equals("OFF") || s.equals("PERSONALUSE") || s.equals("PC") || s.equals("Pc"))
			return "1";
		if (s.equals("ONSLEEP") || s.equals("SLEEPER") || s.equals("SB") || s.equals("OS"))
			return "2";
		if (s.equals("ONDRIVE") || s.equals("DRIVING") || s.equals("DR") || s.equals("ODR"))
			return "3";
		if (s.equals("ONDUTY") || s.equals("ON") || s.equals("OD") || s.contains("YARDMOVE") || s.equals("YM"))
			return "4";

		return "1";
	}

	private String dotNumber(Object value) {
		if (value == null)
			return "0";

		if (value instanceof Number) {
			long n = ((Number) value).longValue();
			return String.valueOf(Math.max(n, 0));
		}

		String s = String.valueOf(value).trim();
		if (s.isEmpty())
			return "0";

		int dot = s.indexOf('.');
		if (dot >= 0)
			s = s.substring(0, dot);

		s = s.replaceAll("[^0-9]", "");
		return s.isEmpty() ? "0" : s;
	}

	private String text(Object value) {
		return value == null ? "" : String.valueOf(value).trim();
	}

	private String lat(EventRecord e) {
		if (e == null)
			return "0";
		return safeLat(n(e.getLocation()).isBlank() ? "0,0" : e.getLocation());
	}

	private String lon(EventRecord e) {
		if (e == null)
			return "0";
		return safeLon(n(e.getLocation()).isBlank() ? "0,0" : e.getLocation());
	}

	private String toHex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (byte x : b)
			sb.append(String.format("%02X", x));
		return sb.toString();
	}

	private String enforceLength(String s, int min, int max) {
		if (s == null)
			s = "";
		if (min > 0 && s.length() < min) {
			StringBuilder sb = new StringBuilder(s);
			while (sb.length() < min)
				sb.append('X');
			s = sb.toString();
		}
		if (max > 0 && s.length() > max)
			s = s.substring(0, max);
		return s;
	}

	private String trimCommentLikeFields(String line) {
		String[] parts = line.split(",", -1);
		boolean changed = false;
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] != null && parts[i].length() > 60) {
				parts[i] = parts[i].substring(0, 60);
				changed = true;
			}
		}
		if (!changed)
			return line;
		return String.join(",", parts);
	}

	private byte[] sign(byte[] d) throws Exception {
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initSign(privateKey);
		sig.update(d);
		byte[] s = sig.sign();
		if (s == null || s.length < 8) {
			throw new RuntimeException("Invalid RSA signature. Private key corrupted or wrong format.");
		}
		return s;
	}

	private String fixVin(String vin) {
		if (vin == null)
			return FALLBACK_VIN;
		vin = vin.trim();
		if (vin.length() != 17)
			return vin;

		Map<Character, Integer> map = new HashMap<>();
		for (char c = '0'; c <= '9'; c++)
			map.put(c, c - '0');
		map.put('A', 1);
		map.put('B', 2);
		map.put('C', 3);
		map.put('D', 4);
		map.put('E', 5);
		map.put('F', 6);
		map.put('G', 7);
		map.put('H', 8);
		map.put('J', 1);
		map.put('K', 2);
		map.put('L', 3);
		map.put('M', 4);
		map.put('N', 5);
		map.put('P', 7);
		map.put('R', 9);
		map.put('S', 2);
		map.put('T', 3);
		map.put('U', 4);
		map.put('V', 5);
		map.put('W', 6);
		map.put('X', 7);
		map.put('Y', 8);
		map.put('Z', 9);

		int[] weights = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2 };
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			char c = Character.toUpperCase(vin.charAt(i));
			int val = map.getOrDefault(c, 0);
			sum += val * weights[i];
		}
		int remainder = sum % 11;
		char check = (remainder == 10 ? 'X' : (char) ('0' + remainder));
		return vin.substring(0, 8) + check + vin.substring(9);
	}

	private void sendMail(String to, File file, String body, String attachmentName)
	        throws javax.mail.MessagingException, IOException {

	    if (attachmentName == null || attachmentName.isBlank()) {
	        attachmentName = "eld_output.csv";
	    }
	    if (!attachmentName.toLowerCase().endsWith(".csv")) {
	        attachmentName += ".csv";
	    }

	    // Read bytes exactly
	    byte[] bytes = Files.readAllBytes(file.toPath());

	    // HARD guarantee again (important because S/MIME/JavaMail can normalize text parts)
	    bytes = normalizeCrOnly(bytes);

	    MimeMessage msg = javaMailSender.createMimeMessage();
	    msg.setRecipients(Message.RecipientType.TO, to);
	    msg.setSubject("ELD CSV Report");

	    MimeMultipart multipart = new MimeMultipart();

	    // Body
	    MimeBodyPart textPart = new MimeBodyPart();
	    textPart.setText(body == null ? "" : body, "UTF-8");
	    multipart.addBodyPart(textPart);

	    // Attachment as binary
	    MimeBodyPart attachPart = new MimeBodyPart();
	    ByteArrayDataSource ds = new ByteArrayDataSource(bytes, "application/octet-stream");
	    attachPart.setDataHandler(new DataHandler(ds));
	    attachPart.setFileName(attachmentName);

	    // Force base64 (prevents newline conversion)
	    attachPart.setDisposition(MimeBodyPart.ATTACHMENT);
	    attachPart.setHeader("Content-Transfer-Encoding", "base64");
	    attachPart.setHeader("Content-Type", "application/octet-stream; name=\"" + attachmentName + "\"");

	    multipart.addBodyPart(attachPart);

	    msg.setContent(multipart);
	    javaMailSender.send(msg);
	}


	private boolean verifySignature(byte[] data, String signatureBase64, PublicKey publicKey) throws Exception {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(Base64.getDecoder().decode(signatureBase64));
	}

	private PrivateKey loadPrivateKey(String f) throws Exception {
		ClassPathResource r = new ClassPathResource("pem_file/" + f);
		String pem = new String(r.getInputStream().readAllBytes()).replaceAll("-----BEGIN (.*)-----", "")
				.replaceAll("-----END (.*)-----", "").replaceAll("\\s", "");
		return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pem)));
	}

	private PublicKey loadPublicKey(String f) throws Exception {
		ClassPathResource r = new ClassPathResource("pem_file/" + f);
		String pem = new String(r.getInputStream().readAllBytes()).replaceAll("-----BEGIN (.*)-----", "")
				.replaceAll("-----END (.*)-----", "").replaceAll("\\s", "");
		return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pem)));
	}

}
