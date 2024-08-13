package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeBaseResponseDTO {


	private Long id;

    private Long kbId;

    private String subcategory;

    private String category;

    private String issueDetails;

    private String status;

    private String priority;

    private int sla;

    private Integer count;

    private String remarks;

    private String knowledgeSolution;

    private boolean isResolved;

    private String createdBy;

    private String createdDate;

    private String modifiedBy;

    private String modifiedDate;

    private Long categoryid;
    
    private Long subcategoryid;
    
    private Long issueDetailsId;    
}
