package com.oasys.posasset.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestDashboardDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.service.WorkFlowService;
import com.oasys.posasset.service.impl.EALRequestAECService;

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

@RequestMapping("/ealrequest/aec")
public class EALRequestAECController {
	
	@Autowired
	EALRequestAECService ealRequestAECService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add EAL  details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddevicelostPage(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(ealRequestAECService.save(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to update EAL AEC details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO) {
		return new ResponseEntity<>(ealRequestAECService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get by id eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealRequestAECService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getByealrequest/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByealrequest(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealRequestAECService.getByealid(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all eal list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(ealRequestAECService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(ealRequestAECService.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAllByUserId/{userId}")
	@ApiOperation(value = "This api is to get all by user id in eal details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByUserId(@PathVariable("userId") Long userId) {
		return new ResponseEntity<>(ealRequestAECService.getAllByUserId(userId), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
    @GetMapping("/getLogsByApplicationNo")
    @ApiOperation(value = "Get all EAL return details", notes = "Returns HTTP 200 if successful")
    public ResponseEntity<GenericResponse> getLogsByApplicationNo(@RequestParam(name ="applicationNo", required = true) String applicationNo) {
        GenericResponse response = ealRequestAECService.getLogByApplicationNo(applicationNo);
        return new ResponseEntity<>(response, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

//	@PostMapping("/ealdasboard")
//	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getmaindashboard(@Valid @RequestBody EalRequestDTO requestDTO) throws ParseException {
//		return new ResponseEntity<>(ealRequestPUtoBWFLService.getmaindashboard(requestDTO),
//				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	@ApiOperation(value = "This api to Approval", notes = "Returns HTTP 200 if successful get the record")
	@PutMapping(value = "/approval")
	public ResponseEntity<Object> updateApproval(@RequestBody ApprovalDTO approvalDto) throws Exception {

		return new ResponseEntity<>(ealRequestAECService.updateApproval(approvalDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	@PostMapping("/forceclosure")
	@ApiOperation(value = "This api is forceclosure", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> forceclosure(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(ealRequestAECService.forceclosureupdate(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAllByapproved")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByDesignationCode() {
		return new ResponseEntity<>(ealRequestAECService.getAllByapproved(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PostMapping("/updateWorkFlow")
	@ApiOperation(value = "This api is used for bottling repair WorkFLow", notes = "Returns HTTP 200 if successful get the record")
	public Boolean updateWorkFlowDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
			throws RecordNotFoundException, Exception {
		return workFlowService.updateEALAECWorkFlowDetails(workflowStatusUpdateDto);
	}
	
	@GetMapping("/getByAppliNo")
	@ApiOperation(value = "This api is to get by id eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByAppliNo(@RequestParam(name ="applicationNo", required = true)String applicationNo) {
		return new ResponseEntity<>(ealRequestAECService.getByAppliNo(applicationNo), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	

}
