package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.DeviceDamageEntity;
import com.oasys.posasset.entity.DevicelostEntity;

@Repository
public interface DevicedamageRepository extends JpaRepository<DeviceDamageEntity, Long>{
	
	
	List<DeviceDamageEntity> findAllByOrderByCreatedDateDesc();
		
	@Query("SELECT a FROM DeviceDamageEntity a where  a.deviceDamageapplnno =:deviceDamageapplnno")
	Optional<DeviceDamageEntity> findByDeviceDamageapplnno(@Param("deviceDamageapplnno") String applnno);
	
	@Query(value="select count(id) from device_damage where date(created_date)=current_date()",nativeQuery=true)
	Long findCurrentDateDeviceReturnCount();	

	@Query("SELECT a FROM DeviceDamageEntity a where  a.currentlyWorkwith =:currentlyWorkwith")
	List<DeviceDamageEntity> findByCurrentlyWorkwithOrderByCreatedDateDesc(@Param("currentlyWorkwith") String currentlyWorkwith);

	List<DeviceDamageEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	//Optional<DeviceDamageEntity> findByLicenseNoAndShopId(String licenseNo, String shopId);
	
	
	@Query("SELECT a FROM DeviceDamageEntity a where a.licenseNo=:licenseNo and a.shopId =:shopId and a.deviceId.id =:deviceId")
	Optional<DeviceDamageEntity> findByLicenseNoAndShopIdAndDeviceId(@Param("licenseNo") String licenseNo, @Param("shopId") String shopId, @Param("deviceId") Long deviceid);
	

}
