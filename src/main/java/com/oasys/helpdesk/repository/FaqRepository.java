package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Faq;



@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
	
	@Query("select h from Faq h where h.id=:id  ")
	Faq getById(@Param("id") Long id);

	Optional<Faq> findByCodeIgnoreCase(@Param("code") String code);
	
	Optional<Faq> findByQuestionIgnoreCase(@Param("question") String question);
	List<Faq> findAllByOrderByModifiedDateDesc();
	List<Faq> findByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);
	
	@Query("SELECT a FROM Faq a where  a.subCategoryId.id =:subCategoryId and a.id=:id")
	Page<Faq> getBySubCategoryAndId(@Param("subCategoryId") Long subCategoryId, @Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM Faq a where  a.subCategoryId.helpDeskTicketCategory.id  =:categoryId and a.id=:id")
	Page<Faq> getByCategoryIdAndId(@Param("categoryId") Long type, @Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM Faq a where  a.subCategoryId.helpDeskTicketCategory.id  =:categoryId and a.subCategoryId.id=:subCategoryId")
	Page<Faq> getByCategoryIdAndSubCategoryId(@Param("categoryId") Long categoryId, @Param("subCategoryId") Long subCategoryId,
			Pageable pageable);
	
	@Query("SELECT a FROM Faq a where  a.subCategoryId.id =:subCategoryId and a.subCategoryId.helpDeskTicketCategory.id  =:categoryId and a.id=:id")
	Page<Faq> getByIdCategoryIdAndSubCategoryId(@Param("categoryId") Long categoryId, @Param("id") Long id,
			@Param("subCategoryId") Long subCategoryId, Pageable pageable);
	
	@Query("SELECT a FROM Faq a where  a.subCategoryId.id =:subCategoryId")
	Page<Faq> getBySubCategoryId(@Param("subCategoryId") Long subCategoryId,
			Pageable pageable);
	
	@Query("SELECT a FROM Faq a where  a.subCategoryId.helpDeskTicketCategory.id =:categoryId")
	Page<Faq> getByCategoryId(@Param("categoryId") Long categoryId,
			Pageable pageable);
	
	@Query("SELECT a FROM Faq a where  a.id=:id")
	Page<Faq> getById(@Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM Faq a ")
	Page<Faq> getAll(Pageable pageable);
	
	@Query("SELECT a FROM Faq a where a.subCategoryId.id =:subCategoryId and a.subCategoryId.helpDeskTicketCategory.id =:categoryId")
	Optional<Faq> getById(@Param("subCategoryId") Long subCategoryId,@Param("categoryId") Long categoryId);
	
	@Query("SELECT a FROM Faq a where UPPER(a.question) =:question and a.id !=:id")
	Optional<Faq> findByQuestionNotInId(@Param("question") String question, @Param("id") Long id);
}