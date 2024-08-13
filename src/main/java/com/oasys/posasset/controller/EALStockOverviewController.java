package com.oasys.posasset.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.repository.EALAvailable;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EALAvailableDTO;
import com.oasys.posasset.dto.EALStockwastageDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestDashboardDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.dto.placeholderDTO;
import com.oasys.posasset.service.WorkFlowService;
import com.oasys.posasset.service.impl.EALRequestService;
import com.oasys.posasset.service.impl.EALStockOverviewService;

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

@RequestMapping("/ealstockoverview")
public class EALStockOverviewController {

	@Autowired
	EALRequestService ealrequestservice;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	EALStockOverviewService ealrstockoverviewservice;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;

	@Value("${spring.common.devtoken}")
	private String token;

	@Value("${spring.common.stockbarcode}")
	private String stockurl;

	@PostMapping("/stockoverviewsearch")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> subPagesearchNewByFilter(
			@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.getsubPagesearchNewByFilterstock(paginationRequestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getByeal")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "licenseNo", required = false) String licenseNo,
			@RequestParam(value = "codeType", required = false) String codeType) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.stockgetBylic(fromDate, toDate, licenseNo, codeType),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

//	@GetMapping("/getByealsub")
//	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getById(@RequestParam(value="applnno", required=false) String applnno,@RequestParam(value="fromDate", required=false) String fromDate,@RequestParam(value="toDate", required=false) String toDate,@RequestParam(value="codeTypeValue", required=false) String codeTypeValue,@RequestParam(value="licenseNumber", required=false) String licenseNumber,@RequestParam(value="size", required=false) String size,@RequestParam(value="unmappedType", required=false) String unmappedType) throws ParseException {
//		return new ResponseEntity<>(ealrstockoverviewservice.stockgetById(applnno,fromDate,toDate,codeTypeValue,licenseNumber,size,unmappedType), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}

	@GetMapping("/getByealsub")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@RequestParam(value = "applnno", required = false) String applnno,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "codeTypeValue", required = false) String codeTypeValue,
			@RequestParam(value = "licenseNumber", required = false) String licenseNumber) throws ParseException {
		return new ResponseEntity<>(
				ealrstockoverviewservice.stockgetById(applnno, fromDate, toDate, codeTypeValue, licenseNumber),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/stockusagewastage")
	@ApiOperation(value = "This api is is used to get stockeusage", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealavailability(@Valid @RequestBody EalRequestDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.ealavailable(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/mappedcodes")
	@ApiOperation(value = "This api is is used to get stockeusage", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealmappedcodes(@Valid @RequestBody EalRequestDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.mappedCount(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/unmappedcodes")
	@ApiOperation(value = "This api is is used to get stockeusage", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getealunmappedcodes(@Valid @RequestBody EalRequestDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.unmappedcodes(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getByealrequeststock/{id}")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByealrequeststock(@PathVariable("id") Long id) {
		return new ResponseEntity<>(ealrstockoverviewservice.stockgetByealid(id),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/getByealsubmapunmap")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@RequestParam(value = "applnno", required = false) String applnno,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "codeTypeValue", required = false) String codeTypeValue,
			@RequestParam(value = "licenseNumber", required = false) String licenseNumber,
			@RequestParam(value = "size", required = false) String size,
			@RequestParam(value = "unmappedType", required = false) String unmappedType,
			@RequestParam(value = "codeType", required = false) String codeType) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.stockgetByIdmapunmap(applnno, fromDate, toDate,
				codeTypeValue, licenseNumber, size, unmappedType, codeType), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/stockoverviewnew")
	@ApiOperation(value = "This api is is used to get stocke usage", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getstockoverviewnew(@Valid @RequestBody EalRequestDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.elastockoverviewnew(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/stockoverviewmappunmapcodes")
	@ApiOperation(value = "This api is is used to get stock overview usage", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getEalStockOverviewMappedCodes(@Valid @RequestBody EalRequestDTO requestDTO)
			throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.stockOverviewMappedCount(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/stockoverviewmappunmapcodesparenunit")
	@ApiOperation(value = "This api is is used to get stocke usage parent unit", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> stockOverviewMappedandunppedParentUnitCount(
			@Valid @RequestBody EalRequestDTO requestDTO) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.stockOverviewMappedandunppedParentUnitCount(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/mapStockSummary")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> mapStockSummary(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "licenseNumber", required = false) String licenseNumber,
			@RequestParam(value = "packagingSize", required = false) String packagingSize,
			@RequestParam(value = "cartonSize", required = false) String cartonSize) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.mapStockSummary(fromDate, toDate,
			 licenseNumber,packagingSize,cartonSize), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/unmapStockSummary")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> unmapStockSummary(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "licenseNumber", required = false) String licenseNumber,
			@RequestParam(value = "printingType", required = false) String printingType,
			@RequestParam(value = "unmappedType", required = false) String unmappedType,
			@RequestParam(value = "mapType", required = false) String mapType) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.unmapStockSummary(fromDate, toDate,
			 licenseNumber,printingType,unmappedType,mapType), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/puMapStockSummary")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> puMapStockSummary(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "licenseNumber", required = false) String licenseNumber,
			@RequestParam(value = "packagingSize", required = false) String packagingSize,
			@RequestParam(value = "cartonSize", required = false) String cartonSize) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.puMapStockSummary(fromDate, toDate,
			 licenseNumber,packagingSize,cartonSize), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/puUnmapStockSummary")
	@ApiOperation(value = "This api is to get all eal return details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> puUnmapStockSummary(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "licenseNumber", required = false) String licenseNumber,
			@RequestParam(value = "printingType", required = false) String printingType,
			@RequestParam(value = "unmappedType", required = false) String unmappedType,
			@RequestParam(value = "mapType", required = false) String mapType) throws ParseException {
		return new ResponseEntity<>(ealrstockoverviewservice.puUnmapStockSummary(fromDate, toDate,
			 licenseNumber,printingType,unmappedType,mapType), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	

}
