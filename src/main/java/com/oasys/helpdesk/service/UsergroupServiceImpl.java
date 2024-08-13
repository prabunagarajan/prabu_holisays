package com.oasys.helpdesk.service;



import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.TICKETSTATUS_NAME;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TYPE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.dto.DepartmentRequestDTO;
import com.oasys.helpdesk.dto.DepartmentResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.dto.UsergroupRequestDTO;
import com.oasys.helpdesk.dto.UsergroupResponseDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.entity.UserGroupEntity;
import com.oasys.helpdesk.mapper.AssetTypeMapper;
import com.oasys.helpdesk.mapper.DepartmentMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.mapper.UsergroupMapper;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DepartmentRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.repository.UsergroupRepository;
import com.oasys.helpdesk.response.TicketResponseDto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class UsergroupServiceImpl implements UsergroupService {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private UsergroupRepository usergroupRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String USERGP_CODE = "UG Code";
	
	@Autowired
	private UsergroupMapper usergroupmapper;
	
	
	@Override
	public GenericResponse adddusergroup(UsergroupRequestDTO requestDTO)	{

		Optional<UserGroupEntity> ticketOptional=usergroupRepository.findByUsergroupNameIgnoreCase(requestDTO.getUsergroup_name().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TICKETSTATUS_NAME }));
		}
		ticketOptional = usergroupRepository.findByUsergroupCodeIgnoreCase(requestDTO.getUsergroup_code());
		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		UserGroupEntity tcEntity = commonUtil.modalMap(requestDTO, UserGroupEntity.class);
		tcEntity.setUsergroupName(requestDTO.getUsergroup_name());
		tcEntity.setUsergroupCode(requestDTO.getUsergroup_code());
		usergroupRepository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	
	@Override
	public GenericResponse getAllActive() {
		List<UserGroupEntity> assetTypeList = usergroupRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<UsergroupResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(usergroupmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<UserGroupEntity> depTypeEntity = usergroupRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(usergroupmapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	

	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<UserGroupEntity> list = null;
		String groupname = null;
	    String role=null;
		Boolean status = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {

		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("usergroup_name"))
					&& !paginationDto.getFilters().get("usergroup_name").toString().trim().isEmpty()) {
				groupname = (paginationDto.getFilters().get("usergroup_name").toString());
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get("role"))
					&& !paginationDto.getFilters().get("role").toString().trim().isEmpty()) {
				role = (paginationDto.getFilters().get("role").toString());
			}
			
			
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status =Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(groupname,role,status, pageable);
		if (Objects.isNull(list)) {
			list = usergroupRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<UsergroupResponseDTO> finalResponse = list.map(usergroupmapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	private Page<UserGroupEntity> getByFilter(String groupname ,String role,Boolean status, Pageable pageable){
		Page<UserGroupEntity> list = null;
		if (Objects.nonNull(groupname) && Objects.nonNull(role) && Objects.nonNull(status)) {
			list = usergroupRepository.getByUsergroupNameRoleAndStatus(groupname,role, status, pageable);
		} 
		
		return list;
	}
	
	
	@Override
	public GenericResponse getAll() {
		List<UserGroupEntity> DepList = usergroupRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<UsergroupResponseDTO> depResponseList = DepList.stream()
				.map(usergroupmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	

	@Override
	public GenericResponse updateusergroup(UsergroupRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		
		
		Optional<UserGroupEntity> DeptOptional = usergroupRepository
				.findByUsergroupNameIgnoreCaseNotInId(requestDTO.getUsergroup_name(), requestDTO.getId());
		
		if (DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEPARTMENT_NAME }));
		}
		DeptOptional = usergroupRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		UserGroupEntity deptEntity = DeptOptional.get();
		deptEntity.setUsergroupName(requestDTO.getUsergroup_name());
		deptEntity.setStatus(requestDTO.isStatus());;
		usergroupRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	
	
	
	
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(USERGP_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<UserGroupEntity> TsEntity = usergroupRepository.findByUsergroupCodeIgnoreCase(code);
			if (TsEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	

}
