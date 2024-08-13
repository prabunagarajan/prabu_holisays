package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SurveyFormListEntity;
import com.oasys.posasset.entity.DeviceReturnLogEntity;

@Repository
public interface SurveyFormListRepository extends JpaRepository<SurveyFormListEntity, Long>{
	
	List<SurveyFormListEntity> findByTicketNoOrderByIdDesc(String ticketno);

	
}
