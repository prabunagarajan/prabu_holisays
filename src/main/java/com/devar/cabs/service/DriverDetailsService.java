package com.devar.cabs.service;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.devar.cabs.requestDTO.DriverDetailsRequestDTO;
import com.devar.cabs.requestDTO.PaginationRequestDTO;
import com.devar.cabs.utility.GenericResponse;

public interface DriverDetailsService {

	GenericResponse add(DriverDetailsRequestDTO driverDetailsRequestDTO);

	GenericResponse update(DriverDetailsRequestDTO driverDetailsRequestDTO);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getsubPagesearchNewByFilter(@Valid PaginationRequestDTO paginationRequestDTO);

	GenericResponse getAllActive();

	void importExcelData(MultipartFile file);

}
