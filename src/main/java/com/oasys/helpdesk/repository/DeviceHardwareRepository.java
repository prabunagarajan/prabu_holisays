package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;

@Repository
public interface DeviceHardwareRepository extends JpaRepository<DeviceHardwareEntity, Long> {

	Optional<DeviceHardwareEntity> findByDeviceNameIgnoreCase( String devicenames);

	//	Optional<DeviceHardwareEntity> findByBrandAndType(Long assetBrandId, Long assetTypeId);


//	@Query("SELECT a FROM DeviceHardwareEntity a where  a.brand.id =:brand and a.type.id=:type")
//	Optional<DeviceHardwareEntity> findByBrandAndType(@Param("brand") Long brand, @Param("type") Long type);

	//Optional<DeviceHardwareEntity> findByDeviceCode(String deviceCode);

	@Query("SELECT a FROM DeviceHardwareEntity a ")
	Page<DeviceHardwareEntity> getAll(Pageable pageable);

//	@Query("SELECT a FROM DeviceHardwareEntity a where  a.brand =:brand and a.type =:type and a.deviceName=:deviceName and a.status=:status ")
//	Page<DeviceHardwareEntity> getByTypeStatusDeviceNameAndBrand(@Param("type")  String type,@Param("status")  Boolean status, @Param("deviceName") String deviceName,
//			@Param("brand") String brand, Pageable pageable);


//	@Query("SELECT a FROM DeviceHardwareEntity a where  a.type =:type and a.brand =:brand")
//	Page<DeviceHardwareEntity> getByTypeAndBrand(@Param("type") String type, @Param("brand") String brand, Pageable pageable);

//	@Query("SELECT a FROM DeviceHardwareEntity a where  a.brand =:brand and a.status=:status")
//	Page<DeviceHardwareEntity> getByBrandAndStatus(@Param("brand") String brand, @Param("status") Boolean status, Pageable pageable);


	@Query("SELECT a FROM DeviceHardwareEntity a where  a.type =:type and a.status=:status")
	Page<DeviceHardwareEntity> getByTypeAndStatus(@Param("type") String type,  @Param("status") Boolean status, Pageable pageable);

	@Query("SELECT a FROM DeviceHardwareEntity a where  a.status=:status")
	Page<DeviceHardwareEntity> getByStatus( @Param("status") Boolean status, Pageable pageable);

//	@Query("SELECT a FROM DeviceHardwareEntity a where a.brand =:brand ")
//	Page<DeviceHardwareEntity> getByBrand(@Param("brand") String brand, Pageable pageable);

	@Query("SELECT a FROM DeviceHardwareEntity a where  a.type =:type ")
	Page<DeviceHardwareEntity> getByType(@Param("type") String type, Pageable pageable);

	@Query("SELECT a FROM DeviceHardwareEntity a where a.deviceName like %:deviceName% ")
	Page<DeviceHardwareEntity> getAllSubString(@Param("deviceName") String deviceName, Pageable pageable);

	List<DeviceHardwareEntity> findAllByOrderByModifiedDateDesc();
	
	@Query("select a from DeviceHardwareEntity a where a.type.id=:type")
	List<DeviceHardwareEntity> getById(@Param("type") Long type );

	List<DeviceHardwareEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);

//	List<DeviceHardwareEntity> findByBrandAndTypeAndStatusOrderByModifiedDateDesc(AssetBrandEntity brand, AssetTypeEntity type, Boolean status);
//	//	Optional<DeviceHardwareEntity> findByCodeIgnoreCase(String code);






}
