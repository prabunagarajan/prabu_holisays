package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.posasset.entity.EALRequestMapAECEntity;

@Repository
public interface EALRequestMapAECRepository extends JpaRepository<EALRequestMapAECEntity, Long>{
	
	@Query(value="select * from eal_request_map_aec where ealrequest_id=:ealrequest_id",nativeQuery =true)
	public List<EALRequestMapAECEntity> getById(@Param("ealrequest_id") Long id);
	
	@Query(value="SELECT packaging_size As packagingSize ,SUM(no_barcode_received) As noofBarcodereceived,SUM(no_qrcode_received) As noofQrcodereceived FROM eal_request_map_aec where stock_applnno=:stock_applnno GROUP BY packaging_size",nativeQuery =true)
	public List<BarQrBalanceDTO> getByStockApplnno(@Param("stock_applnno") String stockappln);

	public Optional<EALRequestMapAECEntity> findNoofBarcodeById(Long id);

	public List<EALRequestMapAECEntity> findByCreatedByOrderByIdDesc(Long userId);

	public List<EALRequestMapAECEntity> getByEalrequestId(Long id);



}
