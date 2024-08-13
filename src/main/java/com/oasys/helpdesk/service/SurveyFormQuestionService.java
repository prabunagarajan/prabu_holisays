package com.oasys.helpdesk.service;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.SurveyQuestionMasterRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;


public interface SurveyFormQuestionService {
	
	public GenericResponse updateServeyQuestionForm(SurveyQuestionMasterRequestDTO surveyquestionmasterrequestdto);
	
}
