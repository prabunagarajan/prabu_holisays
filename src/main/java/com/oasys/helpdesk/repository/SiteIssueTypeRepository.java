package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteObservationEntity;
import com.oasys.helpdesk.entity.SiteVisitEntity;
@Repository

public interface SiteIssueTypeRepository extends JpaRepository<SiteIssueTypeEntity, Long>{

	Optional<SiteIssueTypeEntity> findByissuetypeIgnoreCase(String issuetype);

	Optional<SiteIssueTypeEntity> findByissuetypeIgnoreCaseAndIdNot(String issuetype, Long id);

	List<SiteIssueTypeEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean is_Active);

	List<SiteIssueTypeEntity> findAllByOrderByModifiedDateDesc();


}
