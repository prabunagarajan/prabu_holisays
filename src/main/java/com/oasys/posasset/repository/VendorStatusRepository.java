package com.oasys.posasset.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.posasset.entity.VendorStatusMasterEntity;

@Repository
public interface VendorStatusRepository extends JpaRepository<VendorStatusMasterEntity, Long>{

	Optional<VendorStatusMasterEntity> findByNameIgnoreCase(@Param("name") String name);

	Optional<VendorStatusMasterEntity> findByCodeIgnoreCase(@Param("code") String code);

	

	@Query("SELECT a FROM VendorStatusMasterEntity a where  Upper(a.name) =:name and a.id !=:id")
	Optional<VendorStatusMasterEntity> findByStatusIgnoreCaseNotInId(@Param("name") String name, @Param("id") Long id);

	List<VendorStatusMasterEntity> findAllByOrderByModifiedDateDesc();
	
	Optional<VendorStatusMasterEntity> findByCode(String code);

}
