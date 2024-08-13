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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.repository.EALAvailable;
import com.oasys.helpdesk.repository.EALDashboard;
import com.oasys.helpdesk.repository.ReceviedBarQr;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.constant.VendorStatus;
import com.oasys.posasset.dto.ApprovalDTO;
import com.oasys.posasset.dto.EALAvailableDTO;
import com.oasys.posasset.dto.EALStockwastageDTO;
import com.oasys.posasset.dto.EalDispatchedDetailsDTO;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestDashboardDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.dto.EalRequestSummaryCountDTO;
import com.oasys.posasset.dto.EalStocksearchDTO;
import com.oasys.posasset.dto.placeholderDTO;
import com.oasys.posasset.entity.BwflDispatchRequestEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestLogEntity;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLLogEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLMapEntity;
import com.oasys.posasset.entity.EALStockEntity;
import com.oasys.posasset.entity.EALStockinPUEntity;
import com.oasys.posasset.entity.TPRequestEntity;
import com.oasys.posasset.mapper.StockOverviewMapper;
import com.oasys.posasset.repository.BwflDispatchRequestRepository;
import com.oasys.posasset.repository.EALRequestLogRepository;
import com.oasys.posasset.repository.EALRequestMapRepository;
import com.oasys.posasset.repository.EALRequestPUtoBWFLLogRepository;
import com.oasys.posasset.repository.EALRequestPUtoBWFLMapRepository;
import com.oasys.posasset.repository.EALRequestPUtoBWFLRepository;
import com.oasys.posasset.repository.EALRequestRepository;
import com.oasys.posasset.repository.EALStockInPURepository;
import com.oasys.posasset.repository.TpRequestRepository;
import com.oasys.posasset.service.WorkFlowService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EALStockInPURequestService {

	@Autowired
	EALRequestRepository ealrequestRepository;

	@Autowired
	EALRequestMapRepository ealrequestmapRepository;

	@Autowired
	EALStockInPURepository ealstockrepository;
	
	@Autowired
	EALStockInPURepository ealStockInPURepository;
	
	@Autowired
	EALRequestPUtoBWFLLogRepository ealRequestPUtoBWFLLogRepository;
	
	@Autowired
	EALRequestPUtoBWFLMapRepository ealRequestPUtoBWFLMapRepository;
	
	@Autowired
	EALRequestPUtoBWFLRepository ealRequestPUtoBWFLRepository;
	
	@Autowired
	BwflDispatchRequestRepository bwflDispatchRequestRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private StockOverviewMapper ealrequestMapper;

	@Autowired
	EALRequestLogRepository ealrequestlogRepository;

	@Value("${workflow.domain}")
	private String workflow;

	@Value("${domain}")
	private String domain;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	TpRequestRepository tpRequestRepository;

	@Autowired
	HttpServletRequest headerRequest;

	@Autowired
	WorkFlowService workFlowService;

	@Value("${spring.common.devtoken}")
	private String token;

	@Value("${spring.common.stockbarcode}")
	private String stockurl;

	@Autowired
	TpRequestRepository tprepository;

	public GenericResponse save(EalRequestDTO requestDTO) {
		List<EALRequestEntity> finallist = new ArrayList<EALRequestEntity>();
		ArrayList<EALRequestMapEntity> finallist1 = new ArrayList<EALRequestMapEntity>();

		try {
			Optional<EALRequestEntity> ealDetails = ealrequestRepository
					.findByLicenseNoAndRequestedapplnNo(requestDTO.getLicenseNo(), requestDTO.getRequestedapplnNo());
			if (!ealDetails.isPresent()) {
				// EALRequestEntity ealrequestEntity = commonUtil.modalMap(requestDTO,
				// EALRequestEntity.class);
				EALRequestEntity ealrequestEntity = new EALRequestEntity();
				EalRequestDTO obje = new EalRequestDTO();
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
				ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
				ealrequestEntity.setUnitCode(requestDTO.getUnitCode());
				ealrequestEntity.setForceclosureFlag(false);
				// ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
				ealrequestEntity.setVendorStatus(VendorStatus.INPROGRESS);
				ealrequestEntity = ealrequestRepository.save(ealrequestEntity);

				finallist.add(ealrequestEntity);
				Long ealrequstid = ealrequestEntity.getId();

				Optional<EALRequestEntity> DeptOptional = ealrequestRepository.findById(ealrequstid);

				requestDTO.getEalmapRequestDTO().forEach(action -> {
					EALRequestMapEntity ealrequestmap = new EALRequestMapEntity();
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
					ealrequestmapRepository.save(ealrequestmap);
					finallist1.add(ealrequestmap);
				});

				finallist1.stream().forEach(ealrequestmap -> {
					EALRequestEntity ealrequestEntity1 = new EALRequestEntity();
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
					EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
					ealrequestlog.setComments("EAL saved successfully");
					ealrequestlog.setApplnNo(requestDTO.getRequestedapplnNo());
					ealrequestlog.setUserName(requestDTO.getUserName());
					ealrequestlog.setAction(requestDTO.getAction());
					ealrequestlog.setRemarks(requestDTO.getRemarks());
					ealrequestlogRepository.save(ealrequestlog);
					if (requestDTO.getEntityName().equalsIgnoreCase("BREWERY")
							|| requestDTO.getEntityName().equalsIgnoreCase("BOTTELING")) {
						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
					}
					if (requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")) {
						workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTDISTILLERY");
					} else {
						workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
					}
					workflowStatusUpdateDto.setApplicationNumber(requestDTO.getRequestedapplnNo());
					workflowStatusUpdateDto.setModuleNameCode(requestDTO.getModuleNameCode());
					// workflowStatusUpdateDto.setSubModuleNameCode(requestDTO.getSubModuleNameCode());
					workflowStatusUpdateDto.setEvent(requestDTO.getEvent());
					workflowStatusUpdateDto.setLevel(requestDTO.getLevel());
					workFlowService.callEALWorkFlowService(workflowStatusUpdateDto);
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

	public GenericResponse update(EalRequestDTO requestDTO) {
		try {
			Optional<EALRequestEntity> ealDetails = ealrequestRepository.findById(requestDTO.getId());
			if (!ealDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {
				EALRequestEntity ealrq = ealDetails.get();
				ealrq.setStatus(ApprovalStatus.INPROGRESS);
				ealrq.setTotBarcode(requestDTO.getTotBarcode());
				ealrq.setTotQrcode(requestDTO.getTotQrcode());
				ealrequestRepository.save(ealrq);
				// Optional<EALRequestMapEntity> DeptOptional =
				// ealrequestmapRepository.findById(ealrequstid);

				requestDTO.getEalmapRequestDTO().forEach(action -> {
					Optional<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.findById(action.getId());
					EALRequestMapEntity ealrequestmap = DeptOptional.get();
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
					ealrequestmapRepository.save(ealrequestmap);

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
					workFlowService.callEALWorkFlowService(workflowStatusUpdateDto);
					EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
					ealrequestlog.setComments(requestDTO.getRemarks());
					ealrequestlog.setRemarks(requestDTO.getRemarks());
					ealrequestlog.setApplnNo(requestDTO.getRequestedapplnNo());
					ealrequestlog.setUserName(requestDTO.getUserName());
					ealrequestlog.setAction(requestDTO.getAction());
					ealrequestlogRepository.save(ealrequestlog);
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
		List<EALRequestEntity> ealrequstlist = new ArrayList<EALRequestEntity>();

		List<EALRequestEntity> ealrequestEntity = ealrequestRepository.getById(id);

		ealrequstlist.addAll(ealrequestEntity);

		if (!ealrequestEntity.isEmpty()) {

//			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			EALRequestEntity ealrequestEntity1 = new EALRequestEntity();
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

	public GenericResponse getAll() {
		List<EALRequestPUtoBWFLEntity> DepList = ealRequestPUtoBWFLRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(DepList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getCode() {
		// MenuPrefix prefix = MenuPrefix.getType(GS_CODE);
		Year y = Year.now();
		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		String code = RandomUtil.getRandomNumber();
		return Library.getSuccessfulResponse("STIN-" + code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getCodeeal() {
		// MenuPrefix prefix = MenuPrefix.getType(GS_CODE);
		Year y = Year.now();
		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		String code = RandomUtil.getRandomNumber();
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

//	public GenericResponse getByealid(Long id) {
//		
//       List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);
//		
//		Optional<EALRequestEntity> ealrequestEntity =ealrequestRepository.findById(id);
//		
//		Integer totabarcode=ealrequestEntity.get().getTotBarcode();
//		
//		Integer totalqrcode=ealrequestEntity.get().getTotQrcode();
//		
//		String stockappln=ealrequestEntity.get().getRequestedapplnNo();
//		
//		List<BarQrBalanceDTO> balancedetails = ealrequestmapRepository.getByStockApplnno(stockappln);
//		
//		//List<BarQrBalanceDTO> balancedetailst = ealrequestmapRepository.getByStockAppln(stockappln);
////		EalRequestMapResponseDTO tot=new EalRequestMapResponseDTO();	
////		balancedetails.stream().forEach(act->{
////			tot.setPackagingSize(act.getPackagingSize());
////			tot.setNoofBarcodereceived(act.getNoofBarcodereceived());
////			tot.setNoofQrcodereceived(act.getNoofQrcodereceived());
////		});
//		
//		List<EalRequestMapResponseDTO> stockdetails=new ArrayList<EalRequestMapResponseDTO>();
//		DeptOptional.parallelStream().forEach(maplist ->{
//			EalRequestMapResponseDTO obj=new EalRequestMapResponseDTO();
//			obj.setId(maplist.getId());
//			obj.setCreatedDate(String.valueOf(maplist.getCreatedDate()));
//			obj.setCartonSize(maplist.getCartonSize());
//			obj.setPackagingSize(maplist.getPackagingSize());
//			obj.setNoofBarcode(maplist.getNoofBarcode());
//			obj.setNoofQrcode(maplist.getNoofQrcode());
//			obj.setRemarks(maplist.getRemarks());
//			obj.setUnmappedType(maplist.getUnmappedType());
//			obj.setCodeType(maplist.getCodeType());
//			obj.setNoofBarcodepending(maplist.getNoofBarcodepending());
//			obj.setNoofBarcodereceived(maplist.getNoofBarcodereceived());
//			obj.setNoofQrcodereceived(maplist.getNoofQrcodereceived());
//			obj.setNoofRollcodereceived(maplist.getNoofRollcodereceived());
//			obj.setNoofqrpending(maplist.getNoofqrpending());
//			obj.setStockApplnno(maplist.getStockApplnno());
//			obj.setStockDate(maplist.getStockDate());
//			obj.setLicencenumber(maplist.getLicenseNo());
//			obj.setFlag(maplist.isFlag());
//			obj.setEalrequestId(maplist.getEalrequestId().getId());
//			obj.setTotalnumofBarcode(totabarcode);
//			obj.setTotalnumofQrcode(totalqrcode);
//			balancedetails.stream().forEach(stockbalance->{
//				EalRequestMapResponseDTO receivedetails=new EalRequestMapResponseDTO();	
//				receivedetails.setPackagingSize(stockbalance.getPackagingSize());
//				receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
//				receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
//				if(obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
//					obj.setNoofBarcodeBalance(totabarcode-(receivedetails.getNoofBarcodereceived()));	
//					obj.setNoofQrcodeBalance(totalqrcode-(receivedetails.getNoofQrcodereceived()));
//					
//				}
//				
//			});
//					
//			stockdetails.add(obj);
//			
//		});
//		List<EalRequestMapResponseDTO> unique = stockdetails.stream()
//              .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(EalRequestMapResponseDTO::getTotalnumofBarcode))),
//                                         ArrayList::new));
//		
//		if (CollectionUtils.isEmpty(DeptOptional)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		return Library.getSuccessfulResponse(unique, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	} 

	public GenericResponse getByealid(Long id) {

		List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

		List<BarQrBalanceDTO> tpdispatch = tprepository.getByEalreqid(id);

		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository.findById(id);

		Integer totabarcode = ealrequestEntity.get().getTotBarcode();

		Integer totalqrcode = ealrequestEntity.get().getTotQrcode();

		String stockappln = ealrequestEntity.get().getRequestedapplnNo();

		List<BarQrBalanceDTO> balancedetails = ealrequestmapRepository.getByStockApplnno(stockappln);

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
				if (obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
					obj.setNoofBarcodeBalance(totabarcode - (receivedetails.getNoofBarcodereceived()));
					obj.setNoofQrcodeBalance(totalqrcode - (receivedetails.getNoofQrcodereceived()));

				}

			});

			tpdispatch.stream().forEach(dispatch -> {
				if (obj.getPackagingSize().equalsIgnoreCase(dispatch.getPackagingSize())) {
					obj.setDispatchnoofBarcodereceived(dispatch.getDispatchNoofBarcodereceived());
					obj.setDispatchnoofQrcodereceived(dispatch.getDispatchNoofQrcodereceived());
					obj.setDispatchnoofRollcodereceived(dispatch.getDispatchNoofRollcodereceived());
				}
			});

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

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALRequestEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<EALRequestEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<EALRequestEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestEntity> cq = cb.createQuery(EALRequestEntity.class);
		Root<EALRequestEntity> from = cq.from(EALRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestEntity> typedQuery = null;
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

	public List<EALRequestEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestEntity> cq = cb.createQuery(EALRequestEntity.class);
		Root<EALRequestEntity> from = cq.from(EALRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestEntity> typedQuery1 = null;
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
			Root<EALRequestEntity> from) {

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
		List<EALRequestEntity> deviceReturnEntityList = ealrequestRepository.findByCreatedByOrderByIdDesc(userId);
		if (deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(deviceReturnEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse updateApproval(ApprovalDTO approvalDto) {
		GenericResponse resp = null;
		Optional<EALRequestEntity> ealEntity = ealrequestRepository.findById(approvalDto.getId());
		if (ealEntity.isPresent()) {
			EALRequestEntity eal = ealEntity.get();
			eal.setStatus(approvalDto.getStatus());
			eal.setModifiedBy(approvalDto.getUserId());
			eal.setModifiedDate(new Date());
			eal.setRemarks(approvalDto.getRemarks());
			eal.setApprovedBy(ealEntity.get().getCurrentlyWorkwith());
			ealrequestRepository.save(eal);
			EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
			WorkflowDTO workflowStatusUpdateDto = new WorkflowDTO();

			if (approvalDto.getEvent().equals("APPROVED")
					&& ApprovalStatus.INPROGRESS.equals(approvalDto.getStatus())) {

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
				eal.setVendorStatus(null);
				ealrequestRepository.save(eal);
				ealrequestlogRepository.save(ealrequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.REJECTED);

			} else if (ApprovalStatus.APPROVED.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application approved successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				eal.setVendorStatus(VendorStatus.REQUESTED);
				ealrequestRepository.save(eal);
				ealrequestlogRepository.save(ealrequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
				resp = Library.getSuccessfulResponse(approvalDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.APPROVED);

			} else if (ApprovalStatus.REQUESTFORCLARIFICATION.equals(approvalDto.getStatus())) {
				ealrequestlog.setComments("Application requestforclarification successfully");
				ealrequestlog.setApplnNo(approvalDto.getApplnNo());
				ealrequestlog.setUserName(approvalDto.getUserName());
				ealrequestlog.setAction(approvalDto.getAction());
				ealrequestlogRepository.save(ealrequestlog);
				workflowStatusUpdateDto.setEvent(approvalDto.getEvent());
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

	public GenericResponse getLogByApplicationNo(String applicationNo) {
		List<EALRequestLogEntity> ealLogEntity = ealrequestlogRepository.findByApplnNoOrderByIdDesc(applicationNo);
		if (ealLogEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealLogEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getLogByApplicationNostock(String applicationNo) {
		List<EALRequestPUtoBWFLLogEntity> ealLogEntity = ealRequestPUtoBWFLLogRepository.findByApplnNoOrderByIdDesc(applicationNo);
		if (ealLogEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealLogEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getmaindashboard(EalRequestDTO requestDTO) {
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getFromDate() + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getToDate() + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		List<EALDashboard> ticketEntityList = null;

		List<String> licArray;
		if (requestDTO.getLicenseNumberArray().isEmpty()) {
			licArray = null;
		} else {
			licArray = requestDTO.getLicenseNumberArray();
		}

		List<String> unitCodeArray;
		if (requestDTO.getUnitCodeArray().isEmpty()) {
			unitCodeArray = null;
		} else {
			unitCodeArray = requestDTO.getUnitCodeArray();
		}

		String district;
		if (requestDTO.getDistrict().isEmpty()) {
			district = null;
		} else {
			district = requestDTO.getDistrict();
		}
		List<String> licenseNumberArray;
		if (requestDTO.getLicenseNumberArray() == null || requestDTO.getLicenseNumberArray().isEmpty()) {
			licenseNumberArray = null;
		} else {
			licenseNumberArray = requestDTO.getLicenseNumberArray();
		}

//		if (licArray == null) {
//			ticketEntityList = ealrequestRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApproved(
//					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district);
//		} else {
//			ticketEntityList = ealrequestRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedBy(
//					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district,
//					licArray);
//		}

		if (unitCodeArray == null && licArray == null) {
			ticketEntityList = ealrequestRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApproved(
					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district);
		} else if (unitCodeArray != null && licArray == null){
			ticketEntityList = ealrequestRepository
					.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedByAndUnitcodeArray(fromDate, toDate,
							requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district,
							unitCodeArray);
		} else if (unitCodeArray == null && licArray != null) {
			ticketEntityList = ealrequestRepository.getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedBy(
					fromDate, toDate, requestDTO.getCurrentlyWorkwith(), requestDTO.getCurrentlyWorkwith(), district,
					licArray);
		}

		if (CollectionUtils.isEmpty(ticketEntityList)
				|| ticketEntityList.stream().allMatch(ticket -> ticket.getTotalealrequested() == 0)) {
			throw new RecordNotFoundException("No Record Found");
		}

		if (CollectionUtils.isEmpty(ticketEntityList)
				|| ticketEntityList.stream().allMatch(ticket -> ticket.getTotalealrequested() == 0)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse getAllByDesignationCode(String designationCode) {
		List<EALRequestEntity> ealEntityList = ealrequestRepository
				.findByCurrentlyWorkwithOrderByIdDesc(designationCode);
		if (ealEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getmaindashboardapplicant(EalRequestDashboardDTO requestDTO) {
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getFromDate() + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(requestDTO.getToDate() + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		Long userid = requestDTO.getCreatedBy();

		List<EALDashboard> ticketEntityList = ealrequestRepository
				.getCountByStatusAndCreatedDateBetweenAndCreatedBy(fromDate, toDate, userid);

		if (CollectionUtils.isEmpty(ticketEntityList)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return Library.getSuccessfulResponse(ticketEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

//	public GenericResponse EALStocksave(EalRequestDTO requestDTO) {
//		
////		Optional<EALStockinPUEntity> ealDetails=ealstockrepository
////				.findByStockApplnno(requestDTO.getStockApplnno());
////		 if(!ealDetails.isPresent()) {
//		EALStockinPUEntity stockentity=new EALStockinPUEntity();
//		stockentity.setStockApplnno(requestDTO.getStockApplnno());
//		stockentity.setStockDate(requestDTO.getStockDate());
//		stockentity.setTotalnumofBarcode(requestDTO.getTotalnumofBarcode());
//		stockentity.setTotalnumofQrcode(requestDTO.getTotalnumofQrcode());
//		stockentity.setTotalnumofRoll(requestDTO.getTotalnumofRoll());
//		stockentity.setCodeType(requestDTO.getCodeType());
//		stockentity.setUnmappedType(requestDTO.getUnmappedType());
//		stockentity.setEalrequestId(requestDTO.getEalrequestId());
//		stockentity.setOpenstockApplnno(requestDTO.getOpenstockApplnno());
//		stockentity.setLicenseNo(requestDTO.getLicenseNo());
//		ealstockrepository.save(stockentity);
//		
//		ArrayList<EALRequestMapEntity> finallist1=new ArrayList<EALRequestMapEntity>();	
//		try {
//			
//			List<EalRequestMapResponseDTO> setlist=new ArrayList<EalRequestMapResponseDTO>();
//			ArrayList<String> list=new ArrayList<String>();
//				
////			Optional<EALRequestMapEntity> ealDetails=ealrequestmapRepository.findByStockApplnno(requestDTO.getStockApplnno());
////	         if(!ealDetails.isPresent()) {
//			
//			requestDTO.getEalmapRequestDTO().forEach(action -> {
//				Optional<EALRequestMapEntity> DeptOptional = ealrequestmapRepository
//						.findById(action.getId());
//				if(!DeptOptional.isPresent()) {
//					throw new InvalidDataValidation("Record not exist");
//				}
//				EALRequestMapEntity ealrequestmap =new EALRequestMapEntity();
//				//EALRequestMapEntity ealrequestmap = DeptOptional.get();
//				ealrequestmap.setCartonSize(action.getCartonSize());
//				ealrequestmap.setNoofBarcode(action.getNoofBarcode());
//				ealrequestmap.setNoofQrcode(action.getNoofQrcode());
//				ealrequestmap.setPackagingSize(action.getPackagingSize());
//				ealrequestmap.setRemarks(action.getRemarks());
//				ealrequestmap.setUnmappedType(action.getUnmappedType());
//				ealrequestmap.setCodeType(requestDTO.getCodeType());
//				ealrequestmap.setNoofBarcodereceived(action.getNoofBarcodereceived());
//				ealrequestmap.setNoofQrcodereceived(action.getNoofQrcodereceived());
//				ealrequestmap.setNoofRollcodereceived(action.getNoofRollcodereceived());
//				ealrequestmap.setNoofBarcodepending(action.getNoofBarcodepending());
//				ealrequestmap.setNoofqrpending(action.getNoofqrpending());
//				ealrequestmap.setTotalnumofBarcode(action.getTotalnumofBarcode());
//				ealrequestmap.setTotalnumofQrcode(action.getTotalnumofQrcode());
//				ealrequestmap.setTotalnumofRoll(action.getTotalnumofRoll());
//				ealrequestmap.setStockApplnno(action.getStockApplnno());
//				ealrequestmap.setStockDate(action.getStockDate());
//				ealrequestmap.setEalrequestId(DeptOptional.get().getEalrequestId());
//				ealrequestmap.setOpenstock(action.getOpenstock());
//				ealrequestmap.setFlag(action.isFlag());
//				ealrequestmap.setLicenseNo(requestDTO.getLicenseNo());
//				ealrequestmapRepository.save(ealrequestmap);
//				finallist1.add(ealrequestmap);
//			
//			}); 
//			return Library.getSuccessfulResponse(finallist1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//					ErrorMessages.RECORED_CREATED);
//
//			
//					
//		} catch (Exception e) {
//			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
//		}
////		 }
////		else {
////			
////				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record already exist");
////			
////		}
//	}
//	

	public GenericResponse ealStockSave(List<EalRequestMapResponseDTO> stock) {
		ArrayList<EALStockinPUEntity> finallist1 = new ArrayList<EALStockinPUEntity>();
		EALStockinPUEntity stockentity2 = new EALStockinPUEntity();
		try {
	        for (EalRequestMapResponseDTO requestDTO : stock) {
	            List<EALStockinPUEntity> stockdetails = ealStockInPURepository.getbyTpApplnno(requestDTO.getTpApplnNo());
	            if (!stockdetails.isEmpty()) {
	                return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), 
	                                                   "This TP Number is Already Stock In Completed");
	            }
	        }
			stock.stream().forEach(requestDTO -> {
				EALStockinPUEntity stockentity = new EALStockinPUEntity();
				stockentity.setStockApplnno(requestDTO.getStockApplnno());
				stockentity.setStockDate(requestDTO.getStockDate());
				stockentity.setTotalnumofBarcode(requestDTO.getTotalnumofBarcode());
				stockentity.setTotalnumofQrcode(requestDTO.getTotalnumofQrcode());
				stockentity.setTotalnumofRoll(requestDTO.getTotalnumofRoll());
				stockentity.setCodeType(requestDTO.getCodeType());
				stockentity.setUnmappedType(requestDTO.getUnmappedType());
				stockentity.setOpenstockApplnno(requestDTO.getOpenstockApplnno());
				stockentity.setLicenseNo(requestDTO.getLicencenumber());
				stockentity.setCartonSize(requestDTO.getCartonSize());
				stockentity.setNoofBarcode(requestDTO.getNoofBarcode());
				stockentity.setNoofQrcode(requestDTO.getNoofQrcode());
				stockentity.setPackagingSize(requestDTO.getPackagingSize());
				stockentity.setRemarks(requestDTO.getRemarks());
				stockentity2.setRemarks(requestDTO.getRemarks());
				stockentity.setNoofBarcodereceived(requestDTO.getNoofBarcodereceived());
				stockentity.setNoofQrcodereceived(requestDTO.getNoofQrcodereceived());
				stockentity.setNoofRollcodereceived(requestDTO.getNoofRollcodereceived());
				stockentity.setNoofBarcodepending(requestDTO.getNoofBarcodepending());
				stockentity.setNoofqrpending(requestDTO.getNoofqrpending());
				stockentity.setEalrequestapplno(requestDTO.getEalrequestApplnno());
				stockentity2.setEalrequestapplno(requestDTO.getEalrequestApplnno());
				stockentity.setFlag(requestDTO.isFlag());
				stockentity.setNoofBarcodedamaged(requestDTO.getNoofBarcodedamaged());
				stockentity.setNoofQrcodedamaged(requestDTO.getNoofQrcodedamaged());
				stockentity.setPrintingType(requestDTO.getPrintingType());
				stockentity.setMapType(requestDTO.getMapType());
				stockentity.setUnitCode(requestDTO.getUnitCode());
				stockentity.setNoofRollCode(requestDTO.getNoofRollCode());
				stockentity.setCreatedBy(requestDTO.getCreatedBy());
				ealstockrepository.save(stockentity);
				finallist1.add(stockentity);
			});

			try {
				EALRequestPUtoBWFLLogEntity ealrequestlog = new EALRequestPUtoBWFLLogEntity();
				ealrequestlog.setComments("STIN Saved Successfully");
				ealrequestlog.setApplnNo(stockentity2.getEalrequestapplno());
				ealrequestlog.setAction(stockentity2.getRemarks());
				ealRequestPUtoBWFLLogRepository.save(ealrequestlog);
			} catch (Exception e) {
				log.info("STIN LOG::::" + e);
			}

			return Library.getSuccessfulResponse(finallist1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}

	}

	public GenericResponse stockgetById(Long id) {
		List<EALRequestPUtoBWFLEntity> ealrequstlist = new ArrayList<EALRequestPUtoBWFLEntity>();

		List<EALRequestPUtoBWFLEntity> ealrequestEntity = ealRequestPUtoBWFLRepository.getById(id);

		ealrequstlist.addAll(ealrequestEntity);

		if (!ealrequestEntity.isEmpty()) {
			List<EALRequestPUtoBWFLMapEntity> DeptOptional = ealRequestPUtoBWFLMapRepository.getById(id);

			EALRequestPUtoBWFLEntity ealrequestEntity1 = new EALRequestPUtoBWFLEntity();
			Long ealrequestid;
			DeptOptional.stream().forEach(action -> {
				ealrequestEntity1.setEalrequestId(action.getEalrequestPUtoBWFLId().getId());
			});
			ealrequstlist.add(ealrequestEntity1);
			return Library.getSuccessfulResponse(ealrequstlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

		else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}

	}

	public GenericResponse stockgetByealid(Long id) {
		List<EALRequestPUtoBWFLMapEntity> DeptOptional = ealRequestPUtoBWFLMapRepository.getById(id);
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(DeptOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

//	public GenericResponse stockgetByealid(Long id) {
//	
//		
//		List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);
//		
//		Optional<EALRequestEntity> ealrequestEntity =ealrequestRepository.findById(id);
//		
//		Integer totabarcode=ealrequestEntity.get().getTotBarcode();
//		
//		Integer totalqrcode=ealrequestEntity.get().getTotQrcode();
//		
//		String stockappln=ealrequestEntity.get().getRequestedapplnNo();
//		
//		List<EALRequestMapEntity> balancedetails = ealrequestmapRepository.getByStockApplnno(stockappln);
//		
//		List<EalRequestMapResponseDTO> stockdetails=new ArrayList<EalRequestMapResponseDTO>();
//		DeptOptional.parallelStream().forEach(maplist ->{
//			EalRequestMapResponseDTO obj=new EalRequestMapResponseDTO();
//			obj.setId(maplist.getId());
//			obj.setCreatedDate(String.valueOf(maplist.getCreatedDate()));
//			obj.setCartonSize(maplist.getCartonSize());
//			obj.setPackagingSize(maplist.getPackagingSize());
//			obj.setNoofBarcode(maplist.getNoofBarcode());
//			obj.setNoofQrcode(maplist.getNoofQrcode());
//			obj.setRemarks(maplist.getRemarks());
//			obj.setUnmappedType(maplist.getUnmappedType());
//			obj.setCodeType(maplist.getCodeType());
//			obj.setNoofBarcodepending(maplist.getNoofBarcodepending());
//			obj.setNoofBarcodereceived(maplist.getNoofBarcodereceived());
//			obj.setNoofQrcodereceived(maplist.getNoofQrcodereceived());
//			obj.setNoofRollcodereceived(maplist.getNoofRollcodereceived());
//			obj.setNoofqrpending(maplist.getNoofqrpending());
//			obj.setStockApplnno(maplist.getStockApplnno());
//			obj.setStockDate(maplist.getStockDate());
//			obj.setLicencenumber(maplist.getLicenseNo());
//			obj.setFlag(maplist.isFlag());
//			obj.setEalrequestId(maplist.getEalrequestId().getId());
//			balancedetails.stream().forEach(stockbalance->{
//				EalRequestMapResponseDTO receivedetails=new EalRequestMapResponseDTO();	
//				receivedetails.setPackagingSize(stockbalance.getPackagingSize());
//				receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
//				receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
//				if(obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
//					obj.setNoofBarcodeBalance(totabarcode-(receivedetails.getNoofBarcodereceived()));	
//					obj.setNoofQrcodeBalance(totalqrcode-(receivedetails.getNoofQrcodereceived()));
//					
//				}
//				
//			});
//					
//			stockdetails.add(obj);
//			
//		});
//		
//		
//		if (CollectionUtils.isEmpty(DeptOptional)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		return Library.getSuccessfulResponse(stockdetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	} 

	public GenericResponse stockgetAllByUserId(Long userId) {
		List<EALRequestPUtoBWFLMapEntity> deviceReturnEntityList = ealRequestPUtoBWFLMapRepository.findByCreatedByOrderByIdDesc(userId);
		if (deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(deviceReturnEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	// public GenericResponse getyApplicationNocreatedBy(String applicationNo, Long
	// createdby, Boolean forceclosureFlag) {
	public GenericResponse getyApplicationNocreatedBy(String applicationNo, Long createdby) {
		Optional<EALRequestPUtoBWFLEntity> ealforceclosure = ealRequestPUtoBWFLRepository.findByRequestedapplnNo(applicationNo);
		Boolean forceflag = ealforceclosure.get().isForceclosureFlag();
		if (forceflag.equals(true)) {

			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					"This EAL Request Number is Force Closured");
		} else {

			Optional<EALRequestPUtoBWFLEntity> ealoptional = ealRequestPUtoBWFLRepository
					.getByRequestedapplnNoAndStatusAndCreatedby(applicationNo, createdby);

			Optional<EALRequestPUtoBWFLEntity> ealoptionalappl = ealRequestPUtoBWFLRepository.findByRequestedapplnNo(applicationNo);

			Optional<EALRequestPUtoBWFLEntity> ealoptionalreject = ealRequestPUtoBWFLRepository
					.getByRequestedapplnNoAndStatusapAndCreatedby(applicationNo, createdby);

			Optional<EALRequestPUtoBWFLEntity> ealoptionalacknow = ealRequestPUtoBWFLRepository
					.getByRequestedapplnNoAndStatusAckAndCreatedby(applicationNo, createdby);

			if (ealoptionalreject.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						"This EAL Request Number is Rejected");
			} else {
				if (!ealoptionalappl.isPresent()) {
					return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
							"This EAL Request Number Not Available");
				} else {
					if (!ealoptional.isPresent()) {
						return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
								"This EAL Request Not Yet Approved OR This User Record Not available");
					} else {
						if (!ealoptionalacknow.isPresent()) {
							return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
									"This EAL Request Not Yet dispatched");
						} else {
							return Library.getSuccessfulResponse(ealoptional.get(),
									ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
						}

					}

				}
			}
		}
	}

	public GenericResponse getsubPagesearchNewByFilterstock(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALStockinPUEntity> list = this.getSubRecordsByFilterDTO1stock(requestData);
//		List<EALStockinPUEntity> unique = list.stream()
//                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(EALRequestMapEntity::getTotalnumofBarcode))),
//                                           ArrayList::new));
		List<EALStockinPUEntity> list1 = this.getSubRecordsByFilterDTO2stock(requestData);

		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
		if (!list.isEmpty()) {

			List<ReceviedBarQr> rece = ealstockrepository.getByStockreceived();
			List<EalStocksearchDTO> setlist = new ArrayList<EalStocksearchDTO>();
			list.stream().forEach(actuallist -> {
				EalStocksearchDTO obj = new EalStocksearchDTO();
				obj.setId(actuallist.getId());
				obj.setStockApplnno(actuallist.getStockApplnno());
				obj.setCreatedDate(String.valueOf(actuallist.getCreatedDate()));
				obj.setModifiedDate(String.valueOf(actuallist.getModifiedDate()));
				obj.setModifiedBy(actuallist.getModifiedBy());
				obj.setPackagingSize(actuallist.getPackagingSize());
				obj.setCartonSize(actuallist.getCartonSize());
				obj.setNoofBarcode(actuallist.getNoofBarcode());
				obj.setNoofQrcode(actuallist.getNoofQrcode());
				obj.setRemarks(actuallist.getRemarks());
				obj.setNoofBarcodereceived(actuallist.getNoofBarcodereceived());
				obj.setNoofQrcodereceived(actuallist.getNoofQrcodereceived());
				obj.setNoofBarcodepending(actuallist.getNoofBarcodepending());
				obj.setNoofqrpending(actuallist.getNoofqrpending());
				obj.setEalrequestapplno(actuallist.getEalrequestapplno());
				obj.setUnmappedType(actuallist.getUnmappedType());
				obj.setTotalnumofBarcode(actuallist.getTotalnumofBarcode());
				obj.setTotalnumofQrcode(actuallist.getTotalnumofQrcode());
				obj.setTotalnumofRoll(actuallist.getTotalnumofRoll());
				obj.setStockApplnno(actuallist.getStockApplnno());
				obj.setStockDate(actuallist.getStockDate());
				obj.setCodeType(actuallist.getCodeType());
				obj.setOpenstockApplnno(actuallist.getOpenstockApplnno());
				obj.setFlag(actuallist.isFlag());
				obj.setLicenseNo(actuallist.getLicenseNo());
				obj.setNoofBarcodedamaged(actuallist.getNoofBarcodedamaged());
				obj.setNoofQrcodedamaged(actuallist.getNoofQrcodedamaged());
				rece.stream().forEach(sumreceived -> {
					if (obj.getStockApplnno().equalsIgnoreCase(sumreceived.getStockApplnno())) {
						obj.setNoofBarcodereceived(sumreceived.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(sumreceived.getNoofQrcodereceived());
					}
				});
				setlist.add(obj);

			});

			paginationResponseDTO.setContents(setlist);
		}
		Long count1 = (long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list.size()) ? list.size() : null);
		paginationResponseDTO.setTotalElements(count1);
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public List<EALStockinPUEntity> getSubRecordsByFilterDTO1stock(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALStockinPUEntity> cq = cb.createQuery(EALStockinPUEntity.class);
		Root<EALStockinPUEntity> from = cq.from(EALStockinPUEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALStockinPUEntity> typedQuery = null;
		addSubCriteriastock(cb, list, filterRequestDTO, from);
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
		if (Objects.nonNull(filterRequestDTO.getPageNo()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			Expression<String> stockapp = from.get("stockApplnno");
			Expression<String> unmappedtype = from.get("unmappedType");
			Expression<String> printingtype = from.get("printingType");
			// typedQuery =
			// entityManager.createQuery(cq.groupBy(stockapp,unmappedtype,printingtype))
			typedQuery = entityManager.createQuery(cq.groupBy(stockapp))
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}
		return typedQuery.getResultList();
	}

	public List<EALStockinPUEntity> getSubRecordsByFilterDTO2stock(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALStockinPUEntity> cq = cb.createQuery(EALStockinPUEntity.class);
		Root<EALStockinPUEntity> from = cq.from(EALStockinPUEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALStockinPUEntity> typedQuery1 = null;
		addSubCriteriastock(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("createdDate");
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		}
		Expression<String> mainModuleIds = from.get("stockApplnno");
		typedQuery1 = entityManager.createQuery(cq.groupBy(mainModuleIds));

//			Expression<String> stockapp =from.get("stockApplnno");
//			Expression<String> unmappedtype =from.get("unmappedType");
//			Expression<String> printingtype =from.get("printingType");
//			typedQuery1 = entityManager.createQuery(cq.groupBy(stockapp,unmappedtype,printingtype));	
		return typedQuery1.getResultList();
	}

	private void addSubCriteriastock(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<EALStockinPUEntity> from) {
		
		Date fromDate = null;

		Date toDate = null;

		String fdate = null;

		String tdate = null;

		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockApplnno"))
				&& !filterRequestDTO.getFilters().get("stockApplnno").toString().trim().isEmpty()) {

			String stockapplnno = filterRequestDTO.getFilters().get("stockApplnno").toString();
			list.add(cb.equal(from.get("stockApplnno"), stockapplnno));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("eal_request_applnno"))
				&& !filterRequestDTO.getFilters().get("eal_request_applnno").toString().trim().isEmpty()) {

			String stockapplnno = filterRequestDTO.getFilters().get("eal_request_applnno").toString();
			list.add(cb.equal(from.get("eal_request_applnno"), stockapplnno));
		}
		
		
//		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockDate"))
//				&& !filterRequestDTO.getFilters().get("stockDate").toString().trim().isEmpty()) {
//
//			String stockdate = (filterRequestDTO.getFilters().get("stockDate").toString());
//			list.add(cb.equal(from.get("stockDate"), stockdate));
//		}
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockDate"))
				&& !filterRequestDTO.getFilters().get("stockDate").toString().trim().isEmpty()) {
			try {
				try {
					fdate = String.valueOf(filterRequestDTO.getFilters().get("stockDate").toString());
					fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
			} catch (Exception e) {
				log.error("error occurred while parsing refertic_number :: {}", e);
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockDate"))
				&& !filterRequestDTO.getFilters().get("stockDate").toString().trim().isEmpty()) {
			try {
				try {
					tdate = String.valueOf(filterRequestDTO.getFilters().get("stockDate").toString());
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

			String licencenumber = (filterRequestDTO.getFilters().get("licenseNo").toString());
			list.add(cb.equal(from.get("licenseNo"), licencenumber));
		}

		if ((Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
				&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
						&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty())) {
			Long createdby = Long.valueOf(filterRequestDTO.getFilters().get("createdBy").toString());

			list.add(cb.equal(from.get("createdBy"), createdby));
		}

		if ((Objects.nonNull(filterRequestDTO.getFilters().get("flag"))
				&& !filterRequestDTO.getFilters().get("flag").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("flag"))
						&& !filterRequestDTO.getFilters().get("flag").toString().trim().isEmpty())) {
			Boolean flag = Boolean.valueOf(filterRequestDTO.getFilters().get("flag").toString());

			list.add(cb.equal(from.get("flag"), flag));
		}

		if ((Objects.nonNull(filterRequestDTO.getFilters().get("ealrequestId"))
				&& !filterRequestDTO.getFilters().get("ealrequestId").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("ealrequestId"))
						&& !filterRequestDTO.getFilters().get("ealrequestId").toString().trim().isEmpty())) {
			Long createdby = Long.valueOf(filterRequestDTO.getFilters().get("ealrequestId").toString());

			list.add(cb.equal(from.get("ealrequestId"), createdby));
		}

//		if (Objects.nonNull(filterRequestDTO.getFilters().get("status"))
//				&& !filterRequestDTO.getFilters().get("status").toString().trim().isEmpty()) {
//			ApprovalStatus status = null;
//			if (filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.DRAFT.name())) {
//				status = ApprovalStatus.DRAFT;
//			} else if (filterRequestDTO.getFilters().get("status").toString()
//					.equals(ApprovalStatus.INPROGRESS.name())) {
//				status = ApprovalStatus.INPROGRESS;
//			} else if (filterRequestDTO.getFilters().get("status").toString()
//					.equals(ApprovalStatus.REQUESTFORCLARIFICATION.name())) {
//				status = ApprovalStatus.REQUESTFORCLARIFICATION;
//			} else if (filterRequestDTO.getFilters().get("status").toString().equals(ApprovalStatus.APPROVED.name())) {
//				status = ApprovalStatus.APPROVED;
//			}
//			list.add(cb.equal(from.get("status"), status));
//		}

	}

	public GenericResponse EALStockupdate(EalPUtoBWFLRequestDTO requestDTO) {

		ArrayList<EALRequestPUtoBWFLMapEntity> finallist1 = new ArrayList<EALRequestPUtoBWFLMapEntity>();
		try {
			requestDTO.getEalmapRequestDTO().forEach(action -> {
				Optional<EALRequestPUtoBWFLMapEntity> DeptOptional = ealRequestPUtoBWFLMapRepository.findById(action.getId());
				if (!DeptOptional.isPresent()) {
					throw new InvalidDataValidation("Record not exist");
				}
				EALRequestPUtoBWFLMapEntity ealrequestmap = DeptOptional.get();
				ealrequestmap.setCartonSize(action.getCartonSize());
				ealrequestmap.setNoofBarcode(action.getNoofBarcode());
				ealrequestmap.setNoofQrcode(action.getNoofQrcode());
				ealrequestmap.setPackagingSize(action.getPackagingSize());
				ealrequestmap.setRemarks(action.getRemarks());
				ealrequestmap.setUnmappedType(action.getUnmappedType());
				ealrequestmap.setCodeType(action.getCodeType());
				ealrequestmap.setNoofBarcodereceived(action.getNoofBarcodereceived());
				ealrequestmap.setNoofQrcodereceived(action.getNoofQrcodereceived());
				ealrequestmap.setNoofRollcodereceived(action.getNoofRollcodereceived());
				ealrequestmap.setNoofBarcodepending(action.getNoofBarcodepending());
				ealrequestmap.setNoofqrpending(action.getNoofqrpending());
				ealrequestmap.setTotalnumofBarcode(action.getTotalnumofBarcode());
				ealrequestmap.setTotalnumofQrcode(action.getTotalnumofQrcode());
				ealrequestmap.setTotalnumofRoll(action.getTotalnumofRoll());
				ealrequestmap.setStockApplnno(action.getStockApplnno());
				ealrequestmap.setStockDate(action.getStockDate());
				ealrequestmap.setEalrequestPUtoBWFLId(DeptOptional.get().getEalrequestPUtoBWFLId());
				ealRequestPUtoBWFLMapRepository.save(ealrequestmap);
				finallist1.add(ealrequestmap);

			});
			return Library.getSuccessfulResponse(finallist1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);

		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	public GenericResponse ealavailable(EalRequestDTO requestDTO) {
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
		List<EALRequestMapEntity> stocklistall = new ArrayList<EALRequestMapEntity>();
		placeholderDTO ho = new placeholderDTO();
		ho.setCodeTypeValue(requestDTO.getCodeTypeValue());
		ho.setFromDate(requestDTO.getFromDate());
		ho.setToDate(requestDTO.getToDate());
		ho.setLicenseNumber(requestDTO.getLicenseNumber());
		HttpHeaders headers = new HttpHeaders();
		ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
		ealwastagedto.setEntityCode("brew_admin");
		ealwastagedto.setPlaceholderKeyValueMap(ho);
		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;
		JsonNode actualObj = null;
		String response = null;
		final Date fromDate;
		final Date toDate;
		String fdate = null;
		String tdate = null;
		try {
			fdate = String.valueOf(requestDTO.getStartDate().toString());
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			tdate = String.valueOf(requestDTO.getEndDate().toString());
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<EALAvailable> stockinlist = null;
		List<EALAvailableDTO> ealavailablelist = new ArrayList<EALAvailableDTO>();
		try {

			StringBuffer uri = new StringBuffer(stockurl);
			if (uri != null) {

				String access_token = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", access_token);
				headers.set("Authorization", "Bearer " + token);
				RestTemplate restTemplate = new RestTemplate();
				headers.set("X-Authorization", access_token);
				headers.set("Authorization", "Bearer " + token);
				HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
				response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class).getBody();
				log.info("=======EALStockService catch block============" + response);
				try {
					actualObj = mapper.readTree(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final JsonNode arrNode = actualObj.get("responseContent");
//			       stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate,toDate,requestDTO.getCodeType());
//			       stockinlist.stream().forEach(stockpro->{
//					Integer	 totabarcodestock=stockpro.getTotalnoofbarcode();	
//					Integer	 totalqrcodestock=stockpro.getTotalnooqrrcode();
//					if (arrNode.isArray()) {
//		            for (final JsonNode objNode : arrNode) {
//		            	EALAvailableDTO eal=new EALAvailableDTO();
//		            	String packagesize = objNode.get("packaging_size_value").toString();
//		            	Integer	usedbarcode = objNode.get("used_barcode").intValue();
//		            	Integer	usedqrcode= objNode.get("used_qrcode").intValue();
//		            	Integer	wastagebarcode=objNode.get("wastage_barcode").intValue();
//		            	Integer	wastageqrcode=objNode.get("wastage_qrcode").intValue();
//							eal.setPackagesize(packagesize);
//							eal.setUsedbarcode(totabarcodestock-(usedbarcode+wastagebarcode));
//							eal.setUsedqrcode(totalqrcodestock-(usedqrcode+wastageqrcode));
//							ealavailablelist.add(eal);
//						}
//					}
//			   		
//					});	

				stocklistall = ealrequestmapRepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate,
						toDate, requestDTO.getCodeType(), requestDTO.getEalreqId(), requestDTO.getLicenseNo());
				stocklistall.stream().forEach(stockpro -> {
					String totabarcodestock = stockpro.getNoofBarcode();
					String totalqrcodestock = stockpro.getNoofQrcode();
					Integer barcode = Integer.parseInt(totabarcodestock);
					Integer qrcode = Integer.parseInt(totalqrcodestock);
					char quotes = '"';
					String bottlepackages = quotes + stockpro.getPackagingSize() + quotes;
					if (arrNode.isArray()) {
						for (final JsonNode objNode : arrNode) {
							EALAvailableDTO eal = new EALAvailableDTO();
							String packagesize = objNode.get("packaging_size_value").toString();
							Integer usedbarcode = objNode.get("used_barcode").intValue();
							Integer usedqrcode = objNode.get("used_qrcode").intValue();
							Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
							Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
							if (bottlepackages.equalsIgnoreCase(packagesize)) {
								eal.setPackagesize(stockpro.getPackagingSize());
								eal.setTotbarcode(barcode - (usedbarcode + wastagebarcode));
								eal.setTotqrcode(qrcode - (usedqrcode + wastageqrcode));
								ealavailablelist.add(eal);
							}
						}

					}

				});
			}
		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block============", exception);
		}

		return Library.getSuccessfulResponse(ealavailablelist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

//		public  GenericResponse ealavailable(EalRequestDTO requestDTO) {
//			EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
//			placeholderDTO ho=new placeholderDTO();
//			ho.setCodeTypeValue(requestDTO.getCodeTypeValue());
//			ho.setFromDate(requestDTO.getFromDate());
//			ho.setToDate(requestDTO.getToDate());
//			ho.setLicenseNumber(requestDTO.getLicenseNumber());
//			HttpHeaders headers = new HttpHeaders();
//			ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
//			ealwastagedto.setEntityCode("brew_admin");
//			ealwastagedto.setPlaceholderKeyValueMap(ho);
//			ObjectMapper mapper = new ObjectMapper();
//		    String bearertoken = null;
//	        JsonNode actualObj = null;
//			String response =null;
//			final Date fromDate;
//			final Date toDate;
//			String fdate=null;
//			String tdate=null;
//			try {
//		    fdate=String.valueOf(requestDTO.getStartDate().toString());
//			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
//			} catch (ParseException e) {
//				log.error("error occurred while parsing date : {}", e.getMessage());
//				throw new InvalidDataValidation("Invalid date parameter passed");
//			}
//			try {
//				tdate=String.valueOf(requestDTO.getEndDate().toString());
//				toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
//			} catch (ParseException e) {
//				log.error("error occurred while parsing date : {}", e.getMessage());
//				throw new InvalidDataValidation("Invalid date parameter passed");
//			}
//					
//	   		List<EALAvailable> stockinlist=null;
//			List<EALAvailableDTO> ealavailablelist=new ArrayList<EALAvailableDTO>();
//			try {
//
//				StringBuffer uri = new StringBuffer(stockurl);
//			    if (uri != null) {
//
//					String access_token = headerRequest.getHeader("X-Authorization");
//		            headers.set("X-Authorization", access_token);
//					headers.set("Authorization", "Bearer "+ token);
//					RestTemplate restTemplate = new RestTemplate();
//			   		headers.set("X-Authorization", access_token);   		
//			   		headers.set("Authorization", "Bearer "+ token);   		
//			   		HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
//			   		response = restTemplate.exchange(uri.toString(),HttpMethod.POST, APIRequest, String.class).getBody();
//			   		log.info("=======EALStockService catch block============" + response);
//			   		try {
//			  			actualObj = mapper.readTree(response);
//			  		} catch (JsonProcessingException e) {
//			  			// TODO Auto-generated catch block
//			  			e.printStackTrace();
//			  		} catch (IOException e) {
//			  			// TODO Auto-generated catch block
//			  			e.printStackTrace();
//			  		}
//			   		final JsonNode arrNode = actualObj.get("responseContent");		   		
//			       stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate,toDate,requestDTO.getCodeType());
//				 		 //stockinlist = ealstockrepository.getCountByStatusAndCodeType(requestDTO.getCodeType());
//					stockinlist.stream().forEach(stockpro->{
//					Integer	 totabarcodestock=stockpro.getTotalnoofbarcode();	
//					Integer	 totalqrcodestock=stockpro.getTotalnooqrrcode();
//					if (arrNode.isArray()) {
//		            for (final JsonNode objNode : arrNode) {
//		            	EALAvailableDTO eal=new EALAvailableDTO();
//		            	String packagesize = objNode.get("packaging_size_value").toString();
//		            	Integer	usedbarcode = objNode.get("used_barcode").intValue();
//		            	Integer	usedqrcode= objNode.get("used_qrcode").intValue();
//		            	Integer	wastagebarcode=objNode.get("wastage_barcode").intValue();
//		            	Integer	wastageqrcode=objNode.get("wastage_qrcode").intValue();
//							eal.setPackagesize(packagesize);
//							eal.setUsedbarcode(totabarcodestock-(usedbarcode+wastagebarcode));
//							eal.setUsedqrcode(totalqrcodestock-(usedqrcode+wastageqrcode));
//							ealavailablelist.add(eal);
//						}
//					}
//			   		
//					});	
//
//				}
//			} catch (Exception exception) {
//				// general error
//				log.error("=======EALStockService catch block============", exception);
//			}
//			
//			return Library.getSuccessfulResponse(ealavailablelist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//					ErrorMessages.RECORED_FOUND);
//		}

	public GenericResponse getCodeopenstock() {
		// MenuPrefix prefix = MenuPrefix.getType(GS_CODE);
		Year y = Year.now();
		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		String code = "OPST-" + RandomUtil.getRandomNumber();
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse OpeningEALStocksave(List<EalRequestMapResponseDTO> stock) {
		ArrayList<EALStockinPUEntity> finallist1 = new ArrayList<EALStockinPUEntity>();
		EALStockinPUEntity stockentity2 = new EALStockinPUEntity();
		try {
			stock.stream().forEach(requestDTO -> {
				EALStockinPUEntity stockentity = new EALStockinPUEntity();
				stockentity.setStockApplnno(requestDTO.getStockApplnno());
				stockentity2.setRemarks(requestDTO.getRemarks());
				stockentity2.setStockApplnno(requestDTO.getStockApplnno());
				stockentity.setStockDate(requestDTO.getStockDate());
				stockentity.setTotalnumofBarcode(requestDTO.getTotalnumofBarcode());
				stockentity.setTotalnumofQrcode(requestDTO.getTotalnumofQrcode());
				stockentity.setTotalnumofRoll(requestDTO.getTotalnumofRoll());
				stockentity.setCodeType(requestDTO.getCodeType());
				stockentity.setUnmappedType(requestDTO.getUnmappedType());
				stockentity.setOpenstockApplnno(requestDTO.getOpenstockApplnno());
				stockentity.setLicenseNo(requestDTO.getLicencenumber());
				stockentity.setCartonSize(requestDTO.getCartonSize());
				stockentity.setNoofBarcode(requestDTO.getNoofBarcode());
				stockentity.setNoofQrcode(requestDTO.getNoofQrcode());
				stockentity.setPackagingSize(requestDTO.getPackagingSize());
				stockentity.setRemarks(requestDTO.getRemarks());
				Integer barcodereceived = Integer.valueOf(requestDTO.getNoofBarcode());
				Integer qrcodereceived = Integer.valueOf(requestDTO.getNoofQrcode());
				stockentity.setNoofBarcodereceived(barcodereceived);
				stockentity.setNoofQrcodereceived(qrcodereceived);
				stockentity.setNoofRollcodereceived(requestDTO.getNoofRollcodereceived());
				stockentity.setNoofBarcodepending(requestDTO.getNoofBarcodepending());
				stockentity.setNoofqrpending(requestDTO.getNoofqrpending());
				stockentity.setEalrequestapplno(requestDTO.getEalrequestApplnno());
				stockentity.setFlag(requestDTO.isOpenStockFlag());
				stockentity.setNoofBarcodedamaged(requestDTO.getNoofBarcodedamaged());
				stockentity.setNoofQrcodedamaged(requestDTO.getNoofQrcodedamaged());
				stockentity.setPrintingType(requestDTO.getPrintingType());
				// stockentity.setCreatedBy(requestDTO.getCreatedBy());
				stockentity.setEntityAddress(requestDTO.getEntityAddress());
				stockentity.setEntityName(requestDTO.getEntityName());
				stockentity.setEntityType(requestDTO.getEntityType());
				stockentity.setLicenseType(requestDTO.getLicenseType());
				ealstockrepository.save(stockentity);
				finallist1.add(stockentity);

				try {
					EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
					ealrequestlog.setComments("Openstack successfully");
					ealrequestlog.setApplnNo(stockentity2.getStockApplnno());
					ealrequestlog.setAction(stockentity2.getRemarks());
					ealrequestlogRepository.save(ealrequestlog);
				} catch (Exception e) {
					log.info("Open Stock LOG::::" + e);
				}

			});
			return Library.getSuccessfulResponse(finallist1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}

	}

	public GenericResponse getystockApplicationNo(String applicationNo) {
		List<EALStockinPUEntity> ealoptional = ealStockInPURepository.getByStockApplnno(applicationNo);
		List<EalRequestMapResponseDTO> stockdetails = new ArrayList<EalRequestMapResponseDTO>();
		EalRequestMapResponseDTO gh = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(tak -> {
			gh.setEalrequestApplnno(tak.getEalrequestapplno());
			gh.setCodeType(tak.getCodeType());
		});
		List<BarQrBalanceDTO> balancedetails = ealStockInPURepository.getByStockAppln(gh.getEalrequestApplnno());
//		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository
//				.findByRequestedapplnNo(gh.getEalrequestApplnno());
		if (gh.getCodeType().equalsIgnoreCase("MAPPED")) {
			ealoptional.stream().forEach(stock -> {
				EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
				DTO.setTotalnumofBarcode(stock.getTotalnumofBarcode());
				DTO.setTotalnumofQrcode(stock.getTotalnumofQrcode());
				DTO.setTotalnumofRoll(stock.getTotalnumofRoll());
				DTO.setCodeType(stock.getCodeType());
				DTO.setStockApplnno(stock.getStockApplnno());
				DTO.setStockDate(stock.getStockDate());
				DTO.setUnmappedType(stock.getUnmappedType());
				DTO.setLicencenumber(stock.getLicenseNo());
				DTO.setNoofBarcode(stock.getNoofBarcode());
				DTO.setNoofQrcode(stock.getNoofQrcode());
				DTO.setRemarks(stock.getRemarks());
				// DTO.setNoofBarcodepending(stock.getNoofBarcodepending());
				DTO.setNoofBarcodereceived(stock.getNoofBarcodereceived());
				DTO.setNoofQrcodereceived(stock.getNoofQrcodereceived());
				DTO.setNoofRollcodereceived(stock.getNoofRollcodereceived());
				// DTO.setNoofqrpending(stock.getNoofqrpending());
				DTO.setEalrequestApplnno(stock.getEalrequestapplno());
				DTO.setNoofBarcodedamaged(stock.getNoofBarcodedamaged());
				DTO.setNoofQrcodedamaged(stock.getNoofQrcodedamaged());
				DTO.setEalrequestApplnno(stock.getEalrequestapplno());
				DTO.setPackagingSize(stock.getPackagingSize());
				DTO.setCartonSize(stock.getCartonSize());
				DTO.setCreatedDate(String.valueOf(stock.getCreatedDate()));
				DTO.setPrintingType(stock.getPrintingType());
				DTO.setEntityName(stock.getEntityName());
				DTO.setEntityAddress(stock.getEntityAddress());
				DTO.setEntityType(stock.getEntityType());
				DTO.setLicenseType(stock.getLicenseType());
				DTO.setOpenStockFlag(stock.isFlag());
				DTO.setMapType(stock.getMapType());
				DTO.setNoofRollCode((stock.getNoofRollCode()));
				Integer nobarcode = Integer.valueOf(stock.getNoofBarcode());
				Integer noqrcode = Integer.valueOf(stock.getNoofQrcode());
				balancedetails.stream().forEach(stockbalance -> {
					EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
					receivedetails.setPackagingSize(stockbalance.getPackagingSize());
					receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
					receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
					if (Objects.nonNull(DTO.getPackagingSize())) {
						if (DTO.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
							Integer obj1 = new Integer(nobarcode);
							Integer obj2 = new Integer(0);
							Integer obj3 = new Integer(noqrcode);
							if (obj1.equals(obj2)) {
								DTO.setNoofBarcodepending(0);
							} else {
								DTO.setNoofBarcodepending(nobarcode - (receivedetails.getNoofBarcodereceived()));
							}
							if (obj3.equals(obj2)) {
								DTO.setNoofqrpending(0);
							} else {
								DTO.setNoofqrpending(noqrcode - (receivedetails.getNoofQrcodereceived()));
							}

							if (stockbalance.getNoofBarcodedamaged() == null) {

							} else {
//									DTO.setActualbarcode(receivedetails.getNoofBarcodereceived()
//											- stockbalance.getNoofBarcodedamaged());
								DTO.setActualbarcode(stock.getNoofBarcodereceived() - stock.getNoofBarcodedamaged());

							}

							if (stockbalance.getNoofQrcodedamaged() == null) {

							} else {
//									DTO.setActualqrcode(receivedetails.getNoofQrcodereceived()
//											- stockbalance.getNoofQrcodedamaged());

								DTO.setActualqrcode(stock.getNoofQrcodereceived() - stock.getNoofQrcodedamaged());
							}
						}
					}

				});
				stockdetails.add(DTO);

			});

		}

		else {
			ealoptional.stream().forEach(stock -> {
				EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
				DTO.setTotalnumofBarcode(stock.getTotalnumofBarcode());
				DTO.setTotalnumofQrcode(stock.getTotalnumofQrcode());
				DTO.setTotalnumofRoll(stock.getTotalnumofRoll());
				DTO.setCodeType(stock.getCodeType());
				DTO.setStockApplnno(stock.getStockApplnno());
				DTO.setStockDate(stock.getStockDate());
				DTO.setUnmappedType(stock.getUnmappedType());
				DTO.setLicencenumber(stock.getLicenseNo());
				DTO.setNoofBarcode(stock.getNoofBarcode());
				DTO.setNoofQrcode(stock.getNoofQrcode());
				DTO.setNoofRollCode(stock.getNoofRollCode());
				DTO.setRemarks(stock.getRemarks());
				// DTO.setNoofBarcodepending(stock.getNoofBarcodepending());
				DTO.setNoofBarcodereceived(stock.getNoofBarcodereceived());
				DTO.setNoofQrcodereceived(stock.getNoofQrcodereceived());
				DTO.setNoofRollcodereceived(stock.getNoofRollcodereceived());
				// DTO.setNoofqrpending(stock.getNoofqrpending());
				DTO.setEalrequestApplnno(stock.getEalrequestapplno());
				DTO.setNoofBarcodedamaged(stock.getNoofBarcodedamaged());
				DTO.setNoofQrcodedamaged(stock.getNoofQrcodedamaged());
				DTO.setEalrequestApplnno(stock.getEalrequestapplno());
				DTO.setPackagingSize(stock.getPackagingSize());
				DTO.setCartonSize(stock.getCartonSize());
				DTO.setCreatedDate(String.valueOf(stock.getCreatedDate()));
				DTO.setPrintingType(stock.getPrintingType());
				DTO.setEntityName(stock.getEntityName());
				DTO.setEntityAddress(stock.getEntityAddress());
				DTO.setEntityType(stock.getEntityType());
				DTO.setLicenseType(stock.getLicenseType());
				DTO.setOpenStockFlag(stock.isFlag());
				DTO.setMapType(stock.getMapType());
				Integer nobarcode = Integer.valueOf(stock.getNoofBarcode());
				Integer noqrcode = Integer.valueOf(stock.getNoofQrcode());
				List<BarQrBalanceDTO> balancedetailsunmapped = ealStockInPURepository
						.getByStockApplnAndUnmappedTypeAndPrintingType(DTO.getEalrequestApplnno(),
								DTO.getUnmappedType(), DTO.getPrintingType());
				balancedetailsunmapped.stream().forEach(stockbalance -> {
					EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
					receivedetails.setPackagingSize(stockbalance.getPackagingSize());
					receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
					receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
					Integer obj1 = new Integer(nobarcode);
					Integer obj2 = new Integer(0);
					Integer obj3 = new Integer(noqrcode);
					Integer obj4 = new Integer(receivedetails.getNoofBarcodereceived());
					Integer obj5 = new Integer(receivedetails.getNoofQrcodereceived());
					if (obj1.equals(obj4)) {
						DTO.setNoofBarcodepending(0);
					} else {
						DTO.setNoofBarcodepending(nobarcode - (receivedetails.getNoofBarcodereceived()));
					}

					if (obj3.equals(obj5)) {
						DTO.setNoofqrpending(0);
					} else {
						DTO.setNoofqrpending(noqrcode - (receivedetails.getNoofQrcodereceived()));
					}

					if (stockbalance.getNoofBarcodedamaged() == null) {

					} else {
						// DTO.setActualbarcode(
						// receivedetails.getNoofBarcodereceived() -
						// stockbalance.getNoofBarcodedamaged());
						DTO.setActualbarcode(stock.getNoofBarcodereceived() - stock.getNoofBarcodedamaged());

					}

					if (stockbalance.getNoofQrcodedamaged() == null) {

					} else {
//							DTO.setActualqrcode(
//									receivedetails.getNoofQrcodereceived() - stockbalance.getNoofQrcodedamaged());
//						
						DTO.setActualqrcode(stock.getNoofQrcodereceived() - stock.getNoofQrcodedamaged());

					}

				});

				stockdetails.add(DTO);

			});
		}
		if (ealoptional.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(stockdetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getystockavailableApplicationNo(String applicationNo) {

		List<EALStockinPUEntity> ealoptional = ealStockInPURepository.getByEALAPPln(applicationNo);

		List<EalRequestMapResponseDTO> stockdetails = new ArrayList<EalRequestMapResponseDTO>();

		EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(ealstock -> {
			DTO.setTotalnumofBarcode(ealstock.getTotalnumofBarcode());
			DTO.setTotalnumofQrcode(ealstock.getTotalnumofQrcode());
			DTO.setEalrequestApplnno(ealstock.getEalrequestapplno());
			DTO.setStockApplnno(ealstock.getStockApplnno());
			DTO.setCodeType(ealstock.getCodeType());
		});

		Optional<EALRequestPUtoBWFLEntity> ealrequestEntity = ealRequestPUtoBWFLRepository.findByRequestedapplnNo(applicationNo);

		String codetype = ealrequestEntity.get().getCodeType();

		// List<EALStockinPUEntity> ealoptionalstocklist
		// =ealstockrepository.getByStockApplnno(DTO.getStockApplnno());

		List<EALStockinPUEntity> ealoptionalstocklist = null;
		if (codetype.equalsIgnoreCase("MAPPED")) {
			ealoptionalstocklist = ealStockInPURepository.getByEalrequestapplno(DTO.getEalrequestApplnno());
		} else {
			ealoptionalstocklist = ealStockInPURepository.getByEalrequestapplnounmapped(DTO.getEalrequestApplnno());
		}

		List<BarQrBalanceDTO> balancedetails = ealStockInPURepository.getByStockAppln(DTO.getEalrequestApplnno());

		if (codetype.equalsIgnoreCase("MAPPED")) {

			ealoptionalstocklist.stream().forEach(maplist -> {
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
				obj.setNoofRoll(maplist.getTotalnumofRoll());

				obj.setNoofRollcodereceived(balancedetails.get(0).getNoofRollcodereceived());

				obj.setNoofqrpending(maplist.getNoofqrpending());
				obj.setStockApplnno(maplist.getStockApplnno());
				obj.setStockDate(maplist.getStockDate());
				obj.setLicencenumber(maplist.getLicenseNo());
				obj.setFlag(maplist.isFlag());
				obj.setTotalnumofBarcode(maplist.getTotalnumofBarcode());
				obj.setTotalnumofQrcode(maplist.getTotalnumofQrcode());
				obj.setTotalnumofRoll(maplist.getTotalnumofRoll());
				obj.setEalrequestApplnno(maplist.getEalrequestapplno());
				obj.setMapType(maplist.getMapType());
				obj.setPrintingType(ealrequestEntity.get().getPrintingType());
				Integer nobarcode = Integer.valueOf(obj.getNoofBarcode());
				Integer noqrcode = Integer.valueOf(obj.getNoofQrcode());
				Integer totalnumofroll = Integer.valueOf(obj.getTotalnumofRoll());

				balancedetails.stream().forEach(stockbalance -> {
					EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
					receivedetails.setPackagingSize(stockbalance.getPackagingSize());
					receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
					receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
					receivedetails.setNoofRollcodereceived(stockbalance.getNoofRollcodereceived());
					if (Objects.nonNull(obj.getPackagingSize())) {
						if (obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
							// obj.setNoofBarcodeBalance(nobarcode-(receivedetails.getNoofBarcodereceived()));
							// obj.setNoofQrcodeBalance(noqrcode-(receivedetails.getNoofQrcodereceived()));

							Integer obj1 = new Integer(nobarcode);
							Integer obj2 = new Integer(0);
							Integer obj3 = new Integer(noqrcode);

							if (obj1.equals(obj2)) {
								obj.setNoofBarcodeBalance(0);
							} else {
								obj.setNoofBarcodeBalance(nobarcode - (receivedetails.getNoofBarcodereceived()));
							}
							if (obj3.equals(obj2)) {
								obj.setNoofQrcodeBalance(0);
							} else {
								obj.setNoofQrcodeBalance(noqrcode - (receivedetails.getNoofQrcodereceived()));
							}

							obj.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
							obj.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
							// obj.setNoofRollcodereceived(receivedetails.getNoofQrcodereceived());
							// obj.setActualbarcode(receivedetails.getNoofBarcodereceived()-stockbalance.getNoofBarcodedamaged());
							// obj.setActualqrcode(receivedetails.getNoofQrcodereceived()-stockbalance.getNoofQrcodedamaged());
							obj.setNoofBarcodedamaged(stockbalance.getNoofBarcodedamaged());
							obj.setNoofQrcodedamaged(stockbalance.getNoofQrcodedamaged());
							obj.setNoofRollBalance(totalnumofroll - (receivedetails.getNoofRollcodereceived()));

							if (stockbalance.getNoofBarcodedamaged() == null) {

							} else {
								obj.setActualbarcode(
										receivedetails.getNoofBarcodereceived() - stockbalance.getNoofBarcodedamaged());
							}

							if (stockbalance.getNoofQrcodedamaged() == null) {

							} else {
								obj.setActualqrcode(
										receivedetails.getNoofQrcodereceived() - stockbalance.getNoofQrcodedamaged());
							}
						}
					}

				});
				stockdetails.add(obj);

			});

		}
		// unmapped
		else {

			ealoptionalstocklist.stream().forEach(maplist -> {
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
				obj.setNoofRollcodereceived(maplist.getNoofRollcodereceived());
				obj.setNoofqrpending(maplist.getNoofqrpending());
				obj.setStockApplnno(maplist.getStockApplnno());
				obj.setStockDate(maplist.getStockDate());
				obj.setLicencenumber(maplist.getLicenseNo());
				obj.setFlag(maplist.isFlag());
				obj.setTotalnumofBarcode(maplist.getTotalnumofBarcode());
				obj.setTotalnumofQrcode(maplist.getTotalnumofQrcode());
				obj.setEalrequestApplnno(maplist.getEalrequestapplno());
				obj.setPrintingType(maplist.getPrintingType());
				obj.setTotalnumofRoll(maplist.getTotalnumofRoll());
				obj.setMapType(maplist.getMapType());
				Integer nobarcode = Integer.valueOf(obj.getNoofBarcode());
				Integer noqrcode = Integer.valueOf(obj.getNoofQrcode());
				Integer totnobarcode = Integer.valueOf(obj.getTotalnumofBarcode());
				Integer totnoqrcode = Integer.valueOf(obj.getTotalnumofQrcode());
				Integer totalnumofroll = Integer.valueOf(obj.getTotalnumofRoll());

				List<BarQrBalanceDTO> balancedetailsunmapped = ealStockInPURepository
						.getByStockApplnAndUnmappedTypeAndPrintingType(DTO.getEalrequestApplnno(),
								maplist.getUnmappedType(), maplist.getPrintingType());

				balancedetailsunmapped.stream().forEach(stockbalance -> {
					EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
					receivedetails.setPackagingSize(stockbalance.getPackagingSize());
					receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
					receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
					receivedetails.setNoofRollcodereceived(stockbalance.getNoofRollcodereceived());

					Integer obj1 = new Integer(nobarcode);
					Integer obj2 = new Integer(0);
					Integer obj3 = new Integer(noqrcode);

					Integer obj4 = new Integer(receivedetails.getNoofBarcodereceived());
					Integer obj5 = new Integer(receivedetails.getNoofQrcodereceived());

					if (obj1.equals(obj4)) {
						obj.setNoofBarcodeBalance(0);
					} else {
						obj.setNoofBarcodeBalance(nobarcode - (receivedetails.getNoofBarcodereceived()));
					}

					if (obj3.equals(obj5)) {
						obj.setNoofQrcodeBalance(0);
					} else {
						obj.setNoofQrcodeBalance(noqrcode - (receivedetails.getNoofQrcodereceived()));
					}
					obj.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
					obj.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
					obj.setNoofBarcodedamaged(stockbalance.getNoofBarcodedamaged());
					obj.setNoofQrcodedamaged(stockbalance.getNoofQrcodedamaged());
					obj.setNoofRollBalance(totalnumofroll - (receivedetails.getNoofRollcodereceived()));
					if (stockbalance.getNoofBarcodedamaged() == null) {

					} else {
						obj.setActualbarcode(
								receivedetails.getNoofBarcodereceived() - stockbalance.getNoofBarcodedamaged());
					}

					if (stockbalance.getNoofQrcodedamaged() == null) {

					} else {
						obj.setActualqrcode(
								receivedetails.getNoofQrcodereceived() - stockbalance.getNoofQrcodedamaged());
					}

				});
				stockdetails.add(obj);

			});

		}

		if (ealoptional.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(stockdetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getByealApplicationNo(String ealrequestApplnno) {
		List<EALRequestPUtoBWFLEntity> ealoptional = ealRequestPUtoBWFLRepository.getByRequestedapplnNo(ealrequestApplnno);
		if (ealoptional.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealoptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getAllByapproved() {
		List<EALRequestEntity> ealEntityList = ealrequestRepository.findByStatusOrderByIdDesc();
		if (ealEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(ealEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

	public GenericResponse getsubPagesearchNewByFilterpre(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALRequestEntity> list = this.getSubRecordsByFilterDTO1pre(requestData);
		List<EALRequestEntity> list1 = this.getSubRecordsByFilterDTO2pre(requestData);
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

	public List<EALRequestEntity> getSubRecordsByFilterDTO1pre(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestEntity> cq = cb.createQuery(EALRequestEntity.class);
		Root<EALRequestEntity> from = cq.from(EALRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestEntity> typedQuery = null;
		addSubCriteriapre(cb, list, filterRequestDTO, from);
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

	public List<EALRequestEntity> getSubRecordsByFilterDTO2pre(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALRequestEntity> cq = cb.createQuery(EALRequestEntity.class);
		Root<EALRequestEntity> from = cq.from(EALRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALRequestEntity> typedQuery1 = null;
		addSubCriteriapre(cb, list, filterRequestDTO, from);
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

	private void addSubCriteriapre(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<EALRequestEntity> from) {

		Date fromDate = null;

		Date toDate = null;

		String fdate = null;

		String tdate = null;
		// VendorStatus vendorStatus=null;

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

			String licno = filterRequestDTO.getFilters().get("licenseNo").toString();
			list.add(cb.equal(from.get("licenseNo"), licno));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("codeType"))
				&& !filterRequestDTO.getFilters().get("codeType").toString().trim().isEmpty()) {

			String codetype = filterRequestDTO.getFilters().get("codeType").toString();
			list.add(cb.equal(from.get("codeType"), codetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseType"))
				&& !filterRequestDTO.getFilters().get("licenseType").toString().trim().isEmpty()) {

			String licencetype = (filterRequestDTO.getFilters().get("licenseType").toString());
			list.add(cb.equal(from.get("licenseType"), licencetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("requestedAppInNo"))
				&& !filterRequestDTO.getFilters().get("requestedAppInNo").toString().trim().isEmpty()) {

			String requestedapplnNo = (filterRequestDTO.getFilters().get("requestedAppInNo").toString());
			list.add(cb.equal(from.get("requestedapplnNo"), requestedapplnNo));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("district"))
				&& !filterRequestDTO.getFilters().get("district").toString().trim().isEmpty()) {

			String district = (filterRequestDTO.getFilters().get("district").toString());
			list.add(cb.equal(from.get("district"), district));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseType"))
				&& !filterRequestDTO.getFilters().get("licenseType").toString().trim().isEmpty()) {

			String licenseType = (filterRequestDTO.getFilters().get("licenseType").toString());
			list.add(cb.equal(from.get("licenseType"), licenseType));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("entityType"))
				&& !filterRequestDTO.getFilters().get("entityType").toString().trim().isEmpty()) {

			String entityType = (filterRequestDTO.getFilters().get("entityType").toString());
			list.add(cb.equal(from.get("entityType"), entityType));
		}

//				if (Objects.nonNull(filterRequestDTO.getFilters().get("vendor_status"))
//						&& !filterRequestDTO.getFilters().get("vendor_status").toString().trim().isEmpty()) {
//
//					String vendor_status = (filterRequestDTO.getFilters().get("vendor_status").toString());
//					list.add(cb.equal(from.get("vendorStatus"), vendor_status));
//				}

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

		if (Objects.nonNull(filterRequestDTO.getFilters().get("vendorStatus"))
				&& !filterRequestDTO.getFilters().get("vendorStatus").toString().trim().isEmpty()) {
			VendorStatus vendorStatus = null;
			if (filterRequestDTO.getFilters().get("vendorStatus").toString().equals(VendorStatus.DISPATCHED.name())) {
				vendorStatus = VendorStatus.DISPATCHED;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.ACCEPTED.name())) {
				vendorStatus = VendorStatus.ACCEPTED;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.REQUESTFORCLARIFICATION.name())) {
				vendorStatus = VendorStatus.REQUESTFORCLARIFICATION;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.ACKNOWLEDGED.name())) {
				vendorStatus = VendorStatus.ACKNOWLEDGED;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.REJECTED.name())) {
				vendorStatus = VendorStatus.REJECTED;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.FORCECLOSURE.name())) {
				vendorStatus = VendorStatus.FORCECLOSURE;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.REQUESTED.name())) {
				vendorStatus = VendorStatus.REQUESTED;
			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
					.equals(VendorStatus.CANCELLED.name())) {
				vendorStatus = VendorStatus.CANCELLED;
			}

			list.add(cb.equal(from.get("vendorStatus"), vendorStatus));
		}

	}

	public GenericResponse currentlyworkwithsave(EalRequestDTO requestDTO) {
		List<EALRequestEntity> ealDetails = null;
		try {
			ealDetails = ealrequestRepository.getBycurrentlyWorkwithAndStatus(requestDTO.getCurrentlyWorkwith());
			if (CollectionUtils.isEmpty(ealDetails)) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("cuurrently work with based licence number" + e);
		}

		return Library.getSuccessfulResponse(ealDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse forceclosureupdate(EalRequestDTO requestDTO) {
		try {
			ealrequestRepository.updateForceclosure(requestDTO.isForceclosureFlag(), requestDTO.getRequestedapplnNo());
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
		return Library.getSuccessfulResponse("", ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				"Force Closure Updated Successfully");
	}

	public GenericResponse updatevendorstatus(EalRequestDTO requestDTO) {
		try {
			Optional<EALRequestEntity> ealDetails = ealrequestRepository
					.findByRequestedapplnNo(requestDTO.getRequestedapplnNo());
			if (!ealDetails.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record not exist");
			} else {
				EALRequestEntity ealvendor = ealDetails.get();
				ealvendor.setVendorStatus(requestDTO.getVendorStatus());
				ealvendor.setRemarks(requestDTO.getRemarks());
				ealrequestRepository.save(ealvendor);

				return Library.getSuccessfulResponse(ealvendor, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_UPDATED);
			}

		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	public GenericResponse getCount(String fromDate, String toDate) {
		try {
			List<EalRequestSummaryCountDTO> entitysummary = ealrequestRepository.getCount(fromDate, toDate);
			if (entitysummary.isEmpty()) {
				throw new RecordNotFoundException("No record found");
			}

			else {
				return Library.getSuccessfulResponse(entitysummary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving summary count.", e);
		}
	}

	public List<EalDispatchedDetailsDTO> getDiapatchedDetails(String applicationNo) {
		List<BwflDispatchRequestEntity> ealDispatchedDetails = bwflDispatchRequestRepository.getEalDispatchedDetailsByApplno(applicationNo);
		List<EalDispatchedDetailsDTO> ealDispatchedDetailsDTOs = new ArrayList<>();
		ealDispatchedDetails.stream().forEach(entity -> {
			EalDispatchedDetailsDTO dto = new EalDispatchedDetailsDTO();
			dto.setTpDate(entity.getTpDate());
			dto.setTpApplnno(entity.getTpApplnno());
			dto.setPackagingSize(entity.getPackagingSize());
			dto.setCartonSize(entity.getCartonSize());
			dto.setNoOfBarcode(entity.getNoofBarcode());
			dto.setNoOfQrcode(entity.getNoofQrcode());
			dto.setEalRequestApplnno(entity.getEalrequestapplno());
			dto.setNoOfRoll(entity.getNoOfRoll());
			dto.setCreatedBy(entity.getCreatedBy());
			dto.setCreatedDate(String.valueOf(entity.getCreatedDate()));
			dto.setTotalnumofBarcode(entity.getTotalnumofBarcode());
			dto.setTotalnumofQrcode(entity.getTotalnumofQrcode());
			dto.setTotalnumofRoll(entity.getTotalnumofRoll());
			dto.setCodeType(entity.getCodeType());
			dto.setTpApplnno(entity.getTpApplnno());
			dto.setEalrequestPutoBwflId(entity.getEalrequestId());
			dto.setUnmappedType(entity.getUnmappedType());
			dto.setModifiedDate(String.valueOf(entity.getModifiedDate()));
			dto.setModifiedBy(entity.getModifiedBy());
			dto.setOpenstockApplnno(entity.getOpenstockApplnno());
			dto.setFlag(entity.isFlag());
			dto.setLicencenumber(entity.getLicenseNo());
			dto.setRemarks(entity.getRemarks());
			dto.setNoofBarcodepending(entity.getNoofBarcodepending());
			dto.setNoofBarcodereceived(entity.getNoofBarcodereceived());
			dto.setNoofQrcodereceived(entity.getNoofQrcodereceived());
			dto.setNoOfRollReceived(entity.getNoofRollcodereceived());
			dto.setNoofqrpending(entity.getNoofqrpending());
			dto.setNoofBarcodedamaged(entity.getNoofBarcodedamaged());
			dto.setNoofQrcodedamaged(entity.getNoofQrcodedamaged());
			dto.setPrintingType(entity.getPrintingType());
			dto.setEntityName(entity.getEntityName());
			dto.setEntityAddress(entity.getEntityAddress());
			dto.setLicenseType(entity.getLicenseType());
			dto.setEntityType(entity.getEntityType());
			dto.setVendorStatus(entity.getVendorStatus());
			dto.setMapType(entity.getMapType());
			dto.setUnitCode(entity.getUnitCode());

			ealDispatchedDetailsDTOs.add(dto);
		});

		if (ealDispatchedDetailsDTOs.isEmpty()) {
			throw new RecordNotFoundException("No record found");
		}

		else {
			return ealDispatchedDetailsDTOs;
		}

	}
	public GenericResponse getyApplicationNocreatedByLicenseNo(String applicationNo, Long createdby, String licenseno) {

		Optional<EALRequestPUtoBWFLEntity> ealforceclosure = ealRequestPUtoBWFLRepository.findByRequestedapplnNo(applicationNo);
		if (!ealforceclosure.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		Boolean forceflag = ealforceclosure.get().isForceclosureFlag();
		if (forceflag.equals(true)) {

			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					"This EAL Request Number is Force Closured");
		} else {

			if (applicationNo != null && licenseno != null) {
				Optional<EALRequestPUtoBWFLEntity> ealoptionalQ = ealRequestPUtoBWFLRepository
						.getByRequestedapplnNoAndlicenseNo(applicationNo, licenseno);
				Optional<EALRequestPUtoBWFLEntity> dispatched = ealRequestPUtoBWFLRepository
						.getByRequestedapplnNoAndStatusAckAndLicNo(applicationNo, licenseno);
				if (!dispatched.isPresent()) {
					return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
							"This EAL Request Not Yet dispatched");
				} else {
					return Library.getSuccessfulResponse(ealoptionalQ.get(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
							ErrorMessages.RECORED_FOUND);
				}
			}

			Optional<EALRequestPUtoBWFLEntity> ealoptional = ealRequestPUtoBWFLRepository
					.getByRequestedapplnNoAndStatusAndCreatedby(applicationNo, createdby);

			Optional<EALRequestPUtoBWFLEntity> ealoptionalappl = ealRequestPUtoBWFLRepository.findByRequestedapplnNo(applicationNo);

			Optional<EALRequestPUtoBWFLEntity> ealoptionalreject = ealRequestPUtoBWFLRepository
					.getByRequestedapplnNoAndStatusapAndCreatedby(applicationNo, createdby);

			Optional<EALRequestPUtoBWFLEntity> ealoptionalacknow = ealRequestPUtoBWFLRepository
					.getByRequestedapplnNoAndStatusAckAndCreatedby(applicationNo, createdby);

			if (ealoptionalreject.isPresent()) {
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						"This EAL Request Number is Rejected");
			} else {
				if (!ealoptionalappl.isPresent()) {
					return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
							"This EAL Request Number Not Available");
				} else {
					if (!ealoptional.isPresent()) {
						return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
								"This EAL Request Not Yet Approved OR This User Record Not available");
					} else {
						if (!ealoptionalacknow.isPresent()) {
							return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
									"This EAL Request Not Yet dispatched");
						} else {
							return Library.getSuccessfulResponse(ealoptional.get(),
									ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
						}

					}
				}
			}
		}
	}

}
