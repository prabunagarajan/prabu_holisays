package com.oasys.helpdesk.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.dto.SiteVisitUserResponseDTO;
import com.oasys.helpdesk.entity.AssetListEntity;
import com.oasys.helpdesk.entity.SiteVisitEntity;
import com.oasys.helpdesk.entity.SlaMasterEntity;

@Repository

public interface SiteVisitRepository extends JpaRepository<SiteVisitEntity, Long>{

	
	//@Query("select a from SiteVisitEntity a where a.shopCode=:shopCode")
	//Optional<SiteVisitEntity> getByShopcode(@Param("shopCode") String shopCode);
	
	@Query("select a from SiteVisitEntity a where a.ticketNumber=:ticketNumber")
	Optional<SiteVisitEntity> getByTicketNumber(@Param("ticketNumber") String ticketNumber);

	List<SiteVisitEntity> findAllByOrderByModifiedDateDesc();

	Optional<SiteVisitEntity> findByshopCodeIgnoreCaseAndIdNot(String shopCode, Long id);

	@Query(value="select * from site_visit sv where date(sv.created_date) between :fromDate and :toDate AND sv.created_by=:userid",nativeQuery=true)
	List<SiteVisitEntity> getCountByStatusAndCreatedDateandToDate(@Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("userid") Long userid);
	
}
