package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.posasset.entity.SIMEntity;
import com.oasys.posasset.entity.SIMProviderDetEntity;


@Repository
public interface SIMRepository extends JpaRepository<SIMEntity, Long> {
	
	Optional<SIMEntity> findByImisIgnoreCase(String imis);
	

	@Query(value ="select a.* from sim a where a.status = true order by a.modified_date desc", nativeQuery=true)
	List<SIMEntity> findAllByStatusOrderByModifiedDateDesc();
	
	List<SIMEntity> findAllByOrderByModifiedDateDesc();
	
	
	@Query("SELECT a FROM SIMEntity a where  (a.imis) =:imis and a.id !=:id")
	Optional<SIMEntity> findByImisIgnoreCaseNotInId(@Param("imis") String imis, @Param("id") Long id);
	

	

}
