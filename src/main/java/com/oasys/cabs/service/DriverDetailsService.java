package com.oasys.cabs.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.oasys.cabs.requestDTO.DriverDetailsRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;



public interface DriverDetailsService {


	GenericResponse add(DriverDetailsRequestDTO driverDetailsRequestDTO);

	GenericResponse update(DriverDetailsRequestDTO driverDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();


}
