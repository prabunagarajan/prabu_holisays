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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.AccessoriesAddRequestDTO;
import com.oasys.helpdesk.dto.AccessoriesRequestDTO;
import com.oasys.helpdesk.dto.DeviceHardwareAddRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.AccessoriesNameService;
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

@RequestMapping("/accessories")
public class AccessoriesNameController {
	@Autowired
	private AccessoriesNameService accessoriesNameService;

	@PostMapping("/addaccessories")
	@ApiOperation(value = "This api is used to add Accessories", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addAssetType(@Valid @RequestBody AccessoriesRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(accessoriesNameService.addAccessoriesName(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is used to update Accessories record", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody AccessoriesRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(accessoriesNameService.updateDevice(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to get Accessories record based on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(accessoriesNameService.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get Asset Brand by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(accessoriesNameService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all only Accessories records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(accessoriesNameService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/AccessoriesCode")
	@ApiOperation(value = "This api is used to get  unique code for AccessoriesCode", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(accessoriesNameService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to get unique code for AccessoriesList", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getDeviceId(@Valid @RequestBody AccessoriesAddRequestDTO payload)	throws JsonParseException,ParseException 
	{
		return new ResponseEntity<>(accessoriesNameService.getDeviceId(payload), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Accessories records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActiveList() {
		return new ResponseEntity<>(accessoriesNameService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/activeList")
	@ApiOperation(value = "This api is to get all active Accessories List records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(accessoriesNameService.getAllListActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getAllList")
	@ApiOperation(value = "This api is to get all AccessoriesList records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllList() {
		return new ResponseEntity<>(accessoriesNameService.getAllList(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/{AccessoriesCode}")
	@ApiOperation(value = "This api is to get Accessories by deviceCode", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByaccessCode(@PathVariable("AccessoriesCode") String AccessoriesCode) {
		return new ResponseEntity<>(accessoriesNameService.getByaccessCode(AccessoriesCode), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	

}
