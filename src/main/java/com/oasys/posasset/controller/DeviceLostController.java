package com.oasys.posasset.controller;
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
import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.GrievanceOTPVerificationRequestDTO;
import com.oasys.helpdesk.request.OTPVerificationDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.GrievanceregService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.Devicelostrequestdto;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.service.DevicelostService;
import com.oasys.posasset.service.WorkFlowService;
import com.oasys.posasset.service.impl.DeviceReturnService;

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

@RequestMapping("/devicelost")
public class DeviceLostController {
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	private DevicelostService devicelostservice;
	
	@Autowired
	DeviceReturnService deviceReturnService;
	
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add device lost details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddevicelostPage(@Valid @RequestBody Devicelostrequestdto requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(devicelostservice.adddevicelost(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/addtest")
	@ApiOperation(value = "This api is used to add device lost details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddevicelostPageq(@Valid @RequestBody Devicelostrequestdto requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(devicelostservice.adddevicelost1(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
//	@GetMapping("/getlist")
//	@ApiOperation(value = "This api is to get all devicelost list", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getAll() {
//		return new ResponseEntity<>(devicelostservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
//				HttpStatus.OK);
//	}
	
	@GetMapping("getListByDesignationCode/{designationCode}")
	@ApiOperation(value = "This api is used to get Payment And Reconciliation Comments by Application Number", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getListByDesignationCode(@PathVariable("designationCode") String designationCode) {
		return new ResponseEntity<>(devicelostservice.getBydesignationCode(designationCode), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("getListByUserId/{userId}")
	@ApiOperation(value = "This api is used to get Payment And Reconciliation Comments by Application Number", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getListByUserId(@PathVariable("userId") Long userId) {
		return new ResponseEntity<>(devicelostservice.getByUserId(userId), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
//	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view devicelost by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(devicelostservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit devicelost", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDepartment(@Valid @RequestBody Devicelostrequestdto requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(devicelostservice.update(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	


	@PostMapping("/updateWorkFlow")
	@ApiOperation(value = "This api is used for bottling repair WorkFLow", notes = "Returns HTTP 200 if successful get the record")
	public Boolean updateWorkFlowDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
			throws RecordNotFoundException, Exception {

		return workFlowService.updateWorkFlowDetails(workflowStatusUpdateDto);
	}
	
	@GetMapping("/getApplicationNo")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getApplicationNo() {
		return new ResponseEntity<>(deviceReturnService.generateAppNodevicelost(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(
				devicelostservice.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "This api to Approval", notes = "Returns HTTP 200 if successful get the record")
	@PutMapping(value = "/approval")
	public ResponseEntity<Object> updateApproval(@RequestBody ApprovalDTO approvalDto) throws Exception {

		return new ResponseEntity<>(devicelostservice.updateApproval(approvalDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	
	@ApiOperation(value = "This api to licno,complaintno ticket open checking", notes = "Returns HTTP 200 if successful get the record")
	@PutMapping(value = "/verifylicnocomplaintno")
	public ResponseEntity<Object> verifylicenceno(@RequestBody Devicelostrequestdto Devicelostrequestdto) throws Exception {

		return new ResponseEntity<>(devicelostservice.licnoverify(Devicelostrequestdto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	
	
	
}


