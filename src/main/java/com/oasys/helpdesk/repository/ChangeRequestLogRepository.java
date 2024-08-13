package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ChangeRequestLogEntity;

@Repository
public interface ChangeRequestLogRepository extends JpaRepository<ChangeRequestLogEntity, Long>{
	
	List<ChangeRequestLogEntity> findByApplnNoOrderByIdDesc(String applicationNo);
	
	
}
