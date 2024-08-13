package com.oasys.helpdesk.controller;

import com.oasys.helpdesk.dto.EntityMasterTypeRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.service.EntityMasterTypeService;
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

@RequestMapping("/entitytype")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EntityMasterTypeController {
    private final EntityMasterTypeService entityMasterTypeService;

    @RequestMapping(value = "/getAllEntityTypes", method = RequestMethod.GET)
    @ApiOperation(value = "This api is to get  all EntityType data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getAllCategory() {
        GenericResponse objGenericResponse = entityMasterTypeService.getAllEntityMasterTypes();
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllEntityNames", method = RequestMethod.GET)
    @ApiOperation(value = "This api is to get  all Entity Type Names data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getAllEntityNames() {
        GenericResponse objGenericResponse = entityMasterTypeService.getAllEntityNames();
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllStatus", method = RequestMethod.GET)
    @ApiOperation(value = "This api is to get  all Entity Type Status data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getAllStatus() {
        GenericResponse objGenericResponse = entityMasterTypeService.getAllStatuses();
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getEntityTypeById", method = RequestMethod.GET)
    @ApiOperation(value = "This api is used to get Entity Type based on id", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getEntityTypeById(@RequestParam("id") Long id) {
        GenericResponse objGenericResponse = entityMasterTypeService.getEntityTypeById(id);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/searchEntityTypeByNameAndStatus", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to get Entity Type based on Entity Name and Status", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getEntityTypeByNameAndStatus(@RequestBody PaginationRequestDTO paginationRequestDTO) throws Exception {
        GenericResponse objGenericResponse = entityMasterTypeService.getAllEntityMasterTypeByNameAndStatus(paginationRequestDTO);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @GetMapping("/code")
    @ApiOperation(value = "This api is used to get unique code", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> getEntityCode() {
        return new ResponseEntity<>(entityMasterTypeService.getEntityCode(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/addEntityType", method = RequestMethod.POST)
    @ApiOperation(value = "This api is used to create the Entity Type data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> addEntityType(@RequestBody EntityMasterTypeRequestDTO entityMasterTypeDTO) throws Exception {
        GenericResponse objGenericResponse = entityMasterTypeService.createEntityType(entityMasterTypeDTO);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateEntityType", method = RequestMethod.PUT)
    @ApiOperation(value = "This api is used to update the Entity Type data", notes = "Returns HTTP 200 if successful get the record")
    public ResponseEntity<Object> updateEntityType(@RequestBody EntityMasterTypeRequestDTO entityMasterTypeRequestDTO) throws Exception {
        GenericResponse objGenericResponse = entityMasterTypeService.updateEntityType(entityMasterTypeRequestDTO);
        return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
    }
    
    
    
    
    
    
}
