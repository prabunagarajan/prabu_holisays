package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.request.IssueFromRequestDto;
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

@RequestMapping("/issuefrom")
public class IssueFromController {

	@Autowired
	IssueFromService issueFromService;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getIssueFromList data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getIssueFromList() {
		//GenericResponse objGenericResponse = issueFromService.getIssueFromList();
		return new ResponseEntity<>(issueFromService.getIssueFromList(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ISSUE_FROM)
	@RequestMapping(value = "/addIssueFrom", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Issue from", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createIssue(@Valid @RequestBody IssueFromRequestDto issueRequestDto) throws RecordNotFoundException, Exception {
	//	GenericResponse objGenericResponse = issueFromService.createIssueFrom(issueRequestDto);
		return new ResponseEntity<>(issueFromService.createIssueFrom(issueRequestDto), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ISSUE_FROM)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(issueFromService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get Issue From details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(issueFromService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ISSUE_FROM)
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to edit issue from details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateAssetType(@Valid @RequestBody IssueFromRequestDto requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(issueFromService.updateAssetType(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active issue from", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(issueFromService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
