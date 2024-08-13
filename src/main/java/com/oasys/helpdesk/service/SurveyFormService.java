package com.oasys.helpdesk.service;

import java.util.List;

import com.oasys.helpdesk.dto.ServeyFormDataRequestDTO;
import com.oasys.helpdesk.dto.SurveyFormListRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface SurveyFormService {

	
	
	GenericResponse addSurveyFormprocess(List<ServeyFormDataRequestDTO> payload);
	
	GenericResponse getAllByQuestions();
}
