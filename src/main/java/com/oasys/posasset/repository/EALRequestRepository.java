package com.oasys.posasset.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.dto.AssetMapCountDto;
import com.oasys.helpdesk.dto.UserDto;
import com.oasys.helpdesk.repository.EALDashboard;
import com.oasys.posasset.dto.EalRequestSummaryCountDTO;
import com.oasys.posasset.dto.EalRequestVendorDTO;
import com.oasys.posasset.entity.EALRequestEntity;

@Repository
public interface EALRequestRepository extends JpaRepository<EALRequestEntity, Long> {

	@Query(value = "select * from eal_request where id=:id", nativeQuery = true)
	List<EALRequestEntity> getById(@Param("id") Long id);

	List<EALRequestEntity> findAllByOrderByModifiedDateDesc();

	List<EALRequestEntity> findByCreatedByOrderByIdDesc(Long userId);

	Optional<EALRequestEntity> findByLicenseNoAndRequestedapplnNo(String licenseNo, String requestedapplnNo);

	// Optional<EALRequestEntity> findByRequestedapplnNoAndStatus(String
	// applnno,Integer status);

	Optional<EALRequestEntity> findByRequestedapplnNo(String applnno);

	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:requestedapplnNo and a.status=1 and a.createdBy=:createdBy")
	Optional<EALRequestEntity> getByRequestedapplnNoAndStatusAndCreatedby(@Param("requestedapplnNo") String applnno,
			@Param("createdBy") Long createdBy);

	@Query(value = "SELECT COUNT(er.id) As totalealrequested,SUM(er.status=0) As totalinprocess,SUM(er.status=1) As totalapproved,SUM(er.status=3) As totalrequestforclarification,SUM(er.status=5) As totalrejected from eal_request er  where date(er.created_date) between :fromDate and :toDate", nativeQuery = true)
	public List<EALDashboard> getCountByStatusAndCreatedDateBetween(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	List<EALRequestEntity> findByCurrentlyWorkwithOrderByIdDesc(String designationCode);

	@Query(value = "SELECT COUNT(er.id) As totalealrequested,SUM(er.status=0) As totalinprocess,SUM(er.status=1) As totalapproved,SUM(er.status=3) As totalrequestforclarification,SUM(er.status=5) As totalrejected from eal_request er  where date(er.created_date) between :fromDate and :toDate and er.created_by=:created_by", nativeQuery = true)
	public List<EALDashboard> getCountByStatusAndCreatedDateBetweenAndCreatedBy(@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("created_by") Long userid);

	@Query("SELECT a FROM EALRequestEntity a where a.licenseNo =:licenseNo and date(a.createdDate) between :fromDate and :toDate and a.codeType =:codeType order by a.modifiedDate desc")
	List<EALRequestEntity> getByLicenseNoAndCodeType(@Param("licenseNo") String licenceno,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("codeType") String codeType);

	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:requestedapplnNo")
	List<EALRequestEntity> getByRequestedapplnNo(@Param("requestedapplnNo") String ealrequestApplnno);

	@Query("select a from EALRequestEntity a where  a.status=1")
	List<EALRequestEntity> findByStatusOrderByIdDesc();

	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:requestedapplnNo and a.status=1 ")
	Optional<EALRequestEntity> getByRequestedapplnNoAndStatus(@Param("requestedapplnNo") String applnno);

	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:requestedapplnNo and a.status=5 and a.createdBy=:createdBy")
	Optional<EALRequestEntity> getByRequestedapplnNoAndStatusapAndCreatedby(@Param("requestedapplnNo") String applnno,
			@Param("createdBy") Long createdBy);

	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:requestedapplnNo and a.createdBy=:createdBy and a.vendorStatus=3")
	Optional<EALRequestEntity> getByRequestedapplnNoAndStatusAckAndCreatedby(@Param("requestedapplnNo") String applnno,
			@Param("createdBy") Long createdBy);

	@Query(value = "SELECT COUNT(er.id) AS totalealrequested,\r\n"
			+ "    SUM(er.status = 0) AS totalinprocess,SUM(er.status = 1) AS totalapproved,\r\n"
			+ "    SUM(er.status = 3) AS totalrequestforclarification,SUM(er.status = 5) AS totalrejected\r\n"
			+ "FROM eal_request er\r\n" + "WHERE DATE(er.created_date) BETWEEN :fromDate AND :toDate\r\n"
			+ "    AND (er.currently_work_with = :currently_work_with OR er.approved_by = :approved_by)\r\n"
			+ "    AND (er.district = :district OR :district IS NULL)\r\n"
			+ "    AND (er.license_no IN (:license_nos));", nativeQuery = true)
	public List<EALDashboard> getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedBy(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("currently_work_with") String currentlyworkwith, @Param("approved_by") String approvedby,
			@Param("district") String district, @Param("license_nos") List<String> licenseNumberArray);

	@Query("select a from EALRequestEntity a where  a.currentlyWorkwith=:currentlyWorkwith and a.status=0")
	List<EALRequestEntity> getBycurrentlyWorkwithAndStatus(@Param("currentlyWorkwith") String currentlywork);

	@Modifying
	@Transactional
	@Query(value = "UPDATE eal_request SET forceclosure_flag=:forceclosure_flag WHERE requested_appln_no =:requested_appln_no", nativeQuery = true)
	public void updateForceclosure(@Param("forceclosure_flag") Boolean forceclosureflag,
			@Param("requested_appln_no") String requested_appln_no);

	@Query("select a from EALRequestEntity a where   a.forceclosureFlag=:forceclosureFlag")
	Optional<EALRequestEntity> getByForceclosureFlag(@Param("forceclosureFlag") Boolean forceclosureFlag);
	
	@Query(value = "SELECT COUNT(e.id) AS totalDevice, SUM(e.vendor_status = 0) AS accepted,\r\n" + 
			"		SUM(e.vendor_status = 1) AS requestForClarification,\r\n" + 
			"		SUM(e.vendor_status = 2) AS rejected,    SUM(e.vendor_status = 3) AS dispatched,\r\n" + 
			"		SUM(e.vendor_status = 4) AS forceclosure,    SUM(e.vendor_status = 5) AS acknowledge,\r\n" + 
			"		SUM(e.vendor_status = 6) AS cancelled,   SUM(e.vendor_status = 7) AS requested,\r\n" + 
			"		SUM(e.vendor_status = 8) AS inprogress \r\n" + 
			"FROM eal_request e WHERE \r\n" + 
			"		 (DATE(e.created_date) BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL))", nativeQuery = true)
	List<EalRequestSummaryCountDTO> getCount(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	
	@Query(value = "SELECT COUNT(er.id) AS totalealrequested,\r\n"
			+ "    SUM(er.status = 0) AS totalinprocess,SUM(er.status = 1) AS totalapproved,\r\n"
			+ "    SUM(er.status = 3) AS totalrequestforclarification,SUM(er.status = 5) AS totalrejected\r\n"
			+ "FROM eal_request er\r\n" + "WHERE DATE(er.created_date) BETWEEN :fromDate AND :toDate\r\n"
			+ "    AND (er.currently_work_with = :currently_work_with OR er.approved_by = :approved_by)\r\n"
			+ "    AND (er.district = :district OR :district IS NULL);", nativeQuery = true)
	public List<EALDashboard> getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApproved(
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("currently_work_with") String currentlyworkwith, @Param("approved_by") String approvedby,
			@Param("district") String district);
	
	
	
//	@Query(value = "SELECT COUNT(er.id) AS totalealrequested, " +
//            "SUM(er.status = 0) AS totalinprocess, " +
//            "SUM(er.status = 1) AS totalapproved, " +
//            "SUM(er.status = 3) AS totalrequestforclarification, " +
//            "SUM(er.status = 5) AS totalrejected " +
//            "FROM eal_request er " +
//            "WHERE DATE(er.created_date) BETWEEN :fromDate AND :toDate " +
//            "AND (er.currently_work_with = :currently_work_with OR er.approved_by = :approved_by) " +
//            "AND (er.district = :district OR :district IS NULL) " +
//            "AND (er.unit_code IN :unit_code OR :unit_code IS NULL) " +
//            "AND (er.license_no IN :license_no OR :license_no IS NULL)", nativeQuery = true)
//	public List<EALDashboard> getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedByAndUnitcodeArray(
//			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
//			@Param("currently_work_with") String currentlyworkwith, @Param("approved_by") String approvedby,
//			@Param("district") String district, @Param("unit_code") List<String> unitCodeArray,
//			@Param("license_no") List<String> licenseNumberArray);
	
	@Query(value = "SELECT " +
	        "    COUNT(er.id) AS totalealrequested, " +
	        "    SUM(CASE WHEN er.status = 0 THEN 1 ELSE 0 END) AS totalinprocess, " +
	        "    SUM(CASE WHEN er.status = 1 THEN 1 ELSE 0 END) AS totalapproved, " +
	        "    SUM(CASE WHEN er.status = 3 THEN 1 ELSE 0 END) AS totalrequestforclarification, " +
	        "    SUM(CASE WHEN er.status = 5 THEN 1 ELSE 0 END) AS totalrejected " +
	        "FROM " +
	        "    eal_request er " +
	        "WHERE " +
	        "    DATE(er.created_date) BETWEEN :fromDate AND :toDate " +
	        "    AND (:currently_work_with IS NULL OR er.currently_work_with = :currently_work_with) " +
	        "    AND (:approved_by IS NULL OR er.approved_by = :approved_by) " +
	        "    AND (:district IS NULL OR er.district = :district) " +
	        "    AND (er.unit_code IN (:unit_code)) ",
	    nativeQuery = true)
List<EALDashboard> getCountByStatusAndCreatedDateBetweenAndCurrentlyAndApprovedByAndUnitcodeArray(
    @Param("fromDate") Date fromDate,
    @Param("toDate") Date toDate,
    @Param("currently_work_with") String currentlyworkwith,
    @Param("approved_by") String approvedby,
    @Param("district") String district,
    @Param("unit_code") List<String> unitCodeArray);

	
	
	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:requestedapplnNo and a.licenseNo=:licenseNo")
	Optional<EALRequestEntity> getByRequestedapplnNoAndlicenseNo(@Param("requestedapplnNo") String requestedapplnNo,
			@Param("licenseNo") String licenseNo);

	@Query("select a from EALRequestEntity a where  a.requestedapplnNo=:applicationNo and a.licenseNo=:licenseno and a.vendorStatus=3")
	Optional<EALRequestEntity> getByRequestedapplnNoAndStatusAckAndLicNo(String applicationNo, String licenseno);

	
	@Query(value = "SELECT u.id AS UserId, CONCAT(u.first_name, ' ', u.last_name) AS FullName, r.role_name as RoleName " +
            "FROM user u " +
            "JOIN user_role ur ON u.id = ur.user_id " +
            "JOIN role_master r ON ur.role_id = r.id " +
            "WHERE r.role_code ='VENDOR'", nativeQuery = true)
List<EalRequestVendorDTO> getVendorCode();
	
	@Query(value = "SELECT u.id AS UserId, CONCAT(u.first_name, ' ', u.last_name) AS FullName, r.role_name As RoleName, u.address As Address FROM user u JOIN user_role ur ON u.id = ur.user_id JOIN role_master r ON ur.role_id = r.id WHERE u.id = :code", nativeQuery = true)
	List<EalRequestVendorDTO> getUser(@Param("code") String code);
	
	
}
