package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasys.posasset.entity.EALRequestPUtoBWFLLogEntity;

public interface EALRequestPUtoBWFLLogRepository extends JpaRepository<EALRequestPUtoBWFLLogEntity, Long>{

	List<EALRequestPUtoBWFLLogEntity> findByApplnNoOrderByIdDesc(String applicationNo);

}
