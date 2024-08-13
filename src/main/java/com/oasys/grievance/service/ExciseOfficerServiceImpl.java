package com.oasys.grievance.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.GrievanceRegResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketCountResponseDTO;
import com.oasys.helpdesk.entity.GrievanceregisterEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.GrivenaceRegMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceregRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ExciseOfficerServiceImpl implements ExciseOfficerService{


	@Autowired
	private GrievanceregRepository gregRepository;
	
	@Autowired
	private GrivenaceRegMapper grievancemapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private TicketStatusrepository ticketStatusrepository;

	
	
	
	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO paginationDto, AuthenticationDTO authenticationDTO) throws ParseException {
		Pageable pageable = null;
		Page<GrievanceregisterEntity> list = null;
		Long grievancecategory = null;
		String griveid = null;
		String issueform = null;
		String typeOfUser = null;
		String createdDate = null;	
		String status = null;
		String assignToId=null;
		String grivancetcstatus=null;
		
		Date finalDate = null;
		
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					grievancecategory = Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("referticNumber"))
					&& !paginationDto.getFilters().get("referticNumber").toString().trim().isEmpty()) {
				try {
					griveid = String.valueOf(paginationDto.getFilters().get("referticNumber").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing referticNumber :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("createdDate"))
					&& !paginationDto.getFilters().get("createdDate").toString().trim().isEmpty()) {
				try {
					createdDate = String.valueOf(paginationDto.getFilters().get("createdDate").toString());
					
					try {
						finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdDate + " " + "00:00:00");
					} catch (ParseException e) {
						log.error("error occurred while parsing date : {}", e.getMessage());
						throw new InvalidDataValidation("Invalid date parameter passed");
					}
				
				
				} catch (Exception e) {
					log.error("error occurred while parsing refertic_number :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("issueFrom"))
					&& !paginationDto.getFilters().get("issueFrom").toString().trim().isEmpty()) {
				try {
					issueform = String.valueOf(paginationDto.getFilters().get("issueFrom").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing issueFrom :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("assignToId"))
					&& !paginationDto.getFilters().get("assignToId").toString().trim().isEmpty()) {
				try {
					assignToId = String.valueOf(paginationDto.getFilters().get("assignToId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assignToId :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("grievancetcStatus"))
					&& !paginationDto.getFilters().get("grievancetcStatus").toString().trim().isEmpty()) {
				try {
					grivancetcstatus = String.valueOf(paginationDto.getFilters().get("grievancetcStatus").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing issueFrom :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		
		
		
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("typeOfUser"))
					&& !paginationDto.getFilters().get("typeOfUser").toString().trim().isEmpty()) {
				try {
					typeOfUser = String.valueOf(paginationDto.getFilters().get("typeOfUser").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing typeOfUser :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("status"))
					&& !paginationDto.getFilters().get("status").toString().trim().isEmpty()) {
				try {
					status = String.valueOf(paginationDto.getFilters().get("status").toString());
					
					grivancetcstatus=String.valueOf(paginationDto.getFilters().get("status").toString());
					
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		
//		final Date finalDate;
//		try {
//			finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdDate + " " + "00:00:00");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}


		if (Objects.nonNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByReferticnumberIssuefromCategoryIdCreateddateStatusAndAssigntoid(griveid,issueform, grievancecategory,finalDate,status,typeOfUser,assignToId,pageable);
		}
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser)) {
			list = gregRepository.getByIssuefromAndGrievancetcstatus(issueform, status,typeOfUser,pageable);
		}

		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) ) {
			list = gregRepository.getByIssuefromAndCategoryIdAndTypeofuser(issueform, grievancecategory,typeOfUser,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser)) {
			list = gregRepository.getByIssuefrom(issueform,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByIssuefromTypeofuserAndAssigntoid(issueform,typeOfUser,assignToId,pageable);
		}
	
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) ) {
			list = gregRepository.getByReferticnumber(griveid,pageable);
		}
		
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) ) {
			list = gregRepository.getByCategoryId(grievancecategory,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByCategoryIdTypeofuserAndAssigntoid(grievancecategory,typeOfUser,assignToId,pageable);
		}
		
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser)) {
			list = gregRepository.getByCreateddate(finalDate,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) && Objects.nonNull(assignToId) ) {
			list = gregRepository.getByCreateddateTypeofuserAndAssigntoid(finalDate,typeOfUser,assignToId,pageable);
		}
	
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.isNull(typeOfUser)) {
			list = gregRepository.getByGrievancetcstatus(status,pageable);
		}
		

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByGrievancetcstatusAndTypeofuserAndAssigntoid(status,typeOfUser,assignToId,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser)&& Objects.isNull(assignToId)) {
			list = gregRepository.getByTypeofuser(typeOfUser,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByTypeofuserAndAssigntoid(typeOfUser,assignToId,pageable);
		}
		
		
		
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status)) {
			list = gregRepository.getByReferticnumberCreateddate(griveid,finalDate,pageable);
		}	
	
		
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByReferticnumberTypeofuserAndAssigntoid(griveid,typeOfUser,assignToId,pageable);
		}
		
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByStatusTypeofuserAndAssigntoid(status,typeOfUser,assignToId,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepository.getByAssigntoid(assignToId,pageable);
		}
		
//		if (Objects.nonNull(categoryId) && Objects.nonNull(issueFrom) && Objects.nonNull(typeOfUser) && Objects.isNull(referticNumber) && Objects.isNull(status) && Objects.isNull(createdDate)) {
//			list = gregRepository.getByCategoryIdAndIssuefromAndTypeofuser(categoryId, issueFrom, typeOfUser, pageable);
//		}
//		
//		
//		if (Objects.nonNull(categoryId) && Objects.nonNull(referticNumber) && Objects.nonNull(issueFrom) && Objects.nonNull(typeOfUser)  && Objects.nonNull(status) && Objects.nonNull(createdDate) ) {
//			list = gregRepository.getByCategoryIdIssuefromAndTypeofuserStatus(categoryId, issueFrom, typeOfUser,referticNumber, status, finalDate, pageable);
//		}
//		if (Objects.isNull(categoryId) && Objects.isNull(referticNumber) && Objects.isNull(issueFrom) && Objects.isNull(typeOfUser)  && Objects.nonNull(grivancetcstatus) && Objects.isNull(createdDate) ) {
//			list = gregRepository.getByGrievancetcstatus(grivancetcstatus, pageable);
//		}
//		
//		if (Objects.nonNull(categoryId) && Objects.nonNull(issueFrom) && Objects.nonNull(typeOfUser) && Objects.nonNull(referticNumber) && Objects.isNull(status) && Objects.isNull(createdDate)) {
//			list = gregRepository.getByCategoryIdIssuefromAndTypeofuser(categoryId, issueFrom, typeOfUser,
//					referticNumber, pageable);
//		}
//		if (Objects.nonNull(categoryId) && Objects.nonNull(issueFrom) && Objects.nonNull(typeOfUser) && Objects.isNull(referticNumber) && Objects.nonNull(status) && Objects.nonNull(createdDate)) {
//			list = gregRepository.getByCategoryIdIssuefromAndStatus(categoryId, issueFrom, typeOfUser, status,
//					finalDate, pageable);
//		}
//		if (Objects.nonNull(categoryId) && Objects.nonNull(issueFrom) && Objects.nonNull(typeOfUser) && Objects.isNull(referticNumber) && Objects.isNull(status) && Objects.nonNull(createdDate)) {
//			list = gregRepository.getByCategoryIdIssuefromAndType(categoryId, issueFrom, typeOfUser, finalDate, pageable);
//		}

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceRegResponseDTO> finalResponse = list.map(grievancemapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse ListByAll(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<GrievanceregisterEntity> list = null;
		String assignto_Id = null;
		String typeofuser = null;
		String grievancetcStatus = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("assignto_Id"))
					&& !paginationDto.getFilters().get("assignto_Id").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
//					assignto = Long.valueOf(paginationDto.getFilters().get("assignto").toString());
					assignto_Id = (String) paginationDto.getFilters().get("assignto_Id");
				} catch (Exception e) {
					log.error("error occurred while parsing assignto_Id :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("typeofuser"))
					&& !paginationDto.getFilters().get("typeofuser").toString().trim().isEmpty()) {
				try {
					typeofuser = String.valueOf(paginationDto.getFilters().get("typeofuser").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing typeofuser :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get("grievancetcStatus"))
					&& !paginationDto.getFilters().get("grievancetcStatus").toString().trim().isEmpty()) {
				try {
					grievancetcStatus = String.valueOf(paginationDto.getFilters().get("grievancetcStatus").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing grievancetcStatus :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		
		String assignid=assignto_Id;

		if (Objects.nonNull(assignto_Id) && Objects.nonNull(typeofuser) && Objects.nonNull(grievancetcStatus)) {
			list = gregRepository.getByAssigntoIdTypeofuserAndGrievancetcStatus(assignto_Id, typeofuser,
					grievancetcStatus, pageable);
		} 
		
//		else if (Objects.nonNull(assignto_Id) && Objects.nonNull(typeofuser)) {
//			list = gregRepository.getByAssigntoIdAndTypeofuser(assignto_Id,assignto_Id, typeofuser, pageable);
//		}
//		
//		else if (Objects.nonNull(assignto_Id) && Objects.isNull(typeofuser)) {
//			list = gregRepository.getByAssigntoId(assignto_Id,assignto_Id, pageable);
//		}
		 if (Objects.nonNull(assignid) && Objects.nonNull(typeofuser)) {
			list = gregRepository.getByAssigntoIdAndTypeofuser(assignid,typeofuser, pageable);
		}
		
//		 if (Objects.nonNull(assignid) && Objects.nonNull(typeofuser)) {
//			list = gregRepository.getByAssigngroup(assignid,typeofuser,pageable);
//		}
		
		
		 if (Objects.isNull(assignto_Id) && Objects.nonNull(typeofuser)) {
			list = gregRepository.getByTypeofuser(typeofuser , pageable);
		}
		
		if (Objects.isNull(list) || list.isEmpty()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			
			list = gregRepository.getByAssigngroup(assignid,typeofuser,pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceRegResponseDTO> finalResponse = list.map(grievancemapper::convertEntityToResponseDTOList);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	

	@Override
	public GenericResponse getById(Long id) {
		Optional<GrievanceregisterEntity> entity = gregRepository.findById(id);
		
		
		if (!entity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievancemapper.convertEntityToResponseDTO(entity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	
	@Override
	public GenericResponse getCount(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO) {
		String date=requestDto.getDate();
		String todate=requestDto.getTodate();
		String assignto_Id = requestDto.getAssignToId();
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		
		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		List<TicketStatusEntity> ticketList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusList = gregRepository.getCreatedByAndCreatedDateAndAssignTO(fromDate,toDate,assignto_Id);
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}
		
		ticketList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
//			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
//					|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
//				log.error("invalid authentication details : {}", authenticationDTO);
//				throw new RecordNotFoundException("No Record Found");
//			}
		
			ticketCountResponseDTO
			.setCount(gregRepository.getCountByStatusAndCreatedByAndCreatedDateAndAssignTO(ticketStatus.getTicketstatusname(),fromDate,toDate,assignto_Id));		
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});
		
		
//		int totalList = ticketList.size();
//		
//		int totalActive = ticketStatusList.size();
//		
//		List<GrievanceregisterEntity> pendingTickets = ticketStatusList.stream().filter(item->item.getGrievancetcstatus() != null && item.getGrievancetcstatus().equals("Pending")).collect(Collectors.toList());
//		
//		if(!CollectionUtils.isEmpty(pendingTickets)) {
//			int pendingCount = pendingTickets.size();
//			double pendingPercentage = (pendingCount*100)/totalList;
//			System.out.println("pending perentage="+pendingPercentage);
//			GrievanceTicketCountResponseDTO statusPendingItem = new GrievanceTicketCountResponseDTO();
//			statusPendingItem.setCount(pendingCount);
//			statusPendingItem.setPercentage(pendingPercentage);
//			statusPendingItem.setStatus("Pending");
//			
//			ticketCountResponseDTOList.add(statusPendingItem);
//		}
//		
//		List<GrievanceregisterEntity> escalateTickets = ticketStatusList.stream().filter(item->item.getGrievancetcstatus() != null && item.getGrievancetcstatus().equals("Escalated")).collect(Collectors.toList());
//		
//		if(!CollectionUtils.isEmpty(escalateTickets)) {
//			int EscalatedCount = escalateTickets.size();
//			double EscalatedPercentage = (EscalatedCount*100)/totalList;
//			System.out.println("Escalated perentage="+ EscalatedPercentage);
//			GrievanceTicketCountResponseDTO statusEscalatedItem = new GrievanceTicketCountResponseDTO();
//			statusEscalatedItem.setCount(EscalatedCount);
//			statusEscalatedItem.setPercentage(EscalatedPercentage);
//			statusEscalatedItem.setStatus("Escalated");
//			
//			ticketCountResponseDTOList.add(statusEscalatedItem);
//		}
//		List<GrievanceregisterEntity> closeTickets = ticketStatusList.stream().filter(item->item.getGrievancetcstatus() != null && item.getGrievancetcstatus().equals("Closed")).collect(Collectors.toList());
//		
//		if(!CollectionUtils.isEmpty(closeTickets)) {
//			int ClosedCount = closeTickets.size();
//			double ClosedPercentage = (ClosedCount*100)/totalList;
//			System.out.println("Closed perentage="+ClosedPercentage);
//			GrievanceTicketCountResponseDTO statusClosedItem = new GrievanceTicketCountResponseDTO();
//			statusClosedItem.setCount(ClosedCount);
//			statusClosedItem.setPercentage(ClosedPercentage);
//			statusClosedItem.setStatus("closed");
//			
//			ticketCountResponseDTOList.add(statusClosedItem);
//		}
//		List<GrievanceregisterEntity> openTickets = ticketStatusList.stream().filter(item->item.getGrievancetcstatus() != null && item.getGrievancetcstatus().equals("Opned")).collect(Collectors.toList());
//		
//		if(!CollectionUtils.isEmpty(openTickets)) {
//			int OpnedCount = openTickets.size();
//			double OpnedPercentage = (OpnedCount*100)/totalList;
//			System.out.println("Opned perentage="+OpnedPercentage);
//			GrievanceTicketCountResponseDTO statusOpnedItem = new GrievanceTicketCountResponseDTO();
//			statusOpnedItem.setCount(OpnedCount);
//			statusOpnedItem.setPercentage(OpnedPercentage);
//			statusOpnedItem.setStatus("Opned");
//			
//			ticketCountResponseDTOList.add(statusOpnedItem);
//		}
//		List<GrievanceregisterEntity> assignedTickets = ticketStatusList.stream().filter(item->item.getGrievancetcstatus() != null && item.getGrievancetcstatus().equals("Assigned")).collect(Collectors.toList());
//		
//		if(!CollectionUtils.isEmpty(assignedTickets)) {
//			int assignedCount = assignedTickets.size();
//			double assignedPercentage = (assignedCount*100)/totalList;
//			System.out.println("Assigned perentage="+assignedPercentage);
//			GrievanceTicketCountResponseDTO statusOpnedItem = new GrievanceTicketCountResponseDTO();
//			statusOpnedItem.setCount(assignedCount);
//			statusOpnedItem.setPercentage(assignedPercentage);
//			statusOpnedItem.setStatus("Assigned");
//			
//			ticketCountResponseDTOList.add(statusOpnedItem);
//		}
		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}
	
	
	

	@Override
	public GenericResponse getCountscreen(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO) {
		String date=requestDto.getDate();
		String todate=requestDto.getTodate();
		String assignto_Id = requestDto.getAssignToId();
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		
		if(requestDto.getRolename().equalsIgnoreCase("ASSIGN_TO_OFFICER")) {
		
		List<TicketStatusEntity> ticketList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

		List<GrievanceregisterEntity> ticketStatusList = gregRepository.getCreatedByAndCreatedDateAndAssignTO(fromDate,toDate,assignto_Id);
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}
		
		ticketList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
//			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
//					|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
//				log.error("invalid authentication details : {}", authenticationDTO);
//				throw new RecordNotFoundException("No Record Found");
//			}
		
			ticketCountResponseDTO
			.setCount(gregRepository.getCountByStatusAndCreatedByAndCreatedDateAndAssignTO(ticketStatus.getTicketstatusname(),fromDate,toDate,assignto_Id));		
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});
		
		}
		
		if(requestDto.getRolename().equalsIgnoreCase("HANDLING_OFFICER")) {
			List<TicketStatusEntity> ticketList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();

			List<GrievanceregisterEntity> ticketStatusList = gregRepository.getCreatedByAndCreatedDateAndAssigngroup(fromDate,toDate,assignto_Id);
			
			if (CollectionUtils.isEmpty(ticketStatusList)) {
				throw new RecordNotFoundException("No Record Found");
			}
			
			ticketList.forEach(ticketStatus -> {
				if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
					return;
				}
				TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
				ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
//				if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
//						|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
//					log.error("invalid authentication details : {}", authenticationDTO);
//					throw new RecordNotFoundException("No Record Found");
//				}
			
				ticketCountResponseDTO
				.setCount(gregRepository.getCountByStatusAndCreatedByAndCreatedDateAndAssigngroup(ticketStatus.getTicketstatusname(),fromDate,toDate,assignto_Id));		
				ticketCountResponseDTOList.add(ticketCountResponseDTO);
			});
				
		}
		
		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}
	
	
	

}
