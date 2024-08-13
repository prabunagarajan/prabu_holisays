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
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.Status;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.repository.StatusRepository;
import com.oasys.helpdesk.response.PriorityResponseDto;
import com.oasys.helpdesk.response.StatusResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;



@Service
public class StatusService {

	@Autowired
	StatusRepository helpDeskTicketStatusRepository;
	@Autowired
	CommonDataController commonDataController;

	public GenericResponse getAllStatus() {
		List<Status> HelpDeskTicketStatusList = helpDeskTicketStatusRepository.getHelpDeskStatus();

		if (HelpDeskTicketStatusList.size() > 0) {
			List<StatusResponseDto> HelpDeskTicketStatusResponseDtoList = new ArrayList<StatusResponseDto>();
			HelpDeskTicketStatusList.forEach(pt -> {
				HelpDeskTicketStatusResponseDtoList.add(convertHelpDeskStatusToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketStatusResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getAllStatusForFieldAgent() {
		List<Status> HelpDeskTicketStatusList = helpDeskTicketStatusRepository.getHelpDeskStatusForFieldAgent();

		if (HelpDeskTicketStatusList.size() > 0) {
			List<StatusResponseDto> HelpDeskTicketStatusResponseDtoList = new ArrayList<StatusResponseDto>();
			HelpDeskTicketStatusList.forEach(pt -> {
				HelpDeskTicketStatusResponseDtoList.add(convertHelpDeskStatusToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketStatusResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getStatusById(Long id) throws RecordNotFoundException {
		Status helpDeskTicketStatus = helpDeskTicketStatusRepository.getById(id);
		if (helpDeskTicketStatus== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (helpDeskTicketStatus!= null && helpDeskTicketStatus.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskStatusToDto(helpDeskTicketStatus), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	private StatusResponseDto convertHelpDeskStatusToDto(Status helpDeskTicketStatus) {

		StatusResponseDto helpDeskTicketStatusResponseDto = new StatusResponseDto();
		helpDeskTicketStatusResponseDto.setId(helpDeskTicketStatus.getId());
		helpDeskTicketStatusResponseDto.setStatus(helpDeskTicketStatus.getStatus());
		helpDeskTicketStatusResponseDto.setActive(helpDeskTicketStatus.isActive());
		String createduser=commonDataController.getUserNameById(helpDeskTicketStatus.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(helpDeskTicketStatus.getModifiedBy());
		helpDeskTicketStatusResponseDto.setCreatedBy(createduser);
		helpDeskTicketStatusResponseDto.setCreatedDate(helpDeskTicketStatus.getCreatedDate());
		helpDeskTicketStatusResponseDto.setModifiedBy(modifieduser);
		helpDeskTicketStatusResponseDto.setModifiedDate(helpDeskTicketStatus.getModifiedDate());
		return helpDeskTicketStatusResponseDto;

	}

	
}
