package com.oasys.helpdesk.service;

import static com.oasys.posasset.constant.Constant.ASC;
import static com.oasys.posasset.constant.Constant.CREATED_DATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.ShopcodeDTO;
import com.oasys.helpdesk.dto.ShopcodeResponseDTO;
import com.oasys.helpdesk.dto.UserRequestConDTO;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.ShopcodeEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.mapper.ShopcodeMasterMapper;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.ShopcodeRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.security.ServiceHeader;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ShopcodeMasterService {

	@Autowired
	ShopcodeRepository shopcodeRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ServiceHeader serviceHeader;

	@Autowired
	HttpServletRequest headerRequest;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private ShopcodeMasterMapper shopcodemastermapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleMasterRepository rolemasetrepo;
	
	
	public GenericResponse add(List<ShopcodeDTO> requestDTO) {
		try {
			requestDTO.stream().forEach(shopconfig -> {
				ShopcodeEntity shopentity = new ShopcodeEntity();
				shopentity.setDivision(shopconfig.getDivision());
				shopentity.setDistrictCode(shopconfig.getDistrictCode());
				shopentity.setUserId(shopconfig.getUserId());
				shopentity.setShopCode(shopconfig.getShopCode());
				shopentity.setStateCode(shopconfig.getStateCode());
				shopentity.setActive(true);
				shopentity.setDistrictName(shopconfig.getDistrictName());
				//shopentity.setEmployeeId(shopconfig.getEmployeeId());
				shopcodeRepository.save(shopentity);
			});
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Record Already Exist");
		}
		return Library.getSuccessfulResponseT(ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}
	

	public GenericResponse getById(Long id) {
		Optional<ShopcodeEntity> depTypeEntity = shopcodeRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(depTypeEntity.get(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	

	public GenericResponse getsubPagesearchNewByFilter(PaginationRequestDTO requestData) {
		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();
		List<ShopcodeEntity> list = this.getSubRecordsByFilterDTO1(requestData);
		List<ShopcodeEntity> list1 = this.getSubRecordsByFilterDTO2(requestData);
		if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(list1)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(), "No Record Found");
		}
	 	if (!list.isEmpty()) {
	 	
	 		List<ShopcodeResponseDTO> dtoList = list.stream().map(shopcodemastermapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());
	 		paginationResponseDTO.setContents(dtoList);
		}	
		Long count1=(long) list1.size();
		paginationResponseDTO.setNumberOfElements(Objects.nonNull(list1.size()) ? list1.size() : null);
		paginationResponseDTO.setTotalElements(count1);		
		return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
		} 
	
	public List<ShopcodeEntity> getSubRecordsByFilterDTO1(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ShopcodeEntity> cq = cb.createQuery(ShopcodeEntity.class);
		Root<ShopcodeEntity> from = cq.from(ShopcodeEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<ShopcodeEntity> typedQuery = null;
		addSubCriteria(cb, list, filterRequestDTO, from);	
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(CREATED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get(CREATED_DATE)));
		}
		if (Objects.nonNull(filterRequestDTO.getPageNo())
				&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);
		}				
		return typedQuery.getResultList();
	}
	
	public List<ShopcodeEntity> getSubRecordsByFilterDTO2(PaginationRequestDTO filterRequestDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ShopcodeEntity> cq = cb.createQuery(ShopcodeEntity.class);
		Root<ShopcodeEntity> from = cq.from(ShopcodeEntity.class);
		List<Predicate> list = new ArrayList<>();
		TypedQuery<ShopcodeEntity> typedQuery1 = null;
		addSubCriteria(cb, list, filterRequestDTO, from);			
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(CREATED_DATE);
		}
		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));
		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		}
		typedQuery1 = entityManager.createQuery(cq);		
		return typedQuery1.getResultList();
	}
	
	private void addSubCriteria(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequestDTO,
			Root<ShopcodeEntity> from) {

		if (Objects.nonNull(filterRequestDTO.getFilters().get("division"))
				&& !filterRequestDTO.getFilters().get("division").toString().trim().isEmpty()) {

			String divisionid =filterRequestDTO.getFilters().get("division").toString();
			list.add(cb.equal(from.get("division"), divisionid));
		}

		if (Objects.nonNull(filterRequestDTO.getFilters().get("shopCode"))
				&& !filterRequestDTO.getFilters().get("shopCode").toString().trim().isEmpty()) {

			String shopCode = String.valueOf((filterRequestDTO.getFilters().get("shopCode")).toString());
			list.add(cb.equal(from.get("shopCode"), shopCode));
		}
		
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("districtCode"))
				&& !filterRequestDTO.getFilters().get("districtCode").toString().trim().isEmpty()) {

			Integer districtCode = Integer.valueOf((filterRequestDTO.getFilters().get("districtCode")).toString());
			list.add(cb.equal(from.get("districtCode"), districtCode));
		}
		
		
		
		
		if (Objects.nonNull(filterRequestDTO.getFilters().get("userId"))
				&& !filterRequestDTO.getFilters().get("userId").toString().trim().isEmpty()) {
		    String userId = String.valueOf((filterRequestDTO.getFilters().get("userId")).toString());
			String employeeid = String.valueOf((filterRequestDTO.getFilters().get("userId")).toString());
			String str = employeeid;
			String[] part = str.split("(?<=\\D)(?=\\d)");
			System.out.println(part[0]);
			if(part[0].equalsIgnoreCase("EM")) {
				String empid = String.valueOf((filterRequestDTO.getFilters().get("userId")).toString());
				//list.add(cb.equal(from.get("employeeId"), empid));
				Optional<UserEntity> userentity= userRepository.findByEmployeeId(empid);	
				String username=userentity.get().getUsername();
				list.add(cb.equal(from.get("userId"), username));	
			}else {
				list.add(cb.equal(from.get("userId"), userId));	
			}
			
		}
		
//		if (Objects.nonNull(filterRequestDTO.getFilters().get("userId"))
//				&& !filterRequestDTO.getFilters().get("userId").toString().trim().isEmpty()) {
//
//			String empid = String.valueOf((filterRequestDTO.getFilters().get("userId")).toString());
//			list.add(cb.equal(from.get("employeeId"), empid));
//		}
		
	}
	
	
	
	public GenericResponse update(List<ShopcodeDTO> requestDTO) {
		try {
			requestDTO.stream().forEach(shopconfig -> {
				Optional<ShopcodeEntity> depTypeEntity = shopcodeRepository.findById(shopconfig.getId());	
				if(depTypeEntity.isPresent()) {
				ShopcodeEntity shopentity =depTypeEntity.get();
				shopentity.setDivision(shopconfig.getDivision());
				shopentity.setDistrictCode(shopconfig.getDistrictCode());
				shopentity.setUserId(shopconfig.getUserId());
				shopentity.setShopCode(shopconfig.getShopCode());
				shopentity.setStateCode(shopconfig.getStateCode());
				shopentity.setActive(true);
				shopentity.setDistrictName(shopconfig.getDistrictName());
				//shopentity.setEmployeeId(shopconfig.getEmployeeId());
				shopcodeRepository.save(shopentity);
			}
			});
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Please Check Given Record");
		}
		return Library.getSuccessfulResponseT(ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}
	
	
	
	public GenericResponse updatemapping(ShopcodeDTO requestDTO) {
		try {

//			Optional<ShopcodeEntity> depTypeEntity = shopcodeRepository.findByUserId(requestDTO.getCurrentfieldId());
//			if (depTypeEntity.isPresent()) {
//				String shopcode = depTypeEntity.get().getShopCode();
//				if (shopcode != null) {
//					shopcodeRepository.updateAssociatedRecords(requestDTO.getMappingId(),
//							requestDTO.getCurrentfieldId());
//				};
//			} else {
//				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//			}
			
			List<ShopcodeEntity> depTypeEntity = shopcodeRepository.findByUserId(requestDTO.getCurrentfieldId());
			if(requestDTO.getCurrentfieldId().equalsIgnoreCase(requestDTO.getMappingId())) {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),"Both are Same cant Map");
			}
			ShopcodeDTO rq=new ShopcodeDTO();
			if(!depTypeEntity.isEmpty()) {
			depTypeEntity.stream().forEach(useridlist ->{
			String shopcode=useridlist.getShopCode();
			rq.setShopCode(shopcode);
			});
			if (rq.getShopCode() != null) {
				shopcodeRepository.updateAssociatedRecords(requestDTO.getMappingId(),
						requestDTO.getCurrentfieldId());
			}	
				
			}
			
			else {
				return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
						ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
			}

		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					"Current Filed Engineeer doesnot have ShopCode");
		}
		return Library.getSuccessfulResponseT(ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_UPDATED);
	}
	
	
	
	
	
	public GenericResponse shopcodebasedUserDetails(ShopcodeDTO requestDTO) {
		
		List<UserRequestConDTO> uselist=new ArrayList<UserRequestConDTO>();
		try {
		Optional<ShopcodeEntity> depTypeEntity = shopcodeRepository.findByShopCode(requestDTO.getShopCode());	
		if(depTypeEntity.isPresent()) {
		String emp=depTypeEntity.get().getUserId();
		Optional<UserEntity> userentity= userRepository.findByUsername(emp);
		if(userentity.isPresent()) {
		UserRequestConDTO user=new UserRequestConDTO();
		user.setId(userentity.get().getId());
		user.setEmailId(userentity.get().getEmailId());
		user.setEmployeeId(userentity.get().getEmployeeId());
		user.setFirstName(userentity.get().getFirstName());
		user.setLastName(userentity.get().getLastName());
		user.setMiddleName(userentity.get().getMiddleName());
		user.setPhoneNumber(userentity.get().getPhoneNumber());
		user.setUsername(userentity.get().getUsername());
		user.setUsernameMiddlename(userentity.get().getUsername()+"-"+ (userentity.get().getMiddleName()));
	    uselist.add(user);
		}
		}
		
		Long sofware=(long)12;
		List<UserEntity> userList = new ArrayList<>();
		userList = userRepository.getUserByRoleIdsoftware(sofware);
		userList.stream().forEach(softwareaction ->{
	    UserRequestConDTO sofwareuser=new UserRequestConDTO();
	    sofwareuser.setId(softwareaction.getId());
	    sofwareuser.setEmailId(softwareaction.getEmailId());
	    sofwareuser.setEmployeeId(softwareaction.getEmployeeId());
	    sofwareuser.setFirstName(softwareaction.getFirstName());
	    sofwareuser.setLastName(softwareaction.getLastName());
	    sofwareuser.setMiddleName(softwareaction.getMiddleName());	
	    sofwareuser.setPhoneNumber(softwareaction.getPhoneNumber());
	    sofwareuser.setUsername(softwareaction.getUsername());
	    sofwareuser.setUsernameMiddlename(softwareaction.getUsername()+"-"+ softwareaction.getMiddleName());
	    uselist.add(sofwareuser);
		});
		
		
		List<UserEntity> userListcctv = new ArrayList<>();
	    RoleMaster role=rolemasetrepo.findByRoleCode("CCTV_VENDOR");
		userListcctv = userRepository.getUserByRoleIdCCTV(role.getId());
		userListcctv.stream().forEach(cctvaction ->{
	    UserRequestConDTO cctvuser=new UserRequestConDTO();
	    cctvuser.setId(cctvaction.getId());
	    cctvuser.setEmailId(cctvaction.getEmailId());
	    cctvuser.setEmployeeId(cctvaction.getEmployeeId());
	    cctvuser.setFirstName(cctvaction.getFirstName());
	    cctvuser.setLastName(cctvaction.getLastName());
	    cctvuser.setMiddleName(cctvaction.getMiddleName());	
	    cctvuser.setPhoneNumber(cctvaction.getPhoneNumber());
	    cctvuser.setUsername(cctvaction.getUsername());
	    cctvuser.setUsernameMiddlename(cctvaction.getUsername()+"-"+ cctvaction.getMiddleName());
	    uselist.add(cctvuser);
		});
		
		
		List<UserEntity> userListDepart = new ArrayList<>();
	    RoleMaster departmentrole =rolemasetrepo.findByRoleCode("HELPDESK_DEPARTMENT");
	    userListDepart = userRepository.getUserByRoleIdCCTV(departmentrole.getId());
	    userListDepart.stream().forEach(departmentaction ->{
	    UserRequestConDTO departuser=new UserRequestConDTO();
	    departuser.setId(departmentaction.getId());
	    departuser.setEmailId(departmentaction.getEmailId());
	    departuser.setEmployeeId(departmentaction.getEmployeeId());
	    departuser.setFirstName(departmentaction.getFirstName());
	    departuser.setLastName(departmentaction.getLastName());
	    departuser.setMiddleName(departmentaction.getMiddleName());	
	    departuser.setPhoneNumber(departmentaction.getPhoneNumber());
	    departuser.setUsername(departmentaction.getUsername());
	    departuser.setUsernameMiddlename(departmentaction.getUsername()+"-"+ departmentaction.getMiddleName());
	    uselist.add(departuser);
		});
		
		
		
		
		
		
//		
//		else {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}	
//		
		}
		catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Current Filed Engineeer doesnot have ShopCode");
		}
		return Library.getSuccessfulResponse(uselist,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	
public GenericResponse softwarefileddUserDetails() {
		
		List<UserRequestConDTO> uselist=new ArrayList<UserRequestConDTO>();
		try {
		
			List<UserEntity> userListfieldteam = new ArrayList<>();
		    RoleMaster rolesoftware=rolemasetrepo.findByRoleCode("FIELD_TEAM");
		    userListfieldteam = userRepository.getUserByRoleIdCCTV(rolesoftware.getId());
		    userListfieldteam.stream().forEach(filedaction ->{
		    UserRequestConDTO fileduser=new UserRequestConDTO();
		    fileduser.setId(filedaction.getId());
		    fileduser.setEmailId(filedaction.getEmailId());
		    fileduser.setEmployeeId(filedaction.getEmployeeId());
		    fileduser.setFirstName(filedaction.getFirstName());
		    fileduser.setLastName(filedaction.getLastName());
		    fileduser.setMiddleName(filedaction.getMiddleName());	
		    fileduser.setPhoneNumber(filedaction.getPhoneNumber());
		    fileduser.setUsername(filedaction.getUsername());
		    fileduser.setUsernameMiddlename(filedaction.getUsername()+"-"+ filedaction.getMiddleName());
		    uselist.add(fileduser);
			});
		
		
		List<UserEntity> userListswteam = new ArrayList<>();
	    RoleMaster rolefiled=rolemasetrepo.findByRoleCode("SOFTWARE_TEAM");
	    userListswteam = userRepository.getUserByRoleIdCCTV(rolefiled.getId());
	    userListswteam.stream().forEach(swaction ->{
	    UserRequestConDTO swuser=new UserRequestConDTO();
	    swuser.setId(swaction.getId());
	    swuser.setEmailId(swaction.getEmailId());
	    swuser.setEmployeeId(swaction.getEmployeeId());
	    swuser.setFirstName(swaction.getFirstName());
	    swuser.setLastName(swaction.getLastName());
	    swuser.setMiddleName(swaction.getMiddleName());	
	    swuser.setPhoneNumber(swaction.getPhoneNumber());
	    swuser.setUsername(swaction.getUsername());
	    swuser.setUsernameMiddlename(swaction.getUsername()+"-"+ swaction.getMiddleName());
	    uselist.add(swuser);
		});
	    
	    List<UserEntity> userListswmoduelteam = new ArrayList<>();
	    RoleMaster roleswmoduel=rolemasetrepo.findByRoleCode("SOFTWARE_MODULE");
	    userListswmoduelteam = userRepository.getUserByRoleIdCCTV(roleswmoduel.getId());
	    userListswmoduelteam.stream().forEach(swmoduelaction ->{
	    UserRequestConDTO swmodueluser=new UserRequestConDTO();
	    swmodueluser.setId(swmoduelaction.getId());
	    swmodueluser.setEmailId(swmoduelaction.getEmailId());
	    swmodueluser.setEmployeeId(swmoduelaction.getEmployeeId());
	    swmodueluser.setFirstName(swmoduelaction.getFirstName());
	    swmodueluser.setLastName(swmoduelaction.getLastName());
	    swmodueluser.setMiddleName(swmoduelaction.getMiddleName());	
	    swmodueluser.setPhoneNumber(swmoduelaction.getPhoneNumber());
	    swmodueluser.setUsername(swmoduelaction.getUsername());
	    swmodueluser.setUsernameMiddlename(swmoduelaction.getUsername()+"-"+ swmoduelaction.getMiddleName());
	    uselist.add(swmodueluser);
		});
	 
//	    List<UserEntity> userListswfieldteam = new ArrayList<>();
//	    RoleMaster rolesofttfiled=rolemasetrepo.findByRoleCode("SWTEAM_FIELDTEAM");
//	    userListswfieldteam = userRepository.getUserByRoleIdCCTV(rolesofttfiled.getId());
//	    userListswfieldteam.stream().forEach(fswaction ->{
//	    UserRequestConDTO fswuser=new UserRequestConDTO();
//	    fswuser.setId(fswaction.getId());
//	    fswuser.setEmailId(fswaction.getEmailId());
//	    fswuser.setEmployeeId(fswaction.getEmployeeId());
//	    fswuser.setFirstName(fswaction.getFirstName());
//	    fswuser.setLastName(fswaction.getLastName());
//	    fswuser.setMiddleName(fswaction.getMiddleName());	
//	    fswuser.setPhoneNumber(fswaction.getPhoneNumber());
//	    fswuser.setUsername(fswaction.getUsername());
//	    fswuser.setUsernameMiddlename(fswaction.getUsername()+"-"+ fswaction.getMiddleName());
//	    uselist.add(fswuser);
//		});
	    
	    List<UserEntity> userListDepart = new ArrayList<>();
	    RoleMaster departmentrole =rolemasetrepo.findByRoleCode("HELPDESK_DEPARTMENT");
	    userListDepart = userRepository.getUserByRoleIdCCTV(departmentrole.getId());
	    userListDepart.stream().forEach(departmentaction ->{
	    UserRequestConDTO departuser=new UserRequestConDTO();
	    departuser.setId(departmentaction.getId());
	    departuser.setEmailId(departmentaction.getEmailId());
	    departuser.setEmployeeId(departmentaction.getEmployeeId());
	    departuser.setFirstName(departmentaction.getFirstName());
	    departuser.setLastName(departmentaction.getLastName());
	    departuser.setMiddleName(departmentaction.getMiddleName());	
	    departuser.setPhoneNumber(departmentaction.getPhoneNumber());
	    departuser.setUsername(departmentaction.getUsername());
	    departuser.setUsernameMiddlename(departmentaction.getUsername()+"-"+ departmentaction.getMiddleName());
	    uselist.add(departuser);
		});
	    
		}
		catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(), "Current Filed Engineeer doesnot have ShopCode");
		}
		return Library.getSuccessfulResponse(uselist,ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	public GenericResponse softwaremoduleUserDetails() {

		List<UserRequestConDTO> uselist = new ArrayList<UserRequestConDTO>();
		try {

			List<UserEntity> userListswmoduelteam = new ArrayList<>();
			RoleMaster roleswmoduel = rolemasetrepo.findByRoleCode("SOFTWARE_MODULE");
			userListswmoduelteam = userRepository.getUserByRoleIdCCTV(roleswmoduel.getId());
			userListswmoduelteam.stream().forEach(swmoduelaction -> {
				UserRequestConDTO swmodueluser = new UserRequestConDTO();
				swmodueluser.setId(swmoduelaction.getId());
				swmodueluser.setEmailId(swmoduelaction.getEmailId());
				swmodueluser.setEmployeeId(swmoduelaction.getEmployeeId());
				swmodueluser.setFirstName(swmoduelaction.getFirstName());
				swmodueluser.setLastName(swmoduelaction.getLastName());
				swmodueluser.setMiddleName(swmoduelaction.getMiddleName());
				swmodueluser.setPhoneNumber(swmoduelaction.getPhoneNumber());
				swmodueluser.setUsername(swmoduelaction.getUsername());
				swmodueluser.setUsernameMiddlename(swmoduelaction.getUsername() + "-" + swmoduelaction.getMiddleName());
				uselist.add(swmodueluser);
			});
		} catch (Exception e) {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					"Current Filed Engineeer doesnot have ShopCode");
		}
		if (uselist.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		} else {
			return Library.getSuccessfulResponse(uselist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		}

	}
	
}
