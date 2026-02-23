package com.mes.eld_log.serviceImpl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.time.*; 

import static com.mes.eld_log.security.Constants.WEB_URL_BASE_PATH;
import static com.mes.eld_log.security.Constants.WEB_URL_FILE_UPLOAD;

import java.awt.Color;
//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONObject;
import org.modelmapper.internal.bytebuddy.asm.Advice.Exit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

//import com.lowagie.text.Chunk;
//import com.lowagie.text.Document;
//import com.lowagie.text.Element;
//import com.lowagie.text.Font;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.Phrase;
//import com.lowagie.text.Rectangle;
//import com.lowagie.text.pdf.BaseFont;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;

import com.mes.eld_log.dtos.AddCertifiedLogDto;
import com.mes.eld_log.dtos.AddDriveringStatusDto;
import com.mes.eld_log.dtos.AddDriveringStatusResponseDto;
import com.mes.eld_log.dtos.AddDvirDataDto;
import com.mes.eld_log.dtos.AlertsLogViewDto;
import com.mes.eld_log.dtos.AllDispatchDataViewDto;
import com.mes.eld_log.dtos.AssignLogToDriverDto;
import com.mes.eld_log.dtos.CertifiedLogCRUDDto;
import com.mes.eld_log.dtos.CertifiedLogViewDto;
import com.mes.eld_log.dtos.ClientMasterCRUDDto;
import com.mes.eld_log.dtos.CycleTimeRulesViewDto;
import com.mes.eld_log.dtos.DVIRDataCRUDDto;
import com.mes.eld_log.dtos.DefectDetailCRUDDto;
import com.mes.eld_log.dtos.DispatchDataViewDto;
import com.mes.eld_log.dtos.DispatchDetailCRUDDto;
import com.mes.eld_log.dtos.DispatchDetailViewDto;
import com.mes.eld_log.dtos.DriverLogWithLoginLogViewDto;
import com.mes.eld_log.dtos.DriverWorkingStatusViewDto;
import com.mes.eld_log.dtos.DriveringStatusCRUDDto;
import com.mes.eld_log.dtos.DriveringStatusLogViewDto;
import com.mes.eld_log.dtos.DriveringStatusViewDto;
import com.mes.eld_log.dtos.ELDOtaCRUDDto;
import com.mes.eld_log.dtos.ELDSupportViewDto;
import com.mes.eld_log.dtos.EldLogDataViewDto;
import com.mes.eld_log.dtos.EldReportDto;
import com.mes.eld_log.dtos.EldReportDto.Annotation;
import com.mes.eld_log.dtos.EldReportDto.Carrier;
import com.mes.eld_log.dtos.EldReportDto.Driver;
import com.mes.eld_log.dtos.EldReportDto.EldHeader;
import com.mes.eld_log.dtos.EldReportDto.EventRecord;
import com.mes.eld_log.dtos.EldReportDto.Vehicle;
import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.EmployeeMasterViewDto;
import com.mes.eld_log.dtos.IdlingReportViewDto;
import com.mes.eld_log.dtos.IftaReportViewDto;
import com.mes.eld_log.dtos.IftaSummaryReport;
import com.mes.eld_log.dtos.LiveDataLogViewDto;
import com.mes.eld_log.dtos.LoginLogViewDto;
import com.mes.eld_log.dtos.MaxIdViewDto;
import com.mes.eld_log.dtos.OtherChargeViewDto;
import com.mes.eld_log.dtos.ReceiverDataViewDto;
import com.mes.eld_log.dtos.ShiftLogAddDto;
import com.mes.eld_log.dtos.ShipperDataViewDto;
import com.mes.eld_log.dtos.TrailerMasterCRUDDto;
import com.mes.eld_log.dtos.UserLoginDto;
import com.mes.eld_log.dtos.ViewDriverLogWithDetailDto;
import com.mes.eld_log.dtos.ViewDriverWorkingDayStatus;
import com.mes.eld_log.models.AlertsLog;
import com.mes.eld_log.models.CargoTypeMaster;
import com.mes.eld_log.models.CertifiedLog;
import com.mes.eld_log.models.CityMaster;
import com.mes.eld_log.models.ClientMaster;
import com.mes.eld_log.models.CompanyMaster;
import com.mes.eld_log.models.CountryMaster;
import com.mes.eld_log.models.CustomerMaster;
import com.mes.eld_log.models.CycleUsa;
import com.mes.eld_log.models.DVIRData;
import com.mes.eld_log.models.DefectDetails;
import com.mes.eld_log.models.DeviceStatus;
import com.mes.eld_log.models.DispatchDetails;
import com.mes.eld_log.models.DriverStatusLog;
import com.mes.eld_log.models.DriverWorkingStatus;
import com.mes.eld_log.models.DriveringStatus;
import com.mes.eld_log.models.ELDLogData;
import com.mes.eld_log.models.ELDOta;
import com.mes.eld_log.models.ELDOtaStatus;
import com.mes.eld_log.models.ELDSettings;
import com.mes.eld_log.models.ELDSupport;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.ExceptionLog;
import com.mes.eld_log.models.GeofanceMaster;
import com.mes.eld_log.models.IFTAReports;
import com.mes.eld_log.models.IdlingReport;
import com.mes.eld_log.models.LiveDataLog;
import com.mes.eld_log.models.Login;
import com.mes.eld_log.models.LoginLog;
import com.mes.eld_log.models.MACAddressMaster;
import com.mes.eld_log.models.MainTerminalMaster;
import com.mes.eld_log.models.OtherCharges;
import com.mes.eld_log.models.ReceiverMaster;
import com.mes.eld_log.models.ReferModeMaster;
import com.mes.eld_log.models.ShipperMaster;
import com.mes.eld_log.models.ShipperReceiverDetails;
import com.mes.eld_log.models.SplitLog;
import com.mes.eld_log.models.StateMaster;
import com.mes.eld_log.models.TrailerMaster;
import com.mes.eld_log.models.UserMaster;
import com.mes.eld_log.models.VehicleMaster;
import com.mes.eld_log.repo.AlertsLogRepo;
import com.mes.eld_log.repo.CertifiedLogRepo;
import com.mes.eld_log.repo.CityMasterRepo;
import com.mes.eld_log.repo.ClientMasterRepo;
import com.mes.eld_log.repo.CountryMasterRepo;
import com.mes.eld_log.repo.CustomerMasterRepo;
import com.mes.eld_log.repo.CycleUsaRepo;
import com.mes.eld_log.repo.DVIRDataRepo;
import com.mes.eld_log.repo.DefectDetailsRepo;
import com.mes.eld_log.repo.DeviceStatusRepo;
import com.mes.eld_log.repo.DispatchDetailsRepo;
import com.mes.eld_log.repo.DriverStatusLogRepo;
import com.mes.eld_log.repo.DriverWorkingStatusRepo;
import com.mes.eld_log.repo.DriveringStatusRepo;
import com.mes.eld_log.repo.ELDLogDataRepo;
import com.mes.eld_log.repo.ELDOtaRepo;
import com.mes.eld_log.repo.ELDOtaStatusRepo;
import com.mes.eld_log.repo.ELDSettingsRepo;
import com.mes.eld_log.repo.ELDSupportRepo;
import com.mes.eld_log.repo.EmployeeMasterRepo;
import com.mes.eld_log.repo.ExceptionLogRepo;
import com.mes.eld_log.repo.GeofanceMasterRepo;
import com.mes.eld_log.repo.IFTAReportsRepo;
import com.mes.eld_log.repo.LiveDataLogRepo;
import com.mes.eld_log.repo.LoginLogRepo;
import com.mes.eld_log.repo.LoginRepo;
import com.mes.eld_log.repo.MACAddressMasterRepo;
import com.mes.eld_log.repo.MainTerminalMasterRepo;
import com.mes.eld_log.repo.OtherChargesRepo;
import com.mes.eld_log.repo.ReceiverMasterRepo;
import com.mes.eld_log.repo.ReferModeMasterRepo;
import com.mes.eld_log.repo.ShipperMasterRepo;
import com.mes.eld_log.repo.ShipperReceiverDetailsRepo;
import com.mes.eld_log.repo.SplitLogRepo;
import com.mes.eld_log.repo.StateMasterRepo;
import com.mes.eld_log.repo.UserMasterRepo;
import com.mes.eld_log.repo.VehicleMasterRepo;
import com.mes.eld_log.results.Result;
import com.mes.eld_log.results.ResultWrapper;
import com.mes.eld_log.service.DispatchService;
import com.mes.eld_log.util.eldLogUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

@Service(value = "dispatchService")
public class DispatchServiceImpl implements DispatchService{
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	private eldLogUtils eldUtils;
	
	@Autowired
	LoginRepo loginRepo;
	
	@Autowired
	ELDSettingsRepo eldSettingsRepo;
	
	@Autowired
	ELDSupportRepo eldSupportRepo;
	
	@Autowired
	AlertsLogRepo alertsLogRepo;
	
	@Autowired
	DefectDetailsRepo defectDetailsRepo;
	
	@Autowired
	IFTAReportsRepo iftaReportsRepo;
	
	@Autowired
	UserMasterRepo userMasterRepo;
	
	@Autowired
	DriverStatusLogRepo driverStatusLogRepo;
	
	@Autowired
	SplitLogRepo splitLogRepo;
	
	@Autowired
	LiveDataLogRepo liveDataLogRepo;
	
	@Autowired
	LoginLogRepo loginLogRepo;
	
	@Autowired
	GeofanceMasterRepo geofanceMasterRepo;
	
	@Autowired
	ELDLogDataRepo eldLogDataRepo;
	
	@Autowired
	DeviceStatusRepo deviceStatusRepo;
	
	@Autowired
	MACAddressMasterRepo macAddressMasterRepo;
	
	@Autowired
	DriverWorkingStatusRepo driverWorkingStatusRepo;
	
	@Autowired
	private DispatchServiceImpl dispatchServiceImpl;
	
	@Autowired
	private DVIRDataRepo dvirDataRepo;
	
	@Autowired
	private CertifiedLogRepo certifiedLogRepo;
	
	@Autowired
	private ELDOtaRepo eldOtaRepo;
	
	@Autowired
	private ELDOtaStatusRepo eldOtaStatusRepo;
	
	@Autowired
	ClientMasterRepo clientMasterRepo;

	@Autowired
	DispatchDetailsRepo dispatchDetailsRepo;
	
	@Autowired
	MainTerminalMasterRepo mainTerminalMasterRepo;
	
	@Autowired
	VehicleMasterRepo vehicleMasterRepo;
	
	@Autowired
	OtherChargesRepo otherChargesRepo;
	
	@Autowired
	CycleUsaRepo cycleUsaRepo;
	
	@Autowired
	ExceptionLogRepo exceptionLogRepo;
	
	@Autowired
	CustomerMasterRepo customerMasterRepo;
	
	@Autowired
	ShipperMasterRepo shipperMasterRepo;
	
	@Autowired
	ReceiverMasterRepo receiverMasterRepo;
	
	@Autowired
	private EmployeeMasterRepo employeeMasterRepo;
	
	@Autowired
	private CountryMasterRepo countryMasterRepo;
	
	@Autowired
	private StateMasterRepo stateMasterRepo;
	
	@Autowired
	private CityMasterRepo cityMasterRepo;
	
	@Autowired
	private ReferModeMasterRepo referModeMasterRepo;
	
	@Autowired
	ShipperReceiverDetailsRepo shipperReceiverDetailsRepo;
	
	@Autowired
	DriveringStatusRepo driveringStatusRepo;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	private Path fileStorageLocation;
	
	private MongoTemplate mongoTemplate;
	
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    
	@Autowired
    public DispatchServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        workbook = new XSSFWorkbook();
    }
	
	
	public ResultWrapper<String> AddDispatchDetails(DispatchDetailCRUDDto dispatchDetailCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			ArrayList<DispatchDetails> adddispatchData = dispatchDetailCRUDDto.getDispatchData();
			DispatchDetails dispatchDetails = null;
			for(int i=0;i<adddispatchData.size();i++) {
				dispatchDetails = adddispatchData.get(i);
				dispatchDetails.setAddedTimestamp(instant.toEpochMilli());
				dispatchDetails.setUpdatedTimestamp(instant.toEpochMilli());
				
				dispatchDetails = dispatchDetailsRepo.save(dispatchDetails);
			}
			
			DispatchDetailViewDto dispatchDetailViewDto = dispatchDetailsRepo.findDispatchDetailsByTimestamp(dispatchDetails.getAddedTimestamp());
			
			ArrayList<OtherCharges> addOtherChargeData = dispatchDetailCRUDDto.getOtherChargeData();
			OtherCharges otherCharges = null;
			for(int i=0;i<addOtherChargeData.size();i++) {
				otherCharges = addOtherChargeData.get(i);
				
				otherCharges.setDispatchId(dispatchDetailViewDto.get_id());
				otherCharges.setAddedTimestamp(instant.toEpochMilli());
				otherCharges.setUpdatedTimestamp(instant.toEpochMilli());
				
				otherCharges = otherChargesRepo.save(otherCharges);
			}
			
			ArrayList<ShipperReceiverDetails> addShipperData = dispatchDetailCRUDDto.getShipperData();
			ShipperReceiverDetails shipperDetails = null;
			for(int i=0;i<addShipperData.size();i++) {
				shipperDetails = addShipperData.get(i);
				
				shipperDetails.setDispatchId(dispatchDetailViewDto.get_id());
				shipperDetails.setAddedTimestamp(instant.toEpochMilli());
				shipperDetails.setUpdatedTimestamp(instant.toEpochMilli());
				
				shipperDetails = shipperReceiverDetailsRepo.save(shipperDetails);
			}
			
			ArrayList<ShipperReceiverDetails> addReceiverData = dispatchDetailCRUDDto.getReceiverData();
			ShipperReceiverDetails receiverDetails = null;
			for(int i=0;i<addReceiverData.size();i++) {
				receiverDetails = addReceiverData.get(i);
				
				receiverDetails.setDispatchId(dispatchDetailViewDto.get_id());
				receiverDetails.setAddedTimestamp(instant.toEpochMilli());
				receiverDetails.setUpdatedTimestamp(instant.toEpochMilli());
				
				receiverDetails = shipperReceiverDetailsRepo.save(receiverDetails);
			}
				
			result.setResult("Saved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Dispatch Details Added Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<DispatchDetailViewDto>> ViewDispatchDetails(DispatchDetailCRUDDto dispatchDetailCRUDDto) {
		
		ResultWrapper<List<DispatchDetailViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			List<DispatchDetailViewDto> dispatchDetailViewDto = null;
			String dispatchId = dispatchDetailCRUDDto.getDispatchId();
			long clientId = dispatchDetailCRUDDto.getClientId();
			if(dispatchId.equals("")) {
				sDebug+="1,";
				dispatchDetailViewDto = dispatchDetailsRepo.findAllDispatchDetails(clientId);
				sDebug+="2,";
				try {
					for(int i=0;i<dispatchDetailViewDto.size();i++) {
						CustomerMaster customerInfo = customerMasterRepo.findByCustomerId((int)dispatchDetailViewDto.get(i).getCustomerId());
						dispatchDetailViewDto.get(i).setCustomerName(customerInfo.getCustomerName());
						sDebug+="3,";
						List<ShipperReceiverDetails> srDetails = shipperReceiverDetailsRepo.findByDispatchSRId(dispatchDetailViewDto.get(i).get_id());
						sDebug+="4,";
						if(srDetails.size()>0) {
							sDebug+="5,";
							for(int sr=0;sr<srDetails.size();sr++) {
								sDebug+="6,";
								if(srDetails.get(sr).getShipperId()>0) {
									ShipperMaster shipperInfo = shipperMasterRepo.findByShipperId((int)srDetails.get(sr).getShipperId());
									dispatchDetailViewDto.get(i).setShipperId(shipperInfo.getShipperId());
									dispatchDetailViewDto.get(i).setShipperName(shipperInfo.getShipperName());
									dispatchDetailViewDto.get(i).setShippingDate(srDetails.get(sr).getDate());
								}
								if(srDetails.get(sr).getReceiverId()>0) {
									ReceiverMaster receiverInfo = receiverMasterRepo.findByReceiverId((int)srDetails.get(sr).getReceiverId());
									dispatchDetailViewDto.get(i).setReceiverId(receiverInfo.getReceiverId());
									dispatchDetailViewDto.get(i).setReceiverName(receiverInfo.getReceiverName());
									dispatchDetailViewDto.get(i).setReceivingDate(srDetails.get(sr).getDate());
								}
							}
							sDebug+="7,";
						}
						sDebug+="8,";
					}
					sDebug+="9,";
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}else {
				dispatchDetailViewDto = dispatchDetailsRepo.findAndViewDispatchDetailsById(dispatchId,clientId);
			}

			result.setResult(dispatchDetailViewDto);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Dispatch Detail Information Send Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<AllDispatchDataViewDto> ViewDispatchData(DispatchDetailCRUDDto dispatchDetailCRUDDto) {
		
		ResultWrapper<AllDispatchDataViewDto> result = new ResultWrapper<>();
		try {
			AllDispatchDataViewDto allDispatchDataViewDto = new AllDispatchDataViewDto();
			
			long driverId = dispatchDetailCRUDDto.getDriverId();
			String dispatchId="";
			if(driverId>0) {
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query query = new Query(
				  Criteria.where("driverId").is(driverId)
				);
				query.limit(1);
				query.with(pageableRequest);
				List<DispatchDataViewDto> dispatchData = mongoTemplate.find(query, DispatchDataViewDto.class,"dispatch_details");
				for(int i=0;i<dispatchData.size();i++) {
					dispatchId = dispatchData.get(i).get_id();
					CustomerMaster customerInfo = customerMasterRepo.findByCustomerId((int)dispatchData.get(i).getCustomerId());
					dispatchData.get(i).setCustomerName(customerInfo.getCustomerName());
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)dispatchData.get(i).getDriverId());
					dispatchData.get(i).setDriverName(empInfo.getFirstName()+" "+empInfo.getLastName());
					dispatchData.get(i).setDivR(empInfo.getDivR());
				}
				List<OtherChargeViewDto> otherChargesData=null;
				List<ShipperDataViewDto> shipperData=null;
				List<ReceiverDataViewDto> receiverData=null;
				if(!dispatchId.equals("")) {
					otherChargesData = otherChargesRepo.findAndViewByDispatchId(dispatchId);
					
					List<ShipperReceiverDetails> srDetails = shipperReceiverDetailsRepo.findByDispatchSRId(dispatchId);
					if(srDetails.size()>0) {
						for(int sr=0;sr<srDetails.size();sr++) {
							if(srDetails.get(sr).getShipperId()>0) {
								shipperData = shipperReceiverDetailsRepo.findByDispatchIdOfShipperData(dispatchId,srDetails.get(sr).getShipperId());
								if(shipperData.size()>0) {
									for(int i=0;i<shipperData.size();i++) {
										ShipperMaster shipperInfo = shipperMasterRepo.findByShipperId((int)shipperData.get(i).getShipperId());
										shipperData.get(i).setShipperName(shipperInfo.getShipperName());
										CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)shipperData.get(i).getCountryId());
										shipperData.get(i).setCountryName(countryInfo.getCountryName());
										StateMaster stateInfo = stateMasterRepo.findByStateId((int)shipperData.get(i).getStateId());
										shipperData.get(i).setStateName(stateInfo.getStateName());
										CityMaster cityInfo = cityMasterRepo.findByCityId((int)shipperData.get(i).getCityId());
										shipperData.get(i).setCityName(cityInfo.getCityName());
										ReferModeMaster referModeInfo = referModeMasterRepo.findByReferModeId((int)shipperData.get(i).getReferModeId());
										shipperData.get(i).setReferModeName(referModeInfo.getReferModeName());
									}
								}
							}
							//receiving data
							if(srDetails.get(sr).getReceiverId()>0) {
								receiverData = shipperReceiverDetailsRepo.findByDispatchIdOfReceiverData(dispatchId,srDetails.get(sr).getReceiverId());
//								System.out.println(receiverData);
								if(receiverData.size()>0) {
									for(int i=0;i<receiverData.size();i++) {
										ReceiverMaster receiverInfo = receiverMasterRepo.findByReceiverId((int)receiverData.get(i).getReceiverId());
										receiverData.get(i).setReceiverName(receiverInfo.getReceiverName());
										CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)receiverData.get(i).getCountryId());
										receiverData.get(i).setCountryName(countryInfo.getCountryName());
										StateMaster stateInfo = stateMasterRepo.findByStateId((int)receiverData.get(i).getStateId());
										receiverData.get(i).setStateName(stateInfo.getStateName());
										CityMaster cityInfo = cityMasterRepo.findByCityId((int)receiverData.get(i).getCityId());
										receiverData.get(i).setCityName(cityInfo.getCityName());
										ReferModeMaster referModeInfo = referModeMasterRepo.findByReferModeId((int)receiverData.get(i).getReferModeId());
										receiverData.get(i).setReferModeName(referModeInfo.getReferModeName());
									}
								}
							}
						}
					}
				}
				
				allDispatchDataViewDto.setDispatchData(dispatchData);
				allDispatchDataViewDto.setOtherChargeData(otherChargesData);
				allDispatchDataViewDto.setShippingData(shipperData);
				allDispatchDataViewDto.setReceivingData(receiverData);
				
				result.setResult(allDispatchDataViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Dispatch Detail Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> AddExceptionLog(ExceptionLog exceptionLog, String tokenValid) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			exceptionLog.setAddedTimestamp(instant.toEpochMilli());
			exceptionLog.setUpdatedTimestamp(instant.toEpochMilli());
			if(tokenValid.equals("true")) {
				exceptionLog = exceptionLogRepo.save(exceptionLog);
			}else {
				exceptionLog = exceptionLogRepo.save(exceptionLog);
			}
			
			result.setResult("Saved");
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Dispatch Details Added Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<EldLogDataViewDto>> ViewEldLogData() {
		
		ResultWrapper<List<EldLogDataViewDto>> result = new ResultWrapper<>();
		try {
			
			Pageable pageableRequest = PageRequest.of(0, 10, Sort.by("utcDateTime").descending());
			Query query = new Query();
//			query.limit(10);
			query.with(pageableRequest);
			List<EldLogDataViewDto> eldLogData = mongoTemplate.find(query, EldLogDataViewDto.class,"eld_log_data");
				
			result.setResult(eldLogData);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Eld Log Information Send Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<DriveringStatusViewDto> AddDriveringStatus(DriveringStatus driveringStatus, String tokenValid) {
		ResultWrapper<DriveringStatusViewDto> result = new ResultWrapper<>();
		try {
			
			DriverStatusLog driverStatusLog = new DriverStatusLog();
			
			if(tokenValid.equals("true")) {
//				System.out.println(driveringStatus);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				LocalDateTime ldtDateTime = LocalDateTime.parse(driveringStatus.getDateTime(), formatter);
				long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				driveringStatus.setLDateTime(lDateTime);
				if(driveringStatus.getUtcDateTime()<=0) {
					driveringStatus.setUtcDateTime(lDateTime);
				}
				
				String logType = driveringStatus.getLogType();
				String sVoilationHour = logType.replaceAll("\\D+", "");
				int voilationHour = sVoilationHour.isEmpty() ? 0 : Integer.parseInt(sVoilationHour);
				driveringStatus.setVoilationHour(voilationHour);
				driveringStatus.setIsVisible(1);
			
//				System.out.println(driveringStatus);
			
				
				
				if(driveringStatus.getIsVoilation()>0) {
					driverStatusLog.setStatusId(0);
				}else {
					long maxId = lookupMaxIdOfDriverOperation(driveringStatus.getDriverId());
					driverStatusLog = new DriverStatusLog();
					if(maxId<=0) {
						maxId=1;
						driverStatusLog.setStatusId(maxId);
					}else {
						maxId++;
						driverStatusLog.setStatusId(maxId);
					}
				}
				
				Instant instant = Instant.now();
				driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
				if(driveringStatus.getAppVersion().equals("")) {
					driveringStatus.setAppVersion("1.0");
				}
				
				String sAddress="";
				if(driveringStatus.getCustomLocation().equals("") && driveringStatus.getCurrentLocation().equals("")) {
					sAddress = GetGoogleAddress(driveringStatus.getLattitude(),driveringStatus.getLongitude());
					driveringStatus.setCustomLocation(sAddress);
					driveringStatus.setCurrentLocation(sAddress);
				}
				
				List<DriveringStatusViewDto> dsData = driveringStatusRepo.findAndViewDriverStatusByDate(driveringStatus.getDriverId(), driveringStatus.getDateTime());
				if(dsData.size()<=0) {
					driveringStatus = driveringStatusRepo.save(driveringStatus);
					
					Query query = new Query();
			        query.addCriteria(Criteria.where("employeeId").is(driveringStatus.getDriverId()));
			        Update update = new Update();
			        update.set("workingStatus", driveringStatus.getStatus());
			        update.set("updatedTimestamp", instant.toEpochMilli());
			        mongoTemplate.findAndModify(query, update, EmployeeMaster.class);
			        
			        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query queryLog = new Query(
					  Criteria.where("driverId").is(driveringStatus.getDriverId())
					);
					queryLog.limit(1);
					queryLog.with(pageableRequest);
					List<DriveringStatusViewDto> dsDataLog = mongoTemplate.find(queryLog, DriveringStatusViewDto.class,"drivering_status");
					
					driverStatusLog.setLogDataId(dsDataLog.get(0).get_id());
					driverStatusLog.setDriverId(driveringStatus.getDriverId());
					driverStatusLog.setVehicleId(driveringStatus.getVehicleId());
					driverStatusLog.setClientId(driveringStatus.getClientId());
					driverStatusLog.setStatus(driveringStatus.getStatus());
					driverStatusLog.setLattitude(driveringStatus.getLattitude());
					driverStatusLog.setLongitude(driveringStatus.getLongitude());
					driverStatusLog.setDateTime(driveringStatus.getUtcDateTime());
					driverStatusLog.setLogType(driveringStatus.getLogType());
					driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
					driverStatusLog.setOrigin(driveringStatus.getOrigin());
					driverStatusLog.setOdometer(driveringStatus.getOdometer());
					driverStatusLog.setIsVoilation(driveringStatus.getIsVoilation());
					driverStatusLog.setNote(driveringStatus.getNote());
					driverStatusLog.setCustomLocation(driveringStatus.getCustomLocation());
					driverStatusLog.setIsReportGenerated(0);
					driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
					driverStatusLog.setIsVisible(1);
					List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(dsDataLog.get(0).get_id());
					if(logDataExist.size()<=0) {
						driverStatusLogRepo.save(driverStatusLog);
						UpdateDriverSequenceId(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
					}
				}
		        
			}else {
//				System.out.println(driveringStatus);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				LocalDateTime ldtDateTime = LocalDateTime.parse(driveringStatus.getDateTime(), formatter);
				long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				driveringStatus.setLDateTime(lDateTime);
//				System.out.println(driveringStatus);
				
				String logType = driveringStatus.getLogType();
				String sVoilationHour = logType.replaceAll("\\D+", "");
				int voilationHour = sVoilationHour.isEmpty() ? 0 : Integer.parseInt(sVoilationHour);
				driveringStatus.setVoilationHour(voilationHour);
				driveringStatus.setIsVisible(1);
				
				if(driveringStatus.getIsVoilation()>0) {
					driverStatusLog.setStatusId(0);
				}else {
					long maxId = lookupMaxIdOfDriverOperation(driveringStatus.getDriverId());
					driverStatusLog = new DriverStatusLog();
					if(maxId<=0) {
						maxId=1;
						driverStatusLog.setStatusId(maxId);
					}else {
						maxId++;
						driverStatusLog.setStatusId(maxId);
					}
				}
				
				Instant instant = Instant.now();
				driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
				if(driveringStatus.getAppVersion().equals("")) {
					driveringStatus.setAppVersion("1.0");
				}
//				System.out.println(driveringStatus);
				List<DriveringStatusViewDto> dsData = driveringStatusRepo.findAndViewDriverStatusByDate(driveringStatus.getDriverId(), driveringStatus.getDateTime());
				if(dsData.size()<=0) {
					driveringStatus = driveringStatusRepo.save(driveringStatus);
					
					Query query = new Query();
			        query.addCriteria(Criteria.where("employeeId").is(driveringStatus.getDriverId()));
			        Update update = new Update();
			        update.set("workingStatus", driveringStatus.getStatus());
			        update.set("updatedTimestamp", instant.toEpochMilli());
			        mongoTemplate.findAndModify(query, update, EmployeeMaster.class);
			        
			        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query queryLog = new Query(
					  Criteria.where("driverId").is(driveringStatus.getDriverId())
					);
					queryLog.limit(1);
					queryLog.with(pageableRequest);
					List<DriveringStatusViewDto> dsDataLog = mongoTemplate.find(queryLog, DriveringStatusViewDto.class,"drivering_status");
					
					driverStatusLog.setLogDataId(dsDataLog.get(0).get_id());
					driverStatusLog.setDriverId(driveringStatus.getDriverId());
					driverStatusLog.setVehicleId(driveringStatus.getVehicleId());
					driverStatusLog.setClientId(driveringStatus.getClientId());
					driverStatusLog.setStatus(driveringStatus.getStatus());
					driverStatusLog.setLattitude(driveringStatus.getLattitude());
					driverStatusLog.setLongitude(driveringStatus.getLongitude());
					driverStatusLog.setDateTime(driveringStatus.getUtcDateTime());
					driverStatusLog.setLogType(driveringStatus.getLogType());
					driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
					driverStatusLog.setOrigin(driveringStatus.getOrigin());
					driverStatusLog.setOdometer(driveringStatus.getOdometer());
					driverStatusLog.setIsVoilation(driveringStatus.getIsVoilation());
					driverStatusLog.setNote(driveringStatus.getNote());
					driverStatusLog.setCustomLocation(driveringStatus.getCustomLocation());
					driverStatusLog.setIsReportGenerated(0);
					driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
					driverStatusLog.setIsVisible(1);
					List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(dsDataLog.get(0).get_id());
					if(logDataExist.size()<=0) {
						driverStatusLogRepo.save(driverStatusLog);
						UpdateDriverSequenceId(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
					}
					
				}
			}
			
	        DriveringStatusViewDto driverStatus = driveringStatusRepo.findAndViewDriverStatusById(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
			
	        
			if(driveringStatus.getOsVersion().equals("web")) {
				Query queryData = new Query(
				    Criteria.where("dateTime").gte(driveringStatus.getUtcDateTime())
		            .and("driverId").is(driveringStatus.getDriverId())
				);
				queryData.with(Sort.by(Sort.Direction.ASC, "statusId"));
				List<DriverStatusLog> dsLogList = mongoTemplate.find(queryData, DriverStatusLog.class, "driver_status_log");
				Query query = new Query();
		        Update update = new Update();
				for(int i=0;i<dsLogList.size();i++) {
					//update driver_status_log
					long dsLogStatusId = driveringStatus.getStatusId()+i;
			        query.addCriteria(
		        			Criteria.where("logDataId").is(dsLogList.get(i).getLogDataId())
		        		);
			        update = new Update();
			        update.set("statusId", dsLogStatusId);
			        mongoTemplate.updateMulti(query, update, DriverStatusLog.class);
				}
				
				// write code remove violations here
				
			}
	        
			result.setResult(driverStatus);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Status Added Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> AddDriveringStatusFromWeb(DriveringStatus driveringStatus) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="Save Log\n";
		try {
			long remainingWeeklyTime = 0,remainingDutyTime = 0,remainingDriveTime = 0, remainingSleepTime = 0;
			int shift=0, days=0, lastDays=0, lastShift=0;
			String currentStatus=driveringStatus.getStatus(),previousStatus="", nextStatus="";
			boolean isDataSave=true;
			boolean isDayChange=false;
			long lastLogUtcDateTime=0;
            
			DriverStatusLog driverStatusLog = new DriverStatusLog();
			
				
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtDateTime = LocalDateTime.parse(driveringStatus.getDateTime(), formatter);
			long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			driveringStatus.setLDateTime(lDateTime);
			if(driveringStatus.getUtcDateTime()<=0) {
				driveringStatus.setUtcDateTime(lDateTime);
			}
			
			// previous log
			Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
			Query queryData = new Query(
			  Criteria.where("driverId").is(driveringStatus.getDriverId())
			  .and("utcDateTime").lt(driveringStatus.getUtcDateTime())
			);
			queryData.limit(1);
			queryData.with(pageableRequest);
			List<DriveringStatusLogViewDto> driveringStatusLastLog = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
	
			if(driveringStatusLastLog.size()>0) {
				remainingWeeklyTime =  Long.parseLong(driveringStatusLastLog.get(0).getRemainingWeeklyTime());
	            remainingDutyTime = Long.parseLong(driveringStatusLastLog.get(0).getRemainingDutyTime());
	            remainingDriveTime = Long.parseLong(driveringStatusLastLog.get(0).getRemainingDriveTime());
	            remainingSleepTime = Long.parseLong(driveringStatusLastLog.get(0).getRemainingSleepTime());
	            shift=driveringStatusLastLog.get(0).getShift();
	            days=driveringStatusLastLog.get(0).getDays();
	            lastLogUtcDateTime=driveringStatusLastLog.get(0).getUtcDateTime();
	            previousStatus = driveringStatusLastLog.get(0).getStatus();
			}else {
				EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driveringStatus.getDriverId());
	            CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
	            remainingWeeklyTime = (cycleUsaData.getCycleHour()*60*60);
	            remainingDutyTime = (cycleUsaData.getOnDutyTime()*60*60);
	            remainingDriveTime = (cycleUsaData.getOnDutyTime()*60*60);
	            remainingSleepTime = (cycleUsaData.getOnSleepTime()*60*60);
	            shift=1;
	            days=1;
	            previousStatus="OffDuty";
			}
			driveringStatus.setRemainingSleepTime(remainingSleepTime);
			driveringStatus.setShift(shift);
			driveringStatus.setDays(days);
			
			long totalSeconds=0;
			if(lastLogUtcDateTime>0) {
				long duration = driveringStatus.getUtcDateTime()-lastLogUtcDateTime;
	            totalSeconds = Math.abs(duration)/1000;
	            driveringStatus.setRemainingDutyTime(remainingDutyTime-totalSeconds);
	            driveringStatus.setRemainingDriveTime(remainingDriveTime-totalSeconds);
	            driveringStatus.setRemainingWeeklyTime(remainingWeeklyTime-totalSeconds);
			}
			
			long maxId = lookupMaxIdOfDriverOperation(driveringStatus.getDriverId());
			
			driverStatusLog = new DriverStatusLog();
			if(maxId<=0) {
				maxId=1;
				driverStatusLog.setStatusId(maxId);
			}else {
				maxId++;
				driverStatusLog.setStatusId(maxId);
			}
			
			Instant instant = Instant.now();
			driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
			if(driveringStatus.getAppVersion().equals("")) {
				driveringStatus.setAppVersion("1.0");
			}
			
			String sAddress="";
			if(driveringStatus.getCustomLocation().equals("") && driveringStatus.getCurrentLocation().equals("")) {
				sAddress = GetGoogleAddress(driveringStatus.getLattitude(),driveringStatus.getLongitude());
				driveringStatus.setCustomLocation(sAddress);
				driveringStatus.setCurrentLocation(sAddress);
			}
			
			List<DriveringStatusViewDto> dsData = driveringStatusRepo.findAndViewDriverStatusByDate(driveringStatus.getDriverId(), driveringStatus.getDateTime());
			if(dsData.size()<=0) {
				sDebug+="C | "+currentStatus+" P | "+previousStatus+" N | "+nextStatus+"\n";
				if (currentStatus.equals(nextStatus) || currentStatus.equals(previousStatus)) {
					isDataSave=false;
				}else {
					driveringStatus.setIsVisible(1);
					driveringStatus = driveringStatusRepo.save(driveringStatus);
				
					
					Query query = new Query();
			        query.addCriteria(Criteria.where("employeeId").is(driveringStatus.getDriverId()));
			        Update update = new Update();
			        update.set("workingStatus", driveringStatus.getStatus());
			        update.set("updatedTimestamp", instant.toEpochMilli());
			        mongoTemplate.findAndModify(query, update, EmployeeMaster.class);
			        
			        DriveringStatusViewDto driverStatus = driveringStatusRepo.findAndViewDriverStatusById(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
			        					
					driverStatusLog.setLogDataId(driverStatus.get_id());
					driverStatusLog.setDriverId(driveringStatus.getDriverId());
					driverStatusLog.setVehicleId(driveringStatus.getVehicleId());
					driverStatusLog.setClientId(driveringStatus.getClientId());
					driverStatusLog.setStatus(driveringStatus.getStatus());
					driverStatusLog.setLattitude(driveringStatus.getLattitude());
					driverStatusLog.setLongitude(driveringStatus.getLongitude());
					driverStatusLog.setDateTime(driveringStatus.getUtcDateTime());
					driverStatusLog.setLogType(driveringStatus.getLogType());
					driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
					driverStatusLog.setOrigin(driveringStatus.getOrigin());
					driverStatusLog.setOdometer(driveringStatus.getOdometer());
					driverStatusLog.setIsVoilation(driveringStatus.getIsVoilation());
					driverStatusLog.setNote(driveringStatus.getNote());
					driverStatusLog.setCustomLocation(driveringStatus.getCustomLocation());
					driverStatusLog.setIsReportGenerated(0);
					driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
					driverStatusLog.setIsVisible(1);
					
					if (currentStatus.equalsIgnoreCase("Login")
	                        || currentStatus.equalsIgnoreCase("Logout")
	                        || currentStatus.equalsIgnoreCase("Certified")) {
	                    driverStatusLog.setOdometer(0);
	                    driverStatusLog.setEngineHour("0");
	                } else {
	                    driverStatusLog.setOdometer(driveringStatus.getOdometer());
	                    driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
	                }

	                List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(driverStatus.get_id());
	                if (logDataExist == null || logDataExist.isEmpty()) {
	                    driverStatusLogRepo.save(driverStatusLog);
	                    UpdateDriverSequenceId(driveringStatus.getDriverId(), driveringStatus.getUtcDateTime());
	                }

	                // -----------------------------
	                // 9) Login/Logout save in login_log
	                // ----------------------------- 
	                if (currentStatus.equalsIgnoreCase("Login")) {

	                    LoginLog loginLog = new LoginLog();
	                    loginLog.setEmployeeId((int) driveringStatus.getDriverId());
	                    loginLog.setUserId(0);
	                    loginLog.setLoginDateTime(driveringStatus.getUtcDateTime());
	                    loginLog.setLogoutDateTime(0);
	                    loginLog.setLoginType(driveringStatus.getOsVersion() == null ? "web" : driveringStatus.getOsVersion());
	                    loginLog.setVehicleId(driveringStatus.getVehicleId());
	                    loginLog.setReceivedTimestamp(instant.toEpochMilli());
	                    loginLog.setIsCoDriver("0");

	                    mongoTemplate.save(loginLog, "login_log");
	                }

	                if (currentStatus.equalsIgnoreCase("Logout")) {

	                    // Find last open login (logoutDateTime == 0) for this employee
	                    Query qLogin = new Query();
	                    qLogin.addCriteria(
	                            Criteria.where("employeeId").is((int) driveringStatus.getDriverId())
	                                    .and("logoutDateTime").lte(0)
	                    );
	                    qLogin.with(Sort.by(Sort.Direction.DESC, "loginDateTime"));
	                    qLogin.limit(1);

	                    LoginLog lastLogin = mongoTemplate.findOne(qLogin, LoginLog.class, "login_log");
	                    if (lastLogin != null) {
	                        lastLogin.setLogoutDateTime(driveringStatus.getUtcDateTime());
	                        lastLogin.setReceivedTimestamp(instant.toEpochMilli());
	                        mongoTemplate.save(lastLogin, "login_log");
	                    }
	                }

	                // -----------------------------
	                // 10) Certified save/update in certified_log
	                // -----------------------------
	                if (currentStatus.equalsIgnoreCase("Certified")) {

	                    ZonedDateTime zdt = Instant.ofEpochMilli(driveringStatus.getLDateTime())
	                            .atZone(ZoneId.systemDefault());

	                    long startOfDay = zdt.toLocalDate()
	                            .atStartOfDay(ZoneId.systemDefault())
	                            .toInstant()
	                            .toEpochMilli();

	                    Query qCert = new Query();
	                    qCert.addCriteria(
	                            Criteria.where("driverId").is(driveringStatus.getDriverId())
	                                    .and("lCertifiedDate").is(startOfDay)
	                    );

	                    CertifiedLog existing = mongoTemplate.findOne(qCert, CertifiedLog.class, "certified_log");

	                    if (existing == null) {
	                        existing = new CertifiedLog();
	                        existing.setAddedTimestamp(instant.toEpochMilli());
	                        existing.setTrailers(new ArrayList<>());
	                        existing.setShippingDocs(new ArrayList<>());
	                        existing.setCoDriverId(0);
	                        existing.setCertifiedSignature("");
	                    }

	                    existing.setDriverId(driveringStatus.getDriverId());
	                    existing.setVehicleId(driveringStatus.getVehicleId());
	                    existing.setTokenNo(driveringStatus.getTokenNo());
	                    existing.setLocalId(driveringStatus.getLocalId());

	                    existing.setCertifiedDate(zdt.toLocalDate().toString()); // yyyy-MM-dd
	                    existing.setLCertifiedDate(startOfDay);

	                    existing.setCertifiedDateTime(driveringStatus.getUtcDateTime());
	                    existing.setCertifiedAt(driveringStatus.getUtcDateTime());

	                    existing.setUpdatedTimestamp(instant.toEpochMilli());

	                    mongoTemplate.save(existing, "certified_log");
	                }
					
//					List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(driverStatus.get_id());
//					if(logDataExist.size()<=0) {
//						driverStatusLogRepo.save(driverStatusLog);
//						UpdateDriverSequenceId(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
//					}
					
					LocalDateTime ldt15DaysPlusDate = LocalDateTime.parse(driveringStatus.getDateTime(), formatter);
					ldt15DaysPlusDate = ldt15DaysPlusDate.plusDays(14);
					LocalDateTime endOfDay15 = ldt15DaysPlusDate.withHour(23).withMinute(59).withSecond(59);
					long to = endOfDay15.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					ZonedDateTime currentDateTime = Instant.ofEpochMilli(lDateTime).atZone(ZoneId.systemDefault());
			        ZonedDateTime startOfDay = currentDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault());
			        long midnightTimestamp = startOfDay.toInstant().toEpochMilli();
			        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1_000_000);
			        long endOfDayTimestamp = endOfDay.toInstant().toEpochMilli();
			        
					long removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driveringStatus.getDriverId(),midnightTimestamp,endOfDayTimestamp,1);
					
					EditDriverLog(driveringStatus.getDriverId(),lDateTime,midnightTimestamp,endOfDayTimestamp,to,totalSeconds,lastLogUtcDateTime,driveringStatus.getStatus(),shift,days);
						
					UpdateDriverSequenceId(driveringStatus.getDriverId(),lDateTime);
					
		            CheckAllVoilations(driveringStatus.getDriverId(),midnightTimestamp,endOfDayTimestamp,to,shift,days);
					
				}
				
			}
			SaveLog(sDebug);
			if(isDataSave) {
				result.setResult("Saved");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Added Successfully");
			}else {
				result.setResult("Not Save");
				result.setStatus(Result.FAIL);
				result.setMessage("This driver log already exist in before or after of current log.");
			}
			
			
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
	
	private final Map<String, ELDLogData> vehicleMap = new ConcurrentHashMap<>();
	private final Map<String, String> vehicleMsgMap = new ConcurrentHashMap<>();
	public void UpdateVehicleLog(ELDLogData liveDataLog) {
	    String vehicleId = liveDataLog.getVehicleId();
	    double speed = liveDataLog.getSpeed();
	    if (speed == 0) {
	        if (!vehicleMap.containsKey(vehicleId)) {
	            vehicleMap.put(vehicleId, liveDataLog);
	            vehicleMsgMap.put(vehicleId, "Offline");
	        }else {
	        	vehicleMsgMap.put(vehicleId, "Idle");
	        }
	    } else {
	        vehicleMap.remove(vehicleId);
	        vehicleMsgMap.remove(vehicleId);
	    }
	}
	
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDriveringStatusOffline(AddDriveringStatusDto addDriveringStatusDto, String tokenValid) {
		ResultWrapper<List<AddDriveringStatusResponseDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			
			Instant instant = Instant.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			
			List<AddDriveringStatusResponseDto> AddDriveringStatusData = new ArrayList<>();
			AddDriveringStatusResponseDto addDriveringStatus = new AddDriveringStatusResponseDto();
			
			DriverStatusLog driverStatusLog = new DriverStatusLog();
			
			ArrayList<DriveringStatus> driveringStatusData = addDriveringStatusDto.getDriveringStatusData();
//			driveringStatusData.sort((d1, d2) -> Long.compare(d1.getUtcDateTime(), d2.getUtcDateTime()));
			
			ArrayList<ELDLogData> eldLogStatusData = addDriveringStatusDto.getEldLogData();
			SplitLog splitLog  = addDriveringStatusDto.getSplitLog();
			sDebug+=" >> "+eldLogStatusData+",";
			String currentDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date (instant.toEpochMilli()));
			ELDLogData eldLogData = null;
			double dLattitude=0,dLongitude=0;
			String sAddress = "";
			String timezoneName="",timezoneOffSet="";
			LiveDataLog liveDataLog = new LiveDataLog();
			final long MIN_IDLE_DURATION = 60 * 1000;
			
			try {
				 List<SplitLog> splitLogData = splitLogRepo.findSplitLogByDriverId(splitLog.getDriverId());
				 sDebug+="S : "+splitLogData.size()+", "+splitLog;
				 if(splitLogData.size()<=0) {
					 splitLog.setUpdatedTimestamp(instant.toEpochMilli());
					 splitLogRepo.save(splitLog);
				 }else {
					Query query = new Query();
			        query.addCriteria(Criteria.where("driverId").is(splitLog.getDriverId()));
			        Update update = new Update();
			        update.set("dbId", splitLog.getDbId());
			        update.set("status", splitLog.getStatus());
			        update.set("splitTiming", splitLog.getSplitTiming());
			        update.set("shift", splitLog.getShift());
			        update.set("day", splitLog.getDay());
			        update.set("updatedTimestamp", instant.toEpochMilli());
			        mongoTemplate.findAndModify(query, update, SplitLog.class);
				 }
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			for(int i=0;i<eldLogStatusData.size();i++) {
				eldLogData = eldLogStatusData.get(i);
				sDebug+="0--"+eldLogData.getDateTime()+",";
				LocalDateTime ldtDateTime = LocalDateTime.parse(eldLogData.getDateTime(), formatter);
				long lDateTime = ldtDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
				sDebug+="1,";
				eldLogData.setLDateTime(lDateTime);
				eldLogData.setReceive_time(instant.toEpochMilli());
				eldLogData.setReceive_data_time(currentDateTime);
				sDebug+="2,";
				String latLng = eldLogData.getLatLong();
		        String[] splitLatLng = latLng.split("/");
		        dLattitude = Double.valueOf(splitLatLng[0]);
		        dLongitude = Double.valueOf(splitLatLng[1]);
		        sDebug+="3,";
		        
		        double[] point = {dLongitude,dLattitude};
		        try {
		        	 GeofanceMaster lstGeofacne = geofanceMasterRepo.findByAreaIntersects(point);
		        	eldLogData.setGeoStateId(lstGeofacne.getStateId());
		        	List<StateMaster> states = stateMasterRepo.findAndViewByGeofanceId(lstGeofacne.getGeoId());
		        	eldLogData.setStateId(states.get(0).getStateId());
		        }catch(Exception ex) {
		        	ex.printStackTrace();
		        }
		       
		        eldLogData.setLattitude(dLattitude);
		        eldLogData.setLongitude(dLongitude);
		        sDebug+="4,";
		        sAddress = GetGoogleAddress(dLattitude,dLongitude);
		        sDebug+="5,";
		        eldLogData.setPlaceAddress(sAddress);
		        sDebug+="6,";
		        eldLogDataRepo.save(eldLogData);
		        
		        List<LiveDataLog> liveDataLogData = liveDataLogRepo.findAndViewLiveDataLog(eldLogData.getDriverId(),eldLogData.getMAC());
		        if(liveDataLogData.size()<=0) {
		        	liveDataLog = new LiveDataLog();
			        liveDataLog.setMAC(eldLogData.getMAC());
			        liveDataLog.setDateTime(eldLogData.getUtcDateTime());
			        liveDataLog.setDriverId(eldLogData.getDriverId());
			        liveDataLog.setLattitude(dLattitude);
			        liveDataLog.setLongitude(dLongitude);
			        liveDataLog.setPlaceAddress(sAddress);
			        liveDataLog.setModel(eldLogData.getModel());
			        liveDataLog.setOdometer(eldLogData.getOdometer());
			        liveDataLog.setClientId(eldLogData.getClientId());
			        liveDataLog.setSerialNo(eldLogData.getSerialNo());
			        liveDataLog.setSpeed(eldLogData.getSpeed());
			        liveDataLog.setVIN(eldLogData.getVIN());
			        liveDataLog.setVehicleId(eldLogData.getVehicleId());
			        liveDataLog.setVersion(eldLogData.getVersion());
			        liveDataLog.setReceive_time(instant.toEpochMilli());
			        liveDataLogRepo.save(liveDataLog);
		        }else {
		        	Query query = new Query();
		        	 query.addCriteria(
	        			Criteria.where("MAC").is(eldLogData.getMAC())
	        			.and("DriverId").is(eldLogData.getDriverId())
	        		);
			        Update update = new Update();
			        update.set("DateTime", eldLogData.getUtcDateTime());
			        update.set("Lattitude", dLattitude);
			        update.set("Longitude", dLongitude);
			        update.set("PlaceAddress", sAddress);
			        update.set("Model", eldLogData.getModel());
			        update.set("Odometer", eldLogData.getOdometer());
			        update.set("clientId", eldLogData.getClientId());
			        update.set("SerialNo", eldLogData.getSerialNo());
			        update.set("Speed", eldLogData.getSpeed());
			        update.set("VIN", eldLogData.getVIN());
			        update.set("VehicleId", eldLogData.getVehicleId());
			        update.set("Version", eldLogData.getVersion());
			        update.set("receive_time", instant.toEpochMilli());
			        mongoTemplate.findAndModify(query, update, LiveDataLog.class);
			        
			        ELDLogData data = vehicleMap.get(eldLogData.getVehicleId());
		        	String alertMsg = vehicleMsgMap.get(eldLogData.getVehicleId());
			        if (data != null) {
			        	long durationInMillis = eldLogData.getUtcDateTime() - data.getUtcDateTime();
			        	if(durationInMillis>MIN_IDLE_DURATION) {
			        		// idle and save alerts
			        		List<AlertsLog> alertLogData = alertsLogRepo.findAndViewByDriverIdAndUtcDateTime(Long.parseLong(eldLogData.getDriverId()),data.getUtcDateTime());
			        		if(alertLogData.size()<=0) {
			        			AlertsLog alertsLog = new AlertsLog();
				        		alertsLog.setDriverId(Long.parseLong(eldLogData.getDriverId()));
				        		alertsLog.setVehicleId(Long.parseLong(eldLogData.getVehicleId()));
				        		alertsLog.setClientId(eldLogData.getClientId());
				        		alertsLog.setMAC(eldLogData.getMAC());
				        		alertsLog.setModel(eldLogData.getModel());
				        		alertsLog.setSerialNo(eldLogData.getSerialNo());
				        		alertsLog.setVin(eldLogData.getVIN());
				        		alertsLog.setVersion(eldLogData.getVersion());
				        		alertsLog.setOdometer(eldLogData.getOdometer());
				        		alertsLog.setStartUtcDateTime(data.getUtcDateTime());
				        		alertsLog.setEndUtcDateTime(eldLogData.getUtcDateTime());
				        		alertsLog.setDurationInMillis(durationInMillis);
				        		alertsLog.setPlaceAddress(data.getPlaceAddress());
				        		alertsLog.setMessage(alertMsg);
				        		alertsLog.setIsRead(0);
				        		alertsLog.setLattitude(data.getLattitude());
				        		alertsLog.setLongitude(data.getLongitude());
				        		alertsLog.setAddedTimestamp(instant.toEpochMilli());
				        		
				        		alertsLogRepo.save(alertsLog);
			        		}else {
			        			query = new Query();
					        	 query.addCriteria(
				        			Criteria.where("driverId").is(Long.parseLong(eldLogData.getDriverId()))
				        			.and("startUtcDateTime").is(data.getUtcDateTime())
				        		);
						        update = new Update();
						        update.set("endUtcDateTime", eldLogData.getUtcDateTime());
						        update.set("durationInMillis", durationInMillis);
						        mongoTemplate.findAndModify(query, update, AlertsLog.class);
			        		}
			        	}
			        } 
			        UpdateVehicleLog(eldLogData);
			        
		        }
			}
			
			
			DriveringStatus driveringStatus = null;
			for(int i=0;i<driveringStatusData.size();i++) {
				driveringStatus = driveringStatusData.get(i);
				
				String logType = driveringStatus.getLogType();
				String sVoilationHour = logType.replaceAll("\\D+", "");
				int voilationHour = sVoilationHour.isEmpty() ? 0 : Integer.parseInt(sVoilationHour);
				driveringStatus.setVoilationHour(voilationHour);
				
				if(tokenValid.equals("true")) {
					LocalDateTime ldtDateTime = LocalDateTime.parse(driveringStatus.getDateTime(), formatter);
					long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					driveringStatus.setLDateTime(lDateTime);
					
					if(driveringStatus.getIsVoilation()>0) {
						driverStatusLog.setStatusId(0);
					}else {
						long maxId = lookupMaxIdOfDriverOperation(driveringStatus.getDriverId());
						driverStatusLog = new DriverStatusLog();
						if(maxId<=0) {
							maxId=1;
							driverStatusLog.setStatusId(maxId);
						}else {
							maxId++;
							driverStatusLog.setStatusId(maxId);
						}
					}
					
					driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
					if(driveringStatus.getAppVersion().equals("")) {
						driveringStatus.setAppVersion("1.0");
					}
					
					sAddress="";
					if(driveringStatus.getCustomLocation().equals("") && driveringStatus.getCurrentLocation().equals("")) {
						sAddress = GetGoogleAddress(driveringStatus.getLattitude(),driveringStatus.getLongitude());
						driveringStatus.setCustomLocation(sAddress);
						driveringStatus.setCurrentLocation(sAddress);
					}
					
					List<DriveringStatusViewDto> dsData = driveringStatusRepo.findAndViewDriverStatusByDate(driveringStatus.getDriverId(), driveringStatus.getDateTime());
					if(dsData.size()<=0) {
						driveringStatus.setIsVisible(1);
						driveringStatus = driveringStatusRepo.save(driveringStatus);
						
//						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatus.getDriverId());
//						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
//						if(mainTerminal.getStateId()>0) {
//							StateMaster stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
//							timezoneName=stateInfo.getTimeZone();
//							timezoneOffSet=stateInfo.getTimezoneOffSet();
//						}
//						
//						String splitStr[] = timezoneOffSet.split(":");
//						long hour = Math.abs(Long.parseLong(splitStr[0]));
//						long minutes = Math.abs(Long.parseLong(splitStr[1]));
////							System.out.println(Long.parseLong(splitStr[0]));
//						String minusPlus="";
//						long timestampValue=0;
//						if(timezoneOffSet.substring(0,1).equals("-")) {
//							minusPlus = "minus";
//							timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
//						}else {
//							minusPlus = "plus";
//							timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
//						}
						
						Query query = new Query();
				        query.addCriteria(Criteria.where("employeeId").is(driveringStatus.getDriverId()));
				        Update update = new Update();
				        update.set("workingStatus", driveringStatus.getStatus());
				        update.set("updatedTimestamp", driveringStatus.getUtcDateTime());
				        mongoTemplate.findAndModify(query, update, EmployeeMaster.class);
				        
				        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query queryLog = new Query(
						  Criteria.where("driverId").is(driveringStatus.getDriverId())
						);
						queryLog.limit(1);
						queryLog.with(pageableRequest);
						List<DriveringStatusViewDto> dsDataLog = mongoTemplate.find(queryLog, DriveringStatusViewDto.class,"drivering_status");
						
						driverStatusLog.setLogDataId(dsDataLog.get(0).get_id());
						driverStatusLog.setDriverId(driveringStatus.getDriverId());
						driverStatusLog.setVehicleId(driveringStatus.getVehicleId());
						driverStatusLog.setClientId(driveringStatus.getClientId());
						driverStatusLog.setStatus(driveringStatus.getStatus());
						driverStatusLog.setLattitude(driveringStatus.getLattitude());
						driverStatusLog.setLongitude(driveringStatus.getLongitude());
						driverStatusLog.setDateTime(driveringStatus.getUtcDateTime());
						driverStatusLog.setLogType(driveringStatus.getLogType());
						driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
						driverStatusLog.setOrigin(driveringStatus.getOrigin());
						driverStatusLog.setOdometer(driveringStatus.getOdometer());
						driverStatusLog.setIsVoilation(driveringStatus.getIsVoilation());
						driverStatusLog.setNote(driveringStatus.getNote());
						driverStatusLog.setCustomLocation(driveringStatus.getCustomLocation());
						driverStatusLog.setIsReportGenerated(0);
						driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
						driverStatusLog.setIsVisible(1);
						List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(dsDataLog.get(0).get_id());
						if(logDataExist.size()<=0) {
							driverStatusLogRepo.save(driverStatusLog);
							UpdateDriverSequenceId(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
						}
						
					}
					
			        
				}else {
					LocalDateTime ldtDateTime = LocalDateTime.parse(driveringStatus.getDateTime(), formatter);
					long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					driveringStatus.setLDateTime(lDateTime);
					
					if(driveringStatus.getIsVoilation()>0) {
						driverStatusLog.setStatusId(0);
					}else {
						long maxId = lookupMaxIdOfDriverOperation(driveringStatus.getDriverId());
						driverStatusLog = new DriverStatusLog();
						if(maxId<=0) {
							maxId=1;
							driverStatusLog.setStatusId(maxId);
						}else {
							maxId++;
							driverStatusLog.setStatusId(maxId);
						}
					}
					
					driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
					if(driveringStatus.getAppVersion().equals("")) {
						driveringStatus.setAppVersion("1.0");
					}
					List<DriveringStatusViewDto> dsData = driveringStatusRepo.findAndViewDriverStatusByDate(driveringStatus.getDriverId(), driveringStatus.getDateTime());
					if(dsData.size()<=0) {
						driveringStatus.setIsVisible(1);
						driveringStatus = driveringStatusRepo.save(driveringStatus);
						
						Query query = new Query();
				        query.addCriteria(Criteria.where("employeeId").is(driveringStatus.getDriverId()));
				        Update update = new Update();
				        update.set("workingStatus", driveringStatus.getStatus());
				        update.set("updatedTimestamp", instant.toEpochMilli());
				        mongoTemplate.findAndModify(query, update, EmployeeMaster.class);
				        
				        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query queryLog = new Query(
						  Criteria.where("driverId").is(driveringStatus.getDriverId())
						);
						queryLog.limit(1);
						queryLog.with(pageableRequest);
						List<DriveringStatusViewDto> dsDataLog = mongoTemplate.find(queryLog, DriveringStatusViewDto.class,"drivering_status");
						
						driverStatusLog.setLogDataId(dsDataLog.get(0).get_id());
						driverStatusLog.setDriverId(driveringStatus.getDriverId());
						driverStatusLog.setVehicleId(driveringStatus.getVehicleId());
						driverStatusLog.setClientId(driveringStatus.getClientId());
						driverStatusLog.setStatus(driveringStatus.getStatus());
						driverStatusLog.setLattitude(driveringStatus.getLattitude());
						driverStatusLog.setLongitude(driveringStatus.getLongitude());
						driverStatusLog.setDateTime(driveringStatus.getUtcDateTime());
						driverStatusLog.setLogType(driveringStatus.getLogType());
						driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
						driverStatusLog.setOrigin(driveringStatus.getOrigin());
						driverStatusLog.setOdometer(driveringStatus.getOdometer());
						driverStatusLog.setIsVoilation(driveringStatus.getIsVoilation());
						driverStatusLog.setNote(driveringStatus.getNote());
						driverStatusLog.setCustomLocation(driveringStatus.getCustomLocation());
						driverStatusLog.setIsReportGenerated(0);
						driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
						driverStatusLog.setIsVisible(1);
						List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(dsDataLog.get(0).get_id());
						if(logDataExist.size()<=0) {
							driverStatusLogRepo.save(driverStatusLog);
							UpdateDriverSequenceId(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
						}
						
					}
				}
				
				
								
		        DriveringStatusViewDto driverStatus = driveringStatusRepo.findAndViewDriverStatusById(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
		        String objId = driverStatus.get_id();
		        addDriveringStatus = new AddDriveringStatusResponseDto();
		        addDriveringStatus.setLocalId(driveringStatus.getLocalId());
		        addDriveringStatus.setServerId(objId);
		        AddDriveringStatusData.add(addDriveringStatus);
			}
			
			result.setResult(AddDriveringStatusData);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Status Added Successfully"+sDebug);
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	private String GetGoogleAddress(double lat, double lng) {
        String sAddress="";
        try {
        	SaveLog(lat+" :: "+lng);
            String sUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&key=AIzaSyBR2Xiu4PJOVrPD125X0X7qUnC0H72NTus";
            URL url = new URL(sUrl);   
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/4.76"); 

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String text;
            StringBuilder result = new StringBuilder();
            while ((text = in.readLine()) != null)
                result.append(text);

            in.close();
            
            org.json.JSONObject jObject = new org.json.JSONObject(result.toString());	

            if(jObject.has("error")){
                System.err.println(jObject.get("error"));
                return "Invalid Location";
            }	
            
            org.json.JSONObject location = jObject.getJSONArray("results").getJSONObject(0); 
            sAddress = location.getString("formatted_address");
            SaveLog(sAddress);
        }
        catch (IOException ex) {
            System.out.println("Get Address Error: "+ ex.getMessage());
            return sAddress;
        }
        catch (Exception ex) {
            System.out.println("Get Address Error: "+ ex.getMessage());
        }
        return sAddress;
    }
	
	public List<Map<String, Object>> callReportCreateMethods(long driverId, long from, long to, String logType, String driverName) {
		List<Map<String, Object>> finalList = new ArrayList<>();
		if(logType.equals("driver_log")) {
			System.out.println(">> Processing Driver Log PDF");
			finalList.add(dispatchServiceImpl.createDriverLogStatusPDF(driverId,from,to, driverName));
		}else if(logType.equals("dvir_log")) {
			System.out.println(">> Processing DVIR Log PDF");
			finalList.add(dispatchServiceImpl.createDVIRLogStatusPDF(driverId,from,to, driverName));
		}
		
		
		System.out.println("+++++ Finish +++++");
		return finalList;
	}
	
	public PdfPTable getReportHeader(String reportName, Integer cols, String driverName) {
		try {

			PdfPTable table = new PdfPTable(cols);

			table.setWidthPercentage(100f);

			BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font font = new Font(baseFont, 14, Font.NORMAL);
			//font.setColor(255, 255, 255);

			PdfPCell logo = new PdfPCell(new Paragraph("ELD NEXT", font));
			logo.setVerticalAlignment(Element.ALIGN_CENTER);
			logo.setHorizontalAlignment(Element.ALIGN_CENTER);
			//logo.setRowspan(2);
			int iColSpan=0;
			//logo.setColspan(2);
			logo.setPadding(4);
			logo.setPaddingLeft(20);
			table.addCell(logo);
			
			PdfPCell name = new PdfPCell(new Paragraph(reportName, font));
			name.setVerticalAlignment(Element.ALIGN_CENTER);
			name.setHorizontalAlignment(Element.ALIGN_CENTER);
			name.setColspan(cols - iColSpan);
			name.setPadding(15f);
			table.addCell(name);

			/*
			 * report header data Cells
			 */
			BaseFont headreDataBaseFont = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1250,
					BaseFont.EMBEDDED);
			Font headerDataFont = new Font(headreDataBaseFont, 12, Font.NORMAL);
			
			Font headerDataFontColor = new Font(headreDataBaseFont, 12, Font.NORMAL);
//			headerDataFontColor.setColor(255, 255, 255);
			headerDataFontColor.setColor(0,0,0);
			
			BaseColor backgroundColor = new BaseColor(135,206,235);
			
			int datacol = cols / 6;
			
			PdfPCell driverNameHeading = new PdfPCell(new Paragraph("Driver Name :", headerDataFontColor));
			driverNameHeading.setVerticalAlignment(Element.ALIGN_CENTER);
			driverNameHeading.setHorizontalAlignment(Element.ALIGN_CENTER);
			driverNameHeading.setPadding(4f);
			driverNameHeading.setColspan(datacol);
			driverNameHeading.setRowspan(1);
			driverNameHeading.setBackgroundColor(backgroundColor);
			table.addCell(driverNameHeading);
			
			PdfPCell statusHeading = new PdfPCell(new Paragraph(driverName, headerDataFontColor));
			statusHeading.setVerticalAlignment(Element.ALIGN_CENTER);
			statusHeading.setHorizontalAlignment(Element.ALIGN_CENTER);
			statusHeading.setPadding(4f);
			if(reportName.equals("DVIR Log Status")) {
				statusHeading.setColspan(5);
			}else {
				statusHeading.setColspan(2);
			}
			statusHeading.setRowspan(1);
			statusHeading.setBackgroundColor(backgroundColor);
			table.addCell(statusHeading);
			
//			PdfPCell dateTimeHeading = new PdfPCell(new Paragraph("Date Time :", headerDataFontColor));
//			dateTimeHeading.setVerticalAlignment(Element.ALIGN_CENTER);
//			dateTimeHeading.setHorizontalAlignment(Element.ALIGN_CENTER);
//			dateTimeHeading.setPadding(4f);
//			dateTimeHeading.setColspan(datacol);
//			dateTimeHeading.setRowspan(1);
//			dateTimeHeading.setBackgroundColor(backgroundColor);
//			table.addCell(dateTimeHeading);

//			PdfPCell blankCell = new PdfPCell();
//			int blankColspan = cols - (datacol * 4);
//			blankCell.setColspan(blankColspan);
//			table.addCell(blankCell);
			return table;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Object> createDriverLogStatusPDF(long driverId, long from, long to, String sDriverName) {
		Map<String, Object> finalMap = new LinkedHashMap<>();
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			Document document = new Document(PageSize.A4.rotate());
			BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font font = new Font(baseFont, 10, Font.NORMAL);
//			font.setColor(255, 255, 255);
			font.setColor(0, 0, 0);
			BaseColor backgroundColor = new BaseColor(135,206,235);

//			Map<String, Map<String, Object>> resultMap = (Map<String, Map<String, Object>>) reportService.getMonthlyFeederReliabilityReport(ssDeviceId, date);
			
			String fileName = "driver_log.pdf"; 
//			String fileName = "driver_log.xlsx"; 
			String subject = "Driver Log Status";
			String text = "This is driver log status report";

			PdfWriter.getInstance(document, outputStream);
			Integer colNum = 3;
			String reportName = "Driver Log Status";
//			PdfPTable createTable = new PdfPTable(colNum);

			/*
			 * getting report header
			 */
			PdfPTable createTable = getReportHeader(reportName, colNum, sDriverName);

			Paragraph statusP = new Paragraph("Status", font);
			PdfPCell statusCell = new PdfPCell(statusP);
			statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			statusCell.setVerticalAlignment(Element.ALIGN_CENTER);
			statusCell.setRowspan(2);
			statusCell.setBackgroundColor(backgroundColor);
			statusCell.setPadding(8f);
			createTable.addCell(statusCell);

			Paragraph dateTimeP = new Paragraph("Date Time", font);
			PdfPCell dateTimeCell = new PdfPCell(dateTimeP);
			dateTimeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dateTimeCell.setVerticalAlignment(Element.ALIGN_CENTER);
			dateTimeCell.setRowspan(2);
			dateTimeCell.setBackgroundColor(backgroundColor);
			dateTimeCell.setPadding(8f);
			createTable.addCell(dateTimeCell);
			
			Paragraph locationP = new Paragraph("Location", font);
			PdfPCell locationCell = new PdfPCell(locationP);
			locationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			locationCell.setVerticalAlignment(Element.ALIGN_CENTER);
			locationCell.setColspan(1);
			locationCell.setRowspan(2);
			locationCell.setBackgroundColor(backgroundColor);
			locationCell.setPadding(8f);
			createTable.addCell(locationCell);

			BaseFont dataBaseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font dataFont = new Font(dataBaseFont, 9, Font.NORMAL);
			
			List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,driverId);
	        String driverName="",status="",dateTime="", location="";
			for(int i=0;i<driveringStatusViewDto.size();i++) {
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
//				System.out.println(empInfo);
				driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
				status = driveringStatusViewDto.get(i).getStatus();
				dateTime = driveringStatusViewDto.get(i).getDateTime();
				location = driveringStatusViewDto.get(i).getLattitude()+" , "+driveringStatusViewDto.get(i).getLongitude();
				
				PdfPCell commonCell2 = new PdfPCell(new Paragraph(status, dataFont));
//				PdfPCell commonCell2 = new PdfPCell(new Paragraph(testNull(commonValue), dataFont));
				commonCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				commonCell2.setVerticalAlignment(Element.ALIGN_CENTER);
				commonCell2.setRowspan(1);
				commonCell2.setColspan(1);
				commonCell2.setPadding(3f);
				createTable.addCell(commonCell2);
				
				PdfPCell commonCell3 = new PdfPCell(new Paragraph(dateTime, dataFont));
//				PdfPCell commonCell3 = new PdfPCell(new Paragraph(testNull(commonValue), dataFont));
				commonCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				commonCell3.setVerticalAlignment(Element.ALIGN_CENTER);
				commonCell3.setRowspan(1);
				commonCell3.setColspan(1);
				commonCell3.setPadding(3f);
				createTable.addCell(commonCell3);
				
				PdfPCell commonCell1 = new PdfPCell(new Paragraph(location, dataFont));
//				PdfPCell commonCell1 = new PdfPCell(new Paragraph(testNull(commonValue), dataFont));
				commonCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				commonCell1.setVerticalAlignment(Element.ALIGN_CENTER);
				commonCell1.setRowspan(1);
				commonCell1.setColspan(1);
				commonCell1.setPadding(3f);
				createTable.addCell(commonCell1);
        		
			}

			document.open();
			document.add(createTable);
			document.close();
		
			finalMap.put("outputStream", outputStream);
			finalMap.put("fileName", fileName);
			finalMap.put("subject", subject);
			finalMap.put("text", text);
			
			System.out.println("Final mail data : "+finalMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalMap;
	}
	
	public Map<String, Object> createDVIRLogStatusPDF_old(long driverId, long from, long to, String sDriverName) {
	    Map<String, Object> finalMap = new LinkedHashMap<>();
	    try {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, outputStream);

	        Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
	        Font dataFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

	        document.open();

	        // === Title ===
	        Paragraph title = new Paragraph("DVIR", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(Chunk.NEWLINE);

	        // === Fetch Data ===
	        List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(from, to, driverId);
	        EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int) driverId);
	        ClientMaster clientInfo = clientMasterRepo.findByClientId((int) empInfo.getClientId());
	        
	        VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(0).getVehicleId());
	        String vin = vehcileInfo.getVin();
	        String vehicleNo = vehcileInfo.getVehicleNo();


	        String driverName = empInfo.getFirstName() + " " + empInfo.getLastName();
	        String companyName = clientInfo.getClientName();
	        String dateTime = dvirDataViewDto.size() > 0 ? dvirDataViewDto.get(0).getDateTime() : "";
	        String location = dvirDataViewDto.size() > 0 ? dvirDataViewDto.get(0).getLocation() : "";
	        String trailer = dvirDataViewDto.size() > 0 ? String.join(",", dvirDataViewDto.get(0).getTrailer()) : "None";
	        String vehicleDefect = dvirDataViewDto.size() > 0 ? String.join(",", dvirDataViewDto.get(0).getTruckDefect()) : "None";
	        String trailerDefect = dvirDataViewDto.size() > 0 ? String.join(",", dvirDataViewDto.get(0).getTrailerDefect()) : "None";
	        String remarks = dvirDataViewDto.get(0).getNotes();
	        String odometer = dvirDataViewDto.size() > 0 ? String.valueOf(dvirDataViewDto.get(0).getOdometer()) : "0";
	        String engineHour = dvirDataViewDto.size() > 0 ? String.valueOf(dvirDataViewDto.get(0).getEngineHour()) : "0";

	        // Signature file path
	        String signaturePath = dvirDataViewDto.get(0).getDriverSignFile();

	        // === Main Table ===
	        PdfPTable mainTable = new PdfPTable(2);
	        mainTable.setWidthPercentage(100);
	        mainTable.setSpacingBefore(10f);

	        mainTable.addCell(getCell("TIME", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell("LOCATION", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell(dateTime, Element.ALIGN_LEFT, dataFont));
	        mainTable.addCell(getCell(location, Element.ALIGN_LEFT, dataFont));

	        mainTable.addCell(getCell("DRIVER", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell("COMPANY", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell(driverName, Element.ALIGN_LEFT, dataFont));
	        mainTable.addCell(getCell(companyName, Element.ALIGN_LEFT, dataFont));

	        mainTable.addCell(getCell("VEHICLE", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell("TRAILER", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell(vehicleNo, Element.ALIGN_LEFT, dataFont));
	        mainTable.addCell(getCell(trailer, Element.ALIGN_LEFT, dataFont));

	        mainTable.addCell(getCell("VIN", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell(vin, Element.ALIGN_LEFT, dataFont));

	        // Defects (text only)
	        mainTable.addCell(getCell("VEHICLE DEFECTS", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell("TRAILER DEFECTS", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell(vehicleDefect, Element.ALIGN_LEFT, dataFont));
	        mainTable.addCell(getCell(trailerDefect, Element.ALIGN_LEFT, dataFont));

	        mainTable.addCell(getCell("ODOMETER", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell("ENGINE HOUR", Element.ALIGN_LEFT, headerFont));
	        mainTable.addCell(getCell(odometer, Element.ALIGN_LEFT, dataFont));
	        mainTable.addCell(getCell(engineHour, Element.ALIGN_LEFT, dataFont));

	        PdfPCell remarksHeader = getCell("REMARKS", Element.ALIGN_LEFT, headerFont);
	        remarksHeader.setColspan(2);
	        mainTable.addCell(remarksHeader);

	        PdfPCell remarksData = getCell(remarks, Element.ALIGN_LEFT, dataFont);
	        remarksData.setColspan(2);
	        mainTable.addCell(remarksData);

	        document.add(mainTable);

	        // === Signature ===
	        document.add(Chunk.NEWLINE);
	        document.add(new Paragraph("Signed by the driver " +
	                new SimpleDateFormat("yyyy-MM-dd").format(new Date()), dataFont));
	        document.add(Chunk.NEWLINE);

	        if (signaturePath != null) {
	            try {
	                Image signImg = Image.getInstance(WEB_URL_FILE_UPLOAD + "/uploads/dvir/" + signaturePath);
	                signImg.scaleToFit(200f, 80f);
	                PdfPTable signTable = new PdfPTable(1);
	                signTable.setWidthPercentage(40);
	                PdfPCell signCell = new PdfPCell(signImg);
	                signCell.setBorder(Rectangle.BOX);
	                signCell.setPadding(6f);
	                signTable.addCell(signCell);
	                document.add(signTable);
	            } catch (Exception e) {
	                document.add(new Paragraph("[Signature not available]", dataFont));
	            }
	        }

	        document.close();

	        finalMap.put("outputStream", outputStream);
	        finalMap.put("fileName", "dvir_log.pdf");
	        finalMap.put("subject", "DVIR Report");
	        finalMap.put("text", "This is a DVIR report");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return finalMap;
	}
	
	public Map<String, Object> createDVIRLogStatusPDF(long driverId, long from, long to, String sDriverName) {
	    Map<String, Object> finalMap = new LinkedHashMap<>();
	    try {
	    	
	    	DecimalFormat df = new DecimalFormat("0.00");
	    	
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, outputStream);

	        Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
	        Font dataFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

	        document.open();

	        // === Title ===
	        Paragraph title = new Paragraph("DVIR Report", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
	        title.setAlignment(Element.ALIGN_CENTER);
	        document.add(title);
	        document.add(Chunk.NEWLINE);

	        // === Fetch Data ===
	        List<DVIRDataCRUDDto> dvirDataList = lookupDVIRDataOperation(from, to, driverId);
	        EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int) driverId);
	        ClientMaster clientInfo = clientMasterRepo.findByClientId((int) empInfo.getClientId());

	        String driverName = empInfo.getFirstName() + " " + empInfo.getLastName();
	        String companyName = clientInfo.getClientName();

	        // === Group DVIRs by date (yyyy-MM-dd) ===
	        Map<String, List<DVIRDataCRUDDto>> groupedByDate = dvirDataList.stream()
	            .collect(Collectors.groupingBy(d -> d.getDateTime().substring(0, 10), LinkedHashMap::new, Collectors.toList()));

	        // === Iterate each date ===
	        for (Map.Entry<String, List<DVIRDataCRUDDto>> entry : groupedByDate.entrySet()) {
	            String currentDate = entry.getKey();
	            List<DVIRDataCRUDDto> dayRecords = entry.getValue();

	            DVIRDataCRUDDto dvir = dayRecords.get(0); // take first record of the day
	            VehicleMaster vehicleInfo = vehicleMasterRepo.findByVehicleId((int) dvir.getVehicleId());

	            String vin = vehicleInfo.getVin();
	            String vehicleNo = vehicleInfo.getVehicleNo();
	            String location = dvir.getLocation();
	            String trailer = dvir.getTrailer() != null ? String.join(",", dvir.getTrailer()) : "None";
	            String vehicleDefect = dvir.getTruckDefect() != null ? String.join(",", dvir.getTruckDefect()) : "None";
	            String trailerDefect = dvir.getTrailerDefect() != null ? String.join(",", dvir.getTrailerDefect()) : "None";
	            String remarks = dvir.getNotes() != null ? dvir.getNotes() : "";
	            String odometer = df.format(dvir.getOdometer());
	            String engineHour = df.format(Double.parseDouble(dvir.getEngineHour()));
	            String signaturePath = dvir.getDriverSignFile();

	            // === Sub-header for the day ===
	            Paragraph dayHeader = new Paragraph("Date: " + currentDate, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
	            dayHeader.setAlignment(Element.ALIGN_LEFT);
	            document.add(dayHeader);
	            document.add(Chunk.NEWLINE);

	            // === DVIR Info Table ===
	            PdfPTable mainTable = new PdfPTable(2);
	            mainTable.setWidthPercentage(100);
	            mainTable.setSpacingBefore(10f);

	            mainTable.addCell(getCell("TIME", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell("LOCATION", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell(dvir.getDateTime(), Element.ALIGN_LEFT, dataFont));
	            mainTable.addCell(getCell(location, Element.ALIGN_LEFT, dataFont));

	            mainTable.addCell(getCell("DRIVER", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell("COMPANY", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell(driverName, Element.ALIGN_LEFT, dataFont));
	            mainTable.addCell(getCell(companyName, Element.ALIGN_LEFT, dataFont));

	            mainTable.addCell(getCell("VEHICLE", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell("TRAILER", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell(vehicleNo, Element.ALIGN_LEFT, dataFont));
	            mainTable.addCell(getCell(trailer, Element.ALIGN_LEFT, dataFont));

	            mainTable.addCell(getCell("VIN", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell(vin, Element.ALIGN_LEFT, dataFont));

	            mainTable.addCell(getCell("VEHICLE DEFECTS", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell("TRAILER DEFECTS", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell(vehicleDefect, Element.ALIGN_LEFT, dataFont));
	            mainTable.addCell(getCell(trailerDefect, Element.ALIGN_LEFT, dataFont));

	            mainTable.addCell(getCell("ODOMETER", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell("ENGINE HOUR", Element.ALIGN_LEFT, headerFont));
	            mainTable.addCell(getCell(odometer, Element.ALIGN_LEFT, dataFont));
	            mainTable.addCell(getCell(engineHour, Element.ALIGN_LEFT, dataFont));

	            PdfPCell remarksHeader = getCell("REMARKS", Element.ALIGN_LEFT, headerFont);
	            remarksHeader.setColspan(2);
	            mainTable.addCell(remarksHeader);

	            PdfPCell remarksData = getCell(remarks, Element.ALIGN_LEFT, dataFont);
	            remarksData.setColspan(2);
	            mainTable.addCell(remarksData);

	            document.add(mainTable);

	            // === Signature ===
	            document.add(Chunk.NEWLINE);
//	            document.add(new Paragraph("Signed by the driver " +
//	                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()), dataFont));
	            Paragraph signText = new Paragraph("Signed by the driver " + currentDate, dataFont);
	            signText.setAlignment(Element.ALIGN_CENTER);
	            document.add(signText);
	            document.add(Chunk.NEWLINE);

	            if (signaturePath != null) {
	                try {
	                    Image signImg = Image.getInstance(WEB_URL_FILE_UPLOAD + "/uploads/dvir/" + signaturePath);
	                    signImg.scaleToFit(200f, 80f);
	                    PdfPTable signTable = new PdfPTable(1);
	                    signTable.setWidthPercentage(40);
	                    PdfPCell signCell = new PdfPCell(signImg);
	                    signCell.setBorder(Rectangle.BOX);
	                    signCell.setPadding(6f);
	                    signTable.addCell(signCell);
	                    document.add(signTable);
	                } catch (Exception e) {
	                    document.add(new Paragraph("[Signature not available]", dataFont));
	                }
	            }

	            // === Page break if more days remain ===
	            document.newPage();
	        }

	        document.close();

	        finalMap.put("outputStream", outputStream);
	        finalMap.put("fileName", "dvir_log.pdf");
	        finalMap.put("subject", "DVIR Report");
	        finalMap.put("text", "This is a DVIR report");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return finalMap;
	}

	private PdfPCell getCell(String text, int alignment, Font font) {
	    PdfPCell cell = new PdfPCell(new Phrase(text, font));
	    cell.setPadding(6f);
	    cell.setHorizontalAlignment(alignment);
	    return cell;
	}

	public ResultWrapper<List<ViewDriverWorkingDayStatus>> ViewDriverWorkingDay(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<ViewDriverWorkingDayStatus>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			List<ViewDriverWorkingDayStatus> viewDriverWorkingDayStatusData = new ArrayList<>();
			ViewDriverWorkingDayStatus viewDriverWorkingDayStatus = new ViewDriverWorkingDayStatus();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			if(driverId>0) {
				String firstDate="",lastDate="";
				long lDateTime=0;
				List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,driverId);
				sDebug+=">> Size : "+driveringStatusViewDto.size()+",";
				for(int i=0;i<driveringStatusViewDto.size();i++) {
//					try {
						viewDriverWorkingDayStatus = new ViewDriverWorkingDayStatus();
				        
						Date date=new Date(driveringStatusViewDto.get(i).getLDateTime());
				        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
				        firstDate = df2.format(date);
				        sDebug+="Date : "+firstDate+",";
				        LocalDateTime ldtDateTime = LocalDateTime.parse(firstDate+" 00:00:00", formatter);
						lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						sDebug+="lDate : "+lDateTime+",";
				        if(!lastDate.equals(firstDate)) {
				        	sDebug+="here1,";
				        	viewDriverWorkingDayStatus.setDateTime(firstDate);
				        	sDebug+="here2,";
				        	viewDriverWorkingDayStatus.setLDateTime(lDateTime);
				        	sDebug+="here3,";
				        	viewDriverWorkingDayStatusData.add(viewDriverWorkingDayStatus);
				        	sDebug+="here4,";
				        }
				        lastDate = firstDate;
//					}catch(Exception ex) {
//						ex.printStackTrace();
//					}
	        		
				}
				result.setResult(viewDriverWorkingDayStatusData);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Working Day Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatus(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = null;
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String email = driveringStatusCRUDDto.getEmail();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
//			long days = Duration.between(ldtFromDate, ldtToDate).toDays();
//		    days+=1;
//		    String sDate[] = fromDate.split(" ");
//		    for(int x=0; x<days;x++) {
//		    	LocalDate lDate = LocalDate.parse(sDate[0]).plusDays(x);
////		    	System.out.println(" >> Date : "+lDate);
//		    	LocalDateTime ldtStartOfDay = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
//				LocalDateTime ldtEndOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
//				from = ldtStartOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//				to = ldtEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//		    }
		    
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
//			if(driverId>0) {
				driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,driverId);
				sDebug+=" >> "+driveringStatusViewDto.size()+" :: "+from+" : "+to+",";
				
				String onDutyTime = "",onDriveTime = "",onSleepTime = "",weeklyTime = "",onBreak = "";
				
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(from);
					driveringStatusViewDto.get(i).setToDate(to);
//					System.out.println(driverId);
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//						System.out.println(empInfo);
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						driveringStatusViewDto.get(i).setDriverName(driverName);
						driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
						driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());

						driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
						
						CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
						driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
						driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
						
						driveringStatusViewDto.get(i).setExempt(empInfo.getExempt());
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
						
						CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
						driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
						
						if(empInfo.getClientId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
							driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
							driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
						}
						
						String sDate[] = driveringStatusViewDto.get(i).getDateTime().split(" ");
						LocalDate lDate = LocalDate.parse(sDate[0]);
				    	LocalDateTime ldtStartOfDay = LocalDateTime.of(lDate, LocalTime.MIDNIGHT);
						LocalDateTime ldtEndOfDay = LocalDateTime.of(lDate, LocalTime.MAX);
						long dFrom = ldtStartOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						long dTo = ldtEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(dFrom,dTo,driveringStatusViewDto.get(i).getDriverId());
						sDebug+=" << "+certifiedLogViewDto+",";
						for(int c=0;c<certifiedLogViewDto.size();c++) {
							if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
								empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getUsername());
								driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
								driveringStatusViewDto.get(i).setCoDriverName(driverName);
							}
							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
							
							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
							
						}
						

					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					DriverWorkingStatus driverWorkingStatus = driverWorkingStatusRepo.findAndViewDriverWorkingstatusByDriverId(driveringStatusViewDto.get(i).getDriverId());
					try {
						onDutyTime = driverWorkingStatus.getOnDutyTime();
						onDriveTime = driverWorkingStatus.getOnDriveTime();
						onSleepTime = driverWorkingStatus.getOnSleepTime();
						weeklyTime = driverWorkingStatus.getWeeklyTime();
						onBreak = driverWorkingStatus.getOnBreak();
						
						onDutyTime = onDutyTime.substring(0, onDutyTime.length() - 3);
						onDriveTime = onDriveTime.substring(0, onDriveTime.length() - 3);
						onSleepTime = onSleepTime.substring(0, onSleepTime.length() - 3);
						weeklyTime = weeklyTime.substring(0, weeklyTime.length() - 3);
						onBreak = onBreak.substring(0, onBreak.length() - 3);
						
						driveringStatusViewDto.get(i).setOnDutyTime(onDutyTime);
						driveringStatusViewDto.get(i).setOnDriveTime(onDriveTime);
						driveringStatusViewDto.get(i).setOnSleepTime(onSleepTime);
						driveringStatusViewDto.get(i).setWeeklyTime(weeklyTime);
						driveringStatusViewDto.get(i).setOnBreak(onBreak);
					}catch(Exception ex) {
						ex.printStackTrace();
						driveringStatusViewDto.get(i).setOnDutyTime("14:00");
						driveringStatusViewDto.get(i).setOnDriveTime("11:00");
						driveringStatusViewDto.get(i).setOnSleepTime("10:00");
						driveringStatusViewDto.get(i).setWeeklyTime("70:00");
						driveringStatusViewDto.get(i).setOnBreak("08:00");
					}
	        	
				}
				
				
				if(!email.equals("")) {
					// send data from email
					
					List<Map<String, Object>> resultList = dispatchServiceImpl.callReportCreateMethods(driverId, from,to,"driver_log",driverName);
					for (Map<String, Object> valueMap : resultList) {
						System.out.println(">>>SendEmail: ["+ valueMap.size() +"]" +LocalDateTime.now());

						if(valueMap.size()<=0) continue;
						
						ByteArrayOutputStream outputStream = (ByteArrayOutputStream) valueMap.get("outputStream");
						String fileName = valueMap.get("fileName").toString();
						System.out.println(">>ReportName-"+ fileName +"----------------------"+LocalDateTime.now());
						String subject = valueMap.get("subject").toString();
						String text = valueMap.get("text").toString();

						// construct the text body part
						MimeBodyPart textBodyPart = new MimeBodyPart();
						textBodyPart.setText(text);

						byte[] bytes = outputStream.toByteArray();

						// construct the file body part
						DataSource dataSource = null;

						if (fileName.contains("pdf")) {
							dataSource = new ByteArrayDataSource(bytes, "application/pdf");
						} else if (fileName.contains("xlsx")) {
							dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
						}

						MimeBodyPart fileBodyPart = new MimeBodyPart();
						fileBodyPart.setDataHandler(new DataHandler(dataSource));
						fileBodyPart.setFileName(fileName);

						// construct the mime multi part
						MimeMultipart mimeMultipart = new MimeMultipart();
						mimeMultipart.addBodyPart(textBodyPart);
						mimeMultipart.addBodyPart(fileBodyPart);

						MimeMessage message = javaMailSender.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(message);
						helper.setTo(email);
						helper.setSubject(subject);
//				      	helper.addAttachment(attachmentFilename, dataSource);
						message.setContent(mimeMultipart);

						javaMailSender.send(message);
						
					}
				}
				
				result.setResult(driveringStatusViewDto);
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully"+sDebug);
//			}else {
//				result.setResult(null);
//				result.setStatus(Result.FAIL);
//				result.setMessage("Invalid Request.");
//			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> ShiftDriveringStatusWithoutUpdate(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			long lastDriverId = driveringStatusCRUDDto.getLastDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
			List<DriveringStatus> driverStatusData = driveringStatusRepo.findAndViewDriverStatusData(lastDriverId,from,to);
			sDebug+=" Size : "+driverStatusData.size()+",";
			DriveringStatus driveringStatus = null;
			for(int i=0;i<driverStatusData.size();i++) {
				driveringStatus = driverStatusData.get(i);
				driveringStatus.setDriverId(driverId);
				driveringStatus.setIsVisible(1);
				driveringStatusRepo.save(driveringStatus);
			}
				
			result.setResult("Shift Data");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Status Information Shift Successfully"+sDebug);
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusForLog(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = null;
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			long clientId = driveringStatusCRUDDto.getClientId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		    
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
//			if(driverId>0) {
				driveringStatusViewDto = lookupDriverStatusDataByClientOperation(from,to,driverId,clientId);
				sDebug+=" >> "+driveringStatusViewDto.size()+" :: "+from+" : "+to+",";
				
				String onDutyTime = "",onDriveTime = "",onSleepTime = "",weeklyTime = "",onBreak = "";
				
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(from);
					driveringStatusViewDto.get(i).setToDate(to);
//					System.out.println(driverId);
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//						System.out.println(empInfo);
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						driveringStatusViewDto.get(i).setDriverName(driverName);
						driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
						driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());

						driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
						
						CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
						driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
						driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
						
						driveringStatusViewDto.get(i).setExempt(empInfo.getExempt());
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
						
						if(mainTerminal.getStateId()>0) {
							stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
							driveringStatusViewDto.get(i).setTimezoneName(stateInfo.getTimeZone());
							driveringStatusViewDto.get(i).setTimezoneOffSet(stateInfo.getTimezoneOffSet());
						}
						
						CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
						driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
						
						if(empInfo.getClientId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
							driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
							driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
						}
						
//						String sDate[] = driveringStatusViewDto.get(i).getDateTime().split(" ");
//						LocalDate lDate = LocalDate.parse(sDate[0]);
//				    	LocalDateTime ldtStartOfDay = LocalDateTime.of(lDate, LocalTime.MIDNIGHT);
//						LocalDateTime ldtEndOfDay = LocalDateTime.of(lDate, LocalTime.MAX);
//						long dFrom = ldtStartOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//						long dTo = ldtEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//						
//						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(dFrom,dTo,driveringStatusViewDto.get(i).getDriverId());
//						sDebug+=" << "+certifiedLogViewDto+",";
//						for(int c=0;c<certifiedLogViewDto.size();c++) {
//							if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
//								empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
////								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
//								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getUsername());
//								driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
//								driveringStatusViewDto.get(i).setCoDriverName(driverName);
//							}
//							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
//							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
//							
//							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
//							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
//							
//						}
						
						LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(driveringStatusViewDto.get(i).getUtcDateTime()), TimeZone.getDefault().toZoneId());
				        LocalDateTime fromDate1 = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
				        LocalDateTime toDate1 = localDateTime.withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
				        long fromTimestamp = fromDate1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				        long toTimestamp = toDate1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(fromTimestamp,toTimestamp,driverId);
						for(int c=0;c<certifiedLogViewDto.size();c++) {
							if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
								empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getUsername());
								driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
								driveringStatusViewDto.get(i).setCoDriverName(driverName);
							}
							
							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
							
							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
							
						}

					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					DriverWorkingStatus driverWorkingStatus = driverWorkingStatusRepo.findAndViewDriverWorkingstatusByDriverId(driveringStatusViewDto.get(i).getDriverId());
					try {
						onDutyTime = driverWorkingStatus.getOnDutyTime();
						onDriveTime = driverWorkingStatus.getOnDriveTime();
						onSleepTime = driverWorkingStatus.getOnSleepTime();
						weeklyTime = driverWorkingStatus.getWeeklyTime();
						onBreak = driverWorkingStatus.getOnBreak();
						
						onDutyTime = onDutyTime.substring(0, onDutyTime.length() - 3);
						onDriveTime = onDriveTime.substring(0, onDriveTime.length() - 3);
						onSleepTime = onSleepTime.substring(0, onSleepTime.length() - 3);
						weeklyTime = weeklyTime.substring(0, weeklyTime.length() - 3);
						onBreak = onBreak.substring(0, onBreak.length() - 3);
						
						driveringStatusViewDto.get(i).setOnDutyTime(onDutyTime);
						driveringStatusViewDto.get(i).setOnDriveTime(onDriveTime);
						driveringStatusViewDto.get(i).setOnSleepTime(onSleepTime);
						driveringStatusViewDto.get(i).setWeeklyTime(weeklyTime);
						driveringStatusViewDto.get(i).setOnBreak(onBreak);
					}catch(Exception ex) {
						ex.printStackTrace();
						driveringStatusViewDto.get(i).setOnDutyTime("14:00");
						driveringStatusViewDto.get(i).setOnDriveTime("11:00");
						driveringStatusViewDto.get(i).setOnSleepTime("10:00");
						driveringStatusViewDto.get(i).setWeeklyTime("70:00");
						driveringStatusViewDto.get(i).setOnBreak("08:00");
					}
	        	
				}
				
				result.setResult(driveringStatusViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully"+sDebug);
//			}else {
//				result.setResult(null);
//				result.setStatus(Result.FAIL);
//				result.setMessage("Invalid Request.");
//			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DriveringStatusViewDto> lookupDriverStatusDataByClientOperation(long from, long to, long driverId, long clientId){
		
		MatchOperation filter = null;
		
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
					.and("clientId").is(clientId)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("clientId").is(clientId)
				);
		}
			
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId","shift","days",
				"osVersion","simCardNo","origin","utcDateTime","statusId","timezone","remainingWeeklyTime","remainingDutyTime",
				"remainingDriveTime","isReportGenerated").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.DESC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	@Override
	public ResultWrapper<EmployeeMasterCRUDDto> ViewDriveringStatusWithLoginDetails(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid) {
		String sDebug="";
		ResultWrapper<EmployeeMasterCRUDDto> result = new ResultWrapper<>();
		try {
			EmployeeMasterCRUDDto empInfo = null;
			long driverId = driveringStatusCRUDDto.getDriverId();
			Instant instant = Instant.now();
			String timezoneName="",timezoneOffSet="";
			if(driverId>0) {
				
				UserLoginDto loginData = loginRepo.GetLoginDataByEmployeeId((int)driverId);
							
		        empInfo = employeeMasterRepo.findEmployeeByEmployeeId1((int)driverId);
		        
				empInfo.setLoginDateTime(loginData.getLoginDateTime());
				
//				EmployeeMaster empDetailData = employeeMasterRepo.findByEmployeeId((int)driverId);
//				CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetailData.getCycleUsaId());
//				empInfo.setOnDutyTime(cycleUsaData.getOnDutyTime());
//				empInfo.setOnDriveTime(cycleUsaData.getOnDriveTime());
//				empInfo.setOnSleepTime(cycleUsaData.getOnSleepTime());
//				empInfo.setContinueDriveTime(cycleUsaData.getContinueDriveTime());
//				empInfo.setBreakTime(cycleUsaData.getBreakTime());
//				empInfo.setCycleRestartTime(cycleUsaData.getCycleRestartTime());

		        try {
		        	
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
					
					MACAddressMaster macAddressData = macAddressMasterRepo.findByMACAddressMasterId(driverId);
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
		        } catch(Exception ex) {
		        	ex.printStackTrace();
		        }
				  	
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
				List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,driverId);
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
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empDetails.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empDetails.getMainTerminalId());
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
						
						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driverId);
						if(certifiedLogViewDto.size()>0) {
							for(int c=0;c<certifiedLogViewDto.size();c++) {
								empDetails = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//										driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getDriverId());
								driveringStatusViewDto.get(i).setCompanyCoDriverId(empDetails.getUsername());
								driverName = empDetails.getFirstName()+" "+empDetails.getLastName();
								driveringStatusViewDto.get(i).setCoDriverName(driverName);
								driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
								driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
								
								setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
								driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
								
							}
						}else {
							driveringStatusViewDto.get(i).setTrailers(new ArrayList());
							driveringStatusViewDto.get(i).setShippingDocs(new ArrayList());
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
	        	
				}
				
				EmployeeMaster empDetailData = employeeMasterRepo.findByEmployeeId((int)driverId);
				CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetailData.getCycleUsaId());
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
				cycleTimeRules.setWarningBreakTime2((cycleUsaData.getContinueDriveTime()*hourToSec)-(cycleUsaData.getWarningTime1()*minToSec));
				cycleTimeRulesViewDto.add(cycleTimeRules);
				
//				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
//				Query query = new Query(
//				  Criteria.where("driverId").is(driverId)
//				  .and("utcDateTime").lte(to)
//				  .and("isVoilation").is(0)
//				);
//				query.limit(1);
//				query.with(pageableRequest);
//				List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
//				long lastLogUtcDataTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
//				
//				String splitStr[] = timezoneOffSet.split(":");
//				long hour = Math.abs(Long.parseLong(splitStr[0]));
//				long minutes = Math.abs(Long.parseLong(splitStr[1]));
//				String minusPlus="";
//				long timestampValue=0;
//				if(timezoneOffSet.substring(0,1).equals("-")) {
//					minusPlus = "minus";
//					timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
//				}else {
//					minusPlus = "plus";
//					timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
//				}
//				long diffInMillis = timestampValue - lastLogUtcDataTime;
//				long hours = diffInMillis / (1000 * 60 * 60);
//
//				if(hours>=34) {
//					empInfo.setRules(cycleTimeRulesViewDto);
//					
//					query = new Query();
//			        query.addCriteria(Criteria.where("employeeId").is(driverId));
//			        Update update = new Update();
//			        update.set("tokenNo", "0");
//			        mongoTemplate.findAndModify(query, update, Login.class);
//				}else {
//					empInfo.setRules(new ArrayList<>());
//				}
				
				empInfo.setDriverLog(driveringStatusViewDto);
				empInfo.setRules(cycleTimeRulesViewDto);
		        
				result.setResult(empInfo);
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Log Send Successfully");
						
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
	
	public ResultWrapper<List<DriveringStatusLogViewDto>> ViewDriveringStatusLog(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusLogViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			DecimalFormat df = new DecimalFormat("0.00");
			
			List<DriveringStatusLogViewDto> driveringStatusLogViewDto = new ArrayList<>();
			DriveringStatusLogViewDto driveringStatusLogData = new DriveringStatusLogViewDto();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="", employeeStatus="";
			if(driverId>0) {
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
				driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
				employeeStatus = empInfo.getStatus();
				
				List<LoginLog> loginLog = lookupLoginLogDataOperation(from,to,driverId,"loginDateTime");
//				sDebug+=" >> "+loginLog+",";
				for(int i=0;i<loginLog.size();i++) {
					try {
						if(loginLog.get(i).getLoginDateTime()>0L) {
							DriverStatusLog dsLog = driverStatusLogRepo.findAndViewDriverStatusLogById1(loginLog.get(i).get_id(),"Login");

//							Pageable pageableRequest1 = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
//							Query query1 = new Query(
//							  Criteria.where("driverId").is(driverId)
//							  .and("utcDateTime").lte(dsLog.getDateTime())
//							);
//							query1.limit(1);
//							query1.with(pageableRequest1);
//							List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query1, DriveringStatusViewDto.class,"drivering_status");
				            		
							driveringStatusLogData = new DriveringStatusLogViewDto();
							driveringStatusLogData.setDriverId(driverId);
							driveringStatusLogData.setDriverStatusId(loginLog.get(i).get_id());
							driveringStatusLogData.setDriverName(driverName);
							driveringStatusLogData.setLattitude(0);
							driveringStatusLogData.setLongitude(0);
							driveringStatusLogData.setCustomLocation("");
							driveringStatusLogData.setOrigin("");
//							driveringStatusLogData.setOdometer(driveringStatusViewDtoData.get(0).getOdometer());
//							driveringStatusLogData.setEngineHour(driveringStatusViewDtoData.get(0).getEngineHour());
							driveringStatusLogData.setOdometer(0);
							driveringStatusLogData.setEngineHour("0");
							driveringStatusLogData.setNote("");
							driveringStatusLogData.setIsVoilation(0);
							driveringStatusLogData.setLogType("Login");
							driveringStatusLogData.setStatusId(dsLog.getStatusId());
							driveringStatusLogData.setIsReportGenerated(1);
							driveringStatusLogData.setIsPreviousLog(0);
//							driveringStatusLogData.setIsLogDelete(0);
							driveringStatusLogData.setDateTime(String.valueOf(loginLog.get(i).getLoginDateTime()));
							driveringStatusLogData.setStatus("Login");
							driveringStatusLogData.setEmployeeStatus(employeeStatus);
//							sDebug+="1,";
							driveringStatusLogViewDto.add(driveringStatusLogData);
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				loginLog = lookupLoginLogDataOperation(from,to,driverId,"logoutDateTime");
//				sDebug+=" >> "+loginLog+",";
				for(int i=0;i<loginLog.size();i++) {
					try {
						if(loginLog.get(i).getLogoutDateTime()>0L) {
							DriverStatusLog dsLog = driverStatusLogRepo.findAndViewDriverStatusLogById1(loginLog.get(i).get_id(),"Logout");
							
//							Pageable pageableRequest1 = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
//							Query query1 = new Query(
//							  Criteria.where("driverId").is(driverId)
//							  .and("utcDateTime").lte(dsLog.getDateTime())
//							);
//							query1.limit(1);
//							query1.with(pageableRequest1);
//							List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query1, DriveringStatusViewDto.class,"drivering_status");
				            	
							driveringStatusLogData = new DriveringStatusLogViewDto();
							driveringStatusLogData.setDriverId(driverId);
							driveringStatusLogData.setDriverStatusId(loginLog.get(i).get_id());
							driveringStatusLogData.setDriverName(driverName);
							driveringStatusLogData.setLattitude(0);
							driveringStatusLogData.setLongitude(0);
							driveringStatusLogData.setCustomLocation("");
							driveringStatusLogData.setOrigin("");
//							driveringStatusLogData.setOdometer(driveringStatusViewDtoData.get(0).getOdometer());
//							driveringStatusLogData.setEngineHour(driveringStatusViewDtoData.get(0).getEngineHour());
							driveringStatusLogData.setOdometer(0);
							driveringStatusLogData.setEngineHour("0");
							driveringStatusLogData.setNote("");
							driveringStatusLogData.setIsVoilation(0);
							driveringStatusLogData.setLogType("Logout");
							driveringStatusLogData.setStatusId(dsLog.getStatusId());
							driveringStatusLogData.setIsReportGenerated(1);
							driveringStatusLogData.setIsPreviousLog(0);
//							driveringStatusLogData.setIsLogDelete(0);
							driveringStatusLogData.setDateTime(String.valueOf(loginLog.get(i).getLogoutDateTime()));
							driveringStatusLogData.setStatus("Logout");
							driveringStatusLogData.setEmployeeStatus(employeeStatus);
//							sDebug+="2,";
							driveringStatusLogViewDto.add(driveringStatusLogData);
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				List<DriveringStatusViewDto> driveringStatusViewDto = new ArrayList<>();
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
				Query query = new Query(
				  Criteria.where("driverId").is(driverId)
				  .and("utcDateTime").lte(from)
				  .and("isVoilation").is(0)
				  .and("isVisible").is(1)
//				  .and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty")
				  .and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty","PersonalUse","YardMove")
				);
				query.limit(1);
				query.with(pageableRequest);
				List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				if(driveringStatusViewDtoData.size()>0) {
					driveringStatusViewDtoData.get(0).setIsPreviousLog(1);
					driveringStatusLogData.setIsLogDelete(1);
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(0));
				} 
				
				driveringStatusViewDtoData = lookupDriverStatusDataOperation(from,to,driverId);
				for(int i=0;i<driveringStatusViewDtoData.size();i++) {
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(i));
				}
				sDebug+="List Size : "+driveringStatusViewDto.size()+"##,";
				
//				List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,driverId);
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					try {
						sDebug+="1,";
						DriverStatusLog dsLog = driverStatusLogRepo.findAndViewDriverStatusLogById(driveringStatusViewDto.get(i).get_id());
						sDebug+="2,";
						driveringStatusLogData = new DriveringStatusLogViewDto();
						driveringStatusLogData.setVehicleId(driveringStatusViewDto.get(i).getVehicleId());
						driveringStatusLogData.setDriverId(driverId);
						driveringStatusLogData.setDriverStatusId(driveringStatusViewDto.get(i).get_id());
						driveringStatusLogData.setDriverName(driverName);
						sDebug+="3,";
						driveringStatusLogData.setDateTime(String.valueOf(driveringStatusViewDto.get(i).getUtcDateTime()));
						driveringStatusLogData.setUtcDateTime(driveringStatusViewDto.get(i).getUtcDateTime());
						driveringStatusLogData.setStatus(driveringStatusViewDto.get(i).getStatus());
						sDebug+="3-1,";
						driveringStatusLogData.setLattitude(driveringStatusViewDto.get(i).getLattitude());
						sDebug+="3-2,";
						driveringStatusLogData.setLongitude(driveringStatusViewDto.get(i).getLongitude());
						sDebug+="3-3,";
						driveringStatusLogData.setCustomLocation(driveringStatusViewDto.get(i).getCustomLocation());
						sDebug+="3-4,";
						driveringStatusLogData.setOrigin(driveringStatusViewDto.get(i).getOrigin());
						sDebug+="3-5,";
						driveringStatusLogData.setOdometer(Double.parseDouble(df.format(driveringStatusViewDto.get(i).getOdometer())));
						sDebug+="3-6,";
						try {
							driveringStatusLogData.setEngineHour(df.format(Double.parseDouble(driveringStatusViewDto.get(i).getEngineHour())));
						}catch(Exception ex) {
							driveringStatusLogData.setEngineHour("0.00");
							ex.printStackTrace();
						}
						driveringStatusLogData.setNote(driveringStatusViewDto.get(i).getNote());
						sDebug+="3-7,";
						driveringStatusLogData.setIsVoilation(driveringStatusViewDto.get(i).getIsVoilation());
						driveringStatusLogData.setLogType(driveringStatusViewDto.get(i).getLogType());
						sDebug+="3-8,";
						driveringStatusLogData.setEmployeeStatus(employeeStatus);
						try {
							driveringStatusLogData.setStatusId(dsLog.getStatusId());
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						sDebug+="4,";
						driveringStatusLogData.setRemainingWeeklyTime(driveringStatusViewDto.get(i).getRemainingWeeklyTime());
						driveringStatusLogData.setRemainingDutyTime(driveringStatusViewDto.get(i).getRemainingDutyTime());
						driveringStatusLogData.setRemainingDriveTime(driveringStatusViewDto.get(i).getRemainingDriveTime());
						driveringStatusLogData.setRemainingSleepTime(driveringStatusViewDto.get(i).getRemainingSleepTime());
						driveringStatusLogData.setShift(driveringStatusViewDto.get(i).getShift());
						driveringStatusLogData.setDays(driveringStatusViewDto.get(i).getDays());
						driveringStatusLogData.setIsReportGenerated(driveringStatusViewDto.get(i).getIsReportGenerated());
						driveringStatusLogData.setIsPreviousLog(driveringStatusViewDto.get(i).getIsPreviousLog());
						driveringStatusLogData.setIsLogDelete(1);
						driveringStatusLogViewDto.add(driveringStatusLogData);
						sDebug+="5,";
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driverId);
				for(int i=0;i<certifiedLogViewDto.size();i++) {
					try {
						DriverStatusLog dsLog = driverStatusLogRepo.findAndViewDriverStatusLogById(certifiedLogViewDto.get(i).get_id());

//						Pageable pageableRequest1 = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
//						Query query1 = new Query(
//						  Criteria.where("driverId").is(driverId)
//						  .and("utcDateTime").lte(dsLog.getDateTime())
//						);
//						query1.limit(1);
//						query1.with(pageableRequest1);
//						List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query1, DriveringStatusViewDto.class,"drivering_status");
			            	
						driveringStatusLogData = new DriveringStatusLogViewDto();
						driveringStatusLogData.setVehicleId(certifiedLogViewDto.get(i).getVehicleId());
						driveringStatusLogData.setDriverId(driverId);
						driveringStatusLogData.setDriverStatusId(certifiedLogViewDto.get(i).get_id());
						driveringStatusLogData.setDriverName(driverName);
						driveringStatusLogData.setDateTime(String.valueOf(certifiedLogViewDto.get(i).getLCertifiedDate()));
						driveringStatusLogData.setStatus("Certified");
						driveringStatusLogData.setLattitude(0);
						driveringStatusLogData.setLongitude(0);
						driveringStatusLogData.setCustomLocation("");
						driveringStatusLogData.setOrigin("");
//						driveringStatusLogData.setOdometer(driveringStatusViewDtoData.get(0).getOdometer());
//						driveringStatusLogData.setEngineHour(driveringStatusViewDtoData.get(0).getEngineHour());
						driveringStatusLogData.setOdometer(0);
						driveringStatusLogData.setEngineHour("0");
						driveringStatusLogData.setNote("");
						driveringStatusLogData.setIsVoilation(0);
						driveringStatusLogData.setLogType("Certified Log");
						driveringStatusLogData.setEmployeeStatus(employeeStatus);
						driveringStatusLogData.setStatusId(dsLog.getStatusId());
						driveringStatusLogData.setIsReportGenerated(1);
						driveringStatusLogData.setIsPreviousLog(0);
//						driveringStatusLogData.setIsLogDelete(0);
						driveringStatusLogViewDto.add(driveringStatusLogData);
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				
				
				result.setResult(driveringStatusLogViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully"+sDebug);
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
		
	List<ShiftLogAddDto> shiftLogAddDtoData = new ArrayList<>();
	Boolean isVoilation=false;
	String voilationMessage="";
	public ResultWrapper<String> AssignLogToDriver(AssignLogToDriverDto assignLogToDriverDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		shiftLogAddDtoData = new ArrayList<>();
		isVoilation=false;
		voilationMessage="";
		try {
//			boolean checkDvir = assignLogToDriverDto.getCheckDvir();
			boolean checkDvir = Boolean.parseBoolean(assignLogToDriverDto.getCheckDvir());
			boolean checkCertified = Boolean.parseBoolean(assignLogToDriverDto.getCheckCertified());
			
			Instant instant = Instant.now();
			long driverId = assignLogToDriverDto.getDriverId();
			long lastDriverId=0;
			List<DriveringStatusLogViewDto> logStatusData = assignLogToDriverDto.getLogStatusData();
			int iCount=0;
			DriveringStatusLogViewDto lastLogData=null;
			long utcDateTime=0,plus15DaysTimestamp=0;
			List<DriveringStatusLogViewDto> driveringStatus=null;
			
			Map<Long, List<String>> duplicateLogs = new HashMap<>(); 
//			for (DriveringStatusLogViewDto driveringStatusLogViewDto : logStatusData) {
//			    String dateTime = driveringStatusLogViewDto.getDateTime();
//
//			    List<DriveringStatusViewDto> driverLogData = driveringStatusRepo.findAndViewDriverStatusByDate(driverId, dateTime);
//
//			    if (!driverLogData.isEmpty()) {
//			        duplicateLogs
//			            .computeIfAbsent(driverId, k -> new ArrayList<>())
//			            .add(dateTime);
//			    }
//			}

			if (!duplicateLogs.isEmpty()) {
				
			    result.setResult("Error");
			    result.setStatus(Result.FAIL);
			    StringBuilder sb = new StringBuilder("This driver logs already exist:");
			    duplicateLogs.forEach((lDriverId, dateTimes) -> {
			        sb.append(dateTimes);
			    });

			    result.setMessage(sb.toString());
			    
			}else {
				for(int i=0;i<logStatusData.size();i++) {
//					utcDateTime = Long.parseLong(logStatusData.get(i).getDateTime());
					utcDateTime = logStatusData.get(i).getUtcDateTime();
					
					LocalDateTime dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(utcDateTime),ZoneId.systemDefault());
			        // Add 15 days
			        LocalDateTime newDateTime = dateTime.plusDays(15).withHour(23).withMinute(59).withSecond(59).withNano(0);
			        // Convert back to timestamp
			        plus15DaysTimestamp = newDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

				        
					lastDriverId = logStatusData.get(i).getDriverId();
//					sDebug+="Data : "+logStatusData.get(i).getUtcDateTime()+" >> "+lastLogData+" , ";
					if(isVoilation==false) {
//						sDebug+=" 1, ";
						lastLogData = ShiftDriverLog(logStatusData.get(i),driverId,iCount,lastLogData);
					}else {
//						sDebug+=" 2, ";
						break;
					}
					if(i==0) {
						
						EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)lastDriverId);
			            CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
			            long remainingWeeklyTime = (cycleUsaData.getCycleHour()*60*60);
			            long remainingDutyTime = (cycleUsaData.getOnDutyTime()*60*60);
			            long remainingDriveTime = (cycleUsaData.getOnDutyTime()*60*60);
			            
						// log shift old driver offduty
						DriveringStatus driveringStatusLog = new DriveringStatus();
						
						Pageable pageableRequest1 = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query query1 = new Query(
						  Criteria.where("driverId").is(lastDriverId)
						  .and("utcDateTime").lt(utcDateTime)
						);
						query1.limit(1);
						query1.with(pageableRequest1);
						List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query1, DriveringStatusViewDto.class,"drivering_status");
			            LocalDate logDate =Instant.ofEpochMilli(utcDateTime).atZone(ZoneId.systemDefault()).toLocalDate();

			            ZonedDateTime utcDateTime1 = Instant.ofEpochMilli(utcDateTime).atZone(ZoneOffset.UTC);
			            // Define the date format
			            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			            // Format the date
			            String formattedDate = utcDateTime1.format(formatter);
						if(driveringStatusViewDtoData.size()>0) {
							if(driveringStatusViewDtoData.get(0).getStatus().equals("OnDuty") || driveringStatusViewDtoData.get(0).getStatus().equals("OnDrive")) {
								driveringStatusLog = new DriveringStatus();
								driveringStatusLog.setDriverId(lastDriverId);
								driveringStatusLog.setVehicleId(driveringStatusViewDtoData.get(0).getVehicleId());
								driveringStatusLog.setClientId(driveringStatusViewDtoData.get(0).getClientId());
								driveringStatusLog.setStatus("OffDuty");
								driveringStatusLog.setLattitude(driveringStatusViewDtoData.get(0).getLattitude());
								driveringStatusLog.setLongitude(driveringStatusViewDtoData.get(0).getLongitude());
								driveringStatusLog.setDateTime(formattedDate);
								
								driveringStatusLog.setLDateTime(utcDateTime);
								driveringStatusLog.setUtcDateTime(utcDateTime);
								driveringStatusLog.setLogType(driveringStatusViewDtoData.get(0).getLogType());
								driveringStatusLog.setAppVersion(driveringStatusViewDtoData.get(0).getAppVersion());
								driveringStatusLog.setOsVersion(driveringStatusViewDtoData.get(0).getOsVersion());
								driveringStatusLog.setIsVoilation(driveringStatusViewDtoData.get(0).getIsVoilation());
								driveringStatusLog.setSimCardNo(driveringStatusViewDtoData.get(0).getSimCardNo());
								driveringStatusLog.setNote(driveringStatusViewDtoData.get(0).getNote());
								driveringStatusLog.setCustomLocation(driveringStatusViewDtoData.get(0).getCustomLocation());
								driveringStatusLog.setCurrentLocation(driveringStatusViewDtoData.get(0).getCustomLocation());
								driveringStatusLog.setEngineHour(driveringStatusViewDtoData.get(0).getEngineHour());
								driveringStatusLog.setOrigin(driveringStatusViewDtoData.get(0).getOrigin());
								driveringStatusLog.setTimezone(driveringStatusViewDtoData.get(0).getTimezone());
								driveringStatusLog.setOdometer(driveringStatusViewDtoData.get(0).getOdometer());
								driveringStatusLog.setShift(driveringStatusViewDtoData.get(0).getShift());
								driveringStatusLog.setDays(driveringStatusViewDtoData.get(0).getDays());
								driveringStatusLog.setRemainingWeeklyTime(Long.parseLong(driveringStatusViewDtoData.get(0).getRemainingWeeklyTime()));
								driveringStatusLog.setRemainingDutyTime(Long.parseLong(driveringStatusViewDtoData.get(0).getRemainingDutyTime()));
								driveringStatusLog.setRemainingDriveTime(Long.parseLong(driveringStatusViewDtoData.get(0).getRemainingDriveTime()));
								driveringStatusLog.setRemainingSleepTime(Long.parseLong(driveringStatusViewDtoData.get(0).getRemainingSleepTime()));
								driveringStatusLog.setReceivedTimestamp(instant.toEpochMilli());
								driveringStatusLog.setIsVisible(1);
								driveringStatusRepo.save(driveringStatusLog);
							}
						}
						
						// old driver record
						Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query queryData = new Query(
						  Criteria.where("driverId").is(lastDriverId)
						  .and("utcDateTime").lt(utcDateTime)
						  .and("isVoilation").is(0)
						);
						queryData.limit(1);
						queryData.with(pageableRequest);
						driveringStatus = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
					
					}
					iCount++;
				}
				
				long millisecondShift = 34 * 60L * 60 * 1000;
				try {
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
		            CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
		            millisecondShift = (cycleUsaData.getCycleRestartTime()*60*60*1000);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
	            
				//------------------- new driver record------------------------------------
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query queryData = new Query(
				  Criteria.where("driverId").is(driverId)
				  .and("utcDateTime").lt(utcDateTime)
				  .and("isVoilation").is(0)
				);
				queryData.limit(1);
				queryData.with(pageableRequest);
				List<DriveringStatusLogViewDto> driveringStatusNewDriver = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
				if(driveringStatusNewDriver.size()>0) {
					lastLogData = driveringStatusNewDriver.get(0);
				}
				queryData = new Query(
				    Criteria.where("driverId").is(driverId)
				    		.and("utcDateTime").gt(utcDateTime).lte(plus15DaysTimestamp)
				            .and("isVoilation").is(0)
				);
				queryData.with(Sort.by(Sort.Direction.ASC, "utcDateTime")); // Optional sorting
				List<DriveringStatusLogViewDto> driveringStatusList = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class, "drivering_status");
//				sDebug+=" List : "+driveringStatusList.size()+" >> Data : "+driveringStatusList+",";
				for(int i=0;i<driveringStatusList.size();i++) {
//					sDebug+=" List : "+driveringStatusList.get(i)+" >> Data : "+lastLogData+",";
					long duration = driveringStatusList.get(i).getUtcDateTime() - lastLogData.getUtcDateTime();				
			            
//	                if (duration<millisecondShift) {
	                	int diffDays = driveringStatusList.get(i).getDays() - lastLogData.getDays();
	    		        if(diffDays>0) {
	                    	driveringStatusList.get(i).setDays(diffDays);
	    		        }
	                	driveringStatusList.get(i).setDriverStatusId(driveringStatusList.get(i).get_id());
	                	if(isVoilation==false) {
//	                		sDebug+=" 3, ";
	                		lastLogData = ShiftDriverLog(driveringStatusList.get(i),driverId,iCount,lastLogData);
	                	}else {
//	                		sDebug+=" 4, ";
	                		break;
	                	}
	    				iCount++;
//	                }else {
//	                	break;
//	                }
				}
				
				
				
				// --------------- old driver record----------------------
				if(driveringStatus.size()>0) {
					lastLogData = driveringStatus.get(0);
				}
				
				queryData = new Query(
				    Criteria.where("driverId").is(lastDriverId)
				            .and("utcDateTime").gt(utcDateTime).lte(plus15DaysTimestamp)
				            .and("isVoilation").is(0)
				);
				queryData.with(Sort.by(Sort.Direction.ASC, "utcDateTime")); // Optional sorting
				driveringStatusList = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class, "drivering_status");
//				sDebug+=" List : "+driveringStatusList.size()+" >> Data : "+driveringStatusList+",";
				for(int i=0;i<driveringStatusList.size();i++) {
//					sDebug+=" List : "+driveringStatusList.get(i)+" >> Data : "+lastLogData+",";
					long duration = driveringStatusList.get(i).getUtcDateTime() - lastLogData.getUtcDateTime();
//	                if (duration<millisecondShift) {
	                	int diffDays = driveringStatusList.get(i).getDays() - lastLogData.getDays();
//	                	sDebug+=" Log : "+driveringStatusList.get(i)+" >> Data : "+lastLogData+" >> Days : "+diffDays+",";
	    		        if(diffDays>0) {
	                    	driveringStatusList.get(i).setDays(diffDays);
	    		        }
	                	driveringStatusList.get(i).setDriverStatusId(driveringStatusList.get(i).get_id());
	                	if(isVoilation==false) {
//	                		sDebug+=" 5, ";
	                		lastLogData = ShiftDriverLog(driveringStatusList.get(i),lastDriverId,iCount,lastLogData);
	                	}else {
//	                		sDebug+=" 6, ";
	                		break;
	                	}
	    				iCount++;
//	                }else {
//	                	break;
//	                }
				}
				
				
//				sDebug+="Data : "+shiftLogAddDtoData+" : Voilation : "+isVoilation+",";

				long firstUtcDateTime=0, lastUtcDateTime=0;
				int logShift=0, logDays=0;
				if(isVoilation==false) {
					for(int i=0;i<shiftLogAddDtoData.size();i++) {
						if(i==0) {
							firstUtcDateTime = shiftLogAddDtoData.get(i).getUtcDateTime();
							logShift=shiftLogAddDtoData.get(i).getShift();
							logDays=shiftLogAddDtoData.get(i).getDays();
							
							// create date utcDateTime to from date and to date
							instant = Instant.ofEpochMilli(firstUtcDateTime);
					        LocalDate date = instant.atZone(ZoneOffset.UTC).toLocalDate();

					        ZonedDateTime startOfDayUTC = date.atStartOfDay(ZoneOffset.UTC);
					        ZonedDateTime endOfDayUTC = startOfDayUTC.plusDays(1).minusNanos(1);

					        long from = startOfDayUTC.toInstant().toEpochMilli();
					        long to = endOfDayUTC.toInstant().toEpochMilli();
					        if(checkDvir) {
					        	pageableRequest = PageRequest.of(0, 1, Sort.by("_id").ascending());
								Query query1 = new Query();
						        query1.addCriteria(
									Criteria.where("lDateTime").gte(from).lte(to)
									.and("driverId").is(lastDriverId)
								);
						        
						        query1.with(pageableRequest);
								List<DVIRData> dvirData = mongoTemplate.find(query1, DVIRData.class,"dvir_data");
								if(dvirData.size()>0) {
									String driverSignFile = driverId+ "_sign_1.jpg";
									Update update1 = new Update();
									update1.set("driverId", driverId);
									update1.set("driverSignFile", driverSignFile);
									mongoTemplate.updateMulti(query1, update1, DVIRData.class);
								}
					        }
							//---------------------------------------------------------
							
						}else {
							lastUtcDateTime = shiftLogAddDtoData.get(i).getUtcDateTime();
						}
						UpdateLogData(shiftLogAddDtoData.get(i).getUtcDateTime(),shiftLogAddDtoData.get(i).getLogStatusId(),shiftLogAddDtoData.get(i).getLogStatus(),shiftLogAddDtoData.get(i).getDriverId(),shiftLogAddDtoData.get(i).getDays(),shiftLogAddDtoData.get(i).getShift(),shiftLogAddDtoData.get(i).getTotalWeeklyTime(),shiftLogAddDtoData.get(i).getTotalTimeOnDuty(),shiftLogAddDtoData.get(i).getTotalTimeDrive(),shiftLogAddDtoData.get(i).getTotalTimeOnSleep(), checkCertified);
					}
//			        sDebug+=" Delete >> From : "+firstUtcDateTime+" :: "+lastUtcDateTime+" Driver : "+lastDriverId+" UTC Time: "+utcDateTime+"\n";
					
					ZonedDateTime currentDateTime = Instant.ofEpochMilli(firstUtcDateTime).atZone(ZoneId.systemDefault());
			        ZonedDateTime startOfDay = currentDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault());
			        long midnightTimestamp = startOfDay.toInstant().toEpochMilli();
			        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1_000_000);
			        long endOfDayTimestamp = endOfDay.toInstant().toEpochMilli();
			        
					driveringStatusRepo.deleteAllDriveringStatusVoilation(lastDriverId,midnightTimestamp,endOfDayTimestamp,1);

			        sDebug+=" Shift >> From : "+midnightTimestamp+" :: "+endOfDayTimestamp+" Driver : "+driverId+" :: "+logShift+" :: "+logDays+"\n";
					SaveLog(sDebug);
					
					// update sequence ID
					UpdateDriverSequenceId(driverId,firstUtcDateTime);
					
			        CheckAllVoilations(driverId,midnightTimestamp,endOfDayTimestamp,plus15DaysTimestamp,logShift,logDays);
					
					result.setResult("Updated");
					result.setStatus(Result.SUCCESS);
					result.setMessage("Bulk Log Assign To Driver Updated Successfully"+sDebug);
				}else {
					result.setResult("Voilation Generated"+""+sDebug);
					result.setStatus(Result.FAIL);
					result.setMessage(voilationMessage);
				}
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
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

	        SaveLog(sDebug);

	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void UpdateDriverSequenceId_old(long driverId, long utcDateTime) {
		String sDebug="SEQ ID : ";
		try {
			Update update = new Update();
			Query query = new Query();
	        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
	        query = new Query(
			    Criteria.where("driverId").is(driverId)
	    		.and("dateTime").lt(utcDateTime)
	            .and("isVoilation").is(0)
			);
	        query.limit(1);
	        query.with(pageableRequest);
			List<DriverStatusLog> driverStatusLog = mongoTemplate.find(query, DriverStatusLog.class, "driver_status_log");

			long statusId = 0;
			if(driverStatusLog.size()>0) {
				long dataUtcDateTime = driverStatusLog.get(0).getDateTime();
				statusId = driverStatusLog.get(0).getStatusId();
				Query queryData = new Query();
				queryData = new Query(
				    Criteria.where("dateTime").gte(dataUtcDateTime)
		            .and("driverId").is(driverId)
		            .and("isVoilation").is(0)
				);
				queryData.with(Sort.by(Sort.Direction.ASC, "dateTime"));
				List<DriverStatusLog> dsLogList = mongoTemplate.find(queryData, DriverStatusLog.class, "driver_status_log");
				for(int i=0;i<dsLogList.size();i++) {
					//update driver_status_log
					long dsLogStatusId = statusId+i;
					query = new Query();
			        query.addCriteria(
		        			Criteria.where("logDataId").is(dsLogList.get(i).getLogDataId())
		        		);
			        update = new Update();
			        update.set("statusId", dsLogStatusId);
			        mongoTemplate.updateMulti(query, update, DriverStatusLog.class);
				}
			}else {
				long dataUtcDateTime = utcDateTime;
				statusId = 1;
				Query queryData = new Query();
				queryData = new Query(
				    Criteria.where("dateTime").gte(dataUtcDateTime)
		            .and("driverId").is(driverId)
		            .and("isVoilation").is(0)
				);
				sDebug+=" >> ";
				queryData.with(Sort.by(Sort.Direction.ASC, "dateTime"));
				List<DriverStatusLog> dsLogList = mongoTemplate.find(queryData, DriverStatusLog.class, "driver_status_log");
				for(int i=0;i<dsLogList.size();i++) {
					//update driver_status_log
					long dsLogStatusId = statusId+i;
					sDebug+=" status ID : "+dsLogStatusId+" :: "+dsLogList.get(i).getStatus()+" :: "+dsLogList.get(i).getLogDataId()+"\n";
					query = new Query();
			        query.addCriteria(
		        			Criteria.where("logDataId").is(dsLogList.get(i).getLogDataId())
		        		);
			        update = new Update();
			        update.set("statusId", dsLogStatusId);
			        mongoTemplate.updateMulti(query, update, DriverStatusLog.class);
				}
				SaveLog(sDebug);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public ResultWrapper<String> ShiftHourInDriveringStatusLog(AssignLogToDriverDto assignLogToDriverDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		boolean isDataExist=false;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 	
			ZoneId zoneId = ZoneId.of("America/Los_Angeles");
			Instant instant = Instant.now();
			long hour = assignLogToDriverDto.getHour();
			String logShift = assignLogToDriverDto.getLogShift();
			long logShiftMilliSeconds = (hour*60*60*1000);
			long driverId=0;
			List<DriveringStatusLogViewDto> logStatusData = assignLogToDriverDto.getLogStatusData();
			List<DriveringStatusLogViewDto> driveringStatus=null;
			long utcDateTime=0,durationInSec=0,logShiftUtcDateTime=0;
			String dateTime="";
			long secondShift = 34 * 60L * 60;
            long secondSleep = 10 * 60L * 60;
            
			for(int i=0;i<logStatusData.size();i++) {
				utcDateTime = logStatusData.get(i).getUtcDateTime();
				LocalDateTime ldtDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(utcDateTime),ZoneId.of("America/Los_Angeles"));

				driverId = logStatusData.get(i).getDriverId();
				ObjectId objId = new ObjectId(logStatusData.get(i).getDriverStatusId());
				if(i==0) {
					Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query queryData = new Query(
					  Criteria.where("driverId").is(driverId)
					  .and("utcDateTime").lt(utcDateTime)
					  .and("isVoilation").is(0)
					);
					queryData.limit(1);
					queryData.with(pageableRequest);
					driveringStatus = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
					
					long duration = utcDateTime-driveringStatus.get(0).getUtcDateTime();
					durationInSec = duration/1000;
					
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
		            CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
		            secondShift = (cycleUsaData.getCycleRestartTime()*60*60);
		            secondSleep = (cycleUsaData.getOnSleepTime()*60*60);
				}
				
				if(durationInSec>=secondSleep && durationInSec<=secondShift) {
					if(logShift.equals("before")) {
						logShiftUtcDateTime = utcDateTime-logShiftMilliSeconds;
						ldtDateTime = ldtDateTime.minusHours(hour);
				        dateTime = ldtDateTime.format(formatter);
					}else {
						logShiftUtcDateTime = utcDateTime+logShiftMilliSeconds;
						ldtDateTime = ldtDateTime.plusHours(hour);
						dateTime = ldtDateTime.format(formatter);
					}
					// check this date data available or not
					if(i==0) {
						LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(logShiftUtcDateTime), zoneId);
				        LocalDateTime fromDate = localDateTime.withHour(0).withMinute(0).withSecond(0);
				        LocalDateTime toDate = localDateTime.withHour(23).withMinute(59).withSecond(59);
				        long fromTimestamp = fromDate.atZone(zoneId).toInstant().toEpochMilli();
				        long toTimestamp = toDate.atZone(zoneId).toInstant().toEpochMilli();
				        
				        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query queryData = new Query(
						  Criteria.where("driverId").is(driverId)
						  .and("utcDateTime").gte(fromTimestamp).lte(toTimestamp)
						  .and("isVoilation").is(0)
						);
						queryData.limit(1);
						queryData.with(pageableRequest);
						driveringStatus = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
						if(driveringStatus.size()>0) {
							isDataExist=true;
							break;
						}
					}

					if(isDataExist==false) {
						Query query = new Query();
				        query.addCriteria(
		        			Criteria.where("_id").is(objId)
		        		);
				        Update update = new Update();
				        update.set("utcDateTime", logShiftUtcDateTime);
				        update.set("lDateTime", logShiftUtcDateTime);
				        update.set("dateTime", dateTime);
				        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
					}
					
				} else {
					// shift wise
					if(logShift.equals("before")) {
						logShiftUtcDateTime = utcDateTime-logShiftMilliSeconds;
						ldtDateTime = ldtDateTime.minusHours(hour);
				        dateTime = ldtDateTime.format(formatter);
					}else {
						logShiftUtcDateTime = utcDateTime+logShiftMilliSeconds;
						ldtDateTime = ldtDateTime.plusHours(hour);
						dateTime = ldtDateTime.format(formatter);
					}
					// check this date data available or not
					if(i==0) {
						LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(logShiftUtcDateTime), zoneId);
				        LocalDateTime fromDate = localDateTime.withHour(0).withMinute(0).withSecond(0);
				        LocalDateTime toDate = localDateTime.withHour(23).withMinute(59).withSecond(59);
				        long fromTimestamp = fromDate.atZone(zoneId).toInstant().toEpochMilli();
				        long toTimestamp = toDate.atZone(zoneId).toInstant().toEpochMilli();
				        
				        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
						Query queryData = new Query(
						  Criteria.where("driverId").is(driverId)
						  .and("utcDateTime").gte(fromTimestamp).lte(toTimestamp)
						  .and("isVoilation").is(0)
						);
						queryData.limit(1);
						queryData.with(pageableRequest);
						driveringStatus = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
						if(driveringStatus.size()>0) {
							isDataExist=true;
							break;
						}
					}

					if(isDataExist==false) {
						Query query = new Query();
				        query.addCriteria(
		        			Criteria.where("_id").is(objId)
		        		);
				        Update update = new Update();
				        update.set("utcDateTime", logShiftUtcDateTime);
				        update.set("lDateTime", logShiftUtcDateTime);
				        update.set("dateTime", dateTime);
				        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
					}
				}
				
				if(isDataExist==false) {
					result.setResult("Updated");
					result.setStatus(Result.SUCCESS);
					result.setMessage("Bulk Log Assign To Driver Updated Successfully"+sDebug);
				}else {
					result.setResult("Error"+""+sDebug);
					result.setStatus(Result.FAIL);
					result.setMessage("Log already exist for the date of shifting hour.");
				}
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public DriveringStatusLogViewDto ShiftDriverLog(DriveringStatusLogViewDto logStatusData, long driverId, int i,DriveringStatusLogViewDto driveringStatus1) {
		String sDebug="";
		String logStatusId="", logStatus="";
		long utcDateTime=0, lastUtcDateTime=0;
//		sDebug+=" >> Size : "+logStatusData.size()+" :: "+driverId+",";
		long onDutyTime = 0;
        long onDriveTime = 0;
        long offDutyTime = 0;
        long onSleepTime = 0;
        
        long totalWeeklyTime=0;
        long totalTimeOnDuty=0;
        long totalTimeDrive=0;
        long totalTimeOnSleep=0;
        
        long totalWeeklyTimeDiff=0;
        long totalTimeOnDutyDiff=0;
        long totalTimeDriveDiff=0;
        long totalTimeOnSleepDiff=0;
        
        int day=0,shift=0;
        long millisecondShift=0,millisecondDays=0;
        
        EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
        String driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
        
        String lastStatus = "", driverLastStatus="", driverCurrentStatus="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        List<DriveringStatusLogViewDto> driveringStatus=null;
        
//		String splitStr[] = logStatusData.get(i).toString().split("-");
		logStatusId = logStatusData.getDriverStatusId();
		logStatus = logStatusData.getStatus();
//		utcDateTime = Long.parseLong(logStatusData.getDateTime());
		utcDateTime = logStatusData.getUtcDateTime();
		
//		sDebug+=" >> ID : "+logStatusId+",";

		ObjectId objId = new ObjectId(logStatusId);
		if(i==0) {
			// new driver record
			Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
			Query queryData = new Query(
			  Criteria.where("driverId").is(driverId)
			  .and("utcDateTime").lt(utcDateTime)
			  .and("isVoilation").is(0)
			);
			queryData.limit(1);
			queryData.with(pageableRequest);
			driveringStatus = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class,"drivering_status");
		
//			sDebug+=" >> One : "+driveringStatus1.size()+",";
			
			if(driveringStatus.size()>0) {
				driverLastStatus = driveringStatus.get(0).getStatus();
//				totalWeeklyTime = Long.parseLong(driveringStatus1.get(0).getRemainingWeeklyTime());
//				totalTimeOnDuty = Long.parseLong(driveringStatus1.get(0).getRemainingDutyTime());
//				totalTimeDrive = Long.parseLong(driveringStatus1.get(0).getRemainingDriveTime());
//				totalTimeOnSleep = Long.parseLong(driveringStatus1.get(0).getRemainingSleepTime());
				day = driveringStatus.get(0).getDays();
				shift = driveringStatus.get(0).getShift();
				lastUtcDateTime = driveringStatus.get(0).getUtcDateTime();
				
				driveringStatus1 = driveringStatus.get(0);
			}else {
				//return logStatusData;
				DriveringStatusLogViewDto dsData = new DriveringStatusLogViewDto();
				dsData.setStatus("OffDuty");
				
				EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
	            CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
	            
				dsData.setRemainingWeeklyTime(String.valueOf(cycleUsaData.getCycleHour()*60*60));
				dsData.setRemainingDutyTime(String.valueOf(cycleUsaData.getOnDutyTime()*60*60));
				dsData.setRemainingDriveTime(String.valueOf(cycleUsaData.getOnDriveTime()*60*60));
				dsData.setRemainingSleepTime(String.valueOf(cycleUsaData.getOnSleepTime()*60*60));
				dsData.setShift(0);
				dsData.setDays(0);
				day=0;
				shift=0;
				driverLastStatus="OffDuty";
				driveringStatus1 = dsData;
			}
			
		}
		
		try {
			DriveringStatusLogViewDto currentLog = driveringStatus1;
//			sDebug+=" >> Status : "+currentLog+" ,";
			
			totalWeeklyTime = Long.parseLong(currentLog.getRemainingWeeklyTime());
			totalTimeOnDuty = Long.parseLong(currentLog.getRemainingDutyTime());
			totalTimeDrive = Long.parseLong(currentLog.getRemainingDriveTime());
			totalTimeOnSleep = Long.parseLong(currentLog.getRemainingSleepTime());
			day = currentLog.getDays();
			shift = currentLog.getShift();
			
			DriveringStatusLogViewDto nextLog = (DriveringStatusLogViewDto) logStatusData;
//			sDebug+=" >> Status : "+nextLog+" ,";
			
//            Date currentDate = sdf.parse(String.valueOf(currentLog.getUtcDateTime()));
//            Date nextDate = sdf.parse(String.valueOf(nextLog.getDateTime()));
            long duration = nextLog.getUtcDateTime() - currentLog.getUtcDateTime();
            long durationInSec = duration/1000;
          
            sDebug+=" >> Duration : "+duration+"  :: "+shift+" :: "+day+" :: "+currentLog.getStatus()+",";

            if(totalWeeklyTime>0) {
            	if(currentLog.getStatus().equals("OnSleep") || currentLog.getStatus().equals("OffDuty")) {
            		totalWeeklyTimeDiff = totalWeeklyTime;
            	}else {
            		totalWeeklyTimeDiff = (totalWeeklyTime - (duration/1000));
            	}
            }else {
    	        totalWeeklyTimeDiff =Long.parseLong(currentLog.getRemainingWeeklyTime());
            }
            if(totalTimeOnDuty>0) {
            	if(currentLog.getStatus().equals("OnSleep") || currentLog.getStatus().equals("OffDuty")) {
            		totalTimeOnDutyDiff = totalTimeOnDuty;
            	}else {
            		totalTimeOnDutyDiff = (totalTimeOnDuty -(duration/1000));
            	}
            }else {
    	        totalTimeOnDutyDiff = Long.parseLong(currentLog.getRemainingDutyTime());
            }
            if(totalTimeDrive>0) {
            	if(currentLog.getStatus().equals("OnSleep") || currentLog.getStatus().equals("OffDuty")) {
            		totalTimeDriveDiff = totalTimeDrive;
            	}else if(currentLog.getStatus().equals("OnDrive")){
	    	        totalTimeDriveDiff = (totalTimeDrive -(duration/1000));
            	}else {
            		totalTimeDriveDiff = totalTimeDrive;
            	}
            }else {
            	totalTimeDriveDiff = Long.parseLong(currentLog.getRemainingDriveTime());
            }
            if(totalTimeOnSleep>0) {
            	if(currentLog.getStatus().equals("OnSleep")) {
	    	        totalTimeOnSleepDiff = (totalTimeOnSleep - (duration/1000));
            	}else {
	    	        totalTimeOnSleepDiff = totalTimeOnSleep;
            	}
            }else {
            	totalTimeOnSleepDiff = Long.parseLong(currentLog.getRemainingSleepTime());
            }
            
            long voilation8Hour = 28800L;
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentLog.getUtcDateTime()), ZoneId.systemDefault());
//            if(currentLog.getStatus().equals("OnDuty") || currentLog.getStatus().equals("OnDrive")) {
//            	if(durationInSec>=voilation8Hour) { // continue drive after 8 hours getting voilations 
//                	isVoilation=true;
//                	voilationMessage+=driverName+"("+dateTime+"), You are continuously driving for 8 hours\n";
//                }else if(totalTimeDrive<0) { //  drive after 11 hours getting voilations 
//                	isVoilation=true;
//                	voilationMessage+=driverName+"("+dateTime+"), Your drive time has been exceeded to 11 hours\n";
//                }else if(totalTimeOnDuty<0) { //  on duty after 14 hours getting voilations 
//                	isVoilation=true;
//                	voilationMessage+=driverName+"("+dateTime+"), Your onduty time has been exceeded to 14 hours\n";
//                }else if(totalWeeklyTime<0) { // cycle after 70 hours getting voilations 
//                	isVoilation=true;
//                	voilationMessage+=driverName+"("+dateTime+"), Your weekly cycle has been exceeded to 70 hours\n";
//                }
//            }
            
//	        sDebug+=" >> Time Diff : "+totalWeeklyTimeDiff+" > "+totalTimeOnDutyDiff+" > "+totalTimeDriveDiff+" > "+totalTimeOnSleepDiff+"#";;
            EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
            CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
            switch (currentLog.getStatus()) {
                case "OnDuty":
                    onDutyTime = duration;
//                    sDebug+="OnDuty,";
                    //if(totalTimeOnDutyDiff<=50400L) { 
                    	//if dureation less than 14 hours
                    	totalWeeklyTime = totalWeeklyTimeDiff;
                    	totalTimeOnDuty = totalTimeOnDutyDiff;
    					totalTimeDrive = totalTimeDriveDiff;
    					totalTimeOnSleep = totalTimeOnSleepDiff;
//                    }else {
//                    	//if dureation greater than 14 hours then getting voilation
//                    	totalWeeklyTime = totalWeeklyTimeDiff;
//                    	totalTimeOnDuty = totalTimeOnDutyDiff;
//    					totalTimeDrive = totalTimeDriveDiff;
//    					totalTimeOnSleep = totalTimeOnSleepDiff;
//                    }
                    break;
                case "OnDrive":
//                	sDebug+="OnDrive,";
                    onDriveTime = duration;
//                    if(totalTimeDriveDiff<=39600L) { 
                    	//if dureation less than 14 hours
                    	totalWeeklyTime = totalWeeklyTimeDiff;
                    	totalTimeOnDuty = totalTimeOnDutyDiff;
    					totalTimeDrive = totalTimeDriveDiff;
    					totalTimeOnSleep = totalTimeOnSleepDiff;
//                    }else {
//                    	//if dureation greater than 14 hours then getting voilation
//                    	totalWeeklyTime = totalWeeklyTimeDiff;
//                    	totalTimeOnDuty = totalTimeOnDutyDiff;
//    					totalTimeDrive = totalTimeDriveDiff;
//    					totalTimeOnSleep = totalTimeOnSleepDiff;
//                    }
                    break;
                case "OffDuty":
//                	sDebug+="OffDuty,";
                    offDutyTime = duration;
                    millisecondShift = 34 * 60L * 60 * 1000;
                    millisecondDays = 10 * 60L * 60 * 1000;
                    
    	            millisecondShift = cycleUsaData.getCycleRestartTime() * 60L * 60 * 1000;
                    millisecondDays = cycleUsaData.getOnSleepTime() * 60L * 60 * 1000;
    				
//                    sDebug+=" Diff : "+millisecondShift+" :: "+millisecondDays+" :: "+offDutyTime+" < ";
                    if (offDutyTime>=millisecondShift) {
                        // change shift and day=1
                    	totalWeeklyTime = cycleUsaData.getCycleHour()*60*60;
    					totalTimeOnDuty = cycleUsaData.getOnDutyTime()*60*60;
    					totalTimeDrive = cycleUsaData.getOnDriveTime()*60*60;
    					totalTimeOnSleep = cycleUsaData.getOnSleepTime()*60*60;
                    	shift+=1;
                    	day=1;
                    }else if (offDutyTime>=millisecondDays) {
                        //day+1
                    	day+=1;
                    	totalWeeklyTime = totalWeeklyTimeDiff;
                    	totalTimeOnDuty = cycleUsaData.getOnDutyTime()*60*60;
    					totalTimeDrive = cycleUsaData.getOnDriveTime()*60*60;
    					totalTimeOnSleep = cycleUsaData.getOnSleepTime()*60*60;
                    }else {
                    	totalWeeklyTime = totalWeeklyTimeDiff;
                    	totalTimeOnDuty = totalTimeOnDutyDiff;
    					totalTimeDrive = totalTimeDriveDiff;
    					totalTimeOnSleep = totalTimeOnSleepDiff;
                    }
                    break;
                case "OnSleep":
//                	sDebug+="OnSleep,";
                    onSleepTime = duration;
                    millisecondShift = 34 * 60L * 60 * 1000;
                    millisecondDays = 10 * 60L * 60 * 1000;
                    millisecondShift = cycleUsaData.getCycleRestartTime() * 60L * 60 * 1000;
                    millisecondDays = cycleUsaData.getOnSleepTime() * 60L * 60 * 1000;
                    if (onSleepTime>=millisecondShift) {
                        // change shift and day=1
                    	shift+=1;
                    	day=1;
    					totalWeeklyTime = cycleUsaData.getCycleHour()*60*60;
    					totalTimeOnDuty = cycleUsaData.getOnDutyTime()*60*60;
    					totalTimeDrive = cycleUsaData.getOnDriveTime()*60*60;
    					totalTimeOnSleep = cycleUsaData.getOnSleepTime()*60*60;
                    }else if (onSleepTime>=millisecondDays) {
                        //day+1
                    	day+=1;
                    	totalWeeklyTime = totalWeeklyTimeDiff;
                    	totalTimeOnDuty = cycleUsaData.getOnDutyTime()*60*60;
    					totalTimeDrive = cycleUsaData.getOnDriveTime()*60*60;
    					totalTimeOnSleep = cycleUsaData.getOnSleepTime()*60*60;
                    }else {
                    	totalWeeklyTime = totalWeeklyTimeDiff;
                    	totalTimeOnDuty = totalTimeOnDutyDiff;
    					totalTimeDrive = totalTimeDriveDiff;
    					totalTimeOnSleep = totalTimeOnSleepDiff;
                    }
                    break;
            }
            lastStatus = nextLog.getStatus();
//            sDebug+=" 1, ";
            logStatusData.setRemainingWeeklyTime(String.valueOf(totalWeeklyTime));
            logStatusData.setRemainingDutyTime(String.valueOf(totalTimeOnDuty));
            logStatusData.setRemainingDriveTime(String.valueOf(totalTimeDrive));
            logStatusData.setRemainingSleepTime(String.valueOf(totalTimeOnSleep));
            logStatusData.setShift(shift);
            logStatusData.setDays(day);
            driveringStatus1 = logStatusData;
//            sDebug+=" 2, "+driveringStatus1+",";
            ShiftLogAddDto shiftLogAddDto = new ShiftLogAddDto();
            shiftLogAddDto.setLogStatusId(logStatusId);
            shiftLogAddDto.setLogStatus(logStatus);
            shiftLogAddDto.setDriverId(driverId);
            shiftLogAddDto.setDays(day);
            shiftLogAddDto.setShift(shift);
            shiftLogAddDto.setTotalWeeklyTime(totalWeeklyTime);
            shiftLogAddDto.setTotalTimeOnDuty(totalTimeOnDuty);
            shiftLogAddDto.setTotalTimeDrive(totalTimeDrive);
            shiftLogAddDto.setTotalTimeOnSleep(totalTimeOnSleep);
            shiftLogAddDto.setUtcDateTime(utcDateTime);
            shiftLogAddDto.setDebugTime(duration);
            shiftLogAddDto.setDebugData(sDebug);
            shiftLogAddDtoData.add(shiftLogAddDto);
//            UpdateLogData(logStatusId,logStatus,driverId,day,shift,totalWeeklyTime,totalTimeOnDuty,totalTimeDrive,totalTimeOnSleep);
            sDebug+=" 3, ";
		} catch (Exception ex) {
            ex.printStackTrace();
        }
		return logStatusData;
	}
	
	public void UpdateLogData(long utcDateTime, String objId, String logStatus, long driverId, int days, int shift,long totalWeeklyTime,long totalTimeOnDuty,long totalTimeDrive,long totalTimeOnSleep, boolean checkCertified) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(objId));
		Update update = new Update();
		if(logStatus.equals("Certified")) {
			if(checkCertified) {
				String certifiedSignature = driverId+ "_sign_1.jpg";
				update.set("certifiedSignature", certifiedSignature);
				update.set("driverId", driverId);
				update.set("coDriverId", 0);
				mongoTemplate.findAndModify(query, update, CertifiedLog.class);
			}
		}else if(logStatus.equals("Login")) {
			update.set("employeeId", driverId);
			mongoTemplate.findAndModify(query, update, LoginLog.class);
		}else if(logStatus.equals("Logout")) {
			update.set("employeeId", driverId);
			mongoTemplate.findAndModify(query, update, LoginLog.class);
		}else {
			
			update.set("driverId", driverId);
			update.set("shift", shift);
			update.set("days", days);
			update.set("remainingWeeklyTime", totalWeeklyTime);
			update.set("remainingDutyTime", totalTimeOnDuty);
			update.set("remainingDriveTime", totalTimeDrive);
			update.set("remainingSleepTime", totalTimeOnSleep);
			mongoTemplate.findAndModify(query, update, DriveringStatus.class);
		}
	}
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusForGraph(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = new ArrayList<>();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String email = driveringStatusCRUDDto.getEmail();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			Instant instant = Instant.now();
			
			 LocalDate ldFrom = ldtFromDate.toLocalDate();
			 LocalDate ldCurrentDate = LocalDate.now();
			
//			LocalDate ldFromDate = ldtFromDate.toLocalDate();
//			ldFromDate = ldFromDate.minusDays(1);
//			LocalDateTime startOfDay = LocalDateTime.of(ldFromDate, LocalTime.MIDNIGHT);
//			LocalDateTime endOfDay = LocalDateTime.of(ldFromDate, LocalTime.MAX);
//			long yesterdayFrom = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//			long yesterdayTo = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			 sDebug+=">> Data :";
			List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driverId);
			ArrayList<String> sCertifiedDate = new ArrayList<>();
			for(int c=0;c<certifiedLogViewDto.size();c++) {				
				sCertifiedDate.add(certifiedLogViewDto.get(c).getCertifiedDate());
			}
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
			if(driverId>0) {
				
				String onDutyTime = "",onDriveTime = "",onSleepTime = "",weeklyTime = "",onBreak = "";
				String timezoneName="",timezoneOffSet="";
				
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
				Query query = new Query(
				  Criteria.where("driverId").is(driverId)
				  .and("utcDateTime").lte(from)
				  .and("isVoilation").is(0)
				  .and("isVisible").is(1)
//				  .and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty")
				  .and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty","PersonalUse","YardMove")
				);
				query.limit(1);
				query.with(pageableRequest);
				List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				if(driveringStatusViewDtoData.size()>0) {
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(0));
				}
				 
				driveringStatusViewDtoData = lookupDriverStatusDataForGraphOperation(from,to,driverId);
				for(int i=0;i<driveringStatusViewDtoData.size();i++) {
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(i));
				}
				
				sDebug+=">> Data1 :";
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(from);
					driveringStatusViewDto.get(i).setToDate(to);
//					System.out.println(driverId);
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//						System.out.println(empInfo);
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						driveringStatusViewDto.get(i).setDriverName(driverName);
						driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
						driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());

						driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
						
						CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
						driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
						driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
						
						if(mainTerminal.getStateId()>0) {
							stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
							timezoneName=stateInfo.getTimeZone();
							timezoneOffSet=stateInfo.getTimezoneOffSet();
							driveringStatusViewDto.get(i).setTimezoneName(timezoneName);
							driveringStatusViewDto.get(i).setTimezoneOffSet(timezoneOffSet);
						}
						
						driveringStatusViewDto.get(i).setExempt(empInfo.getExempt());
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());						
						
						CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
						driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
						
						if(empInfo.getClientId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
							driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
							driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
						}
						
//						LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(driveringStatusViewDto.get(i).getUtcDateTime()), TimeZone.getDefault().toZoneId());
//				        LocalDateTime fromDate1 = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
//				        LocalDateTime toDate1 = localDateTime.withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
//				        long fromTimestamp = fromDate1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//				        long toTimestamp = toDate1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
						String sDate[] = driveringStatusViewDto.get(i).getDateTime().split(" ");
						LocalDate lDate = LocalDate.parse(sDate[0]);
				    	LocalDateTime ldtStartOfDay = LocalDateTime.of(lDate, LocalTime.MIDNIGHT);
						LocalDateTime ldtEndOfDay = LocalDateTime.of(lDate, LocalTime.MAX);
						long fromTimestamp = ldtStartOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						long toTimestamp = ldtEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
				        sDebug+=" >> Date : "+fromTimestamp+" :: "+toTimestamp+" :: "+driverId+",";
//				        sDebug+=" >> Date LDT : "+fromDate1+" :: "+toDate1+",";
						certifiedLogViewDto = lookupCertifiedLogDataOperation(fromTimestamp,toTimestamp,driverId);
//				        List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataByDateOperation(lDate.toString(),driverId);
				        sDebug+=" Size : "+certifiedLogViewDto.size()+",";
						for(int c=0;c<certifiedLogViewDto.size();c++) {
							if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
								empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
								driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getUsername());
								driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
								driveringStatusViewDto.get(i).setCoDriverName(driverName);
							}
							sDebug+=" :: "+certifiedLogViewDto.get(c).getTrailers()+" :: "+certifiedLogViewDto.get(c).getShippingDocs()+",";
							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
							
							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
							
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					DriverWorkingStatus driverWorkingStatus = driverWorkingStatusRepo.findAndViewDriverWorkingstatusByDriverId(driveringStatusViewDto.get(i).getDriverId());
					try {
						onDutyTime = driverWorkingStatus.getOnDutyTime();
						onDriveTime = driverWorkingStatus.getOnDriveTime();
						onSleepTime = driverWorkingStatus.getOnSleepTime();
						weeklyTime = driverWorkingStatus.getWeeklyTime();
						onBreak = driverWorkingStatus.getOnBreak();
						
						onDutyTime = onDutyTime.substring(0, onDutyTime.length() - 3);
						onDriveTime = onDriveTime.substring(0, onDriveTime.length() - 3);
						onSleepTime = onSleepTime.substring(0, onSleepTime.length() - 3);
						weeklyTime = weeklyTime.substring(0, weeklyTime.length() - 3);
						onBreak = onBreak.substring(0, onBreak.length() - 3);
						
						driveringStatusViewDto.get(i).setOnDutyTime(onDutyTime);
						driveringStatusViewDto.get(i).setOnDriveTime(onDriveTime);
						driveringStatusViewDto.get(i).setOnSleepTime(onSleepTime);
						driveringStatusViewDto.get(i).setWeeklyTime(weeklyTime);
						driveringStatusViewDto.get(i).setOnBreak(onBreak);
					}catch(Exception ex) {
						ex.printStackTrace();
						driveringStatusViewDto.get(i).setOnDutyTime("14:00");
						driveringStatusViewDto.get(i).setOnDriveTime("11:00");
						driveringStatusViewDto.get(i).setOnSleepTime("10:00");
						driveringStatusViewDto.get(i).setWeeklyTime("70:00");
						driveringStatusViewDto.get(i).setOnBreak("08:00");
					}
	        	
				}
				
				pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
				query = new Query(
				  Criteria.where("driverId").is(driverId)
				  .and("utcDateTime").lte(to)
				  .and("isVoilation").is(0)
				  .and("isVisible").is(1)
//				  .and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty")
				  .and("status").in("OnDrive", "OnDuty", "OnSleep", "OffDuty","PersonalUse","YardMove")
				);
				query.limit(1);
				query.with(pageableRequest);
				driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				if(driveringStatusViewDtoData.size()>0) {
					
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDtoData.get(0).getDriverId());

					MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
					driveringStatusViewDtoData.get(0).setMainTerminalName(mainTerminal.getMainTerminalName());
					
					if(mainTerminal.getStateId()>0) {
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
						timezoneName=stateInfo.getTimeZone();
						timezoneOffSet=stateInfo.getTimezoneOffSet();;
					}
					
					String splitStr[] = timezoneOffSet.split(":");
					long hour = Math.abs(Long.parseLong(splitStr[0]));
					long minutes = Math.abs(Long.parseLong(splitStr[1]));
					int totalMinutes = (int) ((hour * 60) + minutes);
//						System.out.println(Long.parseLong(splitStr[0]));
					String minusPlus="";
					long timestampValue=0;
					if(timezoneOffSet.substring(0,1).equals("-")) {
						minusPlus = "minus";
						timestampValue = (instant.toEpochMilli()-((hour*60)+minutes)*60000);
					}else {
						minusPlus = "plus";
						timestampValue = (instant.toEpochMilli()+((hour*60)+minutes)*60000);
					}
//					String sDateTime = driveringStatusViewDtoData.get(0).getDateTime();
//					String splitDate[] = sDateTime.split(" ");
					
					Clock cl = Clock.systemUTC(); 
					Clock adjustedClock;
					if (timezoneOffSet.startsWith("-")) {
					    adjustedClock = Clock.offset(cl, Duration.ofMinutes(-totalMinutes));
					} else {
					    adjustedClock = Clock.offset(cl, Duration.ofMinutes(totalMinutes));
					}
			        LocalDate lt = LocalDate.now(adjustedClock); 

			        if(ldFrom.toString().equals(lt.toString())) {
			            sDebug += "today,";
			            driveringStatusViewDtoData.get(0).setUtcDateTime(timestampValue);
			        } else if (ldFrom.isBefore(lt)) {
			            sDebug += "past day,";
			            LocalDateTime localDateTime = LocalDateTime.parse(toDate, formatter);
			            Instant instant1 = localDateTime.toInstant(ZoneOffset.UTC);
			            long utcTimestamp = instant1.getEpochSecond() * 1000;
			            driveringStatusViewDtoData.get(0).setUtcDateTime(utcTimestamp);
			        } else {
			            sDebug += "future day,";
			            LocalDateTime localDateTime = LocalDateTime.parse(fromDate, formatter);
			            Instant instant1 = localDateTime.toInstant(ZoneOffset.UTC);
			            long utcTimestamp = instant1.getEpochSecond() * 1000;
			            driveringStatusViewDtoData.get(0).setUtcDateTime(utcTimestamp);
			        }
			        
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(0));
				}
				
				result.setResult(driveringStatusViewDto);
				result.setArrayData(sCertifiedDate);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully"+sDebug);
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	
	public List<CertifiedLogViewDto> lookupCertifiedLogDataByDateOperation(String sDate, long driverId){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("certifiedDate").is(sDate)
					.and("driverId").is(driverId)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","vehicleId","trailers","shippingDocs","coDriverId","certifiedSignature","certifiedDate","lCertifiedDate",
				"addedTimestamp","_id","certifiedAt");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage);
        List<CertifiedLogViewDto> results = mongoTemplate.aggregate(aggregation, "certified_log" , CertifiedLogViewDto.class).getMappedResults();
        return results;
		
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
				"timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusForGraphNew(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = new ArrayList<>();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String email = driveringStatusCRUDDto.getEmail();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
//			LocalDate ldFromDate = ldtFromDate.toLocalDate();
//			ldFromDate = ldFromDate.minusDays(1);
//			LocalDateTime startOfDay = LocalDateTime.of(ldFromDate, LocalTime.MIDNIGHT);
//			LocalDateTime endOfDay = LocalDateTime.of(ldFromDate, LocalTime.MAX);
//			long yesterdayFrom = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//			long yesterdayTo = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
			if(driverId>0) {
				
				String onDutyTime = "",onDriveTime = "",onSleepTime = "",weeklyTime = "",onBreak = "";
				
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query query = new Query(
				  Criteria.where("driverId").is(driverId)
				  .and("utcDateTime").lte(from)
				  
				);
				query.limit(1);
				query.with(pageableRequest);
				List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				if(driveringStatusViewDtoData.size()>0) {
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(0));
				}
				
				driveringStatusViewDtoData = lookupDriverStatusDataForGraphNewOperation(from,to,driverId);
				for(int i=0;i<driveringStatusViewDtoData.size();i++) {
					driveringStatusViewDto.add(driveringStatusViewDtoData.get(i));
				}
				
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(from);
					driveringStatusViewDto.get(i).setToDate(to);
//					System.out.println(driverId);
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//						System.out.println(empInfo);
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						driveringStatusViewDto.get(i).setDriverName(driverName);
						driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
						driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());

						driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
						
						CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
						driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
						driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
						
						driveringStatusViewDto.get(i).setExempt(empInfo.getExempt());
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
						
						CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
						driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
						
						if(empInfo.getClientId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
							driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
							driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
						}
						
						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driverId);
						for(int c=0;c<certifiedLogViewDto.size();c++) {
							empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//							driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
							driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getUsername());
							driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
							driveringStatusViewDto.get(i).setCoDriverName(driverName);
							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
							
							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
							
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					DriverWorkingStatus driverWorkingStatus = driverWorkingStatusRepo.findAndViewDriverWorkingstatusByDriverId(driveringStatusViewDto.get(i).getDriverId());
					try {
						onDutyTime = driverWorkingStatus.getOnDutyTime();
						onDriveTime = driverWorkingStatus.getOnDriveTime();
						onSleepTime = driverWorkingStatus.getOnSleepTime();
						weeklyTime = driverWorkingStatus.getWeeklyTime();
						onBreak = driverWorkingStatus.getOnBreak();
						
						onDutyTime = onDutyTime.substring(0, onDutyTime.length() - 3);
						onDriveTime = onDriveTime.substring(0, onDriveTime.length() - 3);
						onSleepTime = onSleepTime.substring(0, onSleepTime.length() - 3);
						weeklyTime = weeklyTime.substring(0, weeklyTime.length() - 3);
						onBreak = onBreak.substring(0, onBreak.length() - 3);
						
						driveringStatusViewDto.get(i).setOnDutyTime(onDutyTime);
						driveringStatusViewDto.get(i).setOnDriveTime(onDriveTime);
						driveringStatusViewDto.get(i).setOnSleepTime(onSleepTime);
						driveringStatusViewDto.get(i).setWeeklyTime(weeklyTime);
						driveringStatusViewDto.get(i).setOnBreak(onBreak);
					}catch(Exception ex) {
						ex.printStackTrace();
						driveringStatusViewDto.get(i).setOnDutyTime("14:00");
						driveringStatusViewDto.get(i).setOnDriveTime("11:00");
						driveringStatusViewDto.get(i).setOnSleepTime("10:00");
						driveringStatusViewDto.get(i).setWeeklyTime("70:00");
						driveringStatusViewDto.get(i).setOnBreak("08:00");
					}
	        	
				}
				
				result.setResult(driveringStatusViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DriveringStatusViewDto> lookupDriverStatusDataForGraphNewOperation(long from, long to, long driverId){
		
		MatchOperation filter = null;
		
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
//					.and("origin").is("Driver")
					.and("isVoilation").is(0)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
//					.and("origin").is("Driver")
					.and("isVoilation").is(0)
				);
		}
			
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType","utcDateTime",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId","osVersion","simCardNo","origin",
				"timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewVoilationReport(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
						
			String driverName="";
			if(driverId>0) {
				
				List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataOfVoilationOperation(from,to,driverId);
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(from);
					driveringStatusViewDto.get(i).setToDate(to);
//					System.out.println(driverId);
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//						System.out.println(empInfo);
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						driveringStatusViewDto.get(i).setDriverName(driverName);
						driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
						driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());

						driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
						
						CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
						driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
						driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
						
						driveringStatusViewDto.get(i).setExempt(empInfo.getExempt());
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
						
						if(mainTerminal.getStateId()>0) {
							stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
						}
						
						CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
						driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
						
						if(empInfo.getClientId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
							driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
							driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
						}
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
				}
				
				result.setResult(driveringStatusViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request."+sDebug);
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DriveringStatusViewDto> lookupDriverStatusDataOfVoilationOperation(long from, long to, long driverId){
		
		MatchOperation filter = null;
		
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
					.and("isVoilation").is(1)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("isVoilation").is(1)
				);
		}
			
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType","utcDateTime",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId","osVersion","simCardNo","origin",
				"timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusCalculation(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		try {
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			int voilation=0;
			int shift=3;
			int days=2;
			
			LocalDate periviousDate = LocalDate.now();
			periviousDate = periviousDate.minusDays(14);
			String fromDate = periviousDate.toString()+" 00:00:00";
			String toDate = LocalDate.now().toString()+" 23:59:59";

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			if(driverId>0) {
				
				List<DriveringStatusViewDto> driveringStatusViewDto = lookupDriverStatusDataForCalculationOperation(from,to,driverId,voilation,shift,days);
				
				result.setResult(driveringStatusViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DriveringStatusViewDto> lookupDriverStatusDataForCalculationOperation(long from, long to, long driverId, int voilation, int shift, int days){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
					.and("isVoilation").is(voilation)
					.and("shift").is(shift)
					.and("days").is(days)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId","osVersion","simCardNo",
				"shift","days","origin","utcDateTime","timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,sort(Direction.ASC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewUnidentifiedEvents(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = null;
			
			long vehicleId = driveringStatusCRUDDto.getVehicleId();
			long clientId = driveringStatusCRUDDto.getClientId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
//			if(vehicleId>0) {
				driveringStatusViewDto = lookupUnIdentifiedEventsOperation(from,to,vehicleId,clientId);
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(from);
					driveringStatusViewDto.get(i).setToDate(to);
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//						System.out.println(empInfo);
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						driveringStatusViewDto.get(i).setDriverName(driverName);
						driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
						driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
						driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());

						driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
						
						CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
						driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
						driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
						
						driveringStatusViewDto.get(i).setExempt(empInfo.getExempt());
						
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
						driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
						driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
						
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
						
						CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
						driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
						
						if(empInfo.getClientId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
							driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
							driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
						}
						
						List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driveringStatusViewDto.get(i).getDriverId());
						for(int c=0;c<certifiedLogViewDto.size();c++) {
							empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//							driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
							driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getUsername());
							driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
							driveringStatusViewDto.get(i).setCoDriverName(driverName);
							driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
							driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
							
							setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
							driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
							
						}
						

					}catch(Exception ex) {
						ex.printStackTrace();
					}
	        	
				}
				
				result.setResult(driveringStatusViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully");
//			}else {
//				result.setResult(null);
//				result.setStatus(Result.FAIL);
//				result.setMessage("Invalid Request.");
//			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DriveringStatusViewDto> lookupUnIdentifiedEventsOperation(long from, long to, long vehicleId, long clientId){
		
		MatchOperation filter = null;
		
		if(vehicleId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("vehicleId").is(vehicleId)
					.and("clientId").is(clientId)
					.and("isVisible").is(0)
//					.and("odometer").is(0)
//					.and("engineHour").is("0")
//					.and("lattitude").is(0)
//					.and("longitude").is(0)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("clientId").is(clientId)
					.and("isVisible").is(0)
//					.and("odometer").is(0)
//					.and("engineHour").is("0")
//					.and("lattitude").is(0)
//					.and("longitude").is(0)
				);
		}
			
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId","osVersion","simCardNo",
				"origin","utcDateTime","timezone","remainingWeeklyTime","remainingDutyTime","remainingDriveTime","days","shift");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.DESC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusByDate(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		String sDebug=" >> ";
		try {
			List<DriveringStatusViewDto> driveringStatusViewDto = null;
			List<DriveringStatusViewDto> driveringStatusViewDtoData = new ArrayList<>();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String sDateTime = driveringStatusCRUDDto.getDateTime();
			
			LocalDate currentDate = LocalDate.parse(sDateTime);
			LocalDateTime startOfDay = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT);
			LocalDateTime endOfDay = LocalDateTime.of(currentDate, LocalTime.MAX);
			long from = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long to = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
			double startOdometer=0, endOdometer=0, distance=0;
			String startEngineHour="", endEngineHour="";
			String unidentifiedLogEvent="No";
//			if(driverId>0) {
				driveringStatusViewDto = lookupDriverStatusDataOperation(from,to,driverId);
				if(driveringStatusViewDto.size()<=0) {
					Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query query = new Query(
					  Criteria.where("driverId").is(driverId)
					  .and("utcDateTime").lte(from)
					  
					);
					query.limit(1);
					query.with(pageableRequest);
					driveringStatusViewDto = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				}
				sDebug+=driveringStatusViewDto.size()+" :: "+from+" : "+to+",";
				if(driveringStatusViewDto.size()>0) {
					for(int i=0;i<driveringStatusViewDto.size();i++) {
						if(driveringStatusViewDto.get(i).getOrigin().equals("Unidentified")) {
							unidentifiedLogEvent="Yes";
						}
					}
					for(int i=0;i<1;i++) {
						driveringStatusViewDto.get(i).setFromDate(from);
						driveringStatusViewDto.get(i).setToDate(to);
						driveringStatusViewDto.get(i).setUnidentifiedLog(unidentifiedLogEvent);
//						System.out.println(driverId);
						try {
							sDebug+="1,";
							EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driveringStatusViewDto.get(i).getDriverId());
//							System.out.println(empInfo);
							driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
							driveringStatusViewDto.get(i).setDriverName(driverName);
							driveringStatusViewDto.get(i).setMobileNo(empInfo.getMobileNo());
							driveringStatusViewDto.get(i).setEmail(empInfo.getEmail());
//							driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getDriverId());
							driveringStatusViewDto.get(i).setCompanyDriverId(empInfo.getUsername());
							driveringStatusViewDto.get(i).setCdlNo(empInfo.getCdlNo());
							
							sDebug+="2,";
							CountryMaster countryInfo = countryMasterRepo.findByCountryId((int)empInfo.getCdlCountryId());
							driveringStatusViewDto.get(i).setCountryName(countryInfo.getCountryName());
							sDebug+="3,";
							StateMaster stateInfo = stateMasterRepo.findByStateId((int)empInfo.getCdlStateId());
							driveringStatusViewDto.get(i).setStateName(stateInfo.getStateName());
							String exemptStatus="No";
							if(empInfo.getExempt().equals("active")) {
								exemptStatus="Yes";
							}
							driveringStatusViewDto.get(i).setExempt(exemptStatus);
							sDebug+="4,";
							VehicleMaster vehcileInfo = null;
							if(driveringStatusViewDto.get(i).getVehicleId()>0) {
//								VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) empInfo.getTruckNo());
								vehcileInfo = vehicleMasterRepo.findByVehicleId((int) driveringStatusViewDto.get(i).getVehicleId());
								driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
								driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
							}else {
								driveringStatusViewDto.get(i).setTruckNo("");
								driveringStatusViewDto.get(i).setVin("");
							}
							sDebug+="5,";
							
							MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
							driveringStatusViewDto.get(i).setMainTerminalName(mainTerminal.getMainTerminalName());
							
							sDebug+="6,";
							CycleUsa cycleUsa = cycleUsaRepo.findByCycleUsaId((int)empInfo.getCycleUsaId());
							driveringStatusViewDto.get(i).setCycleUsaName(cycleUsa.getCycleUsaName());
							if(empInfo.getShortHaulException().equals("active")) {
								int shorHaulHour = (int)cycleUsa.getOnDutyTime()+2;
								driveringStatusViewDto.get(i).setShortHaulException(empInfo.getShortHaulException());
								driveringStatusViewDto.get(i).setException("Short Haul ("+shorHaulHour+")");
							}
							if(empInfo.getClientId()>0) {
								ClientMaster clientInfo = clientMasterRepo.findByClientId((int)empInfo.getClientId());
								driveringStatusViewDto.get(i).setCompanyName(clientInfo.getClientName());
								driveringStatusViewDto.get(i).setDotNo(clientInfo.getDotNo());
								driveringStatusViewDto.get(i).setCarrier(clientInfo.getClientName());
								countryInfo = countryMasterRepo.findByCountryId((int)clientInfo.getCountryId());
								String address = clientInfo.getStreet()+" "+clientInfo.getCity()+" "+countryInfo.getCountryName();
								driveringStatusViewDto.get(i).setMainOffice(address);
								driveringStatusViewDto.get(i).setPeriodStartingTime("00:00");
							}
							sDebug+="7,";
							List<CertifiedLogViewDto> certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driverId);
							if(certifiedLogViewDto.size()>0) {
								for(int c=0;c<certifiedLogViewDto.size();c++) {
									sDebug+="8,";
									if(certifiedLogViewDto.get(c).getCoDriverId()>0) {
										empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(c).getCoDriverId());
//										driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getDriverId());
										driveringStatusViewDto.get(i).setCompanyCoDriverId(empInfo.getEmail());
										driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
										driveringStatusViewDto.get(i).setCoDriverName(driverName);

									}
									driveringStatusViewDto.get(i).setCoDriverId(certifiedLogViewDto.get(c).getCoDriverId());

									driveringStatusViewDto.get(i).setCertifiedLogId(certifiedLogViewDto.get(c).get_id());
									driveringStatusViewDto.get(i).setTrailers(certifiedLogViewDto.get(c).getTrailers());
									driveringStatusViewDto.get(i).setShippingDocs(certifiedLogViewDto.get(c).getShippingDocs());
									sDebug+="9,";
									driveringStatusViewDto.get(i).setCertifiedSignatureName(certifiedLogViewDto.get(i).getCertifiedSignature());
									setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
									driveringStatusViewDto.get(i).setCertifiedSignature(setImagePath1);
									sDebug+="10,";
									
									vehcileInfo = vehicleMasterRepo.findByVehicleId((int) certifiedLogViewDto.get(c).getVehicleId());
									driveringStatusViewDto.get(i).setTruckNo(vehcileInfo.getVehicleNo());
									driveringStatusViewDto.get(i).setVin(vehcileInfo.getVin());
									
								}
							}else {
								driveringStatusViewDto.get(i).setTrailers(new ArrayList());
								driveringStatusViewDto.get(i).setShippingDocs(new ArrayList());
							}
							
							try {
								
								MACAddressMaster macAddressData = macAddressMasterRepo.findByMACAddressMasterId(driveringStatusViewDto.get(i).getDriverId());
								driveringStatusViewDto.get(i).setMacAddress(macAddressData.getMacAddress());
								driveringStatusViewDto.get(i).setSerialNo(macAddressData.getSerialNo());
								driveringStatusViewDto.get(i).setVersion(macAddressData.getVersion());
								driveringStatusViewDto.get(i).setModelNo(macAddressData.getModelNo());
								
							} catch(Exception ex)
							{
								ex.printStackTrace();
							}
							
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
								sDebug+="Odometer : "+startOdometer+" :: "+endOdometer+",";
								if(endOdometer>=startOdometer && (startOdometer>0 && endOdometer>0)) {
									distance = endOdometer-startOdometer;
								}else {
									distance=0;
								}
								sDebug+="Distance : "+distance+",";
								driveringStatusViewDto.get(i).setStartEngineHour(startEngineHour);
								driveringStatusViewDto.get(i).setEndEngineHour(endEngineHour);
								driveringStatusViewDto.get(i).setStartOdometer(Double.valueOf(String.format("%.2f", (startOdometer))));
								driveringStatusViewDto.get(i).setEndOdometer(Double.valueOf(String.format("%.2f", (endOdometer))));
								driveringStatusViewDto.get(i).setDistance(Double.valueOf(String.format("%.2f", (distance))));
								
							} catch(Exception ex) {
								ex.printStackTrace();
							}
							
							sDebug+="11,";
							List<ELDSettings> settings =  eldSettingsRepo.findAndViewBySettingId(1);
							if(settings.size()>0) {
								driveringStatusViewDto.get(i).setEldProvider(settings.get(0).getEldProvider());
								driveringStatusViewDto.get(i).setDiagnosticIndicator("NA");
								driveringStatusViewDto.get(i).setMalfunctionIndicator("NA");
								driveringStatusViewDto.get(i).setEldRegistrationId(settings.get(0).getEldRegistrationId());
								driveringStatusViewDto.get(i).setEldIdentifier(settings.get(0).getEldIdentifier());
							}else {
								driveringStatusViewDto.get(i).setEldProvider("gbt-usa");
								driveringStatusViewDto.get(i).setDiagnosticIndicator("NA");
								driveringStatusViewDto.get(i).setMalfunctionIndicator("NA");
								driveringStatusViewDto.get(i).setEldRegistrationId("NA");
								driveringStatusViewDto.get(i).setEldIdentifier("NA");
							}
							
							
							driveringStatusViewDtoData.add(driveringStatusViewDto.get(i));
							
						}catch(Exception ex) {
							ex.printStackTrace();
						}
		        	
					}
					
					result.setResult(driveringStatusViewDtoData);
					result.setToken(tokenValid);
					result.setStatus(Result.SUCCESS);
					result.setMessage("Driver Status Information Send Successfully"+sDebug);
					
				}else {
					result.setResult(null);
					result.setStatus(Result.FAIL);
					result.setMessage("No record found.");
				}
				
//			}else {
//				result.setResult(null);
//				result.setStatus(Result.FAIL);
//				result.setMessage("Invalid Request.");
//			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<ViewDriverLogWithDetailDto> ViewDriverLogWithDetails(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		
		ResultWrapper<ViewDriverLogWithDetailDto> result = new ResultWrapper<>();
		try {
			ViewDriverLogWithDetailDto viewDriverLogWithDetailDto = new ViewDriverLogWithDetailDto();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="";
			if(driverId>0) {
				List<DriveringStatusViewDto> driverLog = lookupDriverStatusDataOperation(from,to,driverId);
				for(int i=0;i<driverLog.size();i++) {
					driverLog.get(i).setFromDate(from);
					driverLog.get(i).setToDate(to);
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					driverLog.get(i).setDriverName(driverName);
					driverLog.get(i).setMobileNo(empInfo.getMobileNo());
					driverLog.get(i).setEmail(empInfo.getEmail());
				}
				
				viewDriverLogWithDetailDto.setDriverLog(driverLog);
				
				result.setResult(viewDriverLogWithDetailDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Log With Details Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<DriverLogWithLoginLogViewDto> ViewDriverLogWithLoginLog(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid) {
		
		ResultWrapper<DriverLogWithLoginLogViewDto> result = new ResultWrapper<>();
		try {
			DriverLogWithLoginLogViewDto driverLogWithLoginLogViewDto = new DriverLogWithLoginLogViewDto();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="";
			if(driverId>0) {
				List<DriveringStatusViewDto> driverLog = lookupDriverStatusDataOperation(from,to,driverId);
				for(int i=0;i<driverLog.size();i++) {
					driverLog.get(i).setFromDate(from);
					driverLog.get(i).setToDate(to);
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					driverLog.get(i).setDriverName(driverName);
	        		
				}
				List<LoginLog> loginLog = lookupLoginLogDataOperation(from,to,driverId,"loginDateTime");
				
				driverLogWithLoginLogViewDto.setDriverLog(driverLog);
				driverLogWithLoginLogViewDto.setLoginLog(loginLog);
				
				result.setResult(driverLogWithLoginLogViewDto);
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Log With Login Log Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<LoginLog> lookupLoginLogDataOperation(long from, long to, long driverId, String loginType){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where(loginType).gte(from).lte(to)
					.and("employeeId").is(driverId)
					.and("loginType").is("app")
				);
		
//		MatchOperation filter = Aggregation.match(
//	        new Criteria().and("employeeId").is(driverId)
//	                      .and("loginType").is("app")
//	                      .orOperator(
//	                          Criteria.where("loginDateTime").gte(from).lte(to),
//	                          Criteria.where("logoutDateTime").gte(from).lte(to)
//	                      )
//	    );
		
		ProjectionOperation projectStage = Aggregation.project(
				"employeeId","loginDateTime","logoutDateTime","receivedTimestamp","isCoDriver","_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage);
        List<LoginLog> results = mongoTemplate.aggregate(aggregation, "login_log" , LoginLog.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<LoginLogViewDto>> LoginLogForWeb(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		String sDebug=" >> ";
		ResultWrapper<List<LoginLogViewDto>> result = new ResultWrapper<>();
		try {			
			Integer userId = driveringStatusCRUDDto.getUserId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			sDebug+=userId+" :: "+fromDate+" :: "+toDate+",";
			List<LoginLogViewDto> loginLogViewDto = lookupLoginLogDataForWebOperation(from,to,userId);
			sDebug+=" Size : "+loginLogViewDto.size()+"<< ";
			for(int i=0;i<loginLogViewDto.size();i++) {
				try {
					UserMaster user = userMasterRepo.findByUserId(loginLogViewDto.get(i).getUserId());
					loginLogViewDto.get(i).setFirstName(user.getFirstName());
					loginLogViewDto.get(i).setLastName(user.getLastName());
					loginLogViewDto.get(i).setEmail(user.getEmail());
					loginLogViewDto.get(i).setMobileNo(user.getMobileNo());
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
				
			result.setResult(loginLogViewDto);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Login Log Information Send Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public List<LoginLogViewDto> lookupLoginLogDataForWebOperation(long from, long to, Integer userId){
		
		MatchOperation filter = null;
		if(userId==0) {
			filter = Aggregation.match(
					Criteria.where("loginDateTime").gte(from).lte(to)
					.and("loginType").is("web")
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("loginDateTime").gte(from).lte(to)
					.and("userId").is(userId)
					.and("loginType").is("web")
				);
		}
		
		ProjectionOperation projectStage = Aggregation.project(
				"userId","loginDateTime","logoutDateTime","receivedTimestamp","isCoDriver","_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage);
        List<LoginLogViewDto> results = mongoTemplate.aggregate(aggregation, "login_log" , LoginLogViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<String> UpdateDriverLog(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			long lastDriverId = driveringStatusCRUDDto.getLastDriverId();
			String fromDate = driveringStatusCRUDDto.getFromDate();
			String toDate = driveringStatusCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			Query query = new Query();
	        query.addCriteria(
	        			Criteria.where("lDateTime").gte(from).lte(to)
	        			.and("driverId").is(lastDriverId)
	        		);
	        Update update = new Update();
	        update.set("driverId", driverId);
	        update.set("updatedTimestamp", instant.toEpochMilli());
//	        mongoTemplate.findAndModify(query, update, DriveringStatus.class);
	        mongoTemplate.updateMulti(query, update, DriveringStatus.class);

			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Log Transfer Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateAndEnableDisableDriverLog(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			
			long driverId = driveringStatusCRUDDto.getDriverId();
			long dateTime = Long.parseLong(driveringStatusCRUDDto.getDateTime());
			int isVisible = driveringStatusCRUDDto.getIsVisible();
			String email = driveringStatusCRUDDto.getEmail();
			String driverStatusId = driveringStatusCRUDDto.getDriverStatusId();
			ObjectId objId = new ObjectId(driverStatusId);
			int logShift = driveringStatusCRUDDto.getDays();
			int logDays = driveringStatusCRUDDto.getShift();
			String logStatus = driveringStatusCRUDDto.getStatus();
			
			Query query = new Query();
	        query.addCriteria(
	        			Criteria.where("_id").is(objId)
	        			.and("driverId").is(driverId)
	        		);
	        Update update = new Update();
	        update.set("isVisible", isVisible);
	        update.set("email", email);
	        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
	        
	        query = new Query();
	        query.addCriteria(
    			Criteria.where("logDataId").is(driverStatusId)
    			.and("driverId").is(driverId)
    		);
	        update = new Update();
        	update.set("statusId", 0L);
	        update.set("isVisible", isVisible);
	        mongoTemplate.updateMulti(query, update, DriverStatusLog.class);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
	        LocalDateTime ldtDateTime = Instant.ofEpochMilli(dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();

	        LocalDateTime startOfDay = ldtDateTime.toLocalDate().atStartOfDay();
	        long midnightTimestamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        LocalDateTime endOfDay = ldtDateTime.withHour(23).withMinute(59).withSecond(59);
	        long lToDate = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        LocalDateTime ldt15DaysPlusDate = ldtDateTime.plusDays(14).withHour(23).withMinute(59).withSecond(59);
	        long lToDate15 = ldt15DaysPlusDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	        
			long nextLogUtcDateTime=0;
			Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").ascending());
    		Query queryData = new Query(
    		    Criteria.where("driverId").is(driverId)
        		.and("utcDateTime").gt(dateTime)
                .and("isVoilation").is(0)
                .and("isVisible").is(1)
    		);
    		queryData.limit(1);
    		queryData.with(pageableRequest);
    		List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
    		if(driveringStatusViewDtoData.size()>0) {
    			nextLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
    		}else {
    			nextLogUtcDateTime = lToDate;
    		}
    		
    		LocalDateTime nexLogDateTime1 = Instant.ofEpochMilli(nextLogUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDate nextLogDate1 = nexLogDateTime1.toLocalDate();
			LocalDateTime nextLogEndOfDay1 = nextLogDate1.atTime(23, 59, 59, 999_000_000);
			nextLogUtcDateTime = nextLogEndOfDay1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
    		LocalDateTime nexLogDateTime = Instant.ofEpochMilli(dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDate nextLogDate = nexLogDateTime.toLocalDate();
			LocalDateTime nextLogEndOfDay = nextLogDate.atTime(23, 59, 59, 999_000_000);
			long lNextLogEndOfDay = nextLogEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime startOfDay1 = nextLogDate.atStartOfDay();
			long lStartOfDay = startOfDay1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			boolean isVoilationDeleted=false;
			if(logStatus.equals("Certified")) {
				
			}else if(logStatus.equals("Login")) {
				
			}else if(logStatus.equals("Logout")) {
				
			}else {
				long removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,nextLogUtcDateTime,1);
				removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,lStartOfDay,lNextLogEndOfDay,1);
			}
		
	        ZoneId zone = ZoneId.systemDefault();
            LocalDate date1 = Instant.ofEpochMilli(midnightTimestamp).atZone(zone).toLocalDate();
            LocalDate date2 = Instant.ofEpochMilli(lNextLogEndOfDay).atZone(zone).toLocalDate();
            LocalDate date3 = Instant.ofEpochMilli(nextLogUtcDateTime).atZone(zone).toLocalDate();
            if (date1.isBefore(date3)) {
            	CheckAllVoilations(driverId,midnightTimestamp,nextLogUtcDateTime,lToDate15,logShift,logDays);
            } else {
                LocalDate newMidnightDate = date3;
                long newMidnightTimestamp = newMidnightDate.atStartOfDay(zone).toInstant().toEpochMilli();

                LocalDateTime endOfDay1 = date1.atTime(23, 59, 59, 999_000_000);
                long newEndOfDayTimestamp = endOfDay1.atZone(zone).toInstant().toEpochMilli();

                CheckAllVoilations(driverId,newMidnightTimestamp,newEndOfDayTimestamp,lToDate15,logShift,logDays);
            }
            
	        UpdateDriverSequenceId(driverId,dateTime);
	        
			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Log Transfer Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateAndShiftDriverLog(DriveringStatusLogViewDto driveringStatusLogViewDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			Instant instant = Instant.now();
			long remainingDutyTime=0, remainingDriveTime=0, remainingWeeklyTime=0,remainingSleepTime=0;
					
			String driverStatusId = driveringStatusLogViewDto.getDriverStatusId();
			ObjectId logStatusId = new ObjectId(driverStatusId);
			String dateTime = driveringStatusLogViewDto.getDateTime();
			long driverId = driveringStatusLogViewDto.getDriverId();
			long totalSeconds = driveringStatusLogViewDto.getTotalSeconds();
			long lastUtcDateTime = driveringStatusLogViewDto.getLastUtcDateTime();
			String logStatus = driveringStatusLogViewDto.getStatus();
			int logShift = driveringStatusLogViewDto.getShift();
			int logDays = driveringStatusLogViewDto.getDays();
//			String isDvirShift = driveringStatusLogViewDto.getIsDvirShift();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtDateTime = LocalDateTime.parse(dateTime, formatter);
			long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        LocalDateTime startOfDay = ldtDateTime.toLocalDate().atStartOfDay();
	        long midnightTimestamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(dateTime, formatter);
			LocalDateTime endOfDay = ldtToDate.withHour(23).withMinute(59).withSecond(59);
			long lToDate = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldt15DaysPlusDate = LocalDateTime.parse(dateTime, formatter);
			ldt15DaysPlusDate = ldt15DaysPlusDate.plusDays(14);
			LocalDateTime endOfDay15 = ldt15DaysPlusDate.withHour(23).withMinute(59).withSecond(59);
			long lToDate15 = endOfDay15.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtAdd8HourDateTime = LocalDateTime.parse(dateTime, formatter);
			ldtAdd8HourDateTime = ldtAdd8HourDateTime.plusHours(8);
			long l8HourDateTime = ldtAdd8HourDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
//			List<DriveringStatusViewDto> driverLogData = driveringStatusRepo.findAndViewDriverStatusByDate(driverId,dateTime);
//			if(driverLogData.size()>0) {
//				result.setResult("Error");
//				result.setStatus(Result.FAIL);
//				result.setMessage("This date time have allready exist log.");
//			}else {
				long nextLogUtcDateTime=0;
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").ascending());
	    		Query queryData = new Query(
	    		    Criteria.where("driverId").is(driverId)
	        		.and("utcDateTime").gt(lastUtcDateTime)
	                .and("isVoilation").is(0)
	    		);
	    		queryData.limit(1);
	    		queryData.with(pageableRequest);
	    		List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
	    		if(driveringStatusViewDtoData.size()>0) {
	    			nextLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
	    		}else {
	    			nextLogUtcDateTime = lToDate;
	    		}
	    		
	    		LocalDateTime nexLogDateTime1 = Instant.ofEpochMilli(nextLogUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
				LocalDate nextLogDate1 = nexLogDateTime1.toLocalDate();
				LocalDateTime nextLogEndOfDay1 = nextLogDate1.atTime(23, 59, 59, 999_000_000);
				nextLogUtcDateTime = nextLogEndOfDay1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
	    		LocalDateTime nexLogDateTime = Instant.ofEpochMilli(lastUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
				LocalDate nextLogDate = nexLogDateTime.toLocalDate();
				LocalDateTime nextLogEndOfDay = nextLogDate.atTime(23, 59, 59, 999_000_000);
				long lNextLogEndOfDay = nextLogEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				LocalDateTime startOfDay1 = nextLogDate.atStartOfDay();
				long lStartOfDay = startOfDay1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				boolean isVoilationDeleted=false;
				if(logStatus.equals("Certified")) {
					
				}else if(logStatus.equals("Login")) {
					
				}else if(logStatus.equals("Logout")) {
					
				}else {
					long removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,nextLogUtcDateTime,1);
					removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,lStartOfDay,lNextLogEndOfDay,1);
				}
				// 8 hour voilation deleted    
//				long removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,l8HourDateTime,1);
			
				LocalDateTime ldtAdd11HourDateTime = LocalDateTime.parse(dateTime, formatter);
				ldtAdd11HourDateTime = ldtAdd11HourDateTime.plusHours(11);
				long l11HourDateTime = ldtAdd11HourDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				// 11 hour voilation deleted
//				removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,l11HourDateTime,1);

				LocalDateTime ldtAdd14HourDateTime = LocalDateTime.parse(dateTime, formatter);
				ldtAdd14HourDateTime = ldtAdd14HourDateTime.plusHours(14);
				long l14HourDateTime = ldtAdd14HourDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				// 14 hour voilation deleted
//				removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,l14HourDateTime,1);
								
				String sAddress = GetGoogleAddress(driveringStatusLogViewDto.getLattitude(),driveringStatusLogViewDto.getLongitude());
				
				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(logStatusId));
				Update update = new Update();
				if(logStatus.equals("Certified")) {
					LocalDate dateOnly = ldtDateTime.toLocalDate();
					update.set("certifiedDate", dateOnly.toString());
					update.set("lCertifiedDate", lDateTime);
					update.set("certifiedDateTime", lDateTime);
					mongoTemplate.findAndModify(query, update, CertifiedLog.class);
				}else if(logStatus.equals("Login")) {
					update.set("loginDateTime", lDateTime);
					mongoTemplate.findAndModify(query, update, LoginLog.class);
				}else if(logStatus.equals("Logout")) {
					update.set("logoutDateTime", lDateTime);
					mongoTemplate.findAndModify(query, update, LoginLog.class);
				}
				
				query = new Query();
		        query.addCriteria(
		        			Criteria.where("_id").is(logStatusId)
		        		);
		        update = new Update();
		        update.set("dateTime", dateTime);
		        update.set("lDateTime", lDateTime);
		        update.set("utcDateTime", lDateTime);
		        update.set("status", driveringStatusLogViewDto.getStatus());
		        update.set("origin", driveringStatusLogViewDto.getOrigin());
		        update.set("lattitude", driveringStatusLogViewDto.getLattitude());
		        update.set("longitude", driveringStatusLogViewDto.getLongitude());
		        update.set("customLocation", sAddress);
		        update.set("currentLocation", sAddress);
		        update.set("odometer", driveringStatusLogViewDto.getOdometer());
		        update.set("engineHour", driveringStatusLogViewDto.getEngineHour());
		        update.set("note", driveringStatusLogViewDto.getNote());
//		        mongoTemplate.findAndModify(query, update, DriveringStatus.class);
		        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
		 		
		        // update driver status log
		        query = new Query();
		        query.addCriteria(
	    			Criteria.where("logDataId").is(driverStatusId)
	    		);
		        update = new Update();
		        update.set("status", driveringStatusLogViewDto.getStatus());
		        update.set("lattitude", driveringStatusLogViewDto.getLattitude());
		        update.set("longitude", driveringStatusLogViewDto.getLongitude());
		        update.set("dateTime", lDateTime);
		        update.set("odometer", driveringStatusLogViewDto.getOdometer());
		        update.set("engineHour", driveringStatusLogViewDto.getEngineHour());
		        update.set("note", driveringStatusLogViewDto.getNote());
		        mongoTemplate.updateMulti(query, update, DriverStatusLog.class);
		        
//		        if(isDvirShift.equals("true")) {
//		        	List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(lStartOfDay,lNextLogEndOfDay,driverId);
//			        if (dvirDataViewDto.size()>0) {
//			            //update dvir datetime
//			            ObjectId objId = new ObjectId(dvirDataViewDto.get(0).get_id());
//			            query = new Query();
//						query.addCriteria(Criteria.where("_id").is(objId));
//				        update = new Update();
//				        update.set("dateTime", dateTime);
//				        update.set("lDateTime", lDateTime);
//				        mongoTemplate.findAndModify(query, update, DVIRData.class);
//			        }
//		        }
		        
				EditDriverLog(driverId,lDateTime,midnightTimestamp,lToDate,lToDate15,totalSeconds,lastUtcDateTime,logStatus,logShift,logDays);
	            
	            ZoneId zone = ZoneId.systemDefault();
	            LocalDate date1 = Instant.ofEpochMilli(midnightTimestamp).atZone(zone).toLocalDate();
	            LocalDate date2 = Instant.ofEpochMilli(lNextLogEndOfDay).atZone(zone).toLocalDate();
	            LocalDate date3 = Instant.ofEpochMilli(nextLogUtcDateTime).atZone(zone).toLocalDate();
//	            if (date1.isBefore(date3)) {
//	            	CheckAllVoilations(driverId,midnightTimestamp,nextLogUtcDateTime,lToDate15,logShift,logDays);
//	            } else {
//	                LocalDate newMidnightDate = date3;
//	                long newMidnightTimestamp = newMidnightDate.atStartOfDay(zone).toInstant().toEpochMilli();
//
//	                LocalDateTime endOfDay1 = date1.atTime(23, 59, 59, 999_000_000);
//	                long newEndOfDayTimestamp = endOfDay1.atZone(zone).toInstant().toEpochMilli();
//
//	                CheckAllVoilations(driverId,newMidnightTimestamp,newEndOfDayTimestamp,lToDate15,logShift,logDays);
//	            }
	            
	            CheckAllVoilations(driverId,midnightTimestamp,nextLogUtcDateTime,lToDate15,logShift,logDays);
	            
	            if (date1.isBefore(date2)) {
	                CheckAndUpdateDVIR(driverId, midnightTimestamp, lNextLogEndOfDay);
	            } else {
	                LocalDate newMidnightDate = date2;
	                long newMidnightTimestamp = newMidnightDate.atStartOfDay(zone).toInstant().toEpochMilli();

	                LocalDateTime endOfDay1 = date1.atTime(23, 59, 59, 999_000_000);
	                long newEndOfDayTimestamp = endOfDay1.atZone(zone).toInstant().toEpochMilli();

	                CheckAndUpdateDVIR(driverId, newMidnightTimestamp, newEndOfDayTimestamp);
	            }
	            
		        UpdateDriverSequenceId(driverId,lDateTime);
	            
				result.setResult("Updated");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Log Transfer Successfully"+sDebug);
//			}
			
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateAndShiftDriverLogInBulk(AssignLogToDriverDto assignLogToDriverDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			Instant instant = Instant.now();
			long remainingDutyTime=0, remainingDriveTime=0, remainingWeeklyTime=0,remainingSleepTime=0;
			
			List<DriveringStatusLogViewDto> logStatusData = assignLogToDriverDto.getLogStatusData();
			int iCount=0;
			DriveringStatusLogViewDto lastLogData=null;
			long utcDateTime=0,plus15DaysTimestamp=0;
			
			Map<Long, List<String>> duplicateLogs = new HashMap<>(); // driverId -> list of duplicate dateTimes

//			for (DriveringStatusLogViewDto driveringStatusLogViewDto : logStatusData) {
//			    Long driverId = driveringStatusLogViewDto.getDriverId();
//			    String dateTime = driveringStatusLogViewDto.getDateTime();
//
//			    List<DriveringStatusViewDto> driverLogData = driveringStatusRepo.findAndViewDriverStatusByDate(driverId, dateTime);
//
//			    if (!driverLogData.isEmpty()) {
//			        duplicateLogs
//			            .computeIfAbsent(driverId, k -> new ArrayList<>())
//			            .add(dateTime);
//			    }
//			}

			if (!duplicateLogs.isEmpty()) {
			    result.setResult("Error");
			    result.setStatus(Result.FAIL);
			    StringBuilder sb = new StringBuilder("This driver logs already exist:");
			    duplicateLogs.forEach((driverId, dateTimes) -> {
			        sb.append(dateTimes);
			    });

			    result.setMessage(sb.toString());
			    
			}else {
				List<DriveringStatusLogViewDto> driveringStatus=null;
				for(int i=0;i<logStatusData.size();i++) {
					DriveringStatusLogViewDto driveringStatusLogViewDto = logStatusData.get(i);
					
					String driverStatusId = driveringStatusLogViewDto.getDriverStatusId();
					ObjectId logStatusId = new ObjectId(driverStatusId);
					String dateTime = driveringStatusLogViewDto.getDateTime();
					long driverId = driveringStatusLogViewDto.getDriverId();
					long totalSeconds = driveringStatusLogViewDto.getTotalSeconds();
					long lastUtcDateTime = driveringStatusLogViewDto.getLastUtcDateTime();
					String logStatus = driveringStatusLogViewDto.getStatus();
					int logShift = driveringStatusLogViewDto.getShift();
					int logDays = driveringStatusLogViewDto.getDays();
					String isDvirShift = driveringStatusLogViewDto.getIsDvirShift();
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime ldtDateTime = LocalDateTime.parse(dateTime, formatter);
					long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

			        LocalDateTime startOfDay = ldtDateTime.toLocalDate().atStartOfDay();
			        long midnightTimestamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					LocalDateTime ldtToDate = LocalDateTime.parse(dateTime, formatter);
					LocalDateTime endOfDay = ldtToDate.withHour(23).withMinute(59).withSecond(59);
					long lToDate = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					LocalDateTime ldt15DaysPlusDate = LocalDateTime.parse(dateTime, formatter);
					ldt15DaysPlusDate = ldt15DaysPlusDate.plusDays(14);
					LocalDateTime endOfDay15 = ldt15DaysPlusDate.withHour(23).withMinute(59).withSecond(59);
					long lToDate15 = endOfDay15.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					LocalDateTime ldtAdd8HourDateTime = LocalDateTime.parse(dateTime, formatter);
					ldtAdd8HourDateTime = ldtAdd8HourDateTime.plusHours(8);
					long l8HourDateTime = ldtAdd8HourDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					long nextLogUtcDateTime=0;
					Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").ascending());
		    		Query queryData = new Query(
		    		    Criteria.where("driverId").is(driverId)
		        		.and("utcDateTime").gt(lastUtcDateTime)
		                .and("isVoilation").is(0)
		    		);
		    		queryData.limit(1);
		    		queryData.with(pageableRequest);
		    		List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
		    		if(driveringStatusViewDtoData.size()>0) {
		    			nextLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
		    		}else {
		    			nextLogUtcDateTime = lToDate;
		    		}
		    		
		    		LocalDateTime nexLogDateTime1 = Instant.ofEpochMilli(nextLogUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDate nextLogDate1 = nexLogDateTime1.toLocalDate();
					LocalDateTime nextLogEndOfDay1 = nextLogDate1.atTime(23, 59, 59, 999_000_000);
					nextLogUtcDateTime = nextLogEndOfDay1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
		    		LocalDateTime nexLogDateTime = Instant.ofEpochMilli(lastUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDate nextLogDate = nexLogDateTime.toLocalDate();
					LocalDateTime nextLogEndOfDay = nextLogDate.atTime(23, 59, 59, 999_000_000);
					long lNextLogEndOfDay = nextLogEndOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					LocalDateTime startOfDay1 = nextLogDate.atStartOfDay();
					long lStartOfDay = startOfDay1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					boolean isVoilationDeleted=false;
					if(logStatus.equals("Certified")) {
						
					}else if(logStatus.equals("Login")) {
						
					}else if(logStatus.equals("Logout")) {
						
					}else {
						long removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,nextLogUtcDateTime,1);
						removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,lStartOfDay,lNextLogEndOfDay,1);
					}
					// 8 hour voilation deleted    
//					long removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,l8HourDateTime,1);
				
					LocalDateTime ldtAdd11HourDateTime = LocalDateTime.parse(dateTime, formatter);
					ldtAdd11HourDateTime = ldtAdd11HourDateTime.plusHours(11);
					long l11HourDateTime = ldtAdd11HourDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					// 11 hour voilation deleted
//					removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,l11HourDateTime,1);

					LocalDateTime ldtAdd14HourDateTime = LocalDateTime.parse(dateTime, formatter);
					ldtAdd14HourDateTime = ldtAdd14HourDateTime.plusHours(14);
					long l14HourDateTime = ldtAdd14HourDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					
					// 14 hour voilation deleted
//					removeCount = driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,midnightTimestamp,l14HourDateTime,1);
									
					String sAddress = GetGoogleAddress(driveringStatusLogViewDto.getLattitude(),driveringStatusLogViewDto.getLongitude());
					
					Query query = new Query();
					query.addCriteria(Criteria.where("_id").is(logStatusId));
					Update update = new Update();
					if(logStatus.equals("Certified")) {
						LocalDate dateOnly = ldtDateTime.toLocalDate();
						update.set("certifiedDate", dateOnly.toString());
						update.set("lCertifiedDate", lDateTime);
						update.set("certifiedDateTime", lDateTime);
						mongoTemplate.findAndModify(query, update, CertifiedLog.class);
					}else if(logStatus.equals("Login")) {
						update.set("loginDateTime", lDateTime);
						mongoTemplate.findAndModify(query, update, LoginLog.class);
					}else if(logStatus.equals("Logout")) {
						update.set("logoutDateTime", lDateTime);
						mongoTemplate.findAndModify(query, update, LoginLog.class);
					}
					
					query = new Query();
			        query.addCriteria(
			        			Criteria.where("_id").is(logStatusId)
			        		);
			        update = new Update();
			        update.set("dateTime", dateTime);
			        update.set("lDateTime", lDateTime);
			        update.set("utcDateTime", lDateTime);
			        update.set("status", driveringStatusLogViewDto.getStatus());
			        update.set("origin", driveringStatusLogViewDto.getOrigin());
			        update.set("lattitude", driveringStatusLogViewDto.getLattitude());
			        update.set("longitude", driveringStatusLogViewDto.getLongitude());
			        update.set("customLocation", sAddress);
			        update.set("currentLocation", sAddress);
			        update.set("odometer", driveringStatusLogViewDto.getOdometer());
			        update.set("engineHour", driveringStatusLogViewDto.getEngineHour());
			        update.set("note", driveringStatusLogViewDto.getNote());
//			        mongoTemplate.findAndModify(query, update, DriveringStatus.class);
			        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
			 		
			        // update driver status log
			        query = new Query();
			        query.addCriteria(
		    			Criteria.where("logDataId").is(driverStatusId)
		    		);
			        update = new Update();
			        update.set("status", driveringStatusLogViewDto.getStatus());
			        update.set("lattitude", driveringStatusLogViewDto.getLattitude());
			        update.set("longitude", driveringStatusLogViewDto.getLongitude());
			        update.set("dateTime", lDateTime);
			        update.set("odometer", driveringStatusLogViewDto.getOdometer());
			        update.set("engineHour", driveringStatusLogViewDto.getEngineHour());
			        update.set("note", driveringStatusLogViewDto.getNote());
			        mongoTemplate.updateMulti(query, update, DriverStatusLog.class);
			        
			        if(isDvirShift.equals("true")) {
			        	List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(lStartOfDay,lNextLogEndOfDay,driverId);
				        if (dvirDataViewDto.size()>0) {
				            //update dvir datetime
				            ObjectId objId = new ObjectId(dvirDataViewDto.get(0).get_id());
				            query = new Query();
							query.addCriteria(Criteria.where("_id").is(objId));
					        update = new Update();
					        update.set("dateTime", dateTime);
					        update.set("lDateTime", lDateTime);
					        mongoTemplate.findAndModify(query, update, DVIRData.class);
				        }
			        }
			        
					EditDriverLog(driverId,lDateTime,midnightTimestamp,lToDate,lToDate15,totalSeconds,lastUtcDateTime,logStatus,logShift,logDays);
//		            CheckAllVoilations(driverId,midnightTimestamp,nextLogUtcDateTime,lToDate15,logShift,logDays);
		            
		            ZoneId zone = ZoneId.systemDefault();
		            LocalDate date1 = Instant.ofEpochMilli(midnightTimestamp).atZone(zone).toLocalDate();
		            LocalDate date2 = Instant.ofEpochMilli(lNextLogEndOfDay).atZone(zone).toLocalDate();
		            LocalDate date3 = Instant.ofEpochMilli(nextLogUtcDateTime).atZone(zone).toLocalDate();
		            if (date1.isBefore(date3)) {
		            	CheckAllVoilations(driverId,midnightTimestamp,nextLogUtcDateTime,lToDate15,logShift,logDays);
		            } else {
		                LocalDate newMidnightDate = date3;
		                long newMidnightTimestamp = newMidnightDate.atStartOfDay(zone).toInstant().toEpochMilli();

		                LocalDateTime endOfDay1 = date1.atTime(23, 59, 59, 999_000_000);
		                long newEndOfDayTimestamp = endOfDay1.atZone(zone).toInstant().toEpochMilli();

		                CheckAllVoilations(driverId,newMidnightTimestamp,newEndOfDayTimestamp,lToDate15,logShift,logDays);
		            }
		            
		            if (date1.isBefore(date2)) {
		                CheckAndUpdateDVIR(driverId, midnightTimestamp, lNextLogEndOfDay);
		            } else {
		                LocalDate newMidnightDate = date2;
		                long newMidnightTimestamp = newMidnightDate.atStartOfDay(zone).toInstant().toEpochMilli();

		                LocalDateTime endOfDay1 = date1.atTime(23, 59, 59, 999_000_000);
		                long newEndOfDayTimestamp = endOfDay1.atZone(zone).toInstant().toEpochMilli();

		                CheckAndUpdateDVIR(driverId, newMidnightTimestamp, newEndOfDayTimestamp);
		            }
		            
			        UpdateDriverSequenceId(driverId,lDateTime);
					
				}
				
				result.setResult("Updated");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Log Transfer Successfully"+sDebug);
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public void CheckAndUpdateDVIR(long driverId,long from,long to){
		List<DriveringStatusLogViewDto> driveringStatusList = loockupDriveringStatusData(driverId,from,to);
		Set<LocalDate> processedDates = new HashSet<>();
		
		for (int i = 0; i < driveringStatusList.size(); i++) {
			
			if (!"ONDRIVE".equalsIgnoreCase(driveringStatusList.get(i).getStatus())) continue;

			long utcDateTime = driveringStatusList.get(i).getUtcDateTime();
			String formattedTime = Instant.ofEpochMilli(utcDateTime).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			
	        LocalDate logDate = Instant.ofEpochMilli(utcDateTime).atZone(ZoneId.systemDefault()).toLocalDate();
	        
	        long startOfDayMillis = logDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	        long endOfDayMillis = logDate.atTime(23, 59, 59, 999_000_000).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        if (processedDates.contains(logDate)) continue;
	        
	        List<DVIRDataCRUDDto> dvirDataViewDto = lookupDVIRDataOperation(startOfDayMillis,endOfDayMillis,driverId);
	        if (dvirDataViewDto.size()>0) {
	            processedDates.add(logDate); 
	            //update dvir datetime
	            ObjectId objId = new ObjectId(dvirDataViewDto.get(0).get_id());
	            Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(objId));
		        Update update = new Update();
		        update.set("dateTime", formattedTime);
		        update.set("lDateTime", utcDateTime);
		        mongoTemplate.findAndModify(query, update, DVIRData.class);
	        }
		}
	}
	
	public void SaveVoilations(DriveringStatusLogViewDto driveringStatusViewDto,String logType,int voilationHour,long voilationUtcDateTime) {
		String sDebug="";
		try {
			
			Instant instant = Instant.now();

			LocalDateTime ldtDateTime = Instant.ofEpochMilli(voilationUtcDateTime).atZone(ZoneOffset.UTC).toLocalDateTime();
			String dateTime = ldtDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			
			long remainingDutyTime = Long.parseLong(driveringStatusViewDto.getRemainingDutyTime());
			long remainingWeeklyTime = Long.parseLong(driveringStatusViewDto.getRemainingWeeklyTime());
			long remainingDriveTime = Long.parseLong(driveringStatusViewDto.getRemainingDriveTime());
			long remainingSleepTime = Long.parseLong(driveringStatusViewDto.getRemainingSleepTime());
			
			sDebug+=" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
			SaveLog(sDebug);
			DriveringStatus driveringStatus = new DriveringStatus();
			
			driveringStatus.setDriverId(driveringStatusViewDto.getDriverId());
			driveringStatus.setVehicleId(driveringStatusViewDto.getVehicleId());
			driveringStatus.setClientId(driveringStatusViewDto.getClientId());
			driveringStatus.setStatus("Voilation");
			driveringStatus.setLattitude(0);
			driveringStatus.setLongitude(0);
			driveringStatus.setDateTime(dateTime);
			driveringStatus.setLDateTime(voilationUtcDateTime);
			driveringStatus.setUtcDateTime(voilationUtcDateTime);
			driveringStatus.setLogType(logType);
			driveringStatus.setAppVersion("1.0");
			driveringStatus.setOsVersion("web");
			driveringStatus.setIsVoilation(1);
			driveringStatus.setVoilationHour(voilationHour);
			driveringStatus.setNote("");
			driveringStatus.setCustomLocation("");
			driveringStatus.setCurrentLocation("");
			driveringStatus.setEngineHour("0");
			driveringStatus.setEngineStatus(driveringStatusViewDto.getEngineStatus());
			driveringStatus.setOrigin("Driver");
			driveringStatus.setOdometer(0);
			driveringStatus.setRemainingWeeklyTime(remainingWeeklyTime);
			driveringStatus.setRemainingDutyTime(remainingDutyTime);
			driveringStatus.setRemainingDriveTime(remainingDriveTime);
			driveringStatus.setRemainingSleepTime(remainingSleepTime);
			driveringStatus.setShift(driveringStatusViewDto.getShift());
			driveringStatus.setDays(driveringStatusViewDto.getDays());
			driveringStatus.setReceivedTimestamp(instant.toEpochMilli());
			driveringStatus.setIsVisible(1);
			driveringStatus = driveringStatusRepo.save(driveringStatus);
	        
	        Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
			Query queryLog = new Query(
			  Criteria.where("driverId").is(driveringStatus.getDriverId())
			);
			queryLog.limit(1);
			queryLog.with(pageableRequest);
			List<DriveringStatusViewDto> dsDataLog = mongoTemplate.find(queryLog, DriveringStatusViewDto.class,"drivering_status");
			
			DriverStatusLog driverStatusLog = new DriverStatusLog();

//			long maxId = lookupMaxIdOfDriverOperation(driveringStatus.getDriverId());
//			if(maxId<=0) {
//				maxId=1;
//				driverStatusLog.setStatusId(maxId);
//			}else {
//				maxId++;
//				driverStatusLog.setStatusId(maxId);
//			}
			driverStatusLog.setStatusId(0);
			driverStatusLog.setLogDataId(dsDataLog.get(0).get_id());
			driverStatusLog.setDriverId(driveringStatus.getDriverId());
			driverStatusLog.setVehicleId(driveringStatus.getVehicleId());
			driverStatusLog.setClientId(driveringStatus.getClientId());
			driverStatusLog.setStatus(driveringStatus.getStatus());
			driverStatusLog.setLattitude(driveringStatus.getLattitude());
			driverStatusLog.setLongitude(driveringStatus.getLongitude());
			driverStatusLog.setDateTime(driveringStatus.getUtcDateTime());
			driverStatusLog.setLogType(driveringStatus.getLogType());
			driverStatusLog.setEngineHour(driveringStatus.getEngineHour());
			driverStatusLog.setOrigin(driveringStatus.getOrigin());
			driverStatusLog.setOdometer(driveringStatus.getOdometer());
			driverStatusLog.setIsVoilation(driveringStatus.getIsVoilation());
			driverStatusLog.setNote(driveringStatus.getNote());
			driverStatusLog.setCustomLocation(driveringStatus.getCustomLocation());
			driverStatusLog.setIsReportGenerated(0);
			driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
			driverStatusLog.setIsVisible(1);
			List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(dsDataLog.get(0).get_id());
			if(logDataExist.size()<=0) {
				driverStatusLogRepo.save(driverStatusLog);
				UpdateDriverSequenceId(driveringStatus.getDriverId(),driveringStatus.getUtcDateTime());
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void EditDriverLogNew(long driverId, long currentLogTime, long midnightTimestamp, long currentDayEndTime, long to, long totalSeconds, long lastUtcDateTime, String currentStatus,int logShift,int logDays) {
		String sDebug=" ## ";
		long remainingDriveTime=0, remainingDutyTime=0, remainingWeeklyTime=0, remainingSleepTime=0;
		int shift=0, lastShift=0, days=0,lastDays=0;
		long secondShift = 34 * 60L * 60;
        long secondSleep = 10 * 60L * 60;
        long continueDriveSeconds = 8 * 60L * 60;
        long remainingContinueDrive=0;
        long breakTimeSeconds = 30*60;
        long splitSleepSeconds = 2*60*60;
        long totalDriveSeconds = 11*60*60;
        long totalDutySeconds = 14*60*60;
        long totalContinueDrive=0;
        long totalBreakSecond=0;
        long breakDuration=0;
        
        EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
        CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
        secondShift = (cycleUsaData.getCycleRestartTime()*60*60);
        secondSleep = (cycleUsaData.getOnSleepTime()*60*60);
        continueDriveSeconds = cycleUsaData.getContinueDriveTime()*60*60;
        breakTimeSeconds = cycleUsaData.getBreakTime()*60;
        totalDriveSeconds = cycleUsaData.getOnDriveTime()*60*60;
        totalDutySeconds = cycleUsaData.getOnDutyTime()*60*60;
        
        long currentUtcDateTime=0, lastLogUtcDateTime=0,nextLogUtcDateTime=0;
        String nextStatus="", previousStatus="";
        int iCount=0;
        boolean isDayChange=false;
        boolean isBreak=false;
        
        try {
        	Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").ascending());
    		Query queryData = new Query(
    		    Criteria.where("driverId").is(driverId)
        		.and("utcDateTime").gt(lastUtcDateTime).lte(to)
                .and("isVoilation").is(0)
    		);
    		queryData.limit(1);
    		queryData.with(pageableRequest);
    		List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
    		if(driveringStatusViewDtoData.size()>0) {
    			nextStatus = driveringStatusViewDtoData.get(0).getStatus();
    			nextLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
    		}else {
    			nextStatus = currentStatus;
    		}
    		
    		pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
    		queryData = new Query(
    		    Criteria.where("driverId").is(driverId)
//        		.and("utcDateTime").lt(midnightTimestamp)
    		    .and("utcDateTime").lt(lastUtcDateTime)
                .and("isVoilation").is(0)
    		);
    		queryData.limit(1);
    		queryData.with(pageableRequest);
    		driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
    		if(driveringStatusViewDtoData.size()>0) {
    			previousStatus = driveringStatusViewDtoData.get(0).getStatus();
    			lastLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
    		}else {
    			previousStatus = "OffDuty";
    			lastLogUtcDateTime = midnightTimestamp;
    		}
    		
    		Query query = new Query();
            Update update = new Update();
            
            long previousMidnightTimestamp = midnightTimestamp - 86400000L;
            List<DriveringStatusLogViewDto> driveringStatusList=loockupDriveringStatusDataForOneDay(driverId,previousMidnightTimestamp,to,logShift,logDays);
            
            sDebug+=" >> "+previousStatus+" :: "+currentStatus+" : "+nextStatus+"\n";
            SaveLog(sDebug);
            
            String logType="";
            int voilationHour=0;
            long voilationUtcDateTime=0;
            currentUtcDateTime=0;
            if ((currentStatus.equals("OnDrive") && nextStatus.equals("OnDuty")) && (previousStatus.equals("OffDuty") || previousStatus.equals("OnSleep"))) {
        		for(int i=0;i<driveringStatusList.size();i++) {
        			
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			
        			if(i==0) {
        				isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentLogTime,lastLogUtcDateTime,nextLogUtcDateTime);
        				breakDuration = (lastUtcDateTime-lastLogUtcDateTime)/1000;
        			}
        			
        			if(isBreak) {
    					remainingDutyTime = CalculateRemainingTime(totalSeconds+breakDuration,remainingDutyTime);
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds+breakDuration,remainingWeeklyTime);
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
    				}else {
    					remainingDutyTime = CalculateRemainingTime(-breakDuration,remainingDutyTime);
            			remainingWeeklyTime = CalculateRemainingTime(-breakDuration,remainingWeeklyTime);
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
    				}
        			sDebug+=" >> BD : "+breakDuration+" :: "+remainingDutyTime+" : "+remainingWeeklyTime+"\n";

        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
        					break;
        				}
        			}
        			lastDays = days;  
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDuty") && nextStatus.equals("OnDrive")) {
        		for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
        					break;
        				}
        			}
        			lastDays = days;  
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDrive") && nextStatus.equals("OnDuty")) {
            	for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
        					break;
        				}
        			}
        			lastDays = days;  
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDrive") && (nextStatus.equals("OffDuty") || nextStatus.equals("OnSleep"))) {
            	for(int i=0;i<driveringStatusList.size();i++) {
            		sDebug+="Next log : "+nextStatus+",\n";
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
//        			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
//        			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			sDebug+="Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDuty") && (nextStatus.equals("OffDuty") || nextStatus.equals("OnSleep"))) {
            	for(int i=0;i<driveringStatusList.size();i++) {
            		sDebug+="Next log : "+nextStatus+",\n";
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			sDebug+="Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        		}
            }else if ((currentStatus.equals("OffDuty") || currentStatus.equals("OnSleep")) && previousStatus.equals("OnDrive")) {
            	sDebug+="Previous log : "+previousStatus+",\n";
            	for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			
        			isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentUtcDateTime,lastLogUtcDateTime,nextLogUtcDateTime);
        			if(isBreak) {
        				if (totalSeconds < 0) {
            			    totalSeconds = Math.abs(totalSeconds);
            			} else {
            			    totalSeconds = -totalSeconds;
            			}
            			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}else {
            			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}
        			
        			sDebug+="Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        	        			sDebug+="Shift | "+shift+" Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        			lastLogUtcDateTime = currentUtcDateTime;
        		}
            }else if ((currentStatus.equals("OffDuty") || currentStatus.equals("OnSleep")) && previousStatus.equals("OnDuty")) {
            	for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentUtcDateTime,lastLogUtcDateTime,nextLogUtcDateTime);
        			if(isBreak) {
        				if (totalSeconds < 0) {
            			    totalSeconds = Math.abs(totalSeconds);
            			} else {
            			    totalSeconds = -totalSeconds;
            			}
        				remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
//            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}else {
        				remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
//            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}
        			
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        		}
            }
            
            SaveLog(sDebug);
            
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
		
	}
	
	public void EditDriverLog(long driverId, long currentLogTime, long midnightTimestamp, long currentDayEndTime, long to, long totalSeconds, long lastUtcDateTime, String currentStatus,int logShift,int logDays) {
		String sDebug=" ## ";
		long remainingDriveTime=0, remainingDutyTime=0, remainingWeeklyTime=0, remainingSleepTime=0;
		int shift=0, lastShift=0, days=0,lastDays=0;
		long secondShift = 34 * 60L * 60;
        long secondSleep = 10 * 60L * 60;
        long continueDriveSeconds = 8 * 60L * 60;
        long remainingContinueDrive=0;
        long breakTimeSeconds = 30*60;
        long splitSleepSeconds = 2*60*60;
        long totalDriveSeconds = 11*60*60;
        long totalDutySeconds = 14*60*60;
        long totalContinueDrive=0;
        long totalBreakSecond=0;
        long breakDuration=0;
        
        EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
        CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
        secondShift = (cycleUsaData.getCycleRestartTime()*60*60);
        secondSleep = (cycleUsaData.getOnSleepTime()*60*60);
        continueDriveSeconds = cycleUsaData.getContinueDriveTime()*60*60;
        breakTimeSeconds = cycleUsaData.getBreakTime()*60;
        totalDriveSeconds = cycleUsaData.getOnDriveTime()*60*60;
        totalDutySeconds = cycleUsaData.getOnDutyTime()*60*60;
        
        long currentUtcDateTime=0, lastLogUtcDateTime=0,nextLogUtcDateTime=0;
        String nextStatus="", previousStatus="";
        int iCount=0;
        boolean isDayChange=false;
        boolean isBreak=false;
        
        if(currentStatus.equals("YardMove")) {
        	currentStatus = "OnDuty";
    	}else if(currentStatus.equals("PersonalUse")) {
    		currentStatus = "OffDuty";
    	}
        
        try {
        	Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").ascending());
    		Query queryData = new Query(
    		    Criteria.where("driverId").is(driverId)
        		.and("utcDateTime").gt(lastUtcDateTime).lte(to)
                .and("isVoilation").is(0)
    		);
    		queryData.limit(1);
    		queryData.with(pageableRequest);
    		List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
    		if(driveringStatusViewDtoData.size()>0) {
    			nextStatus = driveringStatusViewDtoData.get(0).getStatus();
    			nextLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
    		}else {
    			nextStatus = currentStatus;
    		}
    		
    		if(nextStatus.equals("YardMove")) {
    			nextStatus = "OnDuty";
        	}else if(nextStatus.equals("PersonalUse")) {
        		nextStatus = "OffDuty";
        	}
    		
    		pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
    		queryData = new Query(
    		    Criteria.where("driverId").is(driverId)
//        		.and("utcDateTime").lt(midnightTimestamp)
    		    .and("utcDateTime").lt(lastUtcDateTime)
                .and("isVoilation").is(0)
    		);
    		queryData.limit(1);
    		queryData.with(pageableRequest);
    		driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
    		if(driveringStatusViewDtoData.size()>0) {
    			previousStatus = driveringStatusViewDtoData.get(0).getStatus();
    			lastLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
    		}else {
    			previousStatus = "OffDuty";
    			lastLogUtcDateTime = midnightTimestamp;
    		}
    		
    		if(previousStatus.equals("YardMove")) {
    			previousStatus = "OnDuty";
        	}else if(previousStatus.equals("PersonalUse")) {
        		previousStatus = "OffDuty";
        	}
    		
    		Query query = new Query();
            Update update = new Update();
            
            List<DriveringStatusLogViewDto> driveringStatusList=loockupDriveringStatusData(driverId,lastUtcDateTime,currentDayEndTime);
            
            sDebug+=" >> "+previousStatus+" :: "+currentStatus+" : "+nextStatus+"\n";
            SaveLog(sDebug);
            
            String logType="";
            int voilationHour=0;
            long voilationUtcDateTime=0;
            currentUtcDateTime=0;
            if ((currentStatus.equals("OnDrive") && nextStatus.equals("OnDuty")) && (previousStatus.equals("OffDuty") || previousStatus.equals("OnSleep"))) {
        		for(int i=0;i<driveringStatusList.size();i++) {
        			
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			
        			if(i==0) {
        				isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentLogTime,lastLogUtcDateTime,nextLogUtcDateTime);
        				breakDuration = (lastUtcDateTime-lastLogUtcDateTime)/1000;
        			}
        			
        			if(isBreak) {
    					remainingDutyTime = CalculateRemainingTime(totalSeconds+breakDuration,remainingDutyTime);
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds+breakDuration,remainingWeeklyTime);
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
    				}else {
    					remainingDutyTime = CalculateRemainingTime(-breakDuration,remainingDutyTime);
            			remainingWeeklyTime = CalculateRemainingTime(-breakDuration,remainingWeeklyTime);
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
    				}
        			sDebug+=" >> BD : "+breakDuration+" :: "+remainingDutyTime+" : "+remainingWeeklyTime+"\n";

        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
        					break;
        				}
        			}
        			lastDays = days;  
        			lastShift = shift;
        		}
            }if ((currentStatus.equals("OnDuty") && nextStatus.equals("OnDrive")) && (previousStatus.equals("OffDuty") || previousStatus.equals("OnSleep"))) {
        		for(int i=0;i<driveringStatusList.size();i++) {
        			
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			
        			if(i==0) {
        				isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentLogTime,lastLogUtcDateTime,nextLogUtcDateTime);
        				breakDuration = (lastUtcDateTime-lastLogUtcDateTime)/1000;
        			}
        			long shiftDuration = (currentLogTime-lastLogUtcDateTime)/1000;
        			shiftDuration = Math.abs(shiftDuration);
        			if(shiftDuration>secondShift) {
        				sDebug+=" ### Shift Change :\n";
        			}else {
        				if(isBreak) {
        					remainingDutyTime = CalculateRemainingTime(totalSeconds+breakDuration,remainingDutyTime);
                			remainingWeeklyTime = CalculateRemainingTime(totalSeconds+breakDuration,remainingWeeklyTime);
        				}else {
        					remainingDutyTime = CalculateRemainingTime(-breakDuration,remainingDutyTime);
                			remainingWeeklyTime = CalculateRemainingTime(-breakDuration,remainingWeeklyTime);
        				}
            			sDebug+=" >> BD : "+breakDuration+" :: "+remainingDutyTime+" : "+remainingWeeklyTime+"\n";

            			days = driveringStatusList.get(i).getDays();
            			shift = driveringStatusList.get(i).getShift();
            			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
            			if(days==lastDays && lastDays>0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
            			}else {
            				if(lastDays==0 && i==0) {
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
            				}else {
            					break;
            				}
            			}
            			lastDays = days;  
            			lastShift = shift;
        			}
        			
        		}
            }else if (currentStatus.equals("OnDuty") && nextStatus.equals("OnDrive")) {
        		for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
        					break;
        				}
        			}
        			lastDays = days;  
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDrive") && nextStatus.equals("OnDuty")) {
            	for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
        					break;
        				}
        			}
        			lastDays = days;  
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDrive") && (nextStatus.equals("OffDuty") || nextStatus.equals("OnSleep"))) {
            	for(int i=0;i<driveringStatusList.size();i++) {
            		sDebug+="Next log : "+nextStatus+",\n";
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
//        			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
//        			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			sDebug+="Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        		}
            }else if (currentStatus.equals("OnDuty") && (nextStatus.equals("OffDuty") || nextStatus.equals("OnSleep"))) {
            	for(int i=0;i<driveringStatusList.size();i++) {
            		sDebug+="Next log : "+nextStatus+",\n";
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			sDebug+="Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        		}
            }else if ((currentStatus.equals("OffDuty") || currentStatus.equals("OnSleep")) && previousStatus.equals("OnDrive")) {
            	sDebug+="Previous log : "+previousStatus+",\n";
            	for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			
        			isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentUtcDateTime,lastLogUtcDateTime,nextLogUtcDateTime);
        			if(isBreak) {
        				if (totalSeconds < 0) {
            			    totalSeconds = Math.abs(totalSeconds);
            			} else {
            			    totalSeconds = -totalSeconds;
            			}
            			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}else {
            			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}
        			
        			sDebug+="Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
        	        			sDebug+="Shift | "+shift+" Day | "+days+" OD | "+remainingDutyTime+" ODR | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        			lastLogUtcDateTime = currentUtcDateTime;
        		}
            }else if ((currentStatus.equals("OffDuty") || currentStatus.equals("OnSleep")) && previousStatus.equals("OnDuty")) {
            	for(int i=0;i<driveringStatusList.size();i++) {
        			ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
        			currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
        			isBreak = CheckBreak(currentStatus,previousStatus,nextStatus,currentUtcDateTime,lastLogUtcDateTime,nextLogUtcDateTime);
        			if(isBreak) {
        				if (totalSeconds < 0) {
            			    totalSeconds = Math.abs(totalSeconds);
            			} else {
            			    totalSeconds = -totalSeconds;
            			}
        				remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
//            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}else {
        				remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
            			remainingDutyTime = CalculateRemainingTime(totalSeconds,remainingDutyTime);
            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
//            			remainingDriveTime = CalculateRemainingTime(totalSeconds,remainingDriveTime);
            			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
            			remainingWeeklyTime = CalculateRemainingTime(totalSeconds,remainingWeeklyTime);
        			}
        			
        			days = driveringStatusList.get(i).getDays();
        			shift = driveringStatusList.get(i).getShift();
        			if(days==lastDays && lastDays>0) {
            			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        			}else {
        				if(lastDays==0 && i==0) {
                			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        				}else {
//        					break;
        					if(shift!=lastShift) {
            					break;
        					}else {
        						isDayChange=true;
        	        			remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        	        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
                    			UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);

        					}
        				}
        			}
        			if(!isDayChange) {
            			lastDays = days;  
        			}
        			lastShift = shift;
        		}
            }
            
            SaveLog(sDebug);
            
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
		
	}
	
	public void CheckAllVoilations_withsplit(long driverId, long from, long to, long l15toDate, int logShift,int logDays) {
	    long secondShift = 34 * 60L * 60;
	    long secondSleep = 10 * 60L * 60;
	    long continueDriveSeconds = 8 * 60L * 60;
	    long remainingContinueDrive = 0;
	    long breakTimeSeconds = 30*60;
	    long splitSleepSeconds = 2*60*60;
	    long totalDriveSeconds = 11*60*60;
	    long totalDutySeconds = 14*60*60;
	    long totalContinueDrive = 0;
	    long totalBreakSecond = 0;
	    long currentUtcDateTime = 0, lastLogUtcDateTime = 0, nextLogUtcDateTime = 0;
	    long remainingDutyTime = 0, remainingWeeklyTime = 0, remainingDriveTime = 0;
	    String nextStatus = "", previousStatus = "";
	    int shift = 0, lastShift = 0, days = 0, lastDays = 0;
	    boolean isDayChange = false;
	    String sDebug = " Voilation Check >> \n";

	    EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
        CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
        secondShift = (cycleUsaData.getCycleRestartTime()*60*60);
        secondSleep = (cycleUsaData.getOnSleepTime()*60*60);
        continueDriveSeconds = cycleUsaData.getContinueDriveTime()*60*60;
        breakTimeSeconds = cycleUsaData.getBreakTime()*60;
        totalDriveSeconds = cycleUsaData.getOnDriveTime()*60*60;
        totalDutySeconds = cycleUsaData.getOnDutyTime()*60*60;
        
	    // Split sleep tracking
	    long splitSleep1 = 0;
	    long splitSleep2 = 0;
	    boolean isFirstSplitDone = false;
	    LocalDate firstSplitDay = null;

	    LocalDate fromLocalDate = Instant.ofEpochMilli(from).atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate toLocalDate = Instant.ofEpochMilli(to).atZone(ZoneId.systemDefault()).toLocalDate();

	    try {
	        String sCurrentStatus = "", sPreviousStatus = "";
	        long previousUtcDateTime = 0;
	        boolean isBreakTime = false, isVoilationCreate = false;

	        long previousMidnightTimestamp = from - 86400000L;
	        List<DriveringStatusLogViewDto> driveringStatusList = loockupDriveringStatusDataForOneDay(driverId, previousMidnightTimestamp, l15toDate, logShift, logDays);

	        sDebug += "from : " + previousMidnightTimestamp + " to : " + to + " Size : " + driveringStatusList.size() + " : " + driverId + "\n";

	        for (int i = 0; i < driveringStatusList.size(); i++) {
	            if(i == 0) {
	                remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
	                remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
	                remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
	            }

	            currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
	            LocalDate currentLocalDate = Instant.ofEpochMilli(currentUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDate();
	            sCurrentStatus = driveringStatusList.get(i).getStatus();
	            shift = driveringStatusList.get(i).getShift();
	            days = driveringStatusList.get(i).getDays();

	            sDebug += "Check Status: " + sPreviousStatus + "\n";

	            if(!sPreviousStatus.equals("")) {
	                ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
	                long duration = (currentUtcDateTime - previousUtcDateTime) / 1000; // in seconds
	                isBreakTime = false;

	                if(shift != lastShift) {
	                    break;
	                } else {
	                    if((days == lastDays && lastDays > 0) && !isDayChange) {
	                        switch (sPreviousStatus) {
	                            case "OnDuty":
	                                if(totalContinueDrive > 0) {
	                                    if(duration > breakTimeSeconds) totalContinueDrive = 0;
	                                    else {
	                                        totalContinueDrive += duration;
	                                        totalBreakSecond += duration;
	                                    }
	                                }
	                                remainingDutyTime -= duration;
	                                remainingWeeklyTime -= duration;
	                                totalDutySeconds -= duration;
	                                break;

	                            case "OnDrive":
	                                totalBreakSecond = 0;
	                                totalContinueDrive += duration;
	                                totalDriveSeconds -= duration;
	                                remainingDriveTime -= duration;
	                                remainingDutyTime -= duration;
	                                remainingWeeklyTime -= duration;
	                                break;

	                            case "OffDuty":
	                            case "OnSleep":
	                                if(totalContinueDrive > 0) {
	                                    if(duration > breakTimeSeconds) totalContinueDrive = 0;
	                                    else {
	                                        totalContinueDrive += duration;
	                                        totalBreakSecond += duration;
	                                        remainingDutyTime -= duration;
	                                        remainingWeeklyTime -= duration;
	                                    }
	                                }
	                                break;
	                        }
	                    } else {
	                        isDayChange = true;
	                        remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
	                        remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
	                        switch (sPreviousStatus) {
	                            case "OnDuty":
	                                if(totalContinueDrive > 0) {
	                                    if(duration > breakTimeSeconds) totalContinueDrive = 0;
	                                    else {
	                                        totalContinueDrive += duration;
	                                        totalBreakSecond += duration;
	                                    }
	                                }
	                                remainingWeeklyTime -= duration;
	                                totalDutySeconds -= duration;
	                                break;

	                            case "OnDrive":
	                                totalBreakSecond = 0;
	                                totalContinueDrive += duration;
	                                totalDriveSeconds -= duration;
	                                remainingWeeklyTime -= duration;
	                                break;

	                            case "OffDuty":
	                            case "OnSleep":
	                                if(totalContinueDrive > 0) {
	                                    if(duration > breakTimeSeconds) totalContinueDrive = 0;
	                                    else {
	                                        totalContinueDrive += duration;
	                                        totalBreakSecond += duration;
	                                        remainingWeeklyTime -= duration;
	                                    }
	                                }
	                                break;
	                        }
	                    }
	                }

	                sDebug += "Duration | " + duration + " Status | " + sPreviousStatus + " DU | " + remainingDutyTime + " OD | " + remainingDriveTime + " Cycle | " + remainingWeeklyTime + "\n";

	                UpdateRemainingCalculationData(driverId, objId, remainingDriveTime, remainingDutyTime, remainingWeeklyTime);

	                // Check for continuous driving violation
	                if(totalContinueDrive > continueDriveSeconds && !isDayChange) {
	                    int count = driveringStatusRepo.countVoilationRecords(driverId, from, to, 1, 8);
	                    if(count <= 0) {
	                        String logType = "You are continuously driving for "+cycleUsaData.getContinueDriveTime()+" hours";
	                        int voilationHour = (int)cycleUsaData.getOnSleepTime();
	                        long voilationUtcDateTime = currentUtcDateTime - (Math.abs((continueDriveSeconds - totalContinueDrive)) * 1000);
	                        SaveVoilations(driveringStatusList.get(i), logType, voilationHour, voilationUtcDateTime);
	                    }
	                }

	                if((days == lastDays && lastDays > 0) && !isDayChange) {
	                    CheckVoilations(driveringStatusList.get(i), remainingDutyTime, remainingDriveTime, remainingWeeklyTime, currentUtcDateTime, from, to);
	                }

	                // --- Check Split Sleep ---
	                if(sPreviousStatus.equals("OnSleep") || sPreviousStatus.equals("OffDuty")) {
	                    checkSplitSleepViolation(driveringStatusList.get(i), duration, currentLocalDate, currentUtcDateTime, driverId, sPreviousStatus);
	                }
	            }

	            // Map YardMove / PersonalUse to duty status
	            if(sCurrentStatus.equals("YardMove")) sPreviousStatus = "OnDuty";
	            else if(sCurrentStatus.equals("PersonalUse")) sPreviousStatus = "OffDuty";
	            else sPreviousStatus = sCurrentStatus;

	            previousUtcDateTime = currentUtcDateTime;
	            lastShift = shift;
	            lastDays = days;
	        }

	        SaveLog(sDebug);
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    }
	}

	// ================= Helper Function =================
	long splitSleep1 = 0;
	long splitSleep2 = 0;
	boolean isFirstSplitDone = false;
	LocalDate firstSplitDay = null;
	public void checkSplitSleepViolation(DriveringStatusLogViewDto log,long duration,LocalDate currentLocalDate,long currentUtcDateTime,long driverId, String status) {
	    // Only consider OffDuty or OnSleep for split sleep
	    if(!status.equals("OnSleep") && !status.equals("OffDuty")) {
	        return;
	    }

	    // Case 1: Short period (2–6 hours)
	    if(duration >= 2 * 3600 && duration < 7 * 3600) {
	        splitSleep1 = duration;
	        isFirstSplitDone = true;
	        firstSplitDay = currentLocalDate;
	    }
	    // Case 2: Long period (>=7 hours)
	    else if(duration >= 7 * 3600) {
	        if(isFirstSplitDone) {
	            splitSleep2 = duration;
	            boolean sameDay = currentLocalDate.equals(firstSplitDay);

	            if((splitSleep1 + splitSleep2) >= 10 * 3600) {
	                // Split sleep satisfied – reset duty cycle
	                String logType = "Split OffDuty/Sleep satisfied (" +
	                        (splitSleep1 / 3600) + "h + " +
	                        (splitSleep2 / 3600) + "h) " +
	                        (sameDay ? "[Same Day]" : "[Different Day]");

	                SaveVoilations(log, logType, 0, currentUtcDateTime);

	                // Optionally, reset remaining duty time
	                // remainingDutyTime = 14 * 3600;
	                // totalDutySeconds = 14 * 3600;
	            }

	            // Reset trackers
	            splitSleep1 = 0;
	            splitSleep2 = 0;
	            isFirstSplitDone = false;
	            firstSplitDay = null;
	        }
	    }
	}
	
	public void CheckAllVoilations(long driverId, long from, long to, long l15toDate, int logShift,int logDays) {
		long secondShift = 34 * 60L * 60;
        long secondSleep = 10 * 60L * 60;
        long continueDriveSeconds = 8 * 60L * 60;
        long remainingContinueDrive=0;
        long breakTimeSeconds = 30*60;
        long splitSleepSeconds = 2*60*60;
        long totalDriveSeconds = 11*60*60;
        long totalDutySeconds = 14*60*60;
        long totalContinueDrive=0;
        long totalBreakSecond=0;
        long currentUtcDateTime=0, lastLogUtcDateTime=0,nextLogUtcDateTime=0;
        long remainingDutyTime=0,remainingWeeklyTime=0,remainingDriveTime=0;
        String nextStatus="", previousStatus="";
        int shift=0, lastShift=0,days=0,lastDays=0;
        boolean isDayChange=false;
        String sDebug=" Voilation Check >> \n";
        
        EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
        CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
        secondShift = (cycleUsaData.getCycleRestartTime()*60*60);
        secondSleep = (cycleUsaData.getOnSleepTime()*60*60);
        continueDriveSeconds = cycleUsaData.getContinueDriveTime()*60*60;
        breakTimeSeconds = cycleUsaData.getBreakTime()*60;
        totalDriveSeconds = cycleUsaData.getOnDriveTime()*60*60;
        totalDutySeconds = cycleUsaData.getOnDutyTime()*60*60;
        
        LocalDate fromLocalDate = Instant.ofEpochMilli(from).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate toLocalDate = Instant.ofEpochMilli(to).atZone(ZoneId.systemDefault()).toLocalDate();
        
        try {
        	String sCurrentStatus="", sPreviousStatus="";
            long previousUtcDateTime=0;
            boolean isBreakTime=false, isVoilationCreate=false;
            
//    		Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("utcDateTime").descending());
//    		Query queryData = new Query(
//    		    Criteria.where("driverId").is(driverId)
//        		.and("utcDateTime").lt(from)
//                .and("isVoilation").is(0)
//    		);
//    		queryData.limit(1);
//    		queryData.with(pageableRequest);
//    		List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(queryData, DriveringStatusViewDto.class,"drivering_status");
//    		if(driveringStatusViewDtoData.size()>0) {
//    			previousStatus = driveringStatusViewDtoData.get(0).getStatus();
//    			lastLogUtcDateTime = driveringStatusViewDtoData.get(0).getUtcDateTime();
//    		}else {
//    			previousStatus = "OffDuty";
//    			lastLogUtcDateTime = from;
//    		}

//            List<DriveringStatusLogViewDto> driveringStatusList=loockupDriveringStatusData(driverId,from,to);
            long previousMidnightTimestamp = from - 86400000L;
//            List<DriveringStatusLogViewDto> driveringStatusList=loockupDriveringStatusDataForOneDay(driverId,previousMidnightTimestamp,to,logShift,logDays);
            List<DriveringStatusLogViewDto> driveringStatusList=loockupDriveringStatusDataForOneDay(driverId,previousMidnightTimestamp,l15toDate,logShift,logDays);
            sDebug+="from : "+previousMidnightTimestamp+" to : "+to+"Size : "+driveringStatusList.size()+" : "+driverId+"\n";
            for (int i = 0; i < driveringStatusList.size(); i++) {
//                if (i == 0 && lastLogUtcDateTime > 0) {
//                    previousUtcDateTime = lastLogUtcDateTime;
//                    sPreviousStatus = previousStatus;
//                }
            	if(i==0) {
            		remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
            	}
            	currentUtcDateTime = driveringStatusList.get(i).getUtcDateTime();
            	LocalDate currentLocalDate = Instant.ofEpochMilli(currentUtcDateTime).atZone(ZoneId.systemDefault()).toLocalDate();
                sCurrentStatus = driveringStatusList.get(i).getStatus();
                shift = driveringStatusList.get(i).getShift();
                days = driveringStatusList.get(i).getDays();
                
            	sDebug+="Check Status: "+sPreviousStatus+"\n";
            	if(!sPreviousStatus.equals("")) {
                    
                    ObjectId objId = new ObjectId(driveringStatusList.get(i).get_id());
                    
                    long duration = (currentUtcDateTime - previousUtcDateTime) / 1000; // in seconds
//                    sDebug+="Duration : "+duration+" :: "+sPreviousStatus+" :: "+totalContinueDrive+" :: "+totalBreakSecond+"\n";

                    isBreakTime = false;
                    
                    if(shift!=lastShift) {
    					break;
					}else {
						
//						if(days!=lastDays && lastDays>0) {
//							if(currentLocalDate.equals(fromLocalDate.toString()) || currentLocalDate.equals(toLocalDate.toString())) {
//								isDayChange=false;
//								totalContinueDrive=0;
//								
//								remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
//			        			remainingWeeklyTime = Long.parseLong(driveringStatusList.get(i).getRemainingWeeklyTime());
//			        			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
//			        			
//							}
//						}
						
						if((days==lastDays && lastDays>0) && !isDayChange) {
							switch (sPreviousStatus) {
				                case "OnDuty":
				                	if(totalContinueDrive>0) {
				                		if(duration>breakTimeSeconds) {
				                			// break
				                			totalContinueDrive=0;
				                		}else {
				                			totalContinueDrive+=duration;
				                			totalBreakSecond+=duration;
				                		}
				                	}
				                	remainingDutyTime-=duration;
				                	remainingWeeklyTime-=duration;
				                	totalDutySeconds-=duration;
				                    break;
				                case "OnDrive":
				                	totalBreakSecond=0;
				                	totalContinueDrive+=duration;
				                	totalDriveSeconds-=duration;
				                	remainingDriveTime-=duration;
				                	remainingDutyTime-=duration;
				                	remainingWeeklyTime-=duration;
				                    break;
				                case "OffDuty":
				                	if(totalContinueDrive>0) {
				                		if(duration>breakTimeSeconds) {
				                			// break
				                			totalContinueDrive=0;
				                		}else {
				                			totalContinueDrive+=duration;
				                			totalBreakSecond+=duration;
				                			remainingDutyTime-=duration;
				    	                	remainingWeeklyTime-=duration;
				                		}
				                	}
				                    break;
				                case "OnSleep":
				                	if(totalContinueDrive>0) {
				                		if(duration>breakTimeSeconds) {
				                			// break
				                			totalContinueDrive=0;
				                		}else {
				                			totalContinueDrive+=duration;
				                			totalBreakSecond+=duration;
				                			remainingDutyTime-=duration;
				    	                	remainingWeeklyTime-=duration;
				                		}
				                	}
				                    break;
				            }
	        			}else {
	        				isDayChange=true;
	        				remainingDutyTime = Long.parseLong(driveringStatusList.get(i).getRemainingDutyTime());
	            			remainingDriveTime = Long.parseLong(driveringStatusList.get(i).getRemainingDriveTime());
	        				switch (sPreviousStatus) {
				                case "OnDuty":
				                	if(totalContinueDrive>0) {
				                		if(duration>breakTimeSeconds) {
				                			// break
				                			totalContinueDrive=0;
				                		}else {
				                			totalContinueDrive+=duration;
				                			totalBreakSecond+=duration;
				                		}
				                	}
				                	remainingWeeklyTime-=duration;
				                	totalDutySeconds-=duration;
				                    break;
				                case "OnDrive":
				                	totalBreakSecond=0;
				                	totalContinueDrive+=duration;
				                	totalDriveSeconds-=duration;
				                	remainingWeeklyTime-=duration;
				                    break;
				                case "OffDuty":
				                	if(totalContinueDrive>0) {
				                		if(duration>breakTimeSeconds) {
				                			// break
				                			totalContinueDrive=0;
				                		}else {
				                			totalContinueDrive+=duration;
				                			totalBreakSecond+=duration;
				    	                	remainingWeeklyTime-=duration;
				                		}
				                	}
				                    break;
				                case "OnSleep":
				                	if(totalContinueDrive>0) {
				                		if(duration>breakTimeSeconds) {
				                			// break
				                			totalContinueDrive=0;
				                		}else {
				                			totalContinueDrive+=duration;
				                			totalBreakSecond+=duration;
				    	                	remainingWeeklyTime-=duration;
				                		}
				                	}
				                    break;
				            }
	        			}
					}
                    
                    sDebug+="Duration | "+duration+" Status | "+sPreviousStatus+" DU | "+remainingDutyTime+" OD | "+remainingDriveTime+" Cycle | "+remainingWeeklyTime+"\n";
                    
                    UpdateRemainingCalculationData(driverId,objId, remainingDriveTime,remainingDutyTime,remainingWeeklyTime);
                    
//                  // Check for violation
                    if ((totalContinueDrive>continueDriveSeconds) && !isDayChange) {
                        int count = driveringStatusRepo.countVoilationRecords(driverId, from, to, 1, 8);
                        if (count <= 0) {
                        	String logType = "You are continuously driving for "+cycleUsaData.getContinueDriveTime()+" hours";
	                        int voilationHour = (int)cycleUsaData.getOnSleepTime();
                            long voilationUtcDateTime = currentUtcDateTime - (Math.abs((continueDriveSeconds-totalContinueDrive)) * 1000);
                            SaveVoilations(driveringStatusList.get(i), logType, voilationHour, voilationUtcDateTime);
                        }
//                        break;
                    }
                    
                    if((days==lastDays && lastDays>0) && !isDayChange) {
                        CheckVoilations(driveringStatusList.get(i),remainingDutyTime,remainingDriveTime,remainingWeeklyTime,currentUtcDateTime,from,to);
                    }
       
            	}
              
            	if(sCurrentStatus.equals("YardMove")) {
                    sPreviousStatus = "OnDuty";
            	}else if(sCurrentStatus.equals("PersonalUse")) {
                    sPreviousStatus = "OffDuty";
            	}else {
                    sPreviousStatus = sCurrentStatus;
            	}
	          previousUtcDateTime = currentUtcDateTime;
	          lastShift = shift;
	          lastDays = days;
            }
            SaveLog(sDebug);
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        
        
    }

	
	public boolean CheckBreak(String currentStatus,String previousStatus,String nextStatus,long currentUtcDateTime,long lastLogUtcDateTime,long nextLogUtcDateTime) {
		long breakTimeSeconds = 30*60;
        long splitSleepSeconds = 2*60*60;
        boolean isBreak=false;
		if ((currentStatus.equals("OffDuty") || currentStatus.equals("OnSleep")) && (previousStatus.equals("OnDrive") || previousStatus.equals("OnDuty"))) {
			long duration = (currentUtcDateTime-lastLogUtcDateTime)/1000;
			if(duration>breakTimeSeconds || duration>splitSleepSeconds) {
				isBreak=true;
			}
		}else if ((currentStatus.equals("OffDuty") || currentStatus.equals("OnSleep")) && (nextStatus.equals("OnDrive") || nextStatus.equals("OnDuty"))) {
			long duration = (nextLogUtcDateTime-currentUtcDateTime)/1000;
			if(duration>breakTimeSeconds || duration>splitSleepSeconds) {
				isBreak=true;
			}
		}else if ((previousStatus.equals("OffDuty") || previousStatus.equals("OnSleep")) && (currentStatus.equals("OnDrive") || currentStatus.equals("OnDuty"))) {
			long duration = (currentUtcDateTime-lastLogUtcDateTime)/1000;
			if(duration>breakTimeSeconds || duration>splitSleepSeconds) {
				isBreak=true;
			}
		}
		
		return isBreak;
	}
	
	public void CheckVoilations(DriveringStatusLogViewDto driveringStatusList,long remainingDutyTime,long remainingDriveTime,long remainingWeeklyTime,long currentUtcDateTime,long midnightTimestamp,long currentDayEndTime) {
		String sDebug="";
		String logType="";
		int voilationHour=0;
		long voilationUtcDateTime=0;
		long driverId = driveringStatusList.getDriverId();
        
		EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId((int)driverId);
        CycleUsa cycleUsaData = cycleUsaRepo.findByCycleUsaId((int)empDetails.getCycleUsaId());
        
        try {
        	sDebug+=" >> Voilation : DU : "+remainingDutyTime+" | OD : "+remainingDriveTime+" | Cycle : "+remainingWeeklyTime+"\n";
        	if(remainingDutyTime<0) {
    			int count = driveringStatusRepo.countVoilationRecords(driverId,midnightTimestamp,currentDayEndTime,1,14);
    			if(count<=0) {
    				logType="Your onduty time has been exceeded to "+cycleUsaData.getOnDutyTime()+" hours";
    				voilationHour=(int)cycleUsaData.getOnDutyTime();
    				voilationUtcDateTime = currentUtcDateTime-(Math.abs(remainingDutyTime)*1000);
    				SaveVoilations(driveringStatusList,logType,voilationHour,voilationUtcDateTime);
    			}
    		}
    		if(remainingDriveTime<0) {
    			int count = driveringStatusRepo.countVoilationRecords(driverId,midnightTimestamp,currentDayEndTime,1,11);
    			if(count<=0) {
    				logType="Your drive time has been exceeded to "+cycleUsaData.getOnDriveTime()+" hours";
    				voilationHour=(int)cycleUsaData.getOnDriveTime();
    				voilationUtcDateTime = currentUtcDateTime-(Math.abs(remainingDriveTime)*1000);
    				SaveVoilations(driveringStatusList,logType,voilationHour,voilationUtcDateTime);
    			}
    		}
    		if(remainingWeeklyTime<0) {
    			int count = driveringStatusRepo.countVoilationRecords(driverId,midnightTimestamp,currentDayEndTime,1,70);
    			if(count<=0) {
    				logType="Your weekly cycle has been exceeded to "+cycleUsaData.getCycleHour()+" hours";
    				voilationHour=(int)cycleUsaData.getCycleHour();
    				voilationUtcDateTime = currentUtcDateTime-(Math.abs(remainingWeeklyTime)*1000);
    				SaveVoilations(driveringStatusList,logType,voilationHour,voilationUtcDateTime);
    			}
    			
    		}
    		SaveLog(sDebug);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
		
	}
	
	public void UpdateRemainingCalculationData(long driverId,ObjectId objId, long remainingDriveTime, long remainingDutyTime, long remainingWeeklyTime){
		Query query = new Query();
		Update update = new Update();
		query.addCriteria(
			Criteria.where("_id").is(objId)
		);
		update.set("remainingDriveTime", remainingDriveTime);
		update.set("remainingDutyTime", remainingDutyTime);
		update.set("remainingWeeklyTime", remainingWeeklyTime);
        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
        
        query = new Query();
		update = new Update();
		query.addCriteria(
			Criteria.where("driverId").is(driverId)
		);
		update.set("onDutyTime", convertSecondsToTime(remainingDriveTime));
		update.set("onDriveTime", convertSecondsToTime(remainingDutyTime));
		update.set("weeklyTime", convertSecondsToTime(remainingWeeklyTime));
        mongoTemplate.updateMulti(query, update, DriverWorkingStatus.class);
        
        
	}
	
	public String convertSecondsToTime(long totalSeconds) {
	    long hours = totalSeconds / 3600;
	    long minutes = (totalSeconds % 3600) / 60;
	    long seconds = totalSeconds % 60;

	    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public List<DriveringStatusLogViewDto> loockupDriveringStatusDataForOneDay(long driverId, long from, long to,int logShift,int logDays) {
		Query queryData = new Query(
		    Criteria.where("driverId").is(driverId)
    		.and("utcDateTime").gte(from).lte(to)
    		.and("shift").is(logShift)
    		.and("days").is(logDays)
//    		.and("remainingDutyTime").is(50400L)
            .and("isVoilation").is(0)
            .and("isVisible").is(1)
		);
		queryData.with(Sort.by(Sort.Direction.ASC, "utcDateTime"));
		List<DriveringStatusLogViewDto> driveringStatusList = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class, "drivering_status");
	
		return driveringStatusList;
	}
	
	public List<DriveringStatusLogViewDto> loockupDriveringStatusData(long driverId, long from, long to) {
		Query queryData = new Query(
		    Criteria.where("driverId").is(driverId)
    		.and("utcDateTime").gte(from).lte(to)
            .and("isVoilation").is(0)
		);
		queryData.with(Sort.by(Sort.Direction.ASC, "utcDateTime"));
		List<DriveringStatusLogViewDto> driveringStatusList = mongoTemplate.find(queryData, DriveringStatusLogViewDto.class, "drivering_status");
	
		return driveringStatusList;
	}
	
	public long CalculateRemainingTime(long totalSeconds, long remainingTime) {
		try {
			String sLine = "Total Seconds : "+totalSeconds+", Remaining Time : "+remainingTime+"\n";
//			SaveLog(sLine);
			if(totalSeconds<0 && remainingTime>=0) {
				remainingTime-=Math.abs(totalSeconds);
			}else if(totalSeconds>=0 && remainingTime>=0){
				remainingTime+=totalSeconds;						
			}else if(totalSeconds<0 && remainingTime<0){
				remainingTime-=Math.abs(totalSeconds);						
			}else if(totalSeconds>=0 && remainingTime<0){
				remainingTime+=Math.abs(totalSeconds);						
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return remainingTime;
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
				"driverId","clientId","status","lattitude","longitude","dateTime","lDateTime","receivedTimestamp","logType",
				"appVersion","isVoilation","note","customLocation","engineHour","odometer","vehicleId",
				"osVersion","simCardNo","origin","utcDateTime","statusId","timezone","remainingWeeklyTime","remainingDutyTime",
				"remainingDriveTime","remainingSleepTime","isReportGenerated","shift","days","_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.DESC, "utcDateTime"));
        List<DriveringStatusViewDto> results = mongoTemplate.aggregate(aggregation, "drivering_status" , DriveringStatusViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<EmployeeMasterViewDto>> ViewAllDriverStatus(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<EmployeeMasterViewDto>> result = new ResultWrapper<>();
		try {
			List<EmployeeMasterViewDto> driveringStatusViewDto = null;
			Instant instant = Instant.now();
			long driverId = driveringStatusCRUDDto.getDriverId();
			long clientId = driveringStatusCRUDDto.getClientId();
			String timezoneOffSet="";
			driveringStatusViewDto = lookupEmployeeMasterOperation((int)driverId,clientId);
			String onDutyTime="",onDriveTime="",onSleepTime="",weeklyTime="",onBreak="";
			for(int i=0;i<driveringStatusViewDto.size();i++) {
				DriverWorkingStatus driverWorkingStatus = driverWorkingStatusRepo.findAndViewDriverWorkingstatusByDriverId(driveringStatusViewDto.get(i).getEmployeeId());
				try {
					
					MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)driveringStatusViewDto.get(i).getMainTerminalId());
					
					if(mainTerminal.getStateId()>0) {
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
						driveringStatusViewDto.get(i).setTimezoneName(stateInfo.getTimeZone());
						driveringStatusViewDto.get(i).setTimezoneOffSet(stateInfo.getTimezoneOffSet());
						timezoneOffSet=stateInfo.getTimezoneOffSet();
					}
					
					String splitStr[] = timezoneOffSet.split(":");
					long hour = Math.abs(Long.parseLong(splitStr[0]));
					long minutes = Math.abs(Long.parseLong(splitStr[1]));
					long timestampValue=0;
					if(timezoneOffSet.substring(0,1).equals("-")) {
						timestampValue = (driveringStatusViewDto.get(i).getUpdatedTimestamp()-((hour*60)+minutes)*60000);
					}else {
						timestampValue = (driveringStatusViewDto.get(i).getUpdatedTimestamp()+((hour*60)+minutes)*60000);
					}
					driveringStatusViewDto.get(i).setUpdatedTimestamp(timestampValue);
					
					onDutyTime = driverWorkingStatus.getOnDutyTime();
					onDriveTime = driverWorkingStatus.getOnDriveTime();
					onSleepTime = driverWorkingStatus.getOnSleepTime();
					weeklyTime = driverWorkingStatus.getWeeklyTime();
					onBreak = driverWorkingStatus.getOnBreak();
					
					onDutyTime = onDutyTime.substring(0, onDutyTime.length() - 3);
					onDriveTime = onDriveTime.substring(0, onDriveTime.length() - 3);
					onSleepTime = onSleepTime.substring(0, onSleepTime.length() - 3);
					weeklyTime = weeklyTime.substring(0, weeklyTime.length() - 3);
					onBreak = onBreak.substring(0, onBreak.length() - 3);
					
					driveringStatusViewDto.get(i).setOnDutyTime(onDutyTime);
					driveringStatusViewDto.get(i).setOnDriveTime(onDriveTime);
					driveringStatusViewDto.get(i).setOnSleepTime(onSleepTime);
					driveringStatusViewDto.get(i).setWeeklyTime(weeklyTime);
					driveringStatusViewDto.get(i).setOnBreak(onBreak);
					
					Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
					Query query = new Query(
					  Criteria.where("driverId").is(driveringStatusViewDto.get(i).getEmployeeId())
					);
					query.limit(1);
					query.with(pageableRequest);
					List<DriveringStatusViewDto> driveringStatusViewDtoData = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
					
					driveringStatusViewDto.get(i).setCurrentLocation(driveringStatusViewDtoData.get(0).getCustomLocation());
					
					DeviceStatus deviceStatus = deviceStatusRepo.findByDeviceStatusId(driveringStatusViewDto.get(i).getEmployeeId());
					driveringStatusViewDto.get(i).setDeviceStatus(deviceStatus.getStatus());
					
				}catch(Exception ex) {
					ex.printStackTrace();
					driveringStatusViewDto.get(i).setOnDutyTime("14:00");
					driveringStatusViewDto.get(i).setOnDriveTime("11:00");
					driveringStatusViewDto.get(i).setOnSleepTime("10:00");
					driveringStatusViewDto.get(i).setWeeklyTime("70:00");
					driveringStatusViewDto.get(i).setOnBreak("08:00");
				}
			}
			result.setResult(driveringStatusViewDto);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Driver Status Information Send Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<EmployeeMasterViewDto> lookupEmployeeMasterOperation(Integer employeeId,long clientId){
		MatchOperation filter = null;
		if(employeeId>0) {
			filter = Aggregation.match(
					Criteria.where("employeeId").is(employeeId)
					.and("clientId").is(clientId)
					.and("isFirstLogin").is("false")
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("clientId").is(clientId)
					.and("isFirstLogin").is("false")
				);
		}
		
		ProjectionOperation projectStage = Aggregation.project(
				"employeeId","title","firstName","lastName","email","username","companyId","languageId","clientId","startTime",
				"cycleUsaId","cycleCanadaId","mainTerminalId","mobileNo","cdlNo","cdlCountryId","cdlStateId","cdlExpiryDate",
				"pdfEmail","flatRate","exempt","personalUse","yardMoves","divR","cargoTypeId","truckNo","manageEquipement",
				"transferLog","remarks","workingStatus","addedTimestamp","updatedTimestamp","client_master.clientName",
				"cycle_usa_master.cycleUsaName","cycle_canada_master.cycleCanadaName","main_terminal_master.mainTerminalName",
				"country_master.countryName","state_master.stateName","cargo_type_master.cargoTypeName","status",
				"vehicle_master.vehicleNo","driverId","password","restartId","restBreakId","shortHaulException",
				"unlimitedTrailers","unlimitedShippingDocs").andExclude("_id");
		
		LookupOperation client_master = LookupOperation.newLookup()
			      .from("client_master")
			      .localField("clientId")
			      .foreignField("clientId")
			      .as("client_master");
		
		LookupOperation cycle_usa_master = LookupOperation.newLookup()
			      .from("cycle_usa_master")
			      .localField("cycleUsaId")
			      .foreignField("cycleUsaId")
			      .as("cycle_usa_master");
		
		LookupOperation cycle_canada_master = LookupOperation.newLookup()
			      .from("cycle_canada_master")
			      .localField("cycleCanadaId")
			      .foreignField("cycleCanadaId")
			      .as("cycle_canada_master");
		
		LookupOperation main_terminal_master = LookupOperation.newLookup()
			      .from("main_terminal_master")
			      .localField("mainTerminalId")
			      .foreignField("mainTerminalId")
			      .as("main_terminal_master");
		
		LookupOperation country_master = LookupOperation.newLookup()
			      .from("country_master")
			      .localField("cdlCountryId")
			      .foreignField("countryId")
			      .as("country_master");
		
		LookupOperation state_master = LookupOperation.newLookup()
			      .from("state_master")
			      .localField("cdlStateId")
			      .foreignField("stateId")
			      .as("state_master");
		
		LookupOperation cargo_type_master = LookupOperation.newLookup()
			      .from("cargo_type_master")
			      .localField("cargoTypeId")
			      .foreignField("cargoTypeId")
			      .as("cargo_type_master");
		
		LookupOperation vehicle_master = LookupOperation.newLookup()
			      .from("vehicle_master")
			      .localField("truckNo")
			      .foreignField("vehicleId")
			      .as("vehicle_master");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,client_master,cycle_usa_master,cycle_canada_master,
	    		main_terminal_master,country_master,state_master,cargo_type_master,vehicle_master,projectStage);
        List<EmployeeMasterViewDto> results = mongoTemplate.aggregate(aggregation, "employee_master" , EmployeeMasterViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriverWorkingStatus(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<DriveringStatusViewDto>> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			long driverId = driveringStatusCRUDDto.getDriverId();
			if(driverId>0) {
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query query = new Query(
				  Criteria.where("driverId").is(driverId)
				  .and("status").is("OnDuty")
				);
				query.limit(1);
				query.with(pageableRequest);
				List<DriveringStatusViewDto> driveringStatusViewDto = mongoTemplate.find(query, DriveringStatusViewDto.class,"drivering_status");
				
				long fromDate = driveringStatusViewDto.get(0).getLDateTime();
				LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(fromDate), TimeZone.getDefault().toZoneId());  
				currentDateTime = currentDateTime.plusDays(1);
				long toDate = currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
//				System.out.println(fromDate+" :: "+toDate);
				
				driveringStatusViewDto = lookupDriverStatusDataOperation(fromDate,toDate,driverId);
				for(int i=0;i<driveringStatusViewDto.size();i++) {
					driveringStatusViewDto.get(i).setFromDate(fromDate);
					driveringStatusViewDto.get(i).setToDate(toDate);
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int) driverId);
					driveringStatusViewDto.get(i).setDriverName(empInfo.getFirstName()+" "+empInfo.getLastName());
					driveringStatusViewDto.get(i).setStatus(empInfo.getStatus());
				}
				
//				long onDuty=0, onDrive=0, onSleep=0, offDuty=0;
//				long fromOnDuty=0, toOnDuty=0, fromOnDrive=0, toOnDrive=0, fromOnSleep=0, toOnSleep=0, fromOffDuty=0, toOffDuty=0;
//				long lDateTime=0;
//				String lastStatus="";
//				for(int i=0;i<driveringStatusViewDto.size();i++) {
//					if(lastStatus.equals("OnDuty") && lDateTime>0) {
//						fromOnDuty = lDateTime;
//						toOnDuty = driveringStatusViewDto.get(i).getLDateTime();
//						onDuty = TimeUnit.MILLISECONDS.toHours(driveringStatusViewDto.get(i).getLDateTime()-lDateTime);
//					}else if(lastStatus.equals("OnDrive") && lDateTime>0) {
//						fromOnDrive = lDateTime;
//						toOnDrive = driveringStatusViewDto.get(i).getLDateTime();
//						onDrive = TimeUnit.MILLISECONDS.toHours(driveringStatusViewDto.get(i).getLDateTime()-lDateTime);
//					}else if(lastStatus.equals("OnSleep") && lDateTime>0) {
//						fromOnSleep = lDateTime;
//						toOnSleep = driveringStatusViewDto.get(i).getLDateTime();
//						onSleep = TimeUnit.MILLISECONDS.toHours(driveringStatusViewDto.get(i).getLDateTime()-lDateTime);
//					}else if(lastStatus.equals("OffDuty") && lDateTime>0) {
//						fromOffDuty = lDateTime;
//						toOffDuty = driveringStatusViewDto.get(i).getLDateTime();
//						offDuty = TimeUnit.MILLISECONDS.toHours(driveringStatusViewDto.get(i).getLDateTime()-lDateTime);
//					}
//					lastStatus = driveringStatusViewDto.get(i).getStatus();
//					lDateTime = driveringStatusViewDto.get(i).getLDateTime();
//				}
//				
//				if(lastStatus.equals("OnDuty") && lDateTime>0) {
//					fromOnDuty = lDateTime;
//					toOnDuty = instant.toEpochMilli();
//					onDuty = TimeUnit.MILLISECONDS.toHours(instant.toEpochMilli()-lDateTime);
//				}else if(lastStatus.equals("OnDrive") && lDateTime>0) {
//					fromOnDrive = lDateTime;
//					toOnDrive = instant.toEpochMilli();
//					onDrive = TimeUnit.MILLISECONDS.toHours(instant.toEpochMilli()-lDateTime);
//				}else if(lastStatus.equals("OnSleep") && lDateTime>0) {
//					fromOnSleep = lDateTime;
//					toOnSleep = instant.toEpochMilli();
//					onSleep = TimeUnit.MILLISECONDS.toHours(instant.toEpochMilli()-lDateTime);
//				}else if(lastStatus.equals("OffDuty") && lDateTime>0) {
//					fromOffDuty = lDateTime;
//					toOffDuty = instant.toEpochMilli();
//					offDuty = TimeUnit.MILLISECONDS.toHours(instant.toEpochMilli()-lDateTime);
//				}
//				
//				DriverWorkingStatusViewDto driverStatus = new DriverWorkingStatusViewDto();
//				driverStatus.setDriverId(driverId);
//				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int) driverId);
//				driverStatus.setDriverName(empInfo.getTitle()+" "+empInfo.getFirstName()+" "+empInfo.getLastName());
//				driverStatus.setFromOnDuty(fromOnDuty);
//				driverStatus.setToOnDuty(toOnDuty);
//				driverStatus.setOnDuty(onDuty);
//				driverStatus.setFromOnDrive(fromOnDrive);
//				driverStatus.setToOnDrive(toOnDrive);
//				driverStatus.setOnDrive(onDrive);
//				driverStatus.setFromOnSleep(fromOnSleep);
//				driverStatus.setToOnSleep(fromOnSleep);
//				driverStatus.setOnSleep(onSleep);
//				driverStatus.setFromOffDuty(fromOffDuty);
//				driverStatus.setToOffDuty(toOffDuty);
//				driverStatus.setOffDuty(offDuty);
				
				result.setResult(driveringStatusViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Status Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<LiveDataLogViewDto>> ViewLiveDataLog(ClientMasterCRUDDto clientMasterCRUDDto) {
		ResultWrapper<List<LiveDataLogViewDto>> result = new ResultWrapper<>();
		try {
			long clientId = clientMasterCRUDDto.getClientId();
			
			List<LiveDataLogViewDto> liveDataLog = lookupLiveDataLogByClientOperation(clientId);
			for(int i=0;i<liveDataLog.size();i++) {
				if(liveDataLog.get(i).getVehicleId()!=null && !liveDataLog.get(i).getVehicleId().equals("")) {
					try {
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId(Integer.parseInt(liveDataLog.get(i).getVehicleId()));
						liveDataLog.get(i).setVehicleName(vehcileInfo.getVehicleNo());
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
				}
				if(liveDataLog.get(i).getDriverId()!=null && !liveDataLog.get(i).getDriverId().equals("")) {
					try {
						EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId(Integer.parseInt(liveDataLog.get(i).getDriverId()));
						liveDataLog.get(i).setDriverName(empDetails.getFirstName()+" "+empDetails.getLastName());
						liveDataLog.get(i).setMobileNo(empDetails.getMobileNo());
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			
			result.setResult(liveDataLog);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Live Data Log Information Send Successfully");
					
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<LiveDataLogViewDto> lookupLiveDataLogByClientOperation(long clientId){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("clientId").is(clientId)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"MAC","AmbTemp","DateTime","FuelEconomy","FuelRate","IdleHours","Lattitude","Longitude","Model","Odometer",
				"SatStatus","SerialNo","Speed","TotalFuelIdle","TotalFuelUsed","VIN","Version","receive_time","PlaceAddress",
				"DriverId","VehicleId").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,sort(Direction.ASC, "DateTime"));
        List<LiveDataLogViewDto> results = mongoTemplate.aggregate(aggregation, "live_data_log" , LiveDataLogViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<LiveDataLogViewDto>> ViewELDLogHistory(ClientMasterCRUDDto clientMasterCRUDDto) {
		ResultWrapper<List<LiveDataLogViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long vehicleId = clientMasterCRUDDto.getVehicleId();
			String sFromDate = clientMasterCRUDDto.getFromDate();
			String sToDate = clientMasterCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(sFromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(sToDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			sDebug+=" >> "+vehicleId+" :: "+from+" :: "+to+",";
			
			List<LiveDataLogViewDto> liveDataLog = lookupELDLogHistoryOperation(String.valueOf(vehicleId), from, to);
			sDebug+="Size : "+liveDataLog.size()+",";
			for(int i=0;i<liveDataLog.size();i++) {
//				0.62137119
				liveDataLog.get(i).setSpeed(Math.round(Double.valueOf(String.format("%.2f", (liveDataLog.get(i).getSpeed()*0.62137119)))));
				if(liveDataLog.get(i).getVehicleId()!=null && !liveDataLog.get(i).getVehicleId().equals("")) {
					VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId(Integer.parseInt(liveDataLog.get(i).getVehicleId()));
					liveDataLog.get(i).setVehicleName(vehcileInfo.getVehicleNo());
				}
				if(liveDataLog.get(i).getDriverId()!=null && !liveDataLog.get(i).getDriverId().equals("")) {
					EmployeeMaster empDetails = employeeMasterRepo.findByEmployeeId(Integer.parseInt(liveDataLog.get(i).getDriverId()));
					liveDataLog.get(i).setDriverName(empDetails.getFirstName()+" "+empDetails.getLastName());
					liveDataLog.get(i).setMobileNo(empDetails.getMobileNo());
				}
				
				if(liveDataLog.get(i).getStateId()>0) {
					StateMaster stateInfo = stateMasterRepo.findByStateId((int)liveDataLog.get(i).getStateId());
					liveDataLog.get(i).setStateName(stateInfo.getStateName());
					liveDataLog.get(i).setStateCode(stateInfo.getStateCode());
				}
				
			}
			
			result.setResult(liveDataLog);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Live Data Log Information Send Successfully"+sDebug);
					
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateStateInEldLogData(ClientMasterCRUDDto clientMasterCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
//			List<ELDLogData> eldLogData = eldLogDataRepo.findAll();
			String sFromDate = "2025-02-10 00:00:00";
			String sToDate = "2025-02-12 23:59:59";
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(sFromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(sToDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			List<ELDLogData> eldLogData = lookupELDLogHistoryByDateOperation(from,to);
			for(int i=0;i<eldLogData.size();i++) {
				double dLattitude = eldLogData.get(i).getLattitude();
		        double dLongitude = eldLogData.get(i).getLongitude();
		        sDebug+="3,";
		        
		        double[] point = {dLongitude,dLattitude};
		        try {
		        	GeofanceMaster lstGeofacne = geofanceMasterRepo.findByAreaIntersects(point);
		        	List<StateMaster> states = stateMasterRepo.findAndViewByGeofanceId(lstGeofacne.getGeoId());
		        	
		        	Query query = new Query();
					query.addCriteria(
		        			Criteria.where("utcDateTime").is(eldLogData.get(i).getUtcDateTime())
		        			.and("DriverId").is(eldLogData.get(i).getDriverId())
		        		);
					
			        Update update = new Update();
			        update.set("stateId", states.get(0).getStateId());
			        update.set("geoStateId", lstGeofacne.getStateId());
//			        mongoTemplate.findAndModify(query, update, ELDLogData.class);
			        mongoTemplate.updateMulti(query, update, ELDLogData.class);
			        
		        }catch(Exception ex) {
		        	ex.printStackTrace();
		        }
			}
			
			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("ELD Log Updated Successfully");
					
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<ELDLogData> lookupELDLogHistoryByDateOperation(long from, long to){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"MAC","AmbTemp","DateTime","FuelEconomy","FuelRate","IdleHours","Lattitude","Longitude","Model","Odometer",
				"SatStatus","SerialNo","Speed","TotalFuelIdle","TotalFuelUsed","VIN","Version","receive_time","PlaceAddress",
				"DriverId","VehicleId","utcDateTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
        List<ELDLogData> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , ELDLogData.class).getMappedResults();
        return results;
		
    }
	
	public List<LiveDataLogViewDto> lookupLiveDataLogOperation(){
		
		MatchOperation filter = Aggregation.match(new Criteria());
		
		ProjectionOperation projectStage = Aggregation.project(
				"MAC","AmbTemp","DateTime","FuelEconomy","FuelRate","IdleHours","Lattitude","Longitude","Model","Odometer",
				"SatStatus","SerialNo","Speed","TotalFuelIdle","TotalFuelUsed","VIN","Version","receive_time","PlaceAddress",
				"DriverId","VehicleId").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage);
        List<LiveDataLogViewDto> results = mongoTemplate.aggregate(aggregation, "live_data_log" , LiveDataLogViewDto.class).getMappedResults();
        return results;
		
    }
	
	public List<LiveDataLogViewDto> lookupELDLogHistoryOperation(String vehicleId, long from, long to){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
					.and("VehicleId").is(vehicleId)
					.and("Speed").gt(0)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"MAC","AmbTemp","DateTime","FuelEconomy","FuelRate","IdleHours","Lattitude","Longitude","Model","Odometer",
				"SatStatus","SerialNo","Speed","TotalFuelIdle","TotalFuelUsed","VIN","Version","receive_time","PlaceAddress",
				"DriverId","VehicleId","utcDateTime","stateId").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
        List<LiveDataLogViewDto> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , LiveDataLogViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<String> ViewIdlingReport(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long vehicleId = driveringStatusCRUDDto.getVehicleId();
			String sFromDate = driveringStatusCRUDDto.getFromDate();
			String sToDate = driveringStatusCRUDDto.getToDate();
			long clientId = driveringStatusCRUDDto.getClientId();
			
			Instant instant = Instant.now();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(sFromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(sToDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			sDebug+=" >> "+vehicleId+" :: "+from+" :: "+to+",";
			
			String sanitizedFrom = sFromDate.replace(":", "_").replace("-", "_").replace(" ", "_");
			String sanitizedTo = sToDate.replace(":", "_").replace("-", "_").replace(" ", "_");
			
			String isIdlingReportExist = "false";
			List<IdlingReportViewDto> idlingReport = lookupIdlingReportOperation(String.valueOf(vehicleId),from,to,clientId);
			sDebug+="Size : "+idlingReport.size()+",";
			for(int i=0;i<idlingReport.size();i++) {
				idlingReport.get(i).setFromDate(sFromDate);
				idlingReport.get(i).setToDate(sToDate);
				
				if(idlingReport.get(i).getClientId()>0) {
					ClientMaster clientInfo = clientMasterRepo.findByClientId((int)idlingReport.get(i).getClientId());
					idlingReport.get(i).setClientName(clientInfo.getClientName());
				}
				
				if(idlingReport.get(i).getVehicleId()!=null && !idlingReport.get(i).getVehicleId().equals("")) {
					try {
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId(Integer.parseInt(idlingReport.get(i).getVehicleId()));
						idlingReport.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
					}catch(Exception ex) {
						ex.printStackTrace();
						idlingReport.get(i).setVehicleNo("");
					}
				}
				
			}
			
			String outputPath = WEB_URL_FILE_UPLOAD+"/uploads/idling_reports/"+sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf";
			generateIdlingReportPdf(idlingReport, outputPath);
			String sUrl = WEB_URL_BASE_PATH + "/uploads/idling_reports/" +sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf";
			
			result.setToken(isIdlingReportExist);
			result.setResult(sUrl);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Idling Report Information Send Successfully"+sDebug);
				
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public void generateIdlingReportPdf(List<IdlingReportViewDto> report, String outputPath) throws Exception {
	    Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36); // Landscape for wide table
	    File file = new File(outputPath);
	    file.getParentFile().mkdirs();

	    PdfWriter.getInstance(document, new FileOutputStream(file));
	    document.open();

	    // Fonts
	    Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
	    Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	    Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
	    Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	    Font whiteBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

	    BaseColor headerBgColor = new BaseColor(0x04, 0x35, 0x6D);

	    // Title
	    Paragraph title = new Paragraph(report.get(0).getClientName(), titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(5);
	    document.add(title);

	    Paragraph subtitle = new Paragraph("Idling over 1 minutes", sectionFont);
	    subtitle.setAlignment(Element.ALIGN_CENTER);
	    subtitle.setSpacingAfter(20);
	    document.add(subtitle);

	    // Date Period Table (2 columns: DATE / PERIOD)
	    PdfPTable datePeriodTable = new PdfPTable(2);
	    datePeriodTable.setWidthPercentage(60); // center smaller table
	    datePeriodTable.setHorizontalAlignment(Element.ALIGN_CENTER);
	    datePeriodTable.setSpacingAfter(20);

	    // Header row
	    PdfPCell dateHeader = new PdfPCell(new Phrase("DATE", boldFont));
	    PdfPCell periodHeader = new PdfPCell(new Phrase("PERIOD", boldFont));
	    dateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
	    periodHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
	    dateHeader.setBorder(Rectangle.BOTTOM);
	    periodHeader.setBorder(Rectangle.BOTTOM);
	    datePeriodTable.addCell(dateHeader);
	    datePeriodTable.addCell(periodHeader);

	    // Values row
//	    String reportDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
	    String reportDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//	    String period = report.get(0).getFromDate() + " - " + report.get(0).getToDate();

	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String fromDate = LocalDateTime.parse(report.get(0).getFromDate(), inputFormatter).format(outputFormatter);
	    String toDate = LocalDateTime.parse(report.get(0).getToDate(), inputFormatter).format(outputFormatter);
	    String period = fromDate + " - " + toDate;
	    
	    PdfPCell dateValue = new PdfPCell(new Phrase(reportDate, bodyFont));
	    PdfPCell periodValue = new PdfPCell(new Phrase(period, bodyFont));
	    dateValue.setHorizontalAlignment(Element.ALIGN_CENTER);
	    periodValue.setHorizontalAlignment(Element.ALIGN_CENTER);
	    dateValue.setBorder(Rectangle.NO_BORDER);
	    periodValue.setBorder(Rectangle.NO_BORDER);

	    datePeriodTable.addCell(dateValue);
	    datePeriodTable.addCell(periodValue);

	    document.add(datePeriodTable);

	    // Table (Vehicle / Start / End / Duration / Location)
	    PdfPTable table = new PdfPTable(5);
	    table.setWidthPercentage(100);

	    // Narrow columns for vehicle, times, duration; wide for location
	    table.setWidths(new float[]{2, 3, 3, 2, 8});

	    // Header Row
	    Stream.of("VEHICLE", "START TIME", "END TIME", "DURATION", "LOCATION").forEach(col -> {
	        PdfPCell cell = new PdfPCell(new Phrase(col, whiteBoldFont));
	        cell.setBackgroundColor(headerBgColor);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(4f); // smaller padding
	        table.addCell(cell);
	    });

	    // Small font for rows
	    Font smallFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

	    // Data Rows
	    for (IdlingReportViewDto row : report) {
	        PdfPCell vehicleCell = new PdfPCell(new Phrase(row.getVehicleNo(), smallFont));
	        vehicleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        vehicleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        vehicleCell.setPadding(3f);

	        PdfPCell startCell = new PdfPCell(new Phrase(row.getStartDateTime(), smallFont));
	        startCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        startCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        startCell.setPadding(3f);

	        PdfPCell endCell = new PdfPCell(new Phrase(row.getEndDateTime(), smallFont));
	        endCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        endCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        endCell.setPadding(3f);

	        String durationFormatted = convertSecondsToTime(row.getDurationMillis() / 1000);
	        PdfPCell durationCell = new PdfPCell(new Phrase(durationFormatted, smallFont));
	        durationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        durationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        durationCell.setPadding(3f);

	        PdfPCell locationCell = new PdfPCell(new Phrase(row.getStartAddress(), smallFont));
	        locationCell.setHorizontalAlignment(Element.ALIGN_LEFT); // location stays left
	        locationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        locationCell.setPadding(3f);

	        table.addCell(vehicleCell);
	        table.addCell(startCell);
	        table.addCell(endCell);
	        table.addCell(durationCell);
	        table.addCell(locationCell);
	    }

	    document.add(table);
	    document.close();
	}

	
	public List<IdlingReportViewDto> lookupIdlingReportOperation(String vehicleId, long from, long to, long clientId){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("startUtcDateTime").gte(from).lte(to)
					.and("vehicleId").is(vehicleId)
					.and("clientId").is(clientId)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","vehicleId","clientId","VIN","Version","MAC","Model","SerialNo","startDateTime","endDateTime","startUtcDateTime","endUtcDateTime",
				"durationMillis","startAddress","endAddress","startOdometer","endOdometer","startEngineHours","endEngineHours").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,
	    		sort(Direction.ASC, "startUtcDateTime"));
        List<IdlingReportViewDto> results = mongoTemplate.aggregate(aggregation, "idling_report" , IdlingReportViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<IftaReportViewDto>> ViewIftaReport(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<IftaReportViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long vehicleId = driveringStatusCRUDDto.getVehicleId();
			String sFromDate = driveringStatusCRUDDto.getFromDate();
			String sToDate = driveringStatusCRUDDto.getToDate();
			
			Instant instant = Instant.now();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(sFromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(sToDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			sDebug+=" >> "+vehicleId+" :: "+from+" :: "+to+",";
			
			String sanitizedFrom = sFromDate.replace(":", "_").replace("-", "_").replace(" ", "_");
			String sanitizedTo = sToDate.replace(":", "_").replace("-", "_").replace(" ", "_");
			
			String isIftaReportExist = "false";
			List<IftaReportViewDto> iftaReport = null;
			List<IFTAReports> iftaData = iftaReportsRepo.findAndViewIftaReport(String.valueOf(vehicleId),sFromDate,sToDate);
			String companyName="";
			if(iftaData.size()<=0) {
				isIftaReportExist="false";
				iftaReport = lookupIftaReportOperation(String.valueOf(vehicleId), from, to);
				sDebug+="Size : "+iftaReport+",";
				for(int i=0;i<iftaReport.size();i++) {
					
					if(iftaReport.get(i).getClientId()>0) {
						ClientMaster clientInfo = clientMasterRepo.findByClientId((int)iftaReport.get(i).getClientId());
						companyName = clientInfo.getClientName();
					}
					
					iftaReport.get(i).setCarrierName(companyName);
					iftaReport.get(i).setFromDate(sFromDate);
					iftaReport.get(i).setToDate(sToDate);
					
					try {
						Pageable pageableRequest = PageRequest.of(0, 100, Sort.by("utcDateTime").ascending());
						Query query = new Query();
				        query.addCriteria(
				        			Criteria.where("utcDateTime").gte(from).lte(to)
				        			.and("VehicleId").is(String.valueOf(vehicleId))
									.and("stateId").is(iftaReport.get(i).getStateId())
				        		);
						query.with(pageableRequest);
//				        query.with(Sort.by(Sort.Direction.ASC, "_id"));
						List<EldLogDataViewDto> eldLogData = mongoTemplate.find(query, EldLogDataViewDto.class,"eld_log_data");
//						iftaReport.get(i).setFirstOdometer(Math.round(Double.parseDouble(eldLogData.get(0).getOdometer())*0.62137119));
						
						for(int e=0;e<eldLogData.size();e++) {
							if(Double.parseDouble(eldLogData.get(e).getOdometer())>0) {
								iftaReport.get(i).setFirstOdometer(Math.round(Double.parseDouble(eldLogData.get(e).getOdometer())*0.62137119));
								break;
							}
						}
						
						pageableRequest = PageRequest.of(0, 100, Sort.by("utcDateTime").descending());
						query = new Query();
						query.addCriteria(
			        			Criteria.where("utcDateTime").gte(from).lte(to)
			        			.and("VehicleId").is(String.valueOf(vehicleId))
								.and("stateId").is(iftaReport.get(i).getStateId())
			        		);
						query.with(pageableRequest);
//						query.with(Sort.by(Sort.Direction.DESC, "_id"));
						eldLogData = mongoTemplate.find(query, EldLogDataViewDto.class,"eld_log_data");
//						iftaReport.get(i).setLastOdometer(Math.round(Double.parseDouble(eldLogData.get(0).getOdometer())*0.62137119));
						
						for(int e=0;e<eldLogData.size();e++) {
							if(Double.parseDouble(eldLogData.get(e).getOdometer())>0) {
								iftaReport.get(i).setLastOdometer(Math.round(Double.parseDouble(eldLogData.get(e).getOdometer())*0.62137119));
								break;
							}
						}
						
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					
//					iftaReport.get(i).setFirstOdometer(Math.round(iftaReport.get(i).getFirstOdometer()));
//					iftaReport.get(i).setLastOdometer(Math.round(iftaReport.get(i).getLastOdometer()));
					
					if(iftaReport.get(i).getVehicleId()!=null && !iftaReport.get(i).getVehicleId().equals("")) {
						try {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId(Integer.parseInt(iftaReport.get(i).getVehicleId()));
							iftaReport.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
							iftaReport.get(i).setMake(vehcileInfo.getMake());
							iftaReport.get(i).setVin(vehcileInfo.getVin());
							iftaReport.get(i).setModel(vehcileInfo.getModel());
							iftaReport.get(i).setManufacturingYear(vehcileInfo.getManufacturingYear());
						}catch(Exception ex) {
							ex.printStackTrace();
							iftaReport.get(i).setVehicleNo("");
							iftaReport.get(i).setMake("");
							iftaReport.get(i).setVin("");
							iftaReport.get(i).setModel("");
							iftaReport.get(i).setManufacturingYear(0);
						}
					}
					
					if(iftaReport.get(i).getStateId()>0) {
						double lastLat=0,lastLng=0, TOTAL_KM=0;
						long timeDiff=0, lastTimeStamp=0;
						sDebug+=" >> state ID : "+iftaReport.get(i).getStateId()+",";
						List<IftaReportViewDto> eldLogData = lookupAllELDLogDataByVehicleOperation(String.valueOf(vehicleId), from, to, iftaReport.get(i).getStateId());
						sDebug+=" >> Size : "+eldLogData.size()+",";
						for(int e=0;e<eldLogData.size();e++) {
							if(lastLat!=0 && lastLat!=eldLogData.get(e).getLattitude()){
								double KM=calculateDistanceInMeters(eldLogData.get(e).getLattitude(),eldLogData.get(e).getLongitude(),lastLat,lastLng);
					            timeDiff = (eldLogData.get(e).getUtcDateTime() - lastTimeStamp)/1000;
					            if((KM/1000)<=5 && timeDiff<=600){
					              TOTAL_KM+= KM/1000;
					            }
							}
							lastLat = eldLogData.get(e).getLattitude();
							lastLng = eldLogData.get(e).getLongitude();
							lastTimeStamp = eldLogData.get(e).getUtcDateTime();
						}
						iftaReport.get(i).setGpsKm(Math.round(Double.valueOf(String.format("%.2f", (TOTAL_KM*0.62137119)))));
						try {
							StateMaster stateInfo = stateMasterRepo.findByStateId((int)iftaReport.get(i).getStateId());
							iftaReport.get(i).setStateName(stateInfo.getStateName());
							iftaReport.get(i).setStateCode(stateInfo.getStateCode());
						}catch(Exception ex) {
							ex.printStackTrace();
							iftaReport.get(i).setStateName("");
							iftaReport.get(i).setStateCode("");
						}
						
					}
					iftaReport.get(i).setCurrentTimestamp(instant.toEpochMilli());
					iftaReport.get(i).setFileName(sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf");
					
				}
				
				
				String outputPath = WEB_URL_FILE_UPLOAD+"/uploads/ifta_reports/"+sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf";
				generateFullStyledPdf(iftaReport, outputPath);
				
				// save generated ifta reports
//				if(driveringStatusCRUDDto.getIsGenerated()>0) {
					IFTAReports iftaReports = new IFTAReports();
					for(int i=0;i<iftaReport.size();i++) {
						iftaReports = new IFTAReports();
						iftaReports.setCarrierName(iftaReport.get(i).getCarrierName());
						iftaReports.setFromDate(sFromDate);
						iftaReports.setToDate(sToDate);
						iftaReports.setVehicleNo(iftaReport.get(i).getVehicleNo());
						iftaReports.setVehicleId(iftaReport.get(i).getVehicleId());
						iftaReports.setDriverId(iftaReport.get(i).getDriverId());
						iftaReports.setClientId(driveringStatusCRUDDto.getClientId());
						iftaReports.setStateId(iftaReport.get(i).getStateId());
						iftaReports.setUtcDateTime(iftaReport.get(i).getUtcDateTime());
						iftaReports.setMake(iftaReport.get(i).getMake());
						iftaReports.setVin(iftaReport.get(i).getVin());
						iftaReports.setModel(iftaReport.get(i).getModel());
						iftaReports.setManufacturingYear(iftaReport.get(i).getManufacturingYear());
						iftaReports.setStateName(iftaReport.get(i).getStateName());
						iftaReports.setStateCode(iftaReport.get(i).getStateCode());
						iftaReports.setFirstOdometer(iftaReport.get(i).getFirstOdometer());
						iftaReports.setLastOdometer(iftaReport.get(i).getLastOdometer());
						iftaReports.setLattitude(iftaReport.get(i).getLattitude());
						iftaReports.setLongitude(iftaReport.get(i).getLongitude());
						iftaReports.setGpsKm(iftaReport.get(i).getGpsKm());
						iftaReports.setCurrentTimestamp(iftaReport.get(i).getCurrentTimestamp());
						iftaReports.setFileName(sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf");
						
						iftaReportsRepo.save(iftaReports);
						
					}
//				}
				
					result.setToken(isIftaReportExist);
					result.setResult(iftaReport);
					result.setStatus(Result.SUCCESS);
					result.setMessage("IFTA Report Information Send Successfully"+sDebug);
					
			}else {
				isIftaReportExist="true";
				
				result.setToken(isIftaReportExist);
				result.setResult(iftaReport);
				result.setStatus(Result.FAIL);
				result.setMessage("IFTA Report Already Generated.");
			}
				
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<IftaReportViewDto>> ViewIftaReportNew(DriveringStatusCRUDDto driveringStatusCRUDDto) {
	    ResultWrapper<List<IftaReportViewDto>> result = new ResultWrapper<>();
	    String sDebug = "";
	    try {
	        long vehicleId = driveringStatusCRUDDto.getVehicleId();
	        String sFromDate = driveringStatusCRUDDto.getFromDate();
	        String sToDate = driveringStatusCRUDDto.getToDate();

	        Instant instant = Instant.now();

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        LocalDateTime ldtFromDate = LocalDateTime.parse(sFromDate, formatter);
	        long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        LocalDateTime ldtToDate = LocalDateTime.parse(sToDate, formatter);
	        long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        sDebug += " >> " + vehicleId + " :: " + from + " :: " + to + ",";

	        String sanitizedFrom = sFromDate.replace(":", "_").replace("-", "_").replace(" ", "_");
	        String sanitizedTo = sToDate.replace(":", "_").replace("-", "_").replace(" ", "_");

	        String isIftaReportExist = "false";
	        List<IftaReportViewDto> iftaReport = null;
	        List<IFTAReports> iftaData = iftaReportsRepo.findAndViewIftaReport(String.valueOf(vehicleId), sFromDate, sToDate);
	        String companyName = "";
	        long clientId=0;

	        if (iftaData.size() <= 0) {
	            isIftaReportExist = "false";

//	            Query query1 = new Query(Criteria.where("utcDateTime").gte(from).lte(to)
//	                    .and("VehicleId").is(String.valueOf(vehicleId))
//	                    .and("stateId").gt(0)
//	                    .and("Speed").gt(2));
//	            query1.with(Sort.by(Sort.Direction.ASC, "utcDateTime"));
//
//	            iftaReport = mongoTemplate.find(query1, IftaReportViewDto.class, "eld_log_data");
	            
				iftaReport = lookupIftaReportNewOperation(String.valueOf(vehicleId), from, to);

	            sDebug += "Size : " + iftaReport.size() + ",";
//	            SaveLog(sDebug);
	            // -------------------------
	            // Process into state segments
	            // -------------------------
	            List<IftaReportViewDto> processedList = new ArrayList<>();

	            String prevState = null, prevStateCode=null;
	            double firstOdometer = 0.0;
	            double lastOdometer = 0.0;
	            IftaReportViewDto lastData=null;
	            
	            double lastLat=0,lastLng=0, TOTAL_KM=0;
				long timeDiff=0, lastTimeStamp=0;
				
//	            SaveLog(sDebug);
	            for (int i = 0; i < iftaReport.size(); i++) {
	                IftaReportViewDto current = iftaReport.get(i);
	                sDebug+="1,";
	                // enrich current record (company, vehicle, state)
	                if (current.getClientId() > 0) {
	                	clientId = current.getClientId();
	                    ClientMaster clientInfo = clientMasterRepo.findByClientId((int) current.getClientId());
	                    companyName = clientInfo.getClientName();
	                }
	                sDebug+="2,";
	                current.setCarrierName(companyName);
	                current.setFromDate(sFromDate);
	                current.setToDate(sToDate);
	                current.setVehicleId(String.valueOf(vehicleId));
	                current.setClientId(clientId);
	                sDebug+="3,";
//	                if (current.getVehicleId() != null && !current.getVehicleId().equals("")) {
	                    try {
//	                        VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId(Integer.parseInt(current.getVehicleId()));
	                    	VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int) vehicleId);
	                        current.setVehicleNo(vehcileInfo.getVehicleNo());
	                        current.setMake(vehcileInfo.getMake());
	                        current.setVin(vehcileInfo.getVin());
	                        current.setModel(vehcileInfo.getModel());
	                        current.setManufacturingYear(vehcileInfo.getManufacturingYear());
	                    } catch (Exception ex) {
	                        ex.printStackTrace();
	                        current.setVehicleNo("");
	                        current.setMake("");
	                        current.setVin("");
	                        current.setModel("");
	                        current.setManufacturingYear(0);
	                    }
//	                }
                    sDebug+="4,";    
	                try {
	                    StateMaster stateInfo = stateMasterRepo.findByStateId((int) current.getStateId());
	                    current.setStateName(stateInfo.getStateName());
	                    current.setStateCode(stateInfo.getStateCode());
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    current.setStateName("");
	                    current.setStateCode("");
	                }
	                sDebug+="5,";
	                current.setCurrentTimestamp(instant.toEpochMilli());
	                current.setFileName(sanitizedFrom + "_" + sanitizedTo + "_" + vehicleId + ".pdf");

	                // -------------------------
	                // Odometer calculation
	                // -------------------------
	                String currentState = current.getStateName();
	                String currentStateCode = current.getStateCode();
	                double currentOdo = Double.parseDouble(current.getOdometer());
	                sDebug+="6,";
	                if (prevState == null) {
	                    prevState = currentState;
	                    prevStateCode = currentStateCode;
	                    firstOdometer = currentOdo;
	                    lastOdometer = currentOdo;
	                    lastData = current;
	                    
	                    lastLat = current.getLattitude();
						lastLng = current.getLongitude();
						lastTimeStamp = current.getUtcDateTime();
						
	                    continue;
	                }
	                sDebug+="7,";
	                if (lastLat != 0 && lastLat != current.getLattitude()) {
//	                	SaveLog(" lastLat: "+lastLat+", ");
	                    double KM = calculateDistanceInMeters(
	                        current.getLattitude(), current.getLongitude(),
	                        lastLat, lastLng
	                    );
	                    timeDiff = (current.getUtcDateTime() - lastTimeStamp) / 1000;
//	                    SaveLog(" Meters: "+KM+" :: Diff -> "+timeDiff+",");
	                    if ((KM / 1000) <= 5 && timeDiff <= 600) { // filter bad points
	                        TOTAL_KM += KM / 1000;
	                    }
	                }
	                lastLat = current.getLattitude();
	                lastLng = current.getLongitude();
	                lastTimeStamp = current.getUtcDateTime();
	                sDebug+="8,";
//	                SaveLog(prevState+" :: "+currentState+"\n");
	                if (currentState.equals(prevState)) {
	                    lastOdometer = currentOdo;
	                } else {
//	                	SaveLog("KM : "+TOTAL_KM+"\n");
	                    // save completed segment
	                    IftaReportViewDto segment = new IftaReportViewDto();
	                    segment.setStateId(lastData.getStateId());
	                    segment.setStateName(prevState);
	                    segment.setStateCode(prevStateCode);
	                    segment.setFirstOdometer(Math.round(firstOdometer*0.62137119));
	                    segment.setLastOdometer(Math.round(lastOdometer*0.62137119));
	                    segment.setTotalOdometer(Math.round((lastOdometer - firstOdometer)*0.62137119));
	                    segment.setGpsKm(Math.round(TOTAL_KM * 0.62137119));
	                    segment.setVehicleNo(lastData.getVehicleNo());
	                    segment.setVehicleId(lastData.getVehicleId());
	                    segment.setClientId(lastData.getClientId());
	                    segment.setDriverId(lastData.getDriverId());
	                    segment.setCarrierName(lastData.getCarrierName());
	                    segment.setFromDate(lastData.getFromDate());
	                    segment.setToDate(lastData.getToDate());
	                    segment.setMake(lastData.getMake());
	                    segment.setVin(lastData.getVin());
	                    segment.setModel(lastData.getModel());
	                    segment.setManufacturingYear(lastData.getManufacturingYear());
	                    segment.setCurrentTimestamp(lastData.getCurrentTimestamp());
	                    segment.setUtcDateTime(lastData.getUtcDateTime());
	                    segment.setLattitude(lastData.getLattitude());
	                    segment.setLongitude(lastData.getLongitude());
	                    segment.setFileName(lastData.getFileName());

	                    processedList.add(segment);
	                    sDebug+="9,";
	                    // start new state segment
	                    prevState = currentState;
	                    prevStateCode = currentStateCode;
	                    firstOdometer = currentOdo;
	                    lastOdometer = currentOdo;
	                    lastData = current;
	                    TOTAL_KM=0;
	                }
	            }
	            sDebug+="10,";
	            // Add last state segment
	            if (prevState != null) {
	            	IftaReportViewDto segment = new IftaReportViewDto();
	            	segment.setStateId(lastData.getStateId());
                    segment.setStateName(prevState);
                    segment.setStateCode(prevStateCode);
                    segment.setFirstOdometer(Math.round(firstOdometer*0.62137119));
                    segment.setLastOdometer(Math.round(lastOdometer*0.62137119));
                    segment.setTotalOdometer(Math.round((lastOdometer - firstOdometer)*0.62137119));
                    segment.setGpsKm(Math.round(TOTAL_KM * 0.62137119));
                    segment.setVehicleNo(lastData.getVehicleNo());
                    segment.setVehicleId(lastData.getVehicleId());
                    segment.setClientId(lastData.getClientId());
                    segment.setDriverId(lastData.getDriverId());
                    segment.setCarrierName(lastData.getCarrierName());
                    segment.setFromDate(lastData.getFromDate());
                    segment.setToDate(lastData.getToDate());
                    segment.setMake(lastData.getMake());
                    segment.setVin(lastData.getVin());
                    segment.setModel(lastData.getModel());
                    segment.setManufacturingYear(lastData.getManufacturingYear());
                    segment.setCurrentTimestamp(lastData.getCurrentTimestamp());
                    segment.setUtcDateTime(lastData.getUtcDateTime());
                    segment.setLattitude(lastData.getLattitude());
                    segment.setLongitude(lastData.getLongitude());
                    segment.setFileName(lastData.getFileName());
                    
	                processedList.add(segment);
	            }
	            sDebug+="11,";
	            Map<String, IftaReportViewDto> mergedMap = new LinkedHashMap<>();
	            for (IftaReportViewDto seg : processedList) {
	                String key = seg.getStateCode(); // Or seg.getStateName()

	                if (!mergedMap.containsKey(key)) {
	                    // clone segment for the first entry
	                    mergedMap.put(key, seg);
	                } else {
	                    IftaReportViewDto existing = mergedMap.get(key);

	                    // Sum totalOdometer and gpsKm
	                    existing.setTotalOdometer(existing.getTotalOdometer() + seg.getTotalOdometer());
	                    existing.setGpsKm(existing.getGpsKm() + seg.getGpsKm());

	                    // First odometer = min
	                    if (seg.getFirstOdometer() < existing.getFirstOdometer()) {
	                        existing.setFirstOdometer(seg.getFirstOdometer());
	                    }

	                    // Last odometer = max
	                    if (seg.getLastOdometer() > existing.getLastOdometer()) {
	                        existing.setLastOdometer(seg.getLastOdometer());
	                    }
	                }
	            }
	            sDebug+="12,";
	            List<IftaReportViewDto> finalList = new ArrayList<>(mergedMap.values());
	            
	            String outputPath = WEB_URL_FILE_UPLOAD+"/uploads/ifta_reports/"+sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf";
	            generateFullStyledNewPdf(finalList, outputPath);
	            sDebug+="13,";
				IFTAReports iftaReports = new IFTAReports();
				for(int i=0;i<finalList.size();i++) {
					iftaReports = new IFTAReports();
					iftaReports.setCarrierName(finalList.get(i).getCarrierName());
					iftaReports.setFromDate(sFromDate);
					iftaReports.setToDate(sToDate);
					iftaReports.setVehicleNo(finalList.get(i).getVehicleNo());
					iftaReports.setVehicleId(finalList.get(i).getVehicleId());
					iftaReports.setDriverId(finalList.get(i).getDriverId());
					iftaReports.setClientId(finalList.get(i).getClientId());
					iftaReports.setStateId(finalList.get(i).getStateId());
					iftaReports.setUtcDateTime(finalList.get(i).getUtcDateTime());
					iftaReports.setMake(finalList.get(i).getMake());
					iftaReports.setVin(finalList.get(i).getVin());
					iftaReports.setModel(finalList.get(i).getModel());
					iftaReports.setManufacturingYear(finalList.get(i).getManufacturingYear());
					iftaReports.setStateName(finalList.get(i).getStateName());
					iftaReports.setStateCode(finalList.get(i).getStateCode());
					iftaReports.setFirstOdometer(finalList.get(i).getFirstOdometer());
					iftaReports.setLastOdometer(finalList.get(i).getLastOdometer());
					iftaReports.setTotalOdometer(finalList.get(i).getTotalOdometer());
					iftaReports.setLattitude(finalList.get(i).getLattitude());
					iftaReports.setLongitude(finalList.get(i).getLongitude());
					iftaReports.setGpsKm(finalList.get(i).getGpsKm());
					iftaReports.setCurrentTimestamp(finalList.get(i).getCurrentTimestamp());
					iftaReports.setFileName(sanitizedFrom+"_"+sanitizedTo+"_"+vehicleId+".pdf");
					
					iftaReportsRepo.save(iftaReports);
					
				}
				sDebug+="14,";
	            result.setToken(isIftaReportExist);
	            result.setResult(finalList);
	            result.setStatus(Result.SUCCESS);
	            result.setMessage("IFTA Report Information Send Successfully " + sDebug);

	        } else {
	            isIftaReportExist = "true";

	            result.setToken(isIftaReportExist);
	            result.setResult(null);
	            result.setStatus(Result.FAIL);
	            result.setMessage("IFTA Report Already Generated."+sDebug);
	        }

	    } catch (Exception e) {
	        result.setStatus(Result.FAIL);
	        result.setMessage(e.getLocalizedMessage()+sDebug);
	    }
	    return result;
	}
	
	public void generateFullStyledPdf(List<IftaReportViewDto> report, String outputPath) throws Exception {
	    Document document = new Document(PageSize.A4, 36, 36, 36, 36);
	    File file = new File(outputPath);
	    file.getParentFile().mkdirs();
//	    SaveLog(report.toString());
	    PdfWriter.getInstance(document, new FileOutputStream(file));
	    document.open();

	    // Fonts
	    Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
	    Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	    Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	    Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	    Font whiteBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
	    Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	    Font valueFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

	    BaseColor headerBgColor = new BaseColor(0x04, 0x35, 0x6D);

	    // Title
	    Paragraph title = new Paragraph("IFTA Report", titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(10);
	    document.add(title);

	    // Subtitles
	    document.add(new Paragraph("IFTA Mileage By State", sectionFont));
	    document.add(new Paragraph("Summary of total mileage by state for all equipment", bodyFont));
	    document.add(new Paragraph(" "));

	    // Report Info
	    String period = report.get(0).getFromDate() + " To " + report.get(0).getToDate();
	    LocalDateTime dateTime = Instant.ofEpochMilli(report.get(0).getCurrentTimestamp())
	            .atZone(ZoneId.systemDefault()).toLocalDateTime();
	    String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	    document.add(new Paragraph("Carrier: " + report.get(0).getCarrierName(), bodyFont));
	    document.add(new Paragraph("Period: " + period, bodyFont));
	    document.add(new Paragraph("Report Generated:  " + formatted, bodyFont));
	    document.add(new Paragraph(" "));

	    // 1. Vehicle Info Header Row
	    PdfPTable vehicleHeader = new PdfPTable(1);
	    vehicleHeader.setWidthPercentage(100);
	    PdfPCell headerCell = new PdfPCell(new Phrase("Vehicle : " + report.get(0).getVehicleNo(), whiteBoldFont));
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    headerCell.setBackgroundColor(headerBgColor);
	    headerCell.setPadding(8f);
	    vehicleHeader.addCell(headerCell);
	    document.add(vehicleHeader);

	    // 2. Vehicle Details (2-column)
	    PdfPTable vehicleDetails = new PdfPTable(2);
	    vehicleDetails.setWidthPercentage(100);
	    vehicleDetails.setWidths(new float[]{3, 7});

	    String[][] vehicleInfo = {
	            {"NAME", report.get(0).getVehicleNo()},
	            {"MAKE", report.get(0).getMake()},
	            {"VIN", report.get(0).getVin()},
	            {"MODEL", report.get(0).getModel()},
	            {"YEAR", String.valueOf(report.get(0).getManufacturingYear())}
	    };

	    for (String[] row : vehicleInfo) {
	        PdfPCell key = new PdfPCell(new Phrase(row[0], labelFont));
	        PdfPCell val = new PdfPCell(new Phrase(row[1], valueFont));
	        key.setPadding(6);
	        val.setPadding(6);
	        vehicleDetails.addCell(key);
	        vehicleDetails.addCell(val);
	    }

	    document.add(vehicleDetails);

	    // 3. Data Table (State/Odometer/GPS)
	    PdfPTable dataTable = new PdfPTable(3);
	    dataTable.setWidthPercentage(100);
	    dataTable.setSpacingBefore(10f);
	    dataTable.setWidths(new float[]{4, 4, 4});

	    // Table headers
	    Stream.of("State", "Odometer (Miles)", "GPS (Miles)").forEach(col -> {
	        PdfPCell cell = new PdfPCell(new Phrase(col, whiteBoldFont));
	        cell.setBackgroundColor(headerBgColor);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setPadding(6f);
	        dataTable.addCell(cell);
	    });

	    // Data rows
	    for (IftaReportViewDto state : report) {
	        PdfPCell stateCell = new PdfPCell(new Phrase(state.getStateName() + " (" + state.getStateCode() + ")", bodyFont));
	        PdfPCell odoCell = new PdfPCell(new Phrase(String.valueOf(state.getLastOdometer() - state.getFirstOdometer()), bodyFont));
	        PdfPCell gpsCell = new PdfPCell(new Phrase(String.valueOf(state.getGpsKm()), bodyFont));

	        stateCell.setPadding(4f);
	        odoCell.setPadding(4f);
	        gpsCell.setPadding(4f);

	        odoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        gpsCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

	        dataTable.addCell(stateCell);
	        dataTable.addCell(odoCell);
	        dataTable.addCell(gpsCell);
	    }

	    document.add(dataTable);
	    document.close();
	}
	
	public void generateFullStyledNewPdf(List<IftaReportViewDto> report, String outputPath) throws Exception {
	    Document document = new Document(PageSize.A4, 36, 36, 36, 36);
	    File file = new File(outputPath);
	    file.getParentFile().mkdirs();
//	    SaveLog(report.toString());
	    PdfWriter.getInstance(document, new FileOutputStream(file));
	    document.open();

	    // Fonts
	    Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
	    Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	    Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	    Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	    Font whiteBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
	    Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	    Font valueFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

	    BaseColor headerBgColor = new BaseColor(0x04, 0x35, 0x6D);

	    // Title
	    Paragraph title = new Paragraph("IFTA Report", titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(10);
	    document.add(title);

	    // Subtitles
	    document.add(new Paragraph("IFTA Mileage By State", sectionFont));
	    document.add(new Paragraph("Summary of total mileage by state for all equipment", bodyFont));
	    document.add(new Paragraph(" "));

	    // Report Info
	    String period = report.get(0).getFromDate() + " To " + report.get(0).getToDate();
	    LocalDateTime dateTime = Instant.ofEpochMilli(report.get(0).getCurrentTimestamp())
	            .atZone(ZoneId.systemDefault()).toLocalDateTime();
	    String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	    document.add(new Paragraph("Carrier: " + report.get(0).getCarrierName(), bodyFont));
	    document.add(new Paragraph("Period: " + period, bodyFont));
	    document.add(new Paragraph("Report Generated:  " + formatted, bodyFont));
	    document.add(new Paragraph(" "));

	    // 1. Vehicle Info Header Row
	    PdfPTable vehicleHeader = new PdfPTable(1);
	    vehicleHeader.setWidthPercentage(100);
	    PdfPCell headerCell = new PdfPCell(new Phrase("Vehicle : " + report.get(0).getVehicleNo(), whiteBoldFont));
	    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    headerCell.setBackgroundColor(headerBgColor);
	    headerCell.setPadding(8f);
	    vehicleHeader.addCell(headerCell);
	    document.add(vehicleHeader);

	    // 2. Vehicle Details (2-column)
	    PdfPTable vehicleDetails = new PdfPTable(2);
	    vehicleDetails.setWidthPercentage(100);
	    vehicleDetails.setWidths(new float[]{3, 7});

	    String[][] vehicleInfo = {
	            {"NAME", report.get(0).getVehicleNo()},
	            {"MAKE", report.get(0).getMake()},
	            {"VIN", report.get(0).getVin()},
	            {"MODEL", report.get(0).getModel()},
	            {"YEAR", String.valueOf(report.get(0).getManufacturingYear())}
	    };

	    for (String[] row : vehicleInfo) {
	        PdfPCell key = new PdfPCell(new Phrase(row[0], labelFont));
	        PdfPCell val = new PdfPCell(new Phrase(row[1], valueFont));
	        key.setPadding(6);
	        val.setPadding(6);
	        vehicleDetails.addCell(key);
	        vehicleDetails.addCell(val);
	    }

	    document.add(vehicleDetails);

	    // 3. Data Table (State/Odometer/GPS)
	    PdfPTable dataTable = new PdfPTable(3);
	    dataTable.setWidthPercentage(100);
	    dataTable.setSpacingBefore(10f);
	    dataTable.setWidths(new float[]{4, 4, 4});

	    // Table headers
	    Stream.of("State", "Odometer (Miles)", "GPS (Miles)").forEach(col -> {
	        PdfPCell cell = new PdfPCell(new Phrase(col, whiteBoldFont));
	        cell.setBackgroundColor(headerBgColor);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setPadding(6f);
	        dataTable.addCell(cell);
	    });

	    // Data rows
	    for (IftaReportViewDto state : report) {
	        PdfPCell stateCell = new PdfPCell(new Phrase(state.getStateName() + " (" + state.getStateCode() + ")", bodyFont));
	        PdfPCell odoCell = new PdfPCell(new Phrase(String.valueOf(state.getTotalOdometer()), bodyFont));
	        PdfPCell gpsCell = new PdfPCell(new Phrase(String.valueOf(state.getGpsKm()), bodyFont));

	        stateCell.setPadding(4f);
	        odoCell.setPadding(4f);
	        gpsCell.setPadding(4f);

	        odoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        gpsCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

	        dataTable.addCell(stateCell);
	        dataTable.addCell(odoCell);
	        dataTable.addCell(gpsCell);
	    }

	    document.add(dataTable);
	    document.close();
	}
	
	public static double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        if (lat1 == lat2 && lon1 == lon2) {
            return 0;
        } else {
            double radLat1 = Math.toRadians(lat1);
            double radLat2 = Math.toRadians(lat2);
            double theta = lon1 - lon2;
            double radTheta = Math.toRadians(theta);
            double dist = Math.sin(radLat1) * Math.sin(radLat2) +
                          Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radTheta);
            
            if (dist > 1) {
                dist = 1;
            }
            
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515; // Convert to miles
            dist = dist * 1.609344; // Convert miles to kilometers
            
            return dist * 1000; // Convert kilometers to meters
        }
    }
	
	public List<IftaReportViewDto> lookupAllELDLogDataByVehicleOperation(String vehicleId, long from, long to, long stateId){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
					.and("VehicleId").is(vehicleId)
					.and("stateId").is(stateId)
					.and("Speed").gt(2)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"Odometer","VehicleId","DriverId","stateId","Lattitude","Longitude","utcDateTime").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,sort(Direction.ASC, "utcDateTime"));
        List<IftaReportViewDto> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , IftaReportViewDto.class).getMappedResults();
        return results;
		
    }
	
	public List<IftaReportViewDto> lookupIftaReportOperation(String vehicleId, long from, long to){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
					.and("VehicleId").is(vehicleId)
					.and("stateId").gt(0)
					.and("Speed").gt(2)
				);
		
		GroupOperation group1 = Aggregation.group("stateId")
				.count().as("totalCount")
                .first("Odometer").as("firstOdometer")
                .last("Odometer").as("lastOdometer")
                .last("VehicleId").as("vehicleId")
                .last("DriverId").as("driverId")
                .first("utcDateTime").as("utcDateTime")
                .first("clientId").as("clientId")
                .last("stateId").as("stateId");
		
		ProjectionOperation projectStage = Aggregation.project(
				"Odometer","VehicleId","DriverId","stateId","utcDateTime","clientId").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,group1,
	    		sort(Direction.ASC, "stateId").and(Direction.ASC, "utcDateTime"));
        List<IftaReportViewDto> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , IftaReportViewDto.class).getMappedResults();
        return results;
		
    }
	
	public List<IftaReportViewDto> lookupIftaReportNewOperation(String vehicleId, long from, long to){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("utcDateTime").gte(from).lte(to)
					.and("VehicleId").is(vehicleId)
					.and("stateId").gt(0)
					.and("Speed").gt(2)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"Odometer","VehicleId","DriverId","stateId","utcDateTime","clientId","Lattitude","Longitude").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,
	    		sort(Direction.ASC, "utcDateTime"));
        List<IftaReportViewDto> results = mongoTemplate.aggregate(aggregation, "eld_log_data" , IftaReportViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<IftaReportViewDto>> ViewIftaGeneratedSummaryReport(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<IftaReportViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long clientId = driveringStatusCRUDDto.getClientId();
			Instant instant = Instant.now();
			if(clientId>0) {
				List<IftaReportViewDto> iftaReport = lookupIftaGeneratedReportOperation(clientId);
				
				result.setResult(iftaReport);
				result.setStatus(Result.SUCCESS);
				result.setMessage("IFTA Report Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Company.");
			}
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<IftaReportViewDto> lookupIftaGeneratedReportOperation(long clientId){
		
		MatchOperation filter = Aggregation.match(
				Criteria.where("clientId").is(clientId));
		
		GroupOperation group1 = Aggregation.group("currentTimestamp","vehicleNo")
				.count().as("totalCount")
                .first("fromDate").as("fromDate")
                .last("toDate").as("toDate")
                .last("vehicleId").as("vehicleId")
                .last("driverId").as("driverId")
                .first("make").as("make")
                .first("vin").as("vin")
                .first("currentTimestamp").as("currentTimestamp")
                .first("fileName").as("fileName")
                .last("vehicleNo").as("vehicleNo");
		
		ProjectionOperation projectStage = Aggregation.project(
				"fromDate","toDate","vehicleId","driverId","make","vin","currentTimestamp","vehicleNo","fileName").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage,group1,
	    		sort(Direction.DESC, "currentTimestamp"));
        List<IftaReportViewDto> results = mongoTemplate.aggregate(aggregation, "ifta_reports" , IftaReportViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<List<IftaSummaryReport>> ViewIftaSummaryReport(DriveringStatusCRUDDto driveringStatusCRUDDto) {
		ResultWrapper<List<IftaSummaryReport>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long vehicleId = driveringStatusCRUDDto.getVehicleId();
			String sFromDate = driveringStatusCRUDDto.getFromDate();
			String sToDate = driveringStatusCRUDDto.getToDate();
			
			Instant instant = Instant.now();
			List<IftaSummaryReport> iftaSummaryReport = new ArrayList<>();
			IftaSummaryReport iftaSummary = new IftaSummaryReport();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(sFromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(sToDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			 List<LiveDataLogViewDto> eldLogData = lookupELDLogHistoryOperation(String.valueOf(vehicleId), from, to);
			 long iCount=0;
			 long lastStateId=0, startUtcDateTime=0, startStateId=0, lastTimeStamp=0,lastDriverId=0;
			 String startPlaceAddress="", endPlaceAddress="";
			 double firstOdometer=0,lastOdometer=0,totalKm=0,lastLat=0,lastLng=0;
			for(int i=0;i<eldLogData.size();i++) {
				if(lastStateId>0 && lastStateId!=eldLogData.get(i).getStateId()) {
					iftaSummary = new IftaSummaryReport();
					iftaSummary.setCarrierName("Road Liner Logistics Inc");
					iftaSummary.setFromDate(startUtcDateTime);
					iftaSummary.setToDate(eldLogData.get(i).getUtcDateTime());
					
					try {
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)vehicleId);
						iftaSummary.setVehicleNo(vehcileInfo.getVehicleNo());
						iftaSummary.setVehicleId(eldLogData.get(i).getVehicleId());
						iftaSummary.setMake(vehcileInfo.getMake());
						iftaSummary.setModel(vehcileInfo.getModel());
						iftaSummary.setManufacturingYear(vehcileInfo.getManufacturingYear());
					}catch(Exception ex) {
						ex.printStackTrace();
						iftaSummary.setVehicleNo("");
						iftaSummary.setVehicleId("0");
						iftaSummary.setMake("");
						iftaSummary.setModel("");
						iftaSummary.setManufacturingYear(0);
					}
					
					try {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)lastDriverId);
						iftaSummary.setDriverId(eldLogData.get(i).getDriverId());
						iftaSummary.setDriverName(empInfo.getFirstName()+" "+empInfo.getLastName());
					}catch(Exception ex) {
						ex.printStackTrace();
						iftaSummary.setDriverId("0");
						iftaSummary.setDriverName("");
						
					}
					
					iftaSummary.setFromPlaceAddress(startPlaceAddress);
					iftaSummary.setToPlaceAddress(eldLogData.get(i).getPlaceAddress());
					
					try {
						StateMaster stateInfo = stateMasterRepo.findByStateId((int)startStateId);
						iftaSummary.setFromStateName(stateInfo.getStateName());
						iftaSummary.setFromStateCode(stateInfo.getStateCode());
						stateInfo = stateMasterRepo.findByStateId((int)eldLogData.get(i).getStateId());
						iftaSummary.setToStateName(stateInfo.getStateName());
						iftaSummary.setToStateCode(stateInfo.getStateCode());
					}catch(Exception ex) {
						ex.printStackTrace();
						iftaSummary.setFromStateName("");
						iftaSummary.setFromStateCode("");
						iftaSummary.setToStateName("");
						iftaSummary.setToStateCode("");
					}
					
					iftaSummary.setFirstOdometer(firstOdometer);
					double odometer = Math.round(Double.parseDouble(eldLogData.get(i).getOdometer())*0.62137119);
					iftaSummary.setLastOdometer(odometer);
					if(odometer>=firstOdometer) {
						iftaSummary.setOdometerMileage((odometer-firstOdometer));
					}else {
						iftaSummary.setOdometerMileage(0);
					}
					
					double TOTAL_KM = totalKm*0.62137119;
					iftaSummary.setGpsMileage(Math.round(TOTAL_KM));
					
					iftaSummaryReport.add(iftaSummary);
					iCount=0;
					totalKm=0;
				}
				if(eldLogData.get(i).getStateId()>0) {
					lastStateId = eldLogData.get(i).getStateId();
					iCount++;
					if(iCount==1) {
						startUtcDateTime = eldLogData.get(i).getUtcDateTime();
						startPlaceAddress = eldLogData.get(i).getPlaceAddress();
						startStateId = eldLogData.get(i).getStateId();
						firstOdometer = Math.round(Double.parseDouble(eldLogData.get(i).getOdometer())*0.62137119);
					}
					if(lastLat!=0 && lastLat!=eldLogData.get(i).getLattitude()){
						double KM=calculateDistanceInMeters(eldLogData.get(i).getLattitude(),eldLogData.get(i).getLongitude(),lastLat,lastLng);
			            long timeDiff = (eldLogData.get(i).getUtcDateTime() - lastTimeStamp)/1000;
			            if((KM/1000)<=5 && timeDiff<=600){
			              totalKm+= KM/1000;
			            }
					}
					lastLat = eldLogData.get(i).getLattitude();
					lastLng = eldLogData.get(i).getLongitude();
					lastTimeStamp = eldLogData.get(i).getUtcDateTime();
					lastDriverId = Long.parseLong(eldLogData.get(i).getDriverId());
					endPlaceAddress = eldLogData.get(i).getPlaceAddress();
					lastOdometer = Math.round(Double.parseDouble(eldLogData.get(i).getOdometer()));
				}
				
			}
			
			iftaSummary = new IftaSummaryReport();
			iftaSummary.setCarrierName("Road Liner Logistics Inc");
			iftaSummary.setFromDate(startUtcDateTime);
			iftaSummary.setToDate(lastTimeStamp);
			
			try {
				VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)vehicleId);
				iftaSummary.setVehicleNo(vehcileInfo.getVehicleNo());
				iftaSummary.setVehicleId(String.valueOf(vehicleId));
				iftaSummary.setMake(vehcileInfo.getMake());
				iftaSummary.setModel(vehcileInfo.getModel());
				iftaSummary.setManufacturingYear(vehcileInfo.getManufacturingYear());
			}catch(Exception ex) {
				ex.printStackTrace();
				iftaSummary.setVehicleNo("");
				iftaSummary.setVehicleId("0");
				iftaSummary.setMake("");
				iftaSummary.setModel("");
				iftaSummary.setManufacturingYear(0);
			}
			
			try {
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)lastDriverId);
				iftaSummary.setDriverId(String.valueOf(lastDriverId));
				iftaSummary.setDriverName(empInfo.getFirstName()+" "+empInfo.getLastName());
			}catch(Exception ex) {
				ex.printStackTrace();
				iftaSummary.setDriverId("0");
				iftaSummary.setDriverName("");
				
			}
			
			
			iftaSummary.setFromPlaceAddress(startPlaceAddress);
			iftaSummary.setToPlaceAddress(endPlaceAddress);
			
			try {
				StateMaster stateInfo = stateMasterRepo.findByStateId((int)startStateId);
				iftaSummary.setFromStateName(stateInfo.getStateName());
				iftaSummary.setFromStateCode(stateInfo.getStateCode());
				stateInfo = stateMasterRepo.findByStateId((int)lastStateId);
				iftaSummary.setToStateName(stateInfo.getStateName());
				iftaSummary.setToStateCode(stateInfo.getStateCode());
			}catch(Exception ex) {
				ex.printStackTrace();
				iftaSummary.setFromStateName("");
				iftaSummary.setFromStateCode("");
				iftaSummary.setToStateName("");
				iftaSummary.setToStateCode("");
			}
			
			iftaSummary.setFirstOdometer(firstOdometer);
			double odometer = Math.round(lastOdometer*0.62137119);
			iftaSummary.setLastOdometer(odometer);
			if(lastOdometer>=firstOdometer) {
				iftaSummary.setOdometerMileage((odometer-firstOdometer));
			}else {
				iftaSummary.setOdometerMileage(0);
			}
			
			double TOTAL_KM = totalKm*0.62137119;
			iftaSummary.setGpsMileage(Math.round(TOTAL_KM));
			
			iftaSummaryReport.add(iftaSummary);
			iCount=0;
			totalKm=0;
			
			result.setResult(iftaSummaryReport);
			result.setStatus(Result.SUCCESS);
			result.setMessage("IFTA Summary Report Information Send Successfully");
					
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<DVIRDataCRUDDto> AddDVIRData(List<MultipartFile> file,DVIRData dvirData, String tokenValid) {
		ResultWrapper<DVIRDataCRUDDto> result = new ResultWrapper<>();
		try {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtDateTime = LocalDateTime.parse(dvirData.getDateTime(), formatter);
			long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			DVIRDataCRUDDto dvirDataViewDto = null;
			if(tokenValid.equals("true")) {
				dvirData.setLDateTime(lDateTime);
				
				Instant instant = Instant.now();
				dvirData.setReceivedTimestamp(instant.toEpochMilli());
				
				this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/dvir").toAbsolutePath().normalize(); //production
				try {
					if (Files.notExists(this.fileStorageLocation)) {
						 Files.createDirectories(this.fileStorageLocation);
					}
		        } catch (Exception ex) {
		            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
		        }
				
				int iCount=0;
				for(MultipartFile mf: file) {
					iCount++;
//						System.out.println("File Name : "+mf.getOriginalFilename());
					String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
		            String fileName = "";
		            try {
		                // Check if the file's name contains invalid characters
		                if(originalFileName.contains("..")) {
		                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
		                }
		                String fileExtension = "";
		                try {
		                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		                } catch(Exception e) {
		                    fileExtension = "";
		                }
		                if(iCount==1) {
		                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
		                		dvirData.setDriverSignFile("");
		                	}else {
		                		fileName = dvirData.getDriverId()+ "_sign_"+iCount + fileExtension;
		       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
		       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		       	                dvirData.setDriverSignFile(fileName);
		                	}
		                }
		                
		            } catch (IOException ex) {
		                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		            }
				}				
				dvirData = dvirDataRepo.save(dvirData);
				dvirDataViewDto = dvirDataRepo.findAndViewDvirDataByDriverId(dvirData.getDriverId(), dvirData.getLDateTime());
				
				result.setResult(dvirDataViewDto);
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("DVIR Data Added Successfully");
				
			}else {
				result.setResult(null);
				result.setToken(tokenValid);
				result.setStatus(Result.FAIL);
				result.setMessage("Your Token has been expired.");
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> AddDVIRDataImage(List<MultipartFile> file) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
				
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/dvir").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
				
			int iCount=0;
			for(MultipartFile mf: file) {
				iCount++;
//						System.out.println("File Name : "+mf.getOriginalFilename());
				String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
	            String fileName = originalFileName;
	            try {
	                // Check if the file's name contains invalid characters
	                if(originalFileName.contains("..")) {
	                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
	                }
	                String fileExtension = "";
	                try {
	                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	                } catch(Exception e) {
	                    fileExtension = "";
	                }
	                if(iCount==1) {
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		
	                	}else {
	       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	                	}
	                }
	            } catch (IOException ex) {
	                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
	            }
			}
			
			result.setResult("SAved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("DVIR Data Image Added Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDVIRDataOffline(AddDvirDataDto addDvirDataDto, String tokenValid) {
		ResultWrapper<List<AddDriveringStatusResponseDto>> result = new ResultWrapper<>();
		String sDebug=">> ";
		try {			
			Instant instant = Instant.now();
			
			List<AddDriveringStatusResponseDto> AddDvirStatusData = new ArrayList<>();
			AddDriveringStatusResponseDto addDvirStatus = new AddDriveringStatusResponseDto();
			
			ArrayList<DVIRData> dvirStatusData = addDvirDataDto.getDvirStatusData();
			DVIRData dvirData = null;
			sDebug+="1,";
//			System.out.println(addDvirDataDto.getDvirStatusData());
//			System.out.println(dvirStatusData.size());
			
			for(int i=0;i<dvirStatusData.size();i++) {
				sDebug+="2,";
				dvirData = dvirStatusData.get(i);
				sDebug+="3,";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				LocalDateTime ldtDateTime = LocalDateTime.parse(dvirData.getDateTime(), formatter);
				long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				sDebug+="4,";
				
				dvirData.setLDateTime(lDateTime);
				
				dvirData.setReceivedTimestamp(instant.toEpochMilli());
				
				dvirData = dvirDataRepo.save(dvirData);
				sDebug+="5,";
				DVIRDataCRUDDto dvirDataVewDto = dvirDataRepo.findAndViewDvirDataByDriverId(dvirData.getDriverId(), dvirData.getLDateTime());
				String objId = dvirDataVewDto.get_id();
				sDebug+="6,";
				addDvirStatus = new AddDriveringStatusResponseDto();
				addDvirStatus.setLocalId(dvirData.getLocalId());
				addDvirStatus.setServerId(objId);
				AddDvirStatusData.add(addDvirStatus);				
			}
			
			result.setResult(AddDvirStatusData);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("DVIR Data Added Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDefectDataOffline(List<MultipartFile> file,AddDvirDataDto addDvirDataDto, String tokenValid) {
		ResultWrapper<List<AddDriveringStatusResponseDto>> result = new ResultWrapper<>();
		String sDebug=">> ";
		try {			
			Instant instant = Instant.now();
			
			List<AddDriveringStatusResponseDto> AddDefectData = new ArrayList<>();
			AddDriveringStatusResponseDto addDefectDetail = new AddDriveringStatusResponseDto();
			
			ArrayList<DefectDetails> defectDetailData = addDvirDataDto.getDefectData();
			DefectDetails defectDetails = null;
			sDebug+="1,";
			
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/defects").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
			
			for(int i=0;i<defectDetailData.size();i++) {
				sDebug+="2,";
				defectDetails = defectDetailData.get(i);
				sDebug+="3,";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
				LocalDateTime ldtDateTime = LocalDateTime.parse(defectDetails.getDateTime(), formatter);
				long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
				sDebug+="4,";
				defectDetails.setLDateTime(lDateTime);
				defectDetails.setReceivedTimestamp(instant.toEpochMilli());
				
				if (file != null && i < file.size()) {
					MultipartFile mf = file.get(i);
					String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
		            String fileName = originalFileName;
		            try {
		                if(originalFileName.contains("..")) {
		                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
		                }
		                String fileExtension = "";
		                try {
		                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		                } catch(Exception e) {
		                    fileExtension = "";
		                }
		                fileName = "defect_"+defectDetails.getDvirId()+"_"+defectDetails.getDriverId()+"_" + defectDetails.getUtcDateTime()+fileExtension;
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		
	                	}else {
	       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	       	                defectDetails.setFileName(fileName); 
	                	}
		            } catch (IOException ex) {
		                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		            }
				}
				
				defectDetails = defectDetailsRepo.save(defectDetails);
				sDebug+="5,";
				DefectDetailCRUDDto defetctDataViewDto = defectDetailsRepo.findAndViewDefectDataByDriverId(defectDetails.getDriverId(), defectDetails.getLDateTime());
				String objId = defetctDataViewDto.get_id();
				sDebug+="6,";
				addDefectDetail = new AddDriveringStatusResponseDto();
				addDefectDetail.setLocalId(defectDetails.getLocalId());
				addDefectDetail.setServerId(objId);
				AddDefectData.add(addDefectDetail);				
			}
			
			result.setResult(AddDefectData);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Defect Data Added Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDefectData(MultipartFile mf,DefectDetails defectDetails, String tokenValid) {
		ResultWrapper<List<AddDriveringStatusResponseDto>> result = new ResultWrapper<>();
		String sDebug=">> ";
		try {			
			Instant instant = Instant.now();
			
			List<AddDriveringStatusResponseDto> AddDefectData = new ArrayList<>();
			AddDriveringStatusResponseDto addDefectDetail = new AddDriveringStatusResponseDto();
			
			sDebug+="1,";
			
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/defects").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
			
			sDebug+="2,";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtDateTime = LocalDateTime.parse(defectDetails.getDateTime(), formatter);
			long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			sDebug+="4,";
			defectDetails.setLDateTime(lDateTime);
			defectDetails.setReceivedTimestamp(instant.toEpochMilli());
			
			String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
            String fileName = originalFileName;
            try {
                if(originalFileName.contains("..")) {
                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
                }
                String fileExtension = "";
                try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                } catch(Exception e) {
                    fileExtension = "";
                }
                fileName = "defect_"+defectDetails.getDvirId()+"_"+defectDetails.getDriverId()+"_"+defectDetails.getUtcDateTime()+fileExtension;
            	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
            		
            	}else {
   	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
   	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
   	                defectDetails.setFileName(fileName); 
            	}
            } catch (IOException ex) {
                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
            }
			
			
			defectDetails = defectDetailsRepo.save(defectDetails);
			sDebug+="5,";
			DefectDetailCRUDDto defetctDataViewDto = defectDetailsRepo.findAndViewDefectDataByDriverId(defectDetails.getDriverId(), defectDetails.getLDateTime());
			String objId = defetctDataViewDto.get_id();
			sDebug+="6,";
			addDefectDetail = new AddDriveringStatusResponseDto();
			addDefectDetail.setLocalId(defectDetails.getLocalId());
			addDefectDetail.setServerId(objId);
			AddDefectData.add(addDefectDetail);				
			
			result.setResult(AddDefectData);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Defect Data Added Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
//	public ResultWrapper<List<DVIRDataCRUDDto>> ViewDVIRData(DVIRDataCRUDDto dvirDataCRUDDto) {
//		ResultWrapper<List<DVIRDataCRUDDto>> result = new ResultWrapper<>();
//		try {
//			List<DVIRDataCRUDDto> dvirDataViewDto = null;
//			
//			long driverId = dvirDataCRUDDto.getDriverId();
//			String email = dvirDataCRUDDto.getEmail();
//			String fromDate = dvirDataCRUDDto.getFromDate();
//			String toDate = dvirDataCRUDDto.getToDate();
//			
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
//			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
//			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//			
//			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
//			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//			
//			String driverName="";
//			String url = WEB_URL_BASE_PATH+"/uploads/dvir/";
//			String setImagePath1;
//			if(driverId>0) {
//				dvirDataViewDto = lookupDVIRDataOperation(from,to,driverId);
//				for(int i=0;i<dvirDataViewDto.size();i++) {
//					setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
//					dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
//					
//					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
//					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
//					dvirDataViewDto.get(i).setDriverName(driverName);
//					
//					try {
//						if(dvirDataViewDto.get(i).getVehicleId()>0) {
//							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
//							dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
//						}
//					}catch(Exception ex) {
//						ex.printStackTrace();
//					}
//					
//	        		
//				}
//				
//				
//				if(!email.equals("")) {
//					// send data from email
//					
//					List<Map<String, Object>> resultList = dispatchServiceImpl.callReportCreateMethods(driverId, from,to,"dvir_log",driverName);
//					for (Map<String, Object> valueMap : resultList) {
//						System.out.println(">>>SendEmail: ["+ valueMap.size() +"]" +LocalDateTime.now());
//
//						if(valueMap.size()<=0) continue;
//						
//						ByteArrayOutputStream outputStream = (ByteArrayOutputStream) valueMap.get("outputStream");
//						String fileName = valueMap.get("fileName").toString();
//						System.out.println(">>ReportName-"+ fileName +"----------------------"+LocalDateTime.now());
//						String subject = valueMap.get("subject").toString();
//						String text = valueMap.get("text").toString();
//
//						// construct the text body part
//						MimeBodyPart textBodyPart = new MimeBodyPart();
//						textBodyPart.setText(text);
//
//						byte[] bytes = outputStream.toByteArray();
//
//						// construct the file body part
//						DataSource dataSource = null;
//
//						if (fileName.contains("pdf")) {
//							dataSource = new ByteArrayDataSource(bytes, "application/pdf");
//						} else if (fileName.contains("xlsx")) {
//							dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
//						}
//
//						MimeBodyPart fileBodyPart = new MimeBodyPart();
//						fileBodyPart.setDataHandler(new DataHandler(dataSource));
//						fileBodyPart.setFileName(fileName);
//
//						// construct the mime multi part
//						MimeMultipart mimeMultipart = new MimeMultipart();
//						mimeMultipart.addBodyPart(textBodyPart);
//						mimeMultipart.addBodyPart(fileBodyPart);
//
//						MimeMessage message = javaMailSender.createMimeMessage();
//						MimeMessageHelper helper = new MimeMessageHelper(message);
//						helper.setTo(email);
//						helper.setSubject(subject);
////				      	helper.addAttachment(attachmentFilename, dataSource);
//						message.setContent(mimeMultipart);
//
//						javaMailSender.send(message);
//						
//					}
//				}
//				
//				result.setResult(dvirDataViewDto);
//				result.setStatus(Result.SUCCESS);
//				result.setMessage("DVIR Data Information Send Successfully");
//			}else {
//				result.setResult(null);
//				result.setStatus(Result.FAIL);
//				result.setMessage("Invalid Request.");
//			}
//						
//		}catch(Exception e) {
//			result.setStatus(Result.FAIL);
//			result.setMessage(e.getLocalizedMessage());
//		}
//		return result;	
//	}
	
	public ResultWrapper<List<DVIRDataCRUDDto>> ViewDVIRData(DVIRDataCRUDDto dvirDataCRUDDto, String tokenValid) {
		ResultWrapper<List<DVIRDataCRUDDto>> result = new ResultWrapper<>();
		try {
			List<DVIRDataCRUDDto> dvirDataViewDto = null;
			
			long driverId = dvirDataCRUDDto.getDriverId();
			String email = dvirDataCRUDDto.getEmail();
			String fromDate = dvirDataCRUDDto.getFromDate();
			String toDate = dvirDataCRUDDto.getToDate();
			long clientId = dvirDataCRUDDto.getClientId();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/dvir/";
			String setImagePath1;
			if(driverId>0) {
				dvirDataViewDto = lookupDVIRDataByClientOperation(from,to,driverId,clientId);
//				dvirDataViewDto = lookupDVIRDataOperation(from,to,driverId);
				for(int i=0;i<dvirDataViewDto.size();i++) {
					
					try {
						dvirDataViewDto.get(i).setOdometer(Double.valueOf(String.format("%.2f", (dvirDataViewDto.get(i).getOdometer()))));
						dvirDataViewDto.get(i).setEngineHour(df.format(Double.parseDouble(dvirDataViewDto.get(i).getEngineHour())));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					
					setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
					dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
					
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					dvirDataViewDto.get(i).setDriverName(driverName);
					
					try {
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						if(mainTerminal.getStateId()>0) {
							StateMaster stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
							dvirDataViewDto.get(i).setTimezoneName(stateInfo.getTimeZone());
							dvirDataViewDto.get(i).setTimezoneOffSet(stateInfo.getTimezoneOffSet());
						}
						
						if(dvirDataViewDto.get(i).getVehicleId()>0) {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
							dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
							dvirDataViewDto.get(i).setVin(vehcileInfo.getVin());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
	        		
				}
				
				
				if(!email.equals("")) {
					// send data from email
					
					List<Map<String, Object>> resultList = dispatchServiceImpl.callReportCreateMethods(driverId, from,to,"dvir_log",driverName);
					for (Map<String, Object> valueMap : resultList) {
						System.out.println(">>>SendEmail: ["+ valueMap.size() +"]" +LocalDateTime.now());

						if(valueMap.size()<=0) continue;
						
						ByteArrayOutputStream outputStream = (ByteArrayOutputStream) valueMap.get("outputStream");
						String fileName = valueMap.get("fileName").toString();
						System.out.println(">>ReportName-"+ fileName +"----------------------"+LocalDateTime.now());
						String subject = valueMap.get("subject").toString();
						String text = valueMap.get("text").toString();

						// construct the text body part
						MimeBodyPart textBodyPart = new MimeBodyPart();
						textBodyPart.setText(text);

						byte[] bytes = outputStream.toByteArray();

						// construct the file body part
						DataSource dataSource = null;

						if (fileName.contains("pdf")) {
							dataSource = new ByteArrayDataSource(bytes, "application/pdf");
						} else if (fileName.contains("xlsx")) {
							dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
						}

						MimeBodyPart fileBodyPart = new MimeBodyPart();
						fileBodyPart.setDataHandler(new DataHandler(dataSource));
						fileBodyPart.setFileName(fileName);

						// construct the mime multi part
						MimeMultipart mimeMultipart = new MimeMultipart();
						mimeMultipart.addBodyPart(textBodyPart);
						mimeMultipart.addBodyPart(fileBodyPart);

						MimeMessage message = javaMailSender.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(message);
						helper.setTo(email);
						helper.setSubject(subject);
//				      	helper.addAttachment(attachmentFilename, dataSource);
						message.setContent(mimeMultipart);

						javaMailSender.send(message);
						
					}
				}
				
				
			}else {
				dvirDataViewDto = lookupDVIRDataByClientOperation(from,to,driverId,clientId);
//				dvirDataViewDto = lookupDVIRDataOperation(from,to,driverId);
				for(int i=0;i<dvirDataViewDto.size();i++) {
					setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
					dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
					
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)dvirDataViewDto.get(i).getDriverId());
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					dvirDataViewDto.get(i).setDriverName(driverName);
					
					try {
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						if(mainTerminal.getStateId()>0) {
							StateMaster stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
							dvirDataViewDto.get(i).setTimezoneName(stateInfo.getTimeZone());
							dvirDataViewDto.get(i).setTimezoneOffSet(stateInfo.getTimezoneOffSet());
						}
						
						if(dvirDataViewDto.get(i).getVehicleId()>0) {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
							dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
							dvirDataViewDto.get(i).setVin(vehcileInfo.getVin());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
	        		
				}
				
				
				if(!email.equals("")) {
					// send data from email
					
					List<Map<String, Object>> resultList = dispatchServiceImpl.callReportCreateMethods(driverId, from,to,"dvir_log",driverName);
					for (Map<String, Object> valueMap : resultList) {
						System.out.println(">>>SendEmail: ["+ valueMap.size() +"]" +LocalDateTime.now());

						if(valueMap.size()<=0) continue;
						
						ByteArrayOutputStream outputStream = (ByteArrayOutputStream) valueMap.get("outputStream");
						String fileName = valueMap.get("fileName").toString();
						System.out.println(">>ReportName-"+ fileName +"----------------------"+LocalDateTime.now());
						String subject = valueMap.get("subject").toString();
						String text = valueMap.get("text").toString();

						// construct the text body part
						MimeBodyPart textBodyPart = new MimeBodyPart();
						textBodyPart.setText(text);

						byte[] bytes = outputStream.toByteArray();

						// construct the file body part
						DataSource dataSource = null;

						if (fileName.contains("pdf")) {
							dataSource = new ByteArrayDataSource(bytes, "application/pdf");
						} else if (fileName.contains("xlsx")) {
							dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
						}

						MimeBodyPart fileBodyPart = new MimeBodyPart();
						fileBodyPart.setDataHandler(new DataHandler(dataSource));
						fileBodyPart.setFileName(fileName);

						// construct the mime multi part
						MimeMultipart mimeMultipart = new MimeMultipart();
						mimeMultipart.addBodyPart(textBodyPart);
						mimeMultipart.addBodyPart(fileBodyPart);

						MimeMessage message = javaMailSender.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(message);
						helper.setTo(email);
						helper.setSubject(subject);
//				      	helper.addAttachment(attachmentFilename, dataSource);
						message.setContent(mimeMultipart);

						javaMailSender.send(message);
						
					}
				}
			}
			
			result.setResult(dvirDataViewDto);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("DVIR Data Information Send Successfully");
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> DownloadDVIRData(DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			
			long driverId = dvirDataCRUDDto.getDriverId();
			String fromDate = dvirDataCRUDDto.getFromDate();
			String toDate = dvirDataCRUDDto.getToDate();
			long clientId = dvirDataCRUDDto.getClientId();
			String email = dvirDataCRUDDto.getEmail();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			String filePath=""; 
			String fileName="";
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/dvir/";
			String setImagePath1;
			if(driverId>0) {
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
				driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					
				List<Map<String, Object>> resultList = dispatchServiceImpl.callReportCreateMethods(driverId, from,to,"dvir_log",driverName);
				for (Map<String, Object> valueMap : resultList) {

					if(valueMap.size()<=0) continue;
					
					ByteArrayOutputStream outputStream = (ByteArrayOutputStream) valueMap.get("outputStream");
					fileName = valueMap.get("fileName").toString();

					byte[] bytes = outputStream.toByteArray();
					String basePath = WEB_URL_FILE_UPLOAD + "/uploads/dvir_report/";  // ensure this exists
			        File folder = new File(basePath);
			        if (!folder.exists()) folder.mkdirs();

			        filePath = basePath + driverId+"_"+fileName;
			        try (FileOutputStream fos = new FileOutputStream(filePath)) {
			            fos.write(bytes);
			            fos.flush();
			        }
			        
				}
				String fileUrl = WEB_URL_BASE_PATH+"/uploads/dvir_report/"+driverId+"_"+fileName;
				result.setResult(fileUrl);
				result.setStatus(Result.SUCCESS);
				result.setMessage("DVIR Report Download Successfully");
				
//				if (email != null && !email.isEmpty()) {
//	                String subject = driverName + " DVIR Report";
//	                String htmlBody = "<div style='text-align:center;font-family:Arial,sans-serif;'>"
//	                        + "<h3>" + driverName + " sent DVIR Report</h3>"
//	                        + "<p>" + fromDate + " - " + toDate + "</p>"
//	                        + "<div style='margin:20px 0;'>"
//	                        + "  <a href='" + fileUrl + "' "
//	                        + "     style='display:inline-block;padding:12px 24px;"
//	                        + "     background:#28a745;color:#fff;font-size:14px;"
//	                        + "     text-decoration:none;border-radius:5px;"
//	                        + "     font-weight:bold;'>"
//	                        + "     DOWNLOAD DVIR REPORT</a>"
//	                        + "</div>"
//	                        + "<p style='color:#666;font-size:12px;'>This is an automated message, please do not reply.</p>"
//	                        + "</div>";
//
//	                sendEmail(email, subject, htmlBody);
//	            }
				
			}else {
				result.setResult("Error");
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid driver.");
			}
					
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> DownloadDVIRDataEncript(DVIRDataCRUDDto dvirDataCRUDDto) {
	    ResultWrapper<String> result = new ResultWrapper<>();
	    String sDebug="";
	    try {
	        long driverId = dvirDataCRUDDto.getDriverId();
	        String fromDate = dvirDataCRUDDto.getFromDate();
	        String toDate = dvirDataCRUDDto.getToDate();
	        long clientId = dvirDataCRUDDto.getClientId();
	        String email = dvirDataCRUDDto.getEmail(); 

	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
	        LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
	        long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	        LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
	        long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	        String filePath = ""; 
	        String fileName = "";
	        String driverName = "";
	        
	        if(driverId > 0) {
	            EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int) driverId);
	            driverName = empInfo.getFirstName() + " " + empInfo.getLastName();

	            List<Map<String, Object>> resultList = dispatchServiceImpl
	                    .callReportCreateMethods(driverId, from, to, "dvir_log", driverName);

	            for (Map<String, Object> valueMap : resultList) {
	                if (valueMap.size() <= 0) continue;

	                ByteArrayOutputStream outputStream = (ByteArrayOutputStream) valueMap.get("outputStream");
	                fileName = valueMap.get("fileName").toString();

	                byte[] bytes = outputStream.toByteArray();
	                sDebug+="Size : "+bytes.length+",";
	                // 🔐 Encrypt the PDF before saving
//	                String userPassword = String.valueOf(driverId); // password = driverId (can change)
//	                String ownerPassword = "AdminSecret123";        // admin password
//	                sDebug+="PW : "+userPassword+" :: "+ownerPassword+",";
//	                bytes = encryptPdf(bytes, userPassword, ownerPassword);

	                String basePath = WEB_URL_FILE_UPLOAD + "/uploads/dvir_report/";
	                File folder = new File(basePath);
	                if (!folder.exists()) folder.mkdirs();

	                filePath = basePath + driverId + "_" + fileName;
	                try (FileOutputStream fos = new FileOutputStream(filePath)) {
	                    fos.write(bytes);
	                    fos.flush();
	                }
	            }

	            String fileUrl = WEB_URL_BASE_PATH + "/uploads/dvir_report/" + driverId + "_" + fileName;
	            result.setResult(fileUrl);
	            result.setStatus(Result.SUCCESS);
	            result.setMessage("DVIR Report Download Successfully"+sDebug);

	            // === If email is provided, send email with download button ===
	            if (email != null && !email.isEmpty()) {
	                String subject = driverName + " DVIR Report";
	                String htmlBody = "<div style='text-align:center;font-family:Arial,sans-serif;'>"
	                        + "<h3>" + driverName + " sent DVIR Report</h3>"
	                        + "<p>" + fromDate + " - " + toDate + "</p>"
	                        + "<div style='margin:20px 0;'>"
	                        + "  <a href='" + fileUrl + "' "
	                        + "     style='display:inline-block;padding:12px 24px;"
	                        + "     background:#28a745;color:#fff;font-size:14px;"
	                        + "     text-decoration:none;border-radius:5px;"
	                        + "     font-weight:bold;'>"
	                        + "     DOWNLOAD DVIR REPORT</a>"
	                        + "</div>"
	                        + "<p style='color:#666;font-size:12px;'>"
	                        + "This is an automated message, please do not reply."
	                        + "</p>"
	                        + "</div>";

	                sendEmail(email, subject, htmlBody);
	            }

	        } else {
	            result.setResult("Error");
	            result.setStatus(Result.FAIL);
	            result.setMessage("Invalid driver."+sDebug);
	        }

	    } catch (Exception e) {
	        result.setStatus(Result.FAIL);
	        result.setMessage(e.getLocalizedMessage()+sDebug);
	    }
	    return result;
	}

	
	private byte[] encryptPdf(byte[] pdfBytes, String userPassword, String ownerPassword) throws Exception {
	    if (pdfBytes == null || pdfBytes.length == 0) {
	        throw new IllegalArgumentException("Empty PDF content, cannot encrypt.");
	    }

	    // ensure non-null passwords (prefer them to be different)
	    if (userPassword == null) userPassword = "";
	    if (ownerPassword == null) ownerPassword = "";

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfReader reader = null;
	    try {
	    	SaveLog("byte size : "+pdfBytes.length+",\n");
	        reader = new PdfReader(pdfBytes);
	        SaveLog("1,");
	        // CORRECT ORDER for iText 5:
	        // (reader, outputStream, userPasswordBytes, ownerPasswordBytes, permissions, use128bitAES)
	        PdfEncryptor.encrypt(
	            reader,
	            baos,
	            userPassword.getBytes("UTF-8"),
	            ownerPassword.getBytes("UTF-8"),
	            PdfWriter.ALLOW_PRINTING, // permissions mask (0 for no permissions)
	            true // true = use 128-bit AES
	        );
	        SaveLog("2,");
	        return baos.toByteArray();
	    } finally {
	        if (reader != null) reader.close();
	        baos.close();
	    }
	}
	
	public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
	    MimeMessage message = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(htmlBody, true); // true = HTML
	    javaMailSender.send(message);
	}

	public ResultWrapper<EldReportDto> ELDReportEncrypt(DVIRDataCRUDDto dvirDataCRUDDto) {
	    ResultWrapper<EldReportDto> result = new ResultWrapper<>();
	    String sDebug = "";
	    try {
	        // Example static data (replace with your DB/repo lookups if needed)

	        EldHeader header = new EldHeader();
	        header.setEldIdentifier("INURUMPT30");
	        header.setEldProvider("Inurum Technologies");
	        header.setEldSoftwareVersion("1.0.0");
	        header.setOutputFileFormat("JSON");
	        header.setFileGeneratedTime("2025-09-19T14:00:00Z");
	        header.setFileSignature("MIIByzYh3k...base64signature...");

	        Carrier carrier = new Carrier();
	        carrier.setCarrierName("ABC Trucking LLC");
	        carrier.setUsdotNumber("1234567");
	        carrier.setHomeTerminal("Dallas, TX");

	        Driver driver = new Driver();
	        driver.setDriverId("DRV1001");
	        driver.setDriverName("John Doe");
	        driver.setLicenseNumber("TX123456");
	        driver.setLicenseState("TX");

	        Vehicle vehicle = new Vehicle();
	        vehicle.setUnitNumber("TRUCK-101");
	        vehicle.setVin("1XPBDP9X5JD123456");
	        vehicle.setOdometerStart("0");
	        vehicle.setOdometerEnd("0");

	        EventRecord ev1 = new EventRecord();
	        ev1.setEventCode("YM");
	        ev1.setEventDescription("Yard Move");
	        ev1.setEventTime("2025-09-19T09:30:00Z");
	        ev1.setLocation("Dallas Yard, TX");
	        ev1.setOdometer("0");
	        ev1.setEngineHours("0");

	        EventRecord ev2 = new EventRecord();
	        ev2.setEventCode("OFF");
	        ev2.setEventDescription("Off Duty");
	        ev2.setEventTime("2025-09-19T13:30:00Z");
	        ev2.setLocation("Dallas, TX");
	        ev2.setOdometer("0");
	        ev2.setEngineHours("0");

	        Annotation ann = new Annotation();
	        ann.setEventRef("ON-2025-09-19T08:00:00Z");
	        ann.setNote("Pre-trip inspection completed.");

	        // Fill ELD Report
	        EldReportDto report = new EldReportDto();
	        report.setEldHeader(header);
	        report.setCarrier(carrier);
	        report.setDriver(driver);
	        report.setVehicle(vehicle);
	        report.setEventRecords(Arrays.asList(ev1, ev2));
	        report.setAnnotations(Arrays.asList(ann));
	        report.setMalfunctions(Collections.emptyList());
	        report.setDiagnostics(Collections.emptyList());

	        result.setResult(report);
	        result.setStatus(Result.SUCCESS);
	        result.setMessage("Information Send Successfully." + sDebug);

	    } catch (Exception e) {
	        result.setStatus(Result.FAIL);
	        result.setMessage(e.getLocalizedMessage() + sDebug);
	    }
	    return result;
	}
	
	public ResultWrapper<List<AlertsLogViewDto>> ViewAlerts(AlertsLogViewDto alertsLogViewDto) {
		ResultWrapper<List<AlertsLogViewDto>> result = new ResultWrapper<>();
		try {
			List<AlertsLogViewDto> alertsLogViewDtoData = null;
			
			long clientId = alertsLogViewDto.getClientId();
			String driverName="";
			if(clientId>0) {
				alertsLogViewDtoData = alertsLogRepo.findAndViewAllUnreadAlerts(clientId,0);
				for(int i=0;i<alertsLogViewDtoData.size();i++) {
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)alertsLogViewDtoData.get(i).getDriverId());
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					alertsLogViewDtoData.get(i).setDriverName(driverName);
					try {
						if(alertsLogViewDtoData.get(i).getVehicleId()>0) {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)alertsLogViewDtoData.get(i).getVehicleId());
							alertsLogViewDtoData.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				result.setResult(alertsLogViewDtoData);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Alerts Data Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateAlerts(AlertsLogViewDto alertsLogViewDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			String readByEmail = alertsLogViewDto.getReadByEmail();
			String driverName="";
			if(!readByEmail.equals("")) {
				Query query = new Query();
	        	query.addCriteria(
	       			Criteria.where("driverId").is(alertsLogViewDto.getDriverId())
	       			.and("startUtcDateTime").is(alertsLogViewDto.getStartUtcDateTime())
	       		);
	        	 
	        	Update update = new Update();
		        update.set("readByEmail", readByEmail);
		        update.set("isRead", 1);
		        update.set("readingTimestamp", instant.toEpochMilli());
		        mongoTemplate.findAndModify(query, update, AlertsLog.class);
				
				result.setResult("Updated");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Alerts Data Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateAllUnreadAlerts(AlertsLogViewDto alertsLogViewDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Instant instant = Instant.now();
			long clientId = alertsLogViewDto.getClientId();
			String readByEmail = alertsLogViewDto.getReadByEmail();
			Query query = new Query();
			Update update = new Update();
			if(clientId>0) {
				List<AlertsLogViewDto> alertsLogViewDtoData = alertsLogRepo.findAndViewAllUnreadAlerts(clientId,0);
				for(int i=0;i<alertsLogViewDtoData.size();i++) {
					query = new Query();
					query.addCriteria(
		       			Criteria.where("driverId").is(alertsLogViewDtoData.get(i).getDriverId())
		       			.and("startUtcDateTime").is(alertsLogViewDtoData.get(i).getStartUtcDateTime())
		       		);
					
					update = new Update();
					update.set("readByEmail", readByEmail);
			        update.set("isRead", 1);
			        update.set("readingTimestamp", instant.toEpochMilli());
			        mongoTemplate.findAndModify(query, update, AlertsLog.class);
				}
				
				result.setResult("Updated");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Alerts Data Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<DVIRDataCRUDDto>> ViewDVIRDataByTimestamp(DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<List<DVIRDataCRUDDto>> result = new ResultWrapper<>();
		try {
			List<DVIRDataCRUDDto> dvirDataViewDto = null;
			
			String timestamp = dvirDataCRUDDto.getTimestamp();
			DecimalFormat df = new DecimalFormat("0.00");
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/dvir/";
			String defectUrl = WEB_URL_BASE_PATH+"/uploads/defects/";
			String setImagePath1;
			String setImagePath2;
			if(!timestamp.equals("")) {
				dvirDataViewDto = lookupDVIRDataByTimestampOperation(timestamp);
				for(int i=0;i<dvirDataViewDto.size();i++) {
					
					try {
						dvirDataViewDto.get(i).setOdometer(Double.valueOf(String.format("%.2f", (dvirDataViewDto.get(i).getOdometer()))));
						dvirDataViewDto.get(i).setEngineHour(df.format(Double.parseDouble(dvirDataViewDto.get(i).getEngineHour())));
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					
					setImagePath1 = url.concat(dvirDataViewDto.get(i).getDriverSignFile());
					dvirDataViewDto.get(i).setDriverSignFile(setImagePath1);
					
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)dvirDataViewDto.get(i).getDriverId());
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					dvirDataViewDto.get(i).setDriverName(driverName);
					
					ArrayList<String> sTruckDefectImage = new ArrayList<>();
					ArrayList<String> sTrailerDefectImage = new ArrayList<>();
					List<DefectDetailCRUDDto> defectDetailCRUDDto = defectDetailsRepo.findAndViewDefectDataByDvirId(dvirDataViewDto.get(i).get_id());
					for(int d=0;d<defectDetailCRUDDto.size();d++) {
						if(defectDetailCRUDDto.get(d).getDefectType().equals("Truck")) {
							setImagePath2 = defectUrl.concat(defectDetailCRUDDto.get(d).getFileName());
							sTruckDefectImage.add(setImagePath2);
						}else {
							setImagePath2 = defectUrl.concat(defectDetailCRUDDto.get(d).getFileName());
							sTrailerDefectImage.add(setImagePath2);
						}
					}
					
					dvirDataViewDto.get(i).setTruckDefectImage(sTruckDefectImage);
					dvirDataViewDto.get(i).setTrailerDefectImage(sTrailerDefectImage);
					
					try {
						MainTerminalMaster mainTerminal = mainTerminalMasterRepo.findByMainTerminalId((int)empInfo.getMainTerminalId());
						if(mainTerminal.getStateId()>0) {
							StateMaster stateInfo = stateMasterRepo.findByStateId((int)mainTerminal.getStateId());
							dvirDataViewDto.get(i).setTimezoneName(stateInfo.getTimeZone());
							dvirDataViewDto.get(i).setTimezoneOffSet(stateInfo.getTimezoneOffSet());
						}
						if(dvirDataViewDto.get(i).getVehicleId()>0) {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)dvirDataViewDto.get(i).getVehicleId());
							dvirDataViewDto.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
							dvirDataViewDto.get(i).setVin(vehcileInfo.getVin());
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
	        		
				}
				
				result.setResult(dvirDataViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("DVIR Data Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> DeleteDvir(DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			
			String timestamp = dvirDataCRUDDto.getTimestamp();
			long driverId = dvirDataCRUDDto.getDriverId();
			
			String defectUrl = WEB_URL_BASE_PATH+"/uploads/defects/";
			if(!timestamp.equals("")) {
				DVIRDataCRUDDto deleteDvir = dvirDataRepo.deleteDVIRDataByDriverIdAndTimestamp(driverId,timestamp);
				if (deleteDvir != null) {
					
					String dvirId = deleteDvir.get_id();
					
					List<DefectDetailCRUDDto> defectLogs = defectDetailsRepo.findAndViewDefectDataByDvirId(dvirId);
					for (DefectDetailCRUDDto defect : defectLogs) {
                        if (defect.getFileName() != null && !defect.getFileName().trim().isEmpty()) {
                            String filePath = defectUrl + defect.getFileName();
                            File file = new File(filePath);
                            if (file.exists()) {
                                boolean deleted = file.delete();
                                if (!deleted) {
                                    //System.out.println("Could not delete defect image: " + filePath);
                                }
                            }
                        }
                    }
					
					defectDetailsRepo.DeleteAllDVIRDataByDriverIdAndDate(deleteDvir.get_id());
					
					result.setResult("Deleted");
					result.setStatus(Result.SUCCESS);
					result.setMessage("DVIR Data Information Deleted Successfully");
					
				}else {
					result.setResult("Not Found");
	                result.setStatus(Result.FAIL);
	                result.setMessage("No DVIR found for given timestamp.");
				}
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<DVIRDataCRUDDto> lookupDVIRDataByTimestampOperation(String timestamp){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("timestamp").is(timestamp)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"_id","driverId","location","truckDefect","trailerDefect","trailer","dateTime","lDateTime","notes","vehicleCondition",
				"driverSignFile","receivedTimestamp","engineHour","odometer","companyName","vehicleId","timestamp");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage);
        List<DVIRDataCRUDDto> results = mongoTemplate.aggregate(aggregation, "dvir_data" , DVIRDataCRUDDto.class).getMappedResults();
        return results;
		
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
				"_id","driverId","location","truckDefect","trailerDefect","trailer","dateTime","lDateTime","notes","vehicleCondition",
				"driverSignFile","receivedTimestamp","engineHour","odometer","companyName","vehicleId","timestamp");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "driverId").and(Direction.DESC, "receivedTimestamp"));
        List<DVIRDataCRUDDto> results = mongoTemplate.aggregate(aggregation, "dvir_data" , DVIRDataCRUDDto.class).getMappedResults();
        return results;
		
    }
	
	public List<DVIRDataCRUDDto> lookupDVIRDataByClientOperation(long from, long to, long driverId, long clientId){
		
		MatchOperation filter = null;
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("lDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
					.and("clientId").is(clientId)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("lDateTime").gte(from).lte(to)
					.and("clientId").is(clientId)
				);
		}
		ProjectionOperation projectStage = Aggregation.project(
				"_id","driverId","location","truckDefect","trailerDefect","trailer","dateTime","lDateTime","notes","vehicleCondition",
				"driverSignFile","receivedTimestamp","engineHour","odometer","companyName","vehicleId","timestamp");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,
	    		sort(Direction.ASC, "driverId").and(Direction.DESC, "receivedTimestamp"));
        List<DVIRDataCRUDDto> results = mongoTemplate.aggregate(aggregation, "dvir_data" , DVIRDataCRUDDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<String> DeleteAllDriverStatusById(DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			long driverId = dvirDataCRUDDto.getDriverId();
			if(driverId>0) {
				Integer driverStatusCount = driveringStatusRepo.CountDriverStatusByDriverId(driverId);
				Integer dvirCount = dvirDataRepo.CountDVIRByDriverId(driverId);
				Integer certifiedLogCount = certifiedLogRepo.CountCertifiedLogByDriverId(driverId);
				Integer loginLogCount = loginLogRepo.CountLoginByDriverId((int)driverId);
				Integer dsLogCount = driverStatusLogRepo.CountDriverStatusLogByDriverId((int)driverId);
				
				splitLogRepo.deleteAllSplitLogByDriverId(driverId);			

				for(int i=0;i<dsLogCount;i++) {
					driverStatusLogRepo.deleteAllDriveringStatusLogByDriverId(driverId);
				}
				for(int i=0;i<driverStatusCount;i++) {
					driveringStatusRepo.deleteAllDriveringStatusByDriverId(driverId);
				}
				for(int i=0;i<dvirCount;i++) {
					dvirDataRepo.deleteAllDVIRDataByDriverId(driverId);
				}
				for(int i=0;i<certifiedLogCount;i++) {
					certifiedLogRepo.deleteAllCertifiedLogByDriverId(driverId);
				}
				for(int i=0;i<loginLogCount;i++) {
					loginLogRepo.deleteAllLoginLogByDriverId((int)driverId);
				}
				
				result.setResult("Deleted");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Deleted driver record Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> DeleteVoilationByDate(DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			long driverId = dvirDataCRUDDto.getDriverId();
			String fromDate = dvirDataCRUDDto.getFromDate();
			String toDate = dvirDataCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
			driveringStatusRepo.deleteAllDriveringStatusVoilation(driverId,from,to,1);

			result.setResult("Deleted");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Deleted driver voilation Successfully");
			
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> DeleteAllDriverStatusByIdAndDate(DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			long driverId = dvirDataCRUDDto.getDriverId();
			String fromDate = dvirDataCRUDDto.getFromDate();
			String toDate = dvirDataCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			if(driverId>0) {
				
				driveringStatusRepo.deleteAllDriveringStatusByDriverIdAndDate(driverId,from,to);			
				dvirDataRepo.deleteAllDVIRDataByDriverIdAndDate(driverId,from,to);
				certifiedLogRepo.deleteAllCertifiedLogByDriverIdAndDate(driverId,from,to);
				loginLogRepo.deleteAllLoginLogByDriverIdAndDate((int)driverId,from,to);
				driverStatusLogRepo.deleteAllDriveringStatusLogByDriverIdAndDate(driverId,from,to);
				
				result.setResult("Deleted");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Deleted driver record Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateDVIRData(List<MultipartFile> file,DVIRDataCRUDDto dvirDataCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtDateTime = LocalDateTime.parse(dvirDataCRUDDto.getDateTime(), formatter);
			long lDateTime = ldtDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			Instant instant = Instant.now();
			
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/dvir").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
			
			int iCount=0;
			String fileName = "";
			for(MultipartFile mf: file) {
				iCount++;
//					System.out.println("File Name : "+mf.getOriginalFilename());
				String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
	            try {
	                // Check if the file's name contains invalid characters
	                if(originalFileName.contains("..")) {
	                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
	                }
	                String fileExtension = "";
	                try {
	                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	                } catch(Exception e) {
	                    fileExtension = "";
	                }
	                if(iCount==1) {
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		
	                	}else {
	                		fileName = dvirDataCRUDDto.getDriverId()+ "_sign_"+iCount+"_"+lDateTime + fileExtension;
	       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	       	                
	                	}
	                }
	                
	            } catch (IOException ex) {
	                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
	            }
			}
			sDebug+="1,"+dvirDataCRUDDto+" <<,";
//			String sID = dvirDataCRUDDto.getDvirLogId();
//			sDebug+="1-0,"+sID+",";
//			ObjectId objId = new ObjectId(sID);
//			sDebug+="1-1,"+objId+",";
			Query query = new Query();
//			query.addCriteria(Criteria.where("_id").is(objId));
//			List<Criteria> criteria = new ArrayList<>();
//			criteria(Criteria.where("timestamp").is(dvirDataCRUDDto.getTimestamp()));
//			criteria(Criteria.where("driverId").is(dvirDataCRUDDto.getDriverId()));			
//			query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
			
			query.addCriteria(Criteria.where("timestamp").is(dvirDataCRUDDto.getTimestamp()));
			
	        Update update = new Update();
	        sDebug+="2,";
//	        update.set("driverId", dvirDataCRUDDto.getDriverId());
	        update.set("vehicleId", dvirDataCRUDDto.getVehicleId());
//	        update.set("dateTime", dvirDataCRUDDto.getDateTime());
//	        update.set("lDateTime", lDateTime);
//	        update.set("location", dvirDataCRUDDto.getLocation());
	        update.set("truckDefect", dvirDataCRUDDto.getTruckDefect());
	        update.set("trailerDefect", dvirDataCRUDDto.getTrailerDefect());
	        update.set("trailer", dvirDataCRUDDto.getTrailer());
	        update.set("notes", dvirDataCRUDDto.getNotes());
	        update.set("vehicleCondition", dvirDataCRUDDto.getVehicleCondition());
	        update.set("driverSignFile", fileName);
//	        update.set("companyName", dvirDataCRUDDto.getCompanyName());
//	        update.set("odometer", dvirDataCRUDDto.getOdometer());
//	        update.set("engineHour", dvirDataCRUDDto.getEngineHour());
	        mongoTemplate.findAndModify(query, update, DVIRData.class);
	        sDebug+="3,";
			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("DVIR Data Updated Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> AddELDOta(List<MultipartFile> file,ELDOta eldOta) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			
			Integer ID=0;
			Object maxID = eldOtaRepo.findMaxIdInELDOta();
			if(maxID==null) {
				ID=1;
				eldOta.setOtaId(ID);
			}else {
				ID = (Integer) maxID;
				ID++;
				eldOta.setOtaId(ID);
			}
			
			Instant instant = Instant.now();
			eldOta.setAddedTimestamp(instant.toEpochMilli());
			
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/eld_ota_backup").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
			
			int iCount=0;
			for(MultipartFile mf: file) {
				iCount++;
				String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
	            String fileName = "";
	            try {
	                // Check if the file's name contains invalid characters
	                if(originalFileName.contains("..")) {
	                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
	                }
	                String fileExtension = "";
	                try {
	                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	                } catch(Exception e) {
	                    fileExtension = "";
	                }
	                if(iCount==1) {
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		eldOta.setFirmwareFileName("");
	                	}else {
	                		fileName = instant.toEpochMilli()+ "_"+ID+"_"+iCount+fileExtension;
	       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	       	                eldOta.setFirmwareFileName(fileName);
	                	}
	                }
	                
	            } catch (IOException ex) {
	                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
	            }
			}
			
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/eld_ota").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
			
			iCount=0;
			for(MultipartFile mf: file) {
				iCount++;
				String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
	            try {
	                // Check if the file's name contains invalid characters
	                if(originalFileName.contains("..")) {
	                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
	                }
	                if(iCount==1) {
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		eldOta.setOriginalFirmwareFileName("");
	                	}else {
	       	                Path targetLocation = this.fileStorageLocation.resolve(originalFileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	       	                eldOta.setOriginalFirmwareFileName(originalFileName);
	                	}
	                }
	                
	            } catch (IOException ex) {
	                throw new Exception("Could not store file " + originalFileName + ". Please try again!", ex);
	            }
			}
			
			eldOta = eldOtaRepo.save(eldOta);
			
			result.setResult("Saved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("ELD Ota Data Added Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<ELDOta>> ViewELDOta() {
		ResultWrapper<List<ELDOta>> result = new ResultWrapper<>();
		try {
			String url1 = WEB_URL_BASE_PATH+"/uploads/eld_ota_backup/";
			String url2 = WEB_URL_BASE_PATH+"/uploads/eld_ota/";
			String setImagePath1;
			
			List<ELDOta> eldOtaData = eldOtaRepo.findAll();
			for(int i=0;i<eldOtaData.size();i++) {
				setImagePath1 = url1.concat(eldOtaData.get(i).getFirmwareFileName());
				eldOtaData.get(i).setFirmwareFileName(setImagePath1);
				setImagePath1 = url2.concat(eldOtaData.get(i).getOriginalFirmwareFileName());
				eldOtaData.get(i).setOriginalFirmwareFileName(setImagePath1);
			}
				
			result.setResult(eldOtaData);
			result.setStatus(Result.SUCCESS);
			result.setMessage("ELD Ota Data Information Send Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public String ViewLastELDOta() {
		String result = null;
		JSONObject json = new JSONObject();
		try {
			String url1 = WEB_URL_BASE_PATH+"/uploads/eld_ota/";
			String setImagePath1;
			
			Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
			Query query = new Query();
			query.limit(1);
			query.with(pageableRequest);
			List<ELDOta> eldOtaData = mongoTemplate.find(query, ELDOta.class,"eld_ota");
			
			for(int i=0;i<eldOtaData.size();i++) {
				setImagePath1 = url1.concat(eldOtaData.get(i).getOriginalFirmwareFileName());
				json.put("hardwareVersion", eldOtaData.get(i).getHardwareVersion());
				json.put("firmwareVersion", eldOtaData.get(i).getFirmwareVersion());
				json.put("otaUrl", setImagePath1);
				json.put("processOta", 1);
				result = json.toString();
			}
			
		}catch(Exception e) {
			json.put("status", 0);
			result = json.toString();
		}
		return result;	
	}
	
	public ResultWrapper<String> DeleteELDOta(ELDOtaCRUDDto eldOtaCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			Integer otaId = eldOtaCRUDDto.getOtaId();
			if(otaId>0) {
				ELDOta eldOta = eldOtaRepo.deleteELDOtaById(otaId);
				String url1 = WEB_URL_FILE_UPLOAD+"/uploads/eld_ota_backup/"+eldOta.getFirmwareFileName();
				Path  fileDeletePath1 = Paths.get(url1);
				Files.deleteIfExists(fileDeletePath1);
				result.setResult("Deleted");
				result.setStatus(Result.SUCCESS);
				result.setMessage("Deleted driver record Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public String AddELDOtaStatus(ELDOtaStatus eldOtaStatus) {
		String result = null;
		JSONObject json = new JSONObject();
		try {
			
			Integer ID=0;
			Object maxID = eldOtaStatusRepo.findMaxIdInELDOtaStatus();
			if(maxID==null) {
				ID=1;
				eldOtaStatus.setOtaStatusId(ID);
			}else {
				ID = (Integer) maxID;
				ID++;
				eldOtaStatus.setOtaStatusId(ID);
			}
			
			Instant instant = Instant.now();
			eldOtaStatus.setAddedTimestamp(instant.toEpochMilli());
			
			eldOtaStatus = eldOtaStatusRepo.save(eldOtaStatus);
			
			json.put("status", 1);
			result = json.toString();
			
		}catch(Exception e) {
			json.put("status", 0);
			result = json.toString();
		}
		return result;	
	}
	
	public ResultWrapper<List<ELDOtaStatus>> ViewELDOtaStatus(ELDOtaCRUDDto eldOtaCRUDDto) {
		ResultWrapper<List<ELDOtaStatus>> result = new ResultWrapper<>();
		try {
			
			String fromDate = eldOtaCRUDDto.getFromDate();
			String toDate = eldOtaCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
//			List<ELDOtaStatus> eldOtaStatusData = eldOtaStatusRepo.findAll();
			List<ELDOtaStatus> eldOtaStatusData = lookupELDOtaStatusOperation(from,to);
			result.setResult(eldOtaStatusData);
			result.setStatus(Result.SUCCESS);
			result.setMessage("ELD Ota Status Data Information Send Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<ELDOtaStatus> lookupELDOtaStatusOperation(long from, long to){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("addedTimestamp").gte(from).lte(to)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"otaStatusId","hardwareVersion","firmwareVersion","deviceId",
				"addedTimestamp").andExclude("_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage,sort(Direction.DESC, "addedTimestamp"));
        List<ELDOtaStatus> results = mongoTemplate.aggregate(aggregation, "eld_ota_status" , ELDOtaStatus.class).getMappedResults();
        return results;
		
    }

	public String ViewELDOtaStatusForTesting() {
		String result = null;
		JSONObject json = new JSONObject();
		json.put("status", 1);
		result = json.toString();
		return result;	
	}
	
	public ResultWrapper<String> AddCertifiedLogImage(List<MultipartFile> file) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
				
			this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/certified_signature").toAbsolutePath().normalize(); //production
			try {
				if (Files.notExists(this.fileStorageLocation)) {
					 Files.createDirectories(this.fileStorageLocation);
				}
	        } catch (Exception ex) {
	            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
	        }
				
			int iCount=0;
			for(MultipartFile mf: file) {
				iCount++;
//						System.out.println("File Name : "+mf.getOriginalFilename());
				String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
	            String fileName = originalFileName;
	            try {
	                // Check if the file's name contains invalid characters
	                if(originalFileName.contains("..")) {
	                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
	                }
	                String fileExtension = "";
	                try {
	                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	                } catch(Exception e) {
	                    fileExtension = "";
	                }
	                if(iCount==1) {
	                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
	                		
	                	}else {
	       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
	       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	                	}
	                }
	            } catch (IOException ex) {
	                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
	            }
			}
			
			result.setResult("Saved");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Certified Log Image Added Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddCertifiedLogOffline(AddCertifiedLogDto addCertifiedLogDto, String tokenValid) {
		ResultWrapper<List<AddDriveringStatusResponseDto>> result = new ResultWrapper<>();
		String sDebug=">> ";
		try {			
			Instant instant = Instant.now();
			
			List<AddDriveringStatusResponseDto> AddCertifiedLogData = new ArrayList<>();
			AddDriveringStatusResponseDto addCertifiedLog = new AddDriveringStatusResponseDto();
			
			ArrayList<CertifiedLog> certifiedLogData = addCertifiedLogDto.getCertifiedLogData();
			CertifiedLog certifiedLog = null;
			sDebug+="1,";
//			System.out.println(certifiedLogData.getCertifiedLogData());
//			System.out.println(getCertifiedLogData.size());
			
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			for(int i=0;i<certifiedLogData.size();i++) {
				sDebug+="2,";
				certifiedLog = certifiedLogData.get(i);
				sDebug+="3,"+certifiedLog;
				
//				LocalDate certifiedDate = LocalDate.parse(certifiedLog.getCertifiedDate(), formatter);
//				Instant iCertifiedDate = certifiedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();	
//				long timeInMillisCertifiedDate = iCertifiedDate.toEpochMilli();
//				certifiedLog.setLCertifiedDate(timeInMillisCertifiedDate);
				
				certifiedLog.setLCertifiedDate(certifiedLog.getCertifiedDateTime());
				certifiedLog.setAddedTimestamp(instant.toEpochMilli());
				
				certifiedLog = certifiedLogRepo.save(certifiedLog);
				
				DriverStatusLog driverStatusLog = new DriverStatusLog();
				 long maxId = lookupMaxIdOfDriverOperation(certifiedLog.getDriverId());
				if(maxId<=0) {
					maxId=1;
					driverStatusLog.setStatusId(maxId);
				}else {
					maxId++;
					driverStatusLog.setStatusId(maxId);
				}
				
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query queryLog = new Query(
				  Criteria.where("driverId").is(certifiedLog.getDriverId())
				);
				queryLog.limit(1);
				queryLog.with(pageableRequest);
				List<CertifiedLogViewDto> certifiedLogViewDtoData = mongoTemplate.find(queryLog, CertifiedLogViewDto.class,"certified_log");
				
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLog.getDriverId());
				
				driverStatusLog.setLogDataId(certifiedLogViewDtoData.get(0).get_id());
				driverStatusLog.setDriverId(certifiedLog.getDriverId());
				driverStatusLog.setVehicleId(certifiedLog.getVehicleId());
				driverStatusLog.setClientId(empInfo.getClientId());
				driverStatusLog.setStatus("Certified");
				driverStatusLog.setLattitude(0);
				driverStatusLog.setLongitude(0);
				driverStatusLog.setDateTime(certifiedLog.getLCertifiedDate());
				driverStatusLog.setLogType("Certified Log");
				driverStatusLog.setEngineHour("0");
				driverStatusLog.setOrigin("");
				driverStatusLog.setOdometer(0);
				driverStatusLog.setIsVoilation(0);
				driverStatusLog.setNote("");
				driverStatusLog.setCustomLocation("");
				driverStatusLog.setIsReportGenerated(1);
				driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
				driverStatusLog.setIsVisible(1);
				List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(certifiedLogViewDtoData.get(0).get_id());
				if(logDataExist.size()<=0) {
					driverStatusLogRepo.save(driverStatusLog);
					UpdateDriverSequenceId(certifiedLog.getDriverId(),certifiedLog.getLCertifiedDate());
				}
				
				//update sequence ID
				UpdateDriverSequenceId(certifiedLog.getDriverId(),certifiedLog.getLCertifiedDate());
				
				sDebug+="5,";
				CertifiedLogViewDto certifiedLogViewDto = certifiedLogRepo.findAndViewCertifiedLogByDriverId(certifiedLog.getDriverId(), certifiedLog.getLCertifiedDate());
				String objId = certifiedLogViewDto.get_id();
				sDebug+="6,";
				addCertifiedLog = new AddDriveringStatusResponseDto();
				addCertifiedLog.setLocalId(certifiedLog.getLocalId());
				addCertifiedLog.setServerId(objId);
				AddCertifiedLogData.add(addCertifiedLog);				
			}
			if(certifiedLog.getLCertifiedDate()>0) {
				result.setResult(AddCertifiedLogData);
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Certified Log Data Added Successfully");
			}else {
				result.setResult(null);
				result.setToken(tokenValid);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Certified Log Date!");
			}
			
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> AddCertifiedLog(List<MultipartFile> file,CertifiedLog certifiedLog, String tokenValid) {
		ResultWrapper<String> result = new ResultWrapper<>();
		try {
			
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//			LocalDate certifiedDate = LocalDate.parse(certifiedLog.getCertifiedDate(), formatter);
//			Instant iCertifiedDate = certifiedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();	
//			long timeInMillisCertifiedDate = iCertifiedDate.toEpochMilli();
//			certifiedLog.setLCertifiedDate(timeInMillisCertifiedDate);
			certifiedLog.setLCertifiedDate(certifiedLog.getCertifiedDateTime());

			Instant instant = Instant.now();
			certifiedLog.setAddedTimestamp(instant.toEpochMilli());
			
			if(tokenValid.equals("true")) {
				this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/certified_signature").toAbsolutePath().normalize(); //production
				try {
					if (Files.notExists(this.fileStorageLocation)) {
						 Files.createDirectories(this.fileStorageLocation);
					}
		        } catch (Exception ex) {
		            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
		        }
				
				int iCount=0;
				for(MultipartFile mf: file) {
					iCount++;
					String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
		            String fileName = originalFileName;
		            try {
		                // Check if the file's name contains invalid characters
		                if(originalFileName.contains("..")) {
		                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
		                }
		                String fileExtension = "";
		                try {
		                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		                } catch(Exception e) {
		                    fileExtension = "";
		                }
		                if(iCount==1) {
		                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
		                		certifiedLog.setCertifiedSignature("");
		                	}else {
//		                		fileName = instant.toEpochMilli()+ "_"+certifiedLog.getDriverId()+"_"+iCount+fileExtension;
		                		fileName = certifiedLog.getDriverId()+ "_sign_"+iCount + fileExtension;
		       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
		       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		       	                certifiedLog.setCertifiedSignature(fileName);
		                	}
		                }
		                
		            } catch (IOException ex) {
		                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		            }
				}
				
				certifiedLog = certifiedLogRepo.save(certifiedLog);
				
				 DriverStatusLog driverStatusLog = new DriverStatusLog();
				 long maxId = lookupMaxIdOfDriverOperation(certifiedLog.getDriverId());
				if(maxId<=0) {
					maxId=1;
					driverStatusLog.setStatusId(maxId);
				}else {
					maxId++;
					driverStatusLog.setStatusId(maxId);
				}
				
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query queryLog = new Query(
				  Criteria.where("driverId").is(certifiedLog.getDriverId())
				);
				queryLog.limit(1);
				queryLog.with(pageableRequest);
				List<CertifiedLogViewDto> certifiedLogViewDtoData = mongoTemplate.find(queryLog, CertifiedLogViewDto.class,"certified_log");
				
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLog.getDriverId());
				
				driverStatusLog.setLogDataId(certifiedLogViewDtoData.get(0).get_id());
				driverStatusLog.setDriverId(certifiedLog.getDriverId());
				driverStatusLog.setVehicleId(certifiedLog.getVehicleId());
				driverStatusLog.setClientId(empInfo.getClientId());
				driverStatusLog.setStatus("Certified");
				driverStatusLog.setLattitude(0);
				driverStatusLog.setLongitude(0);
				driverStatusLog.setDateTime(certifiedLog.getLCertifiedDate());
				driverStatusLog.setLogType("Certified Log");
				driverStatusLog.setEngineHour("0");
				driverStatusLog.setOrigin("");
				driverStatusLog.setOdometer(0);
				driverStatusLog.setIsVoilation(0);
				driverStatusLog.setNote("");
				driverStatusLog.setCustomLocation("");
				driverStatusLog.setIsReportGenerated(1);
				driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
				driverStatusLog.setIsVisible(1);
				List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(certifiedLogViewDtoData.get(0).get_id());
				if(logDataExist.size()<=0) {
					driverStatusLogRepo.save(driverStatusLog);
					UpdateDriverSequenceId(certifiedLog.getDriverId(),certifiedLog.getLCertifiedDate());
				}
				
			}else {
				this.fileStorageLocation = Paths.get(WEB_URL_FILE_UPLOAD+"/uploads/certified_signature").toAbsolutePath().normalize(); //production
				try {
					if (Files.notExists(this.fileStorageLocation)) {
						 Files.createDirectories(this.fileStorageLocation);
					}
		        } catch (Exception ex) {
		            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
		        }
				
				int iCount=0;
				for(MultipartFile mf: file) {
					iCount++;
					String originalFileName = StringUtils.cleanPath(mf.getOriginalFilename());
		            String fileName = "";
		            try {
		                // Check if the file's name contains invalid characters
		                if(originalFileName.contains("..")) {
		                    throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
		                }
		                String fileExtension = "";
		                try {
		                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		                } catch(Exception e) {
		                    fileExtension = "";
		                }
		                if(iCount==1) {
		                	if(mf.getOriginalFilename().isEmpty() || mf.getOriginalFilename().equals("")) {
		                		certifiedLog.setCertifiedSignature("");
		                	}else {
		                		fileName = instant.toEpochMilli()+ "_"+certifiedLog.getDriverId()+"_"+iCount+fileExtension;
		       	                Path targetLocation = this.fileStorageLocation.resolve(fileName);
		       	                Files.copy(mf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		       	                certifiedLog.setCertifiedSignature(fileName);
		                	}
		                }
		                
		            } catch (IOException ex) {
		                throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		            }
				}
				
				certifiedLog = certifiedLogRepo.save(certifiedLog);
				
				DriverStatusLog driverStatusLog = new DriverStatusLog();
				 long maxId = lookupMaxIdOfDriverOperation(certifiedLog.getDriverId());
				if(maxId<=0) {
					maxId=1;
					driverStatusLog.setStatusId(maxId);
				}else {
					maxId++;
					driverStatusLog.setStatusId(maxId);
				}
				
				Pageable pageableRequest = PageRequest.of(0, 1, Sort.by("_id").descending());
				Query queryLog = new Query(
				  Criteria.where("driverId").is(certifiedLog.getDriverId())
				);
				queryLog.limit(1);
				queryLog.with(pageableRequest);
				List<CertifiedLogViewDto> certifiedLogViewDtoData = mongoTemplate.find(queryLog, CertifiedLogViewDto.class,"certified_log");
				
				EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLog.getDriverId());
				
				driverStatusLog.setLogDataId(certifiedLogViewDtoData.get(0).get_id());
				driverStatusLog.setDriverId(certifiedLog.getDriverId());
				driverStatusLog.setVehicleId(certifiedLog.getVehicleId());
				driverStatusLog.setClientId(empInfo.getClientId());
				driverStatusLog.setStatus("Certified");
				driverStatusLog.setLattitude(0);
				driverStatusLog.setLongitude(0);
				driverStatusLog.setDateTime(certifiedLog.getLCertifiedDate());
				driverStatusLog.setLogType("Certified Log");
				driverStatusLog.setEngineHour("0");
				driverStatusLog.setOrigin("");
				driverStatusLog.setOdometer(0);
				driverStatusLog.setIsVoilation(0);
				driverStatusLog.setNote("");
				driverStatusLog.setCustomLocation("");
				driverStatusLog.setIsReportGenerated(1);
				driverStatusLog.setReceivedTimestamp(instant.toEpochMilli());
				driverStatusLog.setIsVisible(1);
				List<DriverStatusLog> logDataExist = driverStatusLogRepo.CheckDriverStatusLogById(certifiedLogViewDtoData.get(0).get_id());
				if(logDataExist.size()<=0) {
					driverStatusLogRepo.save(driverStatusLog);
					UpdateDriverSequenceId(certifiedLog.getDriverId(),certifiedLog.getLCertifiedDate());
				}
			}
			
			
			if(certifiedLog.getLCertifiedDate()>0) {
				//update sequence ID
				UpdateDriverSequenceId(certifiedLog.getDriverId(),certifiedLog.getLCertifiedDate());
				
				result.setResult("Saved");
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Certified Log Data Added Successfully");
			}else {
				result.setResult("Error");
				result.setToken(tokenValid);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Certified Log Date!");
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public ResultWrapper<List<CertifiedLogViewDto>> ViewCertifiedLog(CertifiedLogCRUDDto certifiedLogCRUDDto) {
		ResultWrapper<List<CertifiedLogViewDto>> result = new ResultWrapper<>();
		try {
			List<CertifiedLogViewDto> certifiedLogViewDto = null;
			
			long driverId = certifiedLogCRUDDto.getDriverId();
			String fromDate = certifiedLogCRUDDto.getFromDate();
			String toDate = certifiedLogCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			String driverName="";
			String url = WEB_URL_BASE_PATH+"/uploads/certified_signature/";
			String setImagePath1;
			if(driverId>0) {
				certifiedLogViewDto = lookupCertifiedLogDataOperation(from,to,driverId);
				for(int i=0;i<certifiedLogViewDto.size();i++) {
					setImagePath1 = url.concat(certifiedLogViewDto.get(i).getCertifiedSignature());
					certifiedLogViewDto.get(i).setCertifiedSignature(setImagePath1);
					
					EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)driverId);
					driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
					certifiedLogViewDto.get(i).setDriverName(driverName);
					
					if(certifiedLogViewDto.get(i).getCoDriverId()>0) {
						empInfo = employeeMasterRepo.findByEmployeeId((int)certifiedLogViewDto.get(i).getCoDriverId());
						driverName = empInfo.getFirstName()+" "+empInfo.getLastName();
						certifiedLogViewDto.get(i).setCoDriverName(driverName);
					}
					if(certifiedLogViewDto.get(i).getVehicleId()>0) {
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)certifiedLogViewDto.get(i).getVehicleId());
						certifiedLogViewDto.get(i).setVehicleName(vehcileInfo.getVehicleNo());
					}
				}
				
				result.setResult(certifiedLogViewDto);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Certified Log Information Send Successfully");
			}else {
				result.setResult(null);
				result.setStatus(Result.FAIL);
				result.setMessage("Invalid Request.");
			}
						
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage());
		}
		return result;	
	}
	
	public List<CertifiedLogViewDto> lookupCertifiedLogDataOperation(long from, long to, long driverId){
		
		MatchOperation filter = Aggregation.match(
					Criteria.where("lCertifiedDate").gte(from).lte(to)
					.and("driverId").is(driverId)
				);
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","vehicleId","trailers","shippingDocs","coDriverId","certifiedSignature","certifiedDate","lCertifiedDate",
				"addedTimestamp","_id","certifiedAt");
	   
	    Aggregation aggregation = Aggregation.newAggregation(filter,projectStage);
        List<CertifiedLogViewDto> results = mongoTemplate.aggregate(aggregation, "certified_log" , CertifiedLogViewDto.class).getMappedResults();
        return results;
		
    }
	
	public ResultWrapper<String> UpdateCertifiedLog(CertifiedLog certifiedLog, String tokenValid) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
		
			Instant instant = Instant.now();
			if(tokenValid.equals("true")) {
				Query query = new Query();
				query.addCriteria(
	        			Criteria.where("certifiedDate").is(certifiedLog.getCertifiedDate())
	        			.and("driverId").is(certifiedLog.getDriverId())
	        		);
		        Update update = new Update();
		        sDebug+="2,";
		        update.set("vehicleId", certifiedLog.getVehicleId());
		        update.set("trailers", certifiedLog.getTrailers());
		        update.set("shippingDocs", certifiedLog.getShippingDocs());
		        update.set("coDriverId", certifiedLog.getCoDriverId());
		        update.set("lCertifiedDate", certifiedLog.getCertifiedDateTime());
		        update.set("certifiedDateTime", certifiedLog.getCertifiedDateTime());
		        update.set("updatedTimestamp", instant.toEpochMilli());
//		        mongoTemplate.findAndModify(query, update, CertifiedLog.class);
		        mongoTemplate.updateMulti(query, update, CertifiedLog.class);
			}else {
				Query query = new Query();
				query.addCriteria(
	        			Criteria.where("certifiedDate").is(certifiedLog.getCertifiedDate())
	        			.and("driverId").is(certifiedLog.getDriverId())
	        		);
		        Update update = new Update();
		        sDebug+="2,";
		        update.set("vehicleId", certifiedLog.getVehicleId());
		        update.set("trailers", certifiedLog.getTrailers());
		        update.set("shippingDocs", certifiedLog.getShippingDocs());
		        update.set("coDriverId", certifiedLog.getCoDriverId());
		        update.set("lCertifiedDate", certifiedLog.getCertifiedDateTime());
		        update.set("certifiedDateTime", certifiedLog.getCertifiedDateTime());
		        update.set("updatedTimestamp", instant.toEpochMilli());
//		        mongoTemplate.findAndModify(query, update, CertifiedLog.class);
		        mongoTemplate.updateMulti(query, update, CertifiedLog.class);
			}
			result.setResult("Updated");
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("Certified Log Updated Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateCertifiedLogWithCoDriver(CertifiedLogCRUDDto certifiedLogCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			String certifiedLogId = certifiedLogCRUDDto.getCertifiedLogId();
			ObjectId logStatusId = new ObjectId(certifiedLogId);
			
			Query query = new Query();
			query.addCriteria(
        			Criteria.where("_id").is(logStatusId)
        		);
	        Update update = new Update();
	        sDebug+="2,";
	        update.set("trailers", certifiedLogCRUDDto.getTrailers());
	        update.set("shippingDocs", certifiedLogCRUDDto.getShippingDocs());
	        update.set("coDriverId", certifiedLogCRUDDto.getCoDriverId());
	        mongoTemplate.findAndModify(query, update, CertifiedLog.class);
	      
			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Certified Log Updated Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> ExportChartGraph(CertifiedLogCRUDDto certifiedLogCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			System.out.println("Here...1");
			
//			XYDataset dataset = createDataset();
//	        JFreeChart chart = createChart(dataset);
	        
//			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//			dataset.addValue(200, "Sales", "January");
//			dataset.addValue(150, "Sales", "February");
//			dataset.addValue(180, "Sales", "March");
//			dataset.addValue(260, "Sales", "April");
//			dataset.addValue(300, "Sales", "May");
			
			System.out.println("Here...2");
			
//			JFreeChart chart = ChartFactory.createLineChart(
//				    "Monthly Sales",
//				    "Month",
//				    "Sales",
//				    dataset);
			
			Document document = new Document(PageSize.A4.rotate());
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1250, BaseFont.EMBEDDED);
			Font font = new Font(baseFont, 10, Font.NORMAL);
//			font.setColor(255, 255, 255);
			font.setColor(0, 0, 0);
			
			String fileName = "graph_data.pdf"; 
			String subject = "Export Graph";
			String text = "graph report";
			
			PdfPTable createTable = getReportHeader("Static Graph Report", 1, "Testing");
			
			PdfWriter.getInstance(document, outputStream);
			
//			BufferedImage chartImage = chart.createBufferedImage( 1000, 400, null);
//			ImageIO.write(chartImage, "png", outputStream);
			
//			writeAsPNG(chart,outputStream,700,400);
			
			Image logoImg = com.itextpdf.text.Image.getInstance(WEB_URL_FILE_UPLOAD+"/graph_img/output_file.png"); 
			
			PdfPCell logo = new PdfPCell();
			logo.addElement(new Chunk(logoImg, 5, -5));
			logo.setVerticalAlignment(Element.ALIGN_CENTER);
			logo.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			logo.setRowspan(2);
			logo.setColspan(1);
			logo.setPadding(4);
			logo.setPaddingLeft(20);
			createTable.addCell(logo);
			
			document.open();
			document.add(createTable);
			document.close();
						
			System.out.println("Done...");

			// construct the text body part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(text);

			byte[] bytes = outputStream.toByteArray();

			// construct the file body part
			DataSource dataSource = null;

			if (fileName.contains("pdf")) {
				dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			} else if (fileName.contains("xlsx")) {
				dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
			}

			MimeBodyPart fileBodyPart = new MimeBodyPart();
			fileBodyPart.setDataHandler(new DataHandler(dataSource));
			fileBodyPart.setFileName(fileName);

			// construct the mime multi part
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(fileBodyPart);

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setTo("mrityunjayk1998@gmail.com");
			helper.setSubject(subject);
//	      	helper.addAttachment(attachmentFilename, dataSource);
			message.setContent(mimeMultipart);

			javaMailSender.send(message);
						
			result.setResult("Export");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Export Graph Successfully");
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public void writeAsPNG( JFreeChart chart, OutputStream out, int width, int height ) {
		try {
			BufferedImage chartImage = chart.createBufferedImage( width, height, null);
			File outputfile = new File("D:\\saved.png");
			ImageIO.write( chartImage, "png", outputfile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
    Map<Integer, String> labels = new HashMap<>();  // Map to store strings
	public XYDataset createDataset() {
		XYSeries series = new XYSeries("Driver Status");
        String iCount="";
        for(int i=1;i<24;i++) {
        	iCount = String.valueOf(i);
        	if(iCount.length()>1) {
                series.add(i, 1);
            	labels.put(i, "Driver " + i);
        	}else {
        		series.add(i, 1);
            	labels.put(i, "Driver " + i);
        	}
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
       
        return dataset;
    }
	

	public JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Driver Working Status",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        
//        for (int i = 1; i <24; i++) {
//            String label = labels.get(i);
//
//            // Create annotations for each data point to display the string label
//            XYTextAnnotation annotation = new XYTextAnnotation(label, i, i);
//            annotation.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
//            plot.addAnnotation(annotation);
//            
//        }

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }
	
	public ResultWrapper<String> AddDriverWorkingStatus(DriverWorkingStatus driverWorkingStatus, String tokenValid) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
						
			Instant instant = Instant.now();
			driverWorkingStatus.setReceivedTimestamp(instant.toEpochMilli());
			
			if(tokenValid.equals("true")) {
				List<DriverWorkingStatus> driverWorkingData = driverWorkingStatusRepo.findAndViewDriverWorkingstatusById(driverWorkingStatus.getDriverId());
				if(driverWorkingData.size()<=0) {
					driverWorkingStatus = driverWorkingStatusRepo.save(driverWorkingStatus);
				}else {
					Query query = new Query();
			        query.addCriteria(Criteria.where("driverId").is(driverWorkingStatus.getDriverId()));
			        Update update = new Update();
			        update.set("shift", driverWorkingStatus.getShift());
			        update.set("days", driverWorkingStatus.getDays());
			        update.set("status", driverWorkingStatus.getStatus());
			        update.set("onDutyTime", driverWorkingStatus.getOnDutyTime());
			        update.set("onDriveTime", driverWorkingStatus.getOnDriveTime());
			        update.set("onSleepTime", driverWorkingStatus.getOnSleepTime());
			        update.set("weeklyTime", driverWorkingStatus.getWeeklyTime());
			        update.set("onBreak", driverWorkingStatus.getOnBreak());
			        update.set("tokenNo", driverWorkingStatus.getTokenNo());
			        update.set("receivedTimestamp", driverWorkingStatus.getReceivedTimestamp());
			        mongoTemplate.findAndModify(query, update, DriverWorkingStatus.class);
				}
				
				result.setResult("Saved");
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("Driver Working Status Save Successfully");
				
			}else {
				result.setResult("error");
				result.setToken(tokenValid);
				result.setStatus(Result.FAIL);
				result.setMessage("Token Expired.");
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> UpdateDriveringStatusForDotReport(CertifiedLogCRUDDto certifiedLogCRUDDto) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
		
			long driverId = certifiedLogCRUDDto.getDriverId();
			String fromDate = certifiedLogCRUDDto.getFromDate();
			String toDate = certifiedLogCRUDDto.getToDate();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			Query query = new Query();
			query.addCriteria(
        			Criteria.where("utcDateTime").gte(from).lte(to)
        			.and("driverId").is(driverId)
        		);
	        Update update = new Update();
	        sDebug+="2,";
	        update.set("isReportGenerated", 1);
	        mongoTemplate.updateMulti(query, update, DriveringStatus.class);
			
			result.setResult("Updated");
			result.setStatus(Result.SUCCESS);
			result.setMessage("Dot Report Data Updated Successfully");
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<String> AddELDSupport(ELDSupport eldSupport, String tokenValid) {
		ResultWrapper<String> result = new ResultWrapper<>();
		String sDebug="";
		try {
			Instant instant = Instant.now();
			
			if(tokenValid.equals("true")) {
				eldSupport.setReceivedTimestamp(instant.toEpochMilli());
				eldSupport.setStatus("pending");
				eldSupport = eldSupportRepo.save(eldSupport);
				
				result.setResult("Saved");
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("ELD Support Save Successfully");
			}else {
				result.setResult("error");
				result.setToken(tokenValid);
				result.setStatus(Result.FAIL);
				result.setMessage("Token Expired.");
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<List<ELDSupportViewDto>> ViewELDSupport(ELDSupportViewDto eldSupportViewDto, String tokenValid) {
		ResultWrapper<List<ELDSupportViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long driverId = eldSupportViewDto.getDriverId();
			
			LocalDate periviousDate = LocalDate.now();
			periviousDate = periviousDate.minusDays(14);
			String fromDate = periviousDate.toString()+" 00:00:00";
			String toDate = LocalDate.now().toString()+" 23:59:59";

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			if(tokenValid.equals("true")) {
				List<ELDSupportViewDto> eldSupportViewDtoData=lookupELDSupportDataOperation(from,to,driverId);
				for(int i=0;i<eldSupportViewDtoData.size();i++) {
					try {
						if(eldSupportViewDtoData.get(i).getDriverId()>0) {
							EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)eldSupportViewDtoData.get(i).getDriverId());
							eldSupportViewDtoData.get(i).setDriverName(empInfo.getFirstName()+" "+empInfo.getLastName());
						}else {
							eldSupportViewDtoData.get(i).setDriverName("");
						}
						if(eldSupportViewDtoData.get(i).getVehicleId()>0) {
							VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)eldSupportViewDtoData.get(i).getVehicleId());
							eldSupportViewDtoData.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
						}else {
							eldSupportViewDtoData.get(i).setVehicleNo("");
						}
						if(eldSupportViewDtoData.get(i).getCompanyId()>0) {
							ClientMaster clientInfo = clientMasterRepo.findByClientId((int)eldSupportViewDtoData.get(i).getCompanyId());
							eldSupportViewDtoData.get(i).setCompanyName(clientInfo.getClientName());
						}else {
							eldSupportViewDtoData.get(i).setCompanyName("");
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				result.setResult(eldSupportViewDtoData);
				result.setToken(tokenValid);
				result.setStatus(Result.SUCCESS);
				result.setMessage("ELD Support Send Data Successfully");
			}else {
				result.setResult(null);
				result.setToken(tokenValid);
				result.setStatus(Result.FAIL);
				result.setMessage("Token Expired.");
			}
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public ResultWrapper<List<ELDSupportViewDto>> ViewELDSupportByDate(ELDSupportViewDto eldSupportViewDto, String tokenValid) {
		ResultWrapper<List<ELDSupportViewDto>> result = new ResultWrapper<>();
		String sDebug="";
		try {
			long driverId = eldSupportViewDto.getDriverId();
			String fromDate = eldSupportViewDto.getFromDate();
			String toDate = eldSupportViewDto.getToDate();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
			LocalDateTime ldtFromDate = LocalDateTime.parse(fromDate, formatter);
			long from = ldtFromDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			LocalDateTime ldtToDate = LocalDateTime.parse(toDate, formatter);
			long to = ldtToDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			List<ELDSupportViewDto> eldSupportViewDtoData=lookupELDSupportDataOperation(from,to,driverId);
			for(int i=0;i<eldSupportViewDtoData.size();i++) {
				try {
					if(eldSupportViewDtoData.get(i).getDriverId()>0) {
						EmployeeMaster empInfo = employeeMasterRepo.findByEmployeeId((int)eldSupportViewDtoData.get(i).getDriverId());
						eldSupportViewDtoData.get(i).setDriverName(empInfo.getFirstName()+" "+empInfo.getLastName());
					}else {
						eldSupportViewDtoData.get(i).setDriverName("");
					}
					if(eldSupportViewDtoData.get(i).getVehicleId()>0) {
						VehicleMaster vehcileInfo = vehicleMasterRepo.findByVehicleId((int)eldSupportViewDtoData.get(i).getVehicleId());
						eldSupportViewDtoData.get(i).setVehicleNo(vehcileInfo.getVehicleNo());
					}else {
						eldSupportViewDtoData.get(i).setVehicleNo("");
					}
					if(eldSupportViewDtoData.get(i).getCompanyId()>0) {
						ClientMaster clientInfo = clientMasterRepo.findByClientId((int)eldSupportViewDtoData.get(i).getCompanyId());
						eldSupportViewDtoData.get(i).setCompanyName(clientInfo.getClientName());
					}else {
						eldSupportViewDtoData.get(i).setCompanyName("");
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			result.setResult(eldSupportViewDtoData);
			result.setToken(tokenValid);
			result.setStatus(Result.SUCCESS);
			result.setMessage("ELD Support Send Data Successfully");
			
			
		}catch(Exception e) {
			result.setStatus(Result.FAIL);
			result.setMessage(e.getLocalizedMessage()+sDebug);
		}
		return result;	
	}
	
	public List<ELDSupportViewDto> lookupELDSupportDataOperation(long from, long to, long driverId){
		
		MatchOperation filter = null;
		if(driverId>0) {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
					.and("driverId").is(driverId)
				);
		}else {
			filter = Aggregation.match(
					Criteria.where("utcDateTime").gte(from).lte(to)
				);
		}
		
		ProjectionOperation projectStage = Aggregation.project(
				"driverId","vehicleId","companyId","message","status","utcDateTime","receivedTimestamp","_id");
	   
	    Aggregation aggregation = Aggregation.newAggregation( filter,projectStage);
        List<ELDSupportViewDto> results = mongoTemplate.aggregate(aggregation, "eld_support" , ELDSupportViewDto.class).getMappedResults();
        return results;
		
    }
	
}
