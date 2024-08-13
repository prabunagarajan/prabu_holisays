package com.oasys.posasset.dto;

import java.io.Serializable;

import com.oasys.helpdesk.dto.holderdto;

import lombok.Data;
@Data
public class EALStockwastageDTO implements Serializable {
	
	private String dataCode;
	private String entityCode;
	private placeholderDTO placeholderKeyValueMap;
	public void setPlaceholderKeyValueMap(String fromDate, String toDate, String codeTypeValue, String licenseNumber) {
	}
}
