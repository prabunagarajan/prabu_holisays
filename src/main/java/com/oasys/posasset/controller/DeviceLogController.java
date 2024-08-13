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
import com.oasys.posasset.service.DeviceLogService;
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

@RequestMapping("/devicelog")
public class DeviceLogController extends BaseController {
	
	@Autowired
	DeviceLogService devicelogService;

	
	
	@PostMapping("/search")
	@ApiOperation(value = "This api is used to search l record using filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)
			throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(devicelogService.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@RequestMapping(value = "/getdevicestatus", method = RequestMethod.GET)
	@ApiOperation(value = "This api is to get all device status details", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(devicelogService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
