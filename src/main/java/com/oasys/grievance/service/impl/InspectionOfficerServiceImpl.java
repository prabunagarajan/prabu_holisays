package com.oasys.grievance.service.impl;

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

import com.oasys.grievance.service.InspectionOfficerService;
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
import com.oasys.helpdesk.repository.GrievanceregRepositoryUP;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class InspectionOfficerServiceImpl implements InspectionOfficerService{

	@Autowired
	private GrievanceregRepository gregRepository;
	
	@Autowired
	private GrievanceregRepositoryUP gregRepositoryup;
	
	@Autowired
	private GrivenaceRegMapper grievancemapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	@Autowired
	private TicketStatusrepository ticketStatusrepository;
	
	@Autowired
	private EntityManager entityManager;
	

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
					assignto_Id = (String) paginationDto.getFilters().get("assignto_Id");
//					assignto_Id = Long.valueOf(paginationDto.getFilters().get("assignto_Id").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assignto :: {}", e);
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
		
		if (Objects.nonNull(assignto_Id) && Objects.nonNull(typeofuser) && Objects.nonNull(grievancetcStatus)) {
			list = gregRepository.getByAssigntoIdTypeofuserAndGrievancetcStatus(assignto_Id, typeofuser,
					grievancetcStatus, pageable);
		} else if (Objects.nonNull(assignto_Id) && Objects.nonNull(typeofuser)) {
			//list = gregRepository.getByAssigntoIdAndTypeofuser(assignto_Id, typeofuser, pageable);
		}
		
		else if (Objects.nonNull(assignto_Id) && Objects.isNull(typeofuser)) {
			//list = gregRepository.getByAssigntoId(assignto_Id, pageable);
		}
		else if (Objects.isNull(assignto_Id) && Objects.nonNull(typeofuser)) {
			//list = gregRepository.getByTypeofuser(typeofuser , pageable);
		}
		
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceRegResponseDTO> finalResponse = list.map(grievancemapper::convertEntityToResponseDTO);
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
			if (Objects.nonNull(paginationDto.getFilters().get("grievanceId"))
					&& !paginationDto.getFilters().get("grievanceId").toString().trim().isEmpty()) {
				try {
					griveid = String.valueOf(paginationDto.getFilters().get("grievanceId").toString());
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
			list = gregRepositoryup.getByReferticnumberIssuefromCategoryIdCreateddateStatusAndAssigngroup(griveid,issueform, grievancecategory,finalDate,status,typeOfUser,assignToId,assignToId,pageable);
		}
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser)) {
			list = gregRepositoryup.getByIssuefromAndGrievancetcstatus(issueform, status,typeOfUser,pageable);
		}

		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) ) {
			list = gregRepositoryup.getByIssuefromAndCategoryIdAndTypeofuser(issueform, grievancecategory,typeOfUser,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser)) {
			list = gregRepositoryup.getByIssuefrom(issueform,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByIssuefromAndTypeofuserAndAssigngroup(issueform,typeOfUser,assignToId,assignToId,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.nonNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByIssuefromAndTypeofuserAndAssigngroupAndCategoryId(issueform,typeOfUser,assignToId,assignToId,grievancecategory,pageable);
		}
	
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) ) {
			list = gregRepositoryup.getByReferticnumber(griveid,pageable);
		}
		
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId) ) {
			list = gregRepositoryup.getByReferticnumberAndAssigngroupAndTypeofuser(griveid,assignToId,assignToId,typeOfUser,pageable);
		}
	
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) ) {
			list = gregRepositoryup.getByCategoryId(grievancecategory,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByCategoryIdAndTypeofuserAndAssigngroup(grievancecategory,typeOfUser,assignToId,assignToId,pageable);
		}
		
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser)) {
			list = gregRepositoryup.getByCreateddate(finalDate,pageable);
		}
		
//		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) && Objects.nonNull(assignToId) ) {
//			list = gregRepositoryup.getByCreateddateAndTypeofuserAndAssigngroup(finalDate,typeOfUser,assignToId,assignToId,pageable);
//		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) && Objects.nonNull(assignToId) ) {
			list = gregRepositoryup.getTypeofuserAndCreateddateAndAssigngroup(typeOfUser,finalDate,assignToId,assignToId,pageable);
		}
	
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.nonNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) && Objects.nonNull(assignToId) ) {
			list = gregRepositoryup.getByCreateddateAndTypeofuserAndAssigngroupAndCategoryId(finalDate,typeOfUser,assignToId,assignToId,grievancecategory,pageable);
		}
	
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.isNull(typeOfUser)) {
			list = gregRepositoryup.getByGrievancetcstatus(status,pageable);
		}
		

		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByGrievancetcstatusAndTypeofuserAndAssigngroup(status,typeOfUser,assignToId,assignToId,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser)&& Objects.isNull(assignToId)) {
			list = gregRepositoryup.getByTypeofuser(typeOfUser,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByTypeofuserAndAssigngroup(typeOfUser,assignToId,assignToId,pageable);
		}
		
		
		
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.nonNull(finalDate) && Objects.isNull(status)) {
			list = gregRepositoryup.getByReferticnumberCreateddate(griveid,finalDate,pageable);
		}	
	
		
		else if (Objects.nonNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByReferticnumberAndTypeofuserAndAssigngroup(griveid,typeOfUser,assignToId,assignToId,pageable);
		}
		
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.nonNull(status) && Objects.nonNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByStatusAndTypeofuserAndAssigngroup(status,typeOfUser,assignToId,assignToId,pageable);
		}
		
		else if (Objects.isNull(griveid) && Objects.isNull(issueform) && Objects.isNull(grievancecategory) && Objects.isNull(finalDate) && Objects.isNull(status) && Objects.isNull(typeOfUser) && Objects.nonNull(assignToId)) {
			list = gregRepositoryup.getByAssigngroup(assignToId,assignToId,pageable);
		}
		

		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceRegResponseDTO> finalResponse = list.map(grievancemapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
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
			if (Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())
					|| CollectionUtils.isEmpty(authenticationDTO.getRoleCodes())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
		
			ticketCountResponseDTO
			.setCount(gregRepository.getCountByStatusAndCreatedByAndCreatedDateAndAssignTO(ticketStatus.getTicketstatusname(),fromDate,toDate,assignto_Id));		
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});
		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}
	
	

}
