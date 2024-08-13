package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceFaq;
import com.oasys.helpdesk.entity.GrievanceKnowledgeBase;

@Repository
public interface GrievanceKnowledgeBaseRepository extends JpaRepository<GrievanceKnowledgeBase, Long> {

	@Query("select h from GrievanceKnowledgeBase h where h.code=:code")
	Optional<GrievanceKnowledgeBase> findByCode(String code);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.issueDetails.id =:issueDetails and a.status =:status")
	Page<GrievanceKnowledgeBase> getByCategoryAndIssueDetailsAndStatus(@Param("categoryId") Long categoryId,
			@Param("issueDetails") Long issueDetails, @Param("status") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.issueDetails.id =:issueDetails and a.status =:status and a.typeofUser =:typeofUser")
	Page<GrievanceKnowledgeBase> getByCategoryAndIssueDetailsAndStatusType(@Param("categoryId") Long categoryId,
			@Param("issueDetails") Long issueDetails, @Param("status") Boolean status,@Param("typeofUser") String typeofUser, Pageable pageable);


	@Query(value = "SELECT a FROM GrievanceKnowledgeBase a ")
	Page<GrievanceKnowledgeBase> getAll(Pageable pageable);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.issueDetails.id =:issueDetails")
	Page<GrievanceKnowledgeBase> getByCategoryAndIssueDetails(@Param("categoryId") Long category,
			@Param("issueDetails") Long issueDetails, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.issueDetails.id =:issueDetails")
	Page<GrievanceKnowledgeBase> getByCategoryAndIssueDetailsTypeNull(@Param("categoryId") Long category,
			@Param("issueDetails") Long issueDetails, Pageable pageable);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where  a.issueDetails.id =:issueDetails and a.status =:status")
	Page<GrievanceKnowledgeBase> getByIssueDetailsAndStatus(@Param("issueDetails") Long issueDetails, Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where  a.issueDetails.id =:issueDetails and a.status =:status  and a.typeofUser =:typeofUser")
	Page<GrievanceKnowledgeBase> getByIssueDetailsAndStatusType(@Param("issueDetails") Long issueDetails, Boolean status,
			@Param("typeofUser") String typeofUser,Pageable pageable);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId")
	Page<GrievanceKnowledgeBase> getByCategory(@Param("categoryId") Long category, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId")
	Page<GrievanceKnowledgeBase> getByCategoryType(@Param("categoryId") Long category, Pageable pageable);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.status =:status")
	Page<GrievanceKnowledgeBase> getByCategoryAndStatus(@Param("categoryId") Long category,
			@Param("status") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.status =:status and a.typeofUser =:typeofUser")
	Page<GrievanceKnowledgeBase> getByCategoryAndStatuss(@Param("categoryId") Long category,
			@Param("status") Boolean status, @Param("typeofUser")String typeofUser,Pageable pageable);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.status =:status")
	Page<GrievanceKnowledgeBase> getByStatus(@Param("status") Boolean status, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.status =:status and a.typeofUser =:typeofUser")
	Page<GrievanceKnowledgeBase> getByStatusType(@Param("status") Boolean status,@Param("typeofUser") String typeofUser, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.typeofUser =:typeofUser")
	Page<GrievanceKnowledgeBase> getByStatusNullType(@Param("typeofUser") String typeofUser, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceKnowledgeBase a where  a.issueDetails.id =:issueDetails and a.typeofUser =:typeofUser")
	Page<GrievanceKnowledgeBase> getByStatusNullTypes(@Param("issueDetails") Long issueDetails,@Param("typeofUser") String typeofUser, Pageable pageable);


	@Query("SELECT a FROM GrievanceKnowledgeBase a ")
	Page<GrievanceKnowledgeBase> getByStatusAllNull( Pageable pageable);

	
	Optional<GrievanceKnowledgeBase> findByCodeIgnoreCase(String code);

	@Query("SELECT a FROM GrievanceKnowledgeBase a where a.categoryId.id =:categoryId and a.issueDetails.id =:issueDetailsId and a.status =true")
	List<GrievanceKnowledgeBase> findByCategoryAndIssueDetailsId(@Param("categoryId") Long category,
			@Param("issueDetailsId") Long issueDetails);

	@Query(value = "SELECT SUM(count1) FROM grievance_knowledge_base a where a.issue_details =:issueDetailsId", nativeQuery = true)
	Integer getByIssueDetailsId(@Param("issueDetailsId") Long issueDetailsId);

	List<GrievanceKnowledgeBase> findAllByOrderByModifiedDateDesc();

	@Query(value = "SELECT status, COUNT(*) AS count\n" + 
			"FROM grievance_knowledge_base\n" + 
			"GROUP BY status", nativeQuery = true)
	List<Map<String, String>> getGrievanceKnowledgeCountByStatus();
	
}
