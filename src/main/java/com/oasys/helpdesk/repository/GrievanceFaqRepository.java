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
import com.oasys.helpdesk.entity.GrievanceFaq;	

@Repository
public interface GrievanceFaqRepository extends JpaRepository<GrievanceFaq, Long>{

	Optional<GrievanceFaq> findByQuestionIgnoreCase(@Param("question") String question);

	Optional<GrievanceFaq> findByCodeIgnoreCase(@Param("code") String code);

	List<GrievanceFaq> findAllByOrderByModifiedDateDesc();

	List<GrievanceFaq> findAllByStatusOrderByModifiedDateDesc(Boolean status);


	@Query("SELECT a FROM GrievanceFaq a where a.issueDetails.category.id=:categoryId and  a.question=:question"  )
	Page<GrievanceFaq> getBySubStringAndStatus(@Param("categoryId")  Long categoryNameO, @Param("question")   String question,
			Pageable pageable);
	
	

	@Query("SELECT a FROM GrievanceFaq a where a.issueDetails.category.id =:categoryId" )
	Page<GrievanceFaq> getByCategoryNameO(@Param("categoryId") Long categoryNameO, Pageable pageable);

	@Query("SELECT a FROM GrievanceFaq a where a.question like %:question%" )
	Page<GrievanceFaq> getBySubString( @Param("question")  String question, Pageable pageable);

	@Query("SELECT a FROM GrievanceFaq a where a.issueDetails.id=:issueDetailsId and  a.status=true order by a.modifiedDate desc "  )
	List<GrievanceFaq> findByIssueDetailsId(@Param("issueDetailsId") Long issueDetailsId);

	@Query( "select a from GrievanceFaq a WHERE a.typeofUser LIKE :typeofUser" )
	Page<GrievanceFaq> getByTypeOfUser( @Param("typeofUser")  String typeofUser, Pageable pageable);

}
