package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.entity.SupplierEntity;
import com.oasys.posasset.entity.DeviceReturnEntity;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long>{

	Optional<SupplierEntity> findByMobileNumberAndEmailIdAndSupplier(String mobileno,String emailid,String supplier);

	
	@Query(value="select * from supplier_master  where active = 1",nativeQuery = true)
	List<SupplierEntity>findAllByIsActiveOrderByModifiedDateDesc(@Param("pass") Boolean pass);
	
	Optional<SupplierEntity> findBySupplierNameIgnoreCase(@Param("supplierName") String supplierName);
	
	
}
