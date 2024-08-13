package com.oasys.helpdesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oasys.helpdesk.entity.DeviceAccessoriesStatusEntity;

@Repository
public interface DeviceAccessoriesStatusRepository extends JpaRepository<DeviceAccessoriesStatusEntity, Long>{

	Optional<DeviceAccessoriesStatusEntity> findByDeviceAccesoriesStatusIgnoreCase(
			@Param("deviceAccesoriesStatus") String deviceAccesoriesStatus);
	
	Optional<DeviceAccessoriesStatusEntity> findByCodeIgnoreCase(@Param("code") String code);
	
	@Query("SELECT a FROM DeviceAccessoriesStatusEntity a where  Upper(a.deviceAccesoriesStatus) =:deviceAccesoriesStatus and a.id !=:id")
	Optional<DeviceAccessoriesStatusEntity> findByAccessoriesStatusIgnoreCaseNotInId(@Param("deviceAccesoriesStatus") String deviceAccesoriesStatus, @Param("id") Long id);

	List<DeviceAccessoriesStatusEntity> findAllByOrderByModifiedDateDesc();

	@Query("SELECT a FROM DeviceAccessoriesStatusEntity a where  a.id =:id and a.status=:status")
	Page<DeviceAccessoriesStatusEntity> getByIdAndStatus(@Param("id") Long id, @Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM DeviceAccessoriesStatusEntity a where  a.id =:id")
	Page<DeviceAccessoriesStatusEntity> getById(@Param("id") Long id,
			Pageable pageable);
	
	@Query("SELECT a FROM DeviceAccessoriesStatusEntity a where  a.status=:status")
	Page<DeviceAccessoriesStatusEntity> getByStatus(@Param("status") Boolean status,
			Pageable pageable);
	
	@Query("SELECT a FROM DeviceAccessoriesStatusEntity a order by modified_date DESC")
	Page<DeviceAccessoriesStatusEntity> getAll(Pageable pageable);
	
	List<DeviceAccessoriesStatusEntity> findByStatusOrderByModifiedDateDesc(@Param("status") Boolean status);

}
