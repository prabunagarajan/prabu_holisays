package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.Accessories;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;

@Repository
public interface AccessoriesNameRepository extends JpaRepository<Accessories, Long> {

	Optional<Accessories> findByAssetsubTypeIgnoreCase(String subtype);

//	@Query("SELECT a FROM Accessories a where a.accessoriesName like %:accessoriesName% ")
//	Page<Accessories> getAllSubString(@Param("accessoriesName") String accessoriesName, Pageable pageable);
//
//
//	@Query("SELECT a FROM Accessories a where  a.brand =:brand and a.type =:type and a.deviceName=:deviceName and a.accessoriesName=:accessoriesName and a.status=:status ")
//	Page<Accessories> getByTypeStatusDeviceNameAndBrandAccessoriesName(@Param("type")  String type,@Param("status")  Boolean status, @Param("deviceName")  String deviceName,
//			@Param("accessoriesName") String accessoriesName,@Param("brand")  String brand, Pageable pageable);

	List<Accessories> findAllByOrderByModifiedDateDesc();

	List<Accessories> findAllByStatusOrderByModifiedDateDesc(Boolean status);

	
	@Query("SELECT a FROM Accessories a where a.assetsubType like %:assetsubType% ")
	Page<Accessories> getAllSubString(@Param("assetsubType") String assetsubType, Pageable pageable);

	
	@Query("SELECT a FROM Accessories a where  a.assetName.id =:assetName and a.type.id =:type and a.assetsubType=:assetsubType and a.status=:status ")
	Page<Accessories> getByAssetNameAndTypeAndAssetsubTypeAndStatus(@Param("assetName")  Long assetname,@Param("type")  Long type,@Param("assetsubType")  String assetsubType,@Param("status")  Boolean status,Pageable pageable);

   
	@Query("SELECT a FROM Accessories a where  a.assetName.id =:assetName")
	Page<Accessories> getByAssetName(@Param("assetName")  Long assetname,Pageable pageable);

	@Query("SELECT a FROM Accessories a where  a.type.id =:type")
	Page<Accessories> getByType(@Param("type")  Long type,Pageable pageable);
	
	@Query("SELECT a FROM Accessories a where  a.assetsubType=:assetsubType")
	Page<Accessories> getByAssetsubType(@Param("assetsubType") String assetsubType,Pageable pageable);
	
	@Query("SELECT a FROM Accessories a where  a.status=:status")
	Page<Accessories> getByStatus(@Param("status")  Boolean status,Pageable pageable);
	
	
	@Query("SELECT a FROM Accessories a where  a.assetName.id =:assetName and a.status=:status")
	Page<Accessories> getByAssetNameAndStatus(@Param("assetName")  Long assetname,@Param("status")  Boolean status,Pageable pageable);

	
	@Query("SELECT a FROM Accessories a where  a.type.id =:type and a.status=:status")
	Page<Accessories> getByTypeAndStatus(@Param("type")  Long assetname,@Param("status")  Boolean status,Pageable pageable);

	
	@Query("SELECT a FROM Accessories a where  a.assetsubType=:assetsubType and a.status=:status")
	Page<Accessories> getByAssetsubTypeAndStatus(@Param("assetsubType")  String assetsubtype,@Param("status")  Boolean status,Pageable pageable);

	
	@Query("SELECT a FROM Accessories a where  a.createdBy=:createdBy")
	Page<Accessories> getByCreatedBy(@Param("createdBy") Long createdby,Pageable pageable);

	
	@Query("select a from Accessories a where a.assetName.id=:assetName")
	List<Accessories> getById(@Param("assetName") Long assetName );
	
	List<Accessories> findAllByTypeAndAssetNameAndAssetsubType(AssetTypeEntity assetType, DeviceHardwareEntity assetTypeName, String assetSubType);

	@Query("select a from Accessories a where a.assetName.id=:assetName and a.type.id =:type and a.status=:status")
	Page<Accessories> getByAssetNameAndTypeAndStatus(@Param("assetName") Long assetName,@Param("type")  Long type,@Param("status")  Boolean status, Pageable pageable);

	@Query("select a from Accessories a where a.assetsubType=:assetsubType and a.type.id =:type and a.status=:status")
	Page<Accessories> getByAssetsubTypeAndTypeAndStatus(@Param("assetsubType")  String assetsubtype,@Param("type")  Long type,@Param("status")  Boolean status, Pageable pageable);

	@Query("select a from Accessories a where a.assetName.id=:assetName and a.assetsubType=:assetsubType and a.status=:status")
	Page<Accessories> getByAssetNameAndAssetsubTypeAndStatus(@Param("assetName") Long assetName,@Param("assetsubType")  String assetsubtype, @Param("status")  Boolean status, Pageable pageable);
	
}
