package com.oasys.helpdesk.repository;

import com.oasys.helpdesk.entity.GrievanceregisterEntity;
import com.oasys.helpdesk.entity.LevelMaster;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelMasterRepository extends JpaRepository<LevelMaster, Long> {
	
	List<LevelMaster> findAllByOrderByModifiedDateDesc();

}
