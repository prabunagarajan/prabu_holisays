package com.oasys.helpdesk.service;

import com.oasys.helpdesk.utility.GenericResponse;

public interface MasterLicenseTypeService {

	GenericResponse getAll();
	GenericResponse getAllActive();

}
