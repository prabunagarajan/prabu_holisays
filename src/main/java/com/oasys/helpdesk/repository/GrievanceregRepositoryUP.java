package com.oasys.helpdesk.repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//gitlab.oasys.co/up_excise/api/helpdeskservice.git
import com.oasys.helpdesk.entity.GrievanceregisterEntity;

@Repository
public interface GrievanceregRepositoryUP extends JpaRepository<GrievanceregisterEntity, Long>  {
	
	Optional<GrievanceregisterEntity> findByLiceneceNumberIgnoreCase(String licencenumber);

	Optional<GrievanceregisterEntity> findByGrievanceidIgnoreCase(String code);
	
	List<GrievanceregisterEntity> findAllByOrderByModifiedDateDesc();
	
	@Query(value ="select a.* from grievance_register a where a.status = true order by a.modified_date desc", nativeQuery=true)
	List<GrievanceregisterEntity> findAllByStatusOrderByModifiedDateDesc();
	
	@Query("SELECT a  FROM GrievanceregisterEntity a where a.typeofuser =:typeofuser and a.nameinfo=:name_info order by a.modifiedDate desc")
	List<GrievanceregisterEntity> getByTypeofuserAndName_info(@Param("typeofuser") String typeofuser,@Param("name_info") String nameinfo);
	

	@Query("SELECT a  FROM GrievanceregisterEntity a where a.phone_number =:phone_number order by a.modifiedDate desc")
	List<GrievanceregisterEntity> getByPhone_number(@Param("phone_number") String phonenumber);
	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  (a.notes) =:notes and a.id !=:id")
	Optional<GrievanceregisterEntity> findByNotesIgnoreCaseNotInId(@Param("notes") String notes, @Param("id") Long id);
	

	@Query("SELECT a FROM GrievanceregisterEntity a where  (a.issuedetails) =:issue_details and a.id !=:id")
	Optional<GrievanceregisterEntity> findByIssuedetailsIgnoreCaseNotInId(@Param("issue_details") String issuedetails, @Param("id") Long id);
	
	
	
//	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and a.createdBy= :createdBy and Date(createdDate)=:createdDate and a.nameinfo=:name_info and a.typeofuser =:typeofuser")
//	Integer getCountByStatusAndCreatedByAndCreatedDateAndUserid(@Param("grievance_tc_status") Long ticketStatusId, @Param("createdBy") Long createdBy, @Param("createdDate") Date createdDate,@Param("name_info") String nameinfo,@Param("typeofuser") String typeofuser);
//		
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus=:grievance_tc_status and a.nameinfo=:name_info and a.typeofuser =:typeofuser and date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedDate(@Param("grievance_tc_status") String ticketStatusId,@Param("name_info") String nameinfo,@Param("typeofuser") String typeofuser,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);

	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus=:grievance_tc_status and a.phone_number=:phone_number and  date(a.createdDate) between :fromDate and :toDate  order by a.modifiedDate desc")
	Integer getCountByStatusAndCreatedDateAndPhone_number(@Param("grievance_tc_status") String ticketStatusId,@Param("phone_number") String phone_number,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);

	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus=:grievance_tc_status and date(a.createdDate) between :fromDate and :toDate and  a.assigntoid=:assigntoid")
	Integer getCountByStatusAndCreatedByAndCreatedDateAndAssignTO(@Param("grievance_tc_status") String ticketStatusId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("assigntoid") String assignto_Id);
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus=:grievance_tc_status and date(a.createdDate) between :fromDate and :toDate and  a.assigngroup=:assigngroup")
	Integer getCountByStatusAndCreatedByAndCreatedDateAndAssigngroup(@Param("grievance_tc_status") String ticketStatusId, @Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("assigngroup") String assignto_Id);
	
	
	
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and a.createdBy= :createdBy")
	Integer getCountByStatusAndCreatedByper(@Param("grievance_tc_status") Long ticketStatusId, @Param("createdBy") Long createdBy);
    
	
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '02'")
	Integer getCountByStatusAndCreatedBy(@Param("grievance_tc_status") Long ticketStatusId, @Param("createdBy") Long createdBy);
    
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '04'")
	Integer getCountByStatusAndCreatedByAp(@Param("grievance_tc_status") Long ticketStatusId, @Param("createdBy") Long createdBy);
    
	
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and a.createdBy= :createdBy and year(created_date) = '2022' and month(created_date) = '03'")
	Integer getCountByStatusAndCreatedBy1(@Param("grievance_tc_status") Long ticketStatusId, @Param("createdBy") Long createdBy);

	
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022' and month(created_date) = '03'")
	Integer getCount();
	
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022' and month(created_date) = '02'")
	Integer getCountfeb();
	
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022' and month(created_date) = '04'")
	Integer getCountap();
	
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022'")
	Integer getCountall();


	
//	@Query("SELECT a FROM GrievanceregisterEntity a where  a.assigntoid =:assigntoid and a.typeofuser =:typeofuser")
//	Page<GrievanceregisterEntity> getByAssigntoIdAndTypeofuser(@Param("assigntoid") String assignto_Id, @Param("typeofuser")  String typeofuser, Pageable pageable);

	
//	@Query("SELECT a FROM GrievanceregisterEntity a where  a.assigntoid =:assigntoid or a.assigngroup =:assigngroup  and a.typeofuser =:typeofuser")
//	Page<GrievanceregisterEntity> getByAssigntoIdAndTypeofuser(@Param("assigntoid") String assignto_Id,@Param("assigngroup") String assigngroup, @Param("typeofuser")  String typeofuser, Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.assigntoid =:assigntoid  and a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getByAssigntoIdAndTypeofuser(@Param("assigntoid") String assignto_Id, @Param("typeofuser")  String typeofuser, Pageable pageable);


	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where   a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getByTypeofuser(@Param("typeofuser")  String typeofuser, Pageable pageable);
	

//	@Query("SELECT a FROM GrievanceregisterEntity a where  a.assigntoid =:assigntoid")
//	Page<GrievanceregisterEntity> getByAssigntoId(@Param("assigntoid") String assignto_Id, Pageable pageable);

//	@Query("SELECT a FROM GrievanceregisterEntity a where  a.assigntoid =:assigntoid or a.assigngroup =:assigngroup")
//	Page<GrievanceregisterEntity> getByAssigntoId(@Param("assigntoid") String assignto_Id,@Param("assigngroup") String assingroup_Id, Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.assigntoid =:assigntoid and a.typeofuser =:typeofuser and a.grievancetcstatus =:grievancetcstatus")
	Page<GrievanceregisterEntity> getByAssigntoIdTypeofuserAndGrievancetcStatus(@Param("assigntoid") String assignto_Id, @Param("typeofuser")  String typeofuser,@Param("grievancetcstatus")  String grievancetcstatus, Pageable pageable);


	

//	@Query("SELECT a FROM GrievanceregisterEntity a where a.grievanceid =:grievanceid and a.userid=:userid ")
//	List<GrievanceregisterEntity> getByGrievanceidAndUserid(@Param("grievanceid") String grievanceid,@Param("userid") String userid);
//	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber =:referticnumber and a.typeofuser=:typeofuser ")
	List<GrievanceregisterEntity> getByGrievanceidAndTypeofuser(@Param("referticnumber") String grievanceid,@Param("typeofuser") String typeofuser);
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber =:referticnumber")
	List<GrievanceregisterEntity> getByGrievanceid(@Param("referticnumber") String grievanceid);
	
	
	
	
//	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and a.createdBy= :createdBy and a.userid=:userid  ")
//	Integer getCountByStatusAndCreatedByperinspect(@Param("grievance_tc_status") Long ticketStatusId, @Param("createdBy") Long createdBy,@Param("userid") String userid);
    
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and  a.assigntoid=:assignToId")
	Integer getCountByStatusAndCreatedByperinspect(@Param("grievance_tc_status") Long ticketStatusId,@Param("assignToId") String userid);
    
	
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity a where a.assigntoid=:assignToId ")
	Integer getCountinspect(@Param("assignToId") String userid);
	
	
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022' and month(created_date) = '03' and assigntoid=:assignToId ")
	Integer getCountinspectmonth(@Param("assignToId") String userid);
	
	
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and year(created_date) = '2022' and month(created_date) = '03' and assigntoid=:assignToId ")
	Integer getCountByStatusAndCreatedByinspectmonth(@Param("grievance_tc_status") Long ticketStatusId, @Param("assignToId") String userid);

	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022' and month(created_date) = '02' and assigntoid=:assignToId ")
	Integer getCountfebinspectmonth(@Param("assignToId") String userid);
	
	@Query("select distinct(count(*)) from GrievanceregisterEntity where year(created_date) = '2022' and month(created_date) = '04' and assigntoid=:assignToId ")
	Integer getCountapinspectmonth(@Param("assignToId") String userid);
	
	
	
	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and year(created_date) = '2022' and month(created_date) = '02' and assigntoid=:assignToId ")
	Integer getCountByStatusAndCreatedByinsfeb(@Param("grievance_tc_status") Long ticketStatusId,@Param("assignToId") String userid);

	@Query("select distinct(count(a.id)) from GrievanceregisterEntity a where a.grievancetcstatus.id=:grievance_tc_status and year(created_date) = '2022' and month(created_date) = '04' and assigntoid=:assignToId ")
	Integer getCountByStatusAndCreatedByinsap(@Param("grievance_tc_status") Long ticketStatusId,@Param("assignToId") String userid);

	
	

	@Query("select a from GrievanceregisterEntity a where date(a.createdDate) between :fromDate and :toDate and  a.assigntoid=:assigntoid")
	List<GrievanceregisterEntity> getCreatedByAndCreatedDateAndAssignTO(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("assigntoid") String assignto_Id);

	
	@Query("select a from GrievanceregisterEntity a where date(a.createdDate) between :fromDate and :toDate and  a.assigngroup=:assigngroup")
	List<GrievanceregisterEntity> getCreatedByAndCreatedDateAndAssigngroup(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("assigngroup") String assignto_Id);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.issuefrom=:issuefrom and a.typeofuser =:typeofuser ")
	Page<GrievanceregisterEntity> getByCategoryIdAndIssuefromAndTypeofuser(@Param("categoryId")  Long categoryNameO, @Param("issuefrom") String issuefrom,
			@Param("typeofuser") String typeofuser, Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom ")
	Page<GrievanceregisterEntity> getByIssuefrom( @Param("issuefrom") String issuefrom, Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.referticnumber=:referticnumber  and a.status =:status and Date(createdDate)=:createdDate")
	Page<GrievanceregisterEntity> getByCategoryIdIssuefromAndTypeofuserStatus(@Param("categoryId") Long categoryNameO,
			 @Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("referticnumber") String referticNumber, @Param("status") Boolean status, @Param("createdDate") Date createdDate, Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.grievancetcstatus =:grievancetcstatus")
	Page<GrievanceregisterEntity> getByGrievancetcstatus(@Param("grievancetcstatus") String grievancetcstatus,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.referticnumber=:referticnumber")
	Page<GrievanceregisterEntity> getByCategoryIdIssuefromAndTypeofuser(@Param("categoryId") Long categoryNameO,
			@Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("referticnumber") String referticnumber, Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.status =:status and Date(createdDate)=:createdDate")
	Page<GrievanceregisterEntity> getByCategoryIdIssuefromAndStatus(@Param("categoryId") Long categoryNameO,
			 @Param("issuefrom") String issuefrom,@Param("typeofuser")  String typeofuser, @Param("status") Boolean status,@Param("createdDate") Date createdDate,  Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and Date(createdDate)=:createdDate")
	Page<GrievanceregisterEntity> getByCategoryIdIssuefromAndType(@Param("categoryId") Long categoryId,  @Param("issuefrom") String issuefrom, @Param("typeofuser") String typeOfUser, @Param("createdDate") Date createdDate, Pageable pageable); 
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.referticnumber=:referticnumber and Date(createdDate)=:createdDate")
	Page<GrievanceregisterEntity> getByCategoryIdIssuefromAndreferticnumber(@Param("categoryId") Long categoryId,  @Param("issuefrom") String issuefrom, @Param("typeofuser") String typeOfUser,@Param("referticnumber") String referticnumber, @Param("createdDate") Date createdDate, Pageable pageable); 
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.referticnumber=:referticnumber")
	Page<GrievanceregisterEntity> getByRefert(@Param("referticnumber") String referticnumber, Pageable pageable); 
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.assigntoid=:assigntoid and a.flag =:flag")
	List<GrievanceregisterEntity> getByFlagAssignto(@Param("assigntoid") String assigntoId,@Param("flag") Boolean flag);
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and  a.phone_number=:phone_number and a.nameinfo=:nameinfo and Date(createdDate)=:createdDate and a.status =:status")
	Page<GrievanceregisterEntity> getByReferticnumberPhoneNumberAndNameinfoAndCreateddate(@Param("referticnumber")String grievanceid, @Param("phone_number") String phonenumber, @Param("nameinfo") String nameinfo,@Param("createdDate") Date finalDate, @Param("status") Boolean status,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and  a.phone_number=:phone_number and a.nameinfo=:nameinfo ")
	Page<GrievanceregisterEntity> getByReferticnumberPhoneNumberAndNameinfo(@Param("referticnumber")String grievanceid, @Param("phone_number") String phonenumber, @Param("nameinfo") String nameinfo,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber ")
	Page<GrievanceregisterEntity> getByReferticnumber(@Param("referticnumber") String grievanceid,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid and a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getByReferticnumberAndAssigngroupAndTypeofuser(@Param("referticnumber") String grievanceid,@Param("assigngroup") String assignto_Id,@Param("assigntoid") String assigntoId,@Param("typeofuser") String typeOfUser,Pageable pageable);

	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber  and a.nameinfo=:nameinfo ")
	Page<GrievanceregisterEntity> getByReferticnumberAndNameinfo(@Param("referticnumber")String grievanceid, @Param("nameinfo") String nameinfo,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where   a.phone_number=:phone_number")
	Page<GrievanceregisterEntity> getByPhoneNumber(@Param("phone_number") String phonenumber,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where   a.phone_number=:phone_number and Date(createdDate)=:createdDate ")
	Page<GrievanceregisterEntity> getByPhoneNumberAndCreateddate(@Param("phone_number") String phonenumber,@Param("createdDate") Date finalDate,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  Date(createdDate)=:createdDate ")
	Page<GrievanceregisterEntity> getByCreateddate(@Param("createdDate") Date finalDate,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where   a.phone_number=:phone_number and a.nameinfo=:nameinfo ")
	Page<GrievanceregisterEntity> getByPhoneNumberAndNameinfo( @Param("phone_number") String phonenumber, @Param("nameinfo") String nameinfo,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and  a.phone_number=:phone_number")
	Page<GrievanceregisterEntity> getByReferticnumberPhoneNumber(@Param("referticnumber")String grievanceid, @Param("phone_number") String phonenumber,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.nameinfo=:nameinfo and a.status =:status")
	Page<GrievanceregisterEntity> getByNameinfo(@Param("nameinfo") String nameinfo, @Param("status") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.status =:status")
	Page<GrievanceregisterEntity> getByStatus(@Param("status") Boolean status, Pageable pageable);
	
	
	
	@Query(value = "select * from grievance_register t, grievance_sla_master s WHERE t.sla=s.id and t.grievance_tc_status in :status" + 
			" and DATE_ADD(t.created_date,interval s.sla hour) < now() ", nativeQuery=true)
	List<GrievanceregisterEntity> findByFilter(@Param("status") List<String> status);
	
	
	
	@Query("select a from GrievanceregisterEntity a where date(a.createdDate) between :fromDate and :toDate")
	List<GrievanceregisterEntity> getCreatedDate(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.hofficerId =:hofficerId and a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getByHofficerIdAndtypeofuser(@Param("hofficerId") Integer hofficerId, @Param("typeofuser")  String typeofuser,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.hofficerId =:hofficerId ")
	Page<GrievanceregisterEntity> getByHofficerId(@Param("hofficerId") Integer hofficerId,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getBytype(@Param("typeofuser") String typeofuser,Pageable pageable);
	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and  a.issuefrom=:issuefrom and a.categoryId.id =:categoryId and Date(createdDate)=:createdDate and a.grievancetcstatus =:grievancetcstatus and a.priority=:priority")
	Page<GrievanceregisterEntity> getByReferticnumberIssuefromCategoryIdCreateddateStatusAndPriority(@Param("referticnumber")String grievanceid, @Param("issuefrom") String issuefrom, @Param("categoryId") Long categoryNameO,@Param("createdDate") Date finalDate, @Param("grievancetcstatus") String status,@Param("priority") String priority,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.categoryId.id =:categoryId  and a.priority=:priority")
	Page<GrievanceregisterEntity> getByIssuefromCategoryIdAndPriority(@Param("issuefrom") String issuefrom, @Param("categoryId") Long categoryNameO,@Param("priority") String priority,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.categoryId.id =:categoryId and  a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getByIssuefromAndCategoryIdAndTypeofuser(@Param("issuefrom") String issuefrom, @Param("categoryId") Long categoryNameO,@Param("typeofuser") String typeofuser,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.categoryId.id =:categoryId")
	Page<GrievanceregisterEntity> getByIssuefromAndCategoryId(@Param("issuefrom") String issuefrom, @Param("categoryId") Long categoryNameO,Pageable pageable);

	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.priority=:priority")
	Page<GrievanceregisterEntity> getByIssuefromAndPriority(@Param("issuefrom") String issuefrom, @Param("priority") String priority,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom ")
	Page<GrievanceregisterEntity> getByIssuefromc(@Param("issuefrom") String issuefrom,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.priority=:priority")
	Page<GrievanceregisterEntity> getByCategoryIdAndPriority(@Param("categoryId") Long categoryNameO,@Param("priority") String priority,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId ")
	Page<GrievanceregisterEntity> getByCategoryId(@Param("categoryId") Long categoryNameO,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.priority=:priority")
	Page<GrievanceregisterEntity> getByPriority(@Param("priority") String priority,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and Date(createdDate)=:createdDate")
	Page<GrievanceregisterEntity> getByReferticnumberCreateddate(@Param("referticnumber")String grievanceid,@Param("createdDate") Date finalDate,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.grievancetcstatus =:grievancetcstatus and a.typeofuser =:typeofuser")
	Page<GrievanceregisterEntity> getByIssuefromAndGrievancetcstatus(@Param("issuefrom") String issuefrom,@Param("grievancetcstatus") String status,@Param("typeofuser") String typeofuser,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByReferticnumberTypeofuserAndAssigntoid(@Param("referticnumber")String grievanceid,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid ")
	Page<GrievanceregisterEntity> getByReferticnumberAndTypeofuserAndAssigngroup(@Param("referticnumber")String grievanceid,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.typeofuser =:typeofuser and a.grievancetcstatus =:grievancetcstatus and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByStatusTypeofuserAndAssigntoid(@Param("typeofuser") String typeofuser,@Param("grievancetcstatus") String status,@Param("assigntoid") String userid,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.typeofuser =:typeofuser and a.grievancetcstatus =:grievancetcstatus and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByStatusAndTypeofuserAndAssigngroup(@Param("typeofuser") String typeofuser,@Param("grievancetcstatus") String status,@Param("assigngroup") String userid,@Param("assigntoid") String useridq,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByIssuefromTypeofuserAndAssigntoid( @Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid, Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByIssuefromAndTypeofuserAndAssigngroup( @Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assignid, Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByCategoryIdTypeofuserAndAssigntoid(@Param("categoryId") Long categoryNameO,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.categoryId.id =:categoryId and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid ")
	Page<GrievanceregisterEntity> getByCategoryIdAndTypeofuserAndAssigngroup(@Param("categoryId") Long categoryNameO,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,Pageable pageable);

	
	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  Date(createdDate)=:createdDate and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByCreateddateTypeofuserAndAssigntoid(@Param("createdDate") Date finalDate,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid,Pageable pageable);

	
//	@Query("SELECT a FROM GrievanceregisterEntity a where  Date(createdDate)=:createdDate and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid")
//	Page<GrievanceregisterEntity> getByCreateddateAndTypeofuserAndAssigngroup(@Param("createdDate") Date finalDate,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.typeofuser =:typeofuser and  Date(createdDate)=:createdDate and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getTypeofuserAndCreateddateAndAssigngroup(@Param("typeofuser") String typeofuser,@Param("createdDate") Date finalDate,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.grievancetcstatus =:grievancetcstatus and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByGrievancetcstatusAndTypeofuserAndAssigntoid(@Param("grievancetcstatus") String grievancetcstatus,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid,Pageable pageable);
 		 
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.grievancetcstatus =:grievancetcstatus and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByGrievancetcstatusAndTypeofuserAndAssigngroup(@Param("grievancetcstatus") String grievancetcstatus,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,Pageable pageable);
 	
	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByAssigntoid(@Param("assigntoid") String userid,Pageable pageable);
	

	@Query("SELECT a FROM GrievanceregisterEntity a where a.assigngroup=:assigngroup or a.assigntoid=:assigntoid ")
	Page<GrievanceregisterEntity> getByAssigngroup(@Param("assigngroup") String userid,@Param("assigntoid") String userida,Pageable pageable);
	
	
			 
	@Query("SELECT a FROM GrievanceregisterEntity a where   a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByTypeofuserAndAssigntoid(@Param("typeofuser")  String typeofuser,@Param("assigntoid") String userid, Pageable pageable);
	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where   a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByTypeofuserAndAssigngroup(@Param("typeofuser")  String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assignid, Pageable pageable);
	
	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and  a.issuefrom=:issuefrom and a.categoryId.id =:categoryId and Date(createdDate)=:createdDate and a.grievancetcstatus =:grievancetcstatus and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid")
	Page<GrievanceregisterEntity> getByReferticnumberIssuefromCategoryIdCreateddateStatusAndAssigntoid(@Param("referticnumber")String grievanceid, @Param("issuefrom") String issuefrom, @Param("categoryId") Long categoryNameO,@Param("createdDate") Date finalDate, @Param("grievancetcstatus") String status,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid,Pageable pageable);

	@Query("SELECT a FROM GrievanceregisterEntity a where a.referticnumber=:referticnumber and  a.issuefrom=:issuefrom and a.categoryId.id =:categoryId and Date(createdDate)=:createdDate and a.grievancetcstatus =:grievancetcstatus and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid ")
	Page<GrievanceregisterEntity> getByReferticnumberIssuefromCategoryIdCreateddateStatusAndAssigngroup(@Param("referticnumber")String grievanceid, @Param("issuefrom") String issuefrom, @Param("categoryId") Long categoryNameO,@Param("createdDate") Date finalDate, @Param("grievancetcstatus") String status,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where a.grievancetcstatus =:grievancetcstatus and Date(createdDate)=:createdDate")
	Page<GrievanceregisterEntity> getByCreateddateAndGrievancetcstatus(@Param("grievancetcstatus") String grievancetcstatus,@Param("createdDate") Date finalDate,Pageable pageable);

	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.assigntoid=:assigntoid and a.categoryId.id =:categoryId")
	Page<GrievanceregisterEntity> getByIssuefromTypeofuserAndAssigntoidAndCategoryId( @Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("assigntoid") String userid,@Param("categoryId") Long category ,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid  and a.categoryId.id =:categoryId")
	Page<GrievanceregisterEntity> getByIssuefromAndTypeofuserAndAssigngroupAndCategoryId( @Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,@Param("categoryId") Long category ,Pageable pageable);

	
	
	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup and a.categoryId.id =:categoryId")
	Page<GrievanceregisterEntity> getByIssuefromTypeofuserAndAssigngroupAndCategoryId( @Param("issuefrom") String issuefrom,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("categoryId") Long category ,Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.nameinfo=:nameinfo and a.grievancetcstatus =:grievancetcstatus")
	Page<GrievanceregisterEntity> getByIssuefromAndNameinfoAndGrievancetcstatus( @Param("issuefrom") String issuefrom,@Param("nameinfo") String nameinfo,@Param("grievancetcstatus") String grievancetcstatus, Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  a.issuefrom=:issuefrom and a.nameinfo=:nameinfo and a.priority=:priority")
	Page<GrievanceregisterEntity> getByIssuefromAndNameinfoAndpriority( @Param("issuefrom") String issuefrom,@Param("nameinfo") String nameinfo,@Param("priority") String priority, Pageable pageable);

	
	@Query("SELECT a FROM GrievanceregisterEntity a where  Date(createdDate)=:createdDate and a.typeofuser =:typeofuser and a.assigngroup=:assigngroup or a.assigntoid=:assigntoid and a.categoryId.id =:categoryId")
	Page<GrievanceregisterEntity> getByCreateddateAndTypeofuserAndAssigngroupAndCategoryId(@Param("createdDate") Date finalDate,@Param("typeofuser") String typeofuser,@Param("assigngroup") String userid,@Param("assigntoid") String assigntoid,@Param("categoryId") Long categoryNameO,Pageable pageable);

	
	
	
}
