package com.oasys.helpdesk.controller;

import java.util.List;

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

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ShopcodeDTO;
import com.oasys.helpdesk.service.ShopcodeMasterService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Department Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/shopcodemaster")
public class ShopcodeController extends BaseController {
	
	@Autowired
	private ShopcodeMasterService shopmasterservice;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add shopcode details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddticketPage(@Valid @RequestBody List<ShopcodeDTO> requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(shopmasterservice.add(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view shopcode by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(shopmasterservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search shopcode record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(
				shopmasterservice.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to add shopcode details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateticketPage(@Valid @RequestBody List<ShopcodeDTO> requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(shopmasterservice.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@PostMapping("/mappingupdate")
	@ApiOperation(value = "This api is used to get mapping update ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddticketPage(@Valid @RequestBody ShopcodeDTO requestDTO)
			throws RecordNotFoundException, Exception {
	return new ResponseEntity<>(shopmasterservice.updatemapping(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

	@PostMapping("/shopcodebaseduser")
	@ApiOperation(value = "This api is used to get userdetail ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> shopcodebaseduser(@Valid @RequestBody ShopcodeDTO requestDTO)
			throws RecordNotFoundException, Exception {
	return new ResponseEntity<>(shopmasterservice.shopcodebasedUserDetails(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/swfielduser")
	@ApiOperation(value = "This api is used to get userdetail ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> softwarefileduser()
			throws RecordNotFoundException, Exception {
	return new ResponseEntity<>(shopmasterservice.softwarefileddUserDetails(),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/swmoduleuser")
	@ApiOperation(value = "This api is used to get software module user detail ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> softwaremoduleuser()
			throws RecordNotFoundException, Exception {
	return new ResponseEntity<>(shopmasterservice.softwaremoduleUserDetails(),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
}
