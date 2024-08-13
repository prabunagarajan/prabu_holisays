package com.oasys.helpdesk.mapper;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.entity.GrievanceFaq;
import com.oasys.helpdesk.response.GrievanceFaqResponseDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class GrievanceFaqMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;


	public GrievanceFaqResponseDTO convertEntityToResponseDTO(GrievanceFaq faqentity) {

		GrievanceFaqResponseDTO responseDTO = commonUtil.modalMap(faqentity, GrievanceFaqResponseDTO.class);

		responseDTO.setId(faqentity.getId());
		responseDTO.setQuestion(faqentity.getQuestion());
		responseDTO.setAnswer(faqentity.getAnswer());
		responseDTO.setCode(faqentity.getCode());
		responseDTO.setStatus(faqentity.getStatus());
		if (Objects.nonNull(faqentity.getIssueDetails())) {
			responseDTO.setIssueDetails(faqentity.getIssueDetails().getIssueName());
			responseDTO.setIssueDetailsId(faqentity.getIssueDetails().getId());
			if (Objects.nonNull(faqentity.getIssueDetails().getCategory())) {
				responseDTO.setCategoryName(faqentity.getIssueDetails().getCategory().getCategoryName());
				responseDTO.setCatId(faqentity.getIssueDetails().getCategory().getId());
			}
		}

		if (Objects.nonNull(faqentity.getCreatedBy())) {
			responseDTO.setCreatedBy(commonDataController.getUserNameById(faqentity.getCreatedBy()));
		}
		if (Objects.nonNull(faqentity.getModifiedBy())) {
			responseDTO.setModifiedBy(commonDataController.getUserNameById(faqentity.getModifiedBy()));
		}
		responseDTO.setCreatedDate(faqentity.getCreatedDate());
		responseDTO.setModifiedDate(faqentity.getModifiedDate());
		responseDTO.setTypeofUser(faqentity.getTypeofUser());
		return responseDTO;
	}

	
	
	
	
	
	
	
	
	
	
}
