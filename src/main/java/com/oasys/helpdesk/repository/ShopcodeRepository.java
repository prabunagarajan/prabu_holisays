package com.oasys.helpdesk.repository;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ShopcodeEntity;
import com.oasys.helpdesk.entity.UserEntity;

@Repository
public interface ShopcodeRepository extends JpaRepository<ShopcodeEntity, Long> {
	
	@Modifying
    @Transactional
	@Query(value ="UPDATE shopcode_master SET user_id =:mappingId WHERE user_id=:currentFieldID", nativeQuery=true)
	public void updateAssociatedRecords(@Param("mappingId") String mappingId,@Param("currentFieldID") String currentFieldID);
	
	
	public Optional<ShopcodeEntity> findByShopCode(@Param("shopCode") String shopcode);
	
public List<ShopcodeEntity> findByUserId(@Param("userId") String empcode);
	
	//public Optional<ShopcodeEntity> findByUserId(@Param("userId") String empcode);
	
}
