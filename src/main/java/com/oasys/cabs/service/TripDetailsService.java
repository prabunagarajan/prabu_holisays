package com.oasys.cabs.service;

import javax.validation.Valid;

import com.oasys.cabs.requestDTO.TripDetailsRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface TripDetailsService {


	GenericResponse add(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse update(TripDetailsRequestDTO tripDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getPendingList();

	GenericResponse getLastRecordByVehicleNumber(String vehicleNumber);
}
