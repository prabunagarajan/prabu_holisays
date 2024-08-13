package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.HelpdeskUserAuditEntity;

@Repository
public interface HelpdeskUserAuditRepository extends JpaRepository<HelpdeskUserAuditEntity, Long> {

}
