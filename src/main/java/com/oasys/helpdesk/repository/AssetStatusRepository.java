package com.oasys.helpdesk.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetStatusEntity;

@Repository

public interface AssetStatusRepository extends JpaRepository<AssetStatusEntity, Long> {

	Optional<AssetStatusEntity> findByNameIgnoreCase(@Param("name") String name);

	Optional<AssetStatusEntity> findByCodeIgnoreCase(@Param("code") String code);

	

	@Query("SELECT a FROM AssetStatusEntity a where  Upper(a.name) =:name and a.id !=:id")
	Optional<AssetStatusEntity> findByStatusIgnoreCaseNotInId(@Param("name") String name, @Param("id") Long id);

	List<AssetStatusEntity> findAllByOrderByModifiedDateDesc();
	
	Optional<AssetStatusEntity> findByCode(String code);

}
