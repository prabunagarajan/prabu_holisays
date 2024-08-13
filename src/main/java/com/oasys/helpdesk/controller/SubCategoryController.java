package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SubCategoryRequestDto;
import com.oasys.helpdesk.service.SubCategoryService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of SubCategory")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ticketsubcategory")
public class SubCategoryController {
	@Autowired
	SubCategoryService helpDeskSubCategoryService;
	
	@RequestMapping(value = "/getAllSubCategory", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllSubCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllSubCategory() {
		GenericResponse objGenericResponse = helpDeskSubCategoryService.getAllSubCategory();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getSubCategoryByCategoryId", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getSubCategoryByCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSubCategoryByCategory(@RequestParam("categoryid") Long categoryid) {
		GenericResponse objGenericResponse = helpDeskSubCategoryService.getSubCategoryByCategoryId(categoryid);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getSubCategoryById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSubCategoryById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = helpDeskSubCategoryService.getSubCategoryById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_SUB_CATEGORY)
	@RequestMapping(value = "/addSubCategory", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the SubCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createSubCategory(@Valid @RequestBody SubCategoryRequestDto helpDeskSubCategoryRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskSubCategoryService.createSubCategory(helpDeskSubCategoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	 
	@RequestMapping(value = "/searchSubCategory", method = RequestMethod.POST)
	public ResponseEntity<Object> searchSubCategory(@RequestBody PaginationRequestDTO paginationRequestDTO) throws JsonParseException,ParseException {
    GenericResponse objGenericResponse = helpDeskSubCategoryService.searchSubCategory(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_SUB_CATEGORY)
	@RequestMapping(value = "/editSubCategory", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to create the SubCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editSubCategory(@Valid @RequestBody SubCategoryRequestDto helpDeskSubCategoryRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskSubCategoryService.editSubCategory(helpDeskSubCategoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_SUB_CATEGORY)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(helpDeskSubCategoryService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(helpDeskSubCategoryService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	
}