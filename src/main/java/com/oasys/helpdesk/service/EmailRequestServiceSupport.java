package com.oasys.helpdesk.service;

import com.oasys.helpdesk.constant.EmailTemplate;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.EmailRequest;
import com.oasys.helpdesk.entity.SentEmailRequest;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.EmailRequestRepository;
import com.oasys.helpdesk.request.BaseTemplateMailDTO;
import com.oasys.helpdesk.request.EmailRequestCreationDto;
import com.oasys.helpdesk.response.EmailRequestResponseDto;
import com.oasys.helpdesk.response.SentEmailResponseDTO;
import com.oasys.helpdesk.utility.*;

import freemarker.template.Configuration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import static com.oasys.helpdesk.constant.Constant.SUBJECT;
import static java.util.Objects.isNull;

import java.util.Objects;

@Log4j2
@Component
public class EmailRequestServiceSupport {

    private static final String ER = "ER";
    @Autowired
    private EmailRequestRepository emailRequestRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    CommonDataController commonDataController;

    @Autowired
    private PaginationMapper paginationMapper;
    
    @Autowired
    private Configuration fmConfiguration;
 

    public static String getUniqueApplicationCode() {
        return MenuPrefix.getType(ER).toString() + RandomUtil.getRandomNumber();
    }

    public EmailRequestResponseDto toEmailResponseDTO(EmailRequest emailRequest) {

        EmailRequestResponseDto emailRequestResponseDto = new EmailRequestResponseDto();
        emailRequestResponseDto.setId(emailRequest.getId());
        emailRequestResponseDto.setApplicationNo(emailRequest.getApplicationNo());
        emailRequestResponseDto.setBccEmailList(emailRequest.getBccEmailList());
        emailRequestResponseDto.setCcEmailList(emailRequest.getCcEmailList());
        emailRequestResponseDto.setEmailBody(emailRequest.getEmailBody());
        emailRequestResponseDto.setEmailType(emailRequest.getEmailType());
        emailRequestResponseDto.setFromEmailId(emailRequest.getFromEmailId());
        emailRequestResponseDto.setValidEmail(emailRequest.isValidEmail());
        emailRequestResponseDto.setPriority(emailRequest.getPriority());
        emailRequestResponseDto.setSubject(emailRequest.getSubject());
        emailRequestResponseDto.setToEmailidList(emailRequest.getToEmailidList());
        emailRequestResponseDto.setActive(emailRequest.isActive());
        emailRequestResponseDto.setCreatedDate(emailRequest.getCreatedDate());
        emailRequestResponseDto.setModifiedDate(emailRequest.getModifiedDate());
		if (Objects.nonNull(emailRequest.getCreatedBy())) {
			emailRequestResponseDto.setCreatedBy(commonDataController.getUserNameById(emailRequest.getCreatedBy()));
		}
		
		if (Objects.nonNull(emailRequest.getModifiedBy())) {
			emailRequestResponseDto.setModifiedBy(commonDataController.getUserNameById(emailRequest.getModifiedBy()));
		}
        return emailRequestResponseDto;
    }
    
    
    public SentEmailResponseDTO toSentEmailResponseDTO(SentEmailRequest sentEmailRequest) {

    	SentEmailResponseDTO sentEmailResponseDTO = commonUtil.modalMap(sentEmailRequest, SentEmailResponseDTO.class);
       
		if (Objects.nonNull(sentEmailRequest.getCreatedBy())) {
			sentEmailResponseDTO.setCreatedBy(commonDataController.getUserNameById(sentEmailRequest.getCreatedBy()));
		}
		
		if (Objects.nonNull(sentEmailRequest.getModifiedBy())) {
			sentEmailResponseDTO.setModifiedBy(commonDataController.getUserNameById(sentEmailRequest.getModifiedBy()));
		}
        return sentEmailResponseDTO;
    }

    public EmailRequest toEmailRequestEntity(EmailRequestCreationDto creationDto) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setBccEmailList(creationDto.getBccEmailList());
        emailRequest.setApplicationNo(getUniqueApplicationCode());
        emailRequest.setCcEmailList(creationDto.getCcEmailList());
        emailRequest.setEmailBody(creationDto.getEmailBody());
        emailRequest.setEmailType(creationDto.getEmailType());
        emailRequest.setFromEmailId(creationDto.getFromEmailId());
        emailRequest.setValidEmail(creationDto.isValidEmail());
        emailRequest.setPriority(creationDto.getPriority());
        emailRequest.setSubject(creationDto.getSubject());
        emailRequest.setToEmailidList(creationDto.getToEmailidList());
        emailRequest.setActive(creationDto.isActive());
        return emailRequest;
    }

    protected static GenericResponse performMandatoryCheck(EmailRequestCreationDto emailRequestDto) {
        if(isNull(emailRequestDto.getSubject())) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{SUBJECT}));
        }
        return null;
    }

	public String geContentFromTemplate(BaseTemplateMailDTO baseTemplateMailDTO, EmailTemplate emailTemplate) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
					fmConfiguration.getTemplate(emailTemplate.getFileName()), CommonUtil.objectToMap(baseTemplateMailDTO)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content.toString();
	}
 

}
