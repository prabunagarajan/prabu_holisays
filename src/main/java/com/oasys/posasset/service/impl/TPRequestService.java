package com.oasys.posasset.service.impl;

import static com.oasys.posasset.constant.Constant.ASC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.dto.TPRequestDTO;
import com.oasys.posasset.entity.EALRequestLogEntity;
import com.oasys.posasset.entity.TPRequestEntity;
import com.oasys.posasset.mapper.StockOverviewMapper;
import com.oasys.posasset.repository.EALRequestLogRepository;
import com.oasys.posasset.repository.EALStockRepository;
import com.oasys.posasset.repository.TpRequestRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TPRequestService {
	
	@Autowired
	TpRequestRepository tprepository;

	@Autowired
	TpRequestRepository tprequestRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private StockOverviewMapper ealrequestMapper;

	@Autowired
	EALRequestLogRepository ealrequestlogRepository;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EALStockRepository ealstockrepository;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;

	public GenericResponse getCode() {
		// MenuPrefix prefix = MenuPrefix.getType(GS_CODE);
		Year y = Year.now();
		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		String code = RandomUtil.getRandomNumber();
		return Library.getSuccessfulResponse("TP-" + code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
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

		public GenericResponse Tprequestsave(List<TPRequestDTO> tprequest) {
			ArrayList<TPRequestEntity> finallist1 = new ArrayList<TPRequestEntity>();
			TPRequestEntity tpentitynew = new TPRequestEntity();
			try {
	
				tprequest.stream().forEach(requestDTO -> {
					TPRequestEntity tpentity = new TPRequestEntity();
					tpentity.setTpApplnno(requestDTO.getTpApplnno());
					tpentity.setTpDate(new Date());
					tpentity.setTotalnumofBarcode(requestDTO.getTotalnumofBarcode());
					tpentity.setTotalnumofQrcode(requestDTO.getTotalnumofQrcode());
					tpentity.setTotalnumofRoll(requestDTO.getTotalnumofRoll());
					tpentity.setCodeType(requestDTO.getCodeType());
					tpentity.setUnmappedType(requestDTO.getUnmappedType());
					tpentity.setOpenstockApplnno(requestDTO.getOpenstockApplnno());
					tpentity.setLicenseNo(requestDTO.getLicencenumber());
					tpentity.setCartonSize(requestDTO.getCartonSize());
					tpentity.setNoofBarcode(requestDTO.getNoofBarcode());
					tpentity.setNoofQrcode(requestDTO.getNoofQrcode());
					tpentity.setPackagingSize(requestDTO.getPackagingSize());
					tpentity.setRemarks(requestDTO.getRemarks());
					tpentity.setNoofBarcodereceived(requestDTO.getNoofBarcodereceived());
					tpentity.setNoofQrcodereceived(requestDTO.getNoofQrcodereceived());
					tpentity.setNoofRollcodereceived(requestDTO.getNoofRollcodereceived());
					tpentity.setNoofBarcodepending(requestDTO.getNoofBarcodepending()==null?0:requestDTO.getNoofBarcodepending());
					tpentity.setNoofqrpending(requestDTO.getNoofqrpending()==null?0:requestDTO.getNoofqrpending());
					tpentity.setEalrequestapplno(requestDTO.getEalrequestApplnno());
					tpentity.setEalrequestId(requestDTO.getEalrequestId());
				tpentity.setFlag(requestDTO.isFlag());
				tpentity.setNoofBarcodedamaged(requestDTO.getNoofBarcodedamaged());
				tpentity.setNoofQrcodedamaged(requestDTO.getNoofQrcodedamaged());
				tpentity.setPrintingType(requestDTO.getPrintingType());
				tpentitynew.setEalrequestapplno(requestDTO.getEalrequestApplnno());
				tpentitynew.setUserRemarks(requestDTO.getUserRemarks());
				tpentity.setVendorStatus(requestDTO.getVendorStatus());
				tpentity.setEntityName(requestDTO.getEntityName());
				tpentity.setEntityType(requestDTO.getEntityType());
				tpentity.setMapType(requestDTO.getMapType());
				tpentity.setNoOfRoll(requestDTO.getNoOfRoll());
				tpentity.setVehicleAgencyName(requestDTO.getVehicleAgencyName());
				tpentity.setVehicleNumber(requestDTO.getVehicleNumber());
				tpentity.setVehicleAgencyAddress(requestDTO.getVehicleAgencyAddress());
				tpentity.setDriverName(requestDTO.getDriverName());
				tpentity.setDigiLockID(requestDTO.getDigiLockID());
				tpentity.setDistanceKms(requestDTO.getDistanceKms());
				tpentity.setRouteType(requestDTO.getRouteType());
				tpentity.setRouteDetails(requestDTO.getRouteDetails());
				tpentity.setMajorRoute(requestDTO.getMajorRoute());
				tpentity.setValidUpto(requestDTO.getValidUpto());
				tpentity.setValidUptohrs(requestDTO.getValidUptohrs());
				tpentity.setGpsDeviceID(requestDTO.getGpsDeviceID());
				tpentity.setGrossWeightQtls(requestDTO.getGrossWeightQtls());
				tpentity.setTareWeightQtls(requestDTO.getTareWeightQtls());
				tpentity.setNetWeightQtls(requestDTO.getNetWeightQtls());
				tpentity.setVendorName(requestDTO.getVendorName());
				tpentity.setNoofRollcodepending(requestDTO.getNoofRollcodepending()==null?0:requestDTO.getNoofRollcodepending());
				tpentity.setVendorAddress(requestDTO.getVendorAddress());
				tpentity.setCreatedBy(requestDTO.getCreatedBy());
				if((requestDTO.getNoofBarcodepending() == 0 ||requestDTO.getNoofBarcodepending() == null) 
						&& (requestDTO.getNoofqrpending() == 0 || requestDTO.getNoofqrpending() == null ) 
						&& (requestDTO.getNoofRollcodepending() ==0 || requestDTO.getNoofRollcodepending() ==null )) {
					List<TPRequestEntity> previousDispatchList = tprequestRepository.previouspreviousDispatchList(
							requestDTO.getEalrequestApplnno(),requestDTO.getUnmappedType(),
							requestDTO.getCodeType(),requestDTO.getPrintingType(),
							requestDTO.getPackagingSize(),requestDTO.getCartonSize(),
							requestDTO.getNoofBarcode(),requestDTO.getNoofQrcode(),
							requestDTO.getMapType(),requestDTO.getNoOfRoll());
					 previousDispatchList.forEach(tpEntity -> {
					        tpEntity.setVendorStatus(requestDTO.getVendorStatus());
					        tprequestRepository.save(tpEntity);
					    });
					
				}
				tprequestRepository.save(tpentity);
				finallist1.add(tpentity);
			});

			try {
				EALRequestLogEntity ealrequestlog = new EALRequestLogEntity();
				ealrequestlog.setComments("TPrequest Saved Successfully");
				ealrequestlog.setApplnNo(tpentitynew.getEalrequestapplno());
				ealrequestlog.setAction(tpentitynew.getRemarks());
				ealrequestlog.setRemarks(tpentitynew.getUserRemarks());
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

	public GenericResponse getAll() {
		List<TPRequestEntity> DpList = tprequestRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DpList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(DpList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getById(Long id) {
		Optional<TPRequestEntity> tpRequestEntity = tprequestRepository.findById(id);
		if (!tpRequestEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(tpRequestEntity.get(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<TPRequestEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<TPRequestEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
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

	public List<TPRequestEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TPRequestEntity> cq = cb.createQuery(TPRequestEntity.class);
		Root<TPRequestEntity> from = cq.from(TPRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<TPRequestEntity> typedQuery = null;
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

	public List<TPRequestEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TPRequestEntity> cq = cb.createQuery(TPRequestEntity.class);
		Root<TPRequestEntity> from = cq.from(TPRequestEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<TPRequestEntity> typedQuery1 = null;
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
			Root<TPRequestEntity> from) {

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

		if (Objects.nonNull(filterRequestDTO.getFilters().get("unitCode"))
				&& !filterRequestDTO.getFilters().get("unitCode").toString().trim().isEmpty()) {

			String unitCode = (filterRequestDTO.getFilters().get("unitCode").toString());
			list.add(cb.equal(from.get("unitCode"), unitCode));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("total_numof_barcode"))
				&& !filterRequestDTO.getFilters().get("total_numof_barcode").toString().trim().isEmpty()) {

			Long barcode = Long.valueOf(filterRequestDTO.getFilters().get("total_numof_barcode").toString());
			list.add(cb.equal(from.get("totalnumofBarcode"), barcode));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("total_numof_qrcode"))
				&& !filterRequestDTO.getFilters().get("total_numof_qrcode").toString().trim().isEmpty()) {

			Long qrcode = Long.valueOf(filterRequestDTO.getFilters().get("total_numof_qrcode").toString());
			list.add(cb.equal(from.get("totalnumofQrcode"), qrcode));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("total_numof_roll"))
				&& !filterRequestDTO.getFilters().get("total_numof_roll").toString().trim().isEmpty()) {

			Long roll = Long.valueOf(filterRequestDTO.getFilters().get("total_numof_roll").toString());
			list.add(cb.equal(from.get("totalnumofRoll"), roll));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("code_type"))
				&& !filterRequestDTO.getFilters().get("code_type").toString().trim().isEmpty()) {

			String codetype = String.valueOf(filterRequestDTO.getFilters().get("code_type").toString());
			list.add(cb.equal(from.get("codeType"), codetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("tpApplnno"))
				&& !filterRequestDTO.getFilters().get("tpApplnno").toString().trim().isEmpty()) {

			String appno = String.valueOf(filterRequestDTO.getFilters().get("tpApplnno").toString());
			list.add(cb.equal(from.get("tpApplnno"), appno));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("eal_requestid"))
				&& !filterRequestDTO.getFilters().get("eal_requestid").toString().trim().isEmpty()) {

			String eal = String.valueOf(filterRequestDTO.getFilters().get("eal_requestid").toString());
			list.add(cb.equal(from.get("ealrequestId"), eal));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("unmapped_type"))
				&& !filterRequestDTO.getFilters().get("unmapped_type").toString().trim().isEmpty()) {

			String unmaptype = String.valueOf(filterRequestDTO.getFilters().get("unmapped_type").toString());
			list.add(cb.equal(from.get("unmappedType"), unmaptype));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("openstock_applnno"))
				&& !filterRequestDTO.getFilters().get("openstock_applnno").toString().trim().isEmpty()) {

			String stockno = String.valueOf(filterRequestDTO.getFilters().get("openstock_applnno").toString());
			list.add(cb.equal(from.get("openstockApplnno"), stockno));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("license_no"))
				&& !filterRequestDTO.getFilters().get("license_no").toString().trim().isEmpty()) {

			String entityno = String.valueOf(filterRequestDTO.getFilters().get("license_no").toString());
			list.add(cb.equal(from.get("licenseNo"), entityno));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("packaging_size"))
				&& !filterRequestDTO.getFilters().get("packaging_size").toString().trim().isEmpty()) {

			String packsize = String.valueOf(filterRequestDTO.getFilters().get("packaging_size").toString());
			list.add(cb.equal(from.get("packagingSize"), packsize));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("carton_size"))
				&& !filterRequestDTO.getFilters().get("carton_size").toString().trim().isEmpty()) {

			String actiontaken = String.valueOf(filterRequestDTO.getFilters().get("carton_size").toString());
			list.add(cb.equal(from.get("cartonSize"), actiontaken));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_of_barcode"))
				&& !filterRequestDTO.getFilters().get("no_of_barcode").toString().trim().isEmpty()) {

			String finalstatus = String.valueOf(filterRequestDTO.getFilters().get("no_of_barcode").toString());
			list.add(cb.equal(from.get("noofBarcode"), finalstatus));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_of_qrcode"))
				&& !filterRequestDTO.getFilters().get("no_of_qrcode").toString().trim().isEmpty()) {

			String noqrcode = String.valueOf(filterRequestDTO.getFilters().get("no_of_qrcode").toString());
			list.add(cb.equal(from.get("noofQrcode"), noqrcode));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_barcode_pending"))
				&& !filterRequestDTO.getFilters().get("no_barcode_pending").toString().trim().isEmpty()) {

			Long pending = Long.valueOf(filterRequestDTO.getFilters().get("no_barcode_pending").toString());
			list.add(cb.equal(from.get("noofBarcodepending"), pending));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_barcode_received"))
				&& !filterRequestDTO.getFilters().get("no_barcode_received").toString().trim().isEmpty()) {

			Long barcode = Long.valueOf(filterRequestDTO.getFilters().get("no_barcode_received").toString());
			list.add(cb.equal(from.get("noofBarcodereceived"), barcode));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_qrcode_received"))
				&& !filterRequestDTO.getFilters().get("no_qrcode_received").toString().trim().isEmpty()) {

			Long image = Long.valueOf(filterRequestDTO.getFilters().get("no_qrcode_received").toString());
			list.add(cb.equal(from.get("noofQrcodereceived"), image));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_roll_received"))
				&& !filterRequestDTO.getFilters().get("no_roll_received").toString().trim().isEmpty()) {

			Long image1 = Long.valueOf(filterRequestDTO.getFilters().get("no_roll_received").toString());
			list.add(cb.equal(from.get("noofRollcodereceived"), image1));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_qrcode_pending"))
				&& !filterRequestDTO.getFilters().get("no_qrcode_pending").toString().trim().isEmpty()) {

			Long uuid1 = Long.valueOf(filterRequestDTO.getFilters().get("no_qrcode_pending").toString());
			list.add(cb.equal(from.get("noofqrpending"), uuid1));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("eal_request_applnno"))
				&& !filterRequestDTO.getFilters().get("eal_request_applnno").toString().trim().isEmpty()) {

			String uuid2 = String.valueOf(filterRequestDTO.getFilters().get("eal_request_applnno").toString());
			list.add(cb.equal(from.get("ealrequestapplno"), uuid2));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_barcode_damaged"))
				&& !filterRequestDTO.getFilters().get("no_barcode_damaged").toString().trim().isEmpty()) {

			Long damage = Long.valueOf(filterRequestDTO.getFilters().get("no_barcode_damaged").toString());
			list.add(cb.equal(from.get("noofBarcodedamaged"), damage));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("no_qrcode_damaged"))
				&& !filterRequestDTO.getFilters().get("no_qrcode_damaged").toString().trim().isEmpty()) {

			Long qrdamage = Long.valueOf(filterRequestDTO.getFilters().get("no_qrcode_damaged").toString());
			list.add(cb.equal(from.get("noofQrcodedamaged"), qrdamage));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("printing_type"))
				&& !filterRequestDTO.getFilters().get("printing_type").toString().trim().isEmpty()) {

			String entitysiteobs = String.valueOf(filterRequestDTO.getFilters().get("printing_type").toString());
			list.add(cb.equal(from.get("printingType"), entitysiteobs));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("entity_name"))
				&& !filterRequestDTO.getFilters().get("entity_name").toString().trim().isEmpty()) {

			String entitysitevisit = String.valueOf(filterRequestDTO.getFilters().get("entity_name").toString());
			list.add(cb.equal(from.get("entityName"), entitysitevisit));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("entity_address"))
				&& !filterRequestDTO.getFilters().get("entity_address").toString().trim().isEmpty()) {

			String entitysiteissue = String.valueOf(filterRequestDTO.getFilters().get("entity_address").toString());
			list.add(cb.equal(from.get("entityAddress"), entitysiteissue));
		}
		if (Objects.nonNull(filterRequestDTO.getFilters().get("license_type"))
				&& !filterRequestDTO.getFilters().get("license_type").toString().trim().isEmpty()) {

			String district = String.valueOf(filterRequestDTO.getFilters().get("license_type").toString());
			list.add(cb.equal(from.get("licenseType"), district));
		}

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



///private static final Logger log = LoggerFactory.logger(TPRequestService.class);

    public GenericResponse getBalanceForApplicationNumber(String tpApplnno) {

        log.info("Fetching dispatched details for TP application number: {}", tpApplnno);

        List<TPRequestEntity> DeptOptional = tprequestRepository.findByTpApplnno(tpApplnno);

        if (DeptOptional.isEmpty()) {
            log.warn("No records found for TP application number: {}", tpApplnno);
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        TPRequestEntity firstEntity = DeptOptional.get(0);
        Long id = firstEntity.getId();
        Optional<TPRequestEntity> ealrequestEntityOptional = tprequestRepository.findById(id);

        if (!ealrequestEntityOptional.isPresent()) {
            log.warn("No TPRequestEntity found with ID: {}", id);
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        TPRequestEntity ealrequestEntity = ealrequestEntityOptional.get();
        Integer totabarcode = ealrequestEntity.getTotalnumofBarcode();
        Integer totalqrcode = ealrequestEntity.getTotalnumofQrcode();       
        Integer totRoll = (ealrequestEntity.getTotalnumofRoll()==null?0:ealrequestEntity.getTotalnumofRoll());        
        String stockappln = ealrequestEntity.getTpApplnno();

        log.info("Fetching balance details for stock application number: {}", stockappln);
        
        
        List<BarQrBalanceDTO> balancedetails;
        if (ealrequestEntityOptional.isPresent() && ealrequestEntityOptional.get().getCodeType().equals("MAPPED")) {
            balancedetails = ealstockrepository.getByStockApplnMap(stockappln);
        } else {
            balancedetails = ealstockrepository.getByStockApplnUnMap(stockappln);
        }
        
//        if (balancedetails.isEmpty()) {
//            log.warn("No Stockin found with stockappln: {}", stockappln);
//            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "This TP Not Yet Stock In");
//        }
        
        List<EalRequestMapResponseDTO> stockdetails = new ArrayList<>();

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
            obj.setNoofBarcodeBalance(maplist.getNoofBarcodepending());
            obj.setNoofBarcodereceived(maplist.getNoofBarcodereceived());
            obj.setNoofQrcodereceived(maplist.getNoofQrcodereceived());
            obj.setNoofRollcodereceived(maplist.getNoofRollcodereceived());
            obj.setNoofQrcodeBalance(maplist.getNoofqrpending());
            obj.setNoofRollBalance(maplist.getNoofRollcodepending());
            obj.setDispatchnoofBarcodereceived(maplist.getNoofBarcodereceived());
            obj.setDispatchnoofQrcodereceived(maplist.getNoofQrcodereceived());
            obj.setDispatchnoofRollcodereceived(maplist.getNoofRollcodereceived());
            obj.setTpApplnNo(maplist.getTpApplnno());
            obj.setNoofBarcodepending(maplist.getNoofBarcodepending()==null?0:maplist.getNoofBarcodepending());
            obj.setNoofqrpending(maplist.getNoofqrpending()==null?0:maplist.getNoofqrpending());
            obj.setLicencenumber(maplist.getLicenseNo());
            obj.setFlag(maplist.isFlag());

            if (maplist.getEalrequestId() != null) {
                obj.setEalrequestId(maplist.getEalrequestId().longValue());
            }

            obj.setTotalnumofBarcode(totabarcode);
            obj.setTotalnumofQrcode(totalqrcode);
            obj.setTotalnumofRoll(totRoll);
            obj.setPrintingType(maplist.getPrintingType());
            obj.setNoofRoll(maplist.getNoOfRoll());
            obj.setMapType(maplist.getMapType());

            balancedetails.stream().forEach(stockbalance -> {
                EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
                receivedetails.setPackagingSize(stockbalance.getPackagingSize());
                receivedetails.setNoofBarcodereceived(stockbalance.getNoofBarcodereceived());
                receivedetails.setNoofQrcodereceived(stockbalance.getNoofQrcodereceived());
                receivedetails.setNoofRollcodereceived(stockbalance.getNoofRollcodereceived());
                if (obj.getCodeType().equals("MAPPED")) {
                    if (obj.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
                        obj.setNoofBarcodeBalance(Integer.parseInt(maplist.getNoofBarcode()) - receivedetails.getNoofBarcodereceived());
                        obj.setNoofQrcodeBalance(Integer.parseInt(maplist.getNoofQrcode()) - receivedetails.getNoofQrcodereceived());
                        obj.setNoofRollBalance(maplist.getNoOfRoll() - receivedetails.getNoofRollcodereceived());
                    }
                } else { // "UNMAPPED"
                    obj.setNoofBarcodeBalance(totabarcode - receivedetails.getNoofBarcodereceived());
                    obj.setNoofQrcodeBalance(totalqrcode - receivedetails.getNoofQrcodereceived());
                    log.info("totRoll------------------"+totRoll);
                    log.info("receivedetails.getNoofRollcodereceived()------------"+receivedetails.getNoofRollcodereceived());
                    obj.setNoofRollBalance(totRoll - receivedetails.getNoofRollcodereceived());
                }
            });

            List<BarQrBalanceDTO> tpdispatch = tprepository.getByEalreqid(id);

            tpdispatch.stream().forEach(dispatch -> {
                if (obj.getCodeType().equals("MAPPED")) {
                    if (obj.getPackagingSize().equalsIgnoreCase(dispatch.getPackagingSize())) {
                        obj.setDispatchnoofBarcodereceived(dispatch.getDispatchNoofBarcodereceived());
                        obj.setDispatchnoofQrcodereceived(dispatch.getDispatchNoofQrcodereceived());
                        obj.setDispatchnoofRollcodereceived(dispatch.getDispatchNoofRollcodereceived());
                    }
                } else { // "UNMAPPED"
                    obj.setDispatchnoofBarcodereceived(dispatch.getDispatchNoofBarcodereceived());
                    obj.setDispatchnoofQrcodereceived(dispatch.getDispatchNoofQrcodereceived());
                    obj.setDispatchnoofRollcodereceived(dispatch.getDispatchNoofRollcodereceived());
                }
            });

            if (obj.getNoofBarcodeBalance() > 0 || obj.getNoofQrcodeBalance() > 0 || obj.getNoofRollBalance() > 0) {
                log.info("Adding record with balances > 0: {}", obj);
                stockdetails.add(obj);
            } else {
                log.info("Filtered out record with balances <= 0: {}", obj);
            }
        });

        log.info("Total stock details after filtering: {}", stockdetails.size());
        

        List<EalRequestMapResponseDTO> filteredStockDetails = stockdetails.stream()
        	    .filter(detail -> 
        	        detail.getNoofBarcodepending() != 0 || detail.getNoofqrpending() != 0)
        	    .collect(Collectors.toList());
        
        GenericResponse response = Library.getSuccessfulResponse(filteredStockDetails, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                ErrorMessages.RECORED_FOUND);
        log.info("Response generated successfully: {}", response);

        return response;
    }
}
