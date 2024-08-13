package com.oasys.helpdesk.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.oasys.helpdesk.entity.EntityDetails;

import io.lettuce.core.dynamic.annotation.Param;

public interface EntityDetailsRepository extends JpaRepository<EntityDetails, Long>{
	
	@Query(value="SELECT * FROM entity_details where entity_name=:entity_name",nativeQuery = true)
	List<EntityDetails> getEntityName(@Param("entity_name") String entity_name);
	
	@Query(value="Select * FROM entity_details WHERE is_active =:is_active",nativeQuery = true)	
	List<EntityDetails> getStatus(@Param("is_active") Boolean is_active);

	Optional<EntityDetails> findByEntityNameIgnoreCase(String entityName);
	
	Optional<EntityDetails> findByEntityCodeIgnoreCase(String entityCode);
	Optional<EntityDetails>findByEntityNameIgnoreCaseAndIdNot(String entityName,long id);
	
	Optional<EntityDetails>findByEntityCodeIgnoreCaseAndIdNot(String entityCode,long id);
	
	
	@Query(value="select * from entity_details  where is_active = 1",nativeQuery = true)
	List<EntityDetails>getIsactiveTrue(@Param("pass")Boolean pass);
	
	List<EntityDetails> findAllByIsActiveOrderByModifiedDateDesc(Boolean is_Active);

	
}
