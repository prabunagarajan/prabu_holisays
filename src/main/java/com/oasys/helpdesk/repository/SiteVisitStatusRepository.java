package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteVisitStatusEntity;

@Repository
public interface SiteVisitStatusRepository extends JpaRepository<SiteVisitStatusEntity, Long> {
	
	@Query(value="select * from site_visit_status where code=:code AND name=:name",nativeQuery = true)
	List<SiteVisitStatusEntity> findByCodeAndName(String code, String name);

	@Query(value="select * from site_visit_status svs where svs.status=:status",nativeQuery = true)
	List<SiteVisitStatusEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean status);


}
