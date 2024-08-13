package com.oasys.helpdesk.service;
import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
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
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.mapper.AssetTypeMapper;
import com.oasys.helpdesk.mapper.DepartmentMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DepartmentRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class DepartmentServiceImpl implements DepartmentService{
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	@Autowired
	private DepartmentMapper deptmapper;
	
	public static final String DEPT_CODE = "Department Code";
	
	
	@Override
	public GenericResponse adddepartment(DepartmentRequestDTO requestDTO)	{

		Optional<DepartmentEntity> departmentOptional=departmentRepository.findByDepartmentIgnoreCase(requestDTO.getDepartment().toUpperCase());
		if (departmentOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEPARTMENT_NAME }));
		}
		 departmentOptional = departmentRepository.findByDepartmentCodeIgnoreCase(requestDTO.getDepartmentCode());
		if (departmentOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		DepartmentEntity departmentEntity = commonUtil.modalMap(requestDTO, DepartmentEntity.class);
		departmentRepository.save(departmentEntity);
		return Library.getSuccessfulResponse(departmentEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}


	@Override
	public GenericResponse getAll() {
		List<DepartmentEntity> DepList = departmentRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DepartmentResponseDTO> depResponseList = DepList.stream()
				.map(deptmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<DepartmentEntity> depTypeEntity = departmentRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(deptmapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse updatedepartment(DepartmentRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<DepartmentEntity> DeptOptional = departmentRepository
				.findByTypeIgnoreCaseNotInId(requestDTO.getDepartment(), requestDTO.getId());
		if (DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEPARTMENT_NAME }));
		}
		DeptOptional = departmentRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		DepartmentEntity deptEntity = DeptOptional.get();
		deptEntity.setDepartment(requestDTO.getDepartment());
		deptEntity.setStatus(requestDTO.getStatus());
		departmentRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	
	
	
	@Override
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<DepartmentEntity> list = null;
		Long id = null;
		String status = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(ID))
					&& !paginationDto.getFilters().get(ID).toString().trim().isEmpty()) {
				try {
					id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					}catch(Exception e) {
						log.error("error occurred while parsing id :: {}", e);
						return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
								ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
					}
			}

			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				try {
					status = (paginationDto.getFilters().get(STATUS).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.nonNull(id) && Objects.nonNull(status)) {
			list = departmentRepository.getByIdAndStatus(id, status, pageable);
		} else if (Objects.nonNull(id) && Objects.isNull(status)) {
			list = departmentRepository.getById(id, pageable);
		} else if (Objects.isNull(id) && Objects.nonNull(status)) {
			list = departmentRepository.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = departmentRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<DepartmentResponseDTO> finalResponse = list.map(deptmapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(DEPT_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<DepartmentEntity> DeptEntity = departmentRepository.findByDepartmentCodeIgnoreCase(code);
			if (DeptEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<DepartmentEntity> List = departmentRepository.findAllByStatusOrderByModifiedDateDesc(String.valueOf(true));
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
