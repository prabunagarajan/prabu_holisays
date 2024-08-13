package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceFaqRequestDTO;
import com.oasys.helpdesk.service.GrievanceFaqService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Faq")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/grievanceFaq")
public class GrievanceFaqController {

	@Autowired
	private GrievanceFaqService grievanceFaqService;

	@PreAuthorize(PermissionConstant.GRIEVANCE_FAQ)
	@RequestMapping(value = "/addFaq", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the Faq data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createFaq(@Valid @RequestBody GrievanceFaqRequestDTO gfaqRequestDto){
		GenericResponse objGenericResponse = grievanceFaqService.createFaq(gfaqRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PreAuthorize(PermissionConstant.GRIEVANCE_FAQ)
	@PutMapping("/updateGrievanceFaq")
	@ApiOperation(value = "This api is used to update grievanceFaq record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateTypeOfUser(@Valid @RequestBody GrievanceFaqRequestDTO requestDTO) {
		return new ResponseEntity<>(grievanceFaqService.updateTypeOfUser(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get grievanceFaq list by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(grievanceFaqService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all grievanceFaq records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(grievanceFaqService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PreAuthorize(PermissionConstant.GRIEVANCE_FAQ)
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(grievanceFaqService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active grievanceFaq records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(grievanceFaqService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@PostMapping("/Search")
	@ApiOperation(value = "This api is used to get grievanceFaq on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByTypeOfUser(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(grievanceFaqService.searchByTypeOfUser(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "This api is to get grievanceFaq list by issueDetailsId", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByIssueDetailsId(@RequestParam(value="issueDetailsId", required=true) Long issueDetailsId) {
		return new ResponseEntity<>(grievanceFaqService.getByIssueDetailsId(issueDetailsId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}




}
