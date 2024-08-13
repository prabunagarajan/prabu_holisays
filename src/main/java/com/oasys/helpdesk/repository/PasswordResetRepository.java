package com.oasys.helpdesk.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.PasswordReset;



@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
	
	public PasswordReset findByToken(String token);
	
}
