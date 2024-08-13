package com.oasys.helpdesk.service;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.EntityDetailsDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.utility.GenericResponse;

import antlr.collections.List;

@Service
public interface EntityService {

	

	//public ResponseEntity<EntityDetails> updateEntity(Long id, EntityDetails ed);

	
	
	GenericResponse getAll();

	GenericResponse getById(Long id);

	GenericResponse addEntityDetail(EntityDetailsDTO requestDTO);
	
	//public ResponseEntity<EntityDetails> getEntityName(EntityDetailsDTO entity_name);

	GenericResponse getEntityName(String entity_name);
	
	GenericResponse getStatus(Boolean is_active);

	GenericResponse getIsactiveTrue(Boolean pass);
	  
	GenericResponse getAllActive();
	
	GenericResponse updateEntity(EntityDetailsDTO requestDTO);
	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData) throws ParseException;




	
}
