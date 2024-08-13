package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.service.EntityService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import antlr.collections.List;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/entity")
public class EntityDetailsController {
	
	@Autowired
	EntityService entityservice;
	
	@PostMapping("/add")
	@ApiOperation(value = "This api is used to add Entity ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> addEntityDetail(@Valid @RequestBody EntityDetailsDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(entityservice.addEntityDetail(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@PutMapping("/update")
	@ApiOperation(value = "This api is to edit Entity", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> updateEntity(@Valid @RequestBody EntityDetailsDTO requestDTO)
			throws RecordNotFoundException, Exception {
		return new ResponseEntity<>(entityservice.updateEntity(requestDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}


	@GetMapping("/getById/{id}")
	@ApiOperation(value = "This api is to get Entity by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		return new ResponseEntity<>(entityservice.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	@GetMapping("/getlist")
	@ApiOperation(value = "This api is to get all Entity list", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(entityservice.getAll(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}

	@GetMapping("/getactivetrue")
	public ResponseEntity<Object>getIsactiveTrue(@RequestParam Boolean pass){
		return new ResponseEntity<>(entityservice.getIsactiveTrue(pass),ResponseHeaderUtility.HttpHeadersConfig(),HttpStatus.OK);
		
	}


    @GetMapping("/activelist")
	@ApiOperation(value = "This api is to get all active Entity records", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllActive() {
		return new ResponseEntity<>(entityservice.getAllActive(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
    
    @PostMapping("/search")
	@ApiOperation(value = "This api is Search By Entity Details ", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllByRequestFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		return new ResponseEntity<>(entityservice.getAllByRequestFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	}	

