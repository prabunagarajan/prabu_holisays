package com.oasys.helpdesk.validation;

import static com.oasys.helpdesk.constant.Constant.EMAIL_ID;
import static com.oasys.helpdesk.constant.Constant.EMPLOYEE_ID;
import static com.oasys.helpdesk.constant.Constant.FIRST_NAME;
import static com.oasys.helpdesk.constant.Constant.LAST_NAME;
import static com.oasys.helpdesk.constant.Constant.MIDDLE_NAME;
import static com.oasys.helpdesk.constant.Constant.PHONE_NUMBER;
import static com.oasys.helpdesk.constant.Constant.USERNAME;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssociatedUserRequestDTO;
import com.oasys.helpdesk.dto.UserRequestDTO;
import com.oasys.helpdesk.entity.EmailVerification;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.EmailVerificationRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Component
public class UserRequestValidator {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private EmailVerificationRepository emailVerificationRepository;
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	public GenericResponse validateCreateUserRequest(UserRequestDTO requestDTO) {
		if(Boolean.FALSE.equals(commonUtil.isEmailValid(requestDTO.getEmailId()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { EMAIL_ID }));
		}
		
		
		EmailVerification emailVerification = emailVerificationRepository.findByEmailId(requestDTO.getEmailId());
		if (Objects.isNull(emailVerification) || Boolean.FALSE.equals(emailVerification.getVerified())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.EMAIL_NOT_VERIFIED.getMessage());
		}

		if(Boolean.FALSE.equals(commonUtil.isPhoneNumberValid(requestDTO.getPhoneNumber()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PHONE_NUMBER }));
		}
		if(StringUtils.isNotBlank(requestDTO.getFirstName()) && Boolean.FALSE.equals(commonUtil.isNameValid(requestDTO.getFirstName()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { FIRST_NAME }));
		}
		if(StringUtils.isNotBlank(requestDTO.getMiddleName()) && Boolean.FALSE.equals(commonUtil.isNameValid(requestDTO.getMiddleName()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { MIDDLE_NAME }));
		}
		if(StringUtils.isNotBlank(requestDTO.getLastName()) && Boolean.FALSE.equals(commonUtil.isNameValid(requestDTO.getLastName()))) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { LAST_NAME }));
		}
		Optional<UserEntity> userEntity = userRepository.findByEmailId(requestDTO.getEmailId());
		if(userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { EMAIL_ID }));
		}
		userEntity = userRepository.findByPhoneNumber(requestDTO.getPhoneNumber());
		if(userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { PHONE_NUMBER }));
		}
		userEntity = userRepository.findByUsernameIgnoreCase(requestDTO.getUsername());
		if(userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { USERNAME }));
		}
		userEntity = userRepository.getByEmployeeIdIgnoreCase(requestDTO.getEmployeeId());
		if(userEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { EMPLOYEE_ID }));
		}
		RoleMaster roleMasterAdmin = roleMasterRepository.findByRoleCode(Constant.HELPDESK_ADMIN);
		if(Objects.isNull(roleMasterAdmin) || Objects.isNull(roleMasterAdmin.getId())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.ROLE_NOT_CONFIGURED);
		}
		if (requestDTO.getRoleId().equals(roleMasterAdmin.getId())) {
			List<UserEntity> adminUserList = userRepository.getUserByRoleId(roleMasterAdmin.getId());
			if (!CollectionUtils.isEmpty(adminUserList)) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS
								.getMessage(new Object[] { roleMasterAdmin.getRoleName() }));
			}
		}
		RoleMaster roleMasterFieldAdmin = roleMasterRepository.findByRoleCode(Constant.FIELD_ADMIN);
		if (Objects.nonNull(roleMasterFieldAdmin) && Objects.nonNull(roleMasterFieldAdmin.getId())
				&& requestDTO.getRoleId().equals(roleMasterFieldAdmin.getId())) {
			List<UserEntity> fieldAdminUserList = userRepository.getUserByRoleId(roleMasterFieldAdmin.getId());
			if (!CollectionUtils.isEmpty(fieldAdminUserList)) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS
								.getMessage(new Object[] { roleMasterFieldAdmin.getRoleName() }));
			}
		}
		
		if (Objects.nonNull(requestDTO.getBackupUserRequestDTO())) {
			
			
			if (!requestDTO.getRoleId().equals(requestDTO.getBackupUserRequestDTO().getRoleId())
					&& !roleMasterAdmin.getId().equals(requestDTO.getBackupUserRequestDTO().getRoleId())) {
				return Library.getFailResponseCode(ErrorCode.ROLES_MISMATCH.getErrorCode(),
						ResponseMessageConstant.ROLES_MISMATCH.getMessage());
			}
		}
		 
		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.VERIFIED);
	}
	
//	public GenericResponse validateAssociateUserRequestDTO(AssociatedUserRequestDTO requestDTO) {
//		if (Boolean.FALSE.equals(requestDTO.getIsUpdateRequest())
//				&& CollectionUtils.isEmpty(requestDTO.getAssignToOfficerIds())) {
//			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//					ResponseMessageConstant.MANDTORY_REQUEST_PARM
//							.getMessage(new Object[] { Constant.ASSIGN_TO_OFFICER_IDS }));
//		}
//		for (Long id : requestDTO.getHandlingOfficerIds()) {
//			Optional<UserEntity> associatedUserEntityOptional = userRepository.findById(id);
//			if (!associatedUserEntityOptional.isPresent()) {
//				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//						ResponseMessageConstant.INVALID_ASSOCIATE_USER_ID
//								.getMessage(new Object[] { Constant.HANDLING_OFFICER_IDS, id }));
//			}
//			if (!associatedUserEntityOptional.get().getRoles().stream()
//					.anyMatch(r -> Constant.HANDLING_OFFICER_ROLE.equals(r.getRoleCode()))) {
//				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//						ResponseMessageConstant.INVALID_ASSOCIATE_USER_ROLE.getMessage(
//								new Object[] { Constant.HANDLING_OFFICER_ROLE, Constant.HANDLING_OFFICER_IDS }));
//			}
//		}
//		if (CollectionUtils.isEmpty(requestDTO.getAssignToOfficerIds())) {
//			return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//					ErrorMessages.VERIFIED);
//		}
//		for (Long id : requestDTO.getAssignToOfficerIds()) {
//			Optional<UserEntity> associatedUserEntityOptional = userRepository.findById(id);
//			if (!associatedUserEntityOptional.isPresent()) {
//				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//						ResponseMessageConstant.INVALID_ASSOCIATE_USER_ID
//								.getMessage(new Object[] { Constant.ASSIGN_TO_OFFICER_IDS, id }));
//			}
//			if (!associatedUserEntityOptional.get().getRoles().stream()
//					.anyMatch(r -> Constant.ASSIGN_TO_OFFICER_ROLE.equals(r.getRoleCode()))) {
//				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//						ResponseMessageConstant.INVALID_ASSOCIATE_USER_ROLE.getMessage(
//								new Object[] { Constant.ASSIGN_TO_OFFICER_ROLE, Constant.ASSIGN_TO_OFFICER_IDS }));
//			}
//		}
//
//		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.VERIFIED);
//	}

}
