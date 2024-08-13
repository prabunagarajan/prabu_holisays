package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceWorkflowRequestDTO;
import com.oasys.helpdesk.service.GrievanceWorkflowService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of GrievanceWorkflow")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/grievanceWorkflow")
public class GrievanceWorkflowController {
	
	@Autowired
	private GrievanceWorkflowService gWorkflowService;

	@PreAuthorize(PermissionConstant.GRIEVANCE_MASTER_WORKFLOW)
	@RequestMapping(value = "/addWorkflow", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the grievanceWorkflow data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createWorkflow(@RequestBody GrievanceWorkflowRequestDTO RequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = gWorkflowService.createWorkflow(RequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.GRIEVANCE_MASTER_WORKFLOW)
	@PutMapping("/updateWorkflow")
	@ApiOperation(value = "This api is used to update Workflow record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateWorkflow(@Valid @RequestBody GrievanceWorkflowRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(gWorkflowService.updateWorkflow(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get gWorkflow list by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gWorkflowService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all gWorkflow records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(gWorkflowService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active gWorkflow records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(gWorkflowService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(gWorkflowService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/Search")
	@ApiOperation(value = "This api is used to get gWorkflow on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByGWorkflow(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(gWorkflowService.searchByGWorkflow(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	
}
