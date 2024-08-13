package com.oasys.helpdesk.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.VersionUpgradeDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.VersionUpgradeService;
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

@RequestMapping("/versionmanagement")
public class VersionUpgradeController extends BaseController{
	
	@Autowired
	VersionUpgradeService versionUpgradeService;

	@ApiOperation(value = "Getting the latest version of apk", notes = "Returns HTTP 200 if successful get the record")
	@GetMapping("/latestVersion")
	public ResponseEntity<Object> latestVersion() {		
		return new ResponseEntity<>(versionUpgradeService.latestVersion(),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@ApiOperation(value = "Creating a new Version of apk", notes = "Returns HTTP 200 if successful get the record")
	@PostMapping("/addNewVersion")
	public ResponseEntity<Object> addNewVersion(@RequestBody VersionUpgradeDTO versionUpgradeDto) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(versionUpgradeService.addNewVersion(versionUpgradeDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
		
	}

	
	@ApiOperation(value = "Updating the version of apk",notes = "Returns HTTP 200 if successful get the record")
	@PutMapping("/updateVersion/{id}")
	public ResponseEntity<Object> updateNewVersion(@PathVariable("id") Long id ,@RequestBody VersionUpgradeDTO versionUpgradeDto) {
		return new ResponseEntity<>(versionUpgradeService.updateVersion(id,versionUpgradeDto),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	
	@ApiOperation(value = "Geting all the apk version added ", notes = "Returns HTTP 200 if successful get the record")
	@GetMapping("/getAllVersion")
	public ResponseEntity<Object> getAllVersion() {
		return new ResponseEntity<>(versionUpgradeService.getAllVersion(),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
