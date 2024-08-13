package com.oasys.posasset.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.oasys.posasset.entity.DeviceMastersEntity;

public interface DeviceMasterRespository extends JpaRepository<DeviceMastersEntity, Long> {

	List<DeviceMastersEntity> findByDeviceNumber(String deviceNumber);

}
