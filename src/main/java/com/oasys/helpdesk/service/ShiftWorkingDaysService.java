package com.oasys.helpdesk.service;




import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ShiftWorkingDaysRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface ShiftWorkingDaysService {

	GenericResponse addShiftWorkingDays(ShiftWorkingDaysRequestDTO requestDTO);

	GenericResponse  updateShiftworkingDays(ShiftWorkingDaysRequestDTO requestDTO);

	GenericResponse getByworkingDays(Long workingdays);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getAllActive();

	GenericResponse searchByWorkingDays(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getCode();


}