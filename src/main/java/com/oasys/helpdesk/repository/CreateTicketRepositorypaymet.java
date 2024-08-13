package com.oasys.helpdesk.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.CreateTicketEntitypayment;

@Repository
public interface CreateTicketRepositorypaymet extends JpaRepository<CreateTicketEntitypayment, Long> {

	List<CreateTicketEntitypayment> findAllByOrderByModifiedDateDesc();

	List<CreateTicketEntitypayment> findByIsActiveOrderByModifiedDateDesc(@Param("isActive") Boolean status);

	@Query("select a from CreateTicketEntitypayment a where a.ticketStatus.id=:id")
	List<CreateTicketEntity> findByStatus(@Param("id") Long id);
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and Date(createdDate)=:createdDate order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedDate(@Param("ticketStatusId") Long ticketStatusId, @Param("createdDate") Date createdDate);

	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate")
	Integer getCountByStatusAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
//	@Query(value = "select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  Date BETWEEN :createdDate AND :modifiedDate")
//	Integer getCountByStatusAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId,@Param("createdDate")Date startDate,@Param("modifiedDate")Date endDate);
//	
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and Date(createdDate)=:createdDate order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedByAndCreatedDate(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy, @Param("createdDate") Date createdDate);
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedByAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
	
	
	Optional<CreateTicketEntitypayment> findByTicketNumberIgnoreCase(String upperCase);
	
	
//	@Query("select distinct(count(*)) from CreateTicketEntity ")
//	Integer getCount();
	
	@Query("select distinct(count(*)) from CreateTicketEntitypayment where year(created_date) = '2022' and month(created_date) = '03'")
	Integer getCount();
	
	@Query("select distinct(count(*)) from CreateTicketEntitypayment where year(created_date) = '2022' and month(created_date) = '02'")
	Integer getCountfeb();
	
	@Query("select distinct(count(*)) from CreateTicketEntitypayment where year(created_date) = '2022' and month(created_date) = '04'")
	Integer getCountapril();
	
	
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy")
	Integer getCountByStatusAndCreatedByper(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy);
    
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId")
	Integer getCountBySAdmin(@Param("ticketStatusId") Long ticketStatusId);
    
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '02'")
	Integer getCountByStatusAndCreatedBy(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy);
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '04'")
	Integer getCountByStatusAndCreatedByApex(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy);
	
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '04'")
	Integer getCountByStatusAndCreatedByApril(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy);
	
	
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2022' and month(created_date) = '02'")
	Integer getCountByFebHelpdesk(@Param("ticketStatusId") Long ticketStatusId);
	

	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2022' and month(created_date) = '04'")
	Integer getCountByFebHelpdeskapril(@Param("ticketStatusId") Long ticketStatusId);
	
	
    
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '03'")
	Integer getCountByStatusAndCreatedBy1(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy);

	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2022' and month(created_date) = '03'")
	Integer getCountByStatusAndHelpdesk(@Param("ticketStatusId") Long ticketStatusId);

	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntitypayment> findByTicketStatusOpen(@Param("licenseNumber") String licenseNumber);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Pending%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntitypayment> findByTicketStatusPending(@Param("licenseNumber") String licenseNumber);
	
	@Query("select a from CreateTicketEntitypayment a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Resolved%')) and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntitypayment> findByTicketStatusClosed(@Param("licenseNumber") String licenseNumber);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntitypayment> findByTicketStatusReopen(@Param("licenseNumber") String licenseNumber);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntitypayment> findByTicketStatusEscalated(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntitypayment a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Resolved%')) and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntitypayment> findByTicketStatusResolved(@Param("licenseNumber") String licenseNumber);

	@Query("SELECT a FROM CreateTicketEntitypayment a where a.assignTo.id =:assign_to order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> getByAssignTo(@Param("assign_to") Long assigntoId);
	
	@Query("SELECT a FROM CreateTicketEntitypayment a where a.assignTo.id =:assign_to and date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> getByAssignToAndCreateddate(@Param("assign_to") Long assigntoId,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
	
	
	@Query("SELECT a FROM CreateTicketEntitypayment a where a.assignTo.id =:assign_to  and a.flag =:flag")
	List<CreateTicketEntitypayment> getByFlagAssignto(@Param("assign_to") Long assigntoId,@Param("flag") Boolean flag);
	

//	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and Date(createdDate)=:createdDate and a.issueFrom.id=:issueFromId")
//	Integer getCountByStatusAndCreatedByAndCreatedDateAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy, @Param("createdDate") Date createdDate, @Param("issueFromId") Long issueFromId);



@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy  and a.issueFrom.id=:issueFromId and a.createdDate between :fromDate and :toDate")
Integer getCountByStatusAndCreatedByAndCreatedDateAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy,@Param("issueFromId") Long issueFromId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate );
	
	List<CreateTicketEntitypayment> findAllByIssueFrom_idOrderByModifiedDateDesc(@Param("issueFrom") Long id);
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.assignTo.id=:assignToId and Date(createdDate)=:createdDate order by a.modifiedDate desc")
	Integer getCountByStatusAndAssignToAndCreatedDate(@Param("ticketStatusId") Long ticketStatusId, @Param("assignToId") Long assignToId, @Param("createdDate") Date createdDate);
	
	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and a.assignTo.id=:assignToId and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Integer getCountByStatusAndAssignToAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId, @Param("assignToId") Long assignToId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
	
	/*
	 * @Query("select a from CreateTicketEntity a where a.ticketStatus.id in :ticketStatus and a.assignTo is null"
	 * + " and (a.createdDate + a.slaMaster.sla) < :currentDate")
	 */
	@Query(value = "select * from ticket t, sla_master s WHERE t.sla_id=s.id and t.sla_id is not null and t.assign_to is null and t.ticket_status_id in :ticketStatusIds" + 
			" and DATE_ADD(t.created_date,interval s.sla hour) < now()", nativeQuery=true)
	List<CreateTicketEntitypayment> findByFilter(@Param("ticketStatusIds") List<Long> ticketStatusIds);

	
	@Query("SELECT t FROM CreateTicketEntitypayment t ")
	Page<CreateTicketEntitypayment> getAll(Pageable pageable);
	
	
	@Query("SELECT t FROM CreateTicketEntitypayment t where t.ticketStatus.id=:status")
	Page<CreateTicketEntitypayment> getByStatus(@Param("status") Long status, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id=:status")
	Page<CreateTicketEntitypayment> getByCreatedDateAndStatus(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("status") Long status, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate")
	Page<CreateTicketEntitypayment> getByCreatedDate(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate, Pageable pageable);

	List<CreateTicketEntitypayment> findByLicenceNumberIgnoreCase(String licenseNumber);
	
	
	
	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id=:status and t.category.id=:category and t.subCategory.id=:subCategory and t.issueFrom.id=:issueFrom and t.licenceTypeId=:licenceTypeId  and t.licenceNumber=:licenceNumber and t.priority.id=:priority and t.ticketNumber=:ticketNumber")
	Page<CreateTicketEntitypayment> getByCreatedDateStatusAll(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("status") Long status,@Param("category") Long category,@Param("subCategory") Long subCategory,@Param("issueFrom") Long issueFrom,@Param("licenceTypeId") String licenceTypeId,@Param("licenceNumber") String licenceNumber,@Param("priority") Long priority,@Param("ticketNumber") String ticketNumber, Pageable pageable);


	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category")
	Page<CreateTicketEntitypayment> getByCreatedDateAndCategory(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("category") Long category, Pageable pageable);

	
	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.subCategory.id=:subCategory")
	Page<CreateTicketEntitypayment> getByCreatedDateAndSubcategory(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("subCategory") Long subCategory, Pageable pageable);

	
	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.issueFrom.id=:issueFrom")
	Page<CreateTicketEntitypayment> getByCreatedDateAndIssuefromId(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("issueFrom") Long issueFrom, Pageable pageable);

	
	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.licenceTypeId=:licenceTypeId ")
	Page<CreateTicketEntitypayment> getByCreatedDateAndLicenceTypeId(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("licenceTypeId") String licenceTypeId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.licenceNumber=:licenceNumber ")
	Page<CreateTicketEntitypayment> getByCreatedDateAndLicenceNumber(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("licenceNumber") String licenceTypeId, Pageable pageable);

	
	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.priority.id=:priority")
	Page<CreateTicketEntitypayment> getByCreatedDateAndPriority(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("priority") Long priority, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and  t.ticketNumber=:ticketNumber")
	Page<CreateTicketEntitypayment> getByCreatedDateAndTicketNumber(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("ticketNumber") String ticketNumber, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntitypayment t where UPPER(t.mobile)=:search Or UPPER(t.email)=:search")
	List<CreateTicketEntitypayment> findByMobileOrEmailIgnoreCase(@Param("search") String search);

	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketOpen(@Param("issueFromId") Long issueFromId);
	
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Pending%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketPending(@Param("issueFromId") Long issueFromId);
	
	@Query("select a from CreateTicketEntitypayment a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Resolved%')) and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketClosed(@Param("issueFromId") Long issueFromId);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketReopen(@Param("issueFromId") Long issueFromId);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketEscalated(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntitypayment a where a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findAllByIssueFrom(@Param("issueFromId") Long issueFromId);

	
	@Query("SELECT t FROM CreateTicketEntitypayment t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id=:status and  t.category.id=:category")
	Page<CreateTicketEntitypayment> getByCreatedDateAndStatusPageAndCategory(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("status") Long status,@Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and a.licenceNumber=:licenceNumber order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketOpen1(@Param("licenceNumber") String licenceNumber);
	
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Pending%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Assigned%') and a.licenceNumber=:licenceNumber order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketPending1(@Param("licenceNumber") String licenceNumber);
	
	@Query("select a from CreateTicketEntitypayment a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%')) and a.licenceNumber=:licenceNumber order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketClosed1(@Param("licenceNumber") String licenceNumber);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and a.licenceNumber=:licenceNumber order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketReopen1(@Param("licenceNumber") String licenceNumber);
	
	@Query("select a from CreateTicketEntitypayment a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and a.licenceNumber=:licenceNumber order by a.modifiedDate desc")
	List<CreateTicketEntitypayment> findByTicketEscalated1(@Param("licenceNumber") String licenceNumber);

	@Query("select a from CreateTicketEntitypayment a where a.licenceNumber=:licenceNumber order by a.createdDate desc")
	List<CreateTicketEntitypayment> findAllByIssueFrom1(@Param("licenceNumber") String licenceNumber);
	
//	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate and a.licenceNumber=:licenceNumber")
//	Integer getCountByStatusAndCreatedDateBetweenAndLicenceNumber(@Param("ticketStatusId") Long ticketStatusId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("licenceNumber") String licenceNumber);

	@Query("select distinct(count(a.id)) from CreateTicketEntitypayment a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate and a.licenceNumber IN :licenceNumber")
	Integer getCountByStatusAndCreatedDateBetweenAndLicenceNumber(@Param("ticketStatusId") Long ticketStatusId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("licenceNumber")ArrayList<String>licnumber);

	
	
	
}
