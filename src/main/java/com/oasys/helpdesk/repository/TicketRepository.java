package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Ticket;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.Priority;




@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query("select h from Ticket h where h.id=:id and h.isActive=true ")
    Ticket getById(@Param("id") Long id);
	
	@Query(value = "select count(*) from help_desk_ticket where MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4", nativeQuery=true)
	Integer getTotalTicketCount();
	
	@Query(value = "select count(*) from help_desk_ticket where ticket_status_id=1 and MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4", nativeQuery=true)
	Integer getOpenTicketCount();
	
	@Query(value = "select count(*) from help_desk_ticket where ticket_status_id in(2,3,4,5) and MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4", nativeQuery=true)
	Integer getInProgressTicketCount();
	
	@Query(value = "select count(*) from help_desk_ticket where ticket_status_id=6 and MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4", nativeQuery=true)
	Integer getClosedTicketCount();
	
	@Query(value = "SELECT * FROM help_desk_ticket ORDER BY id DESC LIMIT 1", nativeQuery=true)
	Ticket getLastTicketValue();
	
	@Query(value = "SELECT * FROM help_desk_ticket where is_active=true  ORDER BY id DESC ", nativeQuery=true)
	List<Ticket> getallActiveTickets();
	
	@Query(value = "SELECT * FROM help_desk_ticket where is_active=true and group_member_id=:id  ORDER BY modified_date DESC ", nativeQuery=true)
	List<Ticket> getallFieldEngineerActiveTickets(@Param("id") Long id);
	
	@Query(value = "select count(*) from help_desk_ticket where group_member_id=:id and category_id=4 and MONTH(created_date)=MONTH(CURRENT_DATE)", nativeQuery=true)
	Integer getFieldAgentTotalTicketCount(@Param("id") Long id);
	
	@Query(value = "select count(*) from help_desk_ticket where group_member_id=:id and category_id=4 and ticket_status_id=1 and MONTH(created_date)=MONTH(CURRENT_DATE)", nativeQuery=true)
	Integer getFieldAgentOpenTicketCount(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM help_desk_ticket where group_member_id=:id order by modified_date DESC LIMIT 5", nativeQuery=true)
	List<Ticket> getRecentTicketForFieldAgentById(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM help_desk_ticket where is_active=true and category_id=:categoryid  ORDER BY id DESC ", nativeQuery=true)
	List<Ticket> getallActiveTicketsByCategory(@Param("categoryid") Long categoryid);
	
	@Query(value = "select * from help_desk_ticket where ticket_status_id=:statusid and MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4", nativeQuery=true)
	List<Ticket> getallActiveTicketsByStatus(@Param("statusid") Long statusid);
//	@Query(value = "select count(*) from help_desk_ticket where ticket_status_id in(8,9) and MONTH(created_date)=MONTH(CURRENT_DATE)", nativeQuery=true)
//	Integer getFieldAgentInProgressTicketCount(@Param("id") Long id);
//	
//	@Query(value = "select count(*) from help_desk_ticket where group_member_id=:id and ticket_status_id=10 and MONTH(created_date)=MONTH(CURRENT_DATE)", nativeQuery=true)
//	Integer getFieldAgentRectifiedTicketCount(@Param("id") Long id);
	
	
	@Query(value = "select count(*) from help_desk_ticket where MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4 and priority_id=2 and group_member_id=:id", nativeQuery=true)
	Integer getP1Count(@Param("id") Long id);
	
	@Query(value = "select count(*) from help_desk_ticket where MONTH(created_date)=MONTH(CURRENT_DATE) and category_id=4 and priority_id=4 and group_member_id=:id", nativeQuery=true)
	Integer getP2Count(@Param("id") Long id);
	 
}   