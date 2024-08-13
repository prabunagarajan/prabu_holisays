package com.oasys.helpdesk.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.service.ActualProblemService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//This is using spring security 
//based on the scope Domain can access  this controller 
//its access and right give the Domain module ,this call goes Domain module 
//@PreAuthorize("#oauth2.hasScope('Admin') or #oauth2.hasScope('Merchant') ")

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Actual Problem")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/actualproblem")
public class ActualProblemController {
	@Autowired
	ActualProblemService helpDeskActualProblemService;

	@RequestMapping(value = "/getAllActualProblem", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllActualProblem data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActualProblem() {
		GenericResponse objGenericResponse = helpDeskActualProblemService.getAllActualProblem();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getActualProblemById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getActualProblemById(@RequestParam("id") Long id)  {
		GenericResponse objGenericResponse = helpDeskActualProblemService.getActualProblemById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ACTUAL_PROBLEM)
	@RequestMapping(value = "/addActualProblem", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the ActualProblem data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createActualProblem(@Valid @RequestBody ActualProblemRequestDto actualProblemRequestDto) {
		GenericResponse objGenericResponse = helpDeskActualProblemService.createActualProblem(actualProblemRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ACTUAL_PROBLEM)
	@RequestMapping(value = "/editActualProblem", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to create the ActualProblem data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editActualProblem(@Valid @RequestBody ActualProblemRequestDto actualProblemRequestDto)  {
		GenericResponse objGenericResponse = helpDeskActualProblemService.editActualProblem(actualProblemRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/searchActualProblem", method = RequestMethod.POST)
	public ResponseEntity<Object> searchActualProblem(@RequestBody PaginationRequestDTO paginationRequestDTO) {
    GenericResponse objGenericResponse = helpDeskActualProblemService.searchByFilter(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PreAuthorize(PermissionConstant.HELP_DESK_ACTUAL_PROBLEM)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode()  {
		return new ResponseEntity<>(helpDeskActualProblemService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(helpDeskActualProblemService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getById/{categoryId}/{subCategoryId}")
	@ApiOperation(value = "This api is to get active records based on category id and subcategory id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("categoryId") Long categoryId,@PathVariable("subCategoryId") Long subcategoryId) {
		return new ResponseEntity<>(helpDeskActualProblemService.getById(categoryId,subcategoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}