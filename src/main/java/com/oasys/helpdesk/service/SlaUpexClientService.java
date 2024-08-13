package com.oasys.helpdesk.service;

import org.json.JSONObject;

import com.oasys.helpdesk.dto.SLADashboardDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SlaUpexClientService {

	JSONObject UpexClient(SLADashboardDTO requestDTO);

	JSONObject upexciseApiPrimaryServer(SLADashboardDTO requestDTO);

	JSONObject upexciseApiSecondaryServer(SLADashboardDTO requestDTO);

	JSONObject upexciseUIServer(SLADashboardDTO requestDTO);

	JSONObject upexciseHaproxyServer(SLADashboardDTO requestDTO);

	JSONObject upexciseMasterDatabase(SLADashboardDTO requestDTO);

	JSONObject upexciseSlaveDatabase(SLADashboardDTO requestDTO);

	JSONObject archivedtabase(SLADashboardDTO requestDTO);

	JSONObject cms(SLADashboardDTO requestDTO);

	JSONObject jenkins(SLADashboardDTO requestDTO);

	JSONObject mdmserver(SLADashboardDTO requestDTO);

	JSONObject misAPI(SLADashboardDTO requestDTO);

	JSONObject qrcode(SLADashboardDTO requestDTO);

	JSONObject wso2(SLADashboardDTO requestDTO);
	
	GenericResponse UpexClientServerData(SLADashboardDTO requestDTO);
	
	GenericResponse upexciseApiPrimaryServerData(SLADashboardDTO requestDTO);
	
	GenericResponse upexciseApiSecondaryServerData(SLADashboardDTO requestDTO);
	
	GenericResponse upexciseUIServerData(SLADashboardDTO requestDTO);

	GenericResponse upexciseHaproxyServerData(SLADashboardDTO requestDTO);
	
	GenericResponse upexciseMasterServerData(SLADashboardDTO requestDTO);
	
	GenericResponse upexciseSlaveDatabaseData(SLADashboardDTO requestDTO);
	
	GenericResponse upexcisearchivedtabaseData(SLADashboardDTO requestDTO);
	
	GenericResponse upexcmstabaseData(SLADashboardDTO requestDTO);
	
	GenericResponse upexjenkinsData(SLADashboardDTO requestDTO);
	
	GenericResponse upexmdmserverData(SLADashboardDTO requestDTO);
	
	GenericResponse upexqrerverData(SLADashboardDTO requestDTO);
	
	GenericResponse wso2serverData(SLADashboardDTO requestDTO);
	
	GenericResponse misData(SLADashboardDTO requestDTO);
	
	
	GenericResponse AllServerData(SLADashboardDTO requestDTO);

	GenericResponse upexServerDetails(SLADashboardDTO requestDto);
	
	
	
	
	
	
}
