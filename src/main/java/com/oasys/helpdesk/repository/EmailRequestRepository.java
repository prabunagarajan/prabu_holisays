package com.oasys.helpdesk.repository;

import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.EmailRequest;
import com.oasys.helpdesk.entity.GrievanceregisterEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface EmailRequestRepository extends JpaRepository<EmailRequest, Long> {
	
	Optional<EmailRequest> findByApplicationNo(@Param("appNo") String applicationNo);
	
	@Query("select e from EmailRequest e where e.id=:id and e.isActive=true ")
	EmailRequest getById(@Param("id") Long id);
	
	@Query(value = "select e.* from email_request e where e.is_active=true and e.modified_date >= :fromdate and e.modified_date <= :todate and e.from_emailid =:fromEmailId", nativeQuery=true)
	List<EmailRequest> getEmailbyBetweenDates(@Param("fromdate") String from, @Param("todate") String to, @Param("fromEmailId") String fromEmailId);

	@Query(value = "select count(*) from email_request e where e.is_active=true and date(e.created_date) = :fromdate", nativeQuery=true)
	Integer getNewEmail(@Param("fromdate") String from);
	

	@Query(value = "SELECT a FROM EmailRequest a ")
	Page<EmailRequest> getAll(Pageable pageable);

	@Query("select h from EmailRequest h where h.fromEmailId=:emailId ")
	Optional<EmailRequest> findByFromEmailId(@Param("emailId") String emailId);

	@Query("select h from EmailRequest h where h.fromEmailId=:emailId")
	Page<EmailRequest> getByFromEmailId(@Param("emailId") String emailId, Pageable pageable);

//	@Query(value = "select * from email_request  where Date(createdDate)=:createdDate", nativeQuery=true)
//	Page<EmailRequest> getByDateRange(@Param("createdDate") Date from, Pageable pageable);

	
	@Query("SELECT a FROM EmailRequest a where  Date(createdDate)=:createdDate")
	Page<EmailRequest> getByDateRange(@Param("createdDate") Date finalDate,Pageable pageable);

	
	
	@Query(value = "select e.* from email_request e where e.created_date >= :fromdate and e.created_date <= :todate and e.from_emailid =:fromEmailId", nativeQuery=true)
	Page<EmailRequest> getByEmailIdAndDateRange(@Param("fromdate") String from, @Param("todate") String to, @Param("fromEmailId") String fromEmailId, Pageable pageable);

	
	
	
	@Query(value = "SELECT a FROM email_request a where a.from_emailid =:fromEmailId and e.created_date >= :fromDate and e.created_date <= :toDate", nativeQuery=true)
	Page<EmailRequest> getByIssueDetailsAndStatus(@Param("fromEmailId") String fromEmailId, @Param("fromDate") String fromDate,@Param("toDate") String toDate, Pageable pageable);

	List<EmailRequest> findAllByOrderByModifiedDateDesc();

}