package com.oasys.posasset.service.impl;

import static com.oasys.posasset.constant.Constant.ASC;
import static com.oasys.posasset.constant.Constant.DESC;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.entity.EalWastage;
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.repository.EalWastageRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.dto.WorkFlowStatusUpdateDTO;
import com.oasys.posasset.entity.EALRequestAECEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestLogAECEntity;
import com.oasys.posasset.entity.EALRequestMapAECEntity;
import com.oasys.posasset.repository.EALRequestAECRepository;
import com.oasys.posasset.repository.EALRequestLogAECRepository;
import com.oasys.posasset.repository.EALRequestMapAECRepository;
import com.oasys.posasset.repository.TpRequestRepository;
import com.oasys.posasset.service.WorkFlowService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EALRequestAECService {

	@Autowired
	EALRequestAECRepository ealRequestAECRepository;

	@Autowired
	EALRequestMapAECRepository ealRequestMapAECRepository;

	@Autowired
	EALRequestLogAECRepository ealRequestLogAECRepository;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TpRequestRepository tprepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private EalWastageRepository ealWastageRepository;

	public GenericResponse save(EalPUtoBWFLRequestDTO requestDTO) {
		List<EALRequestAECEntity> finallist = new ArrayList<EALRequestAECEntity>();
		ArrayList<EALRequestMapAECEntity> finallist1 = new ArrayList<EALRequestMapAECEntity>();

		try {
			Optional<EalWastage> ealwastage =ealWastageRepository.getByAppliNo(requestDTO.getRequestedapplnNo());
			if(ealwastage.isPresent() && !ealwastage.get().getStatus().equals(ApprovalStatus.APPROVED)) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						"EAL Wastage Not Yet Approve For Given Bottling No");
			}
			Optional<EALRequestAECEntity> ealDetails = ealRequestAECRepository
					.findByLicenseNoAndRequestedapplnNo(requestDTO.getLicenseNo(), requestDTO.getRequestedapplnNo());
			if (!ealDetails.isPresent()) {
			// EALRequestEntity ealrequestEntity = commonUtil.modalMap(requestDTO,
			// EALRequestEntity.class);
			EALRequestAECEntity ealrequestEntity = new EALRequestAECEntity();
			EalPUtoBWFLRequestDTO obje = new EalPUtoBWFLRequestDTO();
			requestDTO.getEalmapRequestDTO().forEach(enti -> {
				obje.setPrintingType(enti.getPrintingType());
			});
			ealrequestEntity.setPrintingType(obje.getPrintingType());
			ealrequestEntity.setForceclosureFlag(false);
			ealrequestEntity.setCreatedBy(requestDTO.getCreatedBy());
			ealrequestEntity.setApplicationDate(requestDTO.getApplicationDate());
			ealrequestEntity.setRequestedapplnNo(requestDTO.getRequestedapplnNo());
			ealrequestEntity.setStatus(requestDTO.getStatus());
			ealrequestEntity.setEntityName(requestDTO.getEntityName());
			ealrequestEntity.setEntityAddress(requestDTO.getEntityAddress());
			ealrequestEntity.setLicenseType(requestDTO.getLicenseType());
			ealrequestEntity.setEntityType(requestDTO.getEntityType());
			ealrequestEntity.setLicenseNo(requestDTO.getLicenseNo());
			ealrequestEntity.setCodeType(requestDTO.getCodeType());
			ealrequestEntity.setTotBarcode(requestDTO.getTotBarcode());
			ealrequestEntity.setTotQrcode(requestDTO.getTotQrcode());
			ealrequestEntity.setDistrict(requestDTO.getDistrict());
			ealrequestEntity.setRemarks(requestDTO.getRemarks());
//				ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
			ealrequestEntity.setUnitCode(requestDTO.getUnitCode());
			ealrequestEntity.setDateOfPackaging(requestDTO.getDateOfPackaging());
			ealrequestEntity.setLiquorType(requestDTO.getLiquorType());
			ealrequestEntity.setLiquorSubType(requestDTO.getLiquorSubType());
			ealrequestEntity.setBrandName(requestDTO.getBrandName());
			ealrequestEntity.setRequestType(requestDTO.getRequestType());

			ealrequestEntity.setForceclosureFlag(false);
			// ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
//				ealrequestEntity.setVendorStatus(VendorStatus.INPROGRESS);
			ealrequestEntity = ealRequestAECRepository.save(ealrequestEntity);

			finallist.add(ealrequestEntity);
			Long ealrequstid = ealrequestEntity.getId();

			Optional<EALRequestAECEntity> DeptOptional = ealRequestAECRepository.findById(ealrequstid);

			requestDTO.getEalmapRequestDTO().forEach(action -> {
				EALRequestMapAECEntity ealrequestmap = new EALRequestMapAECEntity();
				ealrequestmap.setCartonSize(action.getCartonSize());
				ealrequestmap.setNoofBarcode(action.getNoofBarcode());
				ealrequestmap.setNoofQrcode(action.getNoofQrcode());
				ealrequestmap.setPackagingSize(action.getPackagingSize());
				ealrequestmap.setRemarks(action.getRemarks());
				ealrequestmap.setEalrequestId(DeptOptional.get());
				ealrequestmap.setUnmappedType(action.getUnmappedType());
				ealrequestmap.setLicenseNo(requestDTO.getLicenseNo());
				ealrequestmap.setCodeType(requestDTO.getCodeType());
				ealrequestmap.setPrintingType(action.getPrintingType());
				ealrequestmap.setNoofRoll(action.getNoofRoll());
				ealrequestmap.setCreatedBy(requestDTO.getCreatedBy());
				ealrequestmap.setMapType(action.getMapType());
				ealRequestMapAECRepository.save(ealrequestmap);
				finallist1.add(ealrequestmap);
			});

			finallist1.stream().forEach(ealrequestmap -> {
				EALRequestAECEntity ealrequestEntity1 = new EALRequestAECEntity();
				ealrequestEntity1.setPackagingSize(ealrequestmap.getPackagingSize());
				ealrequestEntity1.setCartonSize(ealrequestmap.getCartonSize());
				ealrequestEntity1.setNoofBarcode(ealrequestmap.getNoofBarcode());
				ealrequestEntity1.setNoofQrcode(ealrequestmap.getNoofQrcode());
				ealrequestEntity1.setPackagingSize(ealrequestmap.getPackagingSize());
				ealrequestEntity1.setRemarks(ealrequestmap.getRemarks());
				ealrequestEntity1.setEalrequestId(ealrequstid);
				ealrequestEntity1.setUnmappedType(ealrequestmap.getUnmappedType());
				ealrequestEntity1.setLicenseNo(ealrequestmap.getLicenseNo());
				ealrequestEntity1.setCodeType(ealrequestmap.getCodeType());
				ealrequestEntity1.setPrintingType(ealrequestmap.getPrintingType());
				ealrequestEntity1.setNoofRoll(ealrequestmap.getNoofRoll());
				finallist.add(ealrequestEntity1);
			});

			try {
				WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
				EALRequestLogAECEntity ealrequestlog = new EALRequestLogAECEntity();
				ealrequestlog.setComments("EAL saved successfully");
				ealrequestlog.setApplnNo(requestDTO.getRequestedapplnNo());
				ealrequestlog.setUserName(requestDTO.getUserName());
				ealrequestlog.setAction(requestDTO.getAction());
				ealrequestlog.setRemarks(requestDTO.getRemarks());
				ealRequestLogAECRepository.save(ealrequestlog);
				if (requestDTO.getEntityName().equalsIgnoreCase("BREWERY")
						|| requestDTO.getEntityName().equalsIgnoreCase("BOTTELING")
						|| requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")) {
					log.info(requestDTO.getEntityName());
					workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
//						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTDISTILLERY");
				}
//					if (requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")) {
//						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTDISTILLERY");
//					}
				else {
					workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
				}
				workflowStatusUpdateDto.setApplicationNumber(requestDTO.getRequestedapplnNo());
				workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
				// workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
				workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
				workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
				workFlowService.callEALAECWorkFlowService(workflowStatusUpdateDto);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("EAL Log " + e);
			}

			return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			} 
			else {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Previous EAL Request of the Bottling Plan Not Yet Approved");
			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	public GenericResponse update(EalPUtoBWFLRequestDTO requestDTO) {
		try {
			Optional<EALRequestAECEntity> ealDetails = ealRequestAECRepository.findById(requestDTO.getId());
			if (!ealDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {
				EALRequestAECEntity ealrq = ealDetails.get();
				ealrq.setStatus(ApprovalStatus.INPROGRESS);
//				ealrq.setTotBarcode(requestDTO.getTotBarcode());
//				ealrq.setTotQrcode(requestDTO.getTotQrcode());
				ealRequestAECRepository.save(ealrq);
				// Optional<EALRequestMapEntity> DeptOptional =
				// ealrequestmapRepository.findById(ealrequstid);

				requestDTO.getEalmapRequestDTO().forEach(action -> {
					Optional<EALRequestMapAECEntity> DeptOptional = ealRequestMapAECRepository.findById(action.getId());
					EALRequestMapAECEntity ealrequestmap = DeptOptional.get();
					ealrequestmap.setCartonSize(action.getCartonSize());
					ealrequestmap.setNoofBarcode(action.getNoofBarcode());
					ealrequestmap.setNoofQrcode(action.getNoofQrcode());
					ealrequestmap.setNoofRoll(action.getNoofRoll());
					ealrequestmap.setPackagingSize(action.getPackagingSize());
					ealrequestmap.setRemarks(action.getRemarks());
					ealrequestmap.setEalrequestId(DeptOptional.get().getEalrequestId());
					ealrequestmap.setUnmappedType(DeptOptional.get().getUnmappedType());
					ealrequestmap.setLicenseNo(DeptOptional.get().getLicenseNo());
					ealrequestmap.setCodeType(DeptOptional.get().getCodeType());
					ealRequestMapAECRepository.save(ealrequestmap);

				});

				try {
					WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
					workflowStatusUpdateDto.setApplicationNumber(requestDTO.getRequestedapplnNo());
					if (requestDTO.getEntityName().equalsIgnoreCase("BREWERY")
							|| requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")
							|| requestDTO.getEntityName().equalsIgnoreCase("BOTTELING")) {
						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");

					} else {
						workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
					}

					workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
					// workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
					workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
					workflowStatusUpdateDto.setLevel("Level 1");
					workFlowService.callEALAECWorkFlowService(workflowStatusUpdateDto);
					EALRequestLogAECEntity ealrequestlog = new EALRequestLogAECEntity();
					ealrequestlog.setComments(requestDTO.getRemarks());
					ealrequestlog.setRemarks(requestDTO.getRemarks());
					ealrequestlog.setApplnNo(requestDTO.getRequestedapplnNo());
					ealrequestlog.setUserName(requestDTO.getUserName());
					ealrequestlog.setAction(requestDTO.getAction());
					ealRequestLogAECRepository.save(ealrequestlog);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return Library.getSuccessfulResponse(ealrq, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_UPDATED);
			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	public GenericResponse getById(Long id) {
		List<EALRequestAECEntity> ealrequstlist = new ArrayList<EALRequestAECEntity>();

		List<EALRequestAECEntity> ealrequestEntity = ealRequestAECRepository.getById(id);

		ealrequstlist.addAll(ealrequestEntity);

		if (!ealrequestEntity.isEmpty()) {

//			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			List<EALRequestMapAECEntity> DeptOptional = ealRequestMapAECRepository.getById(id);

			EALRequestAECEntity ealrequestEntity1 = new EALRequestAECEntity();
			Long ealrequestid;
			DeptOptional.stream().forEach(action -> {
//				ealrequestEntity1.setPackagingSize(action.getPackagingSize());
//				ealrequestEntity1.setCartonSize(action.getCartonSize());
//				ealrequestEntity1.setNoofBarcode(action.getNoofBarcode());
//				ealrequestEntity1.setNoofQrcode(action.getNoofQrcode());
//				ealrequestEntity1.setPackagingSize(action.getPackagingSize());
//				ealrequestEntity1.setRemarks(action.getRemarks());
				ealrequestEntity1.setEalrequestId(action.getEalrequestId().getId());
//				ealrequestEntity1.setUnmappedType(action.getUnmappedType());
				ealrequestEntity1.setId(action.getId());
				ealrequstlist.add(ealrequestEntity1);
			});

			return Library.getSuccessfulResponse(ealrequstlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

		else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}

	}

	public GenericResponse getByealid(Long id) {

		List<EALRequestMapAECEntity> DeptOptional = ealRequestMapAECRepository.getById(id);

//		Optional<EALRequestMapAECEntity> DeptOptional1 = ealRequestMapAECRepository.findById(id);
//		List<BarQrBalanceDTO> tpdispatch = tprepository.getByEalreqid(id);

		Optional<EALRequestAECEntity> ealrequestEntity = ealRequestAECRepository.findById(id);

		Integer totabarcode = ealrequestEntity.get().getTotBarcode();

		Integer totalqrcode = ealrequestEntity.get().getTotQrcode();

//		Integer totabarcode = null;
//		Integer totalqrcode = null;
//
//		if (ealrequestEntity.isPresent()) {
//			EALRequestMapAECEntity entity = DeptOptional1.get();
//
//			if (entity.getNoofBarcode() != null) {
//				totabarcode = Integer.parseInt(entity.getNoofBarcode());
//			}
//
//			if (entity.getNoofQrcode() != null) {
//				totalqrcode = Integer.parseInt(entity.getNoofQrcode());
//			}
//		}
//
//		final Integer finalTotabarcode = totabarcode;
//		final Integer finalTotalqrcode = totalqrcode;

		String stockappln = ealrequestEntity.get().getRequestedapplnNo();

		List<BarQrBalanceDTO> balancedetails = ealRequestMapAECRepository.getByStockApplnno(stockappln);

		List<EalRequestMapResponseDTO> stockdetails = new ArrayList<EalRequestMapResponseDTO>();
		DeptOptional.parallelStream().forEach(maplist -> {
			EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
			obj.setId(maplist.getId());
			obj.setCreatedDate(String.valueOf(maplist.getCreatedDate()));
			obj.setCartonSize(maplist.getCartonSize());
			obj.setPackagingSize(maplist.getPackagingSize());
			obj.setNoofBarcode(maplist.getNoofBarcode());
			obj.setNoofQrcode(maplist.getNoofQrcode());
			obj.setRemarks(maplist.getRemarks());
			obj.setUnmappedType(maplist.getUnmappedType());
			obj.setCodeType(maplist.getCodeType());
			obj.setNoofBarcodepending(maplist.getNoofBarcodepending());
			obj.setNoofBarcodereceived(maplist.getNoofBarcodereceived());
			obj.setNoofQrcodereceived(maplist.getNoofQrcodereceived());
			obj.setNoofRollcodereceived(maplist.getNoofRollcodereceived());
			obj.setNoofqrpending(maplist.getNoofqrpending());
			obj.setStockApplnno(maplist.getStockApplnno());
			obj.setStockDate(maplist.getStockDate());
			obj.setLicencenumber(maplist.getLicenseNo());
			obj.setFlag(maplist.isFlag());
			obj.setEalrequestId(maplist.getEalrequestId().getId());
			obj.setTotalnumofBarcode(totabarcode);
			obj.setTotalnumofQrcode(totalqrcode);
			obj.setPrintingType(maplist.getPrintingType());
			obj.setNoofRoll(maplist.getNoofRoll());
			obj.setMapType(maplist.getMapType());

			balancedetails.stream().forEach(stockbalance -> {
				EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
				receivedetails.setPackagingSize(stockbalance.getPackagingSize());
				receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
				receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
//				if (obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
//					obj.setNoofBarcodeBalance(totabarcode - (receivedetails.getNoofBarcodereceived()));
//					obj.setNoofQrcodeBalance(totalqrcode - (receivedetails.getNoofQrcodereceived()));
//
//				}

			});

//			tpdispatch.stream().forEach(dispatch -> {
//				if (obj.getPackagingSize().equalsIgnoreCase(dispatch.getPackagingSize())) {
//					obj.setDispatchnoofBarcodereceived(dispatch.getDispatchNoofBarcodereceived());
//					obj.setDispatchnoofQrcodereceived(dispatch.getDispatchNoofQrcodereceived());
//					obj.setDispatchnoofRollcodereceived(dispatch.getDispatchNoofRollcodereceived());
//				}
//			});

			stockdetails.add(obj);

		});
//			List<EalRequestMapResponseDTO> unique = stockdetails.stream()
//	              .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(EalRequestMapResponseDTO::getTotalnumofBarcode))),
//	                                         ArrayList::new));
//			
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(stockdetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAll() {
		List<EALRequestAECEntity> DepList = ealRequestAECRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALRequestAECEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<EALRequestAECEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<EALRequestAECEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestAECEntity> cq = cb.createQuery(EALRequestAECEntity.class);
		Root<EALRequestAECEntity> from = cq.from(EALRequestAECEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestAECEntity> typedQuery = null;
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

	public List<EALRequestAECEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestAECEntity> cq = cb.createQuery(EALRequestAECEntity.class);
		Root<EALRequestAECEntity> from = cq.from(EALRequestAECEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestAECEntity> typedQuery1 = null;
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
			Root<EALRequestAECEntity> from) {

		Date fromDate = null;

		Date toDate = null;


	    // Parse fromDate
	    if (Objects.nonNull(filterRequestDTO.getFilters().get("fromDate")) &&
	        !filterRequestDTO.getFilters().get("fromDate").toString().trim().isEmpty()) {
	        try {
	            String fdate = filterRequestDTO.getFilters().get("fromDate").toString();
	            fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " 00:00:00");
	        } catch (ParseException e) {
	            log.error("error occurred while parsing date : {}", e.getMessage());
	            throw new InvalidDataValidation("Invalid date parameter passed");
	        }
	    }

	    // Parse toDate
	    if (Objects.nonNull(filterRequestDTO.getFilters().get("toDate")) &&
	        !filterRequestDTO.getFilters().get("toDate").toString().trim().isEmpty()) {
	        try {
	            String tdate = filterRequestDTO.getFilters().get("toDate").toString();
	            toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " 23:59:59");
	        } catch (ParseException e) {
	            log.error("error occurred while parsing date : {}", e.getMessage());
	            throw new InvalidDataValidation("Invalid date parameter passed");
	        }
	    }

	    // Add date range predicate
	    if (fromDate != null && toDate != null) {
	        list.add(cb.between(from.get("createdDate"), fromDate, toDate));
	    }

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseNo"))
				&& !filterRequestDTO.getFilters().get("licenseNo").toString().trim().isEmpty()) {
			List<String> licenseNo = (List<String>) (filterRequestDTO.getFilters().get("licenseNo"));
			if (!licenseNo.isEmpty()) {
				Expression<String> mainModule = from.get("licenseNo");
				list.add(mainModule.in(licenseNo));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("puLicenseNo"))
				&& !filterRequestDTO.getFilters().get("puLicenseNo").toString().trim().isEmpty()) {
			List<String> puLicenseNo = (List<String>) (filterRequestDTO.getFilters().get("puLicenseNo"));
			if (!puLicenseNo.isEmpty()) {
				Expression<String> mainModule = from.get("puLicenseNo");
				list.add(mainModule.in(puLicenseNo));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("unitCode"))
				&& !filterRequestDTO.getFilters().get("unitCode").toString().trim().isEmpty()) {
			List<String> unitCode = (List<String>) (filterRequestDTO.getFilters().get("unitCode"));
			if (!unitCode.isEmpty()) {
				Expression<String> mainModule = from.get("unitCode");
				list.add(mainModule.in(unitCode));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("codeType"))
				&& !filterRequestDTO.getFilters().get("codeType").toString().trim().isEmpty()) {

			String codetype = filterRequestDTO.getFilters().get("codeType").toString();
			list.add(cb.equal(from.get("codeType"), codetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("dateOfPackaging"))
				&& !filterRequestDTO.getFilters().get("dateOfPackaging").toString().trim().isEmpty()) {

			String dateOfPackaging = filterRequestDTO.getFilters().get("dateOfPackaging").toString();
			list.add(cb.equal(from.get("dateOfPackaging"), dateOfPackaging));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("liquorType"))
				&& !filterRequestDTO.getFilters().get("liquorType").toString().trim().isEmpty()) {

			String liquorType = filterRequestDTO.getFilters().get("liquorType").toString();
			list.add(cb.equal(from.get("liquorType"), liquorType));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("liquorSubType"))
				&& !filterRequestDTO.getFilters().get("liquorSubType").toString().trim().isEmpty()) {

			String liquorSubType = filterRequestDTO.getFilters().get("liquorSubType").toString();
			list.add(cb.equal(from.get("liquorSubType"), liquorSubType));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("brandName"))
				&& !filterRequestDTO.getFilters().get("brandName").toString().trim().isEmpty()) {

			String brandName = filterRequestDTO.getFilters().get("brandName").toString();
			list.add(cb.equal(from.get("brandName"), brandName));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("requestType"))
				&& !filterRequestDTO.getFilters().get("requestType").toString().trim().isEmpty()) {

			String requestType = filterRequestDTO.getFilters().get("requestType").toString();
			list.add(cb.equal(from.get("requestType"), requestType));
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

		if (Objects.nonNull(filterRequestDTO.getFilters().get("applicationNo"))
				&& !filterRequestDTO.getFilters().get("applicationNo").toString().trim().isEmpty()) {

			String requestedapplnNo = (filterRequestDTO.getFilters().get("applicationNo").toString());
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
//		if (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
//				&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty()) {
//
//			String createdBy = (filterRequestDTO.getFilters().get("createdBy").toString());
//			list.add(cb.equal(from.get("createdBy"), createdBy));
//		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy")) &&
		        !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty()) {
		        String createdBy = filterRequestDTO.getFilters().get("createdBy").toString();
		       
		        list.add(cb.and(
		            cb.equal(from.get("createdBy"), createdBy),
		            cb.isNotNull(from.get("createdBy"))
		        ));
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

	public GenericResponse getAllByUserId(Long userId) {
		List<EALRequestAECEntity> deviceReturnEntityList = ealRequestAECRepository.findByCreatedByOrderByIdDesc(userId);
		if (deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(deviceReturnEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getLogByApplicationNo(String applicationNo) {
		List<EALRequestLogAECEntity> ealLogEntity = ealRequestLogAECRepository
				.findByApplnNoOrderByIdDesc(applicationNo);
		if (ealLogEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealLogEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

//	public GenericResponse getmaindashboard(EalPUtoBWFLRequestDTO requestDTO) {
//		final Date fromDate;
//		final Date toDate;
//		try {
//			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getFromDate() + " " + "00:00:00");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}
//		try {
//			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getToDate() + " " + "23:59:59");
//		} catch (ParseException e) {
//			log.error("error occurred while parsing date : {}", e.getMessage());
//			throw new InvalidDataValidation("Invalid date parameter passed");
//		}
//		List<EALDashboard> ticketEntityList = null;
//
////		List<String> licArray;
////		if (requestDTO.getLicenseNumberArray().isEmpty()) {
////			licArray = null;
////		} else {
////			licArray = requestDTO.getLicenseNumberArray();
////		}
//
//		List<String> unitCodeArray;
//		if (requestDTO.getUnitCodeArray().isEmpty()) {
//			unitCodeArray = null;
//		} else {
//			unitCodeArray = requestDTO.getUnitCodeArray();
//		}
//
//		String district;
//		if (requestDTO.getDistrict().isEmpty()) {
//			district = null;
//		} else {
//			district = requestDTO.getDistrict();
//		}
//
////		if (licArray == null) {
////			ticketEntityList = ealrequestRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApproved(
////					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district);
////		} else {
////			ticketEntityList = ealrequestRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedBy(
////					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district,
////					licArray);
////		}
//
//		if (unitCodeArray == null) {
//			ticketEntityList = ealRequestPUtoBWFLRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApproved(
//					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district);
//		} else {
//			ticketEntityList = ealRequestPUtoBWFLRepository
//					.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedByAndUnitcodeArray(fromDate, toDate,
//							requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district,
//							unitCodeArray);
//		}
//
//		if (CollectionUtils.isEmpty(ticketEntityList)
//				|| ticketEntityList.stream().allMatch(ticket -> ticket.getTotalealrequested() == 0)) {
//			throw new RecordNotFoundException("No Record Found");
//		}
//
//		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}

	public GenericResponse updateApproval(ApprovalDTO approvalDto) {
		GenericResponse resp = null;
		Optional<EALRequestAECEntity> ealEntity = ealRequestAECRepository.findById(approvalDto.getId());
		if (ealEntity.isPresent()) {
			EALRequestAECEntity eal = ealEntity.get();
			eal.setStatus(approvalDto.getStatus());
			eal.setModifiedBy(approvalDto.getUserId());
			eal.setModifiedDate(new Date());
			eal.setRemarks(approvalDto.getRemarks());
			eal.setApprovedBy(ealEntity.get().getCurrentlyWorkwith());
			ealRequestAECRepository.save(eal);

			EALRequestLogAECEntity ealrequestlog = new EALRequestLogAECEntity();
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();

			Optional<EalWastage> ealDetailsList = ealWastageRepository.getByApplicationNo(approvalDto.getApplnNo());

			if (ealDetailsList.isPresent()) {
				EalWastage ealWastageEntity = ealDetailsList.get();
				if (ApprovalStatus.APPROVED.equals(approvalDto.getStatus())) {
					ealWastageEntity.setRequestStatus(1);
					ealWastageRepository.save(ealWastageEntity);
				}
//				else if (ApprovalStatus.REJECT.equals(approvalDto.getStatus())) {
//					ealWastageEntity.setRequestStatus(2);
//					ealWastageRepository.save(ealWastageEntity);
//				}
			}

			if (ApprovalStatus.INPROGRESS.equals(approvalDto.getStatus())) {

				ealrequestlog.setAction(approvalDto.getAction());
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setComments("Application forwarded successfully");
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.FORWARDED);

			}

			else if (ApprovalStatus.REJECT.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application rejected successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				// eal.setVendorStatus(null);
				ealRequestAECRepository.save(eal);
				ealRequestLogAECRepository.save(ealrequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.REJECTED);

			} else if (ApprovalStatus.APPROVED.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application approved successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				// eal.setVendorStatus(VendorStatus.REQUESTED);
				ealRequestAECRepository.save(eal);
				ealRequestLogAECRepository.save(ealrequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.APPROVED);

			} else if (ApprovalStatus.REQUESTFORCLARIFICATION.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application requestforclarification successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				ealRequestLogAECRepository.save(ealrequestlog);
				// workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.CLARIFICATION_REQUEST_SENT_SUCCESSFULLY);
				workflowStatusUpdateDto.setSendBackTo("Level 1");
			}

			if (approvalDto.getEntityName().equalsIgnoreCase("BREWERY")
					|| approvalDto.getEntityName().equalsIgnoreCase("DISTILLERY")
					|| approvalDto.getEntityName().equalsIgnoreCase("BOTTELING")) {
				workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");

			} else {
				workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
			}

			workflowStatusUpdateDto.setApplicationNumber(ealEntity.get().getRequestedapplnNo());
			workflowStatusUpdateDto.setModuleNameCode(approvalDto.getModuleNameCode());
			// workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
			workflowStatusUpdateDto.setLevel(approvalDto.getLevel());
			workFlowService.callEALWorkFlowService(workflowStatusUpdateDto);
			return resp;
		} else {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

	}

	public GenericResponse forceclosureupdate(EalPUtoBWFLRequestDTO requestDTO) {
		try {
			ealRequestAECRepository.updateForceclosure(requestDTO.isForceclosureFlag(),
					requestDTO.getRequestedapplnNo());
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
		return Library.getSuccessfulResponse("", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				"Force Closure Updated Successfully");
	}

	public GenericResponse getAllByapproved() {
		List<EALRequestAECEntity> ealEntityList = ealRequestAECRepository.findByStatusOrderByIdDesc();
		if (ealEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getByAppliNo(String appliNo) {
		List<EALRequestAECEntity> ealRequestList = new ArrayList<>();

		List<EALRequestAECEntity> ealRequestEntities = ealRequestAECRepository.getByRequestedapplnNo(appliNo);

		ealRequestList.addAll(ealRequestEntities);

		if (!ealRequestEntities.isEmpty()) {
			if(ealRequestEntities.get(0).getStatus().equals(ApprovalStatus.INPROGRESS)) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), 
						"Previous EAL Request of the Bottling Plan Not Yet Approved");
			}
			for (EALRequestAECEntity entity : ealRequestEntities) {
				List<EALRequestMapAECEntity> deptOptional = ealRequestMapAECRepository.getById(entity.getId());

				for (EALRequestMapAECEntity action : deptOptional) {
					EALRequestAECEntity ealRequestEntity = new EALRequestAECEntity();
//	                ealRequestEntity.setEalrequestId(action.getEalrequestId().getId());
					ealRequestEntity.setId(action.getId());
					ealRequestEntity.setPackagingSize(action.getPackagingSize());
					ealRequestEntity.setCartonSize(action.getCartonSize());
					ealRequestEntity.setNoofBarcode(action.getNoofBarcode());
					ealRequestEntity.setNoofQrcode(action.getNoofQrcode());
					ealRequestEntity.setRemarks(action.getRemarks());
//	                ealRequestEntity.setEalrequestId(action.getEalrequestId());
					ealRequestEntity.setModifiedBy(action.getModifiedBy());
					ealRequestEntity.setModifiedDate(action.getModifiedDate());
					ealRequestEntity.setUnmappedType(action.getUnmappedType());
					ealRequestEntity.setCodeType(action.getCodeType());
					ealRequestEntity.setNoofBarcodepending(action.getNoofBarcodepending());
					ealRequestEntity.setNoofBarcodereceived(action.getNoofBarcodereceived());
					ealRequestEntity.setNoofQrcodereceived(action.getNoofQrcodereceived());
					ealRequestEntity.setNoofRollcodereceived(action.getNoofRollcodereceived());
					ealRequestEntity.setNoofqrpending(action.getNoofqrpending());
					ealRequestEntity.setStockApplnno(action.getStockApplnno());
					ealRequestEntity.setStockDate(action.getStockDate());
					ealRequestEntity.setTotalnumofBarcode(action.getTotalnumofBarcode());
					ealRequestEntity.setTotalnumofQrcode(action.getTotalnumofQrcode());
					ealRequestEntity.setTotalnumofRoll(action.getTotalnumofRoll());
//	                ealRequestEntity.setForceclosureFlag(action.isForceclosureFlag());  // Assuming forceclosureFlag maps to `flag`
					ealRequestEntity.setOpeningStock(action.getOpenstock());
					ealRequestEntity.setLicenseNo(action.getLicenseNo()); // Assuming licenece_number maps to licenseNo
					ealRequestEntity.setPrintingType(action.getPrintingType());
					ealRequestEntity.setNoofRoll(action.getNoofRoll());
					ealRequestEntity.setMapType(action.getMapType());

					ealRequestList.add(ealRequestEntity);
				}
			}

			return Library.getSuccessfulResponse(ealRequestList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}

}