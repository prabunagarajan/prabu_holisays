package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievancePriorityEntity;

@Repository
public interface GrievancePriorityRepository extends JpaRepository<GrievancePriorityEntity, Long> {

	Optional<GrievancePriorityEntity> findByCodeIgnoreCase(String code);

	List<GrievancePriorityEntity> findAllByOrderByModifiedDateDesc();

	List<GrievancePriorityEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);

	
	@Query("SELECT a FROM GrievancePriorityEntity a where  a.category.id =:categoryId and a.priority =:priority  and a.status=:status ")
	Page<GrievancePriorityEntity> getByCategoryNameOPriorityAndStatus(@Param("categoryId") Long categoryId,
			@Param("priority") String priority,@Param("status")  Boolean status, Pageable pageable);

	
	@Query("SELECT a FROM GrievancePriorityEntity a where  a.category.id =:categoryId and a.priority =:priority")
	Page<GrievancePriorityEntity> getByCategoryNameOAndPriority(@Param("categoryId") Long categoryId,@Param("priority")  String priority,
			Pageable pageable);

	@Query("SELECT a FROM GrievancePriorityEntity a where a.priority =:priority")
	Page<GrievancePriorityEntity> getByPriority(@Param("priority")  String priority, Pageable pageable);

	GrievancePriorityEntity getByPriority(@Param("priority")  String priority);

	@Query("SELECT a FROM GrievancePriorityEntity a where  a.category.id =:categoryid")
	Page<GrievancePriorityEntity> getByCategoryNameO(@Param("categoryid") Long categoryId, Pageable pageable);

	
//	Optional<GrievancePriorityEntity> getById( Long categoryId);

	@Query("SELECT a FROM GrievancePriorityEntity a where  a.status =:status")
	Page<GrievancePriorityEntity> getByStatus(@Param("status")  Boolean status, Pageable pageable);

	@Query("SELECT a FROM GrievancePriorityEntity a where  a.category =:categoryid")
	List<GrievancePriorityEntity> getByCategoryId(@Param("categoryid") Long category);

	@Query(value ="select a from GrievancePriorityEntity a where a.id =:id")
	List<GrievancePriorityEntity> findAllById(@Param("id") Long id);

	@Query(value ="select a from GrievancePriorityEntity a where a.category.id =:categoryId")
	List<GrievancePriorityEntity> findAllByCategory(@Param("categoryId") Long category);

	
	GrievancePriorityEntity getById(Long long1);
	
	@Query("SELECT a FROM GrievancePriorityEntity a where  a.category.id =:categoryid")
	List<GrievancePriorityEntity> getPriorityByCategoryId(@Param("categoryid") Long categoryId);
	
	@Query("SELECT a FROM GrievancePriorityEntity a where  a.category.id =:categoryid and a.id !=:id")
	List<GrievancePriorityEntity> getPriorityByCategoryIdNotInId(@Param("categoryid") Long categoryId, @Param("id") Long id);


	

	

}
