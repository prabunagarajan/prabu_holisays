package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.PriorityMaster;

@Repository
public interface PriorityMasterRepository extends JpaRepository<PriorityMaster, Long>{

	List<PriorityMaster> findAllByOrderByModifiedDateDesc();

	Optional<PriorityMaster> findByCodeIgnoreCase(String code);

	@Query("SELECT a FROM PriorityMaster a ")
	Page<PriorityMaster> getAll(Pageable pageable);
	
	@Query("SELECT a FROM PriorityMaster a where a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.priority =:priority and a.isActive=:status")
	Page<PriorityMaster> getByCategoryActualPSubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("priority") String priority,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.priority =:priority")
	Page<PriorityMaster> getByCategorySubcategoryAndActualP(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("priority") String priority, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.isActive=:status")
	Page<PriorityMaster> getByCategorySubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId")
	Page<PriorityMaster> getByCategoryAndSubcategory(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			 Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.isActive=:status")
	Page<PriorityMaster> getByCategoryIdAndStatus(@Param("categoryId") Long categoryId,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where a.subCategory.id=:subCategoryId and a.isActive=:status")
	Page<PriorityMaster> getBySubCategoryIdAndStatus(@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where  a.priority =:priority and a.isActive=:status")
	Page<PriorityMaster> getByActualPAndStatus(@Param("priority") String issueDetails,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where  a.subCategory.helpDeskTicketCategory.id =:categoryId")
	Page<PriorityMaster> getByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where a.subCategory.id=:subCategoryId")
	Page<PriorityMaster> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where a.isActive=:status")
	Page<PriorityMaster> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM PriorityMaster a where a.priority =:priority")
	Page<PriorityMaster> getByActualProblem(@Param("priority") String priority, Pageable pageable);

	List<PriorityMaster> findByIsActiveOrderByModifiedDateDesc(@Param("isActive") Boolean status);
	
	@Query("SELECT a FROM PriorityMaster a where a.subCategory.id =:subCategoryId and a.subCategory.helpDeskTicketCategory.id=:categoryId and a.isActive=true")
	Optional<PriorityMaster> getById(@Param("subCategoryId") Long subCategoryId,@Param("categoryId") Long categoryId);

	@Query("SELECT a FROM PriorityMaster a where a.subCategory.id =:subCategoryId ")
	Optional<PriorityMaster> findBySubCategoryId(@Param("subCategoryId") Long subCategoryId);
	
	@Query("SELECT a FROM PriorityMaster a where a.subCategory.id =:subCategoryId and a.id != :id")
	Optional<PriorityMaster> findBySubCategoryIdNotInId(@Param("subCategoryId") Long subCategoryId, @Param("id") Long id);


}
