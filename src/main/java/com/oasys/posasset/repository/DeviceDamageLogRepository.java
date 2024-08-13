package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasys.posasset.entity.DeviceDamageLogEntity;

public interface DeviceDamageLogRepository extends JpaRepository<DeviceDamageLogEntity, Long> {
	
	List<DeviceDamageLogEntity> findByApplicationNoOrderByIdDesc(String applicationNo);

}
