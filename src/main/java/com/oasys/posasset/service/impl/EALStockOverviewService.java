package com.oasys.posasset.service.impl;

import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.posasset.constant.Constant.DESC;
import static com.oasys.posasset.constant.Constant.MODIFIED_DATE;

import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.Stockbalance;
import com.oasys.helpdesk.repository.BarQrOverviewDTO;
import com.oasys.helpdesk.repository.EALAvailable;
import com.oasys.helpdesk.repository.EALWastageUsage;
import com.oasys.helpdesk.repository.EalWastageRepository;
import com.oasys.helpdesk.repository.ReceviedBarQr;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.dto.EALStockwastageDTO;
import com.oasys.posasset.dto.EALUsageDTO;
import com.oasys.posasset.dto.EalRequestDTO;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.dto.StockEALDECAECDTO;
import com.oasys.posasset.dto.StockOverviewMapResponseDTO;
import com.oasys.posasset.dto.StockOverviewTilesDTO;
import com.oasys.posasset.dto.StockOverviewUnMapResponseDTO;
import com.oasys.posasset.dto.StockOverviewoverAll;
import com.oasys.posasset.dto.StockoverviewDTO;
import com.oasys.posasset.dto.placeholderDTO;
import com.oasys.posasset.entity.EALDeclarationEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALStockEntity;
import com.oasys.posasset.mapper.StockOverviewMapper;
import com.oasys.posasset.repository.EALDeclarationRepository;
import com.oasys.posasset.repository.EALRequestLogRepository;
import com.oasys.posasset.repository.EALRequestMapRepository;
import com.oasys.posasset.repository.EALRequestRepository;
import com.oasys.posasset.repository.EALStockRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EALStockOverviewService {

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
	RestTemplate restTemplate;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;

	@Autowired
	StockOverviewMapper stockoverviewmapper;

	@Value("${spring.common.devtoken}")
	private String token;

	@Value("${spring.common.stockbarcode}")
	private String stockurl;

	@Autowired
	EalWastageRepository ealwastagerepo;

	@Autowired
	EALDeclarationRepository ealdecrepo;

	public GenericResponse getsubPagesearchNewByFilterstock(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALStockEntity> list = this.getSubRecordsByFilterDTO1stock(requestData);
//		List<EALStockEntity> unique = list.stream()
//                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(EALRequestMapEntity::getTotalnumofBarcode))),
//                                           ArrayList::new));
		List<EALStockEntity> list1 = this.getSubRecordsByFilterDTO2stock(requestData);

		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}

//		
//		List<EalRequestMapResponseDTO> dtoList = list.stream().map(stockoverviewmapper::entityToResponseDTOstockoverview)
//				.collect(Collectors.toList());
		if (!list.isEmpty()) {
			List<EalRequestMapResponseDTO> setlist = new ArrayList<EalRequestMapResponseDTO>();
			List<EalRequestMapResponseDTO> setlistBal = new ArrayList<EalRequestMapResponseDTO>();
			if (Objects.nonNull(requestData.getFilters().get("codeType"))
					&& !requestData.getFilters().get("codeType").toString().trim().isEmpty()) {
				String codetype = requestData.getFilters().get("codeType").toString();
				if (codetype.equalsIgnoreCase("MAPPED")) {
					List<ReceviedBarQr> rece = ealstockrepository.getByoverStockreceived();
					list.stream().forEach(actuallist -> {
						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						// obj.setLicencenumber(actuallist.);
						obj.setStockApplnno(actuallist.getStockApplnno());
						obj.setCreatedDate(String.valueOf(actuallist.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(actuallist.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(actuallist.getPackagingSize());
						obj.setCartonSize(actuallist.getCartonSize());
						obj.setNoofBarcode(actuallist.getNoofBarcode());
						obj.setNoofQrcode(actuallist.getNoofQrcode());
						obj.setRemarks(actuallist.getRemarks());
						obj.setNoofBarcodereceived(actuallist.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(actuallist.getNoofQrcodereceived());
						obj.setNoofBarcodepending(actuallist.getNoofBarcodepending());
						obj.setNoofqrpending(actuallist.getNoofqrpending());
						// obj.setEalrequestApplnno(actuallist.);
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
						obj.setCreatedBy(actuallist.getCreatedBy());
						rece.stream().forEach(sumreceived -> {
							if (obj.getPackagingSize().equalsIgnoreCase(sumreceived.getPackageSize())) {
								obj.setNoofBarcodereceived(sumreceived.getNoofBarcodereceived());
								obj.setNoofQrcodereceived(sumreceived.getNoofQrcodereceived());
							}
						});

						setlist.add(obj);
					});

					EalRequestMapResponseDTO licobj = new EalRequestMapResponseDTO();
					setlist.stream().forEach(act -> {
						licobj.setLicencenumber(act.getLicenseNo());
					});

					String licencenumber = licobj.getLicencenumber();
					String codeType = "MAPPED";
					String fdate = "2023-01-01";
					String tdate = "2025-12-31";

					final Date fromDate;
					final Date toDate;

					try {
						fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
					} catch (ParseException e) {
						log.error("error occurred while parsing date : {}", e.getMessage());
						throw new InvalidDataValidation("Invalid date parameter passed");
					}
					try {
						toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
					} catch (ParseException e) {
						log.error("error occurred while parsing date : {}", e.getMessage());
						throw new InvalidDataValidation("Invalid date parameter passed");
					}
					setlist.stream().forEach(act -> {

						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						obj.setStockApplnno(act.getStockApplnno());
						obj.setCreatedDate(String.valueOf(act.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(act.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(act.getPackagingSize());
						obj.setCartonSize(act.getCartonSize());

						obj.setRemarks(act.getRemarks());
						obj.setNoofBarcodereceived(act.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(act.getNoofQrcodereceived());
						obj.setNoofBarcodeBalance((act.getNoofBarcodereceived()));
						obj.setNoofQrcodeBalance(act.getNoofQrcodereceived());

						obj.setNoofBarcode(act.getNoofBarcode());
						obj.setNoofQrcode(act.getNoofQrcode());
						obj.setNoofBarcodepending(act.getNoofBarcodepending());
						obj.setNoofqrpending(act.getNoofqrpending());
						// obj.setEalrequestApplnno(actuallist.);
						obj.setUnmappedType(act.getUnmappedType());
						obj.setTotalnumofBarcode(act.getTotalnumofBarcode());
						obj.setTotalnumofQrcode(act.getTotalnumofQrcode());
						obj.setTotalnumofRoll(act.getTotalnumofRoll());
						obj.setStockApplnno(act.getStockApplnno());
						obj.setStockDate(act.getStockDate());
						obj.setCodeType(act.getCodeType());
						obj.setOpenstockApplnno(act.getOpenstockApplnno());
						obj.setFlag(act.isFlag());
						obj.setNoofBarcode(act.getNoofBarcode());
						obj.setNoofQrcode(act.getNoofQrcode());
						obj.setLicencenumber(act.getLicencenumber());
						obj.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
						obj.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
						obj.setLicenseNo(act.getLicenseNo());
						obj.setLicencenumber(obj.getLicenseNo());
						try {
							String licNumber = obj.getLicencenumber();
							String packagesize = obj.getPackagingSize();
							Long createby = act.getCreatedBy();
							Integer[] usedbarcodeun = { 0 };
							Integer[] usedqrcodeun = { 0 };
							List<EALWastageUsage> ealusagewastage = ealwastagerepo
									.getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSizeAndCreatedBy(
											licNumber, fromDate, toDate, codeType, packagesize, createby);
							ealusagewastage.forEach(action -> {
								usedbarcodeun[0] += action.getUsedbarcode();
								usedqrcodeun[0] += action.getUsedqrcode();
								Integer barcodereceived = obj.getNoofBarcodereceived();
								Integer qrcodereceived = obj.getNoofQrcodereceived();
								Integer balancebar = barcodereceived - usedbarcodeun[0];
								Integer balanceQr = qrcodereceived - usedqrcodeun[0];
								obj.setNoofBarcodeBalance(balancebar);
								obj.setNoofQrcodeBalance(balanceQr);
							});
						} catch (Exception e) {
							log.info("Mapped Error :::");
						}
						setlistBal.add(obj);
					});

				}

				else {
					List<ReceviedBarQr> receunmap = ealstockrepository.getByoverStockreceivedunmap();
					EalRequestMapResponseDTO licobj = new EalRequestMapResponseDTO();
					list.stream().forEach(act -> {

						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						obj.setLicenseNo(act.getLicenseNo());
						obj.setStockApplnno(act.getStockApplnno());
						obj.setCreatedDate(String.valueOf(act.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(act.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(act.getPackagingSize());
						obj.setCartonSize(act.getCartonSize());
						// obj.setLicenseNo(act.getLicenseNo());
						obj.setRemarks(act.getRemarks());
						obj.setNoofBarcodereceived(act.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(act.getNoofQrcodereceived());
						obj.setNoofBarcodeBalance((act.getNoofBarcodereceived()));
						obj.setNoofQrcodeBalance(act.getNoofQrcodereceived());

						obj.setNoofBarcode(act.getNoofBarcode());
						obj.setNoofQrcode(act.getNoofQrcode());
						obj.setNoofBarcodepending(act.getNoofBarcodepending());
						obj.setNoofqrpending(act.getNoofqrpending());
						// obj.setEalrequestApplnno(actuallist.);
						obj.setUnmappedType(act.getUnmappedType());
						obj.setTotalnumofBarcode(act.getTotalnumofBarcode());
						obj.setTotalnumofQrcode(act.getTotalnumofQrcode());
						obj.setTotalnumofRoll(act.getTotalnumofRoll());
						obj.setStockApplnno(act.getStockApplnno());
						obj.setStockDate(act.getStockDate());
						obj.setCodeType(act.getCodeType());
						obj.setOpenstockApplnno(act.getOpenstockApplnno());
						obj.setFlag(act.isFlag());
						obj.setNoofBarcode(act.getNoofBarcode());
						obj.setNoofQrcode(act.getNoofQrcode());
						obj.setLicencenumber(obj.getLicenseNo());
						obj.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
						obj.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
						obj.setCreatedBy(act.getCreatedBy());
						receunmap.stream().forEach(sumreceivedunmap -> {
							if (obj.getUnmappedType().equalsIgnoreCase(sumreceivedunmap.getUnmappedType())) {
								obj.setNoofBarcodereceived(sumreceivedunmap.getNoofBarcodereceived());
								obj.setNoofQrcodereceived(sumreceivedunmap.getNoofQrcodereceived());
							}
						});

						setlist.add(obj);
					});

					setlist.stream().forEach(act -> {
						licobj.setLicencenumber(act.getLicenseNo());
					});

					String licencenumber = licobj.getLicencenumber();
					String codeTypeunmap = "UNMAPPED";
					String fdate = "2023-01-01";
					String tdate = "2024-12-31";
					final Date fromDate;
					final Date toDate;
					try {
						fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
					} catch (ParseException e) {
						log.error("error occurred while parsing date : {}", e.getMessage());
						throw new InvalidDataValidation("Invalid date parameter passed");
					}
					try {
						toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
					} catch (ParseException e) {
						log.error("error occurred while parsing date : {}", e.getMessage());
						throw new InvalidDataValidation("Invalid date parameter passed");
					}

//					EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
//					placeholderDTO ho = new placeholderDTO();
//					ho.setCodeTypeValue("'" + codeType + "'");
//					ho.setFromDate("'" + fromDate + "'");
//					ho.setToDate("'" + toDate + "'");
//					ho.setLicenseNumber("'" + licencenumber + "'");
//					HttpHeaders headers = new HttpHeaders();
//					ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
//					ealwastagedto.setEntityCode("brew_admin");
//					ealwastagedto.setPlaceholderKeyValueMap(ho);
//					ObjectMapper mapper = new ObjectMapper();
//					String bearertoken = null;
//					JsonNode actualObj = null;
//					String response = null;
//
//					List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
//					EALUsageDTO ealq = new EALUsageDTO();
//					List<EALUsageDTO> ealscmbal = new ArrayList<EALUsageDTO>();
//					StringBuffer uri = new StringBuffer(stockurl);
//					if (uri != null) {
//
//						String access_token = headerRequest.getHeader("X-Authorization");
//						headers.set("X-Authorization", access_token);
//						headers.set("Authorization", "Bearer " + token);
//						RestTemplate restTemplate = new RestTemplate();
//						headers.set("X-Authorization", access_token);
//						headers.set("Authorization", "Bearer " + token);
//						HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
//						response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
//								.getBody();
//						log.info("=======EALStockService catch block============" + response);
//						try {
//							actualObj = mapper.readTree(response);
//						} catch (JsonProcessingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Integer usedbarcode = 0;
//						Integer usedqrcode = 0;
//						Integer wastagebarcode = 0;
//						Integer wastageqrcode = 0;
//						String unmappedtype = null;
//						final JsonNode arrNodeun = actualObj.get("responseContent");
//						if (arrNodeun.isArray()) {
//							EALUsageDTO usgae = new EALUsageDTO();
//							EalRequestMapResponseDTO hj = new EalRequestMapResponseDTO();
//							for (final JsonNode objNode : arrNodeun) {
//								unmappedtype = null;
//								EALUsageDTO eal = new EALUsageDTO();
//								char quotes = '"';
//								String type = objNode.get("type").toString();
//								String casettype = quotes + "CASE" + quotes;
//								usedbarcode = objNode.get("used_barcode").intValue();
//								usedqrcode = objNode.get("used_qrcode").intValue();
//								wastagebarcode = objNode.get("wastage_barcode").intValue();
//								wastageqrcode = objNode.get("wastage_qrcode").intValue();
//								if (type.equalsIgnoreCase(casettype)) {
//									unmappedtype = "BarCode and QRCode";
//									hj.setUnmappedType(unmappedtype);
//								} else {
//									unmappedtype = "Mono Carton";
//									hj.setUnmappedType(unmappedtype);
//								}
//
//								eal.setPackagesize(hj.getUnmappedType());
//								eal.setUsedbarcode(usedbarcode);
//								eal.setUsedqrcode(usedqrcode);
//								eal.setWastagebarcode(wastagebarcode);
//								eal.setWastageqrcode(wastageqrcode);
//								ealscmbal.add(eal);
//
//							}
//
//						}
//					}
//
//					final JsonNode arrNodeun = actualObj.get("responseContent");

					setlist.stream().forEach(actuallist -> {
						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						obj.setLicencenumber(actuallist.getLicenseNo());
						obj.setStockApplnno(actuallist.getStockApplnno());
						obj.setCreatedDate(String.valueOf(actuallist.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(actuallist.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(actuallist.getPackagingSize());
						obj.setCartonSize(actuallist.getCartonSize());
						obj.setNoofBarcode(actuallist.getNoofBarcode());
						obj.setNoofQrcode(actuallist.getNoofQrcode());
						obj.setRemarks(actuallist.getRemarks());
						obj.setNoofBarcodereceived(actuallist.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(actuallist.getNoofQrcodereceived());
						obj.setNoofBarcodeBalance((actuallist.getNoofBarcodereceived()));
						obj.setNoofQrcodeBalance(actuallist.getNoofQrcodereceived());
						obj.setNoofBarcodepending(actuallist.getNoofBarcodepending());
						obj.setNoofqrpending(actuallist.getNoofqrpending());
						obj.setUnmappedType(actuallist.getUnmappedType());
						obj.setTotalnumofBarcode(actuallist.getTotalnumofBarcode());
						obj.setTotalnumofQrcode(actuallist.getTotalnumofQrcode());
						obj.setTotalnumofRoll(actuallist.getTotalnumofRoll());
						obj.setStockApplnno(actuallist.getStockApplnno());
						obj.setStockDate(actuallist.getStockDate());
						obj.setCodeType(actuallist.getCodeType());
						obj.setOpenstockApplnno(actuallist.getOpenstockApplnno());
						obj.setFlag(actuallist.isFlag());
						obj.setNoofBarcodedamaged(actuallist.getNoofBarcodedamaged());
						obj.setNoofQrcodedamaged(actuallist.getNoofQrcodedamaged());
						try {
							String licNumber = obj.getLicencenumber();
							String packagesize = obj.getPackagingSize();
							Long createby = obj.getCreatedBy();
							Integer[] usedbarcodeun = { 0 };
							Integer[] usedqrcodeun = { 0 };
							List<EALWastageUsage> ealusagewastage = ealwastagerepo
									.getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSizeAndCreatedBy(
											licNumber, fromDate, toDate, codeTypeunmap, packagesize, createby);
							ealusagewastage.forEach(action -> {
								usedbarcodeun[0] += action.getUsedbarcode();
								usedqrcodeun[0] += action.getUsedqrcode();
								Integer barcodereceived = obj.getNoofBarcodereceived();
								Integer qrcodereceived = obj.getNoofQrcodereceived();
								Integer balancebar = barcodereceived - usedbarcodeun[0];
								Integer balanceQr = qrcodereceived - usedqrcodeun[0];
								obj.setNoofBarcodeBalance(balancebar);
								obj.setNoofQrcodeBalance(balanceQr);
							});
						} catch (Exception e) {
							log.info("Unmapped Error::");
						}
//						Integer usedbarcodeun = 0;
//						Integer usedqrcodeun = 0;
//						for (final JsonNode objNode : arrNodeun) {
//							EALUsageDTO eal = new EALUsageDTO();
//							usedbarcodeun += objNode.get("used_barcode").intValue();
//							usedqrcodeun += objNode.get("used_qrcode").intValue();
//							Integer barcodereceove = obj.getNoofBarcodereceived();
//							Integer qrcoderecieved = obj.getNoofQrcodereceived();
//							Integer balancebar = barcodereceove - usedbarcodeun;
//							Integer balanceQr = qrcoderecieved - usedqrcodeun;
//							obj.setNoofBarcodeBalance((balancebar));
//							obj.setNoofQrcodeBalance(balanceQr);
//							log.info("count loop" + balanceQr);
//						}
						setlistBal.add(obj);

					});

				}
			}

			paginationResponseDTO.setContents(setlistBal);
		}
		Long count1 = (long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list.size()) ? list.size() : null);
		paginationResponseDTO.setTotalElements(count1);
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public List<EALStockEntity> getSubRecordsByFilterDTO1stock(PaginationRequestDTO filterRequestDTO)
			throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALStockEntity> cq = cb.createQuery(EALStockEntity.class);
		Root<EALStockEntity> from = cq.from(EALStockEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALStockEntity> typedQuery = null;
		addSubCriteriastock(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get(MODIFIED_DATE)));
		}
		if (Objects.nonNull(filterRequestDTO.getPageNo()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {

			String codetype = filterRequestDTO.getFilters().get("codeType").toString();
			if (codetype.equalsIgnoreCase("UN-MAPPED")) {
				Expression<String> mainModuleIds = from.get("unmappedType");
				typedQuery = entityManager.createQuery(cq.groupBy(mainModuleIds))
						.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
						.setMaxResults(filterRequestDTO.getPaginationSize());
			} else {
				Expression<String> mainModuleIds = from.get("packagingSize");
				typedQuery = entityManager.createQuery(cq.groupBy(mainModuleIds))
						.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
						.setMaxResults(filterRequestDTO.getPaginationSize());
			}

		} else {
			typedQuery = entityManager.createQuery(cq);
		}
		return typedQuery.getResultList();
	}

	public List<EALStockEntity> getSubRecordsByFilterDTO2stock(PaginationRequestDTO filterRequestDTO)
			throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EALStockEntity> cq = cb.createQuery(EALStockEntity.class);
		Root<EALStockEntity> from = cq.from(EALStockEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<EALStockEntity> typedQuery1 = null;
		addSubCriteriastock(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(DESC)) {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		}

		String codetype = filterRequestDTO.getFilters().get("codeType").toString();

		if (codetype.equalsIgnoreCase("UN-MAPPED")) {
			Expression<String> mainModuleIds = from.get("unmappedType");
			typedQuery1 = entityManager.createQuery(cq.groupBy(mainModuleIds));
		} else {
			Expression<String> mainModuleIds = from.get("packagingSize");
			typedQuery1 = entityManager.createQuery(cq.groupBy(mainModuleIds));
		}

		return typedQuery1.getResultList();
	}

	private void addSubCriteriastock(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<EALStockEntity> from) throws ParseException {
		Date fromDate = null;
		;
		if (Objects.nonNull(filterRequestDTO.getFilters().get("fromDate"))
				&& !filterRequestDTO.getFilters().get("fromDate").toString().trim().isEmpty()) {

			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(filterRequestDTO.getFilters().get("fromDate").toString() + " " + "00:00:00");

		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("toDate"))
				&& !filterRequestDTO.getFilters().get("toDate").toString().trim().isEmpty()) {

			Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(filterRequestDTO.getFilters().get("toDate").toString() + " " + "23:59:59");
			list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockApplnno"))
				&& !filterRequestDTO.getFilters().get("stockApplnno").toString().trim().isEmpty()) {

			String stockapplnno = filterRequestDTO.getFilters().get("stockApplnno").toString();
			list.add(cb.equal(from.get("stockApplnno"), stockapplnno));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseNumber"))
				&& !filterRequestDTO.getFilters().get("licenseNumber").toString().trim().isEmpty()) {

			String licenceno = filterRequestDTO.getFilters().get("licenseNumber").toString();
			list.add(cb.equal(from.get("licenseNo"), licenceno));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("codeType"))
				&& !filterRequestDTO.getFilters().get("codeType").toString().trim().isEmpty()) {

			String codetype = filterRequestDTO.getFilters().get("codeType").toString();
			list.add(cb.equal(from.get("codeType"), codetype));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("stockDate"))
				&& !filterRequestDTO.getFilters().get("stockDate").toString().trim().isEmpty()) {

			String stockdate = (filterRequestDTO.getFilters().get("stockDate").toString());
			list.add(cb.equal(from.get("stockDate"), stockdate));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("unitCode"))
				&& !filterRequestDTO.getFilters().get("unitCode").toString().trim().isEmpty()) {

			String unitCode = (filterRequestDTO.getFilters().get("unitCode").toString());
			list.add(cb.equal(from.get("unitCode"), unitCode));
		}

		if ((Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
				&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty())
				&& (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
						&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty())) {
			Long createdby = Long.valueOf(filterRequestDTO.getFilters().get("createdBy").toString());

			list.add(cb.equal(from.get("createdBy"), createdby));
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

	public GenericResponse stockgetBylic(String fromDate, String toDate, String licenseNo, String codetype) {
		List<EALRequestEntity> ealrequstlist = new ArrayList<EALRequestEntity>();

		final Date f;
		final Date tDate;
		try {
			f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			tDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<EALRequestEntity> ealrequestEntity = ealrequestRepository.getByLicenseNoAndCodeType(licenseNo, f, tDate,
				codetype);

		ealrequstlist.addAll(ealrequestEntity);

//			if(!ealrequestEntity.isEmpty()) {
//				List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);
//				
//				EALRequestEntity ealrequestEntity1 =new EALRequestEntity(); 	
//				Long ealrequestid;
//				DeptOptional.stream().forEach(action -> {
//				ealrequestEntity1.setEalrequestId(action.getEalrequestId().getId());
//				});
//				ealrequstlist.add(ealrequestEntity1);
//			return Library.getSuccessfulResponse(ealrequstlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//						ErrorMessages.RECORED_FOUND);
//			} 
//		
//		else {
//				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);
//			}

		return Library.getSuccessfulResponse(ealrequstlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

//		public GenericResponse stockgetById(Long id,String fromDate,String toDate,String codeTypeValue,String licenseNumber) {
//			List<EALRequestEntity>ealrequstlist=new ArrayList<EALRequestEntity>();
//			List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);
//			List<StockoverviewDTO> stocklistall=new ArrayList<StockoverviewDTO>();	
//			EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
//			placeholderDTO ho=new placeholderDTO();
//			ho.setCodeTypeValue(codeTypeValue);
//			ho.setFromDate(fromDate);
//			ho.setToDate(toDate);
//			ho.setLicenseNumber(licenseNumber);
//			HttpHeaders headers = new HttpHeaders();
//			ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
//			ealwastagedto.setEntityCode("brew_admin");
//			ealwastagedto.setPlaceholderKeyValueMap(ho);
//			ObjectMapper mapper = new ObjectMapper();
//		    String bearertoken = null;
//	        JsonNode actualObj = null;
//			String response =null;
////			final Date fromDate;
////			final Date toDate;
////			String fdate=null;
////			String tdate=null;
////			try {
////		    fdate=String.valueOf(requestDTO.getStartDate().toString());
////			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
////			} catch (ParseException e) {
////				log.error("error occurred while parsing date : {}", e.getMessage());
////				throw new InvalidDataValidation("Invalid date parameter passed");
////			}
////			try {
////				tdate=String.valueOf(requestDTO.getEndDate().toString());
////				toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
////			} catch (ParseException e) {
////				log.error("error occurred while parsing date : {}", e.getMessage());
////				throw new InvalidDataValidation("Invalid date parameter passed");
////			}
//					
//	   		List<EALAvailable> stockinlist=null;
//			List<EALUsageDTO> ealavailablelist=new ArrayList<EALUsageDTO>();
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
//					if (arrNode.isArray()) {
//						DeptOptional.stream().forEach(stocklist -> {
//							for (final JsonNode objNode : arrNode) {
//								EALUsageDTO eal = new EALUsageDTO();
//								String packagesize = objNode.get("packaging_size_value").toString();
//								char quotes = '"';
//								String bottlepackages = quotes + stocklist.getPackagingSize() + quotes;
//								Integer usedbarcode = objNode.get("used_barcode").intValue();
//								Integer usedqrcode = objNode.get("used_qrcode").intValue();
//								Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
//								Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
//								if (bottlepackages.equalsIgnoreCase(packagesize)) {
//									StockoverviewDTO stock = new StockoverviewDTO();
//									stock.setId(stocklist.getId());
//									stock.setPackagingSize(stocklist.getPackagingSize());
//									stock.setCartonSize(stocklist.getCartonSize());
//									stock.setNoofBarcode(stocklist.getNoofBarcode());
//									stock.setNoofQrcode(stocklist.getNoofQrcode());
//									stock.setEalrequestId(stocklist.getEalrequestId().getId());
//									stock.setUnmappedType(stocklist.getUnmappedType());
//									stock.setCodeType(stocklist.getCodeType());
//									stock.setNoofBarcodepending(stocklist.getNoofBarcodepending());
//									stock.setNoofBarcodereceived(stocklist.getNoofBarcodereceived());
//									stock.setNoofQrcodereceived(stocklist.getNoofQrcodereceived());
//									stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
//									stock.setNoofqrpending(stocklist.getNoofqrpending());
//									stock.setStockApplnno(stocklist.getStockApplnno());
//									stock.setStockDate(stocklist.getStockDate());
//									stock.setLicencenumber(stocklist.getLicenseNo());
//									stock.setUsedBarcode(usedbarcode);
//									stock.setUsedQrcode(usedqrcode);
//									stock.setWastageBarcode(wastagebarcode);
//									stock.setWastageQrcode(wastageqrcode);
//									stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
//									stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
//									stocklistall.add(stock);
//								}
//
//							}
//
//						});
//
//					}
//
//				}
//			} catch (Exception exception) {
//				// general error
//				log.error("=======EALStockService catch block============", exception);
//			}
//			return Library.getSuccessfulResponse(stocklistall, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//						ErrorMessages.RECORED_FOUND);
//			} 
//		

	public GenericResponse stockgetByIdmapunmap(String applnno, String fromDate, String toDate, String codeTypeValue,
			String licenseNumber, String size, String unmappedType, String codeType) {
		List<EALStockEntity> ealoptional = ealstockrepository.getByEALAPPlnAndCodeType(applnno, codeType);
		EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(ealstock -> {
			DTO.setEalrequestApplnno(ealstock.getEalrequestapplno());
			DTO.setStockApplnno(ealstock.getStockApplnno());
		});
		List<EALStockEntity> DeptOptional = null;

		List<BarQrOverviewDTO> balancedetails = ealstockrepository.getByStockApplnAll(DTO.getEalrequestApplnno());

		if (size != null) {
			DeptOptional = ealstockrepository.getByStockApplnnoAndPackagingSize(DTO.getStockApplnno(), size);
		} else if (unmappedType != null) {
			DeptOptional = ealstockrepository.getByStockApplnnoAndUnmappedType(DTO.getStockApplnno(), unmappedType);
		}

		List<StockoverviewDTO> stocklistall = new ArrayList<StockoverviewDTO>();
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
		placeholderDTO ho = new placeholderDTO();
		ho.setCodeTypeValue("'" + codeTypeValue + "'");
		ho.setFromDate("'" + fromDate + "'");
		ho.setToDate("'" + toDate + "'");
		ho.setLicenseNumber("'" + licenseNumber + "'");
		HttpHeaders headers = new HttpHeaders();
		ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
		ealwastagedto.setEntityCode("brew_admin");
		ealwastagedto.setPlaceholderKeyValueMap(ho);
		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;
		JsonNode actualObj = null;
		String response = null;

		try {

			if (codeTypeValue.equalsIgnoreCase("MAPPED")) {

				StringBuffer uri = new StringBuffer(stockurl);
				if (uri != null) {

					String access_token = headerRequest.getHeader("X-Authorization");
					headers.set("X-Authorization", access_token);
					headers.set("Authorization", "Bearer " + token);
					RestTemplate restTemplate = new RestTemplate();
					headers.set("X-Authorization", access_token);
					headers.set("Authorization", "Bearer " + token);
					HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
					response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
							.getBody();
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
					if (arrNode.isArray()) {
						DeptOptional.stream().forEach(stocklist -> {
							for (final JsonNode objNode : arrNode) {
								EALUsageDTO eal = new EALUsageDTO();
								String packagesize = objNode.get("packaging_size_value").toString();
								char quotes = '"';
								String bottlepackages = quotes + stocklist.getPackagingSize() + quotes;
								Integer usedbarcode = objNode.get("used_barcode").intValue();
								Integer usedqrcode = objNode.get("used_qrcode").intValue();
								Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
								Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();

								if (bottlepackages.equalsIgnoreCase(packagesize)) {
									StockoverviewDTO stock = new StockoverviewDTO();
									stock.setId(stocklist.getId());
									stock.setPackagingSize(stocklist.getPackagingSize());
									stock.setCartonSize(stocklist.getCartonSize());
									// stock.setEalrequestId(stocklist.getEalrequestId().getId());
									stock.setUnmappedType(stocklist.getUnmappedType());
									stock.setCodeType(stocklist.getCodeType());
									balancedetails.parallelStream().forEach(act -> {
										EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
										receivedetails.setPackagingSize(act.getPackagingSize());
										receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
										receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
										receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
										receivedetails.setNoofqrpending(act.getNoofqrpending());
										receivedetails.setNoofBarcode(act.getNoofBarcode());
										receivedetails.setNoofQrcode(act.getNoofQrcode());
										receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
										receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
										if (stocklist.getPackagingSize()
												.equalsIgnoreCase(receivedetails.getPackagingSize())) {
											stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
											stock.setNoofqrpending(receivedetails.getNoofqrpending());
											stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
											stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
											stock.setNoofBarcode(receivedetails.getNoofBarcode());
											stock.setNoofQrcode(receivedetails.getNoofQrcode());
											stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
											stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
										}
									});

									stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
									stock.setStockApplnno(stocklist.getStockApplnno());
									stock.setStockDate(stocklist.getStockDate());
									stock.setLicencenumber(stocklist.getLicenseNo());
									stock.setUsedBarcode(usedbarcode);
									stock.setUsedQrcode(usedqrcode);
									stock.setWastageBarcode(wastagebarcode);
									stock.setWastageQrcode(wastageqrcode);
									stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
									stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
									stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
									stocklistall.add(stock);
								}

							}

						});

					}

				}

			}

			else {

				StringBuffer uri = new StringBuffer(stockurl);
				if (uri != null) {

					String access_token = headerRequest.getHeader("X-Authorization");
					headers.set("X-Authorization", access_token);
					headers.set("Authorization", "Bearer " + token);
					RestTemplate restTemplate = new RestTemplate();
					headers.set("X-Authorization", access_token);
					headers.set("Authorization", "Bearer " + token);
					HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
					response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
							.getBody();
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
					Integer usedbarcode = 0;
					Integer usedqrcode = 0;
					Integer wastagebarcode = 0;
					Integer wastageqrcode = 0;
					String unmappedtype = null;
					final JsonNode arrNode = actualObj.get("responseContent");
					if (arrNode.isArray()) {
						EALUsageDTO usgae = new EALUsageDTO();
						EalRequestMapResponseDTO hj = new EalRequestMapResponseDTO();
						for (final JsonNode objNode : arrNode) {
							unmappedtype = null;
							EALUsageDTO eal = new EALUsageDTO();
							char quotes = '"';
							String type = objNode.get("type").toString();
							String casettype = quotes + "CASE" + quotes;
							usedbarcode += objNode.get("used_barcode").intValue();
							usedqrcode += objNode.get("used_qrcode").intValue();
							wastagebarcode += objNode.get("wastage_barcode").intValue();
							wastageqrcode += objNode.get("wastage_qrcode").intValue();
							if (type.equalsIgnoreCase(casettype)) {
								unmappedtype = "BarCode and QRCode";
								hj.setUnmappedType(unmappedtype);
							} else {
								unmappedtype = "Mono Carton";
								hj.setUnmappedType(unmappedtype);
							}

						}
						usgae.setUsedbarcode(usedbarcode);
						usgae.setUsedqrcode(usedqrcode);
						usgae.setWastagebarcode(wastagebarcode);
						usgae.setWastageqrcode(wastageqrcode);

						DeptOptional.stream().forEach(stocklist -> {
							if (stocklist.getUnmappedType().equalsIgnoreCase(hj.getUnmappedType())) {
								StockoverviewDTO stock = new StockoverviewDTO();
								stock.setId(stocklist.getId());
								stock.setPackagingSize(stocklist.getPackagingSize());
								stock.setCartonSize(stocklist.getCartonSize());
								// stock.setEalrequestId(stocklist.getEalrequestId().getId());
								stock.setUnmappedType(stocklist.getUnmappedType());
								stock.setCodeType(stocklist.getCodeType());
								balancedetails.parallelStream().forEach(act -> {
									EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
									receivedetails.setPackagingSize(act.getPackagingSize());
									receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
									receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
									receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
									receivedetails.setNoofqrpending(act.getNoofqrpending());
									receivedetails.setNoofBarcode(act.getNoofBarcode());
									receivedetails.setNoofQrcode(act.getNoofQrcode());
									receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
									receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
									receivedetails.setUnmappedType(act.getUnmappedType());
									if (stocklist.getUnmappedType()
											.equalsIgnoreCase(receivedetails.getUnmappedType())) {
										stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
										stock.setNoofqrpending(receivedetails.getNoofqrpending());
										stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
										stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
										stock.setNoofBarcode(receivedetails.getNoofBarcode());
										stock.setNoofQrcode(receivedetails.getNoofQrcode());
										stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
										stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
									}
								});

								stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
								stock.setStockApplnno(stocklist.getStockApplnno());
								stock.setStockDate(stocklist.getStockDate());
								stock.setLicencenumber(stocklist.getLicenseNo());
								stock.setUsedBarcode(usgae.getUsedbarcode());
								stock.setUsedQrcode(usgae.getUsedqrcode());
								stock.setWastageBarcode(usgae.getWastagebarcode());
								stock.setWastageQrcode(usgae.getWastageqrcode());
								stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
								stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
								stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
								stocklistall.add(stock);
							}

							// }

						});

					}

				}

			}

		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block============", exception);
		}
		return Library.getSuccessfulResponse(stocklistall, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse stockgetByIdOLD(String applnno, String fromDate, String toDate, String codeTypeValue,
			String licenseNumber) {
		// List<EALRequestEntity>ealrequstlist=new ArrayList<EALRequestEntity>();
		// List<EALRequestMapEntity> DeptOptional = ealrequestmapRepository.getById(id);
		// List<EalRequestMapResponseDTO> stockdetails=new
		// ArrayList<EalRequestMapResponseDTO>();
		// List<EALAvailable> stockinlist=null;
		// List<EALUsageDTO> ealavailablelist=new ArrayList<EALUsageDTO>();
		List<EALStockEntity> DeptOptional = null;

		List<EALStockEntity> ealoptional = ealstockrepository.getByEALAPPlnAndCodeType(applnno, codeTypeValue);
		EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(ealstock -> {
			DTO.setEalrequestApplnno(ealstock.getEalrequestapplno());
			DTO.setStockApplnno(ealstock.getStockApplnno());
		});

		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository
				.findByRequestedapplnNo(DTO.getEalrequestApplnno());

		List<BarQrOverviewDTO> balancedetails = ealstockrepository.getByStockApplnAll(DTO.getEalrequestApplnno());

		if (codeTypeValue.equalsIgnoreCase("MAPPED")) {
			DeptOptional = ealstockrepository.getByStockApplno(DTO.getStockApplnno());
		} else {
			DeptOptional = ealstockrepository.getByStockapllunmaptype(DTO.getStockApplnno());
		}

		List<StockoverviewDTO> stocklistall = new ArrayList<StockoverviewDTO>();
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
		placeholderDTO ho = new placeholderDTO();
		ho.setCodeTypeValue("'" + codeTypeValue.toString() + "'");
		ho.setFromDate("'" + fromDate.toString() + "'");
		ho.setToDate("'" + toDate.toString() + "'");
		String licensenum = ("'" + licenseNumber.toString().trim() + "'");
		ho.setLicenseNumber(licensenum.trim());
		ho.setCreatedBy(Long.valueOf(1));
		HttpHeaders headers = new HttpHeaders();
		ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
		ealwastagedto.setEntityCode("brew_admin");
		ealwastagedto.setPlaceholderKeyValueMap(ho);
		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;
		JsonNode actualObj = null;
		String response = null;

		try {
			if (codeTypeValue.equalsIgnoreCase("MAPPED")) {

				StringBuffer uri = new StringBuffer(stockurl);
				if (uri != null) {

					String access_token = headerRequest.getHeader("X-Authorization");
					headers.set("X-Authorization", access_token);
					headers.set("Authorization", "Bearer " + token);
					RestTemplate restTemplate = new RestTemplate();
					HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
					response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
							.getBody();
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
					if (arrNode.isArray()) {
						DeptOptional.stream().forEach(stocklist -> {
							for (final JsonNode objNode : arrNode) {
								EALUsageDTO eal = new EALUsageDTO();
								String packagesize = objNode.get("packaging_size_value").toString();
								char quotes = '"';
								String bottlepackages = quotes + stocklist.getPackagingSize() + quotes;
								String bottle = bottlepackages.replace(" ML", "").trim();
								Integer usedbarcode = objNode.get("used_barcode").intValue();
								Integer usedqrcode = objNode.get("used_qrcode").intValue();
								Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
								Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
								if (bottle.equalsIgnoreCase(packagesize)) {
									StockoverviewDTO stock = new StockoverviewDTO();
									stock.setId(stocklist.getId());
									stock.setPackagingSize(stocklist.getPackagingSize());
									stock.setCartonSize(stocklist.getCartonSize());
									// stock.setEalrequestId(stocklist.getEalrequestId().getId());
									stock.setUnmappedType(stocklist.getUnmappedType());
									stock.setCodeType(stocklist.getCodeType());
									balancedetails.parallelStream().forEach(act -> {
										EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
										receivedetails.setPackagingSize(act.getPackagingSize());
										receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
										receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
										receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
										receivedetails.setNoofqrpending(act.getNoofqrpending());
										receivedetails.setNoofBarcode(act.getNoofBarcode());
										receivedetails.setNoofQrcode(act.getNoofQrcode());
										receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
										receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
										if (stocklist.getPackagingSize()
												.equalsIgnoreCase(receivedetails.getPackagingSize())) {
											stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
											stock.setNoofqrpending(receivedetails.getNoofqrpending());
											stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
											stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
											stock.setNoofBarcode(receivedetails.getNoofBarcode());
											stock.setNoofQrcode(receivedetails.getNoofQrcode());
											stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
											stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
											stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
										}
									});

									// stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
									stock.setStockApplnno(stocklist.getStockApplnno());
									stock.setStockDate(stocklist.getStockDate());
									stock.setLicencenumber(stocklist.getLicenseNo());
									stock.setUsedBarcode(usedbarcode);
									stock.setUsedQrcode(usedqrcode);
									stock.setWastageBarcode(wastagebarcode);
									stock.setWastageQrcode(wastageqrcode);
									stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
									stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
									stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
									stock.setPrintingType(stocklist.getPrintingType());
									stocklistall.add(stock);
								}

								else if (bottlepackages.equalsIgnoreCase(packagesize)) {
									StockoverviewDTO stock = new StockoverviewDTO();
									stock.setId(stocklist.getId());
									stock.setPackagingSize(stocklist.getPackagingSize());
									stock.setCartonSize(stocklist.getCartonSize());
									// stock.setEalrequestId(stocklist.getEalrequestId().getId());
									stock.setUnmappedType(stocklist.getUnmappedType());
									stock.setCodeType(stocklist.getCodeType());
									balancedetails.parallelStream().forEach(act -> {
										EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
										receivedetails.setPackagingSize(act.getPackagingSize());
										receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
										receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
										receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
										receivedetails.setNoofqrpending(act.getNoofqrpending());
										receivedetails.setNoofBarcode(act.getNoofBarcode());
										receivedetails.setNoofQrcode(act.getNoofQrcode());
										receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
										receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
										if (stocklist.getPackagingSize()
												.equalsIgnoreCase(receivedetails.getPackagingSize())) {
											stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
											stock.setNoofqrpending(receivedetails.getNoofqrpending());
											stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
											stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
											stock.setNoofBarcode(receivedetails.getNoofBarcode());
											stock.setNoofQrcode(receivedetails.getNoofQrcode());
											stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
											stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
											stock.setNoofRollcodereceived(receivedetails.getNoofRollcodereceived());
										}
									});

									// stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
									stock.setStockApplnno(stocklist.getStockApplnno());
									stock.setStockDate(stocklist.getStockDate());
									stock.setLicencenumber(stocklist.getLicenseNo());
									stock.setUsedBarcode(usedbarcode);
									stock.setUsedQrcode(usedqrcode);
									stock.setWastageBarcode(wastagebarcode);
									stock.setWastageQrcode(wastageqrcode);
									stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
									stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
									stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
									stock.setPrintingType(stocklist.getPrintingType());
									stocklistall.add(stock);
								}

							}

						});

					}

				}
			}

			else {

				StringBuffer uri = new StringBuffer(stockurl);
				if (uri != null) {
					ho.setCodeTypeValue("'" + "UNMAPPED" + "'");
					ealwastagedto.setPlaceholderKeyValueMap(ho);
					String access_token = headerRequest.getHeader("X-Authorization");
					headers.set("X-Authorization", access_token);
					headers.set("Authorization", "Bearer " + token);
					RestTemplate restTemplate = new RestTemplate();
					// headers.set("X-Authorization", access_token);
					// headers.set("Authorization", "Bearer " + token);
					HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
					response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
							.getBody();
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
					Integer usedbarcode = 0;
					Integer usedqrcode = 0;
					Integer wastagebarcode = 0;
					Integer wastageqrcode = 0;
					String unmappedtype = null;
					final JsonNode arrNode = actualObj.get("responseContent");
					if (arrNode.isArray()) {
						EALUsageDTO usgae = new EALUsageDTO();
						EalRequestMapResponseDTO hj = new EalRequestMapResponseDTO();
						for (final JsonNode objNode : arrNode) {
							unmappedtype = null;
							EALUsageDTO eal = new EALUsageDTO();
							char quotes = '"';
							String type = objNode.get("type").toString();
							String casettype = quotes + "CASE" + quotes;
							usedbarcode += objNode.get("used_barcode").intValue();
							usedqrcode += objNode.get("used_qrcode").intValue();
							wastagebarcode += objNode.get("wastage_barcode").intValue();
							wastageqrcode += objNode.get("wastage_qrcode").intValue();
							if (type.equalsIgnoreCase(casettype)) {
								unmappedtype = "BarCode and QRCode";
								hj.setUnmappedType(unmappedtype);
							} else {
								unmappedtype = "Mono Carton";
								hj.setUnmappedType(unmappedtype);
							}

						}
						usgae.setUsedbarcode(usedbarcode);
						usgae.setUsedqrcode(usedqrcode);
						usgae.setWastagebarcode(wastagebarcode);
						usgae.setWastageqrcode(wastageqrcode);

						DeptOptional.stream().forEach(stocklist -> {
							if (stocklist.getUnmappedType().equalsIgnoreCase(hj.getUnmappedType())) {
								StockoverviewDTO stock = new StockoverviewDTO();
								stock.setId(stocklist.getId());
								stock.setPackagingSize(stocklist.getPackagingSize());
								stock.setCartonSize(stocklist.getCartonSize());
								stock.setUnmappedType(stocklist.getUnmappedType());
								stock.setCodeType(stocklist.getCodeType());
								stock.setPrintingType(stocklist.getPrintingType());
								List<BarQrOverviewDTO> balancedetailsunmap = ealstockrepository
										.getByStockApplnAndUnmappedTypeAndPrintingTypeAll(DTO.getEalrequestApplnno(),
												stock.getUnmappedType(), stock.getPrintingType());
								balancedetailsunmap.parallelStream().forEach(act -> {
									EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
									receivedetails.setPackagingSize(act.getPackagingSize());
									receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
									receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
									receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
									receivedetails.setNoofqrpending(act.getNoofqrpending());
									receivedetails.setNoofBarcode(act.getNoofBarcode());
									receivedetails.setNoofQrcode(act.getNoofQrcode());
									receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
									receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
									receivedetails.setUnmappedType(act.getUnmappedType());
									if (stocklist.getUnmappedType()
											.equalsIgnoreCase(receivedetails.getUnmappedType())) {
										stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
										stock.setNoofqrpending(receivedetails.getNoofqrpending());
										stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
										stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
										stock.setNoofBarcode(receivedetails.getNoofBarcode());
										stock.setNoofQrcode(receivedetails.getNoofQrcode());
										stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
										stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
										stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
									}
								});

								// stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
								stock.setStockApplnno(stocklist.getStockApplnno());
								stock.setStockDate(stocklist.getStockDate());
								stock.setLicencenumber(stocklist.getLicenseNo());
								stock.setUsedBarcode(usgae.getUsedbarcode());
								stock.setUsedQrcode(usgae.getUsedqrcode());
								stock.setWastageBarcode(usgae.getWastagebarcode());
								stock.setWastageQrcode(usgae.getWastageqrcode());
								stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
								stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
								stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
								stocklistall.add(stock);
							}
						});
					}
				}
			}

		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block============", exception);
		}

		if (stocklistall.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					"No Record Found SCM Packagesize");
		} else {
			return Library.getSuccessfulResponse(stocklistall, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

	}

	public GenericResponse stockgetById(String applnno, String fromDate, String toDate, String codeTypeValue,
			String licenseNumber) {
		List<EALStockEntity> DeptOptional = null;

		List<EALStockEntity> ealoptional = ealstockrepository.getByEALAPPlnAndCodeType(applnno, codeTypeValue);
		EalRequestMapResponseDTO DTO = new EalRequestMapResponseDTO();
		ealoptional.stream().forEach(ealstock -> {
			DTO.setEalrequestApplnno(ealstock.getEalrequestapplno());
			DTO.setStockApplnno(ealstock.getStockApplnno());
		});

		Optional<EALRequestEntity> ealrequestEntity = ealrequestRepository
				.findByRequestedapplnNo(DTO.getEalrequestApplnno());

		List<BarQrOverviewDTO> balancedetails = ealstockrepository.getByStockApplnAll(DTO.getEalrequestApplnno());

		if (codeTypeValue.equalsIgnoreCase("MAPPED")) {
			DeptOptional = ealstockrepository.getByStockApplno(DTO.getStockApplnno());
		} else {
			DeptOptional = ealstockrepository.getByStockapllunmaptype(DTO.getStockApplnno());
		}

		List<StockoverviewDTO> stocklistall = new ArrayList<StockoverviewDTO>();
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
//		placeholderDTO ho = new placeholderDTO();
//		ho.setCodeTypeValue("'" + codeTypeValue.toString() + "'");
//		ho.setFromDate("'" + fromDate.toString() + "'");
//		ho.setToDate("'" + toDate.toString() + "'");
//		String licensenum = ("'" + licenseNumber.toString().trim() + "'");
//		ho.setLicenseNumber(licensenum.trim());
//		ho.setCreatedBy(Long.valueOf(1));
//		HttpHeaders headers = new HttpHeaders();
//		ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
//		ealwastagedto.setEntityCode("brew_admin");
//		ealwastagedto.setPlaceholderKeyValueMap(ho);
//		ObjectMapper mapper = new ObjectMapper();
//		String bearertoken = null;
//		JsonNode actualObj = null;
		String response = null;

		try {
			if (codeTypeValue.equalsIgnoreCase("MAPPED")) {

//				StringBuffer uri = new StringBuffer(stockurl);
//				if (uri != null) {
//
//					String access_token = headerRequest.getHeader("X-Authorization");
//					headers.set("X-Authorization", access_token);
//					headers.set("Authorization", "Bearer " + token);
//					RestTemplate restTemplate = new RestTemplate();
//					HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
//					response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
//							.getBody();
//					log.info("=======EALStockService catch block============" + response);
//					try {
//						actualObj = mapper.readTree(response);
//					} catch (JsonProcessingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

				final Date fromDateQ;
				final Date toDateQ;

				try {
					fromDateQ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
				try {
					toDateQ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate + " " + "23:59:59");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}

//					final JsonNode arrNode = actualObj.get("responseContent");
//					if (arrNode.isArray()) {
				DeptOptional.stream().forEach(stocklist -> {

					try {
						String licNumber = stocklist.getLicenseNo();
						String packagesize = stocklist.getPackagingSize();
						Long createby = stocklist.getCreatedBy();
						String codetype = stocklist.getCodeType();
						List<EALWastageUsage> ealusagewastage = ealwastagerepo
								.getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSize(licNumber,
										fromDateQ, toDateQ, codetype, packagesize);
						ealusagewastage.forEach(action -> {
							Integer usedbarcode = action.getUsedbarcode();
							Integer usedqrcode = action.getUsedqrcode();
							Integer wastagebarcode = action.getWastagecases();
							Integer wastageqrcode = action.getWastageqrcode();

//							for (final JsonNode objNode : arrNode) {
//								EALUsageDTO eal = new EALUsageDTO();
//								String packagesize = objNode.get("packaging_size_value").toString();
//								char quotes = '"';
//								String bottlepackages = quotes + stocklist.getPackagingSize() + quotes;
//								String bottle = bottlepackages.replace(" ML", "").trim();
//								Integer usedbarcode = objNode.get("used_barcode").intValue();
//								Integer usedqrcode = objNode.get("used_qrcode").intValue();
//								Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
//								Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
//								if (bottle.equalsIgnoreCase(packagesize)) {
							StockoverviewDTO stock = new StockoverviewDTO();
							stock.setId(stocklist.getId());
							stock.setPackagingSize(stocklist.getPackagingSize());
							stock.setCartonSize(stocklist.getCartonSize());
							// stock.setEalrequestId(stocklist.getEalrequestId().getId());
							stock.setUnmappedType(stocklist.getUnmappedType());
							stock.setCodeType(stocklist.getCodeType());
							balancedetails.parallelStream().forEach(act -> {
								EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
								receivedetails.setPackagingSize(act.getPackagingSize());
								receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
								receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
								receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
								receivedetails.setNoofqrpending(act.getNoofqrpending());
								receivedetails.setNoofBarcode(act.getNoofBarcode());
								receivedetails.setNoofQrcode(act.getNoofQrcode());
								receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
								receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
								if (stocklist.getPackagingSize().equalsIgnoreCase(receivedetails.getPackagingSize())) {
									stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
									stock.setNoofqrpending(receivedetails.getNoofqrpending());
									stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
									stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
									stock.setNoofBarcode(receivedetails.getNoofBarcode());
									stock.setNoofQrcode(receivedetails.getNoofQrcode());
									stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
									stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
									stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
								}
							});

							// stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
							stock.setStockApplnno(stocklist.getStockApplnno());
							stock.setStockDate(stocklist.getStockDate());
							stock.setLicencenumber(stocklist.getLicenseNo());
							stock.setUsedBarcode(usedbarcode);
							stock.setUsedQrcode(usedqrcode);
							stock.setWastageBarcode(wastagebarcode);
							stock.setWastageQrcode(wastageqrcode);
							stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
							stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
							stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
							stock.setPrintingType(stocklist.getPrintingType());
							stocklistall.add(stock);
//								}

//							}

						});
					} catch (Exception e) {
						log.info("Mapped Error :::");
					}

				});

//					}

				// }
			}

			else {

//				StringBuffer uri = new StringBuffer(stockurl);
//				if (uri != null) {
//					ho.setCodeTypeValue("'" + "UNMAPPED" + "'");
//					ealwastagedto.setPlaceholderKeyValueMap(ho);
//					String access_token = headerRequest.getHeader("X-Authorization");
//					headers.set("X-Authorization", access_token);
//					headers.set("Authorization", "Bearer " + token);
//					RestTemplate restTemplate = new RestTemplate();
//					// headers.set("X-Authorization", access_token);
//					// headers.set("Authorization", "Bearer " + token);
//					HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
//					response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
//							.getBody();
//					log.info("=======EALStockService catch block============" + response);
//					try {
//						actualObj = mapper.readTree(response);
//					} catch (JsonProcessingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

				final Date fromDateQ;
				final Date toDateQ;

				try {
					fromDateQ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDate + " " + "00:00:00");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}
				try {
					toDateQ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate + " " + "23:59:59");
				} catch (ParseException e) {
					log.error("error occurred while parsing date : {}", e.getMessage());
					throw new InvalidDataValidation("Invalid date parameter passed");
				}

//					Integer usedbarcode = 0;
//					Integer usedqrcode = 0;
//					Integer wastagebarcode = 0;
//					Integer wastageqrcode = 0;
//					String unmappedtype = null;
//					final JsonNode arrNode = actualObj.get("responseContent");
//					if (arrNode.isArray()) {
//						EALUsageDTO usgae = new EALUsageDTO();
//						EalRequestMapResponseDTO hj = new EalRequestMapResponseDTO();
//						for (final JsonNode objNode : arrNode) {
//							unmappedtype = null;
//							EALUsageDTO eal = new EALUsageDTO();
//							char quotes = '"';
//							String type = objNode.get("type").toString();
//							String casettype = quotes + "CASE" + quotes;
//							usedbarcode += objNode.get("used_barcode").intValue();
//							usedqrcode += objNode.get("used_qrcode").intValue();
//							wastagebarcode += objNode.get("wastage_barcode").intValue();
//							wastageqrcode += objNode.get("wastage_qrcode").intValue();
//							if (type.equalsIgnoreCase(casettype)) {
//								unmappedtype = "BarCode and QRCode";
//								hj.setUnmappedType(unmappedtype);
//							} else {
//								unmappedtype = "Mono Carton";
//								hj.setUnmappedType(unmappedtype);
//							}
//
//						}
//				
//						
//						
//						usgae.setUsedbarcode(usedbarcodeun);
//						usgae.setUsedqrcode(usedqrcodeun);
//						usgae.setWastagebarcode(wastagebarcodeun);
//						usgae.setWastageqrcode(wastageqrcodeun);

				DeptOptional.stream().forEach(stocklist -> {
//							if (stocklist.getUnmappedType().equalsIgnoreCase(hj.getUnmappedType())) {
					StockoverviewDTO stock = new StockoverviewDTO();
					stock.setId(stocklist.getId());
					stock.setPackagingSize(stocklist.getPackagingSize());
					stock.setCartonSize(stocklist.getCartonSize());
					stock.setUnmappedType(stocklist.getUnmappedType());
					stock.setCodeType(stocklist.getCodeType());
					stock.setPrintingType(stocklist.getPrintingType());
					List<BarQrOverviewDTO> balancedetailsunmap = ealstockrepository
							.getByStockApplnAndUnmappedTypeAndPrintingTypeAll(DTO.getEalrequestApplnno(),
									stock.getUnmappedType(), stock.getPrintingType());
					balancedetailsunmap.parallelStream().forEach(act -> {
						EalRequestMapResponseDTO receivedetails = new EalRequestMapResponseDTO();
						receivedetails.setPackagingSize(act.getPackagingSize());
						receivedetails.setNoofBarcodereceived(act.getNoofBarcodereceived());
						receivedetails.setNoofQrcodereceived(act.getNoofQrcodereceived());
						receivedetails.setNoofBarcodepending(act.getNoofBarcodepending());
						receivedetails.setNoofqrpending(act.getNoofqrpending());
						receivedetails.setNoofBarcode(act.getNoofBarcode());
						receivedetails.setNoofQrcode(act.getNoofQrcode());
						receivedetails.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
						receivedetails.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
						receivedetails.setUnmappedType(act.getUnmappedType());
						if (stocklist.getUnmappedType().equalsIgnoreCase(receivedetails.getUnmappedType())) {
							stock.setNoofBarcodepending(receivedetails.getNoofBarcodepending());
							stock.setNoofqrpending(receivedetails.getNoofqrpending());
							stock.setNoofBarcodereceived(receivedetails.getNoofBarcodereceived());
							stock.setNoofQrcodereceived(receivedetails.getNoofQrcodereceived());
							stock.setNoofBarcode(receivedetails.getNoofBarcode());
							stock.setNoofQrcode(receivedetails.getNoofQrcode());
							stock.setNoofBarcodedamaged(receivedetails.getNoofBarcodedamaged());
							stock.setNoofQrcodedamaged(receivedetails.getNoofQrcodedamaged());
							stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
						}
					});

					// stock.setNoofRollcodereceived(stocklist.getNoofRollcodereceived());
					stock.setStockApplnno(stocklist.getStockApplnno());
					stock.setStockDate(stocklist.getStockDate());
					stock.setLicencenumber(stocklist.getLicenseNo());
					stock.setCreatedDate(String.valueOf(stocklist.getCreatedDate()));
					stock.setModifiedDate(String.valueOf(stocklist.getModifiedDate()));
					stock.setEalrequestApplnno(stocklist.getEalrequestapplno());
					try {

						String licNumber = licenseNumber;
						String packagesize = stocklist.getPackagingSize();
						Long createby = stocklist.getCreatedBy();
						String codeType = "UNMAPPED";

						List<EALWastageUsage> ealusagewastage = ealwastagerepo
								.getCountByLicenseNumberAndCreatedDateBetweenAndCodetypeAndPackageSize(licNumber,
										fromDateQ, toDateQ, codeType, packagesize);
						ealusagewastage.forEach(action -> {
							Integer usedbarcodeun = action.getUsedbarcode();
							Integer usedqrcodeun = action.getUsedqrcode();
							Integer wastagebarcodeun = action.getWastagecases();
							Integer wastageqrcodeun = action.getWastageqrcode();
							stock.setUsedBarcode(usedbarcodeun);
							stock.setUsedQrcode(usedqrcodeun);
							stock.setWastageBarcode(wastagebarcodeun);
							stock.setWastageQrcode(wastageqrcodeun);

						});
					} catch (Exception e) {
						log.info("Mapped Error :::");
					}

					stocklistall.add(stock);
					// }
				});

//				}
			}
			// }

		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block============", exception);
		}

		if (stocklistall.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					"No Record Found SCM Packagesize");
		} else {
			return Library.getSuccessfulResponse(stocklistall, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

	}

	public GenericResponse ealavailable(EalRequestDTO requestDTO) {
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
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
		List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
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
				stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate, toDate,
						requestDTO.getCodeType(), requestDTO.getCreatedBy());
				// stockinlist =
				// ealstockrepository.getCountByStatusAndCodeType(requestDTO.getCodeType());
				stockinlist.stream().forEach(stockpro -> {
					Integer totabarcodestock = stockpro.getTotalnoofbarcode();
					Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
					if (arrNode.isArray()) {
						for (final JsonNode objNode : arrNode) {
							EALUsageDTO eal = new EALUsageDTO();
							String packagesize = objNode.get("packaging_size_value").toString();
							Integer usedbarcode = objNode.get("used_barcode").intValue();
							Integer usedqrcode = objNode.get("used_qrcode").intValue();
							Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
							Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
							eal.setPackagesize(packagesize);
							eal.setUsedbarcode(usedbarcode);
							eal.setUsedqrcode(usedqrcode);
							eal.setWastagebarcode(wastagebarcode);
							eal.setWastageqrcode(wastageqrcode);
							ealavailablelist.add(eal);
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

	public GenericResponse mappedcodes(EalRequestDTO requestDTO) {
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
		placeholderDTO ho = new placeholderDTO();
		ho.setCodeTypeValue(requestDTO.getCodeTypeValue());
		ho.setFromDate(requestDTO.getFromDate());
		ho.setToDate(requestDTO.getToDate());
		ho.setCreatedBy(requestDTO.getCreatedBy());
		ho.setLicenseNumber(requestDTO.getLicenseNumber());
		HttpHeaders headers = new HttpHeaders();
		ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
		ealwastagedto.setEntityCode("brew_admin");
		ealwastagedto.setPlaceholderKeyValueMap(ho);

		EALStockwastageDTO ealwastagedtounmap = new EALStockwastageDTO();
		placeholderDTO hounmap = new placeholderDTO();
		hounmap.setCodeTypeValue("'UNMAPPED'");
		hounmap.setFromDate(requestDTO.getFromDate());
		hounmap.setToDate(requestDTO.getToDate());
		hounmap.setLicenseNumber(requestDTO.getLicenseNumber());

		ealwastagedtounmap.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
		ealwastagedtounmap.setEntityCode("brew_admin");
		ealwastagedtounmap.setPlaceholderKeyValueMap(hounmap);
		ObjectMapper mapper = new ObjectMapper();
		String bearertoken = null;
		JsonNode actualObj = null;
		String response = null;
		String responseunmap = null;
		final Date fromDate;
		final Date toDate;
		String fdate = null;
		String tdate = null;
		ObjectMapper mapperunmap = new ObjectMapper();
		JsonNode actualObjun = null;
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
		List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
		List<EALUsageDTO> stockint = new ArrayList<EALUsageDTO>();
		List<StockOverviewTilesDTO> tiles = new ArrayList<StockOverviewTilesDTO>();
		List<StockOverviewoverAll> tilesoverall = new ArrayList<StockOverviewoverAll>();
		List<StockOverviewTilesDTO> tilesunmap = new ArrayList<StockOverviewTilesDTO>();
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
				// stockinlist =
				// ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate,toDate,requestDTO.getCodeType());

				if (requestDTO.getCreatedBy() != null) {
					stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeTypeAndCreatedby(
							fromDate, toDate, requestDTO.getCodeType(), requestDTO.getCreatedBy());
				} else {
					stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate, toDate,
							requestDTO.getCodeType(), requestDTO.getCreatedBy());
				}

				if (!arrNode.isNull()) {
					if (arrNode.isArray()) {
						for (final JsonNode objNode : arrNode) {
							EALUsageDTO eal = new EALUsageDTO();
							String packagesize = objNode.get("packaging_size_value").toString();
							Integer usedbarcode = objNode.get("used_barcode").intValue();
							Integer usedqrcode = objNode.get("used_qrcode").intValue();
							Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
							Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
							eal.setPackagesize(packagesize);
							eal.setUsedbarcode(usedbarcode);
							eal.setUsedqrcode(usedqrcode);
							eal.setWastagebarcode(wastagebarcode);
							eal.setWastageqrcode(wastageqrcode);
							ealavailablelist.add(eal);
						}
					}
					int barcode = 0;
					EALUsageDTO ealq = new EALUsageDTO();
					ealavailablelist.stream().forEach(mapped -> {
						ealq.setTotusewastagebarqr(mapped.getUsedbarcode() + mapped.getWastagebarcode()
								+ mapped.getUsedqrcode() + mapped.getWastageqrcode());

					});

					stockint.add(ealq);

					stockinlist.stream().forEach(stockpro -> {
						Integer totabarcodestock = stockpro.getTotalnoofbarcode();
						Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
						Integer totabarqrcode = totabarcodestock + totalqrcodestock;
						stockint.stream().forEach(overallmap -> {
							StockOverviewTilesDTO tikesmap = new StockOverviewTilesDTO();
							Integer totalmapped = (totabarqrcode) - (overallmap.getTotusewastagebarqr());
							tikesmap.setTotalmapped(totalmapped);
							tiles.add(tikesmap);

						});

					});
				}

				else {

					stockinlist.stream().forEach(stockpro -> {
						Integer totabarcodestock = stockpro.getTotalnoofbarcode();
						Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
						Integer totabarqrcode = totabarcodestock + totalqrcodestock;
						StockOverviewTilesDTO tikesmap = new StockOverviewTilesDTO();
						Integer totalmapped = (totabarqrcode);
						tikesmap.setTotalmapped(totalmapped);
						tiles.add(tikesmap);

					});
				}

			}

		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block map block============", exception);
		}

		List<EALAvailable> stockinlistunmap = null;
		List<EALUsageDTO> ealavailablelistunmap = new ArrayList<EALUsageDTO>();
		List<EALUsageDTO> stockintunmap = new ArrayList<EALUsageDTO>();
		// List<StockOverviewTilesDTO> tilesunmap=new
		// ArrayList<StockOverviewTilesDTO>();

		try {

			StringBuffer uristockinlistunmap = new StringBuffer(stockurl);
			if (uristockinlistunmap != null) {

				String access_tokenunmap = headerRequest.getHeader("X-Authorization");
				headers.set("X-Authorization", access_tokenunmap);
				headers.set("Authorization", "Bearer " + token);
				RestTemplate restTemplate = new RestTemplate();
				headers.set("X-Authorization", access_tokenunmap);
				headers.set("Authorization", "Bearer " + token);
				HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedtounmap, headers);
				responseunmap = restTemplate
						.exchange(uristockinlistunmap.toString(), HttpMethod.POST, APIRequest, String.class).getBody();
				log.info("=======EALStockService catch block============" + responseunmap);
				try {
					actualObjun = mapper.readTree(responseunmap);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final JsonNode arrNodeun = actualObjun.get("responseContent");

				if (requestDTO.getCreatedBy() != null) {
					stockinlistunmap = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndUnmappedAndCreatedby(
							fromDate, toDate, requestDTO.getCodeType(), requestDTO.getCreatedBy());
				} else {
					stockinlistunmap = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate,
							toDate, "UNMAPPED", requestDTO.getCreatedBy());
				}

				if (!arrNodeun.isNull()) {
					if (arrNodeun.isArray()) {
						for (final JsonNode objNodeun : arrNodeun) {
							EALUsageDTO eal = new EALUsageDTO();
							String packagesize = objNodeun.get("packaging_size_value").toString();
							Integer usedbarcode = objNodeun.get("used_barcode").intValue();
							Integer usedqrcode = objNodeun.get("used_qrcode").intValue();
							Integer wastagebarcode = objNodeun.get("wastage_barcode").intValue();
							Integer wastageqrcode = objNodeun.get("wastage_qrcode").intValue();
							eal.setPackagesize(packagesize);
							eal.setUsedbarcode(usedbarcode);
							eal.setUsedqrcode(usedqrcode);
							eal.setWastagebarcode(wastagebarcode);
							eal.setWastageqrcode(wastageqrcode);
							ealavailablelistunmap.add(eal);
						}
					}
					int barcode = 0;
					EALUsageDTO ealq = new EALUsageDTO();
					ealavailablelistunmap.stream().forEach(mapped -> {
						ealq.setTotusewastagebarqr(mapped.getUsedbarcode() + mapped.getWastagebarcode()
								+ mapped.getUsedqrcode() + mapped.getWastageqrcode());
					});

					stockintunmap.add(ealq);

					stockinlistunmap.stream().forEach(stockpro -> {
						Integer totabarcodestock = stockpro.getTotalnoofbarcode();
						Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
						Integer totabarqrcode = totabarcodestock + totalqrcodestock;
						stockintunmap.stream().forEach(overallmap -> {
							StockOverviewTilesDTO tikesmap = new StockOverviewTilesDTO();
							Integer totalunmapped = (totabarqrcode) - (overallmap.getTotusewastagebarqr());
							tikesmap.setTotalunmapped(totalunmapped);
							tilesunmap.add(tikesmap);

						});

					});

					StockOverviewoverAll stockoverview = new StockOverviewoverAll();
					tiles.stream().forEach(map -> {
						stockoverview.setTotalmapped(map.getTotalmapped());
						tilesoverall.add(stockoverview);
					});

					tilesunmap.stream().forEach(unmap -> {
						stockoverview.setTotalunmapped(unmap.getTotalunmapped());
						tilesoverall.add(stockoverview);
					});

				}

				else {
					stockinlistunmap.stream().forEach(stockpro -> {
						Integer totabarcodestock = stockpro.getTotalnoofbarcode();
						Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
						Integer totabarqrcode = totabarcodestock + totalqrcodestock;
						StockOverviewTilesDTO tikesmap = new StockOverviewTilesDTO();
						Integer totalunmapped = (totabarqrcode);
						tikesmap.setTotalunmapped(totalunmapped);
						tilesunmap.add(tikesmap);

					});
					StockOverviewoverAll stockoverview = new StockOverviewoverAll();
					tiles.stream().forEach(map -> {
						stockoverview.setTotalmapped(map.getTotalmapped());
						tilesoverall.add(stockoverview);
					});

					tilesunmap.stream().forEach(unmap -> {
						stockoverview.setTotalunmapped(unmap.getTotalunmapped());
						tilesoverall.add(stockoverview);
					});

				}

			}

		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block============", exception);
		}

		return Library.getSuccessfulResponse(tilesoverall, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse mappedCount(EalRequestDTO requestDTO) {
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
		placeholderDTO ho = new placeholderDTO();
		ho.setCodeTypeValue("'MAPPED'");
		ho.setFromDate(requestDTO.getFromDate());
		ho.setCreatedBy(requestDTO.getCreatedBy());
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
		List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
		List<EALUsageDTO> stockint = new ArrayList<EALUsageDTO>();
		List<StockOverviewTilesDTO> tiles = new ArrayList<StockOverviewTilesDTO>();

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
				stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate, toDate,
						requestDTO.getCodeType(), requestDTO.getCreatedBy());
// stockinlist =
// ealstockrepository.getCountByStatusAndCodeType(requestDTO.getCodeType());

				if (arrNode.isArray()) {
					Integer usedbarcode = 0;
					Integer usedqrcode = 0;
					Integer wastagebarcode = 0;
					Integer wastageqrcode = 0;
					for (final JsonNode objNode : arrNode) {
						EALUsageDTO eal = new EALUsageDTO();
						String packagesize = objNode.get("packaging_size_value").toString();
						usedbarcode += objNode.get("used_barcode").intValue();
						usedqrcode += objNode.get("used_qrcode").intValue();
						wastagebarcode += objNode.get("wastage_barcode").intValue();
						wastageqrcode += objNode.get("wastage_qrcode").intValue();
						eal.setPackagesize(packagesize);
						eal.setUsedbarcode(usedbarcode);
						eal.setUsedqrcode(usedqrcode);
						eal.setWastagebarcode(wastagebarcode);
						eal.setWastageqrcode(wastageqrcode);
						ealavailablelist.add(eal);
					}
				}
				int barcode = 0;
				EALUsageDTO ealq = new EALUsageDTO();
				ealavailablelist.stream().forEach(mapped -> {
					ealq.setTotusewastagebarqr(mapped.getUsedbarcode() + mapped.getWastagebarcode()
							+ mapped.getUsedqrcode() + mapped.getWastageqrcode());
					// barcode+=mapped.getUsedbarcode();

				});

				stockint.add(ealq);

				stockinlist.stream().forEach(stockpro -> {
					Integer totabarcodestock = stockpro.getTotalnoofbarcode();
					Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
					Integer totabarqrcode = totabarcodestock + totalqrcodestock;
					stockint.stream().forEach(overallmap -> {
						StockOverviewTilesDTO tikesmap = new StockOverviewTilesDTO();
						Integer totalmapped = (totabarqrcode) - (overallmap.getTotusewastagebarqr());
						tikesmap.setTotalmapped(totalmapped);
						tiles.add(tikesmap);

					});

				});

			}
		} catch (Exception exception) {
// general error
			log.error("=======EALStockService catch block============", exception);
		}

		return Library.getSuccessfulResponse(tiles, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse unmappedcodes(EalRequestDTO requestDTO) {
		EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
		placeholderDTO ho = new placeholderDTO();
		ho.setCodeTypeValue("'UNMAPPED'");
		ho.setFromDate(requestDTO.getFromDate());
		ho.setCreatedBy(requestDTO.getCreatedBy());
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
		List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
		List<EALUsageDTO> stockint = new ArrayList<EALUsageDTO>();
		List<StockOverviewTilesDTO> tiles = new ArrayList<StockOverviewTilesDTO>();

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
				stockinlist = ealstockrepository.getCountByStatusAndCreatedDateBetweenAndCodeType(fromDate, toDate,
						requestDTO.getCodeType(), requestDTO.getCreatedBy());
				// stockinlist =
				// ealstockrepository.getCountByStatusAndCodeType(requestDTO.getCodeType());

				if (arrNode.isArray()) {
					Integer usedbarcode = 0;
					Integer usedqrcode = 0;
					Integer wastagebarcode = 0;
					Integer wastageqrcode = 0;
					for (final JsonNode objNode : arrNode) {
						EALUsageDTO eal = new EALUsageDTO();
						String packagesize = objNode.get("packaging_size_value").toString();
						usedbarcode += objNode.get("used_barcode").intValue();
						usedqrcode += objNode.get("used_qrcode").intValue();
						wastagebarcode += objNode.get("wastage_barcode").intValue();
						wastageqrcode += objNode.get("wastage_qrcode").intValue();
						eal.setPackagesize(packagesize);
						eal.setUsedbarcode(usedbarcode);
						eal.setUsedqrcode(usedqrcode);
						eal.setWastagebarcode(wastagebarcode);
						eal.setWastageqrcode(wastageqrcode);
						ealavailablelist.add(eal);
					}
				}
				int barcode = 0;
				EALUsageDTO ealq = new EALUsageDTO();
				ealavailablelist.stream().forEach(mapped -> {
					ealq.setTotusewastagebarqr(mapped.getUsedbarcode() + mapped.getWastagebarcode()
							+ mapped.getUsedqrcode() + mapped.getWastageqrcode());
					// barcode+=mapped.getUsedbarcode();

				});

				stockint.add(ealq);

				stockinlist.stream().forEach(stockpro -> {
					Integer totabarcodestock = stockpro.getTotalnoofbarcode();
					Integer totalqrcodestock = stockpro.getTotalnooqrrcode();
					Integer totabarqrcode = totabarcodestock + totalqrcodestock;
					stockint.stream().forEach(overallmap -> {
						StockOverviewTilesDTO tikesmap = new StockOverviewTilesDTO();
						Integer totalunmapped = (totabarqrcode) - (overallmap.getTotusewastagebarqr());
						tikesmap.setTotalunmapped(totalunmapped);
						tiles.add(tikesmap);

					});

				});

			}
		} catch (Exception exception) {
			// general error
			log.error("=======EALStockService catch block============", exception);
		}

		return Library.getSuccessfulResponse(tiles, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse stockgetByealid(Long id) {
		List<EALStockEntity> DeptOptional = ealstockrepository.getById(id);
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(DeptOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getsubPagesearchNewByFilterstockOverviewOLD(PaginationRequestDTO requestData)
			throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<EALStockEntity> list = this.getSubRecordsByFilterDTO1stock(requestData);
//		List<EALStockEntity> unique = list.stream()
//                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(EALRequestMapEntity::getTotalnumofBarcode))),
//                                           ArrayList::new));
		List<EALStockEntity> list1 = this.getSubRecordsByFilterDTO2stock(requestData);

		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}

//		
//		List<EalRequestMapResponseDTO> dtoList = list.stream().map(stockoverviewmapper::entityToResponseDTOstockoverview)
//				.collect(Collectors.toList());
		if (!list.isEmpty()) {
			List<EalRequestMapResponseDTO> setlist = new ArrayList<EalRequestMapResponseDTO>();
			List<EalRequestMapResponseDTO> setlistBal = new ArrayList<EalRequestMapResponseDTO>();
			if (Objects.nonNull(requestData.getFilters().get("codeType"))
					&& !requestData.getFilters().get("codeType").toString().trim().isEmpty()) {
				String codetype = requestData.getFilters().get("codeType").toString();
				if (codetype.equalsIgnoreCase("MAPPED")) {
					List<ReceviedBarQr> rece = ealstockrepository.getByoverStockreceived();

					list.stream().forEach(actuallist -> {
						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						// obj.setLicencenumber(actuallist.);
						obj.setStockApplnno(actuallist.getStockApplnno());
						obj.setCreatedDate(String.valueOf(actuallist.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(actuallist.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(actuallist.getPackagingSize());
						obj.setCartonSize(actuallist.getCartonSize());
						obj.setNoofBarcode(actuallist.getNoofBarcode());
						obj.setNoofQrcode(actuallist.getNoofQrcode());
						obj.setRemarks(actuallist.getRemarks());
						obj.setNoofBarcodereceived(actuallist.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(actuallist.getNoofQrcodereceived());
						obj.setNoofBarcodepending(actuallist.getNoofBarcodepending());
						obj.setNoofqrpending(actuallist.getNoofqrpending());
						// obj.setEalrequestApplnno(actuallist.);
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
							if (obj.getPackagingSize().equalsIgnoreCase(sumreceived.getPackageSize())) {
								obj.setNoofBarcodereceived(sumreceived.getNoofBarcodereceived());
								obj.setNoofQrcodereceived(sumreceived.getNoofQrcodereceived());
							}
						});

						setlist.add(obj);
					});

					EalRequestMapResponseDTO licobj = new EalRequestMapResponseDTO();
					setlist.stream().forEach(act -> {
						licobj.setLicencenumber(act.getLicenseNo());
					});

					String licencenumber = licobj.getLicencenumber();
					String codeType = "MAPPED";
					String fromDate = "2022-04-01";
					String toDate = "2023-03-31";

					EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
					placeholderDTO ho = new placeholderDTO();
					ho.setCodeTypeValue("'" + codeType + "'");
					ho.setFromDate("'" + fromDate + "'");
					ho.setToDate("'" + toDate + "'");
					ho.setLicenseNumber("'" + licencenumber + "'");
					HttpHeaders headers = new HttpHeaders();
					ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
					ealwastagedto.setEntityCode("brew_admin");
					ealwastagedto.setPlaceholderKeyValueMap(ho);
					ObjectMapper mapper = new ObjectMapper();
					String bearertoken = null;
					JsonNode actualObj = null;
					String response = null;

					List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
					EALUsageDTO ealq = new EALUsageDTO();
					List<EALUsageDTO> ealscmbal = new ArrayList<EALUsageDTO>();
					StringBuffer uri = new StringBuffer(stockurl);
					if (uri != null) {

						String access_token = headerRequest.getHeader("X-Authorization");
						headers.set("X-Authorization", access_token);
						headers.set("Authorization", "Bearer " + token);
						RestTemplate restTemplate = new RestTemplate();
						headers.set("X-Authorization", access_token);
						headers.set("Authorization", "Bearer " + token);
						HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
						response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
								.getBody();
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

						if (arrNode.isArray()) {
							for (final JsonNode objNode : arrNode) {
								EALUsageDTO eal = new EALUsageDTO();
								String packagesize = objNode.get("packaging_size_value").toString();
								Integer usedbarcode = objNode.get("used_barcode").intValue();
								Integer usedqrcode = objNode.get("used_qrcode").intValue();
								Integer wastagebarcode = objNode.get("wastage_barcode").intValue();
								Integer wastageqrcode = objNode.get("wastage_qrcode").intValue();
								eal.setPackagesize(packagesize);
								eal.setUsedbarcode(usedbarcode);
								eal.setUsedqrcode(usedqrcode);
								eal.setWastagebarcode(wastagebarcode);
								eal.setWastageqrcode(wastageqrcode);
								ealavailablelist.add(eal);
							}
						}

						ealavailablelist.stream().forEach(mapped -> {
							EALUsageDTO ealqt = new EALUsageDTO();
							ealqt.setPackagesize(mapped.getPackagesize());
							ealqt.setUsedbarcode(mapped.getUsedbarcode() + mapped.getWastagebarcode());
							ealqt.setUsedqrcode(mapped.getUsedqrcode() + mapped.getWastageqrcode());
							ealscmbal.add(ealqt);
						});

						final JsonNode arrNodeun = actualObj.get("responseContent");

						setlist.stream().forEach(act -> {

							EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
							obj.setStockApplnno(act.getStockApplnno());
							obj.setCreatedDate(String.valueOf(act.getCreatedDate()));
							obj.setModifiedDate(String.valueOf(act.getModifiedDate()));
							// obj.setModifiedBy(actuallist.getModifiedBy());
							obj.setPackagingSize(act.getPackagingSize());
							obj.setCartonSize(act.getCartonSize());

							obj.setRemarks(act.getRemarks());
							obj.setNoofBarcodereceived(act.getNoofBarcodereceived());
							obj.setNoofQrcodereceived(act.getNoofQrcodereceived());
							obj.setNoofBarcodeBalance((act.getNoofBarcodereceived()));
							obj.setNoofQrcodeBalance(act.getNoofQrcodereceived());

							obj.setNoofBarcode(act.getNoofBarcode());
							obj.setNoofQrcode(act.getNoofQrcode());
							obj.setNoofBarcodepending(act.getNoofBarcodepending());
							obj.setNoofqrpending(act.getNoofqrpending());
							// obj.setEalrequestApplnno(actuallist.);
							obj.setUnmappedType(act.getUnmappedType());
							obj.setTotalnumofBarcode(act.getTotalnumofBarcode());
							obj.setTotalnumofQrcode(act.getTotalnumofQrcode());
							obj.setTotalnumofRoll(act.getTotalnumofRoll());
							obj.setStockApplnno(act.getStockApplnno());
							obj.setStockDate(act.getStockDate());
							obj.setCodeType(act.getCodeType());
							obj.setOpenstockApplnno(act.getOpenstockApplnno());
							obj.setFlag(act.isFlag());
							obj.setNoofBarcode(act.getNoofBarcode());
							obj.setNoofQrcode(act.getNoofQrcode());
							obj.setLicencenumber(act.getLicencenumber());
							obj.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
							obj.setNoofQrcodedamaged(act.getNoofQrcodedamaged());
							obj.setLicenseNo(act.getLicenseNo());
							obj.setLicencenumber(obj.getLicenseNo());

							Integer usedbarcodeun = 0;
							Integer usedqrcodeun = 0;
							for (final JsonNode objNode : arrNodeun) {
								EALUsageDTO eal = new EALUsageDTO();
								usedbarcodeun += objNode.get("used_barcode").intValue();
								usedqrcodeun += objNode.get("used_qrcode").intValue();
								Integer barcodereceove = obj.getNoofBarcodereceived();
								Integer qrcoderecieved = obj.getNoofQrcodereceived();
								Integer balancebar = barcodereceove - usedbarcodeun;
								Integer balanceQr = qrcoderecieved - usedqrcodeun;
								obj.setNoofBarcodeBalance((balancebar));
								obj.setNoofQrcodeBalance(balanceQr);
							}

							setlistBal.add(obj);
						});

					}
				}

				else {
					List<ReceviedBarQr> receunmap = ealstockrepository.getByoverStockreceivedunmap();
					EalRequestMapResponseDTO licobj = new EalRequestMapResponseDTO();
					list.stream().forEach(act -> {

						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						obj.setLicenseNo(act.getLicenseNo());
						obj.setStockApplnno(act.getStockApplnno());
						obj.setCreatedDate(String.valueOf(act.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(act.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(act.getPackagingSize());
						obj.setCartonSize(act.getCartonSize());
						// obj.setLicenseNo(act.getLicenseNo());
						obj.setRemarks(act.getRemarks());
						obj.setNoofBarcodereceived(act.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(act.getNoofQrcodereceived());
						obj.setNoofBarcodeBalance((act.getNoofBarcodereceived()));
						obj.setNoofQrcodeBalance(act.getNoofQrcodereceived());

						obj.setNoofBarcode(act.getNoofBarcode());
						obj.setNoofQrcode(act.getNoofQrcode());
						obj.setNoofBarcodepending(act.getNoofBarcodepending());
						obj.setNoofqrpending(act.getNoofqrpending());
						// obj.setEalrequestApplnno(actuallist.);
						obj.setUnmappedType(act.getUnmappedType());
						obj.setTotalnumofBarcode(act.getTotalnumofBarcode());
						obj.setTotalnumofQrcode(act.getTotalnumofQrcode());
						obj.setTotalnumofRoll(act.getTotalnumofRoll());
						obj.setStockApplnno(act.getStockApplnno());
						obj.setStockDate(act.getStockDate());
						obj.setCodeType(act.getCodeType());
						obj.setOpenstockApplnno(act.getOpenstockApplnno());
						obj.setFlag(act.isFlag());
						obj.setNoofBarcode(act.getNoofBarcode());
						obj.setNoofQrcode(act.getNoofQrcode());
						obj.setLicencenumber(obj.getLicenseNo());
						obj.setNoofBarcodedamaged(act.getNoofBarcodedamaged());
						obj.setNoofQrcodedamaged(act.getNoofQrcodedamaged());

						receunmap.stream().forEach(sumreceivedunmap -> {
							if (obj.getUnmappedType().equalsIgnoreCase(sumreceivedunmap.getUnmappedType())) {
								obj.setNoofBarcodereceived(sumreceivedunmap.getNoofBarcodereceived());
								obj.setNoofQrcodereceived(sumreceivedunmap.getNoofQrcodereceived());
							}
						});

						setlist.add(obj);
					});

					setlist.stream().forEach(act -> {
						licobj.setLicencenumber(act.getLicenseNo());
					});

					String licencenumber = licobj.getLicencenumber();
					String codeType = "UNMAPPED";
					String fromDate = "2022-04-01";
					String toDate = "2023-03-31";

					EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
					placeholderDTO ho = new placeholderDTO();
					ho.setCodeTypeValue("'" + codeType + "'");
					ho.setFromDate("'" + fromDate + "'");
					ho.setToDate("'" + toDate + "'");
					ho.setLicenseNumber("'" + licencenumber + "'");
					HttpHeaders headers = new HttpHeaders();
					ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS");
					ealwastagedto.setEntityCode("brew_admin");
					ealwastagedto.setPlaceholderKeyValueMap(ho);
					ObjectMapper mapper = new ObjectMapper();
					String bearertoken = null;
					JsonNode actualObj = null;
					String response = null;

					List<EALUsageDTO> ealavailablelist = new ArrayList<EALUsageDTO>();
					EALUsageDTO ealq = new EALUsageDTO();
					List<EALUsageDTO> ealscmbal = new ArrayList<EALUsageDTO>();
					StringBuffer uri = new StringBuffer(stockurl);
					if (uri != null) {

						String access_token = headerRequest.getHeader("X-Authorization");
						headers.set("X-Authorization", access_token);
						headers.set("Authorization", "Bearer " + token);
						RestTemplate restTemplate = new RestTemplate();
						headers.set("X-Authorization", access_token);
						headers.set("Authorization", "Bearer " + token);
						HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
						response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
								.getBody();
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

						Integer usedbarcode = 0;
						Integer usedqrcode = 0;
						Integer wastagebarcode = 0;
						Integer wastageqrcode = 0;
						String unmappedtype = null;
						final JsonNode arrNodeun = actualObj.get("responseContent");
						if (arrNodeun.isArray()) {
							EALUsageDTO usgae = new EALUsageDTO();
							EalRequestMapResponseDTO hj = new EalRequestMapResponseDTO();
							for (final JsonNode objNode : arrNodeun) {
								unmappedtype = null;
								EALUsageDTO eal = new EALUsageDTO();
								char quotes = '"';
								String type = objNode.get("type").toString();
								String casettype = quotes + "CASE" + quotes;
								usedbarcode = objNode.get("used_barcode").intValue();
								usedqrcode = objNode.get("used_qrcode").intValue();
								wastagebarcode = objNode.get("wastage_barcode").intValue();
								wastageqrcode = objNode.get("wastage_qrcode").intValue();
								if (type.equalsIgnoreCase(casettype)) {
									unmappedtype = "BarCode and QRCode";
									hj.setUnmappedType(unmappedtype);
								} else {
									unmappedtype = "Mono Carton";
									hj.setUnmappedType(unmappedtype);
								}

								eal.setPackagesize(hj.getUnmappedType());
								eal.setUsedbarcode(usedbarcode);
								eal.setUsedqrcode(usedqrcode);
								eal.setWastagebarcode(wastagebarcode);
								eal.setWastageqrcode(wastageqrcode);
								ealscmbal.add(eal);

							}

						}
					}

					final JsonNode arrNodeun = actualObj.get("responseContent");

					setlist.stream().forEach(actuallist -> {
						EalRequestMapResponseDTO obj = new EalRequestMapResponseDTO();
						obj.setLicencenumber(actuallist.getLicenseNo());
						obj.setStockApplnno(actuallist.getStockApplnno());
						obj.setCreatedDate(String.valueOf(actuallist.getCreatedDate()));
						obj.setModifiedDate(String.valueOf(actuallist.getModifiedDate()));
						// obj.setModifiedBy(actuallist.getModifiedBy());
						obj.setPackagingSize(actuallist.getPackagingSize());
						obj.setCartonSize(actuallist.getCartonSize());
						obj.setNoofBarcode(actuallist.getNoofBarcode());
						obj.setNoofQrcode(actuallist.getNoofQrcode());
						obj.setRemarks(actuallist.getRemarks());
						obj.setNoofBarcodereceived(actuallist.getNoofBarcodereceived());
						obj.setNoofQrcodereceived(actuallist.getNoofQrcodereceived());
						obj.setNoofBarcodeBalance((actuallist.getNoofBarcodereceived()));
						obj.setNoofQrcodeBalance(actuallist.getNoofQrcodereceived());
						obj.setNoofBarcodepending(actuallist.getNoofBarcodepending());
						obj.setNoofqrpending(actuallist.getNoofqrpending());
						obj.setUnmappedType(actuallist.getUnmappedType());
						obj.setTotalnumofBarcode(actuallist.getTotalnumofBarcode());
						obj.setTotalnumofQrcode(actuallist.getTotalnumofQrcode());
						obj.setTotalnumofRoll(actuallist.getTotalnumofRoll());
						obj.setStockApplnno(actuallist.getStockApplnno());
						obj.setStockDate(actuallist.getStockDate());
						obj.setCodeType(actuallist.getCodeType());
						obj.setOpenstockApplnno(actuallist.getOpenstockApplnno());
						obj.setFlag(actuallist.isFlag());
						obj.setNoofBarcodedamaged(actuallist.getNoofBarcodedamaged());
						obj.setNoofQrcodedamaged(actuallist.getNoofQrcodedamaged());

//							ealscmbal.stream().forEach(sumreceived -> {							
//								if (obj.getUnmappedType().equalsIgnoreCase(ealscmbalun.getPackagesize())) {
						Integer usedbarcodeun = 0;
						Integer usedqrcodeun = 0;
						for (final JsonNode objNode : arrNodeun) {
							EALUsageDTO eal = new EALUsageDTO();
							usedbarcodeun += objNode.get("used_barcode").intValue();
							usedqrcodeun += objNode.get("used_qrcode").intValue();
							Integer barcodereceove = obj.getNoofBarcodereceived();
							Integer qrcoderecieved = obj.getNoofQrcodereceived();
							Integer balancebar = barcodereceove - usedbarcodeun;
							Integer balanceQr = qrcoderecieved - usedqrcodeun;
							obj.setNoofBarcodeBalance((balancebar));
							obj.setNoofQrcodeBalance(balanceQr);
							log.info("count loop" + balanceQr);
						}
//								}

//							});

						setlistBal.add(obj);

					});

				}
			}

			paginationResponseDTO.setContents(setlistBal);
		}
		Long count1 = (long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list.size()) ? list.size() : null);
		paginationResponseDTO.setTotalElements(count1);
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse elastockoverviewnew(EalRequestDTO requestDTO) {
		try {
			EALDeclarartion(requestDTO);
		} catch (Exception e) {
			log.info("::::EAL DECLARATION INSERT ISSUE:::" + e);

		}

		return Library.getSuccessfulResponse(null, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public void EALDeclarartion(EalRequestDTO requestDTO) {
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
		List<StockEALDECAECDTO> ealdecealAECList = ealdecrepo.getBYLicenseNo(requestDTO.getLicenseNumber());
		try {
			if (!ealdecealAECList.isEmpty()) {
				ealdecealAECList.stream().forEach(action -> {
					JsonNode actualObj = null;
					String response = null;
					StringBuffer uri = new StringBuffer(stockurl);
					if (uri != null) {
						EALStockwastageDTO ealwastagedto = new EALStockwastageDTO();
						placeholderDTO ho = new placeholderDTO();
						ho.setCodeTypeValue("'" + requestDTO.getCodeTypeValue() + "'");
						ho.setFromDate("'" + requestDTO.getStartDate() + "'");
						ho.setToDate("'" + requestDTO.getEndDate() + "'");
						ho.setLicenseNumber("'" + requestDTO.getLicenseNumber() + "'");
						ho.setBottlingPlanId("'" + action.getbottlingPlanId() + "'");
						HttpHeaders headers = new HttpHeaders();
						ealwastagedto.setDataCode("SELECT_EAL_REQUEST_STOCK_DETAILS_FOR_PAYMENT_TEAM");
						ealwastagedto.setEntityCode("22200");
						ealwastagedto.setPlaceholderKeyValueMap(ho);
						ObjectMapper mapper = new ObjectMapper();
						String bearertoken = null;
						String access_token = headerRequest.getHeader("X-Authorization");
						headers.set("X-Authorization", access_token);
						headers.set("Authorization", "Bearer " + token);
						RestTemplate restTemplate = new RestTemplate();
						headers.set("X-Authorization", access_token);
						headers.set("Authorization", "Bearer " + token);
						HttpEntity<EALStockwastageDTO> APIRequest = new HttpEntity<>(ealwastagedto, headers);
						response = restTemplate.exchange(uri.toString(), HttpMethod.POST, APIRequest, String.class)
								.getBody();
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
						if (arrNode.isArray()) {
							for (final JsonNode objNode : arrNode) {
								EALUsageDTO eal = new EALUsageDTO();
								String licenseno = objNode.get("license_number").toString();
								String licencenumberreplace = licenseno.substring(1, licenseno.length() - 1);
								String entitycode = objNode.get("entity_code").toString();
								String entitycodereplace = entitycode.substring(1, entitycode.length() - 1);
								String bottlingplanid = objNode.get("bottling_plan_id").toString();
								String bottlingplanidplace = bottlingplanid.substring(1, bottlingplanid.length() - 1);
								String code_type_value = objNode.get("code_type_value").toString();
								String code_type_valueplace = code_type_value.substring(1,
										code_type_value.length() - 1);
								String packaging_size_value = objNode.get("packaging_size_value").toString();
								String type = objNode.get("type").toString();
								String typereplace = type.substring(1, type.length() - 1);
								int planned_cases = objNode.get("planned_cases").intValue();
								int planned_bottles = objNode.get("planned_bottles").intValue();
								int addl_requested_cases = objNode.get("addl_requested_cases").intValue();
								int addl_requested_bottles = objNode.get("addl_requested_bottles").intValue();
								int printed_cases = objNode.get("printed_cases").intValue();
								int printed_bottles = objNode.get("printed_bottles").intValue();
								int scanned_cases = objNode.get("scanned_cases").intValue();
								int scanned_bottles = objNode.get("scanned_bottles").intValue();
								EALDeclarationEntity declarationEntity = new EALDeclarationEntity();
								declarationEntity.setLicenseNo(licencenumberreplace);
								declarationEntity.setEntityCode(entitycodereplace);
								declarationEntity.setBottlingPlanId(bottlingplanidplace);
								declarationEntity.setCodeTypeVaue(code_type_valueplace);
								declarationEntity.setPackagingsizeValue(packaging_size_value);
								declarationEntity.setPackagingType(typereplace);
								declarationEntity.setPlannedCases(planned_cases);
								declarationEntity.setPlannedBottles(planned_bottles);
								declarationEntity.setAddlrequestedBottles(addl_requested_bottles);
								declarationEntity.setAddlrequestedCases(addl_requested_cases);
								declarationEntity.setPrintedCases(printed_cases);
								declarationEntity.setPrintedBottles(printed_bottles);
								declarationEntity.setScannedBottles(scanned_bottles);
								declarationEntity.setScannedCases(scanned_cases);
								ealdecrepo.save(declarationEntity);

							}
						}

					}
				});
			}
		} catch (Exception exception) {
			log.error("=======EAL Stock Service============", exception);
		}

	}

	public GenericResponse stockOverviewMappedCount(EalRequestDTO requestDTO) {

		final Date fromDate;
		final Date toDate;
		String fdate = null;
		String tdate = null;
		try {
			fdate = String.valueOf(requestDTO.getFromDate().toString());
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			tdate = String.valueOf(requestDTO.getToDate().toString());
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<Stockbalance> stockEntities = null;

		if ("MAPPED".equals(requestDTO.getCodeType())) {
			stockEntities = ealstockrepository.findbystockOverviewMappedCount(requestDTO.getCodeType(),
					requestDTO.getLicenseNo(), fromDate, toDate);
		} else {
			stockEntities = ealstockrepository.findByStockOverviewUnmapedCount(requestDTO.getCodeType(),
					requestDTO.getLicenseNo(), fromDate, toDate);
		}

		// Validate if stockEntities is null or empty after the repository call
		if (stockEntities == null || stockEntities.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(stockEntities, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse stockOverviewMappedandunppedParentUnitCount(EalRequestDTO requestDTO) {

		final Date fromDate;
		final Date toDate;
		String fdate = null;
		String tdate = null;
		try {
			fdate = String.valueOf(requestDTO.getFromDate().toString());
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fdate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		try {
			tdate = String.valueOf(requestDTO.getToDate().toString());
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tdate + " " + "23:59:59");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}

		List<Stockbalance> stockEntities = null;

		if ("MAPPED".equals(requestDTO.getCodeType())) {
			stockEntities = ealstockrepository.findbystockOverviewMappedParentUnitCount(requestDTO.getCodeType(),
					requestDTO.getLicenseNo(), fromDate, toDate);
		} else {
			stockEntities = ealstockrepository.findByStockOverviewUnmapedParentUnitCount(requestDTO.getCodeType(),
					requestDTO.getLicenseNo(), fromDate, toDate);
		}

		// Validate if stockEntities is null or empty after the repository call
		if (stockEntities == null || stockEntities.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(stockEntities, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

		
	}
	
	public GenericResponse mapStockSummary(String fromDate,String toDate,
			String licenseNumber,String packagingSize,String cartonSize) {
		List<StockOverviewMapResponseDTO> DeptOptional = ealstockrepository.mapStockSummary(fromDate, toDate,
				 licenseNumber,packagingSize,cartonSize);
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(DeptOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse unmapStockSummary(String fromDate,String toDate,
			String licenseNumber,String printingType,String unmappedType,String mapType) {
		List<StockOverviewUnMapResponseDTO> DeptOptional = ealstockrepository.unmapStockSummary(fromDate, toDate,
				 licenseNumber,printingType,unmappedType,mapType);
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(DeptOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse puMapStockSummary(String fromDate,String toDate,
			String licenseNumber,String packagingSize,String cartonSize) {
		List<StockOverviewMapResponseDTO> DeptOptional = ealstockrepository.puMapStockSummary(fromDate, toDate,
				 licenseNumber,packagingSize,cartonSize);
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(DeptOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	public GenericResponse puUnmapStockSummary(String fromDate,String toDate,
			String licenseNumber,String printingType,String unmappedType,String mapType) {
		List<StockOverviewUnMapResponseDTO> DeptOptional = ealstockrepository.puUnmapStockSummary(fromDate, toDate,
				 licenseNumber,printingType,unmappedType,mapType);
		if (CollectionUtils.isEmpty(DeptOptional)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(DeptOptional, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}