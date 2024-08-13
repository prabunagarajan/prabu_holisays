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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.GrievanceTicketStatusDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.GrievanceOTPVerificationRequestDTO;
import com.oasys.helpdesk.request.OTPVerificationDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.GrievanceregService;
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

@RequestMapping("/grievancereg")
public class GrievanceRegController extends BaseController {

	@Autowired
	private GrievanceregService grievanceservice;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add ticket status details testing purpose ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddticketPage(@Valid @RequestBody GrievanceRegRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(grievanceservice.addreg(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all Ticketstatus list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(grievanceservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/toglelist", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getfieldeng(@Valid @RequestBody GrievanceRegRequestDTO requestDto) throws Exception {
		return new ResponseEntity<>(grievanceservice.getBytoglelist(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("getById/{id}")
	@ApiOperation(value = "This api is to get view Ticketstatus by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(grievanceservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/allactive")
	@ApiOperation(value = "This api is to get all Ticket Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(grievanceservice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(grievanceservice.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/codetypeofuser", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getcodeoftypeofuser(@Valid @RequestBody GrievanceRegRequestDTO requestDto)
			throws Exception {
		return new ResponseEntity<>(grievanceservice.getCodetypeofuser(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/count")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount(@Valid @RequestBody GrievanceRegRequestDTO requestDto)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getCount(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit Ticketstatus", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDepartment(@Valid @RequestBody GrievanceRegRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(grievanceservice.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/dashboardcount")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount_percentage() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getCount_percentage(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/dashboardbymonth")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getbyMonth() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getbyMonth(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/updateinspectex")
	@ApiOperation(value = "This api is used to update exciseofficer,Inspecting officer", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> inspectingexcise(@Valid @RequestBody GrievanceRegRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(grievanceservice.updateinspectex(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/commonsearch")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getcommonserach(@Valid @RequestBody GrievanceRegRequestDTO requestDto)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getBycommonserach(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	// Dashboard API for excise officer & Inspec

	@PostMapping("/dashboardcountinspect")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status inspection  officer or ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount_percentageinspect(@Valid @RequestBody GrievanceRegRequestDTO requestDto)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getCount_percentageinspect(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/dashboardbymonthinspect")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getbyMonthinspect(@Valid @RequestBody GrievanceRegRequestDTO requestDto)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getbyMonthinspect(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/generate-otp", method = RequestMethod.POST)
	// @PreAuthorize(PermissionConstant.GRIEVANCE_REGISTERATION)
	@ApiOperation(value = "This api is used to send otp on SMS", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> generateOTP(
			@RequestParam(value = "phoneNumber", required = true) String phoneNumber) {
		return new ResponseEntity<>(grievanceservice.sendOTP(phoneNumber), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/verify/otp")
	// @PreAuthorize(PermissionConstant.GRIEVANCE_REGISTERATION)
	@ApiOperation(value = "This api is used to verify otp", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> verifyOTP(@Valid @RequestBody GrievanceOTPVerificationRequestDTO otpVerificationDTO) {
		return new ResponseEntity<>(grievanceservice.verifyOTP(otpVerificationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/flaglist", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on assign list flag 0 only", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getfieldengflg(@Valid @RequestBody GrievanceRegRequestDTO requestDto)
			throws Exception {
		return new ResponseEntity<>(grievanceservice.FlagLIstAPI(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/flagupdate", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on assign list flag 0 only", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> FlagUpdate(@Valid @RequestBody GrievanceRegRequestDTO requestDto) throws Exception {
		return new ResponseEntity<>(grievanceservice.UpdateFlag(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	/*
	 * View List API
	 */

	@RequestMapping(value = "/viewlist", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get from date to date based on list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> ViewList(@Valid @RequestBody GrievanceRegRequestDTO requestDto) throws Exception {
		return new ResponseEntity<>(grievanceservice.ViewList(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/getisthandlingoff", method = RequestMethod.POST)
	@ApiOperation(value = "This api is to get all grievance handling officer details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByAll(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		return new ResponseEntity<>(grievanceservice.ListByAll(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/grievancedashboardcountftdate")
	@ApiOperation(value = "This api is used to grievance dashboard", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getgrievancedashboardcount(@Valid @RequestBody CreateTicketRequestDto requestDto)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(
				grievanceservice.getCount_percentage_grievance_dashboardprocess(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/grievancedashboardbymonth")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getbygrievanceDashboardByMonth() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(grievanceservice.getbyGrievanceDashboardByMonth(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	//@PreAuthorize(PermissionConstant.HELP_DESK_TICKET_STATUS)
	@PostMapping("/addgrievanceticketstatus")
	@ApiOperation(value = "This api is used to add grievance ticket status details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addGrievanceTicketPage(@Valid @RequestBody GrievanceTicketStatusDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(grievanceservice.addGrievanceTicketStatus(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/allactivegrievancestatus")
	@ApiOperation(value = "This api is to get all grievance Ticket Status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getGrievanceAllActive() {
		return new ResponseEntity<>(grievanceservice.getGrievanceAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
