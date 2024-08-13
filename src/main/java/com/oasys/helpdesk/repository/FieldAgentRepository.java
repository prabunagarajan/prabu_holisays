package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.FieldAgent;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.entity.Priority;



@Repository
public interface FieldAgentRepository extends JpaRepository<FieldAgent, Long> {
	
	
	
	@Query("select h from FieldAgent h where h.id=:id and h.isActive=true  ")
	FieldAgent getById(@Param("id") Long id);

	@Query(value = "select * from help_desk_field_agent where is_active=true and group_id=:groupid ", nativeQuery=true)
	List<FieldAgent> getfieldagentByGroupId(@Param("groupid") Long groupid);
	
}