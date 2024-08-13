package com.oasys.posasset.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.controller.BaseController;
import com.oasys.helpdesk.dto.RoleMasterResponseDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.DeviceMasterDTO;
import com.oasys.posasset.service.DeviceMasterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Device master", description = "This controller contain api to get all Device master")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/device-master")
public class DeviceMasterController extends BaseController {

	@Autowired
	private DeviceMasterService devicemasterservice;

	@PostMapping("/addDevicemaster")
	@ApiOperation(value = "This API is used to add device numbers to the device master table", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addDeviceMasterDeviceNumber(@RequestBody DeviceMasterDTO deviceMasterDTO) {
		return new ResponseEntity<>(devicemasterservice.addDeviceMasterDeviceNumber(deviceMasterDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/updateDevicemaster")
	@ApiOperation(value = "This api is to Update device numbers to the device master table", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDeviceEntity(@Valid @RequestBody DeviceMasterDTO deviceMasterDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(devicemasterservice.updateDeviceNumber(deviceMasterDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
