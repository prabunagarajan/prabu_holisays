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

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.DistrictRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.UsergroupRequestDTO;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.request.IssueRequestDto;
import com.oasys.helpdesk.service.ActionTakenService;
import com.oasys.helpdesk.service.ActualProblemService;
import com.oasys.helpdesk.service.PriorityService;
import com.oasys.helpdesk.service.TicketStatusService;
import com.oasys.helpdesk.service.UsergroupService;
import com.oasys.helpdesk.service.IssueDetailsService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Issue Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/usergroup")
public class UserGroupController {
	
	
	
	@Autowired
	private UsergroupService usergroupservice;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add ticket status details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddticketPage(@Valid @RequestBody UsergroupRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(usergroupservice.adddusergroup(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all Ticketstatus list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(usergroupservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get view Ticketstatus by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(usergroupservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit Ticketstatus", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDepartment(@Valid @RequestBody UsergroupRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(usergroupservice.updateusergroup(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	@PostMapping("/search")
    @ApiOperation(value = "This api is used to get Ticketstaus base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(usergroupservice.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all Ticket Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(usergroupservice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(usergroupservice.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	

}
