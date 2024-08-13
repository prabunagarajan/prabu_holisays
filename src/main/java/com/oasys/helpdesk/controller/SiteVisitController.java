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
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.dto.SiteVisitDTO;
import com.oasys.helpdesk.service.SiteObservation;
import com.oasys.helpdesk.service.SiteVisitService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping("/sitevisit")

public class SiteVisitController {
	
	@Autowired
	SiteVisitService sitevisitservice;
	
	@PostMapping("/add")
    @ApiOperation(value = "This api is used to add SiteVisit", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody SiteVisitDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(sitevisitservice.add(requestDTO),ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get SiteVisit by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
	return new ResponseEntity<>(sitevisitservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit SiteVisit ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateSite(@Valid @RequestBody SiteVisitDTO requestDTO)
		throws RecordNotFoundException, Exception {
	return new ResponseEntity<>(sitevisitservice.updateSite(requestDTO),
			ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@PostMapping("/search")
	@ApiOperation(value = "This api is all SiteVisit", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByRequestFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(sitevisitservice.getAllByRequestFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

    @GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all SiteVisit list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(sitevisitservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
    
    @PostMapping("/userid")
    @ApiOperation(value = "This api is is used to get sitevisit count", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getsitevisit(@Valid @RequestBody SiteVisitDTO requestDto) throws ParseException {
	return new ResponseEntity<>(sitevisitservice.getsitevisit(requestDto),
			ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
    
    
    
}
