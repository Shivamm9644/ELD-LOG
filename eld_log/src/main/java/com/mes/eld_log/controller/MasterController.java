package com.mes.eld_log.controller;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.eld_log.dtos.AddEmployeeInfoDto;
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
import com.mes.eld_log.service.MasterService;
import com.mes.eld_log.util.eldLogUtils;


@RestController
@RequestMapping("/master")
public class MasterController{
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

	@Autowired
	private MasterService masterService;
	
	@Autowired
	private eldLogUtils imobilityUtils;
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
	   Map<String, String> errors = new HashMap<>();
	 
	   errors.put("status", "FAIL");
	   ex.getBindingResult().getFieldErrors().forEach(error ->
	           errors.put(error.getField(), error.getDefaultMessage()));
	   
	   return errors;
	}
	
	@PostMapping(value="/add_employee")
//	public ResponseEntity<ResultWrapper<EmployeeMaster>> AddEmployee(@Valid @RequestBody AddEmployeeInfoDto addEmployeeInfoDto)
	public ResponseEntity<ResultWrapper<EmployeeMaster>> AddEmployee(@Valid @RequestBody EmployeeMaster employeeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		System.out.println(" >>> 1");
//		EmployeeMaster employeeMaster = imobilityUtils.CopyEmployeeInfoDtoObjectToObject(addEmployeeInfoDto);
		System.out.println(" >>> 2");
		ResultWrapper<EmployeeMaster> result = null;	
		result = masterService.AddEmployee(employeeMaster);
		return new ResponseEntity<ResultWrapper<EmployeeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_employee")
	public ResponseEntity<ResultWrapper<List<EmployeeMasterViewDto>>> ViewEmployee(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<EmployeeMasterViewDto>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)employeeMasterCRUDDto.getEmployeeId(),employeeMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewEmployee(employeeMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<EmployeeMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_employee_first_login")
	public ResponseEntity<ResultWrapper<List<EmployeeMasterViewDto>>> ViewEmployeeFirstLogin(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<EmployeeMasterViewDto>> result = null;	
		String tokenValid="false";
		
		result = masterService.ViewEmployeeFirstLogin(employeeMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<EmployeeMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_employee_by_client")
	public ResponseEntity<ResultWrapper<List<EmployeeMasterListViewDto>>> ViewEmployeeByClient(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<EmployeeMasterListViewDto>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)employeeMasterCRUDDto.getEmployeeId(),employeeMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewEmployeeByClient(employeeMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<EmployeeMasterListViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_employee")
	public ResponseEntity<ResultWrapper<EmployeeMaster>> DeleteEmployee(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<EmployeeMaster> result = null;	
		result = masterService.DeleteEmployee(employeeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<EmployeeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_employee")
	public ResponseEntity<ResultWrapper<EmployeeMaster>> UpdateEmployee(@Valid @RequestBody EmployeeMaster employeeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<EmployeeMaster> result = null;	
		result = masterService.UpdateEmployee(employeeMaster);
		return new ResponseEntity<ResultWrapper<EmployeeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_employee_active_inactive")
	public ResponseEntity<ResultWrapper<String>> UpdateEmployeeActiveInactive(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.UpdateEmployeeActiveInactive(employeeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_driver_information")
	public ResponseEntity<ResultWrapper<List<DriverInfoViewDto>>> ViewDriverInformation(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<DriverInfoViewDto>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)employeeMasterCRUDDto.getEmployeeId(),employeeMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewDriverInformation(employeeMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<DriverInfoViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/token_checking_api")
	public ResponseEntity<ResultWrapper<String>> TokenCheckingAPI(@Valid @RequestBody EmployeeMasterCRUDDto employeeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;
		
		String tokenValid = imobilityUtils.CheckTokenNo(employeeMasterCRUDDto.getEmployeeId(),employeeMasterCRUDDto.getTokenNo());
		
		result = masterService.TokenCheckingAPI(employeeMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_device")
	public ResponseEntity<ResultWrapper<DeviceMaster>> AddDevice(@Valid @RequestBody DeviceMaster deviceMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DeviceMaster> result = null;	
		result = masterService.AddDevice(deviceMaster);
		return new ResponseEntity<ResultWrapper<DeviceMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_device")
	public ResponseEntity<ResultWrapper<List<DeviceMasterViewDto>>> ViewDevice(@Valid @RequestBody DeviceMasterCRUDDto deviceMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<DeviceMasterViewDto>> result = null;	
		result = masterService.ViewDevice(deviceMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<DeviceMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_device")
	public ResponseEntity<ResultWrapper<DeviceMaster>> DeleteDevice(@Valid @RequestBody DeviceMasterCRUDDto deviceMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DeviceMaster> result = null;	
		result = masterService.DeleteDevice(deviceMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<DeviceMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_device")
	public ResponseEntity<ResultWrapper<DeviceMaster>> UpdateDevice(@Valid @RequestBody DeviceMaster deviceMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DeviceMaster> result = null;	
		result = masterService.UpdateDevice(deviceMaster);
		return new ResponseEntity<ResultWrapper<DeviceMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_vehicle")
	public ResponseEntity<ResultWrapper<VehicleMaster>> AddVehicle(@Valid @RequestBody VehicleMaster vehicleMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleMaster> result = null;	
		result = masterService.AddVehicle(vehicleMaster);
		return new ResponseEntity<ResultWrapper<VehicleMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_vehicle")
	public ResponseEntity<ResultWrapper<List<VehicleMasterViewDto>>> ViewVehicle(@Valid @RequestBody VehicleMasterCRUDDto vehicleMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<VehicleMasterViewDto>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)vehicleMasterCRUDDto.getDriverId(),vehicleMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewVehicle(vehicleMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<VehicleMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_active_vehicle")
	public ResponseEntity<ResultWrapper<List<VehicleMasterViewDto>>> ViewActiveVehicle(@Valid @RequestBody VehicleMasterCRUDDto vehicleMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<VehicleMasterViewDto>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)vehicleMasterCRUDDto.getDriverId(),vehicleMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewActiveVehicle(vehicleMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<VehicleMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_vehicle")
	public ResponseEntity<ResultWrapper<VehicleMaster>> DeleteVehicle(@Valid @RequestBody VehicleMasterCRUDDto vehicleMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleMaster> result = null;	
		result = masterService.DeleteVehicle(vehicleMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<VehicleMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_vehicle")
	public ResponseEntity<ResultWrapper<VehicleMaster>> UpdateVehicle(@Valid @RequestBody VehicleMaster vehicleMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleMaster> result = null;	
		result = masterService.UpdateVehicle(vehicleMaster);
		return new ResponseEntity<ResultWrapper<VehicleMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_country")
	public ResponseEntity<ResultWrapper<CountryMaster>> AddCountry(@Valid @RequestBody CountryMaster countryMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CountryMaster> result = null;	
		result = masterService.AddCountry(countryMaster);
		return new ResponseEntity<ResultWrapper<CountryMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_country")
	public ResponseEntity<ResultWrapper<List<CountryMaster>>> ViewCountry(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CountryMaster>> result = null;	
		result = masterService.ViewCountry(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CountryMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_country")
	public ResponseEntity<ResultWrapper<CountryMaster>> DeleteCountry(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CountryMaster> result = null;	
		result = masterService.DeleteCountry(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<CountryMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_country")
	public ResponseEntity<ResultWrapper<CountryMaster>> UpdateCountry(@Valid @RequestBody CountryMaster countryMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CountryMaster> result = null;	
		result = masterService.UpdateCountry(countryMaster);
		return new ResponseEntity<ResultWrapper<CountryMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_state")
	public ResponseEntity<ResultWrapper<StateMaster>> AddState(@Valid @RequestBody StateMaster stateMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<StateMaster> result = null;	
		result = masterService.AddState(stateMaster);
		return new ResponseEntity<ResultWrapper<StateMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_state")
	public ResponseEntity<ResultWrapper<List<StateMaster>>> ViewState(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<StateMaster>> result = null;	
		result = masterService.ViewState(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<StateMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_state_by_country")
	public ResponseEntity<ResultWrapper<List<StateMaster>>> ViewStateByCountry(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<StateMaster>> result = null;	
		result = masterService.ViewStateByCountry(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<StateMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_state")
	public ResponseEntity<ResultWrapper<StateMaster>> DeleteState(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<StateMaster> result = null;	
		result = masterService.DeleteState(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<StateMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_state")
	public ResponseEntity<ResultWrapper<StateMaster>> UpdateState(@Valid @RequestBody StateMaster stateMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<StateMaster> result = null;	
		result = masterService.UpdateState(stateMaster);
		return new ResponseEntity<ResultWrapper<StateMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_city")
	public ResponseEntity<ResultWrapper<CityMaster>> AddCity(@Valid @RequestBody CityMaster cityMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CityMaster> result = null;	
		result = masterService.AddCity(cityMaster);
		return new ResponseEntity<ResultWrapper<CityMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_city")
	public ResponseEntity<ResultWrapper<List<CityMaster>>> ViewCity(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CityMaster>> result = null;	
		result = masterService.ViewCity(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CityMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_city")
	public ResponseEntity<ResultWrapper<CityMaster>> DeleteCity(@Valid @RequestBody CountryStateCityMasterCRUDDto countryStateCityMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CityMaster> result = null;	
		result = masterService.DeleteCity(countryStateCityMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<CityMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_city")
	public ResponseEntity<ResultWrapper<CityMaster>> UpdateCity(@Valid @RequestBody CityMaster cityMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CityMaster> result = null;	
		result = masterService.UpdateCity(cityMaster);
		return new ResponseEntity<ResultWrapper<CityMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_product")
	public ResponseEntity<ResultWrapper<ProductMaster>> AddProduct(@Valid @RequestBody ProductMaster productMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ProductMaster> result = null;	
		result = masterService.AddProduct(productMaster);
		return new ResponseEntity<ResultWrapper<ProductMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_product")
	public ResponseEntity<ResultWrapper<List<ProductMaster>>> ViewProduct(@Valid @RequestBody ProductMasterCRUDDto productMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ProductMaster>> result = null;	
		result = masterService.ViewProduct(productMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<ProductMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_product")
	public ResponseEntity<ResultWrapper<ProductMaster>> DeleteProduct(@Valid @RequestBody ProductMasterCRUDDto productMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ProductMaster> result = null;	
		result = masterService.DeleteProduct(productMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<ProductMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_product")
	public ResponseEntity<ResultWrapper<ProductMaster>> UpdateProduct(@Valid @RequestBody ProductMaster productMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ProductMaster> result = null;	
		result = masterService.UpdateProduct(productMaster);
		return new ResponseEntity<ResultWrapper<ProductMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_customer")
	public ResponseEntity<ResultWrapper<CustomerMaster>> AddCustomer(@Valid @RequestBody CustomerMaster customerMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CustomerMaster> result = null;	
		result = masterService.AddCustomer(customerMaster);
		return new ResponseEntity<ResultWrapper<CustomerMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_customer")
	public ResponseEntity<ResultWrapper<List<CustomerMaster>>> ViewCustomer(@Valid @RequestBody CustomerMasterCRUDDto customerMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CustomerMaster>> result = null;	
		result = masterService.ViewCustomer(customerMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CustomerMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_customer")
	public ResponseEntity<ResultWrapper<CustomerMaster>> DeleteCustomer(@Valid @RequestBody CustomerMasterCRUDDto customerMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CustomerMaster> result = null;	
		result = masterService.DeleteCustomer(customerMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<CustomerMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_customer")
	public ResponseEntity<ResultWrapper<CustomerMaster>> UpdateCustomer(@Valid @RequestBody CustomerMaster customerMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CustomerMaster> result = null;	
		result = masterService.UpdateCustomer(customerMaster);
		return new ResponseEntity<ResultWrapper<CustomerMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_shipper")
	public ResponseEntity<ResultWrapper<ShipperMaster>> AddShipper(@Valid @RequestBody ShipperMaster shipperMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ShipperMaster> result = null;	
		result = masterService.AddShipper(shipperMaster);
		return new ResponseEntity<ResultWrapper<ShipperMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_shipper")
	public ResponseEntity<ResultWrapper<List<ShipperMaster>>> ViewShipper(@Valid @RequestBody ShipperMasterCRUDDto shipperMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ShipperMaster>> result = null;	
		result = masterService.ViewShipper(shipperMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<ShipperMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_shipper")
	public ResponseEntity<ResultWrapper<ShipperMaster>> DeleteShipper(@Valid @RequestBody ShipperMasterCRUDDto shipperMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ShipperMaster> result = null;	
		result = masterService.DeleteShipper(shipperMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<ShipperMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_shipper")
	public ResponseEntity<ResultWrapper<ShipperMaster>> UpdateShipper(@Valid @RequestBody ShipperMaster shipperMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ShipperMaster> result = null;	
		result = masterService.UpdateShipper(shipperMaster);
		return new ResponseEntity<ResultWrapper<ShipperMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_receiver")
	public ResponseEntity<ResultWrapper<ReceiverMaster>> AddReceiver(@Valid @RequestBody ReceiverMaster receiverMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ReceiverMaster> result = null;	
		result = masterService.AddReceiver(receiverMaster);
		return new ResponseEntity<ResultWrapper<ReceiverMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_receiver")
	public ResponseEntity<ResultWrapper<List<ReceiverMaster>>> ViewReceiver(@Valid @RequestBody ReceiverMasterCRUDDto receiverMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ReceiverMaster>> result = null;	
		result = masterService.ViewReceiver(receiverMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<ReceiverMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_receiver")
	public ResponseEntity<ResultWrapper<ReceiverMaster>> DeleteReceiver(@Valid @RequestBody ReceiverMasterCRUDDto receiverMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ReceiverMaster> result = null;	
		result = masterService.DeleteReceiver(receiverMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<ReceiverMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_receiver")
	public ResponseEntity<ResultWrapper<ReceiverMaster>> UpdateReceiver(@Valid @RequestBody ReceiverMaster receiverMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ReceiverMaster> result = null;	
		result = masterService.UpdateReceiver(receiverMaster);
		return new ResponseEntity<ResultWrapper<ReceiverMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_route")
	public ResponseEntity<ResultWrapper<RouteMaster>> AddRoute(@Valid @RequestBody RouteMaster routeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RouteMaster> result = null;	
		result = masterService.AddRoute(routeMaster);
		return new ResponseEntity<ResultWrapper<RouteMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_route")
	public ResponseEntity<ResultWrapper<List<RouteMaster>>> ViewRoute(@Valid @RequestBody RouteMasterCRUDDto routeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<RouteMaster>> result = null;	
		result = masterService.ViewRoute(routeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<RouteMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_route")
	public ResponseEntity<ResultWrapper<RouteMaster>> DeleteRoute(@Valid @RequestBody RouteMasterCRUDDto routeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RouteMaster> result = null;	
		result = masterService.DeleteRoute(routeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<RouteMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_route")
	public ResponseEntity<ResultWrapper<RouteMaster>> UpdateRoute(@Valid @RequestBody RouteMaster routeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RouteMaster> result = null;	
		result = masterService.UpdateRoute(routeMaster);
		return new ResponseEntity<ResultWrapper<RouteMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_carrier")
	public ResponseEntity<ResultWrapper<CarrierMaster>> AddCarrier(@Valid @RequestBody CarrierMaster carrierMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CarrierMaster> result = null;	
		result = masterService.AddCarrier(carrierMaster);
		return new ResponseEntity<ResultWrapper<CarrierMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_carrier")
	public ResponseEntity<ResultWrapper<List<CarrierMaster>>> ViewCarrier(@Valid @RequestBody CarrierMasterCRUDDto carrierMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CarrierMaster>> result = null;	
		result = masterService.ViewCarrier(carrierMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CarrierMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_carrier")
	public ResponseEntity<ResultWrapper<CarrierMaster>> DeleteCarrier(@Valid @RequestBody CarrierMasterCRUDDto carrierMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CarrierMaster> result = null;	
		result = masterService.DeleteCarrier(carrierMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<CarrierMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_carrier")
	public ResponseEntity<ResultWrapper<CarrierMaster>> UpdateCarrier(@Valid @RequestBody CarrierMaster carrierMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CarrierMaster> result = null;	
		result = masterService.UpdateCarrier(carrierMaster);
		return new ResponseEntity<ResultWrapper<CarrierMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/get_next_company_no")
	public ResponseEntity<ResultWrapper<String>> GenerateCompanyNo()
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.GenerateCompanyNo();
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_client")
	public ResponseEntity<ResultWrapper<ClientMaster>> AddClient(@Valid @RequestBody ClientMaster clientMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ClientMaster> result = null;	
		result = masterService.AddClient(clientMaster);
		return new ResponseEntity<ResultWrapper<ClientMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_client")
	public ResponseEntity<ResultWrapper<List<ClientMasterViewDto>>> ViewClient(@Valid @RequestBody ClientMasterCRUDDto clientMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ClientMasterViewDto>> result = null;	
		result = masterService.ViewClient(clientMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<ClientMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_client")
	public ResponseEntity<ResultWrapper<ClientMaster>> DeleteClient(@Valid @RequestBody ClientMasterCRUDDto clientMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ClientMaster> result = null;	
		result = masterService.DeleteClient(clientMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<ClientMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_client")
	public ResponseEntity<ResultWrapper<ClientMaster>> UpdateClient(@Valid @RequestBody ClientMaster clientMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ClientMaster> result = null;	
		result = masterService.UpdateClient(clientMaster);
		return new ResponseEntity<ResultWrapper<ClientMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_client_grace_time")
	public ResponseEntity<ResultWrapper<String>> UpdateClientGraceTime(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.UpdateClientGraceTime(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_client_active_inactive")
	public ResponseEntity<ResultWrapper<String>> UpdateClientActiveInactive(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.UpdateClientActiveInactive(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_user_type")
	public ResponseEntity<ResultWrapper<UserTypeMaster>> AddUserType(@Valid @RequestBody UserTypeMaster userTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserTypeMaster> result = null;	
		result = masterService.AddUserType(userTypeMaster);
		return new ResponseEntity<ResultWrapper<UserTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_user_type")
	public ResponseEntity<ResultWrapper<List<UserTypeMaster>>> ViewUserType(@Valid @RequestBody UserTypeMasterCRUDDto userTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<UserTypeMaster>> result = null;	
		result = masterService.ViewUserType(userTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<UserTypeMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_user_type")
	public ResponseEntity<ResultWrapper<UserTypeMaster>> DeleteUserType(@Valid @RequestBody UserTypeMasterCRUDDto userTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserTypeMaster> result = null;	
		result = masterService.DeleteUserType(userTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<UserTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_user_type")
	public ResponseEntity<ResultWrapper<UserTypeMaster>> UpdateUserType(@Valid @RequestBody UserTypeMaster userTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserTypeMaster> result = null;	
		result = masterService.UpdateUserType(userTypeMaster);
		return new ResponseEntity<ResultWrapper<UserTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_user")
	public ResponseEntity<ResultWrapper<UserMaster>> AddUser(@Valid @RequestBody UserMaster userMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserMaster> result = null;	
		result = masterService.AddUser(userMaster);
		return new ResponseEntity<ResultWrapper<UserMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_user")
	public ResponseEntity<ResultWrapper<List<UserMasterViewDto>>> ViewUser(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<UserMasterViewDto>> result = null;	
		result = masterService.ViewUser(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<UserMasterViewDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_user")
	public ResponseEntity<ResultWrapper<UserMaster>> DeleteUser(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserMaster> result = null;	
		result = masterService.DeleteUser(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<UserMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_user")
	public ResponseEntity<ResultWrapper<UserMaster>> UpdateUser(@Valid @RequestBody UserMaster userMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<UserMaster> result = null;	
		result = masterService.UpdateUser(userMaster);
		return new ResponseEntity<ResultWrapper<UserMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_user_feature")
	public ResponseEntity<ResultWrapper<String>> UpdateUserFeature(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.UpdateUserFeature(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/reset_user_token")
	public ResponseEntity<ResultWrapper<String>> ResetUserToken(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.ResetUserToken(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_company")
	public ResponseEntity<ResultWrapper<CompanyMaster>> AddCompany(@Valid @RequestBody CompanyMaster companyMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CompanyMaster> result = null;	
		result = masterService.AddCompany(companyMaster);
		return new ResponseEntity<ResultWrapper<CompanyMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_company")
	public ResponseEntity<ResultWrapper<List<CompanyMaster>>> ViewCompany(@Valid @RequestBody CompanyMasterCRUDDto companyMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CompanyMaster>> result = null;	
		result = masterService.ViewCompany(companyMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CompanyMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_company")
	public ResponseEntity<ResultWrapper<CompanyMaster>> DeleteCompany(@Valid @RequestBody CompanyMasterCRUDDto companyMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CompanyMaster> result = null;	
		result = masterService.DeleteCompany(companyMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<CompanyMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_company")
	public ResponseEntity<ResultWrapper<CompanyMaster>> UpdateCompany(@Valid @RequestBody CompanyMaster companyMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CompanyMaster> result = null;	
		result = masterService.UpdateCompany(companyMaster);
		return new ResponseEntity<ResultWrapper<CompanyMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_language")
	public ResponseEntity<ResultWrapper<LanguageMaster>> AddLanguage(@Valid @RequestBody LanguageMaster languageMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<LanguageMaster> result = null;	
		result = masterService.AddLanguage(languageMaster);
		return new ResponseEntity<ResultWrapper<LanguageMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_language")
	public ResponseEntity<ResultWrapper<List<LanguageMaster>>> ViewLanguage(@Valid @RequestBody LanguageMasterCRUDDto languageMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<LanguageMaster>> result = null;	
		result = masterService.ViewLanguage(languageMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<LanguageMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_language")
	public ResponseEntity<ResultWrapper<LanguageMaster>> DeleteLanguage(@Valid @RequestBody LanguageMasterCRUDDto languageMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<LanguageMaster> result = null;	
		result = masterService.DeleteLanguage(languageMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<LanguageMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_language")
	public ResponseEntity<ResultWrapper<LanguageMaster>> UpdateLanguage(@Valid @RequestBody LanguageMaster languageMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<LanguageMaster> result = null;	
		result = masterService.UpdateLanguage(languageMaster);
		return new ResponseEntity<ResultWrapper<LanguageMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_cycle_usa")
	public ResponseEntity<ResultWrapper<CycleUsa>> AddCycleUsa(@Valid @RequestBody CycleUsa cycleUsa)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CycleUsa> result = null;	
		result = masterService.AddCycleUsa(cycleUsa);
		return new ResponseEntity<ResultWrapper<CycleUsa>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_cycle_usa")
	public ResponseEntity<ResultWrapper<List<CycleUsa>>> ViewCycleUsa(@Valid @RequestBody CycleUsaCRUDDto cycleUsaCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CycleUsa>> result = null;	
		result = masterService.ViewCycleUsa(cycleUsaCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CycleUsa>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_cycle_usa")
	public ResponseEntity<ResultWrapper<CycleUsa>> DeleteCycleUsa(@Valid @RequestBody CycleUsaCRUDDto cycleUsaCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CycleUsa> result = null;	
		result = masterService.DeleteCycleUsa(cycleUsaCRUDDto);
		return new ResponseEntity<ResultWrapper<CycleUsa>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_cycle_usa")
	public ResponseEntity<ResultWrapper<CycleUsa>> UpdateCycleUsa(@Valid @RequestBody CycleUsa cycleUsa)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CycleUsa> result = null;	
		result = masterService.UpdateCycleUsa(cycleUsa);
		return new ResponseEntity<ResultWrapper<CycleUsa>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_cycle_canada")
	public ResponseEntity<ResultWrapper<CycleCanada>> AddCycleCanada(@Valid @RequestBody CycleCanada cycleCanada)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CycleCanada> result = null;	
		result = masterService.AddCycleCanada(cycleCanada);
		return new ResponseEntity<ResultWrapper<CycleCanada>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_cycle_canada")
	public ResponseEntity<ResultWrapper<List<CycleCanada>>> ViewCycleCanada(@Valid @RequestBody CycleCanadaCRUDDto cycleCanadaCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CycleCanada>> result = null;	
		result = masterService.ViewCycleCanada(cycleCanadaCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CycleCanada>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_cycle_canada")
	public ResponseEntity<ResultWrapper<CycleCanada>> DeleteCycleCanada(@Valid @RequestBody CycleCanadaCRUDDto cycleCanadaCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CycleCanada> result = null;	
		result = masterService.DeleteCycleCanada(cycleCanadaCRUDDto);
		return new ResponseEntity<ResultWrapper<CycleCanada>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_cycle_canada")
	public ResponseEntity<ResultWrapper<CycleCanada>> UpdateCycleCanada(@Valid @RequestBody CycleCanada cycleCanada)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CycleCanada> result = null;	
		result = masterService.UpdateCycleCanada(cycleCanada);
		return new ResponseEntity<ResultWrapper<CycleCanada>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_device_modal")
	public ResponseEntity<ResultWrapper<DeviceModalMaster>> AddDeviceModal(@Valid @RequestBody DeviceModalMaster deviceModalMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DeviceModalMaster> result = null;	
		result = masterService.AddDeviceModal(deviceModalMaster);
		return new ResponseEntity<ResultWrapper<DeviceModalMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_device_modal")
	public ResponseEntity<ResultWrapper<List<DeviceModalMaster>>> ViewDeviceModal(@Valid @RequestBody DeviceModalMasterCRUDDto deviceModalMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<DeviceModalMaster>> result = null;	
		result = masterService.ViewDeviceModal(deviceModalMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<DeviceModalMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_device_modal")
	public ResponseEntity<ResultWrapper<DeviceModalMaster>> DeleteDeviceModal(@Valid @RequestBody DeviceModalMasterCRUDDto deviceModalMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DeviceModalMaster> result = null;	
		result = masterService.DeleteDeviceModal(deviceModalMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<DeviceModalMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_device_modal")
	public ResponseEntity<ResultWrapper<DeviceModalMaster>> UpdateDeviceModal(@Valid @RequestBody DeviceModalMaster deviceModalMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DeviceModalMaster> result = null;	
		result = masterService.UpdateDeviceModal(deviceModalMaster);
		return new ResponseEntity<ResultWrapper<DeviceModalMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_vehicle_type")
	public ResponseEntity<ResultWrapper<VehicleTypeMaster>> AddVehicleType(@Valid @RequestBody VehicleTypeMaster vehicleTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleTypeMaster> result = null;	
		result = masterService.AddVehicleType(vehicleTypeMaster);
		return new ResponseEntity<ResultWrapper<VehicleTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_vehicle_type")
	public ResponseEntity<ResultWrapper<List<VehicleTypeMaster>>> ViewVehicleType(@Valid @RequestBody VehicleTypeMasterCRUDDto vehicleTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<VehicleTypeMaster>> result = null;	
		result = masterService.ViewVehicleType(vehicleTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<VehicleTypeMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_vehicle_type")
	public ResponseEntity<ResultWrapper<VehicleTypeMaster>> DeleteVehicleType(@Valid @RequestBody VehicleTypeMasterCRUDDto vehicleTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleTypeMaster> result = null;	
		result = masterService.DeleteVehicleType(vehicleTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<VehicleTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_vehicle_type")
	public ResponseEntity<ResultWrapper<VehicleTypeMaster>> UpdateVehicleType(@Valid @RequestBody VehicleTypeMaster vehicleTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleTypeMaster> result = null;	
		result = masterService.UpdateVehicleType(vehicleTypeMaster);
		return new ResponseEntity<ResultWrapper<VehicleTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_payment_status")
	public ResponseEntity<ResultWrapper<PaymentStatusMaster>> AddPaymentStatus(@Valid @RequestBody PaymentStatusMaster paymentStatusMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<PaymentStatusMaster> result = null;	
		result = masterService.AddPaymentStatus(paymentStatusMaster);
		return new ResponseEntity<ResultWrapper<PaymentStatusMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_payment_status")
	public ResponseEntity<ResultWrapper<List<PaymentStatusMaster>>> ViewPaymentStatus(@Valid @RequestBody PaymentStatusMasterCRUDDto paymentStatusMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<PaymentStatusMaster>> result = null;	
		result = masterService.ViewPaymentStatus(paymentStatusMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<PaymentStatusMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_payment_status")
	public ResponseEntity<ResultWrapper<PaymentStatusMaster>> DeletePaymentStatus(@Valid @RequestBody PaymentStatusMasterCRUDDto paymentStatusMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<PaymentStatusMaster> result = null;	
		result = masterService.DeletePaymentStatus(paymentStatusMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<PaymentStatusMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_payment_status")
	public ResponseEntity<ResultWrapper<PaymentStatusMaster>> UpdatePaymentStatus(@Valid @RequestBody PaymentStatusMaster paymentStatusMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<PaymentStatusMaster> result = null;	
		result = masterService.UpdatePaymentStatus(paymentStatusMaster);
		return new ResponseEntity<ResultWrapper<PaymentStatusMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_exception")
	public ResponseEntity<ResultWrapper<ExceptionMaster>> AddExceptionMaster(@Valid @RequestBody ExceptionMaster exceptionMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ExceptionMaster> result = null;	
		result = masterService.AddExceptionMaster(exceptionMaster);
		return new ResponseEntity<ResultWrapper<ExceptionMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_exception")
	public ResponseEntity<ResultWrapper<List<ExceptionMaster>>> ViewExceptionMaster(@Valid @RequestBody ExceptionMasterCRUDDto exceptionMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ExceptionMaster>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)exceptionMasterCRUDDto.getDriverId(),exceptionMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewExceptionMaster(exceptionMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<ExceptionMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_exception")
	public ResponseEntity<ResultWrapper<ExceptionMaster>> DeleteExceptionMaster(@Valid @RequestBody ExceptionMasterCRUDDto exceptionMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ExceptionMaster> result = null;	
		result = masterService.DeleteExceptionMaster(exceptionMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<ExceptionMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_exception")
	public ResponseEntity<ResultWrapper<ExceptionMaster>> UpdateExceptionMaster(@Valid @RequestBody ExceptionMaster exceptionMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ExceptionMaster> result = null;	
		result = masterService.UpdateExceptionMaster(exceptionMaster);
		return new ResponseEntity<ResultWrapper<ExceptionMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_main_terminal")
	public ResponseEntity<ResultWrapper<MainTerminalMaster>> AddMainTerminalMaster(@Valid @RequestBody MainTerminalMaster mainTerminalMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<MainTerminalMaster> result = null;	
		result = masterService.AddMainTerminalMaster(mainTerminalMaster);
		return new ResponseEntity<ResultWrapper<MainTerminalMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_main_terminal")
	public ResponseEntity<ResultWrapper<List<MainTerminalMaster>>> ViewMainTerminalMaster(@Valid @RequestBody MainTerminalMasterCRUDDto mainTerminalMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<MainTerminalMaster>> result = null;	
		result = masterService.ViewMainTerminalMaster(mainTerminalMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<MainTerminalMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_all_main_terminal")
	public ResponseEntity<ResultWrapper<List<MainTerminalMaster>>> ViewAllMainTerminalMaster(@Valid @RequestBody MainTerminalMasterCRUDDto mainTerminalMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<MainTerminalMaster>> result = null;	
		result = masterService.ViewAllMainTerminalMaster(mainTerminalMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<MainTerminalMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_main_terminal")
	public ResponseEntity<ResultWrapper<MainTerminalMaster>> DeleteMainTerminalMaster(@Valid @RequestBody MainTerminalMasterCRUDDto mainTerminalMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<MainTerminalMaster> result = null;	
		result = masterService.DeleteMainTerminalMaster(mainTerminalMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<MainTerminalMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_main_terminal")
	public ResponseEntity<ResultWrapper<MainTerminalMaster>> UpdateMainTerminalMaster(@Valid @RequestBody MainTerminalMaster mainTerminalMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<MainTerminalMaster> result = null;	
		result = masterService.UpdateMainTerminalMaster(mainTerminalMaster);
		return new ResponseEntity<ResultWrapper<MainTerminalMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_cargo_type")
	public ResponseEntity<ResultWrapper<CargoTypeMaster>> AddCargoTypeMaster(@Valid @RequestBody CargoTypeMaster cargoTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CargoTypeMaster> result = null;	
		result = masterService.AddCargoTypeMaster(cargoTypeMaster);
		return new ResponseEntity<ResultWrapper<CargoTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_cargo_type")
	public ResponseEntity<ResultWrapper<List<CargoTypeMaster>>> ViewCargoTypeMaster(@Valid @RequestBody CargoTypeMasterCRUDDto cargoTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<CargoTypeMaster>> result = null;	
		result = masterService.ViewCargoTypeMaster(cargoTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<CargoTypeMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_cargo_type")
	public ResponseEntity<ResultWrapper<CargoTypeMaster>> DeleteCargoTypeMaster(@Valid @RequestBody CargoTypeMasterCRUDDto cargoTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CargoTypeMaster> result = null;	
		result = masterService.DeleteCargoTypeMaster(cargoTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<CargoTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_cargo_type")
	public ResponseEntity<ResultWrapper<CargoTypeMaster>> UpdateCargoTypeMaster(@Valid @RequestBody CargoTypeMaster cargoTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<CargoTypeMaster> result = null;	
		result = masterService.UpdateCargoTypeMaster(cargoTypeMaster);
		return new ResponseEntity<ResultWrapper<CargoTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_fuel_type")
	public ResponseEntity<ResultWrapper<FuelTypeMaster>> AddFuelTypeMaster(@Valid @RequestBody FuelTypeMaster fuelTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<FuelTypeMaster> result = null;	
		result = masterService.AddFuelTypeMaster(fuelTypeMaster);
		return new ResponseEntity<ResultWrapper<FuelTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_fuel_type")
	public ResponseEntity<ResultWrapper<List<FuelTypeMaster>>> ViewFuelTypeMaster(@Valid @RequestBody FuelTypeMasterCRUDDto fuelTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<FuelTypeMaster>> result = null;	
		result = masterService.ViewFuelTypeMaster(fuelTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<FuelTypeMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_fuel_type")
	public ResponseEntity<ResultWrapper<FuelTypeMaster>> DeleteFuelTypeMaster(@Valid @RequestBody FuelTypeMasterCRUDDto fuelTypeMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<FuelTypeMaster> result = null;	
		result = masterService.DeleteFuelTypeMaster(fuelTypeMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<FuelTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_fuel_type")
	public ResponseEntity<ResultWrapper<FuelTypeMaster>> UpdateFuelTypeMaster(@Valid @RequestBody FuelTypeMaster fuelTypeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<FuelTypeMaster> result = null;	
		result = masterService.UpdateFuelTypeMaster(fuelTypeMaster);
		return new ResponseEntity<ResultWrapper<FuelTypeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_refer_mode")
	public ResponseEntity<ResultWrapper<ReferModeMaster>> AddReferModeMaster(@Valid @RequestBody ReferModeMaster referModeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ReferModeMaster> result = null;	
		result = masterService.AddReferModeMaster(referModeMaster);
		return new ResponseEntity<ResultWrapper<ReferModeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_refer_mode")
	public ResponseEntity<ResultWrapper<List<ReferModeMaster>>> ViewReferModeMaster(@Valid @RequestBody ReferModeCRUDDto referModeCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ReferModeMaster>> result = null;	
		result = masterService.ViewReferModeMaster(referModeCRUDDto);
		return new ResponseEntity<ResultWrapper<List<ReferModeMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_refer_mode")
	public ResponseEntity<ResultWrapper<ReferModeMaster>> DeleteReferModeMaster(@Valid @RequestBody ReferModeCRUDDto referModeCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ReferModeMaster> result = null;	
		result = masterService.DeleteReferModeMaster(referModeCRUDDto);
		return new ResponseEntity<ResultWrapper<ReferModeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_refer_mode")
	public ResponseEntity<ResultWrapper<ReferModeMaster>> UpdateReferModeMaster(@Valid @RequestBody ReferModeMaster referModeMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ReferModeMaster> result = null;	
		result = masterService.UpdateReferModeMaster(referModeMaster);
		return new ResponseEntity<ResultWrapper<ReferModeMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_trailer")
	public ResponseEntity<ResultWrapper<TrailerMaster>> AddTrailerMaster(@Valid @RequestBody TrailerMaster trailerMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<TrailerMaster> result = null;	
		result = masterService.AddTrailerMaster(trailerMaster);
		return new ResponseEntity<ResultWrapper<TrailerMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_trailer")
	public ResponseEntity<ResultWrapper<List<TrailerMaster>>> ViewTrailerMaster(@Valid @RequestBody TrailerMasterCRUDDto trailerMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<TrailerMaster>> result = null;	
		result = masterService.ViewTrailerMaster(trailerMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<TrailerMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_trailer")
	public ResponseEntity<ResultWrapper<TrailerMaster>> DeleteTrailerMaster(@Valid @RequestBody TrailerMasterCRUDDto trailerMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<TrailerMaster> result = null;	
		result = masterService.DeleteTrailerMaster(trailerMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<TrailerMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_trailer")
	public ResponseEntity<ResultWrapper<TrailerMaster>> UpdateTrailerMaster(@Valid @RequestBody TrailerMaster trailerMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<TrailerMaster> result = null;	
		result = masterService.UpdateTrailerMaster(trailerMaster);
		return new ResponseEntity<ResultWrapper<TrailerMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_simulator")
	public ResponseEntity<ResultWrapper<Simulator>> AddSimulator(@Valid @RequestBody Simulator simulator)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<Simulator> result = null;	
		result = masterService.AddSimulator(simulator);
		return new ResponseEntity<ResultWrapper<Simulator>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_simulator")
	public ResponseEntity<ResultWrapper<List<SimulatorCRUDDto>>> ViewSimulator(@Valid @RequestBody SimulatorCRUDDto simulatorCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<SimulatorCRUDDto>> result = null;	
		result = masterService.ViewSimulator(simulatorCRUDDto);
		return new ResponseEntity<ResultWrapper<List<SimulatorCRUDDto>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_simulator")
	public ResponseEntity<ResultWrapper<Simulator>> DeleteSimulator(@Valid @RequestBody SimulatorCRUDDto simulatorCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<Simulator> result = null;	
		result = masterService.DeleteSimulator(simulatorCRUDDto);
		return new ResponseEntity<ResultWrapper<Simulator>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_defect")
	public ResponseEntity<ResultWrapper<DefectMaster>> AddDefect(@Valid @RequestBody DefectMaster defectMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DefectMaster> result = null;	
		result = masterService.AddDefect(defectMaster);
		return new ResponseEntity<ResultWrapper<DefectMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_defect")
	public ResponseEntity<ResultWrapper<List<DefectMaster>>> ViewDefect(@Valid @RequestBody DefectMasterCRUDDto defectMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<DefectMaster>> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)defectMasterCRUDDto.getDriverId(),defectMasterCRUDDto.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.ViewDefect(defectMasterCRUDDto,tokenValid);
		return new ResponseEntity<ResultWrapper<List<DefectMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_defect")
	public ResponseEntity<ResultWrapper<DefectMaster>> DeleteDefect(@Valid @RequestBody DefectMasterCRUDDto defectMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DefectMaster> result = null;	
		result = masterService.DeleteDefect(defectMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<DefectMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_defect")
	public ResponseEntity<ResultWrapper<DefectMaster>> UpdateDefect(@Valid @RequestBody DefectMaster defectMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<DefectMaster> result = null;	
		result = masterService.UpdateDefect(defectMaster);
		return new ResponseEntity<ResultWrapper<DefectMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_restart")
	public ResponseEntity<ResultWrapper<RestartMaster>> AddRestart(@Valid @RequestBody RestartMaster restartMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RestartMaster> result = null;	
		result = masterService.AddRestart(restartMaster);
		return new ResponseEntity<ResultWrapper<RestartMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_restart")
	public ResponseEntity<ResultWrapper<List<RestartMaster>>> ViewRestart(@Valid @RequestBody RestartMasterCRUDDto restartMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<RestartMaster>> result = null;	
		result = masterService.ViewRestart(restartMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<RestartMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_restart")
	public ResponseEntity<ResultWrapper<RestartMaster>> DeleteRestart(@Valid @RequestBody RestartMasterCRUDDto restartMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RestartMaster> result = null;	
		result = masterService.DeleteRestart(restartMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<RestartMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_restart")
	public ResponseEntity<ResultWrapper<RestartMaster>> UpdateRestart(@Valid @RequestBody RestartMaster restartMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RestartMaster> result = null;	
		result = masterService.UpdateRestart(restartMaster);
		return new ResponseEntity<ResultWrapper<RestartMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_rest_break")
	public ResponseEntity<ResultWrapper<RestBreakMaster>> AddRestBreak(@Valid @RequestBody RestBreakMaster restBreakMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RestBreakMaster> result = null;	
		result = masterService.AddRestBreak(restBreakMaster);
		return new ResponseEntity<ResultWrapper<RestBreakMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_rest_break")
	public ResponseEntity<ResultWrapper<List<RestBreakMaster>>> ViewRestBreak(@Valid @RequestBody RestBreakMasterCRUDDto restBreakMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<RestBreakMaster>> result = null;	
		result = masterService.ViewRestBreak(restBreakMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<RestBreakMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_rest_break")
	public ResponseEntity<ResultWrapper<RestBreakMaster>> DeleteRestBreak(@Valid @RequestBody RestBreakMasterCRUDDto restBreakMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RestBreakMaster> result = null;	
		result = masterService.DeleteRestBreak(restBreakMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<RestBreakMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_rest_break")
	public ResponseEntity<ResultWrapper<RestBreakMaster>> UpdateRestBreak(@Valid @RequestBody RestBreakMaster restBreakMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<RestBreakMaster> result = null;	
		result = masterService.UpdateRestBreak(restBreakMaster);
		return new ResponseEntity<ResultWrapper<RestBreakMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_timezone")
	public ResponseEntity<ResultWrapper<TimezoneMaster>> AddTimezone(@Valid @RequestBody TimezoneMaster timezoneMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<TimezoneMaster> result = null;	
		result = masterService.AddTimezone(timezoneMaster);
		return new ResponseEntity<ResultWrapper<TimezoneMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_timezone")
	public ResponseEntity<ResultWrapper<List<TimezoneMaster>>> ViewTimezone(@Valid @RequestBody TimezoneMasterCRUDDto timezoneMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<TimezoneMaster>> result = null;	
		result = masterService.ViewTimezone(timezoneMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<TimezoneMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_timezone")
	public ResponseEntity<ResultWrapper<TimezoneMaster>> DeleteTimezone(@Valid @RequestBody TimezoneMasterCRUDDto timezoneMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<TimezoneMaster> result = null;	
		result = masterService.DeleteTimezone(timezoneMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<TimezoneMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_timezone")
	public ResponseEntity<ResultWrapper<TimezoneMaster>> UpdateTimezone(@Valid @RequestBody TimezoneMaster timezoneMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<TimezoneMaster> result = null;	
		result = masterService.UpdateTimezone(timezoneMaster);
		return new ResponseEntity<ResultWrapper<TimezoneMaster>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_eld_connection_interface")
	public ResponseEntity<ResultWrapper<EldConnectionInterface>> AddEldConnectionInterface(@Valid @RequestBody EldConnectionInterface eldConnectionInterface)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<EldConnectionInterface> result = null;	
		result = masterService.AddEldConnectionInterface(eldConnectionInterface);
		return new ResponseEntity<ResultWrapper<EldConnectionInterface>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_eld_connection_interface")
	public ResponseEntity<ResultWrapper<List<EldConnectionInterface>>> ViewEldConnectionInterface(@Valid @RequestBody EldConnectionInterfaceCRUDDto eldConnectionInterfaceCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<EldConnectionInterface>> result = null;	
		result = masterService.ViewEldConnectionInterface(eldConnectionInterfaceCRUDDto);
		return new ResponseEntity<ResultWrapper<List<EldConnectionInterface>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_mac_address")
	public ResponseEntity<ResultWrapper<String>> AddMacAddress(@Valid @RequestBody MACAddressMaster macAddressMaster)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)macAddressMaster.getDriverId(),macAddressMaster.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.AddMacAddress(macAddressMaster,tokenValid);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_device_status")
	public ResponseEntity<ResultWrapper<String>> AddDeviceStatus(@Valid @RequestBody DeviceStatus deviceStatus)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		String tokenValid="";
		try {
			tokenValid = imobilityUtils.CheckTokenNo((int)deviceStatus.getDriverId(),deviceStatus.getTokenNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		result = masterService.AddDeviceStatus(deviceStatus,tokenValid);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_vehicle_condition")
	public ResponseEntity<ResultWrapper<VehicleCondition>> AddVehicleCondition(@Valid @RequestBody VehicleCondition vehicleCondition)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleCondition> result = null;	
		result = masterService.AddVehicleCondition(vehicleCondition);
		return new ResponseEntity<ResultWrapper<VehicleCondition>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_vehicle_condition")
	public ResponseEntity<ResultWrapper<List<VehicleCondition>>> ViewVehicleCondition(@Valid @RequestBody VehicleConditionCRUDDto vehicleConditionCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<VehicleCondition>> result = null;	
		result = masterService.ViewVehicleCondition(vehicleConditionCRUDDto);
		return new ResponseEntity<ResultWrapper<List<VehicleCondition>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_vehicle_condition")
	public ResponseEntity<ResultWrapper<VehicleCondition>> DeleteVehicleCondition(@Valid @RequestBody VehicleConditionCRUDDto vehicleConditionCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleCondition> result = null;	
		result = masterService.DeleteVehicleCondition(vehicleConditionCRUDDto);
		return new ResponseEntity<ResultWrapper<VehicleCondition>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_vehicle_condition")
	public ResponseEntity<ResultWrapper<VehicleCondition>> UpdateTimezone(@Valid @RequestBody VehicleCondition vehicleCondition)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<VehicleCondition> result = null;	
		result = masterService.UpdateVehicleCondition(vehicleCondition);
		return new ResponseEntity<ResultWrapper<VehicleCondition>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_geofance_master")
	public ResponseEntity<ResultWrapper<List<GeofanceMaster>>> ViewGeofanceMaster(@Valid @RequestBody GeofanceMasterCRUDDto geofanceMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<GeofanceMaster>> result = null;	
		result = masterService.ViewGeofanceMaster(geofanceMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<List<GeofanceMaster>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_eld_settings")
	public ResponseEntity<ResultWrapper<ELDSettings>> AddEldSettings(@Valid @RequestBody ELDSettings eldSettings)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ELDSettings> result = null;	
		result = masterService.AddEldSettings(eldSettings);
		return new ResponseEntity<ResultWrapper<ELDSettings>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_eld_settings")
	public ResponseEntity<ResultWrapper<List<ELDSettings>>> ViewEldSettings(@Valid @RequestBody ELDSettings eldSettings)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<ELDSettings>> result = null;	
		result = masterService.ViewEldSettings(eldSettings);
		return new ResponseEntity<ResultWrapper<List<ELDSettings>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/delete_eld_settings")
	public ResponseEntity<ResultWrapper<ELDSettings>> DeleteEldSettings(@Valid @RequestBody ELDSettings eldSettings)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ELDSettings> result = null;	
		result = masterService.DeleteEldSettings(eldSettings);
		return new ResponseEntity<ResultWrapper<ELDSettings>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_eld_settings")
	public ResponseEntity<ResultWrapper<ELDSettings>> UpdateEldSettings(@Valid @RequestBody ELDSettings eldSettings)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ELDSettings> result = null;	
		result = masterService.UpdateEldSettings(eldSettings);
		return new ResponseEntity<ResultWrapper<ELDSettings>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/add_disclaimer")
	public ResponseEntity<ResultWrapper<Disclaimer>> AddDisclaimer(@Valid @RequestBody Disclaimer disclaimer)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<Disclaimer> result = null;	
		result = masterService.AddDisclaimer(disclaimer);
		return new ResponseEntity<ResultWrapper<Disclaimer>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_disclaimer")
	public ResponseEntity<ResultWrapper<List<Disclaimer>>> ViewDisclaimer()
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<List<Disclaimer>> result = null;	
		result = masterService.ViewDisclaimer();
		return new ResponseEntity<ResultWrapper<List<Disclaimer>>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_disclaimer_in_user")
	public ResponseEntity<ResultWrapper<String>> UpdateDisclaimerInUser(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.UpdateDisclaimerInUser(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/update_disclaimer_in_driver")
	public ResponseEntity<ResultWrapper<String>> UpdateDisclaimerInDriver(@Valid @RequestBody UserMasterCRUDDto userMasterCRUDDto)
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<String> result = null;	
		result = masterService.UpdateDisclaimerInDriver(userMasterCRUDDto);
		return new ResponseEntity<ResultWrapper<String>>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/view_project_detail_analytics")
	public ResponseEntity<ResultWrapper<ProjectDetailAnalyticsViewDto>> ViewProjectDetailAnalytics()
		throws UnsupportedEncodingException , JsonProcessingException {
		ResultWrapper<ProjectDetailAnalyticsViewDto> result = null;	
		result = masterService.ViewProjectDetailAnalytics();
		return new ResponseEntity<ResultWrapper<ProjectDetailAnalyticsViewDto>>(result, HttpStatus.OK);
	}
	
}
