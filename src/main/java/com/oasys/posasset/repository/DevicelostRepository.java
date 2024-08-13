package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.GrievanceSlaEntity;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.entity.DevicelostEntity;

@Repository
public interface DevicelostRepository extends JpaRepository<DevicelostEntity, Long>{
	
	
	List<DevicelostEntity> findAllByOrderByCreatedDateDesc();
		
	@Query("SELECT a FROM DevicelostEntity a where  a.applicationNumber =:applicationNumber")
	Optional<DevicelostEntity> findByApplicationNumber(@Param("applicationNumber") String applnno);
	
	@Query(value="select count(id) from device_lost_main where date(created_date)=current_date()",nativeQuery=true)
	Long findCurrentDateDeviceReturnCount();	

	@Query("SELECT a FROM DevicelostEntity a where  a.currentlyWorkwith =:currentlyWorkwith")
	List<DevicelostEntity> findByCurrentlyWorkwithOrderByCreatedDateDesc(@Param("currentlyWorkwith") String currentlyWorkwith);

	List<DevicelostEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	Optional<DevicelostEntity> findByLicenseNoAndShopId(String licenseNo, String shopId);
	
	
	@Query("SELECT a FROM DevicelostEntity a where a.licenseNo=:licenseNo and a.shopId =:shopId and a.deviceId.id =:deviceId")
	Optional<DevicelostEntity> findByLicenseNoAndShopIdAndDeviceId(@Param("licenseNo") String licenseNo, @Param("shopId") String shopId, @Param("deviceId") Long deviceid);
	

}
