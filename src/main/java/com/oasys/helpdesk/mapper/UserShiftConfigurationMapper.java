package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.UserShiftConfigurationRequestDTO;
import com.oasys.helpdesk.dto.UserShiftConfigurationResponseDTO;
import com.oasys.helpdesk.entity.ShiftConfigEntity;
import com.oasys.helpdesk.entity.ShiftWorkingDaysEntity;
import com.oasys.helpdesk.entity.UserShiftConfigurationEntity;
import com.oasys.helpdesk.repository.ShiftConfigRepository;
import com.oasys.helpdesk.repository.ShiftWorkingDaysRepository;
import com.oasys.helpdesk.repository.UserShiftConfigurationRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class UserShiftConfigurationMapper {
	@Autowired
	private ShiftConfigMapper shiftConfigMapper;

	@Autowired
	private ShiftWorkingDaysMapper shiftWorkingDaysMapper;
	
	@Autowired
	private ShiftConfigRepository shiftConfigEntityRepository;

	@Autowired
	private ShiftWorkingDaysRepository shiftWorkingDaysRepository;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private UserShiftConfigurationRepository userShiftConfigurationRepository;
	
	public UserShiftConfigurationEntity userShiftConfigRequestDTOToEntity(
			UserShiftConfigurationRequestDTO requestDTO, Long userId) {
		LocalDate startDate = null;
		LocalDate endDate = null;
		if (Objects.isNull(requestDTO.getStartDate())) {
			throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
					.getMessage(new Object[] { Constant.START_DATE }));
		}
		if (Objects.isNull(requestDTO.getEndDate())) {
			throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
					.getMessage(new Object[] { Constant.END_DATE }));
		}
		if (Objects.isNull(requestDTO.getShifConfigId())) {
			throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
					.getMessage(new Object[] { Constant.SHIFT_CONFIG_ID }));
		}
		if (Objects.isNull(requestDTO.getShifWorkingDaysId())) {
			throw new InvalidDataValidation(ResponseMessageConstant.MANDTORY_REQUEST_PARM
					.getMessage(new Object[] { Constant.SHIFT_WORKING_DAYS }));
		}
		UserShiftConfigurationEntity userShiftConfigEntity = null;
		if (Objects.nonNull(requestDTO.getId())) {
			Optional<UserShiftConfigurationEntity> userShiftConfigurationEntityOptional = userShiftConfigurationRepository
					.findById(requestDTO.getId());
			if (!userShiftConfigurationEntityOptional.isPresent()) {
				throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
						.getMessage(new Object[] { Constant.USER_SHIFT_CONFIGURATION_ID }));
			}
			userShiftConfigEntity = userShiftConfigurationEntityOptional.get();
		} 
		if(Objects.isNull(userShiftConfigEntity) && Objects.nonNull(userId)) {
			userShiftConfigEntity = userShiftConfigurationRepository.findByUserId(userId);
		}
		if(Objects.isNull(userShiftConfigEntity)){
			userShiftConfigEntity = new UserShiftConfigurationEntity();
		}
		if (StringUtils.isNotBlank(requestDTO.getStartDate())) {
			startDate = LocalDate.parse(requestDTO.getStartDate());
			
		}
		if (StringUtils.isNotBlank(requestDTO.getEndDate())) {
			endDate = LocalDate.parse(requestDTO.getEndDate());
			
		}
		if(endDate.isBefore(startDate)) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.SHIFT_END_DATE_LESS_THAN_START_DATE.getMessage());
		}
		if (Objects.isNull(userId)) {
			if (startDate.isBefore(LocalDate.now())) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.SHIFT_START_DATE_LESS_THAN_TODAY_DATE.getMessage());
			}
		}
		userShiftConfigEntity.setStartDate(startDate);
		userShiftConfigEntity.setEndDate(endDate);
		Optional<ShiftConfigEntity> shiftConfigEntity = shiftConfigEntityRepository
				.findById(requestDTO.getShifConfigId());
		if (!shiftConfigEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.SHIFT_CONFIG_ID }));
		}
		Optional<ShiftWorkingDaysEntity> shiftWorkingDaysId = shiftWorkingDaysRepository
				.findById(requestDTO.getShifWorkingDaysId());
		if (!shiftWorkingDaysId.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { Constant.SHIFT_WORKING_DAYS_ID }));
		}
		userShiftConfigEntity.setShifConfig(shiftConfigEntity.get());
		userShiftConfigEntity.setShifWorkingDays(shiftWorkingDaysId.get());
		return userShiftConfigEntity;
	}
	
	public UserShiftConfigurationResponseDTO userShiftConfigEntityToResponseDTO(UserShiftConfigurationEntity entity) {
		UserShiftConfigurationResponseDTO responseDTO = commonUtil.modalMap(entity,
				UserShiftConfigurationResponseDTO.class);
		if (Objects.nonNull(entity.getStartDate())) {
			responseDTO.setStartDate(entity.getStartDate().toString());
		}
		if (Objects.nonNull(entity.getEndDate())) {
			responseDTO.setEndDate(entity.getEndDate().toString());
		}
		if (Objects.nonNull(entity.getShifConfig())) {
			responseDTO.setShiftConfigResponseDTO(shiftConfigMapper.convertEntityToResponseDTO(entity.getShifConfig()));
		}
		if (Objects.nonNull(entity.getShifWorkingDays())) {
			responseDTO.setShiftWorkingDaysResponseDTO(
					shiftWorkingDaysMapper.convertEntityToResponseDTO(entity.getShifWorkingDays()));
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
}
