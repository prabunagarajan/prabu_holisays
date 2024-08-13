package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.AssetTypeEntity;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetTypeEntity, Long>{

	Optional<AssetTypeEntity> findByTypeIgnoreCase(@Param("type") String type);
	
	Optional<AssetTypeEntity> findByCodeIgnoreCase(@Param("code") String code);
	
	@Query("SELECT a FROM AssetTypeEntity a where  Upper(a.type) =:type and a.id !=:id")
	Optional<AssetTypeEntity> findByTypeIgnoreCaseNotInId(@Param("type") String type, @Param("id") Long id);

	List<AssetTypeEntity> findAllByOrderByModifiedDateDesc();
	
	@Query(value ="select a.* from asset_type_accessories a where a.status = :status order by a.modified_date desc", nativeQuery=true)
	List<AssetTypeEntity> findAllByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);
	
	@Query("SELECT a FROM AssetTypeEntity a where  a.id =:id and a.status=:status")
	Page<AssetTypeEntity> getByIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetTypeEntity a where  a.id =:id")
	Page<AssetTypeEntity> getById(@Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetTypeEntity a where  a.status=:status")
	Page<AssetTypeEntity> getByStatus(@Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM AssetTypeEntity a ")
	Page<AssetTypeEntity> getAll(Pageable pageable);
	
}
