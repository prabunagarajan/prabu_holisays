package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Category;




@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("select c from Category c where c.id=:id ")
	Category getById(@Param("id") Long id);
	
	@Query("select c from Category c where Upper(c.categoryName) =:categoryName ")
	Category findByName(@Param("categoryName") String categoryName);
	
	Optional<Category> findByCodeIgnoreCase(@Param("code") String code);
	
	@Query(value ="select a.* from help_desk_ticket_category a where a.is_active = :active order by a.modified_date desc", nativeQuery=true)
	List<Category> findAllByStatusOrderByModifiedDateDesc(@Param("active") Boolean active);
	
	@Query("SELECT a FROM Category a where  Upper(a.categoryName) =:categoryName and a.id !=:id")
	Optional<Category> findByCategoryNameIgnoreCaseNotInId(@Param("categoryName") String categoryName, @Param("id") Long id);

	@Query("SELECT a FROM Category a where  a.id =:id and a.active=:status")
	Page<Category> getByIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM Category a where  a.id =:id")
	Page<Category> getById(@Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM Category a where  a.active=:status")
	Page<Category> getByStatus(@Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM Category a ")
	Page<Category> getAll(Pageable pageable);
	
	//List<Category> findAllByActiveOrderByModifiedDateDesc(Boolean status);
	
	List<Category> findAllByOrderByModifiedDateDesc();
	
}