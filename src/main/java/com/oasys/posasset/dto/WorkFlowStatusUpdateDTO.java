package com.oasys.posasset.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class WorkFlowStatusUpdateDTO implements Serializable {

	private Long id;

	private String workFlowRemarks;

	private String status;

	private String statusDesc;

	private String stage;

	private String stageDesc;

	private String applicationNumber;

	private String sentBackBy;

	private Boolean isDigitalSignature = false;

	private Boolean isApplicationApproved = false;
	
	
	private String deviceDamageapplnno;

}