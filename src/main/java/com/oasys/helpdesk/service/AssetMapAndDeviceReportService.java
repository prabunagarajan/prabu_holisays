package com.oasys.helpdesk.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetMapAndDeviceReportDTO;
import com.oasys.helpdesk.dto.AssetReportRequestDTO;
import com.oasys.helpdesk.repository.AssetMapRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class AssetMapAndDeviceReportService {
	
	@Autowired
	AssetMapRepository assetMapRepository;
	
	public GenericResponse report(AssetReportRequestDTO requestDTO) {
		try {
			String entityName=requestDTO.getEntity();
			String unitName=requestDTO.getUnitName();
			String unitCode=requestDTO.getUnitCode();
			String district=requestDTO.getDistrict();	
			Date fromDate=requestDTO.getFromDate();
			Date toDate=requestDTO.getToDate();
			String status=requestDTO.getStatus();
			
			List<AssetMapAndDeviceReportDTO> report = assetMapRepository.report(entityName,unitName,
					unitCode,district,status,fromDate,toDate);
			if (report.isEmpty()) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

			else {
				return Library.getSuccessfulResponse(report, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			}

		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving Asset Map And Device Report", e);
		}
	}

}
