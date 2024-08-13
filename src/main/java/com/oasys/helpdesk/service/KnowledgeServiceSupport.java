package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.KB_MANDATORY_FIELDS;
import static java.util.Objects.isNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.KnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.KnowledgeBaseResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.IssueDetailsRepository;
import com.oasys.helpdesk.repository.KnowledgeRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class KnowledgeServiceSupport {

	@Autowired
	private KnowledgeRepository helpDeskKnowledgeRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private IssueDetailsRepository issueDetailsRepository;

	//TODO - Remove once dedicated API is available from sub-solution module
	protected static List<String> getKnowledgeBaseSolutions() {
		return Arrays.asList("Solution-1", "Solution-2", "Solution-3");
	}

	protected KnowledgeBaseResponseDTO toKnowledgeBaseResponseDTO(KnowledgeBase knowledgeBase) {
		Optional<SubCategory> subCategoryOptional = subCategoryRepository.findById(knowledgeBase.getSubcategoryId().getId());
		Optional<Category> categoryOptional = categoryRepository.findById(knowledgeBase.getCategoryId().getId());
		String createdByUserName = commonDataController.getUserNameById(knowledgeBase.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(knowledgeBase.getModifiedBy());
		return KnowledgeBaseResponseDTO.builder()
				.id(knowledgeBase.getId())
				.kbId(knowledgeBase.getKbId())
				.category(categoryOptional.isPresent() ? categoryOptional.get().getCategoryName() : "")
				.subcategory(subCategoryOptional.isPresent() ? subCategoryOptional.get().getSubCategoryName() : "")
				.issueDetails(knowledgeBase.getIssueDetailsEntity().getIssueName())
				.issueDetailsId(Objects.nonNull(knowledgeBase.getIssueDetailsEntity())? knowledgeBase.getIssueDetailsEntity().getId():null)
			    .categoryid(categoryOptional.isPresent() ? categoryOptional.get().getId(): 0)
			    .subcategoryid(subCategoryOptional.isPresent() ? subCategoryOptional.get().getId(): 0)
				.status(knowledgeBase.getStatus())
				.priority(knowledgeBase.getPriority())
				.sla(knowledgeBase.getSla())
				.count(knowledgeBase.getCount())
				.remarks(knowledgeBase.getRemarks())
				.knowledgeSolution(knowledgeBase.getKnowledgeSolution())
				.isResolved(knowledgeBase.isResolved())
				.createdBy(createdByUserName)
				.modifiedBy(modifiedByUserName)
				.createdDate(String.valueOf(knowledgeBase.getCreatedDate()))
				.modifiedDate(String.valueOf(knowledgeBase.getModifiedDate()))
				.build();
	}

	protected static GenericResponse performMandatoryCheck(KnowledgeBaseRequestDTO requestDTO) {
		if(isNull(requestDTO.getKbId()) || isNull(requestDTO.getCategoryId()) ||
				isNull(requestDTO.getSubcategoryId()) || isNull(requestDTO.getPriority()) ||
		isNull(requestDTO.getIssueDetails()) || isNull(requestDTO.getIssueDetailsId())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{KB_MANDATORY_FIELDS}));
		}
		return null;
	}

	protected KnowledgeBase toKnowledgeBaseEntity(KnowledgeBaseRequestDTO requestDTO) {
		KnowledgeBase knowledgeBase = new KnowledgeBase();
		knowledgeBase.setKbId(requestDTO.getKbId());
		knowledgeBase.setCategoryId(categoryRepository.getById(requestDTO.getCategoryId()));
		knowledgeBase.setSubcategoryId(subCategoryRepository.getById(requestDTO.getSubcategoryId()));
		knowledgeBase.setIssueDetails(requestDTO.getIssueDetails());
		knowledgeBase.setStatus(requestDTO.getStatus().toUpperCase());
		knowledgeBase.setPriority(requestDTO.getPriority());
		knowledgeBase.setSla(requestDTO.getSla());
		knowledgeBase.setCount(0);
		knowledgeBase.setRemarks(requestDTO.getRemarks());
		knowledgeBase.setKnowledgeSolution(requestDTO.getKnowledgeSolution());
		knowledgeBase.setResolved(requestDTO.isResolved());
		Optional<IssueDetails> issueDetailsEntity = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
		if(!issueDetailsEntity.isPresent()) {
			throw new InvalidDataValidation(com.oasys.helpdesk.constant.ErrorMessages.INVALID_ISSUE_DETAILS_ID);
		}
		knowledgeBase.setIssueDetailsEntity(issueDetailsEntity.get());
		return knowledgeBase;
	}

	protected KnowledgeBase toKnowledgeBaseUpdateEntity(KnowledgeBaseRequestDTO requestDTO, KnowledgeBase knowledgeBase) {
		knowledgeBase.setCategoryId(categoryRepository.getById(requestDTO.getCategoryId()));
		knowledgeBase.setSubcategoryId(subCategoryRepository.getById(requestDTO.getSubcategoryId()));
		knowledgeBase.setIssueDetails(requestDTO.getIssueDetails());
		knowledgeBase.setStatus(requestDTO.getStatus());
		knowledgeBase.setPriority(requestDTO.getPriority());
		knowledgeBase.setSla(requestDTO.getSla());
		knowledgeBase.setRemarks(requestDTO.getRemarks());
		knowledgeBase.setKnowledgeSolution(requestDTO.getKnowledgeSolution());
		knowledgeBase.setResolved(requestDTO.isResolved());
		Optional<IssueDetails> issueDetailsEntity = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
		if(!issueDetailsEntity.isPresent()) {
			throw new InvalidDataValidation(com.oasys.helpdesk.constant.ErrorMessages.INVALID_ISSUE_DETAILS_ID);
		}
		knowledgeBase.setIssueDetailsEntity(issueDetailsEntity.get());
		return knowledgeBase;
	}

	protected static long getUniqueCode() {
		Random rnd = new Random();
		return rnd.nextInt(999999);
	}

}
