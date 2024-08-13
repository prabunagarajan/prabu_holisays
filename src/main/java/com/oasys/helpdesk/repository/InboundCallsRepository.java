package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.dto.InboundCallsSummaryDTO;
import com.oasys.helpdesk.entity.InboundCallsEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface InboundCallsRepository extends JpaRepository<InboundCallsEntity, Long> {

	@Query("select i from InboundCallsEntity i where i.id=:id ")
	InboundCallsEntity getById(@Param("id") Long id);;
	
	@Query(value = "SELECT\r\n" + 
			"    SUM(ib.total_calls_received) AS TotalCallsReceived,\r\n" + 
			"    SUM(ib.total_calls_abondoned) AS TotalCallsAbandoned,\r\n" + 
			"    SUM(ib.total_calls_attended) AS TotalCallsAttended,\r\n" + 
			"    (SUM(ib.total_calls_received) - SUM(ib.total_calls_abondoned)) / SUM(ib.total_calls_received) * 100 AS CallsAttendedPercentage\r\n" + 
			"FROM inbound_calls ib\r\n" + 
			"WHERE ib.created_date BETWEEN :fromDate AND :toDate\r\n" + 
			"HAVING\r\n" + 
			"    TotalCallsReceived IS NOT NULL\r\n" + 
			"    AND TotalCallsAbandoned IS NOT NULL\r\n" + 
			"    AND TotalCallsAttended IS NOT NULL\r\n" + 
			"    AND CallsAttendedPercentage IS NOT NULL;\r\n" + 
			"", nativeQuery = true)
	List<InboundCallsSummaryDTO> getTotalCallsSummaryCount(@Param("fromDate")Date fromDate,@Param("toDate")Date toDate);
	
	@Query(value = "select * from inbound_calls ic WHERE date(created_date) =:date", nativeQuery = true)
	List<InboundCallsEntity> findByCreatedDate(LocalDate date);


}
