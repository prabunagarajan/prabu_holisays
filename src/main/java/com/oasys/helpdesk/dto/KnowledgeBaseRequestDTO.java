package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeBaseRequestDTO {

    private Long kbId;

    private Long subcategoryId;

    private Long categoryId;

    private String issueDetails;

    private String status;

    private String priority;

    private int sla;


    private String remarks;

    private String knowledgeSolution;

    private boolean isResolved;
    
    private Long issueDetailsId;

}
