package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SlaMasterEntity;

@Repository
public interface SlaMasterRepository  extends JpaRepository<SlaMasterEntity, Long>{

	List<SlaMasterEntity> findAllByOrderByModifiedDateDesc();

	Optional<SlaMasterEntity> findByCodeIgnoreCase(String code);

	@Query("SELECT a FROM SlaMasterEntity a ")
	Page<SlaMasterEntity> getAll(Pageable pageable);
	
	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and a.issueDetails.subcategoryId.id=:subCategoryId and a.issueDetails.id =:issueDetails and a.isActive=:status")
	Page<SlaMasterEntity> getByCategoryIssueDSubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("issueDetails") Long issueDetails,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and a.issueDetails.subcategoryId.id=:subCategoryId and a.issueDetails.id =:issueDetails")
	Page<SlaMasterEntity> getByCategorySubcategoryAndIssueD(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("issueDetails") Long issueDetails, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and a.issueDetails.subcategoryId.id=:subCategoryId and a.isActive=:status")
	Page<SlaMasterEntity> getByCategorySubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and a.issueDetails.subcategoryId.id=:subCategoryId")
	Page<SlaMasterEntity> getByCategoryAndSubcategory(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			 Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId and a.isActive=:status")
	Page<SlaMasterEntity> getByCategoryIdAndStatus(@Param("categoryId") Long categoryId,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.subcategoryId.id=:subCategoryId and a.isActive=:status")
	Page<SlaMasterEntity> getBySubCategoryIdAndStatus(@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.id =:issueDetails and a.isActive=:status")
	Page<SlaMasterEntity> getByIssueDAndStatus(@Param("issueDetails") Long issueDetails,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where  a.issueDetails.subcategoryId.helpDeskTicketCategory.id =:categoryId")
	Page<SlaMasterEntity> getByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.subcategoryId.id=:subCategoryId")
	Page<SlaMasterEntity> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where a.isActive=:status")
	Page<SlaMasterEntity> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.id =:issueDetails")
	Page<SlaMasterEntity> getByIssueD(@Param("issueDetails") Long issueDetails, Pageable pageable);
	
	List<SlaMasterEntity> findByIsActiveOrderByModifiedDateDesc(@Param("isActive") Boolean status);

	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.id =:issueDetailsId")
	Optional<SlaMasterEntity> getByIssueDetailsId(@Param("issueDetailsId") Long issueDetailsid);

	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.subcategoryId.id =:subCategoryId and a.issueDetails.subcategoryId.helpDeskTicketCategory.id=:categoryId")
	Optional<SlaMasterEntity> getByCategoryAndSubcategory(@Param("subCategoryId") Long subCategoryId,@Param("categoryId") Long categoryId);

	
	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.subcategoryId.helpDeskTicketCategory.id=:categoryId and a.issueDetails.subcategoryId.id=:subcategoryId and a.issueDetails.id=:issueDetailId and isActive=1")
	List<SlaMasterEntity> findByCategory_idAndSubCategory_idAndIssueDetails_id(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId,
			@Param("issueDetailId") Long issueDetailsId);
	
	@Query("SELECT a FROM SlaMasterEntity a where a.issueDetails.id =:issueDetailsId and a.id !=:id")
	Optional<SlaMasterEntity> getByIssueDetailsIdNotInId(@Param("issueDetailsId") Long issueDetailsid, @Param("id") Long id);

	
}
