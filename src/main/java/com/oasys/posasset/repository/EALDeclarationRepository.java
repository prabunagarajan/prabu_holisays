package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.dto.StockEALDECAECDTO;
import com.oasys.posasset.entity.EALDeclarationEntity;

@Repository
public interface EALDeclarationRepository extends JpaRepository<EALDeclarationEntity, Long> {
	
	@Query(value = "SELECT er.requested_appln_no AS bottlingplanid,bottling_plan_id AS decbottlingplanid " +
            "FROM eal_declaration edd " + 
            "RIGHT JOIN eal_request_aec er ON er.license_no = edd.license_no " + 
            "WHERE er.license_no = :license_no and edd.bottling_plan_id is null", nativeQuery = true)
List<StockEALDECAECDTO> getBYLicenseNo(@Param("license_no") String license_no);

	
	
}
