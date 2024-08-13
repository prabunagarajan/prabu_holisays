package com.oasys.helpdesk.controller;

import org.springframework.security.core.context.SecurityContextHolder;

import com.oasys.helpdesk.security.AuthenticationDTO;

/**
 * This controller will be used as a base controller for all the controller to get the required value from the authentication 
 * @author User
 *
 */
public class BaseController {

	/**
	 * This method will be used to get the authentication DTO from the security context
	 * This we are doing to avoid the issue in wso2 deployment for post calls
	 * @return
	 */
	public AuthenticationDTO findAuthenticationObject() {
		if(SecurityContextHolder.getContext()!=null && SecurityContextHolder.getContext().getAuthentication()!=null) {
			AuthenticationDTO authenticationDTO = (AuthenticationDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return authenticationDTO;
		}
		return null;
	}
}
