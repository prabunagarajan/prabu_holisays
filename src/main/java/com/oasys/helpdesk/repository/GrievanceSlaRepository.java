package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceSlaEntity;

@Repository
public interface GrievanceSlaRepository extends JpaRepository<GrievanceSlaEntity, Long>{

	Optional<GrievanceSlaEntity> findByCodeIgnoreCase(String code);

	@Query("SELECT a FROM GrievanceSlaEntity a where  a.sla =:sla")
	Optional<GrievanceSlaEntity> findBySla(@Param("sla") Long sla);

	List<GrievanceSlaEntity> findAllByOrderByModifiedDateDesc();

	List<GrievanceSlaEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);

	@Query("SELECT a FROM GrievanceSlaEntity a where  a.gIssueDetails.category.id =:category")
	Page<GrievanceSlaEntity> getByCategoryNameO(@Param("category") Long categoryId, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceSlaEntity a where  a.gIssueDetails.category.id =:category")
	Page<GrievanceSlaEntity> getByCategoryName(@Param("category") Long categoryId, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceSlaEntity a where  a.gIssueDetails.category.id =:category and a.typeofUser=:typeofUser")
	Page<GrievanceSlaEntity> getByCategoryUser(@Param("category") Long categoryId,@Param("typeofUser")  String typeofUser, Pageable pageable);

	@Query("SELECT a FROM GrievanceSlaEntity a where  a.typeofUser=:typeofUser")
	Page<GrievanceSlaEntity> getByCategoryU(@Param("typeofUser")  String typeofUser, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceSlaEntity a ")
	Page<GrievanceSlaEntity> getByCategoryNull( Pageable pageable);

	@Query("SELECT a FROM GrievanceSlaEntity a where  a.sla =:sla")
	GrievanceSlaEntity getBySla(@Param("sla") Long sla);

	@Query("SELECT a FROM GrievanceSlaEntity a where a.gIssueDetails.id=:gIssueDetailsId")
	Optional<GrievanceSlaEntity> getByIssueDetailId(@Param("gIssueDetailsId") Long gIssueDetailsId);

	@Query("SELECT a FROM GrievanceSlaEntity a where a.gIssueDetails.id=:gIssueDetailsId and a.id !=:id")
	Optional<GrievanceSlaEntity> getByIssueDetailIdNotInSlaId(@Param("gIssueDetailsId") Long gIssueDetailsId, @Param("id") Long id);

	@Query("SELECT a FROM GrievanceSlaEntity a where a.gIssueDetails.category.id =:categoryId and a.gIssueDetails.id=:gIssueDetailsId ")
	Optional<GrievanceSlaEntity> getByCategoryIdAndIssueDetailId(@Param("categoryId") Long categoryId, @Param("gIssueDetailsId") Long gIssueDetailsId);


}
