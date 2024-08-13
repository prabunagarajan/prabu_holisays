package com.oasys.helpdesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetStatusResponseDTO;
import com.oasys.helpdesk.dto.ZabbixMasterServerDTO;
import com.oasys.helpdesk.entity.ZabbixMasterServerEntity;
import com.oasys.helpdesk.repository.ZabbixMasterServerRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class ZabbixMasterServerServiceImpl implements ZabbixMasterServerService {

	@Autowired
	private ZabbixMasterServerRepository zabbixmasterserverrepository;

	@Override
	public GenericResponse addZabbixMasterServer(ZabbixMasterServerDTO zabbixmasterservicedto) {

		Optional<ZabbixMasterServerEntity> findByServiceIdEntity = zabbixmasterserverrepository
				.findByServiceId(zabbixmasterservicedto.getServiceId());

		if (findByServiceIdEntity.isPresent()) {

			return Library.getFailedfulResponse(findByServiceIdEntity, ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "SERVICE ID" }));
		}

		ZabbixMasterServerEntity zabbixMasterEntityObj = new ZabbixMasterServerEntity();
		zabbixMasterEntityObj.setServerName(zabbixmasterservicedto.getServerName());
		zabbixMasterEntityObj.setServiceId(zabbixmasterservicedto.getServiceId());
		zabbixMasterEntityObj.setStatus(zabbixmasterservicedto.getStatus());
		zabbixmasterserverrepository.save(zabbixMasterEntityObj);
		return Library.getSuccessfulResponse(zabbixMasterEntityObj, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);

	}

	@Override
	public GenericResponse getAll() {

		List<ZabbixMasterServerEntity> findAllByOrderByModiArrayList = zabbixmasterserverrepository
				.findAllByOrderByModifiedDateDesc();

		if (CollectionUtils.isEmpty(findAllByOrderByModiArrayList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(findAllByOrderByModiArrayList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

}
