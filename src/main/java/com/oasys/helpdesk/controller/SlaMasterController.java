package com.oasys.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.SlaMasterRequestDto;
import com.oasys.helpdesk.service.SlaMasterService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Action Taken")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/sla")
public class SlaMasterController extends BaseController {
	
	@Autowired
	SlaMasterService slaMasterService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get sla master data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getIssueFromList() {
		//GenericResponse objGenericResponse = issueFromService.getIssueFromList();
		return new ResponseEntity<>(slaMasterService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_SLA)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the sla", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody SlaMasterRequestDto slaMasterRequestDto)  {
	//	GenericResponse objGenericResponse = issueFromService.createIssueFrom(issueRequestDto);
		return new ResponseEntity<>(slaMasterService.create(slaMasterRequestDto), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get Sla details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(slaMasterService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_SLA)
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to edit sla details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody SlaMasterRequestDto requestDTO) {
		return new ResponseEntity<>(slaMasterService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELP_DESK_SLA)
	@GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode()  {
		return new ResponseEntity<>(slaMasterService.getCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Object> search(@RequestBody PaginationRequestDTO paginationRequestDTO)  {
    GenericResponse objGenericResponse = slaMasterService.searchByFilter(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@GetMapping("/active")
	@ApiOperation(value = "This api is to get all active sla details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(slaMasterService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/get/{subCategoryId}/{categoryId}")
	@ApiOperation(value = "This api is to get Sla details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("subCategoryId") Long subCategoryId,@PathVariable("categoryId") Long categoryId) {
		return new ResponseEntity<>(slaMasterService.getById(subCategoryId,categoryId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getSla/{categoryId}/{subcategoryId}/{issueDetailsId}")
	@ApiOperation(value = "This api is to get sla details based on category, subcategory and issue details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSla(@PathVariable("categoryId") Long categoryId,@PathVariable("subcategoryId") Long subcategoryId,@PathVariable("issueDetailsId") Long issueDetailsId) {
		return new ResponseEntity<>(slaMasterService.getSla(categoryId,subcategoryId,issueDetailsId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
