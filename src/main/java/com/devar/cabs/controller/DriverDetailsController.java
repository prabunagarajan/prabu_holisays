package com.devar.cabs.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

import com.devar.cabs.requestDTO.DriverDetailsRequestDTO;
import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.service.DriverDetailsService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Driver Details", description = "This controller contain all  operation of Driver Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/DriverDetails/")
public class DriverDetailsController {

	@Autowired
	DriverDetailsService driverDetailsService;

	@PostMapping("add")
	@ApiOperation(value = "This api is used to create a new DriverDetails", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse createDriverDetails(@RequestBody DriverDetailsRequestDTO driverDetailsRequestDTO) {
		return driverDetailsService.add(driverDetailsRequestDTO);
	}

	@PutMapping("update")
	@ApiOperation(value = "This api is used to update existing DriverDetails", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse updateDriverDetails(@RequestBody DriverDetailsRequestDTO driverDetailsRequestDTO) {
		return driverDetailsService.update(driverDetailsRequestDTO);
	}

	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get SiteVisit by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(driverDetailsService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all SiteVisit list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(driverDetailsService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(driverDetailsService.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(driverDetailsService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	 @PostMapping("/import")
	    public String importExcelData(@RequestParam("file") MultipartFile file) {
	        try {
	            driverDetailsService.importExcelData(file);
	            return "Data imported successfully";
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	            return "Failed to import data: " + e.getMessage();
	        }
	    }
}
