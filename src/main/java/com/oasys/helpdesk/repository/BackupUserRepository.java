package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.BackupUserEntity;

@Repository
public interface BackupUserRepository extends JpaRepository<BackupUserEntity, Long>{

	List<BackupUserEntity> findByBackupUserId(Long backupUserId);
	
	@Query(value = "select u from BackupUserEntity u where  u.user.id=:userId")
	BackupUserEntity findByUserId(Long userId);
}
