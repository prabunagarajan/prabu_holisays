package com.oasys.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.KnowledgeSolution;

@Repository
public interface KnowledgeSolutionRepository extends JpaRepository<KnowledgeSolution, Long> {

	Optional<KnowledgeSolution> findBySolutionIdIgnoreCase(@Param("solutionId") String solutionId);

	Optional<KnowledgeSolution> findBySolutionId(@Param("solutionId") String solutionId);




}
