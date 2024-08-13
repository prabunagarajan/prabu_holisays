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

import com.oasys.helpdesk.dto.DashboardCount;
import com.oasys.helpdesk.dto.DurationDTO;
import com.oasys.helpdesk.dto.InboundCallsTotalOperatinghrsDTO;
import com.oasys.helpdesk.dto.Resolution24hrDTO;
import com.oasys.helpdesk.dto.ResolutionDTO;
import com.oasys.helpdesk.dto.SecuritymanagementDTO;
import com.oasys.helpdesk.entity.CreateTicketEntity;

@Repository
public interface CreateTicketRepository extends JpaRepository<CreateTicketEntity, Long> {
	List<CreateTicketEntity> findAllByOrderByModifiedDateDesc();

	List<CreateTicketEntity> findByIsActiveOrderByModifiedDateDesc(@Param("isActive") Boolean status);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.id=:id")
	List<CreateTicketEntity> findByStatus(@Param("id") Long id);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and Date(createdDate)=:createdDate order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedDate(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdDate") Date createdDate);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate")
	Integer getCountByStatusAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

//	@Query(value = "select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  Date BETWEEN :createdDate AND :modifiedDate")
//	Integer getCountByStatusAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId,@Param("createdDate")Date startDate,@Param("modifiedDate")Date endDate);
//	

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and Date(createdDate)=:createdDate order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedByAndCreatedDate(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy, @Param("createdDate") Date createdDate);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedByAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	Optional<CreateTicketEntity> findByTicketNumberIgnoreCase(String upperCase);

//	@Query("select distinct(count(*)) from CreateTicketEntity ")
//	Integer getCount();

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '04' and  month(created_date) = '03' and  month(created_date) = '02'")
	Integer getCountover();

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023'")
	Integer getCount();

//	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '04'")
//	Integer getCount();

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2024' and month(created_date) = '01'")
	Integer getCountfeb();

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '12'")
	Integer getCountapril();

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '11'")
	Integer getCountF();

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy")
	Integer getCountByStatusAndCreatedByper(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and year(created_date) = '2023'")
	Integer getCountBySAdmin(@Param("ticketStatusId") Long ticketStatusId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2023' and month(created_date) = '01'")
	Integer getCountByStatusAndCreatedBy(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2023' and month(created_date) = '03'")
	Integer getCountByStatusAndCreatedByApex(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2023' and month(created_date) = '08'")
	Integer getCountByStatusAndCreatedByApril(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2024' and month(created_date) = '01'")
	Integer getCountByFebHelpdesk(@Param("ticketStatusId") Long ticketStatusId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2023' and month(created_date) = '12'")
	Integer getCountByFebHelpdeskapril(@Param("ticketStatusId") Long ticketStatusId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2023' and month(created_date) = '02'")
	Integer getCountByStatusAndCreatedBy1(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2023' and month(created_date) = '11'")
	Integer getCountByStatusAndHelpdesk(@Param("ticketStatusId") Long ticketStatusId);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusOpen(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Pending%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusPending(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntity a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Resolved%')) and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusClosed(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusReopen(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusEscalated(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntity a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Resolved%')) and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusResolved(@Param("licenseNumber") String licenseNumber);

	@Query("SELECT a FROM CreateTicketEntity a where a.assignTo.id =:assign_to order by a.modifiedDate desc")
	List<CreateTicketEntity> getByAssignTo(@Param("assign_to") Long assigntoId);

	@Query("SELECT a FROM CreateTicketEntity a where a.assignTo.id =:assign_to and date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	Page<CreateTicketEntity> getByAssignToAndCreateddate(@Param("assign_to") Long assigntoId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

//	@Query("SELECT a FROM CreateTicketEntity a where a.assignTo.id =:assign_to and date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
//	Page<CreateTicketEntity> getByAssignToAndCreateddate(@Param("assign_to") Long assigntoId,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,Pageable pageable);
//	

	@Query("SELECT a FROM CreateTicketEntity a where a.assignTo.id =:assign_to  and a.flag =:flag")
	List<CreateTicketEntity> getByFlagAssignto(@Param("assign_to") Long assigntoId, @Param("flag") Boolean flag);

//	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and Date(createdDate)=:createdDate and a.issueFrom.id=:issueFromId")
//	Integer getCountByStatusAndCreatedByAndCreatedDateAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy, @Param("createdDate") Date createdDate, @Param("issueFromId") Long issueFromId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy  and a.issueFrom.id=:issueFromId and a.createdDate between :fromDate and :toDate")
	Integer getCountByStatusAndCreatedByAndCreatedDateAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy, @Param("issueFromId") Long issueFromId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	List<CreateTicketEntity> findAllByIssueFrom_idOrderByModifiedDateDesc(@Param("issueFrom") Long id);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.assignTo.id=:assignToId and Date(createdDate)=:createdDate order by a.modifiedDate desc")
	Integer getCountByStatusAndAssignToAndCreatedDate(@Param("ticketStatusId") Long ticketStatusId,
			@Param("assignToId") Long assignToId, @Param("createdDate") Date createdDate);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.assignTo.id=:assignToId and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Integer getCountByStatusAndAssignToAndCreatedDateBetween(@Param("ticketStatusId") Long ticketStatusId,
			@Param("assignToId") Long assignToId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	/*
	 * @Query("select a from CreateTicketEntity a where a.ticketStatus.id in :ticketStatus and a.assignTo is null"
	 * + " and (a.createdDate + a.slaMaster.sla) < :currentDate")
	 */
	@Query(value = "select * from ticket t, sla_master s WHERE t.sla_id=s.id and t.sla_id is not null and t.assign_to is null and t.ticket_status_id in :ticketStatusIds"
			+ " and DATE_ADD(t.created_date,interval s.sla hour) < now()", nativeQuery = true)
	List<CreateTicketEntity> findByFilter(@Param("ticketStatusIds") List<Long> ticketStatusIds);

	@Query("SELECT t FROM CreateTicketEntity t ")
	Page<CreateTicketEntity> getAll(Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByStatus(@Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndStatus(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate")
	Page<CreateTicketEntity> getByCreatedDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	List<CreateTicketEntity> findByLicenceNumberIgnoreCase(String licenseNumber);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id in :ticketStatusId and t.category.id=:category and t.subCategory.id=:subCategory and t.issueFrom.id=:issueFrom and t.licenceTypeId=:licenceTypeId  and t.licenceNumber=:licenceNumber and t.priority.id=:priority and t.ticketNumber=:ticketNumber")
	Page<CreateTicketEntity> getByCreatedDateStatusAll(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("category") Long category,
			@Param("subCategory") Long subCategory, @Param("issueFrom") Long issueFrom,
			@Param("licenceTypeId") String licenceTypeId, @Param("licenceNumber") String licenceNumber,
			@Param("priority") Long priority, @Param("ticketNumber") String ticketNumber, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category")
	Page<CreateTicketEntity> getByCreatedDateAndCategory(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("category") Long category, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.subCategory.id=:subCategory")
	Page<CreateTicketEntity> getByCreatedDateAndSubcategory(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("subCategory") Long subCategory, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.issueFrom.id=:issueFrom")
	Page<CreateTicketEntity> getByCreatedDateAndIssuefromId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("issueFrom") Long issueFrom, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.licenceTypeId=:licenceTypeId ")
	Page<CreateTicketEntity> getByCreatedDateAndLicenceTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceTypeId") String licenceTypeId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.licenceNumber=:licenceNumber ")
	Page<CreateTicketEntity> getByCreatedDateAndLicenceNumber(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceTypeId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.priority.id=:priority")
	Page<CreateTicketEntity> getByCreatedDateAndPriority(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("priority") Long priority, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.ticketNumber=:ticketNumber")
	Page<CreateTicketEntity> getByCreatedDateAndTicketNumber(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketNumber") String ticketNumber, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where UPPER(t.mobile)=:search Or UPPER(t.email)=:search")
	List<CreateTicketEntity> findByMobileOrEmailIgnoreCase(@Param("search") String search);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketOpen(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Pending%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketPending(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntity a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Resolved%')) and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketClosed(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketReopen(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketEscalated(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntity a where a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findAllByIssueFrom(@Param("issueFromId") Long issueFromId);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id in :ticketStatusId and  t.category.id=:category")
	Page<CreateTicketEntity> getByCreatedDateAndStatusPageAndCategory(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId,
			@Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketOpen1(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

//	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Pending%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') or UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Assigned%') and a.licenceNumber=:licenceNumber order by a.modifiedDate desc")
//	List<CreateTicketEntity> findByTicketPending1(@Param("licenceNumber") String licenceNumber);
//	
	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Reopen','Open','Assigned','Change Request','Need Clarification','Escalated') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketPending1(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String unitCode);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketClosedReCAL(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

	@Query("select a from CreateTicketEntity a where (UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Closed%')) and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketClosed1(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketReopen1(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketEscalated1(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

//	@Query("select a from CreateTicketEntity a where a.licenceNumber in :licenceNumber order by a.modifiedDate desc")
//	List<CreateTicketEntity> findAllByIssueFrom1(@Param("licenceNumber") List<String> licenceNumber);

//	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate and a.licenceNumber=:licenceNumber")
//	Integer getCountByStatusAndCreatedDateBetweenAndLicenceNumber(@Param("ticketStatusId") Long ticketStatusId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("licenceNumber") String licenceNumber);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.id=:id  and a.ticketNumber=:ticketNumber and a.licenceNumber=:licenceNumber")
	Optional<CreateTicketEntity> findByStatusAndTicketNumberAndLicenceNumber(@Param("id") Long openid,
			@Param("ticketNumber") String ticnumber, @Param("licenceNumber") String licenceNumber);

	@Query("select a from CreateTicketEntity a where  a.ticketNumber=:ticketNumber and a.licenceNumber=:licenceNumber")
	Optional<CreateTicketEntity> findByTicketNumberAndLicenceNumber(@Param("ticketNumber") String ticnumber,
			@Param("licenceNumber") String licenceNumber);

	@Query("select distinct(count(*)) from CreateTicketEntity")
	Integer getoverAllCount();

	@Query(value = "select a from CreateTicketEntity a where a.ticketStatus.id=:id")
	List<CreateTicketEntity> findByticketStatus(@Param("id") List<Long> ticketStatusIds);

	@Query(value = "select * from ticket t, sla_master s WHERE t.sla_id=s.id and t.sla_id is not null  and t.ticket_status_id in :ticketStatusIds"
			+ " and DATE_ADD(t.created_date,interval s.sla hour) <= now()", nativeQuery = true)
	List<CreateTicketEntity> findByClosedinsla(@Param("ticketStatusIds") List<Long> ticketStatusIds);

	@Query(value = "select * from ticket t, sla_master s WHERE t.sla_id=s.id and t.sla_id is not null  and t.ticket_status_id in :ticketStatusIds"
			+ " and DATE_ADD(t.created_date,interval s.sla hour) >= now()", nativeQuery = true)
	List<CreateTicketEntity> findByClosedoutsla(@Param("ticketStatusIds") List<Long> ticketStatusIds);

	@Query(value = "select * from ticket t, sla_master s WHERE t.sla_id=s.id and t.sla_id is not null  and t.ticket_status_id in :ticketStatusIds"
			+ " and DATE_ADD(t.created_date,interval s.sla hour) <= now()", nativeQuery = true)
	List<CreateTicketEntity> findByOpeninsla(@Param("ticketStatusIds") List<Long> ticketStatusIds);

	@Query(value = "select * from ticket t, sla_master s WHERE t.sla_id=s.id and t.sla_id is not null  and t.ticket_status_id in :ticketStatusIds"
			+ " and DATE_ADD(t.modified_date,interval s.sla hour) >= now()", nativeQuery = true)
	List<CreateTicketEntity> findByOpenoutsla(@Param("ticketStatusIds") List<Long> ticketStatusIds);

	@Query(value = "SELECT COUNT(t.ticket_number) AS overalltickets,\\r\\n\"\r\n"
			+ "			+ \"COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) < sm.sla \\r\\n\"\r\n"
			+ "			+ \"AND ts.ticketstatusname IN('Closed','Resolved') \\r\\n\"\r\n"
			+ "			+ \"THEN t.ticket_number END) AS closedwithinsla,\\r\\n\"\r\n"
			+ "			+ \"COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) > sm.sla \\r\\n\"\r\n"
			+ "			+ \"AND ts.ticketstatusname IN('Closed','Resolved') THEN t.ticket_number END) AS closedoutofsla,\\r\\n\"\r\n"
			+ "			+ \"COUNT(CASE WHEN TIMESTAMPDIFF(HOUR,t.modified_date,NOW()) < sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS openwithinsla, \\r\\n\"\r\n"
			+ "			+ \"COUNT(CASE WHEN TIMESTAMPDIFF(HOUR,t.modified_date,NOW()) > sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS openoutofsla \\r\\n\"\r\n"
			+ "			+ \"FROM ticket t \\r\\n\" + \"JOIN sla_master sm ON t.sla_id = sm.id \\r\\n\"\r\n"
			+ "			+ \"JOIN ticket_status_help ts ON ts.id=t.ticket_status_id \\r\\n\"\r\n"
			+ "			+ \"JOIN help_desk_ticket_category tc ON tc.id=t.category_id \\r\\n\" + \"where date(t.created_date) \\r\\n\"\r\n"
			+ "			+ \"between :fromDate and :toDate and t.category_id=:category_id \\r\\n\"\r\n"
			+ "			+ \"and t.search_option not in('RTO-123456','RPO-123456','tollfreenumber','securitymanagement')", nativeQuery = true)
	public List<Ticketdashboard> getCountByStatusAndCreatedDateBetweenAndCategory(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category_id") Long categoryid);

	@Query(value = "SELECT t.district, COUNT(t.ticket_number) AS OverallTickets,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) < sm.sla AND ts.ticketstatusname IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS Closedwithinsla,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) > sm.sla AND ts.ticketstatusname IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS Closedoutofsla,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR,t.modified_date,NOW()) < sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS Openwithinsla,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR,t.modified_date,NOW()) > sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS Openoutofsla \r\n" + "FROM ticket t \r\n"
			+ "JOIN sla_master sm ON t.sla_id = sm.id \r\n"
			+ "JOIN ticket_status_help ts ON ts.id=t.ticket_status_id \r\n"
			+ "JOIN help_desk_ticket_category tc ON tc.id=t.category_id \r\n"
			+ "where date(t.created_date) between :fromDate and :toDate and t.category_id=:category_id  \r\n"
			+ "and t.search_option not in('RTO-123456','RPO-123456','tollfreenumber','securitymanagement')\r\n"
			+ "GROUP BY t.district", nativeQuery = true)
	public List<Districtdashboard> getCountByCreatedDateBetweenAndCategory(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category_id") Long categoryid);

	// district category
	@Query(value = "SELECT t.district,hdt.sub_category_name As subcategoryname, COUNT(t.ticket_number) AS overalltickets,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) < sm.sla AND ts.ticketstatusname IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS closedwithinsla,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) > sm.sla AND ts.ticketstatusname IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS closedoutofsla,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR,t.modified_date,NOW()) < sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS Openwithinsla,\r\n"
			+ "COUNT(CASE WHEN TIMESTAMPDIFF(HOUR,t.modified_date,NOW()) > sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved') \r\n"
			+ "THEN t.ticket_number END) AS Openoutofsla \r\n" + "FROM ticket t \r\n"
			+ "JOIN sla_master sm ON t.sla_id = sm.id \r\n"
			+ "JOIN ticket_status_help ts ON ts.id=t.ticket_status_id  \r\n"
			+ "JOIN  help_desk_ticket_sub_category hdt ON hdt.id=t.subcategory_id  \r\n"
			+ "JOIN help_desk_ticket_category tc ON tc.id=t.category_id \r\n"
			+ "WHERE  t.district=:district  and date(t.created_date) between :fromDate and :toDate  and t.category_id=:category_id \r\n"
			+ "AND t.search_option not in ('RTO-123456','RPO-123456','tollfreenumber','securitymanagement')\r\n"
			+ "GROUP BY t.district,hdt.sub_category_name", nativeQuery = true)
	public List<Districtcategorydashboard> getCountByDistrictAndCreatedDateBetweenAndCategory(
			@Param("district") String district, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("category_id") Long categoryid);

	// @Query(value="SELECT t.district,hdt.sub_category_name As subcategoryname,
	// COUNT(t.ticket_number) AS overalltickets,COUNT(CASE WHEN TIMESTAMPDIFF(HOUR
	// ,t.created_date , t.modified_date) < sm.sla AND ts.ticketstatusname
	// IN('Closed','Resolved') THEN t.ticket_number END) AS
	// closedwithinsla,COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date ,
	// t.modified_date) > sm.sla AND ts.ticketstatusname IN('Closed','Resolved')
	// THEN t.ticket_number END) AS closedoutofsla,COUNT(CASE WHEN
	// TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) < sm.sla AND
	// ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS
	// Openwithinsla,COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date ,
	// t.modified_date) > sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved')
	// THEN t.ticket_number END) AS Openoutofsla FROM ticket t JOIN sla_master sm ON
	// t.sla_id = sm.id JOIN ticket_status_help ts ON ts.id=t.ticket_status_id JOIN
	// help_desk_ticket_sub_category hdt ON hdt.id=t.subcategory_id WHERE
	// t.district=:district and date(t.created_date) between :fromDate and :toDate
	// GROUP BY t.district,hdt.sub_category_name",nativeQuery=true)
	// public List<EntitylicenseDTO>
	// getCountByDistrictAndEntityTypeIdAndLicenceTypeIdAndCreatedDateBetween(@Param("district")
	// String district,@Param("entityTypeId") String
	// entityTypeId,@Param("licenceTypeId") String licenceTypeId,@Param("fromDate")
	// Date fromDate,@Param("toDate") Date toDate);

	@Query(value = "SELECT t.district_code As districtcode,t.district AS district,t.entity_type_id As entitytype,t.licence_type_id As licencetype,licence_number As licencenumber,COUNT(t.ticket_number) AS overalltickets, COUNT(CASE WHEN  ts.ticketstatusname IN('Closed','Resolved') THEN t.ticket_number END) AS closedcount,COUNT(CASE WHEN  ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS opencount FROM ticket t JOIN ticket_status_help ts ON ts.id=t.ticket_status_id  where t.district_code in :districtCode  GROUP BY t.district_code", nativeQuery = true)
	public List<EntitylicenseDTO> getCountBy(@Param("districtCode") List<String> districtcodeid);

	// @Query(value="SELECT t.district,hdt.sub_category_name As subcategoryname,
	// COUNT(t.ticket_number) AS overalltickets,COUNT(CASE WHEN TIMESTAMPDIFF(HOUR
	// ,t.created_date , t.modified_date) < sm.sla AND ts.ticketstatusname
	// IN('Closed','Resolved') THEN t.ticket_number END) AS
	// closedwithinsla,COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date ,
	// t.modified_date) > sm.sla AND ts.ticketstatusname IN('Closed','Resolved')
	// THEN t.ticket_number END) AS closedoutofsla,COUNT(CASE WHEN
	// TIMESTAMPDIFF(HOUR ,t.created_date , t.modified_date) < sm.sla AND
	// ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS
	// Openwithinsla,COUNT(CASE WHEN TIMESTAMPDIFF(HOUR ,t.created_date ,
	// t.modified_date) > sm.sla AND ts.ticketstatusname NOT IN('Closed','Resolved')
	// THEN t.ticket_number END) AS Openoutofsla FROM ticket t JOIN sla_master sm ON
	// t.sla_id = sm.id JOIN ticket_status_help ts ON ts.id=t.ticket_status_id JOIN
	// help_desk_ticket_sub_category hdt ON hdt.id=t.subcategory_id WHERE
	// t.district=:district and date(t.created_date) between :fromDate and :toDate
	// GROUP BY t.district,hdt.sub_category_name",nativeQuery=true)
	// public List<EntitylicenseshopDTO>
	// getCountByDistrictAndEntityTypeIdAndLicenceTypeIdAndShopCodeAndCreatedDateBetween(@Param("district")
	// String district,@Param("entityTypeId") String
	// entityTypeId,@Param("licenceTypeId") String licenceTypeId,@Param("shopCode")
	// String shopCode,@Param("fromDate") Date fromDate,@Param("toDate") Date
	// toDate);

	@Query(value = "SELECT t.district_code As districtcode,t.district AS district,t.entity_type_id As entitytype ,t.licence_type_id As licencetype ,t.shop_code As shopcode,t.licence_number As licencenumber,COUNT(t.ticket_number) AS overalltickets,COUNT(CASE WHEN  ts.ticketstatusname IN('Closed','Resolved') THEN t.ticket_number END) AS closedcount,COUNT(CASE WHEN  ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS opencount FROM ticket t JOIN ticket_status_help ts ON ts.id=t.ticket_status_id where t.district_code=:district_code GROUP BY t.district_code,t.district,t.entity_type_id,t.licence_type_id,t.shop_code,t.licence_number", nativeQuery = true)
	public List<EntitylicenseshopDTO> getByDistrictCode(@Param("district_code") String districtCode);

	@Query("select a from CreateTicketEntity a where  a.licenceNumber=:licenceNumber")
	List<CreateTicketEntity> findLicenceNumber(@Param("licenceNumber") String licenceNumber);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatus(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	Optional<CreateTicketEntity> findByViewStatusAndId(@Param("viewStatus") Boolean status, @Param("id") Long id);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and date(a.createdDate) between :fromDate and :toDate and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedByAndCreatedDateBetweenAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("issueFromId") Long issueFromId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate and a.issueFrom.id=:issueFromId")
	Integer getCountByStatusAndCreatedDateBetweenAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("issueFromId") Long issueFromId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.assignTo.id=:assignToId and date(a.createdDate) between :fromDate and :toDate and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	Integer getCountByStatusAndAssignToAndCreatedDateBetweenAndIssueFrom(@Param("ticketStatusId") Long ticketStatusId,
			@Param("assignToId") Long assignToId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("issueFromId") Long issueFromId);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate and a.licenceNumber IN :licenceNumber and a.issueFrom.id=:issueFromId ")
	Integer getCountByStatusAndCreatedDateBetweenAndLicenceNumberAndIssueFrom(
			@Param("ticketStatusId") Long ticketStatusId, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") ArrayList<String> licnumber,
			@Param("issueFromId") Long issueFromId);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id in :ticketStatusId and t.category.id=:category and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateStatusAndCategoryAll(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId,
			@Param("category") Long category, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateStatusAll(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAll(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryLic(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusAll(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusTic(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query(value = "SELECT  COUNT(t.ticket_number) AS OverallTickets,COUNT(CASE WHEN  ts.ticketstatusname IN('Closed','Resolved') THEN t.ticket_number END) AS closedtickets,COUNT(CASE WHEN  ts.ticketstatusname IN('Open','Reopen') THEN t.ticket_number END) AS opentickets,COUNT(CASE WHEN  ts.ticketstatusname IN('Pending','Escalated','Assigned') THEN t.ticket_number END) AS inprogresstickets FROM ticket t JOIN ticket_status_help ts ON ts.id=t.ticket_status_id where date(t.created_date) between :fromDate and :toDate and t.district_code in :district_code", nativeQuery = true)
	public List<Districtticketdashboard> getCountByStatusAndCreatedDateBetweenAndDistrictCode(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("district_code") List<String> district_code);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen') and a.districtCode=:districtCode order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketOpenAndDistrictCode(@Param("districtCode") String districtCode);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated') and a.districtCode=:districtCode order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketPendingAndDistrictCode(@Param("districtCode") String districtCode);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved') and a.districtCode=:districtCode order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketClosedAndDistrictCode(@Param("districtCode") String districtCode);

	@Query("select a from CreateTicketEntity a where a.districtCode=:districtCode order by a.modifiedDate desc")
	List<CreateTicketEntity> findAllByDistrictCode(@Param("districtCode") String districtCode);

	@Query(value = "SELECT t.district_code As districtcode,t.district AS district,t.entity_type_id As entitytype,t.licence_type_id As licencetype,licence_number As licencenumber,COUNT(t.ticket_number) AS overalltickets, COUNT(CASE WHEN  ts.ticketstatusname IN('Closed','Resolved') THEN t.ticket_number END) AS closedcount,COUNT(CASE WHEN  ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS opencount FROM ticket t JOIN ticket_status_help ts ON ts.id=t.ticket_status_id  where t.district_code in :districtCode and date(t.created_date) between :fromDate and :toDate and t.category_id=:category_id GROUP BY t.district_code", nativeQuery = true)
	public List<EntitylicenseDTO> getCountByDistrictCodeAndCreatedDateBetweenAndCategory(
			@Param("districtCode") List<String> districtcodeid, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category_id") Long category);

//	@Query("select t from CreateTicketEntity t where  date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.districtCode=:districtCode order by t.modifiedDate desc" )
//	List<CreateTicketEntity> findByCreatedDateBetweenAndCategoryAndDistrictCode(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("category") Long category,@Param("districtCode") String districtCode);
//	

	@Query("select t from CreateTicketEntity t where  date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.districtCode=:districtCode order by t.modifiedDate desc")
	Page<CreateTicketEntity> findByCreatedDateBetweenAndCategoryAndDistrictCode(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, @Param("districtCode") String districtCode,
			Pageable pageable);

	@Query("select t from CreateTicketEntity t where  date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category  order by t.modifiedDate desc")
	Page<CreateTicketEntity> findByCreatedDateBetweenAndCategoryAll(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, Pageable pageable);

	@Query(value = "SELECT t.district_code As districtcode,t.district AS district,t.entity_type_id As entitytype,t.licence_type_id As licencetype,licence_number As licencenumber,COUNT(t.ticket_number) AS overalltickets, COUNT(CASE WHEN  ts.ticketstatusname IN('Closed','Resolved') THEN t.ticket_number END) AS closedcount,COUNT(CASE WHEN  ts.ticketstatusname NOT IN('Closed','Resolved') THEN t.ticket_number END) AS opencount FROM ticket t JOIN ticket_status_help ts ON ts.id=t.ticket_status_id  where  date(t.created_date) between :fromDate and :toDate and t.category_id=:category_id", nativeQuery = true)
	public List<EntitylicenseDTO> getCountByCreatedDateBetweenAndCategoryall(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category_id") Long category);

	@Query("select t from CreateTicketEntity t where  date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.districtCode=:districtCode order by t.modifiedDate desc")
	List<CreateTicketEntity> findByCreatedDateBetweenAndCategoryAndDistrictCodeAll(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, @Param("districtCode") String districtCode);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpenAndDistrictCode(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective') or (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPendingAndDistrictCode(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosedAndDistrictCode(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.districtCode in :districtCode) and (date(a.createdDate) between :fromDate and :toDate)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByDistrictCode(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

//	@Query("select a from CreateTicketEntity a where date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
//	Page<CreateTicketEntity> findAllByFromDateAndToDateAndPageable(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  a.ticketNumber=:ticketNumber or (a.licenceNumber=:licenceNumber) or (a.shopCode=:shopCode) and a.category.id=:category")
	Optional<CreateTicketEntity> getByTicketNumberAndLicenceNumberAndShopCodeAndCategory(
			@Param("ticketNumber") String ticnumber, @Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode, @Param("category") Long category);

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and category.id=:category")
	Integer getCountincident(@Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and category.id=:category")
	Integer getCountBySAdminIncident(@Param("ticketStatusId") Long ticketStatusId, @Param("category") Long category);

//	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and a.category.id=:category")
//	Integer getCountByStatusAndCreatedByperIncident(@Param("ticketStatusId") Long ticketStatusId, @Param("createdBy") Long createdBy,@Param("category") Long category);

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '07' and category.id=:category")
	Integer getCountsep(@Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2023' and month(created_date) = '07' and a.category.id=:category")
	Integer getCountByStatusAndCategory(@Param("ticketStatusId") Long ticketStatusId, @Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2022' and month(created_date) = '09'and a.category.id=:category")
	Integer getCountByFebHelpdeskCategory(@Param("ticketStatusId") Long ticketStatusId,
			@Param("category") Long category);

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '08' and category.id=:category")
	Integer getCountoct(@Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and  year(created_date) = '2023' and month(created_date) = '08' and category.id=:category")
	Integer getCountByStatusAndOctCate(@Param("ticketStatusId") Long ticketStatusId, @Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2022' and month(created_date) = '10' and category.id=:category")
	Integer getCountByStatusAndHelpdeskCate(@Param("ticketStatusId") Long ticketStatusId,
			@Param("category") Long category);

	@Query("select distinct(count(*)) from CreateTicketEntity where year(created_date) = '2023' and month(created_date) = '09' and category.id=:category")
	Integer getCountNov(@Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and year(created_date) = '2023' and month(created_date) = '09' and category.id=:category")
	Integer getCountByStatusAndNovCate(@Param("ticketStatusId") Long ticketStatusId, @Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId  and year(created_date) = '2022' and month(created_date) = '11' and category.id=:category")
	Integer getCountByStatusAndHelpdeskCateNov(@Param("ticketStatusId") Long ticketStatusId,
			@Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.createdBy= :createdBy and year(created_date) = '2022' and category.id=:category")
	Integer getCountByStatusAndCreatedByper(@Param("ticketStatusId") Long ticketStatusId,
			@Param("createdBy") Long createdBy, @Param("category") Long category);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and a.category.id=:category and year(created_date) = '2023'")
	Integer getCountByStatusAndperIncident(@Param("ticketStatusId") Long ticketStatusId,
			@Param("category") Long category);

	@Query("select a from CreateTicketEntity a where (a.licenceNumber =:licenceNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	List<CreateTicketEntity> findAllByIssueFrom1(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

	@Query("SELECT t FROM CreateTicketEntity t where t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByStatusId(@Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusA(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	@Query("select distinct(count(a.id)) from CreateTicketEntity a where a.ticketStatus.id=:ticketStatusId and date(a.createdDate) between :fromDate and :toDate and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode)")
	Integer getCountByStatusAndCreatedDateBetweenAndLicenceNumber(@Param("ticketStatusId") Long ticketStatusId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") ArrayList<String> licnumber, @Param("shopCode") ArrayList<String> shopCode);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen')  and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpen(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective')  and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPending(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported')  and date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosed(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate  order by a.createdDate desc")
	Page<CreateTicketEntity> findAllBy(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Change Request%') and UPPER(a.licenceNumber)=:licenseNumber")
	List<CreateTicketEntity> findByTicketStatusChangeRequest(@Param("licenseNumber") String licenseNumber);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Change Request%') and a.issueFrom.id=:issueFromId order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketChangeRequest(@Param("issueFromId") Long issueFromId);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Change Request%') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketChangeRequest(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen')  and date(a.createdDate) between :fromDate and :toDate and  a.category.id=:category order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpenAndIncidentcategory(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective')  and date(a.createdDate) between :fromDate and :toDate and a.category.id=:category order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPendingAndIncidentcategory(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported')  and date(a.createdDate) between :fromDate and :toDate and a.category.id=:category  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosedAndIncident(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate and  a.category.id=:category  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByIncident(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) and  date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketOpenFTDate(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Reopen','Open','Assigned','Change Request','Need Clarification','Escalated') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) and  date (a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketPendingFTDate(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String unitCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketClosedReCALFTDate(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketReopenFTDate(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select a from CreateTicketEntity a where (a.licenceNumber =:licenceNumber or a.shopCode=:shopCode) and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	List<CreateTicketEntity> findAllByIssueFromFTDate(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen')  and date(a.createdDate) between :fromDate and :toDate and a.category.id=:category and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpenAndIncidentAndLCTNSPC(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective')  and date(a.createdDate) between :fromDate and :toDate and a.category.id=:category and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPendingAndIncidentAndTNCNSPCO(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported')  and date(a.createdDate) between :fromDate and :toDate and a.category.id=:category and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosedAndIncidentAndTNCNSPCO(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate and  a.category.id=:category and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByIncidentAndLNSCTN(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("category") Long category, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen') order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpenGet(Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective')  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPendingGet(Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported')   order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosedGet(Pageable pageable);

	@Query("select a from CreateTicketEntity a order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByGet(Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where  a.districtCode in :districtCode or date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCode(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate  and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketCommon(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.districtCode in :districtCode and  date(a.createdDate) between :fromDate and :toDate  and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDistrictCommon(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.districtCode in :districtCode and  date(a.createdDate) between :fromDate and :toDate  and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) and a.unitCode=:unitCode order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDistrictCommonUnitName(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, @Param("unitCode") String unitCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.districtCode in :districtCode and  date(a.createdDate) between :fromDate and :toDate and a.unitCode=:unitCode order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDistrictUnitName(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("unitCode") String unitCode,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate and a.unitCode=:unitCode order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketUnitName(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("unitCode") String unitCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Assigned') and (a.licenceNumber=:licenceNumber or a.shopCode=:shopCode) and  date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	List<CreateTicketEntity> findByTicketAssigned(@Param("licenceNumber") String licenceNumber,
			@Param("shopCode") String unitCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

//	@Query(value="SELECT MONTH(t.created_date) As month,COUNT(t.ticket_number) AS 'TotalRaisedTicketsINYEAR2023',COUNT(CASE WHEN  t.`sla_id` > 12 THEN t.ticket_number END) AS 'Below12HoursTickets',COUNT(CASE WHEN  t.`sla_id` < 12 THEN t.ticket_number END) AS 'Above12HoursTickets'FROM ticket t JOIN `sla_master` ts ON ts.id=t.`sla_id` JOIN help_desk_ticket_sub_category tsub ON tsub.id=t.subcategory_id WHERE date(t.created_date) between :fromDate and :toDate AND (t.subcategory_id IN(24)) GROUP BY MONTH(t.created_date)",nativeQuery=true)
//	public List<ResolutionDTO> getCountByCreatedDateBetweenPos(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
//
//	@Query(value="SELECT MONTH(t.created_date) As month,COUNT(t.ticket_number) AS 'TotalRaisedTicketsINYEAR2023',COUNT(CASE WHEN  t.`sla_id` > 24 THEN t.ticket_number END) AS 'Below12HoursTickets',COUNT(CASE WHEN  t.`sla_id` < 24 THEN t.ticket_number END) AS 'Above12HoursTickets'FROM ticket t JOIN `sla_master` ts ON ts.id=t.`sla_id` JOIN help_desk_ticket_sub_category tsub ON tsub.id=t.subcategory_id WHERE date(t.created_date) between :fromDate and :toDate AND (t.subcategory_id IN(24)) GROUP BY MONTH(t.created_date)",nativeQuery=true)
//	public List<ResolutionDTO> getCountByCreatedDateBetweenPos24(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);

	@Query(value = "SELECT MONTH(t.created_date) As month, COUNT(t.ticket_number) AS 'TotalRaisedTicketsINYEAR2023', COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) <= 43200 and TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) <= 43200 THEN t.ticket_number END) AS 'Below12HoursTickets', COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 43200 or TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) > 43200 THEN t.ticket_number END) AS 'Above12HoursTickets'  FROM ticket t  JOIN `sla_master` sm ON sm.id=t.`sla_id`   JOIN test_upe_helpdesk_grievance_22_02_2022.ticket_status_help ts ON ts.id=t.ticket_status_id  JOIN help_desk_ticket_sub_category tsub ON tsub.id=t.subcategory_id  WHERE date(t.created_date)  between :fromDate and :toDate AND (t.subcategory_id IN(24,28,30))  GROUP BY MONTH(t.created_date)", nativeQuery = true)
	public List<ResolutionDTO> getCountByCreatedDateBetweenPos(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query(value = "SELECT MONTH(t.created_date) As month, COUNT(t.ticket_number) AS 'TotalRaisedTicketsINYEAR2023', COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) <= 86400 and TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) <= 86400 THEN t.ticket_number END) AS 'Below24HoursTickets', COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 86400 or TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) > 86400 THEN t.ticket_number END) AS 'Above24HoursTickets'  FROM ticket t  JOIN `sla_master` sm ON sm.id=t.`sla_id`   JOIN test_upe_helpdesk_grievance_22_02_2022.ticket_status_help ts ON ts.id=t.ticket_status_id  JOIN help_desk_ticket_sub_category tsub ON tsub.id=t.subcategory_id  WHERE date(t.created_date)  between :fromDate and :toDate AND (t.subcategory_id IN(24,23,22,29,114))  GROUP BY MONTH(t.created_date)", nativeQuery = true)
	public List<Resolution24hrDTO> getCountByCreatedDateBetweenPos24(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query(value = "SELECT DATE(t.created_date) AS created_date, " + "COUNT(*) AS ticket_count, "
			+ "CONCAT(FLOOR(TIMESTAMPDIFF(SECOND, MIN(t.created_date), "
			+ "CASE WHEN t.ticket_status_id IN ('56', '59') THEN MAX(t.modified_date) ELSE NOW() END) / 3600), ' hours ') AS down_time "
			+ "FROM ticket t " + "WHERE search_option = 'tollfreenumber' "
			+ "AND DATE(t.created_date) BETWEEN :fromDate AND :toDate " + "GROUP BY DATE(t.created_date) "
			+ "ORDER BY created_date DESC", nativeQuery = true)
	List<InboundCallsTotalOperatinghrsDTO> getDowntimeHrs(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query(value = "SELECT MONTH(t.created_date) AS MONTH, COUNT(t.ticket_number) AS Total_Tickets_Raised, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) <= 3600 and TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) <= 3600 THEN t.ticket_number END) AS Tickets_Closed_Inprogress_Within_SLA, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 3600 or TIMESTAMPDIFF(SECOND, t.created_date,now()) > 3600 THEN t.ticket_number END) AS Tickets_Breached_SLA FROM ticket t JOIN sla_master sm ON sm.id = t.sla_id JOIN ticket_status_help ts ON ts.id = t.ticket_status_id JOIN help_desk_ticket_sub_category tsub ON tsub.id = t.subcategory_id WHERE DATE(t.created_date) BETWEEN :fromDate AND :toDate AND t.subcategory_id = 115 GROUP BY MONTH(t.created_date)", nativeQuery = true)
	public List<SecuritymanagementDTO> getCountByCreatedDateBetweensecuritymanagementPos(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpenNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.ticketstatusname in ('Assigned') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) and date(a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketAssignedNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Open%') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) and  date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketOpenFTDateNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.ticketstatusname in ('Pending','Reopen','Open','Assigned','Change Request','Need Clarification','Escalated') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPendingNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> unitCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.ticketstatusname in ('Closed','Resolved','Call Closer Reported') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosedReCALNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.ticketstatusname in ('Pending','Reopen','Open','Assigned','Change Request','Need Clarification','Escalated') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) and  date (a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketPendingFTDateNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> unitCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where a.ticketStatus.ticketstatusname in ('Closed','Resolved','Call Closer Reported') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketClosedReCALFTDateNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketReopenNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Reopen%') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketReopenFTDateNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			Pageable pageable);

	@Query("select a from CreateTicketEntity a where UPPER(a.ticketStatus.ticketstatusname) like UPPER('%Escalated%') and (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketEscalatedNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByIssueFromNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("shopCode") List<String> shopCode, Pageable pageable);

//	@Query("select a from CreateTicketEntity a where (a.licenceNumber IN :licenceNumber or a.shopCode IN :shopCode) and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
//	Page<CreateTicketEntity> findAllByIssueFromFTDateNew(@Param("licenceNumber") List<String> licenceNumber,
//			@Param("shopCode") List<String> shopCode, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,Pageable pageable);
//	

	@Query(value = "SELECT TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) AS TimestampDiffernce FROM ticket t WHERE t.id=:id", nativeQuery = true)
	Optional<DurationDTO> getByTimestamp(@Param("id") Long id);

	@Query(value = "SELECT CONCAT(FLOOR(TIMESTAMPDIFF(SECOND, MIN(t.created_date), Now()) / 3600), '.',FLOOR(MOD(TIMESTAMPDIFF(SECOND, MIN(t.created_date), Now()), 3600) / 60), '') AS HoursMins FROM ticket t WHERE t.id=:id", nativeQuery = true)
	Optional<DurationDTO> getByTimeHRMins(@Param("id") Long id);

	@Query("select a from CreateTicketEntity a where a.licenceNumber IN :licenceNumber and date (a.createdDate) between :fromDate and :toDate order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByIssueFromFTDateNew(@Param("licenceNumber") List<String> licenceNumber,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

//	@Query(value = "SELECT MONTH(t.created_date) AS MONTH, COUNT(t.ticket_number) AS Total_Tickets_Raised, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) <= 3600 and TIMESTAMPDIFF(SECOND, t.created_date,now()) <= 3600 THEN t.ticket_number END) AS Tickets_Closed_Inprogress_Within_SLA, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 3600 or TIMESTAMPDIFF(SECOND, t.created_date,now()) > 3600 THEN t.ticket_number END) AS Tickets_Breached_SLA FROM ticket t JOIN sla_master sm ON sm.id = t.sla_id JOIN ticket_status_help ts ON ts.id = t.ticket_status_id JOIN help_desk_ticket_sub_category tsub ON tsub.id = t.subcategory_id WHERE DATE(t.created_date) BETWEEN :fromDate AND :toDate  GROUP BY MONTH(t.created_date)", nativeQuery = true)
//	public List<SecuritymanagementDTO> getCountByCreatedDateBetweenrecoverytimeobjectivePos(
//			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
//	

	@Query(value = "SELECT MONTH(t.created_date) AS MONTH, COUNT(t.ticket_number) AS Total_Tickets_Raised, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) <= 3600 and TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) <= 3600 THEN t.ticket_number END) AS Tickets_Closed_Inprogress_Within_SLA, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 3600 or TIMESTAMPDIFF(SECOND, t.created_date,t.modified_date) > 3600 THEN t.ticket_number END) AS Tickets_Breached_SLA FROM ticket t  JOIN sla_master sm ON sm.id = t.sla_id JOIN ticket_status_help ts ON ts.id = t.ticket_status_id WHERE DATE(t.created_date) BETWEEN :fromDate AND :toDate and t.search_option=:search_option GROUP BY MONTH(t.created_date)", nativeQuery = true)
	public List<SecuritymanagementDTO> getCountByCreatedDateBetweenrecoverytimeobjectivePos(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("search_option") String rto);

//	@Query(value = "SELECT MONTH(t.created_date) AS MONTH, COUNT(t.ticket_number) AS Total_Tickets_Raised, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) <= 900 and TIMESTAMPDIFF(SECOND, t.created_date,now()) <= 900 THEN t.ticket_number END) AS Tickets_Closed_Inprogress_Within_SLA, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 900 or TIMESTAMPDIFF(SECOND, t.created_date,now()) > 900 THEN t.ticket_number END) AS Tickets_Breached_SLA FROM ticket t JOIN sla_master sm ON sm.id = t.sla_id JOIN ticket_status_help ts ON ts.id = t.ticket_status_id JOIN help_desk_ticket_sub_category tsub ON tsub.id = t.subcategory_id WHERE DATE(t.created_date) BETWEEN :fromDate AND :toDate AND t.subcategory_id = 115 GROUP BY MONTH(t.created_date)", nativeQuery = true)
//	public List<SecuritymanagementDTO> getCountByCreatedDateBetweenrecoverypointobjectivePos(
//			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
//	

	@Query(value = "SELECT MONTH(t.created_date) AS MONTH, COUNT(t.ticket_number) AS Total_Tickets_Raised, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) <= 900 and TIMESTAMPDIFF(SECOND, t.created_date,now()) <= 900 THEN t.ticket_number END) AS Tickets_Closed_Inprogress_Within_SLA, COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, t.created_date, t.modified_date) > 900 or TIMESTAMPDIFF(SECOND, t.created_date,now()) > 900 THEN t.ticket_number END) AS Tickets_Breached_SLA FROM ticket t JOIN sla_master sm ON sm.id = t.sla_id JOIN ticket_status_help ts ON ts.id = t.ticket_status_id WHERE DATE(t.created_date) BETWEEN :fromDate AND :toDate AND t.search_option=:search_option GROUP BY MONTH(t.created_date)", nativeQuery = true)
	public List<SecuritymanagementDTO> getCountByCreatedDateBetweenrecoverypointobjectivePos(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("search_option") String rtpnumber);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.ticketStatus.id in :ticketStatusId and t.category.id=:category")
	Page<CreateTicketEntity> getByCreatedDateAndStatusAndCategoryStage(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId,
			@Param("category") Long category, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate and  (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByCommon(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  date(a.createdDate) between :fromDate and :toDate and  (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) and (a.districtCode in :districtCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByCommonDistricode(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, @Param("districtCode") List<String> districtCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  (a.ticketStatus.ticketstatusname) in ('Open','Reopen') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCodeAndcommonserach(
			@Param("districtCode") List<String> districtCode, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen') and  date(a.createdDate) between :fromDate and :toDate and  (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByTicketCommonSearch(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Open','Reopen') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate   order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCodelist(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	//

	@Query("select a from CreateTicketEntity a where  (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCodeAndcommonserachClosed(
			@Param("districtCode") List<String> districtCode, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate   order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCodelistClosed(@Param("districtCode") List<String> districtCode,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Closed','Resolved','Call Closer Reported') and  date(a.createdDate) between :fromDate and :toDate and  (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByTicketCommonSearchClosed(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where  (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate and (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode) order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCodeAndcommonserachPending(
			@Param("districtCode") List<String> districtCode, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective') and (a.districtCode in :districtCode) and date(a.createdDate) between :fromDate and :toDate   order by a.modifiedDate desc")
	Page<CreateTicketEntity> findByTicketDateAndDistrictCodelistPending(
			@Param("districtCode") List<String> districtCode, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, Pageable pageable);

	@Query("select a from CreateTicketEntity a where (a.ticketStatus.ticketstatusname) in ('Pending','Assigned','Escalated','Change Request','Need Clarification','Need Clarification from Dept.','Escalate to Service Provider','Escalate to  Security Management','Recovery Time Objective','Recovery Point Objective') and  date(a.createdDate) between :fromDate and :toDate and  (a.licenceNumber=:licenceNumber or a.ticketNumber=:ticketNumber or a.shopCode=:shopCode)  order by a.modifiedDate desc")
	Page<CreateTicketEntity> findAllByTicketCommonSearchPending(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,

			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query(value = "SELECT\r\n" + "	COUNT(t.id) AS Total_Tickets,\r\n"
			+ "	SUM(CASE WHEN tsh.ticketstatusname IN ('Open', 'Reopen') THEN 1 ELSE 0 END) AS Open_Total,\r\n"
			+ "	SUM(CASE WHEN tsh.ticketstatusname IN ('Pending', 'Escalated', 'Assigned', 'Need Clarification', 'Change Request',\r\n"
			+ "                'Need Clarification from Dept.', 'Escalate to Service Provider',\r\n"
			+ "                'Escalate to  Security Management', 'Recovery Point Objective', 'Escalate to Production Support') THEN 1 ELSE 0 END) AS Pending_Total,\r\n"
			+ "	SUM(CASE WHEN tsh.ticketstatusname IN ('Call Closer Reported', 'Closed', 'Resolved') THEN 1 ELSE 0 END) AS Closed_Total,\r\n"
			+ "	(SUM(CASE WHEN tsh.ticketstatusname IN ('Open', 'Reopen') THEN 1 ELSE 0 END) / COUNT(t.id) * 100) AS Open_Percentage,\r\n"
			+ "	(SUM(CASE WHEN tsh.ticketstatusname IN ('Pending', 'Escalated', 'Assigned', 'Need Clarification', 'Change Request',\r\n"
			+ "                'Need Clarification from Dept.', 'Escalate to Service Provider',\r\n"
			+ "                'Escalate to  Security Management', 'Recovery Point Objective', 'Escalate to Production Support') THEN 1 ELSE 0 END) / COUNT(t.id) * 100) AS Pending_Percentage,\r\n"
			+ "	(SUM(CASE WHEN tsh.ticketstatusname IN ('Call Closer Reported', 'Closed', 'Resolved') THEN 1 ELSE 0 END) / COUNT(t.id) * 100) AS Closed_Percentage\r\n"
			+ "FROM\r\n" + "	ticket t\r\n" + "JOIN \r\n" + "    ticket_status_help tsh ON\r\n"
			+ "	tsh.id = t.ticket_status_id\r\n" + "WHERE\r\n"
			+ "	DATE(t.created_date) BETWEEN :fromDate AND :toDate", nativeQuery = true)
	List<DashboardCount> getCountByFromDataAndToDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT * FROM ( (\r\n" + "	SELECT\r\n"
			+ "		DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 0 MONTH), '%M') AS month_name\r\n" + "UNION ALL\r\n"
			+ "	SELECT\r\n" + "		DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%M')\r\n" + "UNION ALL\r\n"
			+ "	SELECT\r\n" + "		DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%M')) AS a\r\n"
			+ "LEFT JOIN ( SELECT\r\n" + "		DATE_FORMAT(t.created_date, '%M') AS Month,\r\n"
			+ "		COUNT(t.id) AS Total_Tickets,\r\n"
			+ "		SUM(CASE WHEN tsh.ticketstatusname IN ('Open', 'Reopen') THEN 1 ELSE 0 END) AS Open_Total,\r\n"
			+ "		SUM(CASE WHEN tsh.ticketstatusname IN ('Pending', 'Escalated', 'Assigned', 'Need Clarification', 'Change Request',\r\n"
			+ "                'Need Clarification from Dept.', 'Escalate to Service Provider',\r\n"
			+ "                'Escalate to  Security Management', 'Recovery Point Objective', 'Escalate to Production Support') THEN 1 ELSE 0 END) AS Pending_Total,\r\n"
			+ "		SUM(CASE WHEN tsh.ticketstatusname IN ('Call Closer Reported', 'Closed', 'Resolved') THEN 1 ELSE 0 END) AS Closed_Total,\r\n"
			+ "		(SUM(CASE WHEN tsh.ticketstatusname IN ('Open', 'Reopen') THEN 1 ELSE 0 END) / CAST(COUNT(t.id) AS DECIMAL) * 100) AS Open_Percentage,\r\n"
			+ "		(SUM(CASE WHEN tsh.ticketstatusname IN ('Pending', 'Escalated', 'Assigned', 'Need Clarification', 'Change Request',\r\n"
			+ "                'Need Clarification from Dept.', 'Escalate to Service Provider',\r\n"
			+ "                'Escalate to  Security Management', 'Recovery Point Objective', 'Escalate to Production Support') THEN 1 ELSE 0 END) / CAST(COUNT(t.id) AS DECIMAL) * 100) AS Pending_Percentage,\r\n"
			+ "		(SUM(CASE WHEN tsh.ticketstatusname IN ('Call Closer Reported', 'Closed', 'Resolved') THEN 1 ELSE 0 END) / CAST(COUNT(t.id) AS DECIMAL) * 100) AS Closed_Percentage\r\n"
			+ "	FROM\r\n" + "		ticket t\r\n" + "	JOIN ticket_status_help tsh ON\r\n"
			+ "		tsh.id = t.ticket_status_id\r\n" + "	WHERE\r\n"
			+ "		t.created_date >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m-01')\r\n"
			+ "		AND t.created_date <= LAST_DAY(DATE_SUB(CURDATE(), INTERVAL 0 MONTH))\r\n" + "	GROUP BY\r\n"
			+ "		DATE_FORMAT(t.created_date, '%M')\r\n" + "    ) AS b ON\r\n"
			+ "	b.Month = month_name )", nativeQuery = true)
	List<DashboardCount> getByCountTicketDashboardByMonth();

	@Query(value = "select * from ticket t order by id DESC limit 1", nativeQuery = true)
	Optional<CreateTicketEntity> getByTicketNumber();

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAll(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityType")
	Page<CreateTicketEntity> getByCreatedDateAndEntityTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("entityType") String entityType, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityType and t.category.id=:category")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndEntityTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, @Param("entityType") String entityType,
			Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode) and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndStatusAndAll(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityType and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndStatusAndEntityType(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId,
			@Param("entityType") String entityType, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode) and t.entityTypeId=:entityType")
	Page<CreateTicketEntity> getByCreatedDateAndAllAndEntityType(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode,
			@Param("entityType") String entityType, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityType and t.category.id=:category and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusAndEntityTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("entityType") String entityType,
			Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityType and t.category.id=:category and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndAllAndEntityTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, @Param("entityType") String entityType, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityType and t.ticketStatus.id in :ticketStatusId and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndStatusAndAllAndEntityTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, @Param("entityType") String entityType, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate and t.category.id=:category and t.entityTypeId=:entityType and t.ticketStatus.id in :ticketStatusId and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusAndAllAndEntityTypeId(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode,
			@Param("entityType") String entityType, Pageable pageable);

	// add entitytype as NonNull status is Null
	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)and t.entityTypeId=:entityTypeId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryLicEntity(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, @Param("entityTypeId") String entityTypeId, Pageable pageable);

	// add entitytype as Null status is Null
	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryLicEntityandStatusNull(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("licenceNumber") String licenceNumber, @Param("ticketNumber") String ticketNumber,
			@Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryEntityNu(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.entityTypeId=:entityTypeId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryEntityNotNull(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category, @Param("entityTypeId") String entityTypeId,
			Pageable pageable);

	// add entity as NonNull
	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)and t.entityTypeId=:entityTypeId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusTicEntity(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode,
			@Param("entityTypeId") String entityTypeId, Pageable pageable);

	// add entity as only Null
	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusTicEntityNull(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and  t.category.id=:category and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusEn(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("category") Long category,
			@Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate  and t.entityTypeId=:entityTypeId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusDate(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("entityTypeId") String entityTypeId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate   and (t.licenceNumber=:licenceNumber or t.ticketNumber=:ticketNumber or t.shopCode=:shopCode)")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatusSearch(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("licenceNumber") String licenceNumber,
			@Param("ticketNumber") String ticketNumber, @Param("shopCode") String shopCode, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t where date(t.createdDate) between :fromDate and :toDate and t.ticketStatus.id in :ticketStatusId")
	Page<CreateTicketEntity> getByCreatedDateAndCategoryAndStatus(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("ticketStatusId") List<Long> ticketStatusId, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t \r\n" + "WHERE \r\n"
			+ "(DATE(t.createdDate) BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL))\r\n"
			+ "AND (t.category = COALESCE(NULLIF(:category, ''), NULL) OR :category IS NULL) \r\n"
			+ "AND ((t.licenceNumber = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL) \r\n"
			+ "OR (t.ticketNumber = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL) \r\n"
			+ "OR (t.shopCode  = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL)) \r\n"
			+ "AND (t.entityTypeId = COALESCE(NULLIF(:entityTypeId, ''), NULL) OR :entityTypeId IS NULL) \r\n"
			+ "AND (t.ticketStatus = COALESCE(NULLIF(:ticketStatus, ''), NULL) OR :ticketStatus IS NULL)\r\n"
			+ "AND (t.issueTypeSH = COALESCE(NULLIF(:issueTypeSH, ''), NULL) OR :issueTypeSH IS NULL)\r\n")
	Page<CreateTicketEntity> findTickets(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("category") Long category, @Param("commonSearch") String commonSearch,
			@Param("entityTypeId") String entityTypeId, @Param("ticketStatus") Long ticketStatus,
			@Param("issueTypeSH") String issueTypeSH, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t  \r\n"
			+ "WHERE (DATE(t.createdDate) BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL)) \r\n"
			+ "    AND (t.districtCode = (COALESCE(NULLIF(:districtId, ''), NULL)) OR :districtId IS NULL)\r\n"
			+ "    AND ((t.licenceNumber = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL) \r\n"
			+ "        OR (t.ticketNumber = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL) \r\n"
			+ "        OR (t.shopCode = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL)) \r\n"
			+ "    AND (t.ticketStatus.id in :ticketstatus) \r\n"
			+ "    AND (t.entityTypeId = COALESCE(NULLIF(:unitName, ''), NULL) OR :unitName IS NULL) \r\n")
	Page<CreateTicketEntity> searchdistrictlist(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("districtId") String districtId, @Param("ticketstatus") List<Long> ticketstatus,
			@Param("commonSearch") String commonSearch, @Param("unitName") String unitName, Pageable pageable);

	@Query("SELECT t FROM CreateTicketEntity t "
			+ "WHERE (FUNCTION('DATE', t.createdDate) BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL)) "
			+ "AND (t.category = COALESCE(NULLIF(:incidentcategory, ''), NULL) OR :incidentcategory IS NULL) "
			+ "AND ((t.licenceNumber = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL) "
			+ "OR (t.ticketNumber = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL) "
			+ "OR (t.shopCode = COALESCE(NULLIF(:commonSearch, ''), NULL) OR :commonSearch IS NULL)) "
			+ "AND (t.entityTypeId = COALESCE(NULLIF(:entityTypeId, ''), NULL) OR :entityTypeId IS NULL) "
			+ "AND (t.ticketStatus = COALESCE(NULLIF(:ticketStatus, ''), NULL) OR :ticketStatus IS NULL)"
			+ "ORDER BY t.modifiedDate DESC")
	Page<CreateTicketEntity> findIncidentTickets(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("incidentcategory") Long incidentcategory, @Param("commonSearch") String commonSearch,
			@Param("entityTypeId") String entityTypeId, @Param("ticketStatus") Long ticketStatus, Pageable pageable);

}
