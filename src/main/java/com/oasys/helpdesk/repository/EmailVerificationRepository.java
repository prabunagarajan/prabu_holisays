package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.EmailVerification;

@Repository
public interface EmailVerificationRepository  extends JpaRepository<EmailVerification, Long> {

	public EmailVerification findByEmailId(String token);
}
