package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.SURVEY_FORM;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.ServeyFormDataRequestDTO;
import com.oasys.helpdesk.dto.SurveyFormListRequestDTO;
import com.oasys.helpdesk.entity.SurveyFormListEntity;
import com.oasys.helpdesk.entity.SurveyQuestionMaster;
import com.oasys.helpdesk.repository.SurveyFormListRepository;
import com.oasys.helpdesk.repository.SurveyQuestionRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.posasset.entity.DeviceReturnLogEntity;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SurveyFormServiceImpl implements SurveyFormService{



	@Autowired
	private SurveyFormListRepository surveyFormListRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private SurveyQuestionRepository surveyquestionrepository;


//	public GenericResponse addSurveyForm(SurveyFormListRequestDTO payload) {
//		SurveyFormListEntity surveyFormEntity = commonUtil.modalMap(payload, SurveyFormListEntity.class);
//		MenuPrefix prefix = MenuPrefix.getType(SURVEY_FORM);
//		String fromId = "form-"+prefix.toString() + RandomUtil.getRandomNumber();
//
//		for (int i = 0; i< payload.getServeyData().size(); i++) {
//			ServeyFormDataRequestDTO current = payload.getServeyData().get(i);
//			SurveyFormListEntity entity = new SurveyFormListEntity();
//			entity.setFormId(fromId );
//			entity.setQuestionId(current.getQuestionId());
//			entity.setRating(current.getRating());
//			entity.setUserName("test");
//			entity.setEmail("");
//			entity.setLicenceId("");
//			surveyFormListRepository.save(entity);
//		}
//
//		return Library.getSuccessfulResponse(surveyFormEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}
	
	public GenericResponse getAllByQuestions() {
		List<SurveyQuestionMaster> questionlist =surveyquestionrepository.findByIsActiveOrderByModifiedDateDesc(true);
		if(questionlist.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);			
		} else {
			return Library.getSuccessfulResponse(questionlist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	
	
	
	public GenericResponse addSurveyFormprocess(List<ServeyFormDataRequestDTO> payload) {
    List<SurveyFormListEntity> surveylist=new ArrayList<SurveyFormListEntity>();
	try {	
	MenuPrefix prefix = MenuPrefix.getType(SURVEY_FORM);
	String fromId = "form-"+prefix.toString() + RandomUtil.getRandomNumber();
  	payload.stream().forEach(formdata->{
  	SurveyFormListEntity listentity=new SurveyFormListEntity();
	listentity.setEmail(formdata.getEmail());	
	listentity.setFormId(fromId);
	listentity.setLicenceId(formdata.getLicenceId());
	listentity.setQuestionId(formdata.getQuestionId());	
	listentity.setQuestionName(formdata.getQuestionName());	
	listentity.setRating(formdata.getRating());
	listentity.setTicketNo(formdata.getTicketNo());
	listentity.setUserName(formdata.getUserName());
	surveyFormListRepository.save(listentity);
	surveylist.add(listentity);
	});
	}
	catch(Exception e) {
	log.info("Submit survey form :::" + e);	
	}
	return Library.getSuccessfulResponse(surveylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_CREATED);

}

	
	
	public GenericResponse getByTicketNo(String ticketNo) {
		List<SurveyFormListEntity> surveyformEntity =surveyFormListRepository.findByTicketNoOrderByIdDesc(ticketNo);
		if(surveyformEntity.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), ErrorMessages.NO_RECORD_FOUND);			
		} else {
			return Library.getSuccessfulResponse(surveyformEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}
	}
	
	
}


