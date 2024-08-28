package com.devar.cabs.service;

import javax.validation.Valid;

import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.requestDTO.VehicleDetailsRequestDTO;
import com.devar.cabs.utility.GenericResponse;



public interface VehicleDetailsService {

	GenericResponse add(VehicleDetailsRequestDTO vehicleDetailsRequestDTO);

	GenericResponse update(VehicleDetailsRequestDTO vehicleDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();

	GenericResponse getNextDate();
}
