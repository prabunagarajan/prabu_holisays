package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.FROM_DATE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.TO_DATE;

import java.security.InvalidParameterException;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AccessoriesRequestDTO;
import com.oasys.helpdesk.dto.AssetAccessoriesResponseDTO;
import com.oasys.helpdesk.dto.AssetReportResponseDTO;
import com.oasys.helpdesk.dto.AssetBrandRequestDTO;
import com.oasys.helpdesk.dto.AssetListRequestDto;
import com.oasys.helpdesk.dto.AssetNamebrandDto;
import com.oasys.helpdesk.dto.AssetReportRequestDTO;
import com.oasys.helpdesk.dto.DeviceHardwareNameRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetListEntity;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.AssetStatusEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.entity.SupplierEntity;
import com.oasys.helpdesk.mapper.AssetListMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AccessoriesListRepository;
import com.oasys.helpdesk.repository.AccessoriesNameRepository;
import com.oasys.helpdesk.repository.AssetBrandRepository;
import com.oasys.helpdesk.repository.AssetListRepository;
import com.oasys.helpdesk.repository.AssetMapRepository;
import com.oasys.helpdesk.repository.AssetStatusRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DeviceHardwareRepository;
import com.oasys.helpdesk.repository.SupplierRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.entity.DeviceRegisterEntity;
import com.oasys.posasset.entity.DevicestatusEntity;

@Service
public class AssetListServiceImpl implements AssetListService {

	@Autowired
	AssetListRepository assetListRepository;

	@Autowired
	private AssetTypeRepository assetTypeRepository;

	@Autowired
	private AccessoriesNameRepository accessoriesRepo;

	@Autowired
	private AccessoriesListRepository accessoriesListRepo;

	@Autowired
	private DeviceHardwareRepository deviceRepository;

	@Autowired
	private AssetBrandRepository assetbrandrepository;

	@Autowired
	private AccessoriesNameRepository accessorienamerepo;

	@Autowired
	private SupplierRepository supplierrepository;

	@Autowired
	private AssetListMapper assetlistmapper;

	@Autowired
	private AssetBrandRepository assetBrandRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	AssetMapRepository assetMapRepository;

	@Autowired
	private AssetStatusRepository assetStatusRepository;
	
	@Override
	public GenericResponse addAssetlist(AssetListRequestDto requestDTO) {
		AssetListEntity entity = commonUtil.modalMap(requestDTO, AssetListEntity.class);
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(requestDTO.getAssetTypeId());
		if (!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset type" }));
		}

		Optional<DeviceHardwareEntity> assetnameEntity = deviceRepository.findById(requestDTO.getAssetNameId());
		if (!assetnameEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Name" }));
		}

		Optional<AssetBrandEntity> assetbrandentity = assetbrandrepository.findById(requestDTO.getAssetBrandId());
		if (!assetbrandentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Brand" }));
		}

		Optional<Accessories> assetsubtype = accessorienamerepo.findById(requestDTO.getAssetSubTypeId());

		if (!assetsubtype.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Sub Type" }));
		}

		Optional<SupplierEntity> supplierentity = supplierrepository.findById(requestDTO.getSupplierId());

		if (!supplierentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Supplier Name" }));
		}

		Long asetname = assetnameEntity.get().getId();
		Long brand = assetbrandentity.get().getId();
		Long assetsubtypew = assetsubtype.get().getId();
		Long type = assetTypeEntity.get().getId();
		Long supplier = supplierentity.get().getId();
		String serialno = requestDTO.getSerialNo();
		List<AssetListEntity> assetlistOptional = assetListRepository.getBySerialNo(serialno);
		if (!assetlistOptional.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
			
		} else {
			entity.setDeviceName(assetnameEntity.get());
			entity.setBrand(assetbrandentity.get());
			entity.setAssetsubTypeNmae(assetsubtype.get());
			entity.setType(assetTypeEntity.get());
			entity.setSupplierName(supplierentity.get());
			entity.setSerialNo(requestDTO.getSerialNo());
			entity.setRating(requestDTO.getRating());
			entity.setWarrantyPeriod(requestDTO.getWarrantyPeriod());
			entity.setDateOfPurchase(requestDTO.getDateOfPurchase());
			entity.setActive(requestDTO.isActive());
			entity = assetListRepository.save(entity);
			return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<AssetListEntity> depTypeEntity = assetListRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(assetlistmapper.convertEntityToResponseDTO(depTypeEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<AssetListEntity> List = assetListRepository.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse updateAssetlist(AssetListRequestDto requestDTO) {

		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		Optional<AssetListEntity> DeptOptional = assetListRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		AssetListEntity deptEntity = DeptOptional.get();

		AssetListEntity entity = commonUtil.modalMap(requestDTO, AssetListEntity.class);

		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(requestDTO.getAssetTypeId());
		if (!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset type" }));
		}

		Optional<DeviceHardwareEntity> assetnameEntity = deviceRepository.findById(requestDTO.getAssetNameId());
		if (!assetnameEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Name" }));
		}

		Optional<AssetBrandEntity> assetbrandentity = assetbrandrepository.findById(requestDTO.getAssetBrandId());
		if (!assetbrandentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Brand" }));
		}

		Optional<Accessories> assetsubtype = accessorienamerepo.findById(requestDTO.getAssetSubTypeId());

		if (!assetsubtype.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Sub Type" }));
		}

		Optional<SupplierEntity> supplierentity = supplierrepository.findById(requestDTO.getSupplierId());

		if (!supplierentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Supplier Name" }));
		}

		String serialno = requestDTO.getSerialNo();
		Long id=requestDTO.getId();
		List<AssetListEntity> assetlistOptional = assetListRepository.findBySerialNoNotINId(serialno,id);
		if (!assetlistOptional.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
			
		}
		deptEntity.setDeviceName(assetnameEntity.get());
		deptEntity.setBrand(assetbrandentity.get());
		deptEntity.setAssetsubTypeNmae(assetsubtype.get());
		deptEntity.setType(assetTypeEntity.get());
		deptEntity.setSupplierName(supplierentity.get());
		deptEntity.setSerialNo(requestDTO.getSerialNo());
		deptEntity.setRating(requestDTO.getRating());
		deptEntity.setWarrantyPeriod(requestDTO.getWarrantyPeriod());
		deptEntity.setDateOfPurchase(requestDTO.getDateOfPurchase());
		deptEntity.setActive(requestDTO.isActive());

		deptEntity = assetListRepository.save(deptEntity);

		try {

			if (requestDTO.isActive() == true) {
		        Optional<AssetMapEntity> findBySerialNo = assetMapRepository.findBySerialNo(requestDTO.getSerialNo());
             	AssetMapEntity assetMapEntity = findBySerialNo.get();
		        Optional<AssetStatusEntity> findBystatsID = assetStatusRepository.findByCode("UNMAPPED");
				Long id2 = findBystatsID.get().getId();
				assetMapEntity.setStatus(findBystatsID.get());
				assetMapEntity = assetMapRepository.save(assetMapEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getBybrandandnameId(Long typeid) {
		List<DeviceHardwareNameRequestDTO> assetlist = new ArrayList<DeviceHardwareNameRequestDTO>();
		List<AssetBrandRequestDTO> requestDTObrand = new ArrayList<AssetBrandRequestDTO>();
		List<AssetNamebrandDto> AssetnameBrandList = new ArrayList<AssetNamebrandDto>();
		AssetNamebrandDto brandassetname = new AssetNamebrandDto();
		List<DeviceHardwareEntity> deviceEntityOptional = deviceRepository.getById(typeid);
		deviceEntityOptional.stream().forEach(action -> {
			DeviceHardwareNameRequestDTO requestDTO = new DeviceHardwareNameRequestDTO();
			requestDTO.setId(action.getId());
			requestDTO.setDeviceName(action.getDeviceName());
			requestDTO.setStatus(action.isStatus());
			requestDTO.setAssetType(action.getType().getType());
			requestDTO.setAssetTypeId(action.getType().getId());
			assetlist.add(requestDTO);
		});
		List<AssetBrandEntity> assetBrandOptional = assetBrandRepository.getById(typeid);
		assetBrandOptional.stream().forEach(brand -> {
			AssetBrandRequestDTO brandrequestDTO = new AssetBrandRequestDTO();
			brandrequestDTO.setId(brand.getId());
			brandrequestDTO.setBrand(brand.getBrand());
			brandrequestDTO.setAssetType(brand.getType().getType());
			brandrequestDTO.setAssetTypeId(brand.getType().getId());
			requestDTObrand.add(brandrequestDTO);
		});
		brandassetname.setAssetBrand(requestDTObrand);
		brandassetname.setAssetName(assetlist);
		AssetnameBrandList.add(brandassetname);
		return Library.getSuccessfulResponse(AssetnameBrandList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getBysubtypeId(Long assetnameid) {
		List<AccessoriesRequestDTO> subtypelist = new ArrayList<AccessoriesRequestDTO>();
		List<Accessories> assetsubtype = accessoriesRepo.getById(assetnameid);
		assetsubtype.stream().forEach(sub -> {
			AccessoriesRequestDTO requestDTO = new AccessoriesRequestDTO();
			requestDTO.setAssetName(sub.getAssetName().getDeviceName());
			requestDTO.setAssetsubType(sub.getAssetsubType());
			requestDTO.setId(sub.getId());
			requestDTO.setAssetType(sub.getType().getType());
			requestDTO.setAssetnameId(sub.getAssetName().getId());
			requestDTO.setAssetTypeId(sub.getType().getId());
			subtypelist.add(requestDTO);

		});

		return Library.getSuccessfulResponse(subtypelist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);

		if (count > 0) {
			List<AssetListEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			Long listcount = (long) list.size();

			paginationResponseDTO.setContents(list);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(listcount) ? listcount.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<AssetListEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssetListEntity> cq = cb.createQuery(AssetListEntity.class);
		Root<AssetListEntity> from = cq.from(AssetListEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<AssetListEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

		}
		if (Objects.nonNull(filterRequestDTO.getPaginationSize())
				&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}

		List<AssetListEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AssetListEntity> from = cq.from(AssetListEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<AssetListEntity> from) throws ParseException {
		DevicestatusEntity div = new DevicestatusEntity();
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {

				if (Objects.nonNull(filterRequestDTO.getFilters().get(FROM_DATE))
						&& !filterRequestDTO.getFilters().get(TO_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(FROM_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(TO_DATE).toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("supplierId"))
						&& !filterRequestDTO.getFilters().get("supplierId").toString().trim().isEmpty()) {

					Long supplierid = Long.valueOf(filterRequestDTO.getFilters().get("supplierId").toString());
					list.add(cb.equal(from.get("supplierName"), supplierid));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetTypeId"))
						&& !filterRequestDTO.getFilters().get("assetTypeId").toString().trim().isEmpty()) {

					Long assetTypeId = Long.valueOf(filterRequestDTO.getFilters().get("assetTypeId").toString());
					list.add(cb.equal(from.get("type"), assetTypeId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetNameId"))
						&& !filterRequestDTO.getFilters().get("assetNameId").toString().trim().isEmpty()) {

					Long deviceName = Long.valueOf(filterRequestDTO.getFilters().get("assetNameId").toString());
					list.add(cb.equal(from.get("deviceName"), deviceName));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetBrandId"))
						&& !filterRequestDTO.getFilters().get("assetBrandId").toString().trim().isEmpty()) {

					Long assetBrandId = Long.valueOf(filterRequestDTO.getFilters().get("assetBrandId").toString());
					list.add(cb.equal(from.get("brand"), assetBrandId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetSubTypeId"))
						&& !filterRequestDTO.getFilters().get("assetSubTypeId").toString().trim().isEmpty()) {

					Long assetSubTypeId = Long.valueOf(filterRequestDTO.getFilters().get("assetSubTypeId").toString());
					list.add(cb.equal(from.get("assetsubTypeNmae"), assetSubTypeId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("serialNo"))
						&& !filterRequestDTO.getFilters().get("serialNo").toString().trim().isEmpty()) {

					String serialNo = String.valueOf(filterRequestDTO.getFilters().get("serialNo").toString());
					list.add(cb.equal(from.get("serialNo"), serialNo));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("warrantyPeriod"))
						&& !filterRequestDTO.getFilters().get("warrantyPeriod").toString().trim().isEmpty()) {

					String warrantyPeriod = String
							.valueOf(filterRequestDTO.getFilters().get("warrantyPeriod").toString());
					list.add(cb.equal(from.get("warrantyPeriod"), warrantyPeriod));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("dateOfPurchase"))
						&& !filterRequestDTO.getFilters().get("dateOfPurchase").toString().trim().isEmpty()) {

					String dateOfPurchase = String
							.valueOf(filterRequestDTO.getFilters().get("dateOfPurchase").toString());
					list.add(cb.equal(from.get("dateOfPurchase"), dateOfPurchase));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("rating"))
						&& !filterRequestDTO.getFilters().get("rating").toString().trim().isEmpty()) {

					String rating = String.valueOf(filterRequestDTO.getFilters().get("rating").toString());
					list.add(cb.equal(from.get("rating"), rating));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("isActive"))
						&& !filterRequestDTO.getFilters().get("isActive").toString().trim().isEmpty()) {

					Long isActive = Long.valueOf(filterRequestDTO.getFilters().get("isActive").toString());
					list.add(cb.equal(from.get("isActive"), isActive));
				}

				
			}
		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid filter value passed!");
		}
	}
	
	public GenericResponse assetReport(AssetReportRequestDTO requestDTO) {
		try {

			String assetGroup = (requestDTO.getAssetGroup() != null && !requestDTO.getAssetGroup().isEmpty())
					? requestDTO.getAssetGroup()
					: null;
			String assetType = (requestDTO.getAssetType() != null && !requestDTO.getAssetType().isEmpty())
					? requestDTO.getAssetType()
					: null;
			String assetName = (requestDTO.getAssetName() != null && !requestDTO.getAssetName().isEmpty())
					? requestDTO.getAssetName()
					: null;
			String assetBrand = (requestDTO.getAssetBrand() != null && !requestDTO.getAssetBrand().isEmpty())
					? requestDTO.getAssetBrand()
					: null;
			String assetSubType = (requestDTO.getAssetSubType() != null && !requestDTO.getAssetSubType().isEmpty())
					? requestDTO.getAssetSubType()
					: null;
			String supplierName = (requestDTO.getSupplierName() != null && !requestDTO.getSupplierName().isEmpty())
					? requestDTO.getSupplierName()
					: null;
			String serialNo = (requestDTO.getSerialNo() != null && !requestDTO.getSerialNo().isEmpty())
					? requestDTO.getSerialNo()
					: null;
			String status = (requestDTO.getStatus() != null && !requestDTO.getStatus().isEmpty())
					? requestDTO.getStatus()
					: null;
			String unitName = (requestDTO.getUnitName() != null && !requestDTO.getUnitName().isEmpty())
					? requestDTO.getUnitName()
					: null;

			List<AssetReportResponseDTO> entitysummary = assetListRepository.assetReport(
					requestDTO.getFromDate(), requestDTO.getToDate(), assetGroup, assetType, assetName, assetBrand,
					assetSubType, supplierName, serialNo, status, unitName);

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
					"An error occurred while retrieving Asset and AssetMap List", e);
		}
	}

}
