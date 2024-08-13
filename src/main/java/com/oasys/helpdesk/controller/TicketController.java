package com.oasys.helpdesk.controller;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.TicketRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.TicketService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Category")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ticket")
public class TicketController extends BaseController {
	@Autowired
	TicketService helpDeskTicketService;

	@RequestMapping(value = "/getAllTicket", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getAllTicket data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllTicket() {
		GenericResponse objGenericResponse = helpDeskTicketService.getAllTicket();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllTicketByStatus", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getAllTicketbystatus data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllTicketByStatus(@RequestParam("statusid") Long statusid) {
		GenericResponse objGenericResponse = helpDeskTicketService.getAllTicketByStatus(statusid);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllTicketByCategory", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getAllTicketByCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllTicketByCategory(@RequestParam("categoryid") Long categoryid) {
		GenericResponse objGenericResponse = helpDeskTicketService.getAllTicketByCategory(categoryid);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getTicketById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = helpDeskTicketService.getTicketById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/addTicket", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the addTicket data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createTicket(@RequestBody TicketRequestDto helpDeskTicketRequestDto)
			throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskTicketService.createTicket(helpDeskTicketRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateTicketStatus", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the updateTicketStatus data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateTicketStatus(@RequestBody TicketRequestDto helpDeskTicketRequestDto)
			throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskTicketService.updateTicketStatus(helpDeskTicketRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	// not needed
//	@RequestMapping(value = "/deleteTicketById", method = RequestMethod.GET)
//	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> deleteTicketById(@RequestParam("id") Long id) throws Exception {
//		GenericResponse objGenericResponse = helpDeskTicketService.deleteTicketById(id);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}

	@RequestMapping(value = "/searchUser", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchUser(@RequestParam("search") String search) throws Exception {
		GenericResponse objGenericResponse = helpDeskTicketService.searchUser(search);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/searchTicket", method = RequestMethod.POST)
	public ResponseEntity<Object> indentReqLazySearch(@RequestBody PaginationRequestDTO paginationRequestDTO)
			throws JsonParseException, ParseException {

		GenericResponse objGenericResponse = helpDeskTicketService.getTicketLazySearch(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@RequestMapping(value = "/createCallDisconnectedTicket", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the addTicket data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createCallDisconnectedTicket(@RequestBody TicketRequestDto helpDeskTicketRequestDto)
			throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskTicketService
				.createCallDisconnectedTicket(helpDeskTicketRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllCallDisconnectedTicket", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getAllTicket data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllCallDisconnectedTicket() {
		GenericResponse objGenericResponse = helpDeskTicketService.getAllCallDisconnectedTicket();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getFieldAgentTickets", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getFieldAgentTickets data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getFieldAgentTickets(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = helpDeskTicketService.getFieldAgentTickets(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getRecentTicketForFieldAgentById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getRecentTicketForFieldAgentById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = helpDeskTicketService.getRecentTicketForFieldAgentById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateTicketStatusForFieldAgent", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the updateTicketStatus data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateTicketStatusForFieldAgent(
			@RequestBody TicketRequestDto helpDeskTicketRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskTicketService
				.updateTicketStatusForFieldAgent(helpDeskTicketRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/searchByLicenceNumber", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to/getUserByDesignationCode based on code", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse searchByLicenceNumber(@RequestParam("applicationNumber") String applicationNumber,
			Locale locale) throws RecordNotFoundException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return helpDeskTicketService.searchByLicenceNumber(authenticationDTO, locale, applicationNumber);
	}

	
}