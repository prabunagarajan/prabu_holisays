package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.RoleMaster;

public interface RoleMasterRepository extends JpaRepository<RoleMaster, Long> {

	@Query("select r from RoleMaster r where r.id=:id ")
	RoleMaster getById(@Param("id") Long id);

	Optional<RoleMaster> findByRoleCodeIgnoreCase(String roleCode);

	Optional<RoleMaster> findByRoleNameIgnoreCase(String roleName);

	List<RoleMaster> findByStatusAndDefaultRoleAndHelpdeskRoleOrderByModifiedDateDesc(@Param("status") Boolean status,
			@Param("defaultRole") Boolean defaultRole, @Param("helpdeskRole") Boolean helpdeskRole);

	List<RoleMaster> findByStatusAndHelpdeskRoleOrderByModifiedDateDesc(@Param("status") Boolean status,
			@Param("helpdeskRole") Boolean helpdeskRole);

	@Query("SELECT a FROM RoleMaster a where  a.id =:id and a.status=:status")
	Page<RoleMaster> getByIdAndStatus(@Param("id") Long id, @Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM RoleMaster a where  a.id =:id")
	Page<RoleMaster> getById(@Param("id") Long id, Pageable pageable);

	@Query("SELECT a FROM RoleMaster a where  a.status=:status")
	Page<RoleMaster> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM RoleMaster a ")
	Page<RoleMaster> getAll(Pageable pageable);

	List<RoleMaster> findByStatusAndDefaultRoleOrderByModifiedDateDesc(@Param("status") Boolean status,
			@Param("defaultRole") Boolean defaultRole);
	
	RoleMaster findByRoleCode(String roleCode);

}
