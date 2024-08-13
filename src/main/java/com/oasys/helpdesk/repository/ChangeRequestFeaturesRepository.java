package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.ChangeRequestFeaturesEntity;

@Repository
public interface ChangeRequestFeaturesRepository extends JpaRepository<ChangeRequestFeaturesEntity, Long> {

	List<ChangeRequestFeaturesEntity> findAllByIsActiveOrderByModifiedDateDesc(Boolean isActive);

	Optional<ChangeRequestFeaturesEntity> findByCodeIgnoreCase(String code);

	Optional<ChangeRequestFeaturesEntity> findById(Long id);

}
