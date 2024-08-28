package com.devar.cabs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devar.cabs.entity.TripDetailsEntity;


public interface TripDetailsRepository extends JpaRepository<TripDetailsEntity, Long> {

	List<TripDetailsEntity> findAllByOrderByIdDesc();

	@Query(value="select * from trip_details td where pending_amount != 0 order by id desc",nativeQuery = true)
	List<TripDetailsEntity> getPendingList();

	@Query(value="select * from trip_details td where vehicle_number=:vehicleNumber order by id desc limit 1",nativeQuery = true)
	Optional<TripDetailsEntity> getLastRecordByVehicleNumber(String vehicleNumber);

}
