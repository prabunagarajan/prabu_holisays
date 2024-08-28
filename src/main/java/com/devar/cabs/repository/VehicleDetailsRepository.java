package com.devar.cabs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devar.cabs.entity.VehicleDetailsEntity;
import com.devar.cabs.requestDTO.VehicleNextDateDTO;



public interface VehicleDetailsRepository extends JpaRepository<VehicleDetailsEntity, Long> {

	Optional<VehicleDetailsEntity> findByVehicleNumber(String vehicleNumber);

	@Query("SELECT a FROM VehicleDetailsEntity a where  a.vehicleNumber =:vehicleNumber and a.id !=:id")
	Optional<VehicleDetailsEntity> findByVehicleNumberNotInId(String vehicleNumber, Long id);

	List<VehicleDetailsEntity> findAllByOrderByIdDesc();

	List<VehicleDetailsEntity> findByStatusOrderByModifiedDateDesc(Boolean true1);

	@Query(value = "SELECT \r\n" + 
			"    vehicle_number AS vehicleNumber,\r\n" + 
			"    CASE \r\n" + 
			"        WHEN insurance_date = 'LTT' THEN 'LTT' \r\n" + 
			"        ELSE DATEDIFF(insurance_date, CURDATE()) \r\n" + 
			"    END AS nextInsuranceDate,\r\n" + 
			"    CASE \r\n" + 
			"        WHEN tax_date = 'LTT' THEN 'LTT' \r\n" + 
			"        ELSE DATEDIFF(tax_date, CURDATE()) \r\n" + 
			"    END AS nextTaxDate,\r\n" + 
			"    CASE \r\n" + 
			"        WHEN fc_date = 'LTT' THEN 'LTT' \r\n" + 
			"        ELSE DATEDIFF(fc_date, CURDATE()) \r\n" + 
			"    END AS nextFcDate\r\n" + 
			"FROM \r\n" + 
			"    vehicle_details vd;", nativeQuery = true)
	List<VehicleNextDateDTO> getNextDate();

}
