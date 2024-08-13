package com.oasys.helpdesk.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.oasys.helpdesk.entity.GrievanceTicketStatusEntity;

@Repository
public interface GrievanceTicketStatusRespository extends JpaRepository<GrievanceTicketStatusEntity, Long> {

	Optional<GrievanceTicketStatusEntity> findByTicketstatusnameIgnoreCase(String ticket);

	Optional<GrievanceTicketStatusEntity> findByTicketstatusCodeIgnoreCase(String code);

	@Query(value = "select a.* from grievance_ticket_status_help a where a.status = true order by a.modified_date desc", nativeQuery = true)
	List<GrievanceTicketStatusEntity> findAllByStatusOrderByModifiedDateDesc();

}
