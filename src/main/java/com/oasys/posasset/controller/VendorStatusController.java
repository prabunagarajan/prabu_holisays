package com.oasys.posasset.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.VendorStatusRequestDTO;
import com.oasys.posasset.service.VendorStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Vendor Status")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/vendorstatus")
public class VendorStatusController {

	@Autowired
	VendorStatusService vendorStatusService;
	

	@PostMapping("/add")
    @ApiOperation(value = "This api is used to add Vendor Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addVendorStatus(@Valid @RequestBody VendorStatusRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(vendorStatusService.add(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PutMapping("/update")
    @ApiOperation(value = "This api is used to Update Vendor Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateVendorStatus(@Valid @RequestBody VendorStatusRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(vendorStatusService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getall")
	@ApiOperation(value = "This api is to get all Vendor Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(vendorStatusService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
