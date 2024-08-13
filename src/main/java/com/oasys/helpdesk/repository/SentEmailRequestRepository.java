package com.oasys.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.EmailRequest;
import com.oasys.helpdesk.entity.SentEmailRequest;


@Repository
public interface SentEmailRequestRepository extends JpaRepository<SentEmailRequest, Long> {
	
	
	@Query("select e from SentEmailRequest e where e.id=:id and e.isActive=true ")
	EmailRequest getById(@Param("id") Long id);
	
	
	@Query(value = "select e.* from email_request e where e.created_date >= :fromdate and e.created_date <= :todate and e.from_emailid =:fromEmailId", nativeQuery=true)
	Page<EmailRequest> getByEmailIdAndDateRange(@Param("fromdate") String from, @Param("todate") String to, @Param("fromEmailId") String fromEmailId, Pageable pageable);


	@Query("select h from SentEmailRequest h where h.toEmailList=:emailId")
	Page<EmailRequest> getByFromEmailId(@Param("emailId") String emailId, Pageable pageable);

	@Query(value = "SELECT a FROM sent_email_request a where a.to_emailid_list =:toEmailId and e.created_date >= :fromDate and e.created_date <= :toDate", nativeQuery=true)
	Page<EmailRequest> getByIssueDetailsAndStatus(@Param("toEmailId") String toEmailId, @Param("fromDate") String fromDate,@Param("toDate") String toDate, Pageable pageable);
}