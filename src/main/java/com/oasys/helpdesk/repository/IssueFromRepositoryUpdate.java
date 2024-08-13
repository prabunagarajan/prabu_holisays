package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.IssueFromEntity;

public interface IssueFromRepositoryUpdate extends JpaRepository<IssueFromEntity, Long>{

	

	Optional<IssueFromEntity> findByIssueFromIgnoreCase(String issueFrom);
	
	
}
