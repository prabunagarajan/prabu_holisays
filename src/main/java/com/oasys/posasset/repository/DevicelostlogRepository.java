package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.DeviceLostLogEntity;


@Repository
public interface DevicelostlogRepository extends JpaRepository<DeviceLostLogEntity, Long>{
	
	
	List<DeviceLostLogEntity> findAllByApplicationNumberOrderByCreatedDateDesc(String applicationNo);	

}