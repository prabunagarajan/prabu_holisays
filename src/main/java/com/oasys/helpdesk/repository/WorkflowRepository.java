package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.WorkflowEntity;

@Repository
public interface WorkflowRepository extends JpaRepository<WorkflowEntity, Long>{

	Optional<WorkflowEntity> findById(Long id);

	List<WorkflowEntity> findAllByOrderByModifiedDateDesc();

	Optional<WorkflowEntity> findByCodeIgnoreCase(String code);

	List<WorkflowEntity> findByIsActiveOrderByModifiedDateDesc(@Param("isActive") Boolean status);

	@Query("SELECT w FROM WorkflowEntity w ")
	Page<WorkflowEntity> getAll(Pageable pageable);
	
	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and w.slaDetails.issueDetails.subcategoryId.id=:subCategoryId and w.slaDetails.issueDetails.id =:issueDetails and w.isActive=:status")
	Page<WorkflowEntity> getByCategoryActualPSubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("issueDetails") String issueDetails,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and w.slaDetails.issueDetails.subcategoryId.id=:subCategoryId and w.slaDetails.issueDetails.id =:issueDetails")
	Page<WorkflowEntity> getByCategorySubcategoryAndActualP(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("issueDetails") String issueDetails, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and w.slaDetails.issueDetails.subcategoryId.id=:subCategoryId and w.isActive=:status")
	Page<WorkflowEntity> getByCategorySubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and w.slaDetails.issueDetails.subcategoryId.id=:subCategoryId")
	Page<WorkflowEntity> getByCategoryAndSubcategory(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			 Pageable pageablWorkflowEntitye);

	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and w.isActive=:status")
	Page<WorkflowEntity> getByCategoryIdAndStatus(@Param("categoryId") Long categoryId,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where w.slaDetails.issueDetails.subcategoryId.id=:subCategoryId and w.isActive=:status")
	Page<WorkflowEntity> getBySubCategoryIdAndStatus(@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.id =:issueDetails and w.isActive=:status")
	Page<WorkflowEntity> getByActualPAndStatus(@Param("issueDetails") String issueDetails,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId")
	Page<WorkflowEntity> getByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where w.slaDetails.issueDetails.subcategoryId.id=:subCategoryId")
	Page<WorkflowEntity> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where w.isActive=:status")
	Page<WorkflowEntity> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where w.slaDetails.issueDetails.id =:issueDetails")
	Page<WorkflowEntity> getByActualProblem(@Param("issueDetails") String issueDetails, Pageable pageable);

	@Query("SELECT w FROM WorkflowEntity w where w.slaDetails.issueDetails.id=:issueDetailId and w.slaDetails.id=:slaId")
	List<WorkflowEntity> getBySlaIdAndIssueDetailId(@Param("issueDetailId") Long issueDetailId, @Param("slaId") Long slaId);

	@Query("SELECT w FROM WorkflowEntity w where w.slaDetails.issueDetails.subcategoryId.helpDeskTicketCategory.id=:categoryId and w.slaDetails.issueDetails.subcategoryId.id=:subcategoryId and w.slaDetails.issueDetails.id=:issueDetailId")
	List<WorkflowEntity> findByCategory_idAndSubCategory_idAndIssueDetails_id(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId,
			@Param("issueDetailId") Long issueDetailsId);
	
	@Query("SELECT w FROM WorkflowEntity w where  w.slaDetails.issueDetails.id=:issueDetailId")
	List<WorkflowEntity> findByissueDetailsId(@Param("issueDetailId") Long issueDetailId);

}
