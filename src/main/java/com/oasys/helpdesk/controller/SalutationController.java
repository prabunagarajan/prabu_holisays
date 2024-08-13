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
import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SalutationRequestDTO;
import com.oasys.helpdesk.service.SalutationService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Salutation")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/salutation")
public class SalutationController {

	@Autowired
	private SalutationService salutationSrevice;

	@PostMapping("/addSalutation")
	@ApiOperation(value = "This api is used to add salutation data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addSalutation(@Valid @RequestBody SalutationRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(salutationSrevice.addSalutation(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/updateSalutation")
	@ApiOperation(value = "This api is used to update salutation", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateSalutation(@Valid @RequestBody SalutationRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(salutationSrevice.updateSalutation(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all salutation", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(salutationSrevice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getAllActive")
	@ApiOperation(value = "This api is to get all active slutation", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(salutationSrevice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get salutation by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(salutationSrevice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(salutationSrevice.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to get salutation base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(salutationSrevice.searchByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
