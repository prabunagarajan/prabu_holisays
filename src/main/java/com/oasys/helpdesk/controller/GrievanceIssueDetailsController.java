package com.oasys.helpdesk.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceIssueRequestDto;
import com.oasys.helpdesk.service.GrievanceIssueDetailsService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Grievance Issue Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/GrievanceIssueDetails")
public class GrievanceIssueDetailsController {

	@Autowired
	private GrievanceIssueDetailsService gidService;
	
	
	@RequestMapping(value = "/addGrievanceIssue", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Grievance Issue data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createIssue(@RequestBody GrievanceIssueRequestDto issueRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = gidService.createIssue(issueRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/updateGrievanceIssue", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to create the Grievance Issue data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateIssue(@RequestBody GrievanceIssueRequestDto issueRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = gidService.updateIssue(issueRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllIssueDetails", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllIssueDetails data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllGIssueDetails() {
		GenericResponse objGenericResponse = gidService.getAllGIssueDetails();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("getIssueDetailsById/{id}")
	@ApiOperation(value = "This api is to get view page by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gidService.getGIssueDetailsById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(gidService.getGCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all active issue status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(gidService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	@ApiOperation(value = "This api is to get all active issue status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByCategory(@PathVariable("categoryId") Long categoryId ) {
		return new ResponseEntity<>(gidService.getByCategory(categoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/searchCategory", method = RequestMethod		.POST)
	public ResponseEntity<Object> searchCategory(@RequestBody PaginationRequestDTO paginationRequestDTO) throws JsonParseException,ParseException {
    GenericResponse objGenericResponse = gidService.searchCategory(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	
	@RequestMapping(value = "/grivanceissuedetails", method = RequestMethod.POST)
	@ApiOperation(value = "This api is to get all category id passed to get issuedetails", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByCategory(@RequestBody GrievanceIssueRequestDto issueRequestDto) {
		return new ResponseEntity<>(gidService.getByCategoryid(issueRequestDto), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	

	
	
}
