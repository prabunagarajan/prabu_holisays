package com.oasys.helpdesk.repository;

import com.oasys.helpdesk.entity.HelpDeskTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface HelpdeskTemplateRepository extends JpaRepository<HelpDeskTemplate, Long> {


	@Query("SELECT a FROM HelpDeskTemplate a ")
	Page<HelpDeskTemplate> getAll(Pageable pageable);

	@Query("SELECT a FROM HelpDeskTemplate a where a.templateName =:templateName and a.templateType =:templateType and a.status =:status")
	Page<HelpDeskTemplate> getByTemplateNameAndTemplateTypeAndStatus(@Param("templateName") String templateName, @Param("templateType") String templateType, @Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM HelpDeskTemplate a where a.templateType =:templateType and a.status =:status")
	Page<HelpDeskTemplate> getByTemplateTypeAndStatus(@Param("templateType") String templateType, @Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM HelpDeskTemplate a where a.templateName =:templateName and a.status =:status")
	Page<HelpDeskTemplate> getByTemplateNameAndStatus(@Param("templateName") String templateName, @Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM HelpDeskTemplate a where a.templateName =:templateName and a.templateType =:templateType")
	Page<HelpDeskTemplate> getByTemplateNameAndTemplateType(@Param("templateName") String templateName, @Param("templateType") String templateType, Pageable pageable);

	Optional<HelpDeskTemplate> findByTemplateId(int templateId);

	@Query("SELECT a FROM HelpDeskTemplate a where a.status =:status")
	Page<HelpDeskTemplate> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM HelpDeskTemplate a where a.templateName =:templateName")
	Page<HelpDeskTemplate> getByTemplateName(@Param("templateName") String templateName, Pageable pageable);

	@Query("SELECT a FROM HelpDeskTemplate a where a.templateType =:templateType")
	Page<HelpDeskTemplate> getByTemplateType(@Param("templateType") String templateType, Pageable pageable);

}