package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.WorkLocationRequestDTO;
import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.helpdesk.entity.WorkLocationEntity;
import com.oasys.helpdesk.repository.WorkLocationRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class WorkLocationMapper {
	

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private WorkLocationRepository workflowLocationRepository;
	
	public WorkLocationEntity workLocationRequestDTOTOEntity(WorkLocationRequestDTO requestDTO, Long userId) {
		WorkLocationEntity workLocationEntity = null;
		if (Objects.nonNull(requestDTO.getId())) {
			Optional<WorkLocationEntity> workLocationEntityOptional = workflowLocationRepository
					.findById(requestDTO.getId());
			if (!workLocationEntityOptional.isPresent()) {
				throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
						.getMessage(new Object[] { Constant.WORKFLOW_LOCATION_ID }));
			}
			workLocationEntity = workLocationEntityOptional.get();
		} 
		if(Objects.isNull(workLocationEntity) && Objects.nonNull(userId)) {
			workLocationEntity = workflowLocationRepository.findByUserId(userId);
		}
		if(Objects.isNull(workLocationEntity)){
			workLocationEntity = new WorkLocationEntity();
		}
		if (StringUtils.isBlank(requestDTO.getEntityTypeCode())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.ENTITY_TYPE_CODE }));
		}
		if (StringUtils.isBlank(requestDTO.getDepartmentCode())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.DEPARTMENT_CODE }));
		}
		
//		String departmentMasterResponse = commonDataController
//				.getMasterDropDownValueByKey(Constant.DEPARTMENT_DROPDOWN_KEY, requestDTO.getDepartmentCode());
//		if (StringUtils.isBlank(departmentMasterResponse)) {
//			throw new InvalidDataValidation(
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.DEPARTMENT_CODE }));
//		}
//		String entityTypeMasterResponse = commonDataController
//				.getMasterDropDownValueByKey(Constant.ENTITY_TYPE_DROPDOWN_KEY, requestDTO.getEntityTypeCode());
//		if (StringUtils.isBlank(entityTypeMasterResponse)) {
//			throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
//					.getMessage(new Object[] { Constant.ENTITY_TYPE_CODE }));
//		}

		
		
		/*
		 * List<DistrictDTO> districtMasterResponse =
		 * commonDataController.callDistrictMasterAPI(requestDTO.getDistrictCode()); if
		 * (CollectionUtils.isEmpty(districtMasterResponse) ||
		 * districtMasterResponse.size() > 1) { throw new InvalidDataValidation(
		 * ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {
		 * Constant.DISTRICT_CODE })); }
		 */
		if (StringUtils.isBlank(requestDTO.getStateCode())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.STATE_CODE }));
		}
		if (StringUtils.isBlank(requestDTO.getDistrictCode())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.DISTRICT_CODE }));
		}
		workLocationEntity.setStateCode(requestDTO.getStateCode());
		workLocationEntity.setDepartmentCode(requestDTO.getDepartmentCode());
		workLocationEntity.setDistrictCode(requestDTO.getDistrictCode());
		workLocationEntity.setEntityTypeCode(requestDTO.getEntityTypeCode());
		if (Objects.nonNull(requestDTO.getUpdateReason())) {
			workLocationEntity.setUpdateReason(requestDTO.getUpdateReason());
		}
		
		return workLocationEntity;
	}
	
	public WorkLocationResponseDTO workLocationEnityToResponseDTO(WorkLocationEntity entity) {

		WorkLocationResponseDTO responseDTO = commonUtil.modalMap(entity, WorkLocationResponseDTO.class);
//		String departmentMasterResponse = commonDataController
//				.getMasterDropDownValueByKey(Constant.DEPARTMENT_DROPDOWN_KEY, entity.getDepartmentCode());
//		if (StringUtils.isNotBlank(departmentMasterResponse)) {
//			responseDTO.setDepartmentValue(departmentMasterResponse);
//		}
		responseDTO.setDepartmentValue(entity.getDepartmentCode());
		
		String entityTypeMasterResponse = commonDataController
				.getMasterDropDownValueByKey(Constant.ENTITY_TYPE_DROPDOWN_KEY, entity.getEntityTypeCode());
		if (StringUtils.isNotBlank(entityTypeMasterResponse)) {
			responseDTO.setEntityTypeValue(entityTypeMasterResponse);
		}

		/*
		 * List<DistrictDTO> districtMasterResponse =
		 * commonDataController.callDistrictMasterAPI(entity.getDistrictCode()); if
		 * (!CollectionUtils.isEmpty(districtMasterResponse)) {
		 * responseDTO.setDistrictValue(districtMasterResponse.get(0).getName()); }
		 */

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
		if (Objects.nonNull(entity.getUpdateReason())) {
			if (entity.getUpdateReason().equalsIgnoreCase("Transfer")) {
				responseDTO.setUpdateReason("TRANSFER");
			} else if (entity.getUpdateReason().equalsIgnoreCase("Promotion")) {
				responseDTO.setUpdateReason("PROMOTION");
			} else if (entity.getUpdateReason().equalsIgnoreCase("Long Leave")) {
				responseDTO.setUpdateReason("LONG_LEAVE");
			} else if (entity.getUpdateReason().equalsIgnoreCase("Suspend")) {
				responseDTO.setUpdateReason("SUSPEND");
			}
			else if (entity.getUpdateReason().equalsIgnoreCase("New User")) {
				responseDTO.setUpdateReason("NEW_USER");
			}

		}
		responseDTO.setStateCode(entity.getStateCode());
		responseDTO.setDistrictCode(entity.getDistrictCode());
		responseDTO.setDistrictNames(entity.getDistrictNames());
		return responseDTO;
	}
}
