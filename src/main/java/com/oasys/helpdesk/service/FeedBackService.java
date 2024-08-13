package com.oasys.helpdesk.service;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.FeedBackComplaintRequestDTO;
import com.oasys.helpdesk.dto.FeedBackEntityRequestDTO;
import com.oasys.helpdesk.entity.FeedBackComplaintEntity;
import com.oasys.helpdesk.entity.FeedBackEntity;
import com.oasys.helpdesk.repository.FeedBackComplaintRepository;
import com.oasys.helpdesk.repository.FeedBackRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class FeedBackService {
	
	@Autowired
	private FeedBackRepository feedBackRepository;

	@Autowired
	private FeedBackComplaintRepository feedBackcomplaintRepository;
		
		@Autowired
		private CommonUtil commonUtil;

		public GenericResponse addSurveyForm(@Valid FeedBackEntityRequestDTO requestDTO) {
			if(Objects.isNull(requestDTO.getEmail())) {
				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
						ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "emailID" }));
			}
			if(Objects.isNull(requestDTO.getName())) {
				return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
						ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "name" }));
			}
			
			FeedBackEntity feedBackEntity = commonUtil.modalMap(requestDTO, FeedBackEntity.class);
			feedBackRepository.save(feedBackEntity);
			return Library.getSuccessfulResponse(feedBackEntity,  ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
		
		

		public GenericResponse addfeedbackcomplint(FeedBackComplaintRequestDTO requestDto)	{
			requestDto.setId(null);
			FeedBackComplaintEntity tcEntity = commonUtil.modalMap(requestDto, FeedBackComplaintEntity.class);
			feedBackcomplaintRepository.save(tcEntity);
			return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
		}
		
	
	

}
