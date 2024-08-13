package com.oasys.helpdesk.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.KeyValueResponseDTO;
import com.oasys.helpdesk.utility.EmploymentStatus;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class EmploymentStatusServiceImpl implements EmploymentStatusService {
	@Override
	public GenericResponse getAll() {
		List<KeyValueResponseDTO> employemntStatusList = new ArrayList<>();
		for (EmploymentStatus status : EmploymentStatus.values()) {
			KeyValueResponseDTO response = new KeyValueResponseDTO();
			response.setKey(status);
			response.setValue(status.getType());
			employemntStatusList.add(response);
		}
		return Library.getSuccessfulResponse(employemntStatusList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
