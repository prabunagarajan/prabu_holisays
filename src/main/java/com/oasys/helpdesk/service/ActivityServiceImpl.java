package com.oasys.helpdesk.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.Activity;
import com.oasys.helpdesk.entity.AppModule;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.ActivityRepository;
import com.oasys.helpdesk.repository.AppModuleRepository;
import com.oasys.helpdesk.repository.RoleActivityRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.response.MenuResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ActivityServiceImpl implements ActivityService{

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
	 * This method will be used to find all the business designation 
	 * @param authenticationDTO
	 * @param searchRequestDTO
	 * @param locale
	 * @return
	 */
	@Override
	public GenericResponse getAllActivity(AuthenticationDTO authenticationDTO, PaginationRequestDTO requestDTO,Locale locale) {
		GenericResponse baseResponse = null;
		Page<Activity> activityList = null;
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
				activityList = activityRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(requestDTO.getSearch(),requestDTO.getSearch(),paging);
			}else { //else no filter
				activityList = activityRepository.findAll(paging);
			}
			return Library.getSuccessfulResponse(activityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}catch(Exception exception) {
			log.error("===========error while fetching getAllActivity data===",exception);
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
	public GenericResponse saveUpdateActivity(AuthenticationDTO authenticationDTO, Activity activity, Locale locale) {
    	GenericResponse baseResponse = null;
		try {
			AppModule appModule = null;
			if(activity.getAppModuleId()!=null) {
				appModule = appModuleRepository.getById(activity.getAppModuleId());
				activity.setAppModule(appModule);
			}else {
				activity.setAppModule(null);
			}
			activity = activityRepository.save(activity);
			return Library.getSuccessfulResponse(activity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} catch (Exception exception) {
			return Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(),
					ErrorMessages.EXCEPTION_MESSAGE);
			}
	}
    
    /**
	 * This method will be used to get the activity details based on the id
	 * @param customer
	 * @param locale
	 * @return
	 */
    @Override
    public GenericResponse getActivityById(AuthenticationDTO authenticationDTO,Long id, Locale locale) {
    	GenericResponse baseResponse = null;
    	//find by id to get the role master object
    	Optional<Activity> activity = activityRepository.findById(id);
		if (activity.isPresent()) {
			return Library.getSuccessfulResponse(activity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.INVALID_DATA_ID);
			}
	}
	
    /**
	 * This api will be used to find all the active designation
	 * @param authenticationDTO
	 * @param locale
	 * @return
	 */
    @Override
	public GenericResponse getAllActiveActivity(AuthenticationDTO authenticationDTO,PaginationRequestDTO requestDTO, Locale locale) {
    	GenericResponse baseResponse = null;
		Page<Activity> activityList = null;
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
				activityList = activityRepository.findByActiveAndNameContainingIgnoreCase(true,requestDTO.getSearch(),paging);
			}else { //else no filter
				activityList = activityRepository.findAll(paging);
			}
		}catch(Exception exception) {
			log.error("===========error while fetching getAllActivity data===",exception);
			return Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(),
					ErrorMessages.EXCEPTION_MESSAGE);
			}
		return Library.getSuccessfulResponse(activityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	/**
	 * This api will be used to get the role menu list
	 * @param authenticationDTO
	 * @param locale
	 * @return
	 */
	@Override
	public GenericResponse getRoleMenuList(AuthenticationDTO authenticationDTO, Locale locale) {
		GenericResponse baseResponse = null;
		List<Activity> activityList = activityRepository.getActiveActivity();
		if (activityList != null && activityList.size() > 0) {
			List<AppModule> parentModuleList = appModuleRepository.getAllActiveModule();
			List<MenuResponseDto> menuResponseDtoList = new ArrayList<>();
			for (Activity activityObj : activityList) {
				MenuResponseDto menuResponseDtoItem = new MenuResponseDto();
				menuResponseDtoItem.setId(activityObj.getId());
				menuResponseDtoItem.setName(activityObj.getName());
				menuResponseDtoItem.setIcon(activityObj.getIcon());
				menuResponseDtoItem.setDisplayOrder(activityObj.getDisplayOrder());
				menuResponseDtoItem.setParentModuleId(
						activityObj.getAppModule() != null ? activityObj.getAppModule().getId() : null);
				menuResponseDtoItem.setHasChild(false);
				menuResponseDtoItem.setChildrenList(null);
				menuResponseDtoItem.setIsChecked(false);
				menuResponseDtoList.add(menuResponseDtoItem);
			}
			for (AppModule appModule : parentModuleList) {
				MenuResponseDto menuResponseDto = new MenuResponseDto();
				menuResponseDto.setId(appModule.getId());
				menuResponseDto.setIcon(appModule.getIcon());
				menuResponseDto.setDisplayOrder(appModule.getDisplayOrder());
				menuResponseDto.setName(appModule.getName());
				menuResponseDto.setHasChild(appModule.getRouteUrl() != null ? false : true);
				menuResponseDto.setChildrenList(null);
				menuResponseDto.setIsChecked(false);
				menuResponseDto.setParentModuleId(
						appModule.getParentModule() != null ? appModule.getParentModule().getId() : null);
				menuResponseDtoList.add(menuResponseDto);
			}
				
			} else {
			throw new RecordNotFoundException();
		}
		return Library.getSuccessfulResponse(activityList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	/**
	 * This method will be used to find the menu list based on the user logged in
	 * @param authenticationDTO
	 * @param locale
	 * @return
	 */
	@Override
	public GenericResponse getMenuByUserId(AuthenticationDTO authenticationDTO, Locale locale) {
		GenericResponse baseResponse = null;
		List<MenuResponseDto> menuResponseDTOList = new ArrayList<>();
		UserEntity user = userRepository.findById(authenticationDTO.getUserId()).orElse(null);
		if (user != null) {
			//role list
			List<RoleMaster> roleList = user.getRoles();
			Map<String, Object> menuResponse=new HashMap<String, Object>();
			//landing page
			menuResponse.put("landingUrl", getLandingUrl(roleList.get(0)));
			//menu creation logic
			
			for (RoleMaster roleMaster : roleList) {
				String landingUrl=getLandingUrl(roleList.get(0));
				menuResponseDTOList.addAll(getRoleActiveMenu(roleMaster,landingUrl));
			}
			menuResponse.put("menuList", menuResponseDTOList);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.INVALID_USER_ENTITY_TYPE);
			}
		return Library.getSuccessfulResponse(menuResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	private String getLandingUrl(RoleMaster roleMaster) {
		Activity activity = roleActivityRepository.getLandingActivityByRoleId(roleMaster.getId());
		return activity!=null?activity.getRouteUrl():"";
	}
	
	//Existing code taken from old service
	private List<MenuResponseDto> getRoleActiveMenu(RoleMaster roleMaster,String landingUrl) {
		List<MenuResponseDto> menuResponseDtoList = new ArrayList<>();
		List<Activity> activityList = roleActivityRepository.getActivityByRoleId(roleMaster.getId());

		List<Activity> activityWithoutModuleList=activityList.stream().filter(a->a.getAppModule()==null).collect(Collectors.toList());
		activityList.removeAll(activityWithoutModuleList);
		Map<AppModule, List<Activity>> activityGroupByModuleObj = activityList.stream()
				.collect(Collectors.groupingBy(Activity::getAppModule));
		List<AppModule> parentModuleList = new ArrayList<AppModule>(activityGroupByModuleObj.keySet().stream()
				.filter(m -> m.getParentModule()==null).collect(Collectors.toList()));		
		
		for (AppModule appModule : parentModuleList) {
			MenuResponseDto menuResponseDto = moduletoMenuResponseDto(appModule,landingUrl);
			menuResponseDtoList.add(menuResponseDto);
		}
		
		for (Activity activity : activityWithoutModuleList) {
			MenuResponseDto menuResponseDto = activitytoMenuResponseDto(activity);
			menuResponseDtoList.add(menuResponseDto);
		}
		
				
		menuResponseDtoList = menuResponseDtoList.stream().sorted(Comparator.comparingLong(MenuResponseDto::getDisplayOrder))
	            .collect(Collectors.toList());
				
		for (AppModule appModuleObj : activityGroupByModuleObj.keySet()) {
			if (appModuleObj.getParentModule() != null && appModuleObj.getParentModule().getId() > 0) {
				MenuResponseDto menuResponseDto = moduletoMenuResponseDto(appModuleObj,landingUrl);
				List<MenuResponseDto> menuResponseDtoItemList = new ArrayList<>();
				for (Activity activityObj : activityGroupByModuleObj.get(appModuleObj)) {
					MenuResponseDto menuResponseDtoItem = new MenuResponseDto();
					menuResponseDtoItem.setId(activityObj.getId());
					menuResponseDtoItem.setCode(activityObj.getCode());
					menuResponseDtoItem.setName(activityObj.getName());
					menuResponseDtoItem.setIcon(activityObj.getIcon());
					menuResponseDtoItem.setRouteUrl(activityObj.getRouteUrl());
					menuResponseDtoItem.setDisplayOrder(activityObj.getDisplayOrder());
					menuResponseDtoItem.setType("item");
					menuResponseDtoItem.setChildrenList(null);
					menuResponseDtoItemList.add(menuResponseDtoItem);
				}
				menuResponseDto.getChildrenList().addAll(menuResponseDtoItemList);
				if(!menuResponseDtoList.stream().anyMatch(m -> appModuleObj.getParentModule().getId().equals(m.getId()))) {
					menuResponseDtoList.add(moduletoMenuResponseDto(appModuleObj.getParentModule(),landingUrl));
				}
				menuResponseDtoList.stream().filter(m -> appModuleObj.getParentModule().getId().equals(m.getId()))
						.forEach(m -> m.getChildrenList().add(menuResponseDto));
			} else {
				List<MenuResponseDto> menuResponseDtoItemList = new ArrayList<>();
				for (Activity activityObj : activityGroupByModuleObj.get(appModuleObj)) {
					MenuResponseDto menuResponseDtoItem = new MenuResponseDto();
					menuResponseDtoItem.setId(activityObj.getId());
					menuResponseDtoItem.setCode(activityObj.getCode());
					menuResponseDtoItem.setName(activityObj.getName());
					menuResponseDtoItem.setIcon(activityObj.getIcon());
					menuResponseDtoItem.setRouteUrl(activityObj.getRouteUrl());
					menuResponseDtoItem.setType("item");
					menuResponseDtoItem.setDisplayOrder(activityObj.getDisplayOrder());
					menuResponseDtoItem.setParentModuleId(appModuleObj.getId());
					menuResponseDtoItem.setChildrenList(null);
					menuResponseDtoItemList.add(menuResponseDtoItem);
				}
				menuResponseDtoList.stream().filter(m -> appModuleObj.getId().equals(m.getId()))
						.forEach(m -> m.getChildrenList().addAll(menuResponseDtoItemList));
			}
		}
		
		menuResponseDtoList.forEach(m -> {
			if (m.getChildrenList() != null && !m.getChildrenList().isEmpty()) {
				m.getChildrenList().forEach(m2 -> {
					if (m2.getChildrenList() != null && !m2.getChildrenList().isEmpty()) {
						m2.setChildrenList(m2.getChildrenList().stream()
								.sorted(Comparator.comparingLong(MenuResponseDto::getDisplayOrder))
								.collect(Collectors.toList()));
					} 
				});
				m.setChildrenList(
						m.getChildrenList().stream().sorted(Comparator.comparingLong(MenuResponseDto::getDisplayOrder))
								.collect(Collectors.toList()));
			} 
		});

		menuResponseDtoList = menuResponseDtoList.stream().sorted(Comparator.comparingLong(MenuResponseDto::getDisplayOrder))
	            .collect(Collectors.toList());

		return menuResponseDtoList;
	}

	private MenuResponseDto moduletoMenuResponseDto(AppModule appModule,String landingUrl) {
		MenuResponseDto menuResponseDto = new MenuResponseDto();
		menuResponseDto.setId(appModule.getId());
		menuResponseDto.setIcon(appModule.getIcon());
		menuResponseDto.setDisplayOrder(appModule.getDisplayOrder());
		menuResponseDto.setCode(appModule.getCode());
		menuResponseDto.setRouteUrl(landingUrl);//for UI side implementation added this extra code.
		menuResponseDto.setType("collapse");
		menuResponseDto.setName(appModule.getName());
//		menuResponseDto.setRouteUrl(appModule.getRouteUrl());
		return menuResponseDto;
	}
	
	private MenuResponseDto activitytoMenuResponseDto(Activity activityObj) {
		MenuResponseDto menuResponseDtoItem = new MenuResponseDto();
		menuResponseDtoItem.setId(activityObj.getId());
		menuResponseDtoItem.setCode(activityObj.getCode());
		menuResponseDtoItem.setName(activityObj.getName());
		menuResponseDtoItem.setIcon(activityObj.getIcon());
		menuResponseDtoItem.setRouteUrl(activityObj.getRouteUrl());
		menuResponseDtoItem.setType("item");
		menuResponseDtoItem.setDisplayOrder(activityObj.getDisplayOrder());
//		menuResponseDtoItem.setParentModuleId(appModuleObj.getId());
		menuResponseDtoItem.setChildrenList(null);
		return menuResponseDtoItem;
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
