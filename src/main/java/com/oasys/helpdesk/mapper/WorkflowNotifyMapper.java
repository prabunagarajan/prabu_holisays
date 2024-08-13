package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.ACTION_TAKEN_ID;
import static com.oasys.helpdesk.constant.Constant.ACTUAL_PROBLEM_ID;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_DETAILS;
import static com.oasys.helpdesk.constant.Constant.ISSUE_FROM_ID;
import static com.oasys.helpdesk.constant.Constant.KB_ID;
import static com.oasys.helpdesk.constant.Constant.KNOWLEDGE_SOLUTION;
import static com.oasys.helpdesk.constant.Constant.PRIORITYID;
import static com.oasys.helpdesk.constant.Constant.PROBLEM_REPORTED_ID;
import static com.oasys.helpdesk.constant.Constant.ROLE_ID;
import static com.oasys.helpdesk.constant.Constant.SLA_ID;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.TICKET_STATUS_ID;
import static com.oasys.helpdesk.constant.Constant.USER_ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.WorkflowNotificationResponseDTO;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.CreateTicketEntitypayment;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.KnowledgeSolution;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.SlaMasterEntity;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.entity.workflowNotificationEntity;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class WorkflowNotifyMapper {
	
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	
	
	public WorkflowNotificationResponseDTO convertEntityToResponseDTO(workflowNotificationEntity entity) {

		WorkflowNotificationResponseDTO responseDTO =new WorkflowNotificationResponseDTO();
		responseDTO.setId(entity.getId());
		
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		if (Objects.nonNull(entity.getCategoryId())) {
			responseDTO.setCategoryName(entity.getCategoryId().getCategoryName());
			responseDTO.setCategoryId(entity.getCategoryId().getId());
		}
		if (Objects.nonNull(entity.getRoleId())) {
			responseDTO.setRoleId(entity.getRoleId().getId());
			responseDTO.setRoleName(entity.getRoleId().getRoleName());
			
		}
		if(Objects.nonNull(entity.getLevelId())) {
			responseDTO.setLevelId(entity.getLevelId().getId());
			responseDTO.setLevelName(entity.getLevelId().getName());
		}
		responseDTO.setActive(entity.getActive());		
		responseDTO.setCreatedBy(entity.getCreatedBy());
		responseDTO.setDescription(entity.getDescription());
		responseDTO.setWorkflowName(entity.getWorkflowName());
		responseDTO.setEmail(entity.getEmail());
		responseDTO.setPush(entity.getPush());
		responseDTO.setSms(entity.getSms());
		return responseDTO;	
	}
	
	
	
}
