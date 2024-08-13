package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.HelpdeskTicketAuditEntity;

@Repository
public interface HelpDeskTicketAuditRepository extends JpaRepository<HelpdeskTicketAuditEntity, Long>{

}
