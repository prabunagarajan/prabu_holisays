package com.oasys.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.VersionUpgradeEntity;

@Repository
public interface VersionUpgradeRepository extends JpaRepository<VersionUpgradeEntity, Long> {

	@Query(nativeQuery=true,value="Select * from version_upgrade v order by v.created_date Desc limit 1")
	VersionUpgradeEntity findTop1OrderByCreatedDateDesc();

}
