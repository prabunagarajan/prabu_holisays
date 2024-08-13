package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ProblemReported;



@Repository
public interface ProblemReportedRepository extends JpaRepository<ProblemReported, Long> {
	
	@Query(value = "select a.* from action_taken a where a.is_active=true and a.actual_problem_id=:actualproblemid ", nativeQuery=true)
	List<ProblemReported> getActionTakenByActionProblemId(@Param("actualproblemid") Long actualproblemid);
	
	@Query("select h from ProblemReported h where h.id=:id ")
	ProblemReported getById(@Param("id") Long id);
	
	
	Optional<ProblemReported> findByPrCodeIgnoreCase(String Code);
	
	@Query(value ="select a.* from problem_reported a where a.is_active =true order by a.modified_date desc", nativeQuery=true)
	//List<ProblemReported> findAllByStatusOrderByModifiedDateDesc();
	List<ProblemReported> findAllByIsActiveOrderByModifiedDateDesc();
	
	
	@Query("SELECT a FROM ProblemReported a ")
	Page<ProblemReported> getAll(Pageable pageable);
	
	
	
	

//	@Query("SELECT a FROM ProblemReported a where  a.categoryId =:category_id and a.subCategoryId.id=:subCategoryId and a.problem =:problem and a.isActive=:is_active")
//	Page<ProblemReported> getByCategoryIdSubCategoryIdProblemAndIsActive(@Param("category_id") Long categoryId,@Param("subCategoryId") Long subCategoryId,
//			@Param("problem") String problem,@Param("is_active") Boolean status, Pageable pageable);

	
	@Query("SELECT a FROM ProblemReported a where  a.subCategoryId.helpDeskTicketCategory.id=:subCategoryId and a.problem =:problem and a.isActive=:is_active")
	Page<ProblemReported> getBySubCategoryIdProblemAndIsActive(@Param("subCategoryId") Long subCategoryId,
			@Param("problem") String problem,@Param("is_active") Boolean status, Pageable pageable);

	//@Query("SELECT a FROM ProblemReported a where  a.subCategoryId.helpDeskTicketCategory.id=:subCategoryId and a.problem =:problem ")
	@Query("SELECT a FROM ProblemReported a WHERE a.subCategoryId.helpDeskTicketCategory.id = :category_id AND a.subCategoryId.id = :subCategoryId AND a.problem = :problem")
	Page<ProblemReported> getByCategoryIdAndSubCategoryIdAndProblem(@Param("category_id") Long CategoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("problem") String problem,Pageable pageable);

	
//	@Query("SELECT a FROM ProblemReported a where   a.subCategoryId.helpDeskTicketCategory.id=:subCategoryId and a.problem =:problem ")
//	Page<ProblemReported> getBySubCategoryIdAndProblem(@Param("subCategoryId") Long subCategoryId,
//			@Param("problem") String problem, Pageable pageable);
	 
	
	
	
	@Query("SELECT a FROM ProblemReported a where   a.subCategoryId.helpDeskTicketCategory.id=:category_id and a.problem =:problem ")
	Page<ProblemReported> getByCategoryIdAndProblem(@Param("category_id") Long CategoryId,
			@Param("problem") String problem, Pageable pageable);
	
	@Query("SELECT a FROM ProblemReported a where  a.problem =:problem ")
	Page<ProblemReported> getByProblem(@Param("problem") String problem, Pageable pageable);
	
	
	
	@Query("SELECT a FROM ProblemReported a where   a.subCategoryId.id=:subCategoryId ")
	Page<ProblemReported> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);
	

	@Query("SELECT a FROM ProblemReported a where   a.subCategoryId.helpDeskTicketCategory.id=:category_id ")
	Page<ProblemReported> getByCategoryId(@Param("category_id") Long CategoryId, Pageable pageable);

	@Query("SELECT a FROM ProblemReported a where a.subCategoryId.helpDeskTicketCategory.id =:categoryId and a.subCategoryId.id=:subCategoryId and a.isActive=true")
	List<ProblemReported> findByCategoryAndSubcategoryId(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subcategoryId);
	
	
	//List<ProblemReported> findAllByIsActiveOrderByModifiedDateDesc(Boolean status);
	
	List<ProblemReported> findAllByOrderByModifiedDateDesc();
	

}