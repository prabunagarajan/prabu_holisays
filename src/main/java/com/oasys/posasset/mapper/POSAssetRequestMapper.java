package com.oasys.posasset.mapper;

import static com.oasys.helpdesk.constant.Constant.ASSET_BRAND_ID;
import static com.oasys.helpdesk.constant.Constant.ASSET_TYPE_ID;
import static com.oasys.helpdesk.constant.Constant.DEVICE_HARDWARE_ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;
import com.oasys.helpdesk.entity.POSAssetApprovalType;
import com.oasys.helpdesk.entity.POSAssetRequestDetailEntity;
import com.oasys.helpdesk.entity.POSAssetRequestEntity;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.AccessoriesNameRepository;
import com.oasys.helpdesk.repository.AssetBrandRepository;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DeviceHardwareRepository;
import com.oasys.helpdesk.repository.POSAssetApprovalTypeRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.ApprovalType;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.POSAssetRequestStatus;
import com.oasys.posasset.dto.POSAssetRequestDTO;
import com.oasys.posasset.dto.POSAssetRequestDetailDTO;
import com.oasys.posasset.dto.POSAssetResponseDTO;
import com.oasys.posasset.dto.POSAssetResponseDetailDTO;

@Component
public class POSAssetRequestMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private AssetBrandRepository assetBrandRepository;
	
	@Autowired
	private AssetTypeRepository assetTypeRepository;
	
	@Autowired
	private DeviceHardwareRepository deviceHardwareRepository;
	
	@Autowired
	private AccessoriesNameRepository accessoriesRepository;
	
	@Autowired
	private POSAssetApprovalTypeRepository posAssetApprovalTypeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	public POSAssetRequestEntity requestDTOToEntity(POSAssetRequestDTO requestDTO) {
		POSAssetRequestEntity entity = commonUtil.modalMap(requestDTO, POSAssetRequestEntity.class);
		Optional<AssetBrandEntity> assetBrandEntity = assetBrandRepository.findById(requestDTO.getAssetBrandId());
		if (!assetBrandEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ASSET_BRAND_ID }));
		}
		Optional<AssetTypeEntity> assetTypeEntity = assetTypeRepository.findById(requestDTO.getAssetTypeId());
		if (!assetTypeEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ASSET_TYPE_ID }));
		}
		Optional<DeviceHardwareEntity> deviceHardwareEntity = deviceHardwareRepository.findById(requestDTO.getDeviceHardwareId());
//		if (!deviceHardwareEntity.isPresent() || Objects.isNull(deviceHardwareEntity.get().getBrand())
//				|| Objects.isNull(deviceHardwareEntity.get().getType())) {
//			throw new InvalidDataValidation(
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { DEVICE_HARDWARE_ID }));
//		}
//		if(!deviceHardwareEntity.get().getBrand().getId().equals(assetBrandEntity.get().getId())) {
//			throw new InvalidDataValidation(
//					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ASSET_BRAND_ID }));
//		}
		if(!deviceHardwareEntity.get().getType().getId().equals(assetTypeEntity.get().getId())) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ASSET_TYPE_ID }));
		}
		entity.setAssetBrand(assetBrandEntity.get());
		entity.setAssetType(assetTypeEntity.get());
		entity.setDeviceHardware(deviceHardwareEntity.get());
		entity.setRequestDateTime(LocalDateTime.now());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE);
		entity.setApplicationDate(LocalDate.parse(requestDTO.getApplicationDate(), formatter));
		if (CollectionUtils.isEmpty(requestDTO.getAssetDetailsList())) {
			throw new InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
					.getMessage(new Object[] { Constant.ASSET_DETAIL_LIST }));
		}
		Optional<POSAssetApprovalType> approvalTypeOptional = posAssetApprovalTypeRepository.findByStatus(Boolean.TRUE);
		Boolean isAutoApproved = Boolean.FALSE ;
		if (approvalTypeOptional.isPresent()
				&& ApprovalType.AUTO.getType().equals(approvalTypeOptional.get().getApprovalType())) {
			
			RoleMaster roleMaster = roleMasterRepository.findByRoleCode(Constant.HELPDESK_ADMIN);
			if (Objects.isNull(roleMaster) || Objects.isNull(roleMaster.getId())) {
				throw new InvalidDataValidation(ErrorMessages.ROLE_NOT_CONFIGURED);
			}
			List<UserEntity> userEntity = userRepository.getUserByRoleId(roleMaster.getId());
			if (CollectionUtils.isEmpty(userEntity)) {
				throw new InvalidDataValidation(ErrorMessages.ADMIN_USER_NOT_FOUND);
			}
			entity.setApprovedBy(userEntity.get(0));
			entity.setApprovalDate(LocalDateTime.now());
			entity.setStatus(POSAssetRequestStatus.APPROVED);
			entity.setRemarks("Auto Approved");
			isAutoApproved = Boolean.TRUE;
		}else {
			entity.setStatus(POSAssetRequestStatus.PENDING);
		}
		List<POSAssetRequestDetailEntity> assetDetailsList = null;
		if(isAutoApproved) {
			assetDetailsList = requestDTO.getAssetDetailsList().stream()
					.map(a -> this.posAssetRequestDetailToEntity(a, null, Boolean.TRUE)).collect(Collectors.toList());
		}else {
			assetDetailsList = requestDTO.getAssetDetailsList().stream()
					.map(a -> this.posAssetRequestDetailToEntity(a, null, Boolean.FALSE)).collect(Collectors.toList());
		}
		assetDetailsList.forEach(details->{
			details.setPosAssetRequestEntity(entity);
		});
		entity.setAssetDetailsList(assetDetailsList);
		return entity;
	}
	
	public POSAssetRequestDetailEntity posAssetRequestDetailToEntity(POSAssetRequestDetailDTO requestDTO, Long posAssetRequestId, Boolean isAutoApproved) {
		POSAssetRequestDetailEntity entity = commonUtil.modalMap(requestDTO, POSAssetRequestDetailEntity.class);
		if (isAutoApproved) {
			entity.setApprovedAccessoriesCount(requestDTO.getNumberOfAccessories());
			entity.setApprovedDevicesCount(requestDTO.getNumberOfDevices());
		}
		if(Objects.isNull(requestDTO.getAccessoriesId())) {
			return entity;
		}
		Optional<Accessories> accessoriesEntity = accessoriesRepository.findById(requestDTO.getAccessoriesId());
		if (!accessoriesEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ACCESSORIES_ID }));
		}
		entity.setAccessories(accessoriesEntity.get());
		
		return entity;
	}
	
	public POSAssetResponseDTO entityToResponseDTO(POSAssetRequestEntity entity) {
		POSAssetResponseDTO responseDTO = commonUtil.modalMap(entity, POSAssetResponseDTO.class);
		responseDTO.setApplicationDate(Objects.nonNull(entity.getApplicationDate())?entity.getApplicationDate().toString():null);
		if(Objects.nonNull(entity.getStatus())) {
			responseDTO.setStatus(entity.getStatus().getType());
		}
		if(Objects.nonNull(entity.getAssetBrand())) {
			responseDTO.setAssetBrand(entity.getAssetBrand().getBrand());
			responseDTO.setAssetBrandId(entity.getAssetBrand().getId());
		}
		if(Objects.nonNull(entity.getAssetType())) {
			responseDTO.setAssetType(entity.getAssetType().getType());
			responseDTO.setAssetTypeId(entity.getAssetType().getId());
		}
		
		if(Objects.nonNull(entity.getDeviceHardware())) {
			responseDTO.setDeviceHardwareName(entity.getDeviceHardware().getDeviceName());
			responseDTO.setDeviceHardwareNameId(entity.getDeviceHardware().getId());
		}
		if(Objects.nonNull(entity.getApprovedBy())) {
			responseDTO.setApprovedById(entity.getApprovedBy().getId());
			responseDTO.setApprovedByUsername(commonDataController.getUserNameById(entity.getApprovedBy().getId()));
		}
		if(Objects.nonNull(entity.getAssignBy())) {
			responseDTO.setAssignById(entity.getAssignBy().getId());
			responseDTO.setAssignByUsername(commonDataController.getUserNameById(entity.getAssignBy().getId()));
		}
		responseDTO.setApprovalDate(Objects.nonNull(entity.getApprovalDate())?entity.getApprovalDate().toString():null);
		responseDTO.setRequestDateTime(Objects.nonNull(entity.getRequestDateTime())?entity.getRequestDateTime().toString():null);
		if(Objects.nonNull(entity.getAssetDetailsList()) && !CollectionUtils.isEmpty(entity.getAssetDetailsList())) {
			List<POSAssetResponseDetailDTO> assetDetailsList = entity.getAssetDetailsList().stream()
					.map(a -> this.entityToAssetDetailsResponseDTO(a)).collect(Collectors.toList());
			responseDTO.setAssetDetailsList(assetDetailsList);
		}
		return responseDTO;
	}

	public POSAssetResponseDetailDTO entityToAssetDetailsResponseDTO(POSAssetRequestDetailEntity entity) {
		POSAssetResponseDetailDTO responseDTO = commonUtil.modalMap(entity, POSAssetResponseDetailDTO.class);
		
		String createdByUserName=commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(entity.getModifiedBy());
		
		responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		if(Objects.nonNull(entity.getAccessories())) {
			responseDTO.setAccessoriesId(entity.getAccessories().getId());
			//responseDTO.setAccessoriesName(entity.getAccessories().getAccessoriesName());
		}
		
		return responseDTO;
	}
}
