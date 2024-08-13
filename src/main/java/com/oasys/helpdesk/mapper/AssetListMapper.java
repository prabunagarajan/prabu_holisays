package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetListResponseDto;
import com.oasys.helpdesk.entity.AssetListEntity;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class AssetListMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	public AssetListResponseDto convertEntityToResponseDTO(AssetListEntity entity) {
		AssetListResponseDto responseDTO = commonUtil.modalMap(entity,
				AssetListResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		  responseDTO.setId(entity.getId());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
			responseDTO.setCreatedByName(createdByUserName);
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
			responseDTO.setModifiedByName(modifiedByUserName);
		}
		if (Objects.nonNull(entity.getAssetsubTypeNmae())) {
			responseDTO.setAssetSubType(entity.getAssetsubTypeNmae().getAssetsubType());
			responseDTO.setAssetSubTypeId(entity.getAssetsubTypeNmae().getId());
		}
		
		if (Objects.nonNull(entity.getBrand())) {
			responseDTO.setAssetBrand(entity.getBrand().getBrand());
			responseDTO.setAssetBrandId(entity.getBrand().getId());
		}
		
		if (Objects.nonNull(entity.getType())) {
			responseDTO.setAssetType(entity.getType().getType());
			responseDTO.setAssetTypeId(entity.getType().getId());	
		}
		
		if (Objects.nonNull(entity.getDeviceName())) {
			responseDTO.setAssetName(entity.getDeviceName().getDeviceName());
			responseDTO.setAssetNameId((entity.getDeviceName().getId()));
		}
		if (Objects.nonNull(entity.getSupplierName())) {
			responseDTO.setSupplierName(entity.getSupplierName().getSupplierName());
			responseDTO.setSupplierId(entity.getSupplierName().getId());
		}
		
	   responseDTO.setDateOfPurchase(entity.getDateOfPurchase());
	   responseDTO.setWarrantyPeriod(entity.getWarrantyPeriod());	
	   responseDTO.setSerialNo(entity.getSerialNo());
	   responseDTO.setRating(entity.getRating());
	 
		return responseDTO;	
	}
	
	
	
	
	
}
