package com.oasys.helpdesk.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.request.SlaConfigurationRequestDto;
import com.oasys.helpdesk.request.SlaEmailTemplateRequestDto;
import com.oasys.helpdesk.request.SlaSmsTemplateRequestDto;
import com.oasys.helpdesk.request.SlaTemplateRequestDto;
import com.oasys.helpdesk.service.ActionTakenService;
import com.oasys.helpdesk.service.ActualProblemService;
import com.oasys.helpdesk.service.PriorityService;
import com.oasys.helpdesk.service.SlaService;
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
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Sla")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/sla")
public class SlaController {
	@Autowired
    SlaService slaService;

	@RequestMapping(value = "/getAllslaemailtemplate", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllslaemailtemplate data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllslaemailtemplate() {
		GenericResponse objGenericResponse = slaService.getAllslaemailtemplate();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	

	@RequestMapping(value = "/getSlaEmailTemplateById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to getSlaEmailTemplateById data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSlaEmailTemplateById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = slaService.getSlaEmailTemplateById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/addSlaemailtemplate", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Slaemailtemplate data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createSlaemailTemplate(@RequestBody SlaEmailTemplateRequestDto SlaEmailTemplateRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = slaService.createSlaemailTemplate(SlaEmailTemplateRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addSlaSmstemplate", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the SlaSmstemplate data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createSlaSmsTemplate(@RequestBody SlaSmsTemplateRequestDto slaSmsTemplateRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = slaService.createSlaSmsTemplate(slaSmsTemplateRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllslaSmstemplate", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllslaSmstemplate data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllslaSmstemplate() {
		GenericResponse objGenericResponse = slaService.getAllslaSmstemplate();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getSlaSmsTemplateById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to getSlaSmsTemplateById data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSlaSmsTemplateById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = slaService.getSlaSmsTemplateById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addSlaConfiguration", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the SlaConfiguration data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createSlaConfiguration(@RequestBody SlaConfigurationRequestDto slaConfigurationRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = slaService.createSlaConfiguration(slaConfigurationRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllSlaConfiguration", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllSlaConfiguration data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllSlaConfiguration() {
		GenericResponse objGenericResponse = slaService.getAllSlaConfiguration();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchSlaByRuleName", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to searchSlaByRuleName data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchSlaByRuleName(@RequestParam("rulename") String rulename) {
		GenericResponse objGenericResponse = slaService.searchSlaByRuleName(rulename);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getSlaConfigurationById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to getSlaConfigurationById data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSlaConfigurationById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = slaService.getSlaConfigurationById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/createSlaTemplate", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the createSlaTemplate data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createSlaTemplate(@RequestBody SlaTemplateRequestDto slaTemplateRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = slaService.createSlaTemplate(slaTemplateRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllSlaTemplate", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllSlaTemplate data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllSlaTemplate() {
		GenericResponse objGenericResponse = slaService.getAllSlaTemplate();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getSlaTemplateById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to getSlaTemplateById data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSlaTemplateById(@RequestParam("id") Long id) throws Exception {
		GenericResponse objGenericResponse = slaService.getSlaTemplateById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/slaTemplateSearch", method = RequestMethod.POST)
	public ResponseEntity<Object> slaTemplateSearch(@RequestBody PaginationRequestDTO paginationRequestDTO) throws JsonParseException,ParseException {
    GenericResponse objGenericResponse = slaService.slaTemplateSearch(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/editSlaConfiguration", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to edit the SlaConfiguration data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> editSlaConfiguration(@RequestBody SlaConfigurationRequestDto slaConfigurationRequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = slaService.editSlaConfiguration(slaConfigurationRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}