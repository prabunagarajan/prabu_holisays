package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.AssetMapRequestDto;
import com.oasys.helpdesk.dto.AssetReportRequestDTO;
import com.oasys.helpdesk.service.AssetMapAndDeviceReportService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Category")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/assetMapAndDevice")
public class AssetMapAndDeviceReportController {
	
	@Autowired
	AssetMapAndDeviceReportService assetMapAndDeviceReportService;
	
	
	@PostMapping(value = "/report")
	@ApiOperation(value = "This api is to get report", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> report(@Valid @RequestBody AssetReportRequestDTO requestDTO) {
		GenericResponse objGenericResponse = assetMapAndDeviceReportService.report(requestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
