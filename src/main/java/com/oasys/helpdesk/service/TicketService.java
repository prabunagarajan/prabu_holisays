package com.oasys.helpdesk.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.ISSUE_FROM;
import static com.oasys.helpdesk.constant.Constant.TICKET_NUMBER;
import static com.oasys.helpdesk.constant.Constant.LICENSE_NUMBER;
import static com.oasys.helpdesk.constant.Constant.LICENSE_TYPE_ID;
import static com.oasys.helpdesk.constant.Constant.PRIORITYID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.AssetBrandTypeResponseDTO;
import com.oasys.helpdesk.dto.EntityMasterDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.AssetBrandTypeEntity;
import com.oasys.helpdesk.entity.CallDisconnectedTickets;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.SlaConfiguration;
import com.oasys.helpdesk.entity.Status;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.Ticket;
import com.oasys.helpdesk.entity.TicketHistory;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.repository.CallDisconnectedTicketRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.GroupRepository;
import com.oasys.helpdesk.repository.IssueDetailsRepository;
import com.oasys.helpdesk.repository.KnowledgeRepository;
import com.oasys.helpdesk.repository.PriorityRepository;
import com.oasys.helpdesk.repository.ProblemReportedRepository;
import com.oasys.helpdesk.repository.SlaConfigurationRepository;
import com.oasys.helpdesk.repository.StatusRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.TicketHistoryRepository;
import com.oasys.helpdesk.repository.TicketRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.request.TicketRequestDto;
import com.oasys.helpdesk.response.EntityType;
import com.oasys.helpdesk.response.TicketHistoryResponseDto;
import com.oasys.helpdesk.response.TicketResponseDto;
import com.oasys.helpdesk.response.UserDetails;
import com.oasys.helpdesk.response.UserMasterResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;




@Log4j2
@Service
public class TicketService {

	@Autowired
	TicketRepository helpDeskTicketRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	PriorityRepository helpDeskTicketPriorityRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	@Autowired
	StatusRepository helpDeskTicketStatusRepository;
	
	@Autowired
	KnowledgeRepository helpDeskKnowledgeRepository;
	
	
    
	@Autowired
	ActionTakenRepository helpDeskActionTakenRepository;
	
	@Autowired
	AcutalProblemRepository helpDeskAcutalProblemRepository;
	
	@Autowired
	GroupRepository helpDeskGroupRepository;
	
	@Autowired
	ProblemReportedRepository helpDeskProblemReportedRepository;
	
	@Autowired
	SlaConfigurationRepository slaConfigurationRepository;
//	
//	@Autowired
//	LoginService loginservice;
	
	@Autowired
	CommonDataController commonDataController ;
	
	@Autowired
	IssueDetailsRepository issuerepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	TicketHistoryRepository ticketHistoryRepository;
	
	@Autowired
	CallDisconnectedTicketRepository callDisconnectedTicketRepository;

	public GenericResponse getAllTicket() {
		List<Ticket> HelpDeskTicketList = helpDeskTicketRepository.getallActiveTickets();

		if (HelpDeskTicketList.size() > 0) {
			List<TicketResponseDto> HelpDeskTicketResponseDtoList = new ArrayList<TicketResponseDto>();
			HelpDeskTicketList.forEach(pt -> {
				HelpDeskTicketResponseDtoList.add(convertHelpDeskTicketToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getAllTicketByCategory(Long categoryid) {
		List<Ticket> HelpDeskTicketList = helpDeskTicketRepository.getallActiveTicketsByCategory(categoryid);

		if (HelpDeskTicketList.size() > 0) {
			List<TicketResponseDto> HelpDeskTicketResponseDtoList = new ArrayList<TicketResponseDto>();
			HelpDeskTicketList.forEach(pt -> {
				HelpDeskTicketResponseDtoList.add(convertHelpDeskTicketToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getAllTicketByStatus(Long statusid) {
		List<Ticket> HelpDeskTicketList = helpDeskTicketRepository.getallActiveTicketsByStatus(statusid);

		if (HelpDeskTicketList.size() > 0) {
			List<TicketResponseDto> HelpDeskTicketResponseDtoList = new ArrayList<TicketResponseDto>();
			HelpDeskTicketList.forEach(pt -> {
				HelpDeskTicketResponseDtoList.add(convertHelpDeskTicketToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	
	public GenericResponse getTicketById(Long id) throws RecordNotFoundException {
		Ticket helpDeskTicket = helpDeskTicketRepository.getById(id);
		if (helpDeskTicket== null ) {
			throw new RecordNotFoundException("No record found");
		}
		if (helpDeskTicket!= null && helpDeskTicket.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskTicketToDto(helpDeskTicket), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	private TicketResponseDto convertHelpDeskTicketToDto(Ticket helpDeskTicket) {

	    TicketResponseDto helpDeskTicketResponseDto= new TicketResponseDto();
	    helpDeskTicketResponseDto.setId(helpDeskTicket.getId());
	    if(helpDeskTicket.getCategoryId().getId() !=null) {
	    Category helpDeskTicketCategory= helpDeskTicketCategoryRepository.getById(helpDeskTicket.getCategoryId().getId());
	    helpDeskTicketResponseDto.setCategoryName(helpDeskTicketCategory.getCategoryName());
     	}
	    if(helpDeskTicket.getSubcategoryId().getId()!=null) {
		SubCategory helpDeskTicketSubCategory=helpDeskTicketSubCategoryRepository.getById(helpDeskTicket.getSubcategoryId().getId());
		helpDeskTicketResponseDto.setSubcategoryName(helpDeskTicketSubCategory.getSubCategoryName());
		}
	    if(helpDeskTicket.getPriorityId().getId() !=null) {
	    Priority helpDeskTicketPriority= helpDeskTicketPriorityRepository.getById(helpDeskTicket.getPriorityId().getId());
	    helpDeskTicketResponseDto.setPriorityName(helpDeskTicketPriority.getPriority());
	    }
	    if(helpDeskTicket.getClosurepriorityId() !=null) {
	    Priority	helpDeskTicketclosurePriority= helpDeskTicketPriorityRepository.getById(helpDeskTicket.getClosurepriorityId().getId());
	    helpDeskTicketResponseDto.setClosurepriorityName(helpDeskTicketclosurePriority.getPriority());
	    }
	    if(helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Service Request") 
	    || helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Complaints")
	    || helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Incident")  ) {
	    if(helpDeskTicket.getIssueMaster().getId() !=null) {
		IssueDetails issue= issuerepository.getById(helpDeskTicket.getIssueMaster().getId());
		helpDeskTicketResponseDto.setIssueName(issue.getIssueName());
		}
	    }
	    else {
	    helpDeskTicketResponseDto.setIssueName(helpDeskTicket.getIssueName());
	    }
	    if(helpDeskTicket.getGroupId().getId() !=null) {
	    Group group= helpDeskGroupRepository.getById(helpDeskTicket.getGroupId().getId());
	    helpDeskTicketResponseDto.setGroupName(group.getName());
	    }
	    if(helpDeskTicket.getGroupMemberid() !=null) {
	    String fieldagent=commonDataController.getUserNameById(helpDeskTicket.getGroupMemberid());
	    helpDeskTicketResponseDto.setGroupMemberName(fieldagent);
	    }
	    if(helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Service Request") 
	    || helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Complaints")
	    || helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Incident")) {
	    if(helpDeskTicket.getKnowledgeBaseId().getId() !=null) {
	    KnowledgeBase knowledge= helpDeskKnowledgeRepository.getById(helpDeskTicket.getKnowledgeBaseId().getId());
	    helpDeskTicketResponseDto.setKnowledgeBaseName(knowledge.getKnowledgeSolution());
	    }
	    }
	    else {
	    helpDeskTicketResponseDto.setKnowledgeBaseName(helpDeskTicket.getQueryKnowledge());
	    }
	    if(helpDeskTicket.getSlaMaster().getId() !=null) {
	    SlaConfiguration sla= slaConfigurationRepository.getById(helpDeskTicket.getSlaMaster().getId());
	    helpDeskTicketResponseDto.setSlaName(sla.getRuleName());
	    helpDeskTicketResponseDto.setSlaDays(sla.getThresholdTime());
	    }
	    if(helpDeskTicket.getStatus().getId() !=null) {
	    Status ticketstatus= helpDeskTicketStatusRepository.getById(helpDeskTicket.getStatus().getId());
	    helpDeskTicketResponseDto.setTicketstatusName(ticketstatus.getStatus());
	    }
	    log.info("entitytypeidddd"+ helpDeskTicket.getEntityTypeId());
	    if(helpDeskTicket.getEntityTypeId()!=null) {
		//EntityTypeMasterDTO entityTypeResponseDto= commonDataController.getEntityTypeById(helpDeskTicket.getEntityTypeId().intValue());
		EntityType entityTypeResponseDto= commonDataController.getEntityTypeById(helpDeskTicket.getEntityTypeId());
		helpDeskTicketResponseDto.setEntityTypeName((entityTypeResponseDto != null) ? entityTypeResponseDto.getName()  : null);
		helpDeskTicketResponseDto.setEntityType(entityTypeResponseDto);
		}
	    log.info("entityidddd"+ helpDeskTicket.getEntityId());
	    if(helpDeskTicket.getEntityId()!=null) {
	 	EntityMasterDTO entityResponseDto= commonDataController.getEntityById(helpDeskTicket.getEntityId().intValue());
	 	helpDeskTicketResponseDto.setEntityName((entityResponseDto != null) ? entityResponseDto.getName() : null);
	 	helpDeskTicketResponseDto.setEntityMasterDTO(entityResponseDto);
	 	} 
	    helpDeskTicketResponseDto.setCallerNumber(helpDeskTicket.getCallerNumber());
	    helpDeskTicketResponseDto.setDescription(helpDeskTicket.getDescription());
	    helpDeskTicketResponseDto.setNotes(helpDeskTicket.getNotes());
	    helpDeskTicketResponseDto.setTicketNumber(helpDeskTicket.getTicketNumber());
	    helpDeskTicketResponseDto.setLicenceNumber(helpDeskTicket.getLicenceNumber());
	    helpDeskTicketResponseDto.setEmailId(helpDeskTicket.getEmailId());
	    helpDeskTicketResponseDto.setMobileNumber(helpDeskTicket.getMobileNumber());
	    helpDeskTicketResponseDto.setActive(helpDeskTicket.isActive());
	    String createduser=commonDataController.getUserNameById(helpDeskTicket.getCreatedBy());
	    String modifieduser=commonDataController.getUserNameById(helpDeskTicket.getModifiedBy());
	    helpDeskTicketResponseDto.setCreatedBy(createduser);
	    helpDeskTicketResponseDto.setCreatedDate(helpDeskTicket.getCreatedDate());
	    helpDeskTicketResponseDto.setModifiedBy(modifieduser);
	    helpDeskTicketResponseDto.setModifiedDate(helpDeskTicket.getModifiedDate());
	    //Ticket History Response
	    List<TicketHistory> history= ticketHistoryRepository.getByTicketId(helpDeskTicket.getId());
        if(history !=null) {
		List<TicketHistoryResponseDto> ticketHistoryResponseDto= new ArrayList<>();
	    for(TicketHistory tickethistory:history ) {
	    	TicketHistoryResponseDto dto =new TicketHistoryResponseDto();
	    	Long processedby= tickethistory.getAssignedBy();
	    	Long historystatus= tickethistory.getStatus().getId();
	    	String processedbyname=commonDataController.getUserNameById(processedby);
	    	dto.setHistoryprocessedBy(processedbyname);
	    	Status status= helpDeskTicketStatusRepository.getById(historystatus);
	    	dto.setHistoryticketstatusName(status.getStatus());
	    	dto.setHistorycreatedDate(tickethistory.getCreatedDate());
	    	Group group= helpDeskGroupRepository.getById(tickethistory.getGroup().getId());
	    	dto.setHistorygroupName(group.getName());
	    	dto.setActive(tickethistory.isActive());
	    	dto.setHistorydescription(tickethistory.getComments());
	    	if(tickethistory.getActualproblem()!=null)
	    	{
	    	ActualProblem actualproblem = helpDeskAcutalProblemRepository.getById(tickethistory.getActualproblem().getId());
	    	dto.setActualproblem(actualproblem.getActualProblem());
	    	}
	    	if(tickethistory.getActionTaken()!=null)
	    	{
	    	ActionTaken actiontaken= helpDeskActionTakenRepository.getById(tickethistory.getActionTaken().getId());
	    	dto.setActionTaken(actiontaken.getActionTaken());
	        }
	    	if(tickethistory.getClosurepriority()!=null)
	    	{
	    	Priority priority= helpDeskTicketPriorityRepository.getById(tickethistory.getClosurepriority().getId());
	    	dto.setClosurePriority(priority.getPriority());
	        }	
	    	ticketHistoryResponseDto.add(dto);
	    
	    }
	   
	    helpDeskTicketResponseDto.setTicketHistoryResponseDto(ticketHistoryResponseDto);
	    }
	    return helpDeskTicketResponseDto;

	}
	
	public GenericResponse createTicket(TicketRequestDto helpDeskTicketRequestDto)  throws RecordNotFoundException , Exception
	{
		
		if (helpDeskTicketRequestDto != null ) {
			Ticket helpDeskTicket = new Ticket();
			
			
			if(helpDeskTicketRequestDto.getCategoryId() !=null && helpDeskTicketRequestDto.getCategoryId()>0) {
				Category category= helpDeskTicketCategoryRepository.getById(helpDeskTicketRequestDto.getCategoryId());
				helpDeskTicket.setCategoryId(category);
			}
			
			
			if(helpDeskTicketRequestDto.getSubcategoryId() !=null && helpDeskTicketRequestDto.getSubcategoryId()>0) {
				SubCategory subcategory= helpDeskTicketSubCategoryRepository.getById(helpDeskTicketRequestDto.getSubcategoryId());
				helpDeskTicket.setSubcategoryId(subcategory);
			}
			
			
			helpDeskTicket.setCallerNumber(helpDeskTicketRequestDto.getCallerNumber());
			
			if(helpDeskTicketRequestDto.getIssueId() !=null && helpDeskTicketRequestDto.getIssueId()>0) {
				IssueDetails issue= issuerepository.getById(helpDeskTicketRequestDto.getIssueId());
				helpDeskTicket.setIssueMaster(issue);
			}
			else {
				helpDeskTicket.setIssueName(helpDeskTicketRequestDto.getQueryIssue());
			}
			
			if(helpDeskTicketRequestDto.getKnowledgeBaseId() !=null && helpDeskTicketRequestDto.getKnowledgeBaseId()>0) {
				KnowledgeBase knowledge= helpDeskKnowledgeRepository.getById(helpDeskTicketRequestDto.getKnowledgeBaseId());
				helpDeskTicket.setKnowledgeBaseId(knowledge);
			}
			else {
				helpDeskTicket.setQueryKnowledge(helpDeskTicketRequestDto.getQueryKnowledge());
			}
			
			if(helpDeskTicketRequestDto.getGroupId() !=null && helpDeskTicketRequestDto.getGroupId()>0) {
				Group group= helpDeskGroupRepository.getById(helpDeskTicketRequestDto.getGroupId());
				helpDeskTicket.setGroupId(group);
				
			}
			helpDeskTicket.setGroupMemberid(helpDeskTicketRequestDto.getGroupMemberid());
			if(helpDeskTicketRequestDto.getPriorityId() !=null && helpDeskTicketRequestDto.getPriorityId()>0) {
				Priority priority= helpDeskTicketPriorityRepository.getById(helpDeskTicketRequestDto.getPriorityId());
				helpDeskTicket.setPriorityId(priority);
				
			}
			if(helpDeskTicketRequestDto.getSlaId() !=null && helpDeskTicketRequestDto.getSlaId()>0) {
				SlaConfiguration sla= slaConfigurationRepository.getById(helpDeskTicketRequestDto.getSlaId());
				helpDeskTicket.setSlaMaster(sla);
			}
			
			if(helpDeskTicketRequestDto.getTicketstatus() !=null && helpDeskTicketRequestDto.getTicketstatus()>0) {
				Status ticketstatus= helpDeskTicketStatusRepository.getById(helpDeskTicketRequestDto.getTicketstatus());
				helpDeskTicket.setStatus(ticketstatus);
			}
			Ticket lastticketno= helpDeskTicketRepository.getLastTicketValue();
			Long last= lastticketno.getId()+1;
			String str = Long.toString(last);
			Date today=  new Date();
			//DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyss"); 
			DateFormat dateFormat = new SimpleDateFormat("yyyy");  
			String strDate = dateFormat.format(today);  
			String ticketFormat= strDate+str;
			helpDeskTicket.setTicketNumber(ticketFormat);
			helpDeskTicket.setEntityTypeId(helpDeskTicketRequestDto.getEntityTypeId());
			helpDeskTicket.setEntityId(helpDeskTicketRequestDto.getEntityId());
			helpDeskTicket.setDescription(helpDeskTicketRequestDto.getDescription());
			helpDeskTicket.setLicenceNumber(helpDeskTicketRequestDto.getLicenceNumber());
			helpDeskTicket.setEmailId(helpDeskTicketRequestDto.getEmailId());
			helpDeskTicket.setMobileNumber(helpDeskTicketRequestDto.getMobileNumber());
			helpDeskTicket.setActive(helpDeskTicketRequestDto.isActive());
			helpDeskTicketRepository.save(helpDeskTicket);
			return Library.getSuccessfulResponse(helpDeskTicket, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse updateTicketStatus(TicketRequestDto helpDeskTicketRequestDto)  throws RecordNotFoundException , Exception
	{
		
		if (helpDeskTicketRequestDto != null ) {
			
			
			Ticket ticketobj = helpDeskTicketRepository.getById(helpDeskTicketRequestDto.getId());
			TicketHistory history= new TicketHistory();
			if(helpDeskTicketRequestDto.getTicketstatus()!=null)
			{
			Status status= helpDeskTicketStatusRepository.getById(helpDeskTicketRequestDto.getTicketstatus());
			ticketobj.setStatus(status);
			history.setStatus(status);
			}
			if(helpDeskTicketRequestDto.getClosurePriorityId()!=null)
			{
			Priority closurepriority= helpDeskTicketPriorityRepository.getById(helpDeskTicketRequestDto.getClosurePriorityId());
			ticketobj.setClosurepriorityId(closurepriority);
			}
			ticketobj.setNotes(helpDeskTicketRequestDto.getNotes());
			helpDeskTicketRepository.save(ticketobj);
			
			//tickethistory
		
			history.setHelpDeskTicket(ticketobj);
			history.setAssignedBy(ticketobj.getGroupMemberid());
			if(ticketobj.getGroupId()!=null) {
			Group group= helpDeskGroupRepository.getById(ticketobj.getGroupId().getId());
			history.setGroup(group);
			}
			if(ticketobj.getClosurepriorityId()!=null) {
			Priority priority= helpDeskTicketPriorityRepository.getById(ticketobj.getClosurepriorityId().getId());
			history.setClosurepriority(priority);
			}
			history.setActive(true);
			ticketHistoryRepository.save(history);
			
			return Library.getSuccessfulResponse(ticketobj, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	} 
	
	
	public GenericResponse deleteTicketById(Long id) throws RecordNotFoundException {
	    helpDeskTicketRepository.deleteById(id);
		if (id== null ) {
			throw new RecordNotFoundException("Id Should not Be Null");
		}
		if (id!= null) {
			List<Ticket> HelpDeskTicketList = helpDeskTicketRepository.findAll();

			return Library.getSuccessfulResponse(HelpDeskTicketList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_DELETED);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse searchUser(String search ) throws RecordNotFoundException {
	//	User userMaster = userRepository.getByValue(search);search
	if (!search.isEmpty()) {
		TicketResponseDto helpDeskTicketResponseDto= new TicketResponseDto();
		 UserMasterResponseDto userResponseDto= commonDataController.searchUser(search);
	 	    helpDeskTicketResponseDto.setEmailId((userResponseDto != null) ? userResponseDto.getEmailId() : null);
	 	    helpDeskTicketResponseDto.setMobileNumber((userResponseDto != null) ? userResponseDto.getPhoneNumber() : null);

	 	    helpDeskTicketResponseDto.setUserMasterResponseDto(userResponseDto);
 			return Library.getSuccessfulResponse(helpDeskTicketResponseDto,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse getTicketLazySearch(PaginationRequestDTO paginationDto) throws ParseException {
		List<TicketResponseDto> helpDeskTicketResponseDtoList = new ArrayList<>();
		StringBuilder recordQuery = new StringBuilder("Select * from help_desk_ticket helpDeskTicket where 1=1 ");
		StringBuilder countQuery = new StringBuilder("Select count(*) from help_desk_ticket helpDeskTicket where 1=1 "); 

		
		
		if (paginationDto.getFilters() != null) {
			log.info("helpDeskTicket filters :::" + paginationDto.getFilters());

			if (paginationDto.getFilters().get("id") != null
					&& !paginationDto.getFilters().get("id").toString().trim().isEmpty()) {
				Long id = Long.parseLong(paginationDto.getFilters().get("id").toString());

				recordQuery.append(" and helpDeskTicket.id = " + id);
				countQuery.append(" and helpDeskTicket.id = " + id);

			}
			if (paginationDto.getFilters().get("ticketNumber") != null
					&& !paginationDto.getFilters().get("ticketNumber").toString().trim().isEmpty()) {
				String ticketNumber = paginationDto.getFilters().get("ticketNumber").toString();
				log.debug("ticketNumber :"+ paginationDto.getFilters().get("ticketNumber"));
				recordQuery.append( " and helpDeskTicket.ticket_number = '"+ticketNumber +"'");
				countQuery.append( " and helpDeskTicket.ticket_number = '"+ticketNumber+"'");
			}
			
			if (paginationDto.getFilters().get("categoryId") != null
					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
				String category = paginationDto.getFilters().get("categoryId").toString();
				log.debug("categoryId :"+ paginationDto.getFilters().get("categoryId"));
				recordQuery.append( " and helpDeskTicket.category_id = '"+category +"'");
				countQuery.append( " and helpDeskTicket.category_id = '"+category+"'");
			}
			
			if (paginationDto.getFilters().get("subcategoryId") != null
					&& !paginationDto.getFilters().get("subcategoryId").toString().trim().isEmpty()) {
				String subcategory = paginationDto.getFilters().get("subcategoryId").toString();
				log.debug("subcategoryId :"+ paginationDto.getFilters().get("subcategoryId"));
				recordQuery.append( " and helpDeskTicket.subcategory_id = '"+subcategory +"'");
				countQuery.append( " and helpDeskTicket.subcategory_id = '"+subcategory+"'");
			}
			
			if (paginationDto.getFilters().get("entityTypeId") != null
					&& !paginationDto.getFilters().get("entityTypeId").toString().trim().isEmpty()) {
				String entityTypeId = paginationDto.getFilters().get("entityTypeId").toString();
				log.debug("entityTypeId :"+ paginationDto.getFilters().get("entityTypeId"));
				recordQuery.append( " and helpDeskTicket.entity_type_id = '"+entityTypeId +"'");
				countQuery.append( " and helpDeskTicket.entity_type_id = '"+entityTypeId+"'");
			}
			
			if (paginationDto.getFilters().get("priorityId") != null
					&& !paginationDto.getFilters().get("priorityId").toString().trim().isEmpty()) {
				String priorityId = paginationDto.getFilters().get("priorityId").toString();
				log.debug("priorityId :"+ paginationDto.getFilters().get("priorityId"));
				recordQuery.append( " and helpDeskTicket.priority_id = '"+priorityId +"'");
				countQuery.append( " and helpDeskTicket.priority_id = '"+priorityId+"'");
			}
			
			if (paginationDto.getFilters().get("status") != null
					&& !paginationDto.getFilters().get("status").toString().trim().isEmpty()) {
				String status = paginationDto.getFilters().get("status").toString();
				log.debug("status :"+ paginationDto.getFilters().get("status"));
				recordQuery.append( " and helpDeskTicket.ticket_status_id = '"+status +"'");
				countQuery.append( " and helpDeskTicket.ticket_status_id = '"+status+"'");
			}
			
			
			
			if (paginationDto.getFilters().get("callerNumber") != null
					&& !paginationDto.getFilters().get("callerNumber").toString().trim().isEmpty()) {
				String callerNumber = paginationDto.getFilters().get("callerNumber").toString();
				log.debug("callerNumber :"+ paginationDto.getFilters().get("callerNumber"));
				recordQuery.append( " and helpDeskTicket.caller_number = '"+callerNumber +"'");
				countQuery.append( " and helpDeskTicket.caller_number = '"+callerNumber+"'");
			}
			
			if (paginationDto.getFilters().get("licenceNumber") != null
					&& !paginationDto.getFilters().get("licenceNumber").toString().trim().isEmpty()) {
				String licenseNumber = paginationDto.getFilters().get("licenceNumber").toString();
				log.debug("licenceNumber :"+ paginationDto.getFilters().get("licenceNumber"));
				recordQuery.append( " and helpDeskTicket.licence_number = '"+licenseNumber +"'");
				countQuery.append( " and helpDeskTicket.licence_number = '"+licenseNumber+"'");
			}
			
			if (paginationDto.getFilters().get("createdDate") != null
					&& !paginationDto.getFilters().get("createdDate").toString().trim().isEmpty()) {
				String createdDateStr = paginationDto.getFilters().get("createdDate").toString();
				
				log.debug("createdDate :"+ paginationDto.getFilters().get("createdDate"));

				recordQuery.append( " and Date(helpDeskTicket.created_date) = '"+createdDateStr +"'");
				countQuery.append( " and Date(helpDeskTicket.created_date) = '"+createdDateStr+"'");
			}
			recordQuery.append(" order by modified_date desc " );
		}
		
		BigInteger totalCount1 = (BigInteger) entityManager.createNativeQuery(countQuery.toString()).getSingleResult();
		Integer totalCount = totalCount1.intValue();

		javax.persistence.Query query = entityManager.createNativeQuery(recordQuery.toString(),
				Ticket.class);
		query.setFirstResult(paginationDto.getPageNo() * paginationDto.getPaginationSize());
		query.setMaxResults(paginationDto.getPaginationSize());
		List<Ticket> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			throw new RecordNotFoundException();
		} else {
			for (Ticket helpDeskTicket : resultList) {
				TicketResponseDto helpDeskTicketResponseDto = new TicketResponseDto();
				 helpDeskTicketResponseDto.setId(helpDeskTicket.getId());
				 
				  if(helpDeskTicket.getCategoryId().getId() !=null) {
					    Category helpDeskTicketCategory= helpDeskTicketCategoryRepository.getById(helpDeskTicket.getCategoryId().getId());
					    
					    helpDeskTicketResponseDto.setCategoryName(helpDeskTicketCategory.getCategoryName());
				     	}
					    
					    if(helpDeskTicket.getSubcategoryId().getId()!=null) {
						  SubCategory helpDeskTicketSubCategory=helpDeskTicketSubCategoryRepository.getById(helpDeskTicket.getSubcategoryId().getId());
						  helpDeskTicketResponseDto.setSubcategoryName(helpDeskTicketSubCategory.getSubCategoryName());
						}
					 
					    if(helpDeskTicket.getPriorityId().getId() !=null) {
					    Priority helpDeskTicketPriority= helpDeskTicketPriorityRepository.getById(helpDeskTicket.getPriorityId().getId());
					    helpDeskTicketResponseDto.setPriorityName(helpDeskTicketPriority.getPriority());
					    }
					    if(helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Service Request") 
					    		|| helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Complaints")
					    		|| helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Incident")  ) {
					    if(helpDeskTicket.getIssueMaster().getId() !=null) {
						    IssueDetails issue= issuerepository.getById(helpDeskTicket.getIssueMaster().getId());
						    helpDeskTicketResponseDto.setIssueName(issue.getIssueName());
						}
					    }
					    else {
					    	 helpDeskTicketResponseDto.setIssueName(helpDeskTicket.getIssueName());
					    }
					    if(helpDeskTicket.getGroupId().getId() !=null) {
					    	Group group= helpDeskGroupRepository.getById(helpDeskTicket.getGroupId().getId());
					    	helpDeskTicketResponseDto.setGroupName(group.getName());
					    }
					    
					    if(helpDeskTicket.getGroupMemberid() !=null) {
					    	String fieldagent=commonDataController.getUserNameById(helpDeskTicket.getGroupMemberid());
					    	helpDeskTicketResponseDto.setGroupMemberName(fieldagent);
					    }
					    if(helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Service Request") 
					    		|| helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Complaints")
					    		|| helpDeskTicketResponseDto.getCategoryName().equalsIgnoreCase("Incident")  ) {
					    if(helpDeskTicket.getKnowledgeBaseId().getId() !=null) {
					    	KnowledgeBase knowledge= helpDeskKnowledgeRepository.getById(helpDeskTicket.getKnowledgeBaseId().getId());
					    	helpDeskTicketResponseDto.setKnowledgeBaseName(knowledge.getKnowledgeSolution());
					    }
					    }
					    else {
					    	helpDeskTicketResponseDto.setKnowledgeBaseName(helpDeskTicket.getQueryKnowledge());
					    }
					    
					    if(helpDeskTicket.getSlaMaster().getId() !=null) {
					    	SlaConfiguration sla= slaConfigurationRepository.getById(helpDeskTicket.getSlaMaster().getId());
					    	helpDeskTicketResponseDto.setSlaName(sla.getRuleName());
					    	helpDeskTicketResponseDto.setSlaDays(sla.getThresholdTime());
					    }
					    if(helpDeskTicket.getStatus().getId() !=null) {
					    	Status ticketstatus= helpDeskTicketStatusRepository.getById(helpDeskTicket.getStatus().getId());
					    	helpDeskTicketResponseDto.setTicketstatusName(ticketstatus.getStatus());
					    }
					    log.info("entitytypeidddd"+ helpDeskTicket.getEntityTypeId());
//					    if(helpDeskTicket.getEntityTypeId()!=null) {
//						 	   EntityTypeMasterDTO entityTypeResponseDto= commonDataController.getEntityTypeById(helpDeskTicket.getEntityTypeId().intValue());
//						 	    helpDeskTicketResponseDto.setEntityTypeName((entityTypeResponseDto != null) ? entityTypeResponseDto.getName()  : null);
//						 
//						 	    helpDeskTicketResponseDto.setEntityTypeMasterDTO(entityTypeResponseDto);
//						 	 }
					    if(helpDeskTicket.getEntityTypeId()!=null) {
							//EntityTypeMasterDTO entityTypeResponseDto= commonDataController.getEntityTypeById(helpDeskTicket.getEntityTypeId().intValue());
							EntityType entityTypeResponseDto= commonDataController.getEntityTypeById(helpDeskTicket.getEntityTypeId());
							helpDeskTicketResponseDto.setEntityTypeName((entityTypeResponseDto != null) ? entityTypeResponseDto.getName()  : null);
							helpDeskTicketResponseDto.setEntityType(entityTypeResponseDto);
						}
					    log.info("entityidddd"+ helpDeskTicket.getEntityId());
					    if(helpDeskTicket.getEntityId()!=null) {
					 	   EntityMasterDTO entityResponseDto= commonDataController.getEntityById(helpDeskTicket.getEntityId().intValue());
					 	    helpDeskTicketResponseDto.setEntityName((entityResponseDto != null) ? entityResponseDto.getName() : null);
					 	
					 	    helpDeskTicketResponseDto.setEntityMasterDTO(entityResponseDto);
					 	 } 
					    
					   
					    helpDeskTicketResponseDto.setCallerNumber(helpDeskTicket.getCallerNumber());
					    helpDeskTicketResponseDto.setDescription(helpDeskTicket.getDescription());
					    helpDeskTicketResponseDto.setNotes(helpDeskTicket.getNotes());
					    helpDeskTicketResponseDto.setTicketNumber(helpDeskTicket.getTicketNumber());
					    helpDeskTicketResponseDto.setLicenceNumber(helpDeskTicket.getLicenceNumber());
					    helpDeskTicketResponseDto.setEmailId(helpDeskTicket.getEmailId());
					    helpDeskTicketResponseDto.setMobileNumber(helpDeskTicket.getMobileNumber());
					    helpDeskTicketResponseDto.setActive(helpDeskTicket.isActive());
					    String createduser=commonDataController.getUserNameById(helpDeskTicket.getCreatedBy());
					    String modifieduser=commonDataController.getUserNameById(helpDeskTicket.getModifiedBy());
					    helpDeskTicketResponseDto.setCreatedBy(createduser);
					    helpDeskTicketResponseDto.setCreatedDate(helpDeskTicket.getCreatedDate());
					    helpDeskTicketResponseDto.setModifiedBy(modifieduser);
					    helpDeskTicketResponseDto.setModifiedDate(helpDeskTicket.getModifiedDate());

//
//				EntityMasterDTO entityMasterDTO = commonDataController
//						.getEntityById(indentRequestEntity.getDistilleryEntityId());
				//indentResponseDto.setDistillery(entityMasterDTO.getName());
				
				helpDeskTicketResponseDtoList.add(helpDeskTicketResponseDto);

			}
			log.debug("found records : " + helpDeskTicketResponseDtoList.size());

			PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
			paginationResponseDTO.setContents(helpDeskTicketResponseDtoList);
			paginationResponseDTO.setTotalElements(totalCount.longValue());
			paginationResponseDTO.setNumberOfElements(resultList.size());
			if (paginationDto.getPaginationSize() > 0 && totalCount > 0) {
				paginationResponseDTO.setTotalPages(totalCount / paginationDto.getPaginationSize());
				if (totalCount < paginationDto.getPaginationSize())
					paginationResponseDTO.setTotalPages(1);
				else if (totalCount % paginationDto.getPaginationSize() > 0) {
					paginationResponseDTO.setTotalPages(paginationResponseDTO.getTotalPages() + 1);
				}

			} else
				paginationResponseDTO.setTotalPages(0);

			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);

		}
	}
	
	
	public GenericResponse createCallDisconnectedTicket(TicketRequestDto helpDeskTicketRequestDto)  throws RecordNotFoundException , Exception
	{
		
		if (helpDeskTicketRequestDto != null ) {
			CallDisconnectedTickets helpDeskTicket = new CallDisconnectedTickets();
			helpDeskTicket.setCallerNumber(helpDeskTicketRequestDto.getCallerNumber());
			Date today=  new Date();
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyss");  
			String strDate = dateFormat.format(today);  
			String ticketFormat= "Ticket"+strDate;
			helpDeskTicket.setTicketNumber(ticketFormat);
			helpDeskTicket.setDescription(helpDeskTicketRequestDto.getDescription());
			helpDeskTicket.setActive(helpDeskTicketRequestDto.isActive());
			callDisconnectedTicketRepository.save(helpDeskTicket);
			
			return Library.getSuccessfulResponse(helpDeskTicket, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getAllCallDisconnectedTicket() {
		List<CallDisconnectedTickets> CallDisconnectedTicketsList = callDisconnectedTicketRepository.findAll();

		if (CallDisconnectedTicketsList.size() > 0) {
			List<TicketResponseDto> HelpDeskTicketResponseDtoList = new ArrayList<TicketResponseDto>();
			CallDisconnectedTicketsList.forEach(pt -> {
				HelpDeskTicketResponseDtoList.add(convertCallDisconnectedTicketToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	private TicketResponseDto convertCallDisconnectedTicketToDto(CallDisconnectedTickets helpDeskTicket) {

		TicketResponseDto helpDeskTicketResponseDto= new TicketResponseDto();

		    helpDeskTicketResponseDto.setId(helpDeskTicket.getId());
		    helpDeskTicketResponseDto.setCallerNumber(helpDeskTicket.getCallerNumber());
		    helpDeskTicketResponseDto.setDescription(helpDeskTicket.getDescription());
		    helpDeskTicketResponseDto.setTicketNumber(helpDeskTicket.getTicketNumber());
		    helpDeskTicketResponseDto.setActive(helpDeskTicket.isActive());
		    String createduser=commonDataController.getUserNameById(helpDeskTicket.getCreatedBy());
		    String modifieduser=commonDataController.getUserNameById(helpDeskTicket.getModifiedBy());
		    helpDeskTicketResponseDto.setCreatedBy(createduser);
		    helpDeskTicketResponseDto.setCreatedDate(helpDeskTicket.getCreatedDate());
		    helpDeskTicketResponseDto.setModifiedBy(modifieduser);
		    helpDeskTicketResponseDto.setModifiedDate(helpDeskTicket.getModifiedDate());
		    return helpDeskTicketResponseDto;

		}

//	private String generateReferenceNo( ) {
//		String referenceNumber = "", prefix = "", currentYear = "", type = "";
//		try {
//			Calendar calendar = Calendar.getInstance();
//			currentYear = String.valueOf(calendar.get(Calendar.YEAR));
//			Long currentValue = sequenceConfig.getCurrentValue();
//			if (String.valueOf(currentValue).length() == 1) {
//				prefix = "00";
//			} else if (String.valueOf(currentValue).length() == 2) {
//				prefix = "0";
//			} else if (String.valueOf(currentValue).length() == 3) {
//				prefix = "";
//			}
//			type = sequenceConfig.getPrefix();
//			referenceNumber = type + currentYear + prefix + (currentValue + 1);
//			log.info("referenceNumber------" + referenceNumber);
//		} catch (Exception e) {
//			log.info("exception in generateReferenceNo method", e);
//		}
//		return referenceNumber;
//	}
	
	public GenericResponse getFieldAgentTickets(Long id) {
		List<Ticket> HelpDeskTicketList = helpDeskTicketRepository.getallFieldEngineerActiveTickets(id);

		if (HelpDeskTicketList.size() > 0) {
			List<TicketResponseDto> HelpDeskTicketResponseDtoList = new ArrayList<TicketResponseDto>();
			HelpDeskTicketList.forEach(pt -> {
				HelpDeskTicketResponseDtoList.add(convertHelpDeskTicketToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	
	
	public GenericResponse getRecentTicketForFieldAgentById(Long id) throws RecordNotFoundException {
		List<Ticket> HelpDeskTicketList = helpDeskTicketRepository.getRecentTicketForFieldAgentById(id);

		if (HelpDeskTicketList.size() > 0) {
			List<TicketResponseDto> HelpDeskTicketResponseDtoList = new ArrayList<TicketResponseDto>();
			HelpDeskTicketList.forEach(pt -> {
				HelpDeskTicketResponseDtoList.add(convertHelpDeskTicketToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskTicketResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse updateTicketStatusForFieldAgent(TicketRequestDto helpDeskTicketRequestDto)  throws RecordNotFoundException , Exception
	{
		
		if (helpDeskTicketRequestDto != null ) {
			
			TicketHistory ticketobj= new TicketHistory();
			Ticket ticket = helpDeskTicketRepository.getById(helpDeskTicketRequestDto.getTicketid());
			ticketobj.setHelpDeskTicket(ticket);
			
			if(helpDeskTicketRequestDto.getTicketstatus() !=null && helpDeskTicketRequestDto.getTicketstatus()>0) {
			Status status= helpDeskTicketStatusRepository.getById(helpDeskTicketRequestDto.getTicketstatus());
			ticketobj.setStatus(status);
			}
			
			if(ticket.getGroupId().getId() !=null ) {
				Group group= helpDeskGroupRepository.getById(ticket.getGroupId().getId());	
				ticketobj.setGroup(group);
			}
			if(ticket.getGroupMemberid() !=null ) {	
				ticketobj.setAssignedBy(ticket.getGroupMemberid());
			}
			
			if(helpDeskTicketRequestDto.getActualproblemId() !=null && helpDeskTicketRequestDto.getActualproblemId()>0) {
			ActualProblem actualproblem=helpDeskAcutalProblemRepository.getById(helpDeskTicketRequestDto.getActualproblemId());
			ticketobj.setActualproblem(actualproblem);
			}
			if(helpDeskTicketRequestDto.getActionTakenId() !=null && helpDeskTicketRequestDto.getActionTakenId()>0) {
		    ActionTaken action = helpDeskActionTakenRepository.getById(helpDeskTicketRequestDto.getActionTakenId());
		    ticketobj.setActionTaken(action);
			}
			ticketobj.setActive(helpDeskTicketRequestDto.isActive());	
			ticketHistoryRepository.save(ticketobj);
			
			return Library.getSuccessfulResponse(ticketobj, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	} 
	
	public GenericResponse searchByLicenceNumber(AuthenticationDTO authenticationDTO ,Locale locale, String applicationNumber ) throws RecordNotFoundException {
		//	User userMaster = userRepository.getByValue(search);search
		if (!applicationNumber.isEmpty()) {
			TicketResponseDto helpDeskTicketResponseDto= new TicketResponseDto();
			 UserDetails userResponseDto= commonDataController.searchByLicenceNumber(applicationNumber);
             if(userResponseDto !=null) {
             userResponseDto.setLicenceNumber(applicationNumber); 
             }
		 	 helpDeskTicketResponseDto.setUserDetails(userResponseDto);
	 			return Library.getSuccessfulResponse(userResponseDto,
						ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
			} else {
				throw new RecordNotFoundException();
			}
			
		
		}
	 
}
