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
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteObservationRequestDto;
import com.oasys.helpdesk.service.SiteObservation;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/siteobservation")
public class SiteObservationController {
	
	
	@Autowired
	SiteObservation siteobservationservice;
	
	@PostMapping("/add")
	
	
    @ApiOperation(value = "This api is used to add SiteObservation Type", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addObservation(@Valid @RequestBody SiteObservationRequestDto requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(siteobservationservice.add(requestDTO),ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get Site Observation by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(siteobservationservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all Observation list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(siteobservationservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
}
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit Observation ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateSite(@Valid @RequestBody SiteObservationDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(siteobservationservice.updateSite(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active Observation records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(siteobservationservice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to aseet list details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(siteobservationservice.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getSiteObservationByIssueTypeId", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getSiteObservationByIssueTypeId data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSiteObservationByIssueTypeId(@RequestParam("issueTypeId") Long issuetypeid) {
		GenericResponse objGenericResponse = siteobservationservice.getSiteObservationByIssueTypeId(issuetypeid);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
