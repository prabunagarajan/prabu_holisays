package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.DesignationEntity;
import com.oasys.helpdesk.entity.IssueFromEntity;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationEntity, Long>{
	List<DesignationEntity> findAll();

	Optional<DesignationEntity> findByCodeIgnoreCase(String code);

	Optional<DesignationEntity> findByDesignationNameIgnoreCase(String designationName);

	List<DesignationEntity> findAllByOrderByModifiedDateDesc();

	@Query("SELECT a FROM DesignationEntity a where  Upper(a.designationName) =:designationName and a.id !=:id")
	Optional<DesignationEntity> findByDesignationNameIgnoreCaseNotInId(@Param("designationName") String type, @Param("id") Long id);

	@Query(value ="select a.* from designation a where a.is_active = true order by a.modified_date desc", nativeQuery=true)
	List<DesignationEntity> findAllByStatusOrderByModifiedDateDesc();
	
	@Query("SELECT a FROM DesignationEntity a where  a.designationName =:name and a.isActive=:status")
	Page<DesignationEntity> getByDesignationNameAndStatus(@Param("name") String name, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM DesignationEntity a where  a.designationName =:name")
	Page<DesignationEntity> getByDesignationName(@Param("name") String name,
			Pageable pageable);
	
	@Query("SELECT a FROM DesignationEntity a where  a.isActive=:status")
	Page<DesignationEntity> getByStatus(@Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM DesignationEntity a ")
	Page<DesignationEntity> getAll(Pageable pageable);
	

}
