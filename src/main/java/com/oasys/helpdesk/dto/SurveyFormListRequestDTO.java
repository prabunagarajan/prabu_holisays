package com.oasys.helpdesk.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SurveyFormListRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;


//	private String formId;
//	
//	private String userName;
//
//	private String email;
//
//	private String licenceId;

	List<ServeyFormDataRequestDTO> serveyData = new ArrayList<>();

	


}
