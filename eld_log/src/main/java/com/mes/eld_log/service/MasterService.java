package com.mes.eld_log.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mes.eld_log.dtos.CargoTypeMasterCRUDDto;
import com.mes.eld_log.dtos.CarrierMasterCRUDDto;
import com.mes.eld_log.dtos.ClientMasterCRUDDto;
import com.mes.eld_log.dtos.ClientMasterViewDto;
import com.mes.eld_log.dtos.CompanyMasterCRUDDto;
import com.mes.eld_log.dtos.CountryStateCityMasterCRUDDto;
import com.mes.eld_log.dtos.CustomerMasterCRUDDto;
import com.mes.eld_log.dtos.CycleCanadaCRUDDto;
import com.mes.eld_log.dtos.CycleUsaCRUDDto;
import com.mes.eld_log.dtos.DefectMasterCRUDDto;
import com.mes.eld_log.dtos.DeviceMasterCRUDDto;
import com.mes.eld_log.dtos.DeviceMasterViewDto;
import com.mes.eld_log.dtos.DeviceModalMasterCRUDDto;
import com.mes.eld_log.dtos.DriverInfoViewDto;
import com.mes.eld_log.dtos.EldConnectionInterfaceCRUDDto;
import com.mes.eld_log.dtos.EmployeeMasterCRUDDto;
import com.mes.eld_log.dtos.EmployeeMasterListViewDto;
import com.mes.eld_log.dtos.EmployeeMasterViewDto;
import com.mes.eld_log.dtos.ExceptionMasterCRUDDto;
import com.mes.eld_log.dtos.FuelTypeMasterCRUDDto;
import com.mes.eld_log.dtos.GeofanceMasterCRUDDto;
import com.mes.eld_log.dtos.LanguageMasterCRUDDto;
import com.mes.eld_log.dtos.MainTerminalMasterCRUDDto;
import com.mes.eld_log.dtos.PaymentStatusMasterCRUDDto;
import com.mes.eld_log.dtos.ProductMasterCRUDDto;
import com.mes.eld_log.dtos.ProjectDetailAnalyticsViewDto;
import com.mes.eld_log.dtos.ReceiverMasterCRUDDto;
import com.mes.eld_log.dtos.ReferModeCRUDDto;
import com.mes.eld_log.dtos.RestBreakMasterCRUDDto;
import com.mes.eld_log.dtos.RestartMasterCRUDDto;
import com.mes.eld_log.dtos.RouteMasterCRUDDto;
import com.mes.eld_log.dtos.ShipperMasterCRUDDto;
import com.mes.eld_log.dtos.SimulatorCRUDDto;
import com.mes.eld_log.dtos.TimezoneMasterCRUDDto;
import com.mes.eld_log.dtos.TrailerMasterCRUDDto;
import com.mes.eld_log.dtos.UserMasterCRUDDto;
import com.mes.eld_log.dtos.UserMasterViewDto;
import com.mes.eld_log.dtos.UserTypeMasterCRUDDto;
import com.mes.eld_log.dtos.VehicleConditionCRUDDto;
import com.mes.eld_log.dtos.VehicleMasterCRUDDto;
import com.mes.eld_log.dtos.VehicleMasterViewDto;
import com.mes.eld_log.dtos.VehicleTypeMasterCRUDDto;
import com.mes.eld_log.models.CargoTypeMaster;
import com.mes.eld_log.models.CarrierMaster;
import com.mes.eld_log.models.CityMaster;
import com.mes.eld_log.models.ClientMaster;
import com.mes.eld_log.models.CompanyMaster;
import com.mes.eld_log.models.CountryMaster;
import com.mes.eld_log.models.CustomerMaster;
import com.mes.eld_log.models.CycleCanada;
import com.mes.eld_log.models.CycleUsa;
import com.mes.eld_log.models.DefectMaster;
import com.mes.eld_log.models.DeviceMaster;
import com.mes.eld_log.models.DeviceModalMaster;
import com.mes.eld_log.models.DeviceStatus;
import com.mes.eld_log.models.Disclaimer;
import com.mes.eld_log.models.ELDSettings;
import com.mes.eld_log.models.EldConnectionInterface;
import com.mes.eld_log.models.EmployeeMaster;
import com.mes.eld_log.models.ExceptionMaster;
import com.mes.eld_log.models.FuelTypeMaster;
import com.mes.eld_log.models.GeofanceMaster;
import com.mes.eld_log.models.LanguageMaster;
import com.mes.eld_log.models.MACAddressMaster;
import com.mes.eld_log.models.MainTerminalMaster;
import com.mes.eld_log.models.PaymentStatusMaster;
import com.mes.eld_log.models.ProductMaster;
import com.mes.eld_log.models.ReceiverMaster;
import com.mes.eld_log.models.ReferModeMaster;
import com.mes.eld_log.models.RestBreakMaster;
import com.mes.eld_log.models.RestartMaster;
import com.mes.eld_log.models.RouteMaster;
import com.mes.eld_log.models.ShipperMaster;
import com.mes.eld_log.models.Simulator;
import com.mes.eld_log.models.StateMaster;
import com.mes.eld_log.models.TimezoneMaster;
import com.mes.eld_log.models.TrailerMaster;
import com.mes.eld_log.models.UserMaster;
import com.mes.eld_log.models.UserTypeMaster;
import com.mes.eld_log.models.VehicleCondition;
import com.mes.eld_log.models.VehicleMaster;
import com.mes.eld_log.models.VehicleTypeMaster;
import com.mes.eld_log.results.ResultWrapper;

public interface MasterService {

	public ResultWrapper<EmployeeMaster> AddEmployee(EmployeeMaster empInfo);
	public ResultWrapper<List<EmployeeMasterViewDto>> ViewEmployee(EmployeeMasterCRUDDto employeeMasterCRUDDto, String tokenValid);
	public ResultWrapper<List<EmployeeMasterViewDto>> ViewEmployeeFirstLogin(EmployeeMasterCRUDDto employeeMasterCRUDDto, String tokenValid);

	public ResultWrapper<List<EmployeeMasterListViewDto>> ViewEmployeeByClient(EmployeeMasterCRUDDto employeeMasterCRUDDto, String tokenValid);	

	public ResultWrapper<EmployeeMaster> DeleteEmployee(EmployeeMasterCRUDDto employeeMasterCRUDDto);
	public ResultWrapper<EmployeeMaster> UpdateEmployee(EmployeeMaster empInfo);
	public ResultWrapper<String> UpdateEmployeeActiveInactive(EmployeeMasterCRUDDto employeeMasterCRUDDto);

	public ResultWrapper<List<DriverInfoViewDto>> ViewDriverInformation(EmployeeMasterCRUDDto employeeMasterCRUDDto, String tokenValid);
	public ResultWrapper<String> TokenCheckingAPI(EmployeeMasterCRUDDto employeeMasterCRUDDto, String serverToken);

	public ResultWrapper<DeviceMaster> AddDevice(DeviceMaster deviceMaster);
	public ResultWrapper<List<DeviceMasterViewDto>> ViewDevice(DeviceMasterCRUDDto deviceMasterCRUDDto);
	public ResultWrapper<DeviceMaster> DeleteDevice(DeviceMasterCRUDDto deviceMasterCRUDDto);
	public ResultWrapper<DeviceMaster> UpdateDevice(DeviceMaster deviceMaster);
	public ResultWrapper<VehicleMaster> AddVehicle(VehicleMaster vehicleMaster);
	public ResultWrapper<List<VehicleMasterViewDto>> ViewVehicle(VehicleMasterCRUDDto vehicleMasterCRUDDto, String tokenValid);
	public ResultWrapper<List<VehicleMasterViewDto>> ViewActiveVehicle(VehicleMasterCRUDDto vehicleMasterCRUDDto, String tokenValid);

	public ResultWrapper<VehicleMaster> DeleteVehicle(VehicleMasterCRUDDto vehicleMasterCRUDDto);
	public ResultWrapper<VehicleMaster> UpdateVehicle(VehicleMaster vehicleMaster);
	public ResultWrapper<CustomerMaster> AddCustomer(CustomerMaster customerMaster);
	public ResultWrapper<List<CustomerMaster>> ViewCustomer(CustomerMasterCRUDDto customerMasterCRUDDto);
	public ResultWrapper<CustomerMaster> DeleteCustomer(CustomerMasterCRUDDto customerMasterCRUDDto);
	public ResultWrapper<CustomerMaster> UpdateCustomer(CustomerMaster customerMaster);
	public ResultWrapper<ShipperMaster> AddShipper(ShipperMaster shipperMaster);
	public ResultWrapper<List<ShipperMaster>> ViewShipper(ShipperMasterCRUDDto shipperMasterCRUDDto);
	public ResultWrapper<ShipperMaster> DeleteShipper(ShipperMasterCRUDDto shipperMasterCRUDDto);
	public ResultWrapper<ShipperMaster> UpdateShipper(ShipperMaster shipperMaster);
	public ResultWrapper<ReceiverMaster> AddReceiver(ReceiverMaster receiverMaster);
	public ResultWrapper<List<ReceiverMaster>> ViewReceiver(ReceiverMasterCRUDDto receiverMasterCRUDDto);
	public ResultWrapper<ReceiverMaster> DeleteReceiver(ReceiverMasterCRUDDto receiverMasterCRUDDto);
	public ResultWrapper<ReceiverMaster> UpdateReceiver(ReceiverMaster receiverMaster);
	public ResultWrapper<CarrierMaster> AddCarrier(CarrierMaster carrierMaster);
	public ResultWrapper<List<CarrierMaster>> ViewCarrier(CarrierMasterCRUDDto carrierMasterCRUDDto);
	public ResultWrapper<CarrierMaster> DeleteCarrier(CarrierMasterCRUDDto carrierMasterCRUDDto);
	public ResultWrapper<CarrierMaster> UpdateCarrier(CarrierMaster carrierMaster);
	public ResultWrapper<CountryMaster> AddCountry(CountryMaster countryMaster);
	public ResultWrapper<List<CountryMaster>> ViewCountry(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);
	public ResultWrapper<CountryMaster> DeleteCountry(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);
	public ResultWrapper<CountryMaster> UpdateCountry(CountryMaster countryMaster);
	public ResultWrapper<StateMaster> AddState(StateMaster stateMaster);
	public ResultWrapper<List<StateMaster>> ViewState(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);
	public ResultWrapper<List<StateMaster>> ViewStateByCountry(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);

	public ResultWrapper<StateMaster> DeleteState(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);
	public ResultWrapper<StateMaster> UpdateState(StateMaster stateMaster);
	public ResultWrapper<CityMaster> AddCity(CityMaster cityMaster);
	public ResultWrapper<List<CityMaster>> ViewCity(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);
	public ResultWrapper<CityMaster> DeleteCity(CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto);
	public ResultWrapper<CityMaster> UpdateCity(CityMaster cityMaster);
	public ResultWrapper<ProductMaster> AddProduct(ProductMaster ProductMaster);
	public ResultWrapper<List<ProductMaster>> ViewProduct(ProductMasterCRUDDto ProductMasterCRUDDto);
	public ResultWrapper<ProductMaster> DeleteProduct(ProductMasterCRUDDto ProductMasterCRUDDto);
	public ResultWrapper<ProductMaster> UpdateProduct(ProductMaster productMaster);
	public ResultWrapper<RouteMaster> AddRoute(RouteMaster routeMaster);
	public ResultWrapper<List<RouteMaster>> ViewRoute(RouteMasterCRUDDto routeMasterCRUDDto);
	public ResultWrapper<RouteMaster> DeleteRoute(RouteMasterCRUDDto routeMasterCRUDDto);
	public ResultWrapper<RouteMaster> UpdateRoute(RouteMaster routeMaster);
	public ResultWrapper<String> GenerateCompanyNo();

	public ResultWrapper<ClientMaster> AddClient(ClientMaster clientMaster);
	public ResultWrapper<List<ClientMasterViewDto>> ViewClient(ClientMasterCRUDDto clientMasterCRUDDto);
	public ResultWrapper<ClientMaster> DeleteClient(ClientMasterCRUDDto clientMasterCRUDDto);
	public ResultWrapper<ClientMaster> UpdateClient(ClientMaster clientMaster);
	public ResultWrapper<String> UpdateClientActiveInactive(UserMasterCRUDDto userMasterCRUDDto);
	public ResultWrapper<String> UpdateClientGraceTime(UserMasterCRUDDto userMasterCRUDDto);

	public ResultWrapper<UserTypeMaster> AddUserType(UserTypeMaster userTypeMaster);
	public ResultWrapper<List<UserTypeMaster>> ViewUserType(UserTypeMasterCRUDDto userTypeMasterCRUDDto);
	public ResultWrapper<UserTypeMaster> DeleteUserType(UserTypeMasterCRUDDto userTypeMasterCRUDDto);
	public ResultWrapper<UserTypeMaster> UpdateUserType(UserTypeMaster userTypeMaster);
	public ResultWrapper<UserMaster> AddUser(UserMaster userMaster);
	public ResultWrapper<List<UserMasterViewDto>> ViewUser(UserMasterCRUDDto userMasterCRUDDto);
	public ResultWrapper<UserMaster> DeleteUser(UserMasterCRUDDto userMasterCRUDDto);
	public ResultWrapper<UserMaster> UpdateUser(UserMaster userMaster);
	public ResultWrapper<String> UpdateUserFeature(UserMasterCRUDDto userMasterCRUDDto);
	public ResultWrapper<String> ResetUserToken(UserMasterCRUDDto userMasterCRUDDto);


	public ResultWrapper<CompanyMaster> AddCompany(CompanyMaster companyMaster);
	public ResultWrapper<List<CompanyMaster>> ViewCompany(CompanyMasterCRUDDto companyMasterCRUDDto);
	public ResultWrapper<CompanyMaster> DeleteCompany(CompanyMasterCRUDDto companyMasterCRUDDto);
	public ResultWrapper<CompanyMaster> UpdateCompany(CompanyMaster companyMaster);
	public ResultWrapper<LanguageMaster> AddLanguage(LanguageMaster languageMaster);
	public ResultWrapper<List<LanguageMaster>> ViewLanguage(LanguageMasterCRUDDto languageMasterCRUDDto);	
	public ResultWrapper<LanguageMaster> DeleteLanguage(LanguageMasterCRUDDto languageMasterCRUDDto);
	public ResultWrapper<LanguageMaster> UpdateLanguage(LanguageMaster languageMaster);
	public ResultWrapper<CycleUsa> AddCycleUsa(CycleUsa cycleUsa);
	public ResultWrapper<List<CycleUsa>> ViewCycleUsa(CycleUsaCRUDDto cycleUsaCRUDDto);
	public ResultWrapper<CycleUsa> DeleteCycleUsa(CycleUsaCRUDDto cycleUsaCRUDDto);
	public ResultWrapper<CycleUsa> UpdateCycleUsa(CycleUsa cycleUsa);
	public ResultWrapper<CycleCanada> AddCycleCanada(CycleCanada cycleCanada);
	public ResultWrapper<List<CycleCanada>> ViewCycleCanada(CycleCanadaCRUDDto cycleCanadaCRUDDto);
	public ResultWrapper<CycleCanada> DeleteCycleCanada(CycleCanadaCRUDDto cycleCanadaCRUDDto);
	public ResultWrapper<CycleCanada> UpdateCycleCanada(CycleCanada cycleCanada);	
	public ResultWrapper<DeviceModalMaster> AddDeviceModal(DeviceModalMaster deviceModalMaster);
	public ResultWrapper<List<DeviceModalMaster>> ViewDeviceModal(DeviceModalMasterCRUDDto deviceModalMasterCRUDDto);
	public ResultWrapper<DeviceModalMaster> DeleteDeviceModal(DeviceModalMasterCRUDDto deviceModalMasterCRUDDto);
	public ResultWrapper<DeviceModalMaster> UpdateDeviceModal(DeviceModalMaster deviceModalMaster);
	public ResultWrapper<VehicleTypeMaster> AddVehicleType(VehicleTypeMaster vehicleTypeMaster);
	public ResultWrapper<List<VehicleTypeMaster>> ViewVehicleType(VehicleTypeMasterCRUDDto vehicleTypeMasterCRUDDto);
	public ResultWrapper<VehicleTypeMaster> DeleteVehicleType(VehicleTypeMasterCRUDDto vehicleTypeMasterCRUDDto);
	public ResultWrapper<VehicleTypeMaster> UpdateVehicleType(VehicleTypeMaster vehicleTypeMaster);
	public ResultWrapper<PaymentStatusMaster> AddPaymentStatus(PaymentStatusMaster paymentStatusMaster);
	public ResultWrapper<List<PaymentStatusMaster>> ViewPaymentStatus(PaymentStatusMasterCRUDDto paymentStatusMasterCRUDDto);
	public ResultWrapper<PaymentStatusMaster> DeletePaymentStatus(PaymentStatusMasterCRUDDto paymentStatusMasterCRUDDto);
	public ResultWrapper<PaymentStatusMaster> UpdatePaymentStatus(PaymentStatusMaster paymentStatusMaster);
	public ResultWrapper<ExceptionMaster> AddExceptionMaster(ExceptionMaster exceptionMaster);
	public ResultWrapper<List<ExceptionMaster>> ViewExceptionMaster(ExceptionMasterCRUDDto exceptionMasterCRUDDto, String tokenValid);
	public ResultWrapper<ExceptionMaster> DeleteExceptionMaster(ExceptionMasterCRUDDto exceptionMasterCRUDDto);
	public ResultWrapper<ExceptionMaster> UpdateExceptionMaster(ExceptionMaster exceptionMaster);
	public ResultWrapper<MainTerminalMaster> AddMainTerminalMaster(MainTerminalMaster mainTerminalMastere);
	public ResultWrapper<List<MainTerminalMaster>> ViewMainTerminalMaster(MainTerminalMasterCRUDDto mainTerminalMasterCRUDDto);
	public ResultWrapper<List<MainTerminalMaster>> ViewAllMainTerminalMaster(MainTerminalMasterCRUDDto mainTerminalMasterCRUDDto);

	public ResultWrapper<MainTerminalMaster> DeleteMainTerminalMaster(MainTerminalMasterCRUDDto mainTerminalMasterCRUDDto);
	public ResultWrapper<MainTerminalMaster> UpdateMainTerminalMaster(MainTerminalMaster mainTerminalMastere);
	public ResultWrapper<CargoTypeMaster> AddCargoTypeMaster(CargoTypeMaster cargoTypeMaster);
	public ResultWrapper<List<CargoTypeMaster>> ViewCargoTypeMaster(CargoTypeMasterCRUDDto cargoTypeMasterCRUDDto);
	public ResultWrapper<CargoTypeMaster> DeleteCargoTypeMaster(CargoTypeMasterCRUDDto cargoTypeMasterCRUDDto);
	public ResultWrapper<CargoTypeMaster> UpdateCargoTypeMaster(CargoTypeMaster cargoTypeMaster);
	public ResultWrapper<FuelTypeMaster> AddFuelTypeMaster(FuelTypeMaster fuelTypeMaster);
	public ResultWrapper<List<FuelTypeMaster>> ViewFuelTypeMaster(FuelTypeMasterCRUDDto fuelTypeMasterCRUDDto);
	public ResultWrapper<FuelTypeMaster> DeleteFuelTypeMaster(FuelTypeMasterCRUDDto fuelTypeMasterCRUDDto);
	public ResultWrapper<FuelTypeMaster> UpdateFuelTypeMaster(FuelTypeMaster fuelTypeMaster);
	
	public ResultWrapper<ReferModeMaster> AddReferModeMaster(ReferModeMaster referModeMaster);
	public ResultWrapper<List<ReferModeMaster>> ViewReferModeMaster(ReferModeCRUDDto referModeCRUDDto);
	public ResultWrapper<ReferModeMaster> DeleteReferModeMaster(ReferModeCRUDDto referModeCRUDDto);
	public ResultWrapper<ReferModeMaster> UpdateReferModeMaster(ReferModeMaster referModeMaster);
	
	public ResultWrapper<TrailerMaster> AddTrailerMaster(TrailerMaster trailerMaster);
	public ResultWrapper<List<TrailerMaster>> ViewTrailerMaster(TrailerMasterCRUDDto trailerMasterCRUDDto);
	public ResultWrapper<TrailerMaster> DeleteTrailerMaster(TrailerMasterCRUDDto trailerMasterCRUDDto);
	public ResultWrapper<TrailerMaster> UpdateTrailerMaster(TrailerMaster trailerMaster);

	public ResultWrapper<Simulator> AddSimulator(Simulator simulator);
	public ResultWrapper<List<SimulatorCRUDDto>> ViewSimulator(SimulatorCRUDDto simulatorCRUDDto);
	public ResultWrapper<Simulator> DeleteSimulator(SimulatorCRUDDto SimulatorCRUDDto);
	
	public ResultWrapper<DefectMaster> AddDefect(DefectMaster defectMaster);
	public ResultWrapper<List<DefectMaster>> ViewDefect(DefectMasterCRUDDto defectMasterCRUDDto, String tokenValid);
	public ResultWrapper<DefectMaster> DeleteDefect(DefectMasterCRUDDto defectMasterCRUDDto);
	public ResultWrapper<DefectMaster> UpdateDefect(DefectMaster defectMaster);

	public ResultWrapper<RestartMaster> AddRestart(RestartMaster restartMaster);
	public ResultWrapper<List<RestartMaster>> ViewRestart(RestartMasterCRUDDto restartMasterCRUDDto);
	public ResultWrapper<RestartMaster> DeleteRestart(RestartMasterCRUDDto restartMasterCRUDDto);
	public ResultWrapper<RestartMaster> UpdateRestart(RestartMaster restartMaster);
	
	public ResultWrapper<RestBreakMaster> AddRestBreak(RestBreakMaster restBreakMaster);
	public ResultWrapper<List<RestBreakMaster>> ViewRestBreak(RestBreakMasterCRUDDto restBreakMasterCRUDDto);
	public ResultWrapper<RestBreakMaster> DeleteRestBreak(RestBreakMasterCRUDDto restBreakMasterCRUDDto);
	public ResultWrapper<RestBreakMaster> UpdateRestBreak(RestBreakMaster restBreakMaster);

	public ResultWrapper<TimezoneMaster> AddTimezone(TimezoneMaster timezoneMaster);
	public ResultWrapper<List<TimezoneMaster>> ViewTimezone(TimezoneMasterCRUDDto timezoneMasterCRUDDto);
	public ResultWrapper<TimezoneMaster> DeleteTimezone(TimezoneMasterCRUDDto timezoneMasterCRUDDto);
	public ResultWrapper<TimezoneMaster> UpdateTimezone(TimezoneMaster timezoneMaster);

	public ResultWrapper<EldConnectionInterface> AddEldConnectionInterface(EldConnectionInterface eldConnectionInterface);
	public ResultWrapper<List<EldConnectionInterface>> ViewEldConnectionInterface(EldConnectionInterfaceCRUDDto eldConnectionInterfaceCRUDDto);
	public ResultWrapper<String> AddMacAddress(MACAddressMaster macAddressMaster, String tokenValid);
	public ResultWrapper<String> AddDeviceStatus(DeviceStatus deviceStatus, String tokenValid);
	
	public ResultWrapper<VehicleCondition> AddVehicleCondition(VehicleCondition vehicleCondition);
	public ResultWrapper<List<VehicleCondition>> ViewVehicleCondition(VehicleConditionCRUDDto vehicleConditionCRUDDto);
	public ResultWrapper<VehicleCondition> DeleteVehicleCondition(VehicleConditionCRUDDto vehicleConditionCRUDDto);
	public ResultWrapper<VehicleCondition> UpdateVehicleCondition(VehicleCondition vehicleCondition);
	public ResultWrapper<List<GeofanceMaster>> ViewGeofanceMaster(GeofanceMasterCRUDDto geofanceMasterCRUDDto);

	public ResultWrapper<ELDSettings> AddEldSettings(ELDSettings eldSettings);
	public ResultWrapper<List<ELDSettings>> ViewEldSettings(ELDSettings eldSettings);
	public ResultWrapper<ELDSettings> DeleteEldSettings(ELDSettings eldSettings);
	public ResultWrapper<ELDSettings> UpdateEldSettings(ELDSettings eldSettings);
	
	public ResultWrapper<Disclaimer> AddDisclaimer(Disclaimer disclaimer);
	public ResultWrapper<List<Disclaimer>> ViewDisclaimer();
	public ResultWrapper<String> UpdateDisclaimerInUser(UserMasterCRUDDto userMasterCRUDDto);
	public ResultWrapper<String> UpdateDisclaimerInDriver(UserMasterCRUDDto userMasterCRUDDto);

	public ResultWrapper<ProjectDetailAnalyticsViewDto> ViewProjectDetailAnalytics();

}
