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
import com.oasys.helpdesk.dto.InboundCallsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.InboundCallsService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/inboundcalls")
public class InboundCallsController {

	@Autowired
	InboundCallsService inboundservice;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add InboundCalls Details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addInboundCalls(@Valid @RequestBody InboundCallsDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(inboundservice.add(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view InboundCalls Details id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(inboundservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit InboundCalls Details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateInboundCalls(@Valid @RequestBody InboundCallsDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(inboundservice.updateInboundCalls(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to InboundCalls list Details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(inboundservice.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/TotalcallsSummarycount")
	@ApiOperation(value = "This api is to get Count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTotalCallsSummaryCount(@Valid @RequestBody InboundCallsDTO requestDTO) {
		GenericResponse objGenericResponse = inboundservice.getTotalCallsSummaryCount(requestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	
}
