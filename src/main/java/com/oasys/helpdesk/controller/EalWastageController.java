package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.EalWastageDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.EalWastageService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/eal/wastage")
public class EalWastageController {

	@Autowired
	private EalWastageService service;

//	@Autowired
//	WorkFlowService workFlowService;

	@PostMapping("/list")
	@ApiOperation(value = "This api is used to get EalWastage records based on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllWastage(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(service.getBySearchFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@GetMapping("/{id}")
	@ApiOperation(value = "This api is used to get EalWastage record by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getWastageById(@PathVariable Long id) {
		return new ResponseEntity<>(service.getWastageById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping
	@ApiOperation(value = "This api is used to create EalWastage record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createWastage(@RequestBody EalWastageDTO wastage) {
		return new ResponseEntity<>(service.createWastage(wastage), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);

	}

	@PutMapping("/{id}")
	@ApiOperation(value = "This api is used to update existing EalWastage record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateWastage(@PathVariable Long id, @RequestBody EalWastageDTO updatedWastage) {
		return new ResponseEntity<>(service.updateWastage(id, updatedWastage),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "This api is used to delete existing EalWastage record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> deleteWastage(@PathVariable Long id) {
		return new ResponseEntity<>(service.deleteWastage(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);

	}

	@PutMapping("/status/{id}")
	@ApiOperation(value = "This api is used to update status existing EalWastage record by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateWastage(@PathVariable Long id, @RequestParam ApprovalStatus status) {
		return new ResponseEntity<>(service.updateStatus(id, status), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);

	}

	@GetMapping("/inprogresslist")
	@ApiOperation(value = "This api is used to get EalWastage record by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getInprogressList() {
		return new ResponseEntity<>(service.getInprogressList(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

//	@PostMapping("/updateWastageWorkFlow")
//	@ApiOperation(value = "This api is used for update eal Wastage WorkFlow", notes = "Returns HTTP 200 if successful get the record")
//	public Boolean updateWorkFlowDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
//			throws RecordNotFoundException, Exception {
//		return workFlowService.updateEALWastageWorkFlowDetails(workflowStatusUpdateDto);
//	}
	@GetMapping("/getBottelingPlanId")
    @ApiOperation(value = "Get alleal Wastag details", notes = "Returns HTTP 200 if successful")
    public ResponseEntity<GenericResponse> getBottlePlanId(@RequestParam(name ="bottelingPlanId", required = true) String bottelingPlanId) {
        GenericResponse response = service.getBottlePlanId(bottelingPlanId);
        return new ResponseEntity<>(response, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

}