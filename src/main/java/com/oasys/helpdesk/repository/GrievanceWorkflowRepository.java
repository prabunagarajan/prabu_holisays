package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievanceWorkflowEntity;

@Repository
public interface GrievanceWorkflowRepository extends JpaRepository<GrievanceWorkflowEntity, Long>{

	Optional<GrievanceWorkflowEntity> findByCodeIgnoreCase(String code);
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where a.sla.gIssueDetails.id=:issueDetails" )
	Optional<GrievanceWorkflowEntity> findByIssueDetails(@Param("issueDetails") Long issueDetails);
//
	List<GrievanceWorkflowEntity> findAllByOrderByModifiedDateDesc();

	@Query(value ="select a.* from grievance_workflow a where a.is_active = true order by a.modified_date desc", nativeQuery=true)
	List<GrievanceWorkflowEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where a.sla.gIssueDetails.category=:categoryName and  a.sla.gIssueDetails=:issueDetails"  )
	Page<GrievanceWorkflowEntity> getBySubStringAndStatus(@Param("categoryName") GrievanceCategoryEntity categoryNameO,@Param("issueDetails")  GrievanceIssueDetails issueDetailsO, Pageable pageable);

	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where a.sla.gIssueDetails.category = :categoryName"  )
	Page<GrievanceWorkflowEntity> getByCategoryNameO(@Param("categoryName")  GrievanceCategoryEntity categoryNameO, Pageable pageable);

	@Query("SELECT a FROM GrievanceWorkflowEntity a where  a.sla.gIssueDetails=:issueDetails"  )
	Page<GrievanceWorkflowEntity> getByIssueDetails(@Param("issueDetails") GrievanceIssueDetails issueDetailsO, Pageable pageable);
//
	@Query("SELECT a FROM GrievanceWorkflowEntity a where  a.sla.gIssueDetails=:issueDetails and  a.sla.gIssueDetails.category=:categoryName and  a.typeofUser=:typeofUser "  )
	Page<GrievanceWorkflowEntity> getByIssueDetailsAll(@Param("issueDetails") GrievanceIssueDetails issueDetailsO,@Param("categoryName")  GrievanceCategoryEntity categoryNameO,@Param("typeofUser") String typeofUser, Pageable pageable);

	@Query("SELECT a FROM GrievanceWorkflowEntity a where  a.sla.gIssueDetails=:issueDetails and  a.sla.gIssueDetails.category=:categoryName "  )
	Page<GrievanceWorkflowEntity> getByIssueDetailsAllNull(@Param("issueDetails") GrievanceIssueDetails issueDetailsO,@Param("categoryName")  GrievanceCategoryEntity categoryNameO, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a "  ) 
	Page<GrievanceWorkflowEntity> getByIssueDetailsAllDataNull( Pageable pageable);
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where a.typeofUser=:typeofUser "  )
	Page<GrievanceWorkflowEntity> getByIssueDetailsType(@Param("typeofUser") String typeofUser, Pageable pageable);	
	
	GrievanceWorkflowEntity getById(@Param("id")  Long id);

	@Query("SELECT a FROM GrievanceWorkflowEntity a where  a.sla.gIssueDetails=:issueDetails"  )
	Optional<GrievanceWorkflowEntity> findByIssueDetails(@Param("issueDetails") GrievanceIssueDetails grievanceIssueDetails);

	
	@Query(value ="select a from GrievanceWorkflowEntity a where a.sla.gIssueDetails.category.id =:category_id")
	List<GrievanceWorkflowEntity> findAllByCategory(@Param("category_id") Long categoryid);
	
	
//	@Query("SELECT a FROM GrievanceWorkflowEntity a where  a.sla.gIssueDetails.id=:issue_id and a.sla.gIssueDetails.category.id =:category_id and a.status=:status"  )
//	GrievanceWorkflowEntity getByIssueDetailsId(@Param("issue_id") Long issuedetid,@Param("category_id") Long categoryid,@Param("status") Boolean status);
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where  a.sla.gIssueDetails.id=:issue_id  and a.status=:status"  )
	GrievanceWorkflowEntity getByIssueDetailsId(@Param("issue_id") Long issuedetid,@Param("status") Boolean status);
	
	
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where a.sla.gIssueDetails.id=:issueDetailsId" )
	List<GrievanceWorkflowEntity> findByIssueDetailId(@Param("issueDetailsId") Long issueDetailsId);
	
	@Query("SELECT a FROM GrievanceWorkflowEntity a where a.sla.gIssueDetails.id=:issueDetailsId and a.id !=:id" )
	List<GrievanceWorkflowEntity> findByIssueDetailIdNotInWorkflowId(@Param("issueDetailsId") Long issueDetailsId, @Param("id") Long id);

}
