package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CREATED_DATE;
import static com.oasys.helpdesk.constant.Constant.DATE_FORMAT;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.DEVICE_HARDWARE_LIST;
import static com.oasys.helpdesk.constant.Constant.END_TIME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.START_TIME;
import static com.oasys.helpdesk.constant.Constant.STATUS;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.DeviceHardwareAddRequestDTO;
import com.oasys.helpdesk.dto.DeviceHardwareAddResponseDTO;
import com.oasys.helpdesk.dto.DeviceHardwareListResponseDTO;
import com.oasys.helpdesk.dto.DeviceHardwareNameRequestDTO;
import com.oasys.helpdesk.dto.DevicehardwareNameResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.WorkflowNotificationResponseDTO;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.entity.workflowNotificationEntity;
import com.oasys.helpdesk.mapper.DeviceHardwareMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetBrandRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DeviceHardwareRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.POSAssetRequestStatus;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeviceHardwareServiceImpl  implements DeviceHardwareService{

	public static final String DEVICE_HARDWARE = "device_hardware_name";

	@Autowired
	private DeviceHardwareRepository deviceRepository;

//	@Autowired
//	private DeviceHardwareListRepository deviceListRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private DeviceHardwareMapper deviceHardwareMapper;

	@Autowired
	private PaginationMapper paginationMapper;
	
	@Autowired
	private AssetTypeRepository assetTypeRepository;
	
	@Autowired
	private AssetBrandRepository assetBrandRepository;
	
	@Autowired
	private EntityManager entityManager;

	@Override
	@Transactional
	public GenericResponse addDevice(DeviceHardwareNameRequestDTO requestDTO) {
		
		Optional<DeviceHardwareEntity> deviceEntityOptional = deviceRepository.findByDeviceNameIgnoreCase(requestDTO.getDeviceName());
		if(deviceEntityOptional.isPresent()) {
			throw new InvalidDataValidation("Record Already exist");
		}
		
		DeviceHardwareEntity entity = commonUtil.modalMap(requestDTO, DeviceHardwareEntity.class);
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByTypeIgnoreCase(requestDTO.getAssetType());
		if(!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "type" }));
		}
	   entity.setType(assetTypeEntity.get());
	   entity=deviceRepository.save(entity);
		return Library.getSuccessfulResponse(entity,  ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}


	@Override
	public GenericResponse updateDevice(DeviceHardwareNameRequestDTO requestDTO) {

		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<DeviceHardwareEntity> deviceEntityOptional = deviceRepository
				.findById(requestDTO.getId());
		if(!deviceEntityOptional.isPresent()) {
			throw new InvalidDataValidation("Record not exist");
		}

		DeviceHardwareEntity entity = commonUtil.modalMap(requestDTO, DeviceHardwareEntity.class);

		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByTypeIgnoreCase(requestDTO.getAssetType());
		if(!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "type" }));
		}
		entity.setType(assetTypeEntity.get());
		entity.setStatus(requestDTO.isStatus());
		entity.setCreatedBy(requestDTO.getCreatedBy());
      	entity=deviceRepository.save(entity);
		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<DeviceHardwareEntity> list = null;
		String brand = null;
		String type = null;
		String deviceName = null;
		Boolean status = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {

			if (Objects.nonNull(paginationDto.getFilters().get("brand"))
					&& !paginationDto.getFilters().get("brand").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					brand = (String) paginationDto.getFilters().get("brand");
				} catch (Exception e) {
					log.error("error occurred while parsing brand :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("type"))
					&& !paginationDto.getFilters().get("type").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					type = (String) paginationDto.getFilters().get("type");
				} catch (Exception e) {
					log.error("error occurred while parsing type :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("deviceName"))
					&& !paginationDto.getFilters().get("deviceName").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					deviceName = (String) paginationDto.getFilters().get("deviceName");
				} catch (Exception e) {
					log.error("error occurred while parsing deviceName :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				try {
					status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

		}
		list = getByFilter(brand, type, deviceName, status, pageable);


		if (Objects.isNull(list)) {
			list = deviceRepository.getAllSubString(deviceName, pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<DevicehardwareNameResponseDTO> finalResponse = list.map(deviceHardwareMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


	private Page<DeviceHardwareEntity> getByFilter(String brand, String type, String deviceName,Boolean status, Pageable pageable){
		Page<DeviceHardwareEntity> list = null;
		if (Objects.isNull(list) && Objects.nonNull(type) && Objects.nonNull(brand) && Objects.nonNull(deviceName) && Objects.nonNull(status)) {
			//list = deviceRepository.getByTypeStatusDeviceNameAndBrand(type, status, deviceName, brand, pageable);
		} 

		return list;
	}


	@Override
	public GenericResponse getAll() {
		List<DeviceHardwareEntity> deviceName = deviceRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(deviceName)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DevicehardwareNameResponseDTO> responseName = deviceName.stream()
				.map(deviceHardwareMapper::convertEntityToResponseDTO).collect(Collectors.toList());

		return Library.getSuccessfulResponse(responseName, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}



	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(DEVICE_HARDWARE);
		String DeviceCode = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
//			Optional<DeviceHardwareEntity> deviceHardwareEntity = deviceRepository.findByDeviceCodeIgnoreCase(DeviceCode);
//			if (deviceHardwareEntity.isPresent()) {
//				DeviceCode = prefix.toString() + RandomUtil.getRandomNumber();
//			} else {
//				break;
//			}
		}
	}

	@Override
	public GenericResponse getAllActive() {
		List<DeviceHardwareEntity> List = deviceRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DevicehardwareNameResponseDTO> ResponseList = List.stream()
				.map(deviceHardwareMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getByDeviceCode(String deviceCode) {
//		Optional<DeviceHardwareEntity> deviceHardwareEntity = deviceRepository.findByDeviceCode(deviceCode);
//		if (!deviceHardwareEntity.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}

//		DevicehardwareNameResponseDTO resp = (deviceHardwareMapper.convertEntityToResponseDTO(deviceHardwareEntity.get()));
//		Optional<List<DeviceHardwareListEntity>> response = deviceListRepository.findByListDeviceCode(deviceCode);
//		if(response.isPresent()) {
//			List<DeviceHardwareListEntity> elist = response.get();
//			resp.setListResponseDTO(elist.stream().map(item->deviceHardwareMapper.convertListEntityToListResponseDTO(item)).collect(Collectors.toList()));
//		}


//		return Library.getSuccessfulResponse(resp, //deviceHardwareMapper.convertEntityToResponseDTO(deviceHardwareEntity.get()),
//				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	return null;
	}


	@Override
	public GenericResponse getAllList() {
//		List<DeviceHardwareListEntity> deviceHardwareList = deviceListRepository.findAllByOrderByModifiedDateDesc();
//		if (CollectionUtils.isEmpty(deviceHardwareList)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		List<DeviceHardwareListResponseDTO> ResponseList = deviceHardwareList.stream()
				//.map(deviceHardwareMapper::convertListEntityToListResponseDTO).collect(Collectors.toList());
//		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
				
				return null;
	}


	@Override
	public GenericResponse getDeviceId(DeviceHardwareAddRequestDTO payload) {
		MenuPrefix prefix = MenuPrefix.getType(DEVICE_HARDWARE_LIST);
		List<String> result = new ArrayList<>();
		
		while (result.size() < payload.getNoOfDevices()) {
			String deviceId = prefix.toString() + RandomUtil.getRandomNumber();
//			Optional<DeviceHardwareListEntity> deviceHardwareListEntity = deviceListRepository.findByDeviceIdIgnoreCase(deviceId);
//			if (!deviceHardwareListEntity.isPresent()) {
//				if(!result.contains(deviceId)) {
//					result.add(deviceId);
//				}
//			}
		}

		List<DeviceHardwareAddResponseDTO> response = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			String deviceId = result.get(i);
			DeviceHardwareAddResponseDTO item = new DeviceHardwareAddResponseDTO();
			item.setDeviceId(deviceId);
			item.setHardwareName(payload.getHardwareName());
			item.setDeviceStatus("");
			item.setRegisteredDate(null);
			item.setStatus(false);
			item.setExpiryDate(null);
			item.setDeviceSerialNo("");
			item.setWarranty(false);
			
			response.add(item);
		}
		return Library.getSuccessfulResponse(response, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


	@Override
	public GenericResponse getAllActiveList() {
//		List<DeviceHardwareListEntity> List = deviceListRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
//		if (CollectionUtils.isEmpty(List)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		List<DeviceHardwareListResponseDTO> ResponseList = List.stream()
//				.map(deviceHardwareMapper::convertListEntityToListResponseDTO).collect(Collectors.toList());
//		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
		
		return null;
	}
	
	@Override
	public GenericResponse getAllByAssetTypeAndAssetBrandId(Long assetTypeId, Long assetBrandId) {
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(assetTypeId);
		if (!assetTypeEntity.isPresent() || Boolean.FALSE.equals(assetTypeEntity.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {Constant.ASSET_TYPE_ID}));
		}
		Optional<AssetBrandEntity> asseBrandEntity = assetBrandRepository.findById(assetBrandId);
		if (!asseBrandEntity.isPresent() || Boolean.FALSE.equals(asseBrandEntity.get().getStatus())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {Constant.ASSET_BRAND_ID}));
		}
//		List<DeviceHardwareEntity> deviceName = deviceRepository.findByBrandAndTypeAndStatusOrderByModifiedDateDesc(
//				asseBrandEntity.get(), assetTypeEntity.get(), Boolean.TRUE);
		
//		if (CollectionUtils.isEmpty(deviceName)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		List<DevicehardwareNameResponseDTO> responseName = deviceName.stream()
//				.map(deviceHardwareMapper::convertEntityToResponseDTO).collect(Collectors.toList());

//		return Library.getSuccessfulResponse(responseName, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
		
		return null;
	}
	
	@Override
	public GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		Long count = this.getCountBySearchFields(requestData);
		log.info("total count :: {}", count);
		if (count > 0) {
			List<DeviceHardwareEntity> list = this.getRecordsByFilterDTO(requestData);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}
			List<DevicehardwareNameResponseDTO> dtoList = list.stream()
					.map(deviceHardwareMapper::convertEntityToResponseDTO).collect(Collectors.toList());
			
			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		}
		else {
			throw new RecordNotFoundException("No Record Found");
		}
	}
	
	public List<DeviceHardwareEntity> getRecordsByFilterDTO(PaginationRequestDTO filterRequestDTO) throws ParseException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeviceHardwareEntity> cq = cb.createQuery(DeviceHardwareEntity.class);
		Root<DeviceHardwareEntity> from = cq.from(DeviceHardwareEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<DeviceHardwareEntity> typedQuery = null;
		addCriteria(cb, list, filterRequestDTO, from);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
			
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));

		}
		if (Objects.nonNull(filterRequestDTO.getPaginationSize()) && Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}

		List<DeviceHardwareEntity> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}
		return data;
	}
	
	private Long getCountBySearchFields(PaginationRequestDTO filterRequestDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<DeviceHardwareEntity> from = cq.from(DeviceHardwareEntity.class);
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
			Root<DeviceHardwareEntity> from) throws ParseException {
		
		try {
			if (Objects.nonNull(filterRequestDTO.getFilters())) {
				log.info("filters ::" + filterRequestDTO.getFilters());

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.FROM_DATE))
						&& !filterRequestDTO.getFilters().get(Constant.TO_DATE).toString().trim().isEmpty()) {

					Date fromDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(Constant.FROM_DATE).toString() + " " + START_TIME);
					Date toDate = new SimpleDateFormat(DATE_FORMAT)
							.parse(filterRequestDTO.getFilters().get(Constant.TO_DATE).toString() + " " + END_TIME);
					list.add(cb.between(from.get(CREATED_DATE), fromDate, toDate));

				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.ASSET_TYPE_ID))
						&& !filterRequestDTO.getFilters().get(Constant.ASSET_TYPE_ID).toString().trim().isEmpty()) {
					Long type = Long.valueOf(filterRequestDTO.getFilters().get(Constant.ASSET_TYPE_ID).toString());
					list.add(cb.equal(from.get("type"), type));
				}

				if (Objects.nonNull(filterRequestDTO.getFilters().get(Constant.STATUS))
						&& !filterRequestDTO.getFilters().get(Constant.STATUS).toString().trim().isEmpty()) {
					Boolean status = Boolean.valueOf(filterRequestDTO.getFilters().get(Constant.STATUS).toString());
					list.add(cb.equal(from.get(Constant.STATUS), status));
				}
				if (Objects.nonNull(filterRequestDTO.getFilters().get("deviceName"))
						&& !filterRequestDTO.getFilters().get("deviceName").toString().trim().isEmpty()) {
					String deviceName = String.valueOf(filterRequestDTO.getFilters().get("deviceName").toString());
					list.add(cb.equal(from.get("deviceName"), deviceName));
				}
				

				if (Objects.nonNull(filterRequestDTO.getFilters().get("createdBy"))
						&& !filterRequestDTO.getFilters().get("createdBy").toString().trim().isEmpty()) {
					Long createdBy = Long.valueOf(filterRequestDTO.getFilters().get("createdBy").toString());
					list.add(cb.equal(from.get("createdBy"), createdBy));
				}

			}
		} catch (ParseException e) {
			throw new InvalidParameterException("No Record Found");
		}
	}
	
	
//	@Override
//	public GenericResponse getById(Long id) {
//		List<DeviceHardwareEntity> entity = deviceRepository.getById(id);
//		if (entity.isEmpty()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		List<DevicehardwareNameResponseDTO> dtoList = entity.stream()
//				.map(deviceHardwareMapper::convertEntityToResponseDTO).collect(Collectors.toList());
//		
//		return Library.getSuccessfulResponse(dtoList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	}
//	
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<DeviceHardwareEntity> assetEntity = deviceRepository.findById(id);
		if (!assetEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(deviceHardwareMapper.convertEntityToResponseDTO(assetEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
