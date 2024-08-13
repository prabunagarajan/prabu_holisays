package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.EalWastageLogEntity;

@Repository
public interface EalWastageLogRepository extends JpaRepository<EalWastageLogEntity, Long> {

	List<EalWastageLogEntity> findByApplnNoOrderByIdDesc(String applicationNo);

}
