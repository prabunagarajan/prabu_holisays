package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetAccessoriesEntity;
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;

@Repository
public interface AssetAccessoriesRepository extends JpaRepository<AssetAccessoriesEntity, Long> {

	Optional<AssetAccessoriesEntity> findById(Long id);

	Optional<AssetAccessoriesEntity> findByAccessoriesCodeIgnoreCase(String accessoriesCode);

	Optional<AssetAccessoriesEntity> findByAccessoriesNameAndId(String accessoriesName, Long id);


	@Query("SELECT a FROM AssetAccessoriesEntity a where  a.accessoriesName =:accessoriesName and  a.status=:status")
	Page<AssetAccessoriesEntity> getByAccessoriesNameAndStatus(@Param("accessoriesName") String assetAccessories,@Param("status")  String status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetAccessoriesEntity a where a.accessoriesName like %:assetAccessories% ")
	Page<AssetAccessoriesEntity> getAllSubString(String assetAccessories, Pageable pageable);

	List<AssetAccessoriesEntity> findAllByStatusOrderByModifiedDateDesc(Boolean status);

	List<AssetAccessoriesEntity> findAllByOrderByModifiedDateDesc();
	
	AssetAccessoriesEntity getById(@Param("id")  Long id);


}
