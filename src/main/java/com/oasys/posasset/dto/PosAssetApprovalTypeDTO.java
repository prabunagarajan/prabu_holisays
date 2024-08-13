package com.oasys.posasset.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.oasys.helpdesk.utility.ApprovalType;

import lombok.Data;

@Data
public class PosAssetApprovalTypeDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "103")
	private ApprovalType approvalType;
	
	@NotNull(message = "103")
	private Boolean status;
}
