package com.oasys.helpdesk.security;

import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtils {
	public static AuthenticationDTO findAuthenticationObject() {
		if(SecurityContextHolder.getContext()!=null && SecurityContextHolder.getContext().getAuthentication()!=null) {
			AuthenticationDTO authenticationDTO = (AuthenticationDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return authenticationDTO;
		}
		return null;
	}
}
