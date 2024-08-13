package com.oasys.posasset.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.dto.DeviceRegistrationRequestDTO;
import com.oasys.posasset.dto.EntitysummaryDTO;
import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceRegisterEntity;

@Repository
public interface DeviceRegistrationRepository extends JpaRepository<DeviceRegisterEntity, Long>{
	
//	@Query(value="select * from device_registration_details where device_number=:device_number",nativeQuery=true)
//	Optional<DeviceRegisterEntity> findByDeviceNumber(String device_number);
//	
	//Optional<DeviceRegisterEntity> findByDeviceStatus(); 
	
	@Query("SELECT t FROM DeviceRegisterEntity t where  t.fpsCode=:fpsCode")
	List<DeviceRegisterEntity> getByFpsCode(@Param("fpsCode") String fpsCode);
	
	@Query("select distinct(count(a.id)) from DeviceRegisterEntity a where a.fpsCode=:fpsCode")
	Integer getCountByShopcode(@Param("fpsCode") String fpsCode);
	
	@Query(value="select * from device_registration_details where device_number=:device_number and status=2",nativeQuery=true)
	Optional<DeviceRegisterEntity> getByDeviceNumber(String device_number);
	
	@Query(value="select * from device_registration_details where device_number=:device_number",nativeQuery=true)
	Optional<DeviceRegisterEntity> findByDeviceNumber(String device_number);
	
	
	
	@Query(value = "SELECT distinct COUNT(t.id) AS Totaldevice FROM device_registration_details t", nativeQuery=true)
	Integer getTotalDeviceCount();
	
	@Query(value = "SELECT distinct SUM(t.status=(SELECT id from device_status ds where code='APPROVED')) AS Mappeddevice FROM device_registration_details t", nativeQuery=true)
	Integer getMappedDeviceCount();
	
	@Query(value = "SELECT distinct SUM(t.status=(SELECT id from device_status ds where code='PENDING')) AS NotMappeddevice FROM device_registration_details t", nativeQuery=true)
	Integer getNotMappedDeviceCount();
	
	@Query(value = "SELECT distinct SUM(t.status=(SELECT id from device_status ds where code='REJECTED')) AS NotMappeddevice FROM device_registration_details t", nativeQuery=true)
	Integer getDeviceRejectedCount();
	
	@Query(value = "SELECT distinct SUM(t.status=(SELECT id from device_status ds where code='DEVICELOST')) AS DeviceLost FROM device_registration_details t", nativeQuery=true)
	Integer getDeviceLostCount();
	
	@Query(value = "SELECT distinct SUM(t.status=(SELECT id from device_status ds where code='DEVICEREPLACE')) AS DeviceReplace FROM device_registration_details t", nativeQuery=true)
	Integer getDeviceReplaceCount();
	
	@Query(value = "SELECT distinct SUM(t.status=(SELECT id from device_status ds where code='DEVICERETURN')) AS DeviceReturn FROM device_registration_details t", nativeQuery=true)
	Integer getDeviceReturnCount();
	
	
	@Query(value="select drd.entity as entityName,count(distinct case when ds.code='APPROVED' then d.fps_code else null end) as \"mappedDeviceCount\",\r\n" + 
			"count(distinct case when d.asset_type='POS Device' and ds.code='APPROVED' then d.fps_code  else null end) as \"posDevice\",\r\n" + 
			"count(distinct case when d.asset_type='Tab' and ds.code='APPROVED' then d.fps_code  else null end) as \"tab\"\r\n" + 
			"from device d \r\n" + 
			"join device_registration_details drd on drd.device_number=d.device_number \r\n" + 
			"join device_status ds on ds.id=drd.status \r\n" + 
			"where drd.entity is not null\r\n" + 
			"group by drd.entity",nativeQuery = true)
	List<EntitysummaryDTO> getCount();
	
}
