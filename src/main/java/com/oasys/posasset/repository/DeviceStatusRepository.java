package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.DeviceEntity;
import com.oasys.posasset.entity.DeviceLogEntity;
import com.oasys.posasset.entity.DevicestatusEntity;
@Repository
public interface DeviceStatusRepository extends JpaRepository<DevicestatusEntity, Long>{ 
	
	Optional<DevicestatusEntity> findByCode(String code);
	
	List<DevicestatusEntity> findAll();

}
