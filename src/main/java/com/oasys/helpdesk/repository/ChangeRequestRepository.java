package com.oasys.helpdesk.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.dto.ChangereqSummaryDTO;
import com.oasys.helpdesk.entity.ChangeRequestEntity;


@Repository
public interface ChangeRequestRepository extends JpaRepository<ChangeRequestEntity, Long>{
	
	@Query("SELECT a FROM ChangeRequestEntity a where  a.changereqApplnNo =:changereqApplnNo")
	Optional<ChangeRequestEntity>findByChangereqApplnNo(@Param("changereqApplnNo") String applnno); 
	
	//Optional<ChangeRequestEntity>findByChangereqApplnNo(String applnno);
	
	Optional<ChangeRequestEntity> findByChangereqApplnNoIgnoreCase(String upperCase);


	List<ChangeRequestEntity> getById(Long id);

	@Query(value="SELECT COUNT(DISTINCT a.id) AS count\r\n" + 
			"			FROM change_request a\r\n" + 
			"			WHERE a.changereq_status = :changereq_status\r\n" + 
			"			  AND (DATE(a.created_date) BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL))\r\n" + 
			"			  AND (a.license_no = COALESCE(:licenceNumbers, a.license_no) \r\n" + 
			"			  OR a.shop_code = COALESCE(:shopCodes, a.shop_code));",nativeQuery = true)
	Integer getAppCountByStatusAndDateRangeAndLicenseNumber(@Param("changereq_status") String changereq_status,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("licenceNumbers") List<String> licenceNumbers, @Param("shopCodes")List<String> shopCodes);


	@Query(value="SELECT\r\n" + 
			"    COUNT(e.id) AS totalChangeRequest,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 0 THEN 1 ELSE 0 END), 0) AS inprogress,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 1 THEN 1 ELSE 0 END), 0) AS accepted,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 2 THEN 1 ELSE 0 END), 0) AS approved,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 3 THEN 1 ELSE 0 END), 0) AS assigned,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 4 THEN 1 ELSE 0 END), 0) AS pending,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 5 THEN 1 ELSE 0 END), 0) AS requestForClarification,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 6 THEN 1 ELSE 0 END), 0) AS rejected,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 7 THEN 1 ELSE 0 END), 0) AS cancelled,\r\n" + 
			"    COALESCE(SUM(CASE WHEN e.changereq_status = 8 THEN 1 ELSE 0 END), 0) AS completed\r\n" + 
			"FROM change_request e where appln_status=2;",nativeQuery = true)
	List<ChangereqSummaryDTO> getCount();
}
