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
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.DeviceHardwareEntity;

@Repository
public interface AssetBrandRepository extends JpaRepository<AssetBrandEntity, Long> {
Optional<AssetBrandEntity> findByBrandIgnoreCase(@Param("brand") String brand);
	
	
	@Query("SELECT a FROM AssetBrandEntity a where  Upper(a.brand) =:brand and a.id !=:id")
	Optional<AssetBrandEntity> findByBrandIgnoreCaseNotInId(@Param("brand") String brand, @Param("id") Long id);

	List<AssetBrandEntity> findAllByOrderByModifiedDateDesc();
	
	@Query(value ="select a.* from asset_brand a where a.status = true order by a.modified_date desc", nativeQuery=true)
	List<AssetBrandEntity> findAllByStatusOrderByModifiedDateDesc();
	
	@Query("SELECT a FROM AssetBrandEntity a where  a.id =:id and a.status=:status")
	Page<AssetBrandEntity> getByIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandEntity a where  a.id =:id")
	Page<AssetBrandEntity> getById(@Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandEntity a where  a.status=:status")
	Page<AssetBrandEntity> getByStatus(@Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandEntity a ")
	Page<AssetBrandEntity> getAll(Pageable pageable);
	
	List<AssetBrandEntity> findByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);

	@Query("SELECT a FROM AssetBrandEntity a where  a.brand =:brand and a.status=:status and a.type.id=:asset_type_id")
	Page<AssetBrandEntity> getByIdAndStatusAndType(@Param("brand") String brand, @Param("status") Boolean status,@Param("asset_type_id") Long assettypeid ,Pageable pageable);
	
	
	@Query("SELECT a FROM AssetBrandEntity a where   a.status=:status and a.brand =:brand")
	Page<AssetBrandEntity> getByStatusAndBrand( @Param("status") Boolean status,@Param("brand") String brand,Pageable pageable);
	
	
	@Query("SELECT a FROM AssetBrandEntity a where a.status=:status and a.type.id=:asset_type_id ")
	Page<AssetBrandEntity> getByStatusAndType( @Param("status") Boolean status,@Param("asset_type_id") Long assettypeid,Pageable pageable);
	
	
	@Query("SELECT a FROM AssetBrandEntity a where  a.createdBy=:createdBy")
	Page<AssetBrandEntity> getByCreatedBy(@Param("createdBy") Long createdBy,Pageable pageable);
	
	
	@Query("SELECT a FROM AssetBrandEntity a where  a.brand =:brand")
	Page<AssetBrandEntity> getByBrand(@Param("brand") String brand,Pageable pageable);
	
	@Query("SELECT a FROM AssetBrandEntity a where a.type.id=:asset_type_id ")
	Page<AssetBrandEntity> getByType(@Param("asset_type_id") Long assettypeid,Pageable pageable);
	
	
	@Query("select a from AssetBrandEntity a where a.type.id=:type")
	List<AssetBrandEntity> getById(@Param("type") Long type);
	
	
}
