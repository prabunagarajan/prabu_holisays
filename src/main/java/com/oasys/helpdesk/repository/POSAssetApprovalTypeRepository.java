package com.oasys.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.POSAssetApprovalType;
import com.oasys.helpdesk.utility.ApprovalType;

@Repository
public interface POSAssetApprovalTypeRepository extends JpaRepository<POSAssetApprovalType, Long>{

	Optional<POSAssetApprovalType> findByApprovalType(ApprovalType code);
	
	@Query("select p from POSAssetApprovalType p where p.id !=:id ")
	Optional<POSAssetApprovalType> findRecordsNotInId(@Param("id") Long id);
	
	Optional<POSAssetApprovalType> findByStatus(Boolean status);
}
