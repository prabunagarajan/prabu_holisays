package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.PRIORITY_NAME;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SlaConfiguration;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.repository.SlaConfigurationRepository;
import com.oasys.helpdesk.request.PriorityRequestDto;
import com.oasys.helpdesk.response.PriorityResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;



@Service
public class PriorityService {


	@Autowired
	PriorityRepository helpDeskTicketPriorityRepository;
	@Autowired
	SlaConfigurationRepository slaConfigurationRepository;
	@Autowired
	CommonDataController commonDataController;

	public GenericResponse getAllPriority() {
		List<Priority> HelpDeskTicketPriorityList = helpDeskTicketPriorityRepository.getallActivePriority();

		if (HelpDeskTicketPriorityList.size() > 0) {
			List<PriorityResponseDto> HelpDeskTicketPriorityResponseDtoList = new ArrayList<PriorityResponseDto>();
			HelpDeskTicketPriorityList.forEach(pt -> {
				HelpDeskTicketPriorityResponseDtoList.add(convertHelpDeskTocketPriorityToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketPriorityResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getPriorityById(Long id) throws RecordNotFoundException {
		Priority helpDeskTicketPriority = helpDeskTicketPriorityRepository.getById(id);
		if (helpDeskTicketPriority== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (helpDeskTicketPriority!= null && helpDeskTicketPriority.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskTocketPriorityToDto(helpDeskTicketPriority), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	private PriorityResponseDto convertHelpDeskTocketPriorityToDto(Priority helpDeskTicketPriority) {

		PriorityResponseDto helpDeskTicketPriorityResponseDto = new PriorityResponseDto();
		helpDeskTicketPriorityResponseDto.setId(helpDeskTicketPriority.getId());
		helpDeskTicketPriorityResponseDto.setPriority(helpDeskTicketPriority.getPriority());
		if(helpDeskTicketPriority.getSlaMaster().getId() !=null && helpDeskTicketPriority.getSlaMaster().getId()>0) {
			SlaConfiguration sla= slaConfigurationRepository.getById(helpDeskTicketPriority.getSlaMaster().getId());
			helpDeskTicketPriorityResponseDto.setSlaName(sla.getRuleName());
			helpDeskTicketPriorityResponseDto.setSlaDays(sla.getThresholdTime());
			helpDeskTicketPriorityResponseDto.setSlaId(sla.getId());
		}
		helpDeskTicketPriorityResponseDto.setActive(helpDeskTicketPriority.isActive());
		String createduser=commonDataController.getUserNameById(helpDeskTicketPriority.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(helpDeskTicketPriority.getModifiedBy());
		helpDeskTicketPriorityResponseDto.setCreatedBy(createduser);
		helpDeskTicketPriorityResponseDto.setCreatedDate(helpDeskTicketPriority.getCreatedDate());
		helpDeskTicketPriorityResponseDto.setModifiedBy(modifieduser);
		helpDeskTicketPriorityResponseDto.setModifiedDate(helpDeskTicketPriority.getModifiedDate());
		return helpDeskTicketPriorityResponseDto;

	}
	
	
	public GenericResponse createPriority(PriorityRequestDto priorityRequestDto )  throws RecordNotFoundException , Exception
	{

		Priority priority = new Priority();

		Priority prioritydup = helpDeskTicketPriorityRepository
				.findByName(priorityRequestDto.getPriorityName().toUpperCase());
		if (prioritydup != null) {
			priority.setPriority(priorityRequestDto.getPriorityName());
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { PRIORITY_NAME }));
		}
		SlaConfiguration sla = slaConfigurationRepository.getById(priorityRequestDto.getSlaconfigurationID());
		if (sla != null) {
			priority.setSlaMaster(sla);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
		}
		priority.setActive(priorityRequestDto.isActive());
		helpDeskTicketPriorityRepository.save(priority);
		return Library.getSuccessfulResponse(priorityRequestDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	
	} 
	
	public GenericResponse editPriority(PriorityRequestDto priorityRequestDto )  throws RecordNotFoundException , Exception
	{
		Priority priority = helpDeskTicketPriorityRepository.getById(priorityRequestDto.getId());
		if (priority.getId() != null ) {
		
			
			Priority prioritydup =helpDeskTicketPriorityRepository.findByName(priorityRequestDto.getPriorityName());
			if(prioritydup != null) {
				priority.setPriority(priorityRequestDto.getPriorityName());
			}
			else {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { PRIORITY_NAME }));
			}
			SlaConfiguration sla = slaConfigurationRepository.getById(priorityRequestDto.getSlaconfigurationID());
			if(sla !=null) {
			priority.setSlaMaster(sla);
			}
			else {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ErrorMessages.NO_RECORD_FOUND);
			}
			priority.setActive(priorityRequestDto.isActive());
			helpDeskTicketPriorityRepository.save(priority);
			return Library.getSuccessfulResponse(priorityRequestDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	} 
  
	
}
