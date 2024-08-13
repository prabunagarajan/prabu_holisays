package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.oasys.helpdesk.entity.SlaEmailTemplate;



@Repository
public interface SlaEmailTemplateRepository extends JpaRepository<SlaEmailTemplate, Long> {
	
	
	
	@Query("select s from SlaEmailTemplate s where s.id=:id ")
	SlaEmailTemplate getById(@Param("id") Long id);
	
}