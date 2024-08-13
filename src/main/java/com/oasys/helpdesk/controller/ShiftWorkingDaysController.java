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
import com.oasys.helpdesk.dto.ShiftWorkingDaysRequestDTO;
import com.oasys.helpdesk.service.ShiftWorkingDaysService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Shift working days")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/shift-working-days")
public class ShiftWorkingDaysController {

	@Autowired
	private ShiftWorkingDaysService workingDaysService;

	@PreAuthorize(PermissionConstant.SHIFT_WORKING_DAYS)
	@PostMapping("/addShiftworkingdays")
	@ApiOperation(value = "This api is used to add Shift working days data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addShiftWorkingDays(@Valid @RequestBody ShiftWorkingDaysRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(workingDaysService.addShiftWorkingDays(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.SHIFT_WORKING_DAYS)
	@PutMapping("/updateShiftWorkingDays")
	@ApiOperation(value = "This api is used to update shift Working Days", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateShiftWorkingDays(@Valid @RequestBody ShiftWorkingDaysRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(workingDaysService.updateShiftworkingDays(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/workingDays")
	@ApiOperation(value = "This api is to get type of shiftworking days list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByworkingDays(@RequestParam("workingDays") Long workingdays) {
		GenericResponse objGenericResponse = workingDaysService.getByworkingDays(workingdays);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get shift workingdays by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(workingDaysService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all shift working days", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(workingDaysService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active working days", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(workingDaysService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/Search")

	@ApiOperation(value = "This api is used to get shift working Days base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByWorkingDays(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(workingDaysService.searchByWorkingDays(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.SHIFT_WORKING_DAYS)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(workingDaysService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}











