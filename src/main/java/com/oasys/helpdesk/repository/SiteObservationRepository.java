package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.AssetStatusEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.entity.SiteActionTakenEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;

public interface SiteObservationRepository extends JpaRepository<SiteObservationEntity, Long> {
	
	   Optional<SiteObservationEntity> findByObservationIgnoreCase(String observation);
	   
	   
	   List<SiteObservationEntity> findAllByOrderByModifiedDateDesc();
		Optional<SiteObservationEntity> findByObservationIgnoreCaseAndIdNot(String observation,long id);
		
		List<SiteObservationEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean is_Active);

//
//		List<SiteObservationEntity> findBySiteIssueTypeIdAndObservation(@NotNull(message = "103") Long siteIssueTypeId,
//				String observation);
		
		@Query(value="select * from site_observation so where so.observation=:observation and so.isssuetype_id=:issueTypeId", nativeQuery=true)
		List<SiteObservationEntity> findBySiteIssueTypeIdAndObservation(@Param("issueTypeId") Long siteIssueTypeId, @Param("observation") String observation );
		
		@Query(value = "select * from site_observation so  where so.is_active=true and so.isssuetype_id  =:issuetypeid ", nativeQuery=true)
		List<SiteObservationEntity> getSiteObservationByIssueTypeId(Long issuetypeid);

		@Query(value="select * from site_observation so where so.observation=:observation and so.isssuetype_id=:issueTypeId and so.is_active = :activeStatus", nativeQuery=true)
		List<SiteObservationEntity> findBySiteIssueTypeIdAndObservation1(@Param("issueTypeId")Long siteIssueTypeId,
				@Param("observation") String observation, @Param("activeStatus") boolean activeStatus);
}
