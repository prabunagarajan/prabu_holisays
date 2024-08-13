package com.oasys.helpdesk.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SlaMasterRequestDto;
import com.oasys.helpdesk.request.WorkflowRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.WorkflowService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Action Taken")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/workflow")
public class WorkflowController extends BaseController {
	@Autowired
	WorkflowService WorkflowService;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api  to get  all getAll details", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getAll()
			throws RecordNotFoundException, GeneralSecurityException, IOException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return WorkflowService.getAll(authenticationDTO);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_WORKFLOW)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to add the workflow details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> create(@Valid @RequestBody WorkflowRequestDto requestDTO) throws RecordNotFoundException, Exception {
	//	GenericResponse objGenericResponse = issueFromService.createIssueFrom(issueRequestDto);
		return new ResponseEntity<>(WorkflowService.create(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get workflow details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(WorkflowService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_WORKFLOW)
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to edit workflow details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody WorkflowRequestDto requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(WorkflowService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_WORKFLOW)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(WorkflowService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Object> search(@RequestBody PaginationRequestDTO paginationRequestDTO) throws JsonParseException,ParseException {
    GenericResponse objGenericResponse = WorkflowService.searchByFilter(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active workflow details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(WorkflowService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getSla/{categoryId}/{subcategoryId}/{issueDetailsId}")
	@ApiOperation(value = "This api is to get sla details based on category, subcategory and issue details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSla(@PathVariable("categoryId") Long categoryId,@PathVariable("subcategoryId") Long subcategoryId,@PathVariable("issueDetailsId") Long issueDetailsId) {
		return new ResponseEntity<>(WorkflowService.getSla(categoryId,subcategoryId,issueDetailsId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
