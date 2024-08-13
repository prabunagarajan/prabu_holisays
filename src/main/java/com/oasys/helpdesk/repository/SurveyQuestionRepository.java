package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SurveyQuestionMaster;
import com.oasys.posasset.entity.DeviceReturnEntity;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestionMaster, Long>{

	
	List<SurveyQuestionMaster> findByIsActiveOrderByModifiedDateDesc(Boolean questintrue);
	
}
