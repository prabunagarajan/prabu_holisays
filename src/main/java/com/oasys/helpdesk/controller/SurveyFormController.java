package com.oasys.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.ServeyFormDataRequestDTO;
import com.oasys.helpdesk.service.SurveyFormServiceImpl;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for SurveyForm")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/surveyform")
public class SurveyFormController {

	@Autowired
	private SurveyFormServiceImpl surveyFormServiceImpl;

	@PostMapping("/addSurveyForm")
	@ApiOperation(value = "This api is used to add SurveyForm", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addSurveyForm(@Valid @RequestBody List<ServeyFormDataRequestDTO> payload) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(surveyFormServiceImpl.addSurveyFormprocess(payload), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	
	
	@GetMapping("/getAllQuestions")
	@ApiOperation(value = "This api is to get all getAllQuestions", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByQuestions() {
		return new ResponseEntity<>(surveyFormServiceImpl.getAllByQuestions(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@GetMapping("/getDatabytcNo/{ticketNo}")
	@ApiOperation(value = "This api is to get all ticket data details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByticketNo(@PathVariable("ticketNo") String ticketNo) {
		return new ResponseEntity<>(surveyFormServiceImpl.getByTicketNo(ticketNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	

}
