package com.oasys.helpdesk.service;


import java.text.ParseException;
import java.util.List;

import com.oasys.helpdesk.dto.AssociatedUserRequestDTO;
import com.oasys.helpdesk.dto.ChangePasswordDto;
import com.oasys.helpdesk.dto.ForgotPasswordDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PasswordResetDTO;
import com.oasys.helpdesk.dto.ResetPasswordDtodet;
import com.oasys.helpdesk.dto.UpdateUserRequestDTO;
import com.oasys.helpdesk.dto.UserFieldDTO;
import com.oasys.helpdesk.dto.UserMultipleDistrictDTO;
import com.oasys.helpdesk.dto.UserRequestConDTO;
import com.oasys.helpdesk.dto.UserRequestDTO;
import com.oasys.helpdesk.entity.LoginHistory;
import com.oasys.helpdesk.entity.UserAttempts;
import com.oasys.helpdesk.request.OTPVerificationDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface UserService {
	
	GenericResponse save(UserRequestDTO requestDTO);
	GenericResponse getUsersByRoleId(Long  roleId, String districtCode);
	GenericResponse getEmployeeId();
	GenericResponse changePassword(ChangePasswordDto requestDTO);
	GenericResponse getAll();
	GenericResponse getById(Long id);
	GenericResponse update(UpdateUserRequestDTO requestDTO);
	GenericResponse searchByFilter(PaginationRequestDTO paginationDto);
	GenericResponse getBackuUserListByRoleId(Long  roleId, Long userId);
	Boolean updateFailAttempts(String username);
	UserAttempts getUserAttempts(Long userId) ;
	void resetFailAttempts(Long userId);
	void saveUpdateLoginHistory(Long userId, String loginIP, String event) ;
	List<LoginHistory> findLastLoginTime(Long userId) ;
	GenericResponse resetPassword(PasswordResetDTO passwordResetDTO);
	GenericResponse getLoginHistoryBySearchFilter(PaginationRequestDTO paginationDto);
	Boolean checkEmail(String emailId, Long id);
	Boolean checkPhoneNumber(String phoneNumber,Long id);
	Boolean checkUserName(String userName, Long id);
	GenericResponse generateOTP(String emailId);
	GenericResponse verifyOTP(OTPVerificationDTO otpVerificationDTO) ;
	GenericResponse generateForgotPasswordOTP(String emailId);
	GenericResponse forgotPassword(ForgotPasswordDTO requestDTO);
	GenericResponse logout();
	GenericResponse saveOrUpdateAssociatedUsers(AssociatedUserRequestDTO requestDTO);
	GenericResponse getAssociatedUsers(Long id) ;
	GenericResponse getAssociatedUsersdropdown(Long userid) ;
	GenericResponse getuseriddistinct();
	GenericResponse getByUserid(Long userid);
	GenericResponse resetPassworddetails(ResetPasswordDtodet passwordResetDTO);
	
	GenericResponse fieldlogindetails(UserFieldDTO requestDto);
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO) throws ParseException;
	
	GenericResponse getUsersByRoleIdMultipledistrict(UserMultipleDistrictDTO requestDTO);
	
	GenericResponse getUsersByRoleIdshop();
	
	GenericResponse getUserByassigntoidinfo(UserRequestConDTO requestDTO);
	
	GenericResponse resetPasswordattempts(ResetPasswordDtodet passwordResetDTO);
}
