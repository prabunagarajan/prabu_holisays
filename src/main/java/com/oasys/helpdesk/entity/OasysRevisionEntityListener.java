package com.oasys.helpdesk.entity;

import java.nio.file.attribute.UserPrincipal;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oasys.helpdesk.HelpDeskApplication;
import com.oasys.helpdesk.security.AuthenticationDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class OasysRevisionEntityListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		OasysRevisionEntity revEntity = (OasysRevisionEntity) revisionEntity;
		String userName = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()
					|| authentication instanceof AnonymousAuthenticationToken) {
				log.debug("======CustomRevisionEntityListener====auditing username." + authentication);
			}
			AuthenticationDTO userPrincipal = (AuthenticationDTO) authentication.getPrincipal();
			userName = userPrincipal.getUserName();
		} catch (Exception exception) {
			log.error("======CustomRevisionEntityListener====auditing username.", exception);
		}
		revEntity.setUsername(userName);
	}
}
