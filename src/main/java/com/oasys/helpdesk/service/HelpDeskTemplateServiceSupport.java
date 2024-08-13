package com.oasys.helpdesk.service;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.HelpDeskTemplateRequestDTO;
import com.oasys.helpdesk.dto.HelpDeskTemplateResponseDTO;
import com.oasys.helpdesk.entity.HelpDeskTemplate;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.oasys.helpdesk.constant.Constant.*;

@Log4j2
@Component
public class HelpDeskTemplateServiceSupport {

    protected static HelpDeskTemplate toHelpDeskTemplateEntity(HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {
        HelpDeskTemplate helpDeskTemplate = new HelpDeskTemplate();
        helpDeskTemplate.setDate(helpDeskTemplateDTO.getDate());
        helpDeskTemplate.setTemplateId(helpDeskTemplateDTO.getTemplateId());
        helpDeskTemplate.setTemplateName(helpDeskTemplateDTO.getTemplateName());
        helpDeskTemplate.setTemplateType(helpDeskTemplateDTO.getTemplateType());
        helpDeskTemplate.setStatus(helpDeskTemplateDTO.isStatus());
        helpDeskTemplate.setMobileNumber(helpDeskTemplateDTO.getMobileNumber());
        helpDeskTemplate.setMessage(helpDeskTemplateDTO.getMessage());
        helpDeskTemplate.setSmsStatus(helpDeskTemplateDTO.isSmsStatus());
        helpDeskTemplate.setFromEmail(getFromEmailAddress());
        helpDeskTemplate.setToEmail(helpDeskTemplateDTO.getToEmail());
        helpDeskTemplate.setSubject(helpDeskTemplateDTO.getSubject());
        helpDeskTemplate.setDescription(helpDeskTemplateDTO.getDescription());
        helpDeskTemplate.setEmailStatus(helpDeskTemplateDTO.isEmailStatus());
        helpDeskTemplate.setLicenseNumberForEmail(helpDeskTemplateDTO.getLicenseNumberForEmail());
        helpDeskTemplate.setDate(helpDeskTemplateDTO.getDate());
        return helpDeskTemplate;
    }

    protected static String getFromEmailAddress() {
        return "Default";
    }

    protected static List<String> getTemplateTypes() {
        return Arrays.asList("Email", "SMS");
    }

    protected static HelpDeskTemplate toUpdateEntityMasterType(HelpDeskTemplate helpDeskTemplate, HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {
        helpDeskTemplate.setTemplateType(helpDeskTemplateDTO.getTemplateType());
        helpDeskTemplate.setDate(helpDeskTemplateDTO.getDate());
        helpDeskTemplate.setTemplateName(helpDeskTemplateDTO.getTemplateName());
        helpDeskTemplate.setStatus(helpDeskTemplateDTO.isStatus());


        if(SMS.equalsIgnoreCase(helpDeskTemplate.getTemplateType()) || BOTH.equalsIgnoreCase(helpDeskTemplate.getTemplateType())) {
            helpDeskTemplate.setMobileNumber(helpDeskTemplateDTO.getMobileNumber());
            helpDeskTemplate.setMessage(helpDeskTemplateDTO.getMessage());
            helpDeskTemplate.setLicenseNumber(helpDeskTemplateDTO.getLicenseNumber());
            helpDeskTemplate.setSmsStatus(helpDeskTemplateDTO.isSmsStatus());
        }

        if(EMAIL.equalsIgnoreCase(helpDeskTemplate.getTemplateType()) || BOTH.equalsIgnoreCase(helpDeskTemplate.getTemplateType())) {
            helpDeskTemplate.setToEmail(helpDeskTemplateDTO.getToEmail());
            helpDeskTemplate.setSubject(helpDeskTemplateDTO.getSubject());
            helpDeskTemplate.setDescription(helpDeskTemplateDTO.getDescription());
            helpDeskTemplate.setLicenseNumber(helpDeskTemplateDTO.getLicenseNumberForEmail());
            helpDeskTemplate.setStatus(helpDeskTemplateDTO.isStatus());
        }
        return helpDeskTemplate;
    }

    protected HelpDeskTemplateRequestDTO toHelpDeskTemplateDTO(HelpDeskTemplate entity) {
       return HelpDeskTemplateRequestDTO.builder()
               .templateId(entity.getTemplateId())
               .templateType(entity.getTemplateType())
               .templateName(entity.getTemplateName())
               .date(entity.getDate())
               .status(entity.isStatus())
               .mobileNumber(entity.getMobileNumber())
               .message(entity.getMessage())
               .licenseNumber(entity.getLicenseNumber())
               .smsStatus(entity.isSmsStatus())
               .fromEmail(entity.getFromEmail())
               .toEmail(entity.getToEmail())
               .subject(entity.getSubject())
               .description(entity.getSubject())
               .emailStatus(entity.isEmailStatus())
               .licenseNumberForEmail(entity.getLicenseNumberForEmail())
               .build();
    }

    protected HelpDeskTemplateResponseDTO toHelpDeskTemplateResponseDTO(HelpDeskTemplate entity) {
        return HelpDeskTemplateResponseDTO.builder()
                .id(entity.getId())
                .templateId(entity.getTemplateId())
                .templateType(entity.getTemplateType())
                .templateName(entity.getTemplateName())
                .date(entity.getDate())
                .status(entity.isStatus())
                .mobileNumber(entity.getMobileNumber())
                .message(entity.getMessage())
                .licenseNumber(entity.getLicenseNumber())
                .smsStatus(entity.isSmsStatus())
                .fromEmail(entity.getFromEmail())
                .toEmail(entity.getToEmail())
                .subject(entity.getSubject())
                .description(entity.getSubject())
                .emailStatus(entity.isEmailStatus())
                .licenseNumberForEmail(entity.getLicenseNumberForEmail())
                .createdDate(String.valueOf(entity.getDate()))
                .build();
    }

    protected static boolean isInvalidSMSRequest(HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {
        return StringUtils.isEmpty(helpDeskTemplateDTO.getMobileNumber()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.getMessage()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.getLicenseNumber()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.isStatus());
    }

    protected static boolean isInvalidEmailRequest(HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {
        return StringUtils.isEmpty(helpDeskTemplateDTO.getFromEmail()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.getToEmail()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.getSubject()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.getDescription()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.getLicenseNumber()) ||
                StringUtils.isEmpty(helpDeskTemplateDTO.isStatus());
    }

    protected static GenericResponse performMandatoryCheck(HelpDeskTemplateRequestDTO helpDeskTemplateRequestDTO) {
        if(SMS.equalsIgnoreCase(helpDeskTemplateRequestDTO.getTemplateType()) && isInvalidSMSRequest(helpDeskTemplateRequestDTO)) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{MANDATORY_SMS_TEMPLATE_FIELDS}));
        }

        if(EMAIL.equalsIgnoreCase(helpDeskTemplateRequestDTO.getTemplateType()) && isInvalidEmailRequest(helpDeskTemplateRequestDTO)) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{MANDATORY_EMAIL_TEMPLATE_FIELDS}));
        }

        if(BOTH.equalsIgnoreCase(helpDeskTemplateRequestDTO.getTemplateType()) && isInvalidEmailRequest(helpDeskTemplateRequestDTO)) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{MANDATORY_SMS_TEMPLATE_FIELDS, MANDATORY_EMAIL_TEMPLATE_FIELDS}));
        }
        return null;
    }

}
