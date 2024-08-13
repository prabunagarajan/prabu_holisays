package com.oasys.posasset.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.posasset.entity.DeviceEntity;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long>{

	List<DeviceEntity> findAllByOrderByModifiedDateDesc();
	
	Optional<DeviceEntity> findByDeviceNumber(String deviceNumber);
	
	@Query("SELECT d.fpsCode FROM DeviceEntity d where d.deviceNumber=:deviceNumber")
	public String getShopCodeByDeviceNumber(@Param("deviceNumber") String deviceNumber);

	Optional<DeviceEntity> findByMacIdIgnoreCase(String macId);
	
	List<DeviceEntity> findByFpsCode(String fpsCode);
	
	@Query(value="select * from device where device_number=:device_number and active=1",nativeQuery=true)
	Optional<DeviceEntity> findByDeviceNumberr(String device_number);
	
	
	Page<DeviceEntity> findByDeviceNumberAndFpsCode(String devicenumber,String fpscoe, Pageable pageable);
	
	@Query("SELECT t FROM DeviceEntity t where  t.deviceNumber=:deviceNumber and t.fpsCode=:fpsCode")
	Page<DeviceEntity> getByDeviceNumberAndFpsCode(@Param("deviceNumber") String deviceNumber,@Param("fpsCode") String fpsCode,Pageable pageable);
	
	@Query("SELECT t FROM DeviceEntity t where  t.deviceNumber=:deviceNumber ")
	Page<DeviceEntity> getByDeviceNumber(@Param("deviceNumber") String deviceNumber,Pageable pageable);
	
	@Query("SELECT t FROM DeviceEntity t where  t.fpsCode=:fpsCode")
	Page<DeviceEntity> getByFpsCode(@Param("fpsCode") String fpsCode,Pageable pageable);
	

	@Query(value="select * from device where device_number IS NOT NULL  and fps_code IS NOT NULL",nativeQuery=true)
	Page<DeviceEntity> getByMapped(Pageable pageable);
	
	@Query(value="select * from device where device_number IS NOT NULL  and fps_code IS  NULL",nativeQuery=true)
	Page<DeviceEntity> getByUnMapped(Pageable pageable);
	
	
	@Query(value="select * from device where fps_code=:fps_code",nativeQuery=true)
	Page<DeviceEntity> getByMorethanonedevice(@Param("fps_code") String fpsCode,Pageable pageable);
	
	@Query("select distinct(count(a.id)) from DeviceEntity a where a.fpsCode=:fpsCode")
	Integer getCountByShopcode(@Param("fpsCode") String fpsCode);

	
	

}
