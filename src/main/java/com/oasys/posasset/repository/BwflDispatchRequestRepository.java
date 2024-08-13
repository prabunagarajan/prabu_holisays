package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.BwflDispatchRequestEntity;
import com.oasys.posasset.entity.EALRequestEntity;
import com.oasys.posasset.entity.EALRequestMapEntity;

@Repository
public interface BwflDispatchRequestRepository extends JpaRepository<BwflDispatchRequestEntity, Long> {

	@Query(value = "select * from dispatch_tp_quantity_pu_bwfl where id=:id", nativeQuery = true)
	List<BwflDispatchRequestEntity> getById(@Param("id") Long id);
	
	Optional<BwflDispatchRequestEntity> findByealrequestapplno(String applnno);

	@Query(nativeQuery = true, value = "SELECT * FROM dispatch_tp_quantity_pu_bwfl WHERE eal_request_applnno = :applicationNo")
	List<BwflDispatchRequestEntity> getEalDispatchedDetailsByApplno(String applicationNo);

	
	
	@Query(value = "select * from dispatch_tp_quantity_pu_bwfl where  eal_request_applnno=:applicationNo and licenseNo=:licenseno and vendorStatus=3",nativeQuery = true)
	Optional<BwflDispatchRequestEntity> getByRequestedapplnNoAndStatusAckAndLicNo(String applicationNo, String licenseno);

	

}
