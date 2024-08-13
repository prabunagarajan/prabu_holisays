package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.oasys.helpdesk.entity.SlaEmailTemplate;
import com.oasys.helpdesk.entity.SlaTemplate;



@Repository
public interface SlaTemplateRepository extends JpaRepository<SlaTemplate, Long> {
	
	
	
	@Query("select s from SlaTemplate s where s.id=:id and s.isActive=true ")
	SlaTemplate getById(@Param("id") Long id);
	
}