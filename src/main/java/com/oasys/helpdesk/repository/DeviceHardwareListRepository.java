//package com.oasys.helpdesk.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.oasys.helpdesk.entity.DeviceHardwareListEntity;
//
//@Repository
//public interface DeviceHardwareListRepository extends JpaRepository<DeviceHardwareListEntity, Long>{
//
//	@Query("SELECT a FROM DeviceHardwareListEntity a where  a.deviceCode =:deviceCode")
//	Optional<DeviceHardwareListEntity> findByDeviceCode(String deviceCode);
//
//	@Query("SELECT a FROM DeviceHardwareListEntity a where  a.deviceCode =:deviceCode")
//	Optional<List<DeviceHardwareListEntity>> findByListDeviceCode(@Param("deviceCode") String deviceCode);
//
//	List<DeviceHardwareListEntity> findAllByOrderByModifiedDateDesc();
//
//	Optional<DeviceHardwareListEntity> findByDeviceIdIgnoreCase(String deviceId);
//
//	List<DeviceHardwareListEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);
//
//}
