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
import com.oasys.helpdesk.entity.Activity;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.ActivityService;
import com.oasys.helpdesk.utility.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@Api(value = "Activity")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/activity")
public class ActivityController extends BaseController{

	@Autowired
	private ActivityService activityService;	
	
	/**
	 * This API will be used to get all the activity based on the pagination
	 * @param httpServletRequest
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	@GetMapping(value = "/getAllActivity")
	@ApiOperation(value = "This api is used to get all the activity", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getAllActivity(HttpServletRequest httpServletRequest,@RequestBody PaginationRequestDTO searchRequestDTO,Locale locale) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return activityService.getAllActivity(authenticationDTO,searchRequestDTO,locale);
	}

	/**
	 * This API will be used to get all the designation based on the pagination
	 * @param httpServletRequest
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	@GetMapping(value = "/getAllActiveActivity")
	@ApiOperation(value = "This api is used to get all the active activity", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getAllActiveActivity(HttpServletRequest httpServletRequest,@RequestBody PaginationRequestDTO searchRequestDTO,Locale locale) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return activityService.getAllActiveActivity(authenticationDTO,searchRequestDTO,locale);
	}
	
	/**
	 * This api is used to get the role information based on the activity id
	 * @param httpServletRequest
	 * @param mobileNumber
	 * @param locale
	 * @return
	 */
	@GetMapping("/getActivityById")
	@ApiOperation(value = "This api is used to get the activity information based on the activity id", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getActivityById(HttpServletRequest httpServletRequest,@RequestParam(name="id",required = true) Long id,Locale locale) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return activityService.getActivityById(authenticationDTO,id,locale);//activity id
	}


	/**
	 * This api will be used to save the activity
	 * @param roleMasterRequestDto
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/saveUpdateActivity")
	@ApiOperation(value = "This api is used to add or update the activity ", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse saveUpdateActivity(@RequestBody Activity activity,Locale locale) throws Exception {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return activityService.saveUpdateActivity(authenticationDTO, activity,locale);
	}
	
	
//	@RequestMapping(value = "/addActivity", method = RequestMethod.POST)
//	@ApiOperation(value = "add Activity ", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> createUserMaster(@RequestBody ActivityRequestDto activityRequestDto) throws Exception {
//		GenericResponse objGenericResponse = activityService.createActivity(activityRequestDto);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
//	@RequestMapping(value = "/updateActivity", method = RequestMethod.POST)
//	@ApiOperation(value = "update Activity ", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> updateUserMaster(@RequestBody ActivityRequestDto activityRequestDto) throws Exception {
//		GenericResponse objGenericResponse = activityService.updateActivity(activityRequestDto);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}

	/**
	 * This api will be used to get the role menu list
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/getRoleMenuList")
	@ApiOperation(value = "Role Menu List", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getRoleMenuList(Locale locale) throws Exception {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return activityService.getRoleMenuList(authenticationDTO,locale);
	}
	
	/**
	 * This api will be used to find the menu list based on the user roles
	 * @return
	 */
	@GetMapping(value = "/getMenuByUserId")
	@ApiOperation(value = "This api is used to get Menu By Logged In User", notes = "Returns HTTP 200 if successful get the record")
	public GenericResponse getMenuByUserId(Locale locale) {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return activityService.getMenuByUserId(authenticationDTO,locale);
	}
	
//	/**
//	 * This api will load all the license menu for the customer only
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/getLicenseMenus", method = RequestMethod.GET)
//	@ApiOperation(value = "License Menu List", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getLicenseMenuList(HttpServletRequest request,@RequestParam(name="moduleCode",required = true) String moduleCode) throws Exception {
//		GenericResponse objGenericResponse = activityService.getLicenseMenuList(moduleCode);
//		return new ResponseEntity<>(objGenericResponse, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
}
