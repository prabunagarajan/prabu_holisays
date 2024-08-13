package com.oasys.posasset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.POSAssetRequestEntity;

@Repository
public interface POSAssetRequestRepository extends JpaRepository<POSAssetRequestEntity, Long>{

}
