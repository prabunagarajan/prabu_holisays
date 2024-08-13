package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALRequestPUtoBWFLMapEntity;

public interface EALRequestPUtoBWFLMapRepository extends JpaRepository<EALRequestPUtoBWFLMapEntity, Long>{

	@Query(value="select * from eal_request_map_pu_bwfl where ealrequest_id=:ealrequest_id",nativeQuery =true)
	public List<EALRequestPUtoBWFLMapEntity> getById(@Param("ealrequest_id") Long id);
	
	@Query(value="SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM eal_request_map_pu_bwfl where stock_applnno=:stock_applnno GROUP BY packaging_size",nativeQuery =true)
	public List<BarQrBalanceDTO> getByStockApplnno(@Param("stock_applnno") String stockappln);

	public Optional<EALRequestPUtoBWFLMapEntity> findNoofBarcodeById(Long id);

	public List<EALRequestPUtoBWFLMapEntity> findByCreatedByOrderByIdDesc(Long userId);
}
