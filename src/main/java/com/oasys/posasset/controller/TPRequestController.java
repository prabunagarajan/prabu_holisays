package com.oasys.posasset.controller;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.TPRequestDTO;
import com.oasys.posasset.service.impl.TPRequestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "PosAssetData", description = "This controller contain all operations for Department Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/tprequest")
public class TPRequestController {

	@Autowired
	TPRequestService tprequestservice;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add tp request details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addtprequestPage(@Valid @RequestBody List<TPRequestDTO> requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(tprequestservice.Tprequestsave(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(tprequestservice.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all TP Dispatch list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(tprequestservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is all TPDispatchDetails", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByRequestFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		return new ResponseEntity<>(tprequestservice.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view TP request dispatch details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(tprequestservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
//	 
	 @GetMapping("/balance/{tpApplnno}")
		@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
		public ResponseEntity<Object> getBalanceForApplicationNumber(@PathVariable String tpApplnno) {
			return new ResponseEntity<>(tprequestservice.getBalanceForApplicationNumber(tpApplnno), ResponseHeaderUtility.HttpHeadersConfig(),
					HttpStatus.OK);
		}
	 
	}

