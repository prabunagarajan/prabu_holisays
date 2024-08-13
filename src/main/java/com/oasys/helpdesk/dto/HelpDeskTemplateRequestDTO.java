package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpDeskTemplateRequestDTO {

    private Date date;

    private int templateId;

    private String templateType;

    private String templateName;

    private boolean status;

    private String mobileNumber;

    private String message;

    private String licenseNumber;

    private boolean smsStatus;

    private String fromEmail;

    private String toEmail;

    private String subject;

    private String description;

    private boolean emailStatus;

    private String licenseNumberForEmail;

}
