package com.oasys.helpdesk.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasys.helpdesk.entity.ZabbixMasterServerEntity;

public interface ZabbixMasterServerRepository extends JpaRepository<ZabbixMasterServerEntity, Long> {

	Optional<ZabbixMasterServerEntity> findByServiceId(Long serviceId);

	List<ZabbixMasterServerEntity> findAllByOrderByModifiedDateDesc();
	
	List<ZabbixMasterServerEntity> findAllByOrderByCreatedDateAsc();
}
