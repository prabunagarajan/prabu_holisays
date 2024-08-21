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
import com.oasys.helpdesk.dto.ChangereqCountRequestDTO;
import com.oasys.helpdesk.dto.ChangereqRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.ChangerequestService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Category")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/changerequest")
public class ChangeRequestController extends BaseController {

	@Autowired
	ChangerequestService changereqservice;

//	@Autowired
//	WorkFlowService workFlowService;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to changereq ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody ChangereqRequestDTO requestDto) throws ParseException {
		return new ResponseEntity<>(changereqservice.addchnagerequest(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("id") Long id) throws Exception {
		return new ResponseEntity<>(changereqservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search change request using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(changereqservice.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/updateWorkFlow")
	@ApiOperation(value = "This api is used for bottling repair WorkFLow", notes = "Returns HTTP 200 if successful get the record")
	public Boolean updateWorkFlowDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
			throws RecordNotFoundException, Exception {
		return changereqservice.updateChangeRequestWorkFlowDetails(workflowStatusUpdateDto);
	}

	@ApiOperation(value = "This api to Approval", notes = "Returns HTTP 200 if successful get the record")
	@PutMapping(value = "/approval")
	public ResponseEntity<Object> updateApproval(@RequestBody ApprovalDTO approvalDto) throws Exception {

		return new ResponseEntity<>(changereqservice.updateApproval(approvalDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is used to add device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody ChangereqRequestDTO requestDTO) {
		return new ResponseEntity<>(changereqservice.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PutMapping("changereqstatusupdate")
	@ApiOperation(value = "This api is used to update vendor status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> changereqStatusUpdate(@Valid @RequestBody ApprovalDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(changereqservice.changereqStatusUpdate(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/changereqcount")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> changereqCount(@Valid @RequestBody ChangereqCountRequestDTO requestDto)
			throws ParseException {
//		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(changereqservice.changereqCount(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/summarycount", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get Count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount() {
		GenericResponse objGenericResponse = changereqservice.getCount();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/getLogsByApplicationNo/{applicationNo}")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getLogsByApplicationNo(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(changereqservice.getLogByApplicationNo(applicationNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping("/draftcall")
	@ApiOperation(value = "This api is used to changereq ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> draftcall(@Valid @RequestBody ChangereqRequestDTO requestDto) throws ParseException {
		return new ResponseEntity<>(changereqservice.draftcall(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
