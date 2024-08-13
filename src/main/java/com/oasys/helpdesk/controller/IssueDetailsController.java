package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.IssueRequestDto;
import com.oasys.helpdesk.service.IssueDetailsService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//This is using spring security 
//based on the scope Domain can access  this controller 
//its access and right give the Domain module ,this call goes Domain module 
//@PreAuthorize("#oauth2.hasScope('Admin') or #oauth2.hasScope('Merchant') ")

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Issue Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/issuedetails")
public class IssueDetailsController {
	@Autowired
	IssueDetailsService issueDetailsService;

	@RequestMapping(value = "/getAllIssueDetails", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllIssueDetails data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllIssueDetails() {
		GenericResponse objGenericResponse = issueDetailsService.getAllIssueDetails();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	

	
	@GetMapping("getIssueDetailsById/{id}")
	@ApiOperation(value = "This api is to get view page by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(issueDetailsService.getIssueDetailsById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ISSUE_DETAILS)
	@RequestMapping(value = "/addIssue", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Issue data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createIssue(@Valid @RequestBody IssueRequestDto issueRequestDto) {
		GenericResponse objGenericResponse = issueDetailsService.createIssue(issueRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ISSUE_DETAILS)
	@RequestMapping(value = "/updateIssue", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to create the Issue data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateIssue(@Valid @RequestBody IssueRequestDto issueRequestDto)  {
		GenericResponse objGenericResponse = issueDetailsService.updateIssue(issueRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@RequestMapping(value = "/searchIssueDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> searchIssueDetails(@RequestBody PaginationRequestDTO paginationRequestDTO) {
    GenericResponse objGenericResponse = issueDetailsService.searchIssueDetails(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ISSUE_DETAILS)
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() {
		return new ResponseEntity<>(issueDetailsService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	
	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all active issue status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(issueDetailsService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("getIssueDetails/{subCategoryId}/{categoryId}")
	@ApiOperation(value = "This api is to get priority details by categoryId and Sub categoryId", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByIds(@PathVariable("subCategoryId") Long subCategoryId,@PathVariable("categoryId") Long categoryId) {
		return new ResponseEntity<>(issueDetailsService.getById(subCategoryId,categoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
