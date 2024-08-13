
package com.oasys.helpdesk.conf;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.security.UserPrincipal;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class UserAuditAware implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			return Optional.ofNullable(2L);
		}

		Object authObject = authentication.getPrincipal();
		if (authObject == null) {
			return Optional.ofNullable(2L);
		} else {
			if(authObject instanceof UserPrincipal) {
				UserPrincipal userPrincipal = (UserPrincipal) authObject;
				return Optional.ofNullable(userPrincipal.getId());
			}else {
				AuthenticationDTO authenticationDTO = (AuthenticationDTO) authObject;
				return Optional.ofNullable(authenticationDTO.getUserId());
			}
		}
	}

	public Long getCurrentUserId() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("getCurrentUserId() - SecurityContextHolder.getContext().getAuthentication()  is null");
			return 2L;
		}
		Object authObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (authObject == null) {
			log.info("getCurrentUserId() - authObject  is null");
			return 2L;
		} else {
			log.info("getCurrentUser()-UserMaster - found");
			AuthenticationDTO authenticationDTO = (AuthenticationDTO) authObject;
			return authenticationDTO.getUserId();
		}
	}

}