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
import com.devar.cabs.requestDTO.VehicleDetailsRequestDTO;
import com.devar.cabs.service.VehicleDetailsService;
import com.devar.cabs.utility.GenericResponse;
import com.devar.cabs.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Vehicle Details", description = "This controller contain all  operation of Vehicle Details")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/vehicleDetails/")
public class VehicleDetailsController {

	@Autowired
	VehicleDetailsService vehicleDetailsService;

	@PostMapping("add")
	@ApiOperation(value = "This api is used to create a new Vehicle Details", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse createDriverDetails(@RequestBody VehicleDetailsRequestDTO vehicleDetailsRequestDTO) {
		return vehicleDetailsService.add(vehicleDetailsRequestDTO);
	}

	@PutMapping("update")
	@ApiOperation(value = "This api is used to update existing Vehicle Details", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse updateG6MasterData(@RequestBody VehicleDetailsRequestDTO vehicleDetailsRequestDTO) {
		return vehicleDetailsService.update(vehicleDetailsRequestDTO);
	}

	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get Vehicle  by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(vehicleDetailsService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all Vehicle list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(vehicleDetailsService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search Vehicle record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(vehicleDetailsService.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Vehicle", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(vehicleDetailsService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getNextDate")
	@ApiOperation(value = "This api is to get all Vehicle list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getNextDate() {
		return new ResponseEntity<>(vehicleDetailsService.getNextDate(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
}
