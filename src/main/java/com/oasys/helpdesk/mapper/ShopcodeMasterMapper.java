package com.oasys.helpdesk.mapper;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.dto.ShopcodeResponseDTO;
import com.oasys.helpdesk.entity.ShopcodeEntity;
import com.oasys.helpdesk.entity.WorkLocationEntity;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.IssueDetailsRepository;
import com.oasys.helpdesk.repository.IssueFromRepository;
import com.oasys.helpdesk.repository.KnowledgeRepository;
import com.oasys.helpdesk.repository.KnowledgeSolutionRepository;
import com.oasys.helpdesk.repository.PriorityMasterRepository;
import com.oasys.helpdesk.repository.ProblemReportedRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.repository.UsergroupRepository;
import com.oasys.helpdesk.repository.WorkLocationRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class ShopcodeMasterMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	@Autowired
	private TicketStatusrepository ticketStatusrepository;
	
	@Autowired
	private IssueDetailsRepository issueDetailsRepository;
	
	@Autowired
	private IssueFromRepository issueFromRepository;
	
	@Autowired
	private SlaMasterRepository slaMasterRepository;
	
	@Autowired
	private KnowledgeRepository knowledgeRepository;
	
	@Autowired
	private UsergroupRepository usergroupRepository;
	
	@Autowired
	private PriorityMasterRepository priorityMasterRepository;
	
	@Autowired
	private AcutalProblemRepository actualProblemRepository;
	
	@Autowired
	private ActionTakenRepository actionTakenRepository;
	
	@Autowired
	private ProblemReportedRepository problemReportedRepository;
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private WorkLocationRepository worklocationRepository;
	
	
	
	
	@Autowired
	private KnowledgeSolutionRepository knowledgeSolutionRepository;
	
	public ShopcodeResponseDTO convertEntityToResponseDTO(ShopcodeEntity entity) {
		ShopcodeResponseDTO responseDTO = commonUtil.modalMap(entity,
				ShopcodeResponseDTO.class);
		responseDTO.setCreated_date(String.valueOf(entity.getCreatedDate()));
		responseDTO.setModified_date(String.valueOf(entity.getModifiedDate()));
		responseDTO.setDistrictCode(entity.getDistrictCode());
		responseDTO.setDivision(entity.getDivision());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setUserId(entity.getUserId());
		responseDTO.setStateCode(entity.getStateCode());
		responseDTO.setDistrictName(entity.getDistrictName());
//		if(Objects.nonNull(entity.getDistrictCode())) {
//		Optional<WorkLocationEntity> worklocaEntity=null;
//		try {
//			String discode=String.valueOf(entity.getDistrictCode());
//			worklocaEntity = worklocationRepository.findByDistrictCode(discode);
//			if(worklocaEntity.isPresent()) {
//				String districtname = worklocaEntity.get().getDistrictNames();
//				responseDTO.setDistrictName(districtname);
//				responseDTO.setStateCode(worklocaEntity.get().getStateCode());
//			}
//		}
//		catch(Exception e) {
//		}
//		}
		return responseDTO;	
	}
	
	
	
	
	
	
}
