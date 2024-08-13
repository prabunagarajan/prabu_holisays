package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceEscalationWorkflowEntity;

@Repository
public interface GrievanceEscalationWorkflowRepository extends JpaRepository<GrievanceEscalationWorkflowEntity, Long>{

	Optional<GrievanceEscalationWorkflowEntity> findByCodeIgnoreCase(String code);
	
	List<GrievanceEscalationWorkflowEntity> findAllByOrderByModifiedDateDesc();
	
	@Query("SELECT a FROM GrievanceEscalationWorkflowEntity a where a.sla.gIssueDetails.id=:issueDetailsId and a.id !=:id" )
	Optional<GrievanceEscalationWorkflowEntity> findByIssueDetailIdNotInWorkflowId(@Param("issueDetailsId") Long issueDetailsId, @Param("id") Long id);
	
	List<GrievanceEscalationWorkflowEntity> findByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);

	@Query("SELECT a FROM GrievanceEscalationWorkflowEntity a where a.sla.gIssueDetails.id=:issueDetailsId " )
	Optional<GrievanceEscalationWorkflowEntity> findByIssueDetailId(@Param("issueDetailsId") Long issueDetailsId);
	
	
	@Query("SELECT a FROM GrievanceEscalationWorkflowEntity a where a.sla.gIssueDetails.id =:issueDetailsId and a.sla.id =:slaId" )
	List<GrievanceEscalationWorkflowEntity> getBySlaIdAndIssueDetailId(@Param("issueDetailsId") Long issueDetailsId, @Param("slaId") Long slaId);
}
