package com.oasys.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ApplicationConstantEntity;

@Repository
public interface ApplicationConstantRepository extends JpaRepository<ApplicationConstantEntity, Long>{
	
	Optional<ApplicationConstantEntity> findByCodeIgnoreCase(@Param("code") String code);
}
