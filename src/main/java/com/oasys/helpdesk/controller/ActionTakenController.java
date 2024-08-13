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
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.ActionTakenService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Action Taken")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/actiontaken")
public class ActionTakenController extends BaseController {
	@Autowired
	ActionTakenService helpDeskActionTakenService;


	@RequestMapping(value = "/getAllActionTaken", method = RequestMethod.GET)
	@ApiOperation(value = "This api  to get  all getAllActionTaken", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getAllActionTaken() {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return helpDeskActionTakenService.getAllActionTaken(authenticationDTO);
	}
	
	
	@RequestMapping(value = "/getActionTakenByActionProblemId", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getActionTakenByActionProblemId data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getActionTakenByActionProblemId(@RequestParam("actionproblemid") Long actionproblemid) {
		GenericResponse objGenericResponse = helpDeskActionTakenService.getActionTakenByActionProblemId(actionproblemid);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getActionTakenById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getActionTakenById(@RequestParam("id") Long id)  {
		GenericResponse objGenericResponse = helpDeskActionTakenService.getActionTakenById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ACTION_TAKEN)
	@RequestMapping(value = "/addActionTaken", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the editActionTaken", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse  createActionTaken(@Valid @RequestBody ActionTakenRequestDto actionTakenRequestDto)
			 {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return  helpDeskActionTakenService.createActionTaken(authenticationDTO, actionTakenRequestDto);
	}

	@RequestMapping(value = "/getAllActionTakenForPending", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllActionTakenForPending data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActionTakenForPending() {
		GenericResponse objGenericResponse = helpDeskActionTakenService.getAllActionTakenForPending();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getActionTakenForPendingById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getActionTakenForPendingById(@RequestParam("id") Long id)  {
		GenericResponse objGenericResponse = helpDeskActionTakenService.getActionTakenForPendingById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchActionTaken", method = RequestMethod.POST)
	public ResponseEntity<Object> searchActionTaken(@RequestBody PaginationRequestDTO paginationRequestDTO) {
    GenericResponse objGenericResponse = helpDeskActionTakenService.searchByFilter(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	} 
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ACTION_TAKEN)
	@RequestMapping(value = "/editActionTaken", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to create the editActionTaken", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse  editActionTaken(@Valid @RequestBody ActionTakenRequestDto actionTakenRequestDto)
			{
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return  helpDeskActionTakenService.editActionTaken(authenticationDTO, actionTakenRequestDto);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ACTION_TAKEN)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() {
		return new ResponseEntity<>(helpDeskActionTakenService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(helpDeskActionTakenService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/getById/{categoryId}/{subCategoryId}/{actualProblemId}")
	@ApiOperation(value = "This api is to get active records based on category id and subcategory id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("categoryId") Long categoryId,@PathVariable("subCategoryId") Long subcategoryId,@PathVariable("actualProblemId") Long actualProblemId) {
		return new ResponseEntity<>(helpDeskActionTakenService.getById(categoryId,subcategoryId,actualProblemId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
}  