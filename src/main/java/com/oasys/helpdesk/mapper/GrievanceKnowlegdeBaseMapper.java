package com.oasys.helpdesk.mapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.dto.GrievanceKbResponseDTO;
import com.oasys.helpdesk.dto.GrievanceKnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.KnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.KnowledgeBaseResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievanceKnowledgeBase;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.GrievanceCategoryRepository;
import com.oasys.helpdesk.repository.GrievanceIssueDetailsRepository;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class GrievanceKnowlegdeBaseMapper {

	@Autowired
	private GrievanceCategoryRepository gcRepository;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private GrievanceIssueDetailsRepository gcDetailsRepository;
	
	
	
	
	
	public GrievanceKnowledgeBase toKnowledgeBaseEntity(GrievanceKnowledgeBaseRequestDTO requestDTO) {
		GrievanceKnowledgeBase knowledgeBase = new GrievanceKnowledgeBase();
		knowledgeBase.setCode(requestDTO.getCode());
		knowledgeBase.setCategoryId(gcRepository.getById(requestDTO.getCategoryId()));
		knowledgeBase.setIssueDetails(gcDetailsRepository.getById(requestDTO.getIssueDetails()));
		knowledgeBase.setStatus(requestDTO.isStatus());
		knowledgeBase.setPriority(requestDTO.getPriority());
		knowledgeBase.setSla(requestDTO.getSla());
		knowledgeBase.setCount(0);
		knowledgeBase.setRemarks(requestDTO.getRemarks());
		knowledgeBase.setKnowledgeSolution(requestDTO.getKnowledgeSolution());
		knowledgeBase.setResolved(requestDTO.isResolved());
		knowledgeBase.setTypeofUser(requestDTO.getTypeofUser());
		return knowledgeBase;
	}
	
	
	public GrievanceKbResponseDTO toKnowledgeBaseResponseDTO(GrievanceKnowledgeBase knowledgeBase) {
		Optional<GrievanceCategoryEntity> categoryOptional = gcRepository.findById(knowledgeBase.getCategoryId().getId());
		Optional<GrievanceIssueDetails> issueOptional = gcDetailsRepository.findById(knowledgeBase.getIssueDetails().getId());
		String createdByUserName = commonDataController.getUserNameById(knowledgeBase.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(knowledgeBase.getModifiedBy());
		return GrievanceKbResponseDTO.builder()
				.id(knowledgeBase.getId())
				.code(knowledgeBase.getCode())
				.category(categoryOptional.isPresent() ? categoryOptional.get().getCategoryName() : "")
				.issueDetails(issueOptional.isPresent() ? issueOptional.get().getIssueName() : "")
				.issueDetailsId(issueOptional.isPresent() ? issueOptional.get().getId(): 0)
			    .categoryid(categoryOptional.isPresent() ? categoryOptional.get().getId(): 0)
				.status(knowledgeBase.isStatus())
				.priority(knowledgeBase.getPriority())
				.sla(knowledgeBase.getSla())
				.count(knowledgeBase.getCount())
				.remarks(knowledgeBase.getRemarks())
				.knowledgeSolution(knowledgeBase.getKnowledgeSolution())
				.isResolved(knowledgeBase.isResolved())
				.createdBy(createdByUserName)
				.modifiedBy(modifiedByUserName)
				.typeofUser(knowledgeBase.getTypeofUser())
				.createdDate(String.valueOf(knowledgeBase.getCreatedDate()))
				.modifiedDate(String.valueOf(knowledgeBase.getModifiedDate()))
				.build();
	}
	
	public GrievanceKnowledgeBase toKnowledgeBaseUpdateEntity(GrievanceKnowledgeBaseRequestDTO requestDTO, GrievanceKnowledgeBase knowledgeBase) {
		knowledgeBase.setCategoryId(gcRepository.getById(requestDTO.getCategoryId()));
		knowledgeBase.setIssueDetails(gcDetailsRepository.getById(requestDTO.getIssueDetails()));
		knowledgeBase.setStatus(requestDTO.isStatus());
		knowledgeBase.setPriority(requestDTO.getPriority());
		knowledgeBase.setSla(requestDTO.getSla());
		knowledgeBase.setRemarks(requestDTO.getRemarks());
		knowledgeBase.setKnowledgeSolution(requestDTO.getKnowledgeSolution());
		knowledgeBase.setResolved(requestDTO.isResolved());
		knowledgeBase.setTypeofUser(requestDTO.getTypeofUser());
		return knowledgeBase;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
