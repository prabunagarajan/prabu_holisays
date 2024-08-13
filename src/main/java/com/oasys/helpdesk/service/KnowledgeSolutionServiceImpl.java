package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.KB_MANDATORY_FIELDS;
import static com.oasys.helpdesk.constant.Constant.KNOWLEDGE_SOLUTION;
import static java.util.Objects.isNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.KnowledgeSolutionRequestDTO;
import com.oasys.helpdesk.entity.KnowledgeSolution;
import com.oasys.helpdesk.mapper.KnowledgeSolutionMapper;
import com.oasys.helpdesk.repository.KnowledgeSolutionRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

@Service
public class KnowledgeSolutionServiceImpl {

	@Autowired
	private KnowledgeSolutionRepository helpDeskKnowledgeRepository;

	@Autowired 
	private CommonUtil commonUtil; 
	
	@Autowired
	private KnowledgeSolutionMapper knowledgeSolutionMapper;

	public GenericResponse createKnowledge(KnowledgeSolutionRequestDTO knowledgeRequestDto) {

		Optional<KnowledgeSolution> entity = helpDeskKnowledgeRepository
				.findById(knowledgeRequestDto.getId());
		if (entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[]{ID}));
		}

		GenericResponse mandatoryCheck = performMandatoryCheck(knowledgeRequestDto);
		if (mandatoryCheck != null)
			return mandatoryCheck;
		

		KnowledgeSolution knowledgeentity = helpDeskKnowledgeRepository.save(knowledgeSolutionMapper.toKnowledgeSolutionEntity(knowledgeRequestDto));
		
		
		return Library.getSuccessfulResponse(knowledgeentity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}
	
	protected static GenericResponse performMandatoryCheck(KnowledgeSolutionRequestDTO requestDTO) {
		if(isNull(requestDTO.getId()) || isNull(requestDTO.getCategoryId()) ||
				isNull(requestDTO.getSubcategoryId()) ||
		isNull(requestDTO.getIssueDetails())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{KB_MANDATORY_FIELDS}));
		}
		return null;
	}
	
	public Object getCode() {
		MenuPrefix prefix = MenuPrefix.getType(KNOWLEDGE_SOLUTION);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<KnowledgeSolution> shiftEntity = helpDeskKnowledgeRepository.findBySolutionIdIgnoreCase(code);
			if (shiftEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

		public Object getBySolutionId(String solutionId) {
			Optional<KnowledgeSolution> knowledgeSolution = helpDeskKnowledgeRepository.findBySolutionId(solutionId);
			if (!knowledgeSolution.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
			return Library.getSuccessfulResponse(knowledgeSolutionMapper.convertEntityTOResponseDTO(knowledgeSolution.get()),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		}


}
