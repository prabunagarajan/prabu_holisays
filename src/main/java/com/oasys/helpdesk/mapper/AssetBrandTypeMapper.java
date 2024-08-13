package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.ASSET_BRAND_ID;
import static com.oasys.helpdesk.constant.Constant.ASSET_TYPE_ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetBrandTypeRequestDTO;
import com.oasys.helpdesk.dto.AssetBrandTypeResponseDTO;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetBrandTypeEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.repository.AssetBrandRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class AssetBrandTypeMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private AssetBrandRepository assetBrandRepository;
	
	@Autowired
	private AssetTypeRepository assetTypeRepository;

	public AssetBrandTypeResponseDTO convertEntityToResponseDTO(AssetBrandTypeEntity assetBrandTypeEntity) {
		AssetBrandTypeResponseDTO responseDTO = commonUtil.modalMap(assetBrandTypeEntity,
				AssetBrandTypeResponseDTO.class);
		String createdByUserName = commonDataController.getUserNameById(assetBrandTypeEntity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(assetBrandTypeEntity.getModifiedBy());

		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(assetBrandTypeEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(assetBrandTypeEntity.getCreatedDate()));
		}
		if (Objects.nonNull(assetBrandTypeEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(assetBrandTypeEntity.getModifiedDate()));
		}
		if (Objects.nonNull(assetBrandTypeEntity.getBrand())) {
			responseDTO.setAssetBrand(assetBrandTypeEntity.getBrand().getBrand());
			responseDTO.setAssetBrandId(assetBrandTypeEntity.getBrand().getId());
		}
		if (Objects.nonNull(assetBrandTypeEntity.getType())) {
			responseDTO.setAssetType(assetBrandTypeEntity.getType().getType());
			responseDTO.setAssetTypeId(assetBrandTypeEntity.getType().getId());
		}
		return responseDTO;
	}
	
	public AssetBrandTypeEntity convertRequestDTOToEntity(AssetBrandTypeRequestDTO requestDTO, AssetBrandTypeEntity entity) {
		if(Objects.isNull(entity)) {
			entity = commonUtil.modalMap(requestDTO, AssetBrandTypeEntity.class);
		}
		Optional<AssetBrandEntity> assetBrandEntity = assetBrandRepository.findById(requestDTO.getAssetBrandId());
		if (!assetBrandEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ASSET_BRAND_ID }));
		}

		entity.setBrand(assetBrandEntity.get());
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(requestDTO.getAssetTypeId());
		if (!assetTypeEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ASSET_TYPE_ID }));
		}
		entity.setType(assetTypeEntity.get());
		entity.setStatus(requestDTO.getStatus());
		return entity;
	}
	
}
