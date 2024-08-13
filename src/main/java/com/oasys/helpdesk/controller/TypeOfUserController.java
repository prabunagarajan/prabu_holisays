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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TypeOfUserRequestDTO;
import com.oasys.helpdesk.service.TypeOfUserService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for typeOfUser")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/typeOfUser")
public class TypeOfUserController {

	@Autowired
	private TypeOfUserService typeOfUserService;

	@PreAuthorize(PermissionConstant.GRIEVANCE_MASTER_TYPE_OF_USER)
	@PostMapping("/addTypeOfUser")
	@ApiOperation(value = "This api is used to add typeOfUser data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addTypeOfUser(@Valid @RequestBody TypeOfUserRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(typeOfUserService.addTypeOfUser(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@PostMapping("/Search")
	@ApiOperation(value = "This api is used to get typeOfUser on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByTypeOfUser(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(typeOfUserService.searchByTypeOfUser(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PreAuthorize(PermissionConstant.GRIEVANCE_MASTER_TYPE_OF_USER)
	@PutMapping("/updateTypeOfUser")
	@ApiOperation(value = "This api is used to update TypeOfUser record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateTypeOfUser(@Valid @RequestBody TypeOfUserRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(typeOfUserService.updateTypeOfUser(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getByTypeOfUser")
	@ApiOperation(value = "This api is to get by type of user list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByTypeOfUser(@RequestParam("typeOfUser") String typeOfUser) {
		GenericResponse objGenericResponse = typeOfUserService.getByTypeOfUser(typeOfUser);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get typeofuser list by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(typeOfUserService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all TypeOfUser records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(typeOfUserService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(typeOfUserService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active TypeOfUser records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(typeOfUserService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}



}
