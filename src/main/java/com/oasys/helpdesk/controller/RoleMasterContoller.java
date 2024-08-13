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
import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.RoleMasterResponseDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.RoleMasterService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Role master", description = "This controller contain api to get all roles")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/role-master")
public class RoleMasterContoller extends BaseController {

	@Autowired
	private RoleMasterService roleMasterService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get roles listing", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll(
			@RequestParam(value = "helpDeskRoleRequired", required = false) Boolean helpDeskRoleRequired,
			@RequestParam(value = "defaultRoleRequired", required = false) Boolean defaultRoleRequired) {
		return new ResponseEntity<>(roleMasterService.getAll(helpDeskRoleRequired, defaultRoleRequired),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search roles records based on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {

		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(roleMasterService.search(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "This api is to get all roles listing", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> get() {
		return new ResponseEntity<>(roleMasterService.get(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/addrolemaster")
	@ApiOperation(value = "This api is used to add role code ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addRoleCode(@RequestBody RoleMasterResponseDTO paginationRequestdto) {
		return new ResponseEntity<>(roleMasterService.addRoleCode(paginationRequestdto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get roles by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(roleMasterService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PutMapping("/updateRoleMaster")
	@ApiOperation(value = "This api is to Update RoleMaster", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateEntity(@Valid @RequestBody RoleMasterResponseDTO rolemasterresponcedto)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(roleMasterService.updateRoleMaster(rolemasterresponcedto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}
