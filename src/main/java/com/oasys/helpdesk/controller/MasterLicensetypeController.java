package com.oasys.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oasys.helpdesk.service.MasterLicenseTypeService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.ApiOperation;

@RestController
public class MasterLicensetypeController {

	@Autowired
	MasterLicenseTypeService masterlicensetypeService;

	@GetMapping("/getAll")
	@ApiOperation(value = "This api is to get all licenseType", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {

		return new ResponseEntity<>(masterlicensetypeService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);

	}

	
	@GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active LicenceType records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(masterlicensetypeService.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
}
