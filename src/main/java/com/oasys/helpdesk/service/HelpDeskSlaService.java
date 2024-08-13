package com.oasys.helpdesk.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.response.ActionTakenResponseDto;
import com.oasys.helpdesk.response.ActualProblemResponseDto;
import com.oasys.helpdesk.response.CategoryResponseDto;
import com.oasys.helpdesk.response.PriorityResponseDto;
import com.oasys.helpdesk.response.SubCategoryResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;



@Service
public class HelpDeskSlaService {

	@Autowired
	ActionTakenRepository helpDeskActionTakenRepository;
	
	@Autowired
	AcutalProblemRepository helpDeskAcutalProblemRepository;
	
	@Autowired
	CommonDataController commonDataController;

	public GenericResponse getAllActionTaken() {
		List<ActionTaken> HelpDeskActionTakenList = helpDeskActionTakenRepository.findAll();
		if(HelpDeskActionTakenList==null ||HelpDeskActionTakenList.size()==0 ) {
	        	throw new RecordNotFoundException("No record found");
	    }
		if (HelpDeskActionTakenList.size() > 0) {
			List<ActionTakenResponseDto> HelpDeskActionTakenResponseDtoList = new ArrayList<ActionTakenResponseDto>();
			HelpDeskActionTakenList.forEach(pt -> {
				HelpDeskActionTakenResponseDtoList.add(convertHelpDeskActionTakenToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskActionTakenResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getActionTakenById(Long id) throws RecordNotFoundException {
		ActionTaken helpDeskActionTaken = helpDeskActionTakenRepository.getById(id);
		if (helpDeskActionTaken== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (helpDeskActionTaken!= null && helpDeskActionTaken.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskActionTakenToDto(helpDeskActionTaken), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	private ActionTakenResponseDto convertHelpDeskActionTakenToDto(ActionTaken helpDeskActionTaken) {

		ActionTakenResponseDto helpDeskActionTakenResponseDto = new ActionTakenResponseDto();
		helpDeskActionTakenResponseDto.setId(helpDeskActionTaken.getId());
		helpDeskActionTakenResponseDto.setActionTaken(helpDeskActionTaken.getActionTaken());
		Optional<ActualProblem> helpDeskActualProblem= helpDeskAcutalProblemRepository.findById(helpDeskActionTaken.getActualProblem().getId());
		ActualProblem actualProblem = helpDeskActualProblem.get();
		helpDeskActionTakenResponseDto.setActualProblem(actualProblem.getActualProblem());
		helpDeskActionTakenResponseDto.setActive(helpDeskActionTaken.isActive());
		String createduser=commonDataController.getUserNameById(actualProblem.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(actualProblem.getModifiedBy());
		helpDeskActionTakenResponseDto.setCreatedBy(createduser);
		helpDeskActionTakenResponseDto.setCreatedDate(helpDeskActionTaken.getCreatedDate().toString());
		helpDeskActionTakenResponseDto.setModifiedBy(modifieduser);
		helpDeskActionTakenResponseDto.setModifiedDate(helpDeskActionTaken.getModifiedDate().toString());
		return helpDeskActionTakenResponseDto;

	}
	
public GenericResponse getActionTakenByActionProblemId(Long actionproblemid) {
		
	List<ActionTaken> HelpDeskActionTakenList = helpDeskActionTakenRepository.getActionTakenByActionProblemId(actionproblemid);
        if(HelpDeskActionTakenList==null ||HelpDeskActionTakenList.size()==0 ) {
        	throw new RecordNotFoundException("No record found");
        }
        if (HelpDeskActionTakenList.size() > 0) {
			List<ActionTakenResponseDto> HelpDeskActionTakenResponseDtoList = new ArrayList<ActionTakenResponseDto>();
			HelpDeskActionTakenList.forEach(pt -> {
				HelpDeskActionTakenResponseDtoList.add(convertHelpDeskActionTakenToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskActionTakenResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		}  else {
			throw new RecordNotFoundException();
		}
	}

public GenericResponse createActionTaken(ActionTakenRequestDto actionTakenRequestDto)  throws RecordNotFoundException , Exception
{
	
	if (actionTakenRequestDto != null ) {
		ActionTaken helpDeskActionTaken = new ActionTaken();
		helpDeskActionTaken.setActionTaken(actionTakenRequestDto.getActionTaken());
        if(actionTakenRequestDto.getActualProblemId() !=null && actionTakenRequestDto.getActualProblemId()>0) {
        	ActualProblem helpDeskActualProblem= helpDeskAcutalProblemRepository.getById(actionTakenRequestDto.getActualProblemId());
        	helpDeskActionTaken.setActualProblem(helpDeskActualProblem);
        }
        helpDeskActionTaken.setActive(actionTakenRequestDto.isActive());
		helpDeskActionTakenRepository.save(helpDeskActionTaken);
		
		return Library.getSuccessfulResponse(helpDeskActionTaken, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
		
	} else {
		throw new RecordNotFoundException();
	}
} 


	
}
