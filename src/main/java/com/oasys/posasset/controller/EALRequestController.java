package com.oasys.posasset.controller;

import java.text.ParseException;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestDashboardDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.service.WorkFlowService;
import com.oasys.posasset.service.impl.EALRequestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "PosAssetData", description = "This controller contain all operations for Department Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ealrequest")
public class EALRequestController {

	@Autowired
	EALRequestService ealrequestservice;

	@Autowired
	WorkFlowService workFlowService;

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add device lost details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> adddevicelostPage(@Valid @RequestBody EalRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(ealrequestservice.save(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealrequestservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getByealrequest/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByealrequest(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealrequestservice.getByealid(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all Ticketstatus list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(ealrequestservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/code")
	@ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCode() throws JsonParseException, ParseException {
		return new ResponseEntity<>(ealrequestservice.getCodeeal(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(ealrequestservice.getsubPagesearchNewByFilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getAllByUserId/{userId}")
	@ApiOperation(value = "This api is to get all eal details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByUserId(@PathVariable("userId") Long userId) {
		return new ResponseEntity<>(ealrequestservice.getAllByUserId(userId), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@ApiOperation(value = "This api to Approval", notes = "Returns HTTP 200 if successful get the record")
	@PutMapping(value = "/approval")
	public ResponseEntity<Object> updateApproval(@RequestBody ApprovalDTO approvalDto) throws Exception {

		return new ResponseEntity<>(ealrequestservice.updateApproval(approvalDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	@GetMapping("/getLogsByApplicationNo/{applicationNo}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getLogsByApplicationNo(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(ealrequestservice.getLogByApplicationNo(applicationNo),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/updateWorkFlow")
	@ApiOperation(value = "This api is used for bottling repair WorkFLow", notes = "Returns HTTP 200 if successful get the record")
	public Boolean updateWorkFlowDetails(@RequestBody WorkFlowStatusUpdateDTO workflowStatusUpdateDto)
			throws RecordNotFoundException, Exception {
		return workFlowService.updateEALWorkFlowDetails(workflowStatusUpdateDto);
	}

	@PutMapping("/update")
	@ApiOperation(value = "This api is used to add device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody EalRequestDTO requestDTO) {
		return new ResponseEntity<>(ealrequestservice.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/ealdasboard")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getmaindashboard(@Valid @RequestBody EalRequestDTO requestDTO) throws ParseException {
		return new ResponseEntity<>(ealrequestservice.getmaindashboard(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getAllByDesignationCode/{designationCode}")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByDesignationCode(@PathVariable("designationCode") String designationCode) {
		return new ResponseEntity<>(ealrequestservice.getAllByDesignationCode(designationCode),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/ealdasboardapplicant")
	@ApiOperation(value = "This api is is used to get ticket count for each ticket status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getmaindashboardapplicant(@Valid @RequestBody EalRequestDashboardDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrequestservice.getmaindashboardapplicant(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/ealavailability")
	@ApiOperation(value = "This api is is used to get ealavailability", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealavailability(@Valid @RequestBody EalRequestDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrequestservice.ealavailable(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getAllByapproved")
	@ApiOperation(value = "This api is to get all device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByDesignationCode() {
		return new ResponseEntity<>(ealrequestservice.getAllByapproved(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/prequementsearch")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilterprequment(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(ealrequestservice.getsubPagesearchNewByFilterpre(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/currentlyworkithinprocess")
	@ApiOperation(value = "This api is used to add device lost details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addcurrentlywork(@Valid @RequestBody EalRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(ealrequestservice.currentlyworkwithsave(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/forceclosure")
	@ApiOperation(value = "This api is forceclosure", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> forceclosure(@Valid @RequestBody EalRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(ealrequestservice.forceclosureupdate(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("vendorstatusupdate")
	@ApiOperation(value = "This api is used to update vendor status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> vendorstatusupdate(@Valid @RequestBody EalRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(ealrequestservice.updatevendorstatus(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@RequestMapping(value = "/summarycount", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get Count", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getCount(@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate) {
		GenericResponse objGenericResponse = ealrequestservice.getCount(fromDate, toDate);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

//	 @PostMapping("/update-vendor-id")
//	    public ResponseEntity<String> updateVendorId(@RequestBody EalRequestDTO ealRequestDTO) {
//		 ealrequestservice.updateVendorIdForRequests(ealRequestDTO.getLicenseNumberArray().stream()
//	            .map(Long::valueOf)
//	            .collect(Collectors.toList()), ealRequestDTO.getEalreqId());
//	        return new ResponseEntity<>("Vendor ID updated successfully", HttpStatus.OK);
//	    }
	@PostMapping("/update-vendor-id")
	public GenericResponse updateVendorId(@RequestBody EalRequestDTO ealRequestDTO) {
		GenericResponse objGenericResponse = ealrequestservice.updateVendorIdForRequests(
				ealRequestDTO.getEalRequestIds().stream().map(Long::valueOf).collect(Collectors.toList()),
				ealRequestDTO.getVendorId());
//	        return new ResponseEntity<>("Vendor ID updated successfully", HttpStatus.OK);
		return objGenericResponse;
	}

	@GetMapping("/getVendorRollCode")
	public ResponseEntity<Object> getVendorCode() {
		GenericResponse objGenericResponse = ealrequestservice.getVendorCode();
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getVendorUserId")
	public ResponseEntity<Object> getUser(String code) {
		GenericResponse objGenericResponse = ealrequestservice.getUser(code);
		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/filtersearch")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> Filtersearch(@RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrequestservice.getAllByfilter(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
