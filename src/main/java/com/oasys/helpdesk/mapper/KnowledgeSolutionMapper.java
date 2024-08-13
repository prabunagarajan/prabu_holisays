package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.KnowledgeSolutionRequestDTO;
import com.oasys.helpdesk.dto.KnowledgeSolutionResponseDTO;
import com.oasys.helpdesk.entity.KnowledgeSolution;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.utility.CommonDataController;


@Component
public class KnowledgeSolutionMapper {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public KnowledgeSolutionResponseDTO convertEntityTOResponseDTO(KnowledgeSolution solution) {
		
		KnowledgeSolutionResponseDTO knowledgeSolutionResponseDTO = new KnowledgeSolutionResponseDTO();
		knowledgeSolutionResponseDTO.setId(solution.getId());
		
		if (Objects.nonNull(solution.getCategoryId())) {
			knowledgeSolutionResponseDTO.setCategoryName(solution.getCategoryId().getCategoryName());
			knowledgeSolutionResponseDTO.setCategoryId(solution.getCategoryId().getId());
		}
		if (Objects.nonNull(solution.getSubcategoryId())) {
			knowledgeSolutionResponseDTO.setSubcategoryName(solution.getSubcategoryId().getSubCategoryName());
			knowledgeSolutionResponseDTO.setSubcategoryId(solution.getSubcategoryId().getId());
		}
		knowledgeSolutionResponseDTO.setSolution(solution.getSolution());
		knowledgeSolutionResponseDTO.setSolutionId(solution.getSolutionId());
		knowledgeSolutionResponseDTO.setIssueDetails(solution.getIssueDetails());
		knowledgeSolutionResponseDTO.setNotes(solution.getNotes());
		String createduser=commonDataController.getUserNameById(solution.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(solution.getModifiedBy());
		knowledgeSolutionResponseDTO.setCreatedBy(createduser);
		if (Objects.nonNull(solution.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			knowledgeSolutionResponseDTO.setCreatedDate(dateFormat.format(solution.getCreatedDate()));
		}

		knowledgeSolutionResponseDTO.setModifiedBy(modifieduser);
		if (Objects.nonNull(solution.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			knowledgeSolutionResponseDTO.setModifiedDate(dateFormat.format(solution.getModifiedDate()));
		}
		
		return knowledgeSolutionResponseDTO;
	}
	
	public KnowledgeSolution toKnowledgeSolutionEntity(KnowledgeSolutionRequestDTO requestDTO) {
		KnowledgeSolution knowledgeSolution = new KnowledgeSolution();
		knowledgeSolution.setCategoryId(categoryRepository.getById(requestDTO.getCategoryId()));
		knowledgeSolution.setSubcategoryId(subCategoryRepository.getById(requestDTO.getSubcategoryId()));
		knowledgeSolution.setIssueDetails(requestDTO.getIssueDetails());
		knowledgeSolution.setSolution(requestDTO.getSolution());
		knowledgeSolution.setSolutionId(requestDTO.getSolutionId());
		knowledgeSolution.setNotes(requestDTO.getNotes());
		return knowledgeSolution;
	}
}
