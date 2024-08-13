package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.IssueFromEntity;

public interface IssueFromRepository extends JpaRepository<IssueFromEntity, Long>{

	List<IssueFromEntity> findAll();

	Optional<IssueFromEntity> findByIssueFromCodeIgnoreCase(String code);

	Optional<IssueFromEntity> findByIssueFromIgnoreCase(String issueFrom);
	
	
	
	//List<IssueFromEntity> findAllByIssueFromInIgnoreCase(List<String> issueFrom);
	

	List<IssueFromEntity> findAllByOrderByModifiedDateDesc();

	@Query("SELECT a FROM IssueFromEntity a where  Upper(a.issueFrom) =:issueFrom and a.id !=:id")
	Optional<IssueFromEntity> findByIssueFromIgnoreCaseNotInId(@Param("issueFrom") String type, @Param("id") Long id);

	@Query(value ="select a.* from issue_from a where a.is_active = true order by a.modified_date desc", nativeQuery=true)
	List<IssueFromEntity> findAllByStatusOrderByModifiedDateDesc();

}
