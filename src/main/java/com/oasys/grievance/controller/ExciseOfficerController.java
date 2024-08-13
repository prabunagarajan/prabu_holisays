package com.oasys.grievance.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.grievance.service.ExciseOfficerService;
import com.oasys.helpdesk.controller.BaseController;
import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;




@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Excise officer")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/GrievanceExciseOfficer")
public class ExciseOfficerController extends BaseController  {
	
	@Autowired
	private ExciseOfficerService eoService;
	
	@RequestMapping(value = "/getList", method = RequestMethod.POST)
	@ApiOperation(value = "This api is to get all grievance ExciseOfficer details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByAll(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(eoService.ListByAll(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get grievance excise officer details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("id") Long id) throws Exception {
		return new ResponseEntity<>(eoService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(eoService.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping("/count")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount(@Valid @RequestBody GrievanceRegRequestDTO requestDto)  throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(eoService.getCount(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@PostMapping("/scrrenbasedcount")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> screngetCount(@Valid @RequestBody GrievanceRegRequestDTO requestDto)  throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(eoService.getCountscreen(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	

	
	
	
	
	
	
}
