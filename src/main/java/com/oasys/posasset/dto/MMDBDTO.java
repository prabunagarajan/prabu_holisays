package com.oasys.posasset.dto;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.oasys.helpdesk.dto.WorkLocationResponseDTO;
import com.oasys.posasset.constant.ApprovalStatus;
import com.oasys.posasset.entity.EALRequestMapEntity;

import lombok.Data;

@Data
public class MMDBDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	
	
	private String device_number;	
	
	private String fps_code;	
	
	private Long associated;
	
	
	}
