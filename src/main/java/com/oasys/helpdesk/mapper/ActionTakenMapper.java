package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.ACTUAL_PROBLEM_ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.response.ActionTakenResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class ActionTakenMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	

	
	@Autowired
	AcutalProblemRepository helpDeskAcutalProblemRepository;

	public ActionTakenResponseDto convertEntityToResponseDTO(ActionTaken actionTakenEntity) {
		ActionTakenResponseDto responseDTO = commonUtil.modalMap(actionTakenEntity, ActionTakenResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(actionTakenEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(actionTakenEntity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setActionTakenCode(actionTakenEntity.getCode());
		if (Objects.nonNull(actionTakenEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(actionTakenEntity.getCreatedDate()));
		}
		if (Objects.nonNull(actionTakenEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(actionTakenEntity.getModifiedDate()));
		}
		if (Objects.nonNull(actionTakenEntity.getActualProblem())) {
			responseDTO.setActualProblem(actionTakenEntity.getActualProblem().getActualProblem());
			responseDTO.setActualProblemId(actionTakenEntity.getActualProblem().getId());
			if (Objects.nonNull(actionTakenEntity.getActualProblem().getSubCategory())) {
				responseDTO
						.setSubcategoryName(actionTakenEntity.getActualProblem().getSubCategory().getSubCategoryName());
				responseDTO.setSubCategoryId(actionTakenEntity.getActualProblem().getSubCategory().getId());
				if (Objects
						.nonNull(actionTakenEntity.getActualProblem().getSubCategory().getHelpDeskTicketCategory())) {
					{
						responseDTO.setCategoryName(actionTakenEntity.getActualProblem().getSubCategory()
								.getHelpDeskTicketCategory().getCategoryName());
						responseDTO.setCategoryId(actionTakenEntity.getActualProblem().getSubCategory()
								.getHelpDeskTicketCategory().getId());
					}
				}
			}
		}
		return responseDTO;
	}

	public ActionTaken convertRequestDTOToEntity(ActionTakenRequestDto requestDTO, ActionTaken entity) {
		if (Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, ActionTaken.class);
		}

		Optional<ActualProblem> helpDeskActualProblem = helpDeskAcutalProblemRepository
				.findById(requestDTO.getActualProblemId());
		if (!helpDeskActualProblem.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTUAL_PROBLEM_ID }));
		}
		entity.setActualProblem(helpDeskActualProblem.get());
		entity.setActive(requestDTO.isActive());
		entity.setActionTaken(requestDTO.getActionTaken());
		return entity;
	}
	
}
