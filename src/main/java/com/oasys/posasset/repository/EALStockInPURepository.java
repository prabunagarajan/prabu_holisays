package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.helpdesk.repository.BarQrOverviewDTO;
import com.oasys.helpdesk.repository.EALAvailable;
import com.oasys.helpdesk.repository.ReceviedBarQr;
import com.oasys.posasset.entity.EALStockinPUEntity;

@Repository
public interface EALStockInPURepository extends JpaRepository<EALStockinPUEntity, Long>{
	
//	@Query(value="select * from eal_request_map where ealrequest_id=:ealrequest_id",nativeQuery =true)
//	public List<EALRequestMapEntity> getById(@Param("ealrequest_id") Long id);
//	
//	List<EALRequestMapEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	Optional<EALStockinPUEntity> findByStockApplnno(String requestedapplnNo);
	
	@Query(value="select SUM(er.total_numof_barcode) As totalnoofbarcode ,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal_pu er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.created_by=:created_by",nativeQuery=true)
	public List<EALAvailable> getCountByStatusAndCreatedDateBetweenAndCodeType(@Param("fromDate") java.util.Date fromDate,@Param("toDate") java.util.Date toDate,@Param("code_type") String codetype,@Param("created_by") Long createdBy);

//	@Query(value="SELECT (er.code_type) As codetype, SUM(er.total_numof_barcode) As totalnoofbarcode,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal_pu er where er.code_type=:code_type",nativeQuery=true)
//	public List<EALAvailable> getCountByStatusAndCodeType(@Param("code_type") String codetype);
//	
//	@Query(value="SELECT (er.code_type),SUM(er.total_numof_barcode),SUM(er.total_numof_qrcode) from stock_ineal_pu er where er.code_type=:code_type",nativeQuery=true)
//	public List<EALStockinPUEntity> getCountByStatusAndCodeType(@Param("code_type") String codetype);

	
	@Query(value="select * from stock_ineal_pu where eal_requestid=:eal_requestid",nativeQuery =true)
	public List<EALStockinPUEntity> getById(@Param("eal_requestid") Long id);
	
	
	@Query("select a from EALStockinPUEntity a where  a.stockApplnno=:stockApplnno")
	List<EALStockinPUEntity> getByStockApplnno(@Param("stockApplnno") String applnno);
	
	
	@Query("select a from EALStockinPUEntity a where  a.stockApplnno=:stockApplnno GROUP BY packaging_size")
	List<EALStockinPUEntity> getByStockApplno(@Param("stockApplnno") String applnno);
	
	@Query("select a from EALStockinPUEntity a where  a.stockApplnno=:stockApplnno GROUP BY printingType")
	List<EALStockinPUEntity> getByStockapllunmaptype(@Param("stockApplnno") String applnno);
	
	
	@Query(value="SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged, SUM(no_roll_received) As NoofRollcodereceived  FROM stock_ineal_pu where eal_request_applnno=:eal_request_applnno GROUP BY packaging_size",nativeQuery =true)
	public List<BarQrBalanceDTO> getByStockAppln(@Param("eal_request_applnno") String ealrequestapplno);
	
	
	@Query("select a from EALStockinPUEntity a where  a.ealrequestapplno=:ealrequestapplno")
	List<EALStockinPUEntity> getByEALAPPln(@Param("ealrequestapplno") String applnno);
	
	@Query("select a from EALStockinPUEntity a where  a.ealrequestapplno=:ealrequestapplno and a.codeType=:codeType")
	List<EALStockinPUEntity> getByEALAPPlnAndCodeType(@Param("ealrequestapplno") String applnno,@Param("codeType") String codeType);

	
	@Query(value="SELECT packaging_size As packagingSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_pending) As noofBarcodepending,SUM(no_qrcode_pending) As noofqrpending,SUM(no_of_barcode) As noofBarcode,SUM(no_of_qrcode) As noofQrcode,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged  FROM stock_ineal_pu where eal_request_applnno=:eal_request_applnno GROUP BY packaging_size",nativeQuery =true)
	public List<BarQrOverviewDTO> getByStockApplnAll(@Param("eal_request_applnno") String ealrequestapplno);
	
	@Query(value="SELECT packaging_size As packagingSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_pending) As noofBarcodepending,SUM(no_qrcode_pending) As noofqrpending,SUM(no_of_barcode) As noofBarcode,SUM(no_of_qrcode) As noofQrcode,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged  FROM stock_ineal_pu where eal_request_applnno=:eal_request_applnno GROUP BY printing_type",nativeQuery =true)
	public List<BarQrOverviewDTO> getByStockApplnAllpritingType(@Param("eal_request_applnno") String ealrequestapplno);
	
	
	
	@Query(value="SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged,SUM(no_roll_received) As NoofRollcodereceived  FROM stock_ineal_pu where eal_request_applnno=:eal_request_applnno and unmapped_type=:unmapped_type and printing_type=:printing_type  GROUP BY printing_type",nativeQuery =true)
	public List<BarQrBalanceDTO> getByStockApplnAndUnmappedTypeAndPrintingType(@Param("eal_request_applnno") String ealrequestapplno,@Param("unmapped_type") String unmapped_type,@Param("printing_type") String printingtype);
	
//	@Query(value="SELECT packaging_size As packagingSize ,(no_barcode_received) As noofBarcodereceived,(no_qrcode_received) As noofQrcodereceived,(no_barcode_damaged) As noofBarcodedamaged,(no_qrcode_damaged) As noofQrcodedamaged FROM stock_ineal_pu where eal_request_applnno=:eal_request_applnno and unmapped_type=:unmapped_type  GROUP BY packaging_size",nativeQuery =true)
//	public List<BarQrBalanceDTO> getByStockApplnAndUnmappedType(@Param("eal_request_applnno") String ealrequestapplno,@Param("unmapped_type") String unmapped_type);
//	
	
//	@Query(value="SELECT stock_applnno As stockApplnno,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal_pu GROUP BY stock_applnno,printing_type,unmapped_type",nativeQuery =true)
//	public List<ReceviedBarQr> getByStockreceived();
	
	@Query(value="SELECT stock_applnno As stockApplnno,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal_pu GROUP BY stock_applnno",nativeQuery =true)
	public List<ReceviedBarQr> getByStockreceived();
	
	
	@Query("select a from EALStockinPUEntity a where  a.stockApplnno=:stockApplnno and a.unmappedType=:unmappedType")
	List<EALStockinPUEntity> getByStockApplnnoAndUnmappedType(@Param("stockApplnno") String applnno,@Param("unmappedType") String unmappedType);
	
	
	@Query("select a from EALStockinPUEntity a where  a.stockApplnno=:stockApplnno and a.packagingSize=:packagingSize")
	List<EALStockinPUEntity> getByStockApplnnoAndPackagingSize(@Param("stockApplnno") String applnno,@Param("packagingSize") String packagingSize);

	@Query("select a from EALStockinPUEntity a where  a.ealrequestapplno=:ealrequestapplno  GROUP BY packaging_size")
	List<EALStockinPUEntity> getByEalrequestapplno(@Param("ealrequestapplno") String applnno);

	@Query("select a from EALStockinPUEntity a where  a.ealrequestapplno=:ealrequestapplno  GROUP BY printingType,unmappedType")
	List<EALStockinPUEntity> getByEalrequestapplnounmapped(@Param("ealrequestapplno") String applnno);
	
	@Query(value="select SUM(er.total_numof_barcode) As totalnoofbarcode ,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal_pu er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.created_by=:created_by",nativeQuery=true)
	public List<EALAvailable> getCountByStatusAndCreatedDateBetweenAndCodeTypeAndCreatedby(@Param("fromDate") java.util.Date fromDate,@Param("toDate") java.util.Date toDate,@Param("code_type") String codetype,@Param("created_by") Long createdby);

	@Query(value="select SUM(er.total_numof_barcode) As totalnoofbarcode ,SUM(er.total_numof_qrcode) As totalnooqrrcode from stock_ineal_pu er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.created_by=:created_by",nativeQuery=true)
	public List<EALAvailable> getCountByStatusAndCreatedDateBetweenAndUnmappedAndCreatedby(@Param("fromDate") java.util.Date fromDate,@Param("toDate") java.util.Date toDate,@Param("code_type") String codetype,@Param("created_by") Long createdby);

	
	@Query(value="SELECT packaging_size As packagingSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived,SUM(no_barcode_pending) As noofBarcodepending,SUM(no_qrcode_pending) As noofqrpending,SUM(no_of_barcode) As noofBarcode,SUM(no_of_qrcode) As noofQrcode,SUM(no_barcode_damaged) As noofBarcodedamaged,SUM(no_qrcode_damaged) As noofQrcodedamaged  FROM stock_ineal_pu where eal_request_applnno=:eal_request_applnno and unmapped_type=:unmapped_type and printing_type=:printing_type GROUP BY printing_type",nativeQuery =true)
	public List<BarQrOverviewDTO> getByStockApplnAndUnmappedTypeAndPrintingTypeAll(@Param("eal_request_applnno") String ealrequestapplno,@Param("unmapped_type") String unmapped_type,@Param("printing_type") String printingtype);

	
	@Query(value="SELECT stock_applnno As stockApplnno,packaging_size As PackageSize,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal_pu GROUP BY packaging_size",nativeQuery =true)
	public List<ReceviedBarQr> getByoverStockreceived();
	
	
	@Query(value="SELECT stock_applnno As stockApplnno,packaging_size As PackageSize,unmapped_type As unmappedType,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM stock_ineal_pu GROUP BY unmapped_type",nativeQuery =true)
	public List<ReceviedBarQr> getByoverStockreceivedunmap();

	@Query(value = "select * FROM stock_ineal_pu where tp_applnno =:tpApplnNo",nativeQuery = true)
	List<EALStockinPUEntity> getbyTpApplnno(String tpApplnNo);
}
