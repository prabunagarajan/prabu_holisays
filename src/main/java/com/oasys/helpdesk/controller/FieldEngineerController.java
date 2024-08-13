package com.oasys.helpdesk.controller;


import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.CreateTicketDashboardDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.CreateTicketService;
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

@RequestMapping("/field")
public class FieldEngineerController extends BaseController{
	
	
	
	@Autowired
	CreateTicketService createTicketService;

	@RequestMapping(value = "/getAll/{type}", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get all ticket details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll(@PathVariable("type") String type) {
		return new ResponseEntity<>(createTicketService.getAll(type), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/helpdeskTracker/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get ticket details based on status id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByStatusId(@PathVariable("id") Long id) {
		return new ResponseEntity<>(createTicketService.getByStatus(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("id") Long id) throws Exception {
		return new ResponseEntity<>(createTicketService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/count")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount(@RequestParam(value="date", required=false) String date) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getCount(date, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody CreateTicketRequestDto requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.add(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody CreateTicketDashboardDTO requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.update(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PutMapping("/updatefieldengr")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updatefiledengineer(@Valid @RequestBody CreateTicketDashboardDTO requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.update(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	
	

}
