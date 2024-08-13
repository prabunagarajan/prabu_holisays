package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.UserShiftConfigurationEntity;

@Repository
public interface UserShiftConfigurationRepository extends JpaRepository<UserShiftConfigurationEntity, Long>{
	
	@Query(value = "select u from UserShiftConfigurationEntity u where  u.user.id=:userId")
	UserShiftConfigurationEntity findByUserId(Long userId);
}
