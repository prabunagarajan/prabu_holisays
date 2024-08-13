package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLEntity;
@Repository
public interface EALRequestPUtoBWFLRepository extends JpaRepository<EALRequestPUtoBWFLEntity, Long>{

	Optional<EALRequestPUtoBWFLEntity> findByLicenseNoAndRequestedapplnNo(String licenseNo, String requestedapplnNo);

	@Query(value = "select * from eal_request_pu_bwfl where id=:id", nativeQuery = true)
	List<EALRequestPUtoBWFLEntity> getById(@Param("id") Long id);

	List<EALRequestPUtoBWFLEntity> findAllByOrderByModifiedDateDesc();

	List<EALRequestPUtoBWFLEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE eal_request_pu_bwfl SET forceclosure_flag=:forceclosure_flag WHERE requested_appln_no =:requested_appln_no", nativeQuery = true)
	public void updateForceclosure(@Param("forceclosure_flag") Boolean forceclosureflag,
			@Param("requested_appln_no") String requested_appln_no);
	
	@Query("select a from EALRequestPUtoBWFLEntity a where  a.status=1")
	List<EALRequestPUtoBWFLEntity> findByStatusOrderByIdDesc();

	Optional<EALRequestPUtoBWFLEntity> findByRequestedapplnNo(String applicationNo);

//	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:applicationNo and (a.status=1 or a.status=6) and a.createdBy=:createdby")
//	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusAndCreatedby(String applicationNo, Long createdby);

	
//	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:applicationNo and a.createdBy=:createdby and a.status=6")
//	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusAckAndCreatedby(String applicationNo,
//			Long createdby);

	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:ealrequestApplnno")
	List<EALRequestPUtoBWFLEntity> getByRequestedapplnNo(String ealrequestApplnno);

	
//	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:requestedapplnNo and a.licenseNo=:licenseNo")
//	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndlicenseNo(String applicationNo, String licenseNo);

//	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:requestedapplnNo and a.licenseNo=:licenseNo")
//	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndlicenseNo(@Param("requestedapplnNo") String requestedapplnNo,
//			@Param("licenseNo") String licenseNo);
//	
//	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:applicationNo and a.licenseNo=:licenseno ")
//	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusAckAndLicNo(@Param("requestedapplnNo")String applicationNo, @Param("licenseNo")String licenseno);
	
	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:requestedapplnNo and a.licenseNo=:licenseNo")
	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndlicenseNo(@Param("requestedapplnNo") String requestedapplnNo,
			@Param("licenseNo") String licenseNo);
	
	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:applicationNo and a.licenseNo=:licenseno")
	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusAckAndLicNo(String applicationNo, String licenseno);

	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:requestedapplnNo and (a.status=1 or a.status=6) and a.createdBy=:createdBy")
	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusAndCreatedby(@Param("requestedapplnNo") String applnno,
			@Param("createdBy") Long createdBy);
	
	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:requestedapplnNo and a.status=5 and a.createdBy=:createdBy")
	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusapAndCreatedby(@Param("requestedapplnNo") String applnno,
			@Param("createdBy") Long createdBy);
	
	@Query("select a from EALRequestPUtoBWFLEntity a where  a.requestedapplnNo=:requestedapplnNo and a.createdBy=:createdBy")
	Optional<EALRequestPUtoBWFLEntity> getByRequestedapplnNoAndStatusAckAndCreatedby(@Param("requestedapplnNo") String applnno,
			@Param("createdBy") Long createdBy);
	 }
	

