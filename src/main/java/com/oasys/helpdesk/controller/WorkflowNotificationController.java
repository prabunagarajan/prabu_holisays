package com.oasys.helpdesk.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.WorkflowNotificationDTO;
import com.oasys.helpdesk.service.WorkflowNotificationService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

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

@RequestMapping("/workflownotification")
public class WorkflowNotificationController extends BaseController {
	
	
	
	@Autowired
	WorkflowNotificationService workFlowService;
	
	
	
	@GetMapping("/getBycategoryId/{categoryid}")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("categoryid") Long categoryid) {
		return new ResponseEntity<>(workFlowService.getById(categoryid), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

		
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addDevice(@Valid @RequestBody WorkflowNotificationDTO workflownotifyDTO) {
		return new ResponseEntity<>(workFlowService.save(workflownotifyDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/byRole")
	@ApiOperation(value = "This api will be used to find the user based on the role Id", notes = "Returns HTTP 200 if successful get the record")
	 public ResponseEntity<Object> getUserByRole(@RequestParam(value="roleId",required=true) Long roleId, @RequestParam(value="districtCode",required=false) String districtCode) {
		return new ResponseEntity<>(workFlowService.getUsersByRoleId(roleId, districtCode), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all level list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(workFlowService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(workFlowService.getsubPagesearchNewByFilterstock(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to update workflow details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody List<WorkflowNotificationDTO> workflownotifyDTO) {
		return new ResponseEntity<>(workFlowService.Workflowupdate(workflownotifyDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
}
