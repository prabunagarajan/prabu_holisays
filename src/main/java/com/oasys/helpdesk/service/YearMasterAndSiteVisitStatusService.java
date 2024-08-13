package com.oasys.helpdesk.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.YearMasterDTO;
import com.oasys.helpdesk.entity.SiteVisitStatusEntity;
import com.oasys.helpdesk.entity.YearMasterEntity;
import com.oasys.helpdesk.utility.GenericResponse;

@Service
public interface YearMasterAndSiteVisitStatusService  {

	GenericResponse createYearMaster(YearMasterEntity yearMasterEntity);

	//Object getByIdYearMaster(Long id);

	GenericResponse createSiteVisitStatus(@Valid SiteVisitStatusEntity siteVisitStatusEntity);


	Object getAllActiveSiteVisitStatus();

	GenericResponse getAllActiveYearMaster();

	//Object getByIdSiteVisitStatus(Long id);


	

}
