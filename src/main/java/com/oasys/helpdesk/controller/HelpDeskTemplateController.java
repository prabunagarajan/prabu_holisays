package com.oasys.helpdesk.controller;

import com.oasys.helpdesk.dto.HelpDeskTemplateRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.HelpDeskTemplateService;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@Api(value = "HelpDeskData", description = "This controller contain all  operation of Email Request")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/template")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HelpDeskTemplateController {
    private final HelpDeskTemplateService helpDeskTemplateService;

    @RequestMapping(value = "/getAllTemplates", method = RequestMethod.GET)
    @ApiOperation(value = "This api is to get  all helpdesk template data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getAllCategory() {
        GenericResponse objGenericResponse = helpDeskTemplateService.getAllHelpDeskTemplates();
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getTemplateById", method = RequestMethod.GET)
    @ApiOperation(value = "This api is used to get template based on id", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getEntityTypeById(@RequestParam("id") Long id) {
        GenericResponse objGenericResponse = helpDeskTemplateService.getHelpDeskTemplateById(id);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/searchTemplateByNameAndTypeAndStatus", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to get Template based on Name, Type and Status", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getEntityTypeByNameAndStatus(@RequestBody PaginationRequestDTO paginationRequestDTO) {
        GenericResponse objGenericResponse = helpDeskTemplateService.getByTemplateNameAndTemplateTypeAndStatus(paginationRequestDTO);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllTemplateTypes", method = RequestMethod.GET)
    @ApiOperation(value = "This api is to get  all Entity Type Names data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getAllTemplateTypes() {
        GenericResponse objGenericResponse = helpDeskTemplateService.getAllTemplateTypes();
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @GetMapping("/address")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getAddress() {
        return new ResponseEntity<>(helpDeskTemplateService.getAddress(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/addTemplate", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Template data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> addTemplate(@RequestBody HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {
        GenericResponse objGenericResponse = helpDeskTemplateService.createTemplate(helpDeskTemplateDTO);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateTemplate", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to update the Template data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> updateTemplate(@RequestBody HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {
        GenericResponse objGenericResponse = helpDeskTemplateService.updateTemplate(helpDeskTemplateDTO);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }
    
    
    
    
}
