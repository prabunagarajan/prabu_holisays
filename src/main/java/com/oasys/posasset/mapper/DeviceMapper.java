package com.oasys.posasset.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.repository.AssetAccessoriesRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.repository.DeviceRepository;
import com.oasys.posasset.request.DeviceRequestDTO;
import com.oasys.posasset.response.DeviceResponseDTO;

@Component
public class DeviceMapper {

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	AssetAccessoriesRepository assetaccessoriesrepository;

	public DeviceResponseDTO convertEntityToResponseDTO(DeviceEntity entity) {
		// DeviceResponseDTO responseDTOO = commonUtil.modalMap(entity,
		// DeviceResponseDTO.class);
		DeviceResponseDTO responseDTO = new DeviceResponseDTO();
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		responseDTO.setId(entity.getId());
		if (Objects.nonNull(entity.getCreatedDate())) {
			responseDTO.setCreatedBy(createdByUserName);
		}

		if (Objects.nonNull(entity.getModifiedBy())) {
			responseDTO.setModifiedBy(modifiedByUserName);
		}
		responseDTO.setActive(entity.isActive());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		// AssetAccessoriesEntity gh=entity.getPrinterId();

		if (Objects.nonNull(entity.getPrinterId())) {
			responseDTO.setPrinterName(entity.getPrinterId().getAccessoriesName());
			responseDTO.setPrinterId(entity.getPrinterId().getId());

		}

		responseDTO.setActive(entity.isActive());
		responseDTO.setAssociated(entity.isAssociated());
		responseDTO.setDeviceNumber(entity.getDeviceNumber());
		responseDTO.setLastSyncOn(entity.getLastSyncOn());

		responseDTO.setMake(entity.getMake());
		responseDTO.setModel(entity.getModel());
		// responseDTO.setProjectName(entity.getProjectName());
		responseDTO.setSerialNumber(entity.getSerialNumber());
		responseDTO.setSimId(entity.getSimId());
		responseDTO.setSimId2(entity.getSimId2());
		responseDTO.setMacId(entity.getMacId());
		responseDTO.setFpsCode(entity.getFpsCode());
		responseDTO.setFpsName(entity.getFpsName());
		responseDTO.setAssetType(entity.getAssetType());
		responseDTO.setAssetTypeId(entity.getAssetTypeId());
		responseDTO.setAssetSubtypeId(entity.getAssetSubtypeId());;
		responseDTO.setAssetNameId(entity.getAssetNameId());
		responseDTO.setAssetBrandId(entity.getAssetBrandId());
		responseDTO.setWarrantyPeriod(entity.getWarrantyPeriod());
		responseDTO.setRating(entity.getRating());
		responseDTO.setSupplierNameId(entity.getSupplierNameId());
		return responseDTO;

	}

	public DeviceEntity updateRequestDTOToEntity(DeviceRequestDTO requestDTO) {

		Optional<DeviceEntity> deviceEntity = deviceRepository.findById(requestDTO.getId());
		if (!deviceEntity.isPresent()) {
			throw new InvalidDataValidation(ResponseMessageConstant.DEVICE_DETAILS_MISSING.getMessage());
		}

		DeviceEntity entity = deviceEntity.get();

		if (Objects.nonNull(requestDTO.getMacId())) {
			entity.setMacId(requestDTO.getMacId());
		}

		if (Objects.nonNull(requestDTO.getLastSyncOn())) {
			entity.setLastSyncOn(requestDTO.getLastSyncOn());
		}

		if (Objects.nonNull(requestDTO.getLastSyncOn())) {
			entity.setLastSyncOn(requestDTO.getLastSyncOn());
		}

		if (Objects.nonNull(requestDTO.getMake())) {
			entity.setMake(requestDTO.getMake());
		}

		if (StringUtils.isNotBlank(requestDTO.getModel())) {
			entity.setModel(requestDTO.getModel());
		}

		if (Objects.nonNull(requestDTO.getPrinterId())) {

			AssetAccessoriesEntity asset = assetaccessoriesrepository.getById(requestDTO.getPrinterId());

			if (asset == null) {
				throw new InvalidDataValidation("PRINTERID_INVALID");
			} else {
				entity.setPrinterId(asset);
			}
		}

		if (StringUtils.isNotBlank(requestDTO.getSerialNumber())) {
			entity.setSerialNumber(requestDTO.getSerialNumber());
		}

		if (StringUtils.isNotBlank(requestDTO.getSimId())) {
			entity.setSimId(requestDTO.getSimId());
		}

		if (StringUtils.isNotBlank(requestDTO.getSimId2())) {
			entity.setSimId2(requestDTO.getSimId2());
		}

		if (StringUtils.isNotBlank(requestDTO.getDeviceNumber())) {
			entity.setDeviceNumber(requestDTO.getDeviceNumber());
		}
		entity.setActive(requestDTO.isActive());
		return entity;
	}

}
