package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.entity.Priority;



@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	
	
	
	@Query("select h from Group h where h.id=:id  ")
	Group getById(@Param("id") Long id);
	
	@Query(value = "select * from act_id_group where name like %:groupName% ", nativeQuery=true)
	List<Group> getgroupByName(@Param("groupName") String groupName);
	
	
	@Query("select h from Group h where h.name=:name  ")
	Group getByName(@Param("name") String name);
	
	
}