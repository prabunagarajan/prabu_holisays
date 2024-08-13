package com.oasys.cabs.service;

import javax.validation.Valid;

import com.oasys.cabs.requestDTO.VehicleDetailsRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface VehicleDetailsService {

	GenericResponse add(VehicleDetailsRequestDTO vehicleDetailsRequestDTO);

	GenericResponse update(VehicleDetailsRequestDTO vehicleDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();
}
