package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.VersionUpgradeDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface VersionUpgradeService {
	GenericResponse addNewVersion(VersionUpgradeDTO versionUpgradeDto);

	GenericResponse updateVersion(Long id, VersionUpgradeDTO versionUpgradeDto);

	GenericResponse getAllVersion();

	GenericResponse latestVersion();

	
}
