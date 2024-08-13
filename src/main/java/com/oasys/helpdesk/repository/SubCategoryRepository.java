package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
//	@Query(value = "select s.* from help_desk_ticket_sub_category s where s.is_active=true and s.category_id=:categoryid ", nativeQuery=true)
//	List<SubCategory> getSubCategoryByCategoryId(@Param("categoryid") Long categoryid);
//	
	@Query(value = "select s.* from help_desk_ticket_sub_category s where  s.category_id=:categoryid and s.is_active = true", nativeQuery = true)
	List<SubCategory> getSubCategoryByCategoryId(@Param("categoryid") Long categoryid);

	@Query("select h from SubCategory h where h.id=:id ")
	SubCategory getById(@Param("id") Long id);

	@Query(value = "select s from SubCategory s where  s.helpDeskTicketCategory.id=:categoryid and Upper(s.subCategoryName) =:subCategoryName")
	Optional<SubCategory> findByCategoryIdAndSubCategoryNameIgnoreCase(@Param("categoryid") Long categoryid,
			@Param("subCategoryName") String subCategoryName);

	@Query(value = "select s from SubCategory s where s.helpDeskTicketCategory.id=:categoryid and Upper(s.subCategoryName) =:subCategoryName and id !=:id")
	Optional<SubCategory> findByCategoryIdAndSubCategoryNameIgnoreCaseNotInId(@Param("categoryid") Long categoryid,
			@Param("subCategoryName") String subCategoryName, @Param("id") Long id);

	Optional<SubCategory> findByCodeIgnoreCase(@Param("code") String code);

	@Query(value = "select a.* from help_desk_ticket_sub_category a where a.is_active =:isActive order by a.modified_date desc", nativeQuery = true)
	List<SubCategory> findAllByStatusOrderByModifiedDateDesc(@Param("isActive") Boolean isActive);

	@Query("SELECT a FROM SubCategory a where  a.id =:id and a.active=:status")
	Page<SubCategory> getBySubCategoryIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT a FROM SubCategory a where  a.helpDeskTicketCategory.id =:id and a.active=:status")
	Page<SubCategory> getByCategoryIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);

	@Query("SELECT a FROM SubCategory a where  a.helpDeskTicketCategory.id =:categoryId and a.id=:id")
	Page<SubCategory> getByCategoryIdAndSubCategoryId(@Param("categoryId") Long categoryId, @Param("id") Long id,
			Pageable pageable);

	@Query("SELECT a FROM SubCategory a where  a.id =:id and a.helpDeskTicketCategory.id  =:categoryId and a.active=:status")
	Page<SubCategory> getBycategoryIdStatusAndSubCategoryId(@Param("categoryId") Long categoryId,
			@Param("status") Boolean status, @Param("id") Long id, Pageable pageable);

	@Query("SELECT a FROM SubCategory a where  a.id =:id")
	Page<SubCategory> getBySubCategoryId(@Param("id") Long id, Pageable pageable);

	@Query("SELECT a FROM SubCategory a where  a.helpDeskTicketCategory.id =:categoryId")
	Page<SubCategory> getByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM SubCategory a where  a.active=:status")
	Page<SubCategory> getByStatus(@Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM SubCategory a ")
	Page<SubCategory> getAll(Pageable pageable);

	// List<SubCategory> findAllByActiveOrderByModifiedDateDesc(Boolean status);

	List<SubCategory> findAllByOrderByModifiedDateDesc();

}