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
import com.oasys.helpdesk.dto.SiteIssueTypeDTO;
import com.oasys.helpdesk.dto.SiteObservationDTO;
import com.oasys.helpdesk.service.SiteIssueTypeService;
import com.oasys.helpdesk.service.SiteObservation;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/siteissuetype")
public class SiteIssueTypeController {
	
	@Autowired
	SiteIssueTypeService siteissuetypeservice;
	
	
	@PostMapping("/add")
	 @ApiOperation(value = "This api is used to add SiteIssueType Type", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody SiteIssueTypeDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(siteissuetypeservice.add(requestDTO),ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get SiteIssueType by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(siteissuetypeservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all SiteIssueType list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(siteissuetypeservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
}
	@PutMapping("/update")
	@ApiOperation(value = "This api is to update SiteIssueType ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateSite(@Valid @RequestBody SiteIssueTypeDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(siteissuetypeservice.updateSite(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to Serach SiteIssueType details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(siteissuetypeservice.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active SiteIssuetype records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(siteissuetypeservice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getalllist")
	@ApiOperation(value = "This api is to get all SiteIssueType list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAlllist() {
		return new ResponseEntity<>(siteissuetypeservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
}

