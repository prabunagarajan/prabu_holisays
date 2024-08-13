package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.repository.BarQrBalanceDTO;
import com.oasys.posasset.entity.TPRequestEntity;

@Repository
public interface TpRequestRepository extends JpaRepository<TPRequestEntity, Long>{
	
	List<TPRequestEntity> findAllByOrderByModifiedDateDesc();
	
	@Query(nativeQuery = true, value = "SELECT * FROM dispatch_tp_quantity WHERE eal_request_applnno = :applicationNo")
	List<TPRequestEntity> getEalDispatchedDetailsByApplno(@Param("applicationNo") String applicationNo);

	@Query(value="SELECT packaging_size As packagingSize ,sum(no_barcode_received) As dispatchnoofBarcodereceived,sum(no_qrcode_received) As dispatchnoofQrcodereceived,sum(no_roll_received) As dispatchnoofrollcodereceived  FROM dispatch_tp_quantity where eal_requestid=:eal_requestid GROUP BY packaging_size,carton_size",nativeQuery =true)
	public List<BarQrBalanceDTO> getByEalreqid(@Param("eal_requestid") Long eal_requestid);
	
	@Query(value="SELECT packaging_size As packagingSize ,sum(no_barcode_received) As dispatchnoofBarcodereceived,sum(no_qrcode_received) As dispatchnoofQrcodereceived,sum(no_roll_received) As dispatchnoofrollcodereceived  FROM dispatch_tp_quantity where eal_requestid=:eal_requestid GROUP BY printing_type,unmapped_type,map_type",nativeQuery =true)
	public List<BarQrBalanceDTO> getByEalreqid1(@Param("eal_requestid") Long eal_requestid);

	@Query("select a from TPRequestEntity a where  a.tpApplnno=:tpApplnno and a.vendorStatus=3")
	List<TPRequestEntity> getbyTpApplnno(String tpApplnno);

	@Query(nativeQuery = true, value = "SELECT * FROM dispatch_tp_quantity WHERE tp_applnno = :tpApplnno")
	List<TPRequestEntity> getEalDispatchedDetailsByTpApplno(String tpApplnno);

	List<TPRequestEntity> findByTpApplnno(String tpApplnno);

	@Query(nativeQuery = true, value ="SELECT * FROM dispatch_tp_quantity\r\n" + 
			"WHERE \r\n" + 
			"    (eal_request_applnno = :ealrequestApplnno)\r\n" + 
			"    AND (unmapped_type = :unmappedType OR :unmappedType IS NULL)\r\n" + 
			"    AND (code_type = :codeType OR :codeType IS NULL)\r\n" + 
			"    AND (printing_type = :printingType OR :printingType IS NULL)\r\n" + 
			"    AND (packaging_size = :packagingSize OR :packagingSize IS NULL)\r\n" + 
			"    AND (carton_size = :cartonSize OR :cartonSize IS NULL)\r\n" + 
			"    AND (no_of_barcode = :noofBarcode OR :noofBarcode IS NULL)\r\n" + 
			"    AND (no_of_qrcode = :noofQrcode OR :noofQrcode IS NULL)\r\n" + 
			"    AND (map_type = :mapType OR :mapType IS NULL)\r\n" + 
			"    and (no_of_roll=:noofRoll or :noofRoll IS NULL)")
	List<TPRequestEntity> previouspreviousDispatchList(String ealrequestApplnno, String unmappedType, String codeType,
			String printingType,String packagingSize, String cartonSize, String noofBarcode, String noofQrcode, String mapType,
			 Integer noofRoll);
	
	
	
}
