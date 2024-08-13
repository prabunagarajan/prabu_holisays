package com.oasys.helpdesk.service;



import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TypeOfUserRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface TypeOfUserService {

	GenericResponse addTypeOfUser(TypeOfUserRequestDTO requestDTO);

	GenericResponse searchByTypeOfUser(PaginationRequestDTO paginationDto);

	GenericResponse updateTypeOfUser(TypeOfUserRequestDTO requestDTO);

	GenericResponse getByTypeOfUser(String typeOfUser);

	GenericResponse getById(Long id);

	GenericResponse getAll();

	GenericResponse getCode();

	GenericResponse getAllActive();



}
