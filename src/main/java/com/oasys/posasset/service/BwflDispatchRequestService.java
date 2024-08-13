package com.oasys.posasset.service;

import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.posasset.constant.Constant.ASC;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.el.Expression;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
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
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.dto.BwflDispatchRequest;
import com.oasys.posasset.dto.BwflDispatchResponseDTO;
import com.oasys.posasset.dto.EalPUtoBWFLRequestDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.entity.BwflDispatchRequestEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestLogEntity;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLEntity;
import com.oasys.posasset.repository.BwflDispatchRequestRepository;
import com.oasys.posasset.repository.EALRequestLogRepository;
import com.oasys.posasset.repository.EALRequestMapRepository;
import com.oasys.posasset.repository.EALRequestPUtoBWFLRepository;
import com.oasys.posasset.repository.EALRequestRepository;
import com.oasys.posasset.repository.TpRequestRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BwflDispatchRequestService {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	BwflDispatchRequestRepository bwfldispatchrequestrepository;

	@Autowired
	EALRequestPUtoBWFLRepository ealRequestPUtoBWFLRepository;

	@Autowired
	EALRequestMapRepository ealrequestmapRepository;

	@Autowired
	EALRequestLogRepository ealrequestlogRepository;

	@Autowired
	EALRequestRepository ealrequestRepository;

	@Autowired
	TpRequestRepository tprepository;

	public GenericResponse BwflDispatchsave(List<BwflDispatchRequest> bwflDispatchRequest) {
		List<BwflDispatchRequestEntity> finallist1 = new ArrayList<>();
		BwflDispatchRequestEntity bwflDispatchRequestEntitynew = new BwflDispatchRequestEntity();

		try {
			bwflDispatchRequest.forEach(requestDTO -> {
				BwflDispatchRequestEntity bwflDispatentity = new BwflDispatchRequestEntity();

				bwflDispatentity.setTpApplnno(requestDTO.getTpApplnno());
				bwflDispatentity.setTpDate(new Date());
				bwflDispatentity.setTotalnumofBarcode(requestDTO.getTotalnumofBarcode());
				bwflDispatentity.setTotalnumofQrcode(requestDTO.getTotalnumofQrcode());
				bwflDispatentity.setCodeType(requestDTO.getCodeType());
				bwflDispatentity.setUnmappedType(requestDTO.getUnmappedType());
				bwflDispatentity.setOpenstockApplnno(requestDTO.getOpenstockApplnno());
				bwflDispatentity.setLicenseNo(requestDTO.getLicencenumber());
				bwflDispatentity.setCartonSize(requestDTO.getCartonSize());
				bwflDispatentity.setNoofBarcode(requestDTO.getNoofBarcode());
				bwflDispatentity.setNoofQrcode(requestDTO.getNoofQrcode());
				bwflDispatentity.setPackagingSize(requestDTO.getPackagingSize());
				bwflDispatentity.setRemarks(requestDTO.getRemarks());
				bwflDispatentity.setNoofBarcodereceived(requestDTO.getNoofBarcodereceived());
				bwflDispatentity.setNoofQrcodereceived(requestDTO.getNoofQrcodereceived());
				bwflDispatentity.setNoofRollcodereceived(requestDTO.getNoofRollcodereceived());
				bwflDispatentity.setNoofBarcodepending(requestDTO.getNoofBarcodepending());
				bwflDispatentity.setNoofqrpending(requestDTO.getNoofqrpending());
				bwflDispatentity.setEalrequestapplno(requestDTO.getEalrequestApplnno());
				bwflDispatentity.setEalrequestId(requestDTO.getEalrequestId());
				bwflDispatentity.setFlag(requestDTO.isFlag());
				bwflDispatentity.setNoofBarcodedamaged(requestDTO.getNoofBarcodedamaged());
				bwflDispatentity.setNoofQrcodedamaged(requestDTO.getNoofQrcodedamaged());
				bwflDispatentity.setPrintingType(requestDTO.getPrintingType());
				bwflDispatchRequestEntitynew.setEalrequestapplno(requestDTO.getEalrequestApplnno());
				bwflDispatchRequestEntitynew.setUserRemarks(requestDTO.getUserRemarks());
				bwflDispatentity.setVendorStatus(requestDTO.getVendorStatus());
				bwflDispatentity.setEntityName(requestDTO.getEntityName());
				bwflDispatentity.setMapType(requestDTO.getMapType());
				bwflDispatentity.setNoOfRoll(requestDTO.getNoOfRoll());
				bwflDispatentity.setVehicleAgencyName(requestDTO.getVehicleAgencyName());
				bwflDispatentity.setVehicleNumber(requestDTO.getVehicleNumber());
				bwflDispatentity.setVehicleAgencyAddress(requestDTO.getVehicleAgencyAddress());
				bwflDispatentity.setDriverName(requestDTO.getDriverName());
				bwflDispatentity.setDigiLockID(requestDTO.getDigiLockID());
				bwflDispatentity.setDistanceKms(requestDTO.getDistanceKms());
				bwflDispatentity.setRouteType(requestDTO.getRouteType());
				bwflDispatentity.setRouteDetails(requestDTO.getRouteDetails());
				bwflDispatentity.setMajorRoute(requestDTO.getMajorRoute());
				bwflDispatentity.setValidUpto(requestDTO.getValidUpto());
				bwflDispatentity.setValidUptohrs(requestDTO.getValidUptohrs());
				bwflDispatentity.setGpsDeviceID(requestDTO.getGpsDeviceID());
				bwflDispatentity.setGrossWeightQtls(requestDTO.getGrossWeightQtls());
				bwflDispatentity.setTareWeightQtls(requestDTO.getTareWeightQtls());
				bwflDispatentity.setNetWeightQtls(requestDTO.getNetWeightQtls());
				bwflDispatentity.setEntityAddress(requestDTO.getEntityAddress());
				bwflDispatentity.setEntityName(requestDTO.getEntityName());
				bwflDispatentity.setEntityType(requestDTO.getEntityType());
				bwflDispatentity.setCreatedBy(requestDTO.getCreatedBy());
				bwfldispatchrequestrepository.save(bwflDispatentity);
				finallist1.add(bwflDispatentity);
			});

			try {
				EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
				ealrequestlog.setComments("TP request Saved Successfully");
				ealrequestlog.setApplnNo(bwflDispatchRequestEntitynew.getEalrequestapplno());
				// ealrequestlog.setAction(bwflDispatchRequestEntitynew.);
				ealrequestlog.setRemarks(bwflDispatchRequestEntitynew.getUserRemarks());
				ealrequestlogRepository.save(ealrequestlog);
			} catch (Exception e) {
				log.info("TPRequest LOG::::" + e);
			}

			return Library.getSuccessfulResponse(finallist1, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), e.getMessage());
		}
	}

	public GenericResponse getById(Long id) {
		List<BwflDispatchRequestEntity> ealrequstBwflDispatchlist = new ArrayList<BwflDispatchRequestEntity>();

		List<BwflDispatchRequestEntity> ealrequestEntity = bwfldispatchrequestrepository.getById(id);

		ealrequstBwflDispatchlist.addAll(ealrequestEntity);

		if (!ealrequestEntity.isEmpty()) {

//			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);

			BwflDispatchRequestEntity ealrequestBwflDispatchEntity1 = new BwflDispatchRequestEntity();
			Long ealrequestid;
			DeptOptional.stream().forEach(action -> {
//				ealrequestEntity1.setPackagingSize(action.getPackagingSize());
//				ealrequestEntity1.setCartonSize(action.getCartonSize());
//				ealrequestEntity1.setNoofBarcode(action.getNoofBarcode());
//				ealrequestEntity1.setNoofQrcode(action.getNoofQrcode());
//				ealrequestEntity1.setPackagingSize(action.getPackagingSize());
//				ealrequestEntity1.setRemarks(action.getRemarks());
				ealrequestBwflDispatchEntity1.setEalrequestId(action.getEalrequestId().getId());
//				ealrequestEntity1.setUnmappedType(action.getUnmappedType());
				ealrequestBwflDispatchEntity1.setId(action.getId());
				ealrequstBwflDispatchlist.add(ealrequestBwflDispatchEntity1);
			});

			return Library.getSuccessfulResponse(ealrequstBwflDispatchlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

		else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}

	}

	public GenericResponse getBybwflealid(Long id) {

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

		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(stockdetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<BwflDispatchRequestEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<BwflDispatchRequestEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<BwflDispatchRequestEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<BwflDispatchRequestEntity> cq = cb.createQuery(BwflDispatchRequestEntity.class);
		Root<BwflDispatchRequestEntity> from = cq.from(BwflDispatchRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<BwflDispatchRequestEntity> typedQuery = null;
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

	public List<BwflDispatchRequestEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<BwflDispatchRequestEntity> cq = cb.createQuery(BwflDispatchRequestEntity.class);
		Root<BwflDispatchRequestEntity> from = cq.from(BwflDispatchRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<BwflDispatchRequestEntity> typedQuery1 = null;
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
			Root<BwflDispatchRequestEntity> from) {

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
				Path<Object> mainModule = from.get("licenseNo");
				list.add(mainModule.in(licenseNo));
			}
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("codeType"))
				&& !filterRequestDTO.getFilters().get("codeType").toString().trim().isEmpty()) {

			String codetype = filterRequestDTO.getFilters().get("codeType").toString();
			list.add(cb.equal(from.get("codeType"), codetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("tpApplnno"))
				&& !filterRequestDTO.getFilters().get("tpApplnno").toString().trim().isEmpty()) {

			String codetype = filterRequestDTO.getFilters().get("tpApplnno").toString();
			list.add(cb.equal(from.get("tpApplnno"), codetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseType"))
				&& !filterRequestDTO.getFilters().get("licenseType").toString().trim().isEmpty()) {

			String licencetype = (filterRequestDTO.getFilters().get("licenseType").toString());
			list.add(cb.equal(from.get("licenseType"), licencetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("requestedapplnNo"))
				&& !filterRequestDTO.getFilters().get("requestedapplnNo").toString().trim().isEmpty()) {

			String requestedapplnNo = (filterRequestDTO.getFilters().get("requestedapplnNo").toString());
			list.add(cb.equal(from.get("ealrequestapplno"), requestedapplnNo));
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

	}

}