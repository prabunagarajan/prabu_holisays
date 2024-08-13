package com.oasys.helpdesk.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityMasterTypeRequestDTO {

    private String entityName;

    private boolean status;

    private String isParentUnitApplicable;

    private String entityCode;

}
