package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;
import com.oasys.helpdesk.entity.Status;



@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
	
	@Query("select h from Status h where h.id=:id and h.isActive=true ")
	Status getById(@Param("id") Long id);
	
	@Query(value = "select * from help_desk_ticket_status where is_active=true and id in(4,5,6)", nativeQuery=true)
	List<Status> getHelpDeskStatus();
	
	@Query(value = "select * from help_desk_ticket_status where is_active=true and id in(8,9,10)", nativeQuery=true)
	List<Status> getHelpDeskStatusForFieldAgent();

	
}