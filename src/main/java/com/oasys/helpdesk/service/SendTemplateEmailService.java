package com.oasys.helpdesk.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.EmailTemplate;
import com.oasys.helpdesk.request.AcknowledgeAndOthersEmailDTO;
import com.oasys.helpdesk.response.CreateTicketResponseDto;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class SendTemplateEmailService {
	
	@Autowired
	private EmailRequestService emailRequestService;
	
	@Value("${email.customercare.contact.number}")
	private String contactNumber;

   
	private AcknowledgeAndOthersEmailDTO getAcknowledgeTemplateDTO(CreateTicketResponseDto createTicketResponseDto, EmailTemplate emiEmailTemplate)
	{		
		String name = createTicketResponseDto.getEmail().substring(0, createTicketResponseDto.getEmail().lastIndexOf("@"));
	    if(name.indexOf("\\.")!=-1) 	
	    {
	    	name = name.substring(0,createTicketResponseDto.getEmail().indexOf("\\."));
	    }	
		AcknowledgeAndOthersEmailDTO acknowledgeReceiptEmailDTO = new AcknowledgeAndOthersEmailDTO();
		acknowledgeReceiptEmailDTO.setCategory(createTicketResponseDto.getCategoryName());
		acknowledgeReceiptEmailDTO.setCustomerName(name);
		acknowledgeReceiptEmailDTO.setLicenseNo(createTicketResponseDto.getLicenceNumber());
		if(Objects.nonNull(createTicketResponseDto.getSla())) {
			acknowledgeReceiptEmailDTO.setSla(createTicketResponseDto.getSla().toString());
		}else {
			acknowledgeReceiptEmailDTO.setSla("0");
		}
		
		acknowledgeReceiptEmailDTO.setSubCategory(createTicketResponseDto.getSubCategoryName());
		acknowledgeReceiptEmailDTO.setSubject(emiEmailTemplate.getSubject());
		acknowledgeReceiptEmailDTO.setTicketID(createTicketResponseDto.getTicketNumber());
		acknowledgeReceiptEmailDTO.setTicketStatus(createTicketResponseDto.getTicketNumber());
		acknowledgeReceiptEmailDTO.setTimeStamp(createTicketResponseDto.getCreatedDate());
		acknowledgeReceiptEmailDTO.setLicenseNo(createTicketResponseDto.getLicenceNumber());
		acknowledgeReceiptEmailDTO.setToId(createTicketResponseDto.getEmail());
		acknowledgeReceiptEmailDTO.setTeamName(createTicketResponseDto.getAssignToName());
		acknowledgeReceiptEmailDTO.setTicketStatus(createTicketResponseDto.getTicketStatus());
		acknowledgeReceiptEmailDTO.setContactNumber(contactNumber);
		return acknowledgeReceiptEmailDTO;
	}
	
	public void sendAcknowledgeTemplate(CreateTicketResponseDto createTicketResponseDto)
	{		
		
		emailRequestService.replyEmailWithTemplate(getAcknowledgeTemplateDTO(createTicketResponseDto, EmailTemplate.ACKNOWLEDGE_RECEIPT) , EmailTemplate.ACKNOWLEDGE_RECEIPT);
	}
	
	public void sendUpdatingTemplate(CreateTicketResponseDto createTicketResponseDto)
	{		
		
		emailRequestService.replyEmailWithTemplate(getAcknowledgeTemplateDTO(createTicketResponseDto, EmailTemplate.UPDATE_TICKET_PROCESS) , EmailTemplate.UPDATE_TICKET_PROCESS);
	}
	
	public void sendResolvingTemplate(CreateTicketResponseDto createTicketResponseDto)
	{		
		emailRequestService.replyEmailWithTemplate(getAcknowledgeTemplateDTO(createTicketResponseDto,EmailTemplate.RESOLVING_TICKET) , EmailTemplate.RESOLVING_TICKET);
	}

}
