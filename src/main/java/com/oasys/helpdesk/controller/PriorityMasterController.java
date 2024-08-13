package com.oasys.helpdesk.controller;

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
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.PriorityMasterRequestDto;
import com.oasys.helpdesk.service.PriorityMasterService;
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

@RequestMapping("/priority")
public class PriorityMasterController {
	@Autowired
	PriorityMasterService priorityMasterService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get priority master data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getIssueFromList() {
		//GenericResponse objGenericResponse = issueFromService.getIssueFromList();
		return new ResponseEntity<>(priorityMasterService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PRIORITY)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to add the priority details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createIssue(@Valid @RequestBody PriorityMasterRequestDto requestDTO)  {
	//	GenericResponse objGenericResponse = issueFromService.createIssueFrom(issueRequestDto);
		return new ResponseEntity<>(priorityMasterService.create(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get priority details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(priorityMasterService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PRIORITY)
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to edit priority details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateAssetType(@Valid @RequestBody PriorityMasterRequestDto requestDTO) {
		return new ResponseEntity<>(priorityMasterService.updateAssetType(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_PRIORITY)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(priorityMasterService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Object> searchActualProblem(@RequestBody PaginationRequestDTO paginationRequestDTO)  {
    GenericResponse objGenericResponse = priorityMasterService.searchByFilter(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active priority details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(priorityMasterService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("get/{subCategoryId}/{categoryId}")
	@ApiOperation(value = "This api is to get priority details by categoryId and Sub categoryId", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("subCategoryId") Long subCategoryId,@PathVariable("categoryId") Long categoryId) {
		return new ResponseEntity<>(priorityMasterService.getById(subCategoryId,categoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
