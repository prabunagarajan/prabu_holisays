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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.POSAssetApprovalRequestDTO;
import com.oasys.posasset.dto.POSAssetRequestDTO;
import com.oasys.posasset.service.POSAssetRequestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "POSAssetRequest", description = "This controller contains api to do all operation for POS Asset Request")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/pos-asset-request")
public class POSAssetRequestController {

	@Autowired
	private POSAssetRequestService posAssetRequestService;
	
	@PreAuthorize(PermissionConstant.POS_ASSET_REQUEST)
	@PostMapping
    @ApiOperation(value = "This api is used to add new POS asset request", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> save(@Valid @RequestBody POSAssetRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(posAssetRequestService.save(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get POS asset request by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(posAssetRequestService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/status")
	@ApiOperation(value = "This api is all POS asset request status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAssetRequestTypes() {
		return new ResponseEntity<>(posAssetRequestService.getAssetRequestTypes(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is all POS asset request status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByRequestFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(posAssetRequestService.getAllByRequestFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/approve")
    @ApiOperation(value = "This api is used to approve POS asset request", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> approve(@Valid @RequestBody POSAssetApprovalRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(posAssetRequestService.approve(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}
