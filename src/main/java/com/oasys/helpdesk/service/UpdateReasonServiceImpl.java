package com.oasys.helpdesk.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.KeyValueResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.UpdateReason;

@Service
public class UpdateReasonServiceImpl implements UpdateReasonService{
	@Override
	public GenericResponse getAll() {
		List<KeyValueResponseDTO> updateReasonList = new ArrayList<>();
		for (UpdateReason reason : UpdateReason.values()) {
			KeyValueResponseDTO response = new KeyValueResponseDTO();
			response.setKey(reason);
			response.setValue(reason.getType());
			updateReasonList.add(response);
		}
		return Library.getSuccessfulResponse(updateReasonList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);	
		}
}
