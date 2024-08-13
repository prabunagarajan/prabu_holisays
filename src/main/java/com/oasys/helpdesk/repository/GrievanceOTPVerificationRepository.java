package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceOTPVerificationEntity;

@Repository
public interface GrievanceOTPVerificationRepository extends JpaRepository<GrievanceOTPVerificationEntity, Long> {

	GrievanceOTPVerificationEntity findByPhoneNumber(String phoneNumber);

}
