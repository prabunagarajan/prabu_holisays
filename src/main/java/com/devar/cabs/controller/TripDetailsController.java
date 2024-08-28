package com.devar.cabs.controller;

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

import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.TripDetailsRequestDTO;
import com.devar.cabs.service.TripDetailsService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Trip Details", description = "This controller contain all  operation of Driver Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/tripDetails/")
public class TripDetailsController {
	
	@Autowired
	TripDetailsService tripDetailsService;
	
	@PostMapping("add")
	@ApiOperation(value = "This api is used to create a new DriverDetails", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse createDriverDetails(@RequestBody TripDetailsRequestDTO tripDetailsRequestDTO) {
		return tripDetailsService.add(tripDetailsRequestDTO);
	}

	@PutMapping("update")
	@ApiOperation(value = "This api is used to update existing DriverDetails", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse updateG6MasterData(@RequestBody TripDetailsRequestDTO tripDetailsRequestDTO) {
		return tripDetailsService.update(tripDetailsRequestDTO);
	}

	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get SiteVisit by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(tripDetailsService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all SiteVisit list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(tripDetailsService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(tripDetailsService.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getPendingList")
	@ApiOperation(value = "This api is to get all SiteVisit list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getPendingList() {
		return new ResponseEntity<>(tripDetailsService.getPendingList(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getLastRecordByV/{vehicleNumber}")
	@ApiOperation(value = "This api is to get SiteVisit by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getLastRecordByVehicleNumber(@PathVariable String vehicleNumber) {
		return new ResponseEntity<>(tripDetailsService.getLastRecordByVehicleNumber(vehicleNumber), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

}
