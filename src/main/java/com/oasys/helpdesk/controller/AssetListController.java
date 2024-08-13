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
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.AssetListRequestDto;
import com.oasys.helpdesk.dto.AssetReportRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.AssetListService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Asset List")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/assetlist")
public class AssetListController {
	
	@Autowired
	AssetListService assetListService;
	
	@PostMapping("/addassetlist")
	@ApiOperation(value = "This api is used to add asset list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addAssetType(@Valid @RequestBody AssetListRequestDto requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(assetListService.addAssetlist(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view assetlist by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(assetListService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	
	@PutMapping("/assetlistupdate")
	@ApiOperation(value = "This api is used to update asset list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateAssetType(@Valid @RequestBody AssetListRequestDto requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(assetListService.updateAssetlist(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

	@GetMapping("getByassetbrandname/{typeid}")
	@ApiOperation(value = "This api is to get view assetbrand asset name id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByassetbranassetname(@PathVariable("typeid") Long typeid) {
		return new ResponseEntity<>(assetListService.getBybrandandnameId(typeid), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@GetMapping("getByassetsubtype/{assetnameid}")
	@ApiOperation(value = "This api is to get view assetsubtype  by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByassetsubtype(@PathVariable("assetnameid") Long assetnameid) {
		return new ResponseEntity<>(assetListService.getBysubtypeId(assetnameid), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to aseet list details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(assetListService.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active Entity records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(assetListService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PostMapping(value = "/assetreport")
	@ApiOperation(value = "This API is to get asset and assetmap list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> assetReport(@Valid @RequestBody AssetReportRequestDTO requestDTO) {
	    return new ResponseEntity<>(assetListService.assetReport(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}
