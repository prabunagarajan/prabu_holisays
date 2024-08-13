package com.oasys.helpdesk.service;

import javax.validation.Valid;

import com.oasys.helpdesk.dto.ChangeRequestFeaturesDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface ChangeRequestFeaturesService {

	GenericResponse getAllMasterActive();

	GenericResponse addMasterChangeRequest(ChangeRequestFeaturesDTO changerequestfeaturesDTO);

	GenericResponse updateMasterchangerequest(ChangeRequestFeaturesDTO changerequestfeaturesDTO);

}
