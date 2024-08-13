package com.oasys.helpdesk.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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

import com.oasys.helpdesk.dto.DistrictDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO2;
import com.oasys.helpdesk.dto.TicketcounRequest;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.CreateTicketRequestDto2;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.CreateTicketService;

import com.oasys.helpdesk.service.HelpdeskTicketAuditService;
import com.oasys.helpdesk.service.HelpdeskUserAuditService;
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

@RequestMapping("/ticket")
public class TicketCountController extends BaseController{

	@Autowired
	CreateTicketService createTicketService;
	
	@Autowired
	private HelpdeskTicketAuditService helpdeskTicketAuditService;

	

	
	@PostMapping("/districtwiseentityticket")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Districtwiseentitytickett(@Valid @RequestBody CreateTicketRequestDto requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.getdistrictwiseentityticket(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@PostMapping("/districtwiseshopcodeticket")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Districtwiseshopcodeticket(@Valid @RequestBody CreateTicketRequestDto requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.getdistrictwiseshopcodeticket(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/districtwiseincident")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Districtwiseincidentticket(@Valid @RequestBody CreateTicketRequestDto requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.getdistrictwiseincident(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping("/searchincident")
    @ApiOperation(value = "This api is used to get incident filter search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException  {
		return new ResponseEntity<>(createTicketService.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	
}
