package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Priority;



@Repository
public interface ActionTakenRepository extends JpaRepository<ActionTaken, Long> {
	
	@Query(value = "select a.* from action_taken a where a.is_active=true and a.actual_problem_id=:actualproblemid ", nativeQuery=true)
	List<ActionTaken> getActionTakenByActionProblemId(@Param("actualproblemid") Long actualproblemid);
	
	@Query("select h from ActionTaken h where h.id=:id ")
	ActionTaken getById(@Param("id") Long id);
	
	@Query("select h from ActionTaken h where h.isActive=true ")
	List<ActionTaken> getAllActiveActionTaken();
	
	@Query("select h from ActionTaken h where h.isActive=false ")
	List<ActionTaken> getAllActionTakenForPending();
	
	@Query("select h from ActionTaken h where h.id=:id and h.isActive=false ")
	Optional<ActionTaken> getActionTakenForPendingById(@Param("id") Long id);

	//List<ActionTaken> findAllByIsActiveOrderByModifiedDateDesc(Boolean status);
	
	List<ActionTaken> findAllByOrderByModifiedDateDesc();
	

	Optional<ActionTaken> findByCodeIgnoreCase(String code);

	List<ActionTaken> findByIsActiveOrderByModifiedDateDesc(Boolean true1);
	
	@Query("SELECT a FROM ActionTaken a where a.actualProblem.id =:actualProblemId and upper(a.actionTaken)=:actionTaken")
	Optional<ActionTaken> findByActualProblemIdAndActionTaken(@Param("actualProblemId") Long actualProblemId, @Param("actionTaken") String actionTaken);

	@Query("SELECT a FROM ActionTaken a where a.actualProblem.id =:actualProblemId and a.id != :id and upper(a.actionTaken)=:actionTaken")
	Optional<ActionTaken> findByActualProblemIdAndActionTakenNotInId(@Param("actualProblemId") Long actualProblemId, @Param("id") Long id, @Param("actionTaken") String actionTaken);

	
	@Query("SELECT a FROM ActionTaken a ")
	Page<ActionTaken> getAll(Pageable pageable);
	
	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.subCategory.helpDeskTicketCategory.id =:categoryId and a.actualProblem.subCategory.id=:subCategoryId and a.actualProblem =:actionTaken and a.isActive=:status")
	Page<ActionTaken> getByCategoryActionTSubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("actionTaken") String actualProblem,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.subCategory.helpDeskTicketCategory.id =:categoryId and a.actualProblem.subCategory.id=:subCategoryId and a.actionTaken =:actionTaken")
	Page<ActionTaken> getByCategorySubcategoryAndActionT(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("actionTaken") String actualProblem, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.subCategory.helpDeskTicketCategory.id =:categoryId and a.actualProblem.subCategory.id=:subCategoryId and a.isActive=:status")
	Page<ActionTaken> getByCategorySubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.subCategory.helpDeskTicketCategory.id=:categoryId and a.actualProblem.subCategory.id=:subCategoryId")
	Page<ActionTaken> getByCategoryAndSubcategory(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			 Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.subCategory.helpDeskTicketCategory.id =:categoryId and a.isActive=:status")
	Page<ActionTaken> getByCategoryIdAndStatus(@Param("categoryId") Long categoryId,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where a.actualProblem.subCategory.id=:subCategoryId and a.isActive=:status")
	Page<ActionTaken> getBySubCategoryIdAndStatus(@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where  a.actionTaken =:actionTaken and a.isActive=:status")
	Page<ActionTaken> getByActionTAndStatus(@Param("actionTaken") String actualProblem,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where a.actualProblem.subCategory.helpDeskTicketCategory.id =:categoryId")
	Page<ActionTaken> getByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where a.actualProblem.subCategory.id=:subCategoryId")
	Page<ActionTaken> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where a.isActive=:status")
	Page<ActionTaken> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where a.actionTaken =:actionTaken")
	Page<ActionTaken> getByActionTaken(@Param("actionTaken") String actualProblem, Pageable pageable);

	@Query("SELECT a FROM ActionTaken a where a.actualProblem.subCategory.helpDeskTicketCategory.id =:categoryId and a.actualProblem.subCategory.id=:subCategoryId and a.isActive=true and a.actualProblem.id=:actualProblem")
	List<ActionTaken> findByCategoryAndSubcategoryIdAndActualProblem(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("actualProblem") Long actualProblem);
	
	
	
	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.id=:actualProblem and a.actionTaken =:actionTaken")
	Page<ActionTaken> getByActualProblemAndActionTaken(@Param("actualProblem") Long actualProblem,@Param("actionTaken") String actiontaken, Pageable pageable);

	
	@Query("SELECT a FROM ActionTaken a where  a.actualProblem.id=:actualProblem ")
	Page<ActionTaken> getByActualProblem(@Param("actualProblem") Long actualProblem, Pageable pageable);

	
}