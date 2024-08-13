package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.posasset.entity.SIMEntity;
import com.oasys.posasset.entity.SIMProviderDetEntity;
@Repository
public interface SIMProviderRepository  extends JpaRepository<SIMProviderDetEntity, Long>{
	
	Optional<SIMProviderDetEntity> findByProvidernameIgnoreCase(String providername);
	
	@Query(value ="select a.* from sim_provider_details a where a.status = true order by a.modified_date desc", nativeQuery=true)
	List<SIMProviderDetEntity> findAllByStatusOrderByModifiedDateDesc();

	SIMProviderDetEntity getById(Long simproviderdetId);
	
	List<SIMProviderDetEntity> findAllByOrderByModifiedDateDesc();
	
}
