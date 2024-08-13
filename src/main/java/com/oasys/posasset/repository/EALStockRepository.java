package com.oasys.posasset.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.dto.Stockbalance;
import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.repository.BarQrOverviewDTO;
import com.oasys.helpdesk.repository.EALAvailable;
import com.oasys.helpdesk.repository.ReceviedBarQr;
import com.oasys.posasset.dto.StockOverviewMapResponseDTO;
import com.oasys.posasset.dto.StockOverviewUnMapResponseDTO;
import com.oasys.posasset.entity.EALStockEntity;

@Repository
public interface EALStockRepository extends JpaRepository<EALStockEntity, Long> {

//	@Query(value="select * from eal_request_map where ealrequest_id=:ealrequest_id",nativeQuery =true)
//	public List<EALRequestMapEntity> getById(@Param("ealrequest_id") Long id);
//	
//	List<EALRequestMapEntity> findByCreatedByOrderByIdDesc(Long userId);

	Optional<EALStockEntity> findByStockApplnno(String requestedapplnNo);

	@Query(value = "select SUM(er.total_numof_barcode) As totalnoofbarcode ,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.created_by=:created_by", nativeQuery = true)
	public List<EALAvailable> getCountByStatusAndCreatedDateBetweenAndCodeType(
			@Param("fromDate") java.util.Date fromDate, @Param("toDate") java.util.Date toDate,
			@Param("code_type") String codetype, @Param("created_by") Long createdBy);

//	@Query(value="SELECT (er.code_type) As codetype, SUM(er.total_numof_barcode) As totalnoofbarcode,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal er where er.code_type=:code_type",nativeQuery=true)
//	public List<EALAvailable> getCountByStatusAndCodeType(@Param("code_type") String codetype);
//	
//	@Query(value="SELECT (er.code_type),SUM(er.total_numof_barcode),SUM(er.total_numof_qrcode) from stock_ineal er where er.code_type=:code_type",nativeQuery=true)
//	public List<EALStockEntity> getCountByStatusAndCodeType(@Param("code_type") String codetype);

	@Query(value = "select * from stock_ineal where eal_requestid=:eal_requestid", nativeQuery = true)
	public List<EALStockEntity> getById(@Param("eal_requestid") Long id);

	@Query("select a from EALStockEntity a where  a.stockApplnno=:stockApplnno")
	List<EALStockEntity> getByStockApplnno(@Param("stockApplnno") String applnno);

	@Query("select a from EALStockEntity a where  a.stockApplnno=:stockApplnno GROUP BY packaging_size")
	List<EALStockEntity> getByStockApplno(@Param("stockApplnno") String applnno);

	@Query("select a from EALStockEntity a where  a.stockApplnno=:stockApplnno GROUP BY printingType")
	List<EALStockEntity> getByStockapllunmaptype(@Param("stockApplnno") String applnno);

	@Query(value = "SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged, SUM(no_roll_received) As NoofRollcodereceived  FROM stock_ineal where eal_request_applnno=:stockappln GROUP BY packaging_size, carton_size", nativeQuery = true)
	public List<BarQrBalanceDTO> getByStockApplnMap(@Param("stockappln") String stockappln);
	
	@Query(value = "SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged, SUM(no_roll_received) As NoofRollcodereceived  FROM stock_ineal where eal_request_applnno=:stockappln GROUP BY unmapped_type, printing_type, map_type", nativeQuery = true)
	public List<BarQrBalanceDTO> getByStockApplnUnMap(@Param("stockappln") String stockappln);
	
	@Query(value = "SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged, SUM(no_roll_received) As NoofRollcodereceived  FROM stock_ineal where eal_request_applnno=:stockappln GROUP BY packaging_size", nativeQuery = true)
	public List<BarQrBalanceDTO> getByStockAppln(@Param("stockappln") String stockappln);

	@Query("select a from EALStockEntity a where  a.ealrequestapplno=:ealrequestapplno")
	List<EALStockEntity> getByEALAPPln(@Param("ealrequestapplno") String applnno);

	@Query("select a from EALStockEntity a where  a.ealrequestapplno=:ealrequestapplno and a.codeType=:codeType")
	List<EALStockEntity> getByEALAPPlnAndCodeType(@Param("ealrequestapplno") String applnno,
			@Param("codeType") String codeType);

	@Query(value = "SELECT packaging_size As packagingSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_pending) As noofBarcodepending,SUM(no_qrcode_pending) As noofqrpending,SUM(no_of_barcode) As noofBarcode,SUM(no_of_qrcode) As noofQrcode,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged  FROM stock_ineal where eal_request_applnno=:eal_request_applnno GROUP BY packaging_size", nativeQuery = true)
	public List<BarQrOverviewDTO> getByStockApplnAll(@Param("eal_request_applnno") String ealrequestapplno);

	@Query(value = "SELECT packaging_size As packagingSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_pending) As noofBarcodepending,SUM(no_qrcode_pending) As noofqrpending,SUM(no_of_barcode) As noofBarcode,SUM(no_of_qrcode) As noofQrcode,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged  FROM stock_ineal where eal_request_applnno=:eal_request_applnno GROUP BY printing_type", nativeQuery = true)
	public List<BarQrOverviewDTO> getByStockApplnAllpritingType(@Param("eal_request_applnno") String ealrequestapplno);

	@Query(value = "SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged,SUM(no_roll_received) As NoofRollcodereceived  FROM stock_ineal where eal_request_applnno=:eal_request_applnno and unmapped_type=:unmapped_type and printing_type=:printing_type  GROUP BY printing_type", nativeQuery = true)
	public List<BarQrBalanceDTO> getByStockApplnAndUnmappedTypeAndPrintingType(
			@Param("eal_request_applnno") String ealrequestapplno, @Param("unmapped_type") String unmapped_type,
			@Param("printing_type") String printingtype);

//	@Query(value="SELECT packaging_size As packagingSize ,(no_barcode_received) As noofBarcodereceived,(no_qrcode_received) As noofQrcodereceived,(no_barcode_damaged) As noofBarcodedamaged,(no_qrcode_damaged) As noofQrcodedamaged FROM stock_ineal where eal_request_applnno=:eal_request_applnno and unmapped_type=:unmapped_type  GROUP BY packaging_size",nativeQuery =true)
//	public List<BarQrBalanceDTO> getByStockApplnAndUnmappedType(@Param("eal_request_applnno") String ealrequestapplno,@Param("unmapped_type") String unmapped_type);
//	

//	@Query(value="SELECT stock_applnno As stockApplnno,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal GROUP BY stock_applnno,printing_type,unmapped_type",nativeQuery =true)
//	public List<ReceviedBarQr> getByStockreceived();

	@Query(value = "SELECT stock_applnno As stockApplnno,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal GROUP BY stock_applnno", nativeQuery = true)
	public List<ReceviedBarQr> getByStockreceived();

	@Query("select a from EALStockEntity a where  a.stockApplnno=:stockApplnno and a.unmappedType=:unmappedType")
	List<EALStockEntity> getByStockApplnnoAndUnmappedType(@Param("stockApplnno") String applnno,
			@Param("unmappedType") String unmappedType);

	@Query("select a from EALStockEntity a where  a.stockApplnno=:stockApplnno and a.packagingSize=:packagingSize")
	List<EALStockEntity> getByStockApplnnoAndPackagingSize(@Param("stockApplnno") String applnno,
			@Param("packagingSize") String packagingSize);

	@Query("select a from EALStockEntity a where  a.ealrequestapplno=:ealrequestapplno  GROUP BY packaging_size")
	List<EALStockEntity> getByEalrequestapplno(@Param("ealrequestapplno") String applnno);

	@Query("select a from EALStockEntity a where  a.ealrequestapplno=:ealrequestapplno  GROUP BY printingType,unmappedType")
	List<EALStockEntity> getByEalrequestapplnounmapped(@Param("ealrequestapplno") String applnno);

	@Query(value = "select SUM(er.total_numof_barcode) As totalnoofbarcode ,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.created_by=:created_by", nativeQuery = true)
	public List<EALAvailable> getCountByStatusAndCreatedDateBetweenAndCodeTypeAndCreatedby(
			@Param("fromDate") java.util.Date fromDate, @Param("toDate") java.util.Date toDate,
			@Param("code_type") String codetype, @Param("created_by") Long createdby);

	@Query(value = "select SUM(er.total_numof_barcode) As totalnoofbarcode ,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.created_by=:created_by", nativeQuery = true)
	public List<EALAvailable> getCountByStatusAndCreatedDateBetweenAndUnmappedAndCreatedby(
			@Param("fromDate") java.util.Date fromDate, @Param("toDate") java.util.Date toDate,
			@Param("code_type") String codetype, @Param("created_by") Long createdby);

	@Query(value = "SELECT packaging_size As packagingSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_pending) As noofBarcodepending,SUM(no_qrcode_pending) As noofqrpending,SUM(no_of_barcode) As noofBarcode,SUM(no_of_qrcode) As noofQrcode,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged  FROM stock_ineal where eal_request_applnno=:eal_request_applnno and unmapped_type=:unmapped_type and printing_type=:printing_type GROUP BY printing_type", nativeQuery = true)
	public List<BarQrOverviewDTO> getByStockApplnAndUnmappedTypeAndPrintingTypeAll(
			@Param("eal_request_applnno") String ealrequestapplno, @Param("unmapped_type") String unmapped_type,
			@Param("printing_type") String printingtype);

	@Query(value = "SELECT stock_applnno As stockApplnno,packaging_size As PackageSize,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal GROUP BY packaging_size", nativeQuery = true)
	public List<ReceviedBarQr> getByoverStockreceived();

	@Query(value = "SELECT stock_applnno As stockApplnno,packaging_size As PackageSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal GROUP BY unmapped_type", nativeQuery = true)
	public List<ReceviedBarQr> getByoverStockreceivedunmap();

	@Query(value = "SELECT si.packaging_size As packageSize, si.carton_size As cartonSize, "
			+ "SUM(si.no_barcode_received) AS balBarcode, " + "SUM(si.no_qrcode_received) AS balQrcode "
			+ "FROM stock_ineal si " + "WHERE si.code_type = :codeType " + "AND si.license_no = :licenseNo "
			+ "AND date(si.created_date) BETWEEN :fromDate AND :toDate "
			+ "GROUP BY si.packaging_size, si.carton_size", nativeQuery = true)
	List<Stockbalance> findbystockOverviewMappedCount(@Param("codeType") String codeType,
			@Param("licenseNo") String licenseNo, @Param("fromDate") java.util.Date fromDate,
			@Param("toDate") java.util.Date toDate);

	@Query(value = "SELECT si.unmapped_type AS unmappedType, si.map_type AS mapType, SUM(si.no_of_barcode) AS balanceBarcode, SUM(si.no_of_qrcode) AS balanceQrcode FROM stock_ineal si WHERE si.code_type = :codeType AND si.license_no = :licenseNo AND DATE(si.created_date) BETWEEN :fromDate AND :toDate GROUP BY si.unmapped_type, si.map_type", nativeQuery = true)
	List<Stockbalance> findByStockOverviewUnmapedCount(@Param("codeType") String codeType,
			@Param("licenseNo") String licenseNo, @Param("fromDate") java.util.Date fromDate,
			@Param("toDate") java.util.Date toDate);

	@Query(value = "SELECT si.packaging_size As packageSize, si.carton_size As cartonSize, "
			+ "SUM(si.no_barcode_received) AS balBarcode, " + "SUM(si.no_qrcode_received) AS balQrcode "
			+ "FROM stock_ineal_pu si " + "WHERE si.code_type = :codeType " + "AND si.license_no = :licenseNo "
			+ "AND date(si.created_date) BETWEEN :fromDate AND :toDate "
			+ "GROUP BY si.packaging_size, si.carton_size", nativeQuery = true)
	List<Stockbalance> findbystockOverviewMappedParentUnitCount(@Param("codeType") String codeType,
			@Param("licenseNo") String licenseNo, @Param("fromDate") java.util.Date fromDate,
			@Param("toDate") java.util.Date toDate);

	@Query(value = "SELECT si.unmapped_type AS unmappedType, si.map_type AS mapType, SUM(si.no_of_barcode) AS balanceBarcode, SUM(si.no_of_qrcode) AS balanceQrcode FROM stock_ineal_pu si WHERE si.code_type = :codeType AND si.license_no = :licenseNo AND DATE(si.created_date) BETWEEN :fromDate AND :toDate GROUP BY si.unmapped_type, si.map_type", nativeQuery = true)
	List<Stockbalance> findByStockOverviewUnmapedParentUnitCount(@Param("codeType") String codeType,
			@Param("licenseNo") String licenseNo, @Param("fromDate") java.util.Date fromDate,
			@Param("toDate") java.util.Date toDate);

	@Query(value = "select * FROM stock_ineal where tp_applnno =:tpApplnNo",nativeQuery = true)
	List<EALStockEntity> getbyTpApplnno(String tpApplnNo);

	@Query(value = "select si.license_no as licenseNumber,si.packaging_size as packagingSize,si.carton_size as cartonSize,\r\n" + 
			"    coalesce(si.no_barcode_received,0) as stockNoOfBarCode,coalesce(si.no_qrcode_received,0) as stockNoOfQrCode,\r\n" + 
			"    coalesce(erd.no_of_barcode,0) as ealRequestNoOfBarCode,coalesce(erd.no_of_qrcode,0) as ealRequestNoOfQrCode,\r\n" + 
			"    coalesce(used_br_code,0) as usedBrCode,coalesce(used_qr_code,0) as usedQrCode,\r\n" + 
			"    coalesce(balance_br_code,0) as balanceBrCode,coalesce(balance_qr_code,0) as balanceQrCode,\r\n" + 
			"    coalesce(bottled_br_code,0) as bottledBrCode,coalesce(bottled_qr_code,0) as bottledQrCode,\r\n" + 
			"    coalesce(damaged_no_of_barcode,0) as damagedNoOfBarCode,coalesce(damaged_no_of_bottle_qr_code,0)as damagedNoOfBottleQrCode,\r\n" + 
			"    coalesce(si.no_barcode_received,0)-(coalesce(erd.no_of_barcode,0)-coalesce(balance_br_code,0)) as actualBarCodeBalance,\r\n" + 
			"    coalesce(si.no_qrcode_received,0)-(coalesce(erd.no_of_qrcode,0)-coalesce(balance_qr_code,0)) as actualQrCodeBalance,\r\n" + 
			"    (coalesce(used_br_code,0)+coalesce(damaged_no_of_barcode,0)) as usedWastageBarCode,\r\n" + 
			"    (coalesce(used_qr_code,0)+coalesce(damaged_no_of_bottle_qr_code,0)) as usedWastageQrCode\r\n" + 
			"from (select license_no,trim(replace(packaging_size,' ML','')) as packaging_size,carton_size,\r\n" + 
			"        created_date,sum(no_barcode_received) as no_barcode_received,sum(no_qrcode_received) as no_qrcode_received\r\n" + 
			"    from stock_ineal where code_type ='MAPPED' group by license_no,trim(replace(packaging_size,' ML','')), carton_size)si\r\n" + 
			"left join (select license_no,trim(replace(erm.packaging_size,' ML','')) as packaging_size,carton_size,\r\n" + 
			"        sum(no_of_barcode) as no_of_barcode,sum(no_of_qrcode) as no_of_qrcode\r\n" + 
			"    from eal_request_aec er\r\n" + 
			"    join eal_request_map_aec erm on er.id=erm.ealrequest_id where erm.code_type ='MAPPED' and er.status=1 group by license_no,\r\n" + 
			"        trim(replace(erm.packaging_size,' ML','')),carton_size) erd on erd.license_no=si.license_no \r\n" + 
			"    and erd.packaging_size=si.packaging_size and erd.carton_size=si.carton_size\r\n" + 
			"left join(select license_number, trim(package_size) as package_size,carton_size, sum(used_br_code) as used_br_code,\r\n" + 
			"        sum(used_qr_code) as used_qr_code,sum(balance_br_code) as balance_br_code,\r\n" + 
			"        sum(balance_qr_code) as balance_qr_code,sum(bottled_br_code) as bottled_br_code,\r\n" + 
			"        sum(bottled_qr_code) as bottled_qr_code,sum(damaged_no_of_barcode) as damaged_no_of_barcode,\r\n" + 
			"        sum(damaged_no_of_bottle_qr_code) as damaged_no_of_bottle_qr_code\r\n" + 
			"    from eal_wastage where code_type ='MAPPED' and status =1 group by license_number,trim(package_size),carton_size) erw on \r\n" + 
			"    si.license_no=erw.license_number and si.packaging_size=erw.package_size and si.carton_size=erw.carton_size\r\n" + 
			"where (si.license_no=:licenseNumber or :licenseNumber is null) and date(si.created_date) between :fromDate and :toDate\r\n" + 
			"and (si.packaging_size=:packagingSize or :packagingSize is null) and (si.carton_size=:cartonSize or :cartonSize is null)",nativeQuery = true)
	List<StockOverviewMapResponseDTO> mapStockSummary(String fromDate, String toDate, String licenseNumber,String packagingSize,String cartonSize);

	@Query(value = "SELECT si.license_no AS licenseNumber,si.printing_type AS printingType,si.unmapped_type AS unmappedType,si.map_type AS mapType,\r\n" + 
			"COALESCE(si.no_barcode_received,0) AS stockNoOfBarCode,COALESCE(si.no_qrcode_received,0) AS stockNoOfQrCode,\r\n" + 
			"COALESCE(erd.no_of_barcode,0) AS ealRequestNoOfBarCode,COALESCE(erd.no_of_qrcode,0) AS ealRequestNoOfQrCode,\r\n" + 
			"COALESCE(used_br_code,0) AS usedBrCode,COALESCE(used_qr_code,0) AS usedQrCode,COALESCE(balance_br_code,0) AS balanceBrCode,\r\n" + 
			"COALESCE(balance_qr_code,0) AS balanceQrCode,COALESCE(bottled_br_code,0)AS bottledBrCode,COALESCE(bottled_qr_code,0) AS bottledQrCode,\r\n" + 
			"COALESCE(damaged_no_of_barcode,0) AS damagedNoOfBarCode,COALESCE(damaged_no_of_bottle_qr_code,0) AS damagedNoOfBottleQrCode,\r\n" + 
			"COALESCE(si.no_barcode_received,0)-(COALESCE(erd.no_of_barcode,0)-COALESCE(balance_br_code,0)) AS actualBarCodeBalance,\r\n" + 
			"COALESCE(si.no_qrcode_received,0)-(COALESCE(erd.no_of_qrcode,0)-COALESCE(balance_qr_code,0)) AS actualQrCodeBalance,\r\n" + 
			"(coalesce(used_br_code,0)+coalesce(damaged_no_of_barcode,0)) as usedWastageBarCode,\r\n" + 
			"(coalesce(used_qr_code,0)+coalesce(damaged_no_of_bottle_qr_code,0)) as usedWastageQrCode\r\n" + 
			"FROM(SELECT license_no,printing_type,unmapped_type,map_type,created_date,\r\n" + 
			"SUM(no_barcode_received) AS no_barcode_received,SUM(no_qrcode_received) AS no_qrcode_received\r\n" + 
			"FROM stock_ineal WHERE code_type ='UNMAPPED'GROUP BY license_no,printing_type,unmapped_type,map_type)si\r\n" + 
			"LEFT JOIN(SELECT license_no,erm.printing_type,erm.unmapped_type,erm.map_type, SUM(no_of_barcode) AS no_of_barcode,SUM(no_of_qrcode) AS no_of_qrcode\r\n" + 
			"FROM eal_request_aec er JOIN eal_request_map_aec erm ON er.id=erm.ealrequest_id WHERE erm.code_type ='UNMAPPED' and er.status=1 \r\n" + 
			"GROUP BY license_no,erm.printing_type,erm.unmapped_type,erm.map_type )erd ON erd.license_no=si.license_no \r\n" + 
			"AND erd.printing_type=si.printing_type AND erd.unmapped_type=si.unmapped_type AND erd.map_type=si.map_type\r\n" + 
			"LEFT JOIN (SELECT license_number,printing_type,unmapped_type,map_type,\r\n" + 
			"SUM(used_br_code) AS used_br_code,SUM(used_qr_code) AS used_qr_code,SUM(balance_br_code) AS balance_br_code,\r\n" + 
			"SUM(balance_qr_code) AS balance_qr_code,SUM(bottled_br_code) AS bottled_br_code,\r\n" + 
			"SUM(bottled_qr_code) AS bottled_qr_code,SUM(damaged_no_of_barcode) AS damaged_no_of_barcode,\r\n" + 
			"SUM(damaged_no_of_bottle_qr_code) AS damaged_no_of_bottle_qr_code\r\n" + 
			"FROM eal_wastage WHERE code_type ='UNMAPPED' and status =1 GROUP BY license_number,printing_type,unmapped_type,map_type ) erw \r\n" + 
			"ON erw.license_number=si.license_no AND erw.printing_type=si.printing_type AND erw.unmapped_type=si.unmapped_type AND erw.map_type=si.map_type\r\n" + 
			"WHERE (si.license_no=:licenseNumber or :licenseNumber is null) and date(si.created_date) between :fromDate and :toDate\r\n" + 
			"and (si.printing_type=:printingType or :printingType is null) and (si.unmapped_type=:unmappedType or :unmappedType is null)\r\n" + 
			"and (si.map_type=:mapType or :mapType is null)",nativeQuery = true)
	List<StockOverviewUnMapResponseDTO> unmapStockSummary(String fromDate, String toDate, String licenseNumber,String printingType,String unmappedType,String mapType);

	@Query(value = "select si.license_no as licenseNumber,si.packaging_size as packagingSize,si.carton_size as cartonSize,\r\n" + 
			"    coalesce(si.no_barcode_received,0) as stockNoOfBarCode,coalesce(si.no_qrcode_received,0) as stockNoOfQrCode,\r\n" + 
			"    coalesce(erd.no_of_barcode,0) as ealRequestNoOfBarCode,coalesce(erd.no_of_qrcode,0) as ealRequestNoOfQrCode,\r\n" + 
			"    coalesce(used_br_code,0) as usedBrCode,coalesce(used_qr_code,0) as usedQrCode,\r\n" + 
			"    coalesce(balance_br_code,0) as balanceBrCode,coalesce(balance_qr_code,0) as balanceQrCode,\r\n" + 
			"    coalesce(bottled_br_code,0) as bottledBrCode,coalesce(bottled_qr_code,0) as bottledQrCode,\r\n" + 
			"    coalesce(damaged_no_of_barcode,0) as damagedNoOfBarCode,coalesce(damaged_no_of_bottle_qr_code,0)as damagedNoOfBottleQrCode,\r\n" + 
			"    coalesce(si.no_barcode_received,0)-(coalesce(erd.no_of_barcode,0)-coalesce(balance_br_code,0)) as actualBarCodeBalance,\r\n" + 
			"    coalesce(si.no_qrcode_received,0)-(coalesce(erd.no_of_qrcode,0)-coalesce(balance_qr_code,0)) as actualQrCodeBalance,\r\n" + 
			"    (coalesce(used_br_code,0)+coalesce(damaged_no_of_barcode,0)) as usedWastageBarCode,\r\n" + 
			"    (coalesce(used_qr_code,0)+coalesce(damaged_no_of_bottle_qr_code,0)) as usedWastageQrCode\r\n" + 
			"FROM (SELECT license_no,TRIM(REPLACE(packaging_size,' ML','')) AS packaging_size,carton_size,created_date,\r\n" + 
			"SUM(no_barcode_received) AS no_barcode_received,SUM(no_qrcode_received) AS no_qrcode_received\r\n" + 
			"FROM stock_ineal_pu WHERE code_type ='MAPPED'GROUP BY license_no,packaging_size,carton_size )si\r\n" + 
			"LEFT JOIN (SELECT license_no,TRIM(REPLACE(erm.packaging_size,' ML','')) AS packaging_size,carton_size,\r\n" + 
			"SUM(no_of_barcode) AS no_of_barcode,SUM(no_of_qrcode) AS no_of_qrcode\r\n" + 
			"FROM eal_request_aec er JOIN eal_request_map_aec erm ON er.id=erm.ealrequest_id WHERE erm.code_type ='MAPPED' and er.status=1 \r\n" + 
			"GROUP BY license_no,packaging_size,carton_size )erd ON erd.license_no=si.license_no AND erd.packaging_size=si.packaging_size \r\n" + 
			"AND erd.carton_size=si.carton_size LEFT JOIN (SELECT license_number,TRIM(package_size) AS package_size,carton_size,\r\n" + 
			"SUM(used_br_code) AS used_br_code,SUM(used_qr_code) AS used_qr_code,SUM(balance_br_code) AS balance_br_code,\r\n" + 
			"SUM(balance_qr_code) AS balance_qr_code,SUM(bottled_br_code) AS bottled_br_code,\r\n" + 
			"SUM(bottled_qr_code) AS bottled_qr_code,SUM(damaged_no_of_barcode) AS damaged_no_of_barcode,\r\n" + 
			"SUM(damaged_no_of_bottle_qr_code) AS damaged_no_of_bottle_qr_code\r\n" + 
			"FROM eal_wastage WHERE code_type ='MAPPED' and status =1 GROUP BY license_number,package_size,carton_size ) erw \r\n" + 
			"ON si.license_no=erw.license_number AND si.packaging_size=erw.package_size AND si.carton_size=erw.carton_size\r\n" + 
			"WHERE (si.license_no=:licenseNumber or :licenseNumber is null) and date(si.created_date) between :fromDate and :toDate\r\n" + 
			"and (si.packaging_size=:packagingSize or :packagingSize is null) and (si.carton_size=:cartonSize or :cartonSize is null)",nativeQuery = true)
	List<StockOverviewMapResponseDTO> puMapStockSummary(String fromDate, String toDate, String licenseNumber,String packagingSize,String cartonSize);

	@Query(value = "SELECT si.license_no AS licenseNumber,si.printing_type AS printingType,si.unmapped_type AS unmappedType,si.map_type AS mapType,\r\n" + 
			"COALESCE(si.no_barcode_received,0) AS stockNoOfBarCode,COALESCE(si.no_qrcode_received,0) AS stockNoOfQrCode,\r\n" + 
			"COALESCE(erd.no_of_barcode,0) AS ealRequestNoOfBarCode,COALESCE(erd.no_of_qrcode,0) AS ealRequestNoOfQrCode,\r\n" + 
			"COALESCE(used_br_code,0) AS usedBrCode,COALESCE(used_qr_code,0) AS usedQrCode,COALESCE(balance_br_code,0) AS balanceBrCode,\r\n" + 
			"COALESCE(balance_qr_code,0) AS balanceQrCode,COALESCE(bottled_br_code,0)AS bottledBrCode,COALESCE(bottled_qr_code,0) AS bottledQrCode,\r\n" + 
			"COALESCE(damaged_no_of_barcode,0) AS damagedNoOfBarCode,COALESCE(damaged_no_of_bottle_qr_code,0) AS damagedNoOfBottleQrCode,\r\n" + 
			"COALESCE(si.no_barcode_received,0)-(COALESCE(erd.no_of_barcode,0)-COALESCE(balance_br_code,0)) AS actualBarCodeBalance,\r\n" + 
			"COALESCE(si.no_qrcode_received,0)-(COALESCE(erd.no_of_qrcode,0)-COALESCE(balance_qr_code,0)) AS actualQrCodeBalance,\r\n" + 
			"(coalesce(used_br_code,0)+coalesce(damaged_no_of_barcode,0)) as usedWastageBarCode,\r\n" + 
			"(coalesce(used_qr_code,0)+coalesce(damaged_no_of_bottle_qr_code,0)) as usedWastageQrCode\r\n" + 
			"FROM (SELECT license_no,printing_type,unmapped_type,map_type,created_date,\r\n" + 
			"SUM(no_barcode_received) AS no_barcode_received,SUM(no_qrcode_received) AS no_qrcode_received\r\n" + 
			"FROM stock_ineal_pu WHERE code_type ='UNMAPPED' GROUP BY license_no,printing_type,unmapped_type,map_type )si\r\n" + 
			"LEFT JOIN (SELECT license_no,erm.printing_type,erm.unmapped_type,erm.map_type, SUM(no_of_barcode) AS no_of_barcode,SUM(no_of_qrcode) AS no_of_qrcode\r\n" + 
			"FROM eal_request_aec er JOIN eal_request_map_aec erm ON er.id=erm.ealrequest_id WHERE erm.code_type ='UNMAPPED' and er.status=1 \r\n" + 
			"GROUP BY license_no,erm.printing_type,erm.unmapped_type,erm.map_type)erd ON erd.license_no=si.license_no \r\n" + 
			"AND erd.printing_type=si.printing_type AND erd.unmapped_type=si.unmapped_type AND erd.map_type=si.map_type\r\n" + 
			"LEFT JOIN (SELECT license_number,printing_type,unmapped_type,map_type,\r\n" + 
			"SUM(used_br_code) AS used_br_code,SUM(used_qr_code) AS used_qr_code,SUM(balance_br_code) AS balance_br_code,\r\n" + 
			"SUM(balance_qr_code) AS balance_qr_code,SUM(bottled_br_code) AS bottled_br_code,\r\n" + 
			"SUM(bottled_qr_code) AS bottled_qr_code,SUM(damaged_no_of_barcode) AS damaged_no_of_barcode,\r\n" + 
			"SUM(damaged_no_of_bottle_qr_code) AS damaged_no_of_bottle_qr_code\r\n" + 
			"FROM eal_wastage WHERE code_type ='UNMAPPED' and status =1 GROUP BY license_number,printing_type,unmapped_type,map_type ) erw \r\n" + 
			"ON erw.license_number=si.license_no AND erw.printing_type=si.printing_type AND erw.unmapped_type=si.unmapped_type AND erw.map_type=si.map_type\r\n" + 
			"WHERE (si.license_no=:licenseNumber or :licenseNumber is null) and date(si.created_date) between :fromDate and :toDate\r\n" + 
			"and (si.printing_type=:printingType or :printingType is null) and (si.unmapped_type=:unmappedType or :unmappedType is null)\r\n" + 
			"and (si.map_type=:mapType or :mapType is null)",nativeQuery = true)
	List<StockOverviewUnMapResponseDTO> puUnmapStockSummary(String fromDate, String toDate, String licenseNumber,String printingType,String unmappedType,String mapType);

	List<EALStockEntity> findAllByOrderByModifiedDateDesc();
		

}
