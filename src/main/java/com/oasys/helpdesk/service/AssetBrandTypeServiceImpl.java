package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.ASSET_BRAND_ID;
import static com.oasys.helpdesk.constant.Constant.ASSET_TYPE_ID;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;

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

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetBrandTypeRequestDTO;
import com.oasys.helpdesk.dto.AssetBrandTypeResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AssetBrandTypeEntity;
import com.oasys.helpdesk.mapper.AssetBrandTypeMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetBrandTypeRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AssetBrandTypeServiceImpl implements AssetBrandTypeService{

	@Autowired
	private AssetBrandTypeRepository assetBrandTypeRepository;
	
	@Autowired
	private AssetBrandTypeMapper assetBrandTypeMapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String ASSET_BRAND_TYPE= "Asset Brand Type";
	public static final String TYPE= "type.id";
	public static final String BRAND= "brand.id";
	
	@Override
	public GenericResponse add(AssetBrandTypeRequestDTO requestDTO)	{

		Optional<AssetBrandTypeEntity> assetBrandTypeOptional = assetBrandTypeRepository
				.findByCodeIgnoreCase(requestDTO.getCode());
		if (assetBrandTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		assetBrandTypeOptional = assetBrandTypeRepository.findByBrandAndType(requestDTO.getAssetBrandId(), requestDTO.getAssetTypeId());
		if (assetBrandTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}
		requestDTO.setId(null);
		AssetBrandTypeEntity entity = assetBrandTypeMapper.convertRequestDTOToEntity(requestDTO,null);
		assetBrandTypeRepository.save(entity);
		return Library.getSuccessfulResponse(assetBrandTypeMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse update(AssetBrandTypeRequestDTO requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		Optional<AssetBrandTypeEntity> assetBrandTypeEntity = assetBrandTypeRepository.findByBrandAndTypeNotInId(requestDTO.getAssetBrandId(),
				requestDTO.getAssetTypeId(), requestDTO.getId());
		if (assetBrandTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		}
		assetBrandTypeEntity = assetBrandTypeRepository.findById(requestDTO.getId());
		if (!assetBrandTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		requestDTO.setCode(assetBrandTypeEntity.get().getCode());
		
		AssetBrandTypeEntity entity = assetBrandTypeMapper.convertRequestDTOToEntity(requestDTO, assetBrandTypeEntity.get());
		assetBrandTypeRepository.save(entity);
		return Library.getSuccessfulResponse(assetBrandTypeMapper.convertEntityToResponseDTO(entity), ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(ASSET_BRAND_TYPE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<AssetBrandTypeEntity> assetBrandTypeEntity = assetBrandTypeRepository.findByCodeIgnoreCase(code);
			if (assetBrandTypeEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<AssetBrandTypeEntity> assetBrandTypeEntity = assetBrandTypeRepository.findById(id);
		if (!assetBrandTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(assetBrandTypeMapper.convertEntityToResponseDTO(assetBrandTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAll() {
		List<AssetBrandTypeEntity> assetBrandTypeList = assetBrandTypeRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetBrandTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetBrandTypeResponseDTO> assetBrandResponseList = assetBrandTypeList.stream()
				.map(assetBrandTypeMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<AssetBrandTypeEntity> list = null;
		Long brand = null;
		Boolean status = null;
		Long type = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
			if(ASSET_TYPE_ID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(TYPE);
			}
			if(ASSET_BRAND_ID.equals(paginationDto.getSortField())) {
				paginationDto.setSortField(BRAND);
			}
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(ASSET_BRAND_ID))
					&& !paginationDto.getFilters().get(ASSET_BRAND_ID).toString().trim().isEmpty()) {
				try {
					brand = Long.valueOf(paginationDto.getFilters().get(ASSET_BRAND_ID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing assetBrandId :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(ASSET_TYPE_ID))
					&& !paginationDto.getFilters().get(ASSET_TYPE_ID).toString().trim().isEmpty()) {
				
				try {
					type = Long.valueOf(paginationDto.getFilters().get(ASSET_TYPE_ID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing assetTypeId :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				try {
					status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing assetTypeId :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}
		}
		list = getByFilter(brand, type, status, pageable);
		if (Objects.isNull(list)) {
			list = assetBrandTypeRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<AssetBrandTypeResponseDTO> finalResponse = list.map(assetBrandTypeMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	private Page<AssetBrandTypeEntity> getByFilter(Long brand, Long type, Boolean status, Pageable pageable){
		Page<AssetBrandTypeEntity> list = null;
		if (Objects.nonNull(type) && Objects.nonNull(brand) && Objects.nonNull(status)) {
			list = assetBrandTypeRepository.getByTypeStatusAndBrand(type, status, brand, pageable);
		} 
		if (Objects.isNull(list) && Objects.nonNull(type) && Objects.nonNull(brand) && Objects.isNull(status)) {
			list = assetBrandTypeRepository.getByTypeAndBrand(type, brand, pageable);
		} 
		if (Objects.isNull(list) && Objects.isNull(type) && Objects.nonNull(brand) && Objects.nonNull(status)) {
			list = assetBrandTypeRepository.getByBrandAndStatus(brand, status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(type) && Objects.isNull(brand) && Objects.nonNull(status)) {
			list = assetBrandTypeRepository.getByTypeAndStatus(type, status, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(type) && Objects.isNull(brand) && Objects.nonNull(status)) {
			list = assetBrandTypeRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list) && Objects.nonNull(type) && Objects.isNull(brand) && Objects.isNull(status)) {
			list = assetBrandTypeRepository.getByType(type, pageable);
		}
		if (Objects.isNull(list) && Objects.isNull(type) && Objects.nonNull(brand) && Objects.isNull(status)) {
			list = assetBrandTypeRepository.getByBrand(brand, pageable);
		}
		return list;
	}

	@Override
	public GenericResponse getAllActive() {
		List<AssetBrandTypeEntity> assetBrandTypeList = assetBrandTypeRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(assetBrandTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetBrandTypeResponseDTO> assetBrandResponseList = assetBrandTypeList.stream()
				.map(assetBrandTypeMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
}
