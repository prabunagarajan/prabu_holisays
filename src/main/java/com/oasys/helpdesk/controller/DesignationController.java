package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.DesignationRequestDto;
import com.oasys.helpdesk.request.IssueFromRequestDto;
import com.oasys.helpdesk.service.DesignationService;
import com.oasys.helpdesk.service.IssueFromService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Issue From")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/designation")
public class DesignationController {
	@Autowired
	DesignationService designationService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get designation details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getList() {
		// GenericResponse objGenericResponse = issueFromService.getIssueFromList();
		return new ResponseEntity<>(designationService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the designation", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createIssue(@Valid @RequestBody DesignationRequestDto issueRequestDto)
			throws RecordNotFoundException, Exception {
		// GenericResponse objGenericResponse =
		// issueFromService.createIssueFrom(issueRequestDto);
		return new ResponseEntity<>(designationService.create(issueRequestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(designationService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get designation details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(designationService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to edit designation details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateAssetType(@Valid @RequestBody DesignationRequestDto requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(designationService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getAllActive")
	@ApiOperation(value = "This api is to get all active designation details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(designationService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to get designation details base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(designationService.searchByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
