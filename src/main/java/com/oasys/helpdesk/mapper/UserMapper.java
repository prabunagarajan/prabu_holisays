package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.BackupUserResponseDTO;
import com.oasys.helpdesk.dto.UserRequestDTO;
import com.oasys.helpdesk.dto.UserResponseDTO;
import com.oasys.helpdesk.dto.UserShiftConfigurationResponseDTO;
import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.entity.WorkLocationEntity;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.WorkLocationRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class UserMapper {

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private UserShiftConfigurationMapper userShiftConfigurationMapper;
	
	@Autowired
	private WorkLocationMapper workLocationMapper;
	
	@Autowired
	private BackupUserMapper backupUserMapper;
	
	@Autowired
	private WorkLocationRepository workflowLocationRepository;

	public UserEntity requestDTOToEntity(UserRequestDTO userRequestDTO) {
		userRequestDTO.setFirstName(userRequestDTO.getFirstName().trim());
		userRequestDTO.setMiddleName(userRequestDTO.getMiddleName().trim());
		userRequestDTO.setLastName(userRequestDTO.getLastName().trim());
		userRequestDTO.setEmailId(userRequestDTO.getEmailId().trim());
		userRequestDTO.setEmployeeId(userRequestDTO.getEmployeeId().trim());
		userRequestDTO.setSalutationCode(userRequestDTO.getSalutationCode().trim());
		userRequestDTO.setDesignationCode(userRequestDTO.getDesignationCode().trim());
		if (StringUtils.isNotBlank(userRequestDTO.getAddress())) {
			userRequestDTO.setAddress(userRequestDTO.getAddress().trim());
		}
//		String salutationMasterResponse = commonDataController.getMasterDropDownValueByKey(
//				Constant.SALUTATION_DROPDOWN_KEY, userRequestDTO.getSalutationCode());
//		if (StringUtils.isBlank(salutationMasterResponse)) {
//			throw new InvalidDataValidation(
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SALUTATION_CODE }));
//		}

//		String designationMasterResponse = commonDataController.getMasterDropDownValueByKey(
//				Constant.DESIGNATION_DROPDOWN_KEY, userRequestDTO.getDesignationCode());
//		if (StringUtils.isBlank(designationMasterResponse)) {
//			throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
//					.getMessage(new Object[] { Constant.DESIGNATION_CODE }));
//		}
		RoleMaster roleMaster = roleMasterRepository.getById(userRequestDTO.getRoleId());
		if (Objects.isNull(roleMaster)) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ROLE_ID }));
		}
		UserEntity entity = commonUtil.modalMap(userRequestDTO, UserEntity.class);
		entity.setIsSystemDefaultUser(Boolean.FALSE);
		entity.setAccountLocked(Boolean.FALSE);
		List<RoleMaster> roles = new ArrayList<>();
		roles.add(roleMaster);
		entity.setRoles(roles);
		if (StringUtils.isNotBlank(userRequestDTO.getDateOfJoining())) {
			LocalDate localDate = LocalDate.parse(userRequestDTO.getDateOfJoining());
			if(localDate.isBefore(LocalDate.now())) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.JOINING_DATE_LESS_THAN_TODAY_DATE.getMessage());
			}
			entity.setDateOfJoining(localDate);
		}
		return entity;
	}

	

	

	

	public UserResponseDTO entityToResponseDTO(UserEntity userEntity, Boolean isMappedDataRequired) {
		UserResponseDTO userResponseDTO = commonUtil.modalMap(userEntity, UserResponseDTO.class);
		String salutationMasterResponse = commonDataController
				.getMasterDropDownValueByKey(Constant.SALUTATION_DROPDOWN_KEY, userEntity.getSalutationCode());
		if (StringUtils.isNotBlank(salutationMasterResponse)) {
			userResponseDTO.setSalutationValue(salutationMasterResponse);
		}

		String designationMasterResponse = commonDataController.getMasterDropDownValueByKey(
				Constant.DESIGNATION_DROPDOWN_KEY, userEntity.getDesignationCode());
		if (StringUtils.isNotBlank(designationMasterResponse)) {
			userResponseDTO.setDesignationValue(designationMasterResponse);

		}
		//userResponseDTO.setDesignationValue(userEntity.getDesignationCode());

		
		if (Objects.nonNull(userEntity.getEmploymentStatus())) {
			userResponseDTO.setEmploymentStatus(userEntity.getEmploymentStatus());
		}
		String createdByUserName = commonDataController.getUserNameByUserId(userEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameByUserId(userEntity.getModifiedBy());

		userResponseDTO.setCreatedBy(createdByUserName);
		userResponseDTO.setModifiedBy(modifiedByUserName);
		//userResponseDTO.setAddress(userEntity.getAddress());
		//userResponseDTO.setDeviceId(userEntity.getDeviceId());
		
		List<RoleMaster> roleMasterList = userEntity.getRoles();
		if(!CollectionUtils.isEmpty(roleMasterList)) {
			userResponseDTO.setRoleId(roleMasterList.get(0).getId());
			userResponseDTO.setRoleName(roleMasterList.get(0).getRoleName());
		}

		
		
		
		if(Boolean.FALSE.equals(isMappedDataRequired)) {
			return userResponseDTO;
		}
		if (Objects.nonNull(userEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			userResponseDTO.setCreatedDate(dateFormat.format(userEntity.getCreatedDate()));
		}
		if (Objects.nonNull(userEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			userResponseDTO.setModifiedDate(dateFormat.format(userEntity.getModifiedDate()));
		}
		if (Objects.nonNull(userEntity.getWorkLocation())) {
			WorkLocationResponseDTO workLocationResponseDTO = workLocationMapper
					.workLocationEnityToResponseDTO(userEntity.getWorkLocation());
			//new
			Long userid=userEntity.getWorkLocation().getUser().getId();
			List<WorkLocationEntity> list =workflowLocationRepository.findAllByUserId(userid);
			userResponseDTO.setWorkLocationResponseDTO(list);
			//old
			//userResponseDTO.setWorkLocationResponseDTO(workLocationResponseDTO);
		}
		if (Objects.nonNull(userEntity.getShiftConfiguration())) {
			UserShiftConfigurationResponseDTO shiftConfigResponseDTO = userShiftConfigurationMapper
					.userShiftConfigEntityToResponseDTO(userEntity.getShiftConfiguration());
			userResponseDTO.setShiftConfigurationResponseDTO(shiftConfigResponseDTO);
		}
		if (Objects.nonNull(userEntity.getBackupUser())) {
			BackupUserResponseDTO backupUserResponseDTO = backupUserMapper
					.backupUserEntityToResponseDTO(userEntity.getBackupUser());
			userResponseDTO.setBackupUserResponseDTO(backupUserResponseDTO);
		}
		
		return userResponseDTO;
	}

}
