package com.oasys.posasset.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.entity.POSAssetApprovalType;
import com.oasys.helpdesk.repository.POSAssetApprovalTypeRepository;
import com.oasys.helpdesk.utility.ApprovalType;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.posasset.dto.PosAssetApprovalTypeDTO;
import com.oasys.posasset.service.POSAssetApprovalTypeService;

@Service
public class POSAssetApprovalTypeServiceImpl implements POSAssetApprovalTypeService {

	@Autowired
	private POSAssetApprovalTypeRepository posAssetApprovalTypeRepository;

	@Override
	@Transactional
	public GenericResponse update(PosAssetApprovalTypeDTO requestDTO) {
		Optional<POSAssetApprovalType> entityOptional = posAssetApprovalTypeRepository
				.findByApprovalType(requestDTO.getApprovalType());
		if (!entityOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.APPROVAL_TYPE }));
		}
		POSAssetApprovalType entity = entityOptional.get();
		entity.setStatus(requestDTO.getStatus());
		Optional<POSAssetApprovalType> approvalTypeEntityOptional = posAssetApprovalTypeRepository
				.findRecordsNotInId(entity.getId());
		if (approvalTypeEntityOptional.isPresent()) {
			POSAssetApprovalType approvalTypeEntity = approvalTypeEntityOptional.get();
			if (Boolean.FALSE.equals(requestDTO.getStatus())) {
				approvalTypeEntity.setStatus(Boolean.TRUE);
			}
			if (Boolean.TRUE.equals(requestDTO.getStatus())) {
				approvalTypeEntity.setStatus(Boolean.FALSE);
			}
			posAssetApprovalTypeRepository.save(approvalTypeEntity);
		}

		return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getAll() {
		List<POSAssetApprovalType> posAssetApprovalTypeList = posAssetApprovalTypeRepository.findAll();
		if (CollectionUtils.isEmpty(posAssetApprovalTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(posAssetApprovalTypeList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
