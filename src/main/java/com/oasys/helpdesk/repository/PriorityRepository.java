package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;



@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
	@Query("select h from Priority h where h.id=:id and h.isActive=true ")
	Priority getById(@Param("id") Long id);
	
	@Query(value = "select * from help_desk_ticket_priority where is_active=true and id in(2,4)", nativeQuery=true)
	List<Priority> getallActivePriority();
	
	@Query("select p from Priority p where Upper(p.priority) =:priorityName ")
	Priority findByName(@Param("priorityName") String priorityName);

	  
}