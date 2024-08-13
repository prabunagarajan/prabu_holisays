package com.oasys.helpdesk.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oasys.helpdesk.dto.AssetMapAndDeviceReportDTO;
import com.oasys.helpdesk.dto.AssetMapCountDto;
import com.oasys.helpdesk.dto.AssetMapEntityDto;
import com.oasys.helpdesk.entity.AssetListEntity;
import com.oasys.helpdesk.entity.AssetMapEntity;
import com.oasys.helpdesk.entity.AssetStatusEntity;

import io.lettuce.core.dynamic.annotation.Param;

public interface AssetMapRepository extends JpaRepository<AssetMapEntity, Long> {

	@Query("select a from AssetMapEntity a where a.serialNo=:serialNo")
	Optional<AssetMapEntity> getBySerialNo(@Param("serialNo") String serialNo);

	List<AssetMapEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean is_Active);

	Optional<AssetMapEntity> findBySerialNo(String seriyalNo);
	
	@Query(value = "SELECT \r\n" + 
			"			    COALESCE(COUNT(t.id), 0) AS Totaldevice,\r\n" + 
			"			    COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'MAPPED') THEN 1 ELSE 0 END), 0) AS MappedDevice,\r\n" + 
			"			  COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'UNMAPPED') THEN 1 ELSE 0 END), 0) AS NotMappeddevice, \r\n" + 
			"			  COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'REJECTED') THEN 1 ELSE 0 END), 0) AS Rejected, \r\n" + 
			"			    COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'LOST') THEN 1 ELSE 0 END), 0) AS DeviceLost, \r\n" + 
			"			    COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'REPLACE') THEN 1 ELSE 0 END), 0) AS AssetReplace, \r\n" + 
			"			    COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'RETURN') THEN 1 ELSE 0 END), 0) AS AssetReturn,\r\n" + 
			"			    COALESCE(SUM(CASE WHEN t.status = (SELECT id FROM asset_status WHERE code = 'DAMAGED') THEN 1 ELSE 0 END), 0) AS Damaged\r\n" + 
			"			FROM asset_map t\r\n" + 
			"			WHERE t.asset_type =:code", nativeQuery = true)
	List<AssetMapCountDto> getCount(@Param("code") String code);

	@Query(value = "SELECT ed.entity_name  AS 'EntityName', COUNT(DISTINCT am.id) AS 'AssetTotalCount',\r\n" + 
			"						 COUNT(CASE WHEN  ass.code='MAPPED'  THEN am.id END) AS 'Mapped', \r\n" + 
			"						 COUNT(CASE WHEN  ass.code='UNMAPPED'  THEN am.id END) AS 'UnMapped', \r\n" + 
			"						 COUNT(CASE WHEN  ass.code='REPLACE'  THEN am.id END) AS 'REPLACE', \r\n" + 
			"						COUNT(CASE WHEN  ass.code='RETURN'  THEN am.id END) AS 'RETURN',\r\n" + 
			"						COUNT(CASE WHEN  ass.code='LOST'  THEN am.id END) AS 'LOST',\r\n" + 
			"						COUNT(CASE WHEN  ass.code='REJECTED'  THEN am.id END) AS 'REJECTED',\r\n" + 
			"						COUNT(CASE WHEN  ass.code='DAMAGED'  THEN am.id END) AS 'DAMAGED'\r\n" + 
			"						FROM asset_map am\r\n" + 
			"						LEFT JOIN asset_status ass ON ass.id=am.status \r\n" + 
			"						JOIN entity_details ed on am.asset_group=ed.id \r\n" + 
			"						where (:assetType IS NULL OR am.asset_type = :assetType)\r\n" + 
			"						GROUP BY ed.entity_name", nativeQuery = true)
	List<AssetMapEntityDto> getassetSummaryCount(@Param("assetType") String assetType);

	@Query(value = "SELECT\r\n" + 
			"	ed.entity_name AS EntityName, am.district AS District, am.asset_location AS AssetLocation,\r\n" + 
			"	at2.type AS AssetType, dhn.asset_name AS AssetName, am.serial_no AS SerialNumber,\r\n" + 
			"	am.date_of_installation AS DateOfInstallation, as2.name AS Status, am.state AS State,\r\n" + 
			"	am.licenseno AS LicenseNo, am.unit_name AS UnitName, am.user_name AS UserName,\r\n" + 
			"	am.user_type AS UserType, am.unit_code AS UnitCode, d.device_number AS DeviceNumber,\r\n" + 
			"	d.make AS Make, d.model AS Model, d.serial_number AS DeviceSerialNumber,\r\n" + 
			"	d.sim_id AS SimId, d.sim_id2 AS SimId2, am.created_date\r\n" + 
			"FROM test_upe_helpdesk_grievance_22_02_2022.asset_map am\r\n" + 
			"JOIN test_upe_helpdesk_grievance_22_02_2022.device d ON d.fps_code = am.unit_code\r\n" + 
			"JOIN test_upe_helpdesk_grievance_22_02_2022.asset_type at2 ON at2.id = am.asset_type\r\n" + 
			"JOIN test_upe_helpdesk_grievance_22_02_2022.device_hardware_name dhn ON dhn.id = am.asset_name\r\n" + 
			"JOIN test_upe_helpdesk_grievance_22_02_2022.entity_details ed ON ed.id = am.asset_group\r\n" + 
			"JOIN test_upe_helpdesk_grievance_22_02_2022.asset_status as2 ON as2.id = am.status\r\n" + 
			"JOIN test_upe_helpdesk_grievance_22_02_2022.asset_brand ab ON ab.id = d.asset_brand_id\r\n" + 
			"where\r\n" + 
			"	(am.unit_code = COALESCE(NULLIF(:unitCode, ''), am.unit_code)OR :unitCode IS NULL)\r\n" + 
			"	AND (ed.entity_name = COALESCE(NULLIF(:entityName, ''), ed.entity_name)OR :entityName IS NULL)\r\n" + 
			"	AND (am.unit_name = COALESCE(NULLIF(:unitName, ''), am.unit_name)OR :unitName IS NULL)\r\n" + 
			"	AND (am.district = COALESCE(NULLIF(:district, ''), am.district)OR :district IS NULL)\r\n" + 
			"	AND (as2.code  = COALESCE(NULLIF(:status, ''), as2.code)OR :status IS NULL)\r\n" + 
			"	AND (DATE(am.created_date) BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL))", nativeQuery = true)
	List<AssetMapAndDeviceReportDTO> report(@Param("entityName") String entityName,@Param("unitName") String unitName,@Param("unitCode") String unitCode,
			@Param("district") String district,@Param("status") String status,@Param("fromDate") Date fromDate,@Param("toDate") Date toDate);


	
}
