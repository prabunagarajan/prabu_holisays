package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.CallDisconnectedTickets;
import com.oasys.helpdesk.entity.Ticket;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;




@Repository
public interface CallDisconnectedTicketRepository extends JpaRepository<CallDisconnectedTickets, Long> {

	
	
}   