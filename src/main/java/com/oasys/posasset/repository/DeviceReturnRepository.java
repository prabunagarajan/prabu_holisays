package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oasys.posasset.entity.DeviceReturnEntity;

public interface DeviceReturnRepository extends JpaRepository<DeviceReturnEntity, Long> {
	
	Optional<DeviceReturnEntity> findByLicenseNoAndShopId(String licenseNo, String shopId);
	
	Optional<DeviceReturnEntity> findByApplicationNumber(String licenseNo);
	
	List<DeviceReturnEntity> findByCurrentlyWorkwithOrderByIdDesc(String designationCode);
	
	List<DeviceReturnEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	@Query(value="select count(id) from device_return_config where date(created_date)=current_date()",nativeQuery=true)
	Long findCurrentDateDeviceReturnCount();	

}
