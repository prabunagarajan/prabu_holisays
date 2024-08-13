package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.BackupUserRequestDTO;
import com.oasys.helpdesk.dto.BackupUserResponseDTO;
import com.oasys.helpdesk.entity.BackupUserEntity;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.BackupUserRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class BackupUserMapper {
	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private BackupUserRepository backupUserRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public BackupUserResponseDTO backupUserEntityToResponseDTO(BackupUserEntity entity) {
		BackupUserResponseDTO responseDTO = commonUtil.modalMap(entity, BackupUserResponseDTO.class);
		String designationMasterResponse = commonDataController
				.getMasterDropDownValueByKey(Constant.DESIGNATION_DROPDOWN_KEY, entity.getDesignationCode());
		if (StringUtils.isNotBlank(designationMasterResponse)) {
			responseDTO.setDesignationValue(designationMasterResponse);
		}
		if (Objects.nonNull(entity.getRoleId())) {
			Optional<RoleMaster> roleMaster = roleMasterRepository.findById(entity.getRoleId());
			responseDTO.setRoleName(roleMaster.isPresent() ? roleMaster.get().getRoleName() : null);
		}
		String createdByUserName = commonDataController.getUserNameByUserId(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameByUserId(entity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		return responseDTO;
	}
	
	public BackupUserEntity backupUserRequestDTOToEntity(BackupUserRequestDTO requestDTO, Long userId) {
		BackupUserEntity backupUserEntity = null;
		if (Objects.nonNull(requestDTO.getId())) {
			Optional<BackupUserEntity> workLocationEntityOptional = backupUserRepository
					.findById(requestDTO.getId());
			if (!workLocationEntityOptional.isPresent()) {
				throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
						.getMessage(new Object[] { Constant.BACKUP_USER_ID }));
			}
			backupUserEntity = workLocationEntityOptional.get();
		} 
		if(Objects.isNull(backupUserEntity) && Objects.nonNull(userId)) {
			backupUserEntity = backupUserRepository.findByUserId(userId);
		}
		if(Objects.isNull(backupUserEntity)){
			backupUserEntity = new BackupUserEntity();
		}
		
//		String designationMasterResponse = commonDataController.getMasterDropDownValueByKey(
//				Constant.DESIGNATION_DROPDOWN_KEY, requestDTO.getDesignationCode());
//		if (StringUtils.isBlank(designationMasterResponse)) {
//			throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
//					.getMessage(new Object[] { Constant.DESIGNATION_CODE }));
//		}
		if(Objects.isNull(requestDTO.getBackupUserId())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.BACKUP_USERID }));
		}
		Optional<UserEntity> userOptional = userRepository.findById(requestDTO.getBackupUserId());
		if(!userOptional.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { Constant.BACKUP_USER }));
		}
		/*
		 * List<BackupUserEntity> backupUserList =
		 * backupUserRepository.findByBackupUserId(requestDTO.getBackupUserId());
		 * 
		 * if (!CollectionUtils.isEmpty(backupUserList)) { throw new
		 * InvalidDataValidation(ResponseMessageConstant.BACKUP_USER_ALREADY_MAPPED.
		 * getMessage()); }
		 */
		 
		
		
		backupUserEntity.setBackupUserId(requestDTO.getBackupUserId());
		backupUserEntity.setDesignationCode(requestDTO.getDesignationCode());
		backupUserEntity.setRoleId(userOptional.get().getRoles().get(0).getId());
		backupUserEntity.setEmployeeCode(userOptional.get().getEmployeeId());
		return backupUserEntity;
	}
}
