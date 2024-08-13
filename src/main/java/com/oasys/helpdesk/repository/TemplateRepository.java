package com.oasys.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.TemplateEntity;

@Repository 
public interface TemplateRepository extends JpaRepository<TemplateEntity, Long>{

	@Query("select t from TemplateEntity t where t.code=:code and status=true ")
	Optional<TemplateEntity> findByCode(@Param("code") String code);

}
