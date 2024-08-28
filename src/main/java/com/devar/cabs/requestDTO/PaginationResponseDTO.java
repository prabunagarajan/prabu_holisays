package com.devar.cabs.requestDTO;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PaginationResponseDTO {

	Long TotalElements;

	Integer NumberOfElements;

	Integer TotalPages;

	List<?> Contents;

}
