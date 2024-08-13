
package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.oasys.helpdesk.entity.ShiftWorkingDaysEntity;

public interface ShiftWorkingDaysRepository extends JpaRepository<ShiftWorkingDaysEntity, Long> {

	Optional<ShiftWorkingDaysEntity> findByCodeIgnoreCase(@Param("code") String code);

	
	
//	@Query("SELECT a FROM ShiftWorkingDaysEntity a where  a.workingdays =:workingdays and a.id !=:id")
	Optional<ShiftWorkingDaysEntity> findByWorkingdaysAndId(@Param("workingdays")Long long1, @Param("id") Long id);



	List<ShiftWorkingDaysEntity> findAllByWorkingdays(@Param("workingdays") Long workingdays);



	List<ShiftWorkingDaysEntity> findAllByOrderByModifiedDateDesc();


	@Query(value ="select a.* from shift_working_days a where a.status = :status order by a.modified_date desc", nativeQuery=true)

	List<ShiftWorkingDaysEntity> findAllByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);
	
	
	@Query("SELECT a FROM ShiftWorkingDaysEntity a where  a.workingdays =:workingdays and a.status=:status")
	Page<ShiftWorkingDaysEntity> getByWorkingdaysAndStatus(@Param("workingdays") Long workingdays, @Param("status") Boolean status,
			Pageable pageable);

	
	
	@Query("SELECT a FROM ShiftWorkingDaysEntity a where  a.workingdays =:workingdays")
	Page<ShiftWorkingDaysEntity> getByWorkingdays(@Param("workingdays") Long workingdays, Pageable pageable);
	
	
	@Query("SELECT a FROM ShiftWorkingDaysEntity a where  a.status=:status")
	Page<ShiftWorkingDaysEntity> getByStatus(Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM ShiftWorkingDaysEntity a ")
	Page<ShiftWorkingDaysEntity> getAll(Pageable pageable);


	
	

}