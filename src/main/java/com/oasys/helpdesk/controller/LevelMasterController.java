package com.oasys.helpdesk.controller;

import com.oasys.helpdesk.request.LevelMasterDto;
import com.oasys.helpdesk.service.LevelMasterService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

@RequestMapping("/level")
public class LevelMasterController extends BaseController {
	@Autowired
	LevelMasterService levelMasterService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get  all level data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllLevel() {
		GenericResponse objGenericResponse = levelMasterService.getAllLevel();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get level data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getLevelById(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = levelMasterService.getLevelById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/saveLevel", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the level data", notes = "Returns HTTP 200 if successful save the record")
	public ResponseEntity<Object> createTicket(@RequestBody LevelMasterDto levelRequestDto) {
		GenericResponse objGenericResponse = levelMasterService.createLevel(levelRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}