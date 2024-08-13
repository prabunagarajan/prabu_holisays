package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.WorkLocationEntity;

@Repository
public interface WorkLocationRepository extends JpaRepository<WorkLocationEntity, Long>{

	@Query(value = "select u from WorkLocationEntity u where  u.user.id=:userId")
	WorkLocationEntity findByUserId(Long userId);
	
	@Modifying
    @Transactional
	@Query(value ="DELETE  FROM work_location  where user_id=:userID", nativeQuery=true)
	public void deleteAssociatedRecords(@Param("userID") Long userid);
	
	@Query(value = "select u from WorkLocationEntity u where  u.user.id=:userId")
	List<WorkLocationEntity> findAllByUserId(Long userId);
	
	@Query(value = "select * from work_location  where district_code=:district_code",nativeQuery=true)
	Optional<WorkLocationEntity> findByDistrictCode(String district_code);
	
}
