package com.oasys.helpdesk.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.AppModule;
import com.oasys.helpdesk.repository.ActivityRepository;
import com.oasys.helpdesk.repository.AppModuleRepository;
import com.oasys.helpdesk.repository.RoleActivityRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AppModuleServiceImpl implements AppModuleService{

	@Autowired
	private ActivityRepository activityRepository;
	
	@Autowired
	private AppModuleRepository appModuleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleActivityRepository roleActivityRepository;

	@Autowired
	private MessageSource messageSource;

	/**
	 * This method will be used to find all the business app module 
	 * @param authenticationDTO
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	@Override
	public GenericResponse getAllAppModule(AuthenticationDTO authenticationDTO, PaginationRequestDTO requestDTO,Locale locale) {
		GenericResponse baseResponse = null;
		Page<AppModule> moduleList = null;
		try {
			String sortBy = "modifiedDate";
			String orderBy = "desc";
			//find the paging object
			if(requestDTO.getSortField()!=null && requestDTO.getSortOrder()!=null) {
				sortBy = requestDTO.getSortField();
				orderBy = requestDTO.getSortOrder();
			}
			Pageable paging = findPagingObject(requestDTO,sortBy,orderBy);
			if(requestDTO.getSearch()!=null && !requestDTO.getSearch().isEmpty()) {
				//activity code or role name
				moduleList = appModuleRepository.findByCodeOrName(requestDTO.getSearch(),requestDTO.getSearch(),paging);
			}else { //else no filter
				moduleList = appModuleRepository.findAll(paging);
			}
			return Library.getSuccessfulResponse(moduleList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}catch(Exception exception) {
			log.error("===========error while fetching getAllAppModule data===",exception);
			return Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(),
					ErrorMessages.EXCEPTION_MESSAGE);
		}
	
	}
	/**
	 * This method will be used to find the active and parent modules
	 * @return
	 */
	@Override
	public GenericResponse getActiveParentModule(AuthenticationDTO authenticationDTO, Locale locale) {
		GenericResponse baseResponse = null;
		try {
			List<AppModule> appModuleList = appModuleRepository.getActiveParentModule();
			return Library.getSuccessfulResponse(appModuleList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}catch(Exception exception) {
			log.error("===========error while fetching getActiveParentModule data===",exception);
			return Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(),
					ErrorMessages.EXCEPTION_MESSAGE);
		}
	}
	
	/**
	 * This method will be used to find the module object based on the id
	 * @param authenticationDTO
	 * @param locale
	 * @param id
	 * @return
	 */
	@Override
	public GenericResponse getAppModuleById(AuthenticationDTO authenticationDTO, Locale locale, Long id) {
		GenericResponse baseResponse = null;
    	//find by id to get the role master object
    	Optional<AppModule> module = appModuleRepository.findById(id);
		if (module.isPresent()) {
			return Library.getSuccessfulResponse(module, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}else {
			return Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(),
					ErrorMessages.EXCEPTION_MESSAGE);
		}
	}
	 /**
	 * This method will be used to save & update the activity details 
	 * @param user
	 * @param locale
	 * @return
	 */
    @Override
	public GenericResponse addUpdateAppModule(AuthenticationDTO authenticationDTO,Locale locale,AppModule appModule) {
		GenericResponse baseResponse = null;
		try {
			if(appModule.getParentAppModuleId()!=null) {
				AppModule parentAppModule = appModuleRepository.getById(appModule.getParentAppModuleId());
				appModule.setParentModule(parentAppModule);
			}else {
				appModule.setParentModule(null);
			}
			appModule = appModuleRepository.save(appModule);
			return Library.getSuccessfulResponse(appModule, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} catch (Exception exception) {
			return Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(),
					ErrorMessages.EXCEPTION_MESSAGE);
		}
	}
    
	/**
	 * This method will be used to find the pageable object for the pagination
	 * @param requestDTO
	 * @return
	 */
	private Pageable findPagingObject(PaginationRequestDTO requestDTO,String sortBy,String orderBy) {
		Integer pageSize = Constant.DEFAULT_PAGE_SIZE;
		Integer page = Constant.DEFAULT_PAGE;
		if(requestDTO!=null && requestDTO.getPageNo()!=null) {
			page = requestDTO.getPageNo();
		}
		if(requestDTO!=null && requestDTO.getPaginationSize()!=null) {
			pageSize = requestDTO.getPaginationSize();
		}
		Pageable paging = null;
		if(orderBy.equalsIgnoreCase("asc")) {
			paging = PageRequest.of(page, pageSize,Sort.by(sortBy).ascending());
		}else {
			paging = PageRequest.of(page, pageSize,Sort.by(sortBy).descending());
		}
		return paging;
	}
}
