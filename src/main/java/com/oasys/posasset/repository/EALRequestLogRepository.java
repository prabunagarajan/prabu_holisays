package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.DeviceReturnLogEntity;
import com.oasys.posasset.entity.EALRequestLogEntity;

@Repository
public interface EALRequestLogRepository extends JpaRepository<EALRequestLogEntity, Long>{
	
	List<EALRequestLogEntity> findByApplnNoOrderByIdDesc(String applicationNo);
	
	
}
