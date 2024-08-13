package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SlaConfiguration;
import com.oasys.helpdesk.entity.SlaEmailTemplate;



@Repository
public interface SlaConfigurationRepository extends JpaRepository<SlaConfiguration, Long> {
	
	
	
	@Query("select s from SlaConfiguration s where s.id=:id and s.isActive=true ")
	SlaConfiguration getById(@Param("id") Long id);
	
	@Query("select s from SlaConfiguration s where s.ruleName=:ruleName and s.isActive=true ")
	List<SlaConfiguration> searchSlaByRuleName(@Param("ruleName") String ruleName);
	
}