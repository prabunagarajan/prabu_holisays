package com.oasys.helpdesk.controller;

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

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SupplierDTO;
import com.oasys.helpdesk.service.SupplierServiceImpl;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Accessories")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/supplier")
public class SupplierController {
	
	@Autowired
	SupplierServiceImpl supplierService;
	

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search supplier record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(supplierService.getsubPagesearchNewByFilterstock(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add supplier details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addDevice(@Valid @RequestBody SupplierDTO supplierDTO) {
		return new ResponseEntity<>(supplierService.save(supplierDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get all supplier details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(supplierService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to supllier details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody SupplierDTO supplierDTO) {
		return new ResponseEntity<>(supplierService.update(supplierDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active supllier records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(supplierService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	

}
