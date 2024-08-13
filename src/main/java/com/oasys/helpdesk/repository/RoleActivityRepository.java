package com.oasys.helpdesk.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oasys.helpdesk.entity.Activity;
import com.oasys.helpdesk.entity.RoleActivity;

public interface RoleActivityRepository extends JpaRepository<RoleActivity, Long> {
	
	@Query("select r.activity from RoleActivity r where r.roleMaster.id=:roleid ORDER BY r.activity.displayOrder")
	List<Activity> getActivityByRoleId(@Param("roleid") Long roleId);
	
	@Query("select r.activity from RoleActivity r where r.roleMaster.id=:roleid and r.landingScreen=true")
	Activity getLandingActivityByRoleId(@Param("roleid") Long roleId);
	
	@Query("select r from RoleActivity r where r.roleMaster.id=:roleid ")
	List<RoleActivity> getByRoleId(@Param("roleid") Long roleId);
	
	@Modifying
    @Transactional
	@Query(nativeQuery=true,value="delete from role_activity where role_id=:roleId")
	void deleteByRoleId(@Param("roleId") Long roleId);
}
