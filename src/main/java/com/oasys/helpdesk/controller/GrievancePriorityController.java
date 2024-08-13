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
import com.oasys.helpdesk.request.GrievancePriorityRequestDTO;
import com.oasys.helpdesk.service.GrievancePriorityService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Faq")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/GrievancePriority")
public class GrievancePriorityController {

	@Autowired
	private GrievancePriorityService grievancePriorityService;
	
	@PreAuthorize(PermissionConstant.GRIEVANCE_PRIORITY)
	@RequestMapping(value = "/addPriority", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the priority data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createPriority(@RequestBody GrievancePriorityRequestDTO priorityRequestDTO) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = grievancePriorityService.createPriority(priorityRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.GRIEVANCE_PRIORITY)
	@PutMapping("/updateGrievancePriority")
	@ApiOperation(value = "This api is used to update grievancePriority record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updatePriority(@Valid @RequestBody GrievancePriorityRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(grievancePriorityService.updatePriority(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get grievancePriority list by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(grievancePriorityService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all grievancePriority records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(grievancePriorityService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active grievancePriority records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(grievancePriorityService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(grievancePriorityService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/Search")
	@ApiOperation(value = "This api is used to get grievancePriority on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByPriority(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(grievancePriorityService.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	@ApiOperation(value = "This api is to get all active issue status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByCategory(@PathVariable (value="categoryId") Long categoryId ) {
		return new ResponseEntity<>(grievancePriorityService.getByCategory(categoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
