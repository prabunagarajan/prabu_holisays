package com.oasys.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityMasterTypeResponseDTO {
    private Long sNo;

    private String entityName;

    private boolean status;

    private String isParentUnitApplicable;

    private String entityCode;

    private String createdBy;

    private String createdDate;

    private String modifiedBy;

    private String modifiedDate;

}
