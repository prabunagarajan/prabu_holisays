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
import com.oasys.helpdesk.dto.DistrictRequestDTO;
import com.oasys.helpdesk.dto.DistrictResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.entity.DistrictEntity;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.AssetTypeMapper;
import com.oasys.helpdesk.mapper.DepartmentMapper;
import com.oasys.helpdesk.mapper.Districtmapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.DepartmentRepository;
import com.oasys.helpdesk.repository.DistrictRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.response.IsssueDetresdto;
import com.oasys.helpdesk.response.TicketResponseDto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class DistrictserviceImpl  implements DistrictService{
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String D_CODE = "District Code";
	
	
	@Autowired
	private Districtmapper distmapper;
	
	
	
	@Override
	public GenericResponse adddistrict(DistrictRequestDTO requestDTO)	{

		Optional<DistrictEntity> ticketOptional=districtRepository.findByCountrynameIgnoreCase(requestDTO.getCountryname().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TICKETSTATUS_NAME }));
		}
		ticketOptional = districtRepository.findByCodeIgnoreCase(requestDTO.getCode());
		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		DistrictEntity tcEntity = commonUtil.modalMap(requestDTO, DistrictEntity.class);
		districtRepository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	
	@Override
	public GenericResponse updatedistrict(DistrictRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		Optional<DistrictEntity> DeptOptional = districtRepository
				.findByCountrynameIgnoreCaseNotInId(requestDTO.getCountryname(), requestDTO.getId());
		
		if (DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { DEPARTMENT_NAME }));
		}
		DeptOptional = districtRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		DistrictEntity deptEntity = DeptOptional.get();
		deptEntity.setCountryname(requestDTO.getCountryname());
		deptEntity.setStatus(requestDTO.getStatus());
		districtRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	
	@Override
	public GenericResponse getAll() {
		List<DistrictEntity> DepList = districtRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DistrictResponseDTO> depResponseList = DepList.stream()
				.map(distmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<DistrictEntity> depTypeEntity = districtRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(distmapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<DistrictEntity> assetTypeList = districtRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<DistrictResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(distmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	

public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
	Pageable pageable = null;
	Page<DistrictEntity> list = null;
	Long categoryId = null;
	String countryname = null;
	String state = null;
	String districtname=null;
	String districtcode=null;
	String zone =null;
	String districtshortname=null;
	
	
	Long subCategoryId = null;
	String problem=null;
	String status = null;
//	if(StringUtils.isNotBlank(paginationDto.getSortField())) {
//		if(ISSUEDETCAT_ID.equals(paginationDto.getSortField())) {
//			paginationDto.setSortField(CATEGORY);
//		}
//		if(ISSUEDETSUBCAT_ID.equals(paginationDto.getSortField())) {
//			paginationDto.setSortField(SUBCATEGORY);
//		}
//	}
	if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
		pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
				Sort.by(Direction.ASC, paginationDto.getSortField()));
	} else {
		pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
				Sort.by(Direction.DESC, paginationDto.getSortField()));
	}
	if (Objects.nonNull(paginationDto.getFilters())) {
		if (Objects.nonNull(paginationDto.getFilters().get("countryname"))
				&& !paginationDto.getFilters().get("countryname").toString().trim().isEmpty()) {
			countryname = (paginationDto.getFilters().get("countryname").toString());
		}
		if (Objects.nonNull(paginationDto.getFilters().get("state"))
				&& !paginationDto.getFilters().get("state").toString().trim().isEmpty()) {
			state =(paginationDto.getFilters().get("state").toString());
		}
		if (Objects.nonNull(paginationDto.getFilters().get("districtname"))
				&& !paginationDto.getFilters().get("districtname").toString().trim().isEmpty()) {
			districtname = String.valueOf(paginationDto.getFilters().get("districtname").toString());
		}
		
		if (Objects.nonNull(paginationDto.getFilters().get("districtcode"))
				&& !paginationDto.getFilters().get("districtcode").toString().trim().isEmpty()) {
			districtcode = String.valueOf(paginationDto.getFilters().get("districtcode").toString());
		}
		
		if (Objects.nonNull(paginationDto.getFilters().get("zone"))
				&& !paginationDto.getFilters().get("zone").toString().trim().isEmpty()) {
			zone = String.valueOf(paginationDto.getFilters().get("zone").toString());
		}
		
		if (Objects.nonNull(paginationDto.getFilters().get("districtshortname"))
				&& !paginationDto.getFilters().get("districtshortname").toString().trim().isEmpty()) {
			districtshortname = String.valueOf(paginationDto.getFilters().get("districtshortname").toString());
		}
		
		
		if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
				&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
			status = (paginationDto.getFilters().get(STATUS).toString());
		}
	}
	
	
	list = getByFilter(countryname, state, status, pageable);
	if (Objects.isNull(list)) {
		list = districtRepository.getAll(pageable);
	}
	
	
	if (Objects.isNull(list) || list.isEmpty()) {
		return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
				ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
	}
	Page<DistrictResponseDTO> finalResponse = list.map(distmapper::convertEntityToResponseDTO);
	
	return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_FOUND);
	
}



private Page<DistrictEntity> getByFilter(String countryname, String state, String status, Pageable pageable){
	Page<DistrictEntity> list = null;
	if (Objects.nonNull(countryname) && Objects.nonNull(state)  && Objects.nonNull(status)) {
		list = districtRepository.getByCountrynameAnstateAndStatus(countryname, state, status, pageable);
	} 
	
	return list;
}

	
	
	
	
	
	
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(D_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<DistrictEntity> TsEntity = districtRepository.findByCodeIgnoreCase(code);
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
