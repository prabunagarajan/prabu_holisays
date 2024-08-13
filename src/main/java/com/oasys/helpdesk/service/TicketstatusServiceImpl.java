package com.oasys.helpdesk.service;


import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TICKETSTATUS_NAME;

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
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class TicketstatusServiceImpl implements TicketStatusService{
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private TicketStatusrepository ticketstatusRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	public static final String TS_CODE = "TS Code";
	
	@Autowired
	private TicketstatusMapper ticketstatusmapper;
	
	
	@Override
	public GenericResponse adddticket(TicketstausRequestDTO requestDTO)	{

		Optional<TicketStatusEntity> ticketOptional=ticketstatusRepository.findByTicketstatusnameIgnoreCase(requestDTO.getTicketstatusname().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TICKETSTATUS_NAME }));
		}
		ticketOptional = ticketstatusRepository.findByTicketstatusCodeIgnoreCase(requestDTO.getTicketstatusCode());
		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		requestDTO.setId(null);
		TicketStatusEntity tcEntity = commonUtil.modalMap(requestDTO, TicketStatusEntity.class);
		ticketstatusRepository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	@Override
	public GenericResponse getAll() {
		List<TicketStatusEntity> DepList = ticketstatusRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<TicketstausResponseDTO> depResponseList = DepList.stream()
				.map(ticketstatusmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<TicketStatusEntity> depTypeEntity = ticketstatusRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(ticketstatusmapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	

	@Override
	public GenericResponse updateticket(TicketstausRequestDTO requestDTO)	{
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		
		
		
		Optional<TicketStatusEntity> DeptOptional = ticketstatusRepository
				.findByTicketstatusnameIgnoreCaseNotInId(requestDTO.getTicketstatusname().toUpperCase(), requestDTO.getId());
		
		if (DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { TICKETSTATUS_NAME }));
		}
		DeptOptional = ticketstatusRepository.findById(requestDTO.getId());
		if (!DeptOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		TicketStatusEntity deptEntity = DeptOptional.get();
		deptEntity.setTicketstatusname(requestDTO.getTicketstatusname());
		deptEntity.setStatus(requestDTO.getStatus());
		ticketstatusRepository.save(deptEntity);
		return Library.getSuccessfulResponse(deptEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	}
	
	
	
	public GenericResponse searchByFilter(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<TicketStatusEntity> list = null;
		Long  ticketStatusId = null;
	
		String status = null;
		if(StringUtils.isNotBlank(paginationDto.getSortField())) {
//			if(ticketstatusname.equals(paginationDto.getSortField())) {
//				paginationDto.setSortField(ticketstatusname);
//			}
			
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("ticketStatusId"))
					&& !paginationDto.getFilters().get("ticketStatusId").toString().trim().isEmpty()) {
				ticketStatusId = Long.valueOf((paginationDto.getFilters().get("ticketStatusId").toString()));
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status =(paginationDto.getFilters().get(STATUS).toString());
			}
		}
		list = getByFilter(ticketStatusId,status, pageable);
		if (Objects.isNull(list)) {
			list = ticketstatusRepository.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<TicketstausResponseDTO> finalResponse = list.map(ticketstatusmapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	private Page<TicketStatusEntity> getByFilter(Long ticketStatusId ,String status, Pageable pageable){
		Page<TicketStatusEntity> list = null;
		if (Objects.nonNull(ticketStatusId) && StringUtils.isNotBlank(status)) {
			list = ticketstatusRepository.getByIdAndStatus(ticketStatusId, status, pageable);
		} else if (Objects.nonNull(ticketStatusId) && StringUtils.isBlank(status)) {
			list = ticketstatusRepository.getById(ticketStatusId, pageable);
		} else if (Objects.isNull(ticketStatusId) && StringUtils.isNotBlank(status)) {
			list = ticketstatusRepository.getByStatus(status, pageable);
		}
		return list;
	}
	
	@Override
	public GenericResponse getAllActive() {
		List<TicketStatusEntity> assetTypeList = ticketstatusRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<TicketstausResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(ticketstatusmapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(TS_CODE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<TicketStatusEntity> TsEntity = ticketstatusRepository.findByTicketstatusCodeIgnoreCase(code);
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
