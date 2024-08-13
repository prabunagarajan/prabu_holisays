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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.AssetAccessoriesRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.AssetAccessoriesService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for AssetAccessories Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })


@RequestMapping("/AssetAccessories")
public class AssetAccessoriesController {
	
	@Autowired 
	private AssetAccessoriesService assetAccessoriesService;
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add assetAccessories details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addAssetAccessories(@Valid @RequestBody AssetAccessoriesRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(assetAccessoriesService.adddAssetAccessories(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/updateAssetAccessories")
	@ApiOperation(value = "This api is to edit AssetAccessories", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateAssetAccessories(@Valid @RequestBody AssetAccessoriesRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(assetAccessoriesService.updateAssetAccessories(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
    @ApiOperation(value = "This api is used to get AssetAccessories base on search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(assetAccessoriesService.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(assetAccessoriesService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all AssetAccessories Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(assetAccessoriesService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all AssetAccessories list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(assetAccessoriesService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view AssetAccessories by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(assetAccessoriesService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}


	
	

}
