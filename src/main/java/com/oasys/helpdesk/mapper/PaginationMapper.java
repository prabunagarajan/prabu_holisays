package com.oasys.helpdesk.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.dto.PaginationResponseDTO;

@Component
public class PaginationMapper {
	
	public PaginationResponseDTO convertToResponseDTO(Page<?> pageable){
		PaginationResponseDTO responseDTO = new PaginationResponseDTO();
		responseDTO.setContents(pageable.getContent());
		responseDTO.setNumberOfElements(pageable.getNumberOfElements());
		responseDTO.setTotalElements(pageable.getTotalElements());
		responseDTO.setTotalPages(pageable.getTotalPages());
		return responseDTO;
	}
}
