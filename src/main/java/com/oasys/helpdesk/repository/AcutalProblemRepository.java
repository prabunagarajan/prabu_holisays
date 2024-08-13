package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ActualProblem;



@Repository
public interface AcutalProblemRepository extends JpaRepository<ActualProblem, Long> {
	@Query("select h from ActualProblem h where h.id=:id ")
	ActualProblem getById(@Param("id") Long id);
	
	@Query("select h from ActualProblem h where h.actualProblem=:name ")
	ActualProblem getByName(@Param("name") String name);

	List<ActualProblem> findAllByIsActiveOrderByModifiedDateDesc(Boolean status);

	Optional<ActualProblem> findByCodeIgnoreCaseAndActualProblem(String code,String actualproblem);
	
	Optional<ActualProblem> findByCodeIgnoreCase(String code);

	@Query("SELECT a FROM ActualProblem a where a.subCategory.id=:subCategoryId and upper(a.actualProblem) =:actualProblem")
	Optional<ActualProblem> findBySubcategoryAndActualProblem(@Param("subCategoryId") Long subCategoryId, @Param("actualProblem") String actualProblem);
	
	@Query("SELECT a FROM ActualProblem a where a.subCategory.id=:subCategoryId and a.id !=:id and upper(a.actualProblem) =:actualProblem")
	Optional<ActualProblem> findBySubcategoryAndActualProblemNotInId(@Param("subCategoryId") Long subCategoryId, @Param("id") Long id, @Param("actualProblem") String actualProblem);

	@Query("SELECT a FROM ActualProblem a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.actualProblem =:actualProblem and a.isActive=:status")
	Page<ActualProblem> getByCategoryActualPSubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("actualProblem") String actualProblem,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.actualProblem =:actualProblem")
	Page<ActualProblem> getByCategorySubcategoryAndActualP(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("actualProblem") String actualProblem, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.isActive=:status")
	Page<ActualProblem> getByCategorySubcategoryAndStatus(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId")
	Page<ActualProblem> getByCategoryAndSubcategory(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subCategoryId,
			 Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where  a.subCategory.helpDeskTicketCategory.id =:categoryId and a.isActive=:status")
	Page<ActualProblem> getByCategoryIdAndStatus(@Param("categoryId") Long categoryId,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where a.subCategory.id=:subCategoryId and a.isActive=:status")
	Page<ActualProblem> getBySubCategoryIdAndStatus(@Param("subCategoryId") Long subCategoryId,
			@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where  a.actualProblem =:actualProblem and a.isActive=:status")
	Page<ActualProblem> getByActualPAndStatus(@Param("actualProblem") String actualProblem,@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where  a.subCategory.helpDeskTicketCategory.id =:categoryId")
	Page<ActualProblem> getByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where a.subCategory.id=:subCategoryId")
	Page<ActualProblem> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where a.isActive=:status")
	Page<ActualProblem> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM ActualProblem a where a.actualProblem =:actualProblem")
	Page<ActualProblem> getByActualProblem(@Param("actualProblem") String actualProblem, Pageable pageable);
	
	@Query("SELECT a FROM ActualProblem a ")
	Page<ActualProblem> getAll(Pageable pageable);

	List<ActualProblem> findByIsActiveOrderByModifiedDateDesc(@Param("isActive") Boolean status);

	@Query("SELECT a FROM ActualProblem a where a.subCategory.helpDeskTicketCategory.id =:categoryId and a.subCategory.id=:subCategoryId and a.isActive=true")
	List<ActualProblem> findByCategoryAndSubCategory(@Param("categoryId") Long categoryId,@Param("subCategoryId") Long subcategoryId);
	
	
	List<ActualProblem> findAllByOrderByModifiedDateDesc();

}