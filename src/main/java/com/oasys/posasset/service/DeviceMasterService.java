package com.oasys.posasset.service;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.posasset.dto.DeviceMasterDTO;

@Service
public interface DeviceMasterService {

	GenericResponse addDeviceMasterDeviceNumber(DeviceMasterDTO deviceMasterDTO);

	GenericResponse updateDeviceNumber(DeviceMasterDTO deviceMasterDTO);
}
