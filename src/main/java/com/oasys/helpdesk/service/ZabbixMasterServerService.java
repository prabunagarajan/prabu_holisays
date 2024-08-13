package com.oasys.helpdesk.service;

import com.oasys.helpdesk.dto.ZabbixMasterServerDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface ZabbixMasterServerService {

	GenericResponse addZabbixMasterServer(ZabbixMasterServerDTO zabbixmasterservicedto);

	GenericResponse getAll();
}
