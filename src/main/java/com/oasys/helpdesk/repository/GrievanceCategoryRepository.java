package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceCategoryEntity;

@Repository
public interface GrievanceCategoryRepository extends JpaRepository<GrievanceCategoryEntity, Long>{

	@Query("select c from GrievanceCategoryEntity c where Upper(c.categoryName) =:categoryName ")
	GrievanceCategoryEntity findByName(@Param("categoryName") String categoryName);


	Optional<GrievanceCategoryEntity> findByCodeIgnoreCase(@Param("code")  String code);

	@Query("SELECT a FROM GrievanceCategoryEntity a where  Upper(a.categoryName) =:categoryName and a.id !=:id")
	Optional<GrievanceCategoryEntity> findByCategoryNameIgnoreCaseNotInId(@Param("categoryName") String categoryName, @Param("id") Long id);


	List<GrievanceCategoryEntity> findAllByOrderByModifiedDateDesc();


	GrievanceCategoryEntity getById(@Param("id")  Long id);

	@Query("SELECT a FROM GrievanceCategoryEntity a where  a.id =:id and a.active=:status")
	Page<GrievanceCategoryEntity> getByIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT a FROM GrievanceCategoryEntity a where  a.id =:id")
	Page<GrievanceCategoryEntity> getById(@Param("id") Long id,
			Pageable pageable);

	@Query("SELECT a FROM GrievanceCategoryEntity a where  a.active=:status")
	Page<GrievanceCategoryEntity> getByStatus(@Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT a FROM GrievanceCategoryEntity a ")
	Page<GrievanceCategoryEntity> getAll(Pageable pageable);

	@Query(value ="select a.* from grievance_category a where a.is_active = :active order by a.modified_date desc", nativeQuery=true)
	List<GrievanceCategoryEntity> findAllByStatusOrderByModifiedDateDesc(@Param("active") Boolean active);


	GrievanceCategoryEntity getByCategoryName(@Param("categoryName") String categoryName);
	
	
	@Query(value ="select a from GrievanceCategoryEntity a where  a.typeofUser = :typeofUser")
	List<GrievanceCategoryEntity> findAllByTypeofUser(@Param("typeofUser") String typeofUser);
	
	@Query(value ="select a.* from grievance_category a where  a.type_of_user = :typeofUser and a.is_active = :active order by a.modified_date desc", nativeQuery=true)
	List<GrievanceCategoryEntity> findAllByTypeofUserAndActive(@Param("typeofUser") String typeofUser,@Param("active") Boolean active);
	
	
	
}
