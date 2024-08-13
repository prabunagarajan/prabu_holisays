package com.oasys.helpdesk.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oasys.helpdesk.request.UserLoginRequestDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface LoginService {
	GenericResponse login(UserLoginRequestDto userLoginRequestDto, HttpSession httpSession,
			HttpServletRequest httpServletRequest);
	AuthenticationDTO authenticateCustomerToken(String customerToken);
}
