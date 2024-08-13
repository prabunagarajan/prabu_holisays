package com.oasys.helpdesk.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.TicketStatusEntity;
@Repository
public interface TicketStatusrepository extends JpaRepository<TicketStatusEntity, Long> {
	
     //Optional<TicketStatusEntity> findByDepartmentIgnoreCase(String department);
	
	

	Optional<TicketStatusEntity> findByTicketstatusnameIgnoreCase(String ticket);

	
	
    Optional<TicketStatusEntity> findByTicketstatusCodeIgnoreCase(String code);
	
	List<TicketStatusEntity> findAllByOrderByModifiedDateDesc();
	
	
	@Query("SELECT a FROM TicketStatusEntity a WHERE LOWER(a.ticketstatusname) = LOWER(:ticketstatusname) AND a.id != :id")
	Optional<TicketStatusEntity> findByTicketstatusnameIgnoreCaseNotInId(@Param("ticketstatusname") String ticketstatusname, @Param("id") Long id);
	

	@Query("SELECT a FROM TicketStatusEntity a ")
	Page<TicketStatusEntity> getAll(Pageable pageable);
	

	
	@Query("SELECT a FROM TicketStatusEntity a where  a.id =:ticketStatusId and  a.status=:status")
	Page<TicketStatusEntity> getByIdAndStatus(@Param("ticketStatusId") Long ticketstatusname,@Param("status") String status, Pageable pageable);
	
	
	@Query(value ="select a.* from ticket_status_help a where a.status = true order by a.modified_date desc", nativeQuery=true)
	List<TicketStatusEntity> findAllByStatusOrderByModifiedDateDesc();
	
	@Query("SELECT a FROM TicketStatusEntity a where  a.id =:ticketStatusId ")
	Page<TicketStatusEntity> getById(@Param("ticketStatusId") Long ticketstatusname, Pageable pageable);
	
	@Query("SELECT a FROM TicketStatusEntity a where   a.status=:status ")
	Page<TicketStatusEntity> getByStatus(@Param("status") String status, Pageable pageable);
	
	@Query("SELECT a.id FROM TicketStatusEntity a where  a.ticketstatusname in :ticketstatusname ")
	List<Long> findByTicketStatusNames(@Param("ticketstatusname") List<String> ticketstatusname);
	
	@Query("SELECT a.id FROM TicketStatusEntity a where  a.ticketstatusname in :ticketstatusname ")
	Long findByTicketStatusNames1(@Param("ticketstatusname") String ticketstatusname);
}
