package com.devar.cabs.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.TripDetailsRequestDTO;
import com.devar.cabs.utility.GenericResponse;

public interface TripDetailsService {

	GenericResponse add(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse update(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse updateApproval(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getPendingList();

	GenericResponse getLastRecordByVehicleNumber(String vehicleNumber);

	List<Map<String, Object>> getTotalVehicleTripsAndProfit(int month, int year);
	
	List<Map<String, Object>> getTotalDriverTripsAndSalary(int month, int year);

//	List<Map<String, Object>> get3MonthVehicleTripsAndProfit();
	
}
