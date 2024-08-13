package com.oasys.helpdesk.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.oasys.helpdesk.dto.LicenseDetails;
import com.oasys.helpdesk.util.CustomAuthorityDeserializer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId;

	private String userName;

	private List<String> entityList;// entity type code and table is ALLOCATED_ENTITY
	
	private Map<String,List<LicenseDetails>> allocatedEntityName; // Department users who has assigned the actual entity
	
	private String email;
	
	private String designationCode;
	
	@JsonDeserialize(using = CustomAuthorityDeserializer.class)
	private Collection<? extends GrantedAuthority> authorities; 
	
	private List<String> roleCodes;
	
	private String token;
	
	private Boolean isCustomer = false;
	private String employeeId;
	@Override
	public String toString() {
		return "AuthenticationDTO [userId=" + userId + ", userName=" + userName + ", email=" + email
				+ ", designationCode=" + designationCode + ", roleCodes=" + roleCodes + ", isCustomer=" + isCustomer
				+ ", employeeId=" + employeeId + "]";
	}
	
}

