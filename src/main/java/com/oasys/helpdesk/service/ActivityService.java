package com.oasys.helpdesk.service;

import java.util.Locale;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.Activity;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

/**
 * This class is responsible to manage the customer service and works as a service layer
 */
public interface ActivityService {

	/**
	 * This api will be used to save or update the activity from the portal
	 * @param authenticationDTO
	 * @param customer
	 * @param locale
	 * @return
	 */
	public GenericResponse saveUpdateActivity(AuthenticationDTO authenticationDTO,Activity activity, Locale locale);
	
	
	/**
	 * This method will be used to get the activity details based on the id
	 * @param customer
	 * @param locale
	 * @return
	 */
    public GenericResponse getActivityById(AuthenticationDTO authenticationDTO,Long id, Locale locale); 
	
	/**
	 * This method will be used to find all the business activity 
	 * @param authenticationDTO
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	public GenericResponse getAllActivity(AuthenticationDTO authenticationDTO, PaginationRequestDTO searchRequestDTO,Locale locale);

	
	/**
	 * This api will be used to find all the active activity
	 * @param authenticationDTO
	 * @param locale
	 * @return
	 */
	public GenericResponse getAllActiveActivity(AuthenticationDTO authenticationDTO,PaginationRequestDTO searchRequestDTO, Locale locale);

	/**
	 * This api will be used to get the role menu list
	 * @param authenticationDTO
	 * @param locale
	 * @return
	 */
	public GenericResponse getRoleMenuList(AuthenticationDTO authenticationDTO, Locale locale);


	/**
	 * This method will be used to find the menu list based on the user logged in
	 * @param authenticationDTO
	 * @param locale
	 * @return
	 */
	public GenericResponse getMenuByUserId(AuthenticationDTO authenticationDTO, Locale locale);

}
