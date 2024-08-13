package com.oasys.helpdesk.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.oasys.helpdesk.dto.SurveyQuestionMasterRequestDTO;
import com.oasys.helpdesk.service.SurveyFormQuestionService;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for SurveyForm")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/surveyformQuestion")
public class SurveyFormQuestionController {

	@Autowired
	SurveyFormQuestionService surveyFormQuestionService;

	@PutMapping("/updatesurveyQuestionForm")
	@ApiOperation(value = "This api is used to update SurveyForm", notes = "Returns HTTP 200 if successful update the record")
	public ResponseEntity<Object> updateServeyForm(
			@Valid @RequestBody SurveyQuestionMasterRequestDTO surveyquestionmasterrequestdto)

			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(surveyFormQuestionService.updateServeyQuestionForm(surveyquestionmasterrequestdto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
