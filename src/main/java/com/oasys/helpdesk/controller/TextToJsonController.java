package com.oasys.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.service.TextConversionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "TextToJson")
public class TextToJsonController {

	@Autowired
	private TextConversionService TextConversionService;

	@PostMapping("/converttexttoJSON")
	@ApiOperation(value = "Convert Text to JSON", notes = "It converts the provided text to JSON format. This API is only used on the backend developer side.")
	public ResponseEntity<Object> convertToJSON(@RequestBody String textDetails) {
		return new ResponseEntity<>(TextConversionService.convertToJSON(textDetails), HttpStatus.OK);
	}
}
