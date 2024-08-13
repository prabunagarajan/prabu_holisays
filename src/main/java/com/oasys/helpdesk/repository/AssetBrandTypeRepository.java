package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetBrandEntity;
import com.oasys.helpdesk.entity.AssetBrandTypeEntity;
import com.oasys.helpdesk.entity.AssetTypeEntity;

@Repository
public interface AssetBrandTypeRepository extends JpaRepository<AssetBrandTypeEntity, Long>{

	Optional<AssetBrandTypeEntity> findByCodeIgnoreCase(@Param("code") String code);
	List<AssetBrandTypeEntity> findAllByOrderByModifiedDateDesc();
	
	List<AssetBrandTypeEntity> findByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.brand.id =:brand and a.type.id=:type")
	Optional<AssetBrandTypeEntity> findByBrandAndType(@Param("brand") Long brand, @Param("type") Long type);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.brand.id =:brand and a.type.id=:type and a.id !=:id")
	Optional<AssetBrandTypeEntity> findByBrandAndTypeNotInId(@Param("brand") Long brand, @Param("type") Long type, @Param("id") Long id);
	
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.brand.id =:brand and a.status=:status")
	Page<AssetBrandTypeEntity> getByBrandAndStatus(@Param("brand") Long brand, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.type.id =:type and a.status=:status")
	Page<AssetBrandTypeEntity> getByTypeAndStatus(@Param("type") Long type, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.type.id =:type and a.brand.id=:brand")
	Page<AssetBrandTypeEntity> getByTypeAndBrand(@Param("type") Long type, @Param("brand") Long brand,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.brand.id =:brand and a.type.id =:type and a.status=:status")
	Page<AssetBrandTypeEntity> getByTypeStatusAndBrand(@Param("type") Long type, @Param("status") Boolean status,
			@Param("brand") Long brand, Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.brand.id =:brand")
	Page<AssetBrandTypeEntity> getByBrand(@Param("brand") Long brand,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.type.id =:type")
	Page<AssetBrandTypeEntity> getByType(@Param("type") Long type,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a where  a.status=:status")
	Page<AssetBrandTypeEntity> getByStatus(@Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandTypeEntity a ")
	Page<AssetBrandTypeEntity> getAll(Pageable pageable);
	
	@Query("SELECT a.brand FROM AssetBrandTypeEntity a where  a.type.id =:assetTypeId and a.status=:status  order by a.modifiedDate desc")
	List<AssetBrandEntity> getAssetBrandByAssetTypeId(@Param("assetTypeId") Long assetTypeId, @Param("status") Boolean status);
	
	@Query("SELECT a.type FROM AssetBrandTypeEntity a where  a.brand.id =:assetBrandId and a.status=:status order by a.modifiedDate desc")
	List<AssetTypeEntity> getAssetTypeByAssetBrandId(@Param("assetBrandId") Long assetBrandId, @Param("status") Boolean status);
	
}
