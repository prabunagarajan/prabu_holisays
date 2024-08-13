package com.oasys.cabs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oasys.cabs.entity.VehicleDetailsEntity;

public interface VehicleDetailsRepository extends JpaRepository<VehicleDetailsEntity, Long> {

	Optional<VehicleDetailsEntity> findByVehicleNumber(String vehicleNumber);

	@Query("SELECT a FROM VehicleDetailsEntity a where  a.vehicleNumber =:vehicleNumber and a.id !=:id")
	Optional<VehicleDetailsEntity> findByVehicleNumberNotInId(String vehicleNumber, Long id);

	List<VehicleDetailsEntity> findAllByOrderByIdDesc();

	List<VehicleDetailsEntity> findByStatusOrderByModifiedDateDesc(Boolean true1);

}
