package com.devar.cabs.service;

import javax.validation.Valid;

import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.TripDetailsRequestDTO;
import com.devar.cabs.utility.GenericResponse;



public interface TripDetailsService {


	GenericResponse add(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse update(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getPendingList();

	GenericResponse getLastRecordByVehicleNumber(String vehicleNumber);
}
