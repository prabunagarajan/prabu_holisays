package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.entity.EntityDetails;


@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
	
	
	Optional<DepartmentEntity> findByDepartmentIgnoreCase(String department);
	
	
	Optional<DepartmentEntity> findByDepartmentCodeIgnoreCase(String code);

	@Query("SELECT a FROM DepartmentEntity a where  a.id =:id")
	Page<DepartmentEntity> getById(@Param("id") Long id,
			Pageable pageable);
	
	
	@Query("SELECT a FROM DepartmentEntity a ")
	Page<DepartmentEntity> getAll(Pageable pageable);
	
	List<DepartmentEntity> findAllByOrderByModifiedDateDesc();
	
	
	
	@Query("SELECT a FROM DepartmentEntity a where  Upper(a.department) =:department and a.id !=:id")
	Optional<DepartmentEntity> findByTypeIgnoreCaseNotInId(@Param("department") String department, @Param("id") Long id);
	
	
	
	@Query("SELECT a FROM DepartmentEntity a where  a.id =:id and a.status=:status")
	Page<DepartmentEntity> getByIdAndStatus(@Param("id") Long id, @Param("status") String status,
			Pageable pageable);
	
	
	
	@Query("SELECT a FROM DepartmentEntity a where  a.status=:status")
	Page<DepartmentEntity> getByStatus(@Param("status") String status,
			Pageable pageable);
	
	
	List<DepartmentEntity> findAllByStatusOrderByModifiedDateDesc(String status);
	
	
}
