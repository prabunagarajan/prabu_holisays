package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.entity.GrievanceWorkflowEntity;

@Repository
public interface GrievanceIssueDetailsRepository extends JpaRepository<GrievanceIssueDetails, Long>{

	GrievanceIssueDetails getById(Long id);

	Optional<GrievanceIssueDetails> findByIssuecodeIgnoreCase(String code);

	
	@Query(value ="select a.* from grievance_issue_details a where a.is_active = true order by a.modified_date desc", nativeQuery=true)
	List<GrievanceIssueDetails> findAllByIsActiveOrderByModifiedDateDesc();
	
	@Query("SELECT a FROM GrievanceIssueDetails a where a.category.id=:categoryId and  a.issueName=:issueName"  )
	Page<GrievanceIssueDetails> getBySubStringAndStatus(@Param("categoryId")  Long categoryId, @Param("issueName")  String issueDetails, Pageable pageable);

	@Query("SELECT a FROM GrievanceIssueDetails a where a.category.id=:categoryId and  a.issueName=:issueName and a.typeofUser=:typeofUser"  )
	Page<GrievanceIssueDetails> getBySubStringAndStatusType(@Param("categoryId")  Long categoryId, @Param("issueName")  String issueDetails,@Param("typeofUser")  String typeofUser, Pageable pageable);

	@Query("SELECT a FROM GrievanceIssueDetails a where a.category.id = :categoryId"  )
	Page<GrievanceIssueDetails> getByCategoryNameO(@Param("categoryId") Long categoryId, Pageable pageable);

	@Query("SELECT a FROM GrievanceIssueDetails a where a.category.id = :categoryId"  )
	Page<GrievanceIssueDetails> getByCategoryNameType(@Param("categoryId") Long categoryId, Pageable pageable);
	
	@Query("SELECT a FROM GrievanceIssueDetails a ")
	Page<GrievanceIssueDetails> getByCategoryNull(Pageable pageable);
	
	@Query("SELECT a FROM GrievanceIssueDetails a where a.issueName = :issueName"  )
	GrievanceIssueDetails getByIssueDetails(@Param("issueName")String issueName);

	@Query("SELECT a FROM GrievanceIssueDetails a where a.issueName = :issueName"  )
	Page<GrievanceIssueDetails> getByIssueDetail(@Param("issueName") String issueDetails, Pageable pageable);
	//
	@Query("SELECT a FROM GrievanceIssueDetails a where a.typeofUser = :typeofUser"  )
	Page<GrievanceIssueDetails> getByIssueDetailType(@Param("typeofUser") String typeofUser, Pageable pageable);


	@Query(value ="select a from GrievanceIssueDetails a where a.category.id =:categoryId")
	List<GrievanceIssueDetails> findAllByCategory(@Param("categoryId") Long categoryId);

	@Query(value ="select a from GrievanceIssueDetails a where a.category.id =:categoryId and a.typeofUser = :typeofUser")
	List<GrievanceIssueDetails> findAllByCategoryAndTypeofUser(@Param("categoryId") Long categoryId,@Param("typeofUser") String typeofUser);


//	
//	List<GrievanceIssueDetails> findAllByCategory(@Param("categoryName") GrievanceCategoryEntity categoryNameO);

	@Query(value ="select a from GrievanceIssueDetails a where a.id =:id")
	List<GrievanceIssueDetails> findAllById(@Param("id") Long id);
	
//	@Query(value ="select a from GrievanceIssueDetails a where a.id =:id")
//	Integer findAllById(@Param("id") Long id);

}
