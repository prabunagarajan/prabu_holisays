package com.oasys.posasset.controller;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.service.WorkFlowService;
import com.oasys.posasset.service.impl.EALStockInPURequestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "ealstock_pu", description = "This controller contain all operations for ealstock pu")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/ealstock/pu")
public class EALStockinPUController {

	
	@Autowired
	EALStockInPURequestService ealrequestservice;
	
	@Autowired
	WorkFlowService workFlowService;
	
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add device lost details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> ealStockSave(@Valid @RequestBody List<EalRequestMapResponseDTO> requestDTO)
			throws RecordNotFoundException, Exception {

		return new ResponseEntity<>(ealrequestservice.ealStockSave(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	

	
	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealrequestservice.stockgetById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getByealrequest/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByealrequest(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealrequestservice.stockgetByealid(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
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
		return new ResponseEntity<>(ealrequestservice.getCode(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(
				ealrequestservice.getsubPagesearchNewByFilterstock(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/getAllByUserId/{userId}")
	@ApiOperation(value = "This api is to get all eal details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByUserId(@PathVariable("userId") Long userId) {
		return new ResponseEntity<>(ealrequestservice.stockgetAllByUserId(userId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	
	@GetMapping("/getLogsByApplicationNo/{applicationNo}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getLogsByApplicationNo(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(ealrequestservice.getLogByApplicationNostock(applicationNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	@ApiOperation(value = "This api is used to add device return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateDevice(@Valid @RequestBody EalPUtoBWFLRequestDTO requestDTO) {
		return new ResponseEntity<>(ealrequestservice.EALStockupdate(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
//	@GetMapping("/getealrequet/{applicationNo}/{createdby}/{forceclosureFlag}")
//	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getealrequestApplicationNocreatedby(@PathVariable("applicationNo") String applicationNo,@PathVariable("createdby") Long createdby,@PathVariable("forceclosureFlag") Boolean forceclosureFlag) {
//		return new ResponseEntity<>(ealrequestservice.getyApplicationNocreatedBy(applicationNo,createdby,forceclosureFlag), 
//				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	
	
	@GetMapping("/getealrequet/{applicationNo}/{createdby}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealrequestApplicationNocreatedby(@PathVariable("applicationNo") String applicationNo,@PathVariable("createdby") Long createdby) {
		return new ResponseEntity<>(ealrequestservice.getyApplicationNocreatedBy(applicationNo,createdby), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
//	@GetMapping("/getealrequet/{applicationNo}")
//	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getealrequestApplicationNo(@PathVariable("applicationNo") String applicationNo) {
//		return new ResponseEntity<>(ealrequestservice.getyApplicationNo(applicationNo), 
//				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	
	
//	@GetMapping("/getealrequet/{applicationNo}/{createdby}")
//	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getealrequestApplicationNo(@PathVariable("applicationNo") String applicationNo,@PathVariable("createdby") Long createdby) {
//		return new ResponseEntity<>(ealrequestservice.getyApplicationNo(applicationNo,createdby), 
//				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	
	
	@GetMapping("/getstock/{applicationNo}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getstocknNo(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(ealrequestservice.getystockApplicationNo(applicationNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@GetMapping("/geteal/{ealrequestApplnno}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealNo(@PathVariable("ealrequestApplnno") String ealrequestApplnno) {
		return new ResponseEntity<>(ealrequestservice.getByealApplicationNo(ealrequestApplnno), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
	@GetMapping("/getstockavailable/{applicationNo}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getstockavailable(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(ealrequestservice.getystockavailableApplicationNo(applicationNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
//	@PostMapping("/searchstock")
//    @ApiOperation(value = "This api is used to get incident filter search filter", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException  {
//		return new ResponseEntity<>(ealrequestservice.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
	

	@GetMapping("/getdiapatcheddetails/{applicationNo}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getDiapatchedDetails(@PathVariable("applicationNo") String applicationNo) {
		return new ResponseEntity<>(ealrequestservice.getDiapatchedDetails(applicationNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getealrequet")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealrequestApplicationNocreatedbyLicenseNO(@RequestParam(value = "applicationNo", required = true) String applicationNo,
	        @RequestParam(value = "createdby", required = false) Long createdby,
	        @RequestParam(value = "licenseNo", required = false) String licenseNo) {
		return new ResponseEntity<>(ealrequestservice.getyApplicationNocreatedByLicenseNo(applicationNo,createdby,licenseNo), 
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}


