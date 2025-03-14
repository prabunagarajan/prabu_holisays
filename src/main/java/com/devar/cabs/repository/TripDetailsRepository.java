package com.devar.cabs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devar.cabs.entity.TripDetailsEntity;

public interface TripDetailsRepository extends JpaRepository<TripDetailsEntity, Long> {

	List<TripDetailsEntity> findAllByOrderByIdDesc();

	@Query(value = "select * from trip_details td where pending_amount != 0 order by id desc", nativeQuery = true)
	List<TripDetailsEntity> getPendingList();

	@Query(value = "select * from trip_details td where vehicle_number=:vehicleNumber and status != 5 order by id desc limit 1", nativeQuery = true)
	Optional<TripDetailsEntity> getLastRecordByVehicleNumber(String vehicleNumber);

	@Query("SELECT td.vehicleNumber, COUNT(td.id), SUM(td.profitAmount) " + "FROM TripDetailsEntity td "
			+ "WHERE td.status IN ('1','2','3') " + "AND FUNCTION('MONTH', td.createdDate) = :month "
			+ "AND FUNCTION('YEAR', td.createdDate) = :year " + "GROUP BY td.vehicleNumber")
	List<Object[]> getTotalTripsAndProfitByVehicle(@Param("month") int month, @Param("year") int year);

//	@Query("SELECT td.vehicleNumber, COUNT(td.id), SUM(td.profitAmount), FUNCTION('MONTH', td.createdDate), FUNCTION('YEAR', td.createdDate) " +
//		       "FROM TripDetailsEntity td " +
//		       "WHERE td.status IN ('1','2','3') " +
//		       "AND td.createdDate >= FUNCTION('DATE_SUB', CURRENT_DATE, INTERVAL 3 MONTH) " +
//		       "GROUP BY td.vehicleNumber, FUNCTION('MONTH', td.createdDate), FUNCTION('YEAR', td.createdDate) " +
//		       "ORDER BY FUNCTION('YEAR', td.createdDate) DESC, FUNCTION('MONTH', td.createdDate) DESC")
//		List<Object[]> getLastThreeMonthsTotalTripsAndProfit();


	
	@Query("SELECT td.driverName, COUNT(td.id), SUM(td.driverPayment) " + "FROM TripDetailsEntity td "
			+ "WHERE td.status IN ('1','2','3') " + "AND FUNCTION('MONTH', td.createdDate) = :month "
			+ "AND FUNCTION('YEAR', td.createdDate) = :year " + "GROUP BY td.driverName")
	List<Object[]> getTotalDriverTripsAndSalary(@Param("month") int month, @Param("year") int year);
}
