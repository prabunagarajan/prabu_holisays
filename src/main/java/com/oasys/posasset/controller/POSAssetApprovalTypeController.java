package com.oasys.posasset.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.PosAssetApprovalTypeDTO;
import com.oasys.posasset.service.POSAssetApprovalTypeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "POSAssetApprovalType", description = "This controller contains api to get and update operation for POS Asset Approval Type")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/pos-asset-approval-type")
public class POSAssetApprovalTypeController {
	
	@Autowired
	private POSAssetApprovalTypeService posAssetApprovalTypeService;
	
	@PutMapping
    @ApiOperation(value = "This api is used to update pos-asset approval type", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody PosAssetApprovalTypeDTO requestDTO) throws RecordNotFoundException, ParseException , Exception {
		return new ResponseEntity<>(posAssetApprovalTypeService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping
    @ApiOperation(value = "This api is used to get approval-type listing", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() throws JsonParseException,ParseException {
		return new ResponseEntity<>(posAssetApprovalTypeService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
