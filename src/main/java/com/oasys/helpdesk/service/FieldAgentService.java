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
import com.oasys.helpdesk.entity.EmailRequest;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.FieldAgent;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.EmailRequestRepository;
import com.oasys.helpdesk.repository.FieldAgentRepository;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.repository.GroupRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.response.EmailRequestResponseDto;
import com.oasys.helpdesk.response.FieldAgentResponseDto;
import com.oasys.helpdesk.response.ActionTakenResponseDto;
import com.oasys.helpdesk.response.ActualProblemResponseDto;
import com.oasys.helpdesk.response.CategoryResponseDto;
import com.oasys.helpdesk.response.PriorityResponseDto;
import com.oasys.helpdesk.response.SubCategoryResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;



@Service
public class FieldAgentService {
	
	@Autowired
	FieldAgentRepository fieldAgentRepository;
	
	@Autowired
	GroupRepository helpDeskGroupRepository;
	
	@Autowired
	CommonDataController commonDataController;

	

//	public GenericResponse getAllEmailRequest() {
//		List<EmailRequest> emailRequestList = emailRequestRepository.findAll();
//		if(emailRequestList==null ||emailRequestList.size()==0 ) {
//	        	throw new RecordNotFoundException("No record found");
//	    }
//		if (emailRequestList.size() > 0) {
//			List<EmailRequestResponseDto> EmailRequestResponseDtoList = new ArrayList<EmailRequestResponseDto>();
//			emailRequestList.forEach(pt -> {
//				EmailRequestResponseDtoList.add(convertEmailRequestToDto(pt));
//			});
//
//			return Library.getSuccessfulResponse(EmailRequestResponseDtoList,
//					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//		} else {
//			throw new RecordNotFoundException();
//		}
//	}
	
	public GenericResponse getfieldagentByGroupId(Long groupid) throws RecordNotFoundException {
		List<FieldAgent> helpDeskFieldAgentList = fieldAgentRepository.getfieldagentByGroupId(groupid);
		if(helpDeskFieldAgentList==null ||helpDeskFieldAgentList.size()==0 ) {
	        	throw new RecordNotFoundException("No record found");
	    }
		if (helpDeskFieldAgentList.size() > 0) {
			List<FieldAgentResponseDto> FieldAgentResponseDtoList = new ArrayList<FieldAgentResponseDto>();
			helpDeskFieldAgentList.forEach(pt -> {
				FieldAgentResponseDtoList.add(convertFieldAgentToDto(pt));
			});

			return Library.getSuccessfulResponse(FieldAgentResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	private FieldAgentResponseDto convertFieldAgentToDto(FieldAgent helpDeskFieldAgent) {

		FieldAgentResponseDto fieldAgentResponseDto = new FieldAgentResponseDto();
		fieldAgentResponseDto.setId(helpDeskFieldAgent.getId());
		
		fieldAgentResponseDto.setActive(helpDeskFieldAgent.isActive());
		if(helpDeskFieldAgent.getGroup().getId() !=null) {
			Group group= helpDeskGroupRepository.getById(helpDeskFieldAgent.getGroup().getId());
			fieldAgentResponseDto.setGroupName(group.getName());
		}
		if(helpDeskFieldAgent.getMemberId()!=null) {
			String fieldagent=commonDataController.getUserNameById(helpDeskFieldAgent.getMemberId());
			fieldAgentResponseDto.setFieldAgentName(fieldagent);
			fieldAgentResponseDto.setFieldAgentid(helpDeskFieldAgent.getMemberId());
		}
	    String createduser=commonDataController.getUserNameById(helpDeskFieldAgent.getCreatedBy());
	    String modifieduser=commonDataController.getUserNameById(helpDeskFieldAgent.getModifiedBy());
	    fieldAgentResponseDto.setCreatedBy(createduser);
	    fieldAgentResponseDto.setCreatedDate(helpDeskFieldAgent.getCreatedDate());
	    fieldAgentResponseDto.setModifiedBy(modifieduser);
	    fieldAgentResponseDto.setModifiedDate(helpDeskFieldAgent.getModifiedDate());
		return fieldAgentResponseDto;

	}
	





	
}
