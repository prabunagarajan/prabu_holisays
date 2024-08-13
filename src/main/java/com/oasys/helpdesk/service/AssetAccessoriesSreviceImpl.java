package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.ASSET_ACCESSORIES;
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
import com.oasys.helpdesk.dto.AssetAccessoriesRequestDTO;
import com.oasys.helpdesk.dto.AssetAccessoriesResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.mapper.AssetAccessoriesmapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetAccessoriesRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

@Service
public class AssetAccessoriesSreviceImpl implements AssetAccessoriesService{
	
	@Autowired
	private AssetAccessoriesRepository accessoriesRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	
	@Autowired
	private AssetAccessoriesmapper assetAccessoriesmapper;
	
	
	@Override
	public  GenericResponse adddAssetAccessories(AssetAccessoriesRequestDTO requestDTO) {
	
	Optional<AssetAccessoriesEntity> accessoriesOptional=accessoriesRepository.findById(requestDTO.getId());

	if (accessoriesOptional.isPresent()) {
		return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
				ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "accessoriesId" }));
	}
	accessoriesOptional = accessoriesRepository.findByAccessoriesCodeIgnoreCase(requestDTO.getAccessoriesCode());
	if (accessoriesOptional.isPresent()) {
		return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
				ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
	}
	requestDTO.setId(null);
	AssetAccessoriesEntity aAEntity = commonUtil.modalMap(requestDTO, AssetAccessoriesEntity.class);
	accessoriesRepository.save(aAEntity);
	return Library.getSuccessfulResponse(aAEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
}
	
	
	@Override
	public GenericResponse updateAssetAccessories(AssetAccessoriesRequestDTO requestDTO)	{
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<AssetAccessoriesEntity>  Optional = accessoriesRepository
				.findByAccessoriesNameAndId(requestDTO.getAccessoriesCode(), requestDTO.getId());
		
		if (Optional.isPresent()) {
			AssetAccessoriesEntity accessoriesEntity = Optional.get();
			accessoriesEntity.setAccessoriesName(requestDTO.getAccessoriesName());
			accessoriesEntity.setStatus(requestDTO.getStatus());
			accessoriesRepository.save(accessoriesEntity);
			return Library.getSuccessfulResponse(accessoriesEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
		}
		
		Optional = accessoriesRepository.findById(requestDTO.getId());
		if (!Optional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		
		AssetAccessoriesEntity accessoriesEntity = Optional.get();
		accessoriesEntity.setAccessoriesName(requestDTO.getAccessoriesName());
		accessoriesEntity.setStatus(requestDTO.getStatus());
		accessoriesRepository.save(accessoriesEntity);
		return Library.getSuccessfulResponse(accessoriesEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}
	
	
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<AssetAccessoriesEntity> list = null;
		String assetAccessories = null;
		String status = null;
		
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
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
			if (Objects.nonNull(paginationDto.getFilters().get("assetAccessories"))
					&& !paginationDto.getFilters().get("assetAccessories").toString().trim().isEmpty()) {
				assetAccessories = (paginationDto.getFilters().get("assetAccessories").toString());
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status =(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(assetAccessories,status, pageable);
		if (Objects.isNull(list)) {
			list = accessoriesRepository.getAllSubString(assetAccessories,pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<AssetAccessoriesResponseDTO> finalResponse = list.map(assetAccessoriesmapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	private Page<AssetAccessoriesEntity> getByFilter(String assetAccessories ,String status, Pageable pageable){
		Page<AssetAccessoriesEntity> list = null;
		if (Objects.nonNull(assetAccessories)  && Objects.nonNull(status)) {
			list = accessoriesRepository.getByAccessoriesNameAndStatus(assetAccessories, status, pageable);
		} 
		
		return list;
	}
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(ASSET_ACCESSORIES);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<AssetAccessoriesEntity> Entity = accessoriesRepository.findByAccessoriesCodeIgnoreCase(code);
			if (Entity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getAllActive() {
		List<AssetAccessoriesEntity> configList = accessoriesRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(configList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetAccessoriesResponseDTO> shiftResponseList = configList.stream()
				.map(assetAccessoriesmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAll() {
		List<AssetAccessoriesEntity> assetList = accessoriesRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<AssetAccessoriesResponseDTO> ResponseList = assetList.stream()
				.map(assetAccessoriesmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<AssetAccessoriesEntity> assetEntity = accessoriesRepository.findById(id);
		if (!assetEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(assetAccessoriesmapper.convertEntityToResponseDTO(assetEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	
}
