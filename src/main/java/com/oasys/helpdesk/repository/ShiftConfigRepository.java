package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.oasys.helpdesk.entity.ShiftConfigEntity;


@Repository
public interface ShiftConfigRepository extends JpaRepository<ShiftConfigEntity, Long> {
	
	
	

	Optional<ShiftConfigEntity> findByCodeIgnoreCase(@Param("code") String code);
	
	ShiftConfigEntity getByConfiguration(@Param("configuration") String configuration);
	
	//@Query("SELECT a FROM ShiftConfigEntity a where  Upper(a.configuration) =:configuration and a.id !=:id")
	//Optional<ShiftConfigEntity> findByConfigurationIgnoreCaseNotInId(@Param("configuration") String configuration, @Param("id") Long id);

	Optional<ShiftConfigEntity> findByConfigurationAndId(@Param("configuration") String configuration, @Param("id") Long id);

	@Query("SELECT a FROM ShiftConfigEntity a where  a.configuration =:configuration and a.status=:status")
	Page<ShiftConfigEntity> getByConfigurationAndStatus(String configuration, Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM ShiftConfigEntity a where  a.configuration =:configuration")
	Page<ShiftConfigEntity> getByConfiguration(String configuration, Pageable pageable);
	
	
	@Query("SELECT a FROM ShiftConfigEntity a where  a.status=:status")
	Page<ShiftConfigEntity> getByStatus(Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM ShiftConfigEntity a where a.configuration like %:shiftConfig% ")
	Page<ShiftConfigEntity> getAllSubString(@Param("shiftConfig") String shiftConfig, Pageable pageable);


	List<ShiftConfigEntity> findAllByConfiguration(String config);

	List<ShiftConfigEntity> findAllByOrderByModifiedDateDesc();

	
	@Query(value ="select a.* from shift_configuration a where a.status = :status order by a.modified_date desc", nativeQuery=true)
	List<ShiftConfigEntity> findAllByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);
}
