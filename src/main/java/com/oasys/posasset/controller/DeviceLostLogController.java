package com.oasys.posasset.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.service.DeviceLostLogService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "PosAssetData", description = "This controller contain all operations for Department Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/devicelostlog")
public class DeviceLostLogController {
	
	
	@Autowired
	private DeviceLostLogService devicelostlogservice;
	
	
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all devicelost list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(devicelostlogservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view devicelost by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(devicelostlogservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("getByApplicationNo/{applicationNo}")
	@ApiOperation(value = "This api is used to get Payment And Reconciliation Comments by Application Number", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByApplicationNo(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(devicelostlogservice.getByApplicationNo(applicationNo), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	

	
}
