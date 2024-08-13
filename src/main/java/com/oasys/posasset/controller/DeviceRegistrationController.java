package com.oasys.posasset.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import com.oasys.posasset.dto.DeviceRegistrationRequestDTO;
import com.oasys.posasset.dto.DeviceRegistrationUpdateRequestDTO;
import com.oasys.posasset.dto.DeviceregDTO;
import com.oasys.posasset.service.impl.DeviceRegistrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "DeviceRegistration", description = "This controller contain all  operation of Device Registration")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/device-registration")
public class DeviceRegistrationController {
	
	@Autowired
	private DeviceRegistrationService deviceRegistrationService;
	
	@PostMapping
    @ApiOperation(value = "This api is used to register a device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> register(@Valid @RequestBody DeviceRegistrationRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceRegistrationService.save(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PutMapping
    @ApiOperation(value = "This api is used to register a device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody DeviceRegistrationUpdateRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceRegistrationService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/de-associate")
    @ApiOperation(value = "This api is used to register a device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> deassociate(@RequestBody DeviceRegistrationRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceRegistrationService.deAssociate(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search device registeration record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(deviceRegistrationService.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get device registeration details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("id") Long id) throws Exception {
		return new ResponseEntity<>(deviceRegistrationService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/upload")
	@ApiOperation(value = "This api is used to register device using bulk upload", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> upload(@RequestParam(value = "file", required = true) MultipartFile file) {
		return new ResponseEntity<>(deviceRegistrationService.upload(file), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PutMapping("/mapping")
    @ApiOperation(value = "This api is used update DeviceDetail and Mapping", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDeviceDetailMapping(@RequestParam(value="deviceNumber", required=true) String deviceNumber, @RequestParam(value="fpsCode", required=true) String fpsCode)  {
		return new ResponseEntity<>(deviceRegistrationService.updateDeviceDetailMapping(deviceNumber, fpsCode), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.POS_ASSET_REPORT)
	@PostMapping("/report")
	@ApiOperation(value = "This api is used to get device registeration report", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getReport(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(deviceRegistrationService.getReport(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@PostMapping("/manualdevicemapping")
    @ApiOperation(value = "This api is used to register a device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> manualMapping(@Valid @RequestBody DeviceregDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceRegistrationService.Manualdevicemapping(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deviceregistrationcount", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get Device Registration Count data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getDeviceRegistrationCount() {
		GenericResponse objGenericResponse = deviceRegistrationService.getDeviceRegistrationCount();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/filterstatussearch")
	@ApiOperation(value = "This api is used to search device registeration record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchPassReport(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(deviceRegistrationService.getAllByPassFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

	@RequestMapping(value = "/entitysummarycount", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get Count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount() {
		GenericResponse objGenericResponse = deviceRegistrationService.getCount();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
