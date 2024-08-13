package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AccessoriesAddRequestDTO;
import com.oasys.helpdesk.dto.AccessoriesAddResponseDTO;
import com.oasys.helpdesk.dto.AccessoriesListDataResponseDTO;
import com.oasys.helpdesk.dto.AccessoriesRequestDTO;
import com.oasys.helpdesk.dto.AccessoriesResponseDTO;
import com.oasys.helpdesk.dto.DeviceHardwareAddRequestDTO;
import com.oasys.helpdesk.dto.DeviceHardwareAddResponseDTO;
import com.oasys.helpdesk.dto.DeviceHardwareNameRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AccessoriesListData;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.mapper.AccessoriesMapper;
import com.oasys.helpdesk.mapper.DeviceHardwareMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AccessoriesListRepository;
import com.oasys.helpdesk.repository.AccessoriesNameRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DeviceHardwareRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service 
@Log4j2
public class AccessoriesNameServiceImpl implements AccessoriesNameService{

	@Autowired
	private AccessoriesNameRepository accessoriesRepo;

	@Autowired
	private AccessoriesListRepository accessoriesListRepo;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private AccessoriesMapper accessoriesMapper;
	
	@Autowired
	private DeviceHardwareRepository deviceRepository;

	@Autowired
	private AssetTypeRepository assetTypeRepository;


	@Override
	public GenericResponse addAccessoriesName(AccessoriesRequestDTO requestDTO) {

		Accessories entity = commonUtil.modalMap(requestDTO, Accessories.class);

		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByTypeIgnoreCase(requestDTO.getAssetType());
		if (!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "type" }));
		}

		Optional<DeviceHardwareEntity> assetnameEntity = deviceRepository
				.findByDeviceNameIgnoreCase(requestDTO.getAssetName());
		if (!assetnameEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Name" }));
		}
		List<Accessories> deviceHardwareOptional1 = accessoriesRepo.findAllByTypeAndAssetNameAndAssetsubType(
				assetTypeEntity.get(), assetnameEntity.get(), requestDTO.getAssetsubType());
		if (!deviceHardwareOptional1.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Asset Sub Type" }));
		}

		entity.setAssetName(assetnameEntity.get());
		entity.setType(assetTypeEntity.get());
		entity.setAssetsubType(requestDTO.getAssetsubType());
		entity.setStatus(requestDTO.isStatus());
		entity = accessoriesRepo.save(entity);

		return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<Accessories> assetAccessories = accessoriesRepo.findById(id);
		if (!assetAccessories.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(accessoriesMapper.convertEntityToResponseDTO(assetAccessories.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	

	@Override
	public GenericResponse updateDevice(AccessoriesRequestDTO requestDTO) {

		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<Accessories> deviceEntityOptional = accessoriesRepo
				.findById(requestDTO.getId());
		if(!deviceEntityOptional.isPresent()) {
			throw new InvalidDataValidation("Record not exist");
		}

		Accessories entity = commonUtil.modalMap(requestDTO, Accessories.class);
    	Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByTypeIgnoreCase(requestDTO.getAssetType());
		if(!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "type" }));
		}
		
		Optional<DeviceHardwareEntity> assetnameEntity = deviceRepository.findByDeviceNameIgnoreCase(requestDTO.getAssetName());
		if(!assetnameEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "Asset Name" }));
		}
		
		List<Accessories> deviceHardwareOptional1 = accessoriesRepo.findAllByTypeAndAssetNameAndAssetsubType(
				assetTypeEntity.get(), assetnameEntity.get(), requestDTO.getAssetsubType());
		if (!deviceHardwareOptional1.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Asset Sub Type" }));
		}
		
	   entity.setAssetName(assetnameEntity.get());
	   entity.setType(assetTypeEntity.get());
	   entity.setAssetsubType(requestDTO.getAssetsubType());
	   entity.setStatus(requestDTO.isStatus());
	   entity.setCreatedBy(requestDTO.getCreatedBy());
	   entity=accessoriesRepo.save(entity); 
	   return Library.getSuccessfulResponse(entity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}



	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<Accessories> list = null;
		Long assetname = null;
		Long assettype = null;
		Long createdBy=null;
		String assetsubtype = null;
		String accessoriesName= null;
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

			if (Objects.nonNull(paginationDto.getFilters().get("assetName"))
					&& !paginationDto.getFilters().get("assetName").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					assetname = Long.valueOf(paginationDto.getFilters().get("assetName").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assetName :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("type"))
					&& !paginationDto.getFilters().get("type").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					assettype = Long.valueOf(paginationDto.getFilters().get("type").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assettype :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("assetsubType"))
					&& !paginationDto.getFilters().get("assetsubType").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					assetsubtype =String.valueOf(paginationDto.getFilters().get("assetsubType").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assetsubType :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get("createdBy"))
					&& !paginationDto.getFilters().get("createdBy").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					createdBy = Long.valueOf(paginationDto.getFilters().get("createdBy").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing createdBy :: {}", e);
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
		list = getByFilter(assetname, assettype, assetsubtype, createdBy, status, pageable);


		if (Objects.isNull(list)) {
			list = accessoriesRepo.getAllSubString(assetsubtype,pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		Page<AccessoriesResponseDTO> finalResponse = list.map(accessoriesMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


	private Page<Accessories> getByFilter(Long assetname, Long assettype, String assetsubtype, Long createdBy, Boolean status, Pageable pageable){
		Page<Accessories> list = null;

		if (Objects.nonNull(assettype) && Objects.nonNull(assetname) && Objects.nonNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByAssetNameAndTypeAndAssetsubTypeAndStatus(assetname,assettype,assetsubtype,status,pageable);
		} 
		
		else if (Objects.nonNull(assettype) && Objects.isNull(assetname) && Objects.isNull(assetsubtype) && Objects.isNull(status)) {
			list = accessoriesRepo.getByType(assettype,pageable);
		} 
		
		else if(Objects.isNull(assettype) && Objects.isNull(assetname) && Objects.nonNull(assetsubtype) && Objects.isNull(status)) {
			list = accessoriesRepo.getByAssetsubType(assetsubtype,pageable);
		} 
		
		else if (Objects.isNull(assettype) && Objects.isNull(assetname) && Objects.isNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByStatus(status,pageable);
		} 
		
		else if (Objects.isNull(assettype) && Objects.nonNull(assetname) && Objects.isNull(assetsubtype) && Objects.isNull(status)) {
			list = accessoriesRepo.getByAssetName(assetname,pageable);
		} 
		
		
		else if (Objects.isNull(assettype) && Objects.nonNull(assetname) && Objects.isNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByAssetNameAndStatus(assetname,status,pageable);
		} 
		
		else if (Objects.nonNull(assettype) && Objects.isNull(assetname) && Objects.isNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByTypeAndStatus(assettype,status,pageable);
		} 
		
		
		else if (Objects.isNull(assettype) && Objects.isNull(assetname) && Objects.nonNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByAssetsubTypeAndStatus(assetsubtype,status,pageable);
		} 
		else if (Objects.isNull(assettype) && Objects.isNull(assetname) && Objects.isNull(assetsubtype) && Objects.isNull(status)) {
			list = accessoriesRepo.getByAssetsubTypeAndStatus(assetsubtype,status,pageable);
		}
		
		else if (Objects.isNull(assettype) && Objects.isNull(assetname) && Objects.isNull(assetsubtype) && Objects.isNull(status)) {
			list = accessoriesRepo.getByCreatedBy(createdBy,pageable);
		}
		
		else if (Objects.nonNull(assettype) && Objects.nonNull(assetname) && Objects.isNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByAssetNameAndTypeAndStatus(assetname,assettype,status,pageable);
		} 
		else if (Objects.nonNull(assettype) && Objects.isNull(assetname) && Objects.nonNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByAssetsubTypeAndTypeAndStatus(assetsubtype,assettype,status,pageable);
		} 
		else if (Objects.isNull(assettype) && Objects.nonNull(assetname) && Objects.nonNull(assetsubtype) && Objects.nonNull(status)) {
			list = accessoriesRepo.getByAssetNameAndAssetsubTypeAndStatus(assetname,assetsubtype,status,pageable);
		} 
		

		return list;
	}


	@Override
	public GenericResponse getAll() {
		List<Accessories> deviceName = accessoriesRepo.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(deviceName)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AccessoriesResponseDTO> responseName = deviceName.stream()
				.map(accessoriesMapper::convertEntityToResponseDTO).collect(Collectors.toList());

		return Library.getSuccessfulResponse(responseName, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


	public static final String ACCESSORIES_CODE = "accesssories_code";
	@Override
	public GenericResponse getCode() {
//		MenuPrefix prefix = MenuPrefix.getType(ACCESSORIES_CODE);
//		String accessoriesCode = prefix.toString() + RandomUtil.getRandomNumber();
//		while (true) {
//			Optional<Accessories> Entity = accessoriesRepo.findByAccessoriesCodeIgnoreCase(accessoriesCode);
//			if (Entity.isPresent()) {
//				accessoriesCode = prefix.toString() + RandomUtil.getRandomNumber();
//			} else {
//				break;
//			}
//		}
	return null;	
//		return Library.getSuccessfulResponse(accessoriesCode, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
	}

	public static final String ACCESSORIES_NAME_ID = "accesssories_name_id";
	
	
	@Override
	public GenericResponse getDeviceId(AccessoriesAddRequestDTO payload) {
		MenuPrefix prefix = MenuPrefix.getType(ACCESSORIES_NAME_ID);
		
		List<String> result = new ArrayList<>();
		while (result.size() < payload.getNoOfDevices()) {
			String accessoriesNameId = prefix.toString() + RandomUtil.getRandomNumber();
			Optional<AccessoriesListData> ListEntity = accessoriesListRepo.findByAccessoriesNameIdIgnoreCase(accessoriesNameId);
			if (!ListEntity.isPresent()) {
				if(!result.contains(accessoriesNameId)) {
					result.add(accessoriesNameId);
				}
			}
		
		}
		List<AccessoriesAddResponseDTO> response = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			String deviceId = result.get(i);
			AccessoriesAddResponseDTO item = new AccessoriesAddResponseDTO();
			item.setAccessoriesNameId(deviceId);
			item.setAccessoriesName(payload.getAccessoriesName());
			item.setAccessoriesSerialNo("");
			item.setRegisteredDate(null);
			item.setExpiredDate(null);
			item.setAccessoriesStatus("");
			item.setWarranty(null);
			item.setStatus(false);
			response.add(item);
		}
		
		return Library.getSuccessfulResponse(response, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<Accessories> List = accessoriesRepo.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AccessoriesResponseDTO> ResponseList = List.stream()
				.map(accessoriesMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllListActive() {
		List<AccessoriesListData> List = accessoriesListRepo.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AccessoriesListDataResponseDTO> ResponseList = List.stream()
				.map(accessoriesMapper::convertListEntityToListResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllList() {
		List<AccessoriesListData> accessList = accessoriesListRepo.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(accessList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AccessoriesListDataResponseDTO> ResponseList = accessList.stream()
				.map(accessoriesMapper::convertListEntityToListResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}




	@Override
	public GenericResponse getByaccessCode(String accessoriesCode) {
//		Optional<Accessories> Entity = accessoriesRepo.findByAccessoriesCode(accessoriesCode);
//		if (!Entity.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//
//		AccessoriesResponseDTO resp = (accessoriesMapper.convertEntityToResponseDTO(Entity.get()));
//		Optional<List<AccessoriesListData>> response = accessoriesListRepo.findByAccessoriesCode(accessoriesCode);
//		if(response.isPresent()) {
//			List<AccessoriesListData> elist = response.get();
//			resp.setListrequestDTO(elist.stream().map(item->accessoriesMapper.convertListEntityToListResponseDTO(item)).collect(Collectors.toList()));
//		}
return null;

//		return Library.getSuccessfulResponse(resp,  //accessoriesMapper.convertEntityToResponseDTO(Entity.get()),
//				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}


}


















