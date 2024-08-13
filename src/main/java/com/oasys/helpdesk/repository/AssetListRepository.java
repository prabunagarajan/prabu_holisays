package com.oasys.helpdesk.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.dto.AssetReportResponseDTO;
import com.oasys.helpdesk.entity.AssetListEntity;

public interface AssetListRepository extends JpaRepository<AssetListEntity, Long> {

	@Query("select a from AssetListEntity a where a.brand.id=:brand and a.deviceName.id=:deviceName and a.assetsubTypeNmae.id=:assetsubTypeNmae and a.type.id=:type and a.serialNo=:serialNo")
	Optional<AssetListEntity> getByBrandAndDeviceNameAndAssetsubTypeNmaeAndTypeAndSerialNo(@Param("brand") Long brand,
			@Param("deviceName") Long deviceName, @Param("assetsubTypeNmae") Long assetsubTypeNmae,
			@Param("type") Long type, @Param("serialNo") String serialNo);

	@Query("select a from AssetListEntity a where a.serialNo=:serialNo")
	List<AssetListEntity> getBySerialNo(@Param("serialNo") String serialNo);

	Optional<AssetListEntity> findBySerialNo(String seriyalNo);

	List<AssetListEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean is_Active);

	@Query(value = "SELECT al.date_of_purchase AS DateOfPurchase,al.is_active AS IsActive,al.created_date AS CreatedDate,\n" + 
			"al.rating AS Rating,al.serial_no AS SerialNo,al.warranty_period AS WarrantyPeriod,\n" + 
			"an.asset_sub_type AS AssetSubType,ab.brand AS AssetBrand,dhn.asset_name AS AssetName,\n" + 
			"sm.supplier_name AS SupplierName,ata.type AS AssetType,ed.entity_name AS AssetGroup,am.district AS District,\n" + 
			"am.user_name AS UserName,am.date_of_installation AS DateOfInstallation,am.state AS State,\n" + 
			"am.licenseno AS Licenseno,am.unit_name AS UnitName,am.user_type AS UserType\n" + 
			"FROM asset_list AS al\n" + 
			"JOIN asset_map AS am ON am.serial_no = al.serial_no\n" + 
			"JOIN supplier_master sm ON sm.id = al.supplier_name\n" + 
			"JOIN device_hardware_name dhn ON dhn.id = am.asset_name\n" + 
			"JOIN asset_type_accessories ata ON ata.id = al.asset_type\n" + 
			"JOIN entity_details ed ON ed.id = am.asset_group\n" + 
			"JOIN accessories_name an ON al.asset_sub_type = an.id\n" + 
			"JOIN asset_brand ab ON ab.id = al.asset_brand\n" + 
			"WHERE 1 = 1 AND ((CAST(am.created_date AS DATE) BETWEEN :fromDate AND :toDate)\n" + 
			"OR (:fromDate IS NULL) OR (:toDate IS NULL))\n" + 
			"AND (ed.id = COALESCE(NULLIF(:assetGroup,''), NULL) OR :assetGroup IS NULL)\n" + 
			"AND (al.asset_type = COALESCE(NULLIF(:assetType, ''), NULL) OR :assetType IS NULL)\n" + 
			"AND (al.asset_name = COALESCE(NULLIF(:assetName, ''), NULL) OR :assetName IS NULL)\n" + 
			"AND (al.asset_brand = COALESCE(NULLIF(:assetBrand, ''), NULL) OR :assetBrand IS NULL)\n" + 
			"AND (al.asset_sub_type = COALESCE(NULLIF(:assetSubType, ''), NULL) OR :assetSubType IS NULL)\n" + 
			"AND (al.supplier_name = COALESCE(NULLIF(:supplierName, ''), NULL) OR :supplierName IS NULL)\n" + 
			"AND (al.serial_no = COALESCE(NULLIF(:serialNo, ''), NULL) OR :serialNo IS NULL)\n" + 
			"AND (al.is_active = COALESCE(NULLIF(:status, ''), NULL) OR :status IS NULL)\n" + 
			"AND (am.unit_name = COALESCE(NULLIF(:unitName, ''), NULL) OR :unitName IS NULL)\n" + 
			"ORDER BY am.created_date DESC;", nativeQuery = true)
	List<AssetReportResponseDTO> assetReport(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("assetGroup") String assetGroup, @Param("assetType") String assetType,
			@Param("assetName") String assetName, @Param("assetBrand") String assetBrand,
			@Param("assetSubType") String assetSubType, @Param("supplierName") String supplierName,
			@Param("serialNo") String serialNo, @Param("status") String status, @Param("unitName") String unitName);

	@Query("select a from AssetListEntity a where a.serialNo=:serialNo AND a.id != :id")
	List<AssetListEntity> findBySerialNoNotINId(@Param("serialNo") String serialNo,@Param("id") Long id);
	
}
