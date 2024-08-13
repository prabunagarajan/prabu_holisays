package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TYPE;

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
import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.mapper.AssetTypeMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetBrandTypeRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AssetTypeServiceImpl implements AssetTypeService{
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private AssetTypeRepository assetTypeRepository;
	
	@Autowired
	private AssetTypeMapper assetTypeMapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String ASSET_TYPE = "Asset Type";
	
	@Autowired
	private AssetBrandTypeRepository assetBrandTypeRepository;

	@Override
	public GenericResponse addAssetType(AssetTypeRequestDTO requestDTO)	{
		Optional<AssetTypeEntity> assetTypeOptional = assetTypeRepository
				.findByTypeIgnoreCase(requestDTO.getType());
		if (assetTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TYPE }));
		}
		assetTypeOptional = assetTypeRepository
				.findByCodeIgnoreCase(requestDTO.getCode().toUpperCase());
		if (assetTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		AssetTypeEntity assetTypeEntity = commonUtil.modalMap(requestDTO, AssetTypeEntity.class);
		assetTypeRepository.save(assetTypeEntity);
		return Library.getSuccessfulResponse(assetTypeEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	} 
	
	@Override
	public GenericResponse updateAssetType(AssetTypeRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<AssetTypeEntity> assetTypeOptional = assetTypeRepository
				.findByTypeIgnoreCaseNotInId(requestDTO.getType().toUpperCase(), requestDTO.getId());
		if (assetTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TYPE }));
		}
		 assetTypeOptional = assetTypeRepository.findById(requestDTO.getId());
		if (!assetTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		AssetTypeEntity assetTypeEntity = assetTypeOptional.get();
		assetTypeEntity.setType(requestDTO.getType());
		assetTypeEntity.setStatus(requestDTO.getStatus());
		assetTypeRepository.save(assetTypeEntity);
		return Library.getSuccessfulResponse(assetTypeEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(id);
		if (!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(assetTypeMapper.convertEntityToResponseDTO(assetTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAll() {
		List<AssetTypeEntity> assetTypeList = assetTypeRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetTypeResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(assetTypeMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActive(Long assetBrandId) {
		List<AssetTypeEntity> assetTypeList = null;
		if(Objects.nonNull(assetBrandId)) {
			assetTypeList = assetBrandTypeRepository.getAssetTypeByAssetBrandId(assetBrandId, Boolean.TRUE);
		}else {
			assetTypeList = assetTypeRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		}
		
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetTypeResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(assetTypeMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActiveQ() {
		List<AssetTypeEntity> assetTypeList = null;
	assetTypeList = assetTypeRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetTypeResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(assetTypeMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<AssetTypeEntity> list = null;
		Long id = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get(ID))
					&& !paginationDto.getFilters().get(ID).toString().trim().isEmpty()) {
				try {
					id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing id :: {}", e);
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
		if (Objects.nonNull(id) && Objects.nonNull(status)) {
			list = assetTypeRepository.getByIdAndStatus(id, status, pageable);
		} else if (Objects.nonNull(id) && Objects.isNull(status)) {
			list = assetTypeRepository.getById(id, pageable);
		} else if (Objects.isNull(id) && Objects.nonNull(status)) {
			list = assetTypeRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = assetTypeRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<AssetTypeResponseDTO> finalResponse = list.map(assetTypeMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(ASSET_TYPE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByCodeIgnoreCase(code);
			if (assetTypeEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
