package com.oasys.helpdesk.repository;



import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.SubsolutionEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface SubsolutionRepository extends JpaRepository<SubsolutionEntity, Long> {
	
	@Query("select i from SubsolutionEntity i where i.id=:id")
	SubsolutionEntity getById(@Param("id") Long id);
	
	
	Optional<SubsolutionEntity> findBySubcodeIgnoreCase(String code);
	
	@Query(value ="select a.* from subsolution_help a where a.status = true order by a.modified_date desc", nativeQuery=true)
    List<SubsolutionEntity> findAllByStatusOrderByModifiedDateDesc();
	
	
	@Query("SELECT a FROM SubsolutionEntity a ")
	Page<SubsolutionEntity> getAll(Pageable pageable);
	
	
	
	@Query("SELECT a FROM SubsolutionEntity a where  a.categoryId.id =:category_id and a.subcategoryId.id=:subcategory_id and a.issuedetails =:issuedetails and a.status=:status")
	Page<IssueDetails> getByCategoryIdSubcategoryIdIssueDetailsAndIsActive(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subcategory_id,
			@Param("issuedetails") String issuedetails,@Param("status") Boolean status, Pageable pageable);

	
	@Query("SELECT a FROM SubsolutionEntity a where a.subcategoryId.id =:subCategoryId and a.categoryId.id=:categoryId")
	List<SubsolutionEntity> getByIds(@Param("subCategoryId") Long subCategoryId, @Param("categoryId") Long categoryId);

	
	@Query("SELECT a FROM SubsolutionEntity a where  a.categoryId.id =:category_id")
	Page<SubsolutionEntity> getByCategoryId(@Param("category_id") Long categoryId, Pageable pageable);
	
	
	@Query("SELECT a FROM SubsolutionEntity a where  a.subcategoryId.id =:subcategory_id")
	Page<SubsolutionEntity> getBySubcategoryId(@Param("subcategory_id") Long categoryId, Pageable pageable);
	
	
	
	@Query("SELECT a FROM SubsolutionEntity a where   a.categoryId.id =:category_id and a.subcategoryId.id =:subcategory_id and a.issuedetails =:issuedetails")
	Page<SubsolutionEntity> getByCategorySubcategoryAndIssueDetails(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subCategoryId,
			@Param("issuedetails") String issuedetails, Pageable pageable);
	
	
	
	@Query("SELECT a FROM SubsolutionEntity a where  a.categoryId.id =:category_id and a.subcategoryId.id =:subcategory_id and a.status=:status")
	Page<SubsolutionEntity> getByCategorySubcategoryAndStatus(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);
	
	
	
	@Query("SELECT a FROM SubsolutionEntity a where a.categoryId.id =:category_id and a.subcategoryId.id =:subcategory_id")
	Page<SubsolutionEntity> getByCategoryAndSubcategory(@Param("category_id") Long categoryId,@Param("subcategory_id") Long subCategoryId,
			 Pageable pageable);
	
	
	@Query("SELECT a FROM SubsolutionEntity a where  a.categoryId.id =:category_id and a.status=:status")
	Page<SubsolutionEntity> getByCategoryIdAndStatus(@Param("category_id") Long categoryId,@Param("status") Boolean status, Pageable pageable);

	
	@Query("SELECT a FROM SubsolutionEntity a where a.subcategoryId.id =:subcategory_id and a.status=:status")
	Page<SubsolutionEntity> getBySubCategoryIdAndStatus(@Param("subcategory_id") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);
	

	@Query("SELECT a FROM SubsolutionEntity a where  a.issuedetails =:issuedetails and a.status=:status")
	Page<SubsolutionEntity> getByIssuAndStatus(@Param("issuedetails") String issueDetails,@Param("status") Boolean status, Pageable pageable);

	
	@Query("SELECT a FROM SubsolutionEntity a where a.status=:status")
	Page<SubsolutionEntity> getByStatus(@Param("status") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM SubsolutionEntity a where a.issuedetails =:issuedetails")
	Page<IssueDetails> getByIssueDetails(@Param("issuedetails") String issuedetails, Pageable pageable);
	
	
	@Query("SELECT a FROM SubsolutionEntity a where  (a.issuedetails) =:issuedetails and a.id !=:id")
	Optional<SubsolutionEntity> findByIssuedetailsIgnoreCaseNotInId(@Param("issuedetails") String ticketstatusname, @Param("id") Long id);
	

	Optional<SubsolutionEntity> findBySubsolutionIgnoreCase(String subsolution);
	
}
