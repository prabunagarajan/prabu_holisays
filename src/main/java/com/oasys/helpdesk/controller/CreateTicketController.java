package com.oasys.helpdesk.controller;

import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO2;
import com.oasys.helpdesk.dto.TicketcounRequest;
import com.oasys.helpdesk.request.CreateTicketDashboardDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.CreateTicketRequestDto2;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.CreateTicketService;
import com.oasys.helpdesk.service.HelpdeskTicketAuditService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Category")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ticket")
public class CreateTicketController extends BaseController {

	@Autowired
	CreateTicketService createTicketService;

	@Autowired
	private HelpdeskTicketAuditService helpdeskTicketAuditService;

	@RequestMapping(value = "/getAll/{type}", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get all ticket details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll(@PathVariable("type") String type) {
		return new ResponseEntity<>(createTicketService.getAll(type), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/helpdeskTracker/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get ticket details based on status id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByStatusId(@PathVariable("id") Long id) {
		return new ResponseEntity<>(createTicketService.getByStatus(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/tracker/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get ticket details based on status name", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByStatusName(@PathVariable("name") String name,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber) {
		return new ResponseEntity<>(createTicketService.getByStatusName(name, licenseNumber),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("id") Long id) throws Exception {
		return new ResponseEntity<>(createTicketService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/count")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount(@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "issueFrom", required = false) String issueFrom) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getCount1(date, todate, issueFrom, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> add(@Valid @RequestBody CreateTicketRequestDto requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.add(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody CreateTicketDashboardDTO requestDto) throws ParseException {
		return new ResponseEntity<>(createTicketService.update(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PutMapping("/updatedescription")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updatedescription(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.updatedesc(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/updatefileder")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updatefielder(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.updatefielder(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/dashboardcount")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount_percentage() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getCount_percentage(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/dashboardbymonth")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getbyMonth() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getbyMonth(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getByassignlist", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on assign list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getfieldeng(@Valid @RequestBody CreateTicketDashboardDTO requestDto) throws Exception {
		return new ResponseEntity<>(createTicketService.getByAssignToId(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

//	@RequestMapping(value = "/getByassignlistfiledengr", method = RequestMethod.POST)
//	@ApiOperation(value = "This api is used to get ticket details based on assign list", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getfieldengnger(@Valid @RequestBody CreateTicketRequestDto requestDto) throws Exception {
//		return new ResponseEntity<>(createTicketService.getByAssignToIdfromdatetodate(requestDto), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}

	@PostMapping("/getByassignlistfiledengr")
	@ApiOperation(value = "This api is used to search complaints using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFiltertest(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(
				createTicketService.getAllByRequestFilterfieldapp(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@RequestMapping(value = "/escalate", method = RequestMethod.PUT)
	@ApiOperation(value = "This api is used to escalate ticket if sla expired", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> get() throws Exception {
		return new ResponseEntity<>(createTicketService.updateAssigneeForSLAExpiredTicket(),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/viewReport")
	@ApiOperation(value = "This api is used to view ticket records base on date and status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> viewReport(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.viewReport(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getByTicketNumber", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on ticket number", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketByNumber(@RequestParam("ticketNumber") String ticketNumber)
			throws Exception {
		return new ResponseEntity<>(createTicketService.getByTicketNumber(ticketNumber),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/activity-logs")
	@ApiOperation(value = "This API is get activity logs", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getActivityLogs(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		return new ResponseEntity<>(helpdeskTicketAuditService.getAllByRequestFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getByLicenseNumber", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on license number", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketByLicenseNumber(@RequestParam("licenseNumber") String licenseNumber)
			throws Exception {
		return new ResponseEntity<>(createTicketService.getTicketByLicenseNumber(licenseNumber),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/flaglist", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on assign list flag 0 only", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getfieldengflg(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws Exception {
		return new ResponseEntity<>(createTicketService.FlagLIstAPI(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/flagupdate", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based on assign list flag 0 only", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> FlagUpdate(@Valid @RequestBody CreateTicketDashboardDTO requestDto) throws Exception {
		return new ResponseEntity<>(createTicketService.UpdateFlag(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getByPhnOrEmail", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on phone number or email id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketByPhnOrEmail(@RequestParam("search") String search) throws Exception {
		return new ResponseEntity<>(createTicketService.getTicketByPhnOrEmail(search),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getByStatusViaApp", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get ticket details based on status name for via app", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByStatusViaApp(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "issueFromId", required = true) Long issueFromId) {
		return new ResponseEntity<>(createTicketService.getByStatusViaApp(name, issueFromId),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/ticketstauscount", method = RequestMethod.POST)
	@ApiOperation(value = "This api is used to get ticket details based ticketstaus name", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> FlagUpdate(@Valid @RequestBody TicketcounRequest requestDto) throws Exception {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getCountticketstatus(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/applicationviappadd")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> applicationviappadd(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.addapp(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PutMapping("/applicationviappupdate")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> applicationviappupdate(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.updateapp(requestDto), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/listlicensenrViaApp")
	@ApiOperation(value = "This api is to get ticket details based on status name for via app", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> listStatusViaApp(@Valid @RequestBody List<CreateTicketDashboardDTO> requestDto) {
		return new ResponseEntity<>(createTicketService.listStatusViaApp(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/paymentappsearch")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> paymentappsearchByFilter(
			@Valid @RequestBody PaginationRequestDTO2 paginationRequestDTO) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(
				createTicketService.getAllByRequestFilterapp(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/paymentappsearchnew")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> paymentAppSearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO2 paginationRequestDTO) throws ParseException {
		// AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getAllByRequestFilterPaymentAppSearchNew(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/paymentappcount")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> paymentcount1(@Valid @RequestBody CreateTicketRequestDto2 requestDto)
			throws ParseException {
		// AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getCountapp(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/maindasboard")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getmaindashboard(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getmaindashboard(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/districtwiseticket")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Districtwisetickett(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getdistrictwiseticket(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/districtcategorytwiseticket")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Districtcategorywisetickett(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "district", required = false) String district) throws ParseException {
		return new ResponseEntity<>(createTicketService.getdistrictcategorywiseticket(fromDate, toDate, district),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getBylicencenumber", method = RequestMethod.GET)
	@ApiOperation(value = "This api is used to get ticket details based on id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getTicketById(@RequestParam("licencenumber") String licencenumber) throws Exception {
		return new ResponseEntity<>(createTicketService.getBylicencenumber(licencenumber),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/viewReportincident")
	@ApiOperation(value = "This api is used to view ticket records base on date and status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> viewReportincident(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.viewReportincident(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/viewReportquery")
	@ApiOperation(value = "This api is used to view ticket records base on date and status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> viewReportquery(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.viewReportquery(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PutMapping("/viewstatusupdate")
	@ApiOperation(value = "This api is used to update view status ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> viewstatusupdate(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.updateviewstausticket(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/districtdasboard")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getdistrictdashboard(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getdistrictdashboard(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getByStatusdistrict", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get ticket details based on status name for via app", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByStatusdistrict(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "districtcode", required = true) String districtcode) {
		return new ResponseEntity<>(createTicketService.getByStatusdistrict(name, districtcode),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/searchdistrictlist")
	@ApiOperation(value = "This api is used to get incident filter search filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilterdistricthadle(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(createTicketService.searchByFilterdistricthadle(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/dashboardcountincident")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount_percentageincident() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getCount_percentageincident(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/dashboardbymonthincident")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getbyMonthincident() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getbyMonthincident(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/resolutionposdevicesla12hrs")
	@ApiOperation(value = "This api is is used to get resolution pos device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Resolutionposdevicetickets(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getResolutionofticketssla12hrs(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/resolutionposdevicesla24hrs")
	@ApiOperation(value = "This api is is used to get resolution pos device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Resolutionpos24hrs(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getResolutionofticketssla24hrs(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/dashboardtollfree")
	@ApiOperation(value = "This api is is used to get resolution pos device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Tollfree(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getTollFreeSummary(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/securitymanagementdevices")
	@ApiOperation(value = "This api is is used to get securitymanagement pos device ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Securitymanagementdevicetickets(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getsecuritymanagements(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/listlicensenrViaApppagination")
	@ApiOperation(value = "This api is used to search complaints using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> listnrviaapppagination(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		// AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getAllByRequestFilterlicensenr(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@PostMapping("/recoverytimeobjective")
	@ApiOperation(value = "This api is is used to get securitymanagement pos device ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> recoverytimeobjective(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getrecoverytimeobjective(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/recoverypointobjective")
	@ApiOperation(value = "This api is is used to get securitymanagement pos device ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> recoverypointobjective(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getrecoverypointobjective(requestDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/dashboardcountftdate")
	@ApiOperation(value = "This api is used to create ticket", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getdashboardcount(@Valid @RequestBody CreateTicketDashboardDTO requestDto)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(
				createTicketService.getCount_percentage_dashboardprocess(requestDto, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/ticketdashboardbymonth")
	@ApiOperation(value = "This api is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getbyMonthCounts() throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(createTicketService.getbyticketDashboardByMonth(authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	
	@PostMapping("/allticketdownload")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Filtersearch(@RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getAllByfilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@PostMapping("/allticketdownloadtestG")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Filtersearch1(@RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		return new ResponseEntity<>(createTicketService.getAllByfilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
