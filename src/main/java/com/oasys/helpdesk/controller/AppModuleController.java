package com.oasys.helpdesk.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AppModule;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.AppModuleService;
import com.oasys.helpdesk.utility.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Application Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/appModule")
public class AppModuleController extends BaseController{

	@Autowired
	private AppModuleService appModuleService;	
	
	/**
	 * This API will be used to get all the module based on the pagination
	 * @param httpServletRequest
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	@GetMapping(value = "/getAllAppModule")
	@ApiOperation(value = "This api is used to get all the module", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getAllAppModule(HttpServletRequest httpServletRequest,@RequestBody PaginationRequestDTO searchRequestDTO,Locale locale) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return appModuleService.getAllAppModule(authenticationDTO,searchRequestDTO,locale);
	}
	
	/**
	 * This api will be used to get the acitve and parent module
	 * @return
	 */
	@GetMapping(value = "/getActiveParentModule")
	@ApiOperation(value = "This api is used to get Active ParentModule data", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getActiveParentModule(Locale locale) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return appModuleService.getActiveParentModule(authenticationDTO,locale);
	}
	
	/**
	 * This api will be used to find the app module based on the module id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/getAppModuleById")
	@ApiOperation(value = "This api is used to get AppModule data based on id", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getUserMasterById(Locale locale,@RequestParam("id") Long id) throws Exception {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return appModuleService.getAppModuleById(authenticationDTO,locale,id);
	}
	
	/**
	 * This api will be used to add or update the app module
	 * @param appModuleRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/addUpdateAppModule")
	@ApiOperation(value = "add AppModule ", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse createUserMaster(@RequestBody AppModule appModule,Locale locale) throws Exception {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return appModuleService.addUpdateAppModule(authenticationDTO,locale,appModule);
	}
}