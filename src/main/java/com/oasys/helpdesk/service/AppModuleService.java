package com.oasys.helpdesk.service;

import java.util.Locale;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AppModule;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;


/**
 * This class is responsible to manage the customer service and works as a service layer
 */
public interface AppModuleService {
	
	/**
	 * This method will be used to find all the business app module 
	 * @param authenticationDTO
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	public GenericResponse getAllAppModule(AuthenticationDTO authenticationDTO, PaginationRequestDTO searchRequestDTO,Locale locale);

	/**
	 * This method will be used to find the active and parent modules
	 * @return
	 */
	public GenericResponse getActiveParentModule(AuthenticationDTO authenticationDTO, Locale locale);
	
	/**
	 * This method will be used to find the module object based on the id
	 * @param authenticationDTO
	 * @param locale
	 * @param id
	 * @return
	 */
	public GenericResponse getAppModuleById(AuthenticationDTO authenticationDTO, Locale locale, Long id);

	/**
	 * This api will be used to save or update the activity from the portal
	 * @param authenticationDTO
	 * @param customer
	 * @param locale
	 * @return
	 */
	public GenericResponse addUpdateAppModule(AuthenticationDTO authenticationDTO,Locale locale,AppModule appModule);
}
