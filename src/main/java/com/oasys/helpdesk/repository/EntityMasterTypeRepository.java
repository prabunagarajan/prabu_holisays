package com.oasys.helpdesk.repository;

import com.oasys.helpdesk.entity.EntityMasterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EntityMasterTypeRepository extends JpaRepository<EntityMasterType, Long> {

	List<EntityMasterType> findAllByEntityNameAndStatus(String entityName, String status);
	Optional<EntityMasterType> findByEntityCode(String entityCode);

	@Query("SELECT a FROM EntityMasterType a ")
	Page<EntityMasterType> getAll(Pageable pageable);

	@Query("SELECT a FROM EntityMasterType a where a.entityName =:entityName and a.status =:status")
	Page<EntityMasterType> getByEntityNameAndStatus(@Param("entityName") String entityName,
															@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM EntityMasterType a where a.status =:status")
	Page<EntityMasterType> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM EntityMasterType a where a.entityName =:entityName")
	Page<EntityMasterType> getByEntityName(@Param("entityName") String entityName, Pageable pageable);

}