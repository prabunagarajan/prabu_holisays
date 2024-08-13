package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.BRAND;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
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
import com.oasys.helpdesk.dto.AssetBrandRequestDTO;
import com.oasys.helpdesk.dto.AssetBrandResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.mapper.AssetBrandMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetBrandRepository;
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
public class AssetBrandServiceImpl implements AssetBrandService{
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private AssetBrandRepository assetBrandRepository;
	
	@Autowired
	private AssetBrandMapper assetBrandMapper;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String ASSET_BRAND= "Asset Brand";
	
	@Autowired
	private AssetBrandTypeRepository assetBrandTypeRepository;
	
	@Autowired
	private AssetTypeRepository assetTypeRepository;

	@Override
	public GenericResponse addAssetBrand(AssetBrandRequestDTO requestDTO)	{
		Optional<AssetBrandEntity> assetBrandOptional = assetBrandRepository
				.findByBrandIgnoreCase(requestDTO.getBrand());
		if (assetBrandOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { BRAND }));
		}
		
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByTypeIgnoreCase(requestDTO.getAssetType());
		if(!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "type" }));
		}
		
		requestDTO.setId(null);
		AssetBrandEntity assetBrandEntity = commonUtil.modalMap(requestDTO, AssetBrandEntity.class);
		assetBrandEntity.setType(assetTypeEntity.get());
		assetBrandRepository.save(assetBrandEntity);
		return Library.getSuccessfulResponse(assetBrandEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	} 
	
	@Override
	public GenericResponse updateAssetBrand(AssetBrandRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<AssetBrandEntity> assetTypeOptional = assetBrandRepository
				.findByBrandIgnoreCaseNotInId(requestDTO.getBrand().toUpperCase(), requestDTO.getId());
		if (assetTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { BRAND }));
		}
		 assetTypeOptional = assetBrandRepository.findById(requestDTO.getId());
		if (!assetTypeOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		AssetBrandEntity assetBrandEntity = assetTypeOptional.get();
		
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findByTypeIgnoreCase(requestDTO.getAssetType());
		if(!assetTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "type" }));
		}
		assetBrandEntity.setType(assetTypeEntity.get());
		assetBrandEntity.setBrand(requestDTO.getBrand());
		assetBrandEntity.setStatus(requestDTO.getStatus());
		assetBrandEntity.setCreatedBy(requestDTO.getCreatedBy());
		assetBrandRepository.save(assetBrandEntity);
		return Library.getSuccessfulResponse(assetBrandEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<AssetBrandEntity> assetBrandEntity = assetBrandRepository.findById(id);
		if (!assetBrandEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(assetBrandMapper.convertEntityToResponseDTO(assetBrandEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAll() {
		List<AssetBrandEntity> assetBrandList = assetBrandRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetBrandList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetBrandResponseDTO> assetBrandResponseList = assetBrandList.stream()
				.map(assetBrandMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<AssetBrandEntity> list = null;
		Long id = null;
		Boolean status = null;
		Long assettypeId=null;
		Long createdBy=null;
		String brand=null;
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
			
			
			if (Objects.nonNull(paginationDto.getFilters().get("type"))
					&& !paginationDto.getFilters().get("type").toString().trim().isEmpty()) {
				try {
					assettypeId = Long.valueOf(paginationDto.getFilters().get("type").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing assettype :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get("brand"))
					&& !paginationDto.getFilters().get("brand").toString().trim().isEmpty()) {
				try {
					
				Long brandid=Long.parseLong((String) paginationDto.getFilters().get("brand"));	
					
				Optional<AssetBrandEntity>	assetbarand=assetBrandRepository.findById(brandid); 
					
				brand = String.valueOf(assetbarand.get().getBrand());
				} catch (Exception e) {
					log.error("error occurred while parsing brand :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			
			
			if (Objects.nonNull(paginationDto.getFilters().get("createdBy"))
					&& !paginationDto.getFilters().get("createdBy").toString().trim().isEmpty()) {
				try {
					createdBy = Long.valueOf(paginationDto.getFilters().get("createdBy").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing createdBy :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			
			
			
			
		}
		
		
//		if (Objects.nonNull(id) && Objects.nonNull(status)) {
//			list = assetBrandRepository.getByIdAndStatus(id, status, pageable);
//		} else if (Objects.nonNull(id) && Objects.isNull(status)) {
//			list = assetBrandRepository.getById(id, pageable);
//		} else if (Objects.isNull(id) && Objects.nonNull(status)) {
//			list = assetBrandRepository.getByStatus(status, pageable);
//		}
		
		if (Objects.nonNull(assettypeId) && Objects.nonNull(status) && Objects.nonNull(brand)) {
			list = assetBrandRepository.getByIdAndStatusAndType(brand, status,assettypeId,pageable);
		}
		
		else if(Objects.isNull(assettypeId) && Objects.nonNull(status) && Objects.nonNull(brand)) {
			list = assetBrandRepository.getByStatusAndBrand(status,brand,pageable);
		}
		
		else if(Objects.nonNull(assettypeId) && Objects.nonNull(status) && Objects.isNull(brand)) {
			list = assetBrandRepository.getByStatusAndType(status,assettypeId,pageable);
		}
		else if(Objects.isNull(assettypeId) && Objects.nonNull(status) && Objects.isNull(brand)) {
			list = assetBrandRepository.getByStatus(status,pageable);
		}
		else if(Objects.isNull(assettypeId) && Objects.isNull(status) && Objects.isNull(brand) && Objects.nonNull(createdBy) ) {
			list = assetBrandRepository.getByCreatedBy(createdBy,pageable);
		}
		
		else if(Objects.isNull(assettypeId) && Objects.isNull(status) && Objects.nonNull(brand) && Objects.isNull(createdBy) ) {
			list = assetBrandRepository.getByBrand(brand,pageable);
		}
		else if(Objects.nonNull(assettypeId) && Objects.isNull(status) && Objects.isNull(brand)) {
			list = assetBrandRepository.getByType(assettypeId,pageable);
		}
		
		
		if (Objects.isNull(list)) {
			list = assetBrandRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<AssetBrandResponseDTO> finalResponse = list.map(assetBrandMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
//	@Override
//	public GenericResponse getCode() {
//		MenuPrefix prefix = MenuPrefix.getType(ASSET_BRAND);
//		String code = prefix.toString() + RandomUtil.getRandomNumber();
//		while (true) {
//			Optional<AssetBrandEntity> assetTypeEntity = assetBrandRepository.findByCodeIgnoreCase(code);
//			if (assetTypeEntity.isPresent()) {
//				code = prefix.toString() + RandomUtil.getRandomNumber();
//			} else {
//				break;
//			}
//		}
//		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//	}
	
	@Override
	public GenericResponse getAllActive(Long assetTypeId) {
		List<AssetBrandEntity> assetBrandList = null;
		if(Objects.nonNull(assetTypeId)) {
			assetBrandList = assetBrandTypeRepository.getAssetBrandByAssetTypeId(assetTypeId, Boolean.TRUE);
		}else {
		assetBrandList = assetBrandRepository.findByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		}
		if (CollectionUtils.isEmpty(assetBrandList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetBrandResponseDTO> assetBrandResponseList = assetBrandList.stream()
				.map(assetBrandMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
