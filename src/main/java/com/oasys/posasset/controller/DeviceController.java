package com.oasys.posasset.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.controller.BaseController;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import com.oasys.posasset.request.DeviceRequestDTO;
import com.oasys.posasset.service.DeviceService;
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

@RequestMapping("/device")
public class DeviceController extends BaseController {
	@Autowired
	DeviceService deviceService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get all device details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(deviceService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping("/is-associated")
	@ApiOperation(value = "This api is to check if device associated or not", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> checkIfAssociated(
			@RequestParam(value = "deviceNumber", required = true) String deviceNumber) {
		return new ResponseEntity<>(deviceService.isDeviceAlreadyAssociated(deviceNumber),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add device details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addDevice(@Valid @RequestBody DeviceRequestDTO deviceReqDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceService.addDevice(deviceReqDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PutMapping
	@ApiOperation(value = "This api is used to add device a device", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody DeviceRequestDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(deviceService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);

	}

	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get view Ticketstatus by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(deviceService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(deviceService.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/getShopCode")
	@ApiOperation(value = "This api is to get view Ticketstatus by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@RequestParam(value="deviceNumber", required=true ) String deviceNumber){
		return new ResponseEntity<>(deviceService.getShopCodeByDeviceNumber(deviceNumber), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@GetMapping("/getdeviceidverify")
	@ApiOperation(value = "This api is to get view Ticketstatus by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getdeviceidverify(@RequestParam(value="deviceNumber", required=true ) String deviceNumber){
		return new ResponseEntity<>(deviceService.geverifyByDeviceNumber(deviceNumber), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	
	@GetMapping("/getShopCodelist")
	@ApiOperation(value = "This api is to get view shopCode by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getShopCode(@RequestParam(value="shopCode", required=true ) String shopCode){
		return new ResponseEntity<>(deviceService.getShopCode(shopCode), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	
	@PostMapping("/devicelistmapunmap")
	@ApiOperation(value = "This api is used to search complaints using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFiltertest(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(deviceService.getAllByRequestFiltermapunmap(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	
	}
	
	
	
	
	@PostMapping("/fpscodemulti/search")
	@ApiOperation(value = "This api is used to search ticket record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFiltermultifpscode(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(deviceService.getAllByRequestFilterfps(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
