package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.oasys.helpdesk.entity.SalutationEntity;


public interface SalutationRepository extends JpaRepository<SalutationEntity, Long> {

	Optional<SalutationEntity> findByCodeIgnoreCase(@Param("code")  String code);

	Optional<SalutationEntity> findBySalutationnameAndId(@Param("salutationname") String salutationname, @Param("id") Long id);

	List<SalutationEntity> findAllByOrderByModifiedDateDesc();

	
	@Query(value ="select a.* from salutation a where a.status = :status order by a.modified_date desc", nativeQuery=true)
	List<SalutationEntity> findAllByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);


	
	@Query("SELECT a FROM SalutationEntity a where a.status=:status  and a.salutationname like %:name%"  )
	Page<SalutationEntity> getBySubStringAndStatus(@Param("name") String name, @Param("status")  Boolean status, Pageable pageable);

	@Query("SELECT a FROM SalutationEntity a where  a.salutationname like %:name%")
	Page<SalutationEntity> getBySubString(@Param("name") String name, Pageable pageable);

	@Query("SELECT a FROM SalutationEntity a where  a.status=:status")
	Page<SalutationEntity> getByStatus(@Param("status")  Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM SalutationEntity a ")
	Page<SalutationEntity> getAll(Pageable pageable);

	@Query("SELECT a FROM SalutationEntity a where a.salutationname like %:name%")
	Page<SalutationEntity> getAllSubString(@Param("name") String name, Pageable pageable);
	

	
}
