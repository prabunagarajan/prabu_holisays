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
import com.oasys.helpdesk.request.CategoryRequestDto;
import com.oasys.helpdesk.service.CategoryService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Category")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ticketcategory")
public class CategoryController {
	@Autowired
	CategoryService helpDeskCategoryService;

	@RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getAllCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllCategory() {
		GenericResponse objGenericResponse = helpDeskCategoryService.getAllCategory();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCategoryById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCategoryById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = helpDeskCategoryService.getCategoryById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/searchCategory", method = RequestMethod.POST)
	public ResponseEntity<Object> searchCategory(@RequestBody PaginationRequestDTO paginationRequestDTO) throws JsonParseException,ParseException {
    GenericResponse objGenericResponse = helpDeskCategoryService.searchCategory(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_CATEGORY)
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Category data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskCategoryService.createCategory(categoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PreAuthorize(PermissionConstant.HELP_DESK_CATEGORY)
	@RequestMapping(value = "/editCategory", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to edit the Category data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = helpDeskCategoryService.editCategory(categoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_CATEGORY)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode(){
		return new ResponseEntity<>(helpDeskCategoryService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(helpDeskCategoryService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}