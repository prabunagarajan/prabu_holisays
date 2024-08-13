package com.oasys.posasset.service.impl;

import static com.oasys.posasset.constant.Constant.ASC;
import static com.oasys.posasset.constant.Constant.DESC;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
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
import org.springframework.transaction.annotation.Transactional;
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
import com.oasys.helpdesk.dto.AssetMapCountDto;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.UserDto;
import com.oasys.helpdesk.dto.WorkflowDTO;
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.repository.EALAvailable;
import com.oasys.helpdesk.repository.EALDashboard;
import com.oasys.helpdesk.repository.ReceviedBarQr;
import com.oasys.helpdesk.repository.UserRepository;
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
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestDashboardDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.dto.EalRequestSummaryCountDTO;
import com.oasys.posasset.dto.EalRequestVendorDTO;
import com.oasys.posasset.dto.EalStocksearchDTO;
import com.oasys.posasset.dto.placeholderDTO;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestLogEntity;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALStockEntity;
import com.oasys.posasset.entity.TPRequestEntity;
import com.oasys.posasset.mapper.StockOverviewMapper;
import com.oasys.posasset.repository.EALRequestLogRepository;
import com.oasys.posasset.repository.EALRequestMapRepository;
import com.oasys.posasset.repository.EALRequestRepository;
import com.oasys.posasset.repository.EALStockRepository;
import com.oasys.posasset.repository.TpRequestRepository;
import com.oasys.posasset.service.WorkFlowService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EALRequestService {

	@Autowired
	EALRequestRepository ealrequestRepository;

	@Autowired
	EALRequestMapRepository ealrequestmapRepository;

	@Autowired
	EALStockRepository ealstockrepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private StockOverviewMapper ealrequestMapper;

	@Autowired
	EALRequestLogRepository ealrequestlogRepository;

	@Autowired
	TpRequestRepository tprequestRepository;

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
	
	@Autowired
	UserRepository userRepository;

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
				ealrequestEntity.setTotRoll(requestDTO.getTotRoll());
				ealrequestEntity.setDistrict(requestDTO.getDistrict());
				ealrequestEntity.setRemarks(requestDTO.getRemarks());
				ealrequestEntity.setVendorStatus(requestDTO.getVendorStatus());
				ealrequestEntity.setUnitCode(requestDTO.getUnitCode());
				ealrequestEntity.setForceclosureFlag(false);
				ealrequestEntity.setPuEntityAddress(requestDTO.getPuEntityAddress());
				ealrequestEntity.setPuEntityName(requestDTO.getPuEntityName());
				ealrequestEntity.setPuLicenseNo(requestDTO.getPuLicenseNo());
				ealrequestEntity.setPuLicenseType(requestDTO.getPuLicenseType());

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
					ealrequestmap.setStockApplnno(requestDTO.getRequestedapplnNo());
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
						//workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
						workflowStatusUpdateDto.setSubModuleNameCode("REQUEST_AEC_BREWERY");
						
					}
					if (requestDTO.getEntityName().equalsIgnoreCase("DISTILLERY")) {
						//workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTDISTILLERY");
						workflowStatusUpdateDto.setSubModuleNameCode("REQUEST_AEC_DISTILLERY");
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
						//workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
						workflowStatusUpdateDto.setSubModuleNameCode("REQUEST_AEC_BREWERY");
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
		List<EALStockEntity> DepList = ealstockrepository.findAllByOrderByModifiedDateDesc();
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

		List<BarQrBalanceDTO> tpdispatch = new ArrayList<BarQrBalanceDTO>();

		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository.findById(id);

		Integer totabarcode = ealrequestEntity.get().getTotBarcode();

		Integer totalqrcode = ealrequestEntity.get().getTotQrcode();

		Integer totRoll = ealrequestEntity.get().getTotRoll();

		String stockappln = ealrequestEntity.get().getRequestedapplnNo();

//		List<BarQrBalanceDTO> balancedetails = ealstockrepository.getByStockAppln(stockappln);

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
			
			List<BarQrBalanceDTO> balancedetails;
			if (obj.getCodeType().equals("MAPPED")) {
				balancedetails = ealstockrepository.getByStockApplnMap(stockappln);
			}else {
				balancedetails = ealstockrepository.getByStockApplnUnMap(stockappln);
			}
			
			balancedetails.stream().forEach(stockbalance -> {
				EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
				receivedetails.setPackagingSize(stockbalance.getPackagingSize());
				receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
				receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
				receivedetails.setNoofRollcodereceived(stockbalance.getNoofRollcodereceived());
				if (obj.getCodeType().equals("MAPPED")) {
					if (obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
						obj.setNoofBarcodeBalance(
								Integer.parseInt(maplist.getNoofBarcode()) - (receivedetails.getNoofBarcodereceived()));
						obj.setNoofQrcodeBalance(
								Integer.parseInt(maplist.getNoofQrcode()) - (receivedetails.getNoofQrcodereceived()));
						obj.setNoofRollBalance(maplist.getNoofRoll() - (receivedetails.getNoofRollcodereceived()));

					}
				} else {
					obj.setNoofBarcodeBalance(totabarcode - (receivedetails.getNoofBarcodereceived()));
					log.info("======================totabarcode" + totabarcode);
					log.info("======================getNoofBarcodereceived" + receivedetails.getNoofBarcodereceived());
					obj.setNoofQrcodeBalance(totalqrcode - (receivedetails.getNoofQrcodereceived()));
					log.info("======================totalqrcode" + totalqrcode);
					log.info("======================getNoofQrcodereceived" + receivedetails.getNoofQrcodereceived());
//					obj.setNoofRollBalance(totRoll - (receivedetails.getNoofRollcodereceived()));
				}

			});

			if (obj.getCodeType().equals("MAPPED")) {
				List<BarQrBalanceDTO> tpdispatch1 = tprepository.getByEalreqid(id);
				tpdispatch.addAll(tpdispatch1);
			} else {
				List<BarQrBalanceDTO> tpdispatch2 = tprepository.getByEalreqid(id);
				tpdispatch.addAll(tpdispatch2);
			}

			tpdispatch.stream().forEach(dispatch -> {
				if (obj.getCodeType().equals("MAPPED")) {
					if (obj.getPackagingSize().equalsIgnoreCase(dispatch.getPackagingSize())) {
						obj.setDispatchnoofBarcodereceived(dispatch.getDispatchNoofBarcodereceived());
						obj.setDispatchnoofQrcodereceived(dispatch.getDispatchNoofQrcodereceived());
						obj.setDispatchnoofRollcodereceived(dispatch.getDispatchNoofRollcodereceived());
					}
				} else {
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
				//workflowStatusUpdateDto.setSubModuleNameCode("EALREQUESTAEC");
				workflowStatusUpdateDto.setSubModuleNameCode("REQUEST_AEC_BREWERY");
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
		List<EALRequestLogEntity> ealLogEntity = ealrequestlogRepository.findByApplnNoOrderByIdDesc(applicationNo);
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
		if (requestDTO.getUnitCodeArray() == null || requestDTO.getUnitCodeArray().isEmpty()) {
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
		} else if (unitCodeArray != null && licArray == null) {
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
////		Optional<EALStockEntity> ealDetails=ealstockrepository
////				.findByStockApplnno(requestDTO.getStockApplnno());
////		 if(!ealDetails.isPresent()) {
//		EALStockEntity stockentity=new EALStockEntity();
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

	public GenericResponse EALStocksave(List<EalRequestMapResponseDTO> stock) {
		ArrayList<EALStockEntity> finallist1 = new ArrayList<EALStockEntity>();
		EALStockEntity stockentity2 = new EALStockEntity();
		try {

			// Validate stock details before processing
			for (EalRequestMapResponseDTO requestDTO : stock) {
				List<EALStockEntity> stockdetails = ealstockrepository.getbyTpApplnno(requestDTO.getTpApplnNo());
				if (!stockdetails.isEmpty()) {
					return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
							"This TP Number is Already Stock In Completed");
				}
			}
			stock.stream().forEach(requestDTO -> {
				List<EALStockEntity> stockdetails = ealstockrepository.getbyTpApplnno(requestDTO.getTpApplnNo());
				EALStockEntity stockentity = new EALStockEntity();
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
				stockentity.setTpApplnNo(requestDTO.getTpApplnNo());
				stockentity.setCreatedBy(requestDTO.getCreatedBy());
				ealstockrepository.save(stockentity);
				finallist1.add(stockentity);
			});

			try {
				EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
				ealrequestlog.setComments("STIN Saved Successfully");
				ealrequestlog.setApplnNo(stockentity2.getEalrequestapplno());
				ealrequestlog.setAction(stockentity2.getRemarks());
				ealrequestlogRepository.save(ealrequestlog);
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
		List<EALRequestEntity> ealrequstlist = new ArrayList<EALRequestEntity>();

		List<EALRequestEntity> ealrequestEntity = ealrequestRepository.getById(id);

		ealrequstlist.addAll(ealrequestEntity);

		if (!ealrequestEntity.isEmpty()) {
			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			EALRequestEntity ealrequestEntity1 = new EALRequestEntity();
			Long ealrequestid;
			DeptOptional.stream().forEach(action -> {
				ealrequestEntity1.setEalrequestId(action.getEalrequestId().getId());
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
		List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);
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
		List<EALRequestMapEntity> deviceReturnEntityList = ealrequestmapRepository.findByCreatedByOrderByIdDesc(userId);
		if (deviceReturnEntityList.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		} else {
			return Library.getSuccessfulResponse(deviceReturnEntityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}

//	 public GenericResponse getyApplicationNocreatedBy(String applicationNo, Long
//	 createdby) {
	public GenericResponse getyApplicationNocreatedByLicenseNo(String applicationNo, Long createdby, String licenseno) {

		Optional<EALRequestEntity> ealforceclosure = ealrequestRepository.findByRequestedapplnNo(applicationNo);
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
				Optional<EALRequestEntity> ealoptionalQ = ealrequestRepository
						.getByRequestedapplnNoAndlicenseNo(applicationNo, licenseno);
				Optional<EALRequestEntity> dispatched = ealrequestRepository
						.getByRequestedapplnNoAndStatusAckAndLicNo(applicationNo, licenseno);
				if (!dispatched.isPresent()) {
					return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
							"This EAL Request Not Yet dispatched");
				} else {
					return Library.getSuccessfulResponse(ealoptionalQ.get(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
							ErrorMessages.RECORED_FOUND);
				}
			}

			Optional<EALRequestEntity> ealoptional = ealrequestRepository
					.getByRequestedapplnNoAndStatusAndCreatedby(applicationNo, createdby);

			Optional<EALRequestEntity> ealoptionalappl = ealrequestRepository.findByRequestedapplnNo(applicationNo);

			Optional<EALRequestEntity> ealoptionalreject = ealrequestRepository
					.getByRequestedapplnNoAndStatusapAndCreatedby(applicationNo, createdby);

			Optional<EALRequestEntity> ealoptionalacknow = ealrequestRepository
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
		List<EALStockEntity> list = this.getSubRecordsByFilterDTO1stock(requestData);
//		List<EALStockEntity> unique = list.stream()
//                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(EALRequestMapEntity::getTotalnumofBarcode))),
//                                           ArrayList::new));
		List<EALStockEntity> list1 = this.getSubRecordsByFilterDTO2stock(requestData);

		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
		if (!list.isEmpty()) {

			List<ReceviedBarQr> rece = ealstockrepository.getByStockreceived();
			List<EalStocksearchDTO> setlist = new ArrayList<EalStocksearchDTO>();
			list.stream().forEach(actuallist -> {
				EalStocksearchDTO obj = new EalStocksearchDTO();
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

	public List<EALStockEntity> getSubRecordsByFilterDTO1stock(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALStockEntity> cq = cb.createQuery(EALStockEntity.class);
		Root<EALStockEntity> from = cq.from(EALStockEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALStockEntity> typedQuery = null;
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

	public List<EALStockEntity> getSubRecordsByFilterDTO2stock(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALStockEntity> cq = cb.createQuery(EALStockEntity.class);
		Root<EALStockEntity> from = cq.from(EALStockEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALStockEntity> typedQuery1 = null;
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
			Root<EALStockEntity> from) {

		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockApplnno"))
				&& !filterRequestDTO.getFilters().get("stockApplnno").toString().trim().isEmpty()) {

			String stockapplnno = filterRequestDTO.getFilters().get("stockApplnno").toString();
			list.add(cb.equal(from.get("stockApplnno"), stockapplnno));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockDate"))
				&& !filterRequestDTO.getFilters().get("stockDate").toString().trim().isEmpty()) {

			try {
				String dateStr = filterRequestDTO.getFilters().get("stockDate").toString().trim();

				// Constructing a pattern to match against the date string
				String pattern = "%" + dateStr + "%";

				// Adding a 'LIKE' predicate to match the date string
				list.add(cb.like(from.get("stockDate").as(String.class), pattern));

			} catch (Exception e) {
				log.error("Error occurred while parsing date: {}", e.getMessage());
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
			list.add(cb.equal(from.get("status"), status));
		}

	}

	public GenericResponse EALStockupdate(EalRequestDTO requestDTO) {

		ArrayList<EALRequestMapEntity> finallist1 = new ArrayList<EALRequestMapEntity>();
		try {
			requestDTO.getEalmapRequestDTO().forEach(action -> {
				Optional<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.findById(action.getId());
				if (!DeptOptional.isPresent()) {
					throw new InvalidDataValidation("Record not exist");
				}
				EALRequestMapEntity ealrequestmap = DeptOptional.get();
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
				ealrequestmap.setEalrequestId(DeptOptional.get().getEalrequestId());
				ealrequestmapRepository.save(ealrequestmap);
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
		ArrayList<EALStockEntity> finallist1 = new ArrayList<EALStockEntity>();
		EALStockEntity stockentity2 = new EALStockEntity();
		try {
			stock.stream().forEach(requestDTO -> {
				EALStockEntity stockentity = new EALStockEntity();
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
		List<EALStockEntity> ealoptional = ealstockrepository.getByStockApplnno(applicationNo);
		List<EalRequestMapResponseDTO> stockdetails = new ArrayList<EalRequestMapResponseDTO>();
		EalRequestMapResponseDTO gh = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(tak -> {
			gh.setEalrequestApplnno(tak.getEalrequestapplno());
			gh.setCodeType(tak.getCodeType());
		});
		List<BarQrBalanceDTO> balancedetails = ealstockrepository.getByStockAppln(gh.getEalrequestApplnno());
		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository
				.findByRequestedapplnNo(gh.getEalrequestApplnno());
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
				Integer nobarcode1 = 0;
				Integer noqrcode1 = 0;

				if (stock.getNoofBarcode() != null && !stock.getNoofBarcode().isEmpty()) {
					nobarcode1 = (Integer.parseInt(stock.getNoofBarcode()));
				}

				if (stock.getNoofQrcode() != null && !stock.getNoofQrcode().isEmpty()) {
					noqrcode1 = Integer.parseInt(stock.getNoofQrcode());
				}
				final Integer nobarcode = nobarcode1;
				final Integer noqrcode = noqrcode1;
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
				List<BarQrBalanceDTO> balancedetailsunmapped = ealstockrepository
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

		List<EALStockEntity> ealoptional = ealstockrepository.getByEALAPPln(applicationNo);

		List<EalRequestMapResponseDTO> stockdetails = new ArrayList<EalRequestMapResponseDTO>();

		EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(ealstock -> {
			DTO.setTotalnumofBarcode(ealstock.getTotalnumofBarcode());
			DTO.setTotalnumofQrcode(ealstock.getTotalnumofQrcode());
			DTO.setEalrequestApplnno(ealstock.getEalrequestapplno());
			DTO.setStockApplnno(ealstock.getStockApplnno());
			DTO.setCodeType(ealstock.getCodeType());
		});

		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository.findByRequestedapplnNo(applicationNo);

		String codetype = ealrequestEntity.get().getCodeType();

		// List<EALStockEntity> ealoptionalstocklist
		// =ealstockrepository.getByStockApplnno(DTO.getStockApplnno());

		List<EALStockEntity> ealoptionalstocklist = null;
		if (codetype.equalsIgnoreCase("MAPPED")) {
			ealoptionalstocklist = ealstockrepository.getByEalrequestapplno(DTO.getEalrequestApplnno());
		} else {
			ealoptionalstocklist = ealstockrepository.getByEalrequestapplnounmapped(DTO.getEalrequestApplnno());
		}

		List<BarQrBalanceDTO> balancedetails = ealstockrepository.getByStockAppln(DTO.getEalrequestApplnno());

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
				obj.setNoofRoll(Integer.parseInt(maplist.getNoofRollCode()));

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
				Integer noofroll = Integer.valueOf(obj.getNoofRoll());

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
							obj.setNoofRollcodereceived(receivedetails.getNoofRollcodereceived());
							// obj.setActualbarcode(receivedetails.getNoofBarcodereceived()-stockbalance.getNoofBarcodedamaged());
							// obj.setActualqrcode(receivedetails.getNoofQrcodereceived()-stockbalance.getNoofQrcodedamaged());
							obj.setNoofBarcodedamaged(stockbalance.getNoofBarcodedamaged());
							obj.setNoofQrcodedamaged(stockbalance.getNoofQrcodedamaged());
							obj.setNoofRollBalance(noofroll - (receivedetails.getNoofRollcodereceived()));

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

				List<BarQrBalanceDTO> balancedetailsunmapped = ealstockrepository
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
		List<EALRequestEntity> ealoptional = ealrequestRepository.getByRequestedapplnNo(ealrequestApplnno);
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

		if (Objects.nonNull(filterRequestDTO.getFilters().get("unmappedType"))
				&& !filterRequestDTO.getFilters().get("unmappedType").toString().trim().isEmpty()) {

			String unmappedType = (filterRequestDTO.getFilters().get("unmappedType").toString());
			list.add(cb.equal(from.get("unmappedType"), unmappedType));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("vendorId"))
				&& !filterRequestDTO.getFilters().get("vendorId").toString().trim().isEmpty()) {

			Long vendorId = Long.valueOf((filterRequestDTO.getFilters().get("vendorId").toString()));
			list.add(cb.equal(from.get("vendorId"), vendorId));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("printingType"))
				&& !filterRequestDTO.getFilters().get("printingType").toString().trim().isEmpty()) {

			String printingType = (filterRequestDTO.getFilters().get("printingType").toString());
			list.add(cb.equal(from.get("printingType"), printingType));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("cartonSize"))
				&& !filterRequestDTO.getFilters().get("cartonSize").toString().trim().isEmpty()) {

			String cartonSize = (filterRequestDTO.getFilters().get("cartonSize").toString());
			list.add(cb.equal(from.get("cartonSize"), cartonSize));
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

//		if (Objects.nonNull(filterRequestDTO.getFilters().get("vendorStatus"))
//				&& !filterRequestDTO.getFilters().get("vendorStatus").toString().trim().isEmpty()) {
//			VendorStatus vendorStatus = null;
//			if (filterRequestDTO.getFilters().get("vendorStatus").toString().equals(VendorStatus.DISPATCHED.name())) {
//				vendorStatus = VendorStatus.DISPATCHED;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.ACCEPTED.name())) {
//				vendorStatus = VendorStatus.ACCEPTED;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.REQUESTFORCLARIFICATION.name())) {
//				vendorStatus = VendorStatus.REQUESTFORCLARIFICATION;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.ACKNOWLEDGED.name())) {
//				vendorStatus = VendorStatus.ACKNOWLEDGED;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.REJECTED.name())) {
//				vendorStatus = VendorStatus.REJECTED;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.FORCECLOSURE.name())) {
//				vendorStatus = VendorStatus.FORCECLOSURE;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.REQUESTED.name())) {
//				vendorStatus = VendorStatus.REQUESTED;
//			} else if (filterRequestDTO.getFilters().get("vendorStatus").toString()
//					.equals(VendorStatus.CANCELLED.name())) {
//				vendorStatus = VendorStatus.CANCELLED;
//			}
//
//			list.add(cb.equal(from.get("vendorStatus"), vendorStatus));
//		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("vendorStatus"))
				&& filterRequestDTO.getFilters().get("vendorStatus") instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> vendorStatuses = (List<String>) filterRequestDTO.getFilters().get("vendorStatus");
			List<Predicate> vendorStatusPredicates = new ArrayList<>();
			for (String vendorStatus : vendorStatuses) {
				if (VendorStatus.ACCEPTED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.ACCEPTED));
				} else if (VendorStatus.PARTIALDISPATCHED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.PARTIALDISPATCHED));
				} else if (VendorStatus.DISPATCHED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.DISPATCHED));
				} else if (VendorStatus.REQUESTFORCLARIFICATION.name().equals(vendorStatus)) {
					vendorStatusPredicates
							.add(cb.equal(from.get("vendorStatus"), VendorStatus.REQUESTFORCLARIFICATION));
				} else if (VendorStatus.ACKNOWLEDGED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.ACKNOWLEDGED));
				} else if (VendorStatus.REJECTED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.REJECTED));
				} else if (VendorStatus.FORCECLOSURE.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.FORCECLOSURE));
				} else if (VendorStatus.REQUESTED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.REQUESTED));
				} else if (VendorStatus.CANCELLED.name().equals(vendorStatus)) {
					vendorStatusPredicates.add(cb.equal(from.get("vendorStatus"), VendorStatus.CANCELLED));
				}
			}
			if (!vendorStatusPredicates.isEmpty()) {
				list.add(cb.or(vendorStatusPredicates.toArray(new Predicate[0])));
			}

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
				if (requestDTO.getVendorStatus().equals("ACCEPTED")) {
					ealvendor.setVendorId(null);
				}
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
		List<TPRequestEntity> ealDispatchedDetails = tpRequestRepository.getEalDispatchedDetailsByApplno(applicationNo);
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
			dto.setEalrequestId(entity.getEalrequestId());
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

	public GenericResponse getDiapatchedDetailsbyTpno(String tpApplnno) {
		List<TPRequestEntity> ealDispatchedDetails = tpRequestRepository.getEalDispatchedDetailsByTpApplno(tpApplnno);
		if (ealDispatchedDetails.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "No record found");
		}
		List<EALStockEntity> stockdetails = ealstockrepository
				.getbyTpApplnno(ealDispatchedDetails.get(0).getTpApplnno());
		if (!stockdetails.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					"This TP Number is Already Stock In Completed");
		}

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
			dto.setEalrequestId(entity.getEalrequestId());
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
			return Library.getSuccessfulResponse(ealDispatchedDetailsDTOs, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

	}

	
	@Transactional
	public GenericResponse updateVendorIdForRequests(List<Long> ids, Long vendorId) {
	    if (ids == null || ids.isEmpty()) {
	        throw new IllegalArgumentException("IDs list cannot be null or empty");
	    }

	    vendorId = vendorId == null ? 0 : vendorId;
	    if (vendorId == 0) {
	    	return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), ErrorMessages.RECORED_FOUND);
	    }

	    List<Long> updatedIds = new ArrayList<>();

	    for (Long id : ids) {
	        Optional<EALRequestEntity> optionalEalRequest = ealrequestRepository.findById(id);
	        if (!optionalEalRequest.isPresent()) {
	            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), id + ": Record not found");
	        }

	        EALRequestEntity ealRequest = optionalEalRequest.get();

	        ealRequest.setVendorId(vendorId);
	        ealRequest.setVendorStatus(VendorStatus.ASSIGNED);
	        ealRequest.setVendor(userRepository.getFirstNameAndLastName(vendorId));
	        ealRequest.setVendorAssignedDate(LocalDateTime.now());

	        ealrequestRepository.save(ealRequest);

	        updatedIds.add(id);
	    }

	    return Library.getSuccessfulResponse(updatedIds, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
	            "Vendor Id updated successfully ");
	}


	public GenericResponse getVendorCode() {
		try {
//				
			List<EalRequestVendorDTO> vendordetails = ealrequestRepository.getVendorCode();
			log.info("vendordetails :{}", vendordetails);
			int vendordetailsSize = vendordetails != null ? vendordetails.size() : 0;
			log.info("vendordetailsSize :{}", vendordetailsSize);
			if (vendordetails.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			else {
				return Library.getSuccessfulResponse(vendordetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving entity wise summary count.", e);
		}
	}

	public GenericResponse getUser(String code) {
		try {
			List<EalRequestVendorDTO> entitysummary = ealrequestRepository.getUser(code);

			if (entitysummary.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			else {
				return Library.getSuccessfulResponse(entitysummary, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving entity wise summary count.", e);
		}
	}

	public List<String> generateSearchCustomQuery(PaginationRequestDTO paginationDto) {
	    List<String> list = new ArrayList<>();
	    StringBuilder response = new StringBuilder();
	    StringBuilder total = new StringBuilder(); // For total count query

	    Map<String, Object> filters = paginationDto.getFilters();

	    if (filters != null && !filters.isEmpty()) {
	        for (Map.Entry<String, Object> entry : filters.entrySet()) {
	            String key = entry.getKey();
	            Object value = entry.getValue();

	            if (value != null && !isNullOrEmpty(value)) {
	                switch (key) {
	                    case "id":
	                        appendCondition(response, "er.id", value.toString());
	                        break;
	                    case "createdBy":
	                        appendCondition(response, "er.created_by", value.toString());
	                        break;
	                    case "status":
	                        appendApprovalStatusCondition(response, value.toString());
	                        break;
	                    case "licenseType":
	                        appendCondition(response, "er.license_type", value.toString());
	                        break;
	                    case "entityType":
	                        appendCondition(response, "er.entity_type", value.toString());
	                        break;
	                    case "cartonSize":
	                        appendCondition(response, "erm.carton_size", value.toString());
	                        break;
	                    case "mapType":
	                        appendCondition(response, "erm.map_type", value.toString());
	                        break;
	                    case "unmappedType":
	                        appendCondition(response, "erm.unmapped_type", value.toString());
	                        break;
	                    case "codeType":
	                        appendCondition(response, "er.code_type", value.toString());
	                        break;
	                    case "printingType":
	                        appendCondition(response, "erm.printing_type", value.toString());
	                        break;
	                    case "packagingSize":
	                        appendCondition(response, "erm.packaging_size", value.toString());
	                        break;
	                    case "vendorStatus":
	                        appendVendorStatusCondition(response, value);
	                        break;
	                    case "vendorId":
	                        appendCondition(response, "er.vendor_id", value.toString());
	                        break;
	                    case "licenseNo":
	                        appendCondition(response, "er.license_no", value.toString());
	                        break;
	                    case "requestedApplicationNo":
	                        appendCondition(response, "er.requested_appln_no", value.toString());
	                        break;
	                    case "fromDate":
	                    case "toDate":
	                        // These are handled together at the end
	                        break;
	                    default:
	                        // Handle unrecognized filters if needed
	                        break;
	                }
	            }
	        }

			// Append sorting if provided
			if (paginationDto.getSortField() != null && !paginationDto.getSortField().isEmpty()
					&& paginationDto.getSortOrder() != null && !paginationDto.getSortOrder().isEmpty()) {

				// Define a method to convert camelCase to snake_case
				Function<String, String> camelToSnake = s -> {
					StringBuilder result = new StringBuilder();
					for (int i = 0; i < s.length(); i++) {
						char c = s.charAt(i);
						if (Character.isUpperCase(c)) {
							result.append('_').append(Character.toLowerCase(c));
						} else {
							result.append(c);
						}
					}
					return result.toString();
				};

				String orderByField = camelToSnake.apply(paginationDto.getSortField());

				response.append(" ORDER BY er.").append(orderByField).append(" ").append(paginationDto.getSortOrder());
			}

	        // Handling date range filter
	        if (filters.containsKey("fromDate") && filters.containsKey("toDate")) {
	            String fromDate = sanitizeDate(filters.get("fromDate").toString());
	            String toDate = sanitizeDate(filters.get("toDate").toString());
	            appendCondition(response, "DATE(er.created_date)", fromDate, toDate, null);
	        }

	        int pageNo = paginationDto.getPageNo();
	        int pageSize = paginationDto.getPaginationSize();
	        int offset = pageNo * pageSize;
	        
	        // Build total count query
	        total.append("SELECT COUNT(*) FROM eal_request er left JOIN eal_request_map erm ON er.id = erm.ealrequest_id");
	        total.append(response.toString()); // Append existing conditions and pagination

	        response.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(offset);
	        list.add(response.toString());
	        list.add(total.toString()); // Add total count query
	        	        
	    }

	    return list;
	}
	// Example of a simple date sanitizer (use a proper date parsing library in production)
	private String sanitizeDate(String date) {
	    // Example: validate or format date string here as needed
	    return date;
	}

	private void appendCondition(StringBuilder response, String column, String value) {
	    if (response.length() == 0) {
	        response.append(" WHERE ");
	    } else {
	        response.append(" AND ");
	    }

	    response.append(column).append("='").append(value).append("'");
	}

	private void appendCondition(StringBuilder response, String column, String fromDate, String toDate, String value) {
	    if (response.length() == 0) {
	        response.append(" WHERE ");
	    } else {
	        response.append(" AND ");
	    }

	    if (fromDate != null && toDate != null) {
	        response.append(column).append(" BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");
	    } else if (fromDate != null) {
	        response.append(column).append(" >= '").append(fromDate).append("'");
	    } else if (toDate != null) {
	        response.append(column).append(" <= '").append(toDate).append("'");
	    } else if (value != null) {
	        response.append(column).append("='").append(value).append("'");
	    } else {
	        response.append(column).append(" IS NULL");
	    }
	}

	private void appendApprovalStatusCondition(StringBuilder response, String status) {
	    status = status.trim().toUpperCase();
	    ApprovalStatus approvalStatus;

	    try {
	        approvalStatus = ApprovalStatus.valueOf(status);
	        appendCondition(response, "er.status", approvalStatus.getId());
	    } catch (IllegalArgumentException e) {
	        // Handle case where status filter doesn't match any enum value
	        // Optionally, you can log an error or throw an exception
	    }
	}

	private void appendVendorStatusCondition(StringBuilder response, Object vendorStatusObj) {
	    if (vendorStatusObj instanceof VendorStatus) {
	        VendorStatus vendorStatus = (VendorStatus) vendorStatusObj;
	        appendCondition(response, "er.vendor_status", vendorStatus.getId());
	    } else if (vendorStatusObj instanceof String) {
	        String vendorStatusStr = ((String) vendorStatusObj).trim();
	        if (!vendorStatusStr.isEmpty()) {
	            try {
	                VendorStatus vendorStatus = VendorStatus.valueOf(vendorStatusStr.toUpperCase());
	                appendCondition(response, "er.vendor_status", vendorStatus.getId());
	            } catch (IllegalArgumentException e) {
	                // Handle invalid status string (not in enum)
	            }
	        }
	    }
	}

	private boolean isNullOrEmpty(Object obj) {
	    return obj == null || obj.toString().trim().isEmpty();
	}

	public GenericResponse getAllByfilter(PaginationRequestDTO paginationDto) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		StringBuilder sb = new StringBuilder();
		StringBuilder total = new StringBuilder();

		String query =  "SELECT \r\n"
		        + "    er.id, er.created_by, er.created_date, er.application_date, er.requested_appln_no, \r\n"
		        + "    er.status, er.entity_name, er.entity_address, er.license_type, er.entity_type, \r\n"
		        + "    er.license_no, er.code_type, er.modified_by, er.modified_date, er.tot_barcode, \r\n"
		        + "    er.tot_qrcode, er.currently_work_with, er.district, er.printing_type, er.approved_by, \r\n"
		        + "    er.forceclosure_flag, er.vendor_status, er.remarks, er.unit_code, er.pu_entity_name, \r\n"
		        + "    er.pu_entity_address, er.pu_license_type, er.pu_license_no, er.tot_roll, er.vendor_id, \r\n"
		        + "    er.vendor, erm.packaging_size, erm.carton_size, erm.no_of_barcode AS map_no_of_barcode, \r\n"
		        + "    erm.no_of_qrcode AS map_no_of_qrcode, erm.remarks AS map_remarks, erm.modified_by AS map_modified_by, \r\n"
		        + "    erm.modified_date AS map_modified_date, erm.unmapped_type, erm.no_barcode_pending, erm.no_barcode_received, \r\n"
		        + "    erm.no_qrcode_received, erm.no_roll_received, erm.no_qrcode_pending, erm.stock_applnno, erm.stock_date, \r\n"
		        + "    erm.total_numof_barcode, erm.total_numof_qrcode, erm.total_numof_roll, erm.flag, erm.opening_stock, \r\n"
		        + "    erm.licenece_number AS license_number_map, erm.printing_type AS map_printing_type, erm.no_of_roll AS map_no_of_roll, \r\n"
		        + "    erm.map_type, er.vendor_assigned_date\r\n"  
		        + "FROM eal_request er left JOIN eal_request_map erm ON er.id = erm.ealrequest_id";

		 try {
		        List<String> list = generateSearchCustomQuery(paginationDto);
		        total.append(list.get(1)); // Use the total count query from list

		        sb.append(query);
		        sb.append(list.get(0)); // Use the main query conditions from list

		        @SuppressWarnings("unchecked")
		        List<Object[]> objList = entityManager.createNativeQuery(sb.toString()).getResultList();

		List<EalRequestMapResponseDTO> finalist = objList.stream().map(val -> {
			EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
			// obj.setId(Objects.nonNull(val[0]) ? val[0].toString() : null);
			obj.setId(Objects.nonNull(val[0]) ? Long.valueOf(val[0].toString()) : null);
			// obj.setCreatedBy(Objects.nonNull(val[1]) ? val[1].toString() : null);
			obj.setCreatedDate(Objects.nonNull(val[2]) ? val[2].toString() : null);
			// obj.setApplicationDa(Objects.nonNull(val[3]) ? val[3].toString() : null);
			obj.setEalrequestApplnno(Objects.nonNull(val[4]) ? val[4].toString() : null);
			// obj.(Objects.nonNull(val[5]) ? val[5].toString() : null);
			obj.setEntityName(Objects.nonNull(val[6]) ? val[6].toString() : null);
			obj.setEntityAddress(Objects.nonNull(val[7]) ? val[7].toString() : null);
			obj.setLicenseType(Objects.nonNull(val[8]) ? val[8].toString() : null);
			obj.setEntityType(Objects.nonNull(val[9]) ? val[9].toString() : null);
			obj.setLicenseNo(Objects.nonNull(val[10]) ? val[10].toString() : null);
			obj.setCodeType(Objects.nonNull(val[11]) ? val[11].toString() : null);
			// obj.setModifiedBy(Objects.nonNull(val[12]) ? val[12].toString() : null);
			// obj.setModifiedDate(Objects.nonNull(val[13]) ? val[13].toString() : null);
//	        obj.setTotalnumofBarcode(Objects.nonNull(val[14]) ? val[14].toString() : null);
//	        obj.setTotQrcode(Objects.nonNull(val[15]) ? val[15].toString() : null);
//	        obj.setCurrentlyWorkWith(Objects.nonNull(val[16]) ? val[16].toString() : null);
//	        obj.setDistrict(Objects.nonNull(val[17]) ? val[17].toString() : null);
			obj.setPrintingType(Objects.nonNull(val[18]) ? val[18].toString() : null);
			// obj.setApprovedBy(Objects.nonNull(val[19]) ? val[19].toString() : null);
			// obj.setForceclosureFlag(Objects.nonNull(val[20]) ? val[20].toString() :
			// null);
			obj.setVendorStatus(Objects.nonNull(val[21]) ? val[21].toString() : null);
			obj.setRemarks(Objects.nonNull(val[22]) ? val[22].toString() : null);
			// obj.setUnitCode(Objects.nonNull(val[23]) ? val[23].toString() : null);
			// obj.setPuEntityName(Objects.nonNull(val[24]) ? val[24].toString() : null);
			// obj.setPuEntityAddress(Objects.nonNull(val[25]) ? val[25].toString() : null);
			// obj.setPuLicenseType(Objects.nonNull(val[26]) ? val[26].toString() : null);
			// obj.setPuLicenseNo(Objects.nonNull(val[27]) ? val[27].toString() : null);
			// obj.setNoofRoll(Objects.nonNull(val[28]) ? val[28].toString() : null);
			// obj.setNoofRoll(Objects.nonNull(val[28]) ? Integer.toString((Integer)
			// val[28]) : null);

			obj.setVendorId(Objects.nonNull(val[29]) ? val[29].toString() : null);
			obj.setVendor(Objects.nonNull(val[30]) ? val[30].toString() : null);
			obj.setPackagingSize(Objects.nonNull(val[31]) ? val[31].toString() : null);
			obj.setCartonSize(Objects.nonNull(val[32]) ? val[32].toString() : null);
			obj.setMapNoOfBarcode(Objects.nonNull(val[33]) ? val[33].toString() : null);
			obj.setMapNoOfQrcode(Objects.nonNull(val[34]) ? val[34].toString() : null);
//	        obj.setMapRemarks(Objects.nonNull(val[35]) ? val[35].toString() : null);
//	        obj.setMapModifiedBy(Objects.nonNull(val[36]) ? val[36].toString() : null);
//	        obj.setMapModifiedDate(Objects.nonNull(val[37]) ? val[37].toString() : null);
//	        obj.setUnmappedType(Objects.nonNull(val[38]) ? val[38].toString() : null);
//	        obj.setNoBarcodePending(Objects.nonNull(val[39]) ? val[39].toString() : null);
			// obj.setTotalnumofBarcode(Objects.nonNull(val[40]) ?
			// Integer.valueOf(val[40].toString()) : null); // Corrected type
			// obj.setTotalnumofQrcode(Objects.nonNull(val[41]) ?
			// Integer.valueOf(val[41].toString()) : null); // Corrected type
			// obj.setTotalnumofRoll(Objects.nonNull(val[42]) ?
			// Integer.valueOf(val[42].toString()) : null); // Corrected type
//	        obj.setNoofBarcodepending(Objects.nonNull(val[43]) ? val[43].toString() : null);
			// obj.setStockApplnno(Objects.nonNull(val[44]) ? val[44].toString() : null);
			// obj.setStockDate(Objects.nonNull(val[45]) ? val[45].toString() : null);
			// obj.setNoofBarcode(val[46] != null ? val[46].toString() : null);
			// obj.setNoofQrcode(val[47] != null ? val[47].toString() : null);
			// obj.setNoofRoll(val[48] != null ? Integer.parseInt(val[48].toString()) :
			// null);
			obj.setTotBarcode(val[49] != null ? val[49].toString() : null);
			obj.setTotQrcode(val[50] != null ? val[50].toString() : null);

			// obj.setFlag(Objects.nonNull(val[49]) ? val[49].toString() : null);
//	        obj.setOpeningStock(Objects.nonNull(val[50]) ? val[50].toString() : null);
//	        obj.setLicenseNumberMap(Objects.nonNull(val[51]) ? val[51].toString() : null);
//	        obj.setMapPrintingType(Objects.nonNull(val[52]) ? val[52].toString() : null);
			obj.setMapNoOfRoll(Objects.nonNull(val[53]) ? val[53].toString() : null);
			obj.setMapType(Objects.nonNull(val[54]) ? val[54].toString() : null);
//	        obj.setTotBarCode(Objects.nonNull(val[14]) ? Integer.valueOf(val[14].toString()) : null);
//	        obj.setTotQrCode(Objects.nonNull(val[15]) ? Integer.valueOf(val[15].toString()) : null);
//	        obj.setTotRoll(Objects.nonNull(val[16]) ? Integer.valueOf(val[16].toString()) : null);
			obj.setVendorAssignedDate(Objects.nonNull(val[55]) ? val[55].toString() : null);
			return obj;
		}).collect(Collectors.toList());

		paginationResponseDTO.setContents(finalist);
        paginationResponseDTO.setNumberOfElements(finalist.size());

        // Execute total count query to get total count
        BigInteger totalCount = (BigInteger) entityManager.createNativeQuery(total.toString()).getSingleResult();
        paginationResponseDTO.setTotalElements(totalCount.longValue());

        return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                ErrorMessages.RECORED_FOUND);
    } catch (Exception e) {
        // Handle exceptions
        e.printStackTrace();
        return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
    }
}
}