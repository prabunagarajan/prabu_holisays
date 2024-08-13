package com.oasys.posasset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.EALRequestLogAECEntity;

@Repository
public interface EALRequestLogAECRepository extends JpaRepository<EALRequestLogAECEntity, Long>{

	List<EALRequestLogAECEntity> findByApplnNoOrderByIdDesc(String applicationNo);
}
