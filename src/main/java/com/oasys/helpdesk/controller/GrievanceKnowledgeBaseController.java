package com.oasys.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.GrievanceKnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.GrievanceKnowledgeBaseService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of GrievanceKnowledgeBase")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/Grievanceknowledge")
public class GrievanceKnowledgeBaseController {

	@Autowired
	private GrievanceKnowledgeBaseService gkbService;

	@RequestMapping(value = "/addKnowledge", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to addKnowledge data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> createKnowledge(@RequestBody GrievanceKnowledgeBaseRequestDTO knowledgeRequestDto) {
		GenericResponse objGenericResponse = gkbService.createKnowledge(knowledgeRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/updateKnowledge", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to update knowledge data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateKnowledge(@RequestBody GrievanceKnowledgeBaseRequestDTO knowledgeRequestDto) {
		GenericResponse objGenericResponse = gkbService.updateKnowledge(knowledgeRequestDto);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/Search", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get by Category, SubCategory, IssueDetails, Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByCategoryAndIssueDetailsAndStatus(
			@RequestBody PaginationRequestDTO paginationRequestDTO) {
		GenericResponse objGenericResponse = gkbService.getByCategoryAndIssueDetailsAndStatus(paginationRequestDTO);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getallknowledgebase", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to getAllknowledgeBase data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllknowledgeBase() {
		GenericResponse objGenericResponse = gkbService.getAllKnowledgeBase();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getknowlegebaseCode")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getEntityCode() {
		return new ResponseEntity<>(gkbService.getUniqueCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/getknowledgebyid", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getKnowledgeById(@RequestParam("id") Long id) {
		GenericResponse objGenericResponse = gkbService.getKnowledgeBaseById(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getknowledgebyid/{categoryId}/{issueDetailsId}", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get knowledge base data based on category id and sub category id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getKnowledgeByCatAndIssueDId(@PathVariable("categoryId") Long categoryId,
			@PathVariable("issueDetailsId") Long issueDetailsId) {
		GenericResponse objGenericResponse = gkbService.getKnowledgeByCatAndIssueDId(categoryId, issueDetailsId);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "{id}/count", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to updated resolved count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateResolvedCount(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gkbService.updateResolvedCount(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/getsolutionbyissuedetailsid", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on issue details id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getSolutionByIssueDetailsId(@RequestParam("issueDetailsId") Long id) {
		GenericResponse objGenericResponse = gkbService.getSolutionByIssueDetailsId(id);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getGrievanceknowledgebasecountbystatus", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get user data based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getGrievanceKnowledgeBaseCountByStatus() {
		GenericResponse objGenericResponse = gkbService.getGrievanceKnowledgeBaseCountByStatus();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
}
