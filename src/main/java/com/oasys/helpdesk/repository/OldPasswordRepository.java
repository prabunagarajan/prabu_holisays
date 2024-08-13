package com.oasys.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.OldPasswordEntity;

@Repository
public interface OldPasswordRepository extends JpaRepository<OldPasswordEntity, Long>{
	
	List<OldPasswordEntity> findByUserId(Long id);

	OldPasswordEntity findTop1ByUserIdOrderByUpdatedDateAsc(Long id);
}
