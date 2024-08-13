package com.oasys.helpdesk.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.CLSRegisterEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
@Repository
public interface CLSRepository extends JpaRepository<CLSRegisterEntity, Long>{
	
	Optional<CLSRegisterEntity> findByUsernameIgnoreCase(String username);

}
