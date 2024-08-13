package com.oasys.helpdesk.controller;

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

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.ProblemReportedRequestDto;
import com.oasys.helpdesk.service.ProblemReportedService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of ProblemReported")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/problemreported")
public class ProblemReportedController {
	@Autowired
	ProblemReportedService helpDeskProblemReportedService;

	@RequestMapping(value = "/getAllProblemReported", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllProblemReported data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActionTaken() {
		GenericResponse objGenericResponse = helpDeskProblemReportedService.getAllProblemReported();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	

//	@RequestMapping(value = "/getProblemReportedById", method = RequestMethod.GET)
//	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getProblemReportedById(@RequestParam("id") Long id) throws Exception {
//		GenericResponse objGenericResponse = helpDeskProblemReportedService.getProblemReportedById(id);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
//	
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view page by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(helpDeskProblemReportedService.getProblemReportedById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PROBLEM_REPORTED)
	@RequestMapping(value = "/addProblemReported", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the ProblemReported data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createProblemReported(@Valid @RequestBody ProblemReportedRequestDto helpDeskProblemReportedRequestDto) {
		GenericResponse objGenericResponse = helpDeskProblemReportedService.createProblemReported(helpDeskProblemReportedRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

	@RequestMapping(value = "/searchProblemReported", method = RequestMethod.POST)
	public ResponseEntity<Object> searchProblemReported(@RequestBody PaginationRequestDTO paginationRequestDTO)  {
    GenericResponse objGenericResponse = helpDeskProblemReportedService.searchProblemReported(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PROBLEM_REPORTED)
	@RequestMapping(value = "/editProblemReported", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to edit the ProblemReported data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editProblemReported(@Valid @RequestBody ProblemReportedRequestDto helpDeskProblemReportedRequestDto) {
		GenericResponse objGenericResponse = helpDeskProblemReportedService.editProblemReported(helpDeskProblemReportedRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

	
	@PreAuthorize(PermissionConstant.HELP_DESK_PROBLEM_REPORTED)
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode()  {
		return new ResponseEntity<>(helpDeskProblemReportedService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	
	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all active Problem Reported", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(helpDeskProblemReportedService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getById/{categoryId}/{subCategoryId}")
	@ApiOperation(value = "This api is to get active records based on category id and subcategory id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("categoryId") Long categoryId,@PathVariable("subCategoryId") Long subcategoryId) {
		return new ResponseEntity<>(helpDeskProblemReportedService.getById(categoryId,subcategoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}