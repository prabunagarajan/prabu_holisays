package com.oasys.posasset.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.oasys.posasset.entity.EALRequestAECEntity;

@Repository
public interface EALRequestAECRepository extends JpaRepository<EALRequestAECEntity, Long> {

	@Query("select a from EALRequestAECEntity a where a.licenseNo=:licenseNo and a.requestedapplnNo=:requestedapplnNo and a.status=0")
	Optional<EALRequestAECEntity> findByLicenseNoAndRequestedapplnNo(String licenseNo, String requestedapplnNo);

	@Query(value = "select * from eal_request_aec where id=:id", nativeQuery = true)
	List<EALRequestAECEntity> getById(@Param("id") Long id);

	List<EALRequestAECEntity> findAllByOrderByModifiedDateDesc();

	List<EALRequestAECEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE eal_request_aec SET forceclosure_flag=:forceclosure_flag WHERE requested_appln_no =:requested_appln_no", nativeQuery = true)
	public void updateForceclosure(@Param("forceclosure_flag") Boolean forceclosureflag,
			@Param("requested_appln_no") String requested_appln_no);
	
	@Query("select a from EALRequestAECEntity a where  a.status=1")
	List<EALRequestAECEntity> findByStatusOrderByIdDesc();

	Optional<EALRequestAECEntity> findByRequestedapplnNo(String applicationNo);

	@Query("select a from EALRequestAECEntity a where  a.requestedapplnNo=:applicationNo and (a.status=1 or a.status=6) and a.createdBy=:createdby")
	Optional<EALRequestAECEntity> getByRequestedapplnNoAndStatusAndCreatedby(String applicationNo, Long createdby);

	@Query("select a from EALRequestAECEntity a where  a.requestedapplnNo=:applicationNo and a.status=5 and a.createdBy=:createdby")
	Optional<EALRequestAECEntity> getByRequestedapplnNoAndStatusapAndCreatedby(String applicationNo,
			Long createdby);

	@Query("select a from EALRequestAECEntity a where  a.requestedapplnNo=:applicationNo and a.createdBy=:createdby and a.status=6")
	Optional<EALRequestAECEntity> getByRequestedapplnNoAndStatusAckAndCreatedby(String applicationNo,
			Long createdby);

	@Query(value ="select * from eal_request_aec era where requested_appln_no =:ealrequestApplnno order by id desc limit 1 ", nativeQuery = true)
	List<EALRequestAECEntity> getByRequestedapplnNo(String ealrequestApplnno);

}
