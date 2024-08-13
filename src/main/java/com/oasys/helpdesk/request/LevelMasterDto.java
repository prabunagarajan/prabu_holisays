package com.oasys.helpdesk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LevelMasterDto {

    private Long id;

    private String name;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

}
