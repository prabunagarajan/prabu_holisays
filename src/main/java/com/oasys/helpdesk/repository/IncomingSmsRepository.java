package com.oasys.helpdesk.repository;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.IncomingSMSEntity;



@Repository
public interface IncomingSmsRepository extends JpaRepository<IncomingSMSEntity, UUID> {
	
	@Query("select distinct(count(a.id)) from IncomingSMSEntity a where  date(a.createdDate) between :fromDate and :toDate")
	Integer getCountByCreatedDateBetween( @Param("fromDate") Date fromDate,@Param("toDate") Date toDate);
	
	

}