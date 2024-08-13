package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Ticket;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.TicketHistory;
import com.oasys.helpdesk.entity.Priority;




@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {

	@Query("select h from TicketHistory h where h.id=:id and h.isActive=true ")
    TicketHistory getById(@Param("id") Long id);
	
	@Query("select h from TicketHistory h where h.helpDeskTicket.id=:ticketid and h.isActive=true ")
    List<TicketHistory> getByTicketId(@Param("ticketid") Long id);

	@Query(value = "select count(*) from help_desk_ticket_history as a  where a.update_status in(8,9) and MONTH(a.created_date)=MONTH(CURRENT_DATE) group by a.update_status", nativeQuery=true)
	Integer getInProgressTicketCount();
	
	@Query(value = "select count(*) from help_desk_ticket_history as h left join help_desk_ticket as t on h.ticket_id=t.id  where h.update_status=10 and t.ticket_status_id !=6 and MONTH(t.created_date)=MONTH(CURRENT_DATE)", nativeQuery=true)
	Integer getRectifiedTicketCount();
	
	@Query(value = "select count(*) from help_desk_ticket_history as a left join help_desk_ticket as b on a.ticket_id=b.id where assigned_by=:id and a.update_status in(8,9) and MONTH(b.created_date)=MONTH(CURRENT_DATE) and category_id=4 ", nativeQuery=true)
	Integer getFieldAgentInProgressTicketCount(@Param("id") Long id);
	
	@Query(value = "select count(*) from help_desk_ticket_history as h left join help_desk_ticket as t on h.ticket_id=t.id  where h.update_status=10 and t.ticket_status_id !=6 and h.assigned_by=:id and MONTH(t.created_date)=MONTH(CURRENT_DATE)", nativeQuery=true)
    Integer getFieldAgentRectifiedTicketCount(@Param("id") Long id);
	
}   