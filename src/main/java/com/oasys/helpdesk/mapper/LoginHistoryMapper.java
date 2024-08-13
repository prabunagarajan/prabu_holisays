package com.oasys.helpdesk.mapper;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssociateresponseDTO;
import com.oasys.helpdesk.dto.LoginHistoryResponseDTO;
import com.oasys.helpdesk.entity.AssociatedUserEntity;
import com.oasys.helpdesk.entity.LoginHistory;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class LoginHistoryMapper {

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	public LoginHistoryResponseDTO convertEntityToResponseDTO(LoginHistory entity) {
		LoginHistoryResponseDTO responseDTO = commonUtil.modalMap(entity, LoginHistoryResponseDTO.class);
		responseDTO.setLoginTime(Objects.nonNull(entity.getLoginTime())? entity.getLoginTime().toString(): null);
		responseDTO.setLogoutTime(Objects.nonNull(entity.getLogoutTime())? entity.getLogoutTime().toString(): null);;
		Optional<UserEntity> userEntity = userRepository.findById(entity.getUserId());
		if(userEntity.isPresent()) {
			responseDTO.setEmailId(userEntity.get().getEmailId());
			responseDTO.setEmployeeId(userEntity.get().getEmployeeId());
			responseDTO.setUserName(userEntity.get().getUsername());
		}else {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.USER_ID }));
		}
		return responseDTO;
	}
	
	
	public AssociateresponseDTO convertEntityToResponseDTOhandlingofficerdropdown(AssociatedUserEntity entity) {
		AssociateresponseDTO responseDTO = commonUtil.modalMap(entity, AssociateresponseDTO.class);
		
		return responseDTO;
	}
	
}
