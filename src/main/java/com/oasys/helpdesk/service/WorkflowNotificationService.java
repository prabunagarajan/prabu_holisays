package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ROLE_ID;
import static com.oasys.posasset.constant.Constant.DESC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceRegResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowNotificationDTO;
import com.oasys.helpdesk.dto.WorkflowNotificationResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.GrievanceregisterEntity;
import com.oasys.helpdesk.entity.LevelMaster;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.workflowNotificationEntity;
import com.oasys.helpdesk.mapper.WorkflowNotifyMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.LevelMasterRepository;
import com.oasys.helpdesk.repository.ReceviedBarQr;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.WorkflowNotificationRepository;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALStockEntity;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class WorkflowNotificationService {

	@Autowired
	WorkflowNotificationRepository workflownotRepository;
	
	@Autowired
	RoleMasterRepository rolemasterRepository;
	
	@Autowired
	LevelMasterRepository levelmasterRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LevelMasterRepository levelRepository;
	
	
	@Autowired
	WorkflowNotifyMapper workflowmapper;
	
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CommonUtil commonUtil;
	
	
	private static <T> Set<T> findDuplicates(List<T> list)
    {
        Set<T> seen = new HashSet<>();
        return list.stream()
                .filter(e -> !seen.add(e))
                .collect(Collectors.toSet());
    }
	
	
public  GenericResponse save(WorkflowNotificationDTO work) {
		
		ArrayList<workflowNotificationEntity> finallist = new ArrayList<workflowNotificationEntity>();	
		List<String> dublicateCheck=new ArrayList<>();
		try {
			work.getWorkflowDTO().stream().forEach(requestDTO ->{
	
		    Optional<Category> catgorydets=categoryRepository.findById(work.getCategoryId());	
			List<workflowNotificationEntity> entity = workflownotRepository.findByCategoryId(catgorydets.get());
			
			Optional<RoleMaster> roleTypeEnt = rolemasterRepository.findById(requestDTO.getRoleId());
			
			List<workflowNotificationEntity> entityrole = workflownotRepository.findByRoleId(roleTypeEnt.get());
		
			if(!entityrole.isEmpty()) {
				dublicateCheck.add(roleTypeEnt.get().getRoleName());
		    }
			else {
				dublicateCheck.add(roleTypeEnt.get().getRoleName());	
			}
			
			});
			
			Set<String> duplicates = findDuplicates(dublicateCheck);
			
			if(!duplicates.isEmpty()) {
					throw new InvalidDataValidation(" role already exist" + duplicates);
			}
			
	        work.getWorkflowDTO().stream().forEach(requestDTO ->{
			
			workflowNotificationEntity workflowEntity=new workflowNotificationEntity();
				
				Optional<RoleMaster> roleTypeEntity = rolemasterRepository.findById(requestDTO.getRoleId());
				if (!roleTypeEntity.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Role Id" }));
				}

				workflowEntity.setRoleId(roleTypeEntity.get());  
				
				Optional<Category> catgorydet=categoryRepository.findById(work.getCategoryId());
				if (!catgorydet.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Category Id" }));
				}
				
				workflowEntity.setCategoryId(catgorydet.get());
				
				Optional<LevelMaster> levelEntity = levelmasterRepository.findById(requestDTO.getLevelId());
				if (!levelEntity.isPresent()) {
					throw new InvalidDataValidation(
							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Level ID" }));
				}
				
				workflowEntity.setLevelId(levelEntity.get());
				workflowEntity.setDescription(work.getDescription());
				workflowEntity.setWorkflowName(work.getWorkflowName());
				workflowEntity.setPush(requestDTO.getPush());
				workflowEntity.setEmail(requestDTO.getEmail());
				workflowEntity.setSms(requestDTO.getSms());
				workflowEntity.setActive(work.getActive());
				workflowEntity.setLevelName(requestDTO.getLevelName());
				workflowEntity=workflownotRepository.save(workflowEntity);
				finallist.add(workflowEntity);
			});
			
			return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		}
		 catch (Exception e) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
			}
			
	}
	




	public GenericResponse getById(Long id) {
		Optional<Category> catgorydet=null;
		try {
	    catgorydet=categoryRepository.findById(id);
		}
		catch(Exception e) {
		}
		List<workflowNotificationEntity> entity = workflownotRepository.findByCategoryId(catgorydet.get());
		if (Objects.isNull(entity)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<WorkflowNotificationResponseDTO> assetTypeResponseList = entity.stream()
				.map(workflowmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	public GenericResponse getUsersByRoleId(Long roleId, String districtCode) {
		Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(roleId);
		if (!roleMasterOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
		}
		if (Boolean.FALSE.equals(roleMasterOptional.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INACTIVE_ID_PASSED.getMessage(new Object[] { ROLE_ID }));
		}
		List<Map<String, String>> userList = new ArrayList<>();
		if(StringUtils.isBlank(districtCode)) {
			userList = userRepository.getUserByRole(roleId);
		}else {

			userList = userRepository.getUserByRoleAndDistri(roleId,districtCode);
		}
		if (CollectionUtils.isEmpty(userList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(userList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	public GenericResponse getAll() {
		List<LevelMaster> DepList = levelRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		
		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	public GenericResponse getsubPagesearchNewByFilterstock(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<workflowNotificationEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<workflowNotificationEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
		
		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
	 	if (!list.isEmpty()) {
	 		List<WorkflowNotificationResponseDTO> dtoList = list.stream().map(workflowmapper::convertEntityToResponseDTO).collect(Collectors.toList());
	 		paginationResponseDTO.setContents(dtoList);	
		}	
		Long count1=(long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list.size()) ? list.size() : null);
		paginationResponseDTO.setTotalElements(count1);		
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
		}


public List<workflowNotificationEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	CriteriaQuery<workflowNotificationEntity> cq = cb.createQuery(workflowNotificationEntity.class);
	Root<workflowNotificationEntity> from = cq.from(workflowNotificationEntity.class);
	List<Predicate> list = new ArrayList<>();
	TypedQuery<workflowNotificationEntity> typedQuery = null;
	addSubCriteria(cb, list, filterRequestDTO, from);	
	cq.where(cb.and(list.toArray(new Predicate[list.size()])));
	cq.distinct(true);
	if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
		filterRequestDTO.setSortField("createdDate");
	}
	if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
			&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
		cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

	} else {
		cq.orderBy(cb.desc(from.get("createdDate")));
	}
	if (Objects.nonNull(filterRequestDTO.getPageNo())
			&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
		
		typedQuery = entityManager.createQuery(cq)
				.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
				.setMaxResults(filterRequestDTO.getPaginationSize());
	} else {
		typedQuery = entityManager.createQuery(cq);
	}				
	return typedQuery.getResultList();
}


public List<workflowNotificationEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	CriteriaQuery<workflowNotificationEntity> cq = cb.createQuery(workflowNotificationEntity.class);
	Root<workflowNotificationEntity> from = cq.from(workflowNotificationEntity.class);
	List<Predicate> list = new ArrayList<>();
	TypedQuery<workflowNotificationEntity> typedQuery = null;
	addSubCriteria(cb, list, filterRequestDTO, from);	
	cq.where(cb.and(list.toArray(new Predicate[list.size()])));
	cq.distinct(true);
	if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
		filterRequestDTO.setSortField("createdDate");
	}
	if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
			&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
		cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

	} else {
		cq.orderBy(cb.desc(from.get("createdDate")));
	}
	if (Objects.nonNull(filterRequestDTO.getPageNo())
			&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
	
		typedQuery = entityManager.createQuery(cq)
				.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
				.setMaxResults(filterRequestDTO.getPaginationSize());
	} else {
		typedQuery = entityManager.createQuery(cq);
	}				
	return typedQuery.getResultList();
}

private void addSubCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
		Root<workflowNotificationEntity> from) {

	if (Objects.nonNull(filterRequestDTO.getFilters().get("categoryId"))
			&& !filterRequestDTO.getFilters().get("categoryId").toString().trim().isEmpty()) {

		Long categoryid =Long.valueOf(filterRequestDTO.getFilters().get("categoryId").toString());
		list.add(cb.equal(from.get("categoryId"), categoryid));
	}
	
	

}




public GenericResponse Workflowupdate(List<WorkflowNotificationDTO> requestDTO) {
	
	ArrayList<workflowNotificationEntity> finallist=new ArrayList<workflowNotificationEntity>();	
	try {
		requestDTO.stream().forEach(action -> {
			Optional<workflowNotificationEntity> DeptOptional = workflownotRepository.findById(action.getId());
			if(!DeptOptional.isPresent()) {
				throw new InvalidDataValidation("Record not exist");
			}
			workflowNotificationEntity workflowEntity = DeptOptional.get();
			
			Optional<Category> catgorydet=categoryRepository.findById(action.getCategoryId());
			if (!catgorydet.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Category Id" }));
			}
			
			workflowEntity.setCategoryId(catgorydet.get());
			
			Optional<RoleMaster> roleTypeEntity = rolemasterRepository.findById(action.getRoleId());
			if (!roleTypeEntity.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Role Id" }));
			}
		    workflowEntity.setRoleId(roleTypeEntity.get());  
		    
			Optional<LevelMaster> levelEntity = levelmasterRepository.findById(action.getLevelId());
			if (!levelEntity.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Level ID" }));
			}
			workflowEntity.setLevelId(levelEntity.get());
			workflowEntity.setDescription(action.getDescription());
			workflowEntity.setWorkflowName(action.getWorkflowName());
			workflowEntity.setPush(action.getPush());
			workflowEntity.setEmail(action.getEmail());
			workflowEntity.setSms(action.getSms());
			workflowEntity.setActive(action.getActive());
			workflowEntity.setLevelName(action.getLevelName());
			workflownotRepository.save(workflowEntity);
			finallist.add(workflowEntity);
			
		});
		return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
				
	} catch (Exception e) {
		return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
	}
}
	
}
