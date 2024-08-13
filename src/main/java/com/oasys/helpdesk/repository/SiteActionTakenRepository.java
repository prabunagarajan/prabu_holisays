package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SiteActionTakenEntity;

@Repository
public interface SiteActionTakenRepository extends JpaRepository<SiteActionTakenEntity, Long>{

	List<SiteActionTakenEntity> findAllByOrderByModifiedDateDesc();

	@Query(value = "select * from site_action_taken sat where sat.is_active=true and sat.observation_id =:observationid ", nativeQuery=true)
	List<SiteActionTakenEntity> getSiteActionTakenByObservationId(Long observationid);

	@Query(value="select * from site_action_taken sat where sat.observation_id=:observationId and sat.action_taken=:actionTaken", nativeQuery=true)
	List<SiteActionTakenEntity> findByObservationIdAndActionTaken(@Param("observationId") Long actualProblemId, @Param("actionTaken") String actionTaken);

	Optional<SiteActionTakenEntity> findBySiteActionTakenIgnoreCaseAndIdNot(String siteActionTaken, Long id);
	
	@Query(value="select * from site_action_taken sat where sat.observation_id=:observationId and sat.action_taken=:actionTaken and sat.is_active = :activeStatus", nativeQuery=true)
	List<SiteActionTakenEntity> findByObservationIdAndActionTaken1(@Param("observationId") Long actualProblemId, @Param("actionTaken") String actionTaken,@Param("activeStatus") boolean activeStatus);

}
