package com.oasys.helpdesk.repository;

import com.oasys.helpdesk.dto.KnowledgeBaseResponseDTO;
import com.oasys.helpdesk.entity.KnowledgeBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface KnowledgeRepository extends JpaRepository<KnowledgeBase, Long> {

	@Query("select h from KnowledgeBase h where h.kbId=:id ")
	KnowledgeBase getById(@Param("id") Long id);

	@Query("select h from KnowledgeBase h where h.kbId=:kbId ")
	Optional<KnowledgeBase> findByKbId(@Param("kbId") Long kbId);

	@Query(value = "SELECT a FROM KnowledgeBase a ")
	Page<KnowledgeBase> getAll(Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and a.subcategoryId.id =:subcategoryId and a.issueDetailsEntity.id =:issueDetailsId and Upper(a.status) =:status")
	Page<KnowledgeBase> getByCategoryAndSubCategoryAndIssueDetailsAndStatus(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId, @Param("issueDetailsId") Long issueDetailsId, @Param("status") String status, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and a.subcategoryId.id =:subcategoryId and a.issueDetailsEntity.id =:issueDetailsId")
	Page<KnowledgeBase> getByCategoryAndSubCategoryAndIssueDetails(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId, @Param("issueDetailsId") Long issueDetailsId, Pageable pageable);

	
	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and a.subcategoryId.id =:subcategoryId and a.issueDetails =:issueDetails")
	Page<KnowledgeBase> getByCategoryAndSubCategoryAndIssueDetailsname(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId, @Param("issueDetails") String issueDetails, Pageable pageable);

	
	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and a.subcategoryId.id =:subcategoryId")
	Page<KnowledgeBase> getByCategoryAndSubCategory(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and Upper(a.status) =:status")
	Page<KnowledgeBase> getByCategoryAndStatus(@Param("categoryId") Long categoryId, @Param("status") String status, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and a.issueDetailsEntity.id =:issueDetailsId")
	Page<KnowledgeBase> getByCategoryAndIssueDetails(@Param("categoryId") Long categoryId, @Param("issueDetailsId") Long issueDetailsId, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.subcategoryId.id =:subcategoryId and Upper(a.status) =:status")
	Page<KnowledgeBase> getBySubCategoryAndStatus(@Param("subcategoryId") Long subcategoryId, @Param("status") String status, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.subcategoryId.id =:subcategoryId and a.issueDetailsEntity.id =:issueDetailsId")
	Page<KnowledgeBase> getBySubCategoryAndIssueDetails(@Param("subcategoryId") Long subcategoryId, @Param("issueDetailsId") Long issueDetailsId, Pageable pageable);

//	@Query("SELECT a FROM KnowledgeBase a where a.subcategoryId.id =:subcategoryId and a.issueDetails =:issueDetails")
//	Page<KnowledgeBase> getByIssueDetails(@Param("subcategoryId") Long subcategoryId, @Param("issueDetailsId") String issueDetails, Pageable pageable);

	
	@Query("SELECT a FROM KnowledgeBase a where a.issueDetailsEntity.id =:issueDetailsId and Upper(a.status) =:status")
	Page<KnowledgeBase> getByIssueDetailsAndStatus(@Param("issueDetailsId") Long issueDetailsId, @Param("status") String status, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId")
	Page<KnowledgeBase> getByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.subcategoryId.id =:subcategoryId")
	Page<KnowledgeBase> getBySubCategory(@Param("subcategoryId") Long subcategoryId, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where a.issueDetailsEntity.id =:issueDetailsId")
	Page<KnowledgeBase> getByIssueDetails(@Param("issueDetailsId") Long issueDetailsId, Pageable pageable);

	@Query("SELECT a FROM KnowledgeBase a where Upper(a.status) =:status")
	Page<KnowledgeBase> getByStatus(@Param("status") String status, Pageable pageable);

	@Query(value = "SELECT status, COUNT(*) AS COUNT FROM knowledge_base GROUP BY status", nativeQuery=true)
	List<Map<String, String>> getKnowledgeCountByStatus();

	@Query("SELECT a FROM KnowledgeBase a where a.categoryId.id =:categoryId and a.subcategoryId.id =:subcategoryId and Upper(a.status) =:status")
	Page<KnowledgeBase> getByCategoryAndSubCategoryAndStatus(@Param("categoryId") Long categoryId, @Param("subcategoryId") Long subcategoryId, @Param("status") String status, Pageable pageable);

	@Query(value = "SELECT SUM(count1) FROM knowledge_base a where a.issue_details_id =:issueDetailsId", nativeQuery=true)
	Integer getByIssueDetailsId(@Param("issueDetailsId") Long issueDetailsId);

	//@Query(value = "SELECT * FROM knowledge_base a order by a.modifiedDate Desc", nativeQuery=true)
	List<KnowledgeBase> findAllByOrderByModifiedDateDesc();

	
}