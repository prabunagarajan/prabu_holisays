package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALStockEntity;

@Repository
public interface EALRequestMapRepository extends JpaRepository<EALRequestMapEntity, Long>{
	
	@Query(value="select * from eal_request_map where ealrequest_id=:ealrequest_id",nativeQuery =true)
	public List<EALRequestMapEntity> getById(@Param("ealrequest_id") Long id);
	
	List<EALRequestMapEntity> findByCreatedByOrderByIdDesc(Long userId);
	
	@Query(value="select * from eal_request_map er where date(er.created_date) between :fromDate and :toDate and er.code_type=:code_type and er.ealrequest_id=:ealrequest_id and er.licenece_number=:licenece_number",nativeQuery=true)
	public List<EALRequestMapEntity> getCountByStatusAndCreatedDateBetweenAndCodeType(@Param("fromDate") java.util.Date fromDate,@Param("toDate") java.util.Date toDate,@Param("code_type") String codetype,@Param("ealrequest_id") Long ealrequestid,@Param("licenece_number") String licenece_number);


	
	@Query(value="SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM eal_request_map where stock_applnno=:stock_applnno GROUP BY packaging_size",nativeQuery =true)
	public List<BarQrBalanceDTO> getByStockApplnno(@Param("stock_applnno") String stockappln);
	
	
	
	@Query(value="SELECT packaging_size As packagingSize ,(no_barcode_received) As noofBarcodereceived,(no_qrcode_received) As noofQrcodereceived FROM eal_request_map where stock_applnno=:stock_applnno GROUP BY packaging_size",nativeQuery =true)
	public List<BarQrBalanceDTO> getByStockAppln(@Param("stock_applnno") String stockappln);
	
	
	Optional<EALRequestMapEntity> findByStockApplnno(String requestedapplnNo);
	
	



}
