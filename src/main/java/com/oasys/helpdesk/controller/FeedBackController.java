package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.FeedBackComplaintRequestDTO;
import com.oasys.helpdesk.dto.FeedBackEntityRequestDTO;
import com.oasys.helpdesk.service.FeedBackService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of feedBack")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/feedBack")
public class FeedBackController {
	
	@Autowired
	private FeedBackService feedBackService;
	
//	@Autowired
//	private DevicelostService devicelostservice;
	
	@PostMapping("/addFeedBack")
	@ApiOperation(value = "This api is used to add FeedBack", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addSurveyForm(@Valid @RequestBody FeedBackEntityRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = feedBackService.addSurveyForm(requestDTO);
		
		return new ResponseEntity<>(ResponseEntity.ok().body("your feedback has been submitted successfuly"),ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/addcomplaint")
	@ApiOperation(value = "This api is used to addcomplaint ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addfeedbackcompliantPage(@Valid @RequestBody FeedBackComplaintRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
	return new ResponseEntity<>(feedBackService.addfeedbackcomplint(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	

}
