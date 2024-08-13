package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasys.posasset.entity.DeviceReturnLogEntity;

public interface DeviceReturnLogRepository extends JpaRepository<DeviceReturnLogEntity, Long> {
	
	List<DeviceReturnLogEntity> findByApplicationNoOrderByIdDesc(String applicationNo);

}
