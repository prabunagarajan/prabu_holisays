package com.oasys.posasset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Advance Duty API", description = "This controller contain all  operation of EntityType Master")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RestController
//@RequestMapping(value = "/helpdeskWorkFlow")
public class WorkFlowController {

//	@Autowired
//	WorkFlowService workFlowService;

	@PostMapping("/helpdeskWorkFlow/updateWorkFlow")
	@ApiOperation(value = "This api is used for bottling repair WorkFLow", notes = "Returns HTTP 200 if successful get the record")
	public Boolean updateWorkFlowDeviceDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
			throws RecordNotFoundException, Exception {

//		return workFlowService.updateWorkFlowDetails(workflowStatusUpdateDto);
		return true;
	}
	
//	@PostMapping("/changerequest/updateWorkFlow")
//	@ApiOperation(value = "This api is used for bottling repair WorkFLow", notes = "Returns HTTP 200 if successful get the record")
//	public Boolean updateWorkFlowDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
//			throws RecordNotFoundException, Exception {
//		return workFlowService.updateWorkFlowDetailschangerequest(workflowStatusUpdateDto);
//	}

//	@ApiOperation(value = "work flow", response = CBaseDTO.class)
//	@PostMapping("/create")
//	public ResponseEntity<Object> addAdvanceDuty(@RequestBody WorkflowDTO workflowStatusUpdateDto) throws Exception {
//
//		return new ResponseEntity<>(workFlowService.callWorkFlowService(workflowStatusUpdateDto),
//				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//
//	}

}
