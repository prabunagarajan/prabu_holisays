package com.oasys.helpdesk.controller;



import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ShiftConfigRequestDTO;
import com.oasys.helpdesk.service.ShiftConfigService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Shift Configuration")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/shift-config")
public class ShiftConfigConroller {

	@Autowired
	private ShiftConfigService shiftConfigService;


	@PreAuthorize(PermissionConstant.SHIFT_CONFIGURATION)
	@PostMapping("/addShiftConfiguration") 
	@ApiOperation(value = "This api is used to add Shift Configuration data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addShiftConfiguration(@Valid @RequestBody ShiftConfigRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(shiftConfigService.addShiftConfiguration(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/shiftConfiguration")
	@ApiOperation(value = "This api is to get type of shift configurations list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByConfiguration(@RequestParam("configuration") String config) {
		GenericResponse objGenericResponse = shiftConfigService.getByConfiguration(config);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PreAuthorize(PermissionConstant.SHIFT_CONFIGURATION)
	@PutMapping("/updateShiftConfig")
	@ApiOperation(value = "This api is used to update shift configuration", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateShiftConfiguration(@Valid @RequestBody ShiftConfigRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(shiftConfigService.updateShiftConfiguration(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/Search")
	@ApiOperation(value = "This api is used to get shift Configurations base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByConfigFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(shiftConfigService.searchByConfigFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all shift Configuration", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(shiftConfigService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get shift configuration by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(shiftConfigService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.SHIFT_CONFIGURATION)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(shiftConfigService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active working days", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(shiftConfigService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
}
