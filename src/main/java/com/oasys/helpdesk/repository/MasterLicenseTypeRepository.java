package com.oasys.helpdesk.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.entity.MasterLicenseTypeEntity;

public interface MasterLicenseTypeRepository extends JpaRepository<MasterLicenseTypeEntity, Long> {

	List<MasterLicenseTypeEntity> findAllByOrderByModifiedDateDesc();



	List<MasterLicenseTypeEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean is_Active);
}
