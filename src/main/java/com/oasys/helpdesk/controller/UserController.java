package com.oasys.helpdesk.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.constant.PermissionConstant;
import com.oasys.helpdesk.dto.AssociatedUserRequestDTO;
import com.oasys.helpdesk.dto.ChangePasswordDto;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ResetPasswordDtodet;
import com.oasys.helpdesk.dto.UpdateUserRequestDTO;
import com.oasys.helpdesk.dto.UserMultipleDistrictDTO;
import com.oasys.helpdesk.dto.UserRequestConDTO;
import com.oasys.helpdesk.dto.UserRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.service.HelpdeskUserAuditService;
import com.oasys.helpdesk.service.UserService;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "User", description = "This controller contains api to do all operation for user")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HelpdeskUserAuditService helpdeskUserAuditService;
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@PostMapping
    @ApiOperation(value = "This api is used to add new user", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> save(@Valid @RequestBody UserRequestDTO requestDTO)  {
		return new ResponseEntity<>(userService.save(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@GetMapping("/employeeId")
    @ApiOperation(value = "This api is used to get unique employeeId", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getEmployeeId() {
		return new ResponseEntity<>(userService.getEmployeeId(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/byRole")
	//@PreAuthorize(PermissionConstant.USER_BY_ROLE)
	@ApiOperation(value = "This api will be used to find the user based on the role Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getUserByRole(@RequestParam(value="roleId",required=true) Long roleId, @RequestParam(value="districtCode",required=false) String districtCode) {
		return new ResponseEntity<>(userService.getUsersByRoleId(roleId, districtCode), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	//@PreAuthorize(PermissionConstant.VIEW_PROFILE)
	@PostMapping(value = "/changePassword")
	@ApiOperation(value = "This api will be used to change the passowrd by user Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordDto requestDTO) {
		return new ResponseEntity<>(userService.changePassword(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@PutMapping
    @ApiOperation(value = "This api is used to update existing user", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody UpdateUserRequestDTO requestDTO)  {
		return new ResponseEntity<>(userService.update(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@GetMapping
    @ApiOperation(value = "This api is used to get all users", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAll()  {
		return new ResponseEntity<>(userService.getAll(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	//@PreAuthorize(PermissionConstant.VIEW_PROFILE)
	@GetMapping("/{id}")
    @ApiOperation(value = "This api is used to get employee details by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getById(@PathVariable Long id)  {
		return new ResponseEntity<>(userService.getById(id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@PostMapping("/search")
    @ApiOperation(value = "This api is used to search users by filter", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilter(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) {
		return new ResponseEntity<>(userService.searchByFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@GetMapping(value = "/backupUsers/byRole")
	@ApiOperation(value = "This api will be used to find the user based on the role Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getBackupUserListByRole(@RequestParam(value="roleId",required=true) Long roleId, @RequestParam(value="userId",required=false) Long userId)  {
		return new ResponseEntity<>(userService.getBackuUserListByRoleId(roleId,userId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PostMapping(value = "/login-history")
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@ApiOperation(value = "This api will be used to find the user login history", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getUserHistory(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(userService.getLoginHistoryBySearchFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@GetMapping("/email-exist")
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@ApiOperation(value = "This api is used to check if user email exist or not", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> checkEmail(@RequestParam(name="id",required = false) Long id,
				@RequestParam(name="emailId",required = true) String emailId) {
		return new ResponseEntity<>(userService.checkEmail(emailId,id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
		
	}
	
	@GetMapping("/phone-number-exist")
	@ApiOperation(value = "This api is used to check if user phoneNumber exist or not", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> checkPhoneNumber(@RequestParam(name="id",required = false) Long id,
				@RequestParam(name="phoneNumber",required = true) String phoneNumber) {
		return new ResponseEntity<>(userService.checkPhoneNumber(phoneNumber,id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping("/username-exist")
	@ApiOperation(value = "This api is used to check the customer email exist or not", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> checkUsername(@RequestParam(name="id",required = false) Long id,
				@RequestParam(name="username",required = true) String username) {
		return new ResponseEntity<>(userService.checkUserName(username, id), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}

	@PostMapping("/logout")
	@ApiOperation(value = "This API is to logout of the application", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> logout() {
		return new ResponseEntity<>(userService.logout(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@PostMapping("/activity-logs")
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@ApiOperation(value = "This API is to logout of the application", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getActivityLogs(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO)  {
		return new ResponseEntity<>(helpdeskUserAuditService.getAllByRequestFilter(paginationRequestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	//@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@PutMapping("/assign")
    @ApiOperation(value = "This api is used to add or update associated users", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> update(@Valid @RequestBody AssociatedUserRequestDTO requestDTO)  {
		return new ResponseEntity<>(userService.saveOrUpdateAssociatedUsers(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/associated-users")
	@ApiOperation(value = "This api will be used to find all the associatedUsers", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getBackupUserListByRole(@RequestParam(value="userId",required=true) Long userId)  {
		return new ResponseEntity<>(userService.getAssociatedUsers(userId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}

	
	@GetMapping("/gethandlingofficerdropdown")
	@ApiOperation(value = "This api is used to gethandlicer officer distinct", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getAllhandlingofficerid() {
		return new ResponseEntity<>(userService.getuseriddistinct(), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/associatedusersdropdown")
	@ApiOperation(value = "This api will be used to find all the associatedUsers", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getassociatedusers(@RequestParam(value="userId",required=true) Long userId)  {
		return new ResponseEntity<>(userService.getAssociatedUsersdropdown(userId), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	

	@GetMapping("getByUserid/{userid}")
	@ApiOperation(value = "This api is to get view Ticketstatus by id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getByUserid(@PathVariable("userid") Long userid) {
		return new ResponseEntity<>(userService.getByUserid(userid), ResponseHeaderUtility.HttpHeadersConfig(),
				HttpStatus.OK);
	}
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@PostMapping(value = "/resetPassword")
	@ApiOperation(value = "This api will be used to change the passowrd by user Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordDtodet requestDTO) {
		return new ResponseEntity<>(userService.resetPassworddetails(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
//	@PostMapping("/fieldloginstatus")
//	@ApiOperation(value = "This api is is used field login status", notes = "Returns HTTP 200 if successful get the record")
//	public ResponseEntity<Object> getdistrictdashboard(@Valid @RequestBody UserFieldDTO requestDto) throws ParseException {
//		return new ResponseEntity<>(userService.fieldlogindetails(requestDto),
//				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
//	}
//	
	
	@PostMapping("/fieldloginstatus")
	@ApiOperation(value = "This api is is used field login status", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> searchByFilterlogin(@Valid @RequestBody PaginationRequestDTO paginationRequestDTO) throws ParseException {
		AuthenticationDTO authenticationDTO = findAuthenticationObject();
		return new ResponseEntity<>(userService.getAllByRequestFilter(paginationRequestDTO, authenticationDTO),
				ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/byRolemultipledistrict")
	@ApiOperation(value = "This api will be used to find the user based on the role Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getUserByRolemultipledistrict(@RequestBody UserMultipleDistrictDTO requestDTO) {
		return new ResponseEntity<>(userService.getUsersByRoleIdMultipledistrict(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@GetMapping(value = "/byRoleblockengineer")
	//@PreAuthorize(PermissionConstant.USER_BY_ROLE)
	@ApiOperation(value = "This api will be used to find the user based on the role Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getUserByRoleblockengineer() {
		return new ResponseEntity<>(userService.getUsersByRoleIdshop(), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	@PostMapping(value = "/assigntoiduserinfo")
	@ApiOperation(value = "This api will be used to find the user based assign to user info", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> getUserByassigntoidinfo(@RequestBody UserRequestConDTO requestDTO) {
		return new ResponseEntity<>(userService.getUserByassigntoidinfo(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);

	}
	
	
	
	@PreAuthorize(PermissionConstant.HELPDESK_USER_MANAGEMENT)
	@PostMapping(value = "/resetPasswordattemts")
	@ApiOperation(value = "This api will be used to change the passowrd by user Id", notes = "Returns HTTP 200 if successful get the record")
	public ResponseEntity<Object> resetPasswordattempts(@Valid @RequestBody ResetPasswordDtodet requestDTO) {
		return new ResponseEntity<>(userService.resetPasswordattempts(requestDTO), ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	
}
