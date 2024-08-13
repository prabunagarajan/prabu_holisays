package com.oasys.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.DeviceMasterEntity;

@Repository
public interface DevicemasterRepository extends JpaRepository<DeviceMasterEntity, Long>{


	Optional<DeviceMasterEntity> findByDeviceNumber(String deviceNumber);
	
	
	
	
}
