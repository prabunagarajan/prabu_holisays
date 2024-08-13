package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.IssueDetails;



@Repository
public interface IssueDetailsRepository extends JpaRepository<IssueDetails, Long> {
	
	
	@Query("select i from IssueDetails i where i.id=:id")
	IssueDetails getById(@Param("id") Long id);
	
	
	Optional<IssueDetails> findByIssuecodeIgnoreCase(String code);
	
	@Query(value ="select a.* from issue_details a where a.is_active = true order by a.modified_date desc", nativeQuery=true)
    List<IssueDetails> findAllByIsActiveOrderByModifiedDateDesc();
	
	
	@Query("SELECT a FROM IssueDetails a ")
	Page<IssueDetails> getAll(Pageable pageable);
	
	
	@Query("SELECT a FROM IssueDetails a where  a.subcategoryId.helpDeskTicketCategory.id =:category_id and a.subcategoryId.id=:subcategory_id and a.issueName =:issue_name and a.isActive=:is_active")
	Page<IssueDetails> getByCategoryIdSubcategoryIdIssueNameAndIsActive(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subcategory_id,
			@Param("issue_name") String issue_name,@Param("is_active") Boolean is_active, Pageable pageable);

	@Query("SELECT a FROM IssueDetails a where a.subcategoryId.id =:subCategoryId and a.subcategoryId.helpDeskTicketCategory.id=:categoryId and a.isActive=true")
	List<IssueDetails> getByIds(@Param("subCategoryId") Long subCategoryId, @Param("categoryId") Long categoryId);

	
	
	@Query("SELECT a FROM IssueDetails a where  a.subcategoryId.helpDeskTicketCategory.id =:category_id")
	Page<IssueDetails> getByCategoryId(@Param("category_id") Long categoryId, Pageable pageable);
	
	@Query("SELECT a FROM IssueDetails a where  a.subcategoryId.id =:subcategory_id")
	Page<IssueDetails> getBySubcategoryId(@Param("subcategory_id") Long categoryId, Pageable pageable);
	
	@Query("SELECT a FROM IssueDetails a where   a.subcategoryId.helpDeskTicketCategory.id=:category_id and a.subcategoryId.id =:subcategory_id and a.issueName =:issue_name")
	Page<IssueDetails> getByCategorySubcategoryAndIssueName(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subCategoryId,
			@Param("issue_name") String issue_name, Pageable pageable);
	
	
	
	
	@Query("SELECT a FROM IssueDetails a where  a.subcategoryId.helpDeskTicketCategory.id =:category_id and a.subcategoryId.id =:subcategory_id and a.isActive=:is_active")
	Page<IssueDetails> getByCategorySubcategoryAndStatus(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subCategoryId,
			@Param("is_active") Boolean status, Pageable pageable);
	
	
	@Query("SELECT a FROM IssueDetails a where a.subcategoryId.helpDeskTicketCategory.id =:category_id and a.subcategoryId.id =:subcategory_id")
	Page<IssueDetails> getByCategoryAndSubcategory(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subCategoryId,
			 Pageable pageable);
	

	@Query("SELECT a FROM IssueDetails a where  a.subcategoryId.helpDeskTicketCategory.id =:category_id and a.isActive=:is_active")
	Page<IssueDetails> getByCategoryIdAndStatus(@Param("category_id") Long categoryId,@Param("is_active") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM IssueDetails a where a.subcategoryId.id =:subcategory_id and a.isActive=:is_active")
	Page<IssueDetails> getBySubCategoryIdAndStatus(@Param("subcategory_id") Long subCategoryId,
			@Param("is_active") Boolean status, Pageable pageable);
	
	
	@Query("SELECT a FROM IssueDetails a where  a.issueName =:issue_name and a.isActive=:is_active")
	Page<IssueDetails> getByIssueNameAndStatus(@Param("issue_name") String issueDetails,@Param("is_active") Boolean status, Pageable pageable);

	
	@Query("SELECT a FROM IssueDetails a where a.isActive=:is_active")
	Page<IssueDetails> getByStatus(@Param("is_active") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM IssueDetails a where a.issueName =:issue_name")
	Page<IssueDetails> getByIssueName(@Param("issue_name") String issue_name, Pageable pageable);
	
	List<IssueDetails> findAllByOrderByModifiedDateDesc();

	
} 