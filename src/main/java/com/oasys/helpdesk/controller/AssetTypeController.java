package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.AssetTypeService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Asset Type")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/asset-type")
public class AssetTypeController {
	
	@Autowired
	private AssetTypeService assetTypeService;
	
	@GetMapping
	@ApiOperation(value = "This api is to get all Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(assetTypeService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive(@RequestParam(value="assetBrandId",required =false) Long assetBrandId) {
		return new ResponseEntity<>(assetTypeService.getAllActive(assetBrandId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all active Asset Types", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActiveq() {
		return new ResponseEntity<>(assetTypeService.getAllActiveQ(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "This api is to get Asset Type by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(assetTypeService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ASSET_TYPE)
	@PostMapping
    @ApiOperation(value = "This api is used to add Asset Type", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addAssetType(@Valid @RequestBody AssetTypeRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(assetTypeService.addAssetType(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ASSET_TYPE)
	@PutMapping
    @ApiOperation(value = "This api is used to add Asset Type", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateAssetType(@Valid @RequestBody AssetTypeRequestDTO requestDTO) throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(assetTypeService.updateAssetType(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	 
	@PostMapping("/search")
    @ApiOperation(value = "This api is used to get Asset Types base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(assetTypeService.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_ASSET_TYPE)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException,ParseException {
		return new ResponseEntity<>(assetTypeService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}
