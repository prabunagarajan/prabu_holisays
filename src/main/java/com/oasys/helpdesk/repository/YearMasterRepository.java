package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteVisitStatusEntity;
import com.oasys.helpdesk.entity.YearMasterEntity;

@Repository
public interface YearMasterRepository extends JpaRepository<YearMasterEntity, Long>{

	@Query(value="select * from year_master where year_code=:yearCode",nativeQuery = true)
	List<YearMasterEntity> findByYearMaster(String yearCode);
	
	@Query(value="select * from year_master ym  where ym.is_active =:status",nativeQuery = true)
	List<YearMasterEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean status);;

	
	


}
