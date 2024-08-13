package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.ChangeRequestFeaturesDTO;
import com.oasys.helpdesk.dto.RoleMasterResponseDTO;
import com.oasys.helpdesk.service.ChangeRequestFeaturesService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of AssetMap")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/masterchangerequestfeatures")
public class ChangeRequestFeaturesController extends BaseController {

	@Autowired
	ChangeRequestFeaturesService changerequestfeaturesservice;

	@GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active Change request features records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(changerequestfeaturesservice.getAllMasterActive(),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/addmasterchangerequest")
	@ApiOperation(value = "This api is used to add code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addMasterChangeRequestCode(
			@RequestBody ChangeRequestFeaturesDTO changerequestfeaturesDTO) {
		return new ResponseEntity<>(changerequestfeaturesservice.addMasterChangeRequest(changerequestfeaturesDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/updatemasterchangerequest")
	@ApiOperation(value = "This api is to Update change Request", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateMasterChangeRequest(
			@Valid @RequestBody ChangeRequestFeaturesDTO changerequestfeaturesDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(changerequestfeaturesservice.updateMasterchangerequest(changerequestfeaturesDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
}
