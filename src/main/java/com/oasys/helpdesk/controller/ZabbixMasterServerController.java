package com.oasys.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.RoleMasterResponseDTO;
import com.oasys.helpdesk.dto.ZabbixMasterServerDTO;
import com.oasys.helpdesk.service.ZabbixMasterServerService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Role master", description = "This controller contain api to get all roles")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/zabbixmasterserver")
public class ZabbixMasterServerController extends BaseController {

	@Autowired
	private ZabbixMasterServerService zabbixmasterserverservice;

	@PostMapping("/addzabbixmasterserver")
	@ApiOperation(value = "This api is used to zabbixmasterserver ", notes = "Returns HTTP 200 if successful get the record")

	public ResponseEntity<Object> addzabbix(@RequestBody ZabbixMasterServerDTO zabbixmasterservicedto) {
		return new ResponseEntity<>(zabbixmasterserverservice.addZabbixMasterServer(zabbixmasterservicedto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "This api is to get all zabbix master server data", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(zabbixmasterserverservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

}
