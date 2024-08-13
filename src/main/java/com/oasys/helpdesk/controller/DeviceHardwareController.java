package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.DeviceHardwareAddRequestDTO;
import com.oasys.helpdesk.dto.DeviceHardwareNameRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.DeviceHardwareService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for device/hardware name")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/DeviceHardwareName")
public class DeviceHardwareController {

	@Autowired
	private DeviceHardwareService deviceService;


	@PostMapping("/addasset")
	@ApiOperation(value = "This api is used to add DeviceHardwareName and devicelist record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addDevice(@Valid @RequestBody DeviceHardwareNameRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceService.addDevice(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is used to update DeviceHardwareName and devicelist record record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody DeviceHardwareNameRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceService.updateDevice(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to get DeviceHardwareNameLists record based on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException  {
		return new ResponseEntity<>(deviceService.getAllByRequestFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getByid/{id}")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(deviceService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	
	
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all only DeviceHardwareName records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(deviceService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/uniquecode")
	@ApiOperation(value = "This api is used to get Mane unique code for DeviceHardwareName and DeviceHardwareList", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(deviceService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active DeviceHardwareName records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(deviceService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/{deviceCode}")
	@ApiOperation(value = "This api is to get DeviceHardwareName and List by deviceCode", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByDeviceCode(@PathVariable("deviceCode") String deviceCode) {
		return new ResponseEntity<>(deviceService.getByDeviceCode(deviceCode), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getAllList")
	@ApiOperation(value = "This api is to get all DeviceHardwareList records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllList() {
		return new ResponseEntity<>(deviceService.getAllList(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to get Mane uniqueDeviceID for List", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCodeList(@Valid @RequestBody DeviceHardwareAddRequestDTO payload)  throws JsonParseException,ParseException
	{
		return new ResponseEntity<>(deviceService.getDeviceId(payload), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/activeList")
	@ApiOperation(value = "This api is to get all active DeviceHardwareList records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActiveList() {
		return new ResponseEntity<>(deviceService.getAllActiveList(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	
	
	
}














