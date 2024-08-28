package com.devar.cabs.requestDTO;

import java.util.ArrayList;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PaginationRequestDTO {

	@Min(value = 0, message = "pageNo must be equals or greater then 0")
	Integer pageNo;

	@Min(value = 1, message = "paginationSize must be equals or greater then 1")
	Integer paginationSize;

	@NotBlank(message = "103")
	String sortField;

	@NotBlank(message = "103")
	String sortOrder;

	Map<String, Object> filters;

	String search;

	String fromDate;

	String toDate;
	
	String status;

}
