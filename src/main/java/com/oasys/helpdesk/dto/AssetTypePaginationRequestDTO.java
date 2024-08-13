package com.oasys.helpdesk.dto;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetTypePaginationRequestDTO {
	@NotNull
	private Integer pageNo;

	@NotNull
	private Integer paginationSize;
	
	private String sortField;
	
	private String sortOrder;

	private Map<String, Object> filters;
	

}
