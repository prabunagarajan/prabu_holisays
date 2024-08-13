package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SiteActionTakenRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.SiteActionTakenService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Site Action Taken")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/site-action-taken")
public class SiteActionTakenController extends BaseController{
	
	@Autowired
	SiteActionTakenService siteActionTakenService;


	@RequestMapping(value = "/addSiteActionTaken", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the Site Action Taken", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse  createSiteActionTaken(@Valid @RequestBody SiteActionTakenRequestDto siteActionTakenRequestDto)
			 {
		return  siteActionTakenService.createSiteActionTaken(siteActionTakenRequestDto);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get Site Action Taken by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(siteActionTakenService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllSiteActionTaken", method = RequestMethod.GET)
	@ApiOperation(value = "This api  to get  all getAll SiteActionTaken", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getAllSiteActionTaken() {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return siteActionTakenService.getAllSiteActionTaken(authenticationDTO);
	}
	
	@RequestMapping(value = "/getSiteActionTakenByObservationId", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getSiteActionTakenByObservationId data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSiteActionTakenByObservationId(@RequestParam("observationid") Long observationid) {
		GenericResponse objGenericResponse = siteActionTakenService.getSiteActionTakenByObservationId(observationid);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
		
	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit Site Action Taken ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateSiteActionTaken(@Valid @RequestBody SiteActionTakenRequestDto requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(siteActionTakenService.editSiteActionTaken(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/filterstatussearch")
	@ApiOperation(value = "This api is used to search device registeration record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchPassReport(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(siteActionTakenService.getAllByPassFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
