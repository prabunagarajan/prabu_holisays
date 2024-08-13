package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.GrievanceCategoryRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.GrievanceCategoryService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of GrievanceCategory")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/grievanceCategory")
public class GrievanceCategoryController {
	
	@Autowired
	private GrievanceCategoryService grievanceCategoryService;

	
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Category data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createCategory(@Valid @RequestBody GrievanceCategoryRequestDTO categoryRequestDto) {
		GenericResponse objGenericResponse = grievanceCategoryService.createCategory(categoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/editCategory", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to edit the Category data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editCategory(@Valid @RequestBody GrievanceCategoryRequestDTO categoryRequestDto)  {
		GenericResponse objGenericResponse = grievanceCategoryService.editCategory(categoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllCategory", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all getAllCategory data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllCategory() {
		GenericResponse objGenericResponse = grievanceCategoryService.getAllCategory();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCategoryById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCategoryById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = grievanceCategoryService.getCategoryById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchCategory", method = RequestMethod.POST)
	public ResponseEntity<Object> searchCategory(@RequestBody PaginationRequestDTO paginationRequestDTO) throws JsonParseException,ParseException {
    GenericResponse objGenericResponse = grievanceCategoryService.searchCategory(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode(){
		return new ResponseEntity<>(grievanceCategoryService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active grievanceCategory", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(grievanceCategoryService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/activetypeofuser", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Category data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActivetypeofuser(@Valid @RequestBody GrievanceCategoryRequestDTO categoryRequestDto) {
		GenericResponse objGenericResponse = grievanceCategoryService.getAllActivetypeofuser(categoryRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}