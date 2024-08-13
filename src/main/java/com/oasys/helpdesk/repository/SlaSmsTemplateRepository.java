package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.oasys.helpdesk.entity.SlaEmailTemplate;
import com.oasys.helpdesk.entity.SlaSmsTemplate;



@Repository
public interface SlaSmsTemplateRepository extends JpaRepository<SlaSmsTemplate, Long> {
	
	
	
	@Query("select s from SlaSmsTemplate s where s.id=:id and s.isActive=true ")
	SlaSmsTemplate getById(@Param("id") Long id);
	
}