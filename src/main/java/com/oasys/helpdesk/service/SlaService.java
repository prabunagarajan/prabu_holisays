package com.oasys.helpdesk.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SlaConfiguration;
import com.oasys.helpdesk.entity.SlaEmailTemplate;
import com.oasys.helpdesk.entity.SlaSmsTemplate;
import com.oasys.helpdesk.entity.SlaTemplate;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.SlaConfigurationRepository;
import com.oasys.helpdesk.repository.SlaEmailTemplateRepository;
import com.oasys.helpdesk.repository.SlaSmsTemplateRepository;
import com.oasys.helpdesk.repository.SlaTemplateRepository;
import com.oasys.helpdesk.request.ActionTakenRequestDto;
import com.oasys.helpdesk.request.ActualProblemRequestDto;
import com.oasys.helpdesk.request.SlaConfigurationRequestDto;
import com.oasys.helpdesk.request.SlaEmailTemplateRequestDto;
import com.oasys.helpdesk.request.SlaSmsTemplateRequestDto;
import com.oasys.helpdesk.request.SlaTemplateRequestDto;
import com.oasys.helpdesk.response.ActionTakenResponseDto;
import com.oasys.helpdesk.response.ActualProblemResponseDto;
import com.oasys.helpdesk.response.CategoryResponseDto;
import com.oasys.helpdesk.response.IssueDetailsResponseDto;
import com.oasys.helpdesk.response.PriorityResponseDto;
import com.oasys.helpdesk.response.SubCategoryResponseDto;
import com.oasys.helpdesk.response.SlaConfigurationResponseDto;
import com.oasys.helpdesk.response.SlaEmailTemplateResponseDto;
import com.oasys.helpdesk.response.SlaSmsTemplateResponseDto;
import com.oasys.helpdesk.response.SlaTemplateResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;



@Service
@Log4j2
public class SlaService {

@Autowired
SlaEmailTemplateRepository slaEmailTemplateRepository;

@Autowired
SlaSmsTemplateRepository slaSmsTemplateRepository;

@Autowired
SlaConfigurationRepository slaConfigurationRepository;

@Autowired
CategoryRepository helpDeskTicketCategoryRepository;

@Autowired
SubCategoryRepository helpDeskTicketSubCategoryRepository;

@Autowired
CommonDataController commonDataController;

@Autowired
SlaTemplateRepository slaTemplateRepository;

@Autowired
EntityManager entityManager;

public GenericResponse createSlaemailTemplate(SlaEmailTemplateRequestDto slaEmailTemplateRequestDto)  throws RecordNotFoundException , Exception
{
	
	if (slaEmailTemplateRequestDto != null ) {
		SlaEmailTemplate slaEmailTemplate = new SlaEmailTemplate();
		slaEmailTemplate.setTemplateName(slaEmailTemplateRequestDto.getTemplateName());
		slaEmailTemplate.setEmailCcList(slaEmailTemplateRequestDto.getEmailccList());
		slaEmailTemplate.setEmailSenderList(slaEmailTemplateRequestDto.getEmailSenderList());
		slaEmailTemplate.setEmailSubject(slaEmailTemplateRequestDto.getEmailSubject());
		slaEmailTemplate.setMessage(slaEmailTemplateRequestDto.getMessage());
		slaEmailTemplate.setActive(slaEmailTemplateRequestDto.isActive());
        slaEmailTemplateRepository.save(slaEmailTemplate);
		
		return Library.getSuccessfulResponse(slaEmailTemplate, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
		
	} else {
		throw new RecordNotFoundException();
	}
} 


    public GenericResponse getAllslaemailtemplate() {
	List<SlaEmailTemplate> SlaEmailTemplateList = slaEmailTemplateRepository.findAll();
	if(SlaEmailTemplateList==null ||SlaEmailTemplateList.size()==0 ) {
        	throw new RecordNotFoundException("No record found");
    }
	if (SlaEmailTemplateList.size() > 0) {
		List<SlaEmailTemplateResponseDto> SlaEmailTemplateResponseDtoList = new ArrayList<SlaEmailTemplateResponseDto>();
		SlaEmailTemplateList.forEach(pt -> {
			SlaEmailTemplateResponseDtoList.add(convertSlaEmailTemplateToDto(pt));
		});

		return Library.getSuccessfulResponse(SlaEmailTemplateResponseDtoList,
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	} else {
		throw new RecordNotFoundException();
	}
}
      
    
    public GenericResponse getSlaEmailTemplateById(Long id) throws RecordNotFoundException {
		SlaEmailTemplate slaEmailTemplate = slaEmailTemplateRepository.getById(id);
		if (slaEmailTemplate== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (slaEmailTemplate!= null && slaEmailTemplate.getId() != null) {
			return Library.getSuccessfulResponse(convertSlaEmailTemplateToDto(slaEmailTemplate), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}

    private SlaEmailTemplateResponseDto convertSlaEmailTemplateToDto(SlaEmailTemplate slaEmailTemplate) {

    	SlaEmailTemplateResponseDto slaEmailTemplateResponseDto = new SlaEmailTemplateResponseDto();
    	slaEmailTemplateResponseDto.setId(slaEmailTemplate.getId());
    	slaEmailTemplateResponseDto.setTemplateName(slaEmailTemplate.getTemplateName());
    	slaEmailTemplateResponseDto.setEmailccList(slaEmailTemplate.getEmailCcList());
    	slaEmailTemplateResponseDto.setEmailSenderList(slaEmailTemplate.getEmailSenderList());
    	slaEmailTemplateResponseDto.setEmailSubject(slaEmailTemplate.getEmailSubject());
    	slaEmailTemplateResponseDto.setMessage(slaEmailTemplate.getMessage());
		slaEmailTemplateResponseDto.setActive(slaEmailTemplate.isActive());
		String createduser=commonDataController.getUserNameById(slaEmailTemplate.getCreatedBy());
		slaEmailTemplateResponseDto.setCreatedBy(createduser);
		slaEmailTemplateResponseDto.setCreatedDate(slaEmailTemplate.getCreatedDate());
		String modifieduser=commonDataController.getUserNameById(slaEmailTemplate.getModifiedBy());
		slaEmailTemplateResponseDto.setModifiedBy(modifieduser);
		slaEmailTemplateResponseDto.setModifiedDate(slaEmailTemplate.getModifiedDate());
		return slaEmailTemplateResponseDto;

	}
    
    public GenericResponse createSlaSmsTemplate(SlaSmsTemplateRequestDto slaSmsTemplateRequestDto)  throws RecordNotFoundException , Exception
    {
    	
    	if (slaSmsTemplateRequestDto != null ) {
    		SlaSmsTemplate slaSmsTemplate = new SlaSmsTemplate();
    		slaSmsTemplate.setTemplateName(slaSmsTemplateRequestDto.getTemplateName());
    		slaSmsTemplate.setMobileNumberList(slaSmsTemplateRequestDto.getMobileNumberList());
    		slaSmsTemplate.setMessage(slaSmsTemplateRequestDto.getMessage());
    		slaSmsTemplate.setActive(slaSmsTemplateRequestDto.isActive());
    		slaSmsTemplateRepository.save(slaSmsTemplate);
    		
    		return Library.getSuccessfulResponse(slaSmsTemplate, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
    				ErrorMessages.RECORED_CREATED);
    		
    	} else {
    		throw new RecordNotFoundException();
    	}
    } 
    
    public GenericResponse getAllslaSmstemplate() {
    	List<SlaSmsTemplate> SlaSmsTemplateList = slaSmsTemplateRepository.findAll();
    	if(SlaSmsTemplateList==null ||SlaSmsTemplateList.size()==0 ) {
            	throw new RecordNotFoundException("No record found");
        }
    	if (SlaSmsTemplateList.size() > 0) {
    		List<SlaSmsTemplateResponseDto> SlaSmsTemplateResponseDtoList  = new ArrayList<SlaSmsTemplateResponseDto>();
    		SlaSmsTemplateList.forEach(pt -> {
    			SlaSmsTemplateResponseDtoList.add(convertSlaSmsTemplateToDto(pt));
    		});

    		return Library.getSuccessfulResponse(SlaSmsTemplateResponseDtoList,
    				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    	} else {
    		throw new RecordNotFoundException();
    	}
    }
    
    public GenericResponse getSlaSmsTemplateById(Long id) throws RecordNotFoundException {
		SlaSmsTemplate slaSmsTemplate = slaSmsTemplateRepository.getById(id);
		if (slaSmsTemplate== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (slaSmsTemplate!= null && slaSmsTemplate.getId() != null) {
			return Library.getSuccessfulResponse(convertSlaSmsTemplateToDto(slaSmsTemplate), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
    
    private SlaSmsTemplateResponseDto convertSlaSmsTemplateToDto(SlaSmsTemplate slaSmsTemplate) {

    	SlaSmsTemplateResponseDto slaSmsTemplateResponseDto = new SlaSmsTemplateResponseDto();
    	slaSmsTemplateResponseDto.setId(slaSmsTemplate.getId());
    	slaSmsTemplateResponseDto.setTemplateName(slaSmsTemplate.getTemplateName());
    	slaSmsTemplateResponseDto.setMobileNumberList(slaSmsTemplate.getMobileNumberList());
        slaSmsTemplateResponseDto.setMessage(slaSmsTemplate.getMessage());
    	slaSmsTemplateResponseDto.setActive(slaSmsTemplate.isActive());
    	String createduser=commonDataController.getUserNameById(slaSmsTemplate.getCreatedBy());
        slaSmsTemplateResponseDto.setCreatedBy(createduser);
    	slaSmsTemplateResponseDto.setCreatedDate(slaSmsTemplate.getCreatedDate());
    	String modifieduser=commonDataController.getUserNameById(slaSmsTemplate.getModifiedBy());
    	slaSmsTemplateResponseDto.setModifiedBy(modifieduser);
    	slaSmsTemplateResponseDto.setModifiedDate(slaSmsTemplate.getModifiedDate());
		return slaSmsTemplateResponseDto;

	}
    
    
    public GenericResponse createSlaConfiguration(SlaConfigurationRequestDto slaConfigurationRequestDto)  throws RecordNotFoundException , Exception
    {
    	
    	if (slaConfigurationRequestDto != null ) {
    		SlaConfiguration slaConfiguration = new SlaConfiguration();
    		slaConfiguration.setRuleName(slaConfigurationRequestDto.getRuleName());
    		slaConfiguration.setPriority(slaConfigurationRequestDto.getPriority());
    		slaConfiguration.setStatus(slaConfigurationRequestDto.getStatus());
    		//slaConfiguration.setUserAssignee(slaConfigurationRequestDto.getUserAssignee());
    		//slaConfiguration.setGroupAssignee(slaConfigurationRequestDto.getGroupAssignee());
    		slaConfiguration.setThresholdTime(slaConfigurationRequestDto.getThresholdTime());
    		if(slaConfigurationRequestDto.getEmailTemplateId() !=null && slaConfigurationRequestDto.getEmailTemplateId()>0) {
    			SlaEmailTemplate slaEmailTemplate= slaEmailTemplateRepository.getById(slaConfigurationRequestDto.getEmailTemplateId());
    			slaConfiguration.setSlaEmailmatser(slaEmailTemplate);
    		}
    		if(slaConfigurationRequestDto.getSmsTemplateId() !=null && slaConfigurationRequestDto.getSmsTemplateId()>0) {
    			SlaSmsTemplate slaSmsTemplate= slaSmsTemplateRepository.getById(slaConfigurationRequestDto.getSmsTemplateId());
    			slaConfiguration.setSlaSmsmatser(slaSmsTemplate);
    		}
    		if(slaConfigurationRequestDto.getTicketCategoryId()!=null && slaConfigurationRequestDto.getTicketCategoryId()>0) {
    			Category ticketcategory = helpDeskTicketCategoryRepository.getById(slaConfigurationRequestDto.getTicketCategoryId());
    			slaConfiguration.setTicketCategory(ticketcategory);
    		}
    		if(slaConfigurationRequestDto.getTicketSubCategoryId()!=null && slaConfigurationRequestDto.getTicketSubCategoryId()>0) {
    			SubCategory ticketsubcategory= helpDeskTicketSubCategoryRepository.getById(slaConfigurationRequestDto.getTicketSubCategoryId());
    			slaConfiguration.setTicketSubCategory(ticketsubcategory);
    		}
    		
    		slaConfiguration.setTemplateType(slaConfigurationRequestDto.getTemplateType());
    		
    		slaConfiguration.setActive(slaConfigurationRequestDto.isActive());
    		slaConfigurationRepository.save(slaConfiguration);
    		
    		return Library.getSuccessfulResponse(slaConfiguration, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
    				ErrorMessages.RECORED_CREATED);
    		
    	} else {
    		throw new RecordNotFoundException();
    	}
    } 
    
    public GenericResponse getAllSlaConfiguration() {
    	List<SlaConfiguration> SlaConfigurationList = slaConfigurationRepository.findAll();
    	if(SlaConfigurationList==null ||SlaConfigurationList.size()==0 ) {
            	throw new RecordNotFoundException("No record found");
        }
    	if (SlaConfigurationList.size() > 0) {
    		List<SlaConfigurationResponseDto> SlaConfigurationResponseDtoList  = new ArrayList<SlaConfigurationResponseDto>();
    		SlaConfigurationList.forEach(pt -> {
    			SlaConfigurationResponseDtoList.add(convertSlaConfigurationToDto(pt));
    		});

    		return Library.getSuccessfulResponse(SlaConfigurationResponseDtoList,
    				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    	} else {
    		throw new RecordNotFoundException();
    	}
    }
    
    public GenericResponse searchSlaByRuleName(String ruleName) {
    	List<SlaConfiguration> SlaConfigurationList = slaConfigurationRepository.searchSlaByRuleName(ruleName);
    	if(SlaConfigurationList==null ||SlaConfigurationList.size()==0 ) {
            	throw new RecordNotFoundException("No record found");
        }
    	if (SlaConfigurationList.size() > 0) {
    		List<SlaConfigurationResponseDto> SlaConfigurationResponseDtoList  = new ArrayList<SlaConfigurationResponseDto>();
    		SlaConfigurationList.forEach(pt -> {
    			SlaConfigurationResponseDtoList.add(convertSlaConfigurationToDto(pt));
    		});

    		return Library.getSuccessfulResponse(SlaConfigurationResponseDtoList,
    				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    	} else {
    		throw new RecordNotFoundException();
    	}
    }
    
    public GenericResponse getSlaConfigurationById(Long id) throws RecordNotFoundException {
		SlaConfiguration slaConfiguration = slaConfigurationRepository.getById(id);
		if (slaConfiguration== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (slaConfiguration!= null && slaConfiguration.getId() != null) {
			return Library.getSuccessfulResponse(convertSlaConfigurationToDto(slaConfiguration), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
    
 
    
    private SlaConfigurationResponseDto convertSlaConfigurationToDto(SlaConfiguration slaConfiguration) {

    	SlaConfigurationResponseDto slaConfigurationResponseDto = new SlaConfigurationResponseDto();
    	slaConfigurationResponseDto.setId(slaConfiguration.getId());
    	slaConfigurationResponseDto.setRuleName(slaConfiguration.getRuleName());
        slaConfigurationResponseDto.setGroupAssignee(slaConfiguration.getGroupAssignee());
        slaConfigurationResponseDto.setPriority(slaConfiguration.getPriority());
        slaConfigurationResponseDto.setStatus(slaConfiguration.getStatus());
        slaConfigurationResponseDto.setThresholdTime(slaConfiguration.getThresholdTime());
        slaConfigurationResponseDto.setUserAssignee(slaConfiguration.getUserAssignee());
        slaConfigurationResponseDto.setTemplateType(slaConfiguration.getTemplateType());
        if(slaConfiguration.getTemplateType().equalsIgnoreCase("EMAIL")||slaConfiguration.getTemplateType().equalsIgnoreCase("BOTH")) {
        SlaEmailTemplate slaEmailTemplate= slaEmailTemplateRepository.getById(slaConfiguration.getSlaEmailmatser().getId());
        slaConfigurationResponseDto.setSlaEmailName(slaEmailTemplate.getTemplateName());
        }
        if(slaConfiguration.getTemplateType().equalsIgnoreCase("SMS")||slaConfiguration.getTemplateType().equalsIgnoreCase("BOTH")) {
        SlaSmsTemplate slaSmsTemplate= slaSmsTemplateRepository.getById(slaConfiguration.getSlaSmsmatser().getId());
        slaConfigurationResponseDto.setSlaSmsName(slaSmsTemplate.getTemplateName());
        }
        Category ticketcategory = helpDeskTicketCategoryRepository.getById(slaConfiguration.getTicketCategory().getId());
        slaConfigurationResponseDto.setCategoryName(ticketcategory.getCategoryName());
        SubCategory subcategory= helpDeskTicketSubCategoryRepository.getById(slaConfiguration.getTicketSubCategory().getId());
        slaConfigurationResponseDto.setSubcategoryName(subcategory.getSubCategoryName());
    	slaConfigurationResponseDto.setActive(slaConfiguration.isActive());
    	String createduser=commonDataController.getUserNameById(slaConfiguration.getCreatedBy());
    	String modifieduser=commonDataController.getUserNameById(slaConfiguration.getModifiedBy());
    	slaConfigurationResponseDto.setCreatedBy(createduser);
    	slaConfigurationResponseDto.setCreatedDate(slaConfiguration.getCreatedDate());
    	slaConfigurationResponseDto.setModifiedBy(modifieduser);
    	slaConfigurationResponseDto.setModifiedDate(slaConfiguration.getModifiedDate());
		return slaConfigurationResponseDto;

	}
    
    
    public GenericResponse createSlaTemplate(SlaTemplateRequestDto slaTemplateRequestDto)  throws RecordNotFoundException , Exception
    {
    	
    	if (slaTemplateRequestDto != null ) {
    		SlaTemplate slaTemplate = new SlaTemplate();
    		slaTemplate.setTemplateType(slaTemplateRequestDto.getTemplateType());
    		slaTemplate.setTemplateName(slaTemplateRequestDto.getTemplateName());
    		if(slaTemplateRequestDto.getTemplateType().equalsIgnoreCase("EMAIL")||
    		slaTemplateRequestDto.getTemplateType().equalsIgnoreCase("BOTH")) {
    		slaTemplate.setSubject(slaTemplateRequestDto.getSubject());
    		slaTemplate.setFromAddress(slaTemplateRequestDto.getFromAddress());
    		slaTemplate.setCcAddress(slaTemplateRequestDto.getCcAddress());
    		slaTemplate.setDescription(slaTemplateRequestDto.getDescription());
    		}
    		if(slaTemplateRequestDto.getTemplateType().equalsIgnoreCase("SMS")||
    	    slaTemplateRequestDto.getTemplateType().equalsIgnoreCase("BOTH")) {
    		slaTemplate.setMobileNumber(slaTemplateRequestDto.getMobileNumber());
    		slaTemplate.setMessage(slaTemplateRequestDto.getMessage());
    		}
    		slaTemplate.setActive(slaTemplateRequestDto.isActive());
    		slaTemplateRepository.save(slaTemplate);
    		
    		return Library.getSuccessfulResponse(slaTemplate, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
    				ErrorMessages.RECORED_CREATED);
    		
    	} else {
    		throw new RecordNotFoundException();
    	}
    } 

    public GenericResponse getAllSlaTemplate() {
    	List<SlaTemplate> SlaTemplateList = slaTemplateRepository.findAll();
    	if(SlaTemplateList==null ||SlaTemplateList.size()==0 ) {
            	throw new RecordNotFoundException("No record found");
        }
    	if (SlaTemplateList.size() > 0) {
    		List<SlaTemplateResponseDto> SlaTemplateResponseDtoList = new ArrayList<SlaTemplateResponseDto>();
    		SlaTemplateList.forEach(pt -> {
    			SlaTemplateResponseDtoList.add(convertSlaTemplateToDto(pt));
    		});

    		return Library.getSuccessfulResponse(SlaTemplateResponseDtoList,
    				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    	} else {
    		throw new RecordNotFoundException();
    	}
    }
    
    public GenericResponse getSlaTemplateById(Long id) throws RecordNotFoundException {
		SlaTemplate slaTemplate = slaTemplateRepository.getById(id);
		if (slaTemplate== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (slaTemplate!= null && slaTemplate.getId() != null) {
			return Library.getSuccessfulResponse(convertSlaTemplateToDto(slaTemplate), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
    
    private SlaTemplateResponseDto convertSlaTemplateToDto(SlaTemplate slaTemplate) {

    	SlaTemplateResponseDto slaTemplateResponseDto = new SlaTemplateResponseDto();
    	slaTemplateResponseDto.setId(slaTemplate.getId());
    	slaTemplateResponseDto.setTemplateType(slaTemplate.getTemplateType());
    	slaTemplateResponseDto.setTemplateName(slaTemplate.getTemplateName());
    	if(slaTemplate.getTemplateType().equalsIgnoreCase("EMAIL")||
    	   slaTemplate.getTemplateType().equalsIgnoreCase("BOTH")) {
    	slaTemplateResponseDto.setSubject(slaTemplate.getSubject());	
    	slaTemplateResponseDto.setFromAddress(slaTemplate.getFromAddress());
    	slaTemplateResponseDto.setCcAddress(slaTemplate.getCcAddress());
    	slaTemplateResponseDto.setDescription(slaTemplate.getDescription());
    	}
    	if(slaTemplate.getTemplateType().equalsIgnoreCase("SMS")||
    	   slaTemplate.getTemplateType().equalsIgnoreCase("BOTH")) {
    	slaTemplateResponseDto.setMobileNumber(slaTemplate.getMobileNumber());  
    	slaTemplateResponseDto.setMessage(slaTemplate.getMessage());
    	}
    	slaTemplateResponseDto.setActive(slaTemplate.isActive());
		String createduser=commonDataController.getUserNameById(slaTemplate.getCreatedBy());
		slaTemplateResponseDto.setCreatedBy(createduser);
		slaTemplateResponseDto.setCreatedDate(slaTemplate.getCreatedDate());
		String modifieduser=commonDataController.getUserNameById(slaTemplate.getModifiedBy());
		slaTemplateResponseDto.setModifiedBy(modifieduser);
		slaTemplateResponseDto.setModifiedDate(slaTemplate.getModifiedDate());
		return slaTemplateResponseDto;

	}
    
    public GenericResponse slaTemplateSearch(PaginationRequestDTO paginationDto) throws ParseException {
    	List<SlaTemplateResponseDto> slaTemplateResponseDtoList = new ArrayList<>();
    	StringBuilder recordQuery = new StringBuilder("Select * from sla_template template ");
    	StringBuilder countQuery = new StringBuilder("Select count(*) from sla_template template "); 

    	
    	
    	if (paginationDto.getFilters() != null) {
    		log.info("template filters :::" + paginationDto.getFilters());

    		if (paginationDto.getFilters().get("id") != null
    				&& !paginationDto.getFilters().get("id").toString().trim().isEmpty()) {
    			Long id = Long.parseLong(paginationDto.getFilters().get("id").toString());

    			recordQuery.append(" and template.id = " + id);
    			countQuery.append(" and template.id = " + id);

    		}
    		if (paginationDto.getFilters().get("templateName") != null
    				&& !paginationDto.getFilters().get("templateName").toString().trim().isEmpty()) {
    			String templateName = paginationDto.getFilters().get("templateName").toString();
    			log.debug("templateName :"+ paginationDto.getFilters().get("templateName"));
    			recordQuery.append( " and template.template_name = '"+templateName +"'");
    			countQuery.append( " and template.template_name = '"+templateName+"'");
    		}
    		
    		
    		
    		if (paginationDto.getFilters().get("templateType") != null
    				&& !paginationDto.getFilters().get("templateType").toString().trim().isEmpty()) {
    			String templateType = paginationDto.getFilters().get("templateType").toString();
    			log.debug("templateType :"+ paginationDto.getFilters().get("templateType"));
    			recordQuery.append( " and template.template_type = '"+templateType +"'");
    			countQuery.append( " and template.template_type = '"+templateType+"'");
    		}
    		
    		if (paginationDto.getFilters().get("isActive") != null
    				&& !paginationDto.getFilters().get("isActive").toString().trim().isEmpty()) {
    			String isActive = paginationDto.getFilters().get("isActive").toString();
    			log.debug("isActive :"+ paginationDto.getFilters().get("isActive"));
    			recordQuery.append( " and template.is_active = '"+isActive +"'");
    			countQuery.append( " and template.is_active = '"+isActive+"'");
    		}
    		
    		

    	}
    	
    	BigInteger totalCount1 = (BigInteger) entityManager.createNativeQuery(countQuery.toString()).getSingleResult();
    	Integer totalCount = totalCount1.intValue();

    	javax.persistence.Query query = entityManager.createNativeQuery(recordQuery.toString(),
    			SlaTemplate.class);
    	query.setFirstResult(paginationDto.getPageNo() * paginationDto.getPaginationSize());
    	query.setMaxResults(paginationDto.getPaginationSize());
    	List<SlaTemplate> resultList = query.getResultList();
    	if (resultList.isEmpty()) {
    		throw new RecordNotFoundException();
    	} else {
    		for (SlaTemplate slaTemplate : resultList) {
    			SlaTemplateResponseDto slaTemplateResponseDto = new SlaTemplateResponseDto();
    	
    			slaTemplateResponseDto.setId(slaTemplate.getId());
    	    	slaTemplateResponseDto.setTemplateType(slaTemplate.getTemplateType());
    	    	slaTemplateResponseDto.setTemplateName(slaTemplate.getTemplateName());
    	    	if(slaTemplate.getTemplateType().equalsIgnoreCase("EMAIL")||
    	        slaTemplate.getTemplateType().equalsIgnoreCase("BOTH")) {
    	    	slaTemplateResponseDto.setSubject(slaTemplate.getSubject());	
     	    	slaTemplateResponseDto.setFromAddress(slaTemplate.getFromAddress());
    	    	slaTemplateResponseDto.setCcAddress(slaTemplate.getCcAddress());
    	    	slaTemplateResponseDto.setDescription(slaTemplate.getDescription());
    	    	}
    	    	if(slaTemplate.getTemplateType().equalsIgnoreCase("SMS")||
    	    	slaTemplate.getTemplateType().equalsIgnoreCase("BOTH")) {
    	    	slaTemplateResponseDto.setMobileNumber(slaTemplate.getMobileNumber());  
    	    	slaTemplateResponseDto.setMessage(slaTemplate.getMessage());
    	    	}
    	    	slaTemplateResponseDto.setActive(slaTemplate.isActive());
    			String createduser=commonDataController.getUserNameById(slaTemplate.getCreatedBy());
    			slaTemplateResponseDto.setCreatedBy(createduser);
    			slaTemplateResponseDto.setCreatedDate(slaTemplate.getCreatedDate());
    			String modifieduser=commonDataController.getUserNameById(slaTemplate.getModifiedBy());
    			slaTemplateResponseDto.setModifiedBy(modifieduser);
    			slaTemplateResponseDto.setModifiedDate(slaTemplate.getModifiedDate());
    			slaTemplateResponseDtoList.add(slaTemplateResponseDto);
    		}
    		log.debug("found records : " + slaTemplateResponseDtoList.size());

    		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
    		paginationResponseDTO.setContents(slaTemplateResponseDtoList);
    		paginationResponseDTO.setTotalElements(totalCount.longValue());
    		paginationResponseDTO.setNumberOfElements(resultList.size());
    		if (paginationDto.getPaginationSize() > 0 && totalCount > 0) {
    			paginationResponseDTO.setTotalPages(totalCount / paginationDto.getPaginationSize());
    			if (totalCount < paginationDto.getPaginationSize())
    				paginationResponseDTO.setTotalPages(1);
    			else if (totalCount % paginationDto.getPaginationSize() > 0) {
    				paginationResponseDTO.setTotalPages(paginationResponseDTO.getTotalPages() + 1);
    			}

    		} else
    			paginationResponseDTO.setTotalPages(0);

    		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
    				ErrorMessages.RECORED_FOUND);

    	}
    }
    
    
    public GenericResponse editSlaConfiguration(SlaConfigurationRequestDto slaConfigurationRequestDto)  throws RecordNotFoundException , Exception
    {
    	if (Objects.isNull(slaConfigurationRequestDto.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
	    }
    	SlaConfiguration slaConfiguration= slaConfigurationRepository.getById(slaConfigurationRequestDto.getId());
    	if(Objects.isNull(slaConfiguration)) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
		}
    	if (slaConfiguration.getId() != null ) {
    		
    		slaConfiguration.setRuleName(slaConfigurationRequestDto.getRuleName());
    		slaConfiguration.setPriority(slaConfigurationRequestDto.getPriority());
    		slaConfiguration.setStatus(slaConfigurationRequestDto.getStatus());
    		//slaConfiguration.setUserAssignee(slaConfigurationRequestDto.getUserAssignee());
    		//slaConfiguration.setGroupAssignee(slaConfigurationRequestDto.getGroupAssignee());
    		slaConfiguration.setThresholdTime(slaConfigurationRequestDto.getThresholdTime());
    		if(slaConfigurationRequestDto.getEmailTemplateId() !=null && slaConfigurationRequestDto.getEmailTemplateId()>0) {
    			SlaEmailTemplate slaEmailTemplate= slaEmailTemplateRepository.getById(slaConfigurationRequestDto.getEmailTemplateId());
    			slaConfiguration.setSlaEmailmatser(slaEmailTemplate);
    		}
    		if(slaConfigurationRequestDto.getSmsTemplateId() !=null && slaConfigurationRequestDto.getSmsTemplateId()>0) {
    			SlaSmsTemplate slaSmsTemplate= slaSmsTemplateRepository.getById(slaConfigurationRequestDto.getSmsTemplateId());
    			slaConfiguration.setSlaSmsmatser(slaSmsTemplate);
    		}
    		if(slaConfigurationRequestDto.getTicketCategoryId()!=null && slaConfigurationRequestDto.getTicketCategoryId()>0) {
    			Category ticketcategory = helpDeskTicketCategoryRepository.getById(slaConfigurationRequestDto.getTicketCategoryId());
    			slaConfiguration.setTicketCategory(ticketcategory);
    		}
    		if(slaConfigurationRequestDto.getTicketSubCategoryId()!=null && slaConfigurationRequestDto.getTicketSubCategoryId()>0) {
    			SubCategory ticketsubcategory= helpDeskTicketSubCategoryRepository.getById(slaConfigurationRequestDto.getTicketSubCategoryId());
    			slaConfiguration.setTicketSubCategory(ticketsubcategory);
    		}
    		
    		slaConfiguration.setTemplateType(slaConfigurationRequestDto.getTemplateType());
    		
    		slaConfiguration.setActive(slaConfigurationRequestDto.isActive());
    		slaConfigurationRepository.save(slaConfiguration);
    		
    		return Library.getSuccessfulResponse(slaConfiguration, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
    				ErrorMessages.RECORED_CREATED);
    		
    	} else {
    		throw new RecordNotFoundException();
    	}
    } 
	
}
