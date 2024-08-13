package com.oasys.posasset.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.repository.EALDashboard;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalPUtoBWFLResponseDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestLogEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLLogEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLMapEntity;
import com.oasys.posasset.repository.EALRequestPUtoBWFLLogRepository;
import com.oasys.posasset.repository.EALRequestPUtoBWFLMapRepository;
import com.oasys.posasset.repository.EALRequestPUtoBWFLRepository;
import com.oasys.posasset.repository.TpRequestRepository;
import com.oasys.posasset.service.WorkFlowService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EALRequestPUtoBWFLService {
	
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	EALRequestPUtoBWFLRepository ealRequestPUtoBWFLRepository;
	
	@Autowired
	EALRequestPUtoBWFLMapRepository ealRequestPUtoBWFLMapRepository;
	
	@Autowired
	EALRequestPUtoBWFLLogRepository ealRequestPUtoBWFLLogRepository;
	
	@Autowired
	TpRequestRepository tprepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	EALRequestPUtoBWFLLogRepository ealPUtoBWFLLogRepository;
	
	public GenericResponse save(EalPUtoBWFLRequestDTO requestDTO) {
		List<EALRequestPUtoBWFLEntity> finallist = new ArrayList<EALRequestPUtoBWFLEntity>();
		ArrayList<EALRequestPUtoBWFLMapEntity> finallist1 = new ArrayList<EALRequestPUtoBWFLMapEntity>();

		try {
			Optional<EALRequestPUtoBWFLEntity> ealDetails = ealRequestPUtoBWFLRepository
					.findByLicenseNoAndRequestedapplnNo(requestDTO.getLicenseNo(), requestDTO.getRequestedapplnNo());
			if (!ealDetails.isPresent()) {
				// EALRequestEntity ealrequestEntity = commonUtil.modalMap(requestDTO,
				// EALRequestEntity.class);
				EALRequestPUtoBWFLEntity ealrequestEntity = new EALRequestPUtoBWFLEntity();
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
				ealrequestEntity.setPuEntityName(requestDTO.getPuEntityName());
				ealrequestEntity.setPuEntityAddress(requestDTO.getPuEntityAddress());
				ealrequestEntity.setPuLicenseType(requestDTO.getPuLicenseType());
				ealrequestEntity.setPuLicenseNo(requestDTO.getPuLicenseNo());			
				ealrequestEntity.setTotBarcode(requestDTO.getTotBarcode());
				ealrequestEntity.setTotQrcode(requestDTO.getTotQrcode());
				ealrequestEntity.setDistrict(requestDTO.getDistrict());
				ealrequestEntity.setRemarks(requestDTO.getRemarks());
//				ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
				ealrequestEntity.setUnitCode(requestDTO.getUnitCode());
				ealrequestEntity.setForceclosureFlag(false);
				// ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
//				ealrequestEntity.setVendorStatus(VendorStatus.INPROGRESS);
				ealrequestEntity = ealRequestPUtoBWFLRepository.save(ealrequestEntity);

				finallist.add(ealrequestEntity);
				Long ealrequstid = ealrequestEntity.getId();

				Optional<EALRequestPUtoBWFLEntity> DeptOptional = ealRequestPUtoBWFLRepository.findById(ealrequstid);

				requestDTO.getEalmapRequestDTO().forEach(action -> {
					EALRequestPUtoBWFLMapEntity ealrequestmap = new EALRequestPUtoBWFLMapEntity();
					ealrequestmap.setCartonSize(action.getCartonSize());
					ealrequestmap.setNoofBarcode(action.getNoofBarcode());
					ealrequestmap.setNoofQrcode(action.getNoofQrcode());
					ealrequestmap.setPackagingSize(action.getPackagingSize());
					ealrequestmap.setRemarks(action.getRemarks());
					ealrequestmap.setEalrequestPUtoBWFLId(DeptOptional.get());
					ealrequestmap.setUnmappedType(action.getUnmappedType());
					ealrequestmap.setLicenseNo(requestDTO.getLicenseNo());
					ealrequestmap.setCodeType(requestDTO.getCodeType());
					ealrequestmap.setPrintingType(action.getPrintingType());
					ealrequestmap.setNoofRoll(action.getNoofRoll());
					ealrequestmap.setCreatedBy(requestDTO.getCreatedBy());
					ealrequestmap.setMapType(action.getMapType());
					ealRequestPUtoBWFLMapRepository.save(ealrequestmap);
					finallist1.add(ealrequestmap);
				});

				finallist1.stream().forEach(ealrequestmap -> {
					EALRequestPUtoBWFLEntity ealrequestEntity1 = new EALRequestPUtoBWFLEntity();
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
//					WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
					EALRequestPUtoBWFLLogEntity ealrequestlog = new EALRequestPUtoBWFLLogEntity();
					ealrequestlog.setComments("EAL saved successfully");
					ealrequestlog.setApplnNo(requestDTO.getRequestedapplnNo());
					ealrequestlog.setUserName(requestDTO.getUserName());
					ealrequestlog.setAction(requestDTO.getAction());
					ealrequestlog.setRemarks(requestDTO.getRemarks());
					ealRequestPUtoBWFLLogRepository.save(ealrequestlog);
//					if (requestDTO.getEntityName().equalsIgnoreCase("BREWERY")
//							|| requestDTO.getEntityName().equalsIgnoreCase("BOTTELING")) {
//						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
//					}
//					if (requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")) {
//						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTDISTILLERY");
//					} else {
//						workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
//					}
//					workflowStatusUpdateDto.setApplicationNumber(requestDTO.getRequestedapplnNo());
//					workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
//					// workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
//					workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
//					workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
//					workFlowService.callEALWorkFlowService(workflowStatusUpdateDto);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("EAL Log " + e);
				}

				return Library.getSuccessfulResponse(finallist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_CREATED);
			} else {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record already exist");
			}
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}
	
	
	public GenericResponse update(EalPUtoBWFLRequestDTO requestDTO) {
		try {
			Optional<EALRequestPUtoBWFLEntity> ealDetails = ealRequestPUtoBWFLRepository.findById(requestDTO.getId());
			if (!ealDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {
				EALRequestPUtoBWFLEntity ealrq = ealDetails.get();
				ealrq.setStatus(ApprovalStatus.INPROGRESS);
//				ealrq.setTotBarcode(requestDTO.getTotBarcode());
//				ealrq.setTotQrcode(requestDTO.getTotQrcode());
				ealRequestPUtoBWFLRepository.save(ealrq);
				// Optional<EALRequestMapEntity> DeptOptional =
				// ealrequestmapRepository.findById(ealrequstid);

				requestDTO.getEalmapRequestDTO().forEach(action -> {
					Optional<EALRequestPUtoBWFLMapEntity> DeptOptional = ealRequestPUtoBWFLMapRepository.findById(action.getId());
					EALRequestPUtoBWFLMapEntity ealrequestmap = DeptOptional.get();
					ealrequestmap.setCartonSize(action.getCartonSize());
					ealrequestmap.setNoofBarcode(action.getNoofBarcode());
					ealrequestmap.setNoofQrcode(action.getNoofQrcode());
					ealrequestmap.setNoofRoll(action.getNoofRoll());
					ealrequestmap.setPackagingSize(action.getPackagingSize());
					ealrequestmap.setRemarks(action.getRemarks());
					ealrequestmap.setEalrequestPUtoBWFLId(DeptOptional.get().getEalrequestPUtoBWFLId());
					ealrequestmap.setUnmappedType(DeptOptional.get().getUnmappedType());
					ealrequestmap.setLicenseNo(DeptOptional.get().getLicenseNo());
					ealrequestmap.setCodeType(DeptOptional.get().getCodeType());
					ealRequestPUtoBWFLMapRepository.save(ealrequestmap);

				});

				try {
//					WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();
//					workflowStatusUpdateDto.setApplicationNumber(requestDTO.getRequestedapplnNo());
//					if (requestDTO.getEntityName().equalsIgnoreCase("BREWERY")
//							|| requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")
//							|| requestDTO.getEntityName().equalsIgnoreCase("BOTTELING")) {
//						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
//
//					} else {
//						workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
//					}
//
//					workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
//					// workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
//					workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
//					workflowStatusUpdateDto.setLevel("Level 1");
//					workFlowService.callEALWorkFlowService(workflowStatusUpdateDto);
					EALRequestPUtoBWFLLogEntity ealrequestlog = new EALRequestPUtoBWFLLogEntity();
					ealrequestlog.setComments(requestDTO.getRemarks());
					ealrequestlog.setRemarks(requestDTO.getRemarks());
					ealrequestlog.setApplnNo(requestDTO.getRequestedapplnNo());
					ealrequestlog.setUserName(requestDTO.getUserName());
					ealrequestlog.setAction(requestDTO.getAction());
					ealRequestPUtoBWFLLogRepository.save(ealrequestlog);
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
		List<EALRequestPUtoBWFLEntity> ealrequstlist = new ArrayList<EALRequestPUtoBWFLEntity>();

		List<EALRequestPUtoBWFLEntity> ealrequestEntity = ealRequestPUtoBWFLRepository.getById(id);

		ealrequstlist.addAll(ealrequestEntity);

		if (!ealrequestEntity.isEmpty()) {

//			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			List<EALRequestPUtoBWFLMapEntity> DeptOptional = ealRequestPUtoBWFLMapRepository.getById(id);

			EALRequestPUtoBWFLEntity ealrequestEntity1 = new EALRequestPUtoBWFLEntity();
			Long ealrequestid;
			DeptOptional.stream().forEach(action -> {
//				ealrequestEntity1.setPackagingSize(action.getPackagingSize());
//				ealrequestEntity1.setCartonSize(action.getCartonSize());
//				ealrequestEntity1.setNoofBarcode(action.getNoofBarcode());
//				ealrequestEntity1.setNoofQrcode(action.getNoofQrcode());
//				ealrequestEntity1.setPackagingSize(action.getPackagingSize());
//				ealrequestEntity1.setRemarks(action.getRemarks());
				ealrequestEntity1.setEalrequestId(action.getEalrequestPUtoBWFLId().getId());
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

		List<EALRequestPUtoBWFLMapEntity> DeptOptional = ealRequestPUtoBWFLMapRepository.getById(id);
		
//		Optional<EALRequestPUtoBWFLMapEntity> DeptOptional1 = ealRequestPUtoBWFLMapRepository.findById(id);
//		List<BarQrBalanceDTO> tpdispatch = tprepository.getByEalreqid(id);

		Optional<EALRequestPUtoBWFLEntity> ealrequestEntity = ealRequestPUtoBWFLRepository.findById(id);

		Integer totabarcode = ealrequestEntity.get().getTotBarcode();

		Integer totalqrcode = ealrequestEntity.get().getTotQrcode();
		
		

//		Integer totabarcode = null;
//		Integer totalqrcode = null;
//
//		if (ealrequestEntity.isPresent()) {
//			EALRequestPUtoBWFLMapEntity entity = DeptOptional1.get();
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

		List<BarQrBalanceDTO> balancedetails = ealRequestPUtoBWFLMapRepository.getByStockApplnno(stockappln);

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
			obj.setEalrequestId(maplist.getEalrequestPUtoBWFLId().getId());
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
		List<EALRequestPUtoBWFLEntity> DepList = ealRequestPUtoBWFLRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	
	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALRequestPUtoBWFLEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<EALRequestPUtoBWFLEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<EALRequestPUtoBWFLEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestPUtoBWFLEntity> cq = cb.createQuery(EALRequestPUtoBWFLEntity.class);
		Root<EALRequestPUtoBWFLEntity> from = cq.from(EALRequestPUtoBWFLEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestPUtoBWFLEntity> typedQuery = null;
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

	public List<EALRequestPUtoBWFLEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestPUtoBWFLEntity> cq = cb.createQuery(EALRequestPUtoBWFLEntity.class);
		Root<EALRequestPUtoBWFLEntity> from = cq.from(EALRequestPUtoBWFLEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestPUtoBWFLEntity> typedQuery1 = null;
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
			Root<EALRequestPUtoBWFLEntity> from) {

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

//		if ((Objects.nonNull(filterRequestDTO.getFilters().get("modifiedBy"))
//				&& !filterRequestDTO.getFilters().get("modifiedBy").toString().trim().isEmpty())
//				&& (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
//						&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty())) {
//			Long createdby=Long.valueOf(filterRequestDTO.getFilters().get("createdBy").toString());
//			
//			list.add(cb.equal(from.get("createdBy"), createdby));	
//		}

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
		List<EALRequestPUtoBWFLEntity> deviceReturnEntityList = ealRequestPUtoBWFLRepository.findByCreatedByOrderByIdDesc(userId);
		if (deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(deviceReturnEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	public GenericResponse getLogByApplicationNo(String applicationNo) {
		List<EALRequestPUtoBWFLLogEntity> ealLogEntity = ealRequestPUtoBWFLLogRepository.findByApplnNoOrderByIdDesc(applicationNo);
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
		Optional<EALRequestPUtoBWFLEntity> ealEntity = ealRequestPUtoBWFLRepository.findById(approvalDto.getId());
		if (ealEntity.isPresent()) {
			EALRequestPUtoBWFLEntity eal = ealEntity.get();
			eal.setStatus(approvalDto.getStatus());
			eal.setModifiedBy(approvalDto.getUserId());
			eal.setModifiedDate(new Date());
			eal.setRemarks(approvalDto.getRemarks());
			//eal.setApprovedBy(ealEntity.get().getCurrentlyWorkwith());
			ealRequestPUtoBWFLRepository.save(eal);
			
			EALRequestPUtoBWFLLogEntity ealrequestlog = new EALRequestPUtoBWFLLogEntity();
			//WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();

			if ( ApprovalStatus.INPROGRESS.equals(approvalDto.getStatus())) {

				ealrequestlog.setAction(approvalDto.getAction());
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setComments("Application forwarded successfully");
				//workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.FORWARDED);

			}

			else if (ApprovalStatus.REJECT.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application rejected successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				//eal.setVendorStatus(null);
				ealRequestPUtoBWFLRepository.save(eal);
				ealPUtoBWFLLogRepository.save(ealrequestlog);
				//workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.REJECTED);

			} else if (ApprovalStatus.APPROVED.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application approved successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				//eal.setVendorStatus(VendorStatus.REQUESTED);
				ealRequestPUtoBWFLRepository.save(eal);
				ealPUtoBWFLLogRepository.save(ealrequestlog);
				//workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.APPROVED);

			} else if (ApprovalStatus.REQUESTFORCLARIFICATION.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application requestforclarification successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				ealPUtoBWFLLogRepository.save(ealrequestlog);
				//workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.CLARIFICATION_REQUEST_SENT_SUCCESSFULLY);
				//workflowStatusUpdateDto.setSendBackTo("Level 1");
			}

//			if (approvalDto.getEntityName().equalsIgnoreCase("BREWERY")
//					|| approvalDto.getEntityName().equalsIgnoreCase("DISTILLERY")
//					|| approvalDto.getEntityName().equalsIgnoreCase("BOTTELING")) {
//				workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
//
//			} else {
//				workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
//			}
//
//			workflowStatusUpdateDto.setApplicationNumber(ealEntity.get().getRequestedapplnNo());
//			workflowStatusUpdateDto.setModuleNameCode(approvalDto.getModuleNameCode());
//			// workflowStatusUpdateDto.setSubModuleNameCode(approvalDto.getSubModuleNameCode());
//			workflowStatusUpdateDto.setLevel(approvalDto.getLevel());
//			workFlowService.callEALWorkFlowService(workflowStatusUpdateDto);
		return resp;
		} else {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		
	}
	public GenericResponse forceclosureupdate(EalPUtoBWFLRequestDTO requestDTO) {
		try {
			ealRequestPUtoBWFLRepository.updateForceclosure(requestDTO.isForceclosureFlag(), requestDTO.getRequestedapplnNo());
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
		return Library.getSuccessfulResponse("", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				"Force Closure Updated Successfully");
	}
	public GenericResponse getAllByapproved() {
		List<EALRequestPUtoBWFLEntity> ealEntityList = ealRequestPUtoBWFLRepository.findByStatusOrderByIdDesc();
		if (ealEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
}
