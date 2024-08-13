package com.oasys.helpdesk.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.SurveyFormQuestionResponseDTO;
import com.oasys.helpdesk.dto.SurveyQuestionMasterRequestDTO;
import com.oasys.helpdesk.entity.SurveyQuestionMaster;
import com.oasys.helpdesk.repository.SurveyQuestionRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
@Service
public class SurveyFormQuestionServiceIMPL implements SurveyFormQuestionService {

	@Autowired
	private SurveyQuestionRepository surveryquestionrepository;

	@Override
	public GenericResponse updateServeyQuestionForm(SurveyQuestionMasterRequestDTO surveyquestionmasterrequestdto) {

		if (Objects.isNull(surveyquestionmasterrequestdto.getId())) {

			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));

		}

		Optional<SurveyQuestionMaster> surveryQustionMasterOptional = surveryquestionrepository
				.findById(surveyquestionmasterrequestdto.getId());

		SurveyQuestionMaster surveyQuestionMaster = null;

		if (surveryQustionMasterOptional.isPresent()) {

			surveyQuestionMaster = surveryQustionMasterOptional.get();
			surveyQuestionMaster.setQuestion(surveyquestionmasterrequestdto.getQuestion());
			surveryquestionrepository.save(surveyQuestionMaster);
		}

		else {
			throw new InvalidDataValidation("Invalid ID");
		}
		return Library.getSuccessfulResponse(surveyQuestionMaster, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);

	}

}
