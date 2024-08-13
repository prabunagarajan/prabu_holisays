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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.FaqRequestDto;
import com.oasys.helpdesk.service.CategoryService;
import com.oasys.helpdesk.service.FaqService;
import com.oasys.helpdesk.service.PriorityService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Faq")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/helpdeskfaq")
public class FaqController {
	@Autowired
	FaqService helpDeskFaqService;

	@RequestMapping(value = "/getAllfaq", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllfaq data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllfaq() {
		GenericResponse objGenericResponse = helpDeskFaqService.getAllfaq();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/getFaqById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getFaqById(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = helpDeskFaqService.getFaqById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_FAQ)
	@RequestMapping(value = "/addFaq", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Faq data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createFaq(@Valid @RequestBody FaqRequestDto helpDeskFaqRequestDto){
		GenericResponse objGenericResponse = helpDeskFaqService.createFaq(helpDeskFaqRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_FAQ)
	@RequestMapping(value = "/editFaq", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to editFaq data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editFaq(@Valid @RequestBody FaqRequestDto helpDeskFaqRequestDto)  {
		GenericResponse objGenericResponse = helpDeskFaqService.editFaq(helpDeskFaqRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PreAuthorize(PermissionConstant.HELP_DESK_FAQ)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() {
		return new ResponseEntity<>(helpDeskFaqService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active FAQ records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(helpDeskFaqService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
    @ApiOperation(value = "This api is used to get FAQ record based on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(helpDeskFaqService.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/get/{categoryId}/{subCategoryId}")
	@ApiOperation(value = "This api is to get faq details by category and subcategory id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("subCategoryId") Long subCategoryId,@PathVariable("categoryId") Long categoryId) {
		return new ResponseEntity<>(helpDeskFaqService.getById(subCategoryId,categoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
}