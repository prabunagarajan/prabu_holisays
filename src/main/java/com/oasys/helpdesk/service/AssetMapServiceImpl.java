package com.oasys.helpdesk.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Expression;
import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetMapRequestDto;
import com.oasys.helpdesk.dto.AssetMapResponseDTO;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.AssetStatusEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.mapper.AssetListMapper;
import com.oasys.helpdesk.mapper.AssetMapMapper;
import com.oasys.helpdesk.repository.AssetMapRepository;
import com.oasys.helpdesk.repository.AssetStatusRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DeviceHardwareRepository;
import com.oasys.helpdesk.repository.EntityDetailsRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
//import static com.oasys.helpdesk.constant.Constant.ASC;
//
//
//import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
//import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
//import static com.oasys.helpdesk.constant.Constant.END_TIME;
//import static com.oasys.helpdesk.constant.Constant.FROM_DATE;
//import static com.oasys.helpdesk.constant.Constant.ID;
//import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
//import static com.oasys.helpdesk.constant.Constant.START_TIME;
//import static com.oasys.helpdesk.constant.Constant.TO_DATE;

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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AccessoriesRequestDTO;
import com.oasys.helpdesk.dto.AssetAccessoriesResponseDTO;
import com.oasys.helpdesk.dto.AssetBrandRequestDTO;
import com.oasys.helpdesk.dto.AssetListRequestDto;
import com.oasys.helpdesk.dto.AssetMapCountDto;
import com.oasys.helpdesk.dto.AssetMapEntityDto;
import com.oasys.helpdesk.dto.AssetNamebrandDto;
import com.oasys.helpdesk.dto.DeviceHardwareNameRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.SiteVisitResponseDTO;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetListEntity;
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
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DeviceHardwareRepository;
import com.oasys.helpdesk.repository.SupplierRepository;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.entity.DeviceRegisterEntity;
import com.oasys.posasset.entity.DevicestatusEntity;

@Service

public class AssetMapServiceImpl implements AssetMapService {
	@Autowired
	AssetMapRepository assetMapRepository;

	@Autowired
	private AssetTypeRepository assetTypeRepository;

	@Autowired
	private DeviceHardwareRepository deviceRepository;

	@Autowired
	private EntityDetailsRepository entityDetailsRepository;

	@Autowired
	private AssetStatusRepository assetStatusRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private AssetMapMapper assetmapmapper;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	AssetListRepository assetListRepository;

	@Override
	public GenericResponse addAssetType(AssetMapRequestDto requestDTO) {
		AssetMapEntity entity = commonUtil.modalMap(requestDTO, AssetMapEntity.class);

		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(requestDTO.getAssetTypeId());
		if (!assetTypeEntity.isPresent())

		{
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset type" }));
		}

		Optional<DeviceHardwareEntity> assetnameEntity = deviceRepository.findById(requestDTO.getAssetNameId());
		if (!assetnameEntity.isPresent())

		{
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Name" }));
		}

		Optional<EntityDetails> assetbrandentity = entityDetailsRepository.findById(requestDTO.getAssetGroupId());
		if (!assetbrandentity.isPresent())

		{
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Group" }));
		}

		Optional<AssetStatusEntity> assetstatusentity = assetStatusRepository
				.findByNameIgnoreCase(requestDTO.getStatusName());

		if (!assetstatusentity.isPresent())

		{
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Status" }));
		}

		Long asetname = assetnameEntity.get().getId();
		Long brand = assetbrandentity.get().getId();
		Long type = assetTypeEntity.get().getId();
		Long status = assetstatusentity.get().getId();

		String serialno = requestDTO.getSerialNo();
		Optional<AssetMapEntity> assetMappingOptional = assetMapRepository.getBySerialNo(serialno);
		if (assetMappingOptional.isPresent()) {
			throw new InvalidDataValidation("Record Already Exist");
		} else {
			entity.setAssetName(assetnameEntity.get());
			entity.setAssetType(assetTypeEntity.get());
			entity.setAssetGroup(assetbrandentity.get());
			entity.setAssetLocation(requestDTO.getAssetLocation());
			entity.setDistrict(requestDTO.getDistrict());
			entity.setDistrictId(requestDTO.getDistrictId());
			entity.setState(requestDTO.getState());
			entity.setStateId(requestDTO.getStateId());
			entity.setUserName(requestDTO.getUserName());
			entity.setSerialNo(requestDTO.getSerialNo());
			entity.setActive(requestDTO.isActive());
			entity.setDateOfInstallation(requestDTO.getDateOfInstallation());
			entity.setStatus(assetstatusentity.get());
			entity.setUserType(requestDTO.getUserType());
			entity.setUnitName(requestDTO.getUnitName());
			entity.setLicenseNo(requestDTO.getLicenseNo());
			entity.setDesignation(requestDTO.getDesignation());
			entity.setReason(requestDTO.getReason());
			entity.setUnitcode(requestDTO.getUnitcode());
			entity = assetMapRepository.save(entity);
			return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<AssetMapEntity> assetEntity = assetMapRepository.findById(id);
		if (!assetEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(assetmapmapper.convertEntityToResponseDTO(assetEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<AssetMapEntity> List = assetMapRepository.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse updateAssetmap(AssetMapRequestDto requestDTO) {

		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}

		Optional<AssetMapEntity> DeptOptional = assetMapRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		AssetMapEntity deptEntity = DeptOptional.get();

		AssetMapEntity entity = commonUtil.modalMap(requestDTO, AssetMapEntity.class);

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

		Optional<EntityDetails> assetbrandentity = entityDetailsRepository.findById(requestDTO.getAssetGroupId());
		if (!assetbrandentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Group" }));
		}
		Optional<AssetStatusEntity> assetstatusentity = assetStatusRepository
				.findByNameIgnoreCase(requestDTO.getStatusName());

		if (!assetstatusentity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Status" }));
		}

		deptEntity.setAssetName(assetnameEntity.get());
		deptEntity.setAssetType(assetTypeEntity.get());
		deptEntity.setAssetGroup(assetbrandentity.get());
		deptEntity.setSerialNo(requestDTO.getSerialNo());
		deptEntity.setAssetLocation(requestDTO.getAssetLocation());
		deptEntity.setDistrict(requestDTO.getDistrict());
		deptEntity.setDistrictId(requestDTO.getDistrictId());
		deptEntity.setState(requestDTO.getState());
		deptEntity.setStateId(requestDTO.getStateId());
		deptEntity.setUserName(requestDTO.getUserName());
		deptEntity.setStatus(assetstatusentity.get());
		deptEntity.setActive(requestDTO.isActive());
		deptEntity.setUserType(requestDTO.getUserType());
		deptEntity.setUnitName(requestDTO.getUnitName());
		deptEntity.setLicenseNo(requestDTO.getLicenseNo());
		deptEntity.setDesignation(requestDTO.getDesignation());
		deptEntity.setReason(requestDTO.getReason());
		deptEntity.setUnitcode(requestDTO.getUnitcode());

		deptEntity = assetMapRepository.save(deptEntity);

		try {
			if (requestDTO.getStatusName().equalsIgnoreCase("mapped")
					|| requestDTO.getStatusName().equalsIgnoreCase("Unmapped")) {

			} else {

				Optional<AssetListEntity> findByNameSerialNo = assetListRepository
						.findBySerialNo(requestDTO.getSerialNo());

				AssetListEntity assetListEntity = findByNameSerialNo.get();

				assetListEntity.setActive(false);
				assetListEntity = assetListRepository.save(assetListEntity);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);

	}

	@Override
	public GenericResponse getAll() {
		List<AssetMapEntity> AssetMapList = assetMapRepository.findAll();
		List<AssetMapResponseDTO> Assetlist = new ArrayList<AssetMapResponseDTO>();
		try {
			AssetMapList.stream().forEach(action -> {
				AssetMapResponseDTO entity = new AssetMapResponseDTO();
				entity.setId(action.getId());
				entity.setActive(action.isActive());
				entity.setCreatedBy(action.getCreatedBy());
				entity.setModifiedBy(action.getModifiedBy());
				EntityDetails assetGroup = action.getAssetGroup();
				entity.setAssetGroup(assetGroup.getEntityName());
				DeviceHardwareEntity assetName = action.getAssetName();
				entity.setAssetName(assetName.getDeviceName());
				AssetTypeEntity assetType = action.getAssetType();
				entity.setAssetType(assetType.getType());
				Long userid = (long) action.getCreatedBy();
				String createdByUserName = commonDataController.getUserNameById(userid);
				String modifiedByUserName = commonDataController.getUserNameById(userid);
				entity.setCreatedByName(createdByUserName);
				entity.setModifiedByName(modifiedByUserName);
				entity.setCreatedDate(action.getCreatedDate().toString());
				entity.setModifiedDate(action.getModifiedDate().toString());
				entity.setSerialNo(action.getSerialNo());
				entity.setAssetLocation(action.getAssetLocation());
				entity.setDistrict(action.getDistrict());
				entity.setUserName(action.getUserName());
				entity.setActive(action.isActive());
				entity.setDateOfInstallation(action.getDateOfInstallation());
				entity.setUserType(action.getUserType());
				entity.setUnitName(action.getUnitName());
				entity.setLicenseNo(action.getLicenseNo());
				entity.setDesignation(action.getDesignation());
				entity.setReason(action.getReason());
				entity.setUnitName(action.getUnitcode());
				Assetlist.add(entity);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (CollectionUtils.isEmpty(Assetlist)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(Assetlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData, authenticationDTO);

		if (count > 0) {
			List<AssetMapEntity> list = this.getRecordsByFilterDTO(requestData, authenticationDTO);

			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}

			List<AssetMapResponseDTO> dtoList = list.stream().map(assetmapmapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());

			// Long listcount = (long) list.size();
			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	public List<AssetMapEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssetMapEntity> cq = cb.createQuery(AssetMapEntity.class);
		Root<AssetMapEntity> from = cq.from(AssetMapEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<AssetMapEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField("MODIFIED_DATE");// double quotes instead of constant
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase("ASC")) {
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

		List<AssetMapEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}

	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<AssetMapEntity> from = cq.from(AssetMapEntity.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteria(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private void addCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<AssetMapEntity> from, AuthenticationDTO authenticationDTO) throws ParseException {
		DevicestatusEntity div = new DevicestatusEntity();
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {

				if (Objects.nonNull(filterRequestDTO.getFilters().get("FROM_DATE"))
						&& !filterRequestDTO.getFilters().get("TO_DATE").toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat("DATE_FORMAT")
							.parse(filterRequestDTO.getFilters().get("FROM_DATE").toString() + " " + "START_TIME");
					Date toDate = new SimpleDateFormat("DATE_FORMAT")
							.parse(filterRequestDTO.getFilters().get("TO_DATE").toString() + " " + "END_TIME");
					list.add(cb.between(from.get("CREATED_DATE"), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("statusId"))
						&& !filterRequestDTO.getFilters().get("statusId").toString().trim().isEmpty()) {

					Long supplierid = Long.valueOf(filterRequestDTO.getFilters().get("statusId").toString());
					list.add(cb.equal(from.get("status"), supplierid));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetTypeId"))
						&& !filterRequestDTO.getFilters().get("assetTypeId").toString().trim().isEmpty()) {

					Long assetTypeId = Long.valueOf(filterRequestDTO.getFilters().get("assetTypeId").toString());
					list.add(cb.equal(from.get("assetType"), assetTypeId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetNameId"))
						&& !filterRequestDTO.getFilters().get("assetNameId").toString().trim().isEmpty()) {

					Long deviceName = Long.valueOf(filterRequestDTO.getFilters().get("assetNameId").toString());
					list.add(cb.equal(from.get("deviceName"), deviceName));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("assetGroupId"))
						&& !filterRequestDTO.getFilters().get("assetGroupId").toString().trim().isEmpty()) {

					Long assetBrandId = Long.valueOf(filterRequestDTO.getFilters().get("assetGroupId").toString());
					list.add(cb.equal(from.get("brand"), assetBrandId));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("serialNo"))
						&& !filterRequestDTO.getFilters().get("serialNo").toString().trim().isEmpty()) {

					String serialNo = String.valueOf(filterRequestDTO.getFilters().get("serialNo").toString());
					list.add(cb.equal(from.get("serialNo"), serialNo));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("dateOfInstallation"))
						&& !filterRequestDTO.getFilters().get("dateOfInstallation").toString().trim().isEmpty()) {

					String dateOfPurchase = String
							.valueOf(filterRequestDTO.getFilters().get("dateOfInstallation").toString());
					list.add(cb.equal(from.get("dateOfInstallation"), dateOfPurchase));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("user_type"))
						&& !filterRequestDTO.getFilters().get("user_type").toString().trim().isEmpty()) {

					String usertype = String.valueOf(filterRequestDTO.getFilters().get("user_type").toString());
					list.add(cb.equal(from.get("userType"), usertype));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("unit_name"))
						&& !filterRequestDTO.getFilters().get("unit_name").toString().trim().isEmpty()) {

					String unitname = String.valueOf(filterRequestDTO.getFilters().get("unit_name").toString());
					list.add(cb.equal(from.get("unitName"), unitname));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("licenseno"))
						&& !filterRequestDTO.getFilters().get("licenseno").toString().trim().isEmpty()) {

					String licenseno = String.valueOf(filterRequestDTO.getFilters().get("licenseno").toString());
					list.add(cb.equal(from.get("licensenNo"), licenseno));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("designation"))
						&& !filterRequestDTO.getFilters().get("designation").toString().trim().isEmpty()) {

					String designation = String.valueOf(filterRequestDTO.getFilters().get("designation").toString());
					list.add(cb.equal(from.get("Designation"), designation));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("reason"))
						&& !filterRequestDTO.getFilters().get("reason").toString().trim().isEmpty()) {

					String reason = String.valueOf(filterRequestDTO.getFilters().get("reason").toString());
					list.add(cb.equal(from.get("reason"), reason));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get("unitcode"))
						&& !filterRequestDTO.getFilters().get("unitcode").toString().trim().isEmpty()) {

					// String unitcode =
					// String.valueOf(filterRequestDTO.getFilters().get("unitcode").toString());
					// list.add(cb.equal(from.get("unitcode"), unitcode));

					List<String> unitcodes = (List<String>) (filterRequestDTO.getFilters().get("unitcode"));

					boolean empty = unitcodes.isEmpty();
					if (empty == true) {
						System.out.println("The ArrayList is empty");
					} else {
						System.out.println("The ArrayList is not empty");

						Expression<String> mainModule = from.get("unitcode");
						list.add(mainModule.in(unitcodes));
					}
				}
			}

		} catch (ParseException e) {
			throw new InvalidParameterException("Invalid filter value passed!");
		}
	}

	public GenericResponse getCount(String code) {
		try {
			List<AssetMapCountDto> entitysummary = assetMapRepository.getCount(code);

			boolean allCountsZero = entitysummary.stream().allMatch(count -> count.getTotaldevice() == 0);

			if (entitysummary.isEmpty() || allCountsZero) {
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

	public GenericResponse getassetSummaryCount(String assetType) {
		try {
			List<AssetMapEntityDto> entitysummary = assetMapRepository.getassetSummaryCount(assetType);
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
					"An error occurred while retrieving entity wise summary count.", e);
		}
	}

	

}
