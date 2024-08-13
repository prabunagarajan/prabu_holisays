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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceSlaRequestDTO;
//import com.oasys.helpdesk.request.GrievanceSlaRequestDTO;
import com.oasys.helpdesk.service.GrievanceSlaService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of SLA")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/grievanceSla")
public class GrievanceSlaController {
	
	@Autowired
	private GrievanceSlaService grievanceSlaServvice;

	@PreAuthorize(PermissionConstant.GRIEVANCE_SLA)
	@RequestMapping(value = "/addSla", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to create the sla data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createSla(@RequestBody GrievanceSlaRequestDTO RequestDto) throws RecordNotFoundException, Exception {
		GenericResponse objGenericResponse = grievanceSlaServvice.createSla(RequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PreAuthorize(PermissionConstant.GRIEVANCE_SLA)
	@PutMapping("/updateGrievanceSla")
	@ApiOperation(value = "This api is used to update grievanceSla record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateSla(@Valid @RequestBody GrievanceSlaRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(grievanceSlaServvice.updateSla(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get grievanceSla list by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(grievanceSlaServvice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all grievanceSla records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(grievanceSlaServvice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active grievanceSla records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(grievanceSlaServvice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(grievanceSlaServvice.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/Search")
	@ApiOperation(value = "This api is used to get grievanceSla on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchBySla(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(grievanceSlaServvice.searchBySla(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("get/{CategoryId}/{IssueDetailsId}")
	@ApiOperation(value = "This api is to get Sla details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("CategoryId") Long categoryId,@PathVariable("IssueDetailsId") Long issueDetailsId) {
		return new ResponseEntity<>(grievanceSlaServvice.getById(categoryId,issueDetailsId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
