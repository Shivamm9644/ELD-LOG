package com.mes.eld_log.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
import com.mes.eld_log.dtos.DVIRDataCRUDDto;
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
import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.EmployeeMasterViewDto;
import com.mes.eld_log.dtos.IftaReportViewDto;
import com.mes.eld_log.dtos.IftaSummaryReport;
import com.mes.eld_log.dtos.LiveDataLogViewDto;
import com.mes.eld_log.dtos.LoginLogViewDto;
import com.mes.eld_log.dtos.ViewDriverLogWithDetailDto;
import com.mes.eld_log.dtos.ViewDriverWorkingDayStatus;
import com.mes.eld_log.models.CertifiedLog;
import com.mes.eld_log.models.DVIRData;
import com.mes.eld_log.models.DefectDetails;
import com.mes.eld_log.models.DispatchDetails;
import com.mes.eld_log.models.DriverWorkingStatus;
import com.mes.eld_log.models.DriveringStatus;
import com.mes.eld_log.models.ELDOta;
import com.mes.eld_log.models.ELDOtaStatus;
import com.mes.eld_log.models.ELDSupport;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.ExceptionLog;
import com.mes.eld_log.models.IFTAReports;
import com.mes.eld_log.results.ResultWrapper;

public interface DispatchService {

	public ResultWrapper<String> AddDispatchDetails(DispatchDetailCRUDDto dispatchDetailCRUDDto);
	public ResultWrapper<List<DispatchDetailViewDto>> ViewDispatchDetails(DispatchDetailCRUDDto dispatchDetailCRUDDto);
	public ResultWrapper<AllDispatchDataViewDto> ViewDispatchData(DispatchDetailCRUDDto dispatchDetailCRUDDto);
	public ResultWrapper<String> AddExceptionLog(ExceptionLog exceptionLog, String tokenValid);
	public ResultWrapper<List<EldLogDataViewDto>> ViewEldLogData();
	public ResultWrapper<DriveringStatusViewDto> AddDriveringStatus(DriveringStatus driveringStatus, String tokenValid);
	public ResultWrapper<String> AddDriveringStatusFromWeb(DriveringStatus driveringStatus);

	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDriveringStatusOffline(AddDriveringStatusDto addDriveringStatusDto, String tokenValid);

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatus(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid);
	public ResultWrapper<String> ShiftDriveringStatusWithoutUpdate(DriveringStatusCRUDDto driveringStatusCRUDDto);	

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusForLog(DriveringStatusCRUDDto driveringStatusCRUDDto);	

	public ResultWrapper<EmployeeMasterCRUDDto> ViewDriveringStatusWithLoginDetails(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid);	

	public ResultWrapper<List<DriveringStatusLogViewDto>> ViewDriveringStatusLog(DriveringStatusCRUDDto driveringStatusCRUDDto);	
	public ResultWrapper<String> AssignLogToDriver(AssignLogToDriverDto assignLogToDriverDto);	
	public ResultWrapper<String> ShiftHourInDriveringStatusLog(AssignLogToDriverDto assignLogToDriverDto);	

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusForGraph(DriveringStatusCRUDDto driveringStatusCRUDDto);	
	public ResultWrapper<List<DriveringStatusViewDto>> ViewVoilationReport(DriveringStatusCRUDDto driveringStatusCRUDDto);	

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusForGraphNew(DriveringStatusCRUDDto driveringStatusCRUDDto);	

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusCalculation(DriveringStatusCRUDDto driveringStatusCRUDDto);	


	public ResultWrapper<List<DriveringStatusViewDto>> ViewUnidentifiedEvents(DriveringStatusCRUDDto driveringStatusCRUDDto);	

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriveringStatusByDate(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid);	

	public ResultWrapper<ViewDriverLogWithDetailDto> ViewDriverLogWithDetails(DriveringStatusCRUDDto driveringStatusCRUDDto);	

	public ResultWrapper<List<ViewDriverWorkingDayStatus>> ViewDriverWorkingDay(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<DriverLogWithLoginLogViewDto> ViewDriverLogWithLoginLog(DriveringStatusCRUDDto driveringStatusCRUDDto, String tokenValid);
	public ResultWrapper<List<LoginLogViewDto>> LoginLogForWeb(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<String> UpdateDriverLog(DriveringStatusCRUDDto driveringStatusCRUDDto);
	public ResultWrapper<String> UpdateAndEnableDisableDriverLog(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<String> UpdateAndShiftDriverLog(DriveringStatusLogViewDto driveringStatusLogViewDto);
	public ResultWrapper<String> UpdateAndShiftDriverLogInBulk(AssignLogToDriverDto assignLogToDriverDto);

	public ResultWrapper<List<EmployeeMasterViewDto>> ViewAllDriverStatus(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<List<DriveringStatusViewDto>> ViewDriverWorkingStatus(DriveringStatusCRUDDto driveringStatusCRUDDto);
	public ResultWrapper<List<LiveDataLogViewDto>> ViewLiveDataLog(ClientMasterCRUDDto clientMasterCRUDDto);
	public ResultWrapper<List<LiveDataLogViewDto>> ViewELDLogHistory(ClientMasterCRUDDto clientMasterCRUDDto);
	public ResultWrapper<String> UpdateStateInEldLogData(ClientMasterCRUDDto clientMasterCRUDDto);
	public ResultWrapper<String> ViewIdlingReport(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<List<IftaReportViewDto>> ViewIftaReport(DriveringStatusCRUDDto driveringStatusCRUDDto);
	public ResultWrapper<List<IftaReportViewDto>> ViewIftaReportNew(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<List<IftaReportViewDto>> ViewIftaGeneratedSummaryReport(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<List<IftaSummaryReport>> ViewIftaSummaryReport(DriveringStatusCRUDDto driveringStatusCRUDDto);

	public ResultWrapper<DVIRDataCRUDDto> AddDVIRData(List<MultipartFile> file, DVIRData dvirData, String tokenValid);
	
	public ResultWrapper<String> AddDVIRDataImage(List<MultipartFile> file);

	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDVIRDataOffline(AddDvirDataDto addDvirDataDto, String tokenValid);
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDefectDataOffline(List<MultipartFile> file,AddDvirDataDto addDvirDataDto, String tokenValid);
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddDefectData(MultipartFile file,DefectDetails defectDetails, String tokenValid);

	public ResultWrapper<List<AlertsLogViewDto>> ViewAlerts(AlertsLogViewDto alertsLogViewDto);
	public ResultWrapper<String> UpdateAlerts(AlertsLogViewDto alertsLogViewDto);
	public ResultWrapper<String> UpdateAllUnreadAlerts(AlertsLogViewDto alertsLogViewDto);

	public ResultWrapper<List<DVIRDataCRUDDto>> ViewDVIRData(DVIRDataCRUDDto dvirDataCRUDDto, String tokenValid);
	public ResultWrapper<String> DownloadDVIRData(DVIRDataCRUDDto dvirDataCRUDDto);
	public ResultWrapper<String> DownloadDVIRDataEncript(DVIRDataCRUDDto dvirDataCRUDDto);
	public ResultWrapper<EldReportDto> ELDReportEncrypt(DVIRDataCRUDDto dvirDataCRUDDto);

	public ResultWrapper<List<DVIRDataCRUDDto>> ViewDVIRDataByTimestamp(DVIRDataCRUDDto dvirDataCRUDDto);
	public ResultWrapper<String> DeleteDvir(DVIRDataCRUDDto dvirDataCRUDDto);

	public ResultWrapper<String> DeleteAllDriverStatusById(DVIRDataCRUDDto dvirDataCRUDDto);
	public ResultWrapper<String> DeleteVoilationByDate(DVIRDataCRUDDto dvirDataCRUDDto);

	public ResultWrapper<String> DeleteAllDriverStatusByIdAndDate(DVIRDataCRUDDto dvirDataCRUDDto);

	public ResultWrapper<String> UpdateDVIRData(List<MultipartFile> file, DVIRDataCRUDDto dvirDataCRUDDto);
	public ResultWrapper<String> AddELDOta(List<MultipartFile> file, ELDOta eldOta);
	public ResultWrapper<List<ELDOta>> ViewELDOta();
	public String ViewLastELDOta();
	public ResultWrapper<String> DeleteELDOta(ELDOtaCRUDDto eldOtaCRUDDto);
	public String AddELDOtaStatus(ELDOtaStatus eldOtaStatus);
	public ResultWrapper<List<ELDOtaStatus>> ViewELDOtaStatus(ELDOtaCRUDDto eldOtaCRUDDto);
	public String ViewELDOtaStatusForTesting();
	
	public ResultWrapper<String> AddCertifiedLogImage(List<MultipartFile> file);
	public ResultWrapper<List<AddDriveringStatusResponseDto>> AddCertifiedLogOffline(AddCertifiedLogDto addCertifiedLogDto, String tokenValid);

	public ResultWrapper<String> AddCertifiedLog(List<MultipartFile> file, CertifiedLog certifiedLog, String tokenValid);
	public ResultWrapper<List<CertifiedLogViewDto>> ViewCertifiedLog(CertifiedLogCRUDDto certifiedLogCRUDDto);
	public ResultWrapper<String> UpdateCertifiedLog(CertifiedLog certifiedLog, String tokenValid);
	public ResultWrapper<String> UpdateCertifiedLogWithCoDriver(CertifiedLogCRUDDto certifiedLogCRUDDto);

	public ResultWrapper<String> ExportChartGraph(CertifiedLogCRUDDto certifiedLogCRUDDto);
	public ResultWrapper<String> AddDriverWorkingStatus(DriverWorkingStatus driverWorkingStatus, String tokenValid);
	public ResultWrapper<String> UpdateDriveringStatusForDotReport(CertifiedLogCRUDDto certifiedLogCRUDDto);
	
	public ResultWrapper<String> AddELDSupport(ELDSupport eldSupport, String tokenValid);
	public ResultWrapper<List<ELDSupportViewDto>> ViewELDSupport(ELDSupportViewDto eldSupportViewDto, String tokenValid);
	public ResultWrapper<List<ELDSupportViewDto>> ViewELDSupportByDate(ELDSupportViewDto eldSupportViewDto, String tokenValid);

}
