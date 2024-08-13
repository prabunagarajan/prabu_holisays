package com.oasys.posasset.controller;

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

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.service.impl.EALRequestPUtoBWFLService;

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

@RequestMapping("/ealrequestputobwfl")
public class EALRequestPUtoBWFLController {
	
	@Autowired 
	EALRequestPUtoBWFLService ealRequestPUtoBWFLService;
	
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add device lost details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddevicelostPage(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(ealRequestPUtoBWFLService.save(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to add device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO) {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getByealrequest/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByealrequest(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getByealid(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all Ticketstatus list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAllByUserId/{userId}")
	@ApiOperation(value = "This api is to get all eal details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByUserId(@PathVariable("userId") Long userId) {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getAllByUserId(userId), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getLogsByApplicationNo/{applicationNo}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getLogsByApplicationNo(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getLogByApplicationNo(applicationNo),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
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

		return new ResponseEntity<>(ealRequestPUtoBWFLService.updateApproval(approvalDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	@PostMapping("/forceclosure")
	@ApiOperation(value = "This api is forceclosure", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> forceclosure(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.forceclosureupdate(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAllByapproved")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByDesignationCode() {
		return new ResponseEntity<>(ealRequestPUtoBWFLService.getAllByapproved(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
}
