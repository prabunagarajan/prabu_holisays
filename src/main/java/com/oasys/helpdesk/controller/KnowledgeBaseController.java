package com.oasys.helpdesk.controller;

import com.oasys.helpdesk.dto.KnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.KnowledgeService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//This is using spring security 
//based on the scope Domain can access  this controller 
//its access and right give the Domain module ,this call goes Domain module 
//@PreAuthorize("#oauth2.hasScope('Admin') or #oauth2.hasScope('Merchant') ")

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of KnowledgeBase")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/helpdeskknowledge")
public class KnowledgeBaseController {
	@Autowired
	KnowledgeService helpDeskKnowledgeService;

	@RequestMapping(value = "/getallknowledgebase", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllknowledgeBase data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllknowledgeBase() {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.getAllKnowledgeBase();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getallknowledgebasesolutions", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAll Knowledge Based Solutions data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllKnowledgeBaseSolutions() {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.getKnowledgeBaseSolutions();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@RequestMapping(value = "/Search", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get by Category, SubCategory, IssueDetails, Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByCategoryAndSubCategoryAndIssueDetailsAndStatus(@RequestBody PaginationRequestDTO paginationRequestDTO) {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.getByCategoryAndSubCategoryAndIssueDetailsAndStatus(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/addKnowledge", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to addKnowledge data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createKnowledge(@RequestBody KnowledgeBaseRequestDTO knowledgeRequestDto) {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.createKnowledge(knowledgeRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateKnowledge", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to update knowledge data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateKnowledge(@RequestBody KnowledgeBaseRequestDTO knowledgeRequestDto) {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.updateKnowledge(knowledgeRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getknowlegebaseid")
	@ApiOperation(value = "This api is used to get unique KB id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getEntityCode() {
		return new ResponseEntity<>(helpDeskKnowledgeService.getUniqueKbId(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getknowledgebyid", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getKnowledgeById(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.getKnowledgeBaseById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@RequestMapping(value = "/getknowledgebasecountbystatus", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getKnowledgeBaseCountByStatus() {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.getKnowledgeBaseCountByStatus();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "{id}/count", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to updated resolved count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateResolvedCount(@PathVariable("id") Long id) {
		return new ResponseEntity<>(helpDeskKnowledgeService.updateResolvedCount(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getsolutionbyissuedetailsid", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on issue details id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSolutionByIssueDetailsId(@RequestParam("issueDetailsId") Long id) {
		GenericResponse objGenericResponse = helpDeskKnowledgeService.getSolutionByIssueDetailsId(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

}