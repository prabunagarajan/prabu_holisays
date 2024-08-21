package com.oasys.helpdesk.service;

import static com.oasys.posasset.constant.Constant.ASC;

import static com.oasys.posasset.constant.Constant.ASC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.EalWastageDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.EalWastage;
import com.oasys.helpdesk.entity.EalWastageLogEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.EalWastageLogRepository;
import com.oasys.helpdesk.repository.EalWastageRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestAECEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestLogAECEntity;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EalWastageServiceImpl implements EalWastageService {
	@Autowired
	private EalWastageRepository repository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private EalWastageLogRepository ealWastageLogRepository;
//
//	@Autowired
//	private WorkFlowService workFlowService;
//	
//	@Autowired
//	private EALRequestAECRepository ealRequestAECRepository;

	@Override
	public GenericResponse getWastageById(Long id) {
		try {
			Optional<EalWastage> ealWastageOptional = repository.findById(id);
			if (!ealWastageOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			return Library.getSuccessfulResponse(commonUtil.modalMap(ealWastageOptional.get(), EalWastageDTO.class),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} catch (Exception e) {
			log.error("error occurred while fetching ealwastage record :: id :{}, exception {}", id, e);
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
	}

	public GenericResponse createWastage(EalWastageDTO wastageRequest) {
		try {
			// Retrieve all records with the given ApplicationNo
			List<EalWastage> ealDetailsList = repository.findByApplicationNo(wastageRequest.getApplicationNo());

			if (!ealDetailsList.isEmpty()) {
				// If there are multiple records, return an error response
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record already exists");
			}

			List<EalWastage> ealDetailsList1 = repository
					.findByBottlingPlanIdAndRequestStatus(wastageRequest.getBottlingPlanId());

			if (!ealDetailsList1.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						"Already Requested Wastage Not Yet Approve");
			}
			
//			List<EALRequestAECEntity> ealbottling = ealRequestAECRepository
//					.getByRequestedapplnNo(wastageRequest.getBottlingPlanId());
			
//			if(ealbottling.isEmpty()) {
//				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
//						"Selected Bottling Number has not been registered in the bottling plan yet.");
//			}else if (!ealbottling.isEmpty() && !ealbottling.get(0).getStatus().equals(ApprovalStatus.APPROVED)) {
//				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
//						"EAL Requested Not Yet Approve For Given Bottling No");
//			}

			// Map the wastageRequest DTO to EalWastage entity
			EalWastage ealWastageEntity = new EalWastage();
			ealWastageEntity.setApplicationDate(wastageRequest.getApplicationDate());
			ealWastageEntity.setApplicationNo(wastageRequest.getApplicationNo());
			ealWastageEntity.setStatus(wastageRequest.getStatus());
			ealWastageEntity.setEntityName(wastageRequest.getEntityName());
			ealWastageEntity.setEntityAddress(wastageRequest.getEntityAddress());
			ealWastageEntity.setLicenseType(wastageRequest.getLicenseType());
			ealWastageEntity.setEntityType(wastageRequest.getEntityType());
			ealWastageEntity.setLicenseNumber(wastageRequest.getLicenseNumber());
			ealWastageEntity.setCodeType(wastageRequest.getCodeType());
			ealWastageEntity.setBottlingPlanId(wastageRequest.getBottlingPlanId());
			ealWastageEntity.setLiquorType(wastageRequest.getLiquorType());
			ealWastageEntity.setLiquorSubType(wastageRequest.getLiquorSubType());
			ealWastageEntity.setLiquorDetailedDescription(wastageRequest.getLiquorDetailedDescription());
			ealWastageEntity.setBrandName(wastageRequest.getBrandName());
			ealWastageEntity.setPackageSize(wastageRequest.getPackageSize().replace("ML", ""));
			ealWastageEntity.setPlannedNumberOfBottles(wastageRequest.getPlannedNumberOfBottles());
			ealWastageEntity.setPlannedNumberOfCases(wastageRequest.getPlannedNumberOfCases());
			ealWastageEntity.setDamagedNumberOfBarcode(wastageRequest.getDamagedNumberOfBarcode());
			ealWastageEntity.setDamagedNumberOfBottleQRCode(wastageRequest.getDamagedNumberOfBottleQRCode());
			ealWastageEntity.setDistrict(wastageRequest.getDistrict());
			ealWastageEntity.setMapType(wastageRequest.getMapType());
			ealWastageEntity.setUnmappedType(wastageRequest.getUnmappedType());
			ealWastageEntity.setPrintingType(wastageRequest.getPrintingType());
			ealWastageEntity.setDistrict(wastageRequest.getDistrict());
			ealWastageEntity.setFinancialYear(wastageRequest.getFinancialYear());
			ealWastageEntity.setRequestStatus(0);
			ealWastageEntity.setCartonSize(wastageRequest.getCartonSize());
			ealWastageEntity.setDateOfPackaging(wastageRequest.getDateOfPackaging());
			ealWastageEntity.setUsedQrCode(wastageRequest.getUsedQrCode());
			ealWastageEntity.setUsedBrCode(wastageRequest.getUsedBrCode());
			ealWastageEntity.setBalanceBrCode(wastageRequest.getBalanceBrCode());
			ealWastageEntity.setBalanceQrCode(wastageRequest.getBalanceQrCode());
			ealWastageEntity.setBottledBrCode(wastageRequest.getBottledBrCode());
			ealWastageEntity.setBottledQrCode(wastageRequest.getBottledQrCode());
			ealWastageEntity.setCreatedBy(wastageRequest.getCreatedBy());

			// Save the EalWastage entity
			repository.save(ealWastageEntity);

			try {
				WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
				EalWastageLogEntity ealWastageLogEntity = new EalWastageLogEntity();
				ealWastageLogEntity.setComments("EAL saved successfully");
				ealWastageLogEntity.setApplnNo(wastageRequest.getRequestedapplnNo());
				ealWastageLogEntity.setUserName(wastageRequest.getUserName());
				ealWastageLogEntity.setAction(wastageRequest.getAction());
				ealWastageLogEntity.setRemarks(wastageRequest.getRemarks());
				ealWastageLogRepository.save(ealWastageLogEntity);
				if (wastageRequest.getEntityName().equalsIgnoreCase("BREWERY")
						|| wastageRequest.getEntityName().equalsIgnoreCase("BOTTELING")
						|| wastageRequest.getEntityName().equalsIgnoreCase("DISTILLERY")) {
					log.info(wastageRequest.getEntityName());
					workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
				} else {
					workflowStatusUpdateDto.setSubModuleNameCode(wastageRequest.getSubModuleNameCode());
				}
				workflowStatusUpdateDto.setApplicationNumber(wastageRequest.getApplicationNo());
				workflowStatusUpdateDto.setModuleNameCode(wastageRequest.getModuleNameCode());
				workflowStatusUpdateDto.setEvent(wastageRequest.getEvent());
				workflowStatusUpdateDto.setLevel(wastageRequest.getLevel());
//				workFlowService.callEALWastageWorkFlowService(workflowStatusUpdateDto);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("EAL Log " + e);
			}

			// Return a successful response
			return Library.getSuccessfulResponse(ealWastageEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		} catch (Exception e) {
			log.error("Error occurred while creating EAL Wastage: ", e);
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	@Override
	public GenericResponse updateWastage(Long id, EalWastageDTO updatedWastageRequestDTO) {
		try {
			Optional<EalWastage> ealWastageOptional = repository.findById(id);
			if (!ealWastageOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
			updatedWastageRequestDTO.setId(id);
			EalWastage ealWastage = ealWastageOptional.get();
			commonUtil.modalMapCopy(updatedWastageRequestDTO, ealWastage);
			repository.save(ealWastage);

			// Update workflow status
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
			String entityName = updatedWastageRequestDTO.getEntityName();
			if ("BREWERY".equalsIgnoreCase(entityName) || "BOTTELING".equalsIgnoreCase(entityName)) {
				workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
			} else if ("DISTILLERY".equalsIgnoreCase(entityName)) {
				workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTDISTILLERY");
			} else {
				workflowStatusUpdateDto.setSubModuleNameCode(updatedWastageRequestDTO.getSubModuleNameCode());
			}
			workflowStatusUpdateDto.setApplicationNumber(updatedWastageRequestDTO.getApplicationNo());
			workflowStatusUpdateDto.setModuleNameCode(updatedWastageRequestDTO.getModuleNameCode());
			workflowStatusUpdateDto.setEvent(updatedWastageRequestDTO.getEvent());
			workflowStatusUpdateDto.setLevel(updatedWastageRequestDTO.getLevel());
//			workFlowService.callEALWastageWorkFlowService(workflowStatusUpdateDto);

			return Library.getSuccessfulResponse(commonUtil.modalMap(ealWastage, EalWastageDTO.class),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_UPDATED);
		} catch (Exception e) {
			log.error("error occurred while updating ealwastage record : {}", e);
			return Library.getFailResponseCode(ErrorCode.RECORD_UPDATION_FAILED.getErrorCode(),
					ResponseMessageConstant.RECORD_UPDATION_FAILED.getMessage());
		}
	}

	@Override
	public GenericResponse deleteWastage(Long id) {
		try {
			Optional<EalWastage> ealWastageOptional = repository.findById(id);
			if (!ealWastageOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
			repository.delete(ealWastageOptional.get());
			return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_DELETED);
		} catch (Exception e) {
			log.error("error occurred while deleting ealwastage record :: id :{}, exception {}", id, e);
			return Library.getFailResponseCode(ErrorCode.RECORD_DELETION_FAILED.getErrorCode(),
					ResponseMessageConstant.RECORD_DELETION_FAILED.getMessage());
		}
	}

//	@Override
//	public GenericResponse getBySearchFilter(PaginationRequestDTO requestDTO) {
//		Pageable pageable = null;
//		Page<EalWastage> list = null;
//
//		if (StringUtils.isBlank(requestDTO.getSortField())) {
//			requestDTO.setSortField(Constant.MODIFIED_DATE);
//		}
//		if (StringUtils.isBlank(requestDTO.getSortOrder())) {
//			requestDTO.setSortOrder(DESC);
//		}
//		if(Objects.isNull(requestDTO.getPageNo())) {
//			requestDTO.setPageNo(0);
//		}
//		if(Objects.isNull(requestDTO.getPaginationSize())) {
//			requestDTO.setPageNo(20);
//		}
//		if (requestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
//			pageable = PageRequest.of(requestDTO.getPageNo(), requestDTO.getPaginationSize(),
//					Sort.by(Direction.ASC, requestDTO.getSortField()));
//		} else {
//			pageable = PageRequest.of(requestDTO.getPageNo(), requestDTO.getPaginationSize(),
//					Sort.by(Direction.DESC, requestDTO.getSortField()));
//		}
//		
//		try {
//			if (StringUtils.isNotBlank(requestDTO.getFromDate()) && StringUtils.isNotBlank(requestDTO.getToDate())) {
//				if (Boolean.FALSE.equals(CommonUtil.isDateValid(requestDTO.getFromDate()))) {
//					return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {Constant.FROM_DATE}));
//
//				}
//				if (Boolean.FALSE.equals(CommonUtil.isDateValid(requestDTO.getToDate()))) {
//					return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//							ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {Constant.TO_DATE}));
//
//				}
//				list = repository.getByFromDateAndToDate(pageable,
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getFromDate() + " " + "00:00:00"),
//						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getToDate() + " " + "23:59:59"));
//			}
//			else {
//				list = repository.getAll(pageable);
//
//			}
//			if (Objects.isNull(list) || list.isEmpty()) {
//				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//
//			}
//			Page<EalWastageDTO> finalResponse = list
//					.map(e -> commonUtil.modalMap(e, EalWastageDTO.class));
//			return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
//					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//
//		} catch (Exception e) {
//			log.error("error occurred while fetching ealwastage list : {}, request: {}", e, requestDTO);
//
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//	}

//	@Override
//	public GenericResponse getBySearchFilter(PaginationRequestDTO requestDTO) {
//	    Pageable pageable = null;
//	    Page<EalWastage> list = null;
//
//	    if (StringUtils.isBlank(requestDTO.getSortField())) {
//	        requestDTO.setSortField(Constant.MODIFIED_DATE);
//	    }
//	    if (StringUtils.isBlank(requestDTO.getSortOrder())) {
//	        requestDTO.setSortOrder(DESC);
//	    }
//	    if (Objects.isNull(requestDTO.getPageNo())) {
//	        requestDTO.setPageNo(0);
//	    }
//	    if (Objects.isNull(requestDTO.getPaginationSize())) {
//	        requestDTO.setPaginationSize(20);
//	    }
//	    if (requestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
//	        pageable = PageRequest.of(requestDTO.getPageNo(), requestDTO.getPaginationSize(),
//	                Sort.by(Direction.ASC, requestDTO.getSortField()));
//	    } else {
//	        pageable = PageRequest.of(requestDTO.getPageNo(), requestDTO.getPaginationSize(),
//	                Sort.by(Direction.DESC, requestDTO.getSortField()));
//	    }
//
//	    try {
//	        // Date validation and parsing
//	    	 final Date[] fromDate = {null};
//	         final Date[] toDate = {null};
//	        if (StringUtils.isNotBlank(requestDTO.getFromDate()) && StringUtils.isNotBlank(requestDTO.getToDate())) {
//	            if (!CommonUtil.isDateValid(requestDTO.getFromDate())) {
//	                return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//	                        ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.FROM_DATE }));
//	            }
//	            if (!CommonUtil.isDateValid(requestDTO.getToDate())) {
//	                return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
//	                        ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.TO_DATE }));
//	            }
//	            fromDate[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getFromDate() + " " + "00:00:00");
//	            toDate [0]= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getToDate() + " " + "23:59:59");
//	        }
//
//	        // Building dynamic query based on provided filters
//	        Specification<EalWastage> spec = Specification.where(null);
//
//	        if (StringUtils.isNotBlank(requestDTO.getCodeType())) {
//	            spec = spec.and((root, query, criteriaBuilder) ->
//	                    criteriaBuilder.equal(root.get("codeType"), requestDTO.getCodeType()));
//	        }
//	        if (StringUtils.isNotBlank(requestDTO.getApplicationNo())) {
//	            spec = spec.and((root, query, criteriaBuilder) ->
//	                    criteriaBuilder.equal(root.get("applicationNo"), requestDTO.getApplicationNo()));
//	        }
//	        if (StringUtils.isNotBlank(requestDTO.getLicenseNumber())) {
//	            spec = spec.and((root, query, criteriaBuilder) ->
//	                    criteriaBuilder.equal(root.get("licenseNumber"), requestDTO.getLicenseNumber()));
//	        }
//	        if (fromDate[0]!= null && toDate[0] != null) {
//	            spec = spec.and((root, query, criteriaBuilder) ->
//	                    criteriaBuilder.between(root.get("modifiedDate"), fromDate[0], toDate[0]));
//	        }
//	        else {
//	        list = repository.getAll(spec,pageable);
//	        }
//	        if (Objects.isNull(list) || list.isEmpty()) {
//	            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//	                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//	        }
//
//	        Page<EalWastageDTO> finalResponse = list.map(e -> commonUtil.modalMap(e, EalWastageDTO.class));
//	        return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
//	                ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//
//	     
//	        }catch (Exception e) {
//	        log.error("error occurred while fetching ealwastage list : {}, request: {}", e, requestDTO);
//	        return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//	                ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//	    }
//	}

	public GenericResponse getBySearchFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EalWastage> list = this.getSubRecordsByFilterDTO1(requestData);
		List<EalWastage> list1 = this.getSubRecordsByFilterDTO2(requestData);
		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
		if (!list.isEmpty()) {
			paginationResponseDTO.setContents(list);
		}
		Long count1 = (long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list1.size()) ? list1.size() : null);
		paginationResponseDTO.setTotalElements(count1);
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public List<EalWastage> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EalWastage> cq = cb.createQuery(EalWastage.class);
		Root<EalWastage> from = cq.from(EalWastage.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EalWastage> typedQuery = null;
		addSubCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get("createdDate")));
		}
		if (Objects.nonNull(filterRequestDTO.getPageNo()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}
		return typedQuery.getResultList();
	}

	public List<EalWastage> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EalWastage> cq = cb.createQuery(EalWastage.class);
		Root<EalWastage> from = cq.from(EalWastage.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EalWastage> typedQuery1 = null;
		addSubCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		}
		typedQuery1 = entityManager.createQuery(cq);
		return typedQuery1.getResultList();
	}

	private void addSubCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<EalWastage> from) {

		Date fromDate = null;

		Date toDate = null;

		String fdate = null;

		String tdate = null;

		if (Objects.nonNull(filterRequestDTO.getFilters().get("fromDate"))
				&& !filterRequestDTO.getFilters().get("fromDate").toString().trim().isEmpty()) {
			try {
				try {
					fdate = String.valueOf(filterRequestDTO.getFilters().get("fromDate").toString());
					fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
			} catch (Exception e) {
				log.error("error occurred while parsing refertic_number :: {}", e);
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("toDate"))
				&& !filterRequestDTO.getFilters().get("toDate").toString().trim().isEmpty()) {
			try {
				try {
					tdate = String.valueOf(filterRequestDTO.getFilters().get("toDate").toString());
					toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
					list.add(cb.between(from.get("createdDate"), fromDate, toDate));
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
			} catch (Exception e) {
				log.error("error occurred while parsing refertic_number :: {}", e);
			}
		}

//		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseNo"))
//				&& !filterRequestDTO.getFilters().get("licenseNo").toString().trim().isEmpty()) {
//			List<String> licenseNo = (List<String>) (filterRequestDTO.getFilters().get("licenseNo"));
//			if (!licenseNo.isEmpty()) {
//				Expression<String> mainModule = from.get("licenseNo");
//				list.add(mainModule.in(licenseNo));
//			}
//		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("unitCode"))
				&& !filterRequestDTO.getFilters().get("unitCode").toString().trim().isEmpty()) {
			List<String> unitCode = (List<String>) (filterRequestDTO.getFilters().get("unitCode"));
			if (!unitCode.isEmpty()) {
				Expression<String> mainModule = from.get("unitCode");
				list.add(mainModule.in(unitCode));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseNo"))
				&& !filterRequestDTO.getFilters().get("licenseNo").toString().trim().isEmpty()) {
			List<String> licenseNumber = (List<String>) (filterRequestDTO.getFilters().get("licenseNo"));
			if (!licenseNumber.isEmpty()) {
				Expression<String> mainModule = from.get("licenseNumber");
				list.add(mainModule.in(licenseNumber));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("codeType"))
				&& !filterRequestDTO.getFilters().get("codeType").toString().trim().isEmpty()) {

			String codetype = filterRequestDTO.getFilters().get("codeType").toString();
			list.add(cb.equal(from.get("codeType"), codetype));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("applicationNo"))
				&& !filterRequestDTO.getFilters().get("applicationNo").toString().trim().isEmpty()) {

			String applicationNo = filterRequestDTO.getFilters().get("applicationNo").toString();
			list.add(cb.equal(from.get("applicationNo"), applicationNo));
		}

//		if (Objects.nonNull(filterRequestDTO.getFilters().get("unitCode"))
//				&& !filterRequestDTO.getFilters().get("unitCode").toString().trim().isEmpty()) {
//
//			String unitCode = (filterRequestDTO.getFilters().get("unitCode").toString());
//			list.add(cb.equal(from.get("unitCode"), unitCode));
//		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseType"))
				&& !filterRequestDTO.getFilters().get("licenseType").toString().trim().isEmpty()) {

			String licencetype = (filterRequestDTO.getFilters().get("licenseType").toString());
			list.add(cb.equal(from.get("licenseType"), licencetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("district"))
				&& !filterRequestDTO.getFilters().get("district").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("district").toString());
			list.add(cb.equal(from.get("district"), district));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("requestedapplnNo"))
				&& !filterRequestDTO.getFilters().get("requestedapplnNo").toString().trim().isEmpty()) {

			String requestedapplnNo = (filterRequestDTO.getFilters().get("requestedapplnNo").toString());
			list.add(cb.equal(from.get("requestedapplnNo"), requestedapplnNo));
		}

		if (filterRequestDTO.getFilters().containsKey("currentlyWorkwith")
				&& !filterRequestDTO.getFilters().get("currentlyWorkwith").toString().trim().isEmpty()) {
			Predicate currentlyWorkwith = cb.equal(from.get("currentlyWorkwith"),
					filterRequestDTO.getFilters().get("currentlyWorkwith"));
			Predicate approvedBy = cb.equal(from.get("approvedBy"),
					filterRequestDTO.getFilters().get("currentlyWorkwith"));
			Predicate currentlyWorkWithApprovedBy = cb.or(currentlyWorkwith, approvedBy);
			list.add(currentlyWorkWithApprovedBy);
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("modifiedBy"))
				&& !filterRequestDTO.getFilters().get("modifiedBy").toString().trim().isEmpty()) {

			String modifiedBy = (filterRequestDTO.getFilters().get("modifiedBy").toString());
			list.add(cb.equal(from.get("modifiedBy"), modifiedBy));
		}

		if ((Objects.nonNull(filterRequestDTO.getFilters().get("modifiedBy"))
				&& !filterRequestDTO.getFilters().get("modifiedBy").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
						&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty())) {
			Long createdby = Long.valueOf(filterRequestDTO.getFilters().get("createdBy").toString());

			list.add(cb.equal(from.get("createdBy"), createdby));
		}

		if ((Objects.nonNull(filterRequestDTO.getFilters().get("isCustomer"))
				&& !filterRequestDTO.getFilters().get("isCustomer").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("filterBy"))
						&& !filterRequestDTO.getFilters().get("filterBy").toString().trim().isEmpty())) {

			if (Boolean.parseBoolean(filterRequestDTO.getFilters().get("isCustomer").toString())) {
				list.add(cb.equal(from.get("createdBy"),
						Long.parseLong(filterRequestDTO.getFilters().get("filterBy").toString())));
			} else {
				list.add(cb.equal(from.get("currentlyWorkwith"),
						filterRequestDTO.getFilters().get("filterBy").toString()));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
				&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {
			ApprovalStatus status = null;
			if (filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.DRAFT.name())) {
				status = ApprovalStatus.DRAFT;
			} else if (filterRequestDTO.getFilters().get("status").toString()
					.equals(ApprovalStatus.INPROGRESS.name())) {
				status = ApprovalStatus.INPROGRESS;
			} else if (filterRequestDTO.getFilters().get("status").toString()
					.equals(ApprovalStatus.REQUESTFORCLARIFICATION.name())) {
				status = ApprovalStatus.REQUESTFORCLARIFICATION;
			} else if (filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.APPROVED.name())) {
				status = ApprovalStatus.APPROVED;
			}

			else if (filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.REJECT.name())) {
				status = ApprovalStatus.REJECT;
			}

			list.add(cb.equal(from.get("status"), status));
		}

	}

	@Override
	public GenericResponse updateStatus(Long id, ApprovalStatus status) {
		try {
			Optional<EalWastage> ealWastageOptional = repository.findById(id);
			if (!ealWastageOptional.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}
			EalWastage ealWastage = ealWastageOptional.get();
			if (status.equals(ealWastage.getStatus())) {
				return Library.getFailResponseCode(ErrorCode.NO_CHANGE_TO_UPDATE.getErrorCode(),
						ResponseMessageConstant.NO_CHANGE_TO_UPDATE.getMessage());
			}
			ealWastage.setStatus(status);
			repository.save(ealWastage);
			return Library.getSuccessfulResponse(commonUtil.modalMap(ealWastageOptional.get(), EalWastageDTO.class),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_UPDATED);
		} catch (Exception e) {
			log.error("error occurred while updating status for ealwastage record :: id :{}, exception {}", id, e);
			return Library.getFailResponseCode(ErrorCode.RECORD_UPDATION_FAILED.getErrorCode(),
					ResponseMessageConstant.RECORD_UPDATION_FAILED.getMessage());
		}
	}

	@Override
	public GenericResponse getInprogressList() {
		try {
			List<EalWastage> ealWastageOptional = repository.inprogressList();
			if (ealWastageOptional.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			return Library.getSuccessfulResponse(ealWastageOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} catch (Exception e) {
			log.error("error occurred while fetching x record :: id :{}, exception {}", e);
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
	}

	public GenericResponse getBottlePlanId(String bottelingPlanId) {
		List<EalWastage> ealLogEntity = repository.findByBottlingPlanIdStatusAndRequestStatus(bottelingPlanId);
		if (ealLogEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			if(!ealLogEntity.get(0).getStatus().equals(ApprovalStatus.APPROVED)) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						ErrorMessages.NO_RECORD_FOUND);
			}else {
			return Library.getSuccessfulResponse(ealLogEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
			}
		}
	}

}
